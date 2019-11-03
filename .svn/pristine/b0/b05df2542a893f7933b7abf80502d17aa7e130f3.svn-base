package com.xryb.zhtc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;


import sun.misc.BASE64Encoder;

/**
 * 短信发送工具
 * @author hshzh
 *
 */
public class HttpEnsms {
	/**
	 * 短信发送
	 * @param args
	 */
	public static boolean sendSMS(String mobile,String msg) {
		SimpleDateFormat df=new SimpleDateFormat("MMddHHmmss");		
		String Stamp = df.format(new Date());
		String password="jkwl7426";
		MD5 md5 = new MD5();
		//md5.getMD5ofStr(inbuf)
		//String Secret=MD5.GetMD5Code(password+Stamp).toUpperCase();
		String Secret=md5.getMD5ofStr(password+Stamp).toUpperCase();
		try {
			JSONObject j=new JSONObject();
			j.put("UserName", "jkwl742");
			j.put("Stamp", Stamp);
			j.put("Secret", Secret);
			j.put("Moblie", mobile);
			j.put("Text", msg);
			//j.put("Text", "【西软弈博】您的验证码是：9527,3分钟内有效，请尽快处理！");
			//【西软弈博】您的验证码是：@,@分钟内有效，请尽快处理！
			j.put("Ext", "");
			j.put("SendTime", "");
			//获取json字符串
			String json=j.toString();
			byte[] data=json.getBytes("utf-8");
			byte[] key=password.getBytes();
			//获取加密的key
			byte[] nkey=new byte[8];
			System.arraycopy(key, 0, nkey, 0, key.length > 8 ? 8 : key.length);
			//Des加密，base64转码
			String str=new BASE64Encoder().encode(DesHelper.encrypt(data, nkey)); 
			
			//System.out.println(str);
			//url编码
			//str=URLEncoder.encode(str, "utf-8");
			
			//发送http请求
			String Url="https://sh2.ipyy.com/ensms.ashx";
			HttpClient client=new HttpClient();
			PostMethod post=new PostMethod(Url);
			post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			NameValuePair UserId=new NameValuePair("UserId","8889");
			NameValuePair Text64=new NameValuePair("Text64",str);
			post.setRequestBody(new NameValuePair[]{UserId,Text64});
			int statu=client.executeMethod(post);
			//System.out.println("statu="+statu);
			//返回结果
			String result=post.getResponseBodyAsString();
			Map resultMap = (Map) JsonUtil.JsonToObj(result, Map.class);
			//System.out.println("result="+result);
			//System.out.println(resultMap.get("StatusCode")+"");
			if("1.0".equals(resultMap.get("StatusCode")+"")) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return false;
	}
	public static void main(String[] args) {
		//String smsMsg="【聚能汇】尊敬的13765156020,您在“9527和他的朋友们”的个人消费提成金额为：6.18元请下载聚能汇app领取http://www.jnhsc.com/jnh/download.jsp,账号：13765156020，默认密码：000000";
		//String smsMsg="【聚能汇】会员[13**71]向您的店铺支付了0.1元，请查收。";
		String smsMsg="【智跑】您的验证码为：111,请在2分钟内处理";
		sendSMS("13765156020",smsMsg);
	}

}
