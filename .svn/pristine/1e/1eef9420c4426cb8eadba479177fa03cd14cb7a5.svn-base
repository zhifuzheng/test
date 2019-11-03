package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;


/**
 * 角色信息
 * @author hshzh
 *
 */
@Table(tableName="role_info")
public class RoleInfo {
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="角色UUID")
    private String roleUUID;
	
	@TableField(fieldSize=100,isNotNull=true,comment="角色名称")
    private String roleName;
	
	@TableField(fieldSize=500,comment="角色备注")
    private String roleMark;
	
	@TableField(fieldSize=32,comment="ecUUID")
    private String ecUUID;
	
	@TableField(fieldSize=20,comment="创建时间,以字符串方式存储,格式为：yyyy-MM-dd HH:mm:ss")
    private String createTime;
	
	@TableField(fieldSize=32,comment="创建人UUID")
    private String createUserUUID;
	
	@TableField(fieldSize=100,comment="创建人名称")
    private String createUserName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleUUID() {
		return roleUUID;
	}
	public void setRoleUUID(String roleUUID) {
		this.roleUUID = roleUUID;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleMark() {
		return roleMark;
	}
	public void setRoleMark(String roleMark) {
		this.roleMark = roleMark;
	}
	public String getEcUUID() {
		return ecUUID;
	}
	public void setEcUUID(String ecUUID) {
		this.ecUUID = ecUUID;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserUUID() {
		return createUserUUID;
	}
	public void setCreateUserUUID(String createUserUUID) {
		this.createUserUUID = createUserUUID;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
    
}
