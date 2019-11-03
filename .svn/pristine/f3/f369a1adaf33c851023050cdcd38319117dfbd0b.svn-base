package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 提现申请信息
 * @author wf
 */
@Table(tableName = "withdrawal")
public class Withdrawal implements Serializable {

	@TableField(isNotField=true)
	private static final long serialVersionUID = -7735380857952264315L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,isUKey=true,comment="全局唯一编号，手动生成")
	private String withdrawalUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="账户UUID")
	private String accountUUID;
	
	@TableField(fieldSize=1,isNotNull=true,comment="账户类型：0零售商账户，1批发商账户，2会员账户，3佣金账户，4平台账户")
	private String accountType;
	
	@TableField(fieldSize=11,comment="账户绑定手机号，11位标准手机长度")
	private String mobile;
	
	@TableField(fieldSize=32,isNotNull=true,comment="账户所属实体UUID")
	private String entityUUID;
	
	@TableField(fieldSize=100,isNotNull=true,comment="账户所属实体名称")
	private String entityName;
	
	@TableField(isNotNull=true,comment = "提现金额")
	private Integer payment;
	
	@TableField(fieldSize=1,comment="提现方式：1微信，2银行卡")
	private String channel;
	
	@TableField(fieldSize=50,comment="银行卡开户行")
	private String bankName;
	
	@TableField(fieldSize=50,comment="银行卡类型")
	private String cardType;
	
	@TableField(fieldSize=50,comment="银行卡号")
	private String cardNumber;
	
	@TableField(isNotNull=true,fieldSize=32,comment="申请人UUID")
	private String personUUID;
	
	@TableField(isNotNull=true,fieldSize=128,comment="申请人openId")
	private String openId;
	
	@TableField(isNotNull=true,fieldSize=30,comment="申请人姓名")
	private String personName;
	
	@TableField(isNotNull=true,fieldSize=1000,comment="申请人logo")
	private String personLogo;
	
	@TableField(fieldSize = 1, comment = "审核状态：1待审核，2审核中，3通过，4驳回，默认值为1")
	private String auditStatus = "1";
	
	@TableField(fieldSize=32,comment="审核人UUID")
	private String auditorUUID;
	
	@TableField(fieldSize=100,comment="审核人姓名")
	private String auditorName;
	
	@TableField(fieldSize=200,comment="审批意见")
	private String auditorOpinion;
	
	@TableField(fieldSize=200,comment="审核时间")
	private String auditTime;
	
	@TableField(fieldSize=30,comment="申请时间")
	private String createTime;
	
	@TableField(fieldSize=30,comment="修改时间")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWithdrawalUUID() {
		return withdrawalUUID;
	}

	public void setWithdrawalUUID(String withdrawalUUID) {
		this.withdrawalUUID = withdrawalUUID;
	}

	public String getAccountUUID() {
		return accountUUID;
	}

	public void setAccountUUID(String accountUUID) {
		this.accountUUID = accountUUID;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Integer getPayment() {
		return payment;
	}

	public void setPayment(Integer payment) {
		this.payment = payment;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
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

	public String getPersonUUID() {
		return personUUID;
	}

	public void setPersonUUID(String personUUID) {
		this.personUUID = personUUID;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonLogo() {
		return personLogo;
	}

	public void setPersonLogo(String personLogo) {
		this.personLogo = personLogo;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditorUUID() {
		return auditorUUID;
	}

	public void setAuditorUUID(String auditorUUID) {
		this.auditorUUID = auditorUUID;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditorOpinion() {
		return auditorOpinion;
	}

	public void setAuditorOpinion(String auditorOpinion) {
		this.auditorOpinion = auditorOpinion;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
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
