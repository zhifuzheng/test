package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 数据字典信息
 * @author wf
 */
@Table(tableName="base_dict")
public class BaseDict implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -4464356236645463590L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，手动生成")
	private String dictUUID;
	
	@TableField(fieldSize=30,comment="分类编号")
	private String typeCode;
	
	@TableField(fieldSize=20,comment="分类名称")
	private String typeName;
	
	@TableField(fieldSize=30,comment="字典项编号")
	private String itemCode;
	
	@TableField(fieldSize=20,comment="字典项名称")
	private String itemName;
	
	@TableField(comment="排列顺序")
	private Integer sort;
	
	@TableField(fieldSize=1,comment="是否禁用：0停用，1正常")
	private String enable;
	
	@TableField(fieldSize=200,comment="描述")
	private String description;
	
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

	public String getDictUUID() {
		return dictUUID;
	}

	public void setDictUUID(String dictUUID) {
		this.dictUUID = dictUUID;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
