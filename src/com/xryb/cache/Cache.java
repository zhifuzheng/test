package com.xryb.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xryb.pay.weixin.HttpClientUtil;
import com.xryb.pay.weixin.WeiXinPayConfig;
import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.DisSetting;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WitSetting;
import com.xryb.zhtc.service.IAccountService;
import com.xryb.zhtc.service.IDisSettingService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.IWitSettingService;
import com.xryb.zhtc.service.impl.AccountServiceImpl;
import com.xryb.zhtc.service.impl.DisSettingServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;
import com.xryb.zhtc.service.impl.WitSettingServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;

import javolution.util.FastMap;
/**
 * fastMap实现缓存
 * @author wf
 */
public class Cache {
	/**
	 * openId-会员信息对象映射
	 */
	private final static FastMap<String, VipInfo> tokenCache = new FastMap<String, VipInfo>().shared();	
	/**
	 * 键值对映射
	 */
	private final static FastMap<String, String> mapCache = new FastMap<String, String>().shared();
	/**
	 * 键值对过期时间映射
	 */
	private final static FastMap<String, Long> mapTime = new FastMap<String, Long>().shared();	
	/**
	 * vipUUID-购物车对象映射
	 */
	private final static FastMap<String, Map<String, Integer>> cartCache = new FastMap<String, Map<String, Integer>>().shared();	
	/**
	 * entityUUID-账户映射
	 */
	private final static FastMap<String, Account> accountCache = new FastMap<String, Account>().shared();
	/**
	 * entityUUID-实体锁映射
	 */
	private final static FastMap<String, Lock> entityLock = new FastMap<String, Lock>().shared();
	/**
	 * itemUUID-商品锁映射
	 */
	private final static FastMap<String, Lock> itemLock = new FastMap<String, Lock>().shared();
	/**
	 * 基础缓存，存大部份通用缓存数据
	 */
	private final static FastMap<String, Map<String, String>> baseCache = new FastMap<String, Map<String, String>>().shared();	

	private final static ReentrantReadWriteLock accessRWL = new ReentrantReadWriteLock();
	
	private final static ReentrantReadWriteLock entityRWL = new ReentrantReadWriteLock();
	
	private final static ReentrantReadWriteLock itemRWL = new ReentrantReadWriteLock();
	
	private final static ReentrantReadWriteLock witSettingRWL = new ReentrantReadWriteLock();
	
	private final static ReentrantReadWriteLock disSettingRWL = new ReentrantReadWriteLock();
	
	private final static Cache instance = new Cache();
	
	private static IVipService vipService = new VipServiceImpl();
	
	private static IAccountService accountService = new AccountServiceImpl();
	
	private static IWitSettingService witSettingService = new WitSettingServiceImpl();
	
	private static IDisSettingService disSettingService = new DisSettingServiceImpl();
	
	private Cache() {
		
	}
	
	public static Cache getInstance() {
		return instance;
	}
	
	/**
	 * 设置openId-会员信息对象映射
	 */
	public boolean setVipInfo(String openId, VipInfo vip){
		if(RegExpUtil.isNullOrEmpty(openId) || vip == null){
			return false;
		}
		tokenCache.put(openId, vip);
		return true;
	}
	/**
	 * 通过openId移除会员信息
	 */
	public boolean delVipInfo(String openId){
		if(RegExpUtil.isNullOrEmpty(openId)){
			return false;
		}
		tokenCache.remove(openId);
		return true;
	}
	/**
	 * 通过openId获取会员信息
	 */
	public VipInfo getVipInfo(String sourceId, boolean closeConn, String openId){
		VipInfo vip = tokenCache.get(openId);
		if(vip == null){
			Map<String, String> findMap = new HashMap<>();
			findMap.put("openId", openId);
			vip = vipService.findByMap(sourceId, closeConn, findMap);
			if(vip == null){
				return null;
			}
			tokenCache.put(openId, vip);
		}
		return vip;
	}
	/**
	 * 设置手机号-短信验证码映射
	 */
	public boolean setPassCode(String mobile, String passCode){
		if(!RegExpUtil.isNullOrEmpty(mobile) && !RegExpUtil.isNullOrEmpty(passCode)){
			//设置验证码时间
			mapTime.put(mobile, System.currentTimeMillis());
			//设置验证码
			mapCache.put(mobile, passCode);
			return true;
		}
		return false;
	}
	/**
	 * 通过手机号移除短信验证码
	 */
	public boolean deletePassCode(String mobile){
		if(RegExpUtil.isNullOrEmpty(mobile)){
			return false;
		}
		//移除验证码时间
		mapTime.remove(mobile);
		//移除验证码
		mapCache.remove(mobile);
		return true;
	}
	/**
	 * 获取手机号短信验证码
	 */
	public Map<String, String> getPassCode(String mobile){
		Map<String, String> result = new HashMap<String, String>();
		String passCode = mapCache.get(mobile);
		if(RegExpUtil.isNullOrEmpty(passCode)){
			result.put("status", "-410");
			result.put("msg", "验证码过期");
			return result;
		}
		
		//验证码过期判断
		Long createTime = mapTime.get(mobile);
		if(createTime != null){
			Long PASSCODE_EXPIRE_TIME = Long.valueOf(ReadProperties.getProperty("/redis.properties", "passCodeExpireTime"))*60*1000;
			if(System.currentTimeMillis() - createTime >= PASSCODE_EXPIRE_TIME){
				mapTime.remove(mobile);
				mapCache.remove(mobile);
				result.put("status", "-410");
				result.put("msg", "验证码过期");
				return result;
			}
		}
		result.put("passCode", passCode);
		result.put("status", "1");
		result.put("msg", "获取验证码成功");
		return result;
	}
	
