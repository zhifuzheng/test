package com.xryb.zhtc.service;

import com.xryb.zhtc.entity.ItemCat;
/**
 * 商品分类相关service接口
 * @author wf
 */
public interface IItemCatService extends IBaseService<ItemCat> {
	/**
	 * 更新节点前缀
	 */
	boolean updatePrefix(String sourceId, boolean closeConn, String oldCode, String newCode);
}
