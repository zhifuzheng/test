package com.xryb.zhtc.manage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spark.annotation.Auto;
import sun.misc.BASE64Encoder;

import com.xryb.zhtc.entity.MenuInfo;
import com.xryb.zhtc.entity.UserInfo;
import com.xryb.zhtc.service.IMenuInfoService;
import com.xryb.zhtc.service.IMenuUserService;
import com.xryb.zhtc.service.IUserService;
import com.xryb.zhtc.service.impl.MenuInfoServiceImpl;
import com.xryb.zhtc.service.impl.MenuUserServiceImpl;
import com.xryb.zhtc.service.impl.UserServiceImpl;
import com.xryb.zhtc.util.CacheUtil;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.HttpConnectionUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.MD5;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.ReqToEntityUtil;
import com.xryb.zhtc.util.ReqToMapUtil;
import com.xryb.zhtc.util.Uploader;

import dbengine.util.Page;

/**
 * 用户管理
 * @author hshzh
 * 
 */
public class UserMng {
	@Auto(name = UserServiceImpl.class)
	private IUserService userService;

	@Auto(name = MenuInfoServiceImpl.class)
	private IMenuInfoService menuInfoService;
	
	@Auto(name=MenuUserServiceImpl.class)
	private IMenuUserService userMenuService;

	/**
	 * 添加或修改用户信息
	 */
	public boolean addOrUpUser(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String loginName = request.getParameter("loginName");
		UserInfo user = (UserInfo) ReqToEntityUtil.reqToEntity(request, UserInfo.class);
		if (user == null) {
			return false;
		}
		if (user.getId() == null) {
			// 为新增
			//判断账号是否存在
			boolean flag = userService.findLoginNameExist(sourceId, closeConn, loginName);
			if (flag) {
				return false;// 账号已经存在
			}
			//设置userUUID和createTime
			user.setUserUUID(UUID.randomUUID().toString().replaceAll("-", ""));// 生成UUID
			user.setCreateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		}
		return userService.saveOrUpUser(sourceId, closeConn, user);
	}
	/**
	 * 删除用户信息
	 */
	public boolean deleteUser(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String userUUIDS = request.getParameter("userUUIDS");
		String[] delUUIds = userUUIDS.split(",");
		for (String uuid : delUUIds) {
			if (uuid == null || "null".equals(uuid) || "".equals(uuid)) {
				return false;
			} else {
				userService.deleteUser(sourceId, closeConn, uuid);
			}
		}
		return true;
	}

	/**
	 * 修改用户状态
	 */
	public boolean updateUserStatus(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String userUUID = request.getParameter("userUUID");
		String status = request.getParameter("status");
		return userService.updateUserStatus(sourceId, closeConn,userUUID, status);
	}

	/**
	 * 查找用户信息(json字符串) userUUID loginName userPwd
	 */
	public String findUserInfo(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Map findMap = (Map) ReqToMapUtil.reqToMap(request, UserInfo.class);//将request中的参数根据UserInFo转化为Map对象
		UserInfo user = userService.findUserInfo(sourceId, closeConn, findMap);
		if (user == null) {
			return "[]";
		} else {
			return JsonUtil.ObjToJson(user);
		}
	}

	/**
	 * 查找用户信息列表
	 */
	public String findUserInfoList(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		Page page = null;
		String record = request.getParameter("rows");
		String pageNumber = request.getParameter("page");
		if(record != null && !"".equals(record) && pageNumber != null && !"".equals(pageNumber)) {
			page = new Page();
			page.setPageRecord(Integer.parseInt(record));
			page.setPage(Integer.parseInt(pageNumber));
		}
		Map<String, String> findMap = ReqToMapUtil.reqToMap(request, UserInfo.class);
		List<UserInfo> userList = userService.findUserInfoList(sourceId, closeConn, page, findMap);
		Map map = new HashMap();	
		if(page == null) {
			map.put("userList", userList);
			return JsonUtil.ObjToJson(map);
		} else {
			map.put("total", page.getTotalRecord());
			map.put("rows", userList);
			return JsonUtil.ObjToJson(map);
		}
	}

