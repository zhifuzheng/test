package com.xryb.zhtc.advice;

import java.util.List;

import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;

/**
 * 充值通知实现类
 * @author wf
 */
public class DemoAdvice implements IOrderAdvice {

	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList) {
		System.out.println("充值订单生成成功了！");
		return true;
	}

	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList) {
		System.out.println("充值订单支付成功了！");
		return true;
	}

	@Override
	public boolean abolishAdvice(String sourceId, boolean closeConn, Order order) {
		System.out.println("充值订单已经取消了！");
		return true;
	}

	@Override
	public boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) {
		System.out.println("充值订单已经超时了！");
		return true;
	}

	@Override
	public boolean delAdvice(String sourceId, boolean closeConn, Order order) {
		System.out.println("充值订单已经删除了！");
		return true;
	}

}
