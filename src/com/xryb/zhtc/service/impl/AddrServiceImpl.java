package com.xryb.zhtc.service.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import com.xryb.zhtc.entity.Addr;
import com.xryb.zhtc.service.IAddrService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.RegExpUtil;
/**
 * 地址相关service
 * @author wf
 */
public class AddrServiceImpl extends BaseServiceImpl<Addr> implements IAddrService {
	
	@Override
	public boolean saveOrUpdate(String sourceId, boolean closeConn, Addr addr) throws Exception {
		if(addr == null){
			return false;
		}
		//生成sql语句
		StringBuffer insert = (new StringBuffer("insert into ")).append(tableName).append(" (");
		StringBuffer values = new StringBuffer(" values(");
		StringBuffer update = (new StringBuffer("update ")).append(tableName).append(" set ");
		//获取字节码对象
		Class<? extends Object> tClass = addr.getClass();
		//获取对象的属性列表
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if("id".equals(field.getName()) || "addrUUID".equals(field.getName()) || "createTime".equals(field.getName()) || "updateTime".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
				continue;
			}
			field.setAccessible(true);
			Object value = field.get(addr);
			if(value != null){
				insert.append(field.getName()).append(",");
				values.append("'").append(value.toString().replaceAll("'", "\"")).append("',");
				update.append(field.getName()).append(" = '").append(value.toString().replaceAll("'", "\"")).append("',");
			}
		}
		String nowDate = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		if(RegExpUtil.isNullOrEmpty(addr.getAddrUUID())){//新增
			String addrUUID = UUID.randomUUID().toString().replaceAll("-", "");
			values.append("'").append(nowDate).append("','").append(addrUUID).append("')");
			insert.append("createTime,").append("addrUUID").append(")").append(values);
			addr.setCreateTime(nowDate);
			addr.setAddrUUID(addrUUID);
			return executeSql(sourceId, insert.toString(), closeConn, null);
		}else{//修改
			update.append("updateTime = '").append(nowDate).append("' where addrUUID = '").append(addr.getAddrUUID()).append("'");
			addr.setUpdateTime(nowDate);
			return executeSql(sourceId, update.toString(), closeConn, null);
		}
	}
	
}
