package com.xryb.zhtc.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.service.IAccountService;
import com.xryb.zhtc.service.impl.AccountServiceImpl;

/**
 * 账户工具
 * @author wf
 */
public class AccountUtil {
	
	private final static Lock lock = new ReentrantLock();
	
	private static IAccountService accountService = new AccountServiceImpl();
	
	 /**
     * 验证整数（正整数）
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkDigit(String digit) { 
        String regex = "[0-9]+"; 
        return Pattern.matches(regex,digit); 
    }
    
    /**
     * 验证整数和浮点数（正整数和正浮点数）
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean checkDecimals(String decimals) { 
        String regex = "[0-9]*(\\.?)[0-9]*"; 
        return Pattern.matches(regex,decimals); 
    } 
	
	/**
	 * 数字字符串小数点平移工具
	 * @param numStr 数字字符串
	 * @param offset 正数表示小数点向右平移offset位，负数表示小数点向左平移offset位
	 * @param retain 表示精确到小数点后几位
	 * @return 处理结果
	 */
	public static String pointMove(String numStr, int offset, int retain){
		if(!checkDecimals(numStr)){
			return numStr;
		}
		BigDecimal num = new BigDecimal(numStr);
		if(offset > 0){
			BigDecimal result = num.multiply(new BigDecimal("10").pow(offset));
			return result.setScale(retain,BigDecimal.ROUND_HALF_UP).toString();
		}
		if(offset < 0){
			BigDecimal result = num.divide(new BigDecimal("10").pow(-offset));
			return result.setScale(retain,BigDecimal.ROUND_HALF_UP).toString();
		}
		return num.setScale(retain,BigDecimal.ROUND_HALF_UP).toString();
	}
	
	/**
	 * 生成交易单号
	 */
	public static String genOrderNo(){
		return DateTimeUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS")+createRandom();
	}
	
	public static String createRandom() {
		StringBuffer str = new StringBuffer();
		for(int i = 0 ;i < 6 ;i++){
			str.append((int)(Math.random() * 9));
		}
		return str.toString();
	}
	
	/**
	 * 开通账户
	 * @param entityUUID 实体UUID
	 * @param entityName 实体名称
	 * @param moblie 实体手机号码
	 */
	public static Account newAccount(String sourceId, boolean closeConn, String accountType, String entityUUID, String entityName, String mobile, String vipUUID) throws Exception{
		lock.lock();
		try {
			Map<String, String> findMap = new HashMap<String, String>();
			findMap.put("entityUUID", entityUUID);
			Account account = accountService.findByMap(sourceId, closeConn, findMap);
			if (account == null) {
				account = new Account();
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

}
