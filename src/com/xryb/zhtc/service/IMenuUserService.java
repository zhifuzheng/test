package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;


/**
 * 菜单用户关联信息接口
 * @author hshzh
 *
 */
public interface IMenuUserService {
	/**
	 * 保存菜单用户关联信息
	 * @param muList
	 * @return
	 */
	boolean saveMenuUser(String sourceId, boolean closeConn, List<MenuUser> muList);
	
	/**
	 * 删除菜单用户信息
	 * @param ecUUID
	 * @param menuUUID
	 * @param userUUID
	 * @return
	 */
	boolean deleteMenuUser(String sourceId, boolean closeConn, String menuUUID, String userUUID);
	/**
	 * 通过用户ID查关联的菜信息
	 * @param ecUUID
	 * @param userUUID
	 * @return
	 */
	List<MenuInfo> findMenuByUserUUID(String sourceId, boolean closeConn, String userUUID);
	/**
	 * 查询用户的菜单权限信息
	 * @param sourceId
	 * @param closeConn
	 * @param userUUID
	 * @return
	 */
	List<Map> findAllMenuItemForUser(String sourceId, boolean closeConn, String userUUID);
	
}
