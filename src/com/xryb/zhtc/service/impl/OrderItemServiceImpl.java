package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.service.IOrderItemService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 订单商品关联service
 * @author wf
 */
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem> implements IOrderItemService {

	@Override
	public List<OrderItem> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String orderUUID = null;
		String price = null;
		String firstUUID = null;
		String secondUUID = null;
		String isPay = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("orderUUID".equals(entry.getKey())){
				orderUUID = entry.getValue();
			}
			if("price".equals(entry.getKey())){
				price = entry.getValue();
			}
			if("firstUUID".equals(entry.getKey())){
				firstUUID = entry.getValue();
			}
			if("secondUUID".equals(entry.getKey())){
				secondUUID = entry.getValue();
			}
			if("isPay".equals(entry.getKey())){
				isPay = entry.getValue();
			}
		}
		if(orderUUID != null){
			findMap.remove("orderUUID");
		}
		if(price != null){
			findMap.remove("price");
		}
		if(firstUUID != null){
			findMap.remove("firstUUID");
		}
		if(secondUUID != null){
			findMap.remove("secondUUID");
		}
		if(isPay != null){
			findMap.remove("isPay");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(orderUUID != null){
			select.append(" and orderUUID = '").append(orderUUID).append("'");
		}
		if(price != null){
			select.append(" and price = '").append(price).append("'");
		}
		if(firstUUID != null){
			select.append(" and firstUUID = '").append(firstUUID).append("'");
			select.append(" and firstIncome > 0");
		}
		if(secondUUID != null){
			select.append(" and secondUUID = '").append(secondUUID).append("'");
			select.append(" and secondIncome > 0");
		}
		if(isPay != null){
			select.append(" and isPay = '").append(isPay).append("'");
		}
		int firstAndIndex = select.indexOf("and",len);
		if(firstAndIndex == len+1){
			select.delete(firstAndIndex, firstAndIndex+3);
		}
		StringBuffer selectTotal = new StringBuffer(select.toString()).replace(select.indexOf("*"), select.indexOf("*")+1, "COUNT(0) as TOTAL");
		//查询总记录数
		Map<String, Object> map = findMapBysql(sourceId, selectTotal.toString(), closeConn, null);
		Long totalRecord = Long.valueOf((map.get("TOTAL").toString()));
		if(totalRecord == 0L){
			return null;
		}
		//计算开始索引
		Long index = (page.getPage()-1L)*page.getPageRecord();
		//计算总页数
		Long totalPage = (totalRecord+page.getPageRecord()-1)/page.getPageRecord();
		//分页查询
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append(" order by ").append(order);
		}
		select.append(" limit ").append(index).append(",").append(page.getPageRecord());
		List<OrderItem> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<OrderItem>) rows;

	}
	
	@Override
	public List<OrderItem> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids, String order) {
		if(uuids == null || uuids.size() == 0){
			return new ArrayList<OrderItem>();
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where orderUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") ");
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append("order by ").append(order);
		}
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
	@Override
	public List<OrderItem> findRefundList(String sourceId, boolean closeConn, List<String> uuids, String orderUUID) {
		if(uuids == null || uuids.size() == 0){
			return new ArrayList<OrderItem>();
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where paramUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") ").append("and orderUUID = '").append(orderUUID).append("'");
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
}
