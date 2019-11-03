package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;
import spark.utils.ObjectUtil;

import com.xryb.zhtc.entity.RoleInfo;
import com.xryb.zhtc.entity.RoleUser;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.service.IRoleService;
import com.xryb.zhtc.service.IRoleUserService;
import com.xryb.zhtc.service.IUserService;
import com.xryb.zhtc.service.impl.RoleServiceImpl;
import com.xryb.zhtc.service.impl.RoleUserServiceImpl;
import com.xryb.zhtc.service.impl.UserServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.TreeBean;

import dbengine.util.Page;

/**
 * 角色管理
 * @author hshzh
 *
 */
public class RoleMng {
	@Auto(name=RoleServiceImpl.class)
	private IRoleService roleService;
	
	@Auto(name=RoleUserServiceImpl.class)
	private IRoleUserService roleUserService;
	
	@Auto(name=UserServiceImpl.class)
	private IUserService userService;
	/**
	 * 保存或修改角色信息
	 */
	public boolean saveOrUpRole(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		RoleInfo role = (RoleInfo)ObjectUtil.req2Obj(request, RoleInfo.class);
		if(role==null){
			return false;
		}
		if(role.getId()==null){
			//新增，设置UUID
			role.setRoleUUID(UUID.randomUUID().toString().replaceAll("-", ""));//UUID
			role.setCreateTime(DateTimeUtil.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		return roleService.saveOrUpRole(sourceId, closeConn, role);
	}
	/**
	 * 删除角色信息
	 */	
	public boolean deleteRole(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleUUID = request.getParameter("roleUUID");
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return false;
		}
		return roleService.deleteRole(sourceId, closeConn, roleUUID);
	}
	/**
	 * 查角色信息
	 */
	public String findRole(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleUUID = request.getParameter("roleUUID");
		if(roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return "[]";
		}
		RoleInfo role = (RoleInfo)roleService.findRole(sourceId, closeConn, roleUUID);
		if(role==null){
			return "[]";
		}
		return JsonUtil.ObjToJson(role);
	}
	/**
	 * 查角色信息列表
	 */
	public String findRoleList(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Page page = new Page();
		page.setPageRecord(Integer.parseInt(request.getParameter("rows")));
		page.setPage(Integer.parseInt(request.getParameter("page")));		// 根据条件查询用户列表数据
		List<RoleInfo> roleList = (List<RoleInfo>)roleService.findRoleList(sourceId, closeConn, page);
		Map map = new HashMap();
		map.put("total", page.getTotalRecord());
		map.put("rows", roleList);
		if(roleList==null){
			return "[]";
		}
		return JsonUtil.ObjToJson(map);
	}	
	/**
	 * 保存角色用户关联关系
	 */
	public boolean saveRoleUser(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
//		if(userinfo==null){
//			return false;
//		}
		String userUUIDS = request.getParameter("userUUIDS");
		String roleUUIDS = request.getParameter("roleUUIDS");
		String operationName = request.getParameter("operationName");
		if(userUUIDS == null || "".equals(userUUIDS) || "null".equals(userUUIDS) || roleUUIDS==null || "".equals(roleUUIDS) || "null".equals(roleUUIDS)){
			return false;
		}
		String[] userUUIDArr = userUUIDS.split(",");
		String[] roleUUIDArr = roleUUIDS.split(",");
		List<RoleUser> ruList = new ArrayList<RoleUser>();
		for(int i=0; i<userUUIDArr.length;i++){
			for(int j=0;j<roleUUIDArr.length;j++){
				RoleUser ru = new RoleUser();
				ru.setRuUUID(UUID.randomUUID().toString().replace("-",""));
				ru.setRoleUUID(roleUUIDArr[j]);
				ru.setUserUUID(userUUIDArr[i]);
				ruList.add(ru);
			}
		}
		if("role".equals(operationName)) {
			//删除指定角色对应的用户后再重新添加
			for(int j=0;j<roleUUIDArr.length;j++){
				roleUserService.deleteRoleUser(sourceId, closeConn, roleUUIDArr[j], null);
			}
		}else {
			//删除指定用户对应的角色后再重新添加
			for(int i=0; i<userUUIDArr.length;i++){
				roleUserService.deleteRoleUser(sourceId, closeConn, null, userUUIDArr[i]);
			}
		}
		return roleUserService.saveRoleUser(sourceId, closeConn, ruList);
	}
	/**
	 * 删除角色用户关联关系
	 */
	public boolean deleteRoleUser(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(userinfo==null){
			return false;
		}
		String roleUUID = request.getParameter("roleUUID");
		String userUUID = request.getParameter("userUUID");
		if(userUUID == null || "".equals(userUUID) || "null".equals(userUUID) || roleUUID==null || "".equals(roleUUID) || "null".equals(roleUUID)){
			return false;
		}
		return roleUserService.deleteRoleUser(sourceId, closeConn, roleUUID, userUUID);
	}	
	/**
	 * 通过角色UUID查关联的用户信息列表
	 */
	public String findUserByRoleUUID(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleUUID = request.getParameter("roleUUID");
		List<UserInfo> userListAll = (List<UserInfo>)userService.findUserInfoList(sourceId, closeConn, null, null);
		List<UserInfo> userList = (List<UserInfo>)roleUserService.findUserByRoleUUID(sourceId, closeConn, roleUUID);
		if(userListAll==null){
			return "[]";
		}else{
			List<Map> mapList = new ArrayList<Map>();
			for(UserInfo user:userListAll){
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put("id", user.getId());
				attributes.put("text",user.getUserName());
				attributes.put("pId", -1);
				attributes.put("userUUID", user.getUserUUID());
				attributes.put("state", "open");// 默认打开树
				//attributes.put("nocheck", false);// 是否没有选择框
				attributes.put("checked", false);// 是否选中
				if(userList!=null){
					for(UserInfo ui :userList){
						if(ui.getUserUUID().equals(user.getUserUUID())){
							attributes.put("checked", true);// 是否选中
						}
					}
				}
				mapList.add(attributes);
			}
			return JsonUtil.ObjToJson(mapList);
		}
	}
	/**
	 * 通过用户UUID查关联的角色信息列表
	 */
	public String findRoleByUserUUID(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userUUID = request.getParameter("userUUID");
//		if(userinfo==null){
//			return "[]";
//		}
		List<RoleInfo> roleListAll = (List<RoleInfo>)roleService.findRoleList(sourceId, closeConn,null);
		List<RoleInfo> roleList = (List<RoleInfo>)roleUserService.findRoleByUserUUID(sourceId, closeConn, userUUID);
		List<TreeBean> trList = new ArrayList<TreeBean>();	
		if(roleListAll==null){
			return "[]";
		}else{
			for(RoleInfo rinfo:roleListAll){
				TreeBean tb = new TreeBean();
				tb.setId(rinfo.getRoleUUID());
				tb.setpId("-1");
				tb.setText(rinfo.getRoleName());
				tb.setState("open");
				tb.setChecked(false);
				if(roleList!=null){
					for(RoleInfo ri :roleList){
						if(ri.getRoleUUID().equals(rinfo.getRoleUUID())){
							tb.setChecked(true);
						}
					}
				}
				trList.add(tb);
			}
			return JsonUtil.ObjToJson(trList);
		}
	}
	/**
	 * 通过用户UUID查未关联的角色信息列表
	 */
	public String findNotRoleByUserUUID(String sourceId, boolean closeConn,UserInfo userinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userUUID = request.getParameter("userUUID");
		if(userinfo==null || userUUID==null || "".equals(userUUID) || "null".equals(userUUID)){
			return "[]";
		}
		List<RoleInfo> roleList = (List<RoleInfo>)roleUserService.findNotRoleByUserUUID(sourceId, closeConn, userUUID);
		List<TreeBean> trList = new ArrayList<TreeBean>();	
		if(roleList==null){
			return "[]";
		}else{
			for(RoleInfo rinfo:roleList){
				TreeBean tb = new TreeBean();
				tb.setId(rinfo.getRoleUUID());
				tb.setpId("-1");
				tb.setText(rinfo.getRoleName());
				tb.setState("open");
				trList.add(tb);
			}
			return JsonUtil.ObjToJson(trList);
		}
	}
}
