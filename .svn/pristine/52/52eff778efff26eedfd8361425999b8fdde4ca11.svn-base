package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 商家等级分类
 * @author Administrator
 *
 */

@Table(tableName = "business_grade")
public class BusinessGrade {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String gradeUUID;

	@TableField(isNotNull=true,fieldSize=200,comment="分类名称")
	private String gradeName;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;
	
	@TableField(isNotNull=true,fieldSize=200,comment="编号根据编号排序")
	private String number;
	
	@TableField(isNotNull=true,fieldSize=200,comment="别名")
	private String aliasName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGradeUUID() {
		return gradeUUID;
	}

	public void setGradeUUID(String gradeUUID) {
		this.gradeUUID = gradeUUID;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	

}
