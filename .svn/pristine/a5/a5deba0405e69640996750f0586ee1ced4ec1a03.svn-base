package com.xryb.zhtc.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
 * HttpServletRequest上传文件工具类
 * 
 * @author hshzh
 * 
 */
public class UpFileUtil {
	final long MAX_SIZE = 10 * 1024 * 1024 * 1024;
	/**
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 
	 * @param request
	 * @param filePath
	 * @return
	 */
	public String saveHttpUpFile(HttpServletRequest request, String filePath) {
		if (request == null) {
			return null;
		}
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fileNamePathS;
	}
	
	/**
	 * 重载
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 同时将提交的普通字段保存到map参数中
	 * @param request
	 * @param filePath
	 * @param map
	 * @return 上传的文件名
	 */
	public String saveHttpUpFile(HttpServletRequest request, String filePath,Map<String,Object> map) {
		if (request == null) {
			return null;
		}
		File dirsFile = new File(filePath);
		//判断该文件夹是否存在
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();//创建文件夹
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();//创建文件上传对象，实现文件上传
		// 设置内存缓冲区大小默认值为10k，超过后写入临时文件
		fac.setSizeThreshold(4096);  
		// 设置上传到服务器上文件的临时存放目录 -- 非常重要，防止存放到系统盘造成系统盘空间不足
		fac.setRepository(new File(ReadProperties.getValue("uploadtmp")));
		//处理上传的文件数据
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");//设置编码格式
		// 设置上传文件总量的最大值（所有上传文件）
		upload.setSizeMax(MAX_SIZE);  // 文件上传上限10G
		
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";//文件名
		String dbName = "";
		String extName = "";//文件扩展名
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				String fieldName = item.getFieldName();//获取字段名
				String fieldValue = null;
				try {
					fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			item.delete();
		}
		return fileNamePathS;
	}
	
	
	/**
	 * 只保存一张图片
	 * HttpServletRequest提交的一张图片保存到指定的目录,返回图片属性和路径到map中
	 * @param request
	 * @param filePath
	 * @param imgMap
	 * @return imgMap
	 */
	public Map<String,String> saveHttpUpImage(HttpServletRequest request, String filePath,Map<String,String> imgMap) {
		if (request == null) {
			return null;
		}
		
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		String imgType = "";
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		String imageSize = "";
		String contentType = "";
		Iterator<FileItem> it = fileList.iterator();
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取真实的文件名
				imageSize = item.getSize()+"";// 取文件大小
				contentType = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				String fieldName = item.getFieldName();//获取字段名
				try {
					if(fieldName.equals("imgType")) {
						imgType= item.getString("UTF-8");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			item.delete();
		}
		
		imgMap.put("imgName",actualName);
		imgMap.put("dbName",dbName+extName);
		imgMap.put("imgPath",fileNamePathS);
		imgMap.put("imgExt",extName);
		imgMap.put("imgSize",imageSize);
		imgMap.put("imgContentType",contentType);
		imgMap.put("imgType",imgType);
		return imgMap;
	}
	
	
	
	/**
	 * 重载
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 同时将提交的普通字段保存到map参数中
	 * 
	 * @param request
	 * @param filePath
	 * @param map
	 * @return
	 */
	public String saveHttpUpFiles(HttpServletRequest request, String filePath,Map map,Map fileName,String num) {
		if (request == null) {
			return null;
		}
		String index = request.getParameter("index");
		System.out.println("index"+index);
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
			System.out.println(fileList);
		} catch (FileUploadException ex) {
			//ex.printStackTrace();
		}
		
		if (fileList==null) {
			//fileName.put("merImger0",request.getParameter("merImger0"));
			return null;
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				String hg = item.getFieldName();
				
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+ dbName + extName;
					}
					/*fileName.put(hg, dbName + extName);*/
					if(fileName.get(hg)!=null) {
						fileName.put(hg, fileName.get(hg)+","+dbName + extName);
						/*fileName.put(hg, fileName.get(hg)+dbName + extName);*/
					}else {
						fileName.put(hg, dbName + extName);
					}
					
				}
				while (file.exists());
				File saveFile;
				if("1".equals(num)) {
					 saveFile = new File(filePath+"/" + dbName + extName);
				}else {
					 saveFile = new File(filePath + dbName + extName);
				}
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				String fieldName = item.getFieldName();//获取字段
				String fieldValue = null;
				try {
					fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return fileNamePathS;
	}
	
	
	/**
	 * 重载
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 同时将提交的普通字段保存到map参数中
	 * @param request
	 * @param filePath
	 * @param map
	 * @return 上传的文件名
	 */
	public String saveHttpUpFileGSY(HttpServletRequest request, String filePath,Map map) {
		if (request == null) {
			return null;
		}
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		
		// 设置内存缓冲区，超过后写入临时文件
		fac.setSizeThreshold(4096);  
		// 设置上传到服务器上文件的临时存放目录 -- 非常重要，防止存放到系统盘造成系统盘空间不足
		fac.setRepository(new File(ReadProperties.getValue("spImgPath")));
		
		
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		
		// 设置单个文件的最大上传值
		upload.setSizeMax(MAX_SIZE);  // 文件上传上限10G
		
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				String fieldName = item.getFieldName();//获取字段名
				String fieldValue = null;
				try {
					fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			item.delete();
		}
		return fileNamePathS;
	}
	
