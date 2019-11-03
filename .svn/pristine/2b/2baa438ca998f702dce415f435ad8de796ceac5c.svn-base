package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需举报
 * @author Administrator
 *
 */

@Table(tableName="gx_report")
public class GxReport {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String reportUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="举报数据UUID")
	private String dataUUID;
	
	@TableField(fieldSize=5000,isNotNull=true,comment="举报内容")
	private String content;
	
	@TableField(fieldSize=2000,isNotNull=true,comment="举报图片")
	private String img;
	
	@TableField(fieldSize=32,isNotNull=true,comment="举报人UUID")
	private String vipUUID;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="举报人姓名")
	private String vipName;
	
	@TableField(fieldSize=20,isNotNull=true,comment="状态:0:未处理，1:已处理")
	private int state = 0;
	
	@TableField(fieldSize=200,isNotNull=true,comment="时间")
	private String time;
	
	@TableField(fieldSize=20,isNotNull=true,comment="状态:0:供需商品，1:供需需求")
	private String type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportUUID() {
		return reportUUID;
	}

	public void setReportUUID(String reportUUID) {
		this.reportUUID = reportUUID;
	}

	public String getDataUUID() {
		return dataUUID;
	}

	public void setDataUUID(String dataUUID) {
		this.dataUUID = dataUUID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	

}
