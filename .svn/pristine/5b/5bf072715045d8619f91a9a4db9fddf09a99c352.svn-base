package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuRole;


/**
 * 菜单角色信息关联接口
 * @author hshzh
 *
 */
public interface IMenuRoleService {
	/**
	 * 批量保存菜单角色信息
	 * @param mrList
	 * @return
	 */
	boolean saveMenuRole(String sourceId, boolean closeConn, List<MenuRole> mrList);
	
	/**
	 * 删除菜单角色信息
	 * @param roleUUID
	 * @param menuUUID
	 * @return
	 */
	boolean deleteMenuRole(String sourceId, boolean closeConn, String roleUUID, String menuUUID);
	/**
	 * 通过角色UUID查关联的菜单信息
	 * @param roleUUID
	 * @return
	 */
	List<MenuInfo> findMenuByRoleUUID(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 通过角色UUID查非关联的菜单信息
	 * @param roleUUID
	 * @return
	 */
	List<MenuInfo> findNotMenuByRoleUUID(String sourceId, boolean closeConn, String roleUUID);
	/**
	 * 查询角色所拥有的菜单权限
	 * @param sourceId
	 * @param closeConn
	 * @param roleUUID
	 * @param unitUUID
	 * @return
	 */
	List<Map> findAllMenuItemForRole(String sourceId, boolean closeConn, String roleUUID);
}
