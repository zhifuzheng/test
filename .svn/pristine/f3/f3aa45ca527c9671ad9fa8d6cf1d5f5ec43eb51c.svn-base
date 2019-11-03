package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 积分规则
 * @author apple
 */
@Table(tableName="integral_rule")
public class IntegralRule {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="规则梯度的uuid")
	private String ruleUUID;
	@TableField(comment="规则类型，1-账户充值；2-购买商品")
	private Integer ruleType;
	@TableField(fieldSize=20,comment="该梯度的最小值")
	private String gradMin;
	@TableField(fieldSize=20,comment="该梯度的最大值")
	private String gradMax;
	@TableField(fieldSize=20,comment="该梯度的计算规则,购买商品按支付金额的倍数计算，账户充值则按充值金额梯度直接赠送指定积分")
	private String calculateRule;
	@TableField(comment="该梯度状态；1-启用，2-停用")
	private Integer state;
	@TableField(fieldSize=20,comment="创建时间")
	private String createTime;
	@TableField(fieldSize=20,comment="修改时间")
	private String modifiedTime;
	@TableField(fieldSize=20,comment="启用时间")
	private String startUseTime;
	@TableField(fieldSize=20,comment="停用时间")
	private String stopUseTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleUUID() {
		return ruleUUID;
	}
	public void setRuleUUID(String ruleUUID) {
		this.ruleUUID = ruleUUID;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public String getGradMin() {
		return gradMin;
	}
	public void setGradMin(String gradMin) {
		this.gradMin = gradMin;
	}
	public String getGradMax() {
		return gradMax;
	}
	public void setGradMax(String gradMax) {
		this.gradMax = gradMax;
	}
	public String getCalculateRule() {
		return calculateRule;
	}
	public void setCalculateRule(String calculateRule) {
		this.calculateRule = calculateRule;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getStartUseTime() {
		return startUseTime;
	}
	public void setStartUseTime(String startUseTime) {
		this.startUseTime = startUseTime;
	}
	public String getStopUseTime() {
		return stopUseTime;
	}
	public void setStopUseTime(String stopUseTime) {
		this.stopUseTime = stopUseTime;
	}
	
	
	
	
}
