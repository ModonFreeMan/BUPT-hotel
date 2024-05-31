package com.backend.service;

import com.backend.pojo.AirConditionerRequest;
import com.backend.pojo.AirConditionerStatus;
/**
 * 程序标题：RoomsServiceImpl
 * 模块描述：这个模块提供了房间服务的实现，包括获取空调状态、处理空调请求等功能。
 * 接口说明：这个模块实现了RoomsService接口，可以被RoomsControl调用。
 * 负责人：陈俞涵 创建时间：2024/5/8
 */
public interface RoomsService {

    /**
     * 程序标题：getAirConditionerStatus
     * 模块描述：这个方法返回给定房间的空调状态。
     * 接口说明：这个方法可以被其他模块调用，以获取房间的空调状态。
     * 负责人：陈俞涵
     */
    AirConditionerStatus getAirConditionerStatus(String roomId);

    /**
     * 程序标题：isTemperatureValid
     * 模块描述：这个方法检查给定的空调请求的目标温度是否有效。
     * 接口说明：这个方法可以被其他模块调用，以验证空调请求的目标温度。
     * 负责人：陈俞涵
     */
    boolean isTemperatureValid (AirConditionerRequest request);

    /**
     * 程序标题：isNeedProcess
     * 模块描述：这个方法检查给定的空调请求是否需要处理。
     * 接口说明：这个方法可以被其他模块调用，以确定是否需要处理空调请求。
     * 负责人：陈俞涵
     */
    boolean isNeedProcess(AirConditionerRequest request);

    /**
     * 程序标题：processRequest
     * 模块描述：这个方法处理给定的空调请求。
     * 接口说明：这个方法可以被其他模块调用，以处理空调请求。
     * 负责人：陈俞涵
     */
    void processRequest(AirConditionerRequest request);

    /**
     * 程序标题：enterServiceQueue
     * 模块描述：这个方法将房间添加到服务队列。
     * 接口说明：这个方法可以被其他模块调用，以将房间添加到服务队列。
     * 负责人：陈俞涵
     */
    boolean enterServiceQueue();

    /**
     * 程序标题：leaveServiceQueue
     * 模块描述：这个方法将房间从服务队列中移除。
     * 接口说明：这个方法可以被其他模块调用，以将房间从服务队列中移除。
     * 负责人：陈俞涵
     */
    void leaveServiceQueue(String roomId, int leaveStatus);

    /**
     * 程序标题：enterWaitQueue
     * 模块描述：这个方法将房间添加到等待队列。
     * 接口说明：这个方法可以被其他模块调用，以将房间添加到等待队列。
     * 负责人：陈俞涵
     */
    void enterWaitQueue(String roomId);

    /**
     * 程序标题：timeTrans
     * 模块描述：这个方法将给定的时间转换为另一种格式。
     * 接口说明：这个方法可以被其他模块调用，以转换时间格式。
     * 负责人：陈俞涵
     */
    String timeTrans(String oldTime, int offset);

    /**
     * 程序标题：isRunning
     * 模块描述：这个方法检查给定的房间是否正在运行。
     * 接口说明：这个方法可以被其他模块调用，以检查房间是否正在运行。
     * 负责人：陈俞涵
     */
    boolean isRunning(String roomId);
}
