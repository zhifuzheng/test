package com.xryb.zhtc.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xryb.zhtc.advice.IOrderAdvice;
import com.xryb.zhtc.util.ParseAutoUtil;

/**
 * 订单通知工厂
 * @author wf
 */
public class AdviceFactory {
	
	private static final Map<String, IOrderAdvice> adviceMap = new HashMap<>();
	
	private static final ReentrantReadWriteLock adviceRWL = new ReentrantReadWriteLock();
	
	/**
	 * 根据通知类名生成具体通知类对象
	 */
	public static IOrderAdvice genSingleAdvice(String className) throws Exception {
		adviceRWL.readLock().lock();
		try {
			IOrderAdvice advice = adviceMap.get(className);
			if(advice == null){
				adviceRWL.readLock().unlock();
				//防止高并发时，多个线程在这里发生阻塞导致多次注入
				adviceRWL.writeLock().lock();
				advice = adviceMap.get(className);
				if(advice == null){
					try {
						Class<?> advisor = Class.forName("com.xryb.zhtc.advice." + className);
						advice = (IOrderAdvice) ParseAutoUtil.autowired(advisor);
						adviceMap.put(className, advice);
						System.out.println("创建通知对象\"" + className + "\"成功！");
					} catch (Exception e) {
						System.out.println("创建通知对象\"" + className + "\"失败！");
					}
				}
				adviceRWL.readLock().lock();
				adviceRWL.writeLock().unlock();
			}
			return advice;
		} finally {
			adviceRWL.readLock().unlock();
		}
	}
	
}
