package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 线下订单
 * @author Administrator
 *
 */

@Table(tableName = "offline_orders")
public class OfflineOrders {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String offlineUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商家UUID")
	private String businessUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商家名称")
	private String businessName;
	
	@TableField(isNotNull=false,fieldSize=200,comment="商家电话号码")
	private String businessPhone;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品UUID")
	private String itemUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品名称")
	private String title;
	
	@TableField(isNotNull=true,fieldSize=1000,comment="商品图片")
	private String commodityImg;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品价格")
	private int price;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品总价格")
	private int totalPrice;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品数量")
	private String number;
	
	@TableField(isNotNull=true,fieldSize=10,comment="订单状态（0：待付款，1：已付款）")
	private String state;
	
	@TableField(isNotNull=true,fieldSize=32,comment="vipUUID")
	private String vipUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="vipName")
	private String vipName;
	
	@TableField(isNotNull=true,fieldSize=200,comment="创建时间")
	private String time;
	
	@TableField(isNotNull=true,fieldSize=32,comment="零售商UUID")
	private String retailerUUID;
	
	@TableField(isNotNull=true,fieldSize=1000,comment="零售商名称")
	private String retailerName;
	
	@TableField(isNotNull=true,fieldSize=32,comment="零售商电话号码")
	private String retailerPhone;
	
	@TableField(isNotNull=true,fieldSize=1000,comment="零售商店铺头像")
	private String retailerImg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOfflineUUID() {
		return offlineUUID;
	}

	public void setOfflineUUID(String offlineUUID) {
		this.offlineUUID = offlineUUID;
	}

	public String getBusinessUUID() {
		return businessUUID;
	}

	public void setBusinessUUID(String businessUUID) {
		this.businessUUID = businessUUID;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getItemUUID() {
		return itemUUID;
	}

	public void setItemUUID(String itemUUID) {
		this.itemUUID = itemUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCommodityImg() {
		return commodityImg;
	}

	public void setCommodityImg(String commodityImg) {
		this.commodityImg = commodityImg;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getRetailerUUID() {
		return retailerUUID;
	}

	public void setRetailerUUID(String retailerUUID) {
		this.retailerUUID = retailerUUID;
	}

	public String getRetailerPhone() {
		return retailerPhone;
	}

	public void setRetailerPhone(String retailerPhone) {
		this.retailerPhone = retailerPhone;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}

	public String getRetailerImg() {
		return retailerImg;
	}

	public void setRetailerImg(String retailerImg) {
		this.retailerImg = retailerImg;
	}

	
	
	

}
