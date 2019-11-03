package com.xryb.cache.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.google.gson.reflect.TypeToken;
import com.xryb.cache.Cache;
import com.xryb.cache.service.IJedisClient;
import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.DisSetting;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WitSetting;
import com.xryb.zhtc.service.IAccountService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.impl.AccountServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * 缓存接口redis连接池实现
 * @author wf
 */
@SuppressWarnings("unchecked")
public class JedisClientPool implements IJedisClient {
	
	private static JedisPool jedisPool; 
	
	private static IVipService vipService = new VipServiceImpl();
	
	private static IAccountService accountService = new AccountServiceImpl();
	
	static {
		//初始化redis连接池
		String maxTotal = ReadProperties.getProperty("/redis.properties", "maxTotal");
		String maxIdle = ReadProperties.getProperty("/redis.properties", "maxIdle");
		String maxWaitMillis = ReadProperties.getProperty("/redis.properties", "maxWaitMillis");
		String ip = ReadProperties.getProperty("/redis.properties", "ip");
		String port = ReadProperties.getProperty("/redis.properties", "port");
		String timeout = ReadProperties.getProperty("/redis.properties", "timeout");
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(maxTotal));
		config.setMaxIdle(Integer.valueOf(maxIdle));
		config.setMaxWaitMillis(Integer.valueOf(maxWaitMillis));
		jedisPool = new JedisPool(config, ip, Integer.valueOf(port), Integer.valueOf(timeout));
	
	}
	
	@Override
	public boolean setVipInfo(String openId, VipInfo vip) {
		Jedis jedis = jedisPool.getResource();
		boolean result = "ok".equalsIgnoreCase(jedis.set(openId, JsonUtil.ObjToJson(vip)));
		jedis.close();
		return result;
	}
	
	@Override
	public boolean delVipInfo(String openId) {
		Jedis jedis = jedisPool.getResource();
		boolean result = jedis.del(openId) > 0;
		jedis.close();
		return result;
	}

	@Override
	public VipInfo getVipInfo(String sourceId, boolean closeConn, String openId){
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(openId);
		jedis.close();
		if(RegExpUtil.isNullOrEmpty(json)){
			Map<String, String> findMap = new HashMap<>();
			findMap.put("openId", openId);
			VipInfo vip = vipService.findByMap(sourceId, closeConn, findMap);
			if(vip == null){
				return null;
			}
			setVipInfo(openId, vip);
			return vip;
		}
		return (VipInfo) JsonUtil.JsonToObj(json, new TypeToken<VipInfo>(){}.getType());
	}

	@Override
	public boolean setSmsPassCode(String mobile, String smsPassCode) {
		if (!RegExpUtil.isNullOrEmpty(mobile) && !RegExpUtil.isNullOrEmpty(smsPassCode)) {
			Jedis jedis = jedisPool.getResource();
			//设置验证码
			String MapResult = jedis.set(mobile, smsPassCode);
			//设置验证码过期时间
			Integer PASSCODE_EXPIRE_TIME = Integer.valueOf(ReadProperties.getProperty("/redis.properties", "passCodeExpireTime")) * 60;
			Long timeResult = jedis.expire(mobile, PASSCODE_EXPIRE_TIME);
			jedis.close();
			if ("ok".equalsIgnoreCase(MapResult) && timeResult == 1) {
				return true;
			} 
		}
		return false;
	}

	@Override
	public boolean deleteSmsPassCode(String mobile) {
		if(RegExpUtil.isNullOrEmpty(mobile)){
			return false;
		}
		Jedis jedis = jedisPool.getResource();
		jedis.del(mobile);
		jedis.close();
		return true;
	}

	@Override
	public Map<String, String> getSmsPassCode(String mobile) {
		Map<String, String> result = new HashMap<String, String>();
		if(RegExpUtil.isNullOrEmpty(mobile)){
			result.put("status", "-404");
			result.put("msg","未登录");
			return result;
		}
		Jedis jedis = jedisPool.getResource();
		String smsPassCode = jedis.get(mobile);
		jedis.close();
		if(RegExpUtil.isNullOrEmpty(smsPassCode)){
			result.put("status", "-410");
			result.put("msg", "登录过期");
			return result;
		}
		result.put("smsPassCode", smsPassCode);
		result.put("status", "1");
		result.put("msg", "获取验证码成功");
		return result;
	}
	
	@Override
	public boolean setCart(String key, Map<String, Integer> value) {
		Jedis jedis = jedisPool.getResource();
		boolean result = "ok".equalsIgnoreCase(jedis.set(key, JsonUtil.ObjToJson(value)));
		jedis.close();
		return result;
	}

	@Override
	public boolean deleteCart(String key) {
		Jedis jedis = jedisPool.getResource();
		boolean result = jedis.del(key) > 0;
		jedis.close();
		return result;
	}

	@Override
	public Map<String, Integer> getCart(String key) {
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(key);
		jedis.close();
		if(RegExpUtil.isNullOrEmpty(json)){
			return null;
		}
		return (Map<String, Integer>) JsonUtil.JsonToObj(json, new TypeToken<Map<String, Integer>>(){}.getType());
	}

	@Override
	public boolean setCache(String key, Map<String, String> value) {
		Jedis jedis = jedisPool.getResource();
		boolean result = "ok".equalsIgnoreCase(jedis.set(key, JsonUtil.ObjToJson(value)));
		jedis.close();
		return result;
	}

	@Override
	public boolean deleteCache(String key) {
		Jedis jedis = jedisPool.getResource();
		boolean result = jedis.del(key) > 0;
		jedis.close();
		return result;
	}

	@Override
	public Map<String, String> getCache(String key) {
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(key);
		jedis.close();
		if(RegExpUtil.isNullOrEmpty(json)){
			return null;
		}
		return (Map<String, String>) JsonUtil.JsonToObj(json, new TypeToken<Map<String, String>>(){}.getType());
	}
	
	@Override
	public Account newAccount(String sourceId, boolean closeConn, String accountType, String entityUUID, String entityName, String mobile, String vipUUID) throws Exception {
		return Cache.getInstance().newAccount(sourceId, closeConn, accountType, entityUUID, entityName, mobile, vipUUID);
	}

	@Override
	public Account getAccount(String sourceId, boolean closeConn, String entityUUID) {
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(entityUUID);
		jedis.close();
		if(RegExpUtil.isNullOrEmpty(json)){
			Map<String, String> findMap = new HashMap<>();
			findMap.put("entityUUID", entityUUID);
			Account account = accountService.findByMap(sourceId, closeConn, findMap);
			if(account == null){
				return null;
			}
			jedis.set(entityUUID, JsonUtil.ObjToJson(account));
			return account;
		}
		return (Account) JsonUtil.JsonToObj(json, new TypeToken<Account>(){}.getType());
	}
	
	@Override
	public boolean setAccount(String entityUUID, Account account) {
		Jedis jedis = jedisPool.getResource();
		boolean result = "ok".equalsIgnoreCase(jedis.set(entityUUID, JsonUtil.ObjToJson(account)));
		jedis.close();
		return result;
	}
	
	@Override
	public String getWitSetting(String sourceId, boolean closeConn) throws Exception {
		return Cache.getInstance().getWitSetting(sourceId, closeConn);
	}

	@Override
	public boolean setWitSetting(String sourceId, boolean closeConn, WitSetting setting) throws Exception {
		return Cache.getInstance().setWitSetting(sourceId, closeConn, setting);
	}
	
	@Override
	public String getDisSetting(String sourceId, boolean closeConn) throws Exception {
		return Cache.getInstance().getDisSetting(sourceId, closeConn);
	}

	@Override
	public boolean setDisSetting(String sourceId, boolean closeConn, DisSetting setting) throws Exception {
		return Cache.getInstance().setDisSetting(sourceId, closeConn, setting);
	}
	
	@Override
	public String getAccessToken() {
		return Cache.getInstance().getAccessToken();
	}
	
	@Override
	public Lock getEntityLock(String entityUUID) {
		return Cache.getInstance().getEntityLock(entityUUID);
	}
	
	@Override
	public Lock getItemLock(String itemUUID) {
		return Cache.getInstance().getItemLock(itemUUID);
	}
	
}
