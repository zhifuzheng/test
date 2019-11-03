package com.xryb.zhtc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Decoder;
/**
 * 适用于当前服务器的base64上传工具
 * @author wf
 */
public class UploadBase64Util {
	
	/**
	 * base64上传
	 * @param request request对象
	 * @param relativePath base64保存相对路径
	 * @param base64ParamName base64参数名
	 * @param ext base64扩展名
	 * @return Map<String,String> 返回包含三个key(path,status,msg)的map，
	 * path代表保存base64的相对路径，status代表base64上传状态，msg代表base64上传描述信息
	 */
	public static Map<String,String> upload(HttpServletRequest request, String relativePath, String base64ParamName, String ext){
		Map<String, String> result = new HashMap<String,String>();
		if(RegExpUtil.isNullOrEmpty(base64ParamName) || RegExpUtil.isNullOrEmpty(ext) || RegExpUtil.isNullOrEmpty(ext)) {
			result.put("status", "-1");
			result.put("msg", "不能有为空的参数");
			return result;
		}
		String base64Data = request.getParameter(base64ParamName);
		BASE64Decoder decoder = new BASE64Decoder();
		base64Data = base64Data.replaceAll("data:image/jpeg;base64,", "");
		//解码
		byte[] base64Arr = null;
		try {
			base64Arr = decoder.decodeBuffer(base64Data);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("status", "-2");
			result.put("msg", "不是base64数据");
			return result;
		}
		for(int i = 0; i < base64Arr.length; i++) {
			if(base64Arr[i] < 0) {
				base64Arr[i] += 256;
			}
		}
		String realPath = request.getSession().getServletContext().getRealPath("/" + relativePath);//获取文件夹在服务器的绝对路径
		//String realPath = "/Users/apple/eclipse-workspace/SportWit/WebContent/"+relativePath;
		//String realPath = "E:/Develop/Eclipse/WorkSpaces/SportWit/WebContent/" + relativePath;//开发阶段先保存在本机电脑	
		String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + ext;
		try {
			File dir = new File(realPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			OutputStream out = new FileOutputStream(realPath + "/" + newFileName);
			out.write(base64Arr);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "-3");
			result.put("msg", "当前网络繁忙，请稍后重试！");
			return result;
		}
		result.put("path", relativePath + newFileName);
		result.put("status", "1");
		result.put("msg", "上传成功！");
		return result;
	}

}
