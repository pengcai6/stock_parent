package com.cai.stock.service;

import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.LoginRespVo;
import com.cai.stock.vo.resp.R;

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
    R<LoginRespVo> login(LoginReqVo vo);
    /**
     * 生成图片验证码功能
     * @return
     */
    R<Map> getCaptchaId();
}
