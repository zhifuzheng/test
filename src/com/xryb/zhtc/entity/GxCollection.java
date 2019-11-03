package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需收藏
 * @author Administrator
 *
 */


@Table(tableName="gx_collection")
public class GxCollection {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String collectionUUID;
	
	@TableField(fieldSize=200,isNotNull=true,comment="卖家名称")
	private String sellerName;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="卖家头像")
	private String sellerLong;
	
	@TableField(fieldSize=32,isNotNull=true,comment="卖家UUID")
	private String sellerUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="数据UUID")
	private String dataUUID;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="图片")
	private String img;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="标题")
	private String title;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="详情")
	private String details;
	
	@TableField(fieldSize=100,isNotNull=false,comment="商品价格")
	private String price;
	
	@TableField(fieldSize=100,isNotNull=true,comment="收藏人姓名")
	private String vipName;
	
	@TableField(fieldSize=100,isNotNull=true,comment="收藏人UUID")
	private String vipUUID;
	
	@TableField(fieldSize=20,isNotNull=true,comment="状态：0：供需商品，1：供需需求")
	private String state;
	
	@TableField(fieldSize=200,isNotNull=true,comment="时间")
	private String time;
	
	@TableField(fieldSize=200,isNotNull=true,comment="电话号码")
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectionUUID() {
		return collectionUUID;
	}

	public void setCollectionUUID(String collectionUUID) {
		this.collectionUUID = collectionUUID;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerLong() {
		return sellerLong;
	}

	public void setSellerLong(String sellerLong) {
		this.sellerLong = sellerLong;
	}

	public String getSellerUUID() {
		return sellerUUID;
	}

	public void setSellerUUID(String sellerUUID) {
		this.sellerUUID = sellerUUID;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDataUUID() {
		return dataUUID;
	}

	public void setDataUUID(String dataUUID) {
		this.dataUUID = dataUUID;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	

}
