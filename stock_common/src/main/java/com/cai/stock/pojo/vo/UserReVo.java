package com.cai.stock.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 多条件查询用户的参数
 */
@ApiModel(description = "多条件查询用户的参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReVo {
    //页数
    @ApiModelProperty("页数")
    Integer pageNum;
    //每页大小
    @ApiModelProperty("每页大小")
    Integer pageSize;
    //用户名
    @ApiModelProperty("用户名")
    String username;
    //昵称
    @ApiModelProperty("昵称")
    String nickName;
    //开始时间
    @ApiModelProperty("开始时间")
    Date startTime;
    //结束时间
    @ApiModelProperty("结束时间")
    Date endTime;
}
