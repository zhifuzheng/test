package com.xryb.zhtc.entity;


import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 线下优惠劵批量生成实体
 * @author zzf
 */
@Table(tableName="coupon_offline")
public class CouponOffline{
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	@TableField(isNotNull=true,fieldSize=32,comment="劵uuid,全局唯一编号，手动生成")
	private String couponUUID;
	@TableField(isNotNull=true,fieldSize=32,comment="劵的批次uuid，同一批劵相同，方便库存和劵信息管理等")
	private String batchUUID;
	@TableField(comment="优惠劵编号，根据优惠劵的发行数量从1开始递增")
	private String couponNumber;
	@TableField(comment="二维码图片")
	private String QRcodeImg;
	@TableField(comment="劵的绑定状态,1-未绑定用户，2-已绑定用户")
	private Integer isGet;
	@TableField(fieldSize=20,comment="劵绑定日期")
	private String useTime;
	@TableField(fieldSize=32,comment="劵绑定者uuid")
	private String vipUUID;
	@TableField(comment="劵绑定着名称")
	private String vipName;
	
	public String getVipName() {
		return vipName;
	}
	public void setVipName(String vipName) {
		this.vipName = vipName;
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
	
	public String getCouponNumber() {
		return couponNumber;
	}
	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}
	public String getQRcodeImg() {
		return QRcodeImg;
	}
	public void setQRcodeImg(String qRcodeImg) {
		QRcodeImg = qRcodeImg;
	}
	
	
	public Integer getIsGet() {
		return isGet;
	}
	public void setIsGet(Integer isGet) {
		this.isGet = isGet;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	
	public String getVipUUID() {
		return vipUUID;
	}
	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}
	
	
	
	
	

}
