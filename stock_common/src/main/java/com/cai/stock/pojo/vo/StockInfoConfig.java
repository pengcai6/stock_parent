package com.cai.stock.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定义股票相关值进行封装
 */

@ApiModel(description = "定义股票相关值进行封装")
@Data
@Component
@ConfigurationProperties(prefix = "stock")
public class StockInfoConfig {
    /**
     * 封装国内A股大盘编码集合
     */
   @ApiModelProperty("封装国内A股大盘编码集合")
   private   List<String> inner;
    /**
     * 外盘编码集合
     */
    @ApiModelProperty("外盘编码集合")
    private  List<String> outer;
    /**
     * 股票涨幅区间标题
     */
    @ApiModelProperty("股票涨幅区间标题")
    private List<String> upDownRange;
 }
