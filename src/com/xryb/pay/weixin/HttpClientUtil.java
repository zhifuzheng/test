package com.xryb.pay.weixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.google.gson.reflect.TypeToken;
import com.xryb.zhtc.util.IpUtil;
import com.xryb.zhtc.util.JsonUtil;

/**
 * 请求微信接口http客服端
 * 
 * @author wf
 */
public class HttpClientUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, String> HttpRequest(String requestUrl, String requestMethod, String xml) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != xml) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(xml.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			Map<String, String> result = null;
			if (xml != null) {
				result = WeiXinPayUtil.xmlToMap(buffer.toString());
			} else {
				result = (Map<String, String>) JsonUtil.JsonToObj(buffer.toString(), new TypeToken<Map<String, String>>() {}.getType());
			}
			return result;
		} catch (ConnectException ce) {
			System.out.println("连接超时：" + ce);
		} catch (Exception e) {
			System.out.println("http请求异常：" + e);
		}
		return null;
	}

	public static Map<String, String> HttpsPost(HttpServletRequest request, String requestUrl, String xml) throws Exception {
		// 加载本地的证书进行https加密传输
		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		String certPath = request.getSession().getServletContext().getRealPath(WeiXinPayConfig.CERT_PATH);
		FileInputStream is = new FileInputStream(new File(certPath));
		try {
			// 加载证书密码，默认为商户ID
			keyStore.load(is, WeiXinPayConfig.PARTNER.toCharArray());
		} finally {
			is.close();
		}

		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WeiXinPayConfig.PARTNER.toCharArray()).build();
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, HttpsURLConnection.getDefaultHostnameVerifier());
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		try {
			HttpPost httpost = new HttpPost(requestUrl); // 设置响应头信息
			httpost.addHeader("Content-Type", "text/xml; charset=UTF-8");
			httpost.setEntity(new StringEntity(xml, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpost);
			try {
				HttpEntity entity = response.getEntity();
				String returnStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				EntityUtils.consume(entity);
				Map<String, String> result = WeiXinPayUtil.xmlToMap(returnStr);
				return result;
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public static Map<String, String> genQRCode(HttpServletRequest request, String requestUrl, String param, String qrCodePath) {
		Map<String, String> result = new HashMap<>();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(param.getBytes());
			outputStream.close();
			
			InputStream inputStream = conn.getInputStream();

			String realPath = request.getSession().getServletContext().getRealPath("/" + qrCodePath);//获取文件夹在服务器的绝对路径
			//String realPath = "E:/Develop/WorkSpace/zhtc/WebContent/" + qrCodePath;// 开发阶段先保存在本机电脑
			File dirs = new File(realPath);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			String filename = UUID.randomUUID().toString().replace("-", "") + ".png";
			File file = new File(realPath, filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] b = new byte[1024];
			int len = 0;
			boolean first = true;
			boolean hasError = false;
			while((len = inputStream.read(b)) != -1){
				if (first) {
					first = false;
					String line = new String(b, 0, len);
					if (line.indexOf("errcode") != -1) {
						hasError = true;
						result.put("status", "-1");
						result.put("msg", line.substring(line.indexOf("errmsg") + 9, line.length() - 2));
						break;
					} 
				}
				fos.write(b, 0, len);
			}
			fos.flush();
			
			// 释放资源
			inputStream.close();
			inputStream = null;
			fos.close();
			fos = null;
			conn.disconnect();
			if(hasError){
				file.delete();
			}else{
				result.put("status", "1");
				result.put("path", qrCodePath+filename);
			}
			return result;
		} catch (Exception ce) {
			result.put("status", "-2");
			result.put("msg", "当前网络繁忙，请稍后重试");
			return result;
		}
		
	}
	
	public static Map<String, String> genQRCode(HttpServletRequest request, String requestUrl, String param, String qrCodePath, String filename) {
		Map<String, String> result = new HashMap<>();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(param.getBytes());
			outputStream.close();
			
			InputStream inputStream = conn.getInputStream();

			String realPath = request.getSession().getServletContext().getRealPath("/" + qrCodePath);//获取文件夹在服务器的绝对路径
			//String realPath = "E:/Develop/WorkSpace/zhtc/WebContent/" + qrCodePath;// 开发阶段先保存在本机电脑
			File dirs = new File(realPath);
			if (!dirs.exists()) {
				dirs.mkdirs();
			}
			File file = new File(realPath, filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] b = new byte[1024];
			int len = 0;
			boolean first = true;
			boolean hasError = false;
			while((len = inputStream.read(b)) != -1){
				if (first) {
					first = false;
					String line = new String(b, 0, len);
					if (line.indexOf("errcode") != -1) {
						hasError = true;
						result.put("status", "-1");
						result.put("msg", line.substring(line.indexOf("errmsg") + 9, line.length() - 2));
						break;
					} 
				}
				fos.write(b, 0, len);
			}
			fos.flush();
			
			// 释放资源
			inputStream.close();
			inputStream = null;
			fos.close();
			fos = null;
			conn.disconnect();
			if(hasError){
				file.delete();
			}else{
				result.put("status", "1");
				result.put("path", qrCodePath+filename);
			}
			return result;
		} catch (Exception ce) {
			result.put("status", "-2");
			result.put("msg", "当前网络繁忙，请稍后重试");
			return result;
		}
		
	}
	
	public static Map<String, String> transfer(HttpServletRequest request, String tradeNo, String openid, String amount) throws Exception{
		String appid = WeiXinPayConfig.APP_ID;
		String mch_id = WeiXinPayConfig.PARTNER;
		String nonce_str = WeiXinPayUtil.getNonceStr();
		String check_name = "NO_CHECK"; // 是否验证真实姓名呢
		String desc = "钱包提现";
		String spbill_create_ip = IpUtil.getIp2(request);

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("mch_appid", appid);
		packageParams.put("mchid", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("partner_trade_no", tradeNo);
		packageParams.put("openid", openid);
		packageParams.put("check_name", check_name);
		packageParams.put("amount", amount);
		packageParams.put("desc", desc);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		
		String sign = WeiXinPayUtil.createSign(packageParams);
		String xml = "<xml>" + 
						"<mch_appid>" + appid + "</mch_appid>" + 
						"<mchid>"+ mch_id + "</mchid>" + 
						"<nonce_str>" + nonce_str + "</nonce_str>" + 
						"<partner_trade_no>" + tradeNo + "</partner_trade_no>" + 
						"<openid>" + openid + "</openid>" + 
						"<check_name>"+check_name+"</check_name>"+
						"<amount>"+amount+"</amount>"+
						"<desc>"+desc+"</desc>"+
						"<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
						"<sign>" + sign + "</sign>" + 
					"</xml>";
		
		Map<String, String> result = HttpClientUtil.HttpsPost(request, "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", xml);
	    if("SUCCESS".equals(result.get("result_code"))){
	    	System.out.println(JsonUtil.ObjToJson(result));
	    }else{
	    	System.out.println(result.get("err_code_des"));
	    	System.out.println(JsonUtil.ObjToJson(result));
	    }
		return result;
	}

}
