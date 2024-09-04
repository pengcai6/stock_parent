package com.cai.stock.controller;

import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.service.PermissionService;
import com.cai.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定义权限相关接口
 */
@Api(value = "/api", tags = {"定义权限相关接口"})
@RestController()
@RequestMapping("/api")
public class PermissionsController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所有权限集合
     * @return
     */
    @ApiOperation(value = "查询所有权限集合", notes = "查询所有权限集合", httpMethod = "GET")
    @GetMapping("/permissions")
    public R<List<SysPermission>> getAllPermissions(){
        return permissionService.getAllPermissions();
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @GetMapping("/permissions/tree")
    public R<List<Map>> getPermissionsTree(){
        return permissionService.getPermissionsTree();
    }

    /**
     *  权限添加按钮
     * @param vo
     * @return
     */
    @PostMapping("/permission")
    public R addPermission(@RequestBody SysPermission vo){
        return permissionService.addPermission(vo);
    }

    /**
     * 更新权限
     * @param vo
     * @return
     */
    @PutMapping("/permission")
    public R updatePermission(@RequestBody SysPermission vo){
        return permissionService.updatePermission(vo);
    }

    /**
     * 删除权限
     * @param permissionId
     * @return
     */
    @DeleteMapping("/permission/{permissionId}")
    //@PreAuthorize("hasAuthority('sys:permission:delete')")
    public R deletePermission(@PathVariable("permissionId") Long permissionId )
    {
        return permissionService.deletePermission(permissionId);
    }
}
