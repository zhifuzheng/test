package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 分销设置
 * @author wf
 */
@Table(tableName="distribute_setting")
public class DisSetting implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 8013990908381202653L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true, fieldSize=32, comment="全局唯一编号，手动生成")
	private String disUUID;
	
	@TableField(fieldSize=1,comment="开启分销：0关闭，1开启，默认值为1")
	private String isDis = "1";
	
	@TableField(fieldSize=8000,comment="商品简介")
	private String rules = "分销规则（待完成）";
	
	@TableField(fieldSize=20,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=20,comment="修改时间")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisUUID() {
		return disUUID;
	}

	public void setDisUUID(String disUUID) {
		this.disUUID = disUUID;
	}

	public String getIsDis() {
		return isDis;
	}

	public void setIsDis(String isDis) {
		this.isDis = isDis;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
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
