package com.xryb.zhtc.manage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.StringUtils;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.dto.RecAddrDto;
import com.xryb.zhtc.entity.GxCommodity;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.SupplyCommodityComment;
import com.xryb.zhtc.entity.SupplyOrder;
import com.xryb.zhtc.entity.SupplyUserAddress;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.IOrderService;
import com.xryb.zhtc.service.ISupplyOrderService;
import com.xryb.zhtc.service.impl.OrderServiceImpl;
import com.xryb.zhtc.service.impl.SupplyOrderServiceImpl;
import com.xryb.zhtc.timetask.DelayOrder;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 供需管理
 * @author zzf
 *
 */
public class SupplyOrderMng {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=SupplyOrderServiceImpl.class)
	private ISupplyOrderService supplyOrderService;
	@Auto(name=OrderServiceImpl.class)
	private IOrderService orderService;
	
	public static DelayQueue<DelayOrder> DELAY_QUEUE = new DelayQueue<>();
	
	
	/**
	 * 获取用户卖出的所有订单列表
	 */
	public String getUserSellSupplyOrderList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		//1.判断用户登陆状态和获取vipUUID
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		//2.分页设置
		Page page = null;
		String pageRecord = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (pageRecord != null && !"".equals(pageRecord) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(pageRecord));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String type = request.getParameter("type");
		//3.查询用户卖出的订单信息列表
		List<SupplyOrder> orderList = supplyOrderService.findUserSellSupplyOrderList(sourceId, closeConn, page, vipUUID,type);
		
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
	 * 我买到的全部订单
	 */
	public String getUserBuySupplyOrderList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		//1.判断用户登陆状态和获取vipUUID
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "请登陆后查看");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		//2.分页设置
		Page page = null;
		String pageRecord = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (pageRecord != null && !"".equals(pageRecord) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(pageRecord));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String type = request.getParameter("type");
		//3.查询用户买到的订单信息列表
		List<SupplyOrder> orderList = supplyOrderService.findUserBuySupplyOrderList(sourceId, closeConn, page, vipUUID,type);
		
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
	 * 新增供需管理订单
	 * 
	 * 订单协议
	 * 
	 * 订单通知
	 * advice					通知实现类的编码
	 * 注意：参数名必须为advice，参数值为实现类的编码，需要在包com.xryb.zhtc.advice中创建一个类，
	 * 并实现IOrderAdvice接口，在advice.properties配置文件中配置，编码=实现类的类名，
	 * 订单在生成前会执行通知方法，若通知方法执行时抛出异常或返回false，则不会生成订单，
	 * @throws Exception 
	 * 
	 */
	public String createSupplyOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		//1.买家信息
		String payerUUID = vip.getVipUUID();
		String payerName = vip.getVipName();
		String payerLogo = vip.getAvatarUrl();
		String payerMobile = vip.getVipMobile();
		String detailAddr = vip.getAddress();
		if (RegExpUtil.isNullOrEmpty(payerName) || RegExpUtil.isNullOrEmpty(payerMobile)
				|| RegExpUtil.isNullOrEmpty(detailAddr)) {
			result.put("status", "-2");
			result.put("msg", "会员信息不全，请完善会员信息后再下单");
			return JsonUtil.ObjToJson(result);
		}
		
		//2.收货地址校验
		String addrJson = request.getParameter("deliveryAddr");
		RecAddrDto addr = null;
		if (!RegExpUtil.isNullOrEmpty(addrJson)) {
			// 校验收货地址数据格式
			try {
				addr = (RecAddrDto) JsonUtil.JsonToObj(addrJson, new TypeToken<RecAddrDto>() {
				}.getType());
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "-3");
				result.put("msg", "收货地址数据格式错误，服务器无法解析，请联系管理员！");
				return JsonUtil.ObjToJson(result);
			}
		}
		// 校验收货地址完整性
		if (addr != null) {
			if (RegExpUtil.isNullOrEmpty(addr.getReceiverName()) || RegExpUtil.isNullOrEmpty(addr.getReceiverMobile())
					|| RegExpUtil.isNullOrEmpty(addr.getProvCityDist())
					|| RegExpUtil.isNullOrEmpty(addr.getDetailAddr())) {
				result.put("status", "-4");
				result.put("msg", "收货地址信息不完整！");
				return JsonUtil.ObjToJson(result);
			}
			if (!RegExpUtil.isNullOrEmpty(addr.getPostCode()) && !RegExpUtil.checkPostcode(addr.getPostCode())) {
				result.put("status", "-5");
				result.put("msg", "邮政编码错误！");
				return JsonUtil.ObjToJson(result);
			}
		}
		String receiverName = request.getParameter("receiverName");
		String receiverMobile = request.getParameter("receiverMobile");
		String provCityDist = request.getParameter("provCityDist");
		String receiverDetailAddr = request.getParameter("receiverDetailAddr");
		String advice = request.getParameter("advice");
		
		String buyerMsg = request.getParameter("buyerMsg");// 获取买家留言
		// 获取商品数据
		String totalCount = request.getParameter("totalCount");
		String goodUUID = request.getParameter("goodUUID");
		
		Lock lock = jedisClient.getItemLock(goodUUID);
		lock.lock();
		
		GxCommodity goodInfo = supplyOrderService.findSupplyGoodInfo(sourceId, closeConn, goodUUID);
		
		//判断商品是否下架
		String state = goodInfo.getState();//商品状态（0：上架，1：下架）
		String stop = goodInfo.getStop();//商品管理（0：启用，1：停用）
		String stock = goodInfo.getStock();//商品库存
		
		if("1".equals(state)) {
			result.put("status", "-6");
			result.put("msg", "该商品已下架");
			lock.unlock();
			return JsonUtil.ObjToJson(result);
		}
		
		if("1".equals(stop)) {
			result.put("status", "-7");
			result.put("msg", "该商品已停用");
			lock.unlock();
			return JsonUtil.ObjToJson(result);
		}
		Integer stocks = Integer.parseInt(stock);
		Integer counts = Integer.parseInt(totalCount);
		
		if(stocks < counts) {//库存不足
			result.put("status", "-8");
			result.put("msg", "商品库存不足");
			lock.unlock();
			return JsonUtil.ObjToJson(result);
		}
		
		//组装订单数据
		SupplyOrder order = new SupplyOrder();
		String orderUUID = UUID.randomUUID().toString().replaceAll("-", "");
		order.setOrderUUID(orderUUID);
		order.setAdvice(request.getParameter("advice"));
		order.setTitle("供需社区商品交易订单");
		order.setSellerUUID(goodInfo.getVipUUID());
		order.setSellerName(goodInfo.getVipName());
		order.setSellerLogo(goodInfo.getVipLogin());
		order.setContactMobile(goodInfo.getPhone());
		
		order.setGoodUUID(goodUUID);
		order.setGoodTitle(goodInfo.getCommodityTitle());
		order.setGoodDetails(goodInfo.getDetails());
		order.setGoodImg(request.getParameter("goodImg"));
		order.setGoodReleaseAddr(goodInfo.getAddress());
		
		Double priced = Double.valueOf(goodInfo.getPrice())*100;//分
		Integer price = priced.intValue();
		order.setPrice(price);
		order.setTotalCount(counts);
		order.setTotalPrice(price*counts);
		
		//邮费计算
		String post = goodInfo.getPost();//是否包邮（0：包邮，1：不包邮）
		if("0".equals(post)) {
			order.setDeliveryMoney(0);
			order.setPayment(price*counts);
		}else {
			Double postages = Double.valueOf(goodInfo.getPostage())*100;//分
			Integer postage = postages.intValue();
			order.setDeliveryMoney(postage*counts);
			order.setPayment(price*counts+postage*counts);
		}
		
		order.setPayChannel(request.getParameter("payChannel"));//支付方式
		//买家信息
		order.setPayerUUID(payerUUID);
		order.setPayerName(payerName);
		order.setPayerLogo(payerLogo);
		order.setPayerMobile(payerMobile);
		order.setDetailAddr(detailAddr);
		order.setBuyerMsg(buyerMsg);
		//收货地址
		order.setDeliveryType(request.getParameter("deliveryType"));
		order.setReceiverName(receiverName);
		order.setReceiverMobile(receiverMobile);
		order.setProvCityDist(provCityDist);
		order.setReceiverDetailAddr(receiverDetailAddr);
		
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-9");
			result.put("msg", "系统繁忙，请稍后重试");
			lock.unlock();
			return JsonUtil.ObjToJson(result);
		}
		
		//减库存
		stocks -= counts;
		if(!supplyOrderService.updateGoodStock(sourceId, closeConn, goodUUID, stocks)) {
			throw new Exception("系统繁忙，请稍后重试");
		}
		
		// 生成新的微信可直接调用的order，类型为购物orderType-2
		Order newOrder = new Order();
		newOrder.setOrderUUID(order.getOrderUUID());
		newOrder.setAdvice(advice);
		newOrder.setTitle("供需社区商品交易订单");
		newOrder.setEntityUUID(order.getSellerUUID());
		newOrder.setEntityName(order.getSellerName());
		newOrder.setEntityLogo(order.getSellerLogo());
		newOrder.setContactName(order.getSellerName());
		newOrder.setContactMobile(order.getContactMobile());
		newOrder.setTotalCount(order.getTotalCount());
		newOrder.setTotalPrice(order.getTotalPrice());
		newOrder.setPayment(order.getPayment());
		newOrder.setOrderType("2");
		newOrder.setPayerUUID(order.getPayerUUID());
		newOrder.setPayerName(order.getPayerName());
		newOrder.setPayerLogo(order.getPayerLogo());
		newOrder.setReceiverMobile(order.getReceiverMobile());
		newOrder.setDetailAddr(order.getDetailAddr());
		newOrder.setPersonMsg(order.getBuyerMsg());
		newOrder.setCreateTime(order.getCreateTime());
		//设置该中间订单为删除不显示
		newOrder.setIsDel("1");
		newOrder.setIsShow("0");
		if(!orderService.saveOrUpdate(sourceId, closeConn, newOrder)) {
			throw new Exception("系统繁忙，请稍后重试");
		}
		
		result.put("status", "1");
		result.put("msg", "订单生成成功");
		result.put("orderUUID", orderUUID);
		//放入订单超时列队中
		DELAY_QUEUE.add(new DelayOrder(orderUUID, DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), 1*60));
		lock.unlock();
		return JsonUtil.ObjToJson(result);
	}
	


	/**
	 * 取消订单
	 * @throws Exception 
	 */
	public String abolishSupplyOrder(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
//		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
//		String orderStatus = order.getOrderStatus();
		
		//执行取消订单
		if(!supplyOrderService.abolishOrder(sourceId, closeConn, orderUUID)) {
			result.put("status", "-2");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		//库存修改
		String goodUUID = request.getParameter("goodUUID");
		String totalCount = request.getParameter("totalCount");
		GxCommodity good = supplyOrderService.findSupplyGoodInfo(sourceId, closeConn, goodUUID);
		String stock = good.getStock();
		Integer stocks = Integer.parseInt(stock)+Integer.parseInt(totalCount);
		if(!supplyOrderService.updateGoodStock(sourceId, closeConn, goodUUID, stocks)) {
			throw new Exception("库存修改失败");
		}
		
		result.put("status", "1");
		result.put("msg", "订单取消成功");
		return JsonUtil.ObjToJson(result);	
	}


	/**
	 * 用户提交退款退货申请
	 */
	public String refundMoneyOrGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//1.登陆验证
		Map<String,Object> result = new HashMap<>();
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if(vip == null) {
			result.put("status", "-1");
			result.put("msg", "未登陆");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		//2.获取订单信息
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		String refundType = request.getParameter("refundType");//退款服务类型，1-仅退款(未收到货)，2-退货退款(已收到货)
		String deliveryStatus = request.getParameter("deliveryStatus");
		String refundMoney = request.getParameter("refundMoney");
		String refundReason = request.getParameter("refundReason");
		String refundDetail = request.getParameter("refundDetail");
		String refundDetailImg = request.getParameter("refundDetailImg");
		
		String refundTime = order.getRefundTime();
		if(StringUtils.isNullOrEmpty(refundTime)) {
			order.setOriginStatus(order.getOrderStatus());//若是对退款订单进行修改，则不改变该值，订单申请退款退货前的订单状态
		}
		
		if("1".equals(refundType)) {//退款
			order.setOrderStatus("-3");//订单状态待退款
		}else {//退货退款
			order.setOrderStatus("-6");//订单状态待退货
		}
		order.setRefundType(refundType);
		order.setDeliveryStatus(deliveryStatus);
		order.setRefundMoney(Integer.valueOf(refundMoney));
		order.setRefundReason(refundReason);
		order.setRefundDetail(refundDetail);
		order.setRefundDetailImg(refundDetailImg);
		order.setRefundTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "申请提交成功");
		result.put("order", order);
		return JsonUtil.ObjToJson(result);
		
	}


	/**
	 * 撤销退款退货申请
	 */
	public String cancelApply(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		//执行修改
		if(!supplyOrderService.cancelApply(sourceId, closeConn,order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "撤销成功");
		return JsonUtil.ObjToJson(result);
		
	}


	/**
	 * 确认收货
	 */
	public String confirmReceipt(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		//确认收货
		if(!supplyOrderService.confirmReceipt(sourceId, closeConn, orderUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "确认收货成功");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 确认收货后，订单评价
	 * @throws Exception 
	 */
	public String SupplyOrderComment(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String payerUUID = vip.getVipUUID();
		String payerName = vip.getVipName();
		String payerLogo = vip.getAvatarUrl();
		String payerMobile = vip.getVipMobile();
		
		SupplyCommodityComment comment = (SupplyCommodityComment) ReqToEntityUtil.reqToEntity(request, SupplyCommodityComment.class);
		String imgList = request.getParameter("imgList");
		comment.setImgList(imgList);
		comment.setPayerUUID(payerUUID);
		comment.setPayerName(payerName);
		comment.setPayerLogo(payerLogo);
		comment.setPayerMobile(payerMobile);
		comment.setCommentUUID(UUID.randomUUID().toString().replaceAll("-", ""));
		comment.setCreateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		//提交评论
		if(!supplyOrderService.submitComment(sourceId, closeConn, comment)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		//评价成功后，修改订单状态
		if(!supplyOrderService.updateSupplyOrderSate(sourceId, closeConn, comment.getOrderUUID())) {
			throw new Exception("订单状态修改失败");
		}
		
		result.put("status", "1");
		result.put("msg", "评价成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 卖家同意退款
	 */
	public String agreeRefund(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		String orderStatus = order.getOrderStatus();
		if("-3".equals(orderStatus)) {//买家仅申请退款
			order.setOrderStatus("-4");
		}
		if("-6".equals(orderStatus)) {//买家申请退款退货
			order.setOrderStatus("-7");
		}
		
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "同意退款成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 获取用户收货地址列表
	 */
	public String getUserAddress(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		// 2.分页设置
		Page page = null;
		String pageRecord = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (pageRecord != null && !"".equals(pageRecord) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(pageRecord));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		List<SupplyUserAddress> address = supplyOrderService.findSupplyUserAddressList(sourceId, closeConn, page, vipUUID);

		if (page == null) {
			result.put("address", address);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (address == null) {
				result.put("address", 0);
			} else {
				result.put("address", address);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 新增或编辑收货地址
	 */
	public String saveOrUpdateAddress(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		SupplyUserAddress address = (SupplyUserAddress) ReqToEntityUtil.reqToEntity(request, SupplyUserAddress.class);
		String id = request.getParameter("id");
		
		String isDefault = address.getIsDefault();
		//判断是否设置为默认收货地址
		if("1".equals(isDefault)) {//设为默认收货地址
			//1.判断用户是否有其他可用收货地址
			Map<String,Object> addr = supplyOrderService.findSupplyUserAddrNumbers(sourceId, closeConn, vipUUID);
			Set<Entry<String,Object>> addrs = addr.entrySet();
			Long num = null;
			for (Entry<String,Object> entry : addrs) {
				num = (Long) entry.getValue();
			}
			if(num > 0) {
				//2.先将用户所有可用的收货地址全部设置为非默认收货地址
				if(!supplyOrderService.updateAllAddressNotDefault(sourceId, closeConn, vipUUID)) {
					result.put("status", "-1");
					result.put("msg", "系统繁忙，请稍后重试");
					return JsonUtil.ObjToJson(result);
				}
			}
		}
		
		if(StringUtils.isNullOrEmpty(id)) {//新增
			address.setAddrUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			address.setVipUUID(vipUUID);
		}
		
		//新增或编辑收货地址
		if(!supplyOrderService.saveOrUpdateAddr(sourceId, closeConn, address)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "操作成功");
		result.put("address", address);
		return JsonUtil.ObjToJson(result);
	}

	
	/**
	 * 获取用户默认收货地址信息
	 */
	public String getUserDefaultAddress(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		SupplyUserAddress address = supplyOrderService.findUserDefaultAddress(sourceId, closeConn, vipUUID);
		if(address == null) {
			result.put("status", "-2");
			result.put("msg", "无默认收货地址");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "默认收货地址获取成功");
		result.put("address", address);
		return JsonUtil.ObjToJson(result);
	}

	
	/**
	 * 删除收货地址
	 */
	public String deleteAddress(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String addrUUID = request.getParameter("addrUUID");
		//删除订单
		if(!supplyOrderService.deleteSupplyAddress(sourceId, closeConn, addrUUID)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "删除成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	/**
	 * 获取商品评价（即订单完成后，用户的评价数据）
	 */
	public String getCommodityComments(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 2.分页设置
		Page page = null;
		String pageRecord = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if (pageRecord != null && !"".equals(pageRecord) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(pageRecord));
			page.setPage(Integer.parseInt(pageNumber));
		}
		
		String commodityUUID = request.getParameter("commodityUUID");

		List<SupplyCommodityComment> comments = supplyOrderService.findSupplyOrderComment(sourceId, closeConn, page, commodityUUID);
				

		if (page == null) {
			result.put("comments", comments);
			return JsonUtil.ObjToJson(result);
		} else {
			result.put("total", page.getTotalRecord());
			if (comments == null) {
				result.put("comments", 0);
			} else {
				result.put("comments", comments);
			}
			result.put("page", page);
		}
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 商家确认发货
	 */
	public String confirmDelivery(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String type = request.getParameter("type");//
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		if("1".equals(type)) {//配送方式：1-快递物流，2-面对面交易，默认值为1
			order.setDeliveryNumber(request.getParameter("deliveryNumber"));
			order.setDeliveryCompany(request.getParameter("deliveryCompany"));
			order.setOrderStatus("2");
			order.setDeliveryType("1");
		}
		
		if ("2".equals(type)) {
			order.setDeliveryType("2");
			order.setOrderStatus("2");
		}
		
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("order",order);
		result.put("msg", "确认发货成功");
		return JsonUtil.ObjToJson(result);
	}

	/**
	 * 卖家拒绝买家退款退货申请
	 */
	public String rejectRefund(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		order.setRejectMsg(request.getParameter("rejectMsg"));
		order.setRejectMsgDetail(request.getParameter("content"));
		order.setRejectImgList(request.getParameter("rejectImgList"));
		order.setRejecTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String orderStatus = order.getOrderStatus();
		if("-3".equals(orderStatus)) {//拒绝退款申请
			order.setOrderStatus("-5");
		}
		if("-6".equals(orderStatus)) {//拒绝退货退款申请
			order.setOrderStatus("-8");
		}
		if("-9".equals(orderStatus)) {//买家已发货，拒绝退款申请
			order.setOrderStatus("-5");
		}
		
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "申请驳回成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 卖家同意退货地址填写
	 */
	public String confirmSendRefundAddr(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		
		order.setRefundName(request.getParameter("refundName"));
		order.setRefundMobile(request.getParameter("refundMobile"));
		order.setRefundProvCityDist(request.getParameter("refundProvCityDist"));
		order.setRefundDetailAddr(request.getParameter("refundDetailAddr"));
		order.setOrderStatus("-7");
		order.setAgreeRefundGTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		//卖家同意退货后，待买家发货并且收到货以后，才可以退款给买家
		if(!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}
		
		result.put("status", "1");
		result.put("msg", "退货地址发送成功");
		return JsonUtil.ObjToJson(result);
	}


	/**
	 * 买家退货发货
	 */
	public String buyerSendGood(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();//封装返回结果
		// 验证用户是否登录，并获取用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		if (vip == null) {
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String orderUUID = request.getParameter("orderUUID");
		SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
		order.setRefundDeliveryNumber(request.getParameter("refundDeliveryNumber"));
		order.setRefundDeliveryCompany(request.getParameter("refundDeliveryCompany"));
		order.setSendGoodImg(request.getParameter("sendGoodImg"));
		order.setOrderStatus("-9");
		order.setBuyerSendTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		// 卖家同意退货后，待买家发货并且收到货以后，才可以退款给买家
		if (!supplyOrderService.saveOrUpdateOrder(sourceId, closeConn, order)) {
			result.put("status", "-1");
			result.put("msg", "系统繁忙，请稍后重试");
			return JsonUtil.ObjToJson(result);
		}

		result.put("status", "1");
		result.put("msg", "发货成功");
		return JsonUtil.ObjToJson(result);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ********************************************************************* PC端 ********************************************************************* //
	

}
