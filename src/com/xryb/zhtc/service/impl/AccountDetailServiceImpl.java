package com.xryb.zhtc.service.impl;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.AccountDetail;
import com.xryb.zhtc.service.IAccountDetailService;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.util.Page;
/**
 * 账户明细相关service
 * @author wf
 */
public class AccountDetailServiceImpl extends BaseServiceImpl<AccountDetail> implements IAccountDetailService {
	
	@Override
	public List<AccountDetail> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		//分页查询sql
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		if(findMap.size() > 0){
			select.append(" where ");
		}
		String accountUUID = null;
		String detailType = null;
		String money = null;
		String payChannel = null;
		String detailSource = null;
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if("accountUUID".equals(entry.getKey())){
				accountUUID = entry.getValue();
			}
			if("detailType".equals(entry.getKey())){
				detailType = entry.getValue();
			}
			if("money".equals(entry.getKey())){
				money = entry.getValue();
			}
			if("payChannel".equals(entry.getKey())){
				payChannel = entry.getValue();
			}
			if("detailSource".equals(entry.getKey())){
				detailSource = entry.getValue();
			}
		}
		if(accountUUID != null){
			findMap.remove("accountUUID");
		}
		if(detailType != null){
			findMap.remove("detailType");
		}
		if(money != null){
			findMap.remove("money");
		}
		if(payChannel != null){
			findMap.remove("payChannel");
		}
		if(detailSource != null){
			findMap.remove("detailSource");
		}
		int len = select.length();
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
		}
		int firstOrIndex = select.indexOf("or",len);
		if(firstOrIndex == len+1){
			select.replace(firstOrIndex, firstOrIndex+2, "(").append(")");
		}
		if(accountUUID != null){
			select.append(" and accountUUID = '").append(accountUUID).append("'");
		}
		if(detailType != null){
			select.append(" and detailType = '").append(detailType).append("'");
		}
		if(money != null){
			select.append(" and money = ").append(money);
		}
		if(payChannel != null){
			select.append(" and payChannel = '").append(payChannel).append("'");
		}
		if(detailSource != null){
			select.append(" and detailSource = '").append(detailSource).append("'");
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
		List<AccountDetail> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<AccountDetail>) rows;	
	}
	
}
