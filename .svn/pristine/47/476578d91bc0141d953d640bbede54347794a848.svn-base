package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 商家等级中间表
 * @author Administrator
 *
 */

@Table(tableName = "business_middle")
public class BusinessMiddle {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String middleUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="供应商UUID")
	private String supplierUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="零售商UUID")
	private String retailerUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="等级编号")
	private String gradeNumber;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMiddleUUID() {
		return middleUUID;
	}

	public void setMiddleUUID(String middleUUID) {
		this.middleUUID = middleUUID;
	}

	public String getSupplierUUID() {
		return supplierUUID;
	}

	public void setSupplierUUID(String supplierUUID) {
		this.supplierUUID = supplierUUID;
	}

	public String getRetailerUUID() {
		return retailerUUID;
	}

	public void setRetailerUUID(String retailerUUID) {
		this.retailerUUID = retailerUUID;
	}
	

	public String getGradeNumber() {
		return gradeNumber;
	}

	public void setGradeNumber(String gradeNumber) {
		this.gradeNumber = gradeNumber;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
