package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import spark.annotation.Auto;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.MenuUser;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.service.IUserService;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.MD5;
import com.xryb.zhtc.util.PinyinUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;

/**
 * 用户信息service实现类
 * @author hshzh
 *
 */
public class UserServiceImpl implements IUserService{
	/**
	 *  注入dao
	 */
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;
	
	/**
	 * 保存或修改用户信息
	 */
	@Override
	public boolean saveOrUpUser(String sourceId, boolean closeConn, UserInfo user) {
		//判断user是否为null
		if(user==null){
			return false;
		}
		//判断id是否为null
		if(user.getId()==null){//是
			//新增
			user.setCreateTime(DateTimeUtil.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			user.setUserStatus("1");//状态为正常启用
			//设置真实姓名的拼音
			user.setFullSpelling(PinyinUtil.getPinYin(user.getUserName()));//全拼
			user.setSimpleSpelling(PinyinUtil.converterToFirstSpell(user.getUserName()));//简拼
			user.setQueryCombination(user.getUserMobile()+"/"+user.getUserName()+"/"+user.getSimpleSpelling()+"/"+user.getFullSpelling());//名称组合(手机/姓名/姓名简拼/姓名的全拼)
			if(user.getUserPwd() == null || "".equals(user.getUserPwd())) {
				//用户没有设置密码，则设置默认密码为6个0并进行密码加密
				user.setUserPwd((new MD5()).getMD5ofStr("000000"));//密码默认6个0
			}else{
				//将密码进行加密并保存
				user.setUserPwd((new MD5()).getMD5ofStr(user.getUserPwd()));//新密码md5加密
			}
			return entitydao.saveEntity(sourceId,user,false);
		}else{
			//修改
			user.setUpdateTime(DateTimeUtil.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));//修改时间
			//设置真实姓名的拼音
			user.setFullSpelling(PinyinUtil.getPinYin(user.getUserName()));//全拼
			user.setSimpleSpelling(PinyinUtil.converterToFirstSpell(user.getUserName()));//简拼
			user.setQueryCombination(user.getUserMobile()+"/"+user.getUserName()+"/"+user.getSimpleSpelling()+"/"+user.getFullSpelling());//名称组合(手机/姓名/姓名简拼/姓名的全拼)
			if(user.getUserPwd().length() < 32){
				user.setUserPwd((new MD5()).getMD5ofStr(user.getUserPwd()));
			}
			return entitydao.updateEntity(sourceId,user,closeConn);
		}
	}

