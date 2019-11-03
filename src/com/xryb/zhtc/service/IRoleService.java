package com.xryb.zhtc.service;

import java.util.List;

import com.xryb.zhtc.entity.RoleInfo;

import dbengine.util.Page;

/**
 * 角色信息service接口
 * @author chengyz
 *
 */
public interface IRoleService {
	/**
	 * 保存或修改角色
	 * @param role
	 * @return
	 */
	boolean saveOrUpRole(String sourceId, boolean closeConn, RoleInfo role);
	/**
	 * 删除角色
	 * @param ecUUID
	 * @param roleUUID
	 * @return
	 */
	boolean deleteRole(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 查角色信息
	 * @param ecUUID
	 * @param roleUUID
	 * @return
	 */
	RoleInfo findRole(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 查角色信息列表
	 * @param ecUUID
	 * @return
	 */
	List<RoleInfo> findRoleList(String sourceId, boolean closeConn, Page page);
}
