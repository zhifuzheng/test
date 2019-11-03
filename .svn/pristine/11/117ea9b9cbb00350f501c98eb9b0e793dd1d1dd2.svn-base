package com.xryb.cache.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
/**
 * 缓存接口redis集群实现
 * @author wf
 */
@SuppressWarnings("unchecked")
public class JedisClientCluster implements IJedisClient {
	
	private static JedisCluster jedisCluster;
	
	private static IVipService vipService = new VipServiceImpl();
	
	private static IAccountService accountService = new AccountServiceImpl();
	
	static {
		//初始化redis集群
		
		//redis集群结点1
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		String host1 = ReadProperties.getProperty("/redis.properties", "host1");
		String port1 = ReadProperties.getProperty("/redis.properties", "port1");
		HostAndPort one = new HostAndPort(host1, Integer.valueOf(port1));
		nodes.add(one);
		
		//redis集群结点2
		String host2 = ReadProperties.getProperty("/redis.properties", "host2");
		String port2 = ReadProperties.getProperty("/redis.properties", "port2");
		HostAndPort two = new HostAndPort(host2, Integer.valueOf(port2));
		nodes.add(two);
		
		//redis集群结点3
		String host3 = ReadProperties.getProperty("/redis.properties", "host3");
		String port3 = ReadProperties.getProperty("/redis.properties", "port3");
		HostAndPort three = new HostAndPort(host3, Integer.valueOf(port3));
		nodes.add(three);
		
		//redis集群结点2
		String host4 = ReadProperties.getProperty("/redis.properties", "host4");
		String port4 = ReadProperties.getProperty("/redis.properties", "port4");
		HostAndPort four = new HostAndPort(host4, Integer.valueOf(port4));
		nodes.add(four);
		
		//redis集群结点2
		String host5 = ReadProperties.getProperty("/redis.properties", "host5");
		String port5 = ReadProperties.getProperty("/redis.properties", "port5");
		HostAndPort five = new HostAndPort(host5, Integer.valueOf(port5));
		nodes.add(five);
		
		//redis集群结点2
		String host6 = ReadProperties.getProperty("/redis.properties", "host6");
		String port6 = ReadProperties.getProperty("/redis.properties", "port6");
		HostAndPort six = new HostAndPort(host6, Integer.valueOf(port6));
		nodes.add(six);
		
		jedisCluster = new JedisCluster(nodes);
		
	}
	
	@Override
	public boolean setVipInfo(String openId, VipInfo vip) {
		return "ok".equalsIgnoreCase(jedisCluster.set(openId, JsonUtil.ObjToJson(vip)));
	}
	
	@Override
	public boolean delVipInfo(String openId) {
		return jedisCluster.del(openId) > 0;
	}

	@Override
	public VipInfo getVipInfo(String sourceId, boolean closeConn, String openId) {
		String json = jedisCluster.get(openId);
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
			//设置验证码
			String MapResult = jedisCluster.set(mobile, smsPassCode);
			//设置验证码过期时间
			Integer PASSCODE_EXPIRE_TIME = Integer.valueOf(ReadProperties.getProperty("/redis.properties", "passCodeExpireTime")) * 60;
			Long timeResult = jedisCluster.expire(mobile, PASSCODE_EXPIRE_TIME);
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
		jedisCluster.del(mobile);
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
		String smsPassCode = jedisCluster.get(mobile);
		if(RegExpUtil.isNullOrEmpty(smsPassCode)){
			result.put("status", "-410");
			result.put("msg", "登录过期");
			return result;
		}
		//更新令牌过期时间
		result.put("smsPassCode", smsPassCode);
		result.put("status", "1");
		result.put("msg", "获取验证码成功");
		return result;
	}
	
	@Override
	public boolean setCart(String key, Map<String, Integer> value) {
		return "ok".equalsIgnoreCase(jedisCluster.set(key, JsonUtil.ObjToJson(value)));
	}

	@Override
	public boolean deleteCart(String key) {
		return jedisCluster.del(key) > 0;
	}

	@Override
	public Map<String, Integer> getCart(String key) {
		String json = jedisCluster.get(key);
		if(RegExpUtil.isNullOrEmpty(json)){
			return null;
		}
		return (Map<String, Integer>) JsonUtil.JsonToObj(json, new TypeToken<Map<String, Integer>>(){}.getType());
	}

	@Override
	public boolean setCache(String key, Map<String, String> value) {
		return "ok".equalsIgnoreCase(jedisCluster.set(key, JsonUtil.ObjToJson(value)));
	}

	@Override
	public boolean deleteCache(String key) {
		return jedisCluster.del(key) > 0;
	}

	@Override
	public Map<String, String> getCache(String key) {
		String json = jedisCluster.get(key);
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
		String json = jedisCluster.get(entityUUID);
		if(RegExpUtil.isNullOrEmpty(json)){
			Map<String, String> findMap = new HashMap<>();
			findMap.put("entityUUID", entityUUID);
			Account account = accountService.findByMap(sourceId, closeConn, findMap);
			if(account == null){
				return null;
			}
			jedisCluster.set(entityUUID, JsonUtil.ObjToJson(account));
			return account;
		}
		return (Account) JsonUtil.JsonToObj(json, new TypeToken<Account>(){}.getType());
	}
	
	@Override
	public boolean setAccount(String entityUUID, Account account) {
		return "ok".equalsIgnoreCase(jedisCluster.set(entityUUID, JsonUtil.ObjToJson(account)));
	}
	
	@Override
	public String getWitSetting(String sourceId, boolean closeConn) throws Exception {
		return Cache.getInstance().getWitSetting(sourceId, closeConn);
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
	public boolean setWitSetting(String sourceId, boolean closeConn, WitSetting setting) throws Exception {
		return Cache.getInstance().setWitSetting(sourceId, closeConn, setting);
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
