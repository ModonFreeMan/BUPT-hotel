package com.backend.utils;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * hutool的雪花算法生成
 */
public class SnowFlakeUtil {
    // 私有静态成员变量，用于保存SnowFlake单例对象
    //参数1为终端ID
    //参数2为数据中心ID
    private final static Snowflake snowflake = IdUtil.getSnowflake(1, 1);
    public static String getSnowStr(){
        return snowflake.nextIdStr();
    }

}
