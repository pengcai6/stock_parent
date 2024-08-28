package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysRole;
import org.apache.ibatis.annotations.Param;

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
}
