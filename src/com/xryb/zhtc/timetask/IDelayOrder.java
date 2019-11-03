package com.xryb.zhtc.timetask;

import java.util.concurrent.DelayQueue;

/**
 *  订单延时操作接口
 * @author zzf
 */
public interface IDelayOrder {
	/**
	 * 用户下单成功未支付，超时处理
	 */
	public boolean orderNotPay(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);
	
	/**
	 * 用户下单支付成功，卖家未发货超时处理
	 */
	public boolean orderNotDelivery(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);
	
	/**
	 * 卖家发货成功，买家为收货超时处理
	 */
	public boolean buyerNotReceive(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);
	
	/**
	 * 用户申请退款、退货退款，卖家未处理超时处理
	 */
	public boolean buyerSupplyRefund(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);
	
	/**
	 * 卖家同意退货退款，买家未发货超时处理
	 */
	public boolean buyerNotDelivery(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);
	
	/**
	 * 买家发货成功，卖家未收货，超时处理
	 */
	public boolean sellerNotReceive(String sourceId,boolean closeConn,DelayQueue<DelayOrder> order);

}
