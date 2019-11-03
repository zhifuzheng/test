package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.reflect.TypeToken;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.service.impl.ItemParamServiceImpl;
import com.xryb.zhtc.service.impl.ItemServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.KindEditorUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 商品管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class ItemMng {
	
	@Auto(name=ItemServiceImpl.class)
	private IItemService itemService;
	
	@Auto(name=ItemParamServiceImpl.class)
	private IItemParamService paramService;
	
	/**
	 * 商品图片、视频上传
	 */
	public String fileupload(HttpServletRequest request) throws Exception {
		String imgPath = ReadProperties.getValue("itemImgPath");//读取商品图片保存路径
		int imgSize = Integer.valueOf(ReadProperties.getValue("itemImgSize"));//读取商品图片大小限制
		String tmpPath = ReadProperties.getValue("uploadtmp");//临时文件保存的路径
		return KindEditorUtil.fileUpload(request, imgPath, imgSize, null, 0, tmpPath);
	}
	/**
	 * 添加商品
	 */
	public String addItem(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Item item = (Item) ReqToEntityUtil.reqToEntity(request, Item.class);
		String itemUUID = UUID.randomUUID().toString().replace("-", "");
		item.setItemUUID(itemUUID);
		//封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		if(itemService.saveOrUpdate(sourceId, closeConn, item)){
			List<ItemParam> itemList = new ArrayList<>();
			ItemParam itemParam = (ItemParam) ReqToEntityUtil.reqToEntity(request, ItemParam.class);
			itemParam.setItemUUID(itemUUID);
			String gradeJson = request.getParameter("gradeData");
			if(!RegExpUtil.isNullOrEmpty(gradeJson)){
				try {
					Map<String, Integer> gradeData = (Map<String, Integer>) JsonUtil.JsonToObj(gradeJson, new TypeToken<Map<String, Integer>>() {}.getType());
					for(Map.Entry<String, Integer> entry : gradeData.entrySet()){
						ItemParam gradeParam = new ItemParam();
						BeanUtils.copyProperties(gradeParam, itemParam);
						gradeParam.setParamUUID(UUID.randomUUID().toString().replace("-", ""));
						gradeParam.setGrade(entry.getKey());
						gradeParam.setPrice(entry.getValue());
						gradeParam.setParamStatus("1");
						itemList.add(gradeParam);
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("status", "-1");
					result.put("msg", "零售商等级对应价格格式错误，服务器无法解析，请联系管理员");
					return JsonUtil.ObjToJson(result);
				}
			}
			if(paramService.batchInsert(sourceId, closeConn, itemList)){
				result.put("status", "1");
				result.put("msg", "添加商品成功");
				return JsonUtil.ObjToJson(result);
			}
		}
		result.put("status", "-2");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 添加或修改商品规格
	 */
	public String saveOrUpParam(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ItemParam param = (ItemParam) ReqToEntityUtil.reqToEntity(request, ItemParam.class);
		
		//封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		String gradeJson = request.getParameter("gradeData");
		if(!RegExpUtil.isNullOrEmpty(gradeJson)){//新增
			List<ItemParam> itemList = new ArrayList<>();
			try {
				Map<String, Integer> gradeData = (Map<String, Integer>) JsonUtil.JsonToObj(gradeJson, new TypeToken<Map<String, Integer>>() {}.getType());
				for(Map.Entry<String, Integer> entry : gradeData.entrySet()){
					ItemParam gradeParam = new ItemParam();
					BeanUtils.copyProperties(gradeParam, param);
					gradeParam.setParamUUID(UUID.randomUUID().toString().replace("-", ""));
					gradeParam.setGrade(entry.getKey());
					gradeParam.setPrice(entry.getValue());
					itemList.add(gradeParam);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "-1");
				result.put("msg", "零售商等级对应价格格式错误，服务器无法解析，请联系管理员");
				return JsonUtil.ObjToJson(result);
			}
			paramService.batchInsert(sourceId, closeConn, itemList);
			result.put("status", "1");
			result.put("msg", "新增商品规格成功");
			return JsonUtil.ObjToJson(result);
		}else{//修改
			paramService.saveOrUpdate(sourceId, closeConn, param);
			result.put("status", "1");
			result.put("msg", "修改商品规格成功");
			return JsonUtil.ObjToJson(result);
		}
	}
	/**
	 * 根据itemUUID修改商品
	 */
	public String upItem(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> paramsMap = ReqToMapUtil.reqToMap(request, Item.class);
		String modelUUID = request.getParameter("modelUUID");
		if("".equals(modelUUID)){
			paramsMap.put("modelUUID", "null");
		}
		String itemUUID = paramsMap.remove("itemUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("itemUUID", itemUUID);
		//使用result封装返回结果
		Map<String, String> result = new HashMap<String, String>();
		if(itemService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
			String distributeStatus = paramsMap.remove("distributeStatus");
			String catUUID = paramsMap.remove("catUUID");
			
			paramsMap.clear();
			//同步商品规格分销状态
			if (!RegExpUtil.isNullOrEmpty(distributeStatus)) {
				paramsMap.put("distributeStatus", distributeStatus);
				if (distributeStatus.equals("1")) {
					String firstRatio = request.getParameter("firstRatio");
					String secondRatio = request.getParameter("secondRatio");
					paramsMap.put("firstRatio", firstRatio);
					paramsMap.put("secondRatio", secondRatio);
				} 
			}
			if (!RegExpUtil.isNullOrEmpty(catUUID)) {
				paramsMap.put("catUUID", catUUID);
				paramService.updateByMap(sourceId, closeConn, paramsMap, findMap);
			}
			result.put("status", "1");
			result.put("msg", "修改商品成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-1");
		result.put("msg", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 分页查询商品列表
	 */
	public String findItemPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Item.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "createTime desc,updateTime desc";
		itemService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		result.put("page", page);
		List<Item> itemList = (List<Item>) page.getRows();
		if(itemList != null){
			result.put("rows", itemList);
		}else{
			result.put("rows", new ArrayList<Item>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 根据itemUUID查询商品规格列表
	 */
	public String findParamList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, ItemParam.class);
		String order = "createTime desc,updateTime desc";
		List<ItemParam> paramList = paramService.findListByMap(sourceId, closeConn, findMap, order);
		return JsonUtil.ObjToJson(paramList);
	}
	/**
	 * 分页查询商品规格列表
	 */
	public String findParamPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, ItemParam.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "createTime desc,updateTime desc";
		paramService.findPageByMap(sourceId, closeConn, page , findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		List<ItemParam> paramList = (List<ItemParam>) page.getRows();
		if(paramList != null){
			result.put("rows", paramList);
		}else{
			result.put("rows", new ArrayList<ItemParam>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 查询当前登录用户的店铺列表
	 */
	public String findEntityList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//验证用户登录是否过期
		Map<String, Object> result = new HashMap<String, Object>();
		VipInfo vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "请使用手机账号重新登录");
			return JsonUtil.ObjToJson(result);
		}
		//获取店铺信息
		String personUUID = vip.getVipUUID();
		String entityTableName = request.getParameter("entityTableName");
		String personUUIDField = request.getParameter("personUUIDField");
		String entityShowField = request.getParameter("entityShowField");
		String entityShowValue = request.getParameter("entityShowValue");
		String entitySortField = request.getParameter("entitySortField");
		String entityTypeField = request.getParameter("entityTypeField");
		String entityTypeValue = request.getParameter("entityTypeValue");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put(personUUIDField, personUUID);
		if(!RegExpUtil.isNullOrEmpty(entityShowField) && !RegExpUtil.isNullOrEmpty(entityShowValue)){
			String[] showFieldArray = entityShowField.split(",");
			String[] showValueArray = entityShowValue.split(",");
			for(int i = 0;i < showFieldArray.length;i++){
				findMap.put(showFieldArray[i], showValueArray[i]);
			}
		}
		if(!RegExpUtil.isNullOrEmpty(entityTypeField) && !RegExpUtil.isNullOrEmpty(entityTypeValue)){
			findMap.put(entityTypeField, entityTypeValue);
		}
		List<Map<String, Object>> entityList = itemService.findMapList(sourceId, closeConn, entityTableName, findMap, entitySortField);
		if(entityList.size() == 0){
			result.put("status", "-2");
			result.put("msg", "没有查询到您的店铺信息");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("msg", "获取数据成功");
		result.put("data", entityList);
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 查询所有店铺列表
	 */
	public String findAllEntity(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, BusinessApply.class);
		String entityTableName = request.getParameter("entityTableName");
		List<Map<String, Object>> entityList = itemService.findMapList(sourceId, closeConn, entityTableName, findMap , null);
		return JsonUtil.ObjToJson(entityList);
	}
	/**
	 * 查询零售商等级列表
	 */
	public String findGradeList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> gradeList = itemService.findMapList(sourceId, closeConn, "business_grade", null, "number");
		if(gradeList == null || gradeList.size() == 0){
			return "[]";
		}
		return JsonUtil.ObjToJson(gradeList);
	}
	/**
	 * 根据itemUUID查询商品信息
	 */
	public String findItem(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String itemUUID = request.getParameter("itemUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("itemUUID", itemUUID);
		Item item = itemService.findByMap(sourceId, closeConn, findMap );
		return JsonUtil.ObjToJson(item);
	}
	
}
