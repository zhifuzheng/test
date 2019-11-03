package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.pay.weixin.HttpClientUtil;
import com.xryb.pay.weixin.WeiXinPayConfig;
import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.Bankcard;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.IAccountService;
import com.xryb.zhtc.service.IBankcardService;
import com.xryb.zhtc.service.IRealNameService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.impl.AccountServiceImpl;
import com.xryb.zhtc.service.impl.BankcardServiceImpl;
import com.xryb.zhtc.service.impl.RealNameServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;

import dbengine.util.Page;

import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.MD5;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.KindEditorUtil;
/**
 * vip管理
 * @author wf
 */
@SuppressWarnings("unchecked")
public class VipMng {

	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=VipServiceImpl.class)
	private IVipService vipService;
	
	@Auto(name=BankcardServiceImpl.class)
	private IBankcardService bankcardService;
	
	@Auto(name=RealNameServiceImpl.class)
	private IRealNameService realNameService;
	
	@Auto(name=AccountServiceImpl.class)
	private IAccountService accountService;
	
	/**
	 * pc端会员登陆(通过登录名和密码)
	 */
	public String vipLoginByPc(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginName = request.getParameter("username");
		String vipPwd = request.getParameter("password");
		Map<String, String> result = new HashMap<>();
		if (RegExpUtil.isNullOrEmpty(loginName)) {
			result.put("status", "-1");//失败，会员为空
			result.put("msg", "未输入登录账号！");
			return JsonUtil.ObjToJson(result);
		}
		if (RegExpUtil.isNullOrEmpty(vipPwd)) {
			result.put("status", "-2");// 失败，密码为空
			result.put("msg", "未输入登录密码！");
			return JsonUtil.ObjToJson(result);
		}
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("loginName", loginName);
		findMap.put("vipPwd", (new MD5()).getMD5ofStr(vipPwd));
		// 查会员信息
		VipInfo vip = vipService.findByMap(sourceId, closeConn, findMap);
		if (vip == null) {
			result.put("status", "-3");// 会员名或密码错误
			result.put("msg", "账号与密码不匹配！");
			return JsonUtil.ObjToJson(result);
		}
		if ("2".equals(vip.getVipStatus())) {
			result.put("status", "-4");// 会员已注销
			result.put("msg", "账号已被注销！");
			return JsonUtil.ObjToJson(result);
		}
		if ("3".equals(vip.getVipStatus())) {
			result.put("status", "-5");// 会员已禁用
			result.put("msg", "账号已被禁用！");
			return JsonUtil.ObjToJson(result);
		}
		// 修改会员的登陆时间和次数
		vip.setLoginTime(DateTimeUtil.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		vip.setLoginNum(vip.getLoginNum() + 1);
		vipService.saveOrUpdate(sourceId, closeConn, vip);
		String openId = vip.getOpenId();
		//同步缓存
		if (jedisClient.getVipInfo(sourceId, closeConn, openId) != null) {
			//同步缓存，兼容redis实现
			jedisClient.setVipInfo(openId, vip);
		}
		//将会员信息放入session
		result.put("status", "1");// 会员状态正常
		result.put("msg", "账号状态正常！");
		request.getSession().setAttribute("vipinfo", vip);
		return JsonUtil.ObjToJson(result);//登陆成功
	}
	/**
	 * 修改会员状态
	 */
	public String upStatus(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		String vipStatus = request.getParameter("vipStatus");
		String vipType = request.getParameter("vipType");
		
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到会员信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!RegExpUtil.isNullOrEmpty(vipStatus)){
			vip.setVipStatus(vipStatus);
		}
		if(!RegExpUtil.isNullOrEmpty(vipType)){
			vip.setVipType(vipType);
		}
		vipService.saveOrUpdate(sourceId, closeConn, vip);
		jedisClient.setVipInfo(openId, vip);
		
		result.put("status", "1");
		result.put("msg", "修改会员状态成功");
		return JsonUtil.ObjToJson(result);
		
	}
	/**
	 * 会员注销
	 */
	public String delVip(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到会员信息");
			return JsonUtil.ObjToJson(result);
		}
		String vipUUID = vip.getVipUUID();
		String firstUUID = vip.getFirstUUID();
		String secondUUID = vip.getSecondUUID();
		
		//注销一度人脉
		Map<String, String> findMap = new HashMap<>();
		findMap.put("firstUUID", vipUUID);
		List<VipInfo> firstList = vipService.findListByMap(sourceId, closeConn, findMap, null);
		if(firstList != null){
			for (VipInfo vipInfo : firstList) {
				vipInfo.setFirstUUID(null);
				jedisClient.setVipInfo(vipInfo.getOpenId(), vipInfo);
			}
			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("firstUUID", null);
			vipService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		}
		
		//注销二度人脉
		findMap.remove("firstUUID");
		findMap.put("secondUUID", vipUUID);
		List<VipInfo> secondList = vipService.findListByMap(sourceId, closeConn, findMap, null);
		if(secondList != null){
			for (VipInfo vipInfo : secondList) {
				vipInfo.setSecondUUID(null);
				jedisClient.setVipInfo(vipInfo.getOpenId(), vipInfo);
			}
			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("secondUUID", null);
			vipService.updateByMap(sourceId, closeConn, paramsMap, findMap);
		}
		
		findMap.remove("secondUUID");
		//上级分销商一度人脉数量减一
		if(!RegExpUtil.isNullOrEmpty(firstUUID)){
			Lock lock = jedisClient.getEntityLock(firstUUID);
			lock.lock();
			try {
				findMap.put("vipUUID", firstUUID);
				VipInfo firstVip = vipService.findByMap(sourceId, closeConn, findMap);
				if (firstVip != null) {
					Long firstNum = firstVip.getFirstNum() - 1;
					firstVip.setFirstNum(firstNum);
					Map<String, String> paramsMap = new HashMap<>();
					paramsMap.put("firstNum", firstNum.toString());
					vipService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					jedisClient.setVipInfo(firstVip.getOpenId(), firstVip);
				} 
			} finally {
				lock.unlock();
			}
		}
		
		//上上级分销商二度人脉数量减一
		if(!RegExpUtil.isNullOrEmpty(secondUUID)){
			Lock lock = jedisClient.getEntityLock(secondUUID);
			lock.lock();
			try {
				findMap.put("vipUUID", secondUUID);
				VipInfo secondVip = vipService.findByMap(sourceId, closeConn, findMap);
				if (secondVip != null) {
					Long secondNum = secondVip.getSecondNum() - 1;
					secondVip.setSecondNum(secondNum);
					Map<String, String> paramsMap = new HashMap<>();
					paramsMap.put("secondNum", secondNum.toString());
					vipService.updateByMap(sourceId, closeConn, paramsMap, findMap);
					jedisClient.setVipInfo(secondVip.getOpenId(), secondVip);
				} 
			} finally {
				lock.unlock();
			}
		}
		
		findMap.put("vipUUID", vip.getVipUUID());
		vipService.deleteByMap(sourceId, closeConn, findMap);
		
		result.put("status", "1");
		result.put("msg", "会员注销成功");
		return JsonUtil.ObjToJson(result);
	}
	public String code2session(HttpServletRequest request) throws Exception {
		//通过code获取openId
		String code = request.getParameter("code");
		String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+WeiXinPayConfig.APP_ID+"&secret="+WeiXinPayConfig.APP_SECRET+"&js_code="+code+"&grant_type=authorization_code";
		Map<String,String> result = HttpClientUtil.HttpRequest(requestUrl, "GET", null);
		String openId = result.get("openid");
		//如果获取到openId则生成登录签名
		if(!RegExpUtil.isNullOrEmpty(openId)){
			String loginSign = UUID.randomUUID().toString().replace("-", "") + openId;
			String loginSignCacheName = ReadProperties.getProperty("/redis.properties", "loginSignCache");
			Map<String, String> loginSignCache = jedisClient.getCache(loginSignCacheName);
			if(loginSignCache == null){
				loginSignCache = new HashMap<String, String>();
			}
			loginSignCache.put(code, loginSign);
			jedisClient.setCache(loginSignCacheName, loginSignCache);
			result.put(code, loginSign);
		}
		System.out.println(JsonUtil.ObjToJson(result));
		return JsonUtil.ObjToJson(result);
	}
	public String vipLogin(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取用户信息和登录签名
		String loginSign = request.getParameter("loginSign");
		String code = request.getParameter("code");
		
		Map<String, Object> result = new HashMap<>();
		if(RegExpUtil.isNullOrEmpty(loginSign) || RegExpUtil.isNullOrEmpty(code)){
			result.put("status", "-1");
			result.put("msg", "登录验证信息不完整");
			return JsonUtil.ObjToJson(result);
		}
		
		//验证签名
		String loginSignCacheName = ReadProperties.getProperty("/redis.properties", "loginSignCache");
		Map<String, String> loginSignCache = jedisClient.getCache(loginSignCacheName);
		if(loginSignCache == null || !loginSign.equals(loginSignCache.get(code))){
			result.put("status", "-2");
			result.put("msg", "登录签名验证失败");
			return JsonUtil.ObjToJson(result);
		}
		loginSignCache.remove(code);
		jedisClient.setCache(loginSignCacheName, loginSignCache);
		
		String openId = loginSign.substring(32);
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> findMap = new HashMap<>();
		if(vip == null){
			//用户注册
			String nickName = request.getParameter("nickName");
			String avatarUrl = request.getParameter("avatarUrl");
			String gender = request.getParameter("gender");
			if(RegExpUtil.isNullOrEmpty(nickName) || RegExpUtil.isNullOrEmpty(avatarUrl) || RegExpUtil.isNullOrEmpty(gender)){
				result.put("status", "-3");
				result.put("msg", "注册信息不完整");
				return JsonUtil.ObjToJson(result);
			}
			vip = new VipInfo();
			vip.setOpenId(openId);
			vip.setNickName(nickName);
			vip.setAvatarUrl(avatarUrl);
			vip.setGender(gender);
			vip.setFirstNum(0L);
			vip.setSecondNum(0L);
			vip.setLoginNum(0L);
		}
		
		String firstUUID = request.getParameter("firstUUID");
		if(!RegExpUtil.isNullOrEmpty(firstUUID)){
			VipInfo firstVip = null;
			Lock firstLock = jedisClient.getEntityLock(firstUUID);
			firstLock.lock();
			try {
				findMap.clear();
				findMap.put("vipUUID", firstUUID);
				firstVip = vipService.findByMap(sourceId, closeConn, findMap);
				//不能将自己、自己的上级和已经有上级的会员，发展为自己的下级分销商
				if (firstVip != null && !firstVip.getVipUUID().equals(vip.getVipUUID()) && (vip.getVipUUID() == null || !vip.getVipUUID().equals(firstVip.getFirstUUID())) && RegExpUtil.isNullOrEmpty(vip.getFirstUUID())) {
					firstVip.setFirstNum(firstVip.getFirstNum() + 1);
					vip.setFirstUUID(firstUUID);
					vipService.saveOrUpdate(sourceId, closeConn, firstVip);
					jedisClient.setVipInfo(firstVip.getOpenId(), firstVip);
				} 
			} finally {
				firstLock.unlock();
			}
			
			
			if(firstVip != null && !firstVip.getVipUUID().equals(vip.getVipUUID()) && (vip.getVipUUID() == null || !vip.getVipUUID().equals(firstVip.getFirstUUID()) ) && RegExpUtil.isNullOrEmpty(vip.getFirstUUID())){
				String secondUUID = firstVip.getFirstUUID();
				if(!RegExpUtil.isNullOrEmpty(secondUUID)){
					Lock secondLock = jedisClient.getEntityLock(secondUUID);
					secondLock.lock();
					try {
						findMap.put("vipUUID", secondUUID);
						VipInfo secondVip = vipService.findByMap(sourceId, closeConn, findMap);
						secondVip.setSecondNum(secondVip.getSecondNum() + 1);
						vipService.saveOrUpdate(sourceId, closeConn, secondVip);
						jedisClient.setVipInfo(openId, secondVip);
						vip.setSecondUUID(secondUUID);
					} finally {
						secondLock.unlock();
					}
				}
			}
		}
		Date nowTime = new Date();
		vip.setLoginTime(DateTimeUtil.formatDateTime(nowTime, "yyyy-MM-dd HH:mm:ss"));
		vip.setLoginNum(vip.getLoginNum() + 1);
		//判断会员是否过期
		if("2".equals(vip.getVipType())){
			Date expireTime = DateTimeUtil.parseDate(vip.getExpireTime(), "yyyy-MM-dd HH:mm:ss");
			if(nowTime.before(expireTime)){
				vip.setVipType("1");
			}
		}
		
		//判断会员是否有推广二维码
		if(vip.getQrCode() == null){
			String accessToken = jedisClient.getAccessToken();
			if(accessToken == null){
				result.put("status", "-1");
				result.put("msg", "当前网络繁忙，请稍后重试");
				return JsonUtil.ObjToJson(result);
			}
			String qrCodePath = ReadProperties.getValue("qrCode");//读取会员推广二维码保存路径
			String param = "{\"scene\":\""+vip.getVipUUID()+"\",\"page\":\"pages/index/index\"}";
			Map<String, String> map = HttpClientUtil.genQRCode(request, "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken, param, qrCodePath);
			if(!"1".equals(map.get("status"))){
				result.put("status", "-1");
				result.put("msg", map.get("msg"));
				return JsonUtil.ObjToJson(result);
			}
			String qrCode = map.get("path");
			vip.setQrCode(qrCode);
		}
		
		vipService.saveOrUpdate(sourceId, closeConn, vip);
		jedisClient.setVipInfo(openId, vip);
		
		result.put("status", "1");
		result.put("vipinfo", vip);
		System.out.println(JsonUtil.ObjToJson(vip));
		return JsonUtil.ObjToJson(result);
	}
	public String findVipByOpenId(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, Object> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		result.put("status", "1");
		result.put("vipinfo", vip);
		return JsonUtil.ObjToJson(result);
	}
	public String fileupload(HttpServletRequest request) throws Exception {
		String imgPath = ReadProperties.getValue("portraitPath");//读取会员头像保存路径
		int imgSize = Integer.valueOf(ReadProperties.getValue("portraitImgSize"));//读取会员头像大小限制
		String tmpPath = ReadProperties.getValue("uploadtmp");//临时文件保存的路径
		return KindEditorUtil.fileUpload(request, imgPath, imgSize, null, 0, tmpPath);
	}
	public String vipUpdate(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String,String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未登录");
			return JsonUtil.ObjToJson(result);
		}
		String avatarUrl = request.getParameter("avatarUrl");
		String vipName = request.getParameter("vipName");
		String address = request.getParameter("address");
		String birthday = request.getParameter("birthday");
		String vipMobile = request.getParameter("vipMobile");
		String nickName = request.getParameter("nickName");
		String wxNumber = request.getParameter("wxNumber");
		String gender = request.getParameter("gender");
		String vipType = request.getParameter("vipType");
		String expireTime = request.getParameter("expireTime");
		
		if(!RegExpUtil.isNullOrEmpty(avatarUrl)){
			vip.setAvatarUrl(avatarUrl);
		}
		
		//更新会员信息
		if(!RegExpUtil.isNullOrEmpty(vipName) && !RegExpUtil.isNullOrEmpty(address) && !RegExpUtil.isNullOrEmpty(birthday) && !RegExpUtil.isNullOrEmpty(vipMobile)){
			if(!RegExpUtil.checkMobile(vipMobile)){
				result.put("status", "-2");
				result.put("msg", "手机号码错误");
				return JsonUtil.ObjToJson(result);
			}
			vip.setVipName(vipName);
			vip.setAddress(address);
			vip.setBirthday(birthday);
			if(!vipMobile.equals(vip.getVipMobile())){
				vip.setVipMobile(vipMobile);
				vip.setLoginName(vipMobile);
				vip.setVipPwd(new MD5().getMD5ofStr(vipMobile.substring(5)));
			}
			
			//开通佣金账户
			String vipUUID = vip.getVipUUID();
			Account account = jedisClient.getAccount(sourceId, closeConn, vipUUID);
			if(account == null){
				jedisClient.newAccount(sourceId, closeConn, "3", vipUUID, vipName, vipMobile, vipUUID);
			}
			
			//开通会员钱包
			String walletUUID = "$"+vipUUID.substring(1);
			Account wallet = jedisClient.getAccount(sourceId, closeConn, walletUUID);
			if(wallet == null){
				jedisClient.newAccount(sourceId, closeConn, "2", walletUUID, vipName, vipMobile, vipUUID);
			}
		}
		
		if(!RegExpUtil.isNullOrEmpty(wxNumber)){
			vip.setWxNumber(wxNumber);
		}
		if(!RegExpUtil.isNullOrEmpty(nickName)){
			vip.setNickName(nickName);
		}
		if(!RegExpUtil.isNullOrEmpty(gender)){
			vip.setGender(gender);
		}
		if(!RegExpUtil.isNullOrEmpty(vipType)){
			vip.setVipType(vipType);
		}
		if(!RegExpUtil.isNullOrEmpty(expireTime)){
			vip.setExpireTime(expireTime);
		}
		jedisClient.setVipInfo(openId, vip);
		vipService.saveOrUpdate(sourceId, closeConn, vip);
		
		result.put("status", "1");
		result.put("msg", "更新个人信息成功");
		
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 修改会员密码
	 */
	public String upPwd(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String openId = request.getParameter("openId");
		String oldPwd = request.getParameter("oldPwd");//获取原始密码
		String newPwd = request.getParameter("newPwd");//获取新密码
		
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		Map<String, String> result = new HashMap<>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到会员信息");
			return JsonUtil.ObjToJson(result);
		}
		
		MD5 md5 = new MD5();
		if(!vip.getVipPwd().equals(md5.getMD5ofStr(oldPwd))){
			result.put("status", "-2");
			result.put("msg", "原密码错误");
			return JsonUtil.ObjToJson(result);
		}
		
		vip.setVipPwd(md5.getMD5ofStr(newPwd));
		
		vipService.saveOrUpdate(sourceId, closeConn, vip);
		jedisClient.setVipInfo(openId, vip);
		
		result.put("status", "1");
		result.put("msg", "修改密码成功");
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 分页查询会员列表
	 */
	public String findVipPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, VipInfo.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		vipService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		result.put("page", page);
		List<VipInfo> vipList = (List<VipInfo>) page.getRows();
		if(vipList != null){
			result.put("rows", vipList);
		}else{
			result.put("rows", new ArrayList<VipInfo>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 分页查询分销人员列表
	 */
	public String findDistributePage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, VipInfo.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		vipService.findDistributePage(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		result.put("page", page);
		List<VipInfo> vipList = (List<VipInfo>) page.getRows();
		if(vipList != null){
			result.put("rows", vipList);
		}else{
			result.put("rows", new ArrayList<VipInfo>());
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 银行卡图片上传
	 */
	public String upBankcardImg(HttpServletRequest request) throws Exception {
		String imgPath = ReadProperties.getValue("bankcardPath");//读取银行卡图片保存路径
		int imgSize = Integer.valueOf(ReadProperties.getValue("bankcardSize"));//读取银行卡图片大小限制
		String tmpPath = ReadProperties.getValue("uploadtmp");//临时文件保存的路径
		return KindEditorUtil.fileUpload(request, imgPath, imgSize, null, 0, tmpPath);
	}
	/**
	 * 会员银行卡绑定
	 */
	public String saveBankcard(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Bankcard bankcard = (Bankcard) ReqToEntityUtil.reqToEntity(request, Bankcard.class);
		
		Map<String, String> result = new HashMap<String, String>();
		String cardNumber = request.getParameter("cardNumber");
		if(!RegExpUtil.checkBankCard(cardNumber)){
			result.put("status", "-1");
			result.put("msg", "银行卡号码错误");
			return JsonUtil.ObjToJson(result);
		}
		
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("cardNumber", cardNumber);
		Bankcard find = bankcardService.findByMap(sourceId, closeConn, findMap);
		if(find != null){
			result.put("status", "-2");
			result.put("msg", "银行卡已被绑定");
			return JsonUtil.ObjToJson(result);
		}
		
		String bankcardUUID = UUID.randomUUID().toString().replaceAll("-", "");
		bankcard.setBankcardUUID(bankcardUUID);
		bankcardService.saveOrUpdate(sourceId, closeConn, bankcard);
		
		result.put("status", "1");
		result.put("bankcardUUID", bankcardUUID);
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 会员银行卡解除绑定
	 */
	public boolean delBankcard(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String bankcardUUID = request.getParameter("bankcardUUID");
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("bankcardUUID", bankcardUUID);
		return bankcardService.deleteByMap(sourceId, closeConn, paramsMap);
	}
	/**
	 * 查询会员银行卡
	 */
	public String findBankcard(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Bankcard.class);
		Bankcard bankcard = bankcardService.findByMap(sourceId, closeConn, findMap);
		return JsonUtil.ObjToJson(bankcard);
	}
	/**
	 * 查询会员银行卡列表
	 */
	public String findBankcardList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Bankcard.class);
		List<Bankcard> bankcardList = bankcardService.findListByMap(sourceId, closeConn, findMap, "createTime desc");
		if(bankcardList == null){
			return "[]";
		}
		return JsonUtil.ObjToJson(bankcardList);
	}
	/**
	 * 分页查询会员银行卡列表
	 */
	public String  findBankcardPage(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, Bankcard.class);
		String currentPage = request.getParameter("page");
		String rows = request.getParameter("rows");
		Page page = new Page();
		page.setPage(Long.valueOf(currentPage));
		page.setPageRecord(Long.valueOf(rows));
		String order = request.getParameter("order");
		bankcardService.findPageByMap(sourceId, closeConn, page, findMap, order);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", page.getTotalRecord());
		result.put("totalPage", page.getTotalPage());
		result.put("page", page);
		List<Bankcard> bankcardList = (List<Bankcard>) page.getRows();
		if(bankcardList != null){
			result.put("rows", bankcardList);
		}else{
			result.put("rows", new ArrayList<Bankcard>());
		}
		return JsonUtil.ObjToJson(result);
	}
	
}
