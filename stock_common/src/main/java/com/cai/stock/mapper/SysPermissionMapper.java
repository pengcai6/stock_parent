package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author cai
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:52
* @Entity com.cai.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 侧边栏权限树（不包含按钮权限）
     *
     * @param username 用户名
     * @return
     */
   List<Map<String, Object>> getMenus(@Param("username") String username);

    /**
     * 通过id查找信息
     * @param id
     * @return
     */
   List<Map> selectAllById(@Param("id") Integer id);

    /**
     * 通过pid查找信息
     * @param pid
     * @return
     */
    List<Map> selectAllByPid(@Param("pid") Integer pid);
}
