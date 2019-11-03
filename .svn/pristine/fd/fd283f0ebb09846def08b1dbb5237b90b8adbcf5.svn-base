package com.xryb.zhtc.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import jxl.read.biff.SharedStringFormulaRecord;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PhotoUtil {
	
	/**按时间生成文件名s
	 * @return
	 *
	 *@author wangchen
	 */
	public static String getRandomFileName() {  
		Random r = new Random();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");  
		StringBuffer sb = new StringBuffer();  
		sb.append(r.nextInt(100));  
		sb.append(r.nextInt(100));  
		sb.append("_");  
		sb.append(sdf.format(new Date()));  
		sb.append("_");
		sb.append(r.nextInt(100));  
		sb.append(r.nextInt(100));
		sb.append(".");
		return sb.toString();  
	}
	//base64保存图片
	public static void uploadBASE64(String base64,String path) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		//解码
		byte[] b = null;
		b = decoder.decodeBuffer(base64);
		for(int i = 0; i < b.length; i++) {
			if(b[i] < 0) {
				b[i] +=256;
			}
		}
		System.out.println("path============"+path);
		OutputStream out = new FileOutputStream(path);
		out.write(b);
		out.flush();
		out.close();
	}
	
	//  根据图片路径转dase64
	public static String getImgStr(String path) {
		InputStream inputStream = null;
	    byte[] data = null;
	    try {
	        inputStream = new FileInputStream(path);
	        data = new byte[inputStream.available()];
	        inputStream.read(data);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // 加密
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(data);
	}
}
