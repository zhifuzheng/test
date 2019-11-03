package com.xryb.zhtc.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 将HttpServletRequest中的参数根据Entity转化为Map对象
 * @author hshzh
 */
public class ReqToMapUtil {
	public static Map<String,String> reqToMap(HttpServletRequest request, Class<?> c){
		Object p = null;
		try {
			p = c.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}		
		if (p == null)
			return null;
		Field[] fields = c.getDeclaredFields();
		String fieldName;
		Map<String,String> pMap = new HashMap<String,String>();
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))
				continue;
			String v = request.getParameter(fieldName);
			if (v == null || "".equals(v))
				continue;
			pMap.put(fieldName, v);
		}
		return pMap;
	}
}
