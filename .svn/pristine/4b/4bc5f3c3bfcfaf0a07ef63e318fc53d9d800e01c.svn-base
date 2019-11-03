package com.xryb.zhtc.service.impl;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.service.IOrderService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 订单相关service
 * @author wf
 */
public class OrderServiceImpl extends BaseServiceImpl<Order> implements IOrderService {
	
	@Override
	public List<Order> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String entityUUID = null;
		String entityName = null;
		String payerUUID = null;
		String orderStatus = null;
		String price = null;
		String isDel = null;
		String isShow = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("entityUUID".equals(entry.getKey())){
				entityUUID = entry.getValue();
			}
			if("entityName".equals(entry.getKey())){
				entityName = entry.getValue();
			}
			if("payerUUID".equals(entry.getKey())){
				payerUUID = entry.getValue();
			}
			if("orderStatus".equals(entry.getKey())){
				orderStatus = entry.getValue();
			}
			if("price".equals(entry.getKey())){
				price = entry.getValue();
			}
			if("isDel".equals(entry.getKey())){
				isDel = entry.getValue();
			}
			if("isShow".equals(entry.getKey())){
				isShow = entry.getValue();
			}
		}
		if(entityUUID != null){
			findMap.remove("entityUUID");
		}
		if(entityName != null){
			findMap.remove("entityName");
		}
		if(payerUUID != null){
			findMap.remove("payerUUID");
		}
		if(orderStatus != null){
			findMap.remove("orderStatus");
		}
		if(price != null){
			findMap.remove("price");
		}
		if(isDel != null){
			findMap.remove("isDel");
		}
		if(isShow != null){
			findMap.remove("isShow");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(entityUUID != null){
			select.append(" and entityUUID = '").append(entityUUID).append("'");
		}
		if(entityName != null){
			select.append(" and entityName like '%").append(entityName).append("%'");
		}
		if(payerUUID != null){
			select.append(" and payerUUID = '").append(payerUUID).append("'");
		}
		if(orderStatus != null){
			if("refund".equals(orderStatus)){
				select.append(" and orderStatus in (-3,-4,-5,-6,-7,-8)");
			}else{
				select.append(" and orderStatus = '").append(orderStatus).append("'");
			}
		}
		if(price != null){
			select.append(" and price = '").append(price).append("'");
		}
		if(isDel != null){
			select.append(" and isDel = '").append(isDel).append("'");
		}
		if(isShow != null){
			select.append(" and isShow = '").append(isShow).append("'");
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
		List<Order> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<Order>) rows;
	}
	
}
