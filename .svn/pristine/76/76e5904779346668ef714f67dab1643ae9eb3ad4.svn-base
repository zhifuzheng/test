package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xryb.zhtc.entity.GxMenu;
import com.xryb.zhtc.service.IGxMenuService;
import com.xryb.zhtc.service.impl.GxMenuServiceImpl;
import com.xryb.zhtc.util.JsonUtil;

import spark.annotation.Auto;
import spark.render.JsonRender;
import spark.utils.ObjectUtil;

/**
 * 供需商品菜单分类
 * @author Administrator
 *
 */

public class GxMng {
	@Auto(name=GxMenuServiceImpl.class)
	private IGxMenuService iGxMenuService;
	
	/**
	 * 新增或修改菜单分类
	 */
	public boolean menuSave(String sourceId,boolean closeConn,HttpServletRequest request, HttpServletResponse response) {
		GxMenu gxMenu = (GxMenu)ObjectUtil.req2Obj(request, GxMenu.class);
		if(gxMenu == null) {
			return false;
		}
		if(gxMenu.getId()==null) {//新增
			//设置UUID
			gxMenu.setMenuUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			//获得节点编号
			gxMenu.setMenuCode(iGxMenuService.getMenuCodeNext(sourceId, closeConn, gxMenu.getMenuParentCode()));
		}
		return iGxMenuService.menuSave(sourceId, closeConn, gxMenu);
	}
	
	/**
	 * 查询专场分类菜单
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
		List<GxMenu> menuList = (List<GxMenu>)iGxMenuService.findMenuList(sourceId, closeConn, null, findMap);
		if(menuList==null){
			return "[]";
		}else{
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for(GxMenu menu :menuList){
				Map<String,Object> attributes = new HashMap<String,Object>();
				attributes.put("id", menu.getMenuCode());
				attributes.put("name", menu.getMenuName());
				attributes.put("pId", menu.getMenuParentCode());
				attributes.put("open", "true");// 默认打开树
				attributes.put("nocheck", true);//是否没有选择框
				attributes.put("checked", false);//是否选中
				attributes.put("menuUUID", menu.getMenuUUID());//菜单UUID
				attributes.put("menuCode", menu.getMenuCode());//
				attributes.put("menuName", menu.getMenuName());//
				attributes.put("menuParentCode", menu.getMenuParentCode());//
				attributes.put("menuType", menu.getMenuType());//菜单类型
				attributes.put("menuHaveLowerNode", menu.getMenuHaveLowerNode());//是否有下级节点
				attributes.put("ecUUID", null);//ecUUID
				mapList.add(attributes);
			}
			return JsonUtil.ObjToJson(mapList);
		}
	}
	
	/**
	 * 根据ID查专场分类菜单信息
	 */
	public String findMenu(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String menuUUID = request.getParameter("menuUUID");
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return "[]";
		}
		GxMenu menu = (GxMenu)iGxMenuService.findMenu(sourceId, closeConn, menuUUID);
		if(menu==null){
			return "[]";
		}
		return new JsonRender().render(menu).toString();
	}
	
	/**
	 * 删除专场菜单信息
	 */
	public boolean delMenu(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String menuUUID = request.getParameter("menuUUID");
		if(menuUUID == null) {
			return false;
		}
		return iGxMenuService.delMenu(sourceId, closeConn, menuUUID);
	}
	
	/**
	 * 查询menuParentCode为-1
	 */
	public String menuParentCodeAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String menuParentCode = request.getParameter("menuParentCode");
		List<GxMenu> list = iGxMenuService.menuParentCodeAll(sourceId, closeConn, menuParentCode);
		return JsonUtil.ObjToJson(list);
	}

}
