package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.annotation.Auto;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.service.IMenuInfoService;
import com.xryb.zhtc.util.CodeUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
/**
 * 菜单权限信息service
 * @author hshzh
 *
 */
public class MenuInfoServiceImpl implements IMenuInfoService{
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;
	
	/**
	 * 保存或修改菜单权限信息
	 */
	@Override
	public boolean saveOrUpMenu(String sourceId,boolean closeConn,MenuInfo mi) {
		if(mi==null){
			return false;
		}
		if(!"-1".equals(mi.getMenuParentCode())){
			//不是顶级节点，修改父节点状态为有下级节点
			this.upMenuLowerNodeState( sourceId, closeConn,null,mi.getMenuParentCode(),"1");
		}
		if(mi.getId()==null){
			return entitydao.saveEntity(sourceId,mi,closeConn);
		}else{
			return entitydao.updateEntity(sourceId,mi,closeConn);
		}
	}
    /**
     * 删除用户关联的所有菜单权限
     */
	@Override
	public boolean deleteMenuUser(String sourceId, boolean closeConn, String userUUID){
		if(userUUID == null || "null".equals(userUUID) || "".equals(userUUID)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sql.append("delete from menu_user where userUUID= ?");
		params.add(userUUID);
		return sqldao.executeSql("jdbcwrite", sql.toString(), closeConn, params);
	}
	/**
	 * 保存用户菜单关联信息
	 */
	@Override
	public boolean saveMenuUser(String sourceId,boolean closeConn,MenuUser info){
		if(info==null){
			return false;
		}
		return entitydao.saveEntity("jdbcwrite", info, closeConn);
	}
	/**
	 * 删除菜单信息
	 */
	@Override
	public boolean deleteMenu(String sourceId,boolean closeConn, String menuUUID) {
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("delete from menu_info where  menuUUID = ?");
		params.add(menuUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}
	/**
	 * 查权限菜单信息
	 */
	@Override
	public MenuInfo findMenu(String sourceId,boolean closeConn, String menuUUID) {
		if(menuUUID==null || "".equals(menuUUID) || "null".equals(menuUUID)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from menu_info where menuUUID = ?");
		params.add(menuUUID);		
		return (MenuInfo)sqldao.findEntityBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
	}
	/**
	 * 查菜单信息列表
	 */
	@Override
	public List<MenuInfo> findMenuList(String sourceId,boolean closeConn,Page page, Map<String, String> findMap) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from menu_info where 1=1"); 
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
			return (List<MenuInfo>)sqldao.findListBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
		}else{
			return (List<MenuInfo>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, MenuInfo.class, page, params);
		}
	}
    /**
     * 查下级节点的下一个菜单节点编号
     */
	@Override
	public String getMenuCodeNext(String sourceId,boolean closeConn,String menuParentCode) {
		if (menuParentCode == null || "".equals(menuParentCode) || "null".equals(menuParentCode)) {
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from menu_info t where 1=1");
		sql.append(" and t.menuParentCode = '").append(menuParentCode).append("'");
		//查该父节点的所有下级节点
		List<MenuInfo> menuList = (List<MenuInfo>) sqldao.findListBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
		String returnCode = "";
		// 生成下一个节点
		// 将同级节点保存到map,目的是从中找出没有被利用的code,避免节点被删除后编码不能再次使用的问题
		Map map = new HashMap();
		for (MenuInfo menuinfo : menuList) {
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
	/**
	 * 修改是否有下级节点状态
	 * @param ecUUID
	 * @param menuCode
	 * @param menuHaveLowerNode
	 * @return
	 */
	private boolean upMenuLowerNodeState(String sourceId,boolean closeConn,String ecUUID,String menuCode,String menuHaveLowerNode){
		if(menuCode==null || "".equals(menuCode) || "null".equals(menuCode)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("update menu_info set menuHaveLowerNode = ?");
		params.add(menuHaveLowerNode);
		sql.append(" where 1=1 ");
		sql.append(" and menuCode = ?");
		params.add(menuCode);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

}
