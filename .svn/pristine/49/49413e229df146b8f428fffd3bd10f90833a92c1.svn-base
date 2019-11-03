package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.MenuRole;
import com.xryb.zhtc.service.IMenuRoleService;
import com.xryb.zhtc.service.impl.MenuRoleServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
/**
 * 权限菜单与角色关联 管理
 * @author chengyz
 *
 */
public class MenuRoleMng {
	
	@Auto(name=MenuRoleServiceImpl.class)
	private IMenuRoleService roleMenuService;
	/**
	 * 给角色分配权限
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String addPermissionForRole(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String roleUUID = request.getParameter("roleUUID");
		String menuUUIDS = request.getParameter("menuUUIDS");//选择的菜单参数
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNullOrEmpty(roleUUID) || StringUtils.isNullOrEmpty(menuUUIDS)){
			map.put("status", "-1");
			map.put("msg", "未获取到任何信息");
			return JsonUtil.ObjToJson(map);
		}
		roleMenuService.deleteMenuRole(sourceId, closeConn, roleUUID, null);
		String[] menusList = menuUUIDS.split(",");
		List<MenuRole> menuList = new ArrayList<MenuRole>();
		for (String menuUUID : menusList) {
			MenuRole menuRole = new MenuRole();
			menuRole.setRoleUUID(roleUUID.toString().replaceAll("-", ""));
			menuRole.setMenuUUID(menuUUID.toString().replaceAll("-", ""));
			menuList.add(menuRole);
		}
		if (roleMenuService.saveMenuRole(sourceId, closeConn, menuList)){
			map.put("status", "1");
			map.put("msg", "权限分配成功");
			return JsonUtil.ObjToJson(map);
		}else{
			map.put("status", "0");
			map.put("msg", "权限分配失败");
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 查询角色拥有的菜单权限
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String findAllMenuItemToAddPermission(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String roleUUID = request.getParameter("roleUUID");
		if (StringUtils.isNullOrEmpty(roleUUID)){
			return JsonUtil.ObjToJson("[]");
		}
		List<Map> listMap = roleMenuService.findAllMenuItemForRole(sourceId, closeConn, roleUUID);
		if (listMap!=null){
			return JsonUtil.ObjToJson(listMap);
		}else{
			return JsonUtil.ObjToJson("[]");
		}
	}
}
