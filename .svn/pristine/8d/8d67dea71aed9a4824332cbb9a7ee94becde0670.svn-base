package com.xryb.zhtc.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import com.xryb.zhtc.entity.WitSetting;
import com.xryb.zhtc.service.IWitSettingService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.RegExpUtil;
/**
 * 提现设置相关service
 * @author wf
 */
public class WitSettingServiceImpl extends BaseServiceImpl<WitSetting> implements IWitSettingService {
	
	@Override
	public boolean saveOrUpdate(String sourceId, boolean closeConn, WitSetting witSetting) throws Exception {
		if(witSetting == null){
			return false;
		}
		//生成sql语句
		StringBuffer insert = (new StringBuffer("insert into ")).append(tableName).append(" (");
		StringBuffer values = new StringBuffer(" values(");
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set ");
		//获取字节码对象
		Class<? extends Object> tClass = witSetting.getClass();
		//获取对象的属性列表
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if("id".equals(field.getName()) || "witUUID".equals(field.getName()) || "createTime".equals(field.getName()) || "updateTime".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
				continue;
			}
			field.setAccessible(true);
			Object value = field.get(witSetting);
			if(value != null){
				insert.append(field.getName()).append(",");
				values.append("'").append(value.toString().replaceAll("'", "\"")).append("',");
				update.append(field.getName()).append(" = '").append(value.toString().replaceAll("'", "\"")).append("',");
			}
		}
		String nowDate = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		if(RegExpUtil.isNullOrEmpty(witSetting.getWitUUID())){//新增
			String witUUID = UUID.randomUUID().toString().replaceAll("-", "");
			values.append("'").append(nowDate).append("','").append(witUUID).append("')");
			insert.append("createTime,").append("witUUID").append(")").append(values);
			//accountUUID返回
			witSetting.setCreateTime(nowDate);
			witSetting.setWitUUID(witUUID);
			return executeSql(sourceId, insert.toString(), closeConn, null);
		}else{//修改
			update.append("updateTime = '").append(nowDate).append("' where witUUID = '").append(witSetting.getWitUUID()).append("'");
			witSetting.setUpdateTime(nowDate);
			return executeSql(sourceId, update.toString(), closeConn, null);
		}
	}
	
}
