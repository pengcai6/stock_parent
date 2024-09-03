package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
* @author cai
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:52
* @Entity com.cai.stock.pojo.entity.SysRolePermission
*/
@Mapper
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    /**
     * 根据角色id获取权限id列表
     * @param roleId
     * @return
     */
    List<Long> getPermsByRoleId(@Param("roleId") String roleId);

    /**
     * //通过角色id删除对应权限
     * @param id
     * @return
     */
    int deleteByRoleId(@Param("id") Long id);

    /**
     *  //批量添加用户对应权限
     * @param list
     * @param currentTime
     * @return
     */
    int insertPerms(@Param("list") List<SysRolePermission> list, @Param("currentTime") Timestamp currentTime);

    void deleteByUserId(@Param("userId") String userId);
}