	/**
	 * 用户登陆
	 * boolean closeConn：是否关闭数据库连接
	 */
	@SuppressWarnings({"rawtypes","unchecked"})//抑制多类型警告
	public String userLogin(String sourceId, boolean closeConn, HttpServletRequest request, HttpServletResponse response) {
		String loginName = request.getParameter("username");//登录名
		String userPwd = request.getParameter("password");//登录密码
		String passcode = request.getParameter("passcode");//验证码
		String tokens = request.getParameter("tokens");//令牌对象数据缓存
		Map maps = CacheUtil.getInstance().getTokenObject(tokens);
		Map<String, Object> map = new HashMap<String, Object>();//存放缓存信息
//		if(!passcode.equals(maps.get("strCode"))) {
//			map.put("status", "-1");// 失败，验证码输入错误
//			map.put("msg", "验证码输入错误");
//			return JsonUtil.ObjToJson(map);
//		}
		if (loginName == null || "".equals(loginName)) {// 失败，用户名为空
			map.put("status", "-2");
			map.put("msg", "用户名为空");
			return JsonUtil.ObjToJson(map);
		}
		if (userPwd == null || "".equals(userPwd)) {// 失败，密码为空
			map.put("status", "-3");
			map.put("msg", "密码为空");
			return JsonUtil.ObjToJson(map);
		}
		//创建HashMap,并将登录名和密码（进行加密处理）放入map中
		Map<String, String> findMap = new HashMap<String, String>();
		findMap.put("loginName", loginName);
		findMap.put("userPwd", (new MD5()).getMD5ofStr(userPwd));
		// 查用户信息
		UserInfo user = userService.findUserInfo(sourceId, closeConn, findMap);
		//判断查询到的用户信息是否null
		if (user == null) {//为null，说明用户名或密码错误
			map.put("status", "-4");
			map.put("msg", "用户名或密码错误");
			return JsonUtil.ObjToJson(map);
		}
		//根据查到的用户的status判断用户的当前状态，status：2-用户已注销，3-用户已禁用
		if ("2".equals(user.getUserStatus())) {
			map.put("status", "-5");// 用户已注销
			map.put("msg", "用户已注销");
			return JsonUtil.ObjToJson(map);
		}
		if ("3".equals(user.getUserStatus())) {
			map.put("status", "-6");// 用户已禁用
			map.put("msg", "用户已禁用");
			return JsonUtil.ObjToJson(map);
		}
		// 查登陆人的所有的菜单权限
		List<MenuInfo> miList = userService.findMenuByUserUUID(sourceId, closeConn, user.getUserUUID(), null);
		String permissionStr = "";
		for (MenuInfo mi : miList) {
			if (!"".equals(permissionStr)) {
				permissionStr += "," + mi.getMenuPermission();
			} else {
				permissionStr += mi.getMenuPermission();
			}
		}
		// 将权限信息存入缓存中
		map.put("permissionStr", permissionStr);
		String token = UUID.randomUUID().toString().replaceAll("-", ""); 
		// 将权限信息存入session中
		request.getSession().setAttribute("permissionStr", permissionStr);
		map.put("userinfo", user);
		request.getSession().setAttribute("userinfo", user);
		request.getSession().setAttribute("userName", user.getUserName());//用户名
		request.getSession().setAttribute("userType", user.getUserType());//用户类型
		request.getSession().setAttribute("userUUID", user.getUserUUID());//用户uuid
		map.put("userName", user.getUserName());
		map.put("userType", user.getUserType());
		// 修改用户的登陆时间和次数
		user.setLoginTime(DateTimeUtil.formatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss"));
		user.setLoginNum(user.getLoginNum() + 1);
		userService.saveOrUpUser(sourceId, closeConn, user);
//		HttpConnectionUtil hcu = new HttpConnectionUtil();
//		//远程缓存设置令牌生成时间(以分钟为单位)
//		CacheUtil.getInstance().setTokenTime(token, DateTimeUtil.getTimeInterval(0, (new Date()).getTime()));
//		String url = ReadProperties.getValue("cacheServiceAddr")+"/admin/cache/setTokenTime";
//		Map parameterMap = new HashMap();
//		parameterMap.put("token", token);
//		parameterMap.put("date", DateTimeUtil.getTimeInterval(0, (new Date()).getTime())+"");
//		try {
//			hcu.doPost(url, parameterMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
		//令牌信息存入session
		request.getSession().setAttribute("token", token);
		//令牌存入缓存
		map.put("token", token);
		//缓存设置登陆成功状态
		map.put("status", "1");//登陆成功
		map.put("msg", "登陆成功!");//登陆成功
		//缓存中设置登陆信息对象
//		CacheUtil.getInstance().setTokenObject(token, map);		
//		url = ReadProperties.getValue("cacheServiceAddr")+"/admin/cache/setTokenObject";
//		Map objParameterMap = new HashMap();
//		objParameterMap.put("token", token);
//		objParameterMap.put("objMap", JsonUtil.ObjToJson(map));
//		try {
//			hcu.doPost(url, objParameterMap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
		return JsonUtil.ObjToJson(map);// 登陆成功
	}

	/**
	 * 修改密码
	 */
	public String upUserPwd(String sourceId, boolean closeConn, UserInfo userinfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前ec信息和登陆用户信息
		String inputOldPwd = request.getParameter("oldPwd");//获取输入原始密码
		String newPwd = request.getParameter("newPwd");//获取输入的新密码
		Map map = new HashMap();
		//判断获取的原始密码和新密码是否为null或空
		if (inputOldPwd == null || "".equals(inputOldPwd) || newPwd == null || "".equals(newPwd)) {//true
			map.put("status", "-4");
			map.put("msg", "未获取到任何参数");
			return JsonUtil.ObjToJson(map);
		}
		//判断是否获取到当前用户的用户信息
		if (userinfo == null) {
			map.put("status", "-2");
			map.put("msg", "末登陆或登陆信息失效，请重新登陆！");
			return JsonUtil.ObjToJson(map);
		}
		String userOldPwd = userinfo.getUserPwd();//获取用户的旧密码（该处获取到的旧密码是加密的）
		MD5 md5 = new MD5();
		String inputOldPwdMd5 = md5.getMD5ofStr(inputOldPwd);//将输入的旧密码进行加密
		String newPwdMd5 = md5.getMD5ofStr(newPwd);//将输入的新密码密码进行加密
		//判断输入的旧密码是否正确
		if (!inputOldPwdMd5.equals(userOldPwd)) {
			//不正确
			map.put("status", "-3");
			map.put("msg", "原始密码输入错误！");
			return JsonUtil.ObjToJson(map);
		} else {
			//正确
			//执行密码修改操作
			boolean flag = userService.upUserPwd(sourceId, closeConn, userinfo.getUserUUID(), userOldPwd, newPwdMd5);
			
			if (flag) {
				map.put("status", "1");
				map.put("msg", "修改密码成功！");
				return JsonUtil.ObjToJson(map);
			} else {
				map.put("status", "-1");
				map.put("msg", "修改密码失败！");
				return JsonUtil.ObjToJson(map);
			}
		}
	}

	/**
	 * 初始化密码
	 */
	public String initUserPwd(String sourceId, boolean closeConn, UserInfo userinfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取当前ec信息
		Map map = new HashMap();
		//判断用户信息是否为null
		if (userinfo == null) {
			map.put("status", false);
			map.put("msg", "末登陆或登陆信息失效，请重新登陆！");
			return JsonUtil.ObjToJson(map);
		}
		//获取用户所有的userUUID，并判断是否为null或为空
		String userUUIDs = request.getParameter("userUUIDs");
		if (userUUIDs == null || "".equals(userUUIDs)) {
			map.put("status", false);
			map.put("msg", "userUUIDs参数为空！");
			return JsonUtil.ObjToJson(map);
		}
		//对获取的userUUIDs进行拆分，循环修改用户密码为初始化密码（6个0）（进行加密处理）
		String[] userUUIDList = userUUIDs.split(",");
		for (String userUUID : userUUIDList) {
			MD5 md5 = new MD5();
			String newPwdMd5 = md5.getMD5ofStr("000000");// 初始化密码为“000000”
			userService.upUserPwd(sourceId, closeConn, userUUID, null, newPwdMd5);
		}
		map.put("status", true);
		map.put("msg", "初始化密码成功！");
		
		return JsonUtil.ObjToJson(map);
	}
	
	/**
	 * 查用户登陆名是否存在
	 */
	public boolean findLoginNameExist(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response){
		String loginName = request.getParameter("loginName");
		if(loginName==null || "".equals(loginName)){
			return false;
		}
		return userService.findLoginNameExist(sourceId, closeConn, loginName);
	}
	
    /**
     * 生成验证码
     */
	public String getVCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String abc = request.getParameter("abc");
		int width = 63; //图片的宽
		int height = 37; //图片的高
		Random random = new Random();//用于生成随机参数

		// 设置response头信息
		// 禁止浏览器缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 生成缓冲区image对象
		BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);//相当于一张图片
		// 获得BufferedImage对象的Graphics对象 ，用于绘制操作
		Graphics g = image.getGraphics();
		
