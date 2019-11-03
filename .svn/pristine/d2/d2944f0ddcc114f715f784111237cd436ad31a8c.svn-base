package com.xryb.zhtc.action;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

import com.xryb.zhtc.manage.MenuUserMng;
import com.xryb.zhtc.util.CacheUtil;
import com.xryb.zhtc.util.JsonUtil;
/**
 * 菜单用户关联的action
 * @author hshzh
 *
 */
public class MenuUserAction implements ISparkApplication {

	@Auto(name=MenuUserMng.class)
	private MenuUserMng menuUserMng;
	
	@Override
	public void run() {
		/**
		 * 添加或修改菜单与用户关联信息
		 */
		Spark.post(new Route("/system/menuUser/saveMenuUser", true, "jdbcwrite"){
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String token = (String)request.raw().getSession().getId();
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
					return menuUserMng.addPermissionForUser("jdbcwrite", false, request.raw(), response.raw());
				}
			}
			
		});
		/**
		 * 查询用户所拥有的所有权限
		 */
		Spark.post(new Route("/system/menuUser/findMenuToAddPermission", true, "jdbcread"){
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
				String token = (String)request.raw().getSession().getId();
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
					return menuUserMng.findAllMenuItemToAddPermission("jdbcread", false, request.raw(), response.raw());
				}
			}
			
		});
	}

}
