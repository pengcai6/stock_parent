package com.cai.stock.face;

import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;

import java.util.List;

public interface RolesFace {

    List<SysPermission> getPermsByUserId(Long userid);

    List<String> getRolesStrList(Long userid);

    List<menusPermDomain> getPermissionTree(List<SysPermission> perms, long preId);

    List<String> getPermissions(List<SysPermission> perms);
}
