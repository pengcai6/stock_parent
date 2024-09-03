package com.cai.stock.service.impl;

import com.cai.stock.mapper.SysPermissionMapper;
import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;
import com.cai.stock.service.PermissionService;
import com.cai.stock.utils.DateTimeUtil;
import com.cai.stock.utils.IdWorker;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class permissionServiceImpl implements PermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private IdWorker idWorker;
    @Override
    public List<menusPermDomain> getTree(List<SysPermission> perms, long pid) {
        ArrayList<menusPermDomain> list = new ArrayList<>();
        for (SysPermission p : perms) {
            if(p.getType()<3 ){
                if (p.getPid().equals(pid)) {
                    list.add(new menusPermDomain(p.getId(), p.getTitle(), p.getIcon(), p.getUrl(), p.getName(), getTree(perms, p.getId())));
                }
            }
        }
        return list;
    }
    /**
     * 查询所有权限集合
     * @return
     */
    @Override
    public R<List<SysPermission>> getAllPermissions() {
        List<SysPermission> allPerms = sysPermissionMapper.getAllPerms();
        return R.ok(allPerms);
    }
    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @Override
    public R<List<Map>> getPermissionsTree() {
       List<Map> data= sysPermissionMapper.getPermissionsTree();
       return R.ok(data);
    }

    @Override
    public R addPermission(SysPermission vo) {
        vo.setStatus(1);
        vo.setId(idWorker.nextId());
        vo.setDeleted(1);
        vo.setCreateTime(DateTime.now().toDate());
        int i = sysPermissionMapper.insert(vo);
        if(i>0){
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        else return R.error(ResponseCode.ERROR.getMessage());
    }
    /**
     * 更新权限
     * @param vo
     * @return
     */
    @Override
    public R updatePermission(SysPermission vo) {
        vo.setUpdateTime(DateTime.now().toDate());
        int i = sysPermissionMapper.updateByPrimaryKeySelective(vo);
        if(i>0){
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        else return R.error(ResponseCode.ERROR.getMessage());
    }
    /**
     * 删除权限
     * @param permissionId
     * @return
     */
    @Override
    public R deletePermission(Long permissionId) {
        int i = sysPermissionMapper.deleteByPrimaryKey(permissionId);
        if(i>0){
            return R.ok(ResponseCode.SUCCESS.getMessage());
        }
        else return R.error(ResponseCode.ERROR.getMessage());
    }
}
