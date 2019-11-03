package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spark.annotation.Auto;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.service.IMenuUserService;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;

/**
 * 菜单用户关联信息接口service
 * @author hshzh
 *
 */
public class MenuUserServiceImpl implements IMenuUserService{
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;
	/**
	 * 保存菜单用户关联信息
	 */
	@Override
	public boolean saveMenuUser(String sourceId,boolean closeConn,List<MenuUser> muList) {
		if(muList==null || muList.size()<1){
			return false;
		}
		try {
			return entitydao.saveBatch(sourceId, muList, closeConn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 删除菜单用户关联信息
	 */
	@Override
	public boolean deleteMenuUser(String sourceId,boolean closeConn, String menuUUID, String userUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("delete from menu_user where 1 = 1");
		if(menuUUID!=null && !"".equals(menuUUID) && !"null".equals(menuUUID)){
			sql.append(" and menuUUID = ?");
			params.add(menuUUID);
		}
		if(userUUID!=null && !"".equals(userUUID) && !"null".equals(userUUID)){
			sql.append(" and userUUID = ?");
			params.add(userUUID);
		}
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}
	/**
	 * 通过用户uuid查菜单信息
	 */
	@Override
	public List<MenuInfo> findMenuByUserUUID(String sourceId,boolean closeConn, String userUUID) {
		if(userUUID==null || "".equals(userUUID) || "null".equals(userUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from menu_info m where 1 = 1");
		sql.append(" and menuUUID in(");
			sql.append("select menuUUID from menu_user mu where 1 = 1");
			sql.append(" and mu.userUUID = ?");
			params.add(userUUID);
		sql.append(")");
		return (List<MenuInfo>)sqldao.findListBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
	}
	/**
	 * 查询用户的权限
	 */
	@Override
	public List<Map> findAllMenuItemForUser(String sourceId, boolean closeConn,String userUUID) {
		if (StringUtils.isNullOrEmpty(userUUID)) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT mri.menuUUID,mri.menuName,mr.menuUUID AS selected FROM menu_info mri LEFT JOIN (select menuUUID from menu_user ai where ai.userUUID='"+userUUID+"') mr ON mri.menuUUID = mr.menuUUID ORDER BY menuCode");		
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
