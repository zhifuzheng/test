package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;
import spark.render.JsonRender;
import spark.utils.ObjectUtil;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuRole;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.service.IMenuInfoService;
import com.xryb.zhtc.service.IMenuRoleService;
import com.xryb.zhtc.service.IMenuUserService;
import com.xryb.zhtc.service.impl.MenuInfoServiceImpl;
import com.xryb.zhtc.service.impl.MenuRoleServiceImpl;
import com.xryb.zhtc.service.impl.MenuUserServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.MenuTree;

import dbengine.util.Page;
/**
 * 菜单信息管理
 * @author hshzh
 *
 */
public class MenuMng {
	@Auto(name=MenuInfoServiceImpl.class)
	private IMenuInfoService menuInfoService;
    	
	@Auto(name=MenuRoleServiceImpl.class)
	private IMenuRoleService menuRoleService;
	
	@Auto(name=MenuUserServiceImpl.class)
	private IMenuUserService menuUserService; 
	/**
	 * 保存或修改菜单权限信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrUpMenu(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		MenuInfo mi = (MenuInfo)ObjectUtil.req2Obj(request, MenuInfo.class);
		if(mi==null){
			return false;
		}
		if(mi.getId()==null){
			//新增，设置UUID
			mi.setMenuUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			//获得节点编号
			mi.setMenuCode(menuInfoService.getMenuCodeNext(sourceId, closeConn, mi.getMenuParentCode()));
		}
		return menuInfoService.saveOrUpMenu(sourceId, closeConn, mi);
	}
	/**
	 * 删除菜单信息
	 */
	public boolean deleteMenu(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String menuUUID = request.getParameter("menuUUID");
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return false;
		}
		return menuInfoService.deleteMenu(sourceId, closeConn, menuUUID);
	}
	/**
	 * 查菜单信息
	 */
	public String findMenu(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String menuUUID = request.getParameter("menuUUID");
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return "[]";
		}
		MenuInfo menu = (MenuInfo)menuInfoService.findMenu(sourceId, closeConn, menuUUID);
		if(menu==null){
			return "[]";
		}
		return new JsonRender().render(menu).toString();
	}
	/**
	 * 查菜单信息列表
	 */
	public String findMenuList(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {		
		Page page = new Page();
		page.setPageRecord(Integer.parseInt(request.getParameter("rows")));
		page.setPage(Integer.parseInt(request.getParameter("page")));
		Map<String,String> findMap = new HashMap<String, String>();
		findMap.put("menuName",request.getParameter("menuName"));
		findMap.put("menuUrl",request.getParameter("menuUrl"));
		List<MenuInfo> menuList = (List<MenuInfo>)menuInfoService.findMenuList(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		map.put("total", page.getTotalRecord());
		map.put("rows", menuList);
		if (menuList == null) {
			return "[]";
		} else {
			return JsonUtil.ObjToJson(map);
		}
	}	
	/**
	 * 保存或修改菜单角色信息（为角色分配菜单）
	 */
	public boolean saveOrUpMenuRole(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<MenuRole> mrList = new ArrayList<MenuRole>();
		String menuCodes = request.getParameter("menuCodes");
		String roleUUIDs = request.getParameter("roleUUIDs");
		if(menuCodes==null || "null".equals(menuCodes) || "".equals(menuCodes) || roleUUIDs==null || "".equals(roleUUIDs) || "null".equals(roleUUIDs)){
			return false;
		}
		String[] menuCodeArr = menuCodes.split(",");
		String[] roleUUIDArr = roleUUIDs.split(",");
		for(int i=0;i<menuCodeArr.length;i++){
			for(int j=0;j<roleUUIDArr.length;j++){
				MenuRole mr = new MenuRole();
				mr.setMenuUUID(menuCodeArr[i]);
				mr.setRoleUUID(roleUUIDArr[j]);
				mrList.add(mr);
			}
		}
		for(String roleUUID: roleUUIDArr){
			menuRoleService.deleteMenuRole(sourceId, closeConn, roleUUID, null);
		}
		return menuRoleService.saveMenuRole(sourceId, closeConn, mrList);
	}
	/**
	 * 删除角色菜单关联信息(roleUUID,menuUUIDS)
	 */
	public boolean deleteMenuRoleByRoleUUID(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleUUID = request.getParameter("roleUUID");
		String menuUUIDS = request.getParameter("menuUUIDS");
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return false;
		}
		if(menuUUIDS==null || "".equals(menuUUIDS)){
			return false;
		}
		String[] menuUUIDArr = menuUUIDS.split(",");
		for(String menuUUID:menuUUIDArr){
			menuRoleService.deleteMenuRole(sourceId, closeConn, roleUUID, menuUUID);
		}
		return true;
	}
	
	/**
	 * 通过角色UUID查角色关联菜单关联信息
	 */
	public String findMenuByRoleUUID(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleUUID = request.getParameter("roleUUID");
		Map findMap = new HashMap();
		List<MenuInfo> menuListAll = (List<MenuInfo>)menuInfoService.findMenuList(sourceId, closeConn,null,findMap);
		List<MenuInfo> menuList = (List<MenuInfo>)menuRoleService.findMenuByRoleUUID(sourceId, closeConn,roleUUID);
		if(menuListAll==null){
			return null;
		}else{
			MenuTree mt = new MenuTree();
			String[] isCheckGroupids = null;
			if(menuList != null && menuList.size() > 0){
				isCheckGroupids = new String[menuList.size()];
				for(int i=0;i<menuList.size();i++){
					isCheckGroupids[i] = ((MenuInfo)menuList.get(i)).getMenuUUID();
				}
			}
			return JsonUtil.ObjToJson(mt.createTreeJson(menuListAll, isCheckGroupids, false, "-1"));
		}
	}
	
	/**
	 * 为用户分配菜单
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean saveMenuUser(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userUUID = request.getParameter("userUUID");//用户uuid
		String menus = request.getParameter("menuUUIDS");//菜单uuid列表
		if(userUUID == null || "null".equals(userUUID) || "".equals(userUUID)){//修改当前用户权限菜单
			return false;
		}
		// 删原来的权限 
		menuUserService.deleteMenuUser(sourceId, closeConn, null, userUUID);
		if(menus != null && !"".equals(menus)){
			String[] menusList = menus.split(",");
			MenuUser menuUser = new MenuUser();
			for (String menuUUID : menusList) {
				menuUser.setUserUUID(userUUID);
				menuUser.setMenuUUID(menuUUID);
				menuInfoService.saveMenuUser(sourceId, closeConn, menuUser);
			}
		}
		return true;
	}
	/**
	 * 删除用户菜单权限
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean deleteMenuUser(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userUUID = request.getParameter("userUUID");
		String menuUUIDS = request.getParameter("menuUUIDS");
		if(userUUID==null || "".equals(userUUID) || "null".equals(userUUID)){
			return false;
		}
		if(menuUUIDS==null || "".equals(menuUUIDS)){
			return false;
		}
		String[] menuUUIDArr = menuUUIDS.split(",");
		for(String menuUUID:menuUUIDArr){
			menuUserService.deleteMenuUser(sourceId, closeConn, menuUUID, userUUID);
		}
		return true;
	}
	
	/**
	 * 通过用户UUID查用户关联菜单信息
	 */
	public String findMenuByUserUUID(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userUUID = request.getParameter("userUUID");		
		List<MenuInfo> menuListAll = (List<MenuInfo>)menuInfoService.findMenuList(sourceId, closeConn,null,null);
		List<MenuInfo> menuList = (List<MenuInfo>)menuUserService.findMenuByUserUUID(sourceId, closeConn,userUUID);
		if(menuListAll==null){
			return null;
		}else{
			MenuTree mt = new MenuTree();
			String[] isCheckGroupids = null;
			if(menuList != null && menuList.size() > 0){
				isCheckGroupids = new String[menuList.size()];
				for(int i=0;i<menuList.size();i++){
					isCheckGroupids[i] = ((MenuInfo)menuList.get(i)).getMenuUUID();
				}
			}
			return JsonUtil.ObjToJson(mt.createTreeJson(menuListAll, isCheckGroupids, false, "-1"));
		}
	}
	/**
	 * 查询权限菜单
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String findMenuTree(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		Map<String,String> findMap = new HashMap<String, String>();
		findMap.put("menuName",request.getParameter("userQuery"));
		findMap.put("menuUrl",request.getParameter("userQuery"));
		List<MenuInfo> menuList = (List<MenuInfo>)menuInfoService.findMenuList(sourceId, closeConn, null, findMap);
		if(menuList==null){
			return "[]";
		}else{
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for(MenuInfo menu :menuList){
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("id", menu.getMenuCode());
				attributes.put("name", menu.getMenuName());
				attributes.put("pId", menu.getMenuParentCode());
				attributes.put("open", "false");// 默认打开树
				attributes.put("nocheck", true);//是否没有选择框
				attributes.put("checked", false);//是否选中
				attributes.put("menuUUID", menu.getMenuUUID());//菜单UUID
				attributes.put("menuCode", menu.getMenuCode());//
				attributes.put("menuName", menu.getMenuName());//
				attributes.put("menuParentCode", menu.getMenuParentCode());//
				attributes.put("menuPermission", menu.getMenuPermission());//菜单权限名称
				attributes.put("menuUrl", menu.getMenuUrl());//菜单地址
				attributes.put("menuType", menu.getMenuType());//菜单类型
				attributes.put("menuHaveLowerNode", menu.getMenuHaveLowerNode());//是否有下级节点
				attributes.put("ecUUID", null);//ecUUID
				mapList.add(attributes);
			}
			return JsonUtil.ObjToJson(mapList);
		}
	}
}
