package com.cai.stock.mapper;

import com.cai.stock.pojo.entity.SysUser;
import com.cai.stock.pojo.vo.UserReVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 查询所有用户信息
     * @return
     */
     List<SysUser> FindAll();

    /**
     * 多条件综合查询用户分页信息
      * @param reVo 条件
     * @return
     */
    List<SysUser> getUserByConditions(@Param("reVo") UserReVo reVo);

    /**
     * 批量删除用户信息
     * @param userIds 批量删除的用户id
     * @return
     */
    int deleteUsers(@Param("userIds") List<Long> userIds);
    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return
     */
    Map<String, Object> getUserInfoByUserId(@Param("userId") String userId);
}
