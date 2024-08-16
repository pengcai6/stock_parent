package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
* @author cai
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-08-16 15:51:52
* @Entity com.cai.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findUserInfoByUsername(@Param("username") String username);

}
