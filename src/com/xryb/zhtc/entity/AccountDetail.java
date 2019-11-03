package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 账户明细信息
 * @author wf
 */
@Table(tableName = "account_detail")
public class AccountDetail implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 5969933314971870518L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="账户UUID")
	private String accountUUID;
	
	@TableField(fieldSize=32,comment="订单号")
	private String orderNo;
	
	@TableField(fieldSize=1,isNotNull=true,comment="收支类型：1收入，2支出")
	private String detailType;
	
	@TableField(isNotNull=true,comment="收入为正，支出为负")
	private Integer money;
	
	@TableField(fieldSize=1,comment="支付渠道：0余额，1微信")
	private String payChannel;
	
	@TableField(fieldSize=1,comment="明细来源：1充值消费，2购物消费，3进货，4提现，5商家退款，6分销商退款，7提现失败退款，8交易收入，9其他")
	private String detailSource;
	
	@TableField(fieldSize=32,comment="付款人UUID")
	private String payerUUID;
	
	@TableField(fieldSize=100,comment="付款人名称")
	private String payerName;
	
	@TableField(fieldSize=1000,comment="付款人logo")
	private String payerLogo;
	
	@TableField(fieldSize=20,comment="创建时间",isNotNull=true)
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountUUID() {
		return accountUUID;
	}

	public void setAccountUUID(String accountUUID) {
		this.accountUUID = accountUUID;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getDetailSource() {
		return detailSource;
	}

	public void setDetailSource(String detailSource) {
		this.detailSource = detailSource;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
