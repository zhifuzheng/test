package com.xryb.zhtc.service;

import java.util.List;

import com.xryb.zhtc.entity.Item;
/**
 * 商品相关service接口
 * @author wf
 */
public interface IItemService extends IBaseService<Item> {
	/**
	 * 更新商品分类前缀
	 */
	boolean updatePrefix(String sourceId, boolean closeConn, String oldCode, String newCode);
	/**
	 * 批量增加商品销量
	 */
	boolean increseSales(String sourceId, boolean closeConn, Object[][] params) throws Exception;
	/**
	 * 根据商品UUID批量查询商品列表
	 */
	List<Item> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids);
	/**
	 * 查询商品进货单
	 */
	List<Item> findPurchase(String sourceId, boolean closeConn, List<String> uuids, String entityUUID);
}
