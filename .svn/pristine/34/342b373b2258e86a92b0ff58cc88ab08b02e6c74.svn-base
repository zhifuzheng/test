package com.xryb.zhtc.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 适用于当前服务器的kindEditor-4.1.11图片、视频上传工具
 * @author wf
 */
public class KindEditorUtil {
	
	private static DiskFileItemFactory fac = new DiskFileItemFactory();
	
	/**
	 * @param request request对象
	 * @param imgPath 上传图片保存相对路径
	 * @param imgSize 上传图片大小限制(单位M)
	 * @param tmp 上传过程中产生的临时文件保存的绝对路径
	 * @return Map<String,String> 返回包含三个key(error,message,url)的map，error为0代表上传成功，error为1代表上传失败，
	 * message代表上传失败错误信息，url代表上传成功时文件的绝对路径
	 */
	public static String fileUpload(HttpServletRequest request, String imgPath, int imgSize, String videoPath, int videoSize, String tmpPath) throws Exception {
		//将返回结果封装在result中
		String fileType = request.getParameter("dir");
		String relativePath = "";
		//默认值大小限制10M
		int uploadSize = 10;
		Map<String, Object> result = new HashMap<String, Object>();
		if("image".equals(fileType)){
			relativePath = imgPath;
			uploadSize = imgSize;
		}
		if("media".equals(fileType)){
			relativePath = videoPath;
			uploadSize = videoSize;
		}
		String realPath = request.getSession().getServletContext().getRealPath("/" + relativePath);//获取文件夹在服务器的绝对路径		
		//String realPath = "E:/Develop/WorkSpace/zhtc/WebContent/" + relativePath;//开发阶段先保存在本机电脑		
		File tmpFile = new File(tmpPath);
		File dirs = new File(realPath);
		if (!tmpFile.exists()) {
			throw new Exception("服务器调用方法错误，临时文件目录不存在");
		}
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		//设置内存缓冲区大小，超过后写入临时文件
		fac.setSizeThreshold(4096);  
		//设置上传到服务器上文件的临时存放目录
		fac.setRepository(tmpFile);
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List<FileItem> fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {//判断是普通表单(true),文件表单(false)
				String filename = item.getName();// 取文件名
				if (filename == null || "".equals(filename.trim())) {
					continue;
				}
				long size = item.getSize();//取文件大小
				if(size > uploadSize*1024*1024){
					result.put("error", 1);
					result.put("message", "文件大小超过限制，最大" + uploadSize + "M");
					return JsonUtil.ObjToJson(result);
				}
				//获取文件扩展名格式：
				if (filename.lastIndexOf(".") == -1) {
					result.put("error", 1);
					result.put("message","文件格式错误");
					return JsonUtil.ObjToJson(result);
				}
				String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase();
				if("image".equals(fileType)){
					if(!".jpg".equals(extName) && !".jpeg".equals(extName) && !".gif".equals(extName) && !".png".equals(extName)){
						result.put("error", 1);
						result.put("message","只支持jpg,jpeg,gif,png格式的图片");
						return JsonUtil.ObjToJson(result);
					}
				}
				if("media".equals(fileType)){
					if(!".mp4".equals(extName)){
						result.put("error", 1);
						result.put("message","只支持mp4格式的视频");
						return JsonUtil.ObjToJson(result);
					}
				}
				String newFilename = UUID.randomUUID().toString().replace("-", "") + extName;
				File path = new File(realPath + "/" + newFilename);
				try {
					//将表单文件保存到指定的路径中
					item.write(path);
					//删除临时文件
					item.delete();
					//获取项目根路径
					String contextPath = request.getContextPath();
					String webappRootUrl = ReadProperties.getValue("imgServer") + contextPath + "/";
					result.put("error", new Integer(0));
					result.put("url", webappRootUrl + relativePath + newFilename);
					return JsonUtil.ObjToJson(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		result.put("error", 1);
		result.put("message", "当前网络繁忙，请稍后重试！");
		return JsonUtil.ObjToJson(result);
	}

}
