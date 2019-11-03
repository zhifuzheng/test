package com.xryb.pay.weixin;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class WeiXinPayUtil {
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : packageParams.entrySet()){
			String value = entry.getValue();
			String key = entry.getKey();
			if(null == value || "".equals(value.trim()) || "sign".equals(key) || "key".equals(key)){
				continue;
			}
			sb.append(key).append("=").append(value).append("&");
		}
		sb.append("key=").append(WeiXinPayConfig.PARTNER_KEY);
		return MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();

	}
	
	/**
	 * 将xml解析为map
	 * @param xml
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(String xml) {
		Map<String, String> retMap = new HashMap<>();
		try {
			StringReader read = new StringReader(xml);
			//创建新的输入源SAX解析器将使用 InputSource对象来确定如何读取 XML输入
			InputSource source = new InputSource(read);
			//创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			//通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	
	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String currTime = dateFormat.format(now);
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}
	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @param length
	 *            int 设定所取出随机数的长度。length小于11
	 * @return int 返回生成的随机数。
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

}
