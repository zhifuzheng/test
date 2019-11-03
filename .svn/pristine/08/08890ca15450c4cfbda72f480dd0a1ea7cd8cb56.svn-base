package com.xryb.zhtc.action;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.manage.BusinessMng;
import com.xryb.zhtc.service.IBusinessService;
import com.xryb.zhtc.service.impl.BusinessServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

/**
 * 零售商供应商
 * @author Administrator
 *
 */

public class BusinessAction implements ISparkApplication {
	@Auto(name = BusinessMng.class)
	private BusinessMng businessMng;
	
	@Auto(name = BusinessServiceImpl.class)
	private IBusinessService iBusinessService;

	@Override
	public void run() {
		/**
		 * 零售商供应商入驻
		 */
		Spark.post(new Route("/Public/business/businessSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 零售商供应商入驻查询
		 */
		Spark.post(new Route("/Public/business/businessAppliesList",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessAppliesList("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询我申请的商家
		 */
		Spark.post(new Route("/Public/business/myBusiness",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.myBusiness("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 根据商家UUID查询
		 */
		Spark.post(new Route("/Public/business/applyId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.applyId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 启用或停用
		 */
		Spark.post(new Route("/Public/business/shopStateSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.shopStateSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家入驻费用设置新增或修改
		 */
		Spark.post(new Route("/Public/business/settledSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.settledSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家入驻费用设置查询
		 */
		Spark.post(new Route("/Public/business/settledAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.settledAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家入驻费用设置根据ID查询
		 */
		Spark.post(new Route("/Public/business/settledId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.settledId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家入驻费用设置删除
		 */
		Spark.post(new Route("/Public/business/settledDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.settledDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询订单
		 */
		Spark.post(new Route("/Public/business/orderAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.orderAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 销售额
		 */
		Spark.post(new Route("/Public/business/totalPriceSum",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.totalPriceSum("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 订单数量
		 */
		Spark.post(new Route("/Public/business/orderNumber",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.orderNumber("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 下单人数
		 */
		Spark.post(new Route("/Public/business/placeNumber",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.placeNumber("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 设置分销商新增或修改
		 */
		Spark.post(new Route("/Public/business/distributionSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.distributionSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 设置分销商根据UUID查询
		 */
		Spark.post(new Route("/Public/business/distributionUUID",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.distributionUUID("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * App查询订单管理
		 */
		Spark.post(new Route("/Public/business/orderAllApp",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.orderAllApp("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 总销售-下单人数-今日订单App查询
		 */
		Spark.post(new Route("/Public/business/dataStatistics",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.dataStatistics("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * uni-app图片上传
		 */
		Spark.post(new Route("/Public/business/uploadImg",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.uploadImg("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * app申请商家入驻
		 */
		Spark.post(new Route("/Public/business/businessAppSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessAppSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * App根据距离查询商家
		 */
		Spark.post(new Route("/Public/business/distanceBusiness",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.distanceBusiness("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 小程序点击我的店铺查询该用户是否有店铺
		 */
		Spark.post(new Route("/Public/business/myBusinessApp",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.myBusinessApp("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 小程序我的店铺（今日交易额-总销售额-今日订单）
		 */
		Spark.post(new Route("/Public/business/businessDay",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessDay("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 批发市场查询
		 */
		Spark.post(new Route("/Public/business/wholesaleMarketsAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.wholesaleMarketsAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 批发市场新增或修改
		 */
		Spark.post(new Route("/Public/business/pfSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.pfSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 批发市场根据ID查询
		 */
		Spark.post(new Route("/Public/business/wholesaleMarketId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.wholesaleMarketId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 收藏新增或修改
		 */
		Spark.post(new Route("/Public/business/collectionSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.collectionSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 收藏查询
		 */
		Spark.post(new Route("/Public/business/collectionAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.collectionAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  收藏删除
		 */
		Spark.post(new Route("/Public/business/collectionDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.collectionDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  入驻消息通知查询
		 */
		Spark.post(new Route("/Public/business/applyMsgsAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.applyMsgsAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除消息通知
		 */
		Spark.post(new Route("/Public/business/msgDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.msgDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 入驻消息通知修改阅读状态
		 */
		Spark.post(new Route("/Public/business/msgReadUp",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.msgReadUp("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 入驻消息通知根据ID查询
		 */
		Spark.post(new Route("/Public/business/applyMsgId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.applyMsgId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 获取服务器时间
		 */
		Spark.post(new Route("/Public/business/getTime",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.getTime("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家等级设置新增或修改
		 */
		Spark.post(new Route("/Public/business/gradeSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.gradeSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家等级设置删除
		 */
		Spark.post(new Route("/Public/business/gradeDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.gradeDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家等级设置查询
		 */
		Spark.post(new Route("/Public/business/businessGradeAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessGradeAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商家等级设置根据UUID查询
		 */
		Spark.post(new Route("/Public/business/businessGradeId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.businessGradeId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 零售商管理查询
		 */
		Spark.post(new Route("/Public/business/retailerAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.retailerAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供应商添加零售商查询
		 */
		Spark.post(new Route("/Public/business/retailerChoiceAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.retailerChoiceAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 零售商管理中间表新增或修改
		 */
		Spark.post(new Route("/Public/business/middleSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.middleSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 零售商管理中间表删除
		 */
		Spark.post(new Route("/Public/business/middleDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.middleDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 线下订单新增或修改
		 */
		Spark.post(new Route("/Public/business/offlineSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.offlineSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询线下订单
		 */
		Spark.post(new Route("/Public/business/offlineOrdersAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.offlineOrdersAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 线下退货
		 */
		Spark.post(new Route("/Public/business/returnGoods",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.returnGoods("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询线下订单明细表
		 */
		Spark.post(new Route("/Public/business/offlineDetailedAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.offlineDetailedAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  商铺线上线下统计交易数据
		 */
		Spark.post(new Route("/Public/business/orderSingle",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.orderSingle("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  线下收款统计查询
		 */
		Spark.post(new Route("/Public/business/offlineStatistics",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.offlineStatistics("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  线下退款统计
		 */
		Spark.post(new Route("/Public/business/detailedRefund",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.detailedRefund("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  零售商订单交易额总交易额统计
		 */
		Spark.post(new Route("/Public/business/xxOrderStatistics",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.xxOrderStatistics("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  根据供应商零售商查询中间表
		 */
		Spark.post(new Route("/Public/business/gysOrlss",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.gysOrlss("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  店铺银行卡绑定或解除
		 */
		Spark.post(new Route("/Public/business/bankSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.bankSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  幻灯片查询
		 */
		Spark.post(new Route("/Public/business/slideImg",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.slideImg("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  幻灯片根据ID查询
		 */
		Spark.post(new Route("/Public/business/slideImgId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.slideImgId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  幻灯片新增或修改
		 */
		Spark.post(new Route("/Public/business/slideSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.slideSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  确认订单核销
		 */
		Spark.post(new Route("/Public/business/purchase",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.purchase("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  幻灯片删除
		 */
		Spark.post(new Route("/Public/business/slideDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.slideDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  平台管理(今日交易额-总销售额-今日订单)
		 */
		Spark.post(new Route("/Public/business/platformStatistics",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.platformStatistics("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  平台数据统计
		 */
		Spark.post(new Route("/Public/business/platformData",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.platformData("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 *  编辑商铺
		 */
		Spark.post(new Route("/Public/business/editSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return businessMng.editSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 定时器
		 */
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				//商家入驻续费时间
				List<BusinessApply> list = new ArrayList<>();
				Map<String, String> findMap = new HashMap<>();
				findMap.put("approvalStatus", "1");
				list = iBusinessService.businessAppliesList("jdbcread", false, null, findMap);
				for(int i = 0; i < list.size(); i++) {
					BusinessApply businessApply = null;
					businessApply = list.get(i);
					String time = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd");
					if(time.equals(businessApply.getDueTime())) {
						//修改为停用状态
						iBusinessService.shopStateSave("jdbcread", false, businessApply.getBusinessUUID(), "0");
					}
				}
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 24,TimeUnit.HOURS);//已小时为单位
		//service.scheduleAtFixedRate(runnable, 0, 10,TimeUnit.SECONDS);//已秒为单位
		
	}

}
