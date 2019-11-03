package com.xryb.zhtc.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import com.xryb.zhtc.entity.DisSetting;
import com.xryb.zhtc.service.IDisSettingService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.RegExpUtil;
/**
 * 分销设置相关service
 * @author wf
 */
public class DisSettingServiceImpl extends BaseServiceImpl<DisSetting> implements IDisSettingService {

	@Override
	public boolean saveOrUpdate(String sourceId, boolean closeConn, DisSetting disSetting) throws Exception {
		if(disSetting == null){
			return false;
		}
		//生成sql语句
		StringBuffer insert = (new StringBuffer("insert into ")).append(tableName).append(" (");
		StringBuffer values = new StringBuffer(" values(");
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set ");
		//获取字节码对象
		Class<? extends Object> tClass = disSetting.getClass();
		//获取对象的属性列表
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if("id".equals(field.getName()) || "disUUID".equals(field.getName()) || "createTime".equals(field.getName()) || "updateTime".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
				continue;
			}
			field.setAccessible(true);
			Object value = field.get(disSetting);
			if(value != null){
				insert.append(field.getName()).append(",");
				values.append("'").append(value.toString().replaceAll("'", "\"")).append("',");
				update.append(field.getName()).append(" = '").append(value.toString().replaceAll("'", "\"")).append("',");
			}
		}
		String nowDate = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		if(RegExpUtil.isNullOrEmpty(disSetting.getDisUUID())){//新增
			String disUUID = UUID.randomUUID().toString().replaceAll("-", "");
			values.append("'").append(nowDate).append("','").append(disUUID).append("')");
			insert.append("createTime,").append("disUUID").append(")").append(values);
			//accountUUID返回
			disSetting.setCreateTime(nowDate);
			disSetting.setDisUUID(disUUID);
			return executeSql(sourceId, insert.toString(), closeConn, null);
		}else{//修改
			update.append("updateTime = '").append(nowDate).append("' where disUUID = '").append(disSetting.getDisUUID()).append("'");
			disSetting.setUpdateTime(nowDate);
			return executeSql(sourceId, update.toString(), closeConn, null);
		}
	}
	
}
