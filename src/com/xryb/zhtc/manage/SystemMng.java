package com.xryb.zhtc.manage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.util.HttpEnsms;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;

import spark.annotation.Auto;
/**
 * 系统服务
 * @author wf
 */
public class SystemMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	/**
	 * 发送验证码,返回值为{"msg":"操作成功！","timeout":"2分钟","passcode":"253174","status":"1"}格式的json字符串,
	 * msg为操作信息,timeout为超时时间,passcode为验证码,status为信息状态
	 */
	public String getSmsPassCode(HttpServletRequest request) {
		String mobile = request.getParameter("mobile");
		Map<String,String> result = new HashMap<String,String>();
		if(RegExpUtil.isNullOrEmpty(mobile)){
			result.put("status", "-1");
			result.put("msg", "请输入手机号码");
			return JsonUtil.ObjToJson(result);
		}
		if(!RegExpUtil.checkMobile(mobile)){
			result.put("status", "-2");
			result.put("msg", "手机号码错误");
			return JsonUtil.ObjToJson(result);
		}
		String passCode = createRandomVcode();
		String sms = "【智跑时代】您的验证码为："+passCode+",请在2分钟内处理";
		if(HttpEnsms.sendSMS(mobile, sms)){
			//将手机验证码信息放入缓存
			jedisClient.setSmsPassCode(mobile, passCode);
			result.put("status", "1");
			result.put("msg", "操作成功！");
			result.put("timeout", "2分钟");
			result.put("passCode", passCode);
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "-3");
		result.put("msg", "当前网络繁忙,请稍后重试");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 生成有6位随机数组成的验证码
	 */
	public String createRandomVcode() {
		StringBuffer vcode = new StringBuffer();
		for(int i = 0 ;i < 6 ;i++){
			vcode.append((int)(Math.random() * 9));
		}
		return vcode.toString();
	}
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
