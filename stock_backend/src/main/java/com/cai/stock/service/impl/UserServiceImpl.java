package com.cai.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import com.cai.stock.constant.StockConstant;
import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.mapper.SysRoleMapper;
import com.cai.stock.mapper.SysUserMapper;
import com.cai.stock.pojo.domain.LoginUserDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.pojo.entity.SysRole;
import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.pojo.vo.UserReVo;
import com.cai.stock.service.UserService;
import com.cai.stock.service.permissionService;
import com.cai.stock.utils.IdWorker;
import com.cai.stock.utils.permission;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.req.UpdateRoleReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.cai.stock.vo.resp.accessTokenLoginRespVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private permission permission;
    @Autowired
    private permissionService permissionService;
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
    public R<accessTokenLoginRespVo> login(LoginReqVo vo) {
        //1.判断参数是否合法、
        if (vo == null || StringUtils.isBlank(vo.getUsername()) || StringUtils.isBlank(vo.getPassword())) {
            return R.error(ResponseCode.DATA_ERROR);
        }
        //判断输入的验证码是否存在
        if (StringUtils.isBlank(vo.getCode()) || StringUtils.isBlank(vo.getSessionId())) {
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
        String username = vo.getUsername();

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
        LoginUserDomain loginUserDomain = new LoginUserDomain();
        BeanUtils.copyProperties(dbUser,loginUserDomain);
        List<SysPermission> perms = sysPermissionMapper.getPermsByUserId(dbUser.getId());
        //前端需要的获取菜单按钮集合
        List<String> permissions = perms.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        loginUserDomain.setPermissions(permissions);
        loginUserDomain.setMenus(permissionService.getTree(perms, 0L));
        accessTokenLoginRespVo accessTokenLoginRespVo = new accessTokenLoginRespVo();
        BeanUtils.copyProperties(loginUserDomain,accessTokenLoginRespVo);
        String info=accessTokenLoginRespVo.getId()+":"+ accessTokenLoginRespVo.getUsername();
        accessTokenLoginRespVo.setAccessToken(BaseEncoding.base64().encode(info.getBytes()));
        return R.ok(accessTokenLoginRespVo);
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
        GifCaptcha Captcha = CaptchaUtil.createGifCaptcha(250, 40, 4);
        //获取校验码
        String checkCode = Captcha.getCode();

        //获取经过base64编码处理过的图片数据
        String imageData = Captcha.getImageBase64();
        //2.获取唯一sessionId 转化为String，避免前端精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的验证码为:{}，会话id为:{}", checkCode, sessionId);
        //3.SessionId作为key，校验码作为value存到redis中
        /*
        使用redis模拟Session行为，通过过期时间设置
         */
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX + sessionId, checkCode, 5, TimeUnit.MINUTES);
        //4.组合数据
        Map<String, String> data = new HashMap();
        data.put("imageData", imageData);
        data.put("sessionId", sessionId);
        //5.响应数据
        return R.ok(data);
    }

    /**
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     *
     * @param reVo 查询参数
     * @return
     */
    @Override
    public R<PageResult<SysUser>> getUserByConditions(UserReVo reVo) {
        Integer pageNum = reVo.getPageNum();
        Integer pageSize = reVo.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        //调用mapper接口查询数据
        List<SysUser> data = sysUserMapper.getUserByConditions(reVo);
        //封装后返回
        PageInfo<SysUser> pageInfo = new PageInfo<>(data);
        PageResult<SysUser> result = new PageResult<>(pageInfo);
        result.setSize(20);
        return R.ok(result);
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public R insetUser(SysUser user) {
        user.setCreateTime(DateTime.now().toDate());
        user.setId(idWorker.nextId());
        //调用插入接口
        int count = sysUserMapper.insert(user);
        if (count > 0) {
            return R.ok("操作成功");
        } else return R.error(ResponseCode.ERROR);
    }

    /**
     * 获取用户具有的角色信息，以及所有角色信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public R<Map<String, Object>> getRolesById(String userId) {
        //创建Map
        Map<String, Object> data = new HashMap<>();
        //调用Mapper通过角色所有角色列表
        List<SysRole> allRole = sysRoleMapper.getAllRole();
        data.put("allRole", allRole);
        //调用Mapper通过id查询用户拥有的角色
        List<Long> ownRoleIds = sysRoleMapper.getRolesIdsById(userId);
        data.put("ownRoleIds", ownRoleIds);
        return R.ok(data);
    }

    @Override
    public R updateRole(UpdateRoleReqVo updateRoleReqVo) {
        String userId = updateRoleReqVo.getUserId();
        List<String> roleIds = updateRoleReqVo.getRoleIds();
        //1.使用Mapper接口调用删除方法
        sysRoleMapper.deleteByUserId(userId);
        roleIds.forEach(roleId -> {
            //生成列表id
            long id = idWorker.nextId();
            //2.使用mapper接口调用插入方法
            sysRoleMapper.insertList(id, roleId, userId);
        });
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 批量删除用户信息，delete请求可通过请求体携带数据
     *
     * @param userIds 用户ids
     * @return
     */
    @Override
    public R deleteUser(List<Long> userIds) {
        //调用mapper接口批量删除数据
        int count = sysUserMapper.deleteUsers(userIds);
        if (count > 0) {
            return R.ok(ResponseCode.SUCCESS.getMessage());
        } else return R.error(ResponseCode.ERROR.getMessage());
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public R<Map<String, Object>> getUserInfoByUserId(String userId) {
//       调用mapper接口查询用户信息
        Map<String, Object> data = sysUserMapper.getUserInfoByUserId(userId);
        //返回数据
        if (data != null) {
            return R.ok(data);
        } else return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
    }
    /**
     * 根据id更新用户基本信息
     *
     * @param sysUser 用户信息
     * @return
     */
    @Override
    public R updateUserInfo(SysUser sysUser) {
        //调用mapper接口更新用户信息
        int count =sysUserMapper.updateByPrimaryKey(sysUser);
        if(count>0){
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        else return R.error(ResponseCode.ERROR.getMessage());
    }
}

