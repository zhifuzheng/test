package com.xryb.zhtc.manage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.StringUtils;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Integral;
import com.xryb.zhtc.entity.IntegralGoodOrder;
import com.xryb.zhtc.entity.IntegralGoods;
import com.xryb.zhtc.entity.IntegralRecordOfUser;
import com.xryb.zhtc.entity.IntegralRule;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.PlatformActiveGood;
import com.xryb.zhtc.entity.PlatformActivityOrder;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;

import dbengine.util.Page;
import spark.annotation.Auto;

/**
 * 用户积分管理
 * @author apple
 */
public class IntegralMng {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	
	/**
	 * 获取用户积分
	 */
	public String getUserIntegral(String sourceID, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceID, closeConn, openId);
		if(vip == null ) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		String vipUUID = request.getParameter("vipUUID");
		Map<String,Object> map = integralService.getUserIntegral(sourceID, closeConn, vipUUID);
		if(map == null) {
			//在积分表中创建该用户的积分，初值为0
			Integral integral = new Integral();
			integral.setVipUUID(vipUUID);
			integral.setIntegral(0);
			if(integralService.addUserIntegral(sourceID, closeConn, integral)) {
				result.put("status", "1");
				result.put("msg", "积分获取成功");
				result.put("userIntegral", 0);
				return JsonUtil.ObjToJson(result);
			}
			result.put("status", "-1");
			result.put("msg", "系统繁忙请稍后重试！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "积分获取成功");
		result.put("userIntegral", map.get("integral"));
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 积分修改
	 */
	public String changeIntegral(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//1.获取用户积分
		String vipUUID = request.getParameter("vipUUID");
		Map<String,Object> map = integralService.getUserIntegral(sourceId, closeConn, vipUUID);
		Integer userIntegral  = (Integer) map.get("integral");
		
		//TODO 2.用户积分的修改量需通过订单号取查询获取
		Integer integer  = Integer.parseInt(request.getParameter("integer"));
		
		//3.计算修改后的值
		Integer type = Integer.parseInt(request.getParameter("type"));//1-积分消耗，2-获得积分
		if(type == 1) {
			userIntegral -= integer;
		}else {
			userIntegral += integer;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//4.执行修改
		if(!integralService.updateUserIntegral(sourceId, closeConn, userIntegral, vipUUID)) {
			result.put("status", "-1");
			result.put("msg", "积分修改失败");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "积分修改成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 积分计算
	 */
	public String calculateIntegral(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//TODO 订单实际支付金额，充值金额(根据订单号查询金额)
		String orderUUID = request.getParameter("orderUUID");
		
		String orderMoney = request.getParameter("money");
		Double money = Double.parseDouble(orderMoney);
		
		//1.获取积分规则类型 1-账户充值；2-购买商品
		Integer type = Integer.parseInt(request.getParameter("type"));
		long integration = 0;//获得的积分值
		
		//2.获取对应类型的积分规则，并判断用户购买金额满足那种梯度计算规则
		List<IntegralRule> ruleList = integralService.findIntegralRuleListByType(sourceId, closeConn, type);
		if (ruleList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "敬请期待！");
			result.put("integration", integration);
			return JsonUtil.ObjToJson(result);
		}
		
		//3.判断范围并计算
		for (IntegralRule integralRule : ruleList) {
			Double gradMin = Double.parseDouble(integralRule.getGradMin());
			Double gradMax = Double.parseDouble(integralRule.getGradMax());
			if (money >= gradMin && money < gradMax ) {
				Double rule = Double.parseDouble(integralRule.getCalculateRule());
				integration = (long) Math.rint(money * rule);
				break;
			}
		}
		
		result.put("status", "1");
		result.put("msg", "积分计算成功");
		result.put("integration", integration);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 获取用户积分详情记录(积分来源和消耗)
	 */
	public String getUserIntegralList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		
		String vipUUID = request.getParameter("vipUUID");
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		//查询
		List<IntegralRecordOfUser> integralList = integralService.findUserIntegralList(sourceId, closeConn, vipUUID, page);
		if (page == null) {
			result.put("integralList", integralList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (integralList == null) {
				result.put("integralList", 0);
			} else {
				result.put("integralList", integralList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 积分兑换商品记录
	 */
	public String getIntegralExGoodsList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
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
		
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String searchWords = request.getParameter("searchWords");
		//查询
		List<IntegralGoodOrder> goodOrderList = integralService.findIntegralExGoodsList(sourceId, closeConn, vipUUID, searchWords,page);
		
		if (page == null) {
			result.put("goodOrderList", goodOrderList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodOrderList == null) {
				result.put("goodOrderList", 0);
			} else {
				result.put("goodOrderList", goodOrderList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 删除用户积分商品兑换记录
	 */
	public String delGoodsExRecord(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后删除");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		//执行删除
		if(!integralService.deleteUserIntegralGoodExRecord(sourceId, closeConn,orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);

	}


	/**
	 * 判断用户是否参与过完善资料优惠活动
	 */
	public String userIsJoined(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "您未登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		String vipUUID = vip.getVipUUID();
		//判断用户资料是否完善
		VipInfo vipInfo = integralService.findUserInfo(sourceId, closeConn, vipUUID);
		result.put("status", "1");
		result.put("msg", "未参与");
		return JsonUtil.ObjToJson(result);
	}


	
	/**
	 * 积分商城生成积分商品兑换订单
	 */
	public String createIntegralGoodOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "请授权登陆后兑换");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		String vipLogo = vip.getAvatarUrl();
		
		//2.获取积分商品信息
		String goodUUID = request.getParameter("goodUUID");
		IntegralGoods goods = integralService.getGoodsInfo(sourceId, closeConn, goodUUID);
		if(goods.getStock() == 0) {// 商品库存为0
			result.put("status", "-1");
			result.put("msg", "非常遗憾，该商品已兑换完，您可选择其他商品兑换");
			return JsonUtil.ObjToJson(result);
		}
		
		//3.获取用户积分值
		Map<String,Object> inte = integralService.getUserIntegral(sourceId, closeConn, vipUUID);
		Integer userIntegral = (Integer) inte.get("integral");
		if(goods.getIntegration() > userIntegral) {//用户积分不够领取
			result.put("status", "-1");
			result.put("msg", "您的积分余额不够");
			return JsonUtil.ObjToJson(result);
		}
		userIntegral -= goods.getIntegration();
		
		//4.获取领取店铺的相关信息
		String entityUUID = request.getParameter("entityUUID");
		BusinessApply entity = integralService.findBusinessInfoByUUID(sourceId, closeConn, entityUUID);
		
		//5.生成订单
		synchronized (this) {
			//5.1修改库存
			IntegralGoods good = integralService.getGoodsInfo(sourceId, closeConn, goodUUID);
			Integer stock = good.getStock();
			stock -= 1;
			if(!integralService.updateGoodStock(sourceId, closeConn, stock, goodUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
			//添加积分记录
			IntegralRecordOfUser ofUser = new IntegralRecordOfUser();
			ofUser.setVipUUID(vipUUID);
			ofUser.setMoneyOrIntegral(good.getIntegration()+"");
			ofUser.setIntegration(good.getIntegration());
			ofUser.setChangeTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			ofUser.setIntegralFrom(3);
			ofUser.setIntegralType(2);
			if(!integralService.createUserIntegralRecord(sourceId, closeConn, ofUser)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
			//修改用户积分
			if(!integralService.updateUserIntegral(sourceId, closeConn, userIntegral, vipUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
			
			//5.2创建订单对象
			IntegralGoodOrder goodOrder = new IntegralGoodOrder();
			//订单UUID
			goodOrder.setOrderUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			//订单编号设置
			Date date = new Date();
			int ran = (int) (Math.random()*89999999+10000000);
			String number = date.getTime()+""+ran;
			goodOrder.setOrderNumber(number);
			goodOrder.setGoodUUID(goodUUID);
			//商品标题
			goodOrder.setTitle(good.getTitle());
			//商品图片
			goodOrder.setGoodImg(good.getGoodImg());
			//商品件数
			goodOrder.setTotalCount(1);
			//订单积分总价（积分）
			goodOrder.setIntegration(good.getIntegration());
			//用户剩余积分
			goodOrder.setUserIntegration(userIntegral);
			//领取时间限制
			goodOrder.setGetTime(good.getGetTime());
			//领取店铺UUID
			goodOrder.setEntityUUID(entity.getBusinessUUID());
			//领取店铺名称
			goodOrder.setEntityName(entity.getBusinessName());
			//领取店铺图片
			goodOrder.setEntityLogo(entity.getStorefrontImg());
			//领取店铺地址
			goodOrder.setBusinessAdd(entity.getBusinessAdd());
			goodOrder.setLongitude(entity.getLongitude());
			goodOrder.setLatitude(entity.getLatitude());
			//领取店铺联系电话
			goodOrder.setBusinessPhone(entity.getBusinessPhone());
			//订单状态;0-未领取，1-已领取，2-已过期
			goodOrder.setOrderStatus(0);
			goodOrder.setUserIsDel(0);;//用户：0-未删除，1-已删除
			goodOrder.setPlatformIsDel(0);//平台：0-未删除，1-已删除
			//买家UUID
			goodOrder.setPayerUUID(vip.getVipUUID());
			//买家名称
			goodOrder.setPayerName(vip.getVipName());
			//买家头像
			goodOrder.setPayerLogo(vipLogo);
			//买家联系电话
			goodOrder.setReceiverMobile(vip.getVipMobile());
			//订单创建时间
			Date date1 = new Date();
			SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time1 = adf.format(date1);
			goodOrder.setCreateTime(time1);
			//设置订单关闭/失效时间
			long timeMill = date1.getTime();
			timeMill = timeMill+goodOrder.getGetTime()*60*60*1000;
			date1.setTime(timeMill);
			String time2 = adf.format(date1);
			goodOrder.setCloseTime(time2);
			
			//5.3创建订单
			if(!integralService.createIntegralGoodOrder(sourceId, closeConn, goodOrder)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试或联系管理员");
				return JsonUtil.ObjToJson(result);
			}
			
			result.put("status", "1");
			result.put("msg", "订单创建成功");
			result.put("goodOrder", goodOrder);
			return JsonUtil.ObjToJson(result);
		}
	}
	
	/**
	 * 领取商品
	 * @throws Exception 
	 */
	public String getIntegralOrderGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "请授权登陆后兑换");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		
		String orderUUID = request.getParameter("orderUUID");
		Integer type = Integer.valueOf(request.getParameter("type"));
		if(type == 1) {//积分商品
			//1.获取订单信息
			IntegralGoodOrder order = integralService.findIntegralGoodOrderInfo(sourceId, closeConn, orderUUID);
			//2.判断是否已过领取时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			long longDate1 = Long.valueOf(nowTime.replaceAll("[-\\s:]",""));
			String closeTime = order.getCloseTime();
			long longDate2 = Long.valueOf(closeTime.replaceAll("[-\\s:]",""));
			if(longDate1>longDate2) {//已过领取时间
				//修改订单为已过期
				integralService.updateIntegralOrderStatus(sourceId, closeConn, orderUUID);
				result.put("status", "-1");
				result.put("msg", "非常遗憾，领取失败，已过领取时间！");
				return JsonUtil.ObjToJson(result);
			}
			//领取
			if(!integralService.updateIntegralGoodInfo(sourceId, closeConn, orderUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请联系管理员");
				return JsonUtil.ObjToJson(result);
			}
			
			IntegralGoodOrder orders = integralService.findIntegralGoodOrderInfo(sourceId, closeConn, orderUUID);
			result.put("status", "1");
			result.put("msg", "领取成功");
			result.put("orders", orders);
			return JsonUtil.ObjToJson(result);
			
		}else {//官方活动商品
			PlatformActivityOrder order = integralService.findActiveOrderInfo(sourceId, closeConn, orderUUID);
			//2.判断是否已过领取时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			long longDate1 = Long.valueOf(nowTime.replaceAll("[-\\s:]",""));
			String closeTime = order.getCloseTime();
			long longDate2 = Long.valueOf(closeTime.replaceAll("[-\\s:]",""));
			if(longDate1>longDate2) {//已过领取时间
				//修改订单为已过期
				integralService.updateActiveOrderStatus(sourceId, closeConn, orderUUID);
				result.put("status", "-1");
				result.put("msg", "非常遗憾，领取失败，已过领取时间！");
				return JsonUtil.ObjToJson(result);
			}
			
			//领取
			if(!integralService.updateActiveGoodInfo(sourceId, closeConn, orderUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请联系管理员");
				return JsonUtil.ObjToJson(result);
			}
			PlatformActivityOrder orders = integralService.findActiveOrderInfo(sourceId, closeConn, orderUUID);
			result.put("status", "1");
			result.put("msg", "领取成功");
			result.put("orders", orders);
			return JsonUtil.ObjToJson(result);
		}
	}
	
	/**
	 * 获取积分商品信息
	 */
	public String getIntegralGoodInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		String goodUUID = request.getParameter("goodUUID");
		//查询
		IntegralGoods goods = integralService.getGoodsInfo(sourceId, closeConn, goodUUID);
		if(goods == null) {
			result.put("status", "-1");
			result.put("msg", "获取失败");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "获取成功");
		result.put("goods", goods);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 手机端添加积分商城商品
	 */
	public String addIntegralGoodofMobile(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		IntegralGoods goods = (IntegralGoods) ReqToEntityUtil.reqToEntity(request, IntegralGoods.class);
		System.out.println(goods.getSubImgList());
		System.out.println(goods.getGoodParams());
		if(integralService.addIntegralGoods(sourceId, closeConn, goods)) {
			result.put("status", "1");
			result.put("msg", "添加成功！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取积分商城已添加的商品
	 */
	public String getIntegralGoodListofMobile(String sourceId, boolean closeConn, HttpServletRequest request,HttpServletResponse resposne) {
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
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		// 获取查询条件
		Map<String, Object> param = new HashMap<String, Object>();// 封装查询条件
		param.put("range", request.getParameter("range"));
		param.put("status", request.getParameter("status"));
		param.put("searchWords", request.getParameter("searchWords"));
		param.put("vipUUID", vipUUID);
		
		// 查询结果
		List<IntegralGoods> goodsList = integralService.findIntegralGoodsList(sourceId, closeConn, page, param);

		if (page == null) {
			result.put("couponList", goodsList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodsList == null) {
				result.put("goodsList", 0);
			} else {
				result.put("goodsList", goodsList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 平台获取积分商品订单信息
	 */
	public String getGoodOrderList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请授权登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		Integer orderStatus = Integer.valueOf(request.getParameter("orderStatus"));//订单兑换状态
		String keyWords = request.getParameter("keyWords");
		// 2.查询
		List<IntegralGoodOrder> orderList = integralService.fingGoodOrderList(sourceId, closeConn, keyWords,orderStatus, page);
		if (page == null) {
			result.put("orderList", orderList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (orderList == null) {
				result.put("orderList", 0);
			} else {
				result.put("orderList", orderList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 获取平台活动商品列表
	 */
	public String getActivityGoodList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		//查询
		List<PlatformActiveGood> activeGoodList = integralService.findActivityGoodsList(sourceId, closeConn, page);
		
		if (page == null) {
			result.put("activeGoodList", activeGoodList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (activeGoodList == null) {
				result.put("activeGoodList", 0);
			} else {
				result.put("activeGoodList", activeGoodList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 平台删除积分商品兑换订单
	 */
	public String platformDelOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		//执行删除
		if(!integralService.platformDelOrderByOrderUUID(sourceId, closeConn, orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 平台商品管理获取积分商城商品
	 */
	public String getIntegralShopGoodList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		Integer goodStatus = Integer.parseInt(request.getParameter("goodStatus"));//商品状态 1上架，2下架，3未上架
		String  keyWords = request.getParameter("keyWords");
		
		// 2.查询
		List<IntegralGoods> goodList = integralService.findIntegralShopGoodList(sourceId, closeConn,keyWords, goodStatus, page);
		if (page == null) {
			result.put("goodList", goodList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodList == null) {
				result.put("goodList", 0);
			} else {
				result.put("goodList", goodList);
			}
			result.put("page", page);
			
		}
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 平台删除积分商城商品
	 */
	public String platformDelGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		String goodUUID = request.getParameter("goodUUID");
		//执行删除
		if(!integralService.platformDelGoodByGoodUUID(sourceId, closeConn, goodUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 积分商城商品下架
	 */
	public String downIntegralGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		
		String goodUUID = request.getParameter("goodUUID");
		//执行删除
		if(!integralService.platformDownGood(sourceId, closeConn, goodUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "商品下架成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 积分商城商品上架
	 */
	public String releaseIntegralGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		String goodUUID = request.getParameter("goodUUID");
		//执行删除
		if(!integralService.platformReleaseGood(sourceId, closeConn, goodUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "商品上架成功");
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取积分商城订单信息
	 * @throws ParseException 
	 */
	public Object getOrderInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		Integer type = Integer.parseInt(request.getParameter("type"));//type=1为积分商品订单详情 type=2为平台活动订单详情
		
		if(type == 1) {
			//查询
			IntegralGoodOrder goodOrder = integralService.findIntegralGoodOrderInfo(sourceId, closeConn, orderUUID);
			if(goodOrder == null) {
				result.put("status", "-1");
				result.put("msg", "未获取到相关信息");
				return JsonUtil.ObjToJson(result);
			}
			
			//若还未领取，判断是否过期，若已过期，修改订单为已过期
			if(goodOrder.getOrderStatus() == 0) {//未领取
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String closeTime = goodOrder.getCloseTime();
				Date date = new Date();
				long nowTime = date.getTime();
				long clsTime = sdf.parse(closeTime).getTime();
				if(nowTime >= clsTime) {//已过，修改订单状态
					integralService.updateIntegralOrderStatus(sourceId, closeConn, orderUUID);
					goodOrder.setOrderStatus(2);
				}
			}
			
			result.put("status", "1");
			result.put("msg", "信息获取成功");
			result.put("goodOrder", goodOrder);
			return JsonUtil.ObjToJson(result);
		}else {
			PlatformActivityOrder  goodOrder = integralService.findActiveOrderInfo(sourceId, closeConn, orderUUID);
			if(goodOrder == null) {
				result.put("status", "-1");
				result.put("msg", "未获取到相关信息");
				return JsonUtil.ObjToJson(result);
			}
			//若还未领取，判断是否过期，若已过期，修改订单为已过期
			if(goodOrder.getOrderStatus() == 0) {//未领取
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String closeTime = goodOrder.getCloseTime();
				Date date = new Date();
				long nowTime = date.getTime();
				long clsTime = sdf.parse(closeTime).getTime();
				if(nowTime >= clsTime) {//已过，修改订单状态
					integralService.updateActiveOrderStatus(sourceId, closeConn, orderUUID);
					goodOrder.setOrderStatus(2);
				}
			}
			
			result.put("status", "1");
			result.put("msg", "信息获取成功");
			result.put("goodOrder", goodOrder);
			return JsonUtil.ObjToJson(result);
		}
		
	}
	
	/**
	 * 获取订单uuid
	 */
	public String getOrderUUID(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse resposne) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		String orderUUID = request.getParameter("orderUUID");
		String vipUUID = request.getParameter("vipUUID");
		//商品信息表中查询活动uuid或者商品uuid
		OrderItem item = integralService.findOrderItem(sourceId, closeConn, orderUUID);
		String goodUUID = item.getParamUUID();//活动商品uuid
		String entityUUID = item.getItemUUID();//线下领取店铺uuid
		
		//查询订单uuid
		IntegralGoodOrder goodOrder = integralService.findActivityOrderItem(sourceId, closeConn, vipUUID, goodUUID, entityUUID);
		if(goodOrder == null) {
			result.put("status", "-1");
			result.put("msg", "未获取到相关信息");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "获取成功");
		result.put("goodOrder",goodOrder);
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取官方活动
	 */
	public String getpaltformActiveList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse resposne) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}

		// 查询结果
		List<PlatformActiveGood> activeList = integralService.findActiveGoodsListOfUser(sourceId, closeConn, page);
		
		if (page == null) {
			result.put("activeList", activeList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (activeList == null) {
				result.put("activeList", 0);
			} else {
				result.put("activeList", activeList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 获取用户参与的官方活动订单
	 */
	public String getUserJoinActiveOrderlist(String sourceId, boolean closeConn, HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请授权登陆");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		// 2.查询
		List<PlatformActivityOrder> orderList = integralService.findUserActiveOrderList(sourceId, closeConn, vipUUID, page);
		if (page == null) {
			result.put("orderList", orderList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (orderList == null) {
				result.put("orderList", 0);
			} else {
				result.put("orderList", orderList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 用户删除活动订单
	 */
	public String userDelJoinActiveOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		//1.登陆验证
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后操作");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		//执行删除
		if(!integralService.userDelAvtiveGoodOrder(sourceId, closeConn, orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 获取用户积分商品兑换信息
	 */
	public String getAnnounceMsg(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		List<IntegralGoodOrder> msgList = integralService.getAnnounceMsg(sourceId, closeConn);
		if(msgList == null || msgList.size() == 0) {
			result.put("status", "-1");
			result.put("msg", "为获取到相关数据");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "数据获取成功");
		result.put("msgList",msgList);
		return JsonUtil.ObjToJson(result);
	}
	
	
	
	
	//* *****************************************************PC端请求 *********************************************************************************/
	
	

	/**
	 * 获取用户积分详情记录，条件查询
	 */
	public String getIntegralDetailRecord(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
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
		
		String type = request.getParameter("type");
		//1.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		//2.查询
		List<IntegralRecordOfUser> integralList = integralService.findUserIntegralListByType(sourceId, closeConn, vipUUID,type, page);
		if (page == null) {
			result.put("rows", integralList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (integralList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", integralList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 新增或保存编辑积分规则
	 */
	public String createIntegralRule(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		IntegralRule rule = (IntegralRule) ReqToEntityUtil.reqToEntity(request, IntegralRule.class);
		
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		
		if(rule == null) {
			result.put("status", "-1");
			result.put("msg", "填写参数不能为空");
			return JsonUtil.ObjToJson(result);
		}
		if(!integralService.addOrUpdataIntegralRule(sourceId, closeConn, rule)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "新增或编辑保存成功");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 获取积分规则数据
	 */
	public String getIntegralRuleList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		String  type = request.getParameter("type");
		//1.分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		//2.查询
		List<IntegralRule> ruleList = integralService.findIntegralRuleList(sourceId, closeConn, page, type);
		if (page == null) {
			result.put("rows", ruleList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (ruleList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", ruleList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 删除积分规则
	 */
	public String deleteIntegralRule(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String ruleUUID = request.getParameter("ruleUUID");
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		if(StringUtils.isNullOrEmpty(ruleUUID)) {
			result.put("status", "-1");
			result.put("msg", "规则uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//执行删除
		if(!integralService.delIntegralRule(sourceId, closeConn, ruleUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}

		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * PC端获取积分商品兑换记录
	 */
	public String getIntegralExGoodsListSystem(String sourceId, boolean closeConn, HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
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
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String searchWords = request.getParameter("searchWords");
		//查询
		List<IntegralGoodOrder> integralGoodList = integralService.findIntegralExGoodsList(sourceId, closeConn, vipUUID,searchWords, page);
		
		if (page == null) {
			result.put("rows", integralGoodList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (integralGoodList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", integralGoodList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 积分商城商品添加
	 */
	public String addIntegralGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		String vipName = "";
		String vipImg = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请重新登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
			vipName = userInfo.getUserName();
			vipImg = userInfo.getUserPic();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
			vipName = vipInfo.getVipName();
			vipImg = vipInfo.getAvatarUrl();
		}
		
		IntegralGoods integralGoods = (IntegralGoods) ReqToEntityUtil.reqToEntity(request, IntegralGoods.class);
		String goodUUID = UUID.randomUUID().toString().replaceAll("-", "");
		integralGoods.setGoodUUID(goodUUID);
		integralGoods.setEntityUUID(vipUUID);
		integralGoods.setEntityName(vipName);
		integralGoods.setEntityLogo(vipImg);
		
		String goodParams = request.getParameter("goodParams");
		integralGoods.setGoodParams(goodParams);
		
		if(integralService.addIntegralGoods(sourceId, closeConn, integralGoods)) {
			result.put("status", "1");
			result.put("msg", "添加成功！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 获取积分商城添加的商品
	 */
	public String getIntegralGoodList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
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
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		// 获取查询条件
		Map<String, Object> param = new HashMap<String, Object>();// 封装查询条件
		param.put("range", request.getParameter("range"));
		param.put("status", request.getParameter("status"));
		param.put("searchWords", request.getParameter("searchWords"));
		param.put("vipUUID", vipUUID);
		
		// 查询结果
		List<IntegralGoods> goodsList = integralService.findIntegralGoodsList(sourceId, closeConn, page, param);

		if (page == null) {
			result.put("rows", goodsList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodsList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", goodsList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 手机端获取积分商城可兑换商品
	 */
	public String getExIntegralGoodList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		// 查询结果
		List<IntegralGoods> goodsList = integralService.findExIntegralGoodsList(sourceId, closeConn, page);
		
		if (page == null) {
			result.put("goodsList", goodsList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (goodsList == null) {
				result.put("goodsList", 0);
			} else {
				result.put("goodsList", goodsList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 平台添加活动商品
	 */
	public String addActivityGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		String vipName = "";
		String vipImg = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
			vipName = userInfo.getUserName();
			vipImg = userInfo.getUserPic();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
			vipName = vipInfo.getVipName();
			vipImg = vipInfo.getAvatarUrl();
		}
		
		PlatformActiveGood activeGood = (PlatformActiveGood) ReqToEntityUtil.reqToEntity(request, PlatformActiveGood.class);
		//后台设置相关参数属性
		activeGood.setActivityUUID(UUID.randomUUID().toString().replaceAll("-", ""));//活动UUID
		String goodUUID = UUID.randomUUID().toString().replaceAll("-", "");
		activeGood.setGoodUUID(goodUUID);//活动商品uuid
		activeGood.setEntityUUID(vipUUID);
		activeGood.setEntityName(vipName);
		activeGood.setEntityLogo(vipImg);
		Double price = Double.parseDouble(request.getParameter("price"))*100;
		Integer prices = price.intValue();
		activeGood.setPrice(prices);//设置商品价格，单位分
		
		if(integralService.addActivityGood(sourceId, closeConn, activeGood)) {
			result.put("status", "1");
			result.put("msg", "添加成功！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * PC端用户删除积分商品兑换记录
	 */
	public String delGoodsExRecordOfPc(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		String orderUUIDs = request.getParameter("orderUUIDs");
		if(StringUtils.isNullOrEmpty(orderUUIDs)) {
			result.put("status", "-1");
			result.put("msg", "订单uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		
		String[] orderUUIDArr = orderUUIDs.split(",");
		for (String orderUUID : orderUUIDArr) {
			//获取订单数据
			IntegralGoodOrder goodOrder = integralService.findIntegralGoodOrderInfo(sourceId, closeConn, orderUUID);
			if(goodOrder.getOrderStatus() == 0) {//未领取
				result.put("status", "-1");
				result.put("msg", "只能删除已领取和已过期订单");
				return JsonUtil.ObjToJson(result);
			}
			
			//执行删除
			if(!integralService.deleteUserIntegralGoodExRecord(sourceId, closeConn,orderUUID)) {
				result.put("status", "-1");
				result.put("msg", "系统繁忙，请稍后重试！");
				return JsonUtil.ObjToJson(result);
			}
			
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 平台获取积分详情记录
	 */
	public String platformGetIntegralDetailRecord(String sourceId, boolean closeConn, HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		//查询
		// 分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String type = request.getParameter("type");
		// 查询结果
		List<IntegralRecordOfUser> integralList = integralService.platformGetIntegralDetailRecord(sourceId, closeConn,type,page);

		if (page == null) {
			result.put("rows", integralList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (integralList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", integralList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 平台获取积分商城商品兑换记录
	 */
	public String platformGetIntegralExGoodsList(String sourceId, boolean closeConn, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
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
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String searchWords = request.getParameter("searchWords");
		
		// 查询
		List<IntegralGoodOrder> integralGoodList = integralService.paltformFindIntegralExGoodsList(sourceId, closeConn,searchWords,page);

		if (page == null) {
			result.put("rows", integralGoodList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (integralGoodList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", integralGoodList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 平台删除积分商品兑换订单记录
	 */
	public String platformDelGoodsExRecordOfPc(String sourceId, boolean closeConn, HttpServletRequest request,
			HttpServletResponse resopnse) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		String vipUUID = "";
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		if(userInfo != null) {
			vipUUID = userInfo.getUserUUID();
		}
		if(vipInfo != null) {
			vipUUID = vipInfo.getVipUUID();
		}
		String orderUUID = request.getParameter("orderUUID");
		if(StringUtils.isNullOrEmpty(orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "订单uuid不能为空");
			return JsonUtil.ObjToJson(result);
		}
		
		// 获取订单数据
		IntegralGoodOrder goodOrder = integralService.findIntegralGoodOrderInfo(sourceId, closeConn, orderUUID);
		if (goodOrder.getOrderStatus() == 0) {// 未领取
			result.put("status", "-1");
			result.put("msg", "只能删除已领取和已过期订单");
			return JsonUtil.ObjToJson(result);
		}

		// 执行删除
		if (!integralService.platformDeleteUserIntegralGoodExRecord(sourceId, closeConn, orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试！");
			return JsonUtil.ObjToJson(result);
		}
			
		
		
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 平台获取优惠活动列表
	 */
	public String findActiveList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		//分页设置
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		// 获取查询条件
		Map<String, Object> param = new HashMap<String, Object>();// 封装查询条件
		param.put("status", request.getParameter("status"));
		param.put("searchWords", request.getParameter("searchWords"));

		// 查询结果
		List<PlatformActiveGood> activeList = integralService.findActiveGoodsList(sourceId, closeConn, page, param);
		
		if (page == null) {
			result.put("rows", activeList);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (activeList == null) {
				result.put("rows", 0);
			} else {
				result.put("rows", activeList);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
		
	}

	/**
	 * 平台删除优惠活动
	 */
	public String deleteActive(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		String activityUUID = request.getParameter("activityUUID");
		Integer type = Integer.valueOf(request.getParameter("type"));//1-删除、2-上架、3-下架活动
		//执行相关操作
		if(!integralService.platformOprateActivity(sourceId, closeConn, activityUUID, type)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		String msg = "";
		if(type == 1) {
			msg = "删除成功";
		}else if(type == 2) {
			msg = "上架成功";
		}else {
			msg = "下架成功";
		}
		
		result.put("status", "1");
		result.put("msg", msg);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 *  获取活动详情
	 */
	public String getActivityInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		String activityUUID = request.getParameter("activityUUID");
		PlatformActiveGood activeGood = integralService.findActivityInfo(sourceId, closeConn, activityUUID);
		if(activeGood == null) {
			result.put("status", "-1");
			result.put("msg", "为获取到相关信息");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "信息获取成功");
		result.put("activeGood", activeGood);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 编辑修改活动信息
	 */
	public String editorActivityGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		
		PlatformActiveGood activeGood = (PlatformActiveGood) ReqToEntityUtil.reqToEntity(request, PlatformActiveGood.class);
		//后台设置相关参数属性
		Double price = Double.parseDouble(request.getParameter("price"))*100;
		Integer prices = price.intValue();
		activeGood.setPrice(prices);//设置商品价格，单位分
		Long id = Long.valueOf(request.getParameter("id"));
		activeGood.setId(id);
		if(integralService.editorActivityGood(sourceId, closeConn, activeGood)) {
			result.put("status", "1");
			result.put("msg", "编辑成功！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
		
	}
	
	/**
	 * 删除、上架、下架积分商品
	 */
	public String oprateIntegralGoods(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		String goodUUID = request.getParameter("goodUUID");
		Integer type = Integer.valueOf(request.getParameter("type"));//1-删除、2-上架、3-下架活动
		//执行相关操作
		if(!integralService.platformOprateIntegralGoods(sourceId, closeConn, goodUUID, type)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		String msg = "";
		if(type == 1) {
			msg = "删除成功";
		}else if(type == 2) {
			msg = "上架成功";
		}else {
			msg = "下架成功";
		}
		result.put("status", "1");
		result.put("msg", msg);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 获取积分商品信息
	 */
	public String platformGetIntegralGoodInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> result = new HashMap<String,Object>();//封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		String goodUUID = request.getParameter("goodUUID");
		IntegralGoods integralGood = integralService.platformFindIntegralGoodInfo(sourceId, closeConn, goodUUID);
		if(integralGood == null) {
			result.put("status", "-1");
			result.put("msg", "为获取到相关信息");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "信息获取成功");
		result.put("integralGood", integralGood);
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 编辑修改积分商品信息
	 */
	public String editorIntegralGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();// 封装返回结果
		UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userinfo");
		VipInfo vipInfo = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(userInfo == null && vipInfo == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆");
			return JsonUtil.ObjToJson(result);
		}
		IntegralGoods integralGood = (IntegralGoods) ReqToEntityUtil.reqToEntity(request, IntegralGoods.class);
		if(integralService.editorIntegralGood(sourceId, closeConn, integralGood)) {
			result.put("status", "1");
			result.put("msg", "编辑成功！");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}

	

	

	

	

	
	
	

	

	

	

	

	

	

	

	

	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
