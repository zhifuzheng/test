package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.IntegralMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 积分action
 * @author apple
 *
 */
public class IntegralAction implements ISparkApplication{
	@Auto(name=IntegralMng.class)
	private IntegralMng integralMng;

	@Override
	public void run() {
		/**
		 * 获取用户积分值
		 */
		Spark.post(new Route("/mobile/integral/getUserIntegral",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getUserIntegral("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 判断用户是否参与过完善资料优惠活动
		 */
		Spark.post(new Route("/mobile/integral/userIsJoined",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.userIsJoined("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 删除用户积分商城商品兑换订单记录
		 */
		Spark.post(new Route("/mobile/integral/delGoodsExRecord",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.delGoodsExRecord("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户积分兑换商品记录列表
		 */
		Spark.post(new Route("/mobile/integral/getIntegralExGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralExGoodsList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户积分详情记录(积分来源和消耗)
		 */
		Spark.post(new Route("/mobile/integral/getUserIntegralList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getUserIntegralList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 用户积分修改(用户充值成功，或者购买订单完成)
		 */
		Spark.post(new Route("/mobile/integral/changeIntegral",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.changeIntegral("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 按照积分规则计算应给予用户的积分数值
		 */
		Spark.post(new Route("/mobile/integral/calculateIntegral",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.calculateIntegral("jdbcread",true,request.raw(),response.raw());
			}
		});
		/**
		 * 获取积分商城添加的商品
		 */
		Spark.post(new Route("/mobile/integral/getIntegralGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getExIntegralGoodList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 积分商城生成积分商品兑换订单
		 */
		Spark.post(new Route("/mobile/integral/createIntegralGoodOrder",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.createIntegralGoodOrder("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 领取兑换的积分商城商品或官方活动商品
		 */
		Spark.post(new Route("/mobile/integral/getIntegralOrderGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralOrderGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取积分商品信息
		 */
		Spark.post(new Route("/mobile/integral/getIntegralGoodInfo",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralGoodInfo("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 手机平台添加积分商城商品
		 */
		Spark.post(new Route("/mobile/integral/addIntegralGoodofMobile",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.addIntegralGoodofMobile("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取积分商城已添加的商品
		 */
		Spark.post(new Route("/mobile/integral/getIntegralGoodListofMobile",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralGoodListofMobile("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台获取订单信息
		 */
		Spark.post(new Route("/mobile/integral/getGoodOrderList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getGoodOrderList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取平台活动商品
		 */
		Spark.post(new Route("/mobile/integral/getActivityGoodList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getActivityGoodList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取平台积分商城添加的商品
		 */
		Spark.post(new Route("/mobile/integral/getIntegralShopGoodList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralShopGoodList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台删除已领取和已过期订单
		 */
		Spark.post(new Route("/mobile/integral/platformDelOrder",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformDelOrder("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 *  平台删除积分商城商品
		 */
		Spark.post(new Route("/mobile/integral/platformDelGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformDelGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 下架积分商品
		 */
		Spark.post(new Route("/mobile/integral/downIntegralGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.downIntegralGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 上架积分商品
		 */
		Spark.post(new Route("/mobile/integral/releaseIntegralGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.releaseIntegralGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 用户获取订单信息
		 */
		Spark.post(new Route("/mobile/integral/getOrderInfo",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getOrderInfo("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 根据支付成功返回的订单查询后生成的积分商品订单uuid
		 */
		Spark.post(new Route("/mobile/integral/getOrderUUID",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getOrderUUID("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取官方活动
		 */
		Spark.post(new Route("/mobile/integral/getpaltformActiveList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getpaltformActiveList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户参与的官方活动订单
		 */
		Spark.post(new Route("/mobile/integral/getUserJoinActiveOrderlist",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getUserJoinActiveOrderlist("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		
		/**
		 * 用户删除已领取的活动订单
		 */
		Spark.post(new Route("/mobile/integral/userDelJoinActiveOrder",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.userDelJoinActiveOrder("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户积分商品兑换信息
		 */
		Spark.post(new Route("/mobile/integral/getAnnounceMsg",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getAnnounceMsg("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		
		
		
		
		// **************************************************************************PC端请求 *************************************************************/
		
		
		/**
		 * 获取用户积分详情记录(积分来源和消耗)
		 */
		Spark.post(new Route("/system/integral/getIntegralDetailRecord",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralDetailRecord("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 积分商城添加商品
		 */
		Spark.post(new Route("/system/integral/addIntegralGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.addIntegralGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取积分商城添加的商品
		 */
		Spark.post(new Route("/system/integral/getIntegralGoodList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralGoodList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 新增或编辑保存积分规则
		 */
		Spark.post(new Route("/system/integral/createIntegralRule",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.createIntegralRule("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 删除积分规则
		 */
		Spark.post(new Route("/system/integral/deleteIntegralRule",true,"jdbcwrite") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.deleteIntegralRule("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		
		/**
		 * 获取积分规则（条件查询）
		 */
		Spark.post(new Route("/system/integral/getIntegralRuleList",true,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralRuleList("jdbcread",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取用户积分兑换商品记录列表
		 */
		Spark.post(new Route("/system/integral/getIntegralExGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getIntegralExGoodsListSystem("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台获取积分商品兑换记录列表
		 */
		Spark.post(new Route("/system/integral/platformGetIntegralExGoodsList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformGetIntegralExGoodsList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平活动商品添加
		 */
		Spark.post(new Route("/system/integral/addActivityGood", true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.addActivityGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * PC用户删除积分商城商品兑换订单记录
		 */
		Spark.post(new Route("/system/integral/delGoodsExRecordOfPc",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.delGoodsExRecordOfPc("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台删除积分商城商品兑换订单记录
		 */
		Spark.post(new Route("/system/integral/platformDelGoodsExRecordOfPc",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformDelGoodsExRecordOfPc("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台获取积分详情记录
		 */
		Spark.post(new Route("/system/integral/platformGetIntegralDetailRecord",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformGetIntegralDetailRecord("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台获取优惠活动列表
		 */
		Spark.post(new Route("/system/integral/findActiveList",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.findActiveList("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 平台删除、上架、下架优惠活动
		 */
		Spark.post(new Route("/system/integral/deleteActive",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.deleteActive("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取活动详情
		 */
		Spark.post(new Route("/system/integral/getActivityInfo",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.getActivityInfo("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 编辑修改活动信息
		 */
		Spark.post(new Route("/system/integral/editorActivityGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.editorActivityGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 删除、上架、下架积分商品
		 */
		Spark.post(new Route("/system/integral/oprateGoods",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.oprateIntegralGoods("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
		/**
		 * 获取积分商品详情
		 */
		Spark.post(new Route("/system/integral/getIntegralGoodInfo",false,"jdbcread") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.platformGetIntegralGoodInfo("jdbcread",true,request.raw(),response.raw());
			}
		});
		
		/**
		 * 编辑修改积分商品信息
		 */
		Spark.post(new Route("/system/integral/editorIntegralGood",true,"jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return integralMng.editorIntegralGood("jdbcwrite",false,request.raw(),response.raw());
			}
		});
		
	}

}
