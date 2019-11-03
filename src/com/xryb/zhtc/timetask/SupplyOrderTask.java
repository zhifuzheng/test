package com.xryb.zhtc.timetask;

import java.util.concurrent.DelayQueue;

import com.xryb.zhtc.entity.GxCommodity;
import com.xryb.zhtc.entity.SupplyOrder;
import com.xryb.zhtc.service.ISupplyOrderService;
import com.xryb.zhtc.service.impl.SupplyOrderServiceImpl;

import spark.annotation.Auto;

public class SupplyOrderTask implements IDelayOrder {
	@Auto(name=SupplyOrderServiceImpl.class)
	private ISupplyOrderService supplyOrderService;
	
	/**
	 * 用户下单成功未支付，超时处理
	 */
	@Override
	public boolean orderNotPay(String sourceId,boolean closeConn,DelayQueue<DelayOrder> orderQueue) {
		try {
			DelayOrder delay = orderQueue.take();
			String orderUUID = delay.getOrderUUID();
			//订单超过15分钟未支付，自动取消订单，并修改库存
			SupplyOrder order = supplyOrderService.findOrderInfoByOrderUUID(sourceId, closeConn, orderUUID);
			if("0".equals(order.getOrderStatus())) {
				order.setOrderStatus("-1");
				Integer totalCount = order.getTotalCount();
				GxCommodity good = supplyOrderService.findSupplyGoodInfo(sourceId, closeConn, order.getGoodUUID());
				Integer stock = Integer.parseInt(good.getStock());
				stock += totalCount;
				if(supplyOrderService.updateGoodStock(sourceId, closeConn, good.getCommodityUUID(), stock)) {
					//将该订单移除消息队列
					orderQueue.remove(delay);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * 用户下单支付成功，卖家未发货超时处理
	 */
	@Override
	public boolean orderNotDelivery(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order) {
		
		return false;
	}
	
	/**
	 * 卖家发货成功，买家为收货超时处理
	 */
	@Override
	public boolean buyerNotReceive(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 用户申请退款、退货退款，卖家未处理超时处理
	 */
	@Override
	public boolean buyerSupplyRefund(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 卖家同意退货退款，买家未发货超时处理
	 */
	@Override
	public boolean buyerNotDelivery(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 买家发货成功，卖家未收货，超时处理
	 */
	@Override
	public boolean sellerNotReceive(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order) {
		// TODO Auto-generated method stub
		return false;
	}

}
