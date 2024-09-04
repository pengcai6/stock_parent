package com.cai.stock.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.cai.stock.face.RolesFace;
import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.mapper.SysRoleMapper;
import com.cai.stock.mapper.SysRolePermissionMapper;
import com.cai.stock.pojo.domain.ExterPermissionsDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.pojo.entity.SysRole;
import com.cai.stock.pojo.entity.SysRolePermission;
import com.cai.stock.service.RolesService;
import com.cai.stock.utils.IdWorker;
import com.cai.stock.vo.req.updatePermsByRoleIdReqVo;
import com.cai.stock.vo.resp.PageResult;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cai.stock.utils.PermissionsStockUtil.getAllPermissions;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    @Autowired
    private RolesFace rolesFace;


    @Autowired
    private IdWorker idWorker;

    /**
     * 分页查询当前角色信息
     * @param map
     * @return
     */
    @Override
    public R<PageResult> getAllRoles(Map<String, Integer> map) {
        //获取分页
        Integer pageNum = map.get("pageNum");
        //获取大小
        Integer pageSize = map.get("pageSize");
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //查询数据
        List<SysRole> rows = sysRoleMapper.getAllRole();
        //封装数据
        PageInfo<SysRole> pageInfo = new PageInfo<>(rows);
        PageResult<SysRole> result = new PageResult<>(pageInfo);
        return R.ok(result);
    }

    /**
     * 树状结构回显权限集合,底层通过递归获取权限数据集合
     *
     * @return
     */
    @Override
    public R<List<ExterPermissionsDomain>> getPermissionsTree() {
        //获取全部权限集合
        List<SysPermission> Permissions = sysPermissionMapper.getAllPerms();
        // 递归通过pid进行查询生成权限树
        List<ExterPermissionsDomain> allPermissions = getAllPermissions(Permissions, 0L);
        return R.ok(allPermissions);
    }

    /**
     * 添加角色和角色关联权限
     * @param reqVo
     * @return
     */
    @Override
    public R addRolesPermissions(updatePermsByRoleIdReqVo reqVo) {
        //生成角色id
        long rid = idWorker.nextId();
        List<SysRolePermission> rolePermissions = new ArrayList<>();
        for (Long permissionsId : reqVo.getPermissionsIds()) {
            rolePermissions.add(SysRolePermission.builder().id(idWorker.nextId()).roleId(rid).permissionId(permissionsId).build());
        }
        // 在 Java 代码中生成当前时间
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        //给role中添加id
        SysRole sysRole = new SysRole();
        //生成默认状态
        sysRole.setStatus(1);
        sysRole.setDeleted(1);
        //拷贝属性
        BeanUtils.copyProperties(reqVo, sysRole);
        sysRole.setCreateTime(DateTime.now().toJdkDate());
        sysRole.setId(rid);
        //添加用户
        int i = sysRoleMapper.addRole(sysRole);
        //批量添加用户对应权限
        int j = sysRolePermissionMapper.insertPerms(rolePermissions, currentTime);
        if (j > 0 && i > 0) {
            return R.ok("操作成功");
        } else return R.error(ResponseCode.DATA_ERROR.getMessage());
    }

    /**
     * 根据角色id删除角色信息
     * @param roleId
     * @return
     */
    @Override
    public R deleteRole(String roleId) {
        //通过角色id删除对应权限
        int i = sysRolePermissionMapper.deleteByRoleId(Long.parseLong(roleId));
        //通过角色id删除对象的角色
        int j = sysRoleMapper.deleteByPrimaryKey(Long.parseLong(roleId));
        if (i > 0 || j > 0) {
            return R.ok(ResponseCode.SUCCESS);
        } else return R.error(ResponseCode.ERROR);
    }

    /**
     * 更新用户的状态信息
     * @param roleId
     * @param status
     * @return
     */
    @Override
    public R updateRoleStatue(Long roleId, Integer status) {
        //根据角色信息更新角色状态
        int i = sysRoleMapper.updateRoleStatue(roleId, status);
        if (i > 0) {
            return R.ok(ResponseCode.SUCCESS);
        } else return R.error(ResponseCode.ERROR);
    }

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @Override
    public R<List<String>> getOwnPermission(String roleId) {
        //根据角色id获取权限id列表
        List<Long> permsByRoleId = sysRolePermissionMapper.getPermsByRoleId(roleId);
        //将权限id类型转为string
        List<String> list = permsByRoleId.stream().map(perms -> perms.toString()).collect(Collectors.toList());
        return R.ok(list);
    }

    /**
     * 更新角色信息，包含角色关联的权限信息
     * @param reqVo
     * @return
     */
    @Override
    public R updateRolesPermissions(updatePermsByRoleIdReqVo reqVo) {
        //判断是否有id
        if (ObjectUtil.isNotNull(reqVo.getId())) {
            //根据角色id获取权限id列表
            List<Long> perms = sysRolePermissionMapper.getPermsByRoleId(String.valueOf(reqVo.getId()));
            if (!CollectionUtils.isEmpty(perms)) {
                //根据角色id删除权限id列表
                int i = sysRolePermissionMapper.deleteByRoleId(reqVo.getId());
                if (i <= 0) {
                    return R.error(ResponseCode.DATA_ERROR.getMessage());
                }
            }

            //生成角色id
            long rid = idWorker.nextId();
            //生成角色权限集合
            List<SysRolePermission> rolePermissions = new ArrayList<>();
            for (Long permissionsId : reqVo.getPermissionsIds()) {
                rolePermissions.add(SysRolePermission.builder().id(idWorker.nextId()).roleId(rid).permissionId(permissionsId).build());
            }
            // 在 Java 代码中生成当前时间
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(reqVo.getId());
            BeanUtils.copyProperties(reqVo, sysRole);
            sysRole.setUpdateTime(DateTime.now().toJdkDate());
            //给role中添加id
            sysRole.setId(rid);
            //添加角色
            int i = sysRoleMapper.addRole(sysRole);
            //添加角色权限信息
            int j = sysRolePermissionMapper.insertPerms(rolePermissions, currentTime);
            sysRoleMapper.deleteByUserId(String.valueOf(reqVo.getId()));
            if (j > 0 && i > 0) {
                return R.ok("操作成功");
            } else return R.error(ResponseCode.DATA_ERROR.getMessage());

        } else return R.error(ResponseCode.ERROR.getMessage());
    }
}
