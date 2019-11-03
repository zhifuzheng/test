package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 商品分类信息
 * @author wf
 */
@Table(tableName="item_cat")
public class ItemCat implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -7879597829363319714L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，UUID自动生成")
	private String catUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="父节点UUID，-1代表顶级节点")
	private String parentUUID;
	
	@TableField(fieldSize=400,comment="节点编号")
	private String catCode;
	
	@TableField(isNotNull=true,fieldSize=1,comment="是否是父节点：0叶子节点，1是，默认值是0")
	private String isParent = "0";
	
	@TableField(fieldSize=100,comment="分类名称")
	private String catName;
	
	@TableField(comment="排列顺序")
	private Integer sort;
	
	@TableField(fieldSize=1,comment="是否禁用：0停用，1正常，默认值是1")
	private String enable = "1";
	
	@TableField(fieldSize=30,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatUUID() {
		return catUUID;
	}

	public void setCatUUID(String catUUID) {
		this.catUUID = catUUID;
	}

	public String getParentUUID() {
		return parentUUID;
	}

	public void setParentUUID(String parentUUID) {
		this.parentUUID = parentUUID;
	}

	public String getCatCode() {
		return catCode;
	}

	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
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

}
