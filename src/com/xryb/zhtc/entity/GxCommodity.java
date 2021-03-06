package com.xryb.zhtc.entity;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;

/**
 * 供需管理商品表
 * @author Administrator
 *
 */
@Table(tableName="gx_commodity")
public class GxCommodity {
	@TableField(isPKey=true,isAutoIncrement=true,comment="主键ID,自动增长")
	private Long id;
	
	@TableField(fieldSize=32,isNotNull=true,comment="UUID")
	private String commodityUUID;
	
	@TableField(fieldSize=32,isNotNull=true,comment="vipUUID")
	private String vipUUID;
	
	@TableField(fieldSize=200,isNotNull=true,comment="vipName")
	private String vipName;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="会员头像")
	private String vipLogin;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单编码(每两位为一组)")
	private String menuCode;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单名称")
	private String menuName;
	
	@TableField(fieldSize=100,isNotNull=true,comment="菜单父节")
	private String menuParentCode;
	
	@TableField(fieldSize=5000,isNotNull=true,comment="商品标题")
	private String commodityTitle;
	
	@TableField(fieldSize=5000,isNotNull=true,comment="商品图片")
	private String commodityImg;
	
	@TableField(fieldSize=100,isNotNull=true,comment="商品价格")
	private String price;
	
	@TableField(fieldSize=100,isNotNull=true,comment="商品库存")
	private String stock;
	
	@TableField(fieldSize=100,isNotNull=true,comment="是否包邮（0：包邮，1：不包邮）")
	private String post;
	
	@TableField(fieldSize=100,isNotNull=true,comment="商品状态（0：上架，1：下架）")
	private String state;
	
	@TableField(fieldSize=65534,isNotNull=true,comment="详情")
	private String details;
	
	@TableField(fieldSize=65534,isNotNull=true,comment="详情图片")
	private String detailsImg;
	
	@TableField(fieldSize=5000,isNotNull=false,comment="地址")
	private String address;
	
	@TableField(fieldSize=1000,isNotNull=false,comment="浏览量")
	private int browse = 0;
	
	@TableField(fieldSize=1000,isNotNull=false,comment="收藏量")
	private int collection = 0;
	
	@TableField(fieldSize=100,isNotNull=false,comment="时间")
	private String time;
	
	@TableField(fieldSize=100,isNotNull=true,comment="商品管理（0：启用，1：停用）")
	private String stop;
	
	@TableField(fieldSize=100,isNotNull=true,comment="手机号码")
	private String phone;
	
	@TableField(fieldSize=100,isNotNull=false,comment="商品运费")
	private String postage;
	
	@TableField(fieldSize=1000,isNotNull=true,comment="分享")
	private int share = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommodityUUID() {
		return commodityUUID;
	}

	public void setCommodityUUID(String commodityUUID) {
		this.commodityUUID = commodityUUID;
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

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getCommodityTitle() {
		return commodityTitle;
	}

	public void setCommodityTitle(String commodityTitle) {
		this.commodityTitle = commodityTitle;
	}

	public String getCommodityImg() {
		return commodityImg;
	}

	public void setCommodityImg(String commodityImg) {
		this.commodityImg = commodityImg;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDetailsImg() {
		return detailsImg;
	}

	public void setDetailsImg(String detailsImg) {
		this.detailsImg = detailsImg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBrowse() {
		return browse;
	}

	public void setBrowse(int browse) {
		this.browse = browse;
	}

	public int getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public String getMenuParentCode() {
		return menuParentCode;
	}

	public void setMenuParentCode(String menuParentCode) {
		this.menuParentCode = menuParentCode;
	}
	
	


}
