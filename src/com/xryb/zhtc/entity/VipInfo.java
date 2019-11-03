package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 会员信息
 * @author wf
 */
@Table(tableName="vip_info")
public class VipInfo implements Serializable {
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -2518250199676633756L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，UUID自动生成")
	private String vipUUID;
	
	@TableField(isNotNull=true,fieldSize=128,comment="微信账号对应的openId")
	private String openId;
	
	@TableField(fieldSize=36,comment="上级分销商UUID")
	private String firstUUID;
	
	@TableField(fieldSize=36,comment="上上级分销商UUID")
	private String secondUUID;
	
	@TableField(comment="下级分销商数量")
	private Long firstNum;
	
	@TableField(comment="下下级分销商数量")
	private Long secondNum;
	
	@TableField(fieldSize=30,comment="登陆名，长度不超过30字符，建议用手机号")
	private String loginName;
	
	@TableField(fieldSize=36,comment="登陆密码，md5加密")
	private String vipPwd;
	
	@TableField(fieldSize=50,comment="昵称")
	private String nickName;
	
	@TableField(fieldSize=200,comment="会员头像，存放绝对地址")
	private String avatarUrl;
	
	@TableField(fieldSize=1,comment="性别 ：1男，2女")
	private String gender;
	
	@TableField(fieldSize=11,comment="会员手机，11位标准手机长度，全局唯一")
	private String vipMobile;
	
	@TableField(fieldSize=100,comment="邮箱")
	private String vipEmail;
	
	@TableField(fieldSize=100,comment="会员姓名")
	private String vipName;
	
	@TableField(fieldSize=30,comment="会员生日")
	private String birthday;
	
	@TableField(fieldSize=100,comment="住址")
	private String address;
	
	@TableField(fieldSize=50,comment="微信号码")
	private String wxNumber;
	
	@TableField(fieldSize=2,isNotNull=true,comment="会员类型：1普通会员， 2付费会员，3平台管理员，默认值是1")
	private String vipType = "1";
	
	@TableField(fieldSize=2,comment="状态：0停用，1正常，默认值是1")
	private String vipStatus = "1";
	
	@TableField(fieldSize=1,isNotNull=true,comment="实名认证状态：0未认证，1未提交，2待审核，3审核中，4通过，5驳回，默认值是0")
	private String realNameStatus = "0";
	
	@TableField(fieldSize=50,comment="登陆时间")
	private String loginTime;
	
	@TableField(comment="登陆次数")
	private Long loginNum;
	
	@TableField(fieldSize=200,comment="会员推广二维码，存放相对地址")
	private String qrCode;
	
	@TableField(fieldSize=50,comment="会员过期时间")
	private String expireTime;
	
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

	public String getVipUUID() {
		return vipUUID;
	}

	public void setVipUUID(String vipUUID) {
		this.vipUUID = vipUUID;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getFirstUUID() {
		return firstUUID;
	}

	public void setFirstUUID(String firstUUID) {
		this.firstUUID = firstUUID;
	}

	public String getSecondUUID() {
		return secondUUID;
	}

	public void setSecondUUID(String secondUUID) {
		this.secondUUID = secondUUID;
	}

	public Long getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(Long firstNum) {
		this.firstNum = firstNum;
	}

	public Long getSecondNum() {
		return secondNum;
	}

	public void setSecondNum(Long secondNum) {
		this.secondNum = secondNum;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getVipPwd() {
		return vipPwd;
	}

	public void setVipPwd(String vipPwd) {
		this.vipPwd = vipPwd;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getVipMobile() {
		return vipMobile;
	}

	public void setVipMobile(String vipMobile) {
		this.vipMobile = vipMobile;
	}

	public String getVipEmail() {
		return vipEmail;
	}

	public void setVipEmail(String vipEmail) {
		this.vipEmail = vipEmail;
	}

	public String getVipName() {
		return vipName;
	}

	public void setVipName(String vipName) {
		this.vipName = vipName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWxNumber() {
		return wxNumber;
	}

	public void setWxNumber(String wxNumber) {
		this.wxNumber = wxNumber;
	}

	public String getVipType() {
		return vipType;
	}

	public void setVipType(String vipType) {
		this.vipType = vipType;
	}

	public String getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
	}

	public String getRealNameStatus() {
		return realNameStatus;
	}

	public void setRealNameStatus(String realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public Long getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Long loginNum) {
		this.loginNum = loginNum;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
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
