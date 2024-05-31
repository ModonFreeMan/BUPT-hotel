package com.backend.service;

import com.backend.pojo.AirConditionerStatus;
import com.backend.pojo.CentralACStatus;

import java.util.List;

/**
 * 程序标题：CentralACService
 * 模块描述：这个接口定义了中央空调服务的方法，包括获取所有空调信息、获取中央空调状态等功能。
 * 接口说明：这个接口的实现类可以被其他模块调用。
 * 负责人：王炜麒 创建日期：2021/4/30
 */
public interface CentralACService {

    /**
     * 程序标题：getAllACInfo
     * 模块描述：这个方法返回所有空调的信息。
     * 接口说明：这个方法可以被其他模块调用，以获取所有空调的信息。
     * 负责人：王炜麒
     */
    List<AirConditionerStatus> getAllACInfo();


    /**
     * 程序标题：getCentralACStatus
     * 模块描述：这个方法返回中央空调的状态。
     * 接口说明：这个方法可以被其他模块调用，以获取中央空调的状态。
     * 负责人：王炜麒
     */
    CentralACStatus getCentralACStatus();

    /**
     * 程序标题：isACAllOff
     * 模块描述：这个方法检查所有空调是否都已关闭。
     * 接口说明：这个方法可以被其他模块调用，以检查所有空调是否都已关闭。
     * 负责人：王炜麒
     */
    boolean isACAllOff();


    /**
     * 程序标题：changeCentralACStatus
     * 模块描述：这个方法改变中央空调的状态。
     * 接口说明：这个方法可以被其他模块调用，以改变中央空调的状态。
     * 负责人：王炜麒
     */
    void changeCentralACStatus(CentralACStatus centralACStatus);
}
