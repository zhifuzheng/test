package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 收藏
 * @author Administrator
 *
 */

@Table(tableName = "collection")
public class Collection {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String collectionUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="外部UUID（如店铺UUID）")
	private String externalUUID;
	
	@TableField(isNotNull=false,fieldSize=2000,comment="店铺名称")
	private String buinessName;
	
	@TableField(isNotNull=false,fieldSize=2000,comment="商品名称")
	private String commodityName;
	
	@TableField(isNotNull=false,fieldSize=200,comment="收藏人数")
	private int collectorsNumber = 0;
	
	@TableField(isNotNull=false,fieldSize=200,comment="价格")
	private String price;
	
	@TableField(isNotNull=false,fieldSize=200,comment="图片")
	private String img;
	
	@TableField(isNotNull=true,fieldSize=200,comment="收藏人UUID")
	private String vipUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="收藏人姓名")
	private String vipName;
	
	@TableField(isNotNull=true,fieldSize=200,comment="收藏时间")
	private String time;
	
	@TableField(isNotNull=true,fieldSize=20,comment="收藏类型（0：商品，1：商家）")
	private String type;
	
	@TableField(isNotNull=false,fieldSize=32,comment="商品UUID")
	private String commodityUUID;

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

	public String getExternalUUID() {
		return externalUUID;
	}

	public void setExternalUUID(String externalUUID) {
		this.externalUUID = externalUUID;
	}

	public String getBuinessName() {
		return buinessName;
	}

	public void setBuinessName(String buinessName) {
		this.buinessName = buinessName;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public int getCollectorsNumber() {
		return collectorsNumber;
	}

	public void setCollectorsNumber(int collectorsNumber) {
		this.collectorsNumber = collectorsNumber;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCommodityUUID() {
		return commodityUUID;
	}

	public void setCommodityUUID(String commodityUUID) {
		this.commodityUUID = commodityUUID;
	}
	
	

}
