package com.cai.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 外盘指数域对象
 */
@ApiModel(description = "外盘指数域对象")
@Data
public class ExternalMarKetDomain {

    /**
     * 指数名称
     */
    @ApiModelProperty("指数名称")
    private String name;

    /**
     * 当前点
     */
    @ApiModelProperty("当前点")
    private BigDecimal curPoint;

    /**
     * 涨跌值
     */
    @ApiModelProperty("涨跌值")
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    @ApiModelProperty("涨幅")
    private BigDecimal rose;

    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间")
    @JsonFormat(pattern = "yyyyMMdd")
    private Date curTime;
}
