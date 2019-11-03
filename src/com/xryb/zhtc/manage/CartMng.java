package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.Cart;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.ICartService;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.service.impl.CartServiceImpl;
import com.xryb.zhtc.service.impl.ItemParamServiceImpl;
import com.xryb.zhtc.util.JsonUtil;

import spark.annotation.Auto;
/**
 * 购物车管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class CartMng {

	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=CartServiceImpl.class)
	private ICartService cartService;
	
	@Auto(name=ItemParamServiceImpl.class)
	private IItemParamService paramService;
	
	/**
	 * 向购物车中添加商品规格
	 */
	public String addCart(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取当前登录用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, Object> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		//获取服务器缓存中的购物车信息
		Map<String, Integer> cartCache = jedisClient.getCart("cart:"+vipUUID);
		//获取数据库中的购物车信息
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("vipUUID", vipUUID);
		Cart cart = cartService.findByMap(sourceId, closeConn, findMap);
		
		if(cartCache == null){
			if(cart == null || cart.getCartData().equals("{}")){
				cartCache = new HashMap<String, Integer>();
			}else{
				cartCache = (Map<String, Integer>) JsonUtil.JsonToObj(cart.getCartData(), new TypeToken<Map<String, Integer>>(){}.getType());
			}
		}
		//获取商品规格信息
		String paramUUID = request.getParameter("paramUUID");
		Integer count = Integer.valueOf(request.getParameter("count"));
		Integer sum = cartCache.get(paramUUID);
		if(sum == null){
			sum = count;
		}else{
			sum += count;
		}
		//将商品规格信息放入购物车
		cartCache.put(paramUUID, sum);
		//兼容redis缓存
		jedisClient.setCart("cart:"+vipUUID, cartCache);
		//同步数据库
		if(cart == null){
			cart = new Cart();
			cart.setCartUUID(UUID.randomUUID().toString().replace("-", ""));
			cart.setVipUUID(vipUUID);
		}
		cart.setCartData(JsonUtil.ObjToJson(cartCache));
		if(cartService.saveOrUpdate(sourceId, closeConn, cart)){
			result.put("status", "1");
			result.put("msg", "放入购物车成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "当前网络繁忙，请稍后尝试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 移除购物车中的商品规格
	 */
	public String delCart(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//获取当前登录用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, Object> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		//获取服务器缓存中购物车的信息
		Map<String, Integer> cart = jedisClient.getCart("cart:"+vipUUID);
		//移除购物车中的商品规格
		String uuids = request.getParameter("uuids");
		String[] uuidArr = uuids.split(",");
		for (String paramUUID : uuidArr) {
			cart.remove(paramUUID);
		}
		//兼容redis缓存
		jedisClient.setCart("cart:"+vipUUID, cart);
		//同步数据库
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("vipUUID", vipUUID);
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("cartData", JsonUtil.ObjToJson(cart));
		if(cartService.updateByMap(sourceId, closeConn, paramsMap, findMap)){
			result.put("status", "1");
			result.put("msg", "移除商品规格成功");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-2");
		result.put("msg", "当前网络繁忙，请稍后尝试！");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 设置购物车中的商品规格数量
	 */
	public String setCart(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		//获取当前登录用户信息
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, Object> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		//获取服务器缓存中购物车的信息
		Map<String, Integer> cart = jedisClient.getCart("cart:"+vipUUID);
		//获取商品规格信息
		String paramUUID = request.getParameter("paramUUID");
		Integer count = Integer.valueOf(request.getParameter("count"));
		//设置商品规格数量
		cart.put(paramUUID, count);
		//兼容redis缓存
		jedisClient.setCart("cart:"+vipUUID, cart);
		result.put("status", "1");
		result.put("msg", "商品数量修改成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 展示购物车中的商品规格列表
	 */
	public String findCart(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//验证用户是否登录
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, Object> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		
		//获取购物车信息
		Map<String, Integer> cartCache = jedisClient.getCart("cart:"+vipUUID);
		if(cartCache == null){
			Map<String, String> findMap = new HashMap<String, String>();
			findMap.put("vipUUID", vipUUID);
			Cart cart = cartService.findByMap(sourceId, closeConn, findMap);
			if(cart == null || cart.getCartData().equals("{}")){
				return "[]";
			}
			cartCache = (Map<String, Integer>) JsonUtil.JsonToObj(cart.getCartData(), new TypeToken<Map<String, Integer>>(){}.getType());
			jedisClient.setCart("cart:"+vipUUID, cartCache);
		}
		
		//通过购物车信息查询商品规格列表
		ArrayList<String> uuids = new ArrayList<String>();
		for(Map.Entry<String, Integer> entry : cartCache.entrySet()){
			uuids.add(entry.getKey());
		}
		String order = request.getParameter("order");
		List<Map<String, Object>> findList = paramService.findListMapByUUIDs(sourceId, closeConn, uuids, order);
		for (Map<String, Object> map : findList) {
			//组装返回结果
			String paramUUID = map.get("paramUUID").toString();
			Integer count = cartCache.get(paramUUID);
			map.put("count", count);
		}
		return JsonUtil.ObjToJson(findList);
	}
	
}
