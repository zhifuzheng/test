package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.reflect.TypeToken;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.pay.config.PayConfig;
import com.xryb.zhtc.advice.IOrderAdvice;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemComment;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.factory.AdviceFactory;
import com.xryb.zhtc.service.ICartService;
import com.xryb.zhtc.service.IItemCommentService;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.service.IOrderItemService;
import com.xryb.zhtc.service.IOrderService;
import com.xryb.zhtc.service.impl.CartServiceImpl;
import com.xryb.zhtc.service.impl.ItemCommentServiceImpl;
import com.xryb.zhtc.service.impl.ItemParamServiceImpl;
import com.xryb.zhtc.service.impl.ItemServiceImpl;
import com.xryb.zhtc.service.impl.OrderItemServiceImpl;
import com.xryb.zhtc.service.impl.OrderServiceImpl;
import com.xryb.zhtc.util.AccountUtil;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.KindEditorUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 订单管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class OrderMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=ItemServiceImpl.class)
	private IItemService itemService;
	
	@Auto(name=ItemParamServiceImpl.class)
	private IItemParamService paramService;
	
	@Auto(name=OrderServiceImpl.class)
	private IOrderService orderService;
	
	@Auto(name=OrderItemServiceImpl.class)
	private IOrderItemService orderItemService;
	
	@Auto(name=ItemCommentServiceImpl.class)
	private IItemCommentService commentService;
	
	@Auto(name=CartServiceImpl.class)
	private ICartService cartService;
	
	/**
	 * 新增订单
	 * 
	 * 订单协议
	 * 
	 * 订单通知
	 * advice					通知实现类的编码
	 * 注意：参数名必须为advice，参数值为实现类的编码，需要在包com.xryb.zhtc.advice中创建一个类，
	 * 并实现IOrderAdvice接口，在advice.properties配置文件中配置，编码=实现类的类名，
	 * 订单在生成前会执行通知方法，若通知方法执行时抛出异常或返回false，则不会生成订单，
	 * ajax请求参数格式	=>	advice:9527
	 * 
	 * 订单类型
	 * orderType(选填)			订单类型：1充值，2购物，3进货，4其他，默认值为2
	 * 注意：参数名必须为orderType，参数值为订单类型，ajax请求参数格式	=>	orderType:1
	 * 
	 * 商品信息(必填)
	 * paramData				多个商品用逗号分隔
	 * 注意：参数名必须为paramData，参数值为商品信息对象，ajax请求参数格式      =>  paramData:{A店铺UUID={A商品UUID=A商品数量, B商品UUID=B商品数量},B店铺UUID={A商品UUID=A商品数量, B商品UUID=B商品数量}}
	 * 
	 * 优惠券信息
	 * couponData				多个优惠券用逗号分隔
	 * 注意：参数名必须为couponData，参数值为优惠券信息对象，ajax请求参数格式      =>  couponData:{A商品UUID=A商品优惠券UUID, A店铺UUID=A店铺优惠券UUID}
	 * 
	 * 买家留言(选填)
	 * msg						多组留言用逗号分隔
	 * 注意：参数名必须为msg，参数值为留言对象，ajax请求参数格式	  =>  msg:{A店铺UUID=A店铺留言, B店铺UUID=B店铺留言}
	 * 
	 * 
	 */
	public String genOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String payerUUID = vip.getVipUUID();
		String payerName = vip.getVipName();
		String payerLogo = vip.getAvatarUrl();
		String receiverMobile = vip.getVipMobile();
		String detailAddr = vip.getAddress();
		String firstUUID = vip.getFirstUUID();
		String secondUUID = vip.getSecondUUID();
		
		String orderType = request.getParameter("orderType");
		if("3".equals(orderType)){
			payerUUID = request.getParameter("entityUUID");
			payerName = request.getParameter("entityName");
			payerLogo = request.getParameter("entityLogo");
			receiverMobile = request.getParameter("mobile");
			detailAddr = request.getParameter("addr");
			firstUUID = null;
			secondUUID = null;
		}
		
		if(RegExpUtil.isNullOrEmpty(payerName) || RegExpUtil.isNullOrEmpty(receiverMobile) || RegExpUtil.isNullOrEmpty(detailAddr)){
			result.put("status", "-2");
			result.put("msg", "会员信息不全，请完善会员信息后再下单");
			return JsonUtil.ObjToJson(result);
		}
		
		//获取订单通知
		String advice = request.getParameter("advice");
		
		//获取优惠券信息
		String couponJson = request.getParameter("couponData");
		Map<String, String> couponData = null;
		if(!RegExpUtil.isNullOrEmpty(couponJson)){
			try {
				couponData = (Map<String, String>) JsonUtil.JsonToObj(couponJson, new TypeToken<Map<String, String>>() {}.getType());
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "-3");
				result.put("msg", "优惠券数据格式错误，服务器无法解析，请联系管理员");
				return JsonUtil.ObjToJson(result);
			}
		}
		
		//获取买家留言
		String msgJson = request.getParameter("msg");
		Map<String, String> msgData = null;
		if(!RegExpUtil.isNullOrEmpty(msgJson)){
			try {
				msgData = (Map<String, String>) JsonUtil.JsonToObj(msgJson, new TypeToken<Map<String, String>>() {}.getType());
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "-4");
				result.put("msg", "买家留言数据格式错误，服务器无法解析，请联系管理员");
				return JsonUtil.ObjToJson(result);
			}
		}
		
		//获取商品数据并检验数据合法性
		String paramJson = request.getParameter("paramData");
		if(RegExpUtil.isNullOrEmpty(paramJson)){
			result.put("status", "-5");
			result.put("msg", "没有获取到订单数据");
			return JsonUtil.ObjToJson(result);
		}
		Map<String,Map<String, Integer>> paramData = null;
		try {
			paramData = (Map<String,Map<String, Integer>>) JsonUtil.JsonToObj(paramJson, new TypeToken<Map<String,Map<String, Integer>>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "-6");
			result.put("msg", "订单数据格式错误，服务器无法解析，请联系管理员");
			return JsonUtil.ObjToJson(result);
		}
		if(paramData == null || paramData.size() == 0){
			result.put("status", "-7");
			result.put("msg", "没有获取到订单数据");
			return JsonUtil.ObjToJson(result);
		}
		Map<String, List<String>> uuidsData = new HashMap<String, List<String>>();
		for (Map.Entry<String, Map<String, Integer>> entity : paramData.entrySet()) {
			List<String> uuids = uuidsData.get(entity.getKey());
			if(uuids == null){
				uuids = new ArrayList<String>();
				uuidsData.put(entity.getKey(), uuids);
			}
			for (Map.Entry<String, Integer> param : entity.getValue().entrySet()){
				if(param.getValue() < 1){
					result.put("status", "-8");
					result.put("msg", "商品数量异常");
					return JsonUtil.ObjToJson(result);
				}
				uuids.add(param.getKey());
			}
		}
		
		//生成订单
		List<String> orderUUIDs = new ArrayList<String>();
		List<String> paramUUIDs = new ArrayList<String>();
		
		String batchNo = null;
		if (paramData.size() > 1) {
			batchNo = "_" + UUID.randomUUID().toString().replaceAll("-", "").substring(1);
		}
		for(Map.Entry<String, List<String>> entity : uuidsData.entrySet()){
			String entityUUID = entity.getKey();
			List<String> uuids = entity.getValue();
			Integer totalCount = 0;
			Integer totalPrice = 0;
			
			List<OrderItem> orderItemList = new ArrayList<OrderItem>();
			String orderUUID = UUID.randomUUID().toString().replaceAll("-", "");
			
			Lock lock = jedisClient.getEntityLock(entityUUID);
			lock.lock();
			try {
				List<ItemParam> paramList = paramService.findListByUUIDs(sourceId, closeConn, uuids);
				for (ItemParam param : paramList) {
					String paramStatus = param.getParamStatus();
					String paramUUID = param.getParamUUID();
					Integer count = paramData.get(entityUUID).get(paramUUID);
					if("2".equals(paramStatus)){
						String unshelve = result.get("unshelve");
						if(unshelve == null){
							unshelve = "1";
						}else{
							unshelve = Integer.parseInt(unshelve)+1+"";
						}
						result.put("unshelve", unshelve);
						continue;
					}
					if (param.getStock() < count) {
						String shortage = result.get("shortage");
						if(shortage == null){
							shortage = "1";
						}else{
							shortage = Integer.parseInt(shortage)+1+"";
						}
						result.put("shortage", shortage);
						continue;
					}
					paramUUIDs.add(paramUUID);

					OrderItem orderItem = new OrderItem();
					orderItem.setOrderUUID(orderUUID);
					if (!RegExpUtil.isNullOrEmpty(batchNo)) {
						orderItem.setBatchNo(batchNo);
					}
					if (couponData != null) {
						String paramCouponUUID = couponData.get(paramUUID);
						if (!RegExpUtil.isNullOrEmpty(batchNo)) {
							orderItem.setCouponUUID(paramCouponUUID);
						}
					}
					
					Integer price = param.getPrice();
					//会员价
					if("0".equals(param.getItemType()) && "2".equals(vip.getVipType())){
						price = param.getVipPrice();
					}
					orderItem.setItemUUID(param.getItemUUID());
					orderItem.setItemType(param.getItemType());
					orderItem.setParamUUID(paramUUID);
					orderItem.setTitle(param.getTitle());
					orderItem.setPrice(price);
					orderItem.setPayment(price);
					orderItem.setCount(count);
					orderItem.setAllowRefund(count);
					orderItem.setItemImg(param.getItemImg());
					orderItem.setParamData(param.getParamData());
					orderItem.setPayerUUID(payerUUID);
					orderItem.setPayerName(payerName);
					orderItem.setPayerLogo(payerLogo);
					String dbs = param.getDistributeStatus();
					orderItem.setDistributeStatus(dbs);
					if ("1".equals(dbs)) {
						orderItem.setFirstUUID(firstUUID);
						orderItem.setSecondUUID(secondUUID);
						orderItem.setFirstRatio(param.getFirstRatio());
						orderItem.setSecondRatio(param.getSecondRatio());
					}
					orderItemList.add(orderItem);

					//计算订单总件数、总价格
					totalCount += orderItem.getCount();
					totalPrice += orderItem.getPrice() * orderItem.getCount();
				}
				if (orderItemList.size() == 0) {
					continue;
				}
				Order order = new Order();
				order.setOrderUUID(orderUUID);
				if (!RegExpUtil.isNullOrEmpty(batchNo)) {
					order.setBatchNo(batchNo);
				}
				if (couponData != null) {
					String couponUUID = couponData.get(entityUUID);
					if (!RegExpUtil.isNullOrEmpty(couponUUID)) {
						order.setCouponUUID(couponUUID);
					}
				}
				if (!RegExpUtil.isNullOrEmpty(advice)) {
					order.setAdvice(advice);
				}
				if (paramList.size() > 1) {
					order.setTitle(paramList.get(0).getEntityName());
				} else {
					order.setTitle(paramList.get(0).getTitle());
				}
				order.setEntityUUID(entityUUID);
				order.setEntityName(paramList.get(0).getEntityName());
				order.setEntityLogo(paramList.get(0).getEntityLogo());
				order.setTotalCount(totalCount);
				order.setTotalPrice(totalPrice);
				order.setPayment(totalPrice);
				if(!RegExpUtil.isNullOrEmpty(orderType)){
					order.setOrderType(orderType);
				}
				order.setPayerUUID(payerUUID);
				order.setPayerName(payerName);
				order.setPayerLogo(payerLogo);
				order.setReceiverMobile(receiverMobile);
				order.setDetailAddr(detailAddr);
				if (msgData != null) {
					String msg = msgData.get(entityUUID);
					if (!RegExpUtil.isNullOrEmpty(msg)) {
						order.setPersonMsg(msg);
					}
				}
				order.setFirstUUID(firstUUID);
				order.setSecondUUID(secondUUID);
				//执行订单生成通知
				if (!RegExpUtil.isNullOrEmpty(advice)) {
					try {
						String className = ReadProperties.getProperty("/advice.properties", advice);
						if (!RegExpUtil.isNullOrEmpty(className)) {
							IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
							if (!orderAdvice.genAdvice(sourceId, closeConn, order, orderItemList)) {
								continue;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						result.put("status", "-9");
						result.put("msg", "执行通知方法失败");
						System.out.println("通知订单生成失败！编号为\"" + advice + "\"");
						return JsonUtil.ObjToJson(result);
					}
				}
				orderItemService.batchInsert(sourceId, closeConn, orderItemList);
				orderService.saveOrUpdate(sourceId, closeConn, order);
				//减库存
				Object[][] params = new Object[orderItemList.size()][2];
				int i = 0;
				for (OrderItem orderItem : orderItemList) {
					params[i][0] = orderItem.getCount();
					params[i][1] = orderItem.getParamUUID();
					i++;
				}
				paramService.decreseStock(sourceId, closeConn, params);
				orderUUIDs.add(orderUUID);
			} finally {
				lock.unlock();
			}
		}
		
		String source = request.getParameter("source");
		//移除购物车中的商品
		if("cart".equals(source)){
			Map<String, Integer> cart = jedisClient.getCart("cart:"+payerUUID);
			for (String paramUUID : paramUUIDs) {
				cart.remove(paramUUID);
			}
			jedisClient.setCart("cart:"+payerUUID, cart);
			//同步数据库购物车
			Map<String, String> findMap = new HashMap<>();
			findMap.put("vipUUID", payerUUID);
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("cartData", JsonUtil.ObjToJson(cart));
			cartService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		}
		
		if(orderUUIDs.size() == 0){
			result.put("status", "-10");
			result.put("msg", "商品下架或库存不足");
		}else{
			result.put("status", "1");
			if(orderUUIDs.size() == 1){
				result.put("orderUUID", orderUUIDs.get(0));
			}else{
				result.put("orderUUID", batchNo);
			}
		}
		return JsonUtil.ObjToJson(result);
	}
	
	/**
	 * 生成充值订单
	 */
	public String genRechargeOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		String payment = request.getParameter("payment");
		String advice = request.getParameter("advice");
		
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String payerName = vip.getVipName();
		String receiverMobile = vip.getVipMobile();
		String detailAddr = vip.getAddress();
		if(RegExpUtil.isNullOrEmpty(payerName) || RegExpUtil.isNullOrEmpty(receiverMobile) || RegExpUtil.isNullOrEmpty(detailAddr)){
			result.put("status", "-2");
			result.put("msg", "会员信息不全，请完善会员信息后再充值");
			return JsonUtil.ObjToJson(result);
		}
		if(RegExpUtil.isNullOrEmpty(payment)){
			result.put("status", "-3");
			result.put("msg", "请输入充值金额");
			return JsonUtil.ObjToJson(result);
		}
		//验证充值金额
		if(!AccountUtil.checkDigit(payment)){
			result.put("status", "-4");
			result.put("msg", "充值金额错误");
			return JsonUtil.ObjToJson(result);
		}
		Integer money = Integer.valueOf(payment);
		if(money <= 0){
			result.put("status", "-5");
			result.put("msg", "充值金额必须大于0");
			return JsonUtil.ObjToJson(result);
		}
		
		//获取充值商品信息
		String entityUUID = "$"+vip.getVipUUID().substring(1);
		String entityName = PayConfig.zhtcName;
		String entityLogo = PayConfig.rechargeItemImg;
		String itemUUID = PayConfig.rechargeUUID;
		String title = PayConfig.rechargeTitle;
		String itemImg = PayConfig.rechargeItemImg;
		String paramUUID = PayConfig.paramUUID;
		String paramData = PayConfig.rechargeParamData;
		
		//生成交易流水号
		String orderUUID = UUID.randomUUID().toString().replaceAll("-", "");
		
		//创建充值订单
		Order order = new Order();
		order.setOrderUUID(orderUUID);
		if (!RegExpUtil.isNullOrEmpty(advice)) {
			order.setAdvice(advice);
		}
		order.setTitle(title);
		order.setEntityUUID(entityUUID);
		order.setEntityName(entityName);
		order.setEntityLogo(entityLogo);
		order.setTotalCount(1);
		order.setTotalPrice(money);
		order.setPayment(money);
		order.setOrderType("1");//订单类型：1充值，2购物，3进货，4其他，默认值为2
		order.setIsDel("1");
		order.setPayerUUID(vip.getVipUUID());
		order.setPayerName(payerName);
		order.setPayerLogo(vip.getAvatarUrl());
		order.setReceiverMobile(receiverMobile);
		order.setDetailAddr(detailAddr);
		
		
		//创建充值订单项
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderUUID(orderUUID);
		orderItem.setItemUUID(itemUUID);
		orderItem.setItemType("1");
		orderItem.setParamUUID(paramUUID);
		orderItem.setTitle(title);
		orderItem.setPrice(money);
		orderItem.setPayment(money);
		orderItem.setCount(1);
		orderItem.setAllowRefund(1);
		orderItem.setItemImg(itemImg);
		orderItem.setParamData(paramData);
		orderItem.setIsDel("1");
		orderItem.setPayerUUID(vip.getVipUUID());
		orderItem.setPayerName(payerName);
		orderItem.setPayerLogo(vip.getAvatarUrl());
		orderItem.setDistributeStatus("0");//商品分销状态：0关闭，1开启
		orderItem.setFirstRatio(0d);
		orderItem.setSecondRatio(0d);
		
		ArrayList<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem);
		//执行订单生成通知
		if (!RegExpUtil.isNullOrEmpty(advice)) {
			try {
				String className = ReadProperties.getProperty("/advice.properties", advice);
				if (!RegExpUtil.isNullOrEmpty(className)) {
					IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
					if(!orderAdvice.genAdvice(sourceId, closeConn, order, orderItemList)){
						result.put("status", "-6");
						result.put("msg", "执行通知方法失败");
						System.out.println("通知订单生成失败！编号为\"" + advice + "\"");
						return JsonUtil.ObjToJson(result);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "-6");
				result.put("msg", "执行通知方法失败");
				System.out.println("通知订单生成失败！编号为\"" + advice + "\"");
				return JsonUtil.ObjToJson(result);
			}
		}
		
		orderItemService.saveOrUpdate(sourceId, closeConn, orderItem);
		orderService.saveOrUpdate(sourceId, closeConn, order);
		result.put("status", "1");
		result.put("msg", "生成订单成功");
		result.put("orderUUID", orderUUID);
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 生成平台订单
	 */
	public String genPlatOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//必填
		String openId = request.getParameter("openId");
		String title = request.getParameter("title");
		String payment = request.getParameter("payment");
		String advice = request.getParameter("advice");
		
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		if(RegExpUtil.isNullOrEmpty(vip.getVipName()) || RegExpUtil.isNullOrEmpty(vip.getVipMobile()) || RegExpUtil.isNullOrEmpty(vip.getAddress())){
			result.put("status", "-2");
			result.put("msg", "会员信息不全，请完善会员信息后再提交");
			return JsonUtil.ObjToJson(result);
		}
		if(RegExpUtil.isNullOrEmpty(title)){
			result.put("status", "-3");
			result.put("msg", "订单标题不能为空");
			return JsonUtil.ObjToJson(result);
		}
		if(RegExpUtil.isNullOrEmpty(payment)){
			result.put("status", "-4");
			result.put("msg", "订单金额不能为空");
			return JsonUtil.ObjToJson(result);
		}
		//验证充值金额
		if(!AccountUtil.checkDigit(payment)){
			result.put("status", "-5");
			result.put("msg", "订单金额错误");
			return JsonUtil.ObjToJson(result);
		}
		Integer money = Integer.valueOf(payment);
		if(money <= 0){
			result.put("status", "-6");
			result.put("msg", "订单金额至少为1分");
			return JsonUtil.ObjToJson(result);
		}
		if(RegExpUtil.isNullOrEmpty(advice)){
			result.put("status", "-7");
			result.put("msg", "订单通知编号不能为空");
			return JsonUtil.ObjToJson(result);
		}
		
		//选填
		String itemUUID = request.getParameter("itemUUID");
		String paramUUID = request.getParameter("paramUUID");
		String itemImg = request.getParameter("itemImg");
		String payerUUID = request.getParameter("payerUUID");
		String payerName = request.getParameter("payerName");
		String payerLogo = request.getParameter("payerLogo");
		String receiverMobile = request.getParameter("receiverMobile");
		String detailAddr = request.getParameter("detailAddr");
		if(RegExpUtil.isNullOrEmpty(payerUUID)){
			payerUUID = vip.getVipUUID();
		}
		if(RegExpUtil.isNullOrEmpty(payerName)){
			payerName = vip.getVipName();
		}
		if(RegExpUtil.isNullOrEmpty(payerLogo)){
			payerLogo = vip.getAvatarUrl();
		}
		if(RegExpUtil.isNullOrEmpty(receiverMobile)){
			receiverMobile = vip.getVipMobile();
		}
		if(RegExpUtil.isNullOrEmpty(detailAddr)){
			detailAddr = vip.getAddress();
		}
		
		//获取平台信息
		String entityUUID = PayConfig.zhtcUUID;
		String entityName = PayConfig.zhtcName;
		String entityLogo = PayConfig.rechargeItemImg;
		if(RegExpUtil.isNullOrEmpty(itemUUID)){
			itemUUID = PayConfig.rechargeUUID;
		}
		if(RegExpUtil.isNullOrEmpty(paramUUID)){
			paramUUID = PayConfig.paramUUID;
		}
		if(RegExpUtil.isNullOrEmpty(itemImg)){
			itemImg = PayConfig.rechargeItemImg;
		}
		
		//生成交易流水号
		String orderUUID = UUID.randomUUID().toString().replaceAll("-", "");
		
		//创建充值订单
		Order order = new Order();
		order.setOrderUUID(orderUUID);
		order.setAdvice(advice);
		order.setTitle(title);
		order.setEntityUUID(entityUUID);
		order.setEntityName(entityName);
		order.setEntityLogo(entityLogo);
		order.setTotalCount(1);
		order.setTotalPrice(money);
		order.setPayment(money);
		order.setOrderType("4");//订单类型：1充值，2购物，3进货，4其他，默认值为2
		order.setIsDel("1");
		order.setPayerUUID(payerUUID);
		order.setPayerName(payerName);
		order.setPayerLogo(payerLogo);
		order.setReceiverMobile(receiverMobile);
		order.setDetailAddr(detailAddr);
		
		
		//创建充值订单项
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderUUID(orderUUID);
		orderItem.setItemUUID(itemUUID);
		orderItem.setItemType("4");
		orderItem.setParamUUID(paramUUID);
		orderItem.setTitle(title);
		orderItem.setPrice(money);
		orderItem.setPayment(money);
		orderItem.setCount(1);
		orderItem.setAllowRefund(1);
		orderItem.setItemImg(itemImg);
		orderItem.setIsDel("1");
		orderItem.setPayerUUID(payerUUID);
		orderItem.setPayerName(payerName);
		orderItem.setPayerLogo(payerLogo);
		orderItem.setDistributeStatus("0");//商品分销状态：0关闭，1开启
		
		ArrayList<OrderItem> orderItemList = new ArrayList<>();
		orderItemList.add(orderItem);
		//执行订单生成通知
		try {
			String className = ReadProperties.getProperty("/advice.properties", advice);
			if (RegExpUtil.isNullOrEmpty(className)) {
				result.put("status", "-8");
				result.put("msg", "advice.properties中未配置通知\"" + advice + "\"");
				return JsonUtil.ObjToJson(result);
			}
			IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
			if(!orderAdvice.genAdvice(sourceId, closeConn, order, orderItemList)){
				result.put("status", "-9");
				result.put("msg", "执行通知方法失败");
				System.out.println("通知订单生成失败！编号为\"" + advice + "\"");
				return JsonUtil.ObjToJson(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "-9");
			result.put("msg", "执行通知方法失败");
			System.out.println("通知订单生成失败！编号为\"" + advice + "\"");
			return JsonUtil.ObjToJson(result);
		}
		
		orderItemService.saveOrUpdate(sourceId, closeConn, orderItem);
		orderService.saveOrUpdate(sourceId, closeConn, order);
		result.put("status", "1");
		result.put("msg", "生成订单成功");
		result.put("orderUUID", orderUUID);
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 删除订单
	 */
	public String delOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"3".equals(order.getOrderStatus()) && !"4".equals(order.getOrderStatus()) && !"-1".equals(order.getOrderStatus()) && !"-2".equals(order.getOrderStatus()) && !"-4".equals(order.getOrderStatus()) && !"-5".equals(order.getOrderStatus()) && !"-7".equals(order.getOrderStatus()) && !"-8".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能删除订单");
			return JsonUtil.ObjToJson(result);
		}
		if("1".equals(order.getIsDel())){
			result.put("status", "-4");
			result.put("msg", "订单已删除，不能再删除订单");
			return JsonUtil.ObjToJson(result);
		}
		
		Map<String, String> paramsMap = new HashMap<>();
		String isShow = request.getParameter("isShow");
		if(!RegExpUtil.isNullOrEmpty(isShow)){
			paramsMap.put("isShow", isShow);
		}else{
			paramsMap.put("isDel", "1");
		}
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		
		result.put("status", "1");
		result.put("msg", "删除订单成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 取消订单
	 */
	public String abolishOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		String personMsg = request.getParameter("personMsg");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"0".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能取消订单");
			return JsonUtil.ObjToJson(result);
		}
		Map<String, String> paramsMap = new HashMap<>();
		//更新订单状态
		paramsMap.put("personMsg", personMsg);
		paramsMap.put("orderStatus", "-1");
		paramsMap.put("closeTime", DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		//更新订单项状态
		paramsMap.remove("personMsg");
		paramsMap.remove("closeTime");
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		//归还商品库存
		List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
		Map<String, Integer> uuid2count = new HashMap<>();
		for(OrderItem orderItem : orderItemList){
			uuid2count.put(orderItem.getParamUUID(), orderItem.getCount());
		}
		result = increseStock(sourceId, closeConn, order.getEntityUUID(), uuid2count);
		if(!"1".equals(result.get("status"))){
			return JsonUtil.ObjToJson(result);
		}
		
		String advice = order.getAdvice();
		//执行订单取消通知
		if(!RegExpUtil.isNullOrEmpty(advice)){
			try {
				String className = ReadProperties.getProperty("/advice.properties", advice);
				if (!RegExpUtil.isNullOrEmpty(className)) {
					IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
					if(!orderAdvice.abolishAdvice(sourceId, closeConn, order)){
						System.out.println("通知订单取消失败！编号为\"" + advice + "\"");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("通知订单取消失败！编号为\"" + advice + "\"");
			}
		}
		
		result.put("status", "1");
		result.put("msg", "取消订单成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 订单超时
	 */
	public String timeoutOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"0".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能执行订单超时");
			return JsonUtil.ObjToJson(result);
		}
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderStatus", "-2");
		paramsMap.put("closeTime", DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		paramsMap.remove("closeTime");
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		
		//归还商品库存
		List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
		Map<String, Integer> uuid2count = new HashMap<>();
		for(OrderItem orderItem : orderItemList){
			uuid2count.put(orderItem.getParamUUID(), orderItem.getCount());
		}
		result = increseStock(sourceId, closeConn, order.getEntityUUID(), uuid2count);
		if(!"1".equals(result.get("status"))){
			return JsonUtil.ObjToJson(result);
		}
		
		String advice = order.getAdvice();
		//执行订单超时通知
		if(!RegExpUtil.isNullOrEmpty(advice)){
			try {
				String className = ReadProperties.getProperty("/advice.properties", advice);
				if (!RegExpUtil.isNullOrEmpty(className)) {
					IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
					if(!orderAdvice.timeoutAdvice(sourceId, closeConn, order)){
						System.out.println("通知订单超时失败！编号为\"" + advice + "\"");
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("通知订单超时失败！编号为\"" + advice + "\"");
			}
		}
		
		result.put("status", "1");
		result.put("msg", "订单超时执行成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 发起退款
	 */
	public String applyRefund(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"1".equals(order.getOrderStatus()) && !"2".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能发起退款");
			return JsonUtil.ObjToJson(result);
		}
		
		String personMsg = request.getParameter("personMsg");
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderStatus", "-3");
		paramsMap.put("personMsg", personMsg);
		paramsMap.put("originStatus", order.getOrderStatus());
		paramsMap.put("rejectMsg", null);
		orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		paramsMap.clear();
		paramsMap.put("orderStatus", "-3");
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		
		result.put("status", "1");
		result.put("msg", "发起退款请求成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 发起退货
	 */
	public String applyReturn(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"3".equals(order.getOrderStatus()) && !"4".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能发起退货");
			return JsonUtil.ObjToJson(result);
		}
		String refundJson = request.getParameter("refundData");
		Map<String,Integer> refundData = null;
		try {
			refundData = (Map<String, Integer>) JsonUtil.JsonToObj(refundJson, new TypeToken<Map<String, Integer>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "-4");
			result.put("msg", "退货订单数据格式错误，服务器无法解析，请联系管理员");
			return JsonUtil.ObjToJson(result);
		}
		List<String> uuids = new ArrayList<>();
		for(Map.Entry<String, Integer> orderItem : refundData.entrySet()){
			if(orderItem.getValue() < 1){
				result.put("status", "-5");
				result.put("msg", "退货数量不能小于1件");
				return JsonUtil.ObjToJson(result);
			}
			uuids.add(orderItem.getKey());
		}
		
		List<OrderItem> refundList = orderItemService.findRefundList(sourceId, closeConn, uuids, orderUUID);
		List<OrderItem> newRefundList = new ArrayList<>();
		String refundUUID = UUID.randomUUID().toString().replaceAll("-", "");
		Integer totalCount = 0;
		Integer reducePrice = 0;
		Integer payment = 0;
		Integer firstIncome = 0;
		Integer secondIncome = 0;
		for(OrderItem orderItem : refundList){
			Integer allowRefund = orderItem.getAllowRefund();
			Integer refundCount = refundData.get(orderItem.getParamUUID());
			if(allowRefund < refundCount){
				result.put("status", "-6");
				result.put("msg", "退货数量超出允许数量");
				return JsonUtil.ObjToJson(result);
			}
			
			//生成退货订单项
			OrderItem refundOrderItem = new OrderItem();
			BeanUtils.copyProperties(refundOrderItem, orderItem);
			refundOrderItem.setId(null);
			refundOrderItem.setOrderUUID(refundUUID);
			refundOrderItem.setBatchNo(null);
			refundOrderItem.setCount(refundCount);
			refundOrderItem.setOrderStatus("-6");
			newRefundList.add(refundOrderItem);
			
			if(allowRefund == refundCount){
				orderItem.setOrderStatus("-9");
				orderItem.setIsDel("1");
				orderItem.setIsShow("0");
				result.put("orderStatus", "-9");
			}
			orderItem.setAllowRefund(allowRefund-refundCount);
			totalCount += refundCount;
			reducePrice += orderItem.getReducePrice() * refundCount;
			payment += orderItem.getPayment() * refundCount;
			firstIncome += orderItem.getFirstIncome();
			secondIncome += orderItem.getSecondIncome();
		}
		
		//生成退货单
		String personMsg = request.getParameter("personMsg");
		Order refundOrder = new Order();
		BeanUtils.copyProperties(refundOrder, order);
		refundOrder.setId(null);
		refundOrder.setOrderUUID(refundUUID);
		refundOrder.setBatchNo(null);
		refundOrder.setRefundNo(order.getBatchNo()==null?order.getOrderUUID():order.getBatchNo());
		refundOrder.setTotalCount(totalCount);
		refundOrder.setTotalPrice(order.getPayment());
		refundOrder.setReducePrice(reducePrice);
		refundOrder.setPayment(payment);
		refundOrder.setOrderStatus("-6");
		refundOrder.setRejectMsg(null);
		refundOrder.setIsDel("0");
		refundOrder.setIsShow("1");
		refundOrder.setPersonMsg(personMsg);
		refundOrder.setFirstIncome(firstIncome);
		refundOrder.setSecondIncome(secondIncome);
		
		orderItemService.batchUpByField(sourceId, closeConn, refundList, "id");
		Integer total = orderItemService.getTotal(sourceId, closeConn, findMap);
		findMap.put("orderStatus", "-9");
		Integer complete = orderItemService.getTotal(sourceId, closeConn, findMap);
		if(total == complete){
			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("personMsg", personMsg);
			paramsMap.put("orderStatus", "-9");
			paramsMap.put("originStatus", order.getOrderStatus());
			paramsMap.put("isDel", "1");
			paramsMap.put("isShow", "-1");
			findMap.remove("orderStatus");
			orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		}
		
		orderService.saveOrUpdate(sourceId, closeConn, refundOrder);
		orderItemService.batchInsert(sourceId, closeConn, newRefundList);
		
		result.put("status", "1");
		result.put("msg", "发起退货请求成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 确认收货
	 */
	public String receipt(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		if(null == order){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"2".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能确认收货");
			return JsonUtil.ObjToJson(result);
		}
		
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderStatus", "3");
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		if("3".equals(order.getOrderType())){
			String entityUUID = order.getPayerUUID();
			String entityName = order.getPayerName();
			String entityLogo = order.getPayerLogo();
			
			Map<String, Integer> uuid2count = new HashMap<>();
			List<String> uuids = new ArrayList<>();
			List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
			for(OrderItem orderItem : orderItemList){
				uuid2count.put(orderItem.getParamUUID(), orderItem.getCount());
				uuids.add(orderItem.getParamUUID());
			}
			
			result = purchase(sourceId, closeConn, entityUUID, entityName, entityLogo, uuid2count);
			if(!"1".equals(result.get("status"))){
				return JsonUtil.ObjToJson(result);
			}
		}
		result.put("status", "1");
		result.put("msg", "确认收货成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 零售商确认收货
	 */
	public Map<String, String> purchase(String sourceId, boolean closeConn, String entityUUID, String entityName, String entityLogo, Map<String, Integer> uuid2count) throws Exception {
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
		result.put("status", "1");
		result.put("msg", "零售商进货成功");
		return result;
	}
	/**
	 * 移动端分页查询订单信息
	 */
	public String findOrderPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		//根据条件查询订单列表
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Order.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		orderService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		//根据订单UUID列表批量查询订单关联的订单项
		List<Order> orderList = (List<Order>) page.getRows();
		if(orderList != null){
			ArrayList<String> uuids = new ArrayList<String>();
			for(Order o : orderList){
				uuids.add(o.getOrderUUID());
			}
			List<OrderItem> orderItemList = orderItemService.findListByUUIDs(sourceId, closeConn, uuids, null);
			Map<String, List<OrderItem>> subRows = new HashMap<String, List<OrderItem>>();
			for(OrderItem orderItem : orderItemList){
				List<OrderItem> subList = subRows.get(orderItem.getOrderUUID());
				if(subList == null){
					subList =new ArrayList<OrderItem>();
					subRows.put(orderItem.getOrderUUID(), subList);
				}
				subList.add(orderItem);
			}
			result.put("rows", orderList);
			result.put("subRows", subRows);
		}else{
			result.put("rows", new ArrayList<Order>());
			result.put("subRows", new HashMap<String, List<OrderItem>>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 分页查询订单项信息
	 */
	public String findOrderItemPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, OrderItem.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		orderItemService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		List<OrderItem> orderItemList = (List<OrderItem>) page.getRows();
		if(orderItemList != null){
			result.put("rows", orderItemList);
		}else{
			result.put("rows", new ArrayList<OrderItem>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 根据条件查询订单信息
	 */
	public String findOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Order.class);
		HashMap<String, Object> result = new HashMap<String, Object>();
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		if(order != null){
			findMap.clear();
			findMap.put("orderUUID", order.getOrderUUID());
			List<OrderItem> paramList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
			result.put("status", "1");
			result.put("msg", "查询到订单信息");
			result.put("order", order);
			result.put("paramList", paramList);
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "未查询到订单信息");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 评论图片上传
	 */
	public String fileupload(String sourceId, boolean closeConn, HttpServletRequest request) throws Exception {
		String imgPath = ReadProperties.getValue("commentImgPath");//读取商品图片保存路径
		int imgSize = Integer.valueOf(ReadProperties.getValue("commentImgSize"));//读取商品图片大小限制
		String tmpPath = ReadProperties.getValue("uploadtmp");//临时文件保存的路径
		return KindEditorUtil.fileUpload(request, imgPath, imgSize, null, 0, tmpPath);
	}
	/**
	 * 新增评论
	 */
	public String addComment(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证会员是否登录
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		//订单验证
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		if(order == null){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息！");
			return JsonUtil.ObjToJson(result);
		}
		if(!"3".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能进行评论！");
			return JsonUtil.ObjToJson(result);
		}
		String paramUUID = request.getParameter("paramUUID");
		findMap.put("paramUUID", paramUUID);
		OrderItem orderItem = orderItemService.findByMap(sourceId, closeConn, findMap);
		if(orderItem == null){
			result.put("status", "-4");
			result.put("msg", "评论的商品不在订单内！");
			return JsonUtil.ObjToJson(result);
		}
		
		//生成评论
		String payerUUID = vip.getVipUUID();
		String payerName = vip.getNickName();
		String payerLogo = vip.getAvatarUrl();
		if("3".equals(order.getOrderType())){
			payerUUID = request.getParameter("entityUUID");
			payerName = request.getParameter("entityName");
			payerLogo = request.getParameter("entityLogo");
		}
		String itemUUID = orderItem.getItemUUID();
		ItemComment comment = (ItemComment) ReqToEntityUtil.reqToEntity(request, ItemComment.class);
		comment.setCommentUUID(UUID.randomUUID().toString().replace("-", ""));
		comment.setEntityUUID(order.getEntityUUID());
		comment.setEntityName(order.getEntityName());
		comment.setEntityLogo(order.getEntityLogo());
		comment.setItemUUID(itemUUID);
		comment.setTitle(orderItem.getTitle());
		comment.setPayerUUID(payerUUID);
		comment.setPayerName(payerName);
		comment.setPayerLogo(payerLogo);
		commentService.saveOrUpdate(sourceId, closeConn, comment);
		
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("orderStatus", "4");
		orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		
		findMap.remove("paramUUID");
		findMap.put("orderStatus", "3");
		if(orderItemService.getTotal(sourceId, closeConn, findMap) == 0){
			findMap.remove("orderStatus");
			paramsMap.put("endTime", DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
			result.put("orderStatus", "4");
		}
		
		Lock lock = jedisClient.getItemLock(itemUUID);
		lock.lock();
		try {
			String score = commentService.getItemScore(sourceId, closeConn, itemUUID);
			findMap.clear();
			findMap.put("itemUUID", itemUUID);
			paramsMap.clear();
			paramsMap.put("score", score);
			itemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		} finally {
			lock.unlock();
		}
		result.put("status", "1");
		result.put("msg", "添加评论成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 删除评论
	 */
	public String delComment(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String commentUUID = request.getParameter("commentUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("commentUUID", commentUUID);
		ItemComment comment = commentService.findByMap(sourceId, closeConn, findMap);
		
		HashMap<String, String> result = new HashMap<>();
		if(comment == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到评论信息");
			return JsonUtil.ObjToJson(result);
		}
		
		if(commentService.deleteByMap(sourceId, closeConn, findMap)){
			String itemUUID = comment.getItemUUID();
			Lock lock = jedisClient.getItemLock(itemUUID);
			lock.lock();
			try {
				String score = commentService.getItemScore(sourceId, closeConn, itemUUID);
				findMap.clear();
				findMap.put("itemUUID", itemUUID);
				Map<String, String> paramsMap = new HashMap<>();
				paramsMap.put("score", score);
				itemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
			} finally {
				lock.unlock();
			}
			result.put("status", "1");
			result.put("msg", "删除成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "删除失败");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 修改评论
	 */
	public String upComment(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String commentUUID = request.getParameter("commentUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("commentUUID", commentUUID);
		ItemComment comment = commentService.findByMap(sourceId, closeConn, findMap);
		
		HashMap<String, String> result = new HashMap<>();
		if(comment == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到评论信息");
			return JsonUtil.ObjToJson(result);
		}
		
		String status = request.getParameter("status");
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("status", status);
		if(commentService.updateByMap(sourceId, closeConn, paramsMap , findMap)){
			String itemUUID = comment.getItemUUID();
			Lock lock = jedisClient.getItemLock(itemUUID);
			lock.lock();
			try {
				String score = commentService.getItemScore(sourceId, closeConn, itemUUID);
				findMap.clear();
				findMap.put("itemUUID", itemUUID);
				paramsMap.clear();
				paramsMap.put("score", score);
				itemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
			} finally {
				lock.unlock();
			}
			result.put("status", "1");
			result.put("msg", "修改成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "修改失败");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 分页查询评论信息
	 */
	public String findCommentPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, ItemComment.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "createTime desc";
		commentService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		List<Order> commentList = (List<Order>) page.getRows();
		if(commentList != null){
			result.put("rows", commentList);
		}else{
			result.put("rows", new ArrayList<ItemComment>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * pc端分页查询订单信息
	 */
	public String findOrderPageByPc(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Order.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "createTime desc,updateTime desc";
		orderService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		List<Order> orderList = (List<Order>) page.getRows();
		if(orderList != null){
			result.put("rows", orderList);
		}else{
			result.put("rows", new ArrayList<Order>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 卖家接单
	 */
	public String receive(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		Map<String, String> result = new HashMap<String, String>();
		if(order == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"1".equals(order.getOrderStatus())){
			result.put("status", "-2");
			result.put("msg", "订单状态异常，不能接单");
			return JsonUtil.ObjToJson(result);
		}
		
		String contactName = request.getParameter("name");
		String contactMobile = request.getParameter("mobile");
		String pickAddr = request.getParameter("address");
		if(RegExpUtil.isNullOrEmpty(contactName) || RegExpUtil.isNullOrEmpty(contactMobile) || RegExpUtil.isNullOrEmpty(pickAddr)){
			result.put("status", "-3");
			result.put("msg", "没有获取到取货信息");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("orderStatus", "2");
		orderItemService.updateByMap(sourceId, closeConn, result, findMap);
		
		result.put("contactName", contactName);
		result.put("contactMobile", contactMobile);
		result.put("pickAddr", pickAddr);
		result.put("receiveTime", DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		if(orderService.updateByMap(sourceId, closeConn, result, findMap)){
			result.put("status", "1");
			result.put("msg", "接单成功");
			return JsonUtil.ObjToJson(result);
		}
		result.clear();
		result.put("status", "-4");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 驳回退款
	 */
	public String rejectRefund(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		if(order == null){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"-3".equals(order.getOrderStatus())){
			result.put("status", "-3");
			result.put("msg", "订单状态异常，不能驳回退款");
			return JsonUtil.ObjToJson(result);
		}
		
		List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderStatus(order.getOriginStatus());
		}
		orderItemService.batchUpByField(sourceId, closeConn, orderItemList, "id");
		
		order.setOrderStatus(order.getOriginStatus());
		String rejectMsg = request.getParameter("rejectMsg");
		order.setRejectMsg(rejectMsg);
		orderService.saveOrUpdate(sourceId, closeConn, order);
		
		result.put("status", "1");
    	result.put("msg", "驳回退款成功");
    	return JsonUtil.ObjToJson(result);
	}
	/**
	 * 驳回退货
	 */
	public String rejectReturn(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		if(order == null){
			result.put("status", "-2");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		
		List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
		List<String> uuids = new ArrayList<>();
		Map<String, Integer> refundMap = new HashMap<>();
		for (OrderItem orderItem : orderItemList) {
			orderItem.setOrderStatus("-8");
			
			String paramUUID = orderItem.getParamUUID();
			uuids.add(paramUUID);
			refundMap.put(paramUUID, orderItem.getCount());
		}
		
		String rejectMsg = request.getParameter("rejectMsg");
		findMap.put("orderUUID", order.getRefundNo());
		Order originOrder = orderService.findByMap(sourceId, closeConn, findMap);
		if("-9".equals(originOrder.getOrderStatus())){
			originOrder.setOrderStatus(originOrder.getOriginStatus());
			originOrder.setRejectMsg(rejectMsg);
			originOrder.setIsDel("0");
			originOrder.setIsShow("1");
			orderService.saveOrUpdate(sourceId, closeConn, originOrder);
		}
		List<OrderItem> refundList = orderItemService.findRefundList(sourceId, closeConn, uuids, order.getRefundNo());
		for(OrderItem refund : refundList){
			refund.setOrderStatus(originOrder.getOrderStatus());
			refund.setIsDel("0");
			refund.setIsShow("1");
			refund.setAllowRefund(refund.getAllowRefund() + refundMap.get(refund.getParamUUID()));
		}
		
		refundList.addAll(orderItemList);
		orderItemService.batchUpByField(sourceId, closeConn, refundList, "id");
		
		order.setRejectMsg(rejectMsg);
		order.setOrderStatus("-8");
		order.setCloseTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		orderService.saveOrUpdate(sourceId, closeConn, order);
		
		result.put("status", "1");
    	result.put("msg", "驳回退货成功");
    	return JsonUtil.ObjToJson(result);
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
	
}