		// Graphics类的样式
		//绘制背景
		g.fillRect(0, 0, width, height);//填充矩形，即指定要画的区域
		g.setColor(this.getRandColor(200, 250));//设置颜色
		g.setFont(new Font("Times New Roman", 0, 28));//设置字体，样式，大小

		// 随机绘制40条干扰线，使图像中的验证码不易被其他程序探测到
		//(x,y)起点，偏移量(x1,y1)
		for (int i = 0; i < 40; i++) {
			g.setColor(this.getRandColor(130, 200));//每条干扰线的颜色随机产生
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}

		// 绘制字符（4个字符）
		String strCode = "";//保存生成的验证码
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));//0-9的随机数，然后转化为字符
			strCode = strCode + rand;//验证码的拼接，供验证使用
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色将不同
			g.drawString(rand, 13 * i + 6, 28);//执行绘制，并指定字符的绘制位置
		}
		
		g.dispose();//释放g所占用的资源
		
		//输出内存图片到页面
		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.getOutputStream().flush();//将数据强制输出
		
		HttpConnectionUtil hcu = new HttpConnectionUtil();//?
		
		Map map = new HashMap();
		map.put("tokens", abc);//存放tokens
		map.put("strCode", strCode);//存放验证码
		CacheUtil.getInstance().setTokenObject(abc, map);//设置登陆账号令牌map对象
		
		return strCode;//返回验证码
	}

	/**
	 * 产生随机颜色
	 * @param frontColor
	 * @param backColor
	 * @return
	 */
	Color getRandColor(int frontColor, int backColor) {
		
		Random random = new Random();
		if (frontColor > 255) {
			frontColor = 255;
		}
		if (backColor > 255) {
			backColor = 255;
		}
		int red = frontColor + random.nextInt(backColor - frontColor);
		int green = frontColor + random.nextInt(backColor - frontColor);
		int blue = frontColor + random.nextInt(backColor - frontColor);
		return new Color(red, green, blue);
	}

	/**
	 * 图片转化成base64字符串
	 * @param path 图片的地址
	 * @return
	 */
	private String GetImageStr(String path) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串
		return encoder.encode(data);
	}
		
}
