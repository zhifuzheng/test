package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.List;

import spark.annotation.Auto;

import com.xryb.zhtc.entity.RoleInfo;
import com.xryb.zhtc.service.IRoleService;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;

/**
 * 角色信息service实现
 * @author hshzh
 *
 */
public class RoleServiceImpl implements IRoleService{
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;
    /**
     * 添加或新增角色
     */
	@Override
	public boolean saveOrUpRole(String sourceId,boolean closeConn,RoleInfo role) {
		if(role==null){
			return false;
		}
		if(role.getId()==null){
			//新增
		    return	entitydao.saveEntity(sourceId,role,closeConn);
		}else{
			return  entitydao.updateEntity(sourceId,role,closeConn);
		}
	}
    /**
     * 删除角色
     */
	@Override
	public boolean deleteRole(String sourceId,boolean closeConn,String roleUUID) {
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("delete from role_info where 1 = 1");
		sql.append(" and roleUUID = ?");
		params.add(roleUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}
    /**
     * 角色uui查角色信息
     */
	@Override
	public RoleInfo findRole(String sourceId,boolean closeConn,String roleUUID) {
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from role_info where 1 = 1");
		sql.append(" and roleUUID = ?");
		params.add(roleUUID);
		return (RoleInfo)sqldao.findEntityBySql(sourceId, sql.toString(), RoleInfo.class, closeConn, params);
	}
	/**
	 * 查角色列表
	 */
	@Override
	public List<RoleInfo> findRoleList(String sourceId,boolean closeConn, Page page) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from role_info");
		if(page == null){
			return (List<RoleInfo>)sqldao.findListBySql(sourceId, sql.toString(), RoleInfo.class, closeConn, null);
		}else{
			return (List<RoleInfo>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, RoleInfo.class, page, null);
		}

	}

}
