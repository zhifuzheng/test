package com.xryb.zhtc.service.impl;

import com.xryb.zhtc.entity.ItemCat;
import com.xryb.zhtc.service.IItemCatService;
/**
 * 商品分类相关service
 * @author wf
 */
public class ItemCatServiceImpl extends BaseServiceImpl<ItemCat> implements IItemCatService {

	@Override
	public boolean updatePrefix(String sourceId, boolean closeConn, String oldCode, String newCode) {
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set catCode = concat('").append(newCode).append("',substring(catCode,").append(oldCode.length()+1).append(")) where catCode like '").append(oldCode).append("%'");
		return executeSql(sourceId, update.toString(), closeConn, null);
	}
	
}
