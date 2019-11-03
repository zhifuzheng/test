package com.xryb.zhtc.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本树结构，用于生成页面树
 * @author hshzh
 */


public class TreeBean implements Serializable {
	private String id;//节点ID
	private String pId;//父节点ID
	private String text;//节点名称
	private String state;//节点状态(展开|关闭,对应open|closed)
	private String iconCls;//节点图标
	private boolean checked;//节点选中状态
	private Map<String, String> attributes = new HashMap<String, String>();//节点其它属性，可根据需要带相关参数
	private List<TreeBean> children = new ArrayList<TreeBean>();//子节点(存放直接下级子节点)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIconCls() {
		return iconCls;
	}
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public List<TreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<TreeBean> children) {
		this.children = children;
	}

}
