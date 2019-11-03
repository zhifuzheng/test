package com.xryb.zhtc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件处理
 * @author hshzh
 *
 */
public class ReadProperties {
	private static String scipconfig = "/config.properties";// 默认配置文件路径
	//private static Properties properties = null;
	private static Map<String,Properties> prMap = new HashMap<String,Properties>();//配置文件放入内存
	//private static InputStream in = null;
	private static Class<ReadProperties> c = ReadProperties.class;

	/**
	 * 初始化配置文件
	 * @param fileName 文件名
	 */
	public static Properties initProperties(String fileName) {
		Properties pr = prMap.get(fileName);
		if (null == pr) {
			pr = new Properties();
			try {
				InputStream in = (InputStream) c.getResourceAsStream(fileName);
				pr.load(in);
				prMap.put(fileName, pr);
				return pr;
			} catch (IOException e) {
				return null;
			}
		}else{
			return pr;
		}
	}
	/**
	 * 获取默认配置文件的值
	 * @param param
	 * @return
	 */
	public static String getValue(String param) {
		try {
			return initProperties(scipconfig).getProperty(param);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	/**
	 * 读取指定配置文件中的值
	 * @param propertyFile
	 * @param param
	 * @return
	 */
	public static String getProperty(String propertyFile,String param) {
		try {
			return initProperties(propertyFile).getProperty(param);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	/**
	 * 返回所有配置文件内存信息
	 * @return
	 */
	public static Map<String,Properties> getPrMap(){
		return prMap;
	}
	
}
