package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.RoleUser;
import com.xryb.zhtc.service.IRoleUserService;
import com.xryb.zhtc.service.impl.RoleUserServiceImpl;
import com.xryb.zhtc.util.JsonUtil;
/**
 * 角色和用户关联管理
 * @author chengyz
 *
 */
public class RoleUserMng {

	@Auto(name=RoleUserServiceImpl.class)
	private IRoleUserService roleUserService;
	/**
	 * 给用户分配角色
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String saveRoleUser(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String userUUID = request.getParameter("userUUID");
		String roleUUIDS = request.getParameter("roleUUIDS");//选择的菜单参数
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtils.isNullOrEmpty(userUUID) && !StringUtils.isNullOrEmpty(roleUUIDS)) {
			roleUserService.deleteRoleUser(sourceId, closeConn, null, userUUID);
			String[] roleUUIDArray = roleUUIDS.split(",");
			List<RoleUser> roleUserList =  new ArrayList<RoleUser>();
			for (String roleUUID : roleUUIDArray) {
				RoleUser roleUser = new RoleUser();
				roleUser.setRuUUID(UUID.randomUUID().toString().replaceAll("-", ""));
				roleUser.setRoleUUID(roleUUID.replaceAll("-", ""));
				roleUser.setUserUUID(userUUID.replaceAll("-", ""));
				roleUserList.add(roleUser);
			}
			if (roleUserService.saveRoleUser(sourceId, closeConn, roleUserList)) {
				map.put("status", "1");
				map.put("msg", "角色分配成功");
				return JsonUtil.ObjToJson(map);
			}else{
				map.put("status", "0");
				map.put("msg", "角色分配失败");
				return JsonUtil.ObjToJson(map);
			}
		}else{
			map.put("status", "0");
			map.put("msg", "角色分配失败");
			return JsonUtil.ObjToJson(map);
		}

	}
	/**
	 * 删除用户角色关联管理
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String delRoleUser(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String userUUID = request.getParameter("userUUID");
		String roleUUID = request.getParameter("roleUUID");
		Map<String, String> map = new HashMap<String, String>();
		if (!StringUtils.isNullOrEmpty(userUUID) || !StringUtils.isNullOrEmpty(roleUUID)){
			if (roleUserService.deleteRoleUser(sourceId, closeConn, roleUUID, userUUID)){
				map.put("status", "1");
				map.put("msg", "成功！");
				return JsonUtil.ObjToJson(map);
			}else{
				map.put("status", "0");
				map.put("msg", "失败！");
				return JsonUtil.ObjToJson(map);
			}
		}else{
			map.put("status", "0");
			map.put("msg", "失败！");
			return JsonUtil.ObjToJson(map);
		}
	}
	/**
	 * 查询用户所拥有的角色
	 * @param sourceId
	 * @param closeConn
	 * @param request
	 * @param response
	 * @return
	 */
	public String findRoleToAssignRole(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response){
		String userUUID = request.getParameter("userUUID");
		if (!StringUtils.isNullOrEmpty(userUUID)){
			List<Map> listMap = roleUserService.findRoleToAssignRole(sourceId, closeConn, userUUID);
			if (listMap!=null){
				return JsonUtil.ObjToJson(listMap);
			}else{
				return JsonUtil.ObjToJson("[]");
			}
		}else{
			return JsonUtil.ObjToJson("[]");
		}
	}
}
