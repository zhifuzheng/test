package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.GxMenu;
import com.xryb.zhtc.service.IGxMenuService;
import com.xryb.zhtc.util.CodeUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
import spark.annotation.Auto;

public class GxMenuServiceImpl implements IGxMenuService {
	/**
	 *  注入dao
	 */
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;

	@Override
	public boolean menuSave(String sourceId, boolean closeConn, GxMenu gxMenu) {
		if(gxMenu == null) {
			return false;
		}
		if(!"-1".equals(gxMenu.getMenuParentCode())) {//不是顶级节点，修改父节点状态为有下级节点
			this.upMenuLowerNodeState(sourceId, closeConn, null, gxMenu.getMenuParentCode(), "1");
		}
		if(gxMenu.getId()==null) {
			return entitydao.saveEntity(sourceId, gxMenu, closeConn);
		}else {
			return entitydao.updateEntity(sourceId, gxMenu, closeConn);
		}
	}

	@Override
	public boolean upMenuLowerNodeState(String sourceId, boolean closeConn, String ecUUID, String menuCode,
			String menuHaveLowerNode) {
		if(menuCode==null || "".equals(menuCode) || "null".equals(menuCode)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("update gx_menu set menuHaveLowerNode = ?");
		params.add(menuHaveLowerNode);
		sql.append(" where 1=1 ");
		sql.append(" and menuCode = ?");
		params.add(menuCode);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public String getMenuCodeNext(String sourceId, boolean closeConn, String menuParentCode) {
		if (menuParentCode == null || "".equals(menuParentCode) || "null".equals(menuParentCode)) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from gx_menu t where 1=1");
		sql.append(" and t.menuParentCode = '").append(menuParentCode).append("'");
		//查该父节点的所有下级节点
		List<GxMenu> menuList = (List<GxMenu>) sqldao.findListBySql(sourceId, sql.toString(), GxMenu.class, closeConn, params);
		String returnCode = "";
		// 生成下一个节点
		// 将同级节点保存到map,目的是从中找出没有被利用的code,避免节点被删除后编码不能再次使用的问题
		Map map = new HashMap();
		for (GxMenu menuinfo : menuList) {
			// 表示是直接下级节点，加入到map中
			map.put(menuinfo.getMenuCode(), menuinfo.getMenuCode());
		}
		for (int i = 0; i < CodeUtil.getCode2Arr().length; i++) {
			if ("-1".equals(menuParentCode)) {// 父节点为-1表示为顶级节点，做特殊处理
				if (map.get(CodeUtil.getCode2Arr()[i]) == null) {
					// 没有查到该编码，说明可用
					returnCode = CodeUtil.getCode2Arr()[i];
					break;
				}
			} else {
				if (map.get(menuParentCode + CodeUtil.getCode2Arr()[i]) == null) {
						// 没有查到该编码，说明可用
						returnCode = menuParentCode + CodeUtil.getCode2Arr()[i];
						break;
				}
			}
		}
		return returnCode;
	}

	@Override
	public List<GxMenu> findMenuList(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from gx_menu where 1=1"); 
		if(findMap!=null){
			if(findMap.get("menuName") != null && !"".equals(findMap.get("menuName")) && !"null".equals(findMap.get("menuName"))){
				sql.append(" and menuName like '%").append(findMap.get("menuName").replace("'", "''")).append("%'");
			}
			
			if(findMap.get("menuUrl") != null && !"".equals(findMap.get("menuUrl")) && !"null".equals(findMap.get("menuUrl"))){
				//手机
				sql.append(" or menuUrl like '%").append(findMap.get("menuUrl").replace("'", "''")).append("%'");
			}
		}		
		sql.append(" order by menuCode asc ");
		if(page==null){
			return (List<GxMenu>)sqldao.findListBySql(sourceId, sql.toString(), GxMenu.class, closeConn, params);
		}else{
			return (List<GxMenu>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, GxMenu.class, page, params);
		}
	}

	@Override
	public GxMenu findMenu(String sourceId, boolean closeConn, String menuUUID) {
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from gx_menu where menuUUID = ?");
		params.add(menuUUID);
		return (GxMenu) sqldao.findEntityBySql(sourceId, sql.toString(), GxMenu.class, closeConn, params);
	}

	@Override
	public boolean delMenu(String sourceId, boolean closeConn, String menuUUID) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("delete from gx_menu where menuUUID = ?");
		params.add(menuUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<GxMenu> menuParentCodeAll(String sourceId, boolean closeConn, String menuParentCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from gx_menu where menuParentCode = ?");
		params.add(menuParentCode);
		return (List<GxMenu>)sqldao.findListBySql(sourceId, sql.toString(), GxMenu.class, closeConn, params);
	}

}
