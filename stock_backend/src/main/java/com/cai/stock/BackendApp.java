package com.cai.stock;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@MapperScan("com.cai.stock.mapper") //扫描持久层mapper接口，生成代理对象，并且维护到Spring IOC容器中
@SpringBootApplication
@EnableCaching
public class BackendApp {
    public static void main(String[] args) {
        SpringApplication.run(BackendApp.class, args);
    }
}
