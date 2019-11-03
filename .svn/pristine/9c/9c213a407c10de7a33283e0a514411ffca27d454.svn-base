package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 购物车信息
 * @author wf
 */
@Table(tableName="cart")
public class Cart implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 8851371527147207793L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="购物车UUID")
	private String cartUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="会员UUID")
	private String vipUUID;
	
	@TableField(fieldSize=8000,comment="购物车数据，json格式")
	private String cartData;
	
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

	public String getCartUUID() {
		return cartUUID;
	}

	public void setCartUUID(String cartUUID) {
		this.cartUUID = cartUUID;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getCartData() {
		return cartData;
	}

	public void setCartData(String cartData) {
		this.cartData = cartData;
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
