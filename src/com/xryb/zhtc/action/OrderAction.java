package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.OrderMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 订单action
 * @author wf
 */
public class OrderAction implements ISparkApplication {
	
	@Auto(name=OrderMng.class)
	private OrderMng orderMng;

	@Override
	public void run() {
		//pc端
		/**
		 * 分页查询订单信息
		 */
		Spark.post(new Route("/shop/order/findOrderPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findOrderPageByPc("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询订单项信息
		 */
		Spark.post(new Route("/shop/orderItem/findOrderItemPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findOrderItemPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 删除评论
		 */
		Spark.post(new Route("/shop/comment/delComment", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.delComment("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 修改评论状态
		 */
		Spark.post(new Route("/shop/comment/upComment", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.upComment("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询评论信息
		 */
		Spark.post(new Route("/shop/comment/findCommentPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findCommentPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 卖家接单
		 */
		Spark.post(new Route("/shop/order/receive", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.receive("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 驳回退款
		 */
		Spark.post(new Route("/mobile/order/rejectRefund", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.rejectRefund("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 驳回退货
		 */
		Spark.post(new Route("/mobile/order/rejectReturn", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.rejectReturn("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 删除订单
		 */
		Spark.post(new Route("/shop/order/delOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.delOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		//移动端
		/**
		 * 生成购物订单
		 */
		Spark.post(new Route("/mobile/order/genOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.genOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 生成充值订单
		 */
		Spark.post(new Route("/mobile/order/genRechargeOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.genRechargeOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 生成平台订单
		 */
		Spark.post(new Route("/mobile/order/genPlatOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.genPlatOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 删除订单
		 */
		Spark.post(new Route("/mobile/order/delOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.delOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 取消订单
		 */
		Spark.post(new Route("/mobile/order/abolishOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.abolishOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 订单超时
		 */
		Spark.post(new Route("/mobile/order/timeoutOrder", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.timeoutOrder("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 发起退款
		 */
		Spark.post(new Route("/mobile/order/applyRefund", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.applyRefund("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 发起退货
		 */
		Spark.post(new Route("/mobile/order/applyReturn", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.applyReturn("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 确认收货
		 */
		Spark.post(new Route("/mobile/order/receipt", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.receipt("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 移动端分页查询订单信息
		 */
		Spark.post(new Route("/mobile/order/findOrderPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findOrderPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 移动端分页查询订单项信息
		 */
		Spark.post(new Route("/mobile/order/findOrderItemPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findOrderItemPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据条件查询订单信息
		 */
		Spark.post(new Route("/mobile/order/findOrder", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findOrder("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 评论图片上传
		 */
		Spark.post(new Route("/mobile/comment/fileupload", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.fileupload("jdbcread", false, request.raw());
			}
		});
		/**
		 * 新增评论
		 */
		Spark.post(new Route("/mobile/comment/addComment", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.addComment("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询评论信息
		 */
		Spark.post(new Route("/mobile/comment/findCommentPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.findCommentPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 卖家接单
		 */
		Spark.post(new Route("/mobile/order/receive", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return orderMng.receive("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 驳回退款
		 */
		Spark.post(new Route("/mobile/order/rejectRefund", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.rejectRefund("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 驳回退货
		 */
		Spark.post(new Route("/mobile/order/rejectReturn", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return orderMng.rejectReturn("jdbcwrite", false, request.raw(), response.raw());
			}
		});
	}

}