	/**
	 * 重载
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 同时将提交的普通字段保存到map参数中
	 * @param request
	 * @param filePath
	 * @param map
	 * @return 上传的文件名
	 */
	public String saveHttpUpFileGSYOoverall(HttpServletRequest request, String filePath,Map map) {
		if (request == null) {
			return null;
		}
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		
		// 设置内存缓冲区，超过后写入临时文件
		fac.setSizeThreshold(4096);  
		// 设置上传到服务器上文件的临时存放目录 -- 非常重要，防止存放到系统盘造成系统盘空间不足
		fac.setRepository(new File(ReadProperties.getValue("overallImger")));
		
		
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		
		// 设置单个文件的最大上传值
		upload.setSizeMax(MAX_SIZE);  // 文件上传上限10G
		
		System.out.println("changdu==="+upload.getFileSizeMax());
		
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				String fieldName = item.getFieldName();//获取字段名
				String fieldValue = null;
				try {
					fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
					System.out.println("fieldValue---"+fieldValue);
					System.out.println("fieldName---"+fieldName);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			item.delete();
		}
		return fileNamePathS;
	}
	
	/**
	 * 重载
	 * HttpServletRequest提交的文件保存到指定的目录,返回保存的文件全路径， 如果有多个文件则用","分开
	 * 同时将提交的普通字段保存到map参数中
	 * @param request
	 * @param filePath
	 * @param map
	 * @return 上传的文件名
	 */
	public String saveHttpUpFileGSYVideo(HttpServletRequest request, String filePath,Map map) {
		if (request == null) {
			return null;
		}
		File dirsFile = new File(filePath);
		if (!dirsFile.exists()) {
			dirsFile.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		
		// 设置内存缓冲区，超过后写入临时文件
		fac.setSizeThreshold(4096);  
		// 设置上传到服务器上文件的临时存放目录 -- 非常重要，防止存放到系统盘造成系统盘空间不足
		fac.setRepository(new File(ReadProperties.getValue("spVideoPath")));
		
		
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		
		// 设置单个文件的最大上传值
		upload.setSizeMax(MAX_SIZE);  // 文件上传上限10G
		
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);//读取请求中的所有表单项
		} catch (FileUploadException ex) {
			ex.printStackTrace();
		}
		Iterator<FileItem> it = fileList.iterator();
		String actualName = "";
		String dbName = "";
		String extName = "";
		String fileNamePathS = "";
		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {// 判断是普通表单(true),文件表单(false)
				actualName = item.getName();// 取文件名
				long size = item.getSize();// 取文件大小
				String type = item.getContentType();// 取文件类型
				if (actualName == null || actualName.trim().equals("")) {
					continue;
				}
				// 扩展名格式：
				if (actualName.lastIndexOf(".") >= 0) {
					extName = actualName.substring(actualName.lastIndexOf("."));
				}
				File file = null;
				do {
					// 生成文件名：
					dbName = UUID.randomUUID().toString();
					file = new File(filePath + dbName + extName);
					// 多个文件用","分隔
					if (fileNamePathS.equals("")) {
						fileNamePathS = dbName + extName;
					} else {
						fileNamePathS += ","+dbName + extName;
					}
				} while (file.exists());
				File saveFile = new File(filePath + dbName + extName);
				try {
					item.write(saveFile);// 将表单文件保存到指定的路径中
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				String fieldName = item.getFieldName();//获取字段名
				String fieldValue = null;
				try {
					fieldValue = item.getString("UTF-8");
					map.put(fieldName, fieldValue);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			item.delete();
		}
		return fileNamePathS;
	}
}
