package com.xryb.zhtc.action;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

import com.xryb.zhtc.manage.VipMng;
/**
 * 会员action
 * @author wf
 */
public class VipAction implements ISparkApplication {

	@Auto(name=VipMng.class)
	private VipMng vipMng;
	
	@Override
	public void run() {
		//pc端
		/**
		 * pc端会员登陆(通过登录名和密码)
		 */
		Spark.post(new Route("/system/vip/vipLoginByPc", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.vipLoginByPc("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 会员信息修改
		 */
		Spark.post(new Route("/system/vip/vipUpdate", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.vipUpdate("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 修改会员密码
		 */
		Spark.post(new Route("/system/vip/upPwd", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.upPwd("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 修改会员状态
		 */
		Spark.post(new Route("/system/vip/upStatus", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.upStatus("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 会员注销
		 */
		Spark.post(new Route("/system/vip/delVip", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.delVip("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询会员列表
		 */
		Spark.post(new Route("/system/vip/findVipPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findVipPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询分销人员列表
		 */
		Spark.post(new Route("/system/vip/findDistributePage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findDistributePage("jdbcread", false, request.raw(), response.raw());
			}
		});
		//移动端
		/**
		 * 通过code获取openid
		 */
		Spark.get(new Route("/mobile/vip/code2session") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.code2session(request.raw());
			}
		});
		/**
		 * 会员注册
		 */
		Spark.post(new Route("/mobile/vip/vipLogin", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.vipLogin("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 通过openid获取会员信息
		 */
		Spark.post(new Route("/mobile/vip/findVipByOpenId", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.findVipByOpenId("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 会员头像上传
		 */
		Spark.post(new Route("/mobile/vip/fileupload") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.fileupload(request.raw());
			}
		});
		/**
		 * 会员信息修改
		 */
		Spark.post(new Route("/mobile/vip/vipUpdate", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.vipUpdate("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询会员列表
		 */
		Spark.post(new Route("/mobile/vip/findVipPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findVipPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询分销人员列表
		 */
		Spark.post(new Route("/mobile/vip/findDistributePage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findDistributePage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 银行卡图片上传
		 */
		Spark.post(new Route("/mobile/bankcard/upBankcardImg") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.upBankcardImg(request.raw());
			}
		});
		/**
		 * 会员银行卡绑定
		 */
		Spark.post(new Route("/mobile/bankcard/saveBankcard", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return vipMng.saveBankcard("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 会员银行卡解除绑定
		 */
		Spark.post(new Route("/mobile/bankcard/delBankcard", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.delBankcard("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询会员银行卡
		 */
		Spark.post(new Route("/system/bankcard/findBankcard", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findBankcard("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询会员银行卡列表
		 */
		Spark.post(new Route("/mobile/bankcard/findBankcardList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findBankcardList("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询会员银行卡列表
		 */
		Spark.post(new Route("/mobile/bankcard/findBankcardPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return vipMng.findBankcardPage("jdbcread", false, request.raw(), response.raw());
			}
		});
	}

}