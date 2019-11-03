package com.xryb.cache.service;

import java.util.Map;
import java.util.concurrent.locks.Lock;

import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.DisSetting;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WitSetting;
/**
 * 缓存接口
 * @author wf
 */
public interface IJedisClient {
	
	/**
	 * 设置openId-会员信息对象映射
	 */
	boolean setVipInfo(String openId, VipInfo vip);
	/**
	 * 通过openId移除会员信息
	 */
	boolean delVipInfo(String openId);
	/**
	 * 通过openId获取会员信息
	 */
	VipInfo getVipInfo(String sourceId, boolean closeConn, String openId);
	/**
	 * 设置手机号-短信验证码映射
	 */
	boolean setSmsPassCode(String mobile, String smsPassCode);
	/**
	 * 通过手机号移除短信验证码
	 */
	boolean deleteSmsPassCode(String mobile);
	/**
	 * 获取手机号短信验证码
	 */
	Map<String, String> getSmsPassCode(String mobile);
	/**
	 * 设置购物车缓存
	 */
	boolean setCart(String key, Map<String, Integer> value);
	/**
	 * 移除购物车缓存
	 */
	boolean deleteCart(String key);
	/**
	 * 获取购物车缓存
	 */
	Map<String, Integer> getCart(String key);
	/**
	 * 设置缓存key-value键值对
	 */
	boolean setCache(String key, Map<String, String> value);
	/**
	 * 通过key删除缓存value
	 */
	boolean deleteCache(String key);
	/**
	 * 通过key获取缓存value
	 */
	Map<String, String> getCache(String key);
	/**
	 * 开通账户
	 * @param accountType 账户类型：0会员钱包，1佣金钱包，2零售商钱包，3批发商钱包，4平台钱包
	 * @param entityUUID 实体UUID
	 * @param entityName 实体名称
	 * @param moblie 实体手机号码
	 * @param vipUUID 会员UUID
	 * return 账户对象
	 */
	public Account newAccount(String sourceId, boolean closeConn, String accountType, String entityUUID, String entityName, String mobile, String vipUUID) throws Exception;
	/**
	 * 通过实体UUID获取账户对象
	 * @param entityUUID 实体UUID
	 * @return 账户对象
	 */
	Account getAccount(String sourceId, boolean closeConn, String entityUUID);
	
	boolean setAccount(String entityUUID, Account account);
	
	String getWitSetting(String sourceId, boolean closeConn) throws Exception;
	
	boolean setWitSetting(String sourceId, boolean closeConn, WitSetting setting) throws Exception;
	
	String getDisSetting(String sourceId, boolean closeConn) throws Exception;
	
	boolean setDisSetting(String sourceId, boolean closeConn, DisSetting setting) throws Exception;
	
	/**
	 * 获取access_token
	 */
	String getAccessToken();
	/**
	 * 通过实体UUID获取实体锁对象
	 * @param entityUUID 实体UUID
	 * @return 实体锁对象
	 */
	Lock getEntityLock(String entityUUID);
	/**
	 * 通过商品UUID获取商品锁对象
	 * @param itemUUID 商品UUID
	 * @return 商品锁对象
	 */
	Lock getItemLock(String itemUUID);
	
}