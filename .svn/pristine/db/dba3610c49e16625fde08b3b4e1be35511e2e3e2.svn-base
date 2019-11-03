package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 账户日志
 * @author wf
 */
@Table(tableName="account_log")
public class AccountLog implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 7636591757501256124L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id，自动增长")
    private	Long id;
	
	@TableField(fieldSize=32,isUKey=true,comment="日志UUID")
	private String logUUID;
	
	@TableField(fieldSize=1,comment="支付渠道：0余额，1微信")
	private String payChannel;
	
	@TableField(isNotNull=true,comment="交易金额")
	private String payment;
	
	@TableField(fieldSize=32,isNotNull=true,comment="微信交易流水号")
	private String tradeNo;
	
	@TableField(fieldSize=32,isNotNull=true,comment="交易订单号")
	private String orderNo;
	
	@TableField(fieldSize=50,isNotNull=true,comment="模块名称")
    private String moduleName;
	
	@TableField(fieldSize=50,isNotNull=true,comment="功能说明")
	private String functionName;
	
	@TableField(fieldSize=500,isNotNull=true,comment="内容")
	private String content;
	
	@TableField(fieldSize=20,comment="支付时间",isNotNull=true)
	private String payTime;
	
	@TableField(fieldSize=20,comment="操作时间")
	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogUUID() {
		return logUUID;
	}

	public void setLogUUID(String logUUID) {
		this.logUUID = logUUID;
	}
	
	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
