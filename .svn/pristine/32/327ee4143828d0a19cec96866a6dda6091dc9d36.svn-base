package com.xryb.zhtc.util;
import java.io.File;  
import java.io.FileOutputStream;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.URL;  
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;


/**
 * 图片下载
 * @author zzf
 */
public class DownloadImgUtil {
	/*
	 * @param urlList  图片下载地址集合
	 * @param savePath  保存路径
	 */
	public static boolean download(List<String> urlList ,String savePath) {
		String[] files = new String[urlList.size()];
		urlList.toArray(files);
		//开始批量下载
		try {
			long nowTimeString = System.currentTimeMillis();
			String downloadFilename = nowTimeString+"";//文件的名称
			downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
			
			for(int i = 0; i < files.length; i++) {
				URL url = new URL(files[i]);// 构造URL
				URLConnection con = url.openConnection();// 打开连接
				con.setConnectTimeout(5 * 1000);// 设置请求超时为5s
				InputStream is = con.getInputStream();// 输入流
				byte[] bs = new byte[1024];// 1K的数据缓冲
				int len;// 读取到的数据长度
				File sf = new File(savePath+downloadFilename);// 输出的文件流
				if (!sf.exists()) {
					sf.mkdirs();
				}
				String[] fileNameArr = files[i].split("/");
				String fileName = fileNameArr[fileNameArr.length-1];
				OutputStream os = new FileOutputStream(sf.getPath()+"/"+fileName);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				// 完毕，关闭所有链接
				os.close();
				is.close();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 删除图片
	 * @param imgUrl 图片完整路径
	 * @return false 删除失败 true 删除成功
	 */
	public static boolean deleteImg(String imgUrl) {
		File file = new File(imgUrl);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	
	

}
