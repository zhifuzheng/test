package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 提现设置
 * @author wf
 */
@Table(tableName="withdrawal_setting")
public class WitSetting implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -5180571246435617355L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true, fieldSize=32, comment="全局唯一编号，手动生成")
	private String witUUID;
	
	@TableField(fieldSize=1,comment="商户钱包提现方式：1提现到银行卡，2提现到微信，3都支持，默认值为1")
	private String busChannel = "1";
	
	@TableField(fieldSize=20,comment="商户钱包提现最低金额，默认值为100分")
	private Integer busMin = 100;
	
	@TableField(comment="商户钱包提现手续费，默认值为0")
	private Double busRatio = 0d;
	
	@TableField(fieldSize=1,comment="分销商佣金提现方式：1提现到银行卡，2提现到微信，3都支持，默认值为1")
	private String disChannel = "1";
	
	@TableField(fieldSize=20,comment="分销商佣金提现最低金额，默认值为100分")
	private Integer disMin = 100;
	
	@TableField(comment="分销商佣金提现手续费，默认值为0")
	private Double disRatio =0d;
	
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

	public String getWitUUID() {
		return witUUID;
	}

	public void setWitUUID(String witUUID) {
		this.witUUID = witUUID;
	}

	public String getBusChannel() {
		return busChannel;
	}

	public void setBusChannel(String busChannel) {
		this.busChannel = busChannel;
	}

	public Integer getBusMin() {
		return busMin;
	}

	public void setBusMin(Integer busMin) {
		this.busMin = busMin;
	}

	public Double getBusRatio() {
		return busRatio;
	}

	public void setBusRatio(Double busRatio) {
		this.busRatio = busRatio;
	}

	public String getDisChannel() {
		return disChannel;
	}

	public void setDisChannel(String disChannel) {
		this.disChannel = disChannel;
	}

	public Integer getDisMin() {
		return disMin;
	}

	public void setDisMin(Integer disMin) {
		this.disMin = disMin;
	}

	public Double getDisRatio() {
		return disRatio;
	}

	public void setDisRatio(Double disRatio) {
		this.disRatio = disRatio;
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
