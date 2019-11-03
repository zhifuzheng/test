package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 批发市场
 * @author Administrator
 *
 */
@Table(tableName = "wholesale_market")
public class WholesaleMarket {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String pfUUID;
	
	@TableField(isNotNull=true,fieldSize=2000,comment="批发市场名称")
	private String pfName;
	
	@TableField(isNotNull=true,fieldSize=2000,comment="批发市场地址")
	private String pfAdd;
	
	@TableField(isNotNull=true,fieldSize=5000,comment="批发市场简介")
	private String pfSynopsis;
	
	@TableField(isNotNull=true,fieldSize=100,comment="批发市场门店图片")
	private String storefrontImg;
	
	@TableField(isNotNull=true,fieldSize=100,comment="时间")
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPfUUID() {
		return pfUUID;
	}

	public void setPfUUID(String pfUUID) {
		this.pfUUID = pfUUID;
	}

	public String getPfName() {
		return pfName;
	}

	public void setPfName(String pfName) {
		this.pfName = pfName;
	}

	public String getPfAdd() {
		return pfAdd;
	}

	public void setPfAdd(String pfAdd) {
		this.pfAdd = pfAdd;
	}

	public String getPfSynopsis() {
		return pfSynopsis;
	}

	public void setPfSynopsis(String pfSynopsis) {
		this.pfSynopsis = pfSynopsis;
	}

	public String getStorefrontImg() {
		return storefrontImg;
	}

	public void setStorefrontImg(String storefrontImg) {
		this.storefrontImg = storefrontImg;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
