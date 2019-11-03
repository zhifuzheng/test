package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.GxMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

/**
 * 供需商品菜单分类
 * @author Administrator
 *
 */

public class GxAction implements ISparkApplication {
	@Auto(name = GxMng.class)
	private GxMng gxMng;

	@Override
	public void run() {
		/**
		 * 新增或修改菜单分类
		 */
		Spark.post(new Route("/Public/gx/menuSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return gxMng.menuSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询专场分类菜单
		 */
		Spark.post(new Route("/Public/gx/findMenuTree",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return gxMng.findMenuTree("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 根据ID查专场分类菜单信息
		 */
		Spark.get(new Route("/Public/gx/findMenu",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return gxMng.findMenu("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除专场菜单信息
		 */
		Spark.post(new Route("/Public/gx/delMenu",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return gxMng.delMenu("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询menuParentCode为-1
		 */
		Spark.post(new Route("/Public/gx/menuParentCodeAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return gxMng.menuParentCodeAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
	}

}
