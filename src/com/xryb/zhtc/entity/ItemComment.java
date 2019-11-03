package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 商品评论信息
 * @author wf
 */
@Table(tableName="item_comment")
public class ItemComment implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 7400800772067286870L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，UUID自动生成")
	private String commentUUID;
	
	@TableField(fieldSize=32,comment="订单UUID")
	private String orderUUID;
	
	@TableField(fieldSize=32,comment="店铺UUID")
	private String entityUUID;
	
	@TableField(fieldSize=128,comment="店铺名称")
	private String entityName;
	
	@TableField(fieldSize=1000,comment="店铺logo")
	private String entityLogo;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商品UUID")
	private String itemUUID;
	
	@TableField(fieldSize=32,comment="商品规格UUID")
	private String paramUUID;
	
	@TableField(fieldSize=200,comment="规格名称")
	private String title;
	
	@TableField(fieldSize=32,comment="评论人UUID")
	private String payerUUID;
	
	@TableField(fieldSize=30,comment="评论人名称")
	private String payerName;
	
	@TableField(fieldSize=200,comment="评论人头像，存放相对地址")
	private String payerLogo;
	
	@TableField(fieldSize=1,comment="匿名状态：0不使用匿名，1使用匿名，默认值为0")
	private String anonym = "0";
	
	@TableField(comment="评分，1~5的整数，默认值为5分")
	private Integer score = 5;
	
	@TableField(fieldSize=300,comment="评论信息")
	private String comment;
	
	@TableField(fieldSize=8000,comment="图片路径列表，以逗号分割")
	private String imgList;
	
	@TableField(fieldSize=1,comment="状态：1正常，2违规，默认值为1")
	private String status = "1";
	
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

	public String getCommentUUID() {
		return commentUUID;
	}

	public void setCommentUUID(String commentUUID) {
		this.commentUUID = commentUUID;
	}

	public String getOrderUUID() {
		return orderUUID;
	}

	public void setOrderUUID(String orderUUID) {
		this.orderUUID = orderUUID;
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

	public String getItemUUID() {
		return itemUUID;
	}

	public void setItemUUID(String itemUUID) {
		this.itemUUID = itemUUID;
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

	public String getAnonym() {
		return anonym;
	}

	public void setAnonym(String anonym) {
		this.anonym = anonym;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getImgList() {
		return imgList;
	}

	public void setImgList(String imgList) {
		this.imgList = imgList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
