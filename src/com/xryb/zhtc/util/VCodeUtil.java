package com.xryb.zhtc.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Encoder;

/**
 * 生成验证码工具
 * 
 * @author hshzh
 * 
 */
public class VCodeUtil {
	// 设置字母的大小,大小
	private Font mFont = new Font("Times New Roman", Font.PLAIN, 17);
	private String getPicture(String picName, HttpServletRequest request) {
		// 图片路径
		String imagePath = ReadProperties.getValue("imgVCode");
		String path = request.getServletContext().getRealPath(imagePath + "/" + picName);
		File file = new File(path);
		// 如果服务器上的图片目录不存在就返回
		if (!file.exists()) {
			System.out.println(file.getAbsolutePath() + "");
			return null;
		}
		BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		FileInputStream fips = null;
		try {
			fips = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}
		ByteArrayOutputStream bops = new ByteArrayOutputStream();
		int data = -1;
		try {
			while ((data = fips.read()) != -1) {
				bops.write(data);
			}
		} catch (Exception e) {
			return null;
		}
		byte[] btImg = bops.toByteArray();
		
		// ***************************************************************
		// 修改图片名称为实际的图片 base64编码的图片内容
		return "data:image/png;base64," + encoder.encodeBuffer(btImg).trim();
	}
	/**
	 * 通过指定图片生成随机码
	 * @return
	 */
	public Map getVcodePic(HttpServletRequest request){
		// 生成随机数
		String sRand = "";
		Random random = new Random();
		Map map = new HashMap();
		Map cache = (Map)CacheUtil.getInstance().getBaseCache();
		String pic = "";
		String passcode = "";
		for (int i = 0; i < 4; i++) {
			int itmp = random.nextInt(10);//生成(0-9随机数)
			int in = random.nextInt(2);//生成(0-1随机数)
			if(in==0){
				pic = itmp+""+itmp+".png";
			}else{
				pic = itmp+".png";
			}
			if(!cache.containsKey(pic)){
				cache.put(pic, getPicture(pic, request));
			}
			passcode += itmp;
			map.put("img"+i, cache.get(pic));
			map.put("passcode", passcode);
		}
		return map; 
	}
	/**
	 * 通过绘图生成随机码
	 * @param vcodeStr
	 * @return
	 */
	public String getVcode(StringBuilder vcodeStr) {
		int width = 80, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(1, 1, width - 1, height - 1);
		g.setColor(new Color(102, 102, 102));
		g.drawRect(0, 0, width - 1, height - 1);
		g.setFont(mFont);
		g.setColor(getRandColor(160, 200));
//		// 画随机线
//		for (int i = 0; i < 155; i++) {
//			int x = random.nextInt(width - 1);
//			int y = random.nextInt(height - 1);
//			int xl = random.nextInt(6) + 1;
//			int yl = random.nextInt(12) + 1;
//			g.drawLine(x, y, x + xl, y + yl);
//		}
//		 //从另一方向画随机线
//		for (int i = 0; i < 70; i++) {
//			int x = random.nextInt(width - 1);
//			int y = random.nextInt(height - 1);
//			int xl = random.nextInt(12) + 1;
//			int yl = random.nextInt(6) + 1;
//			g.drawLine(x, y, x - xl, y - yl);
//		}
		// 生成随机数,并将随机数字转换为字母
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			int itmp = random.nextInt(26) + 65;
			char ctmp = (char) itmp;
			sRand += String.valueOf(ctmp);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(String.valueOf(ctmp), 15 * i + 10, 16);
		}
		BASE64Encoder encoder = new sun.misc.BASE64Encoder();
		// byte[] btImg = image.ttoByteArray();
		byte[] data = imageToBytes(image,"png");
		// ***************************************************************
		// 修改图片名称为实际的图片 base64编码的图片内容
		vcodeStr.append("data:image/png;base64," + encoder.encodeBuffer(data).trim());
		return sRand;
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 转换Image数据为byte数组
	 * 
	 * @param image
	 *            Image对象
	 * @param format
	 *            image格式字符串.如"gif","png"
	 * @return byte数组
	 */
	private byte[] imageToBytes(BufferedImage bImage, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
}