	public boolean setCart(String key, Map<String, Integer> value){
		cartCache.put(key, value);
		return true;
	}
	
	public boolean deleteCart(String key){
		cartCache.remove(key);
		return true;
	}
	
	public Map<String, Integer> getCart(String key){
		return cartCache.get(key);
	}
	
	public boolean setCache(String key, Map<String, String> value){
		baseCache.put(key, value);
		return true;
	}
	
	public boolean deleteCache(String key){
		baseCache.remove(key);
		return true;
	}
	
	public Map<String, String> getCache(String key){
		return baseCache.get(key);
	}
	/**
	 * 开通账户
	 * @param accountType 账户类型：0会员钱包，1佣金钱包，2零售商钱包，3批发商钱包，4平台钱包
	 * @param entityUUID 实体UUID
	 * @param entityName 实体名称
	 * @param moblie 实体手机号码
	 * @param vipUUID 会员UUID
	 */
	public Account newAccount(String sourceId, boolean closeConn, String accountType, String entityUUID, String entityName, String mobile, String vipUUID) throws Exception{
		Lock lock = getEntityLock(entityUUID);
		lock.lock();
		try {
			Map<String, String> findMap = new HashMap<String, String>();
			findMap.put("entityUUID", entityUUID);
			Account account = accountService.findByMap(sourceId, closeConn, findMap);
			if (account == null) {
				account = new Account();
				account.setAccountType(accountType);
				account.setEntityUUID(entityUUID);
				account.setEntityName(entityName);
				account.setMobile(mobile);
				account.setVipUUID(vipUUID);
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} 
			return account;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * 通过实体UUID获取账户对象
	 * @param entityUUID 实体UUID
	 * @return 账户对象
	 */
	public Account getAccount(String sourceId, boolean closeConn, String entityUUID){
		Account account = accountCache.get(entityUUID);
		if(account == null){
			Map<String, String> findMap = new HashMap<>();
			findMap.put("entityUUID", entityUUID);
			account = accountService.findByMap(sourceId, closeConn, findMap);
			if(account == null){
				return null;
			}
			accountCache.put(entityUUID, account);
		}
		return account;
	}
	
	public boolean setAccount(String entityUUID, Account account){
		accountCache.put(entityUUID, account);
		return true;
	}
	/**
	 * 获取提现设置
	 */
	public String getWitSetting(String sourceId, boolean closeConn) throws Exception{
		witSettingRWL.readLock().lock();
		try {
			String setting = mapCache.get("withdrawal_setting");
			if (setting == null) {
				witSettingRWL.readLock().unlock();
				witSettingRWL.writeLock().lock();
				setting = mapCache.get("withdrawal_setting");
				if(setting == null){
					Map<String, String> findMap = new HashMap<>();
					findMap.put("id", "1");
					WitSetting witSetting = witSettingService.findByMap(sourceId, closeConn, findMap);
					if(witSetting == null){
						witSettingService.truncate(sourceId, closeConn);
						witSetting = new WitSetting();
						witSettingService.saveOrUpdate(sourceId, closeConn, witSetting);
					}
					setting = JsonUtil.ObjToJson(witSetting);
					mapCache.put("withdrawal_setting", setting);
				}
				witSettingRWL.readLock().lock();
				witSettingRWL.writeLock().unlock();
			} 
			return setting;
		} finally {
			witSettingRWL.readLock().unlock();
		}
	}
	
	/**
	 * 修改提现设置
	 */
	public boolean setWitSetting(String sourceId, boolean closeConn, WitSetting setting) throws Exception{
		witSettingRWL.writeLock().lock();
		try {
			mapCache.put("withdrawal_setting", JsonUtil.ObjToJson(setting));
			return witSettingService.saveOrUpdate(sourceId, closeConn, setting);
		} finally {
			witSettingRWL.writeLock().unlock();
		}
	}
	
	/**
	 * 获取分销设置
	 */
	public String getDisSetting(String sourceId, boolean closeConn) throws Exception{
		disSettingRWL.readLock().lock();
		try {
			String setting = mapCache.get("distribute_setting");
			if (setting == null) {
				disSettingRWL.readLock().unlock();
				disSettingRWL.writeLock().lock();
				setting = mapCache.get("distribute_setting");
				if(setting == null){
					Map<String, String> findMap = new HashMap<>();
					findMap.put("id", "1");
					DisSetting disSetting = disSettingService.findByMap(sourceId, closeConn, findMap);
					if(disSetting == null){
						disSettingService.truncate(sourceId, closeConn);
						disSetting = new DisSetting();
						disSettingService.saveOrUpdate(sourceId, closeConn, disSetting);
					}
					setting = JsonUtil.ObjToJson(disSetting);
					mapCache.put("distribute_setting", setting);
				}
				disSettingRWL.readLock().lock();
				disSettingRWL.writeLock().unlock();
			} 
			return setting;
		} finally {
			disSettingRWL.readLock().unlock();
		}
	}
	
	/**
	 * 修改分销设置
	 */
	public boolean setDisSetting(String sourceId, boolean closeConn, DisSetting setting) throws Exception{
		disSettingRWL.writeLock().lock();
		try {
			mapCache.put("distribute_setting", JsonUtil.ObjToJson(setting));
			return disSettingService.saveOrUpdate(sourceId, closeConn, setting);
		} finally {
			disSettingRWL.writeLock().unlock();
		}
	}
	
	public String getAccessToken(){
		accessRWL.readLock().lock();
		try {
			String access_token = mapCache.get("access_token");
			Long createTime = mapTime.get("access_token");
			if (access_token == null || System.currentTimeMillis() - createTime >= 7200000) {
				accessRWL.readLock().unlock();
				accessRWL.writeLock().lock();
				access_token = mapCache.get("access_token");
				createTime = mapTime.get("access_token");
				if(access_token == null || System.currentTimeMillis() - createTime >= 7200000){
					String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WeiXinPayConfig.APP_ID+"&secret="+WeiXinPayConfig.APP_SECRET;
					Map<String, String> result = HttpClientUtil.HttpRequest(requestUrl, "GET", null);
					access_token = result.get("access_token");
					mapCache.put("access_token", access_token);
					mapTime.put("access_token", System.currentTimeMillis());
				}
				accessRWL.readLock().lock();
				accessRWL.writeLock().unlock();
			} 
			return access_token;
		} finally {
			accessRWL.readLock().unlock();
		}
	}
	/**
	 * 通过实体UUID获取实体锁对象
	 * @param entityUUID 实体UUID
	 * @return 实体锁对象
	 */
	public Lock getEntityLock(String entityUUID){
		entityRWL.readLock().lock();
		try {
			Lock lock = entityLock.get(entityUUID);
			if (lock == null) {
				entityRWL.readLock().unlock();
				entityRWL.writeLock().lock();
				lock = entityLock.get(entityUUID);
				if(lock == null){
					lock = new ReentrantLock();
					entityLock.put(entityUUID, lock);
				}
				entityRWL.readLock().lock();
				entityRWL.writeLock().unlock();
			} 
			return lock;
		} finally {
			entityRWL.readLock().unlock();
		}
	}
	/**
	 * 通过商品UUID获取商品锁对象
	 * @param itemUUID 商品UUID
	 * @return 商品锁对象
	 */
	public Lock getItemLock(String itemUUID){
		itemRWL.readLock().lock();
		try {
			Lock lock = itemLock.get(itemUUID);
			if (lock == null) {
				itemRWL.readLock().unlock();
				itemRWL.writeLock().lock();
				lock = itemLock.get(itemUUID);
				if(lock == null){
					lock = new ReentrantLock();
					itemLock.put(itemUUID, lock);
				}
				itemRWL.readLock().lock();
				itemRWL.writeLock().unlock();
			} 
			return lock;
		} finally {
			itemRWL.readLock().unlock();
		}
	}

}