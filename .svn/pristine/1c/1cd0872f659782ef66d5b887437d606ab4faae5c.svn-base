package com.xryb.zhtc.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * EXCEL工具类
 * 
 * @author hshzh
 * 
 */
public class ExcelUtil {
	/**
	 * 写数据入excel文件
	 * 
	 * @param f
	 * @param lstData
	 * @param sheetName
	 * @param columns
	 * @return 成功:true,失败 false
	 */
	public boolean writeData(File f, List lstData, String sheetName, String[] columns) {
		WritableWorkbook wwb;
		try {
			WorkbookSettings settings = new WorkbookSettings();
			settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
			wwb = Workbook.createWorkbook(f, settings);
			WritableSheet ws = wwb.createSheet(sheetName, 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setBackground(Colour.AQUA);
			int maxRow = 60000;
			int index = 0;
			int sheetNo = 1;
			if (columns != null && columns.length > 0) {
				// 添加表头
				for (int i = 0; i < columns.length; i++) {
					ws.addCell(new Label(i, 0, columns[i], wcf));
				}
				if (lstData != null && lstData.size() > 0) {
					for (int i = 0; i < lstData.size(); i++) {
						// List obj = (List) lstData.get(i);
						String[] obj = (String[]) lstData.get(i);
						for (int j = 0; j < obj.length; j++) {
							ws.addCell(new Label(j, index + 1, (String) obj[j]));
						}
						++index;
						if (index > maxRow) {
							// 超过60000条重新建表头
							ws = wwb.createSheet(sheetName + sheetNo, 0);
							index = 0;
							++sheetNo;
							// 添加表头
							for (int k = 0; k < columns.length; k++) {
								ws.addCell(new Label(k, 0, columns[k], wcf));
							}
						}
					}
				}
				wwb.write();
				wwb.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 写数据入excel文件 重载原写入方法，修改了listData 为List<Map<String,String>> 类型，表头的字段为listData 的key,(这样更方便调整字段)
	 * 
	 * @param response
	 * @param listData
	 * @param sheetName
	 * @param columnsKey
	 * @throws java.io.IOException
	 * @throws WriteException
	 */
	public void writeData(File f, String[] columnsKey, List<Map> listData, String sheetName) throws java.io.IOException, WriteException {
		if ( listData == null || sheetName == null || columnsKey == null || listData.size() < 1) {
			// 参数错误
			return;
		}
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		WorkbookSettings settings = new WorkbookSettings();
		settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
		WritableWorkbook wwb = Workbook.createWorkbook(f, settings);
		WritableSheet ws = wwb.createSheet(sheetName, 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setBackground(Colour.AQUA);
		int maxRow = 60000;
		int index = 0;
		int sheetNo = 1;
		if (columnsKey != null && columnsKey.length > 0) {
			// 添加表头
			for (int i = 0; i < columnsKey.length; i++) {
				ws.addCell(new Label(i, 0, columnsKey[i], wcf));
			}
			if (listData != null && listData.size() > 0) {
				for (int i = 0; i < listData.size(); i++) {
					// List obj = (List) lstData.get(i);
					Map<String, Object> objMap = (Map<String, Object>) listData.get(i);
					for (int j = 0; j < columnsKey.length; j++) {
						ws.addCell(new Label(j, index + 1, objMap.get(columnsKey[j]) + ""));
					}
					++index;
					if (index > maxRow) {
						// 超过60000条重新建表头
						ws = wwb.createSheet(sheetName + sheetNo, 0);
						index = 0;
						++sheetNo;
						// 添加表头
						for (int k = 0; k < columnsKey.length; k++) {
							ws.addCell(new Label(k, 0, columnsKey[k], wcf));
						}
					}
				}
			}
			// 写入
			wwb.write();
			wwb.close();
		}
	}

	/**
	 * 导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载)
	 * 
	 * @param response
	 * @param lstData
	 * @param sheetName
	 * @param columns
	 * @throws java.io.IOException
	 * @throws WriteException
	 */
	public static void exportExcel(HttpServletResponse response, List lstData, String sheetName, String[] columns) throws java.io.IOException, WriteException {
		if (response == null || lstData == null || sheetName == null || columns == null) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型
		WorkbookSettings settings = new WorkbookSettings();
		settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
		WritableWorkbook wwb = Workbook.createWorkbook(os, settings);
		WritableSheet ws = wwb.createSheet(sheetName, 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setBackground(Colour.AQUA);
		int maxRow = 60000;
		int index = 0;
		int sheetNo = 1;
		if (columns != null && columns.length > 0) {
			// 添加表头
			for (int i = 0; i < columns.length; i++) {
				ws.addCell(new Label(i, 0, columns[i], wcf));
			}
			if (lstData != null && lstData.size() > 0) {
				for (int i = 0; i < lstData.size(); i++) {
					// List obj = (List) lstData.get(i);
					String[] obj = (String[]) lstData.get(i);
					for (int j = 0; j < obj.length; j++) {
						ws.addCell(new Label(j, index + 1, (String) obj[j]));
					}
					++index;
					if (index > maxRow) {
						// 超过60000条重新建表头
						ws = wwb.createSheet(sheetName + sheetNo, 0);
						index = 0;
						++sheetNo;
						// 添加表头
						for (int k = 0; k < columns.length; k++) {
							ws.addCell(new Label(k, 0, columns[k], wcf));
						}
					}
				}
			}
			wwb.write();
			// 写入
			os.flush();
			wwb.close();
			os.close();
		}
	}


	/**
	 * 导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载) 重载原导出方法，修改了listData 为List<Map<String,String>> 类型，表头的字段为listData 的key,(这样更方便调整字段)
	 * 
	 * @param response
	 * @param listData
	 * @param sheetName
	 * @param columnsKey
	 * @throws java.io.IOException
	 * @throws WriteException
	 */
	public static void exportExcel(HttpServletResponse response, String[] columnsKey, List<Map<String, String>> listData, String sheetName) throws java.io.IOException, WriteException {
		if (response == null || listData == null || sheetName == null || columnsKey == null || listData.size() < 1) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型
		WorkbookSettings settings = new WorkbookSettings();
		settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
		WritableWorkbook wwb = Workbook.createWorkbook(os, settings);
		WritableSheet ws = wwb.createSheet(sheetName, 0);
		WritableCellFormat wcf = new WritableCellFormat();
		wcf.setBackground(Colour.AQUA);
		int maxRow = 60000;
		int index = 0;
		int sheetNo = 1;
		if (columnsKey != null && columnsKey.length > 0) {
			// 添加表头
			for (int i = 0; i < columnsKey.length; i++) {
				ws.addCell(new Label(i, 0, columnsKey[i], wcf));
			}
			if (listData != null && listData.size() > 0) {
				for (int i = 0; i < listData.size(); i++) {
					// List obj = (List) lstData.get(i);
					Map<String, String> objMap = (Map<String, String>) listData.get(i);
					for (int j = 0; j < columnsKey.length; j++) {
						ws.addCell(new Label(j, index + 1, objMap.get(columnsKey[j]) + ""));
					}
					++index;
					if (index > maxRow) {
						// 超过60000条重新建表头
						ws = wwb.createSheet(sheetName + sheetNo, 0);
						index = 0;
						++sheetNo;
						// 添加表头
						for (int k = 0; k < columnsKey.length; k++) {
							ws.addCell(new Label(k, 0, columnsKey[k], wcf));
						}
					}
				}
			}
			wwb.write();
			// 写入
			os.flush();
			wwb.close();
			os.close();
		}
	}

	/**
	 * 通过模板导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载) 修改了listData 为List<Map<String,String>> 类型，表头的字段为listData的key,(这样更方便调整字段)
	 * 
	 * @param response
	 * @param columnsKey
	 *            对应的表头字段
	 * @param listData
	 *            对应表头的Map列表
	 * @param sheetName
	 *            文件名称
	 * @param index
	 *            写入excel的起始行
	 * @param tempAddr
	 *            模板地址
	 * @throws java.io.IOException
	 * @throws WriteException
	 * @throws BiffException
	 */
	public static void exportExcelByTemp(HttpServletResponse response, String[] columnsKey, List<Map<String, Object>> listData, String sheetName, int index, String tempAddr) throws java.io.IOException, WriteException, BiffException {
		if (response == null || sheetName == null || columnsKey == null) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型
		WorkbookSettings settings = new WorkbookSettings();
		settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
		WritableWorkbook excel = Workbook.createWorkbook(os, Workbook.getWorkbook(new File(tempAddr)), settings);
		WritableSheet ws = excel.getSheet(0);
		// ---------------------------------------------
		// WritableCell cell =ws.getWritableCell(0, 0);//获取第一个单元格
		// jxl.format.CellFormat cf = cell.getCellFormat();//获取第一个单元格的格式
		// jxl.write.Label lbl = new jxl.write.Label(0, 0, "修改后的值");//将第一个单元格的值改为“修改后的值”
		// lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样
		// ws.addCell(lbl);//将改过的单元格保存到sheet
		// ---------------------------------------------
		if (columnsKey != null && columnsKey.length > 0) {
			if (listData != null && listData.size() > 0) {
				for (int i = 0; i < listData.size(); i++) {
					Map<String, Object> objMap = (Map<String, Object>) listData.get(i);
					for (int j = 0; j < columnsKey.length; j++) {
						ws.addCell(new Label(j, index + 1, objMap.get(columnsKey[j]) + ""));
					}
					++index;
				}
			}
			excel.write();
			// 写入
			os.flush();
			excel.close();
			os.close();
		}
	}

	/**
	 * 通过双模板导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载) 修改了listData 为List<Map<String,String>> 类型，表头的字段为listData的key,(这样更方便调整字段)
	 * 
	 * @param response
	 * @param columnsKey
	 *            对应的表头字段
	 * @param listData
	 *            对应表头的Map列表
	 * @param sheetName
	 *            文件名称
	 * @param index
	 *            写入excel的起始行
	 * @param tempAddr
	 *            模板地址
	 * @param time1
	 *            时间
	 * @param time2
	 * @throws java.io.IOException
	 * @throws WriteException
	 * @throws BiffException
	 */
	public static void exportExcelBy2Temp(HttpServletResponse response, String[] columnsKey, List<Map<String, Object>> listData, String sheetName, int index, String tempAddr, String time1, String time2) throws java.io.IOException, WriteException, BiffException {
		if (response == null || sheetName == null || columnsKey == null) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型
		WritableWorkbook excel = Workbook.createWorkbook(os, Workbook.getWorkbook(new File(tempAddr)));
		WritableSheet ws = excel.getSheet(0);
		// ---------------------------------------------
		WritableCell cell = ws.getWritableCell(0, 3);// 获取单元格
		jxl.format.CellFormat cf = cell.getCellFormat();// 获取单元格的格式
		jxl.write.Label lbl = new jxl.write.Label(0, 3, time1);// 将第一个单元格的值改为“修改后的值”
		lbl.setCellFormat(cf);// 将修改后的单元格的格式设定成跟原来一样
		ws.addCell(lbl);// 将改过的单元格保存到sheet

		WritableCell cell2 = ws.getWritableCell(0, 0);// 获取单元格
		jxl.format.CellFormat cf2 = cell2.getCellFormat();// 获取元格的格式

		WritableCell cell3 = ws.getWritableCell(0, 1);// 获取单元格
		jxl.format.CellFormat cf3 = cell3.getCellFormat();// 获取元格的格式

		// ---------------------------------------------
		if (columnsKey != null && columnsKey.length > 0) {
			if (listData != null && listData.size() > 0) {
				for (int i = 0; i < listData.size(); i++) {
					Map<String, Object> objMap = (Map<String, Object>) listData.get(i);
					for (int j = 0; j < columnsKey.length; j++) {
						ws.addCell(new Label(j, index + 1, objMap.get(columnsKey[j]) + ""));
					}
					++index;
				}
			}
			ws.mergeCells(0, index + 1, columnsKey.length - 4, index + 1);// 合并单元格
			ws.mergeCells(0, index + 2, columnsKey.length - 4, index + 2);// 合并单元格
			// --------------------------------------------
			jxl.write.Label lbl2 = new jxl.write.Label(0, index + 1, "南明区教育局基础教育科X");// 将第一个单元格的值改为“修改后的值”
			// 构造格式：ARIAL字体、10号、粗体、非斜体、无下划线、黑色
			WritableFont fmtx2TotalCaption = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

			WritableCellFormat cellFormat = new WritableCellFormat(fmtx2TotalCaption);
			cellFormat.setAlignment(jxl.format.Alignment.RIGHT);
			// cellFormat.setFont();
			lbl2.setCellFormat(cellFormat);
			ws.addCell(lbl2);// 将改过的单元格保存到sheet

			jxl.write.Label lbl3 = new jxl.write.Label(0, index + 2, time2);// 将第一个单元格的值改为“修改后的值”
			lbl3.setCellFormat(cf3);// 将修改后的单元格的格式设定成跟原来一样
			WritableCellFormat cellFormat3 = new WritableCellFormat(fmtx2TotalCaption);
			cellFormat3.setAlignment(jxl.format.Alignment.RIGHT);
			lbl3.setCellFormat(cellFormat3);
			ws.addCell(lbl3);// 将改过的单元格保存到sheet
			// wcf_title.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
			// ---------------------------------------------
			excel.write();
			// 写入
			os.flush();
			excel.close();
			os.close();
		}
	}

	/**
	 * 通过双模板导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载) 修改了listData 为List<Map<String,String>> 类型，表头的字段为listData的key,(这样更方便调整字段)
	 * 
	 * @param response
	 * @param columnsKey
	 *            对应的表头字段
	 * @param listData
	 *            对应表头的Map列表
	 * @param sheetName
	 *            文件名称
	 * @param index
	 *            写入excel的起始行
	 * @param tempAddr
	 *            模板地址
	 * @param time1
	 *            时间
	 * @param time2
	 * @throws java.io.IOException
	 * @throws WriteException
	 * @throws BiffException
	 */
	public static void exportExcelBy3Temp(HttpServletResponse response, String[] columnsKey, List<Map<String, Object>> listData, String sheetName, int index, String tempAddr, String time1) throws java.io.IOException, WriteException, BiffException {
		if (response == null || sheetName == null || columnsKey == null) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((sheetName + ".xls").getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型
		WritableWorkbook excel = Workbook.createWorkbook(os, Workbook.getWorkbook(new File(tempAddr)));
		WritableSheet ws = excel.getSheet(0);
		// ---------------------------------------------
		WritableCell cell = ws.getWritableCell(0, 0);// 获取单元格
		jxl.format.CellFormat cf = cell.getCellFormat();// 获取单元格的格式
		jxl.write.Label lbl = new jxl.write.Label(0, 0, time1);// 将第一个单元格的值改为“修改后的值”
		lbl.setCellFormat(cf);// 将修改后的单元格的格式设定成跟原来一样
		ws.addCell(lbl);// 将改过的单元格保存到sheet

		// ---------------------------------------------
		if (columnsKey != null && columnsKey.length > 0) {
			if (listData != null && listData.size() > 0) {
				for (int i = 0; i < listData.size(); i++) {
					Map<String, Object> objMap = (Map<String, Object>) listData.get(i);
					for (int j = 0; j < columnsKey.length; j++) {
						ws.addCell(new Label(j, index + 1, objMap.get(columnsKey[j]) + ""));
					}
					++index;
				}
			}
			excel.write();
			// 写入
			os.flush();
			excel.close();
			os.close();
		}
	}

	/**
	 * 从excel中读数据
	 * 
	 * @param file
	 * @return
	 */
	public List readData(File file) {
		List list = new ArrayList();
		if (".xls".equals(file.getName().substring(file.getName().length() - 4, file.getName().length())) || ".csv".equals(file.getName().substring(file.getName().length() - 4, file.getName().length())) || ".xlsx".equals(file.getName().substring(file.getName().length() - 4, file.getName().length()))) {
			try {
				FileInputStream fis = new FileInputStream(file);
				WorkbookSettings settings = new WorkbookSettings();
				settings.setWriteAccess(null);// 为解决Linux环境下 报ArrayIndexOutOfBoundsException 错误的问题（问题出在WriteAccessRecord文件里，分析代码发现，byte数组data的最大长度被定义为112，当被传入的参数userName达到一定长度时就会抛错。 跟踪代码WritableWorkbookImpl发现，userName实际就是WorkbookSettings类中的writeAccess字段，亦即生成Excel是的用户信息。可能在linux环境UTF8下每个汉字的字节数为3位(Windows中是2位)的缘故，出现了上诉的奇异现象。 ）
				Workbook wb = Workbook.getWorkbook(file, settings);
				Sheet sheet = wb.getSheet(0);
				int rowCount = sheet.getRows();
				int cols = sheet.getColumns();
				String[] temp = null;
				for (int i = 0; i < rowCount; i++) {
					temp = new String[cols];
					for (int j = 0; j < cols; j++) {
						Cell cell = sheet.getCell(j, i);
						temp[j] = cell.getContents();
					}
					list.add(temp);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				list = null;
			}
		} else {
			return null;
		}
		return list;
	}

	/**
	 * 读取csv文件
	 * 
	 * @param file
	 *            csv文件(路径+文件)
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static List<String> readCsv(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		List<String> dataList = new ArrayList<String>();
		BufferedReader br = null;
		// CSV文件编码
		String ENCODE = "UTF-8";
		File file = new File(fileName);
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(file));
			br = new BufferedReader(new InputStreamReader(in, "GBK"));
			String line = "";
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				dataList.add(line);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}
}
