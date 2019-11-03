package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 商家入驻消息通知
 * @author Administrator
 *
 */

@Table(tableName = "apply_msg")
public class ApplyMsg {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String msgUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="申请人UUID")
	private String vipUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="驳回数据UUID")
	private String dateUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="审核状态（1：审核通过，2：审核未通过）")
	private String state;
	
	@TableField(isNotNull=true,fieldSize=32,comment="是否阅读（0：未阅读，1：已阅读）")
	private String msgRead;
	
	@TableField(isNotNull=true,fieldSize=200,comment="月日")
	private String monthDay;
	
	@TableField(isNotNull=false,fieldSize=5000,comment="通知消息")
	private String message;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;
	
	@TableField(isNotNull=false,fieldSize=200,comment="登录名")
	private String loginName;
	
	@TableField(isNotNull=false,fieldSize=200,comment="登录密码")
	private String loginPassword;
	
	@TableField(isNotNull=false,fieldSize=1000,comment="后台地址")
	private String urlAdd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsgUUID() {
		return msgUUID;
	}

	public void setMsgUUID(String msgUUID) {
		this.msgUUID = msgUUID;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getDateUUID() {
		return dateUUID;
	}

	public void setDateUUID(String dateUUID) {
		this.dateUUID = dateUUID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsgRead() {
		return msgRead;
	}

	public void setMsgRead(String msgRead) {
		this.msgRead = msgRead;
	}

	public String getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(String monthDay) {
		this.monthDay = monthDay;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getUrlAdd() {
		return urlAdd;
	}

	public void setUrlAdd(String urlAdd) {
		this.urlAdd = urlAdd;
	}
	
	

}
