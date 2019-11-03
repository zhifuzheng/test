package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需需求评论
 * @author Administrator
 *
 */

@Table(tableName="gx_demand_comment")
public class GxDemandComment {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String commentUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="vipUUID")
	private String vipUUID;
	
	@TableField(fieldSize=200,isNotNull=true,comment="vipName")
	private String vipName;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="会员头像")
	private String vipLogin;
	
	@TableField(fieldSize=32,isNotNull=true,comment="评论数据UUID")
	private String dataUUID;
	
	@TableField(fieldSize=5000,isNotNull=true,comment="评论内容")
	private String content;
	
	@TableField(fieldSize=200,isNotNull=true,comment="点赞")
	private int give = 0;
	
	@TableField(fieldSize=100,isNotNull=false,comment="时间")
	private String time;

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

	public String getVipLogin() {
		return vipLogin;
	}

	public void setVipLogin(String vipLogin) {
		this.vipLogin = vipLogin;
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

	public int getGive() {
		return give;
	}

	public void setGive(int give) {
		this.give = give;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	

}
