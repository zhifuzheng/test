package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 角色用户关联信息
 * @author hshzh
 * 
 */
@Table(tableName="role_user")
public class RoleUser {
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="uuid，全球唯一标识，不能为空")
	private String ruUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="角色uuid")
	private String roleUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="用户uuid")
	private String userUUID;
	
	@TableField(fieldSize=32,comment="单位UUID")
	private String ecUUID;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuUUID() {
		return ruUUID;
	}
	public void setRuUUID(String ruUUID) {
		this.ruUUID = ruUUID;
	}
	public String getRoleUUID() {
		return roleUUID;
	}
	public void setRoleUUID(String roleUUID) {
		this.roleUUID = roleUUID;
	}
	public String getUserUUID() {
		return userUUID;
	}
	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}
	public String getEcUUID() {
		return ecUUID;
	}
	public void setEcUUID(String ecUUID) {
		this.ecUUID = ecUUID;
	}
	
}
