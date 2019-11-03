package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 幻灯片
 * @author Administrator
 *
 */

@Table(tableName = "slide_img")
public class SlideImg {
	@TableField(isPKey=true, isAutoIncrement=true, comment="主键，自增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="UUID")
	private String slideUUID;
	
	@TableField(isNotNull=true,fieldSize=200,comment="标题")
	private String title;
	
	@TableField(isNotNull=true,fieldSize=200,comment="幻灯片")
	private String slide;
	
	@TableField(isNotNull=true,fieldSize=65534,comment="详情")
	private String details;
	
	@TableField(isNotNull=false,fieldSize=200,comment="备注")
	private String remarks;
	
	@TableField(isNotNull=true,fieldSize=200,comment="时间")
	private String time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSlideUUID() {
		return slideUUID;
	}

	public void setSlideUUID(String slideUUID) {
		this.slideUUID = slideUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlide() {
		return slide;
	}

	public void setSlide(String slide) {
		this.slide = slide;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

}
