package com.xryb.zhtc.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xryb.zhtc.service.IBaseService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.RegExpUtil;

import dbengine.annotation.Table;
import dbengine.dao.BaseDao;
import dbengine.util.Page;
/**
 * 基本实体类的增、删、改、查service
 * @author wf
 * @param <T> 需要操作的实体类的泛型参数
 */
public class BaseServiceImpl<T> extends BaseDao<T> implements IBaseService<T> {
	
	protected Class<T> c;
	
	protected String tableName;
	
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
		c = (Class<T>) superclass.getActualTypeArguments()[0];
		tableName = c.getAnnotation(Table.class).tableName();
	}
	
	@Override
	public boolean truncate(String sourceId, boolean closeConn) {
		StringBuffer truncate =  (new StringBuffer("truncate table ")).append(tableName);
		return executeSql(sourceId, truncate.toString(), closeConn, null);
	}

	@Override
	public boolean saveOrUpdate(String sourceId, boolean closeConn, T t) throws Exception {
		if(t == null){
			return false;
		}
		//生成sql语句
		StringBuffer insert = (new StringBuffer("insert into ")).append(tableName).append(" (");
		StringBuffer values = new StringBuffer(" values(");
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set ");
		//获取字节码对象
		Class<? extends Object> tClass = t.getClass();
		//获取对象的属性列表
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if("id".equals(field.getName()) || "createTime".equals(field.getName()) || "updateTime".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
				continue;
			}
			field.setAccessible(true);
			Object value = field.get(t);
			insert.append(field.getName()).append(",");
			if(RegExpUtil.isNullOrEmpty(value)){
				values.append("null,");
			}else{
				values.append("'").append(value.toString().replaceAll("'", "\"")).append("',");
				update.append(field.getName()).append(" = '").append(value.toString().replaceAll("'", "\"")).append("',");
			}
			
		}
		
		Field idField = tClass.getDeclaredField("id");
		idField.setAccessible(true);
		Object idValue = idField.get(t);
		if(idValue == null){//新增
			values.append("'").append(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")).append("')");
			insert.append("createTime)").append(values);
			return executeSql(sourceId, insert.toString(), closeConn, null);
		}else{//修改
			update.append("updateTime = '").append(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")).append("' where id = ").append(idValue.toString());
			return executeSql(sourceId, update.toString(), closeConn, null);
		}
	}
	
	@Override
	public boolean batchInsert(String sourceId, boolean closeConn, List<T> list) throws Exception {
		if(list == null || list.size() == 0){
			return false;
		}
		//获取对象的属性列表
		List<String> names = new ArrayList<String>();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if("id".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
				continue;
			}
			names.add(field.getName());
		}
		//组装参数
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		Object[][] params = new Object[list.size()][names.size()];
		for(int i = 0;i < list.size();i++){
			T t = list.get(i);
			Class<? extends Object> tClass = t.getClass();
			for(int j = 0;j < names.size();j++){
				Field field = tClass.getDeclaredField(names.get(j));
				field.setAccessible(true);
				Object value = field.get(t);
				if("createTime".equals(names.get(j))){
					params[i][j] = nowTime;
				}else if("updateTime".equals(names.get(j))){
					params[i][j] = null;
				}else{
					if(RegExpUtil.isNullOrEmpty(value)){
						params[i][j] = null;
					}
					params[i][j] = value;
				}
			}
		}
		//生成sql语句
		StringBuffer insert = (new StringBuffer("insert into ")).append(tableName).append(" (");
		StringBuffer values = new StringBuffer(" values(");
		for(String name : names){
			insert.append(name).append(",");
			values.append("?,");
		}
		values.deleteCharAt(values.lastIndexOf(",")).append(")");
		insert.deleteCharAt(insert.lastIndexOf(",")).append(")").append(values);
		return batchSql(sourceId, insert.toString(), closeConn, params);
	}
	
	@Override
	public boolean deleteByMap(String sourceId, boolean closeConn, Map<String, String> findMap) {
		if(findMap == null || findMap.size() == 0){
			return false;
		}
		StringBuffer del = (new StringBuffer("delete from ")).append(tableName).append(" where 1 = 1");
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if(RegExpUtil.isNullOrEmpty(entry.getValue())){
				del.append(" and ").append(entry.getKey()).append(" is null");
			}else{
				del.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
			}
		}
		return executeSql(sourceId, del.toString(), closeConn, null);
	}
	
	@Override
	public boolean batchDelByIds(String sourceId, boolean closeConn, String[] ids) throws Exception {
		if(ids == null || ids.length ==0){
			return false;
		}
		Object[][] params = new Object[ids.length][1];
		for(int i = 0;i < ids.length;i++){
			params[i][0] = ids[i];
		}
		StringBuffer del = (new StringBuffer("delete from ")).append(tableName).append(" where id=? ");
		return batchSql(sourceId, del.toString(), closeConn, params);
	}
	
	@Override
	public boolean updateByMap(String sourceId, boolean closeConn, Map<String, String> paramsMap, Map<String, String> findMap) {
		if(paramsMap == null || paramsMap.size() == 0 || findMap == null || findMap.size() == 0){
			return false;
		}
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set updateTime = '").append(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")).append("',");
		for(Map.Entry<String, String> entry : paramsMap.entrySet()){
			if(RegExpUtil.isNullOrEmpty(entry.getValue())){
				update.append(entry.getKey()).append(" = null,");
			}else{
				update.append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("',");
			}
		}
		update.deleteCharAt(update.lastIndexOf(",")).append(" where 1 = 1");
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if(RegExpUtil.isNullOrEmpty(entry.getValue())){
				update.append(" and ").append(entry.getKey()).append(" is null");
			}else{
				update.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
			}
		}
		return executeSql(sourceId, update.toString(), closeConn, null);
	}
	
	@Override
	public boolean batchUpByField(String sourceId, boolean closeConn, List<T> list, String fieldName) throws Exception {
		if(list == null || list.size() == 0 || fieldName == null){
			return false;
		}
		//获取对象的属性列表
		List<String> names = new ArrayList<String>();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if("serialVersionUID".equals(field.getName())){
				continue;
			}
			names.add(field.getName());
		}
		//组装参数
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		Object[][] params = new Object[list.size()][names.size()+1];
		for(int i = 0;i < list.size();i++){
			T t = list.get(i);
			Class<? extends Object> tClass = t.getClass();
			for(int j = 0;j < names.size();j++){
				Field field = tClass.getDeclaredField(names.get(j));
				field.setAccessible(true);
				Object value = field.get(t);
				if("updateTime".equals(names.get(j))){
					params[i][j] = nowTime;
				}else{
					if(RegExpUtil.isNullOrEmpty(value)){
						params[i][j] = null;
					}else{
						params[i][j] = value;
						if(fieldName.equals(names.get(j))){
							params[i][names.size()] = value;
						}
					}
				}
			}
		}
		//生成sql语句
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set ");
		for(String name : names){
			update.append(name).append(" = ?,");
		}
		update.deleteCharAt(update.lastIndexOf(",")).append(" where ").append(fieldName).append(" = ?");
		return batchSql(sourceId, update.toString(), closeConn, params);
	}
	
	@Override
	public boolean batchUpByIds(String sourceId, boolean closeConn, Map<String, String> paramsMap, String[] ids) {
		if(ids == null || ids.length == 0 || paramsMap == null || paramsMap.size() == 0){
			return false;
		}
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set updateTime = '").append(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")).append("',");
		for(Map.Entry<String, String> entry : paramsMap.entrySet()){
			if(RegExpUtil.isNullOrEmpty(entry.getValue())){
				update.append(entry.getKey()).append(" = null,");
			}else{
				update.append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("',");
			}
		}
		update.deleteCharAt(update.lastIndexOf(",")).append(" where id in (");
		for(String id : ids){
			update.append(id).append(",");
		}
		update.deleteCharAt(update.lastIndexOf(",")).append(")");
		return executeSql(sourceId, update.toString(), closeConn, null);
	}
	
	@Override
	public T findByMap(String sourceId, boolean closeConn, Map<String, String> findMap) {
		if(findMap == null || findMap.size() == 0){
			return null;
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where 1 = 1");
		for(Map.Entry<String, String> entry : findMap.entrySet()){
			if(RegExpUtil.isNullOrEmpty(entry.getValue())){
				select.append(" and ").append(entry.getKey()).append(" is null");
			}else{
				select.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
			}
		}
		return findEntityBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
	@Override
	public List<T> findListByMap(String sourceId, boolean closeConn, Map<String, String> findMap, String order) {
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName).append(" where 1 = 1");
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					select.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append(" order by ").append(order);
		}
		return findListBySql(sourceId, select.toString(), closeConn, c, null);
	}
	
	@Override
	public List<Map<String, Object>> findMapList(String sourceId, boolean closeConn, String entityTableName, Map<String, String> findMap, String order) {
		if(RegExpUtil.isNullOrEmpty(entityTableName)){
			entityTableName = tableName;
		}
		StringBuffer select = (new StringBuffer("select * from ")).append(entityTableName).append(" where 1 = 1");
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					select.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append(" order by ").append(order);
		}
		return findMapListBysql(sourceId, select.toString(), closeConn, null);
	}

	@Override
	public List<T> findPageByMap(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order) {
		//分页查询sql
		StringBuffer select = (new StringBuffer("select * from ")).append(tableName);
		int len = select.length();
		if(findMap.size() > 0){
			select.append(" where ");
		}
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" or ").append(entry.getKey()).append(" is null");
				}else{
					select.append(" or ").append(entry.getKey()).append(" like '%").append(entry.getValue().toString().replaceAll("'", "\"")).append("%'");
				}
			}
		}
		int firstOrIndex = select.indexOf("or", len);
		if(firstOrIndex != -1){
			select.delete(firstOrIndex, firstOrIndex+2);
		}
		StringBuffer selectTotal = new StringBuffer(select.toString()).replace(select.indexOf("*"), select.indexOf("*")+1, "COUNT(0) as TOTAL");
		//查询总记录数
		Map<String, Object> map = findMapBysql(sourceId, selectTotal.toString(), closeConn, null);
		Long totalRecord = Long.valueOf((map.get("TOTAL").toString()));
		if(totalRecord == 0L){
			return null;
		}
		//计算开始索引
		Long index = (page.getPage()-1L)*page.getPageRecord();
		//计算总页数
		Long totalPage = (totalRecord+page.getPageRecord()-1)/page.getPageRecord();
		//分页查询
		if(!RegExpUtil.isNullOrEmpty(order)){
			select.append(" order by ").append(order);
		}
		select.append(" limit ").append(index).append(",").append(page.getPageRecord());
		List<T> rows = findListBySql(sourceId, select.toString(), closeConn, c, null);
		page.setTotalRecord(totalRecord);
		page.setTotalPage(totalPage);
		page.setRows(rows);
		return (List<T>) rows;
	}

	@Override
	public Integer getTotal(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuffer select = (new StringBuffer("select COUNT(0) as TOTAL from ")).append(tableName).append(" where 1 = 1");
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					select.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		Map<String, Object> map = findMapBysql(sourceId, select.toString(), closeConn, null);
		return Integer.valueOf((map.get("TOTAL").toString()));
	}
	
	@Override
	public Integer getTotal(String sourceId, boolean closeConn, Map<String, String> findMap, Map<String, String> notMap) {
		StringBuffer select = (new StringBuffer("select COUNT(0) as TOTAL from ")).append(tableName).append(" where 1 = 1");
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					select.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		if(notMap != null){
			for(Map.Entry<String, String> entry : notMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					select.append(" and ").append(entry.getKey()).append(" is not null");
				}else{
					select.append(" and ").append(entry.getKey()).append(" != '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		Map<String, Object> map = findMapBysql(sourceId, select.toString(), closeConn, null);
		return Integer.valueOf((map.get("TOTAL").toString()));
	}
	
	@Override
	public boolean sortUp(Integer beginSort, Integer endSort, String sort, Map<String,String> findMap) {
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set ").append(sort).append(" = ").append(sort).append("-1 where ").append(sort).append(" > ").append(beginSort).append(" and ").append(sort).append(" <= ").append(endSort);
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					update.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					update.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		return executeSql("jdbcwrite", update.toString(), false, null);
	}

	@Override
	public boolean sortDown(Integer beginSort, Integer endSort, String sort, Map<String,String> findMap) {
		StringBuffer update = new StringBuffer("update ").append(tableName).append(" set ").append(sort).append(" = ").append(sort).append("+1 where ").append(sort).append(" >= ").append(beginSort).append(" and ").append(sort).append(" < ").append(endSort);
		if(findMap != null){
			for(Map.Entry<String, String> entry : findMap.entrySet()){
				if(RegExpUtil.isNullOrEmpty(entry.getValue())){
					update.append(" and ").append(entry.getKey()).append(" is null");
				}else{
					update.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue().toString().replaceAll("'", "\"")).append("'");
				}
			}
		}
		return executeSql("jdbcwrite", update.toString(), false, null);
	}

}
