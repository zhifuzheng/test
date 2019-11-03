package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 平台积分商城商品
 * @author zzf
 */
@Table(tableName="integral_goods")
public class IntegralGoods {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，UUID自动生成")
	private String goodUUID;
	
	@TableField(fieldSize=32,comment="平台UUID")
	private String entityUUID;
	
	@TableField(fieldSize=128,comment="平台名称")
	private String entityName;
	
	@TableField(fieldSize=1000,comment="平台logo")
	private String entityLogo;
	
	@TableField(fieldSize=128,comment="商品标题")
	private String title;
	
	@TableField(fieldSize=100,comment="商品简介")
	private String introduce;
	
	@TableField(comment="商品所值积分")
	private Integer integration;
	
	@TableField(fieldSize=1000,comment="商品封面图，图片地址相对路径")
	private String goodImg;
	
	@TableField(fieldSize=8000,comment="商品展示图，图片地址相对路径，多个路径以逗号分割")
	private String subImgList;
	
	@TableField(fieldSize=8000,comment="商品详情图,图片地址相对路径，多个路径以逗号分割 ")
	private String detailImgList;
	
	@TableField(fieldSize=8000,comment="商品参数，多条参时以逗号分割，参数形式为name=value")
	private String goodParams;
	
	@TableField(comment="商品库存")
	private Integer stock;
	
	@TableField(comment="销量")
	private Integer totalSales = 0;
	
	@TableField(fieldSize=1,comment="商品状态：0上架，1下架，2未上架，3商品违规")
	private Integer goodStatus;
	
	@TableField(fieldSize=1,comment="商品是否删除：0未删除，1回收站，2删除，默认值为0")
	private Integer isDel = 0;
	
	@TableField(fieldSize=30,comment="创建时间")
	private String createTime;
	
	@TableField(comment="领取时间限制，即用户兑换订单生成开始计时超过则视为自动放弃，单位小时，整数")
	private Integer getTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;
	
	
	

	

	public Integer getGetTime() {
		return getTime;
	}

	public void setGetTime(Integer getTime) {
		this.getTime = getTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGoodUUID() {
		return goodUUID;
	}

	public void setGoodUUID(String goodUUID) {
		this.goodUUID = goodUUID;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getIntegration() {
		return integration;
	}

	public void setIntegration(Integer integration) {
		this.integration = integration;
	}

	public String getGoodImg() {
		return goodImg;
	}

	public void setGoodImg(String goodImg) {
		this.goodImg = goodImg;
	}

	public String getSubImgList() {
		return subImgList;
	}

	public void setSubImgList(String subImgList) {
		this.subImgList = subImgList;
	}

	

	public String getDetailImgList() {
		return detailImgList;
	}

	public void setDetailImgList(String detailImgList) {
		this.detailImgList = detailImgList;
	}

	public String getGoodParams() {
		return goodParams;
	}

	public void setGoodParams(String goodParams) {
		this.goodParams = goodParams;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(Integer totalSales) {
		this.totalSales = totalSales;
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

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
