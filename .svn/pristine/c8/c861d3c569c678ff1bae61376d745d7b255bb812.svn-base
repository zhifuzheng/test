package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 线下订单退货明细表
 * @author Administrator
 *
 */
@Table(tableName = "offline_detailed")
public class OfflineDetailed {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String detailedUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="线下订单UUID")
	private String offlineUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商家UUID")
	private String businessUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商品UUID")
	private String itemUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品名称")
	private String title;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商品价格")
	private int price;
	
	@TableField(isNotNull=true,fieldSize=200,comment="退货数量")
	private String returnQuantity;
	
	@TableField(isNotNull=true,fieldSize=200,comment="退款金额")
	private int returnPrice;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;
	
	@TableField(isNotNull=true,fieldSize=32,comment="零售商UUID")
	private String retailerUUID;
	
	@TableField(isNotNull=true,fieldSize=1000,comment="零售商名称")
	private String retailerName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDetailedUUID() {
		return detailedUUID;
	}

	public void setDetailedUUID(String detailedUUID) {
		this.detailedUUID = detailedUUID;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(String returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public int getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(int returnPrice) {
		this.returnPrice = returnPrice;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRetailerUUID() {
		return retailerUUID;
	}

	public void setRetailerUUID(String retailerUUID) {
		this.retailerUUID = retailerUUID;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = retailerName;
	}
	
	

}
