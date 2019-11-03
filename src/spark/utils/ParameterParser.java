package spark.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 功能描述：参数解析基类,根据servlet中的参数类型获取正确的参数值
 * @author      freedom.xie    
 * @version     版本：1.0 
 */
final public class ParameterParser {
	
	/**
	 * 转化参数值
	 * @param name 参数名
	 * @param valueType 参数类型
	 * @return
	 * <li>参数值与指定类型匹配时，返回转化后的参数值</li>
	 * <li>其它，返回指定类型的默认值</li>
	 * @throws Exception 发生异常
	 */
	protected Object parseTypeParam(String name, Class<?> valueType,String v) throws Exception {
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
