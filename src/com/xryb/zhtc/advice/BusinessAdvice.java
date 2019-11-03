package com.xryb.zhtc.advice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.service.IBusinessService;
import com.xryb.zhtc.service.impl.BusinessServiceImpl;

import spark.annotation.Auto;

public class BusinessAdvice implements IOrderAdvice {
	@Auto(name = BusinessServiceImpl.class)
	private IBusinessService iBusinessService;

	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList)
			throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	//订单支付成功
	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList)
			throws Exception {
		OrderItem orderItem = new OrderItem();
		for(int i = 0; i < itemList.size(); i++) {
			orderItem = itemList.get(i);
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if("0".equals(orderItem.getParamUUID())) {//一个星期
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+7);
		}else if("1".equals(orderItem.getParamUUID())) {//半年
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+6);
		}else if("2".equals(orderItem.getParamUUID())) {//一年
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
		}
		Date date = calendar.getTime();
		String dueTime = format.format(date).toString();
		
		BusinessApply businessApplyId = new BusinessApply();
		businessApplyId = iBusinessService.applyId(sourceId, closeConn, orderItem.getItemUUID());
		
		BusinessApply businessApply = new BusinessApply();
		businessApply.setId(businessApplyId.getId());
		businessApply.setBusinessUUID(businessApplyId.getBusinessUUID());
		businessApply.setDueTime(dueTime);
		businessApply.setShopState("1");
		iBusinessService.businessSave(sourceId, closeConn, businessApply);
		return true;
	}

	@Override
	public boolean abolishAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean delAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
