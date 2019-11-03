package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import dbengine.util.Page;
/**
 * 基本实体类的增、删、改、查service接口
 * @author wf
 * @param <T> 需要操作的实体类
 */
public interface IBaseService<T> {
	/**
	 * 清空表记录
	 */
	boolean truncate(String sourceId, boolean closeConn);
	/**
	 * 根据实体id，保存或修改实体对象
	 * @param t 需要保存或修改实体对象
	 * @return boolean 保存或修改是否成功
	 */
	boolean saveOrUpdate(String sourceId, boolean closeConn, T t) throws Exception;
	/**
	 * 批量插入实体
	 * @param list 实体集合
	 * @return boolean 批量插入是否成功
	 */
	boolean batchInsert(String sourceId, boolean closeConn, List<T> list) throws Exception;
	/**
	 * 将删除条件封装进paramsMap中，根据条件删除实体
	 * @param findMap 删除条件 字段名-字段值键值对
	 * @return boolean 删除是否成功
	 */
	boolean deleteByMap(String sourceId, boolean closeConn, Map<String, String> findMap);
	/**
	 * 将需要删除的实体id封装进ids数组中，根据id批量删除实体
	 * @param ids id数组
	 * @return boolean 批量删除是否成功
	 */
	boolean batchDelByIds(String sourceId, boolean closeConn, String[] ids) throws Exception;
	/**
	 * 将需要修改的字段封装进paramsMap中，将修改条件封装进findMap中，根据条件修改实体的指定字段
	 * @param paramsMap 需要修改的 字段名-字段值键值对
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @return boolean 更新是否成功
	 */
	boolean updateByMap(String sourceId, boolean closeConn, Map<String, String> paramsMap, Map<String, String> findMap);
	/**
	 * 将需要批量更新的实体放入list中，根据指定的字段名更新实体列表
	 * @param list 需要更新的实体列表
	 * @param fieldName 指定更新条件字段
	 * @return boolean 批量更新是否成功
	 */
	boolean batchUpByField(String sourceId, boolean closeConn, List<T> list, String fieldName) throws Exception;
	/**
	 * 将需要修改的字段封装进paramsMap中，将需要更新的实体id封装进ids数组中，根据id批量更新实体
	 * @param paramsMap 需要修改的 字段名-字段值键值对
	 * @param ids id数组
	 * @return boolean 批量更新是否成功
	 */
	boolean batchUpByIds(String sourceId, boolean closeConn, Map<String, String> paramsMap, String[] ids);
	/**
	 * 将查询条件封装进findMap中，根据条件查询实体对象
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @return T 查询结果实体
	 */
	T findByMap(String sourceId, boolean closeConn, Map<String,String> findMap);
	/**
	 * 将查询条件封装进findMap中，根据条件查询实体列表
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @param order 排序字段
	 * @return List<T> 查询结果列表
	 */
	List<T> findListByMap(String sourceId, boolean closeConn,Map<String,String> findMap, String order);
	/**
	 * 将查询条件封装进findMap中，将查询到的每条记录以键值对的形式映射到map中，并将所有映射好的map封装进list中返回
	 * @param entityTableName 需要查询的数据库表名，默认值实体T对应的数据库表名
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @param order 排序字段
	 * @return List<Map<String, Object>> 查询结果列表
	 */
	List<Map<String, Object>> findMapList(String sourceId, boolean closeConn, String entityTableName, Map<String, String> findMap, String order);
	/**
	 * 将查询条件封装进findMap中，根据条件分页查询
	 * @param page 分页bean必须，否则返回空
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @param order 排序字段
	 * @return List<T> 查询结果列表
	 */
	List<T> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String,String> findMap, String order);
	/**
	 * 将查询条件封装进findMap中，根据条件查询实体数量
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @return Integer 查询结果总数
	 */
	Integer getTotal(String sourceId, boolean closeConn, Map<String,String> findMap);
	/**
	 * 将查询条件封装进findMap(=)和notMap(!=)中，根据条件查询实体数量
	 * @param findMap 查询条件 字段名-字段值键值对
	 * @param notMap 查询条件 字段名-字段值键值对
	 * @return Integer 查询结果总数
	 */
	Integer getTotal(String sourceId, boolean closeConn, Map<String,String> findMap, Map<String,String> notMap);
	/**
	 * 将指定排序在(beginSort,endSort]区间的内，满足条件的记录，整体上移一位
	 * @param beginSort 起始排序
	 * @param endSort 终点排序
	 * @param sort 代表排序的字段名称
	 * @param findMap 排序条件
	 * @return boolean 更新排序是否成功
	 */
	boolean sortUp(Integer beginSort, Integer endSort, String sort, Map<String,String> findMap);
	/**
	 * 将指定排序在[beginSort,endSort)区间的内，满足条件的记录，整体下移一位
	 * @param beginSort 起始排序
	 * @param endSort 终点排序
	 * @param sort 代表排序的字段名称
	 * @param findMap 排序条件
	 * @return boolean 更新排序是否成功
	 */
	boolean sortDown(Integer beginSort, Integer endSort, String sort, Map<String,String> findMap);

}
