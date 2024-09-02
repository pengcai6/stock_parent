package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author cai
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:52
* @Entity com.cai.stock.pojo.entity.SysRole
*/
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     */
    Map selectByUsername(@Param("username") String username);

    /**
     * 通过userId查询角色信息
     * @param userId 用户id
     * @return
     */
    List<SysRole> getRolesById(@Param("userId") Long userId);

    /**
     * 获取全部角色信息
     *
     * @return
     */
    List<SysRole> getAllRole();

    /**
     * 通过角色id删除
     * @param userId
     */
    void deleteByUserId(@Param("userId") String userId);

    /**
     * 批量添加角色信息
     *
     * @param id
     * @param roleId 角色id
     * @param userId 用户id
     */
    void insertList(@Param("id") long id, @Param("roleId") String roleId, @Param("userId") String userId);

    /**
     * 通过用户id查找拥有的角色id
     * @param userId
     * @return
     */
    List<Long> getRolesIdsById(@Param("userId") String userId);
}
