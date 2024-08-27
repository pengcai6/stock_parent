package com.cai.stock.config;

import com.cai.stock.pojo.vo.StockInfoConfig;
import com.cai.stock.utils.IdWorker;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    /**
     * 统一定义long序列化转String设置（所有的long序列化为String）
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        //构建勸ttp信息转换对象
        MappingJackson2HttpMessageConverter converter= new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper= new ObjectMapper();
//反序列化忽略位置属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        SimpleModule simpleModule= new SimpleModule();
//Long1Long类型序列化Str1ng
        simpleModule.addSerializer(Long.class,ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
//注册转化器
        objectMapper.registerModule(simpleModule);
//设置序列化实现
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
