package com.xryb.zhtc.advice;

import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.CouponOfIssuer;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;

import spark.annotation.Auto;

/**
 * 优惠劵积分通知实现类
 * @author zzf
 */
public class CouponAdvice implements IOrderAdvice {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	/**
	 * 订单生产成功通知
	 */
	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList) throws Exception {
		System.out.println("订单生成前优惠劵advice调用......");
		//计算订单优惠金额以及对应商品优惠金额
		String couponUUIDS = order.getCouponUUID();//多张优惠劵，以逗号分隔
		if(StringUtils.isNullOrEmpty(couponUUIDS)) {//该订单未使用优惠劵
			return true;
		}
		
		String orderUUID = order.getOrderUUID();
		//1.修改优惠劵使用相关信息，劵状态,使用时间，订单编号，使用量等，
		if (!couponService.updateIsUse(sourceId, closeConn, couponUUIDS, orderUUID)) {
			return false;
		}
		
		//2.计算订单优惠金额
		String[] couponUUIDArr = couponUUIDS.split(",");
		
		Integer orderTotalReduceMoney = 0;//订单总优惠金额
		Integer reduceMaxPrice = 0;//折扣劵最高优惠金额
		Double money = null;//面值
		Double discount = null;//折扣
		Integer dis = 0;
		
		for (String couponUUID : couponUUIDArr) {
			//获取优惠劵相关信息
			CouponOfIssuer issuer = couponService.findCouponInfoByCouponUUID(sourceId, closeConn, couponUUID);
			//修改对应批次的使用量加1
			Integer usedNumbers = issuer.getUsedNumbers();
			usedNumbers += 1;
			if (!couponService.updateCouponUsedNumbers(sourceId, closeConn, issuer.getBatchUUID(), usedNumbers)) {
				return false;
			}
			
			Integer couponType = issuer.getCouponType();
			Integer goodsUseCondition = issuer.getGoodsUseCondition();
			String goodsUUID = issuer.getGoodsUUID();
			Integer goodsSortCondition = issuer.getGoodsSortCondition();
			String goodsCatUUID = issuer.getGoodsCatUUID();
			
			//优惠劵总优惠金额
			Integer totalReduceMoney = 0;
			if (couponType == 1) {// 代金劵
				money = Double.valueOf(issuer.getMoney()) * 100;// 面值
				totalReduceMoney = money.intValue();
			}else {
				discount = Double.valueOf(issuer.getDiscount()) * 100;// 折扣
				dis = discount.intValue();
				Double reduceMaxPrice1 = Double.parseDouble(issuer.getReduceMaxPrice())*100;//最高优惠金额 分
				reduceMaxPrice = reduceMaxPrice1.intValue();
			}

			// 判断优惠劵指定范围
			// 2.1店铺通用
			Integer payment = order.getPayment();//订单实际支付金额
			if (goodsSortCondition == 2 && goodsUseCondition == 2) {
				if (couponType == 2) {//折扣劵
					totalReduceMoney = ((100 - dis) * payment) / 100;
					if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
						totalReduceMoney = reduceMaxPrice;
					}
				}
				orderTotalReduceMoney += totalReduceMoney;
				// 每件商品商品优惠金额
				for (OrderItem item : itemList) {
					Integer price = item.getPrice();
					Integer reduceMoney = (price * totalReduceMoney / payment);// 单件商品优惠金额
					item.setReducePrice(item.getReducePrice()+reduceMoney);
					item.setPayment(item.getPayment() - reduceMoney);
				}
			}

			// 2.2指定分类可用
			if (goodsSortCondition == 1 && goodsUseCondition == 2) {
				Integer catTalPrice = 0;// 指定分类商品总价
				for (OrderItem item : itemList) {
					String itemUUID = item.getItemUUID();
					String paramUUID = item.getParamUUID();//套餐uuid
					ItemParam itemInfo = couponService.getGoodInfo(sourceId, closeConn, itemUUID,paramUUID);
					if (goodsCatUUID.equals(itemInfo.getCatUUID())) {// 匹配到对应商品
						Integer price = item.getPrice();
						Integer count = item.getCount();
						catTalPrice += (price * count);
					}
				}

				if (couponType == 2) {// 折扣劵
					totalReduceMoney = ((100 - dis) * catTalPrice) / 100;
					if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
						totalReduceMoney = reduceMaxPrice;
					}
				}
				orderTotalReduceMoney += totalReduceMoney;
				// 匹配对应的商品，计算优惠金额
				for (OrderItem item : itemList) {
					String itemUUID = item.getItemUUID();
					String paramUUID = item.getParamUUID();//套餐uuid
					ItemParam itemInfo = couponService.getGoodInfo(sourceId, closeConn, itemUUID,paramUUID);
					if (goodsCatUUID.equals(itemInfo.getCatUUID())) {// 匹配到对应商品
						Integer price = item.getPrice();
						Integer reduceMoney = (price * totalReduceMoney / catTalPrice);
						item.setReducePrice(item.getReducePrice()+reduceMoney);
						item.setPayment(item.getPayment() - reduceMoney);
					}
				}
			}

			// 2.3指定商品可用
			if (goodsUseCondition == 1) {
				for (OrderItem item : itemList) {
					String itemUUID = item.getItemUUID();
					if (itemUUID.equals(issuer.getGoodsUUID())) {// 匹配到对应商品
						Integer count = item.getCount();
						Integer price = item.getPrice();
						if (couponType == 2) {// 折扣劵
							totalReduceMoney = (count*price*(100-dis))/100;
							if(totalReduceMoney>reduceMaxPrice) {//超过最高优惠金额
								orderTotalReduceMoney += reduceMaxPrice;
								totalReduceMoney = reduceMaxPrice;
							}else {
								orderTotalReduceMoney += totalReduceMoney;
							}
						}
						item.setReducePrice(item.getReducePrice()+totalReduceMoney/count);
						item.setPayment(item.getPayment() - (totalReduceMoney/count));
					}
				}
			}
		}
		order.setReducePrice(orderTotalReduceMoney);// 订单总优惠金额
		order.setPayment(order.getPayment()-orderTotalReduceMoney);//订单实际支付金额
		return true;
	}

	/**
	 * 订单支付成功通知
	 */
	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 订单取消通知
	 */
	@Override
	public boolean abolishAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		String couponUUID = order.getCouponUUID();
		if(!StringUtils.isNullOrEmpty(couponUUID)) {
			//1.返还优惠劵给用户
			if(!couponService.returnCouponToUser(sourceId, closeConn, couponUUID)) {
				return false;
			}
			//2.修改对应批次的使用量减1
			CouponOfIssuer issuer = couponService.findCouponInfoByCouponUUID(sourceId, closeConn, couponUUID);
			Integer usedNumbers = issuer.getUsedNumbers();
			usedNumbers -= 1;
			if (!couponService.updateCouponUsedNumbers(sourceId, closeConn, issuer.getBatchUUID(), usedNumbers)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 订单超时通知
	 */
	@Override
	public boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		String couponUUID = order.getCouponUUID();
		if(!StringUtils.isNullOrEmpty(couponUUID)) {
			//1.返还优惠劵给用户
			if(!couponService.returnCouponToUser(sourceId, closeConn, couponUUID)) {
				return false;
			}
			//2.修改对应批次的使用量减1
			CouponOfIssuer issuer = couponService.findCouponInfoByCouponUUID(sourceId, closeConn, couponUUID);
			Integer usedNumbers = issuer.getUsedNumbers();
			usedNumbers -= 1;
			if (!couponService.updateCouponUsedNumbers(sourceId, closeConn, issuer.getBatchUUID(), usedNumbers)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 订单删除通知
	 */
	@Override
	public boolean delAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
