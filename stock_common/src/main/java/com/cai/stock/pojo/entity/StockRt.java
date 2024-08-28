package com.cai.stock.pojo.entity;

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
 * 开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;
 */
@ApiModel(description = "开盘价、前收盘价、最新价、最高价、最低价、成交金额和成交量、交易时间信息;")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRt {
    //最新交易量
    @ApiModelProperty("最新交易量")
    private Long tradeAmt;
    //前收盘价格
    @ApiModelProperty("前收盘价格")
    private BigDecimal preClosePrice;
    //最低价
    @ApiModelProperty("最低价")
    private BigDecimal lowPrice;
    //最高价
    @ApiModelProperty("最高价")
    private BigDecimal highPrice;
    //开盘价
    @ApiModelProperty("开盘价")
    private BigDecimal openPrice;
    //交易金额
    @ApiModelProperty("交易金额")
    private BigDecimal tradeVol;
    //当前价格
    @ApiModelProperty("当前价格")
    private BigDecimal tradePrice;
    //当前日期
    @ApiModelProperty("当前日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
    private Date curDate;
}
