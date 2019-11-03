package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 发行商优惠劵信息管理实体
 * @author apple
 */
@Table(tableName="coupon_manage_issuer")
public class CouponOfIssuer {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="劵的批次uuid，同一批劵相同")
	private String batchUUID;
	@TableField(isNotNull=true,fieldSize=32,comment="发行方uuid,全局唯一编号，手动生成")
	private String vipUUID;
	@TableField(fieldSize=8000,comment="发行方店铺uuid,全局唯一编号")
	private String merchantUUID;
	@TableField(fieldSize=100,comment="发行方店铺名次")
	private String businessName;
	@TableField(fieldSize=100,comment="劵名")
	private String couponName;
	@TableField(fieldSize=100,comment="劵的副标题")
	private String couponSubhead;
	@TableField(fieldSize=500,comment="使用说明")
	private String couponExplain;
	@TableField(comment="劵领取条件,通过积分来限制，0-免费领取，>0-积分兑换领取")
	private Integer integration;
	@TableField(fieldSize=12,comment="劵的额度(代金劵)")
	private String money;
	@TableField(fieldSize=12,comment="劵的折扣(优惠劵，折扣劵)")
	private String discount;
	@TableField(comment="折扣劵最高可优惠金额")
	private String reduceMaxPrice;
	@TableField(comment="劵的类型，1-代金劵，2-优惠劵(折扣劵)")
	private Integer couponType;
	@TableField(comment="劵发行方式，1-线上，2-线下")
	private Integer releaseType;
	@TableField(fieldSize=12,comment="生效金额")
	private String effectMoney;
	@TableField(comment="使用商品限制(1-指定商品，2-不指定商品)")
	private Integer goodsUseCondition;
	@TableField(fieldSize=32,comment="商品uuid,使用条件为指定商品时需填写")
	private String goodsUUID;
	@TableField(comment="商品分类使用限制(1-指定分类商品，2-不指定分类商品)")
	private Integer goodsSortCondition;
	@TableField(fieldSize=32,comment="商品分类的catUUID,使用条件为指定分类商品时需填写")
	private String goodsCatUUID;
	@TableField(comment="重复领取数量")
	private Integer repeatNumber;
	@TableField(fieldSize=20,comment="劵开始领取日期")
	private String releaseStartTime;
	@TableField(fieldSize=20,comment="劵领取截止日期")
	private String releaseEndTime;
	@TableField(fieldSize=20,comment="劵开始使用日期")
	private String useStartTime;
	@TableField(fieldSize=20,comment="劵失效日期")
	private String disabledTime;
	@TableField(fieldSize=20,comment="劵发行日期(劵上架日期)")
	private String releaseTime;
	@TableField(fieldSize=20,comment="劵下架日期")
	private String downTime;
	@TableField(fieldSize=20,comment="劵编辑修改日期")
	private String modifiedTime;
	@TableField(fieldSize=20,comment="劵生成时间")
	private String createTime;
	@TableField(fieldSize=20,comment="劵生批次编号")
	private String batchNumber;
	@TableField(comment="劵发行数量")
	private Integer releaseNumbers;
	@TableField(comment="劵已领取数量")
	private Integer getNumbers;
	@TableField(comment="劵已使用数量")
	private Integer usedNumbers;
	@TableField(comment="劵的库存,初始值为劵发行数量")
	private Integer stock;
	@TableField(comment="优惠劵是否上架,1-已上架，2-未上架,3-已下架")
	private Integer isRelease;
	@TableField(comment="该批次优惠劵是否删除，1-未删除，2-已删除")
	private Integer isDel;
	@TableField(comment="平台优惠劵：平台优惠劵是否可与店铺优惠劵叠加使用；1-是，2-否")
	private Integer isRepeatUse=2;//TODO 叠加使用待完成
	@TableField(comment="优惠劵来源，1-平台，2-商家")
	private Integer fromType;
	
	
	public Integer getIsRepeatUse() {
		return isRepeatUse;
	}
	public void setIsRepeatUse(Integer isRepeatUse) {
		this.isRepeatUse = isRepeatUse;
	}
	public String getReduceMaxPrice() {
		return reduceMaxPrice;
	}
	public void setReduceMaxPrice(String reduceMaxPrice) {
		this.reduceMaxPrice = reduceMaxPrice;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public Integer getFromType() {
		return fromType;
	}
	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}
	public String getVipUUID() {
		return vipUUID;
	}
	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}
	public String getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	public Integer getReleaseType() {
		return releaseType;
	}
	public void setReleaseType(Integer releaseType) {
		this.releaseType = releaseType;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public Integer getGoodsUseCondition() {
		return goodsUseCondition;
	}
	public void setGoodsUseCondition(Integer goodsUseCondition) {
		this.goodsUseCondition = goodsUseCondition;
	}
	public Integer getGoodsSortCondition() {
		return goodsSortCondition;
	}
	public void setGoodsSortCondition(Integer goodsSortCondition) {
		this.goodsSortCondition = goodsSortCondition;
	}
	
	public String getGoodsCatUUID() {
		return goodsCatUUID;
	}
	public void setGoodsCatUUID(String goodsCatUUID) {
		this.goodsCatUUID = goodsCatUUID;
	}
	public Integer getIsRelease() {
		return isRelease;
	}
	public void setIsRelease(Integer isRelease) {
		this.isRelease = isRelease;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBatchUUID() {
		return batchUUID;
	}
	public void setBatchUUID(String batchUUID) {
		this.batchUUID = batchUUID;
	}
	public String getMerchantUUID() {
		return merchantUUID;
	}
	public void setMerchantUUID(String merchantUUID) {
		this.merchantUUID = merchantUUID;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getCouponSubhead() {
		return couponSubhead;
	}
	public void setCouponSubhead(String couponSubhead) {
		this.couponSubhead = couponSubhead;
	}
	public String getCouponExplain() {
		return couponExplain;
	}
	public void setCouponExplain(String couponExplain) {
		this.couponExplain = couponExplain;
	}
	public Integer getIntegration() {
		return integration;
	}
	public void setIntegration(Integer integration) {
		this.integration = integration;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public Integer getCouponType() {
		return couponType;
	}
	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}
	public String getEffectMoney() {
		return effectMoney;
	}
	public void setEffectMoney(String effectMoney) {
		this.effectMoney = effectMoney;
	}
	public String getGoodsUUID() {
		return goodsUUID;
	}
	public void setGoodsUUID(String goodsUUID) {
		this.goodsUUID = goodsUUID;
	}
	public Integer getRepeatNumber() {
		return repeatNumber;
	}
	public void setRepeatNumber(Integer repeatNumber) {
		this.repeatNumber = repeatNumber;
	}
	public String getReleaseStartTime() {
		return releaseStartTime;
	}
	public void setReleaseStartTime(String releaseStartTime) {
		this.releaseStartTime = releaseStartTime;
	}
	public String getReleaseEndTime() {
		return releaseEndTime;
	}
	public void setReleaseEndTime(String releaseEndTime) {
		this.releaseEndTime = releaseEndTime;
	}
	public String getUseStartTime() {
		return useStartTime;
	}
	public void setUseStartTime(String useStartTime) {
		this.useStartTime = useStartTime;
	}
	public String getDisabledTime() {
		return disabledTime;
	}
	public void setDisabledTime(String disabledTime) {
		this.disabledTime = disabledTime;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public Integer getReleaseNumbers() {
		return releaseNumbers;
	}
	public void setReleaseNumbers(Integer releaseNumbers) {
		this.releaseNumbers = releaseNumbers;
	}
	public Integer getGetNumbers() {
		return getNumbers;
	}
	public void setGetNumbers(Integer getNumbers) {
		this.getNumbers = getNumbers;
	}
	public Integer getUsedNumbers() {
		return usedNumbers;
	}
	public void setUsedNumbers(Integer usedNumbers) {
		this.usedNumbers = usedNumbers;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	
}
