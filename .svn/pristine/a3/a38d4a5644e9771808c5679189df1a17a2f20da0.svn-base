package com.xryb.zhtc.action;

import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.manage.RoleMng;
import com.xryb.zhtc.util.CacheUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
/**
 * 角色信息相关pc端调用接口
 * @author hshzh
 *
 */
public class RoleAction implements ISparkApplication{

	@Auto(name=RoleMng.class)
	private RoleMng roleMng;
	@Override
	public void run() {
		/**
		 * 保存或修改角色信息
		 */
		Spark.post(new Route("/system/role/saveOrUpRole", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = null;
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}
				return roleMng.saveOrUpRole("jdbcwrite", false, userinfo, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除角色信息
		 */
		Spark.post(new Route("/system/role/deleteRole", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = new UserInfo();
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}		
				return roleMng.deleteRole("jdbcwrite", false, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 查角色信息
		 */
		Spark.get(new Route("/system/role/findRole", false, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = new UserInfo();
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}				
				return roleMng.findRole("jdbcread", true, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 查角色信息列表
		 */
		Spark.post(new Route("/system/role/findRoleList", false, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = new UserInfo();
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}		
				return roleMng.findRoleList("jdbcread", true, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 保存角色用户关联关系
		 */
		Spark.post(new Route("/system/role/saveRoleUser", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = new UserInfo();
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}			
				return roleMng.saveRoleUser("jdbcwrite", false, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 删除角色用户关联关系
		 */
		Spark.post(new Route("/system/role/deleteRoleUser", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = null;
				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
				if (map != null) {
					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
				}			
				return roleMng.deleteRoleUser("jdbcwrite", false, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 通过角色UUID查关联的用户信息列表
		 */
		Spark.post(new Route("/system/role/findUserByRoleUUID", false, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = null;
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}			
				return roleMng.findUserByRoleUUID("jdbcread", true, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 通过用户UUID查关联的角色信息列表
		 */
		Spark.post(new Route("/system/role/findRoleByUserUUID", false, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = null;
//				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
//				if (map != null) {
//					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
//				}			
				return roleMng.findRoleByUserUUID("jdbcread", true, userinfo, request.raw(), response.raw());
			}
		});
		/**
		 * 通过用户UUID查未关联的角色信息列表
		 */
		Spark.post(new Route("/system/role/findNotRoleByUserUUID", false, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String token = (String)request.raw().getSession().getId();
				UserInfo userinfo = null;
				Map map = (Map)JsonUtil.JsonToObj(CacheUtil.getInstance().getHttpCache("getTokenObject", token),Map.class);	
				if (map != null) {
					userinfo = (UserInfo)ReqToEntityUtil.MapToEntity((Map)map.get("userinfo"), UserInfo.class);
				}				
				return roleMng.findNotRoleByUserUUID("jdbcread", true, userinfo, request.raw(), response.raw());
			}
		});

	}

}
