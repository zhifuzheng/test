package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 商品规格相关service
 * @author wf
 */
public class ItemParamServiceImpl extends BaseServiceImpl<ItemParam> implements IItemParamService {
	
	@Override
	public List<ItemParam> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String itemUUID = null;
		String price = null;
		String paramStatus = null;
		String isDel = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("itemUUID".equals(entry.getKey())){
				itemUUID = entry.getValue();
			}
			if("price".equals(entry.getKey())){
				price = entry.getValue();
			}
			if("paramStatus".equals(entry.getKey())){
				paramStatus = entry.getValue();
			}
			if("isDel".equals(entry.getKey())){
				isDel = entry.getValue();
			}
		}
		if(itemUUID != null){
			findMap.remove("itemUUID");
		}
		if(price != null){
			findMap.remove("price");
		}
		if(paramStatus != null){
			findMap.remove("paramStatus");
		}
		if(isDel != null){
			findMap.remove("isDel");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(itemUUID != null){
			select.append(" and itemUUID = '").append(itemUUID).append("'");
		}
		if(price != null){
			select.append(" and price = ").append(price);
		}
		if(paramStatus != null){
			select.append(" and paramStatus = ").append(paramStatus);
		}
		if(isDel != null){
			select.append(" and isDel = ").append(isDel);
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
		List<ItemParam> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<ItemParam>) rows;
	}
	
	@Override
	public List<Map<String, Object>> findListMapByUUIDs(String sourceId, boolean closeConn, List<String> uuids, String order) {
		if(uuids == null || uuids.size() == 0){
			return new ArrayList<Map<String,Object>>();
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where paramUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") ");
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append("order by ").append(order);
		}
		return findMapListBysql(sourceId, select.toString(), closeConn, null);
	}
	
	@Override
	public List<ItemParam> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids) {
		if(uuids == null || uuids.size() == 0){
			return null;
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where paramUUID in (");
		for (String uuid : uuids) {
			select.append("'").append(uuid).append("',");
		}
		select.deleteCharAt(select.lastIndexOf(",")).append(") ");
		
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
	@Override
	public boolean increseStock(String sourceId, boolean closeConn, Object[][] params) throws Exception {
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set updateTime = '").append(nowTime).append("',stock = stock + ? where paramUUID = ?");
		return batchSql(sourceId, update.toString(), closeConn, params);
	}
	
	@Override
	public boolean decreseStock(String sourceId, boolean closeConn, Object[][] params) throws Exception {
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set updateTime = '").append(nowTime).append("',stock = stock - ? where paramUUID = ?");
		return batchSql(sourceId, update.toString(), closeConn, params);
	}
	
	@Override
	public boolean increseSales(String sourceId, boolean closeConn, Object[][] params) throws Exception {
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set updateTime = '").append(nowTime).append("',sales = sales + ?,monthlySales = monthlySales+? where paramUUID = ?");
		return batchSql(sourceId, update.toString(), closeConn, params);
	}
	
	@Override
	public List<ItemParam> findPurchase(String sourceId, boolean closeConn, List<String> uuids, String entityUUID) {
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
