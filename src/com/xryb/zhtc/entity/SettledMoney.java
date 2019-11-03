package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 
 * @author Administrator
 *商家入驻费用设置
 */

@Table(tableName = "settled_money")
public class SettledMoney {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String settledUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="金额")
	private String money;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;
	
	@TableField(isNotNull=true,fieldSize=32,comment="userUUID")
	private String userUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="userName")
	private String userName;
	
	@TableField(isNotNull=true,fieldSize=32,comment="类型：0：一个星期，1：半年，2：一年")
	private String settledType;
	
	@TableField(isNotNull=false,fieldSize=1000,comment="shuoMing")
	private String shuoMing;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSettledUUID() {
		return settledUUID;
	}

	public void setSettledUUID(String settledUUID) {
		this.settledUUID = settledUUID;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserUUID() {
		return userUUID;
	}

	public void setUserUUID(String userUUID) {
		this.userUUID = userUUID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSettledType() {
		return settledType;
	}

	public void setSettledType(String settledType) {
		this.settledType = settledType;
	}

	public String getShuoMing() {
		return shuoMing;
	}

	public void setShuoMing(String shuoMing) {
		this.shuoMing = shuoMing;
	}

	
	
	

}
