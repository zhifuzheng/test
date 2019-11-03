package com.xryb.cache.service.impl;

import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.xryb.cache.Cache;
import com.xryb.cache.service.IJedisClient;
import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.DisSetting;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WitSetting;
/**
 * 缓存接口fastMap实现
 * @author wf
 */
public class JedisClientFastMap implements IJedisClient {
	
	@Override
	public boolean setVipInfo(String openId, VipInfo vip) {
		return Cache.getInstance().setVipInfo(openId, vip);
	}
	
	@Override
	public boolean delVipInfo(String openId) {
		return Cache.getInstance().delVipInfo(openId);
	}

	@Override
	public VipInfo getVipInfo(String sourceId, boolean closeConn, String openId) {
		return Cache.getInstance().getVipInfo(openId, closeConn, openId);
	}
	
	@Override
	public boolean setSmsPassCode(String mobile, String smsPassCode) {
		return Cache.getInstance().setPassCode(mobile, smsPassCode);
	}

	@Override
	public boolean deleteSmsPassCode(String mobile) {
		return Cache.getInstance().deletePassCode(mobile);
	}

	@Override
	public Map<String, String> getSmsPassCode(String mobile) {
		return Cache.getInstance().getPassCode(mobile);
	}
	
	@Override
	public boolean setCart(String key, Map<String, Integer> value) {
		return Cache.getInstance().setCart(key, value);
	}

	@Override
	public boolean deleteCart(String key) {
		return Cache.getInstance().deleteCart(key);
	}

	@Override
	public Map<String, Integer> getCart(String key) {
		return Cache.getInstance().getCart(key);
	}

	@Override
	public boolean setCache(String key, Map<String, String> value) {
		return Cache.getInstance().setCache(key, value);
	}

	@Override
	public boolean deleteCache(String key) {
		return Cache.getInstance().deleteCache(key);
	}

	@Override
	public Map<String, String> getCache(String key) {
		return Cache.getInstance().getCache(key);
	}
	
	@Override
	public Account newAccount(String sourceId, boolean closeConn, String accountType, String entityUUID, String entityName, String mobile, String vipUUID) throws Exception {
		return Cache.getInstance().newAccount(sourceId, closeConn, accountType, entityUUID, entityName, mobile, vipUUID);
	}

	@Override
	public Account getAccount(String sourceId, boolean closeConn, String entityUUID) {
		return Cache.getInstance().getAccount(sourceId, closeConn, entityUUID);
	}
	
	@Override
	public boolean setAccount(String entityUUID, Account account) {
		return Cache.getInstance().setAccount(entityUUID, account);
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
