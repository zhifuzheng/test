package com.xryb.zhtc.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xryb.zhtc.manage.CommodityMng;
import com.xryb.zhtc.service.IGxCommodityService;
import com.xryb.zhtc.service.impl.GxCommodityServiceImpl;
import com.xryb.zhtc.util.CacheUtil;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

/**
 * 供需管理
 * @author Administrator
 *
 */

public class CommodityAction implements ISparkApplication {
	@Auto(name=CommodityMng.class)
	private CommodityMng commodityMng;
	
	@Auto(name=GxCommodityServiceImpl.class)
	private IGxCommodityService iGxCommodityService;

	@Override
	public void run() {
		/**
		 * uni-app图片上传
		 */
		Spark.post(new Route("/Public/sp/uploadImg",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.uploadImg("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 新增或修改商品
		 */
		Spark.post(new Route("/Public/sp/commoditySave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.commoditySave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 新增或修改需求
		 */
		Spark.post(new Route("/Public/sp/demandSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.demandSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询我的发布
		 */
		Spark.post(new Route("/Public/sp/myRelease",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.myRelease("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 上架或下架
		 */
		Spark.post(new Route("/Public/sp/stateUp",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.stateUp("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 根据ID查询供需商品
		 */
		Spark.post(new Route("/Public/sp/commodityId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.commodityId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 根据ID查询供需需求
		 */
		Spark.post(new Route("/Public/sp/gxDemandId",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.gxDemandId("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 删除供需商品或供需需求
		 */
		Spark.post(new Route("/Public/sp/demandDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.demandDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询供需商品
		 */
		Spark.post(new Route("/Public/sp/commodityAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.commodityAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 查询供需需求
		 */
		Spark.post(new Route("/Public/sp/demandAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.demandAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 商品浏览量缓存
		 */
		Spark.post(new Route("/Public/sp/commodityBrowse",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.commodityBrowse("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 举报新增或修改
		 */
		Spark.post(new Route("/Public/sp/reportSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.reportSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 举报内容查询
		 */
		Spark.post(new Route("/Public/sp/gxReportsAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.gxReportsAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需举报删除
		 */
		Spark.post(new Route("/Public/sp/reportDel",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.reportDel("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 举报内容停用
		 */
		Spark.post(new Route("/Public/sp/stateReportState",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.stateReportState("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需收藏新增或修改
		 */
		Spark.post(new Route("/Public/sp/collectionSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.collectionSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需收藏查询
		 */
		Spark.post(new Route("/Public/sp/collectionsAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.collectionsAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需点赞缓存
		 */
		Spark.post(new Route("/Public/sp/giveCache",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.giveCache("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需评论
		 */
		Spark.post(new Route("/Public/sp/demandCommentSave",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.demandCommentSave("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需评论查询
		 */
		Spark.post(new Route("/Public/sp/commentsAll",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.commentsAll("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 供需停用或启用
		 */
		Spark.post(new Route("/Public/sp/supplyStop",true,"jdbcread") {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return commodityMng.supplyStop("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		/**
		 * 定时器
		 */
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				//商品跟需求浏览量
				List<Object> browseList = CacheUtil.getInstance().getLyBrowse();
				for(int i = 0; i < browseList.size(); i++) {
					iGxCommodityService.commodityBrowse("jdbcread", false, browseList.get(i));
					iGxCommodityService.demandBrowse("jdbcread", false, browseList.get(i));
				}
				CacheUtil.getInstance().lyBrowseListClaer();//清除
				
				//供需收藏量
				List<String> collectionList = CacheUtil.getInstance().getCollection();
				for(int i = 0; i < collectionList.size(); i++) {
					iGxCommodityService.commodityCollection("jdbcread", false, collectionList.get(i));
					iGxCommodityService.demandCollection("jdbcread", false, collectionList.get(i));
				}
				CacheUtil.getInstance().collectionListClaer();//清除
				
				//供需点赞
				List<Map<String, Object>> giveList = CacheUtil.getInstance().getGive();
				for(Map<String, Object> map:giveList) {
					for(Map.Entry<String, Object> entry:map.entrySet()) {
						iGxCommodityService.demandGive("jdbcread", false, entry.getValue());//供需点赞
						iGxCommodityService.commentGive("jdbcread", false, entry.getValue());//需求点赞
					}
				}
				CacheUtil.getInstance().giveListClaer();//清除缓存
				
			}
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, 0, 24,TimeUnit.HOURS);//已小时为单位
		//service.scheduleAtFixedRate(runnable, 0, 100,TimeUnit.SECONDS);//已秒为单位
		
	}

}
