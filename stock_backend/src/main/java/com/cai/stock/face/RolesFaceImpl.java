package com.cai.stock.face;

import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.mapper.SysRoleMapper;
import com.cai.stock.mapper.SysRolePermissionMapper;
import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.pojo.entity.SysRole;
import com.cai.stock.service.PermissionService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.CacheNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Component("rolesFace")
//@CacheConfig(cacheNames = "role")
public class RolesFaceImpl implements RolesFace {
   @Autowired
   private SysRoleMapper sysRoleMapper;
   @Autowired
   private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private PermissionService permissionService;
   @Cacheable(cacheNames = "getPerms",key = "#userid")
    @Override
    public List<SysPermission> getPermsByUserId(Long userid) {
        return sysPermissionMapper.getPermsByUserId(userid);
    }

    @Cacheable(cacheNames = "getRolesStr",key = "#userid")
    @Override
    public List<String> getRolesStrList(Long userid) {
        List<SysRole> roles = sysRoleMapper.getRolesById(userid);
        return roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.toList());
    }
    @Cacheable(cacheNames = "getPermissionTree",key = "#preId")
    @Override
    public List<menusPermDomain> getPermissionTree(List<SysPermission> perms, long preId) {
       return permissionService.getTree(perms, 0L);
    }
    @Cacheable(cacheNames = "getPermissions",key = "'perms'")
    @Override
    public List<String> getPermissions(List<SysPermission> perms) {
       return  perms.stream()
               .filter(per -> !Strings.isNullOrEmpty(per.getCode()) && per.getType() == 3)
               .map(per -> per.getCode()).collect(Collectors.toList());
    }

}
