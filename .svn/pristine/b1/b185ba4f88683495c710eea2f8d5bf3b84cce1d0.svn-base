package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.entity.UserInfo;

import dbengine.util.Page;

/**
 * 用户信息service接口
 * @author hshzh
 *
 */
public interface IUserService {
	/**
	 * 保存或修改用户信息
	 * @param user 
	 * @return 
	 */
	public boolean saveOrUpUser(String sourceId,boolean closeConn,UserInfo user);
	/**
	 * 查用户信息列表
	 * @param sourceId
	 * @param closeConn
	 * @param page
	 * @param findMap(查询条件)
	 * @return
	 */
	public List<UserInfo> findUserInfoList(String sourceId,boolean closeConn,Page page,Map<String,String> findMap);
	/**
	 * 查用户信息(通过账号密码等进行查询)
	 * @param sourceId
	 * @param closeConn
	 * @param ecUUID
	 * @param findMap
	 * @return
	 */
	public UserInfo findUserInfo(String sourceId,boolean closeConn,Map<String,String> findMap);
	/**
	 * 修改用户密码
	 * @param sourceId 数据源id
	 * @param closeConn 是否关闭连接
	 * @param userUUID 用户全局唯一编号
	 * @param oldPwd 老密码
	 * @param newPwd 新密码
	 * @return
	 */
	public boolean upUserPwd(String sourceId,boolean closeConn,String userUUID,String oldPwd,String newPwd);
	/**
	 * 删除用户
	 * @param sourceId
	 * @param closeConn
	 * @param userUUID
	 * @return
	 */
	public boolean deleteUser(String sourceId,boolean closeConn,String userUUID);
	
	/**
	 * 根据UUID查询用户信息
	 * @param sourceId
	 * @param closeConn
	 * @param userUUID
	 * @return
	 */
	public UserInfo findUserInfoByUUID(String sourceId,boolean closeConn,String userUUID);
	/**
	 * 修改用户状态
	 * @param sourceId
	 * @param closeConn
	 * @param ecUUID
	 * @param userUUID
	 * @param status
	 * @return
	 */
	public boolean updateUserStatus(String sourceId,boolean closeConn,String userUUID,String status);
	/**
	 * 通过userUUID查询关联的菜单信息
	 * @param sourceId
	 * @param closeConn
	 * @param ecUUID
	 * @param userUUID
	 * @param menuParentCode
	 * @return
	 */
	public List<MenuInfo> findMenuByUserUUID(String sourceId,boolean closeConn,String userUUID,String menuParentCode);
	
	/**
	 * 通过权限标识查菜单信息
	 * @param sourceId
	 * @param closeConn
	 * @param ecUUID
	 * @param menuPermission
	 * @return
	 */
	public MenuInfo findMenuByPermission(String sourceId,boolean closeConn,String menuPermission);
	
	/**
	 * 查用户菜单关联信息
	 * @param sourceId
	 * @param closeConn
	 * @param ecUUID
	 * @param userUUID
	 * @return
	 */
	public List<MenuUser> findMenuByUserUUID(String sourceId, boolean closeConn, String userUUID);
	/**
	 * 查登陆名是否存在
	 * @param sourceId
	 * @param closeConn 
	 * @param loginName 登录名
	 * @return true 存在，false 不存在
	 */
	public boolean findLoginNameExist(String sourceId,boolean closeConn, String loginName);
	/**
	 * 修改密码？手机号
	 */
	public boolean findPwd(String sourceId,boolean closeConn,String vipPwd,String loginName);
	/**
	 * 用户更新签名
	 * @param sourceId
	 * @param closeConn
	 * @param uuid
	 * @param signName
	 * @return
	 */
	public boolean updateSignName(String sourceId, boolean closeConn, String uuid, String signName);
}
