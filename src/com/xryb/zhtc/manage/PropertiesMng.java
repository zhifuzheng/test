package com.xryb.zhtc.manage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
/**
 * 配置文件管理
 * @author wf
 */
public class PropertiesMng {
	
	/**
	 * 查看系统所有配置文件信息
	 */
	public String findAll() {
		Map<String, Properties> prMap = ReadProperties.getPrMap();
		Map<String, Map<String, String>> result = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String first, String second) {
				return first.compareTo(second);
			}
		});
		for(Map.Entry<String, Properties> entry : prMap.entrySet()){
			String fileName = entry.getKey();
			Properties properties = entry.getValue();
			
			Map<String, String> prSort = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String first, String second) {
					return first.compareTo(second);
				}
			});
			for(Map.Entry<Object, Object> pr : properties.entrySet()){
				String key = pr.getKey().toString();
				String value = pr.getValue().toString();
				prSort.put(key, value);
			}
			
			result.put(fileName, prSort);
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 更新配置文件信息
	 */
	public boolean update(HttpServletRequest request) {
		String fileName = request.getParameter("fileName");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		Map<String, Properties> prMap = ReadProperties.getPrMap();
		Properties properties = prMap.get(fileName);
		properties.put(key, value);
		try {
			String path = PropertiesMng.class.getClassLoader().getResource("/").toURI().getPath()+fileName;
			OutputStream out = new FileOutputStream(path);
			properties.store(out, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * 查看小程序是否正在审核中
	 */
	public String isAudit() {
		String isAudit = ReadProperties.getProperty("/redis.properties", "isAudit");
		return "{\"isAudit\":\""+isAudit+"\"}";
	}

}
