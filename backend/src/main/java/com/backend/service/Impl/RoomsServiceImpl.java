package com.backend.service.Impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.backend.mapper.DetailedBillMapper;
import com.backend.mapper.StatisticsMapper;
import com.backend.pojo.*;
import com.backend.service.ReceptionService;
import com.backend.service.RoomsService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RoomsServiceImpl implements RoomsService {

    @Autowired
    @Qualifier("ACServiceMap")
    private ConcurrentHashMap<String, ACServiceObject> ACServiceMap;

    @Autowired
    @Qualifier("CentralACStatus")
    private CentralACStatus centralACStatus;

    @Autowired
    @Qualifier("StatisticsMap")
    private ConcurrentHashMap<String, Statistics> statisticsMap;

    @Resource
    DetailedBillMapper detailedBillMapper;

    @Resource
    StatisticsMapper statisticsMapper;


    private static final double SPEEDUPRATE = 6;

    @Autowired
    @Qualifier("RecoveryQueue")
    List<String> recoveryQueue;

    @Autowired
    @Qualifier("FiveRoomDetailsMap")
    HashMap<String, FiveRoomDetail> fiveRoomDetailsMap;

    @Resource
    ReceptionService receptionService;


    // 无界等待队列，满足先进先出，需要自行判断优先级再取出
    // 之所以采用数组，不采用set是因为丢失了进入顺序，不采用queue是因为无法随机存取
    // 按照优先级分为三个等待队列,1优先级最高
    private final CopyOnWriteArrayList<String> waiting_queue1 = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<String> waiting_queue2 = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<String> waiting_queue3 = new CopyOnWriteArrayList<>();

    // 服务队列的大小，常量
    private final static int SERVICE_QUEUE_SIZE = 3;

    // 空调调温速度，与风速有关，自定义,中速情况下每分钟0.5度
    private final static double[] attemperation = {1.0 / 3.0, 0.5, 1};

    // 服务队列
    private final CopyOnWriteArrayList<String> service_queue = new CopyOnWriteArrayList<>();


    @Override
    public AirConditionerStatus getAirConditionerStatus(String roomId) {
        ACServiceObject acServiceObject = ACServiceMap.get(roomId);
        // 返回空调状态对象
        return new AirConditionerStatus(
                acServiceObject.getCurrentFee(), acServiceObject.getCurTem(),
                roomId, service_queue.contains(roomId),
                acServiceObject.getSpeedLevel(), acServiceObject.isSwitchStatus(),
                acServiceObject.getTargetTem(), acServiceObject.getTotalFee(),
                acServiceObject.isWorkMode());
    }


    @Override
    public boolean isTemperatureValid(AirConditionerRequest request) {
        double targetTem = request.getTargetTem();
        // 温度不合理两种情况，超出中央空调范围，或者不符合中央空调运行模式
        if (targetTem <= centralACStatus.getUpperBound() && targetTem >= centralACStatus.getLowerBound()) {
            // 温度模式的判断不能影响开关机的变化
            double temper_differ = targetTem - ACServiceMap.get(request.getRoomId()).getCurTem();
            if (!((temper_differ >= 0 && centralACStatus.isWorkMode())// 如果模式不对，但是开关变化，允许进入处理
                    || (temper_differ <= 0 && !centralACStatus.isWorkMode()))) {
                return request.isSwitchStatus() != ACServiceMap.get(request.getRoomId()).isSwitchStatus();
            }
                return true;
        }
        return false;
    }

    @Override
    public boolean isNeedProcess(AirConditionerRequest request) {
        // 从关机到关机状态的请求不执行，因为已经关机所以风速、目标温度之类的request信息都是无意义，不用改变什么
        return request.isSwitchStatus() || ACServiceMap.get(request.getRoomId()).isSwitchStatus();
    }

    @Override
    public void processRequest(AirConditionerRequest request) {
        //检查switchStatus
        ACServiceObject room_message = ACServiceMap.get(request.getRoomId());
        if (request.isSwitchStatus() != room_message.isSwitchStatus()) {// 开变关或者关变开
            room_message.setSwitchStatus(request.isSwitchStatus());// 更新空调开关状态
            statisticsMap.get(request.getRoomId()).setSwitchSum(statisticsMap.get(request.getRoomId()).getSwitchSum() + 1);
            if (!room_message.isSwitchStatus()) {// 现在是false,即突然关机的情况，需要删除请求队列和服务队列中的对应请求
                if (!service_queue.contains(request.getRoomId())) {// 非服务队列的情况
                    if (waiting_queue1.contains(request.getRoomId()) || waiting_queue2.contains(request.getRoomId()) || waiting_queue3.contains(request.getRoomId())) {// 如果在等待队列需要加等待时间
                        // 增加阶段报表的 dispatchSum(等待时长) 当前时间-加入等待队列的时间，转换为秒数
                        int waiting_length = (int) Duration.between(
                                LocalDateTime.parse(
                                        room_message.getWaiting_queue_timestamp(),
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                ),
                                LocalDateTime.parse(
                                        timeTrans(
                                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                                ACServiceMap.get(
                                                        request.getRoomId()
                                                ).getDays()-1
                                        ), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        ).getSeconds();
                        statisticsMap.get(request.getRoomId()).setRequestLength(statisticsMap.get(request.getRoomId()).getRequestLength() + waiting_length);
                    }
                    room_message.setCurrentFee(0d);
                }else{
                    leaveServiceQueue(request.getRoomId(), 0);
                }
                ACServiceMap.get(request.getRoomId()).setDays(ACServiceMap.get(request.getRoomId()).getDays() + 1);
                for (Statistics statistics : statisticsMap.values()) {
                    statisticsMapper.add(statistics);// 每个数据都写入数据库中
                    // 第二天数据的初始化
                    // 更新开始记录的日期，因为是一天的，所以不记录时分秒
                    statistics.setDate(DateUtil.formatDate(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_MONTH, ACServiceMap.get(request.getRoomId()).getDays()-1)));
                    statistics.setDetailedBillSum(0);
                    statistics.setDispatchSum(0);
                    statistics.setRequestLength(0);
                    statistics.setSpeedChangeSum(0);
                    statistics.setSwitchSum(0);
                    statistics.setTemChangeSum(0);
                    statistics.setTotalFee(0);
                }
                return;// 从开机到关机，就处理到这点就够了
            }
            double temper_differ = request.getTargetTem() - ACServiceMap.get(request.getRoomId()).getCurTem();
            if(!((temper_differ >= 0 && centralACStatus.isWorkMode())
                    || (temper_differ <= 0 && !centralACStatus.isWorkMode())))
                return;// 从关机到开机，不符合温度模式需求，只要改完开机状态，不需要更多处理
        }
        // 调风次数增加
        if (request.getSpeedLevel() != room_message.getSpeedLevel()) {
            statisticsMap.get(request.getRoomId()).setSpeedChangeSum(statisticsMap.get(request.getRoomId()).getSpeedChangeSum() + 1);
            room_message.setSpeedLevel(request.getSpeedLevel());
        }
        // 调温次数增加(温度不变时也加？理由如上所述)
        statisticsMap.get(request.getRoomId()).setTemChangeSum(statisticsMap.get(request.getRoomId()).getTemChangeSum() + 1);

        room_message.setTargetTem(request.getTargetTem());// request中的信息只剩下目标温度没有添加了
        // 向等待队列中加入请求
        enterWaitQueue(request.getRoomId());
    }

    @Override
    public void enterWaitQueue(String roomId) {
        recoveryQueue.remove(roomId);
        // 更新加入等待队列的时间戳，是因为后面详单需要该信息
        ACServiceMap.get(roomId).setWaiting_queue_timestamp(timeTrans(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ACServiceMap.get(roomId).getDays()-1));
        // 先删除等待队列中旧有的请求，再根据优先级加入等待队列
        waiting_queue1.remove(roomId);
        waiting_queue2.remove(roomId);
        waiting_queue3.remove(roomId);
        // 假设风速对三个值分别对应1,2,3
        switch (ACServiceMap.get(roomId).getSpeedLevel()) {
            case 1:
                waiting_queue1.add(roomId);
                break;
            case 2:
                waiting_queue2.add(roomId);
                break;
            case 3:
                waiting_queue3.add(roomId);
                break;
        }
        // 判断该请求是否可以加入服务队列
        if(service_queue.size() == SERVICE_QUEUE_SIZE) {
            String lowerBound = getLowerBound();
            if (ACServiceMap.get(roomId).getSpeedLevel() > ACServiceMap.get(lowerBound).getSpeedLevel()) {
                    // 服务队列已满，需要调度
                    leaveServiceQueue(lowerBound, 4);
                }
        }
        // 有新加入等待队列的对象，看看服务队列是否需要补充
        enterServiceQueue();
    }


    @Override
    public boolean enterServiceQueue() {
        if (service_queue.size() >= SERVICE_QUEUE_SIZE)
            return false;
        // 取出优先级最高的请求，优先级相同先来先服务
        String roomId = "-1";
        if (!waiting_queue1.isEmpty()) {
            roomId = waiting_queue1.get(0);
            // 先移出等待队列，再加入服务队列
            waiting_queue1.remove(roomId);
            service_queue.add(roomId);
        } else if (!waiting_queue2.isEmpty()) {
            roomId = waiting_queue2.get(0);
            waiting_queue2.remove(roomId);
            service_queue.add(roomId);
        } else if (!waiting_queue3.isEmpty()) {
            roomId = waiting_queue3.get(0);
            waiting_queue3.remove(roomId);
            service_queue.add(roomId);
        }
        if (!roomId.equals("-1")) {
            // 首先判断是否出现由于回温程序导致目标温度，当前温度，的温度变化不再符合空调制冷模式,保留着，目前逻辑不会有这种情况，但是做好预案
            double temper_differ = ACServiceMap.get(roomId).getTargetTem() - ACServiceMap.get(roomId).getCurTem();
            if (!((centralACStatus.isWorkMode() && temper_differ >= 0) || (!centralACStatus.isWorkMode() && temper_differ <= 0))) {
                // 不计算不处理，直接结束
                service_queue.remove(roomId);
                return enterServiceQueue();
            }
            // 修改加入服务队列的时间戳，修改进入服务队列时的温度
            ACServiceObject room_message = ACServiceMap.get(roomId);
            room_message.setService_queue_timestamp(timeTrans(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ACServiceMap.get(roomId).getDays()-1
            ));
            room_message.setBeforeServiceTem(room_message.getCurTem());
            // 增加今天阶段报表的 dispatchSum(等待时长) 当前时间-加入等待队列的时间，转换为秒数
            int request_length = (int) Duration.between(
                    LocalDateTime.parse(
                            room_message.getWaiting_queue_timestamp(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    ),LocalDateTime.parse(
                            timeTrans(
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                    ACServiceMap.get(roomId).getDays()-1
                            ), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ).getSeconds();
            statisticsMap.get(roomId).setRequestLength(statisticsMap.get(roomId).getRequestLength() + request_length);
            // 增加调度次数
            statisticsMap.get(roomId).setDispatchSum(statisticsMap.get(roomId).getDispatchSum() + 1);
        } else
            return false;// 没有可加入的请求
        return true;
    }


    @Override
    public void leaveServiceQueue(String roomId, int leaveStatus) {
        // 首先字面意思，移出服务队列先
        service_queue.remove(roomId);
        // 计算费用：(当前时间-进入服务队列的时间)*费率数组[(int)风速]：在意外移出的情况下最多再运行10s，所以可以忽略不计这里的价格计算大概，不然可以再频繁一点，只要改变每次温度下降幅度即可
        double nowFee = Math.abs(ACServiceMap.get(roomId).getCurTem() - ACServiceMap.get(roomId).getBeforeServiceTem()) * centralACStatus.getRate();
        // 通知信息计算处理详单
        Duration duration = Duration.between(LocalDateTime.parse(ACServiceMap.get(roomId).getService_queue_timestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(
                        timeTrans(
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                ACServiceMap.get(roomId).getDays()-1
                        ), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        detailedBillMapper.insertBill(
                receptionService.getServiceId(roomId),
                ACServiceMap.get(roomId).getCurTem(),
                timeTrans(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ACServiceMap.get(roomId).getDays()-1),
                nowFee,
                centralACStatus.getRate(),
                roomId,
                ACServiceMap.get(roomId).getSpeedLevel(),
                ACServiceMap.get(roomId).getBeforeServiceTem(),
                ACServiceMap.get(roomId).getService_queue_timestamp(),
                ACServiceMap.get(roomId).getWaiting_queue_timestamp(),
                String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart())
        );
        // 增加currentFee，和TotalFee
        ACServiceMap.get(roomId).setCurrentFee(ACServiceMap.get(roomId).getCurrentFee() + nowFee);
        ACServiceMap.get(roomId).setTotalFee(ACServiceMap.get(roomId).getTotalFee() + nowFee);
        statisticsMap.get(roomId).setTotalFee(statisticsMap.get(roomId).getTotalFee() + nowFee);
        // 增加详单条数
        statisticsMap.get(roomId).setDetailedBillSum(statisticsMap.get(roomId).getDetailedBillSum() + 1);
        switch (leaveStatus) {
            case 0:// 关机移出
                // 本次开机的费用结算完毕
                ACServiceMap.get(roomId).setCurrentFee(0d);
                recoveryQueue.add(roomId);
                break;
            case 1:// 新的请求已抵达，前面的请求作废
                break;
            case 2:// 温度到达，无额外处理
                recoveryQueue.add(roomId);
                break;
            case 3:// 时间片到达
                // 只有这种情况还要重新加入等待队列
                enterWaitQueue(roomId);
                break;
            case 4:// 抢占式优先级调度,被高优先级强行移出服务队列
                recoveryQueue.add(roomId);
                enterWaitQueue(roomId);
                break;
        }
        // 服务队列移出的时候考虑再添加新的服务对象
        enterServiceQueue();
    }


    @Override
    public String timeTrans(String oldTime, int offset) {
        return DateUtil.format(DateUtil.offset(
                        DateUtil.parse(oldTime),
                        DateField.DAY_OF_MONTH, offset),
                "yyyy-MM-dd HH:mm:ss");
    }

    @Scheduled(fixedRate = 500) // 每0.5s检查一次
    public void scheduledTask() {
        // 温度更新
        for (String roomId : service_queue) {
            // 预定变化温度∆t，在服务队列中的由于不再运转回温程序，不会发生逆中央空调工作模式
            double Temperature_variation = attemperation[ACServiceMap.get(roomId).getSpeedLevel()-1] / 60*SPEEDUPRATE /2;
            if (waiting_queue1.contains(roomId) || waiting_queue2.contains(roomId) || waiting_queue3.contains(roomId)) {// 等待队列有新请求，被覆盖，立刻结束
                leaveServiceQueue(roomId, 1);
            } else if (Math.abs(ACServiceMap.get(roomId).getTargetTem() - ACServiceMap.get(roomId).getCurTem()) <= Temperature_variation) {
                // 温度到达,先更新温度再安然退场
                ACServiceMap.get(roomId).setCurTem(ACServiceMap.get(roomId).getTargetTem());
                leaveServiceQueue(roomId, 2);
            } else {// 先更新温度
                if (ACServiceMap.get(roomId).getTargetTem() > ACServiceMap.get(roomId).getCurTem())
                    ACServiceMap.get(roomId).setCurTem(ACServiceMap.get(roomId).getCurTem() + Temperature_variation);
                else
                    ACServiceMap.get(roomId).setCurTem(ACServiceMap.get(roomId).getCurTem() - Temperature_variation);
                if(getMaxWaitLevel() == ACServiceMap.get(roomId).getSpeedLevel()) {// 如果等待队列存在该优先级的等待,执行时间片轮转服务，时间片到达处理
                    if ((double) Duration.between(
                            LocalDateTime.parse(ACServiceMap.get(roomId).getService_queue_timestamp(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            LocalDateTime.parse(
                                    timeTrans(
                                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                            ACServiceMap.get(roomId).getDays() - 1
                                    ), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).getSeconds() / 60d * SPEEDUPRATE >= 2d) {// 时间片为两分钟
                        leaveServiceQueue(roomId, 3);
                    }
                }
            }
        }
    }

    @Scheduled(fixedRate = 600)
    void temperatureRecovery() {
        for (String roomId : recoveryQueue) {
            double curTem = ACServiceMap.get(roomId).getCurTem();
            double initialTem = fiveRoomDetailsMap.get(roomId).getInitialTem();
            //如果当前温度小于初始化温度
            if (curTem < initialTem) {
                if (curTem + 0.05 >= initialTem) {
                    curTem = initialTem;
                    ACServiceMap.get(roomId).setCurTem(curTem);
                    recoveryQueue.remove(roomId);
                } else {
                    curTem += 0.05;
                    ACServiceMap.get(roomId).setCurTem(curTem);
                }
            } else {
                if (curTem - 0.05 <= initialTem) {
                    curTem = initialTem;
                    ACServiceMap.get(roomId).setCurTem(curTem);
                    recoveryQueue.remove(roomId);
                } else {
                    curTem -= 0.05;
                    ACServiceMap.get(roomId).setCurTem(curTem);
                }
            }
        }
    }

    public boolean isRunning(String roomId){
        return this.service_queue.contains(roomId);
    }

    private String getLowerBound(){// 获取服务队列最早优先级最低的请求的房间号,只有服务队列已满时执行该判断
        String roomId = service_queue.get(0);
        for (int i = 1;i<SERVICE_QUEUE_SIZE;i++) {
            if (ACServiceMap.get(roomId).getSpeedLevel() > ACServiceMap.get(service_queue.get(i)).getSpeedLevel())
                roomId = service_queue.get(i);
        }
        return roomId;
    }

    private int getMaxWaitLevel(){// 要求服务队列满时，才会在定时器中调用它,返回当前要执行时间片轮转的优先级(风速)层级
        // 获取等待队列中最高的优先级，也即可能需要时间片调度的优先级
        if(waiting_queue3.isEmpty()){
            if(waiting_queue2.isEmpty()){
                if(waiting_queue1.isEmpty()){
                    return 0;
                }
                    return 1;
            }
                return 2;
        }
            return 3;
    }


}
