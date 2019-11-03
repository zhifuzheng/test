package com.xryb.zhtc.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * cookie操作工具类
 * @author hshzh
 */
public class CookieUtil {
	/**
	 * 设置cookie
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期  以秒为单位
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
		if(name==null || "".equals(name) || "null".equals(name)){
			return;
		}
		String encode=null;
		try {
			//对cookie值进行utf-8编码，解决中文问题
			 encode = URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Cookie cookie = new Cookie(name,encode);
	    cookie.setPath("/");//全工程路径可访问cookie
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static String getCookieByName(HttpServletRequest request,String name){
		if(name==null || "".equals(name) || "null".equals(name)){
			return null;
		}
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	    	String decode=null;
	        for(Cookie cookie : cookies){
	        	if(name.equals(cookie.getName())){
	        		try {
	        			//对cookie值进行utf-8编码，解决中文问题
	        			 decode = URLDecoder.decode(cookie.getValue(),"UTF-8");
	        		} catch (UnsupportedEncodingException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		return decode;
	        	}
	        }
	    }
	    return null;
	}
}
