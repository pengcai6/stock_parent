package com.cai.stock.utils;

import com.cai.stock.pojo.domain.ExterPermissionsDomain;
import com.cai.stock.pojo.entity.SysPermission;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionsStockUtil {
    /**
     *  递归通过pid进行查询生成权限树
     * @param permissions
     * @param pid
     * @return
     */
    public static List<ExterPermissionsDomain> getAllPermissions(List<SysPermission> permissions,Long pid){
        List<ExterPermissionsDomain> arrayList = new ArrayList<>();

        List<SysPermission> Permissions = permissions.stream().filter(permission -> permission.getPid().equals(pid)).collect(Collectors.toList());
        Permissions.forEach(Permission->{
            ExterPermissionsDomain domain = new ExterPermissionsDomain();
            BeanUtils.copyProperties(Permission,domain);
            domain.setPath(Permission.getUrl());
            List<ExterPermissionsDomain> children = getAllPermissions(permissions, Permission.getId());
            domain.setChildren(children);
            arrayList.add(domain);
        });

        return arrayList;
    }
}
