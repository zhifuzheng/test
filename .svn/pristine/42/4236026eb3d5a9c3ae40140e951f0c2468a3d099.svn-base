package com.xryb.zhtc.advice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;

import spark.annotation.Auto;

/**
 * 用户充值vip通知
 * @author zzf
 *
 */
public class RechargeVIPAdvice implements IOrderAdvice {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	@Auto(name=VipServiceImpl.class)
	private IVipService vipService;

	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList)
			throws Exception {
		
		return true;
	}

	/**
	 * 付款成功之后通知
	 */
	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList) throws Exception {
		for (Order order : orderList) {
			//1.判断是否是付费会员
			String vipUUID = order.getPayerUUID();
			Map<String, String> findMap = new HashMap<>();
			findMap.put("vipUUID", vipUUID);
			VipInfo vip = vipService.findByMap(sourceId, closeConn, findMap);
			//会员类型：1普通会员， 2付费会员，默认值是1
			String vipType = vip.getVipType();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String expireTime = "";
			if("1".equals(vipType)) {//普通会员
				//设置该用户为付费会员
				vip.setVipType("2");
				//设置会员过期时间
				cal.setTime(date);//起始时间
				cal.add(Calendar.YEAR, 1);
				Date newdate = cal.getTime();
				expireTime = sdf.format(newdate);
				vip.setExpireTime(expireTime);
			}else if("2".equals(vipType)) {//付费会员
				//判断该会员是否到期
				expireTime = vip.getExpireTime();
				Date expireDate = sdf.parse(expireTime);
				vip.setVipType("2");
				if(date.compareTo(expireDate) == 1) {//已过期
					//重新设置过期时间
					cal.setTime(date);//起始时间
					cal.add(Calendar.YEAR, 1);
					Date newdate = cal.getTime();
					expireTime = sdf.format(newdate);
					vip.setExpireTime(expireTime);
				}
				if(date.compareTo(expireDate) == -1) {//未过期
					//重新设置过期时间，起始时间为会员到期时间
					cal.setTime(expireDate);
					cal.add(Calendar.YEAR, 1);
					Date newdate = cal.getTime();
					expireTime = sdf.format(newdate);
					vip.setExpireTime(expireTime);
				}
			}
			
			jedisClient.setVipInfo(vip.getOpenId(), vip);
			vipService.saveOrUpdate(sourceId, closeConn, vip);
			System.out.println("充值356成为vip会员通知设置成功...");
			return true;
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
