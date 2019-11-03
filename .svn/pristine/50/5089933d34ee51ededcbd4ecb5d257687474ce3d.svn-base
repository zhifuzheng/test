package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.ItemMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 商品action
 * @author wf
 */
public class ItemAction implements ISparkApplication {
	
	@Auto(name=ItemMng.class)
	private ItemMng itemMng;

	@Override
	public void run() {
		//pc端
		/**
		 * 商品图片上传
		 */
		Spark.post(new Route("/shop/item/fileupload") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.fileupload(request.raw());
			}
		});
		/**
		 * 添加商品
		 */
		Spark.post(new Route("/shop/item/addItem", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.addItem("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 添加或修改商品规格
		 */
		Spark.post(new Route("/shop/item/saveOrUpParam", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.saveOrUpParam("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据itemUUID修改商品
		 */
		Spark.post(new Route("/shop/item/upItem", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.upItem("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询商品列表
		 */
		Spark.post(new Route("/shop/item/findItemPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findItemPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询商品规格列表
		 */
		Spark.post(new Route("/shop/item/findParamPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findParamPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询当前登录用户的店铺列表
		 */
		Spark.post(new Route("/shop/item/findEntityList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findEntityList("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询所有店铺列表
		 */
		Spark.post(new Route("/shop/item/findAllEntity", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findAllEntity("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询零售商等级列表
		 */
		Spark.post(new Route("/shop/item/findGradeList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findGradeList("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		//移动端
		/**
		 * 商品图片上传
		 */
		Spark.post(new Route("/mobile/item/fileupload") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.fileupload(request.raw());
			}
		});
		/**
		 * 添加商品
		 */
		Spark.post(new Route("/mobile/item/addItem", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.addItem("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 添加或修改商品规格
		 */
		Spark.post(new Route("/mobile/item/saveOrUpParam", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return itemMng.saveOrUpParam("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据itemUUID修改商品
		 */
		Spark.post(new Route("/mobile/item/upItem", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.upItem("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询商品列表
		 */
		Spark.post(new Route("/mobile/item/findItemPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findItemPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询商品规格列表
		 */
		Spark.post(new Route("/mobile/item/findParamPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findParamPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据itemUUID查询商品规格列表
		 */
		Spark.post(new Route("/mobile/item/findParamList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findParamList("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据itemUUID查询商品信息
		 */
		Spark.post(new Route("/mobile/item/findItem", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findItem("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询零售商等级列表
		 */
		Spark.post(new Route("/mobile/item/findGradeList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return itemMng.findGradeList("jdbcread", false, request.raw(), response.raw());
			}
		});
	}

}
