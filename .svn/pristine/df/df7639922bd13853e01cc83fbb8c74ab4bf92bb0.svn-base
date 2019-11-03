package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 平台活动商品订单
 * @author zzf
 */

@Table(tableName="platform_good_order")
public class PlatformActivityOrder {

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="订单UUID")
	private String orderUUID;
	
	@TableField(fieldSize=32,comment="订单编号")
	private String orderNumber;
	
	@TableField(fieldSize=64,comment="订单通知")
	private String advice;
	
	@TableField(isNotNull=true,fieldSize=32,comment="活动UUID")
	private String activityUUID;
	
	@TableField(fieldSize=100,comment="商品标题")
	private String title;
	
	@TableField(fieldSize=1000,comment="商品图片")
	private String goodImg;
	
	@TableField(comment="商品总件数，积分商城商品每次只能兑换一件")
	private Integer totalCount=1;
	
	@TableField(comment="商品总价")
	private Integer price;
	
	@TableField(comment=" 商品领取时间限制，单位小时，整数")
	private Integer getTime;
	
	@TableField(isNotNull=true,fieldSize=32,comment="领取店铺的UUID")
	private String entityUUID;
	
	@TableField(isNotNull=true,fieldSize=128,comment="领取店铺的名称")
	private String entityName;
	
	@TableField(fieldSize=1000,comment="领取店铺的图片")
	private String entityLogo;
	
	@TableField(isNotNull=true,fieldSize=5000,comment="商家详细地址")
	private String businessAdd;
	
	@TableField(isNotNull=false,fieldSize=36,comment="坐标经度(x)")
	private String longitude;
	
	@TableField(isNotNull=false,fieldSize=36,comment="坐标纬度(y)")
	private String latitude;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商家电话")
	private String businessPhone;
	
	@TableField(isNotNull=true,fieldSize=2,comment="订单状态：0-未领取，1-已领取，2-已过期")
	private Integer orderStatus = 0;
	
	@TableField(isNotNull=true,fieldSize=1,comment="用户删除状态：0-未删除，1-已删除，默认值为0")
	private Integer userIsDel = 0;
	
	@TableField(isNotNull=true,fieldSize=1,comment="平台删除状态：0-未删除，1-已删除，默认值为0")
	private Integer platformIsDel = 0;
	
	@TableField(isNotNull=true,fieldSize=32,comment="买家UUID")
	private String payerUUID;
	
	@TableField(fieldSize=30,comment="买家名称")
	private String payerName;
	
	@TableField(fieldSize=1000,comment="买家头像")
	private String payerLogo;
	
	@TableField(fieldSize=11, comment="买家手机号码，11位标准手机长度，全局唯一")
	private String receiverMobile;
	
	@TableField(fieldSize=30,comment="订单创建时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="订单接单时间")
	private String confirmTime;
	
	@TableField(fieldSize=30,comment="订单完成时间")
	private String endTime;
	
	@TableField(fieldSize=30,comment="订单关闭时间")
	private String closeTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderUUID() {
		return orderUUID;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public String getActivityUUID() {
		return activityUUID;
	}

	public void setActivityUUID(String activityUUID) {
		this.activityUUID = activityUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGoodImg() {
		return goodImg;
	}

	public void setGoodImg(String goodImg) {
		this.goodImg = goodImg;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getGetTime() {
		return getTime;
	}

	public void setGetTime(Integer getTime) {
		this.getTime = getTime;
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

	public String getBusinessAdd() {
		return businessAdd;
	}

	public void setBusinessAdd(String businessAdd) {
		this.businessAdd = businessAdd;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getUserIsDel() {
		return userIsDel;
	}

	public void setUserIsDel(Integer userIsDel) {
		this.userIsDel = userIsDel;
	}

	public Integer getPlatformIsDel() {
		return platformIsDel;
	}

	public void setPlatformIsDel(Integer platformIsDel) {
		this.platformIsDel = platformIsDel;
	}

	public String getPayerUUID() {
		return payerUUID;
	}

	public void setPayerUUID(String payerUUID) {
		this.payerUUID = payerUUID;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getPayerLogo() {
		return payerLogo;
	}

	public void setPayerLogo(String payerLogo) {
		this.payerLogo = payerLogo;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	
}
