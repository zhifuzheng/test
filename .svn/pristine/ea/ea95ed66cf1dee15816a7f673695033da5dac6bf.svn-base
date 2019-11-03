package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.ItemCatMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 商品分类action
 * @author wf
 */
public class ItemCatAction implements ISparkApplication {
	
	@Auto(name=ItemCatMng.class)
	private ItemCatMng catMng;

	@Override
	public void run() {
		
		//pc端
		/**
		 * 新增分类
		 */
		Spark.post(new Route("/shop/itemCat/addItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return catMng.addItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，删除当前分类及所有子分类
		 */
		Spark.post(new Route("/shop/itemCat/delItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.delItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，更新分类名称
		 */
		Spark.post(new Route("/shop/itemCat/upItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.upItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，启用或停用当前分类及所有子类
		 */
		Spark.post(new Route("/shop/itemCat/enableItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.enableItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 拖拽分类进行排序
		 */
		Spark.post(new Route("/shop/itemCat/dragItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.dragItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过pUUID，查询分类列表
		 */
		Spark.get(new Route("/shop/itemCat/findChildrenByParent", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.findChildrenByParent("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		//移动端
		/**
		 * 新增分类
		 */
		Spark.post(new Route("/mobile/itemCat/addItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return catMng.addItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，删除当前分类及所有子分类
		 */
		Spark.post(new Route("/mobile/itemCat/delItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.delItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，更新分类名称
		 */
		Spark.post(new Route("/mobile/itemCat/upItemCat", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.upItemCat("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过catUUID，查询子分类列表
		 */
		Spark.post(new Route("/mobile/itemCat/getCatList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return catMng.getCatList("jdbcread", false, request.raw(), response.raw());
			}
		});
	}
	
}
