package com.xryb.zhtc.manage;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.ws.addressing.AttributedAnyType;
import org.apache.poi.ss.formula.functions.Odd;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.ApplyMsg;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.BusinessDistribution;
import com.xryb.zhtc.entity.BusinessGrade;
import com.xryb.zhtc.entity.BusinessMiddle;
import com.xryb.zhtc.entity.Collection;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.entity.OfflineDetailed;
import com.xryb.zhtc.entity.OfflineOrders;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.SettledMoney;
import com.xryb.zhtc.entity.SlideImg;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WholesaleMarket;
import com.xryb.zhtc.service.IBusinessService;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.service.impl.BusinessServiceImpl;
import com.xryb.zhtc.service.impl.ItemParamServiceImpl;
import com.xryb.zhtc.service.impl.ItemServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;
import com.xryb.zhtc.util.UpFileUtil;
import dbengine.util.Page;
import spark.annotation.Auto;

public class BusinessMng {
	@Auto(name = BusinessServiceImpl.class)
	private IBusinessService iBusinessService;
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=ItemServiceImpl.class)
	private IItemService itemService;
	
	@Auto(name=ItemParamServiceImpl.class)
	private IItemParamService paramService;
	
	
	//计算距离
		public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
	        double radLat1 = rad(lat1);  
	        double radLat2 = rad(lat2);  
	        double a = radLat1 - radLat2;  
	        double b = rad(lng1) - rad(lng2);  
	        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
	                + Math.cos(radLat1) * Math.cos(radLat2)  
	                * Math.pow(Math.sin(b / 2), 2)));  
	        s = s * EARTH_RADIUS;
	        //System.out.println("计算结果s0=="+s);
	        BigDecimal c = new BigDecimal(s);
	        s = c.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//保留3位小数
	        //System.out.println("计算结果保留1位小数后s1=="+s);
	        return s;  
	    }
	    
	    private static double EARTH_RADIUS = 6378.137;  

	    private static double rad(double d) {  
	        return d * Math.PI / 180.0;  
	    }  
	
	
	/**
	 * 零售商供应商入驻PC端
	 * @throws Exception 
	 */
	public Object businessSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		UpFileUtil uy =new UpFileUtil();
		Map map =new HashMap();
	    Map<String, String> maps = new HashMap<String,String>();
	    String path = ReadProperties.getValue("getBusinessImg");
	    String getPath = ReadProperties.getValue("businessImg");
		String fileName = uy.saveHttpUpFiles(request, path, map, maps,"0");
		BusinessApply businessApply = null;
		if(fileName == null) {
			businessApply = (BusinessApply) ReqToEntityUtil.reqToEntity(request, BusinessApply.class);
	    }else {
	    	businessApply = (BusinessApply)ReqToEntityUtil.MapToEntity(map,BusinessApply.class);
	    	businessApply.setBankImg(getPath+maps.get("bankImg"));
	    	businessApply.setStorefrontImg(getPath+maps.get("storefrontImg"));
	    	businessApply.setBusinessImg(getPath+maps.get("businessImg"));
	    	businessApply.setIdJustImg(getPath+maps.get("idJustImg"));
	    	businessApply.setIdBackImg(getPath+maps.get("idBackImg"));
	    }
		
		//商品分类
		if(businessApply.getCatUUID() != null) {
			String[] catArr = businessApply.getCatUUID().split(",");
			for(int i = 0; i < catArr.length; i++) {
				if(i == 0) {
					businessApply.setCatUUID(catArr[i]);
				}else {
					businessApply.setClassification(catArr[i]);
				}
			}
		}
		
		//批发市场
		if(businessApply.getPfUUID() != null) {
			String[] pfUUIDArr = businessApply.getPfUUID().split(",");
			for(int i = 0; i < pfUUIDArr.length; i++) {
				if(i == 0) {
					businessApply.setPfUUID(pfUUIDArr[i]);
				}else {
					businessApply.setPfName(pfUUIDArr[i]);
				}
			}
		}
		
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		String vipName = "";
		Map<String, String> msgMap = new HashMap<String,String>();
		synchronized(this) {
			//查询该用户是否申请过（不能重复申请）
			/*BusinessApply apply = null;
			apply = iBusinessService.apply(sourceId, closeConn, vipUUID, businessApply.getApplyType());
			if(apply != null) {
				if("0".equals(apply.getApprovalStatus())) {//待审批
					msgMap.put("msg", "您申请的信息正在审核中不能重复申请");
					return JsonUtil.ObjToJson(msgMap);
				}else if("1".equals(apply.getApprovalStatus())) {//审批通过
					msgMap.put("msg", "不能重复申请");
					return JsonUtil.ObjToJson(msgMap);
				}
			}*/
			
			//向后台提交数据
			if(businessApply.getId() == null) {
				if(userInfo == null && vipInfo == null) {
					msgMap.put("msg", "系统繁忙请刷新页面重试");
					return JsonUtil.ObjToJson(msgMap);
				}
				if(userInfo != null) {
					if(businessApply.getVipUUID() == "") {
						vipUUID = userInfo.getUserUUID();
						vipName = userInfo.getUserName();
					}else {
						VipInfo info = null;
						info = iBusinessService.vipInfoId(sourceId, closeConn, businessApply.getVipUUID());
						vipUUID = info.getVipUUID();
						vipName = info.getVipName();
					}
				}
				if(vipInfo != null) {
					vipUUID = vipInfo.getVipUUID();
					vipName = vipInfo.getVipName();
				}
				businessApply.setBusinessUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				businessApply.setApplyTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd"));
				businessApply.setApprovalStatus("0");
				businessApply.setShopState("1");
				businessApply.setVipUUID(vipUUID);
				businessApply.setVipName(vipName);
			}else {
				ApplyMsg applyMsg = new ApplyMsg();
				BusinessApply businessApplyTow = null;
				businessApplyTow = iBusinessService.applyId(sourceId, closeConn, businessApply.getBusinessUUID());
				businessApply.setApprovalTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
				if("1".equals(businessApply.getApprovalStatus())) {//审批通过
					jedisClient.newAccount(sourceId, closeConn, businessApplyTow.getApplyType(), businessApplyTow.getBusinessUUID(), businessApplyTow.getBusinessName(), businessApplyTow.getBusinessPhone(), businessApplyTow.getVipUUID());//生成商家钱包
					String time = businessApply.getApprovalTime().substring(0, 4);
					int getTime = Integer.valueOf(time);
					getTime = getTime+1;
					StringBuilder sub = new StringBuilder();
					sub.append(getTime);
					sub.append("-");
					sub.append(businessApply.getApprovalTime().substring(5, 7));
					sub.append("-");
					sub.append(businessApply.getApprovalTime().substring(8, 10));
					System.out.println("time:"+sub);
					businessApply.setDueTime(sub.toString());
					//获取会员手机号码（手机号码为登录名后六位数为登陆 密码）
					VipInfo vInfo = null;
					vInfo = iBusinessService.vipInfoId(sourceId, closeConn, businessApplyTow.getVipUUID());
					String phone = vInfo.getVipMobile();
					String password = phone.substring(phone.length()-6);
					applyMsg.setLoginName(phone);
					applyMsg.setLoginPassword(password);
					applyMsg.setUrlAdd("www.gzyhcg.com/zhtc/admin/index.jsp");
				}
				
				//根据商家UUID查询是否有上一条通知
				ApplyMsg applyMsgTow = null;
				applyMsgTow = iBusinessService.dateUUIDAll(sourceId, closeConn, businessApplyTow.getBusinessUUID());
				if(applyMsgTow != null) {
					iBusinessService.msgDel(sourceId, closeConn, applyMsgTow.getMsgUUID());
				}
				
				
				//添加消息通知
				applyMsg.setMsgUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				applyMsg.setVipUUID(businessApplyTow.getVipUUID());
				applyMsg.setDateUUID(businessApplyTow.getBusinessUUID());
				applyMsg.setState(businessApply.getApprovalStatus());
				applyMsg.setMsgRead("0");
				applyMsg.setMonthDay(DateTimeUtil.formatDate(new Date(), "MM-dd"));
				if(businessApply.getReason() == null || businessApply.getReason() == "") {
					applyMsg.setMessage(businessApplyTow.getBusinessName());
				}else {
					applyMsg.setMessage(businessApplyTow.getBusinessName()+businessApply.getReason());
				}
				applyMsg.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
				iBusinessService.msgSave(sourceId, closeConn, applyMsg);
			}
			if(iBusinessService.businessSave(sourceId, closeConn, businessApply)) {
				msgMap.put("msg", "操作成功");
			}else {
				msgMap.put("msg", "操作失败");
			}
			return JsonUtil.ObjToJson(msgMap);
			
		}
	}
	
	/**
	 * 零售商供应商入驻查询
	 */
	public Object businessAppliesList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		String condition = request.getParameter("condition");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		if(condition != null || condition != "") {
			findMap.put("condition", condition);
		}
		List<BusinessApply> lyList = iBusinessService.businessAppliesList(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("lyList", lyList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(lyList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", lyList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 查询我申请的商家
	 */
	public Object myBusiness(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String vipUUID = "";
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo != null) {
			if("0".equals(userInfo.getUserType())) {
				vipUUID = "";
			}else {
				vipUUID = userInfo.getUserUUID();
			}
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
			if(vipUUID == "" || vipUUID == null) {
				return null;
			}
		}
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		findMap.put("vipUUID", vipUUID);
		if(null != request.getParameter("condition")) {
			findMap.put("condition", request.getParameter("condition"));
		}
		List<BusinessApply> lyList = iBusinessService.businessAppliesList(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("lyList", lyList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(lyList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", lyList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 根据商家UUID查询
	 */
	public String applyId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String businessUUID = request.getParameter("businessUUID");
		BusinessApply businessApply = null;
		businessApply = iBusinessService.applyId(sourceId, closeConn, businessUUID);
		return JsonUtil.ObjToJson(businessApply);
	}
	
	/**
	 * 启用或停用
	 */
	public Object shopStateSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<>();
		String businessUUID = request.getParameter("businessUUID");
		String shopState = request.getParameter("shopState");
		if("".equals(businessUUID) || "".equals(shopState)) {
			map.put("msg", "系统繁忙");
		}
		if(iBusinessService.shopStateSave(sourceId, closeConn, businessUUID, shopState)) {
			map.put("msg", "操作成功");
		}else {
			map.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(map); 
	}
	
	/**
	 * 商家入驻费用设置新增或修改
	 */
	public Object settledSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		SettledMoney settledMoney = (SettledMoney) ReqToEntityUtil.reqToEntity(request, SettledMoney.class);
		Map<String, String> msgMap = new HashMap<>();
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		String userUUID = "";
		String userName = "";
		if(userInfo == null) {
			msgMap .put("msg", "无权操作");
			return JsonUtil.ObjToJson(msgMap);
		}
		if(userInfo != null) {
			userUUID = userInfo.getUserUUID();
			userName = userInfo.getUserName();
		}
		
		List<SettledMoney> moneyList = iBusinessService.settledAll(sourceId, closeConn, null, null);
		for(int i = 0; i < moneyList.size(); i++) {
			SettledMoney settledMoneyTow = null;
			settledMoneyTow = moneyList.get(i);
			if(settledMoneyTow.getSettledType().equals(settledMoney.getSettledType())) {
				msgMap.put("msg", "添加失败不能重复添加");
				return JsonUtil.ObjToJson(msgMap);
			}
		}
		
		if(settledMoney.getId() == null) {
			settledMoney.setSettledUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			settledMoney.setUserUUID(userUUID);
			settledMoney.setUserName(userName);
			settledMoney.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}else {
			settledMoney.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}
		if(iBusinessService.settledSave(sourceId, closeConn, settledMoney)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 商家入驻费用设置查询
	 */
	public Object settledAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, SettledMoney.class);
		List<SettledMoney> lyList = iBusinessService.settledAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("lyList", lyList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(lyList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", lyList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 商家入驻费用设置根据ID查询
	 */
	public String settledId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String settledUUID = request.getParameter("settledUUID");
		SettledMoney settledMoney = null;
		settledMoney = iBusinessService.settledId(sourceId, closeConn, settledUUID);
		return JsonUtil.ObjToJson(settledMoney); 
	}
	
	/**
	 * 商家入驻费用设置删除
	 */
	public boolean settledDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String settledUUID = request.getParameter("settledUUID");
		String[] settledUUIDArr = settledUUID.split(",");
		for(String id:settledUUIDArr) {
			iBusinessService.settledDel(sourceId, closeConn, id);
		}
		return true;
	}
	
	/**
	 * 查询订单
	 */
	public Object orderAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		String condition = request.getParameter("condition");
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Order.class);
		if(condition != null || condition != "") {
			findMap.put("condition", condition);
		}
		List<Order> lyList = iBusinessService.orderAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("lyList", lyList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(lyList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", lyList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 销售额
	 */
	public String totalPriceSum(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String entityUUID = request.getParameter("entityUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("entityUUID", entityUUID);
 		Map map = iBusinessService.totalPriceSum(sourceId, closeConn, findMap);
		if(map.get("totalPrice") == null) {
			map.put("totalPrice", 0);
		}
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 订单数量
	 */
	public String orderNumber(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String entityUUID = request.getParameter("entityUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("entityUUID", entityUUID);
		Map map = iBusinessService.orderNumber(sourceId, closeConn, findMap);
		if(map.get("count") == null) {
			map.put("count", 0);
		}
		return JsonUtil.ObjToJson(map); 
	}
	
	/**
	 * 下单人数
	 */
	public String placeNumber(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String entityUUID = request.getParameter("entityUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("entityUUID", entityUUID);
		Map map = iBusinessService.placeNumber(sourceId, closeConn, findMap);
		if(map.get("rs") == null) {
			map.put("rs", 0);
		}
		return JsonUtil.ObjToJson(map); 
	}
	
	/**
	 * 设置分销商新增或修改
	 */
	public Object distributionSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> msgMap = new HashMap<>();
		BusinessDistribution businessDistribution = (BusinessDistribution) ReqToEntityUtil.reqToEntity(request, BusinessDistribution.class);
		BusinessDistribution businessDistributionTwo = null;
		businessDistributionTwo = iBusinessService.distributionId(sourceId, closeConn, businessDistribution.getBusinessUUID());
		
		if(businessDistributionTwo == null) {//新增
			businessDistribution.setDistributionUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			businessDistribution.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}else {//修改
			businessDistribution.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}
		if(iBusinessService.distributionSave(sourceId, closeConn, businessDistribution)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 设置分销商根据UUID查询
	 */
	public String distributionUUID(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String businessUUID = request.getParameter("businessUUID");
		BusinessDistribution businessDistribution = null;
		BusinessDistribution businessDistributionTwo = null;
		businessDistribution = iBusinessService.distributionId(sourceId, closeConn, businessUUID);
		if(businessDistribution != null) {
			businessDistributionTwo = iBusinessService.distributionUUID(sourceId, closeConn, businessDistribution.getDistributionUUID());
		}
		return JsonUtil.ObjToJson(businessDistributionTwo);
	}
	
	/**
	 * App查询订单管理
	 */
	public Object orderAllApp(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String record = request.getParameter("rows");//每页多少条
		String pageNumber = request.getParameter("page");//当前页
		String condition = request.getParameter("condition");
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Order.class);
		if(condition != null || condition != "") {
			findMap.put("condition", condition);
		}
		List<Map<String, Object>> listMap = iBusinessService.orderAllApp(sourceId, closeConn, null, findMap);
		//排序
		Collections.sort(listMap, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> p1, Map<String, Object> p2) {
            	Integer time1 = Integer.valueOf(p1.get("id").toString());
            	Integer time2 = Integer.valueOf(p2.get("id").toString());
            	return time1.compareTo(time2);
            }
        });
		List<Map<String, Object>> list = new ArrayList<>();
		Map map = new HashMap();
		//计算总页
		int len = listMap.size();
		len = len-1;
		int row = Integer.parseInt(record);
		int count = len/row;
		int pageCount = (int) Math.floor(count);
		pageCount = pageCount+1;//总页数
		int page = Integer.parseInt(pageNumber);
		if(pageCount <= page) {//如果总页数小于等于当前页
			for(int i = (page-1)*row; i < listMap.size(); i++) {
				list.add(listMap.get(i));
			}
		}else {
			for(int i = (page-1)*row; i < page*row; i++) {
				list.add(listMap.get(i));
			}
		}
		map.put("pageCount", pageCount);
		map.put("page", page);
		map.put("list", list);
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 总销售-下单人数-今日订单App查询
	 */
	public Object dataStatistics(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String entityUUID = request.getParameter("entityUUID");
		String createTime = request.getParameter("createTime");
		String closeTime = request.getParameter("closeTime");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("entityUUID", entityUUID);
		findMap.put("createTime", createTime);
		findMap.put("closeTime", closeTime);
		Map map = new HashMap();
		map.put("totalPrice", iBusinessService.totalPriceSum(sourceId, closeConn, findMap));//销售额
		map.put("count", iBusinessService.orderNumber(sourceId, closeConn, findMap));//订单数量
		map.put("rs", iBusinessService.placeNumber(sourceId, closeConn, findMap));//下单人数
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * uni-app图片上传
	 */
	public Object uploadImg(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		UpFileUtil uy =new UpFileUtil();
		Map map =new HashMap();
	    Map<String, String> maps = new HashMap<String,String>();
	    String path = ReadProperties.getValue("getBusinessImg");
	    String getPath = ReadProperties.getValue("businessImg");
		String fileName = uy.saveHttpUpFiles(request, path, map, maps,"0");
		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("url", getPath+fileName);
		msgMap.put("type", type);
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * app申请商家入驻
	 * @throws Exception 
	 */
	public Object businessAppSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> msgMap = new HashMap<>();
		BusinessApply businessApply = (BusinessApply) ReqToEntityUtil.reqToEntity(request, BusinessApply.class);
		synchronized(this) {
			/*//查询该用户是否申请过（不能重复申请）
			BusinessApply apply = null;
			apply = iBusinessService.apply(sourceId, closeConn, businessApply.getVipUUID(), businessApply.getApplyType());
			if(apply != null) {
				if("0".equals(apply.getApprovalStatus())) {//待审批
					msgMap.put("msg", "您申请的信息正在审核中不能重复申请");
					return JsonUtil.ObjToJson(msgMap);
				}else if("1".equals(apply.getApprovalStatus())) {//审批通过
					msgMap.put("msg", "不能重复申请");
					return JsonUtil.ObjToJson(msgMap);
				}
			}*/
			//向后台提交数据
			if(businessApply.getId() == null) {
				businessApply.setBusinessUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				businessApply.setApplyTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd"));
				businessApply.setApprovalStatus("0");
				businessApply.setShopState("1");
			}else {
				ApplyMsg applyMsg = new ApplyMsg();
				BusinessApply businessApplyTow = null;
				businessApplyTow = iBusinessService.applyId(sourceId, closeConn, businessApply.getBusinessUUID());
				businessApply.setApprovalTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
				if("1".equals(businessApply.getApprovalStatus())) {//审批通过
					jedisClient.newAccount(sourceId, closeConn, businessApplyTow.getApplyType(), businessApplyTow.getBusinessUUID(), businessApplyTow.getBusinessName(), businessApplyTow.getBusinessPhone(), businessApplyTow.getVipUUID());//生成商家钱包
					String time = businessApply.getApprovalTime().substring(0, 4);
					int getTime = Integer.valueOf(time);
					getTime = getTime+1;
					StringBuilder sub = new StringBuilder();
					sub.append(getTime);
					sub.append("-");
					sub.append(businessApply.getApprovalTime().substring(5, 7));
					sub.append("-");
					sub.append(businessApply.getApprovalTime().substring(8, 10));
					System.out.println("time:"+sub);
					businessApply.setDueTime(sub.toString());
					//获取会员手机号码（手机号码为登录名后六位数为登陆 密码）
					VipInfo vInfo = null;
					vInfo = iBusinessService.vipInfoId(sourceId, closeConn, businessApplyTow.getVipUUID());
					String phone = vInfo.getVipMobile();
					String password = phone.substring(phone.length()-6);
					applyMsg.setLoginName(phone);
					applyMsg.setLoginPassword(password);
					applyMsg.setUrlAdd("www.gzyhcg.com/zhtc/admin/index.jsp");
				}
				
				//根据商家UUID查询是否有上一条通知
				ApplyMsg applyMsgTow = null;
				applyMsgTow = iBusinessService.dateUUIDAll(sourceId, closeConn, businessApplyTow.getBusinessUUID());
				if(applyMsgTow != null) {
					iBusinessService.msgDel(sourceId, closeConn, applyMsgTow.getMsgUUID());
				}
				
				//添加消息通知
				applyMsg.setMsgUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				applyMsg.setVipUUID(businessApplyTow.getVipUUID());
				applyMsg.setDateUUID(businessApplyTow.getBusinessUUID());
				applyMsg.setState(businessApply.getApprovalStatus());
				applyMsg.setMsgRead("0");
				applyMsg.setMonthDay(DateTimeUtil.formatDate(new Date(), "MM-dd"));
				if(businessApply.getReason() == null || businessApply.getReason() == "") {
					applyMsg.setMessage(businessApplyTow.getBusinessName());
				}else {
					applyMsg.setMessage(businessApplyTow.getBusinessName()+businessApply.getReason());
				}
				applyMsg.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
				iBusinessService.msgSave(sourceId, closeConn, applyMsg);
			}
			if(iBusinessService.businessSave(sourceId, closeConn, businessApply)) {
				msgMap.put("msg", "操作成功");
			}else {
				msgMap.put("msg", "操作失败");
			}
			return JsonUtil.ObjToJson(msgMap);
		}
	}
	
	/**
	 * App根据距离查询商家
	 */
	public Object distanceBusiness(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String record = request.getParameter("rows");//每页多少条
		String pageNumber = request.getParameter("page");//当前页
		String latitude = request.getParameter("latitude");//获取经度
		String longitude = request.getParameter("longitude");//获取纬度
		double latitudeDb = Double.parseDouble(latitude);
		double longitudeDb = Double.parseDouble(longitude);
		double latitudeDbTow;
		double longitudeDbTow;
		List<BusinessApply> listData = new ArrayList<BusinessApply>();
		
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		if(request.getParameter("condition") != "" || request.getParameter("condition") != null) {
			findMap.put("condition", request.getParameter("condition"));
		}
		List<BusinessApply> buList = iBusinessService.businessAppliesList(sourceId, closeConn, null, findMap);
		for(int i = 0; i < buList.size(); i++) {
			BusinessApply businessApply = null;
			businessApply = buList.get(i);
			if(businessApply != null) {
				if(businessApply.getLongitude() != null && businessApply.getLatitude() != null) {
					try {
						latitudeDbTow = Double.parseDouble(businessApply.getLongitude());
						longitudeDbTow = Double.parseDouble(businessApply.getLatitude());
						businessApply.setDistance(this.getDistance(latitudeDb, longitudeDb, latitudeDbTow, longitudeDbTow));
						listData.add(businessApply);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		//排序
		Collections.sort(listData, new Comparator<BusinessApply>() {
            public int compare(BusinessApply p1, BusinessApply p2) {
            	if(p1.getDistance() > p2.getDistance()) {
            		return 1;
            	}
            	if(p1.getDistance() == p2.getDistance()) {
            		return 0;
            	}
            	return -1;
            }
        });
		
		List<BusinessApply> list = new ArrayList<BusinessApply>();
		Map map = new HashMap<>();
		//计算总页数
		int len = listData.size();
		len = len-1;
		int row = Integer.parseInt(record);
		int count = len/row;
		int pageCount = (int) Math.floor(count);
		pageCount = pageCount+1;//总页数
		
		int page = Integer.parseInt(pageNumber);
		if(pageCount <= page){//如果总页数小于等于当前页
			for(int i = (page-1)*row; i < listData.size(); i++) {
				BusinessApply businessApply = null;
				businessApply = listData.get(i);
				list.add(businessApply);
			}
		}else {
			for(int i = (page-1)*row; i < page*row; i++) {
				BusinessApply businessApply = null;
				businessApply = listData.get(i);
				list.add(businessApply);
			}
		}
		//根据商家UUID查询前三条数据
		List<Item> itemList = new ArrayList<>();
		for(int i = 0; i < list.size(); i++) {
			List<Item> item = new ArrayList<>();
			BusinessApply businessApply = null;
			businessApply = list.get(i);
			item = iBusinessService.itemList(sourceId, closeConn, businessApply.getBusinessUUID());
			for(int c = 0; c < item.size(); c++) {
				Item itemTow = null;
				itemTow = item.get(c);
				itemList.add(itemTow);
			}
		}
		map.put("itemList", itemList);
		map.put("pageCount", pageCount);
		map.put("page", page);
		map.put("list", list);
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 小程序点击我的店铺查询该用户是否有店铺
	 */
	public String myBusinessApp(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		List<BusinessApply> buList = iBusinessService.businessAppliesList(sourceId, closeConn, null, findMap);
		BusinessApply businessApply = null;
		for(int i = 0; i < buList.size(); i++) {
			businessApply = buList.get(i);//取第一条数据
			break;
		}
		return JsonUtil.ObjToJson(businessApply);
	}
	
	/**
	 * 小程序我的店铺（今日交易额-总销售额-今日订单）
	 */
	public Object businessDay(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map moneyDayMap = new HashMap<>();
		Map totalPriceSumMap = new HashMap<>();
		Map orderDayMap = new HashMap<>();
		List<Object> list = new ArrayList<>();
		String entityUUID = request.getParameter("businessUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("entityUUID", entityUUID);
		moneyDayMap = iBusinessService.moneyDay(sourceId, closeConn, entityUUID);//今日交易金额
		totalPriceSumMap = iBusinessService.totalPriceSum(sourceId, closeConn, findMap);//总销售额
		orderDayMap = iBusinessService.orderDay(sourceId, closeConn, entityUUID);//今日订单
		if(moneyDayMap.get("moneyDay") == null || moneyDayMap.get("moneyDay") == "") {
			moneyDayMap.put("moneyDay", "0");
		}
		if(totalPriceSumMap.get("totalPrice") == null || totalPriceSumMap.get("totalPrice") == "") {
			totalPriceSumMap.put("totalPrice", "0");
		}
		if(orderDayMap.get("orderDay") == null || orderDayMap.get("orderDay") == "") {
			orderDayMap.put("orderDay", "0");
		}
		Map dataMap = new HashMap<>();
		dataMap.put("moneyDay", moneyDayMap);
		dataMap.put("totalPriceSum", totalPriceSumMap);
		dataMap.put("orderDay", orderDayMap);
		return JsonUtil.ObjToJson(dataMap);
	}
	
	/**
	 * 批发市场查询
	 */
	public Object wholesaleMarketsAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, WholesaleMarket.class);
		List<WholesaleMarket> pfList = iBusinessService.wholesaleMarketsAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("pfList", pfList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(pfList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", pfList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 批发市场新增或修改
	 */
	public Object pfSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		UpFileUtil uy =new UpFileUtil();
		Map map =new HashMap();
	    Map<String, String> maps = new HashMap<String,String>();
	    String path = ReadProperties.getValue("getBusinessImg");
	    String getPath = ReadProperties.getValue("businessImg");
		String fileName = uy.saveHttpUpFiles(request, path, map, maps,"0");
		WholesaleMarket wholesaleMarket = null;
		if(fileName == null) {
			wholesaleMarket = (WholesaleMarket) ReqToEntityUtil.reqToEntity(request, WholesaleMarket.class);
	    }else {
	    	wholesaleMarket = (WholesaleMarket)ReqToEntityUtil.MapToEntity(map,WholesaleMarket.class);
	    	wholesaleMarket.setStorefrontImg(getPath+maps.get("storefrontImg"));
	    }
		
		if(wholesaleMarket.getId() == null) {
			wholesaleMarket.setPfUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			wholesaleMarket.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd"));
		}
		Map<String, String> msgMap = new HashMap<>();
		if(iBusinessService.pfSave(sourceId, closeConn, wholesaleMarket)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 批发市场根据ID查询
	 */
	public String wholesaleMarketId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String pfUUID = request.getParameter("pfUUID");
		WholesaleMarket wholesaleMarket = null;
		wholesaleMarket = iBusinessService.wholesaleMarketId(sourceId, closeConn, pfUUID);
		return JsonUtil.ObjToJson(wholesaleMarket);
	}
	
	/**
	 * 收藏新增或修改
	 */
	public Object collectionSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Collection collectionTow = (Collection) ReqToEntityUtil.reqToEntity(request, Collection.class);
		String type = request.getParameter("type");//收藏类型（0：商品，1：商家）
		Collection collection = null;
		Map<String, String> findMap = new HashMap<>();
		Map<String, String> msgMap = new HashMap<>();
		if("0".equals(type)) {//商品
			findMap.put("commodityUUID", collectionTow.getCommodityUUID());
			findMap.put("vipUUID", collectionTow.getVipUUID());
			collection = iBusinessService.collection(sourceId, closeConn, findMap);
			if(collection != null) {
				msgMap.put("msg", "您已经收藏过了");
				return JsonUtil.ObjToJson(msgMap);
			}
		}
		if("1".equals(type)) {//商家
			findMap.put("externalUUID", collectionTow.getExternalUUID());
			findMap.put("vipUUID", collectionTow.getVipUUID());
			collection = iBusinessService.collection(sourceId, closeConn, findMap);
			if(collection != null) {
				msgMap.put("msg", "您已经收藏过了");
				return JsonUtil.ObjToJson(msgMap);
			}
		}
		
		if(collectionTow.getId() == null) {
			collectionTow.setCollectionUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			collectionTow.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd"));
		}
		if(iBusinessService.collectionSave(sourceId, closeConn, collectionTow)) {
			msgMap.put("msg", "收藏成功");
		}else {
			msgMap.put("msg", "收藏失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 收藏查询
	 */
	public Object collectionAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Collection.class);
		List<Collection> scfList = iBusinessService.collectionAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("scfList", scfList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(scfList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", scfList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 收藏删除
	 */
	public Object collectionDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String collectionUUID = request.getParameter("collectionUUID");
		String[] collectionUUIDArr = collectionUUID.split(",");
		for(String uuid:collectionUUIDArr){
			iBusinessService.collectionDel(sourceId, closeConn, uuid);
		}
		Map<String, String> map = new HashMap<>();
		map.put("msg", "删除成功");
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 入驻消息通知查询
	 */
	public Object applyMsgsAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, ApplyMsg.class);
		List<ApplyMsg> msgList = iBusinessService.applyMsgsAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("msgList", msgList);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(msgList == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", msgList);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 删除消息通知
	 */
	public Object msgDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String msgUUID = request.getParameter("msgUUID");
		Map<String, String> map = new HashMap<>();
		if(iBusinessService.msgDel(sourceId, closeConn, msgUUID)) {
			map.put("msg", "删除成功");
		}else {
			map.put("msg", "删除失败");
		}
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 入驻消息通知修改阅读状态
	 */
	public Object msgReadUp(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String msgUUID = request.getParameter("msgUUID");
		String msgRead = request.getParameter("msgRead");
		Map<String, String> map = new HashMap<>();
		if(iBusinessService.msgReadUp(sourceId, closeConn, msgRead, msgUUID)) {
			map.put("msg", "操作成功");
		}else {
			map.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 入驻消息通知根据ID查询
	 */
	public String applyMsgId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String msgUUID = request.getParameter("msgUUID");
		ApplyMsg applyMsg = null;
		applyMsg = iBusinessService.applyMsgId(sourceId, closeConn, msgUUID);
		return JsonUtil.ObjToJson(applyMsg);
	}
	
	/**
	 * 获取服务器时间
	 * @throws ParseException 
	 */
	public Object getTime(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String time = DateTimeUtil.formatDate(new Date(), "yyyy");
		return JsonUtil.ObjToJson(time);
	}
	
	/**
	 * 商家等级设置新增或修改
	 */
	public Object gradeSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		BusinessGrade businessGrade = (BusinessGrade) ReqToEntityUtil.reqToEntity(request, BusinessGrade.class);
		Map<String, String> msgMap = new HashMap<>();
		if(businessGrade.getId() == null) {
			List<BusinessGrade> businessGradeAll = iBusinessService.businessGradeAll(sourceId, closeConn, null, null);
			for(int i = 0; i < businessGradeAll.size(); i++) {
				BusinessGrade grade = new BusinessGrade();
				grade = businessGradeAll.get(i);
				if(grade.getNumber().equals(businessGrade.getNumber())) {
					msgMap.put("msg", "添加失败不能重复添加等级");
					return JsonUtil.ObjToJson(msgMap);
				}
			}
			businessGrade.setGradeUUID(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		businessGrade.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		if(iBusinessService.gradeSave(sourceId, closeConn, businessGrade)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 商家等级设置删除
	 */
	public boolean gradeDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String gradeUUID = request.getParameter("gradeUUID");
		String[] gradeUUIDArr = gradeUUID.split(",");
		for(String uuid:gradeUUIDArr) {
			iBusinessService.gradeDel(sourceId, closeConn, uuid);
		}
		return true;
	}
	
	/**
	 * 商家等级设置查询
	 */
	public Object businessGradeAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessGrade.class);
		List<BusinessGrade> list = iBusinessService.businessGradeAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 商家等级设置根据UUID查询
	 */
	public Object businessGradeId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String gradeUUID = request.getParameter("gradeUUID");
		BusinessGrade businessGrade = null;
		businessGrade = iBusinessService.businessGradeId(sourceId, closeConn, gradeUUID);
		return JsonUtil.ObjToJson(businessGrade);
	}
	
	/**
	 * 零售商管理查询
	 */
	public Object retailerAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		findMap.put("gradeUUID", request.getParameter("gradeUUID"));
		List<BusinessApply> list = iBusinessService.retailerAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 供应商添加零售商查询
	 */
	public Object retailerChoiceAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		List<BusinessApply> list = iBusinessService.retailerChoiceAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 零售商管理中间表新增或修改
	 */
	public Object middleSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> msgMap = new HashMap<>();
		String supplierUUID = request.getParameter("supplierUUID");
		String gradeNumber = request.getParameter("gradeNumber");
		String retailerUUID = request.getParameter("retailerUUID");
		String[] retailerUUIDArr = retailerUUID.split(",");
		for(String lss:retailerUUIDArr) {
			BusinessMiddle businessMiddle = new BusinessMiddle();
			businessMiddle.setMiddleUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			businessMiddle.setRetailerUUID(lss);
			businessMiddle.setSupplierUUID(supplierUUID);
			businessMiddle.setGradeNumber(gradeNumber);
			businessMiddle.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
			iBusinessService.middleSave(sourceId, closeConn, businessMiddle);
		}
		msgMap.put("msg", "操作成功");
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 零售商管理中间表删除
	 */
	public Object middleDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> msgMap = new HashMap<>();
		String supplierUUID = request.getParameter("supplierUUID");
		String retailerUUID = request.getParameter("retailerUUID");
		String[] retailerUUIDArr = retailerUUID.split(",");
		for(String uuid:retailerUUIDArr) {
			BusinessMiddle businessMiddle = null;
			Map<String, String> map = new HashMap<>();
			map.put("supplierUUID", supplierUUID);
			map.put("retailerUUID", uuid);
			businessMiddle = iBusinessService.businessMiddle(sourceId, closeConn, map);
			iBusinessService.middleDel(sourceId, closeConn, businessMiddle.getMiddleUUID());
		}
		msgMap.put("msg", "操作成功");
		return JsonUtil.ObjToJson(msgMap); 
	}
	
	/**
	 * 线下订单新增或修改
	 * @throws Exception 
	 */
	public Object offlineSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> msgMap = new HashMap<>();
		OfflineOrders offlineOrders = (OfflineOrders) ReqToEntityUtil.reqToEntity(request, OfflineOrders.class);
		BusinessApply businessApply = null;
		OfflineOrders offlineOrdersTow = null;
		Map<String, String> findMap = new HashMap<>();
		findMap.put("businessUUID", offlineOrders.getBusinessUUID());
		findMap.put("itemUUID", offlineOrders.getItemUUID());
		findMap.put("retailerUUID", offlineOrders.getRetailerUUID());
		findMap.put("state", "0");
		offlineOrdersTow = iBusinessService.offlineOrdersParameter(sourceId, closeConn, findMap);
		if(offlineOrdersTow != null) {
			msgMap.put("state", "0");
			msgMap.put("msg", "该订单已存在不能重复下单");
			return JsonUtil.ObjToJson(msgMap);
		}
		if(offlineOrders.getId() == null) {//新增
			String element = BigDecimal.valueOf(Long.valueOf(offlineOrders.getPrice())).divide(new BigDecimal(100)).toString();
			int price = Integer.valueOf(element);//商品单价
			int number = Integer.valueOf(offlineOrders.getNumber());//商品总数
			int totalPrice = price*number;//商品总价
			businessApply = iBusinessService.applyId(sourceId, closeConn, offlineOrders.getBusinessUUID());//获取供应商电话号码
			offlineOrders.setOfflineUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			offlineOrders.setBusinessPhone(businessApply.getBusinessPhone());
			offlineOrders.setPrice(price);
			offlineOrders.setTotalPrice(totalPrice);
			offlineOrders.setState("0");
			offlineOrders.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}
		Map<String,Integer> uuid2count = new HashMap<>();
		uuid2count.put(offlineOrders.getItemUUID(), Integer.valueOf(offlineOrders.getNumber()));
		this.decreseStock(sourceId, closeConn, offlineOrders.getBusinessUUID(), uuid2count);//减少商品库存
		iBusinessService.offlineSave(sourceId, closeConn, offlineOrders);
		msgMap.put("state", "1");
		msgMap.put("msg", "下单成功");
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 查询线下订单
	 */
	public Object offlineOrdersAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, OfflineOrders.class);
		if(request.getParameter("startTime") != null) {
			findMap.put("startTime", request.getParameter("startTime"));//开始时间
		}
		if(request.getParameter("endingTime") != null) {
			findMap.put("endingTime", request.getParameter("endingTime"));//结束时间
		}
		//多条件查询
		if(request.getParameter("condition") != null) {
			findMap.put("condition", request.getParameter("condition"));
			/*findMap.put("offlineUUID", request.getParameter("condition"));
			findMap.put("retailerName", request.getParameter("condition"));
			findMap.put("retailerPhone", request.getParameter("condition"));*/
		}
		List<OfflineOrders> list = iBusinessService.offlineOrdersAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 线下退货
	 * @throws Exception 
	 */
	public Object returnGoods(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> msgMap = new HashMap<>();
		String rNumber = request.getParameter("number");//退货数量
		String offlineUUID = request.getParameter("offlineUUID");//获取订单UUID
		if(rNumber == null || offlineUUID == null) {
			msgMap.put("state", "0");
			msgMap.put("msg", "网络繁忙请稍后重试");
			return JsonUtil.ObjToJson(msgMap);
		}
		boolean isNumber = rNumber.matches("^[0-9]*$");
		if(!isNumber || "0".equals(rNumber)) {
			msgMap.put("state", "0");
			msgMap.put("msg", "输入退货数量有误");
			return JsonUtil.ObjToJson(msgMap);
		}
		int returnNumer = Integer.valueOf(rNumber);
		
		//通过线下订单UUID查询退款明细总数量
		Map returnQuantityMap = iBusinessService.returnQuantityCount(sourceId, closeConn, offlineUUID);
		String returnQuantity = String.valueOf(returnQuantityMap.get("returnQuantity"));
		int returnQuantityNumer = 0;
		if(returnQuantity != "null") {
			int idex = returnQuantity.lastIndexOf(".");
			String index = returnQuantity.substring(0, idex);
			returnQuantityNumer = Integer.valueOf(index);
		}
		
		//根据订单UUID查询线下订单
		OfflineOrders offlineOrders = null;
		Map<String, String> findMap = new HashMap<>();
		findMap.put("offlineUUID", offlineUUID);
		offlineOrders = iBusinessService.offlineOrdersParameter(sourceId, closeConn, findMap);
		if(offlineOrders == null || offlineOrders.getNumber() == "") {
			msgMap.put("state", "0");
			msgMap.put("msg", "网络繁忙请稍后重试");
			return JsonUtil.ObjToJson(msgMap);
		}
		
		//根据所的条件进行计算
		int returnNumberCount = returnNumer+returnQuantityNumer;//退货数量加上明细表退货数量
		String getNumber = offlineOrders.getNumber();//获取线下订单商品总数量
		int number = Integer.valueOf(getNumber);
		if(returnNumberCount > number) {
			int sum = number-returnQuantityNumer;
			String Surplus = String.valueOf(sum);
			msgMap.put("state", "0");
			msgMap.put("msg", "超出退货数量您还能退货："+Surplus+"件");
			return JsonUtil.ObjToJson(msgMap);
		}
		
		//线下订单明细表新增数据
		OfflineDetailed offlineDetailed = new OfflineDetailed();
		if(offlineDetailed.getId() == null) {
			offlineDetailed.setDetailedUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			offlineDetailed.setOfflineUUID(offlineOrders.getOfflineUUID());
			offlineDetailed.setBusinessUUID(offlineOrders.getBusinessUUID());
			offlineDetailed.setItemUUID(offlineOrders.getItemUUID());
			offlineDetailed.setTitle(offlineOrders.getTitle());
			offlineDetailed.setPrice(offlineOrders.getPrice());
			offlineDetailed.setReturnQuantity(String.valueOf(returnNumer));
			offlineDetailed.setReturnPrice(returnNumer*Integer.valueOf(offlineOrders.getPrice()));
			offlineDetailed.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
			offlineDetailed.setRetailerUUID(offlineOrders.getRetailerUUID());
			offlineDetailed.setRetailerName(offlineOrders.getRetailerName());
		}
		
		Map<String, Integer> uuidMap = new HashMap<>();
		uuidMap.put(offlineDetailed.getItemUUID(), Integer.valueOf(returnNumer));
		this.decreseStock(sourceId, closeConn, offlineOrders.getRetailerUUID(), uuidMap);//减少库存
		this.increseStock(sourceId, closeConn, offlineOrders.getBusinessUUID(), uuidMap);//增加商品库存
		if(iBusinessService.detailedSave(sourceId, closeConn, offlineDetailed)) {
			msgMap.put("state", "1");
			msgMap.put("msg", "退货成功");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 查询线下订单明细表
	 */
	public Object offlineDetailedAll(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, OfflineDetailed.class);
		List<OfflineDetailed> list = iBusinessService.offlineDetailedAll(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 商铺线上线下统计交易数据
	 */
	public Object orderSingle(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String businessUUID = request.getParameter("businessUUID");
		Map<String, String> map = new HashMap<>();
		
		//线下今日交易额
		Map getOfflineTodayMoney = iBusinessService.offlineTodayMoney(sourceId, closeConn, businessUUID);
		String offlineTodayMoney = String.valueOf(getOfflineTodayMoney.get("totalPrice"));
		if(offlineTodayMoney != "null") {
			map.put("offlineTodayMoney", offlineTodayMoney);
		}else {
			map.put("offlineTodayMoney", "0");
		}
		
		//线下总交易额
		Map getOfflineCountMoney = iBusinessService.offlineCountMoney(sourceId, closeConn, businessUUID);
		String offlineCountMoney = String.valueOf(getOfflineCountMoney.get("totalPrice"));
		if(offlineCountMoney != "null") {
			map.put("offlineCountMoney", offlineCountMoney);
		}else {
			map.put("offlineCountMoney", "0");
		}
		
		//线下今日订单
		Map getOfflineTodayOrder = iBusinessService.offlineTodayOrder(sourceId, closeConn, businessUUID);
		String offlineTodayOrder = String.valueOf(getOfflineTodayOrder.get("count"));
		map.put("offlineTodayOrder", offlineTodayOrder);
		
		//线下今日退款额
		Map getOfflineTodayRefund = iBusinessService.offlineTodayRefund(sourceId, closeConn, businessUUID);
		String offlineTodayRefund = String.valueOf(getOfflineTodayOrder.get("returnPrice"));
		if(offlineTodayRefund != "null") {
			map.put("offlineTodayRefund", offlineTodayRefund);
		}else {
			map.put("offlineTodayRefund", "0");
		}
		
		//线下总退款额
		Map getOfflineMoneyRefund = iBusinessService.offlineMoneyRefund(sourceId, closeConn, businessUUID);
		String offlineMoneyRefund = String.valueOf(getOfflineMoneyRefund.get("returnPrice"));
		if(offlineMoneyRefund != "null") {
			map.put("offlineMoneyRefund", offlineMoneyRefund);
		}else {
			map.put("offlineMoneyRefund", "0");
		}
		
		//线下退款订单
		Map getOfflineOrderRefund = iBusinessService.offlineOrderRefund(sourceId, closeConn, businessUUID);
		String offlineOrderRefund = String.valueOf(getOfflineOrderRefund.get("count"));
		map.put("offlineOrderRefund", offlineOrderRefund);
		
		//线上今日交易额
		Map getOnLineTodayMoney = iBusinessService.onLineTodayMoney(sourceId, closeConn, businessUUID);
		String onLineTodayMoney = String.valueOf(getOnLineTodayMoney.get("totalPrice"));
		if(onLineTodayMoney != "null") {
			map.put("onLineTodayMoney", onLineTodayMoney);
		}else {
			map.put("onLineTodayMoney", "0");
		}
		
		//线上总销售额
		Map getOnLineCountMoney = iBusinessService.onLineCountMoney(sourceId, closeConn, businessUUID);
		String onLineCountMoney = String.valueOf(getOnLineCountMoney.get("totalPrice"));
		if(onLineCountMoney != "null") {
			map.put("onLineCountMoney", onLineCountMoney);
		}else {
			map.put("onLineCountMoney", "0");
		}
		
		//线上今日订单
		Map getOnLineTodayOrder = iBusinessService.onLineTodayOrder(sourceId, closeConn, businessUUID);
		String onLineTodayOrder = String.valueOf(getOnLineTodayOrder.get("count"));
		map.put("onLineTodayOrder", onLineTodayOrder);
		
		//线上今日退款
		Map getOnLineTodayRefund = iBusinessService.onLineTodayRefund(sourceId, closeConn, businessUUID);
		String onLineTodayRefund = String.valueOf(getOnLineTodayRefund.get("totalPrice"));
		if(onLineTodayRefund != "null") {
			map.put("onLineTodayRefund", onLineTodayRefund);
		}else {
			map.put("onLineTodayRefund", "0");
		}
		
		//线上总退款额
		Map getOnLineMoneyRefund = iBusinessService.onLineMoneyRefund(sourceId, closeConn, businessUUID);
		String onLineMoneyRefund = String.valueOf(getOnLineMoneyRefund.get("totalPrice"));
		if(onLineMoneyRefund != "null") {
			map.put("onLineMoneyRefund", onLineMoneyRefund);
		}else {
			map.put("onLineMoneyRefund", "0");
		}
		
		//线上退款订单
		Map getOnLineOrderRefund = iBusinessService.onLineOrderRefund(sourceId, closeConn, businessUUID);
		String onLineOrderRefund = String.valueOf(getOnLineOrderRefund.get("count"));
		map.put("onLineOrderRefund", onLineOrderRefund);
		
		return JsonUtil.ObjToJson(map);
		
	}
	
	/**
	 * 线下收款统计查询
	 */
	public Object offlineStatistics(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String startTime = request.getParameter("startTime");//开始时间
		String endingTime = request.getParameter("endingTime");//结束时间
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, OfflineOrders.class);
		if(startTime != null) {
			findMap.put("startTime", startTime);
		}
		if(endingTime != null) {
			findMap.put("endingTime", endingTime);
		}
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		List<OfflineOrders> list = iBusinessService.offlineStatistics(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		
		//合计收款金额
		Map getTotalMoney = iBusinessService.totalMoney(sourceId, closeConn, findMap);
		String totalMoney = String.valueOf(getTotalMoney.get("totalPrice"));
		if(totalMoney != "null") {
			map.put("totalMoney", totalMoney);
		}else {
			map.put("totalMoney", "0");
		}
		
		//合计订单数量
		Map getTotalOrder = iBusinessService.totalOrder(sourceId, closeConn, findMap);
		String totalOrder = String.valueOf(getTotalOrder.get("count"));
		map.put("totalOrder", totalOrder);
		
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 线下退款统计
	 */
	public Object detailedRefund(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String startTime = request.getParameter("startTime");//开始时间
		String endingTime = request.getParameter("endingTime");//结束时间
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, OfflineDetailed.class);
		if(startTime != null) {
			findMap.put("startTime", startTime);
		}
		if(endingTime != null) {
			findMap.put("endingTime", endingTime);
		}
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		List<OfflineDetailed> list = iBusinessService.detailedRefund(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		
		//合计退款金额
		Map getDetailedMoney = iBusinessService.detailedMoney(sourceId, closeConn, findMap);
		String detailedMoney = String.valueOf(getDetailedMoney.get("returnPrice"));
		if(detailedMoney != "null") {
			map.put("detailedMoney", detailedMoney);
		}else {
			map.put("detailedMoney", "0");
		}
		
		//合计订单数量
		Map getDetailedOrder = iBusinessService.detailedOrder(sourceId, closeConn, findMap);
		String detailedOrder = String.valueOf(getDetailedOrder.get("count"));
		map.put("detailedOrder", detailedOrder);
		
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 零售商订单交易额总交易额统计
	 */
	public Object xxOrderStatistics(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String entityUUID = request.getParameter("entityUUID");
		Map<String, String> map = new HashMap<>();
		int price = 0;
		
		//今日交易额
		Map getLstoDayMoney = iBusinessService.lstoDayMoney(sourceId, closeConn, entityUUID);
		String lstoDayMoney = String.valueOf(getLstoDayMoney.get("totalPrice"));
		if(lstoDayMoney != "null") {
			price = Integer.valueOf(lstoDayMoney);
			map.put("lstoDayMoney", BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
		}else {
			map.put("lstoDayMoney", "0");
		}
		
		//总销售额
		Map getLsCountMoney = iBusinessService.lsCountMoney(sourceId, closeConn, entityUUID);
		String lsCountMoney = String.valueOf(getLsCountMoney.get("totalPrice"));
		if(lsCountMoney != "null") {
			price = Integer.valueOf(lsCountMoney);
			map.put("lsCountMoney", BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
		}else {
			map.put("lsCountMoney", "0");
		}
		
		//今日订单
		Map getLstoDayOrder = iBusinessService.lstoDayOrder(sourceId, closeConn, entityUUID);
		String lstoDayOrder = String.valueOf(getLstoDayOrder.get("count"));
		map.put("lstoDayOrder", lstoDayOrder);
		
		//今日退款额
		Map getLsTodayRefund = iBusinessService.lsTodayRefund(sourceId, closeConn, entityUUID);
		String lsTodayRefund = String.valueOf(getLsTodayRefund.get("totalPrice"));
		if(lsTodayRefund != "null") {
			price = Integer.valueOf(lsTodayRefund);
			map.put("lsTodayRefund", BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
		}else {
			map.put("lsTodayRefund", "0");
		}
		
		//今日总退款额
		Map getLsMoneyRefund = iBusinessService.lsMoneyRefund(sourceId, closeConn, entityUUID);
		String lsMoneyRefund = String.valueOf(getLsMoneyRefund.get("totalPrice"));
		if(lsMoneyRefund != "null") {
			price = Integer.valueOf(lsMoneyRefund);
			map.put("lsMoneyRefund", BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
		}else {
			map.put("lsMoneyRefund", "0");
		}
		
		//今日退款订单
		Map getLsOrderRefund = iBusinessService.lsOrderRefund(sourceId, closeConn, entityUUID);
		String lsOrderRefund = String.valueOf(getLsOrderRefund.get("count"));
		map.put("lsOrderRefund", lsOrderRefund);
		return JsonUtil.ObjToJson(map); 
	}
	
	/**
	 * 根据供应商零售商查询中间表
	 */
	public Object gysOrlss(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String supplierUUID = request.getParameter("supplierUUID");//供应商UUID
		String retailerUUID = request.getParameter("retailerUUID");//零售商UUID
		Map<String, String> findMap = new HashMap<>();
		if(supplierUUID != null && retailerUUID != null) {
			findMap.put("supplierUUID", supplierUUID);
			findMap.put("retailerUUID", retailerUUID);
		}
		BusinessMiddle businessMiddle = null;
		businessMiddle = iBusinessService.gysOrlss(sourceId, closeConn, findMap);
		return JsonUtil.ObjToJson(businessMiddle);
	}
	
	/**
	 * 店铺银行卡绑定或解除
	 */
	public Object bankSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> msgMap = new HashMap<>();
		BusinessApply businessApply = (BusinessApply) ReqToEntityUtil.reqToEntity(request, BusinessApply.class);
		if(iBusinessService.bankSave(sourceId, closeConn, businessApply)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 幻灯片查询
	 */
	public Object slideImg(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, SlideImg.class);
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		List<SlideImg> list = iBusinessService.slideImg(sourceId, closeConn, page, findMap);
		Map map = new HashMap();
		if(page == null) {
			map.put("list", list);
			return JsonUtil.ObjToJson(map);
		}else {
			map.put("total", page.getTotalRecord());
			if(list == null) {
				map.put("rows", 0);
			}else {
				map.put("rows", list);
			}
			map.put("page", page);
			return JsonUtil.ObjToJson(map);
		}
	}
	
	/**
	 * 幻灯片根据ID查询
	 */
	public Object slideImgId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String slideUUID = request.getParameter("slideUUID");
		SlideImg slideImg = null;
		slideImg = iBusinessService.slideImgId(sourceId, closeConn, slideUUID);
		return JsonUtil.ObjToJson(slideImg);
	}
	
	/**
	 * 幻灯片新增或修改
	 */
	public Object slideSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		UpFileUtil uy =new UpFileUtil();
		Map map =new HashMap();
	    Map<String, String> maps = new HashMap<String,String>();
	    String path = ReadProperties.getValue("getBusinessImg");
	    String getPath = ReadProperties.getValue("businessImg");
		String fileName = uy.saveHttpUpFiles(request, path, map, maps,"0");
		SlideImg slideImg = null;
		if(fileName == null) {
			slideImg = (SlideImg) ReqToEntityUtil.reqToEntity(request, SlideImg.class);
	    }else {
	    	slideImg = (SlideImg)ReqToEntityUtil.MapToEntity(map,SlideImg.class);
	    	if(maps.get("slide") != null) {
	    		slideImg.setSlide(getPath+maps.get("slide"));
	    	}
	    	if(maps.get("details") != null) {
	    		slideImg.setDetails(getPath+maps.get("details"));
	    	}
	    }
		if(slideImg.getId() == null) {
			slideImg.setSlideUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			slideImg.setTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm"));
		}
		Map<String, String> msgMap = new HashMap<>();
		if(iBusinessService.slideSave(sourceId, closeConn, slideImg)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 确认订单核销
	 */
	public Map<String, String> purchase(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String offlineUUID = request.getParameter("offlineUUID");
		OfflineOrders offlineOrders = null;
		Map<String, String> offlineMap = new HashMap<>();
		offlineMap.put("offlineUUID", offlineUUID);
 		offlineOrders = iBusinessService.offlineOrdersParameter(sourceId, closeConn, offlineMap);
		
		String entityUUID = offlineOrders.getRetailerUUID();
		String entityName = offlineOrders.getRetailerName();
		String entityLogo = offlineOrders.getRetailerImg();
		Map<String, Integer> uuid2count = new HashMap<>();
		uuid2count.put(offlineOrders.getItemUUID(), Integer.valueOf(offlineOrders.getNumber()));
		//校验参数
		Map<String, String> result = new HashMap<>();
		if(RegExpUtil.isNullOrEmpty(entityUUID) || uuid2count == null || uuid2count.size() == 0){
			result.put("status", "-1");
			result.put("msg", "调用接口的参数不能有空值");
			return result;
		}
		ArrayList<String> uuids = new ArrayList<>();
		for(Map.Entry<String, Integer> param : uuid2count.entrySet()){
			String uuid = param.getKey();
			Integer count = param.getValue();
			if(RegExpUtil.isNullOrEmpty(uuid)){
				result.put("status", "-2");
				result.put("msg", "商品uuid不能为空");
				return result;
			}
			if(count < 1){
				result.put("status", "-3");
				result.put("msg", "商品数量必须大于0");
				return result;
			}
			uuids.add(uuid);
		}
		
		//查询套餐进货单
		List<ItemParam> paramPurchase = paramService.findPurchase(sourceId, closeConn, uuids, entityUUID);
		List<ItemParam> paramList = null;
		if(paramPurchase == null){
			paramList = paramService.findListByUUIDs(sourceId, closeConn, uuids);
		}else{
			Iterator<String> it = uuids.iterator();
			while(it.hasNext()){
				String uuid = it.next();
				for(ItemParam param : paramPurchase){
					if(param.getOriginUUID().equals(uuid)){
						it.remove();
					}
				}
			}
			if(uuids.size() > 0){
				paramList = paramService.findListByUUIDs(sourceId, closeConn, uuids);
			}
		}
		
		
		Lock lock = jedisClient.getEntityLock(entityUUID);
		lock.lock();
		try {
			//已经存在的套餐增加库存
			if (paramPurchase != null) {
				Object[][] params = new Object[paramPurchase.size()][2];
				int i = 0;
				for (ItemParam param : paramPurchase) {
					params[i][0] = uuid2count.get(param.getOriginUUID());
					params[i][1] = param.getParamUUID();
					i++;
				}
				paramService.increseStock(sourceId, closeConn, params);
			}
			//不存在的套餐进行新增操作
			if (paramList != null) {
				ArrayList<String> itemUUIDs = new ArrayList<>();
				Map<String, List<ItemParam>> itemUUID2param = new HashMap<>();
				for (ItemParam param : paramList) {
					param.setId(null);
					String paramUUID = param.getParamUUID();
					String newUUID = UUID.randomUUID().toString().replaceAll("-", "");
					param.setParamUUID(newUUID);
					param.setOriginUUID(paramUUID);
					param.setEntityUUID(entityUUID);
					param.setEntityName(entityName);
					param.setEntityLogo(entityLogo);
					param.setGrade("0");
					param.setItemType("0");
					param.setStock(uuid2count.get(paramUUID));
					param.setSales(0);
					param.setMonthlySales(0);
					param.setDistributeStatus("0");
					param.setFirstRatio(0d);
					param.setSecondRatio(0d);
					param.setParamStatus("2");
					param.setIsDel("0");

					String itemUUID = param.getItemUUID();
					List<ItemParam> subList = itemUUID2param.get(itemUUID);
					if (subList == null) {
						subList = new ArrayList<>();
						itemUUID2param.put(itemUUID, subList);
						itemUUIDs.add(itemUUID);
					}
					subList.add(param);
				}

				//查询商品进货单
				List<Item> itemPurchase = itemService.findPurchase(sourceId, closeConn, itemUUIDs, entityUUID);
				List<Item> itemList = null;
				if (itemPurchase == null) {
					itemList = itemService.findListByUUIDs(sourceId, closeConn, itemUUIDs);
				} else {
					Iterator<String> it = itemUUIDs.iterator();
					while (it.hasNext()) {
						String uuid = it.next();
						for (Item item : itemPurchase) {
							if (item.getOriginUUID().equals(uuid)) {
								it.remove();
							}
						}
					}
					if (itemUUIDs.size() > 0) {
						itemList = itemService.findListByUUIDs(sourceId, closeConn, itemUUIDs);
					}
				}

				ArrayList<ItemParam> newParamList = new ArrayList<>();
				//修改新增套餐的itemUUID
				if (itemPurchase != null) {
					for (Item item : itemPurchase) {
						List<ItemParam> subList = itemUUID2param.get(item.getOriginUUID());
						for (ItemParam param : subList) {
							param.setItemUUID(item.getItemUUID());
							newParamList.add(param);
						}
					}
				}
				//不存在的商品进行新增操作
				if (itemList != null) {
					for (Item item : itemList) {
						item.setId(null);
						String itemUUID = item.getItemUUID();
						String newUUID = UUID.randomUUID().toString().replaceAll("-", "");
						item.setItemUUID(newUUID);
						item.setOriginUUID(itemUUID);
						item.setEntityUUID(entityUUID);
						item.setEntityName(entityName);
						item.setEntityLogo(entityLogo);
						item.setItemType("0");
						item.setScore(null);
						item.setTotalSales(0);
						item.setTotalMonthlySales(0);
						item.setTagMonthlySales(0);
						item.setDistributeStatus("0");
						item.setFirstRatio(0d);
						item.setSecondRatio(0d);
						item.setItemStatus("2");
						item.setIsDel("0");

						List<ItemParam> subList = itemUUID2param.get(itemUUID);
						for (ItemParam param : subList) {
							param.setItemUUID(newUUID);
							newParamList.add(param);
						}
					}
					itemService.batchInsert(sourceId, closeConn, itemList);
				}
				paramService.batchInsert(sourceId, closeConn, newParamList);
			} 
		} finally {
			lock.unlock();
		}
		iBusinessService.stateUp(sourceId, closeConn, offlineUUID);
		this.increseStock(sourceId, closeConn, offlineOrders.getRetailerUUID(), uuid2count);//增加商品库存
		this.decreseStock(sourceId, closeConn, offlineOrders.getBusinessUUID(), uuid2count);//减少商品库存
		this.increseSales(sourceId, closeConn, offlineOrders.getBusinessUUID(), uuid2count);//增加商品销量
		BusinessApply businessApply = null;
		businessApply = iBusinessService.applyId(sourceId, closeConn, offlineOrders.getBusinessUUID());
		int salesVolume = businessApply.getSalesVolume()+Integer.valueOf(offlineOrders.getNumber());
		iBusinessService.businessXl(sourceId, closeConn, offlineOrders.getBusinessUUID(), salesVolume);
		result.put("status", "1");
		result.put("msg", "零售商进货成功");
		return result;
	}
	
	/**
	 * 幻灯片删除
	 */
	public Object slideDel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String slideUUID = request.getParameter("slideUUID");
		Map<String,String> msgMap = new HashMap<>();
		if(iBusinessService.slideDel(sourceId, closeConn, slideUUID)) {
			msgMap.put("msg", "操作成功");
		}else {
			msgMap.put("msg", "操作失败");
		}
		return JsonUtil.ObjToJson(msgMap);
	}
	
	/**
	 * 增加商品库存
	 */
	public Map<String, String> increseStock(String sourceId, boolean closeConn, String entityUUID, Map<String, Integer> uuid2count) throws Exception {
		//校验参数
		Map<String, String> result = new HashMap<>();
		if(RegExpUtil.isNullOrEmpty(entityUUID) || uuid2count == null || uuid2count.size() == 0){
			result.put("status", "-1");
			result.put("msg", "调用接口的参数不能有空值");
			return result;
		}
		for(Map.Entry<String, Integer> param : uuid2count.entrySet()){
			String uuid = param.getKey();
			Integer count = param.getValue();
			if(RegExpUtil.isNullOrEmpty(uuid)){
				result.put("status", "-2");
				result.put("msg", "商品uuid不能为空");
				return result;
			}
			if(count < 1){
				result.put("status", "-3");
				result.put("msg", "商品数量必须大于0");
				return result;
			}
		}
		
		Lock lock = jedisClient.getEntityLock(entityUUID);
		lock.lock();
		try {
			Object[][] params = new Object[uuid2count.size()][2];
			int i = 0;
			for (Map.Entry<String, Integer> param : uuid2count.entrySet()) {
				params[i][0] = param.getValue();
				params[i][1] = param.getKey();
				i++;
			}
			paramService.increseStock(sourceId, closeConn, params);
			
			result.put("status", "1");
			result.put("msg", "添加库存成功");
			return result;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 减少商品库存
	 */
	public Map<String, String> decreseStock(String sourceId, boolean closeConn, String entityUUID, Map<String, Integer> uuid2count) throws Exception {
		//校验参数
		Map<String, String> result = new HashMap<>();
		if(RegExpUtil.isNullOrEmpty(entityUUID) || uuid2count == null || uuid2count.size() == 0){
			result.put("status", "-1");
			result.put("msg", "调用接口的参数不能有空值");
			return result;
		}
		for(Map.Entry<String, Integer> param : uuid2count.entrySet()){
			String uuid = param.getKey();
			Integer count = param.getValue();
			if(RegExpUtil.isNullOrEmpty(uuid)){
				result.put("status", "-2");
				result.put("msg", "商品uuid不能为空");
				return result;
			}
			if(count < 1){
				result.put("status", "-3");
				result.put("msg", "商品数量必须大于0");
				return result;
			}
		}
		
		Lock lock = jedisClient.getEntityLock(entityUUID);
		lock.lock();
		try {
			Object[][] params = new Object[uuid2count.size()][2];
			int i = 0;
			for (Map.Entry<String, Integer> param : uuid2count.entrySet()) {
				params[i][0] = param.getValue();
				params[i][1] = param.getKey();
				i++;
			}
			paramService.decreseStock(sourceId, closeConn, params);
			
			result.put("status", "1");
			result.put("msg", "减少库存成功");
			return result;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 增加商品销量
	 */
	public Map<String, String> increseSales(String sourceId, boolean closeConn, String entityUUID, Map<String, Integer> uuid2count) throws Exception {
		//校验参数
		Map<String, String> result = new HashMap<>();
		if(RegExpUtil.isNullOrEmpty(entityUUID) || uuid2count == null || uuid2count.size() == 0){
			result.put("status", "-1");
			result.put("msg", "调用接口的参数不能有空值");
			return result;
		}
		List<String> uuids = new ArrayList<>();
		for(Map.Entry<String, Integer> param : uuid2count.entrySet()){
			String uuid = param.getKey();
			Integer count = param.getValue();
			if(RegExpUtil.isNullOrEmpty(uuid)){
				result.put("status", "-2");
				result.put("msg", "商品uuid不能为空");
				return result;
			}
			if(count < 1){
				result.put("status", "-3");
				result.put("msg", "商品数量必须大于0");
				return result;
			}
			uuids.add(uuid);
		}
		
		//统计商品销量
		List<ItemParam> paramList = paramService.findListByUUIDs(sourceId, closeConn, uuids);
		Map<String, Integer> itemUUID2count = new HashMap<>();
		for(ItemParam param : paramList){
			Integer count = itemUUID2count.get(param.getItemUUID());
			if(count == null){
				count = uuid2count.get(param.getParamUUID());
			}else{
				count += uuid2count.get(param.getParamUUID());
			}
			itemUUID2count.put(param.getItemUUID(), count);
		}
		
		Lock lock = jedisClient.getEntityLock(entityUUID);
		lock.lock();
		try {
			//商品规格销量
			Object[][] params = new Object[uuid2count.size()][3];
			int i = 0;
			for (Map.Entry<String, Integer> param : uuid2count.entrySet()) {
				params[i][0] = param.getValue();
				params[i][1] = param.getValue();
				params[i][2] = param.getKey();
				i++;
			}
			
			//商品销量
			Object[][] items = new Object[itemUUID2count.size()][4];
			int j = 0;
			for (Map.Entry<String, Integer> item : itemUUID2count.entrySet()) {
				items[j][0] = item.getValue();
				items[j][1] = item.getValue();
				items[j][2] = item.getValue();
				items[j][3] = item.getKey();
			}
			paramService.increseSales(sourceId, closeConn, params);
			itemService.increseSales(sourceId, closeConn, items);
			
			result.put("status", "1");
			result.put("msg", "增加销量成功");
			return result;
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * 平台管理(今日交易额-总销售额-今日订单)
	 */
	public Object platformStatistics(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Integer> map = new HashMap<>();
		//线下今日交易额
		Map getOfflineTodayMoney = iBusinessService.ptXxMoney(sourceId, closeConn);
		String offlineTodayMoney = String.valueOf(getOfflineTodayMoney.get("totalPrice"));
		if(offlineTodayMoney != "null") {
			offlineTodayMoney = offlineTodayMoney;
		}else {
			offlineTodayMoney = "0";
		}
		
		//线上今日交易额
		Map getLstoDayMoney = iBusinessService.ptXsMoney(sourceId, closeConn);
		String lstoDayMoney = String.valueOf(getLstoDayMoney.get("totalPrice"));
		if(lstoDayMoney != "null") {
			int price = Integer.valueOf(lstoDayMoney);
			price = Integer.valueOf(BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
			lstoDayMoney = String.valueOf(price);
		}else {
			lstoDayMoney = "0";
		}
		map.put("platformToDayMoney", Integer.valueOf(offlineTodayMoney)+Integer.valueOf(lstoDayMoney));//平台今日交易额
		
		//线下总交易额
		Map getOfflineCountMoney = iBusinessService.ptXxNumber(sourceId, closeConn, null);
		String offlineCountMoney = String.valueOf(getOfflineCountMoney.get("countMoney"));
		if(offlineCountMoney != "null") {
			offlineCountMoney = offlineCountMoney;
		}else {
			offlineCountMoney = "0";
		}
		
		//线上总销售额
		Map getLsCountMoney = iBusinessService.ptXsNumber(sourceId, closeConn, null);
		String lsCountMoney = String.valueOf(getLsCountMoney.get("countMoney"));
		if(lsCountMoney != "null") {
			int price = Integer.valueOf(lsCountMoney);
			price = Integer.valueOf(BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString());
			lsCountMoney = String.valueOf(price);
		}else {
			lsCountMoney = "0";
		}
		map.put("platformCountMoney", Integer.valueOf(offlineCountMoney)+Integer.valueOf(lsCountMoney));//平台总销售额
		
		//线下今日订单
		Map getOfflineTodayOrder = iBusinessService.ptXxOrder(sourceId, closeConn);
		String offlineTodayOrder = String.valueOf(getOfflineTodayOrder.get("count"));
		
		//线上今日订单
		Map getLstoDayOrder = iBusinessService.ptXsOrder(sourceId, closeConn);
		String lstoDayOrder = String.valueOf(getLstoDayOrder.get("count"));
		
		map.put("platformDayOrder", Integer.valueOf(offlineTodayOrder)+Integer.valueOf(lstoDayOrder));
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 平台数据统计
	 */
	public Object platformData(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String record = request.getParameter("rows");//每页多少条
		String pageNumber = request.getParameter("page");//当前页
		int countNumber = 0;
		Map<String, String> findMap = new HashMap<>();
		if(request.getParameter("startTime") != null) {//开始时间
			findMap.put("startTime", request.getParameter("startTime"));
		}
		if(request.getParameter("endingTime") != null) {//结束时间
			findMap.put("endingTime", request.getParameter("endingTime"));
		}
		findMap.put("state", "1");
		List<OfflineOrders> offlineOrdersList = new ArrayList<>();//线下订单
		offlineOrdersList = iBusinessService.offlineOrdersAll(sourceId, closeConn, null, findMap);
		
		List<OrderItem> orderItemsList = new ArrayList<>();//线上订单
		orderItemsList = iBusinessService.orderItemsAll(sourceId, closeConn,findMap);
		
		List<Map<Object, Object>> listMap = new ArrayList<>();
		for(int i = 0; i < offlineOrdersList.size(); i++) {
			OfflineOrders offlineOrders = null;
			Map<Object, Object> map = new HashMap<>();
			offlineOrders = offlineOrdersList.get(i);
			map.put("orderUUID", offlineOrders.getOfflineUUID());
			map.put("itemType", "批发商品");
			map.put("time", offlineOrders.getTime());
			map.put("title", offlineOrders.getTitle());
			map.put("totalPrice", offlineOrders.getTotalPrice());
			map.put("number", offlineOrders.getNumber());
			map.put("commodityImg", offlineOrders.getCommodityImg());
			listMap.add(map);
			countNumber ++;
		}
		for(int i = 0; i < orderItemsList.size(); i++) {
			OrderItem orderItem = null;
			Map<Object, Object> map = new HashMap<>();
			orderItem = orderItemsList.get(i);
			map.put("orderUUID", orderItem.getOrderUUID());
			if("0".equals(orderItem.getItemType())) {
				map.put("itemType", "零售商品");
			}else {
				map.put("itemType", "批发商品");
			}
			map.put("time", orderItem.getCreateTime());
			map.put("title", orderItem.getTitle());
			map.put("totalPrice", BigDecimal.valueOf(Long.valueOf(orderItem.getPrice())).divide(new BigDecimal(100)).toString());
			map.put("number", orderItem.getCount());
			map.put("commodityImg", orderItem.getItemImg());
			listMap.add(map);
			countNumber ++;
		}
		
		//排序
		/*Collections.sort(listMap, new Comparator<Map<Object, Object>>() {
            public int compare(Map<Object, Object> p1, Map<Object, Object> p2) {
            	if(Float.parseFloat((String) p1.get("time")) > Float.parseFloat((String) p2.get("time"))) {
            		return 1;
            	}
            	if(Float.parseFloat((String) p1.get("time")) == Float.parseFloat((String) p2.get("time"))) {
            		return 0;
            	}
            	return -1;
            }
        });*/
		
		List<Map<Object, Object>> list = new ArrayList<Map<Object,Object>>();
		Map map = new HashMap<>();
		//计算总页数
		int len = listMap.size();
		len = len-1;
		int row = Integer.parseInt(record);
		int count = len/row;
		int pageCount = (int) Math.floor(count);
		pageCount = pageCount+1;//总页数
		
		int page = Integer.parseInt(pageNumber);
		if(pageCount <= page) {//如果总页数小于等于当前页
			for(int i = (page-1)*row; i < listMap.size(); i++) {
				list.add(listMap.get(i));
			}
		}else {
			for(int i = (page-1)*row; i < page*row; i++) {
				list.add(listMap.get(i));
			}
		}
		
		//线下销售额
		Map ptXxNumber = iBusinessService.ptXxNumber(sourceId, closeConn, findMap);
		String ptxxNumber = String.valueOf(ptXxNumber.get("countMoney"));
		if(ptxxNumber != "null") {
			ptxxNumber = ptxxNumber;
		}else {
			ptxxNumber = "0";
		}
		
		//线上销售额
		Map ptXsNumber = iBusinessService.ptXsNumber(sourceId, closeConn, findMap);
		String ptxsNumber = String.valueOf(ptXsNumber.get("countMoney"));
		if(ptxsNumber != "null") {
			ptxsNumber = ptxsNumber;
		}else {
			ptxsNumber = "0";
		}
		ptxsNumber = BigDecimal.valueOf(Long.valueOf(Integer.valueOf(ptxsNumber))).divide(new BigDecimal(100)).toString();
		
		map.put("pageCount", pageCount);
		map.put("page", page);
		map.put("list", list);
		map.put("countNumber", countNumber);
		map.put("countMoney", Integer.valueOf(ptxxNumber)+Integer.valueOf(ptxsNumber));
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 编辑商铺
	 */
	public Object editSave(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		UpFileUtil uy =new UpFileUtil();
		Map map =new HashMap();
	    Map<String, String> maps = new HashMap<String,String>();
	    String path = ReadProperties.getValue("getBusinessImg");
	    String getPath = ReadProperties.getValue("businessImg");
		String fileName = uy.saveHttpUpFiles(request, path, map, maps,"0");
		BusinessApply businessApply = null;
		if(fileName == null) {
			businessApply = (BusinessApply) ReqToEntityUtil.reqToEntity(request, BusinessApply.class);
	    }else {
	    	businessApply = (BusinessApply)ReqToEntityUtil.MapToEntity(map,BusinessApply.class);
	    	businessApply.setBankImg(getPath+maps.get("bankImg"));
	    	businessApply.setStorefrontImg(getPath+maps.get("storefrontImg"));
	    	businessApply.setBusinessImg(getPath+maps.get("businessImg"));
	    	businessApply.setIdJustImg(getPath+maps.get("idJustImg"));
	    	businessApply.setIdBackImg(getPath+maps.get("idBackImg"));
	    }
		//商品分类
		if(businessApply.getCatUUID() != null) {
			String[] catArr = businessApply.getCatUUID().split(",");
			for(int i = 0; i < catArr.length; i++) {
				if(i == 0) {
					businessApply.setCatUUID(catArr[i]);
				}else {
					businessApply.setClassification(catArr[i]);
				}
			}
		}
		
		//批发市场
		if(businessApply.getPfUUID() != null) {
			String[] pfUUIDArr = businessApply.getPfUUID().split(",");
			for(int i = 0; i < pfUUIDArr.length; i++) {
				if(i == 0) {
					businessApply.setPfUUID(pfUUIDArr[i]);
				}else {
					businessApply.setPfName(pfUUIDArr[i]);
				}
			}
		}
		iBusinessService.businessSave(sourceId, closeConn, businessApply);
		Map<String, String> msgMap = new HashMap<>();
		msgMap.put("msg", "操作成功");
		return JsonUtil.ObjToJson(msgMap);
	}
	

}