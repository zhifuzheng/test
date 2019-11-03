package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.ItemParam;
/**
 * 商品规格相关service接口
 * @author wf
 */
public interface IItemParamService extends IBaseService<ItemParam> {
	/**
	 * 根据商品规格UUIDs批量查询商品规格的map映射列表
	 */
	List<Map<String, Object>> findListMapByUUIDs(String sourceId, boolean closeConn, List<String> uuids, String order);
	/**
	 * 根据商品规格UUID批量查询商品规格列表
	 */
	List<ItemParam> findListByUUIDs(String sourceId, boolean closeConn, List<String> uuids);
	/**
	 * 批量增加商品规格库存
	 */
	boolean increseStock(String sourceId, boolean closeConn, Object[][] params) throws Exception;
	/**
	 * 批量减少商品规格库存
	 */
	boolean decreseStock(String sourceId, boolean closeConn, Object[][] params) throws Exception;
	/**
	 * 批量增加商品规格销量
	 */
	boolean increseSales(String sourceId, boolean closeConn, Object[][] params) throws Exception;
	/**
	 * 查询套餐进货单
	 */
	List<ItemParam> findPurchase(String sourceId, boolean closeConn, List<String> uuids, String entityUUID);
	
}
