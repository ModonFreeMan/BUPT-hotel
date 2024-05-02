package com.backend.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author seaside
 * 2024-05-02 18:17
 */

@Component
public class MybatisUtil {
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public SqlSessionFactory sqlSessionFactoryInit(){
        try {
            //加载配置文件
            InputStream is = Resources.getResourceAsStream("mybatis-config.xml");

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sqlSessionFactory;
    }

    public SqlSession getSession() {
        return sqlSessionFactory.openSession(true);
    }
}
