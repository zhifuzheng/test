package com.xryb.zhtc.action;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

import com.xryb.zhtc.manage.MenuRoleMng;
import com.xryb.zhtc.util.CacheUtil;
import com.xryb.zhtc.util.JsonUtil;
/**
 * 菜单与角色关联的action
 * @author 
 *
 */
public class MenuRoleAction implements ISparkApplication {

	@Auto(name=MenuRoleMng.class)
	private MenuRoleMng menuRoleMng;
	
	@Override
	public void run() {
		/**
		 * 保存菜单角色关联信息
		 */
		Spark.post(new Route("/system/menuRole/saveMenuRole", true, "jdbcwrite"){
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String token = request.raw().getSession().getAttribute("token").toString();
				Map<String, String> resulrMap = new HashMap<String, String>();
				if (token == null) {
					resulrMap.put("status", "-12");
					resulrMap.put("msg", "未获取令牌信息！");
					return JsonUtil.ObjToJson(resulrMap);
				}
//				Map<String,Object> map = (Map<String,Object>) CacheUtil.getInstance().getTokenObject(token); 
//				if (map == null) {
//					// 令牌取不到数据，说明没有登陆，或操作过期。
//					resulrMap.put("status", "-10");
//					resulrMap.put("msg", "未获取令牌信息对象！");
//					return JsonUtil.ObjToJson(resulrMap);
//				}
				
				return menuRoleMng.addPermissionForRole("jdbcwrite", false, request.raw(), response.raw());
				
			}
			
		});
		/**
		 * 查询角色对应的所有权限
		 */
		Spark.post(new Route("/system/menuRole/findMenuToAddPermission", true, "jdbcread"){
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String token = request.raw().getSession().getAttribute("token").toString();
				Map<String, String> resulrMap = new HashMap<String, String>();
				if (token == null) {
					resulrMap.put("status", "-12");
					resulrMap.put("msg", "未获取令牌信息！");
					return JsonUtil.ObjToJson(resulrMap);
				}
				Map<String,Object> map = (Map<String,Object>) CacheUtil.getInstance().getTokenObject(token);
				if (map == null) {
					// 令牌取不到数据，说明没有登陆，或操作过期。
					resulrMap.put("status", "-10");
					resulrMap.put("msg", "未获取令牌信息对象！");
					return JsonUtil.ObjToJson(resulrMap);
				}else{
					return menuRoleMng.findAllMenuItemToAddPermission("jdbcread", false, request.raw(), response.raw());
				}
			}
			
		});
		
	}

}
