package com.cai.stock.controller;

import com.cai.stock.pojo.domain.ExterPermissionsDomain;
import com.cai.stock.service.RolesService;
import com.cai.stock.vo.req.updatePermsByRoleIdReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 定义角色相关接口
 */
@Api(value = "/api", tags = {"定义角色相关接口"})
@RestController
@RequestMapping("/api")
public class RolesController {
    @Autowired
    private RolesService rolesService;

    /**
     * 分页查询当前角色信息
     *
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Map<String, Integer>", name = "map", value = "", required = true)
    })
    @ApiOperation(value = "分页查询当前角色信息", notes = "分页查询当前角色信息", httpMethod = "POST")
    @PostMapping("/roles")
    public R<PageResult> getAllRoles(@RequestBody Map<String, Integer> map) {

        return rolesService.getAllRoles(map);
    }

    /**
     * 树状结构回显权限集合,底层通过递归获取权限数据集合
     *
     * @return
     */
    @ApiOperation(value = "树状结构回显权限集合,底层通过递归获取权限数据集合", notes = "树状结构回显权限集合,底层通过递归获取权限数据集合", httpMethod = "GET")
    @GetMapping("/permissions/tree/all")
    public R<List<ExterPermissionsDomain>> getPermissionsTree() {
        return rolesService.getPermissionsTree();
    }

    /**
     * 添加角色和角色关联权限
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "updatePermsByRoleIdReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "添加角色和角色关联权限", notes = "添加角色和角色关联权限", httpMethod = "POST")
    @PostMapping("/role")
    public R addRolesPermissions(@RequestBody updatePermsByRoleIdReqVo reqVo) {
        return rolesService.addRolesPermissions(reqVo);
    }

    /**
     *  更新角色信息，包含角色关联的权限信息
     * @param reqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "updatePermsByRoleIdReqVo", name = "reqVo", value = "", required = true)
    })
    @ApiOperation(value = "更新角色信息，包含角色关联的权限信息", notes = "更新角色信息，包含角色关联的权限信息", httpMethod = "PUT")
    @PutMapping("/role")
    //@PreAuthorize("hasAuthority('sys:role:update')")
    public R updateRolesPermissions(@RequestBody updatePermsByRoleIdReqVo reqVo) {
        return rolesService.updateRolesPermissions(reqVo);
    }


    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id查找对应的权限id集合", notes = "根据角色id查找对应的权限id集合", httpMethod = "GET")
    @GetMapping("/role/{roleId}")
    public R<List<String>> getOwnPermission(@PathVariable(value = "roleId")String  roleId ){
        return rolesService.getOwnPermission(roleId);
    }


    /**
     *  根据角色id删除角色信息
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id删除角色信息", notes = "根据角色id删除角色信息", httpMethod = "DELETE")
    @DeleteMapping("/role/{roleId}")
    public R deleteRole(@PathVariable(value = "roleId") String roleId) {
        return rolesService.deleteRole(roleId);
    }

    /**
     * 更新用户的状态信息
     * @param roleId
     * @param status
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "roleId", value = "", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "status", value = "", required = true)
    })
    @ApiOperation(value = "更新用户的状态信息", notes = "更新用户的状态信息", httpMethod = "POST")
    @PostMapping("/role/{roleId}/{status}")
    public R updateRoleStatue(@PathVariable(value = "roleId") Long roleId, @PathVariable(value = "status") Integer status) {
        return rolesService.updateRoleStatue(roleId,status);
    }



}
