package com.cai.stock.config;

import com.cai.stock.pojo.vo.StockInfoConfig;
import com.cai.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 定义公共配置bean
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class})//开启对相关配置对象的加载
public class CommonConfig {
    /**
 * 定义密码加密，匹配器bean
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
    }

    /**
     *基于雪花算法保证生成的id唯一
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        /*
        参数一：机器id、
        参数二：机房id
        机器id和机房id一般又运维人员进行唯一性规划
         */
        return new IdWorker(1L,2L);
    }
}
