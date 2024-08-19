package com.cai.stock.controller;


import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.service.impl.UserServiceImpl;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.LoginRespVo;
import com.cai.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定义用户web层接口资源bean
 */
@RestController
@RequestMapping("/api")
@Api(tags = "用户相关接口处理")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 根据用户名称查询用户信息
     *
     * @param name
     * @return
     */
    @ApiOperation(value = "根据用户名查询信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "name",value = "用户名",dataType = "string",required = true,type = "path")
    )
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name) {
        return userService.findUserInfoByUsername(name);
    }

    /**
     * 用户登陆功能
     * @param Vo
     * @return
     */
    @ApiOperation(value = "用户登陆功能")
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo Vo){
    return userService.login(Vo);
    }

    /**
     * 生成图片验证码功能
     * @return
     */
    @ApiOperation("验证码功能")
    @GetMapping("/captcha")
    public R<Map> getCaptchaId(){
    return userService.getCaptchaId();
    }
}
