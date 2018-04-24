package com.wyait.manage.dao;

import com.wyait.manage.entity.RoleVO;
import com.wyait.manage.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 分页查询所有的角色列表
     * @return
     */
	List<Role> findList();

	/**
	 * 获取角色相关的数据
	 * @param id
	 * @return
	 */
	RoleVO findRoleAndPerms(Integer id);

	/**
	 * 根据用户id获取角色数据
	 * @param userId
	 * @return
	 */
	List<Role> getRoleByUserId(Integer userId);

	List<Role> getRoles();

}