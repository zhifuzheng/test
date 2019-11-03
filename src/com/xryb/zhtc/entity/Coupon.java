package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 代金劵、优惠劵线上批量生成模版实体
 * @author apple
 */
@Table(tableName="coupon")
public class Coupon{
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="劵uuid,全局唯一编号，手动生成")
	private String couponUUID;
	@TableField(isNotNull=true,fieldSize=32,comment="劵的批次uuid，同一批劵相同，方便库存和劵信息管理等")
	private String batchUUID;
	@TableField(comment="劵是否被领取,1-未领取，2-已领取")
	private Integer isGet;
	@TableField(comment="优惠劵编号，根据优惠劵的发行数量从1开始递增")
	private String couponNumber;
	
	public String getCouponNumber() {
		return couponNumber;
	}
	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getIsGet() {
		return isGet;
	}
	public void setIsGet(Integer isGet) {
		this.isGet = isGet;
	}
	
}
