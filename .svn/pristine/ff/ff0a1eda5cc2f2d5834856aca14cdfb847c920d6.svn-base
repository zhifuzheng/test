package spark.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述：生成SQL的功能类,暂不缓存生成的SQL， 如确需提高性能，再增加缓存实现
 * 
 * @author freedom.xie
 * @version 版本：1.0
 */

public class ObjectUtil {

	public static Object req2Obj(HttpServletRequest request, Class<?> c) {
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
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))
				continue;
			Class<?> type = field.getType();
			String v = request.getParameter(fieldName);
			if (v == null || v.trim().equals(""))
				continue;
			Object setterValue;
			try {
				setterValue = parseTypeParam(fieldName, type, v);
				field.setAccessible(true);
				field.set(p, setterValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return p;
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
			return DateUtil.getDate(v);
		} else if (valueType == Timestamp.class) {
			return DateUtil.getDateTimeStamp(v);
		} else if (valueType == BigInteger.class) {
			return NumberUtil.getBigInteger(v);
		} else if (valueType == BigDecimal.class) {
			return NumberUtil.getBigDecimal(v);
		} else {
			return null;
		}
	}

}
