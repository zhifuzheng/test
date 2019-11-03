package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 地址信息
 * @author wf
 */
@Table(tableName = "addr")
public class Addr implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = 1963133403124183161L;
	
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键id，自动增长")
    private	Long id;
	
	@TableField(isPKey=true,fieldSize=32,isUKey=true,comment="地址UUID")
	private String addrUUID;
	
	@TableField(isPKey=true,fieldSize=32,comment="所属实体UUID")
	private String entityUUID;
	
	@TableField(fieldSize=50,comment="联系人姓名")
	private String name;
	
	@TableField(fieldSize=11,comment="联系电话")
	private String mobile;
	
	@TableField(fieldSize=200,comment="联系地址")
	private String address;
	
	@TableField(fieldSize=20,comment="创建时间")
	private String createTime;
	
	@TableField(fieldSize=20,comment="修改时间")
	private String updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddrUUID() {
		return addrUUID;
	}

	public void setAddrUUID(String addrUUID) {
		this.addrUUID = addrUUID;
	}

	public String getEntityUUID() {
		return entityUUID;
	}

	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
