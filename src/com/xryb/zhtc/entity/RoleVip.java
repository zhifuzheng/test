package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 角色会员关联表
 * @author hshzh
 *
 */

@Table(tableName="role_vip")
public class RoleVip {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="角色uuid")
	private String roleUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="会员uuid")
	private String vipUUID;
	
	@TableField(fieldSize=32,comment="公司、集团uuid")
	private String ecUUID;

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

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getEcUUID() {
		return ecUUID;
	}

	public void setEcUUID(String ecUUID) {
		this.ecUUID = ecUUID;
	}
	
	
}
