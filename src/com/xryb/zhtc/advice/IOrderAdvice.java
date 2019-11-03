package com.xryb.zhtc.advice;

import java.util.List;

import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;

/**
 * 订单通知接口
 * @author wf
 */
public interface IOrderAdvice {
	/**
	 * 通知订单生成
	 */
	boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList) throws Exception;
	/**
	 * 通知订单支付成功
	 */
	boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList) throws Exception;
	/**
	 * 通知订单取消
	 */
	boolean abolishAdvice(String sourceId, boolean closeConn, Order order) throws Exception;
	/**
	 * 通知订单超时
	 */
	boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) throws Exception;
	/**
	 * 通知订单删除
	 */
	boolean delAdvice(String sourceId, boolean closeConn, Order order) throws Exception;

}