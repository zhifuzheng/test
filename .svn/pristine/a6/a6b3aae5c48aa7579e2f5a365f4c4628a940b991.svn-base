package com.xryb.zhtc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
/**
 * httpConnection工具类，处理http的get和post请求
 * @author hshzh
 *
 */
public class HttpConnectionUtil {
	private String charset = "utf-8";//请求用到的编码
	/**
	 * 处理 GET 请求
	 * 
	 * @param url 地址
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public String doGet(String url) throws Exception {
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        //对正文内容使用URLEncoder.encode 进行编码		
		httpURLConnection.setRequestProperty("Accept-Charset", charset);//设置编码
		// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
		// 意思是正文是urlencoded编码过的form参数
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		if (httpURLConnection.getResponseCode() >= 300) {
			throw new Exception("HTTP 请求失败！,参数超过长度300，  Response code is " + httpURLConnection.getResponseCode());
		}
		try {
			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,charset);//设置编码,否则中文乱码
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return resultBuffer.toString();
	}

	/**
	 * 处理 POST请求
	 * 
	 * @param url 地址
	 * @param parameterMap post参数
	 * @return
	 * @throws Exception
	 */
	public String doPost(String url, Map parameterMap) throws Exception {
		/* Translate parameter map to parameter date string */
		StringBuffer parameterBuffer = new StringBuffer();
		if (parameterMap != null) {
			Iterator iterator = parameterMap.keySet().iterator();
			String key = null;
			String value = null;
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				//判断key对应的value是否有值
				if (parameterMap.get(key) != null) {
					value = (String) parameterMap.get(key);
				} else {
					value = "";
				}
				parameterBuffer.append(key).append("=").append(URLEncoder.encode(value, "utf-8"));
				if (iterator.hasNext()) {
					parameterBuffer.append("&");
				}
			}
		}
		URL localURL = new URL(url);
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true, 默认情况下是false;
		httpURLConnection.setDoOutput(true);
		// 设定请求的方法为"POST"，默认是GET
		httpURLConnection.setRequestMethod("POST");
        //对正文内容使用URLEncoder.encode 进行编码
		httpURLConnection.setRequestProperty("Accept-Charset", charset);
	    // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		//设置正文内容长度
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;
		try {
			outputStream = httpURLConnection.getOutputStream();
			outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.write(parameterBuffer.toString());
			outputStreamWriter.flush();
			// 调用HttpURLConnection连接对象的getInputStream()函数,
			// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream,"utf-8");//设置编码,否则中文乱码
			reader = new BufferedReader(inputStreamReader);
			//读取请求返回的内容
			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return resultBuffer.toString();
	}
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	public static void main(String[] args) throws IOException {
		//String strUrl = "http://112.74.15.109:8500/DataService.asmx/GetRecordData";
		//String strUrl = "http://localhost:8081/ccrm/httpconnection/postmng/findPostUserChildCRM";
		//String strUrl = "http://test.wiseea.com/ccrm/httpconnection/postmng/loginUserInfo";
		//String param = "title=中文测试&ecCode=01&LoginName=qqq";
		HttpConnectionUtil hcu = new HttpConnectionUtil();
		Map parameterMap = new HashMap();
		//parameterMap.put("loginName", "9527");
		//parameterMap.put("userPwd", "000000");
		//parameterMap.put("proFinishTime", "2016-03-01|2018-02-01");
		parameterMap.put("userUUID", "01");
		parameterMap.put("ecCode", "01");
		parameterMap.put("title", " 中文 ");
		parameterMap.put("feedbackContent", "反馈内容");//反馈内容
		parameterMap.put("smrpContent", "详 细 内 ");
		try {
			//String resultStr = hcu.doGet("http://college.gaokao.com/schlist/p2");
			//String resultStr = hcu.doPost(strUrl, parameterMap);
			for(int i=0;i<0;i++){
				String resultStr = UUID.randomUUID().toString();
				resultStr=resultStr.toUpperCase();
				resultStr=resultStr.replace("-", "");
				System.out.println(resultStr);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
