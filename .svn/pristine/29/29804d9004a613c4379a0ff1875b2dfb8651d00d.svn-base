package com.xryb.zhtc.action;


import com.xryb.zhtc.manage.CouponMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 优惠劵action
 */
public class CouponAction implements ISparkApplication{
	@Auto(name=CouponMng.class)
	private CouponMng couponMng;
	
	
	@Override
	public void run() {
		/**
		 * 商品详情、购物车，获取店铺可领取的优惠劵列表
		 */
		Spark.post(new Route("/mobile/coupon/getEntityCoupon",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getEntityCoupon("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 用户领取优惠劵
		 */
		Spark.post(new Route("/mobile/coupon/userGetCoupon",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.UserGetCoupon("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取个人用户领取的优惠劵列表(我的卡劵)
		 */
		Spark.post(new Route("/mobile/coupon/getUserCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getUserCouponListMobile("jdbcread",true,request.raw(),response.raw());
			}
		});	
		
		/**
		 * 获取用户领取的对应店铺的可用的优惠劵列表(提交订单)
		 */
		Spark.post(new Route("/mobile/coupon/getUserEntityCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getUserEntityCouponList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 优惠劵立即使用获取对应商品列表
		 */
		Spark.post(new Route("/mobile/coupon/getCouponGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getCouponGoodsList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户领取的满足使用条件的优惠劵列表(订单提交)
		 */
		Spark.post(new Route("/mobile/coupon/getUserIsUsedCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getUserIsUsedCouponList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 用户点击选择相关使用优惠劵后计算订单小记和该单每个商品优惠金额
		 */
		Spark.post(new Route("/mobile/coupon/returnPerGoodsPrice",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.returnPerGoodsPrice("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 领券中心获取可领取优惠劵列表
		 */
		Spark.post(new Route("/mobile/coupon/getAllIsGetCouponList",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getAllIsGetCouponList("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 线下优惠劵扫码进入，根据优惠劵UUID获取相关信息即操作
		 */
		Spark.post(new Route("/mobile/coupon/getOfflineCouponData",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getOfflineCouponData("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		
		
		//* **************************************************************PC端请求************************************************************************* */
		
		/**
		 * 创建优惠劵
		 */
		Spark.post(new Route("/system/coupon/createCoupon",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.createCoupon("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 获取零售店铺列表
		 */
		Spark.post(new Route("/system/coupon/getBusinessList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getBusinessList("jdbcread", true, request.raw(), response.raw());
			}
		});
		
		/**
		 * 编辑保存优惠劵
		 */
		Spark.post(new Route("/system/coupon/editorSaveCoupon",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.editorSaveCoupon("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 获取发行商所属商品列表
		 */
		Spark.post(new Route("/system/coupon/getGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getGoodsList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取店铺的商品分类列表
		 */
		Spark.post(new Route("/system/coupon/getGoodsSortList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getGoodsSortList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取指定分类的信息列表
		 */
		Spark.post(new Route("/system/coupon/getSortInfoList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getSortInfoList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取指定的商品信息列表
		 */
		Spark.post(new Route("/system/coupon/getGoodsInfoList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getGoodsInfoList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		
		/**
		 * 获取发行商优惠劵列表
		 */
		Spark.post(new Route("/system/coupon/findCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getCouponList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取平台优惠劵列表
		 */
		Spark.post(new Route("/system/coupon/platformFindCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.platformFindCouponList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 发行商删除指定批次优惠劵(只能删除未上架的优惠劵)
		 */
		Spark.post(new Route("/system/coupon/deleteCoupons",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.deleteCoupons("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 上架指定优惠劵
		 */
		Spark.post(new Route("/system/coupon/putawayCoupons",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.putawayCoupons("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 下架指定优惠劵
		 */
		Spark.post(new Route("/system/coupon/downCoupons",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.downCoupons("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 用户获取已领取的优惠劵列表
		 */
		Spark.post(new Route("/system/coupon/getUserCouponList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getUserCouponList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 *  用户删除自己领取的优惠劵(暂时不能让用户删除自己已领取的优惠劵)
		 */
		Spark.post(new Route("/system/coupon/deleteUserCoupons",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.deleteUserCoupons("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 *  商家和平台优惠劵编辑获取优惠劵的详细信息
		 */
		Spark.post(new Route("/system/coupon/getCouponInfo",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getCouponInfo("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 下载指定批次和数量的可下载的优惠劵二维码图片（线下优惠劵）
		 */
		Spark.post(new Route("/system/coupon/getCouponQRcodeImgList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getCouponQRcodeImgList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		
		/**
		 * 获取登陆用户的所有店铺信息(如是平台则查询所有店铺，只查询零售商)
		 */
		Spark.post(new Route("/system/coupon/getUserEntityInfo",true,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getUserEntityInfo("jdbcread",false,request.raw(),response.raw());
			}
		});
			
		/**
		 * 获取店铺发布优惠劵的使用详情记录列表
		 */
		Spark.post(new Route("/system/coupon/getDetailCouponList",true,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return couponMng.getDetailCouponList("jdbcread",false,request.raw(),response.raw());
			}
		});
		
		
	}
	

}
