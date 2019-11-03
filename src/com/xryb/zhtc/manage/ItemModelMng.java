package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xryb.zhtc.entity.ItemModel;
import com.xryb.zhtc.service.IItemModelService;
import com.xryb.zhtc.service.impl.ItemModelServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 规格模板管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class ItemModelMng {
	
	@Auto(name=ItemModelServiceImpl.class)
	private IItemModelService modelService;
	
	/**
	 * 添加或修改规格模板
	 */
	public boolean saveOrUpModel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ItemModel model = (ItemModel) ReqToEntityUtil.reqToEntity(request, ItemModel.class);
		//使用findMap封装查询条件
		Map<String, String> findMap = new HashMap<String, String>();
		//获取指定类型下的模板数目
		String catUUID = request.getParameter("catUUID");
		findMap.put("catUUID", catUUID);
		Integer total = modelService.getTotal(sourceId, closeConn, findMap);
		
		String modelUUID = UUID.randomUUID().toString().replace("-", "");
		//不存在该类型的商品模板，说明该商品模板排在第一位
		if(total == 0){
			if (RegExpUtil.isNullOrEmpty(model.getModelUUID())) {
				model.setModelUUID(modelUUID);
			}
			model.setSort(1);
			return modelService.saveOrUpdate(sourceId, closeConn, model);
		}
		//查询是否存在同类型同名的商品模板
		String modelName = request.getParameter("modelName");
		findMap.put("modelName", modelName);
		ItemModel findModel = modelService.findByMap(sourceId, closeConn, findMap);
		//插入指定分类中的最后一位
		if(model.getSort() == total+1){
			//没有id，也不存在同名同类型的模板，说明是新增操作，不需要维护模板的排序
			if (model.getId() == null && findModel == null && model.getModelUUID() == null) {
				if (RegExpUtil.isNullOrEmpty(model.getModelUUID())) {
					model.setModelUUID(modelUUID);
				}
				return modelService.saveOrUpdate(sourceId, closeConn, model);
			}
		}
		//同名同类型，或者有id，说明是更新操作
		if(findModel != null || model.getId() != null || model.getModelUUID() != null){
			//如果有id
			if(findModel == null){
				findMap.clear();
				findMap.put("modelUUID", model.getModelUUID());
				findModel = modelService.findByMap(sourceId, closeConn, findMap);
			}
			model.setId(findModel.getId());
			//同类型同名同排序的商品模板，不需要维护商品模板排序，更新即可
			if(model.getSort() == findModel.getSort()){
				return modelService.saveOrUpdate(sourceId, closeConn, model);
			}
			findMap.clear();
			findMap.put("catUUID", catUUID);
			//商品模板排序由小变大
			if(model.getSort() > findModel.getSort()){
				if(!modelService.sortUp(findModel.getSort(), model.getSort(), "sort", findMap)){
					return false;
				}
			}
			//商品模板排序由大变小
			if(model.getSort() < findModel.getSort()){
				if(!modelService.sortDown(model.getSort(), findModel.getSort(), "sort", findMap)){
					return false;
				}
			}
			return modelService.saveOrUpdate(sourceId, closeConn, model);
		}
		//新增操作，查看该位置是否有模板
		findMap.clear();
		findMap.put("catUUID", catUUID);
		findMap.put("sort", model.getSort().toString());
		findModel = modelService.findByMap(sourceId, closeConn, findMap);
		//如果该位置有模板了，那么该位置后面的模板都要向后移动一位，没有直接插入
		if(findModel != null){
			findMap.clear();
			findMap.put("catUUID", catUUID);
			if(!modelService.sortDown(model.getSort(), total+1, "sort", findMap)){
				return false;
			}
		}
		if (RegExpUtil.isNullOrEmpty(model.getModelUUID())) {
			model.setModelUUID(modelUUID);
		}
		return modelService.saveOrUpdate(sourceId, closeConn, model);
	}
	/**
	 * 根据modelUUID修改模板状态
	 */
	public boolean upModelEnable(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String modelUUID = request.getParameter("modelUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("modelUUID", modelUUID);
		String enable = request.getParameter("enable");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("enable", enable);
		return modelService.updateByMap(sourceId, closeConn, paramsMap, findMap);
	}
	/**
	 * 根据modelUUID删除商品模板
	 */
	public boolean delModel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//使用findMap封装查询条件
		Map<String, String> findMap = new HashMap<String, String>();
		//获取指定分类下的模板数目
		String catUUID = request.getParameter("catUUID");
		findMap.put("catUUID", catUUID);
		Integer total = modelService.getTotal(sourceId, closeConn, findMap);
		//获取模板信息
		String modelUUID = request.getParameter("modelUUID");
		findMap.put("modelUUID", modelUUID);
		ItemModel model = modelService.findByMap(sourceId, closeConn, findMap);
		Integer sort = model.getSort();
		//删除指定商品模板
		if(!modelService.deleteByMap(sourceId, closeConn, findMap)){
			return false;
		}
		//如果指定模板不是指定分类的最后一个，需要将指定模板之后的记录向上移动一位
		if(sort != total){
			findMap.remove("modelUUID");
			if(!modelService.sortUp(sort, total, "sort", findMap)){
				return false;
			}
		}
		return true;
	}
	/**
	 * 根据modelUUID查询商品模板
	 */
	public String findModel(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String modelUUID = request.getParameter("modelUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("modelUUID", modelUUID);
		ItemModel model = modelService.findByMap(sourceId, closeConn, findMap);
		return JsonUtil.ObjToJson(model);
	}
	/**
	 * 分页查询商品模板列表
	 */
	public String findModelPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, ItemModel.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = "sort";
		modelService.findPageByMap(sourceId, closeConn, page , findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		List<ItemModel> modelList = (List<ItemModel>) page.getRows();
		if(modelList != null){
			result.put("rows", modelList);
		}else{
			result.put("rows", new ArrayList<ItemModel>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 根据catUUID查询商品模板列表
	 */
	public String findListByCatUUID(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		String enable = request.getParameter("enable");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("catUUID", catUUID);
		findMap.put("enable", enable);
		String order = "sort";
		List<ItemModel> modelList = modelService.findListByMap(sourceId, closeConn, findMap, order);
		return JsonUtil.ObjToJson(modelList);
	}
	/**
	 * 查询指定分类的商品模板数目
	 */
	public String getSortList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String catUUID = request.getParameter("catUUID");
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("catUUID", catUUID);
		String total = modelService.getTotal(sourceId, closeConn, findMap).toString();
		List<Map<String, String>> optionList = new ArrayList<Map<String, String>>();
		for(int i = 1;i <= Integer.valueOf(total)+1;i++){
			Map<String, String> option = new HashMap<String, String>();
			option.put("text", Integer.valueOf(i).toString());
			option.put("value", Integer.valueOf(i).toString());
			optionList.add(option);
		}
		return JsonUtil.ObjToJson(optionList);
	}
	
}
