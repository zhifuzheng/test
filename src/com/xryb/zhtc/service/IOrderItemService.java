package com.xryb.zhtc.service;

import java.util.List;

import com.xryb.zhtc.entity.OrderItem;
/**
 * 订单商品关联service接口
 * @author wf
 */
public interface IOrderItemService extends IBaseService<OrderItem> {
	/**
	 * 根据订单UUIDs，批量查询订单关联的商品列表
	 */
	List<OrderItem> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids, String order);
	/**
	 * 根据套餐UUIDs，批量查询订单关联的商品列表
	 */
	List<OrderItem> findRefundList(String sourceId, boolean closeConn, List<String> uuids, String orderUUID);
}
