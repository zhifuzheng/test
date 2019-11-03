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
import com.xryb.zhtc.entity.ItemCat;
import com.xryb.zhtc.service.IItemCatService;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.service.impl.ItemCatServiceImpl;
import com.xryb.zhtc.service.impl.ItemServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;

import spark.annotation.Auto;
/**
 * 商品分类管理
 * @author wf
 */
public class ItemCatMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=ItemCatServiceImpl.class)
	private IItemCatService catService;
	
	@Auto(name=ItemServiceImpl.class)
	private IItemService itemService;
	
	private static final String itemCatCacheName = ReadProperties.getProperty("/redis.properties", "itemCatCache");
	
	//用来存储分类编码，三个数字为一组，范围[001,1000)
	private static List<String> catCodeList= new ArrayList<String>();
	
	static{
		for(int i = 1;i < 1000;i++){
			StringBuffer catCode = new StringBuffer();
			catCode.append(i);
			int length = catCode.length();
			if(length < 3){
				for(int j = 0;j < 3-length;j++){
					catCode.append("0");
				}
				catCode.reverse();
			}
			catCodeList.add(catCode.toString());
		}
	}
	
	/**
	 * 通过parentUUID，查询分类列表
	 */
	public String findChildrenByParent(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String parentUUID = request.getParameter("id");
		if(RegExpUtil.isNullOrEmpty(parentUUID)){
			parentUUID = "-1";
		}
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("parentUUID", parentUUID);
		String order = "sort";
		List<ItemCat> itemCatList = catService.findListByMap(sourceId, closeConn, findMap, order );
		List<Map<String, String>> nodeList = new ArrayList<Map<String, String>>();
		for (ItemCat itemCat : itemCatList) {
			Map<String, String> node = new HashMap<String, String>();
			node.put("id", itemCat.getCatUUID());
			node.put("pid", itemCat.getParentUUID());
			node.put("catCode", itemCat.getCatCode());
			node.put("text", itemCat.getCatName());
			node.put("state", "1".equals(itemCat.getIsParent())?"closed":"open");
			node.put("enable", itemCat.getEnable());
			nodeList.add(node);
		}
		return JsonUtil.ObjToJson(nodeList);
	}
	/**
	 * 新增分类
	 */
	public String addItemCat(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> findMap = new HashMap<String, String>();
		String parentUUID = request.getParameter("parentUUID");
		String parentCode = request.getParameter("parentCode");
		if(RegExpUtil.isNullOrEmpty(parentUUID)){
			parentUUID = "-1";
		}
		findMap.put("parentUUID", parentUUID);
		//查询当前父类的子类数量，新增操作默认排序到最后一位
		Integer total = catService.getTotal(sourceId, closeConn, findMap);
		String catName = request.getParameter("catName");
		ItemCat itemCat = new ItemCat();
		String catUUID = UUID.randomUUID().toString().replace("-", "");
		itemCat.setCatUUID(catUUID);
		itemCat.setParentUUID(parentUUID);
		itemCat.setCatName(catName);
		Integer sort = new Integer(total+1);
		itemCat.setSort(sort);
		if(RegExpUtil.isNullOrEmpty(parentCode)){
			parentCode = "";
		}
		String catCode = genCode(sourceId, closeConn, parentCode, sort);
		itemCat.setCatCode(catCode);
		//封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		//新增子节点
		if(catService.saveOrUpdate(sourceId, closeConn, itemCat)){
			//更新父类
			if(!"-1".equals(parentUUID)){
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("isParent", "1");
				findMap.clear();
				findMap.put("catUUID", parentUUID);
				if(catService.updateByMap(sourceId, closeConn, paramsMap , findMap)){
					result.put("id",catUUID);
					result.put("catCode",catCode);
					result.put("status", "1");
					result.put("msg", "新增节点成功");
					//同步商品分类缓存
					jedisClient.deleteCache(itemCatCacheName);
					return JsonUtil.ObjToJson(result);
				}
				result.put("status", "-1");
				result.put("msg", "当前网络繁忙，请稍后重试！");
				return JsonUtil.ObjToJson(result);
			}
			result.put("id",catUUID);
			result.put("catCode",catCode);
			result.put("status", "1");
			result.put("msg", "新增节点成功");
			//同步商品分类缓存
			jedisClient.deleteCache(itemCatCacheName);
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 通过catUUID，删除当前分类及所有子分类
	 */
	public String delItemCat(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap .put("catUUID", catUUID);
		ItemCat itemCat = catService.findByMap(sourceId, closeConn, findMap);
		findMap.clear();
		String parentUUID = itemCat.getParentUUID();
		findMap.put("parentUUID", parentUUID);
		Integer total = catService.getTotal(sourceId, closeConn, findMap);
		//封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		//如果当前分类是父分类的最后一位，直接删除，不需要维护排序
		Integer sort = itemCat.getSort();
		if(total.equals(sort)){
			if(delCatRec(sourceId, closeConn, catUUID)){
				//将父节点更新为叶子节点
				if(!"-1".equals(parentUUID)){
					findMap.clear();
					findMap.put("catUUID", parentUUID);
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("isParent", "0");
					if(total == 1){
						if(catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
							result.put("status", "1");
							result.put("msg", "删除成功");
							//同步商品分类缓存
							jedisClient.deleteCache(itemCatCacheName);
							return JsonUtil.ObjToJson(result);
						}
						result.put("status", "-1");
						result.put("msg", "当前网络繁忙，请稍后重试！");
						return JsonUtil.ObjToJson(result);
					}
				}
				result.put("status", "1");
				result.put("msg", "删除成功");
				//同步商品分类缓存
				jedisClient.deleteCache(itemCatCacheName);
				return JsonUtil.ObjToJson(result);
			}
		}
		//如果不是最后一位，需要将当前分类之后的分类列表上移一位
		if(delCatRec(sourceId, closeConn, catUUID)){
			findMap.clear();
			findMap.put("parentUUID", parentUUID);
			if(catService.sortUp(sort, total+1, "sort", findMap)){
				result.put("status", "1");
				result.put("msg", "删除成功");
				//同步商品分类缓存
				jedisClient.deleteCache(itemCatCacheName);
				return JsonUtil.ObjToJson(result);
			}
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
		
	}
	/**
	 * 通过catUUID，更新分类名称
	 */
	public String upItemCat(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap .put("catUUID", catUUID);
		String catName = request.getParameter("catName");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("catName", catName);
		//封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		if(catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
			result.put("status", "1");
			result.put("msg", "更新成功");
			//同步商品分类缓存
			jedisClient.deleteCache(itemCatCacheName);
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 通过catUUID，启用或停用当前分类及所有子类
	 */
	public String enableItemCat(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		String enable = request.getParameter("enable");
		Map<String, String> result = new HashMap<String, String>();
		if(enableCatRec(sourceId, closeConn, enable, catUUID)){
			result.put("status", "1");
			result.put("msg", "更新成功");
			//同步商品分类缓存
			jedisClient.deleteCache(itemCatCacheName);
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 将指定分类的排序位置，更新到目标分类的指定位置
	 */
	public boolean dragItemCat(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//同步商品分类缓存
		jedisClient.deleteCache(itemCatCacheName);
		
		String sourceUUID = request.getParameter("sourceUUID");
		String targetUUID = request.getParameter("targetUUID");
		String operate = request.getParameter("operate");//top上，bottom下，append里面
		//使用findMap封装查询条件
		Map<String, String> findMap = new HashMap<String, String>();
		//获取指定分类
		findMap.put("catUUID", sourceUUID);
		ItemCat source = catService.findByMap(sourceId, closeConn, findMap);
		String sourceP = source.getParentUUID();
		Integer sourceSort = source.getSort();
		String sourceCode = source.getCatCode();
		findMap.clear();
		findMap.put("parentUUID", sourceP);
		Integer sourceTotal = catService.getTotal(sourceId, closeConn, findMap);
		//获取目标分类
		findMap.clear();
		findMap.put("catUUID", targetUUID);
		ItemCat target = catService.findByMap(sourceId, closeConn, findMap);
		String targetP = target.getParentUUID();
		Integer targetSort = target.getSort();
		String targetCode = target.getCatCode();
		findMap.clear();
		findMap.put("parentUUID", targetP);
		Integer targetTotal = catService.getTotal(sourceId, closeConn, findMap);
		//使用paramsMap封装需要更新的属性键值对
		Map<String, String> paramsMap = new HashMap<String, String>();
		//1将指定分类放入目标子分类中，默认放在最后一位
		if("append".equals(operate)){
			findMap.clear();
			findMap.put("parentUUID", targetUUID);
			Integer targetChildrenTotal = catService.getTotal(sourceId, closeConn, findMap);
			findMap.clear();
			findMap.put("catUUID", sourceUUID);
			paramsMap.put("parentUUID", targetUUID);
			Integer sort = new Integer(targetChildrenTotal+1);
			paramsMap.put("sort", sort.toString());
			if(!catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
				return false;
			}
			//如果目标分类是叶子，需要更新为节点
			if("0".equals(target.getIsParent())){
				findMap.clear();
				findMap.put("catUUID", targetUUID);
				paramsMap.clear();
				paramsMap.put("isParent", "1");
				if(!catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
					return false;
				}
			}
			//更新节点的前缀
			String catCode = genCode(sourceId, closeConn, targetCode, sort);
			if(!catService.updatePrefix(sourceId, closeConn, sourceCode, catCode)){
				return false;
			}
			itemService.updatePrefix(sourceId, closeConn, sourceCode, catCode);
		}
		//2将指定分类移动到目标子分类上面
		if("top".equals(operate)){
			//如果指定分类和目标分类同属一个父分类
			if(sourceP.equals(targetP)){
				findMap.clear();
				findMap.put("parentUUID", targetP);
				//指定分类排序由小变大
				if(sourceSort < targetSort-1){
					if(catService.sortUp(sourceSort, targetSort-1, "sort", findMap)){
						findMap.clear();
						findMap.put("catUUID", sourceUUID);
						paramsMap.clear();
						Integer sort = new Integer(targetSort-1);
						paramsMap.put("sort", sort.toString());
						return catService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					}
				}
				//指定分类排序由大变小
				if(sourceSort > targetSort){
					if(catService.sortDown(targetSort, sourceSort, "sort", findMap)){
						findMap.clear();
						findMap.put("catUUID", sourceUUID);
						paramsMap.clear();
						paramsMap.put("sort", targetSort.toString());
						return catService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					}
				}
			}
			//如果指定分类和目标分类不属于同一个父分类，需要将目标分类及之下的分类整体下移一位
			findMap.clear();
			findMap.put("parentUUID", targetP);
			if(!catService.sortDown(targetSort, targetTotal+1, "sort", findMap)){
				return false;
			}
			//然后插入指定分类
			findMap.clear();
			findMap.put("catUUID", sourceUUID);
			paramsMap.clear();
			paramsMap.put("parentUUID", targetP);
			paramsMap.put("sort", targetSort.toString());
			if(!catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
				return false;
			}
			//更新节点的前缀
			if(targetCode.lastIndexOf(",") != -1){
				targetCode = targetCode.substring(0,targetCode.lastIndexOf(","));
			}else{
				targetCode = "";
			}
			String catCode = genCode(sourceId, closeConn, targetCode, targetSort);
			if(!catService.updatePrefix(sourceId, closeConn, sourceCode, catCode)){
				return false;
			}
			itemService.updatePrefix(sourceId, closeConn, sourceCode, catCode);
		}
		//3将指定分类移动到目标子分类下面
		if("bottom".equals(operate)){
			//如果指定分类和目标分类同属一个父分类
			if(sourceP.equals(targetP)){
				findMap.clear();
				findMap.put("parentUUID", targetP);
				//指定分类排序由小变大
				if(sourceSort < targetSort){
					if(catService.sortUp(sourceSort, targetSort, "sort", findMap)){
						findMap.clear();
						findMap.put("catUUID", sourceUUID);
						paramsMap.clear();
						paramsMap.put("sort", targetSort.toString());
						return catService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					}
				}
				//指定分类排序由大变小
				if(sourceSort > targetSort+1){
					if(catService.sortDown(targetSort+1, sourceSort, "sort", findMap)){
						findMap.clear();
						findMap.put("catUUID", sourceUUID);
						paramsMap.clear();
						Integer sort = new Integer(targetSort+1);
						paramsMap.put("sort", sort.toString());
						return catService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					}
				}
			}
			//如果指定分类和目标分类不属于同一个父分类，且目标分类不是其父分类的最后一位，需要将目标分类之后的分类整体下移一位
			if(!targetSort.equals(targetTotal)){
				findMap.clear();
				findMap.put("parentUUID", targetP);
				if(!catService.sortDown(targetSort+1, targetTotal+1, "sort", findMap)){
					return false;
				}
			}
			//然后插入指定分类
			findMap.clear();
			findMap.put("catUUID", sourceUUID);
			paramsMap.clear();
			paramsMap.put("parentUUID", targetP);
			Integer sort = new Integer(targetSort+1);
			paramsMap.put("sort", sort.toString());
			if(!catService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
				return false;
			}
			//更新节点的前缀
			if(targetCode.lastIndexOf(",") != -1){
				targetCode = targetCode.substring(0, targetCode.lastIndexOf(","));
			}else{
				targetCode = "";
			}
			String catCode = genCode(sourceId, closeConn, targetCode, sort);
			if(!catService.updatePrefix(sourceId, closeConn, sourceCode, catCode)){
				return false;
			}
			itemService.updatePrefix(sourceId, closeConn, sourceCode, catCode);
		}
		//如果指定分类的父分类只有当前一个子分类，需要将其父分类更新为叶子
		if(sourceTotal == 1){
			findMap.clear();
			findMap.put("catUUID", sourceP);
			paramsMap.clear();
			paramsMap.put("isParent", "0");
			if(catService.updateByMap(sourceId, closeConn, paramsMap , findMap)){
				return true;
			}
		}
		//如果不是最后一位，需要将当前分类之后的子分类，整体向上移动一位
		if(sourceSort != sourceTotal){
			findMap.clear();
			findMap.put("parentUUID", sourceP);
			if(catService.sortUp(sourceSort, sourceTotal, "sort", findMap)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 递归删除
	 */
	private boolean delCatRec(String sourceId, boolean closeConn, String catUUID){
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("parentUUID", catUUID);
		List<ItemCat> itemCatList = catService.findListByMap(sourceId, closeConn, findMap, "sort");
		if(itemCatList != null && itemCatList.size() > 0){
			for (ItemCat itemCat : itemCatList) {
				String childUUID = itemCat.getCatUUID();
				if(delCatRec(sourceId, closeConn, childUUID)){
					continue;
				}
				return false;
			}
		}
		findMap.clear();
		findMap.put("catUUID", catUUID);
		return catService.deleteByMap(sourceId, closeConn, findMap);
	}
	/**
	 * 递归更新
	 */
	private boolean enableCatRec(String sourceId, boolean closeConn, String enable, String catUUID){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("enable", enable);
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("parentUUID", catUUID);
		List<ItemCat> itemCatList = catService.findListByMap(sourceId, closeConn, findMap, "sort");
		if(itemCatList != null && itemCatList.size() > 0){
			for (ItemCat itemCat : itemCatList) {
				String childUUID = itemCat.getCatUUID();
				if(enableCatRec(sourceId, closeConn, enable, childUUID)){
					continue;
				}
				return false;
			}
		}
		findMap.clear();
		findMap.put("catUUID", catUUID);
		return catService.updateByMap(sourceId, closeConn, paramsMap, findMap);
	}
	/**
	 * 使用父节点编号和当前节点排序生成节点编码
	 */
	private String genCode(String sourceId, boolean closeConn, String parentCode, Integer sort){
		//用"0"补足编号位
		StringBuffer sortStr = new StringBuffer();
		sortStr.append(sort);
		int length = sortStr.length();
		if(length < 3){
			for(int j = 0;j < 3-length;j++){
				sortStr.append("0");
			}
			sortStr.reverse();
		}
		String catCode = parentCode + "," + sortStr.toString();
		if(RegExpUtil.isNullOrEmpty(parentCode)){
			catCode = sortStr.toString();
		}
		//查询该编号位是否被占用，没有被占用就将值返回使用
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("catCode", catCode);
		ItemCat find = catService.findByMap(sourceId, closeConn, findMap);
		if(find == null){
			return catCode;
		}
		//如果编号被占用，就查找没有被占用的编号
		findMap.clear();
		findMap.put("parentUUID", find.getParentUUID());
		List<ItemCat> findList = catService.findListByMap(sourceId, closeConn, findMap, "sort");
		findMap.clear();
		for (ItemCat itemCat : findList) {
			String str = itemCat.getCatCode();
			if(str.lastIndexOf(",") != -1){
				str = str.substring(str.lastIndexOf(",")+1);
			}
			findMap.put(str, str);
		}
		for (String code : catCodeList) {
			if(findMap.get(code) == null){
				if(RegExpUtil.isNullOrEmpty(parentCode)){
					return code;
				}
				return parentCode + "," + code;
			}
		}
		return "-1";
	}
	/**
	 * 通过parentUUID，查询子分类列表
	 */
	public String getCatList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String parentUUID = request.getParameter("catUUID");
		Map<String, String> itemCatCache = jedisClient.getCache(itemCatCacheName);
		if(itemCatCache == null){
			itemCatCache = new HashMap<String, String>();
		}
		String cacheList = itemCatCache.get(parentUUID);
		if(!RegExpUtil.isNullOrEmpty(cacheList)){
			return cacheList;
		}
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("parentUUID", parentUUID);
		findMap.put("enable", "1");
		String order = "sort";
		List<ItemCat> itemCatList = catService.findListByMap(sourceId, closeConn, findMap, order);
		cacheList = JsonUtil.ObjToJson(itemCatList);
		itemCatCache.put(parentUUID, cacheList);
		//兼容redis
		jedisClient.setCache(itemCatCacheName, itemCatCache);
		return cacheList;
	}

}
