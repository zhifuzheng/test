package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.BaseDict;
import com.xryb.zhtc.service.IBaseDictService;
import com.xryb.zhtc.service.impl.BaseDictServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 数据字典管理
 * @author wf
 */
public class BaseDictMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=BaseDictServiceImpl.class)
	private IBaseDictService dictService;
	
	private static final String baseDictCacheName = ReadProperties.getProperty("/redis.properties", "baseDictCache");
	
	/**
	 * 添加或修改数据字典
	 */
	public boolean saveOrUpDict(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//同步数据字典缓存
		jedisClient.deleteCache(baseDictCacheName);
		BaseDict baseDict = (BaseDict) ReqToEntityUtil.reqToEntity(request, BaseDict.class);
		if (RegExpUtil.isNullOrEmpty(baseDict.getDictUUID())) {
			baseDict.setDictUUID(UUID.randomUUID().toString().replace("-", ""));
		}
		//字典分类编号默认使用分类名称拼音首字母
		String typeCode = request.getParameter("typeCode");
		//使用findMap封装查询条件
		Map<String, String> findMap = new HashMap<String, String>();
		//获取该类型的字典项数目
		findMap.put("typeCode", typeCode);
		Integer total = dictService.getTotal(sourceId, closeConn, findMap);
		//不存在该类型的字典项，说明该字典项排在第一位
		if(total == 0){
			baseDict.setSort(1);
			return dictService.saveOrUpdate(sourceId, closeConn, baseDict);
		}
		//查询是否存在同类型同名的字典项
		String itemName = request.getParameter("itemName");
		findMap.put("itemName", itemName);
		BaseDict findDict = dictService.findByMap(sourceId, closeConn, findMap);
		//同名同类型，或者有id，说明是更新操作
		if(findDict != null || baseDict.getId() != null){
			//如果有id
			if(findDict == null){
				findMap.clear();
				findMap.put("dictUUID", baseDict.getDictUUID());
				findDict = dictService.findByMap(sourceId, closeConn, findMap);
			}
			//同类型同名同排序的字典项，不需要维护商品字典项排序，更新即可
			if(baseDict.getSort() == findDict.getSort()){
				return dictService.saveOrUpdate(sourceId, closeConn, baseDict);
			}
			findMap.clear();
			findMap.put("typeCode", typeCode);
			//字典项排序由小变大
			if(baseDict.getSort() > findDict.getSort()){
				if(!dictService.sortUp(findDict.getSort(), baseDict.getSort(), "sort", findMap)){
					return false;
				}
			}
			//字典项排序由大变小
			if(baseDict.getSort() < findDict.getSort()){
				if(!dictService.sortDown(baseDict.getSort(), findDict.getSort(), "sort", findMap)){
					return false;
				}
			}
			return dictService.saveOrUpdate(sourceId, closeConn, baseDict);
		}
		//新增操作，查看该位置是否有字典项
		findMap.put("sort", baseDict.getSort().toString());
		findDict = dictService.findByMap(sourceId, closeConn, findMap);
		//如果该位置有字典项了，那么该位置后面的字典项都要向后移动一位，没有直接插入
		if(findDict != null){
			if(!dictService.sortDown(baseDict.getSort(), total+1, "sort", findMap)){
				return false;
			}
		}
		return dictService.saveOrUpdate(sourceId, closeConn, baseDict);
		
	}
	/**
	 * 根据dictUUID删除数据字典
	 */
	public boolean delDict(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//使用findMap封装查询条件
		Map<String, String> findMap = new HashMap<String, String>();
		//获取指定分类下的字典数目
		String typeCode = request.getParameter("typeCode");
		findMap.put("typeCode", typeCode);
		Integer total = dictService.getTotal(sourceId, closeConn, findMap);
		//获取字典信息
		String dictUUID = request.getParameter("dictUUID");
		findMap.put("dictUUID", dictUUID);
		BaseDict baseDict = dictService.findByMap(sourceId, closeConn, findMap);
		Integer sort = baseDict.getSort();
		//删除指定字典
		if(!dictService.deleteByMap(sourceId, closeConn, findMap)){
			return false;
		}
		//如果指定字典不是指定分类的最后一个，需要将指定字典之后的记录向上移动一位
		if(sort != total){
			findMap.remove("dictUUID");
			if(!dictService.sortUp(sort, total, "sort", findMap)){
				return false;
			}
		}
		//同步数据字典缓存
		jedisClient.deleteCache(baseDictCacheName);
		return true;
	}
	/**
	 * 根据dictUUID查询数据字典
	 */
	public String findDict(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String dictUUID = request.getParameter("dictUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("dictUUID", dictUUID);
		BaseDict dict = dictService.findByMap(sourceId, closeConn, findMap);
		return JsonUtil.ObjToJson(dict);
	}
	/**
	 * 分页查询数据字典列表
	 */
	@SuppressWarnings("unchecked")
	public String findDictPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BaseDict.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "typeCode,sort";
		dictService.findPageByMap(sourceId, closeConn, page , findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		List<BaseDict> dictList = (List<BaseDict>) page.getRows();
		if(dictList != null){
			result.put("rows", dictList);
		}else{
			result.put("rows", new ArrayList<BaseDict>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 根据字典类型查询字典项列表
	 */
	public String findListByTypeCode(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String typeCode = request.getParameter("typeCode");
		Map<String, String> baseDictCache = jedisClient.getCache(baseDictCacheName);
		if(baseDictCache == null){
			baseDictCache = new HashMap<String, String>();
		}
		String optionListCache = baseDictCache.get(typeCode);
		if(!RegExpUtil.isNullOrEmpty(optionListCache)){
			return optionListCache;
		}
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("typeCode", typeCode);
		findMap.put("enable", "1");
		String order = "sort";
		List<BaseDict> dictList = dictService.findListByMap(sourceId, closeConn, findMap, order);
		List<Map<String, String>> optionList = new ArrayList<Map<String, String>>();
		for (BaseDict baseDict : dictList) {
			Map<String, String> option = new HashMap<String, String>();
			option.put("text",baseDict.getItemName());
			option.put("value",baseDict.getItemCode());
			optionList.add(option);
		}
		optionListCache = JsonUtil.ObjToJson(optionList);
		baseDictCache.put(typeCode, optionListCache);
		//兼容redis
		jedisClient.setCache(baseDictCacheName, baseDictCache);
		return JsonUtil.ObjToJson(optionList);
	}
	/**
	 * 查询指定类型的字典项数目
	 */
	public String getTotalByTypeCode(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String typeCode = request.getParameter("typeCode");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("typeCode", typeCode);
		return dictService.getTotal(sourceId, closeConn, findMap).toString();
	}
	
}
