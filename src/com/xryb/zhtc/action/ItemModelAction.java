package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.ItemModelMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 商品规格模板action
 * @author wf
 */
public class ItemModelAction implements ISparkApplication {
	
	@Auto(name=ItemModelMng.class)
	private ItemModelMng modelMng;

	@Override
	public void run() {
		
		//pc端
		/**
		 * 添加或修改商品模板
		 */
		Spark.post(new Route("/shop/itemModel/saveOrUpModel", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return modelMng.saveOrUpModel("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据modelUUID修改模板状态
		 */
		Spark.post(new Route("/shop/itemModel/upModelEnable", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return modelMng.upModelEnable("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据modelUUID删除商品模板
		 */
		Spark.post(new Route("/shop/itemModel/delModel", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.delModel("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据modelUUID查询商品模板
		 */
		Spark.post(new Route("/shop/itemModel/findModel", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.findModel("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询商品模板列表
		 */
		Spark.post(new Route("/shop/itemModel/findModelPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.findModelPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据catUUID查询商品模板列表
		 */
		Spark.post(new Route("/shop/itemModel/findListByCatUUID", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.findListByCatUUID("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询指定分类的商品模板排序列表
		 */
		Spark.post(new Route("/shop/itemModel/getSortList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.getSortList("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		//移动端
		/**
		 * 添加或修改商品模板
		 */
		Spark.post(new Route("/mobile/itemModel/saveOrUpModel", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return modelMng.saveOrUpModel("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据modelUUID删除商品模板
		 */
		Spark.post(new Route("/mobile/itemModel/delModel", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.delModel("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据modelUUID查询商品模板
		 */
		Spark.post(new Route("/mobile/itemModel/findModel", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.findModel("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据catUUID查询商品模板列表
		 */
		Spark.post(new Route("/mobile/itemModel/findListByCatUUID", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.findListByCatUUID("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询指定分类的商品模板排序列表
		 */
		Spark.post(new Route("/mobile/itemModel/getSortList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return modelMng.getSortList("jdbcread", false, request.raw(), response.raw());
			}
		});
	}

}
