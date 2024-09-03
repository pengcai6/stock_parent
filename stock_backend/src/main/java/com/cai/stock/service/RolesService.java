package com.cai.stock.service;

import com.cai.stock.pojo.domain.ExterPermissionsDomain;
import com.cai.stock.vo.req.updatePermsByRoleIdReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;

import java.util.List;
import java.util.Map;

public interface RolesService {
    R<PageResult> getAllRoles(Map<String, Integer> map);

    R<List<ExterPermissionsDomain>> getPermissionsTree();

    R addRolesPermissions(updatePermsByRoleIdReqVo reqVo);

    R deleteRole(String roleId);

    R updateRoleStatue(Long roleId, Integer status);

    R<List<String>> getOwnPermission(String roleId);

    R updateRolesPermissions(updatePermsByRoleIdReqVo reqVo);
}
