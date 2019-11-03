package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.service.IMenuUserService;
import com.xryb.zhtc.service.impl.MenuUserServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
/**
 * 权限菜单和用户关联管理
 * @author chengyz
 *
 */
public class MenuUserMng {
	@Auto(name=MenuUserServiceImpl.class)
	private IMenuUserService userMenuService;
	/**
	 * 给用户分配或移除权限
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String addPermissionForUser(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String userUUID = request.getParameter("userUUID");
		String menuUUIDS = request.getParameter("menuUUIDS");//选择的菜单参数
		Map<String, String> map = new HashMap<String, String>();
		if((menuUUIDS == null || "".equals(menuUUIDS)) && !StringUtils.isNullOrEmpty(userUUID)) {
			userMenuService.deleteMenuUser(sourceId, closeConn, null, userUUID);//移除用户的权限
			map.put("status", "2");
			map.put("msg", "权限移除成功");
			return JsonUtil.ObjToJson(map);
		}
		if (!StringUtils.isNullOrEmpty(userUUID) && !StringUtils.isNullOrEmpty(menuUUIDS)){
			userMenuService.deleteMenuUser(sourceId, closeConn, null,userUUID);
			String[] menusList = menuUUIDS.split(",");
			List<MenuUser> menuList = new ArrayList<MenuUser>();
			for (String menuUUID : menusList) {
				MenuUser menuUser = new MenuUser();
				menuUser.setUserUUID(userUUID.toString());
				menuUser.setMenuUUID(menuUUID.toString());
				menuList.add(menuUser);
			}
			if (userMenuService.saveMenuUser(sourceId, closeConn, menuList)){
				map.put("status", "1");
				map.put("msg", "权限分配成功");
				return JsonUtil.ObjToJson(map);
			}else{
				map.put("status", "0");
				map.put("msg", "权限分配失败");
				return JsonUtil.ObjToJson(map);
			}
		}else{
			map.put("status", "0");
			map.put("msg", "权限分配失败");
			return JsonUtil.ObjToJson(map);
		}
		
	}
	/**
	 * 查询用户拥有的菜单权限
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String findAllMenuItemToAddPermission(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String userUUID = request.getParameter("userUUID");
		if (!StringUtils.isNullOrEmpty(userUUID)){
			List<Map> listMap = userMenuService.findAllMenuItemForUser(sourceId, closeConn, userUUID);
			if (listMap!=null){
				return JsonUtil.ObjToJson(listMap);
			}else{
				return JsonUtil.ObjToJson("[]");
			}
		}else{
			return JsonUtil.ObjToJson("[]");
		}
		
	}
}
