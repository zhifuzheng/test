package com.xryb.zhtc.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.pay.weixin.HttpClientUtil;
import com.xryb.pay.weixin.MD5Util;
import com.xryb.pay.weixin.WeiXinPayConfig;
import com.xryb.pay.weixin.WeiXinPayUtil;
import com.xryb.zhtc.advice.IOrderAdvice;
import com.xryb.zhtc.entity.Account;
import com.xryb.zhtc.entity.AccountDetail;
import com.xryb.zhtc.entity.AccountLog;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.factory.AdviceFactory;
import com.xryb.zhtc.service.IAccountDetailService;
import com.xryb.zhtc.service.IAccountLogService;
import com.xryb.zhtc.service.IAccountService;
import com.xryb.zhtc.service.IItemParamService;
import com.xryb.zhtc.service.IItemService;
import com.xryb.zhtc.service.IOrderItemService;
import com.xryb.zhtc.service.IOrderService;
import com.xryb.zhtc.service.impl.AccountDetailServiceImpl;
import com.xryb.zhtc.service.impl.AccountLogServiceImpl;
import com.xryb.zhtc.service.impl.AccountServiceImpl;
import com.xryb.zhtc.service.impl.ItemParamServiceImpl;
import com.xryb.zhtc.service.impl.ItemServiceImpl;
import com.xryb.zhtc.service.impl.OrderItemServiceImpl;
import com.xryb.zhtc.service.impl.OrderServiceImpl;
import com.xryb.zhtc.util.IpUtil;
import com.xryb.zhtc.util.JsonUtil;
import com.xryb.zhtc.util.AccountUtil;
import com.xryb.zhtc.util.DateTimeUtil;
import com.xryb.zhtc.util.ReadProperties;
import com.xryb.zhtc.util.RegExpUtil;

import spark.annotation.Auto;
/**
 * 微信支付管理
 * @author wf
 */
public class WeiXinPayMng {
	
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	
	@Auto(name=OrderServiceImpl.class)
	private IOrderService orderService;
	
	@Auto(name=OrderItemServiceImpl.class)
	private IOrderItemService orderItemService;
	
	@Auto(name=ItemServiceImpl.class)
	private IItemService itemService;
	
	@Auto(name=ItemParamServiceImpl.class)
	private IItemParamService paramService;
	
	@Auto(name=AccountLogServiceImpl.class)
	private IAccountLogService logService;
	
	@Auto(name=AccountServiceImpl.class)
	private IAccountService accountService;
	
	@Auto(name=AccountDetailServiceImpl.class)
	private IAccountDetailService detailService;
	
