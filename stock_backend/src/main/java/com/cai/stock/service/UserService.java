package com.cai.stock.service;

import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.pojo.vo.UserReVo;
import com.cai.stock.vo.req.UpdateRoleReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.accessTokenLoginRespVo;

import java.util.List;
import java.util.Map;

/**
 * 定义用户服务接口
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    SysUser findUserInfoByUsername(String username);


    /**
     * 用户登陆功能
     *
     * @param vo
     * @return
     */
    R<accessTokenLoginRespVo> login(LoginReqVo vo);
    /**
     * 生成图片验证码功能
     * @return
     */
    R<Map> getCaptchaId();
    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * @param reVo 查询参数
     * @return
     */
    R<PageResult<SysUser>> getUserByConditions(UserReVo reVo);
    /**
     * 添加用户
     * @param user 用户信息
     * @return
     */
    R insetUser(SysUser user);
    /**
     * 获取用户具有的角色信息，以及所有角色信息
     * @param userId 用户id
     * @return
     */
    R<Map<String, Object>> getRolesById(String userId);

    /**
     * 更新用户角色信息
     * @return
     */
    R updateRole(UpdateRoleReqVo updateRoleReqVo);

    /**
     *  批量删除用户信息，delete请求可通过请求体携带数据
     * @param userIds 用户ids
     * @return
     */
    R deleteUser(List<Long> userIds);
    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return
     */
    R<Map<String, Object>> getUserInfoByUserId(String userId);
    /**
     * 根据id更新用户基本信息
     *
     * @param sysUser 用户信息
     * @return
     */
    R updateUserInfo(SysUser sysUser);
}
