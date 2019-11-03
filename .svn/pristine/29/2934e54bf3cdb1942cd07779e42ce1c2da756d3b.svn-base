package com.xryb.zhtc.entity;

import java.io.Serializable;
import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 用户信息实体类，实现序列化接口
 * @author hshzh
 */
@Table(tableName = "user_info")//表名
public class UserInfo implements Serializable{
	/**
	 * isPKey:主键设置true/false
	 * isAutoIncrement:自动增长设置true/false
	 * isNotNull:设置是否不为null，true/false
	 * fieldSize:设置字段大小
	 * comment:注释
	 */
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号,UUID自动生成")
	private String userUUID;
	
	@TableField(isNotNull=true,fieldSize=30,comment="登陆名，长度不超过30字符")
	private String loginName;
	
	@TableField(isNotNull=true,fieldSize=36,comment="登陆密码，md5加密")
	private String userPwd;
	
	@TableField(fieldSize=32,comment="归属单位统一编号,UUID自动生成")
	private String ecUUID;
	
	@TableField(fieldSize=100,comment="用户姓名")
	private String userName;
	
	@TableField(fieldSize=2,isNotNull=true,comment="用户类型（0-超级管理员， 1-般管理员， 2-普通用户）")
	private String userType;
	
	@TableField(fieldSize=11,comment="用户手机,11位标准手机长度")
	private String userMobile;
	
	@TableField(fieldSize=100,comment="办公电话,可允许有多个电话")
	private String userTel;
	
	@TableField(fieldSize=100,comment="邮箱")
	private String userEmail;
	
	@TableField(fieldSize=50,comment="昵称")
	private String nickName;
	
	@TableField(fieldSize=1,comment="用户性别（1-男 2-女 3-其他：未知）")
	private String userSex;
	
	@TableField(fieldSize=2,comment="状态：1-正常启用,2-注销, 3-停用")
	private String userStatus;
	
	@TableField(fieldSize=30,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;
	
	@TableField(fieldSize=50,comment="姓名简拼")
	private String simpleSpelling;
	
	@TableField(fieldSize=100,comment="姓名的全拼")
	private String fullSpelling;
	
	@TableField(fieldSize=500,comment="组合查询(手机/姓名/姓名简拼/姓名全拼)")
	private String queryCombination;
	
	@TableField(fieldSize=200,comment="用户头像,存放相对地址")
	private String userPic;
	
	@TableField(fieldSize=500,comment="备注，描述")
	private String userDescription;
	
	@TableField(fieldSize=300,comment="登陆时间")
	private String loginTime;
	
	@TableField(fieldSize=10,comment="登陆次数")
	private int loginNum = 0;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getEcUUID() {
		return ecUUID;
	}
	public void setEcUUID(String ecUUID) {
		this.ecUUID = ecUUID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getSimpleSpelling() {
		return simpleSpelling;
	}
	public void setSimpleSpelling(String simpleSpelling) {
		this.simpleSpelling = simpleSpelling;
	}
	public String getFullSpelling() {
		return fullSpelling;
	}
	public void setFullSpelling(String fullSpelling) {
		this.fullSpelling = fullSpelling;
	}
	public String getQueryCombination() {
		return queryCombination;
	}
	public void setQueryCombination(String queryCombination) {
		this.queryCombination = queryCombination;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public String getUserDescription() {
		return userDescription;
	}
	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public int getLoginNum() {
		return loginNum;
	}
	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
	}
	
}
