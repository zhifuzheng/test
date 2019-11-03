package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 商品规格模板信息
 * @author wf
 */
@Table(tableName="item_model")
public class ItemModel implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -8161638433548612006L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，UUID自动生成")
	private String modelUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="所属商品分类UUID")
	private String catUUID;
	
	@TableField(fieldSize=100,comment="分类名称")
	private String catName;
	
	@TableField(fieldSize=100,comment="模板名称")
	private String modelName;
	
	@TableField(fieldSize=8000,comment="商品模板数据，json格式")
	private String modelData;
	
	@TableField(comment="排列顺序")
	private Integer sort;
	
	@TableField(fieldSize=1,comment="是否禁用：0停用，1正常")
	private String enable;
	
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

	public String getModelUUID() {
		return modelUUID;
	}

	public void setModelUUID(String modelUUID) {
		this.modelUUID = modelUUID;
	}

	public String getCatUUID() {
		return catUUID;
	}

	public void setCatUUID(String catUUID) {
		this.catUUID = catUUID;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelData() {
		return modelData;
	}

	public void setModelData(String modelData) {
		this.modelData = modelData;
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
