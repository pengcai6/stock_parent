package com.cai.stock.controller;


import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.service.impl.UserServiceImpl;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.pojo.vo.UserReVo;
import com.cai.stock.vo.req.UpdateRoleReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.accessTokenLoginRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定义用户相关接口
 */
@Api(value = "/api", tags = {"定义用户相关接口"})
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
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "body", dataType = "LoginReqVo", name = "Vo", value = "", required = true)
//    })
//    @ApiOperation(value = "用户登陆功能", notes = "用户登陆功能", httpMethod = "POST")
//    @PostMapping("/login")
//    public R<accessTokenLoginRespVo> login(@RequestBody LoginReqVo Vo){
//    return userService.login(Vo);
//    }

    /**
     * 生成图片验证码功能
     * @return
     */
    @ApiOperation(value = "验证码功能", notes = "生成图片验证码功能", httpMethod = "GET")
    @GetMapping("/captcha")
    public R<Map> getCaptchaId(){
    return userService.getCaptchaId();
    }

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param reVo 查询参数
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserReVo", name = "reVo", value = "查询参数", required = true)
    })
    @ApiOperation(value = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", notes = "多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围", httpMethod = "POST")
    @PostMapping("/users")
    public R<PageResult<SysUser>> getUserByConditions(@RequestBody UserReVo reVo){
    return userService.getUserByConditions(reVo);
    }

    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "SysUser", name = "user", value = "用户信息", required = true)
    })
    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
    @PostMapping("/user")
    public R insetUser(@RequestBody SysUser user ){
        return userService.insetUser(user);
    }

    /**
     * 获取用户具有的角色信息，以及所有角色信息
     * @param userId 用户id
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "userId", value = "用户id", required = true)
    })
    @ApiOperation(value = "获取用户具有的角色信息，以及所有角色信息", notes = "获取用户具有的角色信息，以及所有角色信息", httpMethod = "GET")
    @GetMapping("/user/roles/{userId}")
    public R<Map<String,Object>> getRolesById(@PathVariable(value = "userId") String userId ){
        return userService.getRolesById(userId);
    }

    /**
     * 更新用户角色信息
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UpdateRoleReqVo", name = "updateRoleReqVo", value = "", required = true)
    })
    @ApiOperation(value = "更新用户角色信息", notes = "更新用户角色信息", httpMethod = "PUT")
    @PutMapping("/user/roles")
    public R updateRole(@RequestBody UpdateRoleReqVo updateRoleReqVo)
    {
        return userService.updateRole(updateRoleReqVo);
    }

    /**
     *  批量删除用户信息，delete请求可通过请求体携带数据
     * @param userIds 用户ids
     * @return
     */
   @ApiImplicitParams({
           @ApiImplicitParam(paramType = "body", dataType = "List<Long>", name = "userIds", value = "用户ids", required = true)
   })
   @ApiOperation(value = "批量删除用户信息，delete请求可通过请求体携带数据", notes = "批量删除用户信息，delete请求可通过请求体携带数据", httpMethod = "DELETE")
   @DeleteMapping("/user")
//   @PreAuthorize("hasAuthority('sys:user:delete')")
    public R deleteUser(@RequestBody List<Long> userIds){
        return userService.deleteUser(userIds);
   }

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return
     */
   @GetMapping("/user/info/{userId}")
    public R<Map<String,Object>> getUserInfoByUserId(@PathVariable("userId") String userId){
       return userService.getUserInfoByUserId(userId);
   }

    /**
     *  根据id更新用户基本信息
      * @param sysUser 用户信息
     * @return
     */
  @PutMapping("/user")
  public R updateUserInfo(@RequestBody() SysUser sysUser){
       return userService.updateUserInfo(sysUser);
  }

}
