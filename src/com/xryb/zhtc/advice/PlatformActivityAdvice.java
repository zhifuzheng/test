package com.xryb.zhtc.advice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mysql.jdbc.StringUtils;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.PlatformActiveGood;
import com.xryb.zhtc.entity.PlatformActivityOrder;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;

import spark.annotation.Auto;

public class PlatformActivityAdvice implements IOrderAdvice {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	@Auto(name=VipServiceImpl.class)
	private IVipService vipService;

	/**
	 * 通知订单生成
	 * itemUUID:entityUUID,//领取店铺uuid
	 * paramUUID:activityUUID//活动uuid
	 */
	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList) throws Exception {
		System.out.println("准备生成官方活动商品订单");
		
		String vipUUID = order.getPayerUUID();
		Map<String, String> findMap = new HashMap<>();
		findMap.put("vipUUID", vipUUID);
		VipInfo vip = vipService.findByMap(sourceId, closeConn, findMap);
		vip = jedisClient.getVipInfo(sourceId, closeConn, vip.getOpenId());
		//判断用户是否能参与该活动
		String vipName =vip.getVipName();
		String address = vip.getAddress();
		String birthday = vip.getBirthday();
		String phone = vip.getVipMobile();
		if(StringUtils.isNullOrEmpty(vipName) || StringUtils.isNullOrEmpty(address) || 
				StringUtils.isNullOrEmpty(birthday) || StringUtils.isNullOrEmpty(phone)) {
			//资料未完善
			return false;
		}
		
		//判断用户是否还可参与该活动
		for (OrderItem orderItem : itemList) {
			String activityUUID = orderItem.getParamUUID();
			//获取活动可参与次数
			PlatformActiveGood activeGood = integralService.findActivityInfo(sourceId, closeConn, activityUUID);
			Integer joinNumber = activeGood.getJoinLimitTime();//活动可参与次数
			//获取用户已参与次数
			Map<String, Object> time = integralService.getUserJoinTime(sourceId, closeConn, activityUUID, vipUUID);
			Integer joinTime = Integer.parseInt(time.get("joinTime").toString());
			if(joinTime >= joinNumber) {//超过参与限制
				return false;
			}
			
			if(activeGood.getStock() == 0) {//库存不足
				return false;
			}
		}
		return true;
	}

	/**
	 * 订单支付成功
	 */
	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList)throws Exception {
		System.out.println("订单已支付完成，准备生成商品领取订单");
		for (Order order : orderList) {
			String vipUUID = order.getPayerUUID();
			Map<String, String> findMap = new HashMap<>();
			findMap.put("vipUUID", vipUUID);
			VipInfo vip = vipService.findByMap(sourceId, closeConn, findMap);
			vip = jedisClient.getVipInfo(sourceId, closeConn, vip.getOpenId());

			// 2.生成类积分商城商品兑换订单
			for (OrderItem orderItem : itemList) {
				String activityUUID = orderItem.getParamUUID();
				// 1.查询活动信息
				PlatformActiveGood activeGood = integralService.findActivityInfo(sourceId, closeConn, activityUUID);
				
				//修改活动的购买量
				Integer getNumber = activeGood.getGetNumber();
				getNumber += 1;
				if(!integralService.updateActiveGetNumber(sourceId, closeConn, getNumber, activityUUID)) {
					return false;
				}

				// 2.获取领取店铺的相关信息itemUUID:entityUUID,//领取店铺uuid
				String entityUUID = orderItem.getItemUUID();
				BusinessApply entity = integralService.findBusinessInfoByUUID(sourceId, closeConn, entityUUID);

				//3.生成订单
				//1.创建订单对象
				PlatformActivityOrder activeOrder = new PlatformActivityOrder();
				// 订单UUID
				activeOrder.setOrderUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				// 订单编号设置
				Date date = new Date();
				int ran = (int) (Math.random() * 89999999 + 10000000);
				String number = date.getTime() + "" + ran;
				activeOrder.setOrderNumber(number);
				activeOrder.setActivityUUID(activityUUID);
				// 商品标题
				activeOrder.setTitle(activeGood.getTitle());
				// 商品图片
				activeOrder.setGoodImg(activeGood.getGoodImg());
				// 商品件数
				activeOrder.setTotalCount(1);
				// 这是订单价格
				activeOrder.setPrice(activeGood.getPrice());
				// 领取时间限制
				activeOrder.setGetTime(activeGood.getGetTime());
				// 领取店铺UUID
				activeOrder.setEntityUUID(entity.getBusinessUUID());
				// 领取店铺名称
				activeOrder.setEntityName(entity.getBusinessName());
				// 领取店铺图片
				activeOrder.setEntityLogo(entity.getStorefrontImg());
				// 领取店铺地址
				activeOrder.setBusinessAdd(entity.getBusinessAdd());
				activeOrder.setLongitude(entity.getLongitude());
				activeOrder.setLatitude(entity.getLatitude());
				// 领取店铺联系电话
				activeOrder.setBusinessPhone(entity.getBusinessPhone());
				// 订单状态;0-未领取，1-已领取，2-已过期
				activeOrder.setOrderStatus(0);
				activeOrder.setUserIsDel(0);// 用户：0-未删除，1-已删除
				activeOrder.setPlatformIsDel(0);// 平台：0-未删除，1-已删除
				// 买家UUID
				activeOrder.setPayerUUID(order.getPayerUUID());
				// 买家名称
				activeOrder.setPayerName(order.getPayerName());
				// 买家头像
				activeOrder.setPayerLogo(order.getPayerLogo());
				// 买家联系电话
				activeOrder.setReceiverMobile(order.getReceiverMobile());
				// 订单创建时间
				Date date1 = new Date();
				SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time1 = adf.format(date1);
				activeOrder.setCreateTime(time1);
				// 设置订单关闭/失效时间
				long timeMill = date1.getTime();
				timeMill = timeMill + activeOrder.getGetTime() * 60 * 60 * 1000;
				date1.setTime(timeMill);
				String time2 = adf.format(date1);
				activeOrder.setCloseTime(time2);
				// 创建订单
				if (!integralService.createActiveGoodOrder(sourceId, closeConn, activeOrder)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean abolishAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
