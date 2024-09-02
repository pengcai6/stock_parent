package com.cai.stock.security.service;

import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.mapper.SysRoleMapper;
import com.cai.stock.mapper.SysUserMapper;
import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.pojo.entity.SysRole;
import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.security.user.LoginUserDetail;
import com.cai.stock.service.permissionService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定义获取用户详情服务bean
 */
@Service("userDetailsService")
public class LoginUserDetailService implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private permissionService permissionService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    /**
     * 根据传入的用户名称获取用户相关的详情信息：用户名，密文密码，权限集合
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名称获取数据库中用户的详细信息
        SysUser dbUser = sysUserMapper.findUserInfoByUsername(username);
        //
        if (dbUser==null) {
            throw new UsernameNotFoundException("当前用户不存在");

        }
        //获取指定用户的权限集合 添加获取侧边栏数据和按钮权限的结合信息
        List<SysPermission> perms = sysPermissionMapper.getPermsByUserId(dbUser.getId());
        List<SysRole> roles = sysRoleMapper.getRolesById(dbUser.getId());
        //前端需要的获取菜单按钮集合
        List<String> permissions = perms.stream()
                .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
                .map(per -> per.getCode()).collect(Collectors.toList());
        //前端需要的获取树状权限菜单数据
        List<menusPermDomain> menus =permissionService.getTree(perms, 0L);
        //获取springSecurity的权限标识
        List<String> ps = new ArrayList<>();
        List<String> pers = perms.stream()
                .filter(per -> StringUtils.isNotBlank(per.getPerms()))
                .map(per -> per.getPerms())
                .collect(Collectors.toList());
        ps.addAll(pers);
        List<String> rs = roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.toList());
        ps.addAll(rs);
        //将用户拥有的权限表示转权限对象
        String[] psArray = ps.toArray(new String[pers.size()]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(psArray);
        //构建用户详情服务对象
        LoginUserDetail UserDetail = new LoginUserDetail();
        BeanUtils.copyProperties(dbUser, UserDetail);
        UserDetail.setMenus(menus);
        UserDetail.setPermissions(permissions);
        UserDetail.setAuthorities(authorityList);
        return UserDetail;
    }
}
