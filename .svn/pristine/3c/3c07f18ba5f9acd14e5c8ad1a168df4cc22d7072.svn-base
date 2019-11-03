package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.Addr;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.IAddrService;
import com.xryb.zhtc.service.impl.AddrServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;
import spark.annotation.Auto;
/**
 * 地址管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class AddrMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=AddrServiceImpl.class)
	private IAddrService addrService;
	
	public String saveOrUpAddr(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		Map<String, String> result = new HashMap<>();
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		Addr addr = (Addr) ReqToEntityUtil.reqToEntity(request, Addr.class);
		if(addrService.saveOrUpdate(sourceId, closeConn, addr)){
			result.put("status", "1");
			result.put("addrUUID", addr.getAddrUUID());
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "当前网络繁忙，请稍后重试");
		return JsonUtil.ObjToJson(result);
	}
	
	public String delAddr(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String openId = request.getParameter("openId");
		Map<String, String> result = new HashMap<>();
		VipInfo vip = null;
		if(RegExpUtil.isNullOrEmpty(openId)){
			vip = (VipInfo) request.getSession().getAttribute("vipinfo");
		}else{
			vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		}
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		
		String addrUUID = request.getParameter("addrUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("addrUUID", addrUUID);
		if(addrService.deleteByMap(sourceId, closeConn, findMap)){
			result.put("status", "1");
			result.put("msg", "删除地址成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "当前网络繁忙，请稍后重试");
		return JsonUtil.ObjToJson(result);
	}
	
	public String findAddrList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Addr.class);
		String order = request.getParameter("order");
		List<Addr> addrList = addrService.findListByMap(sourceId, closeConn, findMap, order);
		if(addrList == null){
			return "[]";
		}
		return JsonUtil.ObjToJson(addrList);
	}
	
	public String findAddrPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Addr.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		addrService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		result.put("page", page);
		List<Addr> addrList = (List<Addr>) page.getRows();
		if(addrList != null){
			result.put("rows", addrList);
		}else{
			result.put("rows", new ArrayList<Addr>());
		}
		return JsonUtil.ObjToJson(result);
	}

}
