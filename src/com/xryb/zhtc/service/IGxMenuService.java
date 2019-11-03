package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.GxMenu;

import dbengine.util.Page;

/**
 * 供需发布商品分类接口
 * @author Administrator
 *
 */

public interface IGxMenuService {
	/**
	 * 新增或修改菜单分类
	 */
	public boolean menuSave(String sourceId,boolean closeConn,GxMenu gxMenu);
	
	/**
	 * 修改是否有下级节点
	 */
	public boolean upMenuLowerNodeState(String sourceId,boolean closeConn,String ecUUID,String menuCode,String menuHaveLowerNode);
	
	/**
	 * 查下级节点的下一个菜单节点编号
	 */
	public String getMenuCodeNext(String sourceId,boolean closeConn,String menuParentCode);
	
	/**
	 * 查菜单列表
	 * @param ecUUID
	 * @param menuType 菜单类型（0顶级菜单，1级菜单，2二级菜单,3三级菜单 ）
	 * @param menuParentCode 父级菜单编码（不为空则查对应的下级菜单）
	 * @return
	 */
	List<GxMenu> findMenuList(String sourceId,boolean closeConn,Page page, Map<String, String> findMap);
	
	/**
	 *根据ID查专场分类菜单信息
	 */
	public GxMenu findMenu(String sourceId,boolean closeConn,String menuUUID);
	
	/**
	 * 删除专场分类菜单信息
	 */
	public boolean delMenu(String sourceId,boolean closeConn,String menuUUID);
	
	/**
	 * 查询menuParentCode
	 */
	public List<GxMenu> menuParentCodeAll(String sourceId,boolean closeConn,String menuParentCode);

}
