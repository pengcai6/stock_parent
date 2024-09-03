package com.cai.stock.service;

import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.vo.resp.R;

import java.util.List;
import java.util.Map;


public interface PermissionService {
    List<menusPermDomain> getTree(List<SysPermission> perms, long pid);
    /**
     * 查询所有权限集合
     * @return
     */
    R<List<SysPermission>> getAllPermissions();
    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    R<List<Map>> getPermissionsTree();
    /**
     *  权限添加按钮
     * @param vo
     * @return
     */
    R addPermission(SysPermission vo);
    /**
     * 更新权限
     * @param vo
     * @return
     */
    R updatePermission(SysPermission vo);
    /**
     * 删除权限
     * @param permissionId
     * @return
     */
    R deletePermission(Long permissionId);
}
