package com.xryb.zhtc.entity;

import java.io.Serializable;

import dbengine.annotation.Table;
import dbengine.annotation.TableField;
/**
 * 实名认证信息
 * @author wf
 */
@Table(tableName="real_name")
public class RealName implements Serializable{
	
	@TableField(isNotField=true)
	private static final long serialVersionUID = -4804684148132277197L;

	@TableField(isPKey=true,isAutoIncrement=true,comment="主键，自动增长")
	private Long id;
	
	@TableField(isNotNull=true,fieldSize=32,comment="全局唯一编号，手动生成")
	private String realNameUUID;
	
	@TableField(isNotNull=true,fieldSize=32,comment="申请人UUID")
	private String personUUID;
	
	@TableField(isNotNull=true,fieldSize=30,comment="申请人登陆名，长度不超过30字符，建议用手机号")
	private String loginName;
	
	@TableField(fieldSize=200,comment="身份证正面照")
	private String idCardPositive;
	
	@TableField(fieldSize=200,comment="身份证反面照")
	private String idCardOppositive;
	
	@TableField(fieldSize=200,comment="手持身份证正面照")
	private String idCardHand;
	
	@TableField(fieldSize=1,comment="身份证审核状态：1未提交，2待审核，3审核中，4审核通过，5审核不通过，默认值是1")
	private String idCardStatus = "1";
	
	@TableField(fieldSize=50,comment="身份证姓名")
	private String idCardName;
	
	@TableField(fieldSize=1,comment="职业跑步运动员：0不是，1是")
	private String isAthlete;
	
	@TableField(fieldSize=1,comment="身份证性别：1男， 2女 ")
	private String idCardSex;
	
	@TableField(fieldSize=10,comment="身份证民族")
	private String idCardMinority;
	
	@TableField(fieldSize=10,comment="身份证出生日期")
	private String idCardBirthday;
	
	@TableField(fieldSize=20,comment="身份证号码")
	private String idCardNumber;
	
	@TableField(fieldSize=100, comment="身份证住址")
	private String idCardAddr;
	
	@TableField(fieldSize=10,comment="身份证有效期限")
	private String idCardExpire;
	
	@TableField(fieldSize=10, comment="现居住城市")
	private String liveProvCity;
	
	@TableField(fieldSize=100, comment="现居住地详细地址")
	private String liveDetailAddr;
	
	@TableField(fieldSize=50, comment="工作单位")
	private String workEnterprise;
	
	@TableField(fieldSize=100, comment="工作地址")
	private String workAddr;
	
	@TableField(fieldSize=100,comment="邮箱")
	private String email;
	
	@TableField(fieldSize=30,comment="QQ号码")
	private String qq;
	
	@TableField(fieldSize=11,comment="手机号码，11位标准手机长度")
	private String mobile;
	
	@TableField(fieldSize=1,comment="实名认证信息审核状态：1未提交，2待审核，3审核中，4审核通过，5审核不通过，默认值是1")
	private String auditInfoStatus = "1";
	
	@TableField(fieldSize=32,comment="审核人UUID")
	private String auditorUUID;
	
	@TableField(fieldSize=100,comment="审核人姓名")
	private String auditorName;
	
	@TableField(fieldSize=200,comment="审批意见")
	private String auditorOpinion;
	
	@TableField(fieldSize=1,comment="审核状态：1未提交，2待审核，3审核中，4审核通过，5审核不通过，默认值是1")
	private String auditStatus = "1";
	
	@TableField(fieldSize=30,comment="审核时间")
	private String auditTime;
	
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

	public String getRealNameUUID() {
		return realNameUUID;
	}

	public void setRealNameUUID(String realNameUUID) {
		this.realNameUUID = realNameUUID;
	}

	public String getPersonUUID() {
		return personUUID;
	}

	public void setPersonUUID(String personUUID) {
		this.personUUID = personUUID;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIdCardPositive() {
		return idCardPositive;
	}

	public void setIdCardPositive(String idCardPositive) {
		this.idCardPositive = idCardPositive;
	}

	public String getIdCardOppositive() {
		return idCardOppositive;
	}

	public void setIdCardOppositive(String idCardOppositive) {
		this.idCardOppositive = idCardOppositive;
	}

	public String getIdCardHand() {
		return idCardHand;
	}

	public void setIdCardHand(String idCardHand) {
		this.idCardHand = idCardHand;
	}

	public String getIdCardStatus() {
		return idCardStatus;
	}

	public void setIdCardStatus(String idCardStatus) {
		this.idCardStatus = idCardStatus;
	}

	public String getIdCardName() {
		return idCardName;
	}

	public void setIdCardName(String idCardName) {
		this.idCardName = idCardName;
	}

	public String getIsAthlete() {
		return isAthlete;
	}

	public void setIsAthlete(String isAthlete) {
		this.isAthlete = isAthlete;
	}

	public String getIdCardSex() {
		return idCardSex;
	}

	public void setIdCardSex(String idCardSex) {
		this.idCardSex = idCardSex;
	}

	public String getIdCardMinority() {
		return idCardMinority;
	}

	public void setIdCardMinority(String idCardMinority) {
		this.idCardMinority = idCardMinority;
	}

	public String getIdCardBirthday() {
		return idCardBirthday;
	}

	public void setIdCardBirthday(String idCardBirthday) {
		this.idCardBirthday = idCardBirthday;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getIdCardAddr() {
		return idCardAddr;
	}

	public void setIdCardAddr(String idCardAddr) {
		this.idCardAddr = idCardAddr;
	}

	public String getIdCardExpire() {
		return idCardExpire;
	}

	public void setIdCardExpire(String idCardExpire) {
		this.idCardExpire = idCardExpire;
	}

	public String getLiveProvCity() {
		return liveProvCity;
	}

	public void setLiveProvCity(String liveProvCity) {
		this.liveProvCity = liveProvCity;
	}

	public String getLiveDetailAddr() {
		return liveDetailAddr;
	}

	public void setLiveDetailAddr(String liveDetailAddr) {
		this.liveDetailAddr = liveDetailAddr;
	}

	public String getWorkEnterprise() {
		return workEnterprise;
	}

	public void setWorkEnterprise(String workEnterprise) {
		this.workEnterprise = workEnterprise;
	}

	public String getWorkAddr() {
		return workAddr;
	}

	public void setWorkAddr(String workAddr) {
		this.workAddr = workAddr;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAuditInfoStatus() {
		return auditInfoStatus;
	}

	public void setAuditInfoStatus(String auditInfoStatus) {
		this.auditInfoStatus = auditInfoStatus;
	}

	public String getAuditorUUID() {
		return auditorUUID;
	}

	public void setAuditorUUID(String auditorUUID) {
		this.auditorUUID = auditorUUID;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditorOpinion() {
		return auditorOpinion;
	}

	public void setAuditorOpinion(String auditorOpinion) {
		this.auditorOpinion = auditorOpinion;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
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