	/**
	 * 查询用户信息列表
	 */
	@Override
	public List<UserInfo> findUserInfoList(String sourceId,boolean closeConn,Page page, Map<String, String> findMap) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
//		sql.append("select * from user_info where ecUUID = ? "); 
//		params.add(ecUUID);
		sql.append("select * from user_info where 1=1 "); 
		if(findMap!=null){
			if(findMap.get("userPwd") != null && !"".equals(findMap.get("userPwd")) && !"null".equals(findMap.get("userPwd"))){
				//密码
				sql.append(" or userPwd = ?");
				params.add(findMap.get("userPwd"));
			}
			if(findMap.get("userName") != null && !"".equals(findMap.get("userName")) && !"null".equals(findMap.get("userName"))){
				//真实用户名
				sql.append(" or userName like '%").append(findMap.get("userName").replace("'", "")).append("%'");
			}
			if(findMap.get("userType") != null && !"".equals(findMap.get("userType")) && !"null".equals(findMap.get("userType"))){
				//用户类型
				sql.append(" or userType = ? ");
				params.add(findMap.get("userType"));
			}
			if(findMap.get("nickName") != null && !"".equals(findMap.get("nickName")) && !"null".equals(findMap.get("nickName"))){
				//昵称
				sql.append(" or nickName like '%").append(findMap.get("nickName").replace("'", "")).append("%'");
			}
			if(findMap.get("emSex") != null && !"".equals(findMap.get("emSex")) && !"null".equals(findMap.get("emSex"))){
				//性别
				sql.append(" or emSex = ?");
				params.add(findMap.get("emSex"));
			}
			if(findMap.get("status") != null && !"".equals(findMap.get("status")) && !"null".equals(findMap.get("status"))){
				//状态
				sql.append(" or status = ?");
				params.add(findMap.get("status"));
			}
			if(findMap.get("createTime") != null && !"".equals(findMap.get("createTime")) && !"null".equals(findMap.get("createTime")) && findMap.get("createTime").indexOf("|")>0){
				//创建时间(格式 begTime|endTime)
				sql.append(" or createTime >= ?");
				params.add(findMap.get("createTime").split("\\|")[0]);
				sql.append(" and createTime <= ?");
				params.add(findMap.get("createTime").split("\\|")[1]);
			}
			if(findMap.get("combination") != null && !"".equals(findMap.get("combination")) && !"null".equals(findMap.get("combination"))){
				sql.append(" or combination like '%").append(findMap.get("combination").replace("'", "")).append("%'");
			}
			
			if(findMap.get("loginName") != null && !"".equals(findMap.get("loginName")) && !"null".equals(findMap.get("loginName"))){
				//登陆名
				sql.append(" and loginName like '%").append(findMap.get("loginName").replace("'", "")).append("%'");
			}
			if(findMap.get("orgUUID") != null && !"".equals(findMap.get("orgUUID")) && !"null".equals(findMap.get("orgUUID"))){
				//部门uuid
				sql.append(" and orgUUID ='").append(findMap.get("orgUUID").replace("'", "")).append("'");
			}
			if(findMap.get("mobilePhone") != null && !"".equals(findMap.get("mobilePhone")) && !"null".equals(findMap.get("mobilePhone"))){
				//手机
				sql.append(" or mobilePhone like '%").append(findMap.get("mobilePhone").replace("'", "")).append("%'");
			}
		}
		sql.append(" order by userType asc ");
		if(page==null){
			return (List<UserInfo>)sqldao.findListBySql(sourceId, sql.toString(), UserInfo.class, closeConn, params);
		}else{
			return (List<UserInfo>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, UserInfo.class, page, params);
		}
	}

	/**
	 * 查询用户信息(通过账号密码等进行查询)
	 */
	@Override
	public UserInfo findUserInfo(String sourceId,boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from user_info where 1=1");
		if(findMap!=null){
			if(findMap.get("userUUID")!=null && !"".equals(findMap.get("userUUID")) && !"null".equals(findMap.get("userUUID"))){
				//UUID唯一编号
				sql.append(" and userUUID = ?");
				params.add(findMap.get("userUUID"));
			}
			if(findMap.get("loginName") != null && !"".equals(findMap.get("loginName")) && !"null".equals(findMap.get("loginName"))){
				sql.append(" and loginName = ?");
				params.add(findMap.get("loginName"));
			}
			if(findMap.get("userPwd") != null && !"".equals(findMap.get("userPwd")) && !"null".equals(findMap.get("userPwd"))){
				sql.append(" and userPwd = ?");
				params.add(findMap.get("userPwd"));
			}
		}
	   return (UserInfo)sqldao.findEntityBySql(sourceId, sql.toString(), UserInfo.class, closeConn, params);
	}

	/**
	 * 修改用户密码（根据用户的UUID和密码来修改）
	 */
	@Override
	public boolean upUserPwd(String sourceId,boolean closeConn,String userUUID, String oldPwd, String newPwd) {
		//判断用户的userUUID和输入的新密码是否为空
		if(userUUID==null || "".equals(userUUID) || newPwd==null || "".equals(newPwd)){
			//为空，返回false，表示修改失败
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();//创建参数集合
		sql.append("update user_info set userPwd = ? ");
		params.add(newPwd);
		sql.append(" where userUUID = ?");
		params.add(userUUID);
		//判断传过来的密码（加密后的）是否不为null，不为空，不为"null"
		if(oldPwd!=null && !"".equals(oldPwd) && !"null".equals(oldPwd)){
			sql.append(" and userPwd = ? ");
			params.add(oldPwd);
		}
		//返回false则表示用户名或密码错误
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	/**
	 * 删除用户
	 */
	@Override
	public boolean deleteUser(String sourceId,boolean closeConn,String userUUID) {
		if(userUUID==null || "".equals(userUUID)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("delete from user_info where userUUID = ?");
		params.add(userUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	/**
	 * 根据UUID查询用户信息
	 */
	@Override
	public UserInfo findUserInfoByUUID(String sourceId,boolean closeConn,String userUUID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from user_info where userUUID = '")
		   .append(userUUID).append("'");
		return (UserInfo)sqldao.findEntityBySql(sourceId, sql.toString(), UserInfo.class, closeConn, null);
	}
	
	/**
	 * 修改用户状态
	 */
	@Override
	public boolean updateUserStatus(String sourceId,boolean closeConn, String userUUID, String status) {
		if(userUUID==null || "".equals(userUUID) || "null".equals(userUUID) || status==null || "".equals(status) || "null".equals(status)){
			return false;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("update user_info set userStatus = ? ");
		params.add(status);
		sql.append(" where userUUID = ?");
		params.add(userUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	/**
	 * 通过userUUID查关联的菜单信息
	 */
	@Override
	public List<MenuInfo> findMenuByUserUUID(String sourceId,boolean closeConn, String userUUID, String menuParentCode) {
		StringBuilder sql = new StringBuilder();//创建sql字符串对象
		List<Object> params = new ArrayList<Object>(); //创建参数集合
		sql.append("select * from menu_info m where 1 = 1");
		if(userUUID != null && !"".equals(userUUID) && !"null".equals(userUUID)){
			sql.append(" and (m.menuUUID in (");
				sql.append("select mr.menuUUID from menu_role mr where mr.roleUUID in (");
					sql.append("select roleUUID from role_user ru where ru.userUUID = ?");
					params.add(userUUID);
				sql.append(")");
			sql.append(")");				
				sql.append("or m.menuUUID in(select mu.menuUUID from menu_user mu where mu.userUUID=?");
				params.add(userUUID);
				sql.append(")");
			sql.append(")");
		}
		if(menuParentCode != null && !"".equals(menuParentCode) && !"null".equals(menuParentCode)){
			sql.append(" and menuParentCode like '").append(menuParentCode).append("%'");
		}
		return sqldao.findListBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
	}
	
	/**
	 * 通过userUUID查关联的菜单信息
	 */
	@Override
	public List<MenuUser> findMenuByUserUUID(String sourceId,boolean closeConn, String userUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>(); 
		sql.append("select * from menu_user m where 1 = 1 and userUUID = ?");
		params.add(userUUID);
		return sqldao.findListBySql(sourceId, sql.toString(), MenuUser.class, closeConn, params);
	}

	/**
	 * 通过权限标识查询菜单信息
	 */
	@Override
	public MenuInfo findMenuByPermission(String sourceId,boolean closeConn, String menuPermission) {
		if(menuPermission==null || "".equals(menuPermission) || "null".equals(menuPermission)){
			return null;
		}
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("select * from menu_info where 1 = 1");
		sql.append(" and menuPermission = ?");
		params.add(menuPermission);
		return (MenuInfo)sqldao.findEntityBySql(sourceId, sql.toString(), MenuInfo.class, closeConn, params);
	}

	/**
	 * 查询登陆账号是否存在
	 */
	@Override
	public boolean findLoginNameExist(String sourceId, boolean closeConn, String loginName) {
		//判断用户登陆名是否为空
		if(loginName==null || "".equals(loginName)){
			//是
			return false;//参数错误
		}
		//创建根据登录名查询用户数据的sql语句，并执行
		String sql = "select * from user_info where loginName='"+loginName.replace("'", "''")+"'";
		Object user = sqldao.findEntityBySql(sourceId, sql, UserInfo.class, closeConn, null);
		//判断是否查询到数据
		if(user==null){
			//未查询到数据，说明登录名不存在
			return false;
		}else{
			user=null;
			return true;
		}
	}
	
	/**
	 * 手机找回密码
	 */
	@Override
	public boolean findPwd(String sourceId, boolean closeConn, String vipPwd,String loginName) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("update vip_info set vipPwd = ? ");
		params.add(vipPwd);
		sql.append(" where loginName = ?");
		params.add(loginName);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}
	/**
	 * 用户更新签名
	 */
	@Override
	public boolean updateSignName(String sourceId, boolean closeConn, String uuid, String signName) {
		StringBuilder sql = new StringBuilder();
		sql.append("update user_info set signName = '").append(signName).append("'");
		sql.append(" where userUUID = '").append(uuid).append("'");
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, null);
	}

}
