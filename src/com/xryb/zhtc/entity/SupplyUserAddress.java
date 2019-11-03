package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需管理用户收货地址管理信息
 * @author zzf
 */
@Table(tableName = "supply_address")
public class SupplyUserAddress implements Serializable{
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 7539124791826854859L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id，自动增长")
    private	Long id;
	
	@TableField(isPKey=true,fieldSize=32,isUKey=true,comment="地址UUID")
	private String addrUUID;
	
	@TableField(isPKey=true,fieldSize=32,comment="用户UUID")
	private String vipUUID;
	
	@TableField(fieldSize=50,comment="收货人姓名")
	private String receiverName;
	
	@TableField(fieldSize=11,comment="收货人联系电话")
	private String receiverMobile;
	
	@TableField(fieldSize=200,comment="收货人省市区")
	private String provCityDist;
	
	@TableField(fieldSize=200,comment="收货人街道地址")
	private String detailAddr;
	
	@TableField(fieldSize=2,comment="是否为默认收货地址：0-否，1-是，默认值为0")
	private String isDefault = "0";
	
	@TableField(fieldSize=2,comment="是否删除收货地址：0-未删除，1-已删除，默认值为0")
	private String isDel = "0";
	
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

	public String getAddrUUID() {
		return addrUUID;
	}

	public void setAddrUUID(String addrUUID) {
		this.addrUUID = addrUUID;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getProvCityDist() {
		return provCityDist;
	}

	public void setProvCityDist(String provCityDist) {
		this.provCityDist = provCityDist;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
