package com.xryb.zhtc.manage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.StringUtils;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.pay.weixin.HttpClientUtil;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Coupon;
import com.xryb.zhtc.entity.CouponOfIssuer;
import com.xryb.zhtc.entity.CouponOfUser;
import com.xryb.zhtc.entity.CouponOffline;
import com.xryb.zhtc.entity.CouponUserList;
import com.xryb.zhtc.entity.IntegralRecordOfUser;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemCat;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.DownloadImgUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.ReqToEntityUtil;

import dbengine.util.Page;
import spark.annotation.Auto;

/**
 * 优惠劵Mng
 * @author apple
 */
public class CouponMng {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	
	/**
	 * 线下优惠劵扫码进入，根据优惠劵UUID获取相关信息即操作
	 * @throws ParseException 
	 */
	public String getOfflineCouponData(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		String vipName = vip.getNickName();//TODO 暂设昵称
		
		String couponUUID = request.getParameter("couponUUID");
		//2.获取优惠劵信息，判断是否可用
		CouponOffline offline = couponService.findCouponOfflineInfo(sourceId, closeConn, couponUUID);
		Integer isGet = offline.getIsGet();//劵的绑定状态,1-未绑定，2-已绑定
		
		if(isGet == 2 ) {//已绑定
			String banVipUUID = offline.getVipUUID();
			String msg = "";
			if(vipUUID.equals(banVipUUID)) {//该用已经绑定该优惠劵
				result.put("status", "-2");
				msg = "您已绑定该优惠劵";
			}else {//该优惠劵被其他人绑定了
				msg = "此优惠劵已被他人绑定，不可使用";
				result.put("status", "-1");
			}
			result.put("msg", msg);
			return JsonUtil.ObjToJson(result);
		}
		
		CouponOfIssuer issuer = couponService.findCouponOfflineIssuerInfo(sourceId, closeConn, couponUUID);
		Date nowtime = new Date();
		String disableTime = issuer.getDisabledTime();//过期时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date expireTime = sdf.parse(disableTime);
		int isUse = nowtime.compareTo(expireTime);
		if(isUse >= 0) {//过期不可使用
			result.put("status", "-1");
			result.put("msg", "此优惠劵已过期，不可使用");
			return JsonUtil.ObjToJson(result);
		}
		
		//3.将扫码进入程序的用户信息与该优惠劵绑定
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("couponUUID", couponUUID);
		params.put("vipUUID", vipUUID);
		params.put("vipName", vipName);
		if(!couponService.updateOfflineCoupon(sourceId, closeConn, params)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙请稍后扫码重试");
			return JsonUtil.ObjToJson(result);
		}
		//4.将该优惠劵添加到用户的卡劵中
		CouponOfUser ofUser = new CouponOfUser();
		ofUser.setCouponUUID(couponUUID);
		ofUser.setBatchUUID(offline.getBatchUUID());
		ofUser.setVipUUID(vipUUID);
		ofUser.setVipName(vipName);
		ofUser.setIsUse(1);
		ofUser.setGetTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if(!couponService.allotCouponToUser(sourceId, closeConn, ofUser)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙请稍后扫码重试");
			return JsonUtil.ObjToJson(result);
		}
		
		//5.判断优惠劵来源和使用范围
		Integer goodsUseCondition = issuer.getGoodsUseCondition();
		Integer goodsSortCondition = issuer.getGoodsSortCondition();
		String businessUUID = issuer.getMerchantUUID();
		
		// 5.1.1指定店铺(平台可能是多店铺)或者全平台零售店铺通用
		if (goodsUseCondition == 2 && goodsSortCondition == 2) {
			if (StringUtils.isNullOrEmpty(businessUUID)) {// 全平台通用
				// 跳转到首页
				result.put("status", "1");
				return JsonUtil.ObjToJson(result);
			} else {// 指定店铺使用
				String[] uuidArr = businessUUID.split(",");
				if (uuidArr.length == 1) {
					// 指定店铺，并获取店铺信息
					result.put("status", "2");
					result.put("offline", offline);
					result.put("issuer", issuer);
					return JsonUtil.ObjToJson(result);
				}

				result.put("status", "1");
				return JsonUtil.ObjToJson(result);
			}
		}

		// 5.1.2指定分类或商品
		if((goodsUseCondition == 2 && goodsSortCondition == 1) || (goodsUseCondition == 1)) {
			result.put("status", "2");
			result.put("offline", offline);
			result.put("issuer", issuer);
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "3");
		result.put("msg", "绑定查询操作成功");
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 领券中心获取可领取优惠劵列表
	 */
	public String  getAllIsGetCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		// 1.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		//2.查询
		List<CouponUserList> couponList = couponService.findAllCouponList(sourceId, closeConn, page);
		
		if(couponList != null) {
			//若是商家发布的优惠劵获取对应优惠劵指定商品和店铺信息
			for (CouponUserList issuer : couponList) {
				if(issuer.getFromType() == 2) {
					Integer goodsUseCondition = issuer.getGoodsUseCondition();
					Integer goodsSortCondition = issuer.getGoodsSortCondition();
					String businessUUID = issuer.getMerchantUUID();
					String catUUID = issuer.getGoodsCatUUID();
					String goodUUID = issuer.getGoodsUUID();
					List<Item> item = null;
					if(goodsSortCondition == 2 && goodsUseCondition == 2) {//店铺通用
						//获取店铺商品信息
						item  = couponService.findEntityGoodInfo(sourceId, closeConn, businessUUID,catUUID,goodUUID,1);
					}
					
					if(goodsSortCondition == 1 && goodsUseCondition == 2) {//店铺指定分类可用
						//获取店铺商品信息
						item  = couponService.findEntityGoodInfo(sourceId, closeConn, businessUUID,catUUID,goodUUID,2);
					}
					
					if(goodsUseCondition == 1) {//店铺指定商品可用
						//获取店铺商品信息
						item  = couponService.findEntityGoodInfo(sourceId, closeConn, businessUUID,catUUID,goodUUID,3);
					}
					issuer.setItem(item);
				}
			}
		}
		
		if (page == null) {
			result.put("couponList", couponList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (couponList == null) {
				result.put("couponList", 0);
			} else {
				result.put("couponList", couponList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 计算每个商品的优惠金额
	 * 根据优惠劵的指定范围计算对应商品的优惠金额
	 * TODO 平台优惠劵
	 * TODO 会员折扣
	 */
	public String  returnPerGoodsPrice(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse resonse) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1 登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后领取");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		String params = request.getParameter("param");//参数
		
		JSONArray paramsArr = null;
		try {
			paramsArr = new JSONArray(params);
			JSONObject common = null;
			
			for (int i = 0; i < paramsArr.length(); i++) {
				common = paramsArr.getJSONObject(i);
				JSONArray goodsarr = new JSONArray(common.get("goodsInfo").toString());
				JSONObject goods = null;
				
				// 1.计算订单小记总价
				Integer totalPrice = 0;
				for (int j = 0; j < goodsarr.length(); j++) {
					goods = goodsarr.getJSONObject(j);
					// 查询获取商品的单价,同一商品有不同规格，所以查询ItemParam
					Integer goodsNums = (Integer) goods.get("goodNums");
					String goodsUUID = (String) goods.get("goodUUID");
					String paramUUID = (String) goods.get("paramUUID");
					ItemParam good = couponService.getGoodInfo(sourceId, closeConn, goodsUUID,paramUUID);
					Integer price = good.getPrice();
					totalPrice += (goodsNums * price);// 订单小记总价
				}
				
				Integer type = (Integer) common.get("type"); //1-优惠劵， TODO 2-会员折扣
				//2.根据不同优惠计算每个商品的优惠金额
				if (type == 1) {//优惠劵
					//如果平台优惠劵可叠加使用，多张优惠劵逗号分隔
					String couponUUIDs = (String) common.get("couponUUID");
					
					if(!StringUtils.isNullOrEmpty(couponUUIDs)) {
						String[] uuid = couponUUIDs.split(",");
						Integer orderTotalReduceMoney = 0;//订单总优惠金额
						Integer reduceMaxPrice = 0;//折扣劵最高优惠金额
						Double money = null;//面值
						Double discount = null;//折扣
						Integer dis = 0;
						
						for (String couponUUID : uuid) {
							CouponOfIssuer couponinfo = couponService.findCouponInfoByCouponUUID(sourceId, closeConn, couponUUID);
							Integer couponType = couponinfo.getCouponType();
							Integer goodsUseCondition = couponinfo.getGoodsUseCondition();
							String goodsUUID = couponinfo.getGoodsUUID();
							Integer goodsSortCondition = couponinfo.getGoodsSortCondition();
							String goodsCatUUID = couponinfo.getGoodsCatUUID();
							
							Integer totalReduceMoney = 0;//优惠劵总优惠金额
							//1.计算总优惠金额
							if (couponType == 1) {// 代金劵
								money = Double.parseDouble(couponinfo.getMoney())*100;
								totalReduceMoney = money.intValue();
								orderTotalReduceMoney += money.intValue();
							}else {
								discount = Double.parseDouble(couponinfo.getDiscount())*100;
								dis = discount.intValue();
								Double reduceMaxPrice1 = Double.parseDouble(couponinfo.getReduceMaxPrice())*100;//最高优惠金额 分
								reduceMaxPrice = reduceMaxPrice1.intValue();
							}
							
							//2.循环计算优惠劵指定范围内每个商品的优惠金额
							for(int j = 0; j < goodsarr.length(); j++) {
								goods = goodsarr.getJSONObject(j);
								String goodUUID = (String) goods.get("goodUUID");
								Integer counts = (Integer) goods.get("goodNums");
								String paramUUID = (String) goods.get("paramUUID");	
								Integer goodTotalReduceMoney = (Integer) goods.get("goodTotalReduceMoney");//单商品总优惠金额
								
								//查询获取商品的单价
								ItemParam goodInfo = couponService.getGoodInfo(sourceId, closeConn, goodUUID,paramUUID);
								Integer price = goodInfo.getPrice();
								
								//店铺通用
								if(goodsUseCondition == 2 && goodsSortCondition == 2) {
									if(couponType == 2) {//折扣劵
										totalReduceMoney = (totalPrice*(100-dis))/100;
										if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
											orderTotalReduceMoney += reduceMaxPrice;
											totalReduceMoney = reduceMaxPrice;
										}else {
											orderTotalReduceMoney += totalReduceMoney;
										}
									}
									
									// 计算每件商品优惠的金额
									Integer treduceMoney = (price * totalReduceMoney / totalPrice);
									goods.put("goodTotalReduceMoney", goodTotalReduceMoney + treduceMoney);
								}
								
								//指定分类
								if(goodsSortCondition == 1 && goodsUseCondition == 2 ) {
									//1.计算属于该分类的商品总价
									Integer catTalPrice = 0;
									for(int f = 0; f < goodsarr.length(); f++) {
										goods = goodsarr.getJSONObject(f);
										//查询获取商品的单价
										String itemUUID = (String) goods.get("goodUUID");
										Integer count = (Integer) goods.get("goodNums");
										String paramUUIDs = (String) goods.get("paramUUID");
										ItemParam itemInfo = couponService.getGoodInfo(sourceId, closeConn, itemUUID,paramUUIDs);
										catTalPrice += itemInfo.getPrice()*count;
									}
									
									if(couponType == 2) {//折扣劵
										totalReduceMoney = (catTalPrice*(100-dis))/100;
										if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
											orderTotalReduceMoney += reduceMaxPrice;
											totalReduceMoney = reduceMaxPrice;
										}else {
											orderTotalReduceMoney += totalReduceMoney;
										}
									}
									
									if(goodsCatUUID.equals(goodInfo.getCatUUID())) {//匹配到对应商品
										// 计算该商品优惠的金额
										Integer reduceMoney = (price * totalReduceMoney / catTalPrice);
										goods.put("goodTotalReduceMoney", goodTotalReduceMoney + reduceMoney);
									}
								}	
								
								//指定商品
								if(goodsUseCondition == 1) {
									if(goodsUUID.equals(goodInfo.getItemUUID())) {//匹配代指定商品
										//计算商品总价
										Integer goodTalPrice = goodInfo.getPrice()*counts;
										//计算该商品的优惠金额
										if(couponType == 2) {//折扣劵
											totalReduceMoney = (goodTalPrice*(100-dis))/100;
											if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
												orderTotalReduceMoney += reduceMaxPrice;
												totalReduceMoney = reduceMaxPrice;
											}else {
												orderTotalReduceMoney += totalReduceMoney;
											}
										}
										Integer reduceMoney = (price * totalReduceMoney / goodTalPrice);
										goods.put("goodTotalReduceMoney", goodTotalReduceMoney + reduceMoney);
									}
								}
								common.put("goodsInfo", goodsarr);
							}
						}
						// 整个小单优惠金额
						common.put("orderTotalReduceMoney", orderTotalReduceMoney);
					}else {
						common.put("orderTotalReduceMoney",0);
						//循环计算每个商品的优惠金额
						for(int j = 0; j < goodsarr.length(); j++) {
							goods = goodsarr.getJSONObject(j);
							goods.put("goodTotalReduceMoney", 0);
						}
						common.put("goodsInfo", goodsarr);
					}
				}
				
				if(type == 2) {//会员折扣
					//TODO 根据vipUUID获取会员的折扣力度
					
					Double  vipDiscount = 0.9*100;
					Integer dis = vipDiscount.intValue();
					// 整个小单优惠金额
					common.put("totalReduMoney", (100-dis)*totalPrice/100);
					//循环计算每个商品的优惠金额
					for (int j = 0; j < goodsarr.length(); j++) {
						goods = goodsarr.getJSONObject(j);
						// 查询获取商品的单价
						String goodsUUID = (String) goods.get("goodUUID");
						String paramUUIDs = (String) goods.get("paramUUID");
						ItemParam good = couponService.getGoodInfo(sourceId, closeConn, goodsUUID,paramUUIDs);
						Integer price = good.getPrice();
							
						// 计算每件商品优惠的金额
						Integer reduceMoney = (price) * (100 - dis)/100;
						goods.put("reduceMoney", reduceMoney);
					}
					common.put("goodsInfo", goodsarr);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String results = paramsArr.toString();
		return JsonUtil.ObjToJson(results);
	}
	
	/**
	 * 获取用户领取的满足使用条件的优惠劵列表(订单提交)
	 */
	public String getUserIsUsedCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后领取");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();// 用户UUID
		
		//1.获取查询条件参数
		String params = request.getParameter("params");
		//System.out.println("params:"+params);
		
		JSONArray paramsArr = null;
		List<CouponUserList> isUesdCouponList = new ArrayList<CouponUserList>();//满足条件的可用的优惠劵列表
		try {
			paramsArr = new JSONArray(params);
			JSONObject entity = null;
			//2.1获取店铺的uuid
			List<String> entityUUIDArr = new ArrayList<String>();
			Integer totalprice = 0;// 所有小单总价
			for (int i = 0; i < paramsArr.length(); i++) {
				entity = paramsArr.getJSONObject(i);
				String entityUUID = (String) entity.get("entityUUID");
				entityUUIDArr.add(entityUUID);
				Integer subPrice = (Integer) entity.get("subPrice");
				totalprice += subPrice;
			}
			
			//1.获取领取的可使用的平台优惠劵（匹配满足条件的平台优惠劵）
			List<CouponUserList> platformlist = couponService.findUserIsUsedPlatformCouponList(sourceId, closeConn, vipUUID);
			if (platformlist != null || platformlist.size() != 0) {
				for (CouponUserList couponUserList : platformlist) {
					//System.out.println("couponUUID:"+couponUserList.getCouponUUID()+",batchUUID:"+couponUserList.getBatchUUID());
					
					Integer goodsUseCondition = couponUserList.getGoodsUseCondition();
					String goodsUUID = couponUserList.getGoodsUUID();
					Integer goodsSortCondition = couponUserList.getGoodsSortCondition();
					String goodsCatUUID = couponUserList.getGoodsCatUUID();
					String businessUUID = couponUserList.getMerchantUUID();
					Double effectMoneys = Double.valueOf(couponUserList.getEffectMoney())*100;
					Integer effectMoney = effectMoneys.intValue();//生效金额(分)
					
					//1.1所有店铺商品可用
					if(goodsUseCondition == 2 && goodsSortCondition == 2 && StringUtils.isNullOrEmpty(businessUUID)) {
						for (int i = 0; i < paramsArr.length(); i++) {
							entity = paramsArr.getJSONObject(i);
							Integer subPrice = (Integer) entity.get("subPrice");
							//匹配是否可用
							if(subPrice >= effectMoney) {
								isUesdCouponList.add(couponUserList);
								break;
							}
						}
					}
					
					//1.2指定店铺所有商品可用(可能是多店铺)
					if(goodsUseCondition == 2 && goodsSortCondition == 2 && !StringUtils.isNullOrEmpty(businessUUID)) {
						String[] uuidArr = businessUUID.split(",");
						flag: for (int i = 0; i < paramsArr.length(); i++) {
							entity = paramsArr.getJSONObject(i);
							String entityUUID = (String) entity.get("entityUUID");
							for (String UUID : uuidArr) {
								if(UUID.indexOf(entityUUID) != -1) {//匹配到指定店铺
									Integer subPrice = (Integer) entity.get("subPrice");
									//匹配是否可用
									if(subPrice >= effectMoney) {
										isUesdCouponList.add(couponUserList);
										break flag;
									}
								}
							}
						}
					}
					
					//1.3指定分类可用
					if(goodsUseCondition == 2 && goodsSortCondition == 1 && !StringUtils.isNullOrEmpty(businessUUID)) {
						for (int i = 0; i < paramsArr.length(); i++) {
							entity = paramsArr.getJSONObject(i);
							String entityUUID = (String) entity.get("entityUUID");
							if(entityUUID.equals(businessUUID)) {
								JSONArray paramsListArr =  (JSONArray) entity.get("paramList");//店铺对应的商品
								Integer catTolPrice = 0;//对应分类商品总价
								for(int j = 0;j<paramsListArr.length();j++) {
									JSONObject good = paramsListArr.getJSONObject(j);
									//1.获取商品uuid
									String itemCatUUID = (String) good.get("catUUID");
									if(itemCatUUID.equals(goodsCatUUID)) {//匹配到指定的分类商品
										Integer price = (Integer) good.get("price");//商品单价(分)
										Integer count = (Integer) good.get("count");//商品数量
										catTolPrice = price*count;//商品总价(分)
										//判断是否满足优惠条件
										if(catTolPrice >= effectMoney) {//满足条件
											isUesdCouponList.add(couponUserList);
											break;
										}
									}
								}
							}
						}
					}
					
					
					
					//1.4指定商品可用
					if(goodsUseCondition == 1 && !StringUtils.isNullOrEmpty(businessUUID)) {
						for (int i = 0; i < paramsArr.length(); i++) {
							entity = paramsArr.getJSONObject(i);
							String entityUUID = (String) entity.get("entityUUID");
							if(entityUUID.equals(businessUUID)) {
								JSONArray paramsListArr =  (JSONArray) entity.get("paramList");//店铺对应的商品
								for(int j = 0;j<paramsListArr.length();j++) {
									JSONObject good = paramsListArr.getJSONObject(j);
									//1.获取商品uuid
									String itemUUID = (String) good.get("itemUUID");
									if(itemUUID.equals(goodsUUID)) {//匹配到指定的商品
										Integer price = (Integer) good.get("price");//商品单价(分)
										Integer count = (Integer) good.get("count");//商品数量
										Integer tolPrice = price*count;//商品总价(分)
										//判断是否满足优惠条件
										if(tolPrice >= effectMoney) {//满足条件
											isUesdCouponList.add(couponUserList);
											break;
										}
									}
								}
							}
							
						}
					}
				}
			}
			
			//2.2根据店铺uuid获取用户领取的对应店铺所有的优惠劵
			List<CouponUserList> couponList = couponService.findUserIsUsedCouponList(sourceId, closeConn, vipUUID, entityUUIDArr);
			if (couponList != null || couponList.size() != 0) {
				// 3.匹配满足条件的商家优惠劵优惠劵
				for (int i = 0; i < paramsArr.length(); i++) {
					entity = paramsArr.getJSONObject(i);
					Integer subPrice = (Integer) entity.get("subPrice");
					String entityUUID = (String) entity.get("entityUUID");
					
					for (CouponUserList couponUserList : couponList) {
						if (entityUUID.equals(couponUserList.getMerchantUUID())) {//该优惠劵属于该店铺
							//判断该优惠劵是全店通用、指定分类还是指定商品可用 goodsUseCondition 1-指定商品，2-不指定商品
							String goodUUID = couponUserList.getGoodsUUID();
							Integer goodsUseCondition = couponUserList.getGoodsUseCondition();
							Double effectMoneys = Double.valueOf(couponUserList.getEffectMoney())*100;
							Integer effectMoney = effectMoneys.intValue();//生效金额(分)
							
							if(!StringUtils.isNullOrEmpty(goodUUID) && goodsUseCondition == 1) {//指定商品可用
								JSONArray paramsListArr =  (JSONArray) entity.get("paramList");//店铺对应的商品
								for(int j = 0;j<paramsListArr.length();j++) {
									JSONObject good = paramsListArr.getJSONObject(j);
									//1.获取商品uuid
									String itemUUID = (String) good.get("itemUUID");
									if(itemUUID.equals(goodUUID)) {//匹配到指定的商品
										Integer price = (Integer) good.get("price");//商品单价(分)
										Integer count = (Integer) good.get("count");//商品数量
										Integer tolPrice = price*count;//商品总价(分)
										//判断是否满足优惠条件
										if(tolPrice >= effectMoney) {//满足条件
											isUesdCouponList.add(couponUserList);
										}
									}
								}
							}
							//商品分类使用限制(1-指定分类商品，2-不指定分类商品) goodsSortCondition
							String catUUID = couponUserList.getGoodsCatUUID();
							Integer goodsSortCondition = couponUserList.getGoodsSortCondition();
							if(!StringUtils.isNullOrEmpty(catUUID) && goodsUseCondition == 2 && goodsSortCondition == 1) {//指定分类可用
								JSONArray paramsListArr = (JSONArray) entity.get("paramList");//店铺对应的商品
								Integer catTolPrice = 0;//对应分类商品总价
								for(int j = 0;j<paramsListArr.length();j++) {
									JSONObject good = paramsListArr.getJSONObject(j);
									//1.获取商品分类catUUID,并计算满该劵分类商品的总价
									String itemCatUUID = (String) good.get("catUUID");
									if(itemCatUUID.equals(catUUID)) {//匹配到指定分类商品
										Integer price = (Integer) good.get("price");//商品单价(分)
										Integer count = (Integer) good.get("count");//商品数量
										catTolPrice += (price*count);
										if(catTolPrice >= effectMoney) {//满足条件
											isUesdCouponList.add(couponUserList);
											break;
										}
									}
								}
							}
							if(StringUtils.isNullOrEmpty(goodUUID) && StringUtils.isNullOrEmpty(catUUID)) {//全店铺商品通用
								// 比较订单金额是否满足满减条件
								if (subPrice >= effectMoney) {// 满足
									isUesdCouponList.add(couponUserList);
								}
							}
						}
					}
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(isUesdCouponList.size() == 0 || isUesdCouponList == null ) {
			result.put("status", "-1");
			result.put("msg", " 无可用优惠劵");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", " 有用优惠劵");
		result.put("isUesdCouponList", isUesdCouponList);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 优惠劵立即使用获取对应商品列表
	 */
	public String getCouponGoodsList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		// 1.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String businessUUID = request.getParameter("businessUUID");
		String catUUID = request.getParameter("catUUID");
		String goodUUID = request.getParameter("goodUUID");
		String searchWords = request.getParameter("searchWords");
		//2.获取查询条件
		Map<String, String> param = new HashMap<String, String>();// 封装查询条件
		param.put("businessUUID", businessUUID);
		param.put("catUUID", catUUID);
		param.put("goodUUID", goodUUID);
		param.put("searchWords", searchWords);
		
		//3.查询
		List<Item> goodsList = couponService.findGoodsCouponList(sourceId, closeConn, page, param);
		if (page == null) {
			result.put("goodsList", goodsList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodsList == null) {
				result.put("couponList", 0);
			} else {
				result.put("goodsList", goodsList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}
	
	
	/**
	 * 获取用户领取的对应店铺的优惠劵列表(提交订单)
	 */
	public String getUserEntityCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		String itemUUIDArr = request.getParameter("itemUUID");//商品uuid
		String entityUUID = request.getParameter("entityUUID");
		System.out.println("itemUUIDArr:"+itemUUIDArr);
		
		//查询
		List<CouponUserList> userEntityCouponList = couponService.findUserEntityCouponList(sourceId, closeConn, vipUUID, entityUUID,itemUUIDArr);
		if(userEntityCouponList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "没有可用的优惠劵");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "优惠劵获取成功");
		result.put("userEntityCouponList", userEntityCouponList);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 手机端获取用户领取的优惠劵列表
	 */
	public String getUserCouponListMobile(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		//2.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		//3.查询结果
		List<CouponUserList> userCouponList = couponService.findUserCouponListMobile(sourceId, closeConn, vipUUID, page);
		
		if (page == null) {
			result.put("couponList", userCouponList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (userCouponList == null) {
				result.put("couponList", 0);
			} else {
				result.put("couponList", userCouponList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 用户领取优惠劵	
	 * @throws Exception 
	 */
	public String UserGetCoupon(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1 登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请授权登陆后领取");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();// 用户UUID
		String vipName = vip.getVipName();
		
		
		String batchUUID = request.getParameter("batchUUID");//获取领取优惠劵的批次UUID
		
		//2 根据batchUUID查询该批次优惠劵详细信息
		CouponOfIssuer issuer = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		//2.1 判断该优惠劵库存量
		if(issuer.getStock() == 0 ) {//库存为0，不可领取
			result.put("status", "-1");
			result.put("msg", "该优惠劵已全部被领取");
			return JsonUtil.ObjToJson(result);
		}
		//2.2 判断是否还在领取时间范围内
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		
		long nowTime = Long.valueOf(time.replaceAll("[-\\s:]",""));
		long endTime = Long.valueOf(issuer.getReleaseEndTime().replaceAll("[-\\s:]",""));
		long startTime = Long.valueOf(issuer.getReleaseStartTime().replaceAll("[-\\s:]",""));
		
		if (nowTime>endTime) {// 超过领取时间
			result.put("status", "-1");
			result.put("msg", "已过领取时间");
			return JsonUtil.ObjToJson(result);
		}
		if (nowTime<startTime) {// 未到领取时间
			result.put("status", "-1");
			result.put("msg", "未到领取时间");
			return JsonUtil.ObjToJson(result);
		}
		
		//3 判断该用户是否已领取过该优惠劵(优惠劵有可重复领取和不可重复领取，不可重复领取只能领取1张，可重复领取可领取商家指定的张数)
		Map nums = couponService.getNumsCouponOfUser(sourceId, closeConn, vipUUID, batchUUID);
		Integer getNums = Integer.parseInt(nums.get("numbers").toString());
		if(getNums >= issuer.getRepeatNumber()) {//已达到领取上线
			result.put("status", "-1");
			result.put("msg", "该优惠劵每人限领"+issuer.getRepeatNumber()+"张，您已领取完");
			return JsonUtil.ObjToJson(result);
		}
		
		
		CouponOfUser user = new CouponOfUser();
		user.setVipUUID(vipUUID);//该劵领取者的vipUUID
		user.setBatchUUID(batchUUID);//该有劵所属批次batchUUID
		user.setIsUse(1);//初值设置为未使用
		user.setIsDel(1);//初值设置为未删除
		user.setGetTime(time);//领取时间
		user.setVipName(vipName);//会员姓名
		
		//4.获取用户的积分，并判断是否足以领取
		Map<String,Object> map = integralService.getUserIntegral(sourceId, closeConn, vipUUID);
		Integer userIntegral  = (Integer) map.get("integral");
		Integer integration = issuer.getIntegration();//优惠劵领取所需积分
		userIntegral = userIntegral-integration;//用户领取后还剩余积分
		if(integration > 0) {//需要积分才能领取
			if(userIntegral < integration) {//用户积分不够
				result.put("status", "-1");
				result.put("msg", "该优惠劵需要"+integration+"积分，您的积分不足");
				return JsonUtil.ObjToJson(result);
			}else {//用户积分够领取
				//领取优惠劵
				if(allotCouponToUser(sourceId, closeConn, user,userIntegral,integration)) {//获取成功
					result.put("status", "2");
					result.put("msg", "领取成功");
					result.put("integration", integration);
					return JsonUtil.ObjToJson(result);
				}
			}
		}else {//免费领取
			if(allotCouponToUser(sourceId, closeConn, user,userIntegral,integration)) {//获取成功
				result.put("status", "1");
				result.put("msg", "领取成功");
				return JsonUtil.ObjToJson(result);
			}
		}
		
		result.put("status", "-1");
		result.put("msg", "系统繁忙请稍后重试");
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 获取店铺上架可领取的优惠劵列表
	 */
	public String getEntityCoupon(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//1-平台，2-商家
		//获取店铺UUID 
		String merchantUUID = request.getParameter("merchantUUID");
		
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//查询可领取的优惠劵列表
		List<CouponUserList> couponList = couponService.findCouponIsUsed(sourceId, closeConn, merchantUUID);
		
		
		if(couponList.size() == 0 ) {
			result.put("status", "-1");
			result.put("msg", "未查询到可领取优惠劵");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "查询到可领取优惠劵");
		result.put("couponList", couponList);
		return JsonUtil.ObjToJson(result);
	}
	
	
	//* **************************************************************PC端请求************************************************************************* */
	/**
	 * 创建优惠劵
	 */
	public String createCoupon(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		String vipName = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
			vipName = userInfo.getUserName();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
			vipName = vipInfo.getVipName();
		}
		
		
		
		CouponOfIssuer couponInfo = (CouponOfIssuer)ReqToEntityUtil.reqToEntity(request, CouponOfIssuer.class);//将请求参数转换为对应的实体
		String batchUUID = UUID.randomUUID().toString().replaceAll("-", "");
		couponInfo.setBatchUUID(batchUUID);//设置该批次优惠的批次uuid
		couponInfo.setVipUUID(vipUUID);
		couponInfo.setIsDel(1);//1-未删除，2-已删除
		//获取发行数量
		Integer couponNumbers = Integer.parseInt(request.getParameter("releaseNumbers"));
		couponInfo.setReleaseNumbers(couponNumbers);
		couponInfo.setGetNumbers(0);//领取量初值为0
		couponInfo.setUsedNumbers(0);//使用量初值为0
		couponInfo.setStock(couponNumbers);//库存初值为发行数量
		String createTime = DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
		couponInfo.setCreateTime(createTime);
		String batchNumber = createTime.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
		couponInfo.setBatchNumber(batchNumber);
		String isRelease = request.getParameter("isRelease");
		//1.判断是否上架，设置发行时间，未上架不设置
		if("1".equals(isRelease)) {//立即上架
			couponInfo.setReleaseTime(createTime);
		}
		
		//2.优惠劵代金劵判断
		String couponType = request.getParameter("couponType");
		if("1".equals(couponType)) {//代金劵
			Integer integration = Integer.parseInt(request.getParameter("integration1"))  ;
			String effectMoney = request.getParameter("effectMoney1");
			couponInfo.setIntegration(integration);
			couponInfo.setEffectMoney(effectMoney);
		}else if("2".equals(couponType)) {//优惠劵
			Integer integration = Integer.parseInt(request.getParameter("integration2"))  ;
			String effectMoney = request.getParameter("effectMoney2");
			couponInfo.setIntegration(integration);
			couponInfo.setEffectMoney(effectMoney);
		}
		
		//3.商品分类使用判断
		String goodsSortCondition = request.getParameter("goodsSortCondition");
		if("1".equals(goodsSortCondition)) {//限定商品使用
			String catUUID = request.getParameter("catUUID");
			couponInfo.setGoodsCatUUID(catUUID);
		}
		
		//4.商品限定判断
		String goodsUseCondition = request.getParameter("goodsUseCondition");
		if("1".equals(goodsUseCondition)) {//限定商品使用
			String goodsUUID = request.getParameter("goodsUUID");
			couponInfo.setGoodsUUID(goodsUUID);
		}
		
		String merchantUUID = request.getParameter("merchantUUID");
		//获取商家信息
		Integer fromType = Integer.valueOf(request.getParameter("fromType"));
		if(fromType == 2) {//商家创建优惠劵
			BusinessApply apply = couponService.getBusinessInfoByUUID(sourceId, closeConn, merchantUUID);
			if(apply == null) {
				result.put("status", "-1");
				result.put("msg", "发布失败，该店铺不存在");
				return JsonUtil.ObjToJson(result);
			}
			couponInfo.setBusinessName(apply.getBusinessName());
		}else {
			couponInfo.setBusinessName(vipName);
		}
		couponInfo.setFromType(fromType);//1-平台，2-商家
		couponInfo.setMerchantUUID(merchantUUID);
		
		//5.创建优惠劵模版
		if (!couponService.createCouponMng(sourceId, closeConn, couponInfo)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		//6.批量生成优惠劵
		if(couponInfo.getReleaseType() == 1) {//线上
			Coupon coupon = new Coupon();
			for (int i = 0; i < couponNumbers; i++) {
				coupon.setCouponUUID(UUID.randomUUID().toString().replaceAll("-", ""));// 劵唯一标识
				coupon.setBatchUUID(batchUUID);
				coupon.setIsGet(1);//劵是否被领取,1-未领取，2-已领取
				//优惠劵编号设置
				String couponNumber = getCouponNumber(couponNumbers, i);
				//1.获取优惠劵开始使用时间
				String startUseTime = request.getParameter("useStartTime");
				//2.截取前六位
				String time = startUseTime.substring(0, 7);
				time = time.replace("-", "");
				couponNumber = "No."+time+couponNumber;//No.201907XXX
				coupon.setCouponNumber(couponNumber);
				
				// 写入数据库
				if (!couponService.createCoupon(sourceId, closeConn, coupon)) {
					result.put("status", "-2");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
			
		}
		
		if(couponInfo.getReleaseType() == 2){//线下
			for (int i = 1; i <= couponNumbers; i++) {
				CouponOffline offline = new CouponOffline();
				offline.setBatchUUID(batchUUID);//劵批次UUID
				offline.setIsGet(1);//劵绑定状态，1-未绑定
				try {
					String couponUUID = UUID.randomUUID().toString().replaceAll("-", "");
					offline.setCouponUUID(couponUUID);//劵唯一标识
					//优惠劵编号设置
					String couponNumber = getCouponNumber(couponNumbers, i);
					//1.获取优惠劵开始使用时间
					String startUseTime = request.getParameter("useStartTime");
					//2.截取前六位
					String time = startUseTime.substring(0, 7);
					time = time.replace("-", "");
					couponNumber = "No."+time+couponNumber;//No.201907XXX
					offline.setCouponNumber(couponNumber);
					
					/*
					 * 生成二维码
					 * 1.二维码内容,根据优惠劵的具体信息，参数为优惠劵couponUUID
					 */
					String accessToken = jedisClient.getAccessToken();
					if(accessToken == null){
						result.put("status", "-1");
						result.put("msg", "当前网络繁忙，请稍后重试");
						return JsonUtil.ObjToJson(result);
					}
					String qrCodePath = ReadProperties.getValue("couponQRcodeImg");//二维码保存路径couponQRcodeImg=static/coupon/QRcodeImg/
					String param = "{\"scene\":\""+couponUUID+"\",\"page\":\"pages/user/yhqsplb/yhqsplb\"}";
					Map<String, String> map = HttpClientUtil.genQRCode(request, "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken, param, qrCodePath);
					if(!"1".equals(map.get("status"))){
						result.put("status", "-1");
						result.put("msg", map.get("msg"));
						return JsonUtil.ObjToJson(result);
					}
					String path = map.get("path");
					offline.setQRcodeImg(path);//保存二维码相对地址
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 写入数据库
				if (!couponService.createCouponOffline(sourceId, closeConn, offline)) {
					result.put("status", "-1");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
		}
		
		result.put("status", "1");
		result.put("msg", "优惠劵生成成功");
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 保存编辑未上架的优惠劵
	 */
	public String editorSaveCoupon(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		CouponOfIssuer couponInfo = (CouponOfIssuer)ReqToEntityUtil.reqToEntity(request, CouponOfIssuer.class);//将请求参数转换为对应的实体
		String batchUUID = request.getParameter("batchUUID");
		
		//1.判断是线上还是线下优惠劵,删除之前批量生成的优惠劵，重新生成
		if(couponInfo.getReleaseType() == 2) {//1-线上，2-线下
			//1.1删除线下优惠劵生成的二维码图片
			List<CouponOffline> couponOfflines = couponService.getQRcodeImg(sourceId, closeConn, batchUUID);
			String baseUrl = request.getParameter("basePath");
			for (CouponOffline couponOffline : couponOfflines) {
				if(!DownloadImgUtil.deleteImg(baseUrl+couponOffline.getQRcodeImg())) {
					result.put("status", "-1");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
			
			//1.2删除数据库coupon_offline的数据
			if (!couponService.delPreCouponOffline(sourceId, closeConn, batchUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
			
		}else {//线上
			if (!couponService.delPreCoupon(sourceId, closeConn, batchUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
		}
		
		//2.优惠劵类型判断
		String couponType = request.getParameter("couponType");
		if ("1".equals(couponType)) {// 代金劵
			Integer integration = Integer.parseInt(request.getParameter("integration1"));
			String effectMoney = request.getParameter("effectMoney1");
			couponInfo.setIntegration(integration);
			couponInfo.setEffectMoney(effectMoney);
		} else if ("2".equals(couponType)) {// 优惠劵
			Integer integration = Integer.parseInt(request.getParameter("integration2"));
			String effectMoney = request.getParameter("effectMoney2");
			couponInfo.setIntegration(integration);
			couponInfo.setEffectMoney(effectMoney);
		}
		
		//3.商品分类使用判断
		String goodsSortCondition = request.getParameter("goodsSortCondition");
		if ("1".equals(goodsSortCondition)) {// 限定商品使用
			String catUUID = request.getParameter("catUUID");
			couponInfo.setGoodsCatUUID(catUUID);
		}

		//4.商品限定判断
		String goodsUseCondition = request.getParameter("goodsUseCondition");
		if ("1".equals(goodsUseCondition)) {// 限定商品使用
			String goodsUUID = request.getParameter("goodsUUID");
			couponInfo.setGoodsUUID(goodsUUID);
		}
		
		Integer fromType = Integer.valueOf(request.getParameter("fromType"));
		if(fromType == 1) {
			String merchantUUID = request.getParameter("merchantUUID");
			couponInfo.setFromType(fromType);//1-平台，2-商家
			couponInfo.setMerchantUUID(merchantUUID);
		}
		
		//5.判断是否上架，设置发行时间，未上架不设置
		String isRelease = request.getParameter("isRelease");
		if("1".equals(isRelease)) {//立即上架
			couponInfo.setReleaseTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		}
		
		//6.获取发行数量,需要重新生成优惠劵
		Integer couponNumbers = Integer.parseInt(request.getParameter("releaseNumbers"));
		couponInfo.setReleaseNumbers(couponNumbers);
		
		//7.重新设置库存初值为修改后的发行数量
		couponInfo.setStock(couponNumbers);
		couponInfo.setModifiedTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));//设置修改时间
		couponInfo.setId(Long.parseLong(request.getParameter("id")));//设置该劵的自增id
		
		//8.执行优惠劵模版信息修改
		if (!couponService.updataCouponInfo(sourceId, closeConn, couponInfo)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		//9.批量生成优惠劵
		if(couponInfo.getReleaseType() == 2) {//线下
			for (int i = 1; i <= couponNumbers; i++) {
				CouponOffline offline = new CouponOffline();
				offline.setBatchUUID(batchUUID);//劵批次UUID
				offline.setIsGet(1);//劵绑定状态，1-未绑定
				
				try {
					String couponUUID = UUID.randomUUID().toString().replaceAll("-", "");
					offline.setCouponUUID(couponUUID);//劵唯一标识
					//优惠劵编号设置
					String couponNumber = getCouponNumber(couponNumbers, i);
					//1.获取优惠劵开始使用时间
					String startUseTime = request.getParameter("useStartTime");
					//2.截取前六位
					String time = startUseTime.substring(0, 7);
					time = time.replace("-", "");
					couponNumber = "No."+time+couponNumber;//No.201907XXX
					offline.setCouponNumber(couponNumber);
					
					/*
					 * 生成二维码
					 * 1.二维码内容,根据优惠劵的具体信息，参数为优惠劵couponUUID
					 */
					String accessToken = jedisClient.getAccessToken();
					if(accessToken == null){
						result.put("status", "-1");
						result.put("msg", "当前网络繁忙，请稍后重试");
						return JsonUtil.ObjToJson(result);
					}
					String qrCodePath = ReadProperties.getValue("couponQRcodeImg");//读取优惠劵二维码保存路径
					String param = "{\"scene\":\""+couponUUID+"\",\"page\":\"pages/user/yhqsplb/yhqsplb\"}";
					Map<String, String> map = HttpClientUtil.genQRCode(request, "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken, param, qrCodePath);
					if(!"1".equals(map.get("status"))){
						result.put("status", "-1");
						result.put("msg", map.get("msg"));
						return JsonUtil.ObjToJson(result);
					}
					String path = map.get("path");
					offline.setQRcodeImg(path);//保存二维码相对地址
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// 写入数据库
				if (!couponService.createCouponOffline(sourceId, closeConn, offline)) {
					result.put("status", "-2");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
		}else {//线上
			for (int i = 0; i < couponNumbers; i++) {
				Coupon coupon = new Coupon();
				coupon.setCouponUUID(UUID.randomUUID().toString().replaceAll("-", ""));// 劵唯一标识
				coupon.setBatchUUID(batchUUID);
				coupon.setIsGet(1);//劵是否被领取,1-未领取，2-已领取
				//优惠劵编号设置
				String couponNumber = getCouponNumber(couponNumbers, i);
				//1.获取优惠劵开始使用时间
				String startUseTime = request.getParameter("useStartTime");
				//2.截取前六位
				String time = startUseTime.substring(0, 7);
				time = time.replace("-", "");
				couponNumber = "No."+time+couponNumber;//No.201907XXX
				coupon.setCouponNumber(couponNumber);
				// 写入数据库
				if (!couponService.createCoupon(sourceId, closeConn, coupon)) {
					result.put("status", "-2");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
		}
		result.put("status", "1");
		result.put("msg", "编辑修改保存成功");
		return JsonUtil.ObjToJson(result);
	}
	

	/**
	 * 获取商具体分类商品列表
	 */
	public String getGoodsList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String businessUUID = request.getParameter("businessUUID");//发行商uuid
		String catUUID = request.getParameter("catUUID");//商品分类UUID
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//查询数据库
		List<Item> goodsList = couponService.getGoodsListByEntityUUID(sourceId, closeConn, businessUUID,catUUID);
		if(goodsList.size() == 0) {//没有查询到商品
			result.put("status", "-1");
			result.put("msg", "未添加商品");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "商品获取成功");
		result.put("goodsList", goodsList);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 *  通过parentUUID，查询子分类列表
	 */
	public String getGoodsSortList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String businessUUID = request.getParameter("businessUUID");//发行商uuid
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if("".equals(businessUUID)) {
			result.put("status", "-1");
			result.put("msg", "发行商uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//1.根据发行商的uuid查询店铺商品分类catUUID
		Map<String,String> map = couponService.getGoodsCatUUIDByBusinessUUID(sourceId, closeConn, businessUUID);
		String catUUID = map.get("catUUID");
		if("".equals(catUUID) || catUUID == null) {
			result.put("status", "-1");
			result.put("msg", "未查询到商品分类信息");
			return JsonUtil.ObjToJson(result);
		}
		
		//2.获取该分类下的子分类列表
		List<ItemCat> sortList = couponService.getGoodsSortList(sourceId, closeConn, catUUID);
		if(sortList.size() == 0) {
			result.put("status", "-2");
			result.put("msg", "该分类下未查到子分类列表");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "成功获取分类列表");
		result.put("sortList", sortList);
		return JsonUtil.ObjToJson(result);
	}
	

	/**
	 * 获取优惠劵列表
	 */
	public String getCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		String userType = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
			userType = userInfo.getUserType();
		}
		if(vipInfo != null ) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		//获取查询条件
		Map<String,Object> param = new HashMap<String,Object>();//封装查询条件
		param.put("range", request.getParameter("range"));
		param.put("type", request.getParameter("type"));
		param.put("releaseType", request.getParameter("releaseType"));
		param.put("status", request.getParameter("status"));
		param.put("searchWords", request.getParameter("searchWords"));
		param.put("business", request.getParameter("business"));
		param.put("vipUUID", vipUUID);
		param.put("userType", userType);
		//查询结果
		List<CouponOfIssuer> couponList = couponService.findCouponList(sourceId, closeConn, param, page);
		
		if (page == null) {
			result.put("rows", couponList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (couponList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", couponList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 获取平台优惠劵列表（条件获取）
	 */
	public String platformFindCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
		}
		if(vipInfo != null ) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		//获取查询条件
		Map<String,Object> param = new HashMap<String,Object>();//封装查询条件
		param.put("range", request.getParameter("range"));
		param.put("type", request.getParameter("type"));
		param.put("releaseType", request.getParameter("releaseType"));
		param.put("status", request.getParameter("status"));
		param.put("searchWords", request.getParameter("searchWords"));
		param.put("vipUUID", vipUUID);
		//查询结果
		List<CouponOfIssuer> couponList = couponService.platformFindCouponList(sourceId, closeConn, param, page);
		
		if (page == null) {
			result.put("rows", couponList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (couponList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", couponList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	

	/**
	 * 发行商删除指定批次优惠劵
	 */
	public String deleteCoupons(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String batchUUID = request.getParameter("batchUUID");
		//删除指定批次优惠劵
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		
		// 根据uuid查询该批优惠劵的相关信息，已上架的不能重复上架
		CouponOfIssuer coupon = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		if(coupon.getIsRelease() != 2) {
			result.put("status", "-1");
			result.put("msg", "只能删除未上架优惠劵");
			return JsonUtil.ObjToJson(result);
		}
		
		boolean del = couponService.deleteCouponByBatchUUID(sourceId, closeConn, batchUUID);
		if(!del) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 上架指定优惠劵
	 * @throws ParseException 
	 */
	public String putawayCoupons(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String batchUUID = request.getParameter("batchUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		// 根据uuid查询该批优惠劵的相关信息，已上架的不能重复上架
		CouponOfIssuer coupon = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		// 判断该优惠劵是否已上架
		if (coupon.getIsRelease() == 1) {// 已上架
			result.put("status", "-1");
			result.put("msg", "上架失败，只能上架未上架优惠劵！");
			return JsonUtil.ObjToJson(result);
		}
		// 获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date date = new Date();
		long nowTime = date.getTime();
		long releaseEndTime = sdf.parse(coupon.getReleaseEndTime()).getTime();// 领取截止时间
		long disabledTime = sdf.parse(coupon.getDisabledTime()).getTime();// 使用截止时间

		if (nowTime >= releaseEndTime && nowTime < disabledTime) {// 超过领取时间,但未超过使用截止时间
			result.put("status", "-2");
			result.put("msg", "上架失败，您选择的" + coupon.getCouponName() + "已过领取截止时间，请重新创建或编辑优惠劵");
			return JsonUtil.ObjToJson(result);
		}

		if (nowTime >= disabledTime) {// 超过使用截止时间
			result.put("status", "-3");
			result.put("msg", "上架失败，您选择的" + coupon.getCouponName() + "已过使用截止时间，请重新创建或编辑优惠劵");
			return JsonUtil.ObjToJson(result);
		}

		// 上架
		if (!couponService.putawayCoupon(sourceId, closeConn, batchUUID)) {
			result.put("status", "-4");
			result.put("msg", "系统繁忙请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "上架成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 下架指定优惠劵
	 */
	public String downCoupons(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String batchUUID = request.getParameter("batchUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		// 根据uuid查询该批优惠劵的相关信息，只能下架已上架的
		CouponOfIssuer coupon = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		// 判断该优惠劵是否已上架
		if (coupon.getIsRelease() != 1) {// 未上架或者已下架
			result.put("status", "-1");
			result.put("msg", "下架失败，只能下架已上架优惠劵！");
			return JsonUtil.ObjToJson(result);
		}
			
		// 下架
		if (!couponService.downCoupon(sourceId, closeConn, batchUUID)) {
			result.put("status", "-2");
			result.put("msg", "系统繁忙请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "下架成功");
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * PC端用户获取领取的优惠劵列表
	 */
	public String getUserCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		String searchWords = request.getParameter("searchWords");
		//1.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		//2.获取查询条件
		Map<String,Object> param = new HashMap<String,Object>();//封装查询条件
		param.put("range", request.getParameter("range"));
		param.put("type", request.getParameter("type"));
		param.put("status", request.getParameter("status"));
		param.put("searchWords",searchWords);
		param.put("vipUUID", vipUUID);
		//3.查询结果
		List<CouponUserList> userCouponList = couponService.findUserCouponList(sourceId, closeConn, param, page);
		
		if (page == null) {
			result.put("rows", userCouponList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (userCouponList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", userCouponList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	
	
	
	
	
	

	/**
	 * 删除用户的优惠劵
	 */
	public String deleteUserCoupons(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String couponUUIDs = request.getParameter("couponUUIDs");
		String[] paramUUids = couponUUIDs.split(",");
		
		//删除指定的优惠劵
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		boolean del = couponService.deleteCouponByCouponUUID(sourceId, closeConn, paramUUids);
		if (!del) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}

	
	
	
	
	
	
	
	
	/**
	 * 获取优惠劵详细信息
	 */
	public String getCouponInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String batchUUID = request.getParameter("batchUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if("".equals(batchUUID)) {
			result.put("status", "-1");
			result.put("msg", "优惠劵的batchUUID不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//查询
		CouponOfIssuer issuer = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		if(issuer == null) {
			result.put("status", "-1");
			result.put("msg", "未查询到相关信息");
			return JsonUtil.ObjToJson(result);
		}
		
		//判断获取指定信息
		if(issuer.getFromType() == 1) {//平台
			//1.判断获取指定店铺信息
			if(!StringUtils.isNullOrEmpty(issuer.getMerchantUUID())) {
				List<BusinessApply> businessInfo = couponService.findBusinessApplyListByBusinessUUID(sourceId, closeConn, issuer.getMerchantUUID());
				result.put("businessInfo", businessInfo);
			}
			//2.判断获取分类信息(1-指定分类商品，2-不指定分类商品) (1-指定商品，2-不指定商品)
			if(issuer.getGoodsSortCondition() == 1 && issuer.getGoodsUseCondition() == 2) {
				ItemCat cat = couponService.findItemCatInfo(sourceId, closeConn, issuer.getGoodsCatUUID());
				result.put("catInfo", cat);
			}
			
			//3.判断获取商品信息
			if(issuer.getGoodsUseCondition() == 1 ) {
				Item item = couponService.getGoodInfo(sourceId, closeConn, issuer.getGoodsUUID());
				result.put("itemInfo", item);
			}
		}
		
		
		if(issuer.getFromType() == 2) {//商家
			//2.判断获取分类信息(1-指定分类商品，2-不指定分类商品) (1-指定商品，2-不指定商品)
			if(issuer.getGoodsSortCondition() == 1 && issuer.getGoodsUseCondition() == 2) {
				ItemCat cat = couponService.findItemCatInfo(sourceId, closeConn, issuer.getGoodsCatUUID());
				result.put("catInfo", cat);
			}
			
			//3.判断获取商品信息
			if(issuer.getGoodsUseCondition() == 1 ) {
				Item item = couponService.getGoodInfo(sourceId, closeConn, issuer.getGoodsUUID());
				result.put("itemInfo", item);
			}
		}
		result.put("status", "1");
		result.put("msg", "查询成功");
		result.put("issuer", issuer);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取指定分类的信息列表
	 */
	public String getSortInfoList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if(StringUtils.isNullOrEmpty(catUUID)) {
			result.put("status", "-1");
			result.put("msg", "分类的uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//查询
		List<ItemCat> sortInfoList = couponService.findSortInfoList(sourceId, closeConn, catUUID);
		if(sortInfoList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "未查询到相关信息");
			return JsonUtil.ObjToJson(result);
		}

		result.put("status", "1");
		result.put("msg", "查询到相关信息");
		result.put("sortInfoList", sortInfoList);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取指定商品的信息列表
	 */
	public String getGoodsInfoList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String  goodsUUID = request.getParameter("goodsUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if(StringUtils.isNullOrEmpty(goodsUUID)) {
			result.put("status", "-1");
			result.put("msg", "商品的uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//查询
		List<ItemParam>goodsInfoList = couponService.findGoodsInfoList(sourceId, closeConn, goodsUUID);
		if(goodsInfoList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "未查询到相关信息");
			return JsonUtil.ObjToJson(result);
		}

		result.put("status", "1");
		result.put("msg", "查询到相关信息");
		result.put("goodsInfoList", goodsInfoList);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取优惠劵二维码图片
	 */
	public String getCouponQRcodeImgList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String batchUUID = request.getParameter("batchUUID");
		Integer downNuber = Integer.parseInt(request.getParameter("downNumber"));
		
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if(StringUtils.isNullOrEmpty(batchUUID)) {
			result.put("status", "-1");
			result.put("msg", "优惠劵批次uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		
		//获取该批次用户指定数量的可下载的优惠劵二维码（已使用的不可再次下载）
		List<CouponOffline> couponOfflines = couponService.getIsDownQRcodeImg(sourceId, closeConn, batchUUID,downNuber);
		if(couponOfflines == null || couponOfflines.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "未查询到相关数据");
			return JsonUtil.ObjToJson(result);
		}
		
		//1.将要下载的文件打包到一个文件夹
		String basePath = request.getSession().getServletContext().getRealPath("/");
		//String basePath = request.getParameter("basePath");
		String filename = UUID.randomUUID().toString().replaceAll("-", "");
		String qrCodePath = ReadProperties.getValue("couponQRcodeImg");//读取优惠劵二维码保存路径
		for (CouponOffline couponOffline : couponOfflines) {
			FileInputStream inputStream = null;       //创建字节输入流
			FileOutputStream outputStream = null;   	//创建字节输出流
			try {
				filename = URLEncoder.encode(filename, "UTF-8");//文件名， 转换中文否则可能会产生乱码
				String imgurl = basePath+couponOffline.getQRcodeImg();//图片路径
				String newimgurl = basePath+qrCodePath+filename+"/"+couponOffline.getCouponNumber()+".png";//图片新的路径
				File f = new File(basePath+qrCodePath+filename);
				if(!f.exists()) {
					f.mkdir();
				}
				inputStream = new FileInputStream(imgurl);	//指定字节输入流要读取的文件
				outputStream = new FileOutputStream(newimgurl);
				byte [] buf = new byte[1024]; 
				int len=0; 
				while((len=inputStream.read(buf))!=-1) {	//循环读取，当读到文件末尾时，再往下读会读取不到文件，这时read方法会返回-1，表示后面没有内容，已经读完；		
					outputStream.write(buf, 0, len);	//用文件输出流的write方法开始往外写，buf表示将字节数组byte写出，0表示从数组的第0个位置开始写，len表示写出多少个
				}
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//2.将文件夹压缩
		//boolean flag = false;
		String file = basePath+qrCodePath+filename;//被压缩文件
		File sourceFile = new File(file);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			File zipFile = new File(file +".zip");
			if(zipFile.exists()){
				System.out.println("该zip文件已存在");
			}else{
				File[] sourceFiles = sourceFile.listFiles();
				if(null == sourceFiles || sourceFiles.length<1){
					System.out.println("待压缩的文件目录里面不存在文件，无需压缩");
				}else{
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024*10];
					for(int i=0;i<sourceFiles.length;i++){
						//创建ZIP实体，并添加进压缩包
						ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						//读取待压缩的文件并写进压缩包里
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024*10);
						int read = 0;
						while((read=bis.read(bufs, 0, 1024*10)) != -1){
							zos.write(bufs,0,read);
						}
					}
					//flag = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			//关闭流
			try {
				if(null != bis) bis.close();
				if(null != zos) zos.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		//TODO 压缩成功后删除图片文件夹 String file = basePath+qrCodePath+filename;//被压缩文件
		//删除被打包文件夹
		File f = new File(basePath+qrCodePath+filename);
		if(f.exists()) {
			f.delete();
		}
		
		String filezip = qrCodePath+filename+".zip";
		result.put("status", "1");
		result.put("filezip", filezip);
		return JsonUtil.ObjToJson(result);
	}
	
	
	
	
	/**
	 * 分配优惠劵给用户
	 * @throws Exception 
	 */
	private synchronized boolean allotCouponToUser(String sourceId,boolean closeConn,CouponOfUser user,Integer userIntegral,Integer integration) throws Exception {
		//1.获取未领取的优惠劵uuid
		List<Coupon>  couponList = couponService.getCouponUUIDByBatchUUID("jdbcread", true, user.getBatchUUID());
		if(couponList == null || couponList.size()==0) {
			return false;//已经被领取完
		}
		
		//2.遍历随机分配给用户
		Random ran = new Random();
		int index = ran.nextInt(couponList.size());
		Coupon coupon = couponList.get(index);
		String couponUUID = coupon.getCouponUUID();
		user.setCouponUUID(couponUUID);
		
		//3.修改用户积分
		String batchUUID = user.getBatchUUID();
		if (!integralService.updateUserIntegral(sourceId, closeConn, userIntegral, user.getVipUUID())) {
			throw new Exception("用户积分修改失败");
		}
		//4.修改分配给用户的该张优惠劵状态
		if(!couponService.upCouponIsGet(sourceId, closeConn, couponUUID)) {
			throw new Exception("优惠劵状态修改失败");
		}
		//5.修改该批优惠劵的领取量，库存
		//5.1.获取该批次优惠劵的库存和领取量
		CouponOfIssuer couponInfo = couponService.findCouponInfo(sourceId, closeConn, batchUUID);
		Integer getNumbers = couponInfo.getGetNumbers();
		Integer stock = couponInfo.getStock();
		getNumbers += 1;//领取量加1
		stock -= 1;//库存量减1
		//5.2.修改库存和领取量
		if(!couponService.updateCouponGetNumsAndStock(sourceId, closeConn, batchUUID, stock, getNumbers)) {
			throw new Exception("优惠劵库存和领取量修改失败");
		}
		//6.数据写入
		if(!couponService.allotCouponToUser(sourceId, closeConn, user)) {
			throw new Exception("数据写入失败");
		}
		
		//若为需积分领去还需，记录积分兑换记录
		if(integration > 0) {
			IntegralRecordOfUser recordOfUser = new IntegralRecordOfUser();
			recordOfUser.setIntegralFrom(3);//1-账户充值，2-购买商品，3-积分兑换
			recordOfUser.setVipUUID(user.getVipUUID());
			recordOfUser.setIntegration(integration);
			recordOfUser.setMoneyOrIntegral(integration+"");
			recordOfUser.setIntegralType(2);//消耗积分
			recordOfUser.setChangeTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			if(!integralService.dataAccess(sourceId, closeConn, recordOfUser)) {
				throw new Exception("数据写入失败");
			}
		}
		return true;
	}
	
	/**
	 * 优惠劵编号
	 * @param releaseNumber 发行数量
	 * @param e 循环变量i
	 * @return
	 */
	private String getCouponNumber(Integer releaseNumber,int e) {
		String b = String.valueOf(releaseNumber);
		int len = b.length();
		String regex = "%0"+len+"d";
		String format = String.format(regex, e+1);
		return format;
	}
	
	/**
	 * 批量下载图片并压缩为zip
	 */
	private void downloadImg(List<String> imgUrl,HttpServletResponse response) {
		String[] files = new String[imgUrl.size()];//
		imgUrl.toArray(files);//将list转化为对应string[]
		try {
			long nowTimeString = System.currentTimeMillis();//获取当前系统时间毫秒值
			String downloadFileName = nowTimeString+".zip";//文件名
			downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");//
			response.setContentType("application/octet-stream");// 指明response的返回对象是文件流 
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFileName);//设置在下载框默认显示的文件名
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
			for (int i = 0; i < files.length; i++) {
				URL url = new URL(files[i]);
				String[] fileNameArr = files[i].split("/");
				String fileName = fileNameArr[fileNameArr.length-1];
				zos.putNextEntry(new ZipEntry(fileName));
				InputStream fis = url.openConnection().getInputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = fis.read(buffer)) != -1) {
					zos.write(buffer, 0, len);
				}
				fis.close();
			}
			zos.flush();
			zos.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * 获取用户旗下店铺信息
	 */
	public String getUserEntityInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请重新登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
			if("0".equals(userInfo.getUserType())) {//平台管理员
				//获取所有零售店铺
				List<BusinessApply> businessApplies = couponService.getAllBusinessInfo(sourceId, closeConn);
				if(businessApplies == null || businessApplies.size() == 0) {
					result.put("status", "-1");
					result.put("msg", "还未有入驻店铺");
					return JsonUtil.ObjToJson(result);
				}
				result.put("status", "1");
				result.put("msg", "店铺信息获取成功");
				result.put("businessApplies", businessApplies);
				return JsonUtil.ObjToJson(result);
			}
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		//根据vipUUID查询其相关店铺信息
		List<BusinessApply> businessApplies = couponService.getBusinessInfo(sourceId, closeConn, vipUUID);
		if(businessApplies == null || businessApplies.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "您还没有任何店铺");
			return JsonUtil.ObjToJson(result);
		}

		result.put("status", "1");
		result.put("msg", "店铺信息获取成功");
		result.put("businessApplies", businessApplies);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 *  获取店铺发布优惠劵的使用详情记录列表
	 */
	public String getDetailCouponList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		String batchUUID = request.getParameter("batchUUID");
		if(StringUtils.isNullOrEmpty(batchUUID)) {
			result.put("status", "-1");
			result.put("msg", "批次uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//1.查询优惠劵相关信息
		CouponOfIssuer issuer = couponService.findCouponInfoByBatchUUID(sourceId, closeConn, batchUUID);
		if(issuer.getIsRelease() == 2) {//未上架
			result.put("status", "-1");
			result.put("msg", "该优惠劵未上架");
			return JsonUtil.ObjToJson(result);
		}
		
		// 2.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		// 查询
		if (issuer.getReleaseType() == 1) {// 线上
			List<CouponUserList> couponlists = couponService.getOnLineCouponList(sourceId, closeConn, batchUUID,page);
			if (page == null) {
				result.put("rows", couponlists);
				return JsonUtil.ObjToJson(result);
			} else {
				result.put("total", page.getTotalRecord());
				if (couponlists == null) {
					result.put("rows", 0);
				} else {
					result.put("rows", couponlists);
				}
				result.put("page", page);
			}
		}
		
		if (issuer.getReleaseType() == 2) {// 线下
			List<CouponUserList> couponlists = couponService.getOffLineCouponList(sourceId, closeConn, batchUUID, page);
			if (page == null) {
				result.put("rows", couponlists);
				return JsonUtil.ObjToJson(result);
			} else {
				result.put("total", page.getTotalRecord());
				if (couponlists == null) {
					result.put("rows", 0);
				} else {
					result.put("rows", couponlists);
				}
				result.put("page", page);
			}
		}
		
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 获取零售店铺列表
	 */
	public String getBusinessList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//获取
		List<BusinessApply> businessList = couponService.findBusinessApplyList(sourceId, closeConn);
		if(businessList == null || businessList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "未查询到任何零售商");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "零售商获取成功");
		result.put("businessList", businessList);
		return JsonUtil.ObjToJson(result);
	}

}
