package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.MenuMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 权限菜单信息pc端调用接口
 * @author hshzh
 *
 */
public class MenuAction implements ISparkApplication{
	@Auto(name=MenuMng.class)
	private MenuMng menuMng;
	
	@Override
	public void run() {
		/**
		 * 保存或修改菜单权限信息
		 */
		Spark.post(new Route("/system/menu/saveOrUpMenu", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.saveOrUpMenu("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		
		/**
		 * 删除菜单信息
		 */
		Spark.post(new Route("/system/menu/deleteMenu", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.deleteMenu("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查菜单信息
		 */
		Spark.get(new Route("/system/menu/findMenu", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.findMenu("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查菜单信息列表
		 */
		Spark.post(new Route("/system/menu/findMenuList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.findMenuList("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 保存或修改菜单角色信息（为角色分配菜单）
		 */
		Spark.post(new Route("/system/menu/saveOrUpMenuRole", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.saveOrUpMenuRole("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除角色菜单关联信息(roleUUID,menuUUIDS)
		 */
		Spark.post(new Route("/system/menu/deleteMenuRoleByRoleUUID", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.deleteMenuRoleByRoleUUID("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 通过角色UUID查角色关联菜单关联信息
		 */
		Spark.get(new Route("/system/menu/findMenuByRoleUUID", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.findMenuByRoleUUID("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 为用户分配菜单
		 */
		Spark.post(new Route("/system/menu/saveMenuUser", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.saveMenuUser("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除用户菜单权限
		 */
		Spark.post(new Route("/system/menu/deleteMenuUser", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return menuMng.deleteMenuUser("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 通过用户UUID查用户关联菜单信息
		 */
		Spark.post(new Route("/system/menu/findMenuByUserUUID",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				return menuMng.findMenuByUserUUID("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询菜单树
		 */
		Spark.post(new Route("/system/menu/findMenuTree",true,"jdbcread"){
			@Override
			public Object handle(Request request, Response response)
					throws Exception {
					return menuMng.findMenuTree("jdbcread", false, request.raw(), response.raw());
			}
			
		});
	}

}
