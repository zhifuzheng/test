package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.WeiXinPayMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 微信支付action
 * @author wf
 */
public class WeiXinPayAction implements ISparkApplication {
	
	@Auto(name = WeiXinPayMng.class)
	private WeiXinPayMng weiXinPayMng;

	@Override
	public void run() {
		//pc端
		/**
		 * 同意退款
		 */
		Spark.post(new Route("/shop/weiXinPay/agreeRefund", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return weiXinPayMng.agreeRefund("jdbcwrite", false, request.raw());
			}
		});
		/**
		 * 同意退货
		 */
		Spark.post(new Route("/shop/weiXinPay/agreeReturn", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return weiXinPayMng.agreeReturn("jdbcwrite", false, request.raw());
			}
		});
		
		//移动端
		/**
		 * 微信支付返回预支付数据
		 */
		Spark.post(new Route("/mobile/weiXinPay/preOrder",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return weiXinPayMng.preOrder("jdbcwrite", false, request.raw(), response.raw());
			}			
		});
		/**
		 * 微信支付回调
		 */
		Spark.post(new Route("/mobile/weiXinPay/notify",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
			    return weiXinPayMng.notify("jdbcwrite", false, request.raw(), response.raw());
			}		
		});
		/**
		 * 同意退款
		 */
		Spark.post(new Route("/mobile/weiXinPay/agreeRefund", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return weiXinPayMng.agreeRefund("jdbcwrite", false, request.raw());
			}
		});
		/**
		 * 同意退货
		 */
		Spark.post(new Route("/mobile/weiXinPay/agreeReturn", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return weiXinPayMng.agreeReturn("jdbcwrite", false, request.raw());
			}
		});
	}

}
