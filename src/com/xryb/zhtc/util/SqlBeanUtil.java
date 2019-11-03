package com.xryb.zhtc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import dbengine.annotation.Table;

public class SqlBeanUtil {
	public StringBuilder sql;
	public List<Object> params;

	public static SqlBeanUtil reqToUpdateSql(HttpServletRequest request,
			Class<?> c) {
		Table table = (Table)c.getAnnotation(Table.class);
		SqlBeanUtil sqlBeanUtil = new SqlBeanUtil();
		List<Object> list = new ArrayList<Object>();
		Field[] fields = c.getDeclaredFields();
		String fieldName;
		long id = 0L;
		
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update " + table.tableName() +" set" );
		//构建sql
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))// 自动生成的一个serialVersionUID序列化版本比较值
				continue;
			String v = request.getParameter(fieldName);
			if (v == null || v.trim().equals(""))
				continue;
			if (fieldName.equals("id")){
				field.setAccessible(true);
				try {
					id = Long.parseLong(v);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				updateSql.append(" "+fieldName + " = '"+v+"',");
			}
		}
		if (updateSql.toString().endsWith(",")) {
			updateSql.replace(updateSql.lastIndexOf(","),
					updateSql.length(), " ");
		}
		updateSql.append("where id = ?");
		list.add(id);
		sqlBeanUtil.sql = updateSql;
		sqlBeanUtil.params = list;
		return sqlBeanUtil;
	}
	
	
	public static SqlBeanUtil ObjToUpdateSql(Object obj, boolean isUUID) {
		if (obj ==null){
			return null;
		}
		Class<? extends Object> c = obj.getClass();
		
		Table table = (Table)c.getAnnotation(Table.class);
		if (table == null){
			return null;
		}
		
		SqlBeanUtil sqlBeanUtil = new SqlBeanUtil();
		List<Object> list = new ArrayList<Object>();
		Field[] fields = c.getDeclaredFields();
		String fieldName;
		long id = 0L;
		String uuid = "";
		
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update " + table.tableName() +" set" );
		//构建sql
		for (Field field : fields) {
			fieldName = field.getName();
			if ("serialVersionUID".equals(field.getName()))// 自动生成的一个serialVersionUID序列化版本比较值
				continue;
			Object value = null;
			
			String methodStr = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1); //将属性的首字符大写，方便构造get，set方法
			try {
				Method method = obj.getClass().getMethod("get" +methodStr);
				try {
					value = method.invoke(obj, null);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
			if (value == null)
				continue;
			
			if (fieldName.equals("id")){
				field.setAccessible(true);
				try {
					id = Long.parseLong(value.toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else if (fieldName.equals("uuid")){
				field.setAccessible(true);
				try {
					uuid = value.toString();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}else{
				updateSql.append(" "+fieldName + " = '"+value.toString()+"',");
			}
		}
		if (updateSql.toString().endsWith(",")) {
			updateSql.replace(updateSql.lastIndexOf(","),
					updateSql.length(), " ");
		}
		
		if (isUUID){
			updateSql.append("where uuid = ?");
			list.add(uuid);
		} else {
			updateSql.append("where id = ?");
			list.add(id);
		}
		
		
		sqlBeanUtil.sql = updateSql;
		sqlBeanUtil.params = list;
		return sqlBeanUtil;
	}
}
