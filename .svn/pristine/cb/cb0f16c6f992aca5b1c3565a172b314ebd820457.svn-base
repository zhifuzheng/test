package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 零售商供应商
 * @author Administrator
 *
 */

@Table(tableName = "business_apply")
public class BusinessApply {
	
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String businessUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商家名称")
	private String businessName;
	
	@TableField(isNotNull=false,fieldSize=36,comment="坐标纬度(x)")
	private String longitude;
	
	@TableField(isNotNull=false,fieldSize=36,comment="坐标经度(y)")
	private String latitude;
	
	@TableField(isNotNull=true,fieldSize=5000,comment="商家详细地址")
	private String businessAdd;
	
	@TableField(isNotNull=true,fieldSize=200,comment="商家电话")
	private String businessPhone;
	
	@TableField(isNotNull=true,fieldSize=200,comment="联系人")
	private String contacts;
	
	@TableField(isNotNull=true,fieldSize=200,comment="行业分类")
	private String classification;
	
	@TableField(isNotNull=true,fieldSize=200,comment="营业时间")
	private String businessHours;
	
	@TableField(isNotNull=true,fieldSize=200,comment="开户银行")
	private String bank;
	
	@TableField(isNotNull=true,fieldSize=200,comment="银行卡号")
	private String bankCard;
	
	@TableField(isNotNull=true,fieldSize=200,comment="银行正面照")
	private String bankImg;
	
	@TableField(isNotNull=true,fieldSize=200,comment="申请时间")
	private String applyTime;
	
	@TableField(isNotNull=true,fieldSize=200,comment="审批状态(0:待审批，1：审批通过，2：审批不通过)")
	private String approvalStatus;
	
	@TableField(isNotNull=false,fieldSize=200,comment="审批时间")
	private String approvalTime;
	
	@TableField(isNotNull=false,fieldSize=200,comment="店铺状态(0：停用，1：启用)")
	private String shopState;
	
	@TableField(isNotNull=false,fieldSize=5000,comment="审批不通过理由")
	private String reason;
	
	@TableField(isNotNull=false,fieldSize=200,comment="到期时间")
	private String dueTime;
	
	@TableField(isNotNull=true,fieldSize=200,comment="申请人UUID")
	private String vipUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="申请人姓名")
	private String vipName;
	
	@TableField(isNotNull=true,fieldSize=200,comment="申请类型(0：零售商，1：供应商)")
	private String applyType;
	
	@TableField(isNotNull=true,fieldSize=200,comment="店面照片")
	private String storefrontImg;
	
	@TableField(isNotNull=true,fieldSize=5000,comment="简介")
	private String synopsis;
	
	@TableField(isNotNull=true,fieldSize=200,comment="工商营业照")
	private String businessImg;
	
	@TableField(isNotNull=true,fieldSize=200,comment="身份证正面照")
	private String idJustImg;
	
	@TableField(isNotNull=true,fieldSize=200,comment="身份证反面照")
	private String idBackImg;
	
	@TableField(isNotNull=false,fieldSize=200,comment="入驻费用")
	private String money;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商品分类UUID")
	private String catUUID;
	
	@TableField(isNotNull=false,fieldSize=200,comment="销量")
	private int salesVolume = 0;
	
	@TableField(isNotNull=false,fieldSize=30,comment="根据坐标计算距离")
	private double distance;
	
	@TableField(isNotNull=false,fieldSize=32,comment="批发市场UUID")
	private String pfUUID;
	
	@TableField(isNotNull=false,fieldSize=2000,comment="批发市场名称")
	private String pfName;
	
	@TableField(isNotNull=false,fieldSize=200,comment="提现密码")
	private String bankPassword;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getBusinessAdd() {
		return businessAdd;
	}

	public void setBusinessAdd(String businessAdd) {
		this.businessAdd = businessAdd;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}

	public String getBankImg() {
		return bankImg;
	}

	public void setBankImg(String bankImg) {
		this.bankImg = bankImg;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}

	public String getShopState() {
		return shopState;
	}

	public void setShopState(String shopState) {
		this.shopState = shopState;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDueTime() {
		return dueTime;
	}

	public void setDueTime(String dueTime) {
		this.dueTime = dueTime;
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

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getStorefrontImg() {
		return storefrontImg;
	}

	public void setStorefrontImg(String storefrontImg) {
		this.storefrontImg = storefrontImg;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getBusinessImg() {
		return businessImg;
	}

	public void setBusinessImg(String businessImg) {
		this.businessImg = businessImg;
	}

	public String getIdJustImg() {
		return idJustImg;
	}

	public void setIdJustImg(String idJustImg) {
		this.idJustImg = idJustImg;
	}

	public String getIdBackImg() {
		return idBackImg;
	}

	public void setIdBackImg(String idBackImg) {
		this.idBackImg = idBackImg;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCatUUID() {
		return catUUID;
	}

	public void setCatUUID(String catUUID) {
		this.catUUID = catUUID;
	}

	public int getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(int salesVolume) {
		this.salesVolume = salesVolume;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public String getBankPassword() {
		return bankPassword;
	}

	public void setBankPassword(String bankPassword) {
		this.bankPassword = bankPassword;
	}

	
	
	

}
