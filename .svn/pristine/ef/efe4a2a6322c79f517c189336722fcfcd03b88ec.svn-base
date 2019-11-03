package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.RoleInfo;
import com.xryb.zhtc.entity.RoleUser;
import com.xryb.zhtc.entity.UserInfo;

/**
 * 角色用户关联信息service接口
 * @author hshzh
 *
 */
public interface IRoleUserService {
	/**
	 * 批量保存角色用户关联信息
	 * @param ruList
	 * @return
	 */
	boolean saveRoleUser(String sourceId, boolean closeConn, List<RoleUser> ruList);	
	/**
	 * 删除角色用户关联
	 * @param ecUUID
	 * @param roleUUID
	 * @param userUUID
	 * @return
	 */
	boolean deleteRoleUser(String sourceId, boolean closeConn, String roleUUID, String userUUID);
	/**
	 * 通过角色UUID查关联的用户
	 * @param roleUUID
	 * @return
	 */
	List<UserInfo> findUserByRoleUUID(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 通过角色UUID查非关联用户
	 * @param roleUUID
	 * @return
	 */
	List<UserInfo> findNotUserByRoleUUID(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 通过用户UUID查关联的角色
	 * @param ecUUID
	 * @param userUUID
	 * @return
	 */
	List<RoleInfo> findRoleByUserUUID(String sourceId, boolean closeConn, String userUUID);
	/**
	 * 通过用户UUID查非关联的角色
	 * @param ecUUID
	 * @param userUUID
	 * @return
	 */
	List<RoleInfo> findNotRoleByUserUUID(String sourceId, boolean closeConn, String userUUID);
	/**
	 * 给用户分配角色
	 * @param sourceId
	 * @param closeConn
	 * @param userUUID
	 * @param unitUUID
	 * @return
	 */
	List<Map> findRoleToAssignRole(String sourceId, boolean closeConn, String userUUID);
	
}
