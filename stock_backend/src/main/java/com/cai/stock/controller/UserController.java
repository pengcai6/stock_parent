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
@Api(value = "/api", tags = {"定义用户web层接口资源bean"})
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 根据用户名称查询用户信息
     *
     * @param name
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "name", value = "", required = true)
    })
    @ApiOperation(value = "根据用户名查询信息", notes = "根据用户名称查询用户信息", httpMethod = "GET")
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name) {
        return userService.findUserInfoByUsername(name);
    }

    /**
     * 用户登陆功能 完善用户登录成功动态回显菜单栏
     * @param Vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "LoginReqVo", name = "Vo", value = "", required = true)
    })
    @ApiOperation(value = "用户登陆功能", notes = "用户登陆功能", httpMethod = "POST")
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginReqVo Vo){
    return userService.login(Vo);
    }

    /**
     * 生成图片验证码功能
     * @return
     */
    @ApiOperation(value = "验证码功能", notes = "生成图片验证码功能", httpMethod = "GET")
    @GetMapping("/captcha")
    public R<Map> getCaptchaId(){
    return userService.getCaptchaId();
    }
}
