package com.xryb.zhtc.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xryb.zhtc.authen.IPayAuthen;
import com.xryb.zhtc.util.ParseAutoUtil;

/**
 * 支付验证工厂
 * @author wf
 */
public class AuthenFactory {
	
	private static final Map<String, IPayAuthen> authenMap = new HashMap<>();
	
	private static final ReentrantReadWriteLock authenRWL = new ReentrantReadWriteLock();
	
	/**
	 * 根据支付验证类名生成具体支付验证类对象
	 */
	public static IPayAuthen genSingleAuthen(String className) throws Exception {
		authenRWL.readLock().lock();
		try {
			IPayAuthen authen = authenMap.get(className);
			if(authen == null){
				authenRWL.readLock().unlock();
				//防止高并发时，多个线程在这里发生阻塞导致多次注入
				authenRWL.writeLock().lock();
				authen = authenMap.get(className);
				if(authen == null){
					try {
						Class<?> advisor = Class.forName("com.xryb.zhtc.authen." + className);
						authen = (IPayAuthen) ParseAutoUtil.autowired(advisor);
						authenMap.put(className, authen);
						System.out.println("创建支付验证对象\"" + className + "\"成功！");
					} catch (Exception e) {
						System.out.println("创建支付验证对象\"" + className + "\"失败！");
					}
				}
				authenRWL.readLock().lock();
				authenRWL.writeLock().unlock();
			}
			return authen;
		} finally {
			authenRWL.readLock().unlock();
		}
	}
	
}
