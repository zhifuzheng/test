package com.xryb.zhtc.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * 将HttpServletRequest中的表单转换为对应的实体工具类
 * (注：这个只转换表单中的名称写实体字段相同表单)
 * @author hshzh
 */
public class ReqToEntityUtil {
	/**
	 * HttpServletRequest 中的表单数据转换为对应的实体
	 * @param request
	 * @param c
	 * @return
	 */
	public static Object reqToEntity(HttpServletRequest request, Class<?> c){
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
		boolean isOK = false;//判断是否取到对应的字段值
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))//自动生成的一个serialVersionUID序列化版本比较值
				continue;
			Class<?> type = field.getType();
			String v = request.getParameter(fieldName);
			if (v == null || "".equals(v))
				continue;
			Object setterValue;
			try {
				setterValue = parseTypeParam(fieldName, type, v);
				field.setAccessible(true);
				field.set(p, setterValue);
				isOK=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isOK){
			return p;//至少转化了一个变量值
		}else{
			return null;
		}		
	}
	
	/**
	 * HttpServletRequest Map数据转换为对应的实体
	 * @param request
	 * @param c
	 * @return
	 */
	public static Object MapToEntity(Map map, Class<?> c){
		if(map==null){
			return null;
		}
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
		boolean isOK = false;//判断是否取到对应的字段值
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))//自动生成的一个serialVersionUID序列化版本比较值
				continue;
			Class<?> type = field.getType();
			String v = map.get(fieldName)+"";
			if (v == null || "".equals(v) || "null".equals(v))
				continue;
			Object setterValue;
			try {
				if(type==Class.forName("java.lang.Long") || "int".equals(type+"")){
					if(v.indexOf(".")>0){
						v=v.substring(0,v.indexOf("."));
					}
				}
				setterValue = parseTypeParam(fieldName, type, v);
				field.setAccessible(true);
				field.set(p, setterValue);
				isOK=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isOK){
			return p;//至少转化了一个变量值
		}else{
			return null;
		}		
	}
	
	/**
	 * 转化参数值
	 * @param name 参数名
	 * @param valueType 参数类型
	 * @return
	 * <li>参数值与指定类型匹配时，返回转化后的参数值</li>
	 * <li>其它，返回指定类型的默认值</li>
	 * @throws Exception 发生异常
	 */
	private static Object parseTypeParam(String name, Class<?> valueType,String v) throws Exception {
		if (valueType == String.class) {
			return v;
		} else if (valueType == int.class || valueType == Integer.class) {
			return Integer.parseInt(v);
		} else if (valueType == short.class || valueType == Short.class) {
			return Short.parseShort(v);
		} else if (valueType == long.class || valueType == Long.class) {
			return Long.parseLong(v);
		} else if (valueType == byte.class || valueType == Byte.class) {
			return Byte.parseByte(v);
		} else if (valueType == boolean.class || valueType == Boolean.class) {
			return Boolean.parseBoolean(v);
		} else if (valueType == float.class || valueType == Float.class) {
			return Float.parseFloat(v);
		} else if (valueType == double.class || valueType == Double.class) {
			return Double.parseDouble(v);
		} else if (valueType == Date.class) {
			return DateTimeUtil.getDate(v);
		} else if (valueType == Timestamp.class) {
			return DateTimeUtil.getDateTimeStamp(v);
		} else if (valueType == BigInteger.class) {
			return BigInteger.valueOf(Long.valueOf(v));
		} else if (valueType == BigDecimal.class) {
			return BigDecimal.valueOf(Long.valueOf(v));
		} else {
			return null;
		}
	}
}
