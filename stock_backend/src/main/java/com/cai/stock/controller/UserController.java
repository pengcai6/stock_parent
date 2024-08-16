package com.cai.stock.controller;


import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.service.impl.UserServiceImpl;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.LoginRespVo;
import com.cai.stock.vo.resp.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 定义用户web层接口资源bean
 */
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
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName(@PathVariable("userName") String name) {
        return userService.findUserInfoByUsername(name);
    }

    /**
     * 用户登陆功能
     * @param Vo
     * @return
     */
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo Vo){
    return userService.login(Vo);
    }
}
