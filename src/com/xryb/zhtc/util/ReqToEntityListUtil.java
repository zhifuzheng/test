package com.xryb.zhtc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class ReqToEntityListUtil {

	public static List<Object> reqToEntityList(HttpServletRequest request,
			int index, Class<?> c) throws NoSuchMethodException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {

		// 获取请求数据集
		Object obj = null;
		try {
			obj = c.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		if (obj == null)
			return null;

		List<String> fieldNameList = new ArrayList<String>();

		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			fieldNameList.add(fieldName);
		}
		List<String[]> reqDataList = new ArrayList<String[]>();
		reqDataList = ReqToEntityListUtil.initRequest(request, fieldNameList);
		
		//组装对象list
		List<Object> objList = new ArrayList<Object>();
		for (int i = 0; i < reqDataList.get(index).length; i++) {
			Object objP = null;
			try {
				objP = c.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			if (objP == null)
				return null;

			Field[] newFields = c.getDeclaredFields();
			String newFieldName;
			boolean isSuccess = false;

			if (i <= 2) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
							
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[0];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}

			} else if (i == 3) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[1];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 4 && i <= 9) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[2];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 10 && i <= 15) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[3];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 16 && i <= 23) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[4];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 24 && i <= 31) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[5];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 32 && i <= 39) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[6];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 40 && i <= 47) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[7];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 48 && i <= 55) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[8];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}

					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else if (i >= 56 && i <= 63) {
				int count = 0;
				for (Field objField : newFields) {
					newFieldName = objField.getName();
					if ("serialVersionUID".equals(objField.getName())) {// 自动生成的一个serialVersionUID序列化版本比较值
						count++;
						continue;
					}
					Class<?> type = objField.getType();
					String v = null;
					if (newFieldName.equalsIgnoreCase("id")) {
						if (reqDataList.get(count)==null){
							count++;
							continue;
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("uuid")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					}else if (newFieldName.equalsIgnoreCase("vipUUID")) {
						if (reqDataList.get(count)==null){
							v = null;
						}else{
							v = reqDataList.get(count)[0];
						}
					} else if (newFieldName.equalsIgnoreCase("batchUUID")) {
						if (reqDataList.get(count)==null){
							v = UUID.randomUUID().toString();
						}else{
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}
					} else if (newFieldName.equalsIgnoreCase("batchCode")) {
						v = reqDataList.get(count)[9];
					}else if (newFieldName.equalsIgnoreCase("deployFlag")) {
						v = request.getParameter("deployFlag"+i);
					} else {
						if (reqDataList.get(count) == null) {
							count++;
							continue;
						} else {
							if (i<reqDataList.get(count).length){
								v = reqDataList.get(count)[i];
							}
						}

					}
					if (v == null || v.trim().equals("")) {
						count++;
						continue;
					}
					
					
					Object setterValue;
					try {
						setterValue = parseTypeParam(newFieldName, type, v);
						objField.setAccessible(true);
						objField.set(objP, setterValue);
						isSuccess = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
					count++;
				}
				if (isSuccess) {
					objList.add(objP);
				}
			} else {
				//
			}
		}
		return objList;
	}

	private static List<String[]> initRequest(HttpServletRequest request,List<String> fieldNameList) {
		List<String[]> reqListStr = new ArrayList<String[]>();
		for (int i = 0; i < fieldNameList.size(); i++) {
			String[] fieldName  = request.getParameterValues(fieldNameList.get(i));
			reqListStr.add(fieldName);
		}
		return reqListStr;
	}

	// if (newFieldName.equalsIgnoreCase(batFieldName)){
	// batFieldName = batFieldName.substring(0, 1).toUpperCase() +
	// batFieldName.substring(1); //将属性的首字符大写，方便构造get，set方法
	// Method m = newBatInfoList.get(count1).getClass().getMethod("get" +
	// batFieldName);
	// v = (String) m.invoke(newBatInfoList.get(count1)); //调用getter方法获取属性值
	// }
	/**
	 * 转化参数值
	 * 
	 * @param name
	 *            参数名
	 * @param valueType
	 *            参数类型
	 * @return <li>参数值与指定类型匹配时，返回转化后的参数值</li> <li>其它，返回指定类型的默认值</li>
	 * @throws Exception
	 *             发生异常
	 */
	private static Object parseTypeParam(String name, Class<?> valueType,
			String v) throws Exception {
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
