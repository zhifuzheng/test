package com.xryb.zhtc.service.impl;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 商品相关service
 * @author wf
 */
public class ItemServiceImpl extends BaseServiceImpl<Item> implements IItemService {
	
	@Override
	public boolean updatePrefix(String sourceId, boolean closeConn, String oldCode, String newCode) {
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set catCode = concat('").append(newCode).append("',substring(catCode,").append(oldCode.length()+1).append(")) where catCode like '").append(oldCode).append("%'");
		return executeSql(sourceId, update.toString(), closeConn, null);
	}
	
	@Override
	public List<Item> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String itemStatus = null;
		String entityUUID = null;
		String entityName = null;
		String catUUID = null;
		String catCode = null;
		String isDel = null;
		String price = null;
		String distributeStatus = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("itemStatus".equals(entry.getKey())){
				itemStatus = entry.getValue();
			}
			if("entityUUID".equals(entry.getKey())){
				entityUUID = entry.getValue();
			}
			if("entityName".equals(entry.getKey())){
				entityName = entry.getValue();
			}
			if("catUUID".equals(entry.getKey())){
				catUUID = entry.getValue();
			}
			if("catCode".equals(entry.getKey())){
				catCode = entry.getValue();
			}
			if("isDel".equals(entry.getKey())){
				isDel = entry.getValue();
			}
			if("price".equals(entry.getKey())){
				price = entry.getValue();
			}
			if("distributeStatus".equals(entry.getKey())){
				distributeStatus = entry.getValue();
			}
		}
		if(itemStatus != null){
			findMap.remove("itemStatus");
		}
		if(entityUUID != null){
			findMap.remove("entityUUID");
		}
		if(entityName != null){
			findMap.remove("entityName");
		}
		if(catUUID != null){
			findMap.remove("catUUID");
		}
		if(catCode != null){
			findMap.remove("catCode");
		}
		if(isDel != null){
			findMap.remove("isDel");
		}
		if(price != null){
			findMap.remove("price");
		}
		if(distributeStatus != null){
			findMap.remove("distributeStatus");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(itemStatus != null){
			select.append(" and itemStatus = ").append(itemStatus);
		}
		if(entityUUID != null){
			select.append(" and entityUUID = '").append(entityUUID).append("'");
		}
		if(entityName != null){
			select.append(" and entityName like '%").append(entityName).append("%'");
		}
		if(catUUID != null){
			select.append(" and catUUID = '").append(catUUID).append("'");
		}
		if(catCode != null){
			select.append(" and catCode like '").append(catCode).append("%'");
		}
		if(isDel != null){
			select.append(" and isDel = ").append(isDel);
		}
		if(price != null){
			select.append(" and price = ").append(price);
		}
		if(distributeStatus != null){
			select.append(" and distributeStatus = ").append(distributeStatus);
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
		List<Item> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<Item>) rows;
	}
	
	@Override
	public boolean increseSales(String sourceId, boolean closeConn, Object[][] params) throws Exception {
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set totalSales = totalSales + ?,totalMonthlySales = totalMonthlySales + ?,tagMonthlySales = tagMonthlySales + ? where itemUUID = ?");
		return batchSql(sourceId, update.toString(), closeConn, params);
	}
	
	@Override
	public List<Item> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids) {
		if(uuids == null || uuids.size() == 0){
			return null;
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where itemUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") ");
		
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
	@Override
	public List<Item> findPurchase(String sourceId, boolean closeConn, List<String> uuids, String entityUUID) {
		if(uuids == null || uuids.size() == 0){
			return null;
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where originUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") and entityUUID = '").append(entityUUID).append("'");
		
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}

}
