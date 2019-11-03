package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 用户积分详情记录
 * @author apple
 */
@Table(tableName="integral_change_record")
public class IntegralRecordOfUser {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="用户uuid")
	private String vipUUID;
	@TableField(comment="积分来源详情，1-账户充值，2-购买商品，3-积分兑换")
	private Integer integralFrom;
	@TableField(comment="充值/订单支付金额，积分商城兑换的积分")
	private String moneyOrIntegral;
	@TableField(comment="积分变化，1-赠送积分，2-消耗积分")
	private Integer integralType;
	@TableField(comment="积分变化值")
	private Integer integration;
	@TableField(fieldSize=20,comment="积分变化时间")
	private String changeTime;
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
	public Integer getIntegralFrom() {
		return integralFrom;
	}
	public void setIntegralFrom(Integer integralFrom) {
		this.integralFrom = integralFrom;
	}
	public String getMoneyOrIntegral() {
		return moneyOrIntegral;
	}
	public void setMoneyOrIntegral(String moneyOrIntegral) {
		this.moneyOrIntegral = moneyOrIntegral;
	}
	public Integer getIntegralType() {
		return integralType;
	}
	public void setIntegralType(Integer integralType) {
		this.integralType = integralType;
	}
	public Integer getIntegration() {
		return integration;
	}
	public void setIntegration(Integer integration) {
		this.integration = integration;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}
	
	
	
}
