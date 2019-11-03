package com.xryb.zhtc.entity;
import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 用户优惠劵管理实体
 * @author apple
 */
@Table(tableName="coupon_manage_user")
public class CouponOfUser{
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="劵持有者uuid")
	private String vipUUID;
	@TableField(fieldSize=100,comment="会员姓名")
	private String vipName;
	@TableField(isNotNull=true,fieldSize=32,comment="劵uuid,全局唯一编号")
	private String couponUUID;
	@TableField(isNotNull=true,fieldSize=32,comment="劵的批次uuid，同一批劵相同，方便库存管理等")
	private String batchUUID;
	@TableField(fieldSize=20,comment="劵领取日期")
	private String getTime;
	@TableField(comment="优惠劵的是否使用，1-未使用，2-已使用，3-已过期")
	private Integer isUse;
	@TableField(comment="1-未删除，2-已删除")
	private Integer isDel=1;
	@TableField(fieldSize=20,comment="劵使用日期")
	private String useTime;
	@TableField(fieldSize=32,comment="该优惠劵使用的订单UUID")
	private String orderUUID;
	
	
	public String getVipName() {
		return vipName;
	}
	public void setVipName(String vipName) {
		this.vipName = vipName;
	}
	public String getOrderUUID() {
		return orderUUID;
	}
	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVipUUID() {
		return vipUUID;
	}
	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}
	public String getCouponUUID() {
		return couponUUID;
	}
	public void setCouponUUID(String couponUUID) {
		this.couponUUID = couponUUID;
	}
	public String getBatchUUID() {
		return batchUUID;
	}
	public void setBatchUUID(String batchUUID) {
		this.batchUUID = batchUUID;
	}
	public String getGetTime() {
		return getTime;
	}
	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	

}
