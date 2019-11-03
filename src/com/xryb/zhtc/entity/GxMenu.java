package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需发布商品分类
 * @author Administrator
 *
 */

@Table(tableName="gx_menu")
public class GxMenu {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String menuUUID;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单编码(每两位为一组)")
	private String menuCode;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单父节点编码（主要为构建菜单树使用）")
	private String menuParentCode;
	
	@TableField(fieldSize=100,comment="菜单名称")
	private String menuName;
	
	@TableField(fieldSize=2,comment="菜单类型（0顶级菜单，1级菜单，2二级菜单,3三级菜单 ）")
	private String menuType;
	
	@TableField(fieldSize=1,comment="菜单是否显示(0不显示，1显示）")
	private String isShow;
	
	@TableField(fieldSize=1,comment="是否有下级节点(1有,0没有)")
    private String menuHaveLowerNode;
	
	@TableField(fieldSize=100,comment="备注")
	private String menuMark;

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

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
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

	public String getMenuMark() {
		return menuMark;
	}

	public void setMenuMark(String menuMark) {
		this.menuMark = menuMark;
	}
	
	

}
