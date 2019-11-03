package com.xryb.zhtc.service.impl;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.ItemModel;
import com.xryb.zhtc.service.IItemModelService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 商品模板相关service
 * @author wf
 */
public class ItemModelServiceImpl extends BaseServiceImpl<ItemModel> implements IItemModelService {
	
	@Override
	public List<ItemModel> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String enable = null;
		String catUUID = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("catUUID".equals(entry.getKey())){
				catUUID = entry.getValue();
			}
			if("enable".equals(entry.getKey())){
				enable = entry.getValue();
			}
		}
		if(catUUID != null){
			findMap.remove("catUUID");
		}
		if(enable != null){
			findMap.remove("enable");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(enable != null){
			select.append(" and enable = ").append(enable);
		}
		if(catUUID != null){
			select.append(" and catUUID = '").append(catUUID).append("'");
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
		List<ItemModel> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<ItemModel>) rows;
	}
	
}
