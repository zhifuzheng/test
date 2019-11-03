package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 订单商品关联信息
 * @author wf
 */
@Table(tableName = "order_item")
public class OrderItem implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 4620636215506079861L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="订单UUID")
	private String orderUUID;
	
	@TableField(fieldSize=32,comment="订单批次号，当一笔交易中有多个订单参与时，参与交易的所有订单共用同一个批次号")
	private String batchNo;
	
	@TableField(fieldSize=8000,comment="针对该商品的优惠券UUID")
	private String couponUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商品UUID")
	private String itemUUID;
	
	@TableField(isNotNull=true,fieldSize=1,comment="商品类型：0零售商品，1批发商品")
	private String itemType;
	
	@TableField(isNotNull=true,fieldSize=32,comment="套餐UUID")
	private String paramUUID;
	
	@TableField(fieldSize=128,comment="商品标题")
	private String title;
	
	@TableField(fieldSize=20,comment="商品单价")
	private Integer price;
	
	@TableField(comment="单件商品优惠金额，默认值为0")
	private Integer reducePrice = 0;
	
	@TableField(comment="单件商品实付金额")
	private Integer payment;
	
	@TableField(comment="数量")
	private Integer count;
	
	@TableField(comment="允许退货数量")
	private Integer allowRefund;
	
	@TableField(fieldSize=1000,comment="图片")
	private String itemImg;
	
	@TableField(fieldSize=8000,comment="规格数据，json格式")
	private String paramData;
	
	@TableField(isNotNull=true,fieldSize=2,comment="付款状态：0未付款，1已付款，默认值为0")
	private String isPay = "0";
	
	@TableField(isNotNull=true,fieldSize=2,comment="订单状态：0未付款，1已付款，未接单，2已接单，未签收，3已签收，未评价，交易完成，4已评价，-1交易取消，-2超时关闭，-3待退款，-4同意退款，-5驳回退款，默认值为0")
	private String orderStatus = "0";
	
	@TableField(fieldSize=2, comment="会员订单删除状态：0未删除，1已删除，默认值为0")
	private String isDel = "0";
	
	@TableField(isNotNull=true,fieldSize=2,comment="店铺订单显示状态：0不显示，1显示，默认值为1")
	private String isShow = "1";
	
	@TableField(isNotNull=true,fieldSize=32,comment="买家UUID")
	private String payerUUID;
	
	@TableField(fieldSize=30,comment="买家名称")
	private String payerName;
	
	@TableField(fieldSize=1000,comment="买家头像")
	private String payerLogo;
	
	@TableField(fieldSize=1,comment="商品分销状态：0关闭，1开启")
	private String distributeStatus;
	
	@TableField(fieldSize=32,comment="上级分销商UUID")
	private String firstUUID;
	
	@TableField(fieldSize=32,comment="上上级分销商UUID")
	private String secondUUID;
	
	@TableField(comment="一级分销商提成比例")
	private Double firstRatio;
	
	@TableField(comment="二级分销商提成比例")
	private Double secondRatio;
	
	@TableField(comment = "一级分销商收入，默认值为0")
	private Integer firstIncome = 0;
	
	@TableField(comment = "二级分销商收入，默认值为0")
	private Integer secondIncome = 0;
	
	@TableField(fieldSize=30,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;

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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCouponUUID() {
		return couponUUID;
	}

	public void setCouponUUID(String couponUUID) {
		this.couponUUID = couponUUID;
	}

	public String getItemUUID() {
		return itemUUID;
	}

	public void setItemUUID(String itemUUID) {
		this.itemUUID = itemUUID;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getParamUUID() {
		return paramUUID;
	}

	public void setParamUUID(String paramUUID) {
		this.paramUUID = paramUUID;
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

	public Integer getReducePrice() {
		return reducePrice;
	}

	public void setReducePrice(Integer reducePrice) {
		this.reducePrice = reducePrice;
	}

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getAllowRefund() {
		return allowRefund;
	}

	public void setAllowRefund(Integer allowRefund) {
		this.allowRefund = allowRefund;
	}

	public String getItemImg() {
		return itemImg;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

	public String getParamData() {
		return paramData;
	}

	public void setParamData(String paramData) {
		this.paramData = paramData;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
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

	public String getDistributeStatus() {
		return distributeStatus;
	}

	public void setDistributeStatus(String distributeStatus) {
		this.distributeStatus = distributeStatus;
	}

	public String getFirstUUID() {
		return firstUUID;
	}

	public void setFirstUUID(String firstUUID) {
		this.firstUUID = firstUUID;
	}

	public String getSecondUUID() {
		return secondUUID;
	}

	public void setSecondUUID(String secondUUID) {
		this.secondUUID = secondUUID;
	}

	public Double getFirstRatio() {
		return firstRatio;
	}

	public void setFirstRatio(Double firstRatio) {
		this.firstRatio = firstRatio;
	}

	public Double getSecondRatio() {
		return secondRatio;
	}

	public void setSecondRatio(Double secondRatio) {
		this.secondRatio = secondRatio;
	}

	public Integer getFirstIncome() {
		return firstIncome;
	}

	public void setFirstIncome(Integer firstIncome) {
		this.firstIncome = firstIncome;
	}

	public Integer getSecondIncome() {
		return secondIncome;
	}

	public void setSecondIncome(Integer secondIncome) {
		this.secondIncome = secondIncome;
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

}
