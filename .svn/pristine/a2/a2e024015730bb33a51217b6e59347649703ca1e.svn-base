package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 银行卡信息
 * @author wf
 */
@Table(tableName = "bankcard")
public class Bankcard implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -1420737925543489035L;

	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true, fieldSize=32, comment="全局唯一编号，手动生成")
	private String bankcardUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="所属实体UUID")
	private String entityUUID;
	
	@TableField(fieldSize=50,comment="所属实体名称")
	private String entityName;
	
	@TableField(fieldSize=20,comment="开户行名称")
	private String bankName;
	
	@TableField(fieldSize=20,comment="银行卡类型")
	private String cardType;
	
	@TableField(fieldSize=50,comment="银行卡号码")
	private String cardNumber;
	
	@TableField(fieldSize=1000,comment="银行卡图片")
	private String cardImg;
	
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

	public String getBankcardUUID() {
		return bankcardUUID;
	}

	public void setBankcardUUID(String bankcardUUID) {
		this.bankcardUUID = bankcardUUID;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardImg() {
		return cardImg;
	}

	public void setCardImg(String cardImg) {
		this.cardImg = cardImg;
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
