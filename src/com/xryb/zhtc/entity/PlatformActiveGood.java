package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 平台活动商品
 * @author zzf
 */
@Table(tableName="platform_active_good")
public class PlatformActiveGood {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(fieldSize=100,comment="活动名称")
	private String activityTitle;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，活动UUID自动生成")
	private String activityUUID;
	
	@TableField(fieldSize=1000,comment="活动封面图片地址相对路径")
	private String activityImg;
	
	@TableField(comment="活动参与次数")
	private Integer joinLimitTime;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，活动商品UUID自动生成")
	private String goodUUID;
	
	@TableField(fieldSize=128,comment="活动商品名称")
	private String title;
	
	@TableField(comment="活动商品价格")
	private Integer price;
	
	@TableField(comment="商品库存")
	private Integer stock;
	
	@TableField(comment="商品购买数量")
	private Integer getNumber=0;
	
	@TableField(comment="领取时间限制，即用户订单生成开始计时超过则视为自动放弃，单位小时，整数")
	private Integer getTime;
	
	@TableField(fieldSize=20,comment="活动开始日期")
	private String activeStartTime;
	
	@TableField(fieldSize=20,comment="活动结束日期")
	private String activeEndTime;
	
	@TableField(fieldSize=1000,comment="活动商品图片地址相对路径")
	private String goodImg;
	
	@TableField(fieldSize=32,comment="平台UUID")
	private String entityUUID;
	
	@TableField(fieldSize=128,comment="平台名称")
	private String entityName;
	
	@TableField(fieldSize=1000,comment="平台logo")
	private String entityLogo;
	
	@TableField(fieldSize=1,comment="活动状态：1上架，2下架，3未上架")
	private Integer goodStatus;
	
	@TableField(fieldSize=1,comment="活动是否删除：0未删除，1回收站，2删除，默认值为0")
	private Integer isDel = 0;
	
	@TableField(fieldSize=30,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;
	
	
	
	

	public Integer getJoinLimitTime() {
		return joinLimitTime;
	}

	public void setJoinLimitTime(Integer joinLimitTime) {
		this.joinLimitTime = joinLimitTime;
	}

	public Integer getGetNumber() {
		return getNumber;
	}

	public void setGetNumber(Integer getNumber) {
		this.getNumber = getNumber;
	}

	public Integer getJoinListTime() {
		return joinLimitTime;
	}

	public void setJoinListTime(Integer joinListTime) {
		this.joinLimitTime = joinListTime;
	}

	public String getActiveStartTime() {
		return activeStartTime;
	}

	public void setActiveStartTime(String activeStartTime) {
		this.activeStartTime = activeStartTime;
	}

	public String getActiveEndTime() {
		return activeEndTime;
	}

	public void setActiveEndTime(String activeEndTime) {
		this.activeEndTime = activeEndTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getActivityUUID() {
		return activityUUID;
	}

	public void setActivityUUID(String activityUUID) {
		this.activityUUID = activityUUID;
	}

	public String getActivityImg() {
		return activityImg;
	}

	public void setActivityImg(String activityImg) {
		this.activityImg = activityImg;
	}

	public String getGoodUUID() {
		return goodUUID;
	}

	public void setGoodUUID(String goodUUID) {
		this.goodUUID = goodUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getGoodImg() {
		return goodImg;
	}

	public void setGoodImg(String goodImg) {
		this.goodImg = goodImg;
	}


	public String getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityLogo() {
		return entityLogo;
	}

	public void setEntityLogo(String entityLogo) {
		this.entityLogo = entityLogo;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getGoodStatus() {
		return goodStatus;
	}

	public void setGoodStatus(Integer goodStatus) {
		this.goodStatus = goodStatus;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getGetTime() {
		return getTime;
	}

	public void setGetTime(Integer getTime) {
		this.getTime = getTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}
