package com.cai.stock.service.impl;

import com.cai.stock.mapper.SysUserMapper;
import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.service.UserService;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.LoginRespVo;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 定义服务实现
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public SysUser findUserInfoByUsername(String username) {
        return sysUserMapper.findUserInfoByUsername(username);
    }

    /**
     * 用户登陆功能
     *
     * @param vo
     * @return
     */
    @Override
    public R<LoginRespVo> login(LoginReqVo vo) {
       //1.判断参数是否合法、
        if (vo==null|| StringUtils.isBlank(vo.getUsername())||StringUtils.isBlank(vo.getPassword())||StringUtils.isBlank(vo.getCode())) {
        return R.error(ResponseCode.DATA_ERROR);
        }
       //2.根据用户名去数据库查询用户信息,获取密码的密文
        SysUser dbUser = sysUserMapper.findUserInfoByUsername(vo.getUsername());
        if(dbUser==null){
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //3.调用密码匹配器匹配输入的密码和数据库的密文密码
        if (!passwordEncoder.matches(vo.getPassword(),dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
       //4.响应
        LoginRespVo RespVo = new LoginRespVo();
//        RespVo.setUsername(dbUser.getUsername());
//        RespVo.setNickName(dbUser.getNickName());
//        RespVo.setPhone(dbUser.getPhone());
        BeanUtils.copyProperties(dbUser,RespVo);
        //必须保证属性名称与类型一致
        return R.ok(RespVo);
    }
}
