package com.cai.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import com.cai.stock.constant.StockConstant;
import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.mapper.SysRoleMapper;
import com.cai.stock.mapper.SysUserMapper;
import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.service.UserService;
import com.cai.stock.utils.IdWorker;
import com.cai.stock.utils.permission;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cai.stock.utils.permission.findAllChildren;

/**
 * 定义服务实现
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private   SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private permission permission;
    /**
     * 根据用户名查询用户信息
     *
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
    public R<Map<String, Object>> login(LoginReqVo vo) {
        //1.判断参数是否合法、
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断输入的验证码是否存在
        if(StringUtils.isBlank(vo.getCode())||StringUtils.isBlank(vo.getSessionId()))
        {
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        //判断redis中保存的验证码是否与输入的验证码相同（比较时忽略大小写）
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + vo.getSessionId());
        if (StringUtils.isBlank(redisCode)) {
            //验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        if (!redisCode.equalsIgnoreCase(vo.getCode())) {
            //验证码错误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }
        String username=vo.getUsername();

        //2.根据用户名去数据库查询用户信息,获取密码的密文
        SysUser dbUser = sysUserMapper.findUserInfoByUsername(username);
        if (dbUser == null) {
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //3.调用密码匹配器匹配输入的密码和数据库的密文密码
        if (!passwordEncoder.matches(vo.getPassword(), dbUser.getPassword())) {
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //封装数据
        Map<String, Object> data = new HashMap<>();
        //1.根据用户名已经查询了用户信息，且做了用户信息合法性的判断；
        Map sysUser =sysRoleMapper.selectByUsername(username);
        // 2.如果 用户合法，则根据用户的id去数据库查询用户拥有的权限信息集合；
        List<Map<String, Object>> menus= sysPermissionMapper.getMenus(username);
        List<String> permissions=new ArrayList<>();
        data.putAll(sysUser);
        List<Map<String, Object>> maps = findAllChildren(0l, menus);
        for (Map<String, Object> map : menus) {
            if((Integer) map.get("type")==3){
                String permission=(String) map.get("code");
                permissions.add(permission);
            }
        }
        data.put("menus",maps);
        data.put("permissions",permissions);
        //3.获取按钮权限标识集合（获取权限集合中type=3的权限信息）



        //4.响应
//        LoginRespVo RespVo = new LoginRespVo();
//        RespVo.setUsername(dbUser.getUsername());
//        RespVo.setNickName(dbUser.getNickName());
//        RespVo.setPhone(dbUser.getPhone());
//        BeanUtils.copyProperties(dbUser, RespVo);
        //必须保证属性名称与类型一致
//        return R.ok(RespVo);
        return R.ok(data);
    }

    /**
     * 生成图片验证码功能
     *
     * @return
     */
    @Override
    public R<Map> getCaptchaId() {
        //1.生成图片验证码
        /*
        参数1：生成图片的宽度
        参数2：生成图片的高度
        参数3：图片中验证码的长度
        参数4：干扰线的数量
         */
        GifCaptcha Captcha = CaptchaUtil.createGifCaptcha(250, 40,4);
        //获取校验码
        String checkCode = Captcha.getCode();

        //获取经过base64编码处理过的图片数据
        String imageData = Captcha.getImageBase64();
        //2.获取唯一sessionId 转化为String，避免前端精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的验证码为:{}，会话id为:{}",checkCode,sessionId);
        //3.SessionId作为key，校验码作为value存到redis中
        /*
        使用redis模拟Session行为，通过过期时间设置
         */
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX+sessionId,checkCode,5, TimeUnit.MINUTES);
        //4.组合数据
        Map<String,String> data=new HashMap();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        //5.响应数据
        return R.ok(data);
    }
}
