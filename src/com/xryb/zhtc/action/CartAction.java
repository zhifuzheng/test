package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.CartMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

/**
 * 购物车action
 * @author wf
 */
public class CartAction implements ISparkApplication {

	@Auto(name=CartMng.class)
	private CartMng cartMng;

	@Override
	public void run() {
		/**
		 * 向购物车中添加商品规格
		 */
		Spark.post(new Route("/mobile/cart/addCart", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return cartMng.addCart("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 移除购物车中的商品规格
		 */
		Spark.post(new Route("/mobile/cart/delCart", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return cartMng.delCart("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 设置购物车中的商品规格数量
		 */
		Spark.post(new Route("/mobile/cart/setCart", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return cartMng.setCart("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 展示购物车中的商品规格列表
		 */
		Spark.post(new Route("/mobile/cart/findCart", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return cartMng.findCart("jdbcread", false, request.raw(), response.raw());
			}
		});
	}
	
}
