package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spark.annotation.Auto;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.RoleInfo;
import com.xryb.zhtc.entity.RoleUser;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.service.IRoleUserService;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;

/**
 * 角色用户关联信息service
 * @author hshzh
 *
 */
public class RoleUserServiceImpl implements IRoleUserService {

	//注入EntityDao实体
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	//注入SqlDao实体
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;
	/**
	 * 保存角色用户
	 */
	@Override
	public boolean saveRoleUser(String sourceId,boolean closeConn,List<RoleUser> ruList) {
		if(ruList==null || ruList.size()<1){
			return false;
		}		
		try {
			return entitydao.saveBatch(sourceId, ruList, closeConn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 删除用户角色
	 */
	@Override
	public boolean deleteRoleUser(String sourceId,boolean closeConn, String roleUUID, String userUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("delete from role_user where 1 = 1");
		if(roleUUID!=null && !"".equals(roleUUID) && !"null".equals(roleUUID)){
			sql.append(" and roleUUID = ?");
			params.add(roleUUID);
		}
		if(userUUID != null && !"".equals(userUUID) && !"null".equals(userUUID)){
			sql.append(" and userUUID = ?");
			params.add(userUUID);
		}
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}
	/**
	 * 通过角色uuid查用户信息列表
	 */
	@Override
	public List<UserInfo> findUserByRoleUUID(String sourceId,boolean closeConn,String roleUUID) {
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from user_info u where 1=1");
		sql.append(" and userUUID in(");
			sql.append("select userUUID from role_user ru where 1 = 1");
			sql.append(" and ru.roleUUID=?");
			params.add(roleUUID);
		sql.append(")");
		return (List<UserInfo>)sqldao.findListBySql(sourceId, sql.toString(), UserInfo.class, closeConn, params);
	}
	/**
	 * 通过角色uuid查没有关联的用户
	 */
	@Override
	public List<UserInfo> findNotUserByRoleUUID(String sourceId,boolean closeConn,String roleUUID) {
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from user_info u where 1=1");
		sql.append(" and userUUID not in(");
			sql.append("select userUUID from role_user ru where 1=1");
			sql.append(" and ru.roleUUID=?");
			params.add(roleUUID);
		sql.append(")");
		return (List<UserInfo>)sqldao.findListBySql(sourceId, sql.toString(), UserInfo.class, closeConn, params);
	}
	/**
	 * 通过用户uuid查关联的角色信息
	 */
	@Override
	public List<RoleInfo> findRoleByUserUUID(String sourceId,boolean closeConn,String userUUID) {
		if(userUUID==null || "".equals(userUUID) || "null".equals(userUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from role_info ri where ri.roleUUID in(");
		sql.append("select roleUUID from role_user ru where 1 = 1");
		sql.append(" and ru.userUUID = ?");
		params.add(userUUID);
		sql.append(")");
		return (List<RoleInfo>)sqldao.findListBySql(sourceId, sql.toString(), RoleInfo.class, closeConn, params);
	}
    /**
     * 通过用户uuid查没有关联的角色信息
     * @param sourceId
     * @param closeConn
     * @param ecUUID
     * @param userUUID
     * @return
     */
	@Override
	public List<RoleInfo> findNotRoleByUserUUID(String sourceId,boolean closeConn, String userUUID) {
		if(userUUID==null || "".equals(userUUID) || "null".equals(userUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from role_info ri where ri.roleUUID not in(");
		sql.append("select roleUUID from role_user ru where 1 = 1");
		sql.append(" and ru.userUUID = ?");
		params.add(userUUID);
		sql.append(")");
		return (List<RoleInfo>)sqldao.findListBySql(sourceId, sql.toString(), RoleInfo.class, closeConn, params);
	}
	/**
	 * 查询用户拥有的角色
	 */
	@Override
	public List<Map> findRoleToAssignRole(String sourceId, boolean closeConn, String userUUID) {
		if (StringUtils.isNullOrEmpty(userUUID)) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT mri.roleUUID,mri.roleName,mr.roleUUID AS selected from role_info mri LEFT JOIN (select roleUUID from role_user ai where ai.userUUID='"+userUUID+"') mr ON mri.roleUUID = mr.roleUUID ORDER BY mri.roleName");		
		List<Map> listMap = (List<Map>)sqldao.findMapListBysql(sourceId, sql.toString(), closeConn, null);
		if(listMap==null || listMap.size() < 1){
			return null;
		}
		for(Map map : listMap){
			if(map.get("selected") != null){
				map.put("selected", "selected");
			}else{
				map.put("selected", "");
			}
		}
		return listMap;
	}
}
