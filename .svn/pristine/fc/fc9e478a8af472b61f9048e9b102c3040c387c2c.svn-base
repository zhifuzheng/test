package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 分销商设置
 * @author Administrator
 *
 */

@Table(tableName = "business_distribution")
public class BusinessDistribution {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String distributionUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="商家UUID")
	private String businessUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="是否开启分销(0:开启，1:关闭 )")
	private String openDistribution;
	
	@TableField(isNotNull=false,fieldSize=32,comment="设置分销商层级(0:一级，1:二级)")
	private String hierarchy;
	
	@TableField(isNotNull=false,fieldSize=32,comment="一级佣金比列")
	private String onPercentage;
	
	@TableField(isNotNull=false,fieldSize=32,comment="二级佣金比列")
	private String twoPercentage;
	
	@TableField(isNotNull=false,fieldSize=200,comment="时间")
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistributionUUID() {
		return distributionUUID;
	}

	public void setDistributionUUID(String distributionUUID) {
		this.distributionUUID = distributionUUID;
	}

	public String getBusinessUUID() {
		return businessUUID;
	}

	public void setBusinessUUID(String businessUUID) {
		this.businessUUID = businessUUID;
	}

	public String getOpenDistribution() {
		return openDistribution;
	}

	public void setOpenDistribution(String openDistribution) {
		this.openDistribution = openDistribution;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	public String getOnPercentage() {
		return onPercentage;
	}

	public void setOnPercentage(String onPercentage) {
		this.onPercentage = onPercentage;
	}

	public String getTwoPercentage() {
		return twoPercentage;
	}

	public void setTwoPercentage(String twoPercentage) {
		this.twoPercentage = twoPercentage;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
