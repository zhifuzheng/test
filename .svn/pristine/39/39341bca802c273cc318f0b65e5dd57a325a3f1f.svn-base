package com.xryb.zhtc.service.impl;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.ItemComment;
import com.xryb.zhtc.service.IItemCommentService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 商品评论相关service
 * @author wf
 */
public class ItemCommentServiceImpl extends BaseServiceImpl<ItemComment> implements IItemCommentService {
	
	@Override
	public List<ItemComment> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		//查询分页sql
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String entityName = null;
		String itemUUID = null;
		String score = null;
		String status = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("entityName".equals(entry.getKey())){
				entityName = entry.getValue();
			}
			if("itemUUID".equals(entry.getKey())){
				itemUUID = entry.getValue();
			}
			if("score".equals(entry.getKey())){
				score = entry.getValue();
			}
			if("status".equals(entry.getKey())){
				status = entry.getValue();
			}
		}
		if(entityName != null){
			findMap.remove("entityName");
		}
		if(itemUUID != null){
			findMap.remove("itemUUID");
		}
		if(score != null){
			findMap.remove("score");
		}
		if(status != null){
			findMap.remove("status");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(entityName != null){
			select.append(" and entityName like '%").append(entityName).append("%'");
		}
		if(itemUUID != null){
			select.append(" and itemUUID = '").append(itemUUID).append("'");
		}
		if(score != null){
			select.append(" and score = '").append(score).append("'");
		}
		if(status != null){
			select.append(" and status = '").append(status).append("'");
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
		List<ItemComment> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<ItemComment>) rows;
	}

	@Override
	public String getItemScore(String sourceId, boolean closeConn, String itemUUID) {
		StringBuffer select = (new StringBuffer("select AVG(score) as SCORE from ")).append(tableName).append(" where itemUUID = '").append(itemUUID).append("' and status = 1");
		Map<String, Object> map = findMapBysql(sourceId, select.toString(), closeConn, null);
		Object score = map.get("SCORE");
		if(score == null){
			return "5";
		}
		return score.toString();
	}
	
}
