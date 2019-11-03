package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 用户积分
 * @author apple
 */
@Table(tableName="integral")
public class Integral {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="用户uuid")
	private String vipUUID;
	@TableField(comment="用户积分")
	private Integer integral;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVipUUID() {
		return vipUUID;
	}
	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	
}
