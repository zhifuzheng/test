package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 菜单信息
 * @author hshzh
 */
@Table(tableName="menu_info")
public class MenuInfo {
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String menuUUID;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单编码(每两位为一组)")
	private String menuCode;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单父节点编码（主要为构建菜单树使用）")
	private String menuParentCode;
	
	@TableField(fieldSize=50,comment="菜单权限标识(如‘system.role’表示系统级角色权限，‘system.user’用户权限)")
	private String menuPermission;
	
	@TableField(fieldSize=100,comment="菜单名称")
	private String menuName;
	
	@TableField(fieldSize=200,comment="菜单连接地址（无连接地址用#号表示）")
	private String menuUrl;
	
	@TableField(fieldSize=2,comment="菜单类型（0顶级菜单，1级菜单，2二级菜单,3三级菜单 ）")
	private String menuType;
	
	@TableField(fieldSize=1,comment="菜单是否显示(0不显示，1显示）")
	private String isShow;
	
	@TableField(fieldSize=1,comment="是否有下级节点(1有,0没有)")
    private String menuHaveLowerNode;
	
	@TableField(fieldSize=32,comment="单位UUID")
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
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuParentCode() {
		return menuParentCode;
	}
	public void setMenuParentCode(String menuParentCode) {
		this.menuParentCode = menuParentCode;
	}
	public String getMenuPermission() {
		return menuPermission;
	}
	public void setMenuPermission(String menuPermission) {
		this.menuPermission = menuPermission;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getMenuHaveLowerNode() {
		return menuHaveLowerNode;
	}
	public void setMenuHaveLowerNode(String menuHaveLowerNode) {
		this.menuHaveLowerNode = menuHaveLowerNode;
	}
	public String getEcUUID() {
		return ecUUID;
	}
	public void setEcUUID(String ecUUID) {
		this.ecUUID = ecUUID;
	}
	
}
