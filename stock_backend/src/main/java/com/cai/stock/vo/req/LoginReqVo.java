package com.cai.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by itheima
 * @Date 2021/12/30
 * @Description 登录请求vo
 */
@Data
@ApiModel(value = "登陆请求参数实体类")
public class LoginReqVo {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;
    /**
     * 会话Id
     */
    @ApiModelProperty(value = "会话Id")
    private String sessionId;
}