	/**
	 * 微信支付返回预支付数据
	 */
	public String preOrder(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {		
		String openId = request.getParameter("openId");
		VipInfo vip = jedisClient.getVipInfo(sourceId, closeConn, openId);
		//使用result封装返回结果
		Map<String, Object> result = new HashMap<String, Object>();
		if(vip == null){
			result.put("status", "-1");
			result.put("msg", "未授权登录");
			return JsonUtil.ObjToJson(result);
		}
		//商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号，必填
		String out_trade_no = request.getParameter("orderUUID");
		if(out_trade_no == null){
			result.put("status", "-2");
			result.put("msg", "没有获取到订单编号！");
			return JsonUtil.ObjToJson(result);
		}
		//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP，必填
	    String spbill_create_ip = IpUtil.getIp2(request);
	    //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据，非必填
	    String attach = "";
		Map<String, String> findMap = new HashMap<String, String>();
		//获取订单信息
		if(out_trade_no.indexOf("_") == -1){//单笔订单处理
			findMap.put("orderUUID", out_trade_no);
			Order order = orderService.findByMap(sourceId, closeConn, findMap);
			if(order == null){
				result.put("status", "-3");
				result.put("msg", "不存在编号为\""+out_trade_no+"\"的订单！");
				return JsonUtil.ObjToJson(result);
			}
			if(!RegExpUtil.isNullOrEmpty(order.getBatchNo())){
				Map<String, String> paramsMap = new HashMap<>();
				paramsMap.put("batchNo", null);
				orderService.updateByMap(sourceId, closeConn, paramsMap, findMap);
				orderItemService.updateByMap(sourceId, closeConn, paramsMap, findMap);
			}
			//付款金额，必填，单位分
			String total_fee = order.getPayment().toString();
			//商品或支付单简要描述，必填
			String body = order.getTitle();
			genWxAppPayStr(result, body, spbill_create_ip, total_fee, openId, out_trade_no, attach);
		}else{//多笔订单处理
			findMap.put("batchNo", out_trade_no);
			List<Order> orderList = orderService.findListByMap(sourceId, closeConn, findMap, null);
			if(orderList == null || orderList.size() < 1){
				result.put("status", "-4");
				result.put("msg", "不存在批次号为\""+out_trade_no+"\"的订单！");
				return JsonUtil.ObjToJson(result);
			}
			Integer total = 0;
			for(Order order : orderList){
				total += order.getPayment();
			}
			//付款金额，必填，单位分
			String total_fee = total.toString();
			//商品或支付单简要描述，必填
			String body = "优好采购";
			genWxAppPayStr(result, body, spbill_create_ip, total_fee, openId, out_trade_no, attach);
		}
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 微信支付回调
	 */
	public byte[] notify(String sourceId, boolean closeConn,HttpServletRequest request, HttpServletResponse response) throws Exception {
		//解析回调xml报文
		String inputLine;
		String notityXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> m = WeiXinPayUtil.xmlToMap(notityXml);
		//支付成功报文
		String successXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		//支付失败报文
		String failXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
		if("SUCCESS".equals(m.get("result_code"))){
			//商户订单号
			String out_trade_no = m.get("out_trade_no");
			//微信支付订单号
			String transaction_id = m.get("transaction_id");
			//支付金额
			String cash_fee = m.get("cash_fee");
			//分转化为元
			String total_fee = AccountUtil.pointMove(cash_fee, -2, 2);
			//支付时间
			String time_end = new StringBuffer(m.get("time_end")).insert(4, "-").insert(7, "-").insert(10, " ").insert(13, ":").insert(16, ":").toString();
			//微信支付分配的商户号
			String mch_id = m.get("mch_id");
			
			//创建账户日志
			AccountLog log = new AccountLog();
			log.setLogUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setPayChannel("1");
			log.setPayment(cash_fee);
			log.setTradeNo(transaction_id);
			log.setOrderNo(out_trade_no);
			log.setModuleName("小程序支付");
			log.setFunctionName("微信支付回调");
			log.setPayTime(time_end);
			
			//验证是否是微信调用
		    SortedMap<String, String> packageParams = new TreeMap<String, String>(m);
			String newSign = WeiXinPayUtil.createSign(packageParams);
			if(!newSign.equals(m.get("sign"))){
				log.setContent("订单数据验签失败，可能发生了数据篡改");
				logService.saveOrUpdate(sourceId, closeConn, log);
				return successXml.getBytes();
			}
			
			//验证是否是向本平台付款
			if(!WeiXinPayConfig.PARTNER.equals(mch_id)){
				StringBuffer content = new StringBuffer("不是向本平台付款，而是支付给了编号为");
				content.append(mch_id).append("的商户，可能是服务器地址受到了网络攻击");
				log.setContent(content.toString());
				logService.saveOrUpdate(sourceId, closeConn, log);
				return successXml.getBytes();
			}
			
			Map<String, String> findMap = new HashMap<String, String>();
			if(out_trade_no.indexOf("_") == -1){//处理单笔订单
				findMap.put("orderUUID", out_trade_no);
			}else{//处理多笔订单
				findMap.put("batchNo", out_trade_no);
			}
			List<Order> orderList = orderService.findListByMap(sourceId, closeConn, findMap, null);
			if(orderList == null || orderList.size() < 1){
				log.setContent("数据库中查无此订单，可能发生了数据丢失");
				logService.saveOrUpdate(sourceId, closeConn, log);
				return successXml.getBytes();
			}
			/*Integer total = 0;
			for(Order order : orderList){
				order.setPayTime(time_end);
				total += order.getPayment();
			}
			String payment = total.toString();
			//验证订单金额
			if(!cash_fee.equals(payment)){
				StringBuffer content = new StringBuffer("支付金额");
				content.append(total_fee).append("元与订单金额").append(AccountUtil.pointMove(payment, -2, 2)).append("元不匹配，数据库可能发生了数据篡改");
				logService.saveOrUpdate(sourceId, closeConn, log);
				return successXml.getBytes();
			}*/
			List<OrderItem> itemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
			allocateAmount(sourceId, closeConn, orderList, itemList);
			//执行订单支付成功通知
			String advice = orderList.get(0).getAdvice();
			if(!RegExpUtil.isNullOrEmpty(advice)){
				String className = ReadProperties.getProperty("/advice.properties", advice);
				if(!RegExpUtil.isNullOrEmpty(advice)){
					try {
						IOrderAdvice orderAdvice = AdviceFactory.genSingleAdvice(className);
						if(!orderAdvice.payAdvice(sourceId, closeConn, orderList, itemList)){
							System.out.println("通知订单支付成功失败！编号为\"" + advice + "\"");
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("通知订单支付成功失败！编号为\"" + advice + "\"");
					}
				}
			}
			return successXml.getBytes();
		}
		return failXml.getBytes();
	}
	/**
	 * 生成微信app支付请求字符串
	 */
	public void genWxAppPayStr(Map<String, Object> result, String body, String spbill_create_ip, String total_fee, String openid, String out_trade_no, String attach){
		total_fee = "1";
		
		//微信分配的开发平台appid（企业号（corpid为此appId），必填 
		String appid = WeiXinPayConfig.APP_ID;
		//微信支付分配的商户号，必填
	    String mch_id = WeiXinPayConfig.PARTNER;
	    //随机字符串，不长于32位。推荐随机数生成算法，必填
	    String nonce_str = WeiXinPayUtil.getNonceStr();
	    //接收微信支付异步通知回调地址，必填
	    String notify_url =  ReadProperties.getValue("weiXin_notifyUrl");
	    //签名，详见签名生成算法，必填
	    String sign = "";
	    //取值如下：JSAPI，NATIVE，APP，详细说明见参数规定，必填
	    String trade_type = "JSAPI";
	    
	    //获取预支付订单号
	    SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("openid", openid);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		
		//支付签名
		sign = WeiXinPayUtil.createSign(packageParams);
		String xml = "<xml>" + 
						"<appid>" + appid + "</appid>" + 
						"<mch_id>"+ mch_id + "</mch_id>" + 
						"<nonce_str>" + nonce_str + "</nonce_str>" + 
						"<sign>" + sign + "</sign>" + 
						"<body><![CDATA[" + body + "]]></body>" + 
						"<openid>" + openid + "</openid>" + 
						"<out_trade_no>" + out_trade_no + "</out_trade_no>" + 
						"<attach>" + attach + "</attach>" + 
						"<total_fee>" + total_fee + "</total_fee>" + 
						"<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" + 
						"<notify_url>" + notify_url + "</notify_url>" + 
						"<trade_type>" + trade_type + "</trade_type>" + 
					"</xml>";
		
		Map<String, String> map = null;
	    String prepay_id = "";
		try {
			//发送请求， 获取预支付id
			map = HttpClientUtil.HttpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", xml);
			if(map != null){
				prepay_id = map.get("prepay_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "-1");
			result.put("msg", "请求微信预支付订单号失败！");
		}
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		//签名
		String s = "appId=" + appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=MD5" + "&timeStamp=" + timestamp +"&key=" + WeiXinPayConfig.PARTNER_KEY;
	    String newSign = MD5Util.MD5Encode(s, "UTF-8").toUpperCase();
	   
	    SortedMap<String, String> m = new TreeMap<String, String>();
	    if("SUCCESS".equals(map.get("return_code"))){//预计支付成功
		    m.put("appid", appid);
		    m.put("prepayid", prepay_id);
		    m.put("noncestr", nonce_str);
		    m.put("timestamp", timestamp);
		    m.put("sign", newSign);
		    result.put("status", "1");
		    result.put("msg", m);
	    }else {
	    	result.put("status", "-2");
	    	result.put("msg", "生成微信预支付订单失败！");
	    }
	}
	/**
	 * 分配金额
	 */
	public void allocateAmount(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList) throws Exception {
		Map<String, Map<String, Integer>> allocation = new HashMap<>();
		Map<String, List<OrderItem>> entity = new HashMap<>();
		List<AccountDetail> detailList = new ArrayList<>();
		String firstUUID = orderList.get(0).getFirstUUID();
		String secondUUID = orderList.get(0).getSecondUUID();
		Integer firstTotal = 0;
		Integer secondTotal = 0;
		String orderType = orderList.get(0).getOrderType();
		String payerUUID = orderList.get(0).getPayerUUID();
		String payerName = orderList.get(0).getPayerName();
		String payerLogo = orderList.get(0).getPayerLogo();
		
		//计算每种商品一级分销商和二级分销商提成
		for (OrderItem item : itemList) {
			if(!"0".equals(item.getIsPay())){
				continue;
			}
			item.setIsPay("1");
			item.setOrderStatus("1");
			if("1".equals(orderType) || "4".equals(orderType)){
				item.setOrderStatus("4");
			}
			
			String orderUUID = item.getOrderUUID();
			List<OrderItem> subItemList = entity.get(orderUUID);
			if(subItemList == null){
				subItemList = new ArrayList<>();
				entity.put(orderUUID, subItemList);
			}
			subItemList.add(item);
			
			String distributeStatus = item.getDistributeStatus();
			if(distributeStatus.equals("1")){
				Map<String, Integer> sub = allocation.get(orderUUID);
				if(sub == null){
					sub = new HashMap<>();
					allocation.put(orderUUID, sub);
				}
				
				Double firstRatio = item.getFirstRatio();
				if(firstUUID != null && firstRatio > 0){
					item.setFirstIncome((int) (item.getPayment()*firstRatio*item.getCount()));
					Integer firstIncome = sub.get(firstUUID);
					if(firstIncome == null){
						firstIncome = item.getFirstIncome();
					}else{
						firstIncome += item.getFirstIncome();
					}
					sub.put(firstUUID, firstIncome);
					
					Double secondRatio = item.getSecondRatio();
					if(secondUUID != null && secondRatio > 0){
						item.setSecondIncome((int) (item.getPayment()*secondRatio*item.getCount()));
						Integer secondIncome = sub.get(secondUUID);
						if(secondIncome == null){
							secondIncome = item.getSecondIncome();
						}else{
							secondIncome += item.getSecondIncome();
						}
						sub.put(secondUUID, secondIncome);
					}
				}
			}
		}
		
		//计算每笔订单商家、一级分销商和二级分销商提成
		for (Order order : orderList) {
			if(!"0".equals(order.getOrderStatus())){
				continue;
			}
			order.setIsPay("1");
			order.setOrderStatus("1");
			if("1".equals(orderType) || "4".equals(orderType)){
				order.setOrderStatus("4");
				order.setEndTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			order.setPayTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			
			Integer entityIncome = order.getPayment();
			String orderUUID = order.getOrderUUID();
			Map<String, Integer> sub = allocation.get(orderUUID);
			if(sub != null){
				Integer firstIncome = sub.get(firstUUID);
				if(firstIncome != null && firstIncome > 0){
					firstTotal += firstIncome;
					entityIncome -= firstIncome;
					order.setFirstIncome(firstIncome);
				}
				Integer secondIncome = sub.get(secondUUID);
				if(secondIncome != null && secondIncome > 0){
					secondTotal += secondIncome;
					entityIncome -= secondIncome;
					order.setSecondIncome(secondIncome);
				}
			}
			
			//生成商家账户明细
			Account entityAccount = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
			AccountDetail entityDetail = new AccountDetail();
			entityDetail.setAccountUUID(entityAccount.getAccountUUID());
			entityDetail.setDetailType("1");//收支类型：1收入，2支出
			entityDetail.setMoney(entityIncome);
			entityDetail.setOrderNo(orderUUID);
			entityDetail.setPayChannel("1");//支付渠道：0余额，1微信
			entityDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
			entityDetail.setPayerUUID(payerUUID);
			entityDetail.setPayerName(payerName);
			entityDetail.setPayerLogo(payerLogo);
			if("1".equals(order.getOrderType())){//充值类型的账户明细，名称和图片需要特殊处理
				entityDetail.setPayerName(order.getEntityName());
				entityDetail.setPayerLogo(order.getEntityLogo());
			}
			detailList.add(entityDetail);
			
			if(RegExpUtil.isNullOrEmpty(firstUUID) && order.getFirstIncome() > 0){
				//生成一级分销商账户明细
				Account firstAccount = jedisClient.getAccount(sourceId, closeConn, firstUUID);
				AccountDetail firstDetail = new AccountDetail();
				firstDetail.setAccountUUID(firstAccount.getAccountUUID());
				firstDetail.setDetailType("1");//收支类型：1收入，2支出
				firstDetail.setMoney(order.getFirstIncome());
				firstDetail.setOrderNo(orderUUID);
				firstDetail.setPayChannel("1");//支付渠道：0余额，1微信
				firstDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				firstDetail.setPayerUUID(order.getEntityUUID());
				firstDetail.setPayerName(order.getEntityName());
				firstDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(firstDetail);
			}
			
			if (RegExpUtil.isNullOrEmpty(secondUUID) && order.getSecondIncome() > 0) {
				//生成二级分销商账户明细
				Account secondAccount = jedisClient.getAccount(sourceId, closeConn, secondUUID);
				AccountDetail secondDetail = new AccountDetail();
				secondDetail.setAccountUUID(secondAccount.getAccountUUID());
				secondDetail.setDetailType("1");//收支类型：1收入，2支出
				secondDetail.setMoney(order.getSecondIncome());
				secondDetail.setOrderNo(orderUUID);
				secondDetail.setPayChannel("1");//支付渠道：0余额，1微信
				secondDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				secondDetail.setPayerUUID(order.getEntityUUID());
				secondDetail.setPayerName(order.getEntityName());
				secondDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(secondDetail);
			}
			
			Integer income = order.getPayment()-order.getFirstIncome()-order.getSecondIncome();
			Lock lock = jedisClient.getEntityLock(order.getEntityUUID());
			lock.lock();
			try {
				//购物或进货订单需要更新商品销量
				if("2".equals(order.getOrderType()) || "3".equals(order.getOrderType())){
					List<OrderItem> orderItemList = entity.get(order.getOrderUUID());
					
					//商品规格销量
					Object[][] params = new Object[orderItemList.size()][3];
					int i = 0;
					for (OrderItem orderItem : orderItemList) {
						params[i][0] = orderItem.getCount();
						params[i][1] = orderItem.getCount();
						params[i][2] = orderItem.getParamUUID();
						i++;
					}
					
					//商品销量
					Map<String, Integer> map = new HashMap<>();
					for (OrderItem orderItem : orderItemList) {
						Integer count = map.get(orderItem.getItemUUID());
						if(count == null){
							count = 0;
						}
						count += orderItem.getCount();
						map.put(orderItem.getItemUUID(), count);
					}
					Object[][] items = new Object[map.size()][4];
					int j = 0;
					for (Map.Entry<String, Integer> item : map.entrySet()) {
						items[j][0] = item.getValue();
						items[j][1] = item.getValue();
						items[j][2] = item.getValue();
						items[j][3] = item.getKey();
					}
					paramService.increseSales(sourceId, closeConn, params);
					itemService.increseSales(sourceId, closeConn, items);
				}
				
				//更新商家账户余额
				Account account = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
				account.setAccountBalance(account.getAccountBalance() + income);
				account.setAccountIncome(account.getAccountIncome() + income);
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} finally {
				lock.unlock();
			}
			
		}
		
		orderItemService.batchUpByField(sourceId, closeConn, itemList, "id");
		orderService.batchUpByField(sourceId, closeConn, orderList, "id");
		detailService.batchInsert(sourceId, closeConn, detailList);
		
		//更新一级分销商账户余额
		if(firstTotal > 0){
			Lock lock = jedisClient.getEntityLock(firstUUID);
			lock.lock();
			try {
				Account account = jedisClient.getAccount(sourceId, closeConn, firstUUID);
				account.setAccountBalance(account.getAccountBalance() + firstTotal);
				account.setAccountIncome(account.getAccountIncome() + firstTotal);
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} finally {
				lock.unlock();
			}
		}
		
		//更新二级分销商账户余额
		if(secondTotal > 0){
			Lock lock = jedisClient.getEntityLock(secondUUID);
			lock.lock();
			try {
				Account account = jedisClient.getAccount(sourceId, closeConn, secondUUID);
				account.setAccountBalance(account.getAccountBalance() + secondTotal);
				account.setAccountIncome(account.getAccountIncome() + secondTotal);
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} finally {
				lock.unlock();
			}
		}
		
	}
	/**
	 * 同意退款
	 */
	public String agreeRefund(String sourceId, boolean closeConn,HttpServletRequest request) throws Exception{
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		Map<String, String> result = new HashMap<>();
		if(order == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		if(!"-3".equals(order.getOrderStatus())){
			result.put("status", "-2");
			result.put("msg", "订单状态异常，不能同意退款");
			return JsonUtil.ObjToJson(result);
		}
		
		//验证支付签名
		String paySign = request.getParameter("paySign");
		String entityUUID = order.getEntityUUID();
		String signCacheName = ReadProperties.getProperty("/redis.properties", "pwdSignCache");
		Map<String, String> signCache = jedisClient.getCache(signCacheName);
		if(RegExpUtil.isNullOrEmpty(paySign) || signCache == null || !paySign.equals(signCache.get(entityUUID))){
			result.put("status", "-3");
			result.put("msg", "支付签名失败");
			return JsonUtil.ObjToJson(result);
		}
		signCache.remove(entityUUID);
		jedisClient.setCache(signCacheName, signCache);
		
		Account entityAccount = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
		if(entityAccount.getAccountBalance() < order.getPayment()){
			result.put("status", "-4");
			result.put("msg", "账户余额不足");
			return JsonUtil.ObjToJson(result);
		}
		
		//调用微信退款接口
		String out_trade_no = order.getBatchNo();
		if(RegExpUtil.isNullOrEmpty(out_trade_no)){
			out_trade_no = order.getOrderUUID();
		}
		String out_refund_no = UUID.randomUUID().toString().replaceAll("-", "");
		String total_fee = order.getPayment().toString();
		String refund_fee = total_fee;
		total_fee = "1";
		refund_fee = "1";
		
		//微信分配的开发平台appid（企业号（corpid为此appId），必填 
		String appid = WeiXinPayConfig.APP_ID;
		//微信支付分配的商户号，必填
	    String mch_id = WeiXinPayConfig.PARTNER;
	    //随机字符串，不长于32位。推荐随机数生成算法，必填
	    String nonce_str = WeiXinPayUtil.getNonceStr();
	    //签名，详见签名生成算法，必填
	    String sign = "";
	    
	    SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("refund_fee", refund_fee);
		
		sign = WeiXinPayUtil.createSign(packageParams);
		String xml = "<xml>" + 
						"<appid>" + appid + "</appid>" + 
						"<mch_id>"+ mch_id + "</mch_id>" + 
						"<nonce_str>" + nonce_str + "</nonce_str>" + 
						"<out_refund_no>" + out_refund_no + "</out_refund_no>" + 
						"<out_trade_no>" + out_trade_no + "</out_trade_no>" + 
						"<refund_fee>"+refund_fee+"</refund_fee>"+
						"<total_fee>"+total_fee+"</total_fee>"+
						"<sign>" + sign + "</sign>" + 
					"</xml>";
		
		Map<String, String> rp = HttpClientUtil.HttpsPost(request, "https://api.mch.weixin.qq.com/secapi/pay/refund", xml);
	    if("SUCCESS".equals(rp.get("result_code"))){
    		String firstUUID = order.getFirstUUID();
	    	Integer firstIncome = order.getFirstIncome();
	    	String secondUUID = order.getSecondUUID();
	    	Integer secondIncome = order.getSecondIncome();
	    	List<AccountDetail> detailList = new ArrayList<>();
	    	
	    	//生成商家账户明细
			AccountDetail entityDetail = new AccountDetail();
			entityDetail.setAccountUUID(entityAccount.getAccountUUID());
			entityDetail.setDetailType("2");//收支类型：1收入，2支出
			entityDetail.setMoney(-order.getPayment());
			entityDetail.setOrderNo(out_trade_no);
			entityDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
			entityDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
			entityDetail.setPayerUUID(order.getPayerUUID());
			entityDetail.setPayerName(order.getPayerName());
			entityDetail.setPayerLogo(order.getPayerLogo());
			detailList.add(entityDetail);
	    	
	    	if(RegExpUtil.isNullOrEmpty(firstUUID) && order.getFirstIncome() > 0){
				//生成一级分销商账户明细
				Account firstAccount = jedisClient.getAccount(sourceId, closeConn, firstUUID);
				AccountDetail firstDetail = new AccountDetail();
				firstDetail.setAccountUUID(firstAccount.getAccountUUID());
				firstDetail.setDetailType("2");//收支类型：1收入，2支出
				firstDetail.setMoney(-order.getFirstIncome());
				firstDetail.setOrderNo(out_trade_no);
				firstDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
				firstDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				firstDetail.setPayerUUID(order.getEntityUUID());
				firstDetail.setPayerName(order.getEntityName());
				firstDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(firstDetail);
			}
			
			if (RegExpUtil.isNullOrEmpty(secondUUID) && order.getSecondIncome() > 0) {
				//生成二级分销商账户明细
				Account secondAccount = jedisClient.getAccount(sourceId, closeConn, secondUUID);
				AccountDetail secondDetail = new AccountDetail();
				secondDetail.setAccountUUID(secondAccount.getAccountUUID());
				secondDetail.setDetailType("2");//收支类型：1收入，2支出
				secondDetail.setMoney(-order.getSecondIncome());
				secondDetail.setOrderNo(out_trade_no);
				secondDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
				secondDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				secondDetail.setPayerUUID(order.getEntityUUID());
				secondDetail.setPayerName(order.getEntityName());
				secondDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(secondDetail);
			}
			detailService.batchInsert(sourceId, closeConn, detailList);
			
			List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
			
			Lock entityLock = jedisClient.getEntityLock(order.getEntityUUID());
			entityLock.lock();
			try {
				//增加库存
				Object[][] params = new Object[orderItemList.size()][2];
				int i = 0;
				for (OrderItem orderItem : orderItemList) {
					orderItem.setOrderStatus("-4");
					params[i][0] = orderItem.getCount();
					params[i][1] = orderItem.getParamUUID();
					i++;
				}
				paramService.increseStock(sourceId, closeConn, params);
				
				//更新商家账户余额
				Account account = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
				account.setAccountBalance(account.getAccountBalance() - order.getPayment());
				account.setAccountExpend(account.getAccountExpend() + order.getPayment());
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} finally {
				entityLock.unlock();
			}
	    	
	    	if(RegExpUtil.isNullOrEmpty(firstUUID) && firstIncome > 0){
	    		Lock lock = jedisClient.getEntityLock(firstUUID);
				lock.lock();
				try {
					Account account = jedisClient.getAccount(sourceId, closeConn, firstUUID);
					account.setAccountBalance(account.getAccountBalance() - firstIncome);
					account.setAccountExpend(account.getAccountExpend() + firstIncome);
					accountService.saveOrUpdate(sourceId, closeConn, account);
				} finally {
					lock.unlock();
				}
	    	}
	    	if(RegExpUtil.isNullOrEmpty(secondUUID) && secondIncome > 0){
	    		Lock lock = jedisClient.getEntityLock(secondUUID);
				lock.lock();
				try {
					Account account = jedisClient.getAccount(sourceId, closeConn, secondUUID);
					account.setAccountBalance(account.getAccountBalance() - secondIncome);
					account.setAccountExpend(account.getAccountExpend() + secondIncome);
					accountService.saveOrUpdate(sourceId, closeConn, account);
				} finally {
					lock.unlock();
				}
	    	}
	    	
	    	orderItemService.batchUpByField(sourceId, closeConn, orderItemList, "id");
	    	order.setOrderStatus("-4");
	    	order.setCloseTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			orderService.saveOrUpdate(sourceId, closeConn, order);
			
	    	result.put("status", "1");
	    	result.put("msg", "同意退货成功");
	    }else{
	    	result.put("status", "-1");
	    	result.put("msg", rp.get("err_code_des"));
	    	System.out.println(JsonUtil.ObjToJson(rp));
	    }
	    
		return JsonUtil.ObjToJson(result);
	}
	/**
	 * 同意退货
	 */
	public String agreeReturn(String sourceId, boolean closeConn,HttpServletRequest request) throws Exception{
		String orderUUID = request.getParameter("orderUUID");
		Map<String, String> findMap = new HashMap<>();
		findMap.put("orderUUID", orderUUID);
		Order order = orderService.findByMap(sourceId, closeConn, findMap);
		
		Map<String, String> result = new HashMap<>();
		if(order == null){
			result.put("status", "-1");
			result.put("msg", "没有查询到订单信息");
			return JsonUtil.ObjToJson(result);
		}
		
		//验证支付签名
		String paySign = request.getParameter("paySign");
		String entityUUID = order.getEntityUUID();
		String signCacheName = ReadProperties.getProperty("/redis.properties", "pwdSignCache");
		Map<String, String> signCache = jedisClient.getCache(signCacheName);
		if(RegExpUtil.isNullOrEmpty(paySign) || signCache == null || !paySign.equals(signCache.get(entityUUID))){
			result.put("status", "-2");
			result.put("msg", "支付签名失败");
			return JsonUtil.ObjToJson(result);
		}
		signCache.remove(entityUUID);
		jedisClient.setCache(signCacheName, signCache);
		
		Account entityAccount = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
		if(entityAccount.getAccountBalance() < order.getPayment()){
			result.put("status", "-3");
			result.put("msg", "账户余额不足");
			return JsonUtil.ObjToJson(result);
		}
		
		//调用微信退款接口
		String out_trade_no = order.getRefundNo();
		String out_refund_no = order.getOrderUUID();
		String total_fee = order.getTotalPrice().toString();
		String refund_fee = order.getPayment().toString();
		total_fee = "1";
		refund_fee = "1";
		
		//微信分配的开发平台appid（企业号（corpid为此appId），必填 
		String appid = WeiXinPayConfig.APP_ID;
		//微信支付分配的商户号，必填
	    String mch_id = WeiXinPayConfig.PARTNER;
	    //随机字符串，不长于32位。推荐随机数生成算法，必填
	    String nonce_str = WeiXinPayUtil.getNonceStr();
	    //签名，详见签名生成算法，必填
	    String sign = "";
	    
	    SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("refund_fee", refund_fee);
		
		sign = WeiXinPayUtil.createSign(packageParams);
		String xml = "<xml>" + 
						"<appid>" + appid + "</appid>" + 
						"<mch_id>"+ mch_id + "</mch_id>" + 
						"<nonce_str>" + nonce_str + "</nonce_str>" + 
						"<out_refund_no>" + out_refund_no + "</out_refund_no>" + 
						"<out_trade_no>" + out_trade_no + "</out_trade_no>" + 
						"<refund_fee>"+refund_fee+"</refund_fee>"+
						"<total_fee>"+total_fee+"</total_fee>"+
						"<sign>" + sign + "</sign>" + 
					"</xml>";
		
		Map<String, String> rp = HttpClientUtil.HttpsPost(request, "https://api.mch.weixin.qq.com/secapi/pay/refund", xml);
	    if("SUCCESS".equals(rp.get("result_code"))){
    		String firstUUID = order.getFirstUUID();
	    	Integer firstIncome = order.getFirstIncome();
	    	String secondUUID = order.getSecondUUID();
	    	Integer secondIncome = order.getSecondIncome();
	    	List<AccountDetail> detailList = new ArrayList<>();
	    	
	    	//生成商家账户明细
			AccountDetail entityDetail = new AccountDetail();
			entityDetail.setAccountUUID(entityAccount.getAccountUUID());
			entityDetail.setDetailType("2");//收支类型：1收入，2支出
			entityDetail.setMoney(-order.getPayment());
			entityDetail.setOrderNo(out_trade_no);
			entityDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
			entityDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
			entityDetail.setPayerUUID(order.getPayerUUID());
			entityDetail.setPayerName(order.getPayerName());
			entityDetail.setPayerLogo(order.getPayerLogo());
			detailList.add(entityDetail);
	    	
	    	if(RegExpUtil.isNullOrEmpty(firstUUID) && order.getFirstIncome() > 0){
				//生成一级分销商账户明细
				Account firstAccount = jedisClient.getAccount(sourceId, closeConn, firstUUID);
				AccountDetail firstDetail = new AccountDetail();
				firstDetail.setAccountUUID(firstAccount.getAccountUUID());
				firstDetail.setDetailType("2");//收支类型：1收入，2支出
				firstDetail.setMoney(-order.getFirstIncome());
				firstDetail.setOrderNo(out_trade_no);
				firstDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
				firstDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				firstDetail.setPayerUUID(order.getEntityUUID());
				firstDetail.setPayerName(order.getEntityName());
				firstDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(firstDetail);
			}
			
			if (RegExpUtil.isNullOrEmpty(secondUUID) && order.getSecondIncome() > 0) {
				//生成二级分销商账户明细
				Account secondAccount = jedisClient.getAccount(sourceId, closeConn, secondUUID);
				AccountDetail secondDetail = new AccountDetail();
				secondDetail.setAccountUUID(secondAccount.getAccountUUID());
				secondDetail.setDetailType("2");//收支类型：1收入，2支出
				secondDetail.setMoney(-order.getSecondIncome());
				secondDetail.setOrderNo(out_trade_no);
				secondDetail.setPayChannel(order.getPayChannel());//支付渠道：0余额，1微信
				secondDetail.setDetailSource(order.getOrderType());//明细来源：1充值，2购物，3进货，4其他，默认值为2
				secondDetail.setPayerUUID(order.getEntityUUID());
				secondDetail.setPayerName(order.getEntityName());
				secondDetail.setPayerLogo(order.getEntityLogo());
				detailList.add(secondDetail);
			}
			detailService.batchInsert(sourceId, closeConn, detailList);
			
			List<OrderItem> orderItemList = orderItemService.findListByMap(sourceId, closeConn, findMap, null);
			
			Lock entityLock = jedisClient.getEntityLock(order.getEntityUUID());
			entityLock.lock();
			try {
				//增加库存
				Object[][] params = new Object[orderItemList.size()][2];
				int i = 0;
				for (OrderItem orderItem : orderItemList) {
					orderItem.setOrderStatus("-7");
					params[i][0] = orderItem.getCount();
					params[i][1] = orderItem.getParamUUID();
					i++;
				}
				paramService.increseStock(sourceId, closeConn, params);
				
				//更新商家账户余额
				Account account = jedisClient.getAccount(sourceId, closeConn, order.getEntityUUID());
				account.setAccountBalance(account.getAccountBalance() - order.getPayment());
				account.setAccountExpend(account.getAccountExpend() + order.getPayment());
				accountService.saveOrUpdate(sourceId, closeConn, account);
			} finally {
				entityLock.unlock();
			}
	    	
	    	if(RegExpUtil.isNullOrEmpty(firstUUID) && firstIncome > 0){
	    		Lock lock = jedisClient.getEntityLock(firstUUID);
				lock.lock();
				try {
					Account account = jedisClient.getAccount(sourceId, closeConn, firstUUID);
					account.setAccountBalance(account.getAccountBalance() - firstIncome);
					account.setAccountExpend(account.getAccountExpend() + firstIncome);
					accountService.saveOrUpdate(sourceId, closeConn, account);
				} finally {
					lock.unlock();
				}
	    	}
	    	if(RegExpUtil.isNullOrEmpty(secondUUID) && secondIncome > 0){
	    		Lock lock = jedisClient.getEntityLock(secondUUID);
				lock.lock();
				try {
					Account account = jedisClient.getAccount(sourceId, closeConn, secondUUID);
					account.setAccountBalance(account.getAccountBalance() - secondIncome);
					account.setAccountExpend(account.getAccountExpend() + secondIncome);
					accountService.saveOrUpdate(sourceId, closeConn, account);
				} finally {
					lock.unlock();
				}
	    	}
	    	
	    	orderItemService.batchUpByField(sourceId, closeConn, orderItemList, "id");
	    	order.setOrderStatus("-7");
	    	order.setCloseTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			orderService.saveOrUpdate(sourceId, closeConn, order);
			
	    	result.put("status", "1");
	    	result.put("msg", "同意退货成功");
	    }else{
	    	result.put("status", "-1");
	    	result.put("msg", rp.get("err_code_des"));
	    	System.out.println(JsonUtil.ObjToJson(rp));
	    }
	    
		return JsonUtil.ObjToJson(result);
	}
	
}
