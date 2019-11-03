package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 菜单用户关联信息
 * @author hshzh
 */
@Table(tableName="menu_user")
public class MenuUser {
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="菜单uuid")
	private String menuUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="用户uuid")
	private String userUUID;
	
	@TableField(fieldSize=32,comment="ecUUID")
	private String ecUUID;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMenuUUID() {
		return menuUUID;
	}
	public void setMenuUUID(String menuUUID) {
		this.menuUUID = menuUUID;
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
