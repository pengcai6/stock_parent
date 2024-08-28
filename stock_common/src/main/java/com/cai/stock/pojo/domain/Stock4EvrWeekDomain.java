package com.cai.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2022/2/28
 * @Description 个股周K数据封装
 */
@ApiModel(description = "个股周K数据封装")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4EvrWeekDomain {
    /**
     * 一周内最大时间
     */
   @ApiModelProperty("一周内最大时间")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
   private Date mxTime;

    /**
     * 股票编码
     */
   @ApiModelProperty("股票编码")
   private String stockCode;

    /**
     * 一周内平均价
     */
    @ApiModelProperty("一周内平均价")
    private BigDecimal avgPrice;
    /**
     * 一周内最低价
     */
    @ApiModelProperty("一周内最低价")
    private BigDecimal minPrice;
    /**
     * 一周内最高价
     */
    @ApiModelProperty("一周内最高价")
    private BigDecimal maxPrice;
    /**
     * 周一开盘价
     */
    @ApiModelProperty("周一开盘价")
    private BigDecimal openPrice;
    /**
     * 周五收盘价（如果当前日期不到周五，则显示最新价格）
     */
    @ApiModelProperty("周五收盘价（如果当前日期不到周五，则显示最新价格）")
    private BigDecimal closePrice;


}