package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;

import dbengine.util.Page;


/**
 * 菜单权限信息接口
 * @author chengyz
 *
 */
public interface IMenuInfoService {
	/**
	 * 保存或修改菜单信息
	 * @param menu
	 * @return
	 */
	boolean saveOrUpMenu(String sourceId,boolean closeConn,MenuInfo menu);
	
	/**
	 * 删除菜单信息
	 * @param menuUUID
	 * @return
	 */
	boolean deleteMenu(String sourceId,boolean closeConn,String menuUUID);
	/**
	 * 查询菜单信息
	 * @param menuUUID
	 * @return
	 */
	MenuInfo findMenu(String sourceId,boolean closeConn,String menuUUID);
	/**
	 * 查菜单列表
	 * @param ecUUID
	 * @param menuType 菜单类型（0顶级菜单，1级菜单，2二级菜单,3三级菜单 ）
	 * @param menuParentCode 父级菜单编码（不为空则查对应的下级菜单）
	 * @return
	 */
	List<MenuInfo> findMenuList(String sourceId,boolean closeConn,Page page, Map<String, String> findMap);
	
    /**
     * 获取该节点的一个未使用的节点编号
     * @param menuParentCode
     * @return
     */
    String getMenuCodeNext(String sourceId,boolean closeConn,String menuParentCode);
    
    /**
     * 删除用户关联的所有菜单权限
     * @param sourceId
     * @param closeConn
     * @param userUUID
     * @return
     */
    boolean deleteMenuUser(String sourceId,boolean closeConn,String userUUID);
    
    /**
     * 保存用户菜单权限关联信息
     * @param sourceId
     * @param closeConn
     * @param info
     * @return
     */
    boolean saveMenuUser(String sourceId,boolean closeConn,MenuUser menu);
    

}
