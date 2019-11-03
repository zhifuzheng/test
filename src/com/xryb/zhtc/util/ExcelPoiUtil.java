package com.xryb.zhtc.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 处理excel相关工具类(需要导入的包，dom4j-1.6.1.jar,poi-3.9.jar,poi-excelant-3.9.jar,poi-ooxml-3.9.jar,poi-ooxml-schemas-3.9.jar,poi-scratchpad-3.9.jar,xmlbeans-2.6.0.jar)
 * @author hshzh
 *
 */
public class ExcelPoiUtil {
	/** 
	 * excel导出到输出流 
	 * 谁调用谁负责关闭输出流  
	 * @param os 输出流 
	 * @param excelExtName excel文件的扩展名，支持xls和xlsx，不带点号 
	 * @param columnsKey 表头
	 * @param listData  
	 * @throws IOException 
	 */  
	public static void writeExcel(OutputStream os, String excelExtName,String[] columnsKey, List<Map<String,Object>> listData) throws IOException{  
	    Workbook wb = null;
	    try {  
	        if ("xls".equals(excelExtName)) {  
	            wb = new HSSFWorkbook();
	        } else if ("xlsx".equals(excelExtName)) { 
	            wb = new XSSFWorkbook();  
	        } else {  
	            throw new Exception("当前文件不是excel文件");
	        }  
            Sheet sheet = wb.createSheet(excelExtName);
			// 添加表头
            Row row = sheet.createRow(0);
			for (int i = 0; i < columnsKey.length; i++) {
                Cell cell = row.createCell(i);  
                cell.setCellValue(columnsKey[i]); 
			}
            for (int j = 0; j < listData.size(); j++) {  
				Map<String,Object> objMap = (Map<String,Object>)listData.get(j); 
                Row rowdata = sheet.createRow(j+1);
                for (int k = 0; k < columnsKey.length; k++) {  
                    Cell cell = rowdata.createCell(k);  
                    cell.setCellValue(objMap.get(columnsKey[k])+"");  
                }  
            }  
	        wb.write(os);
            os.flush();	
			os.close();  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (wb != null) {  
	            wb=null;  
	        }  
	    }
	}
	/**
	 * 导出excel(弹出保存/打开excel的对话框,无需在服务器保存文件然后再下载)
	 * 重载原导出方法，修改了listData 为List<Map<String,String>> 类型，表头的字段为listData 的key,(这样更方便调整字段)
	 * @param response
	 * @param listData
	 * @param sheetName
	 * @param columnsKey
	 * @throws java.io.IOException
	 * @throws WriteException 
	 */
	public static void exportExcel(HttpServletResponse response, String[] columnsKey,List<Map<String,Object>> listData, String excelName) throws java.io.IOException {
		if (response == null || listData == null || excelName == null || columnsKey == null || listData.size()<1) {
			// 参数错误
			return;
		}
		ServletOutputStream os = response.getOutputStream();// 取得输出流
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String((excelName).getBytes(), "iso-8859-1"));// 设定输出文件头
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 定义输出类型		
	    Workbook wb = null;
	    try {  
	        if ("xls".equals(excelName.substring(excelName.indexOf(".")+1,excelName.length()))){
	            wb = new HSSFWorkbook();
	        } else if ("xlsx".equals(excelName.substring(excelName.indexOf(".")+1,excelName.length()))) { 
	            wb = new XSSFWorkbook();  
	        } else {  
	            throw new Exception("当前文件不是excel文件");  
	        }  
            Sheet sheet = wb.createSheet(excelName.substring(0,excelName.indexOf(".")-1));
			// 添加表头
            Row row = sheet.createRow(0);
			for (int i = 0; i < columnsKey.length; i++) {
                Cell cell = row.createCell(i);  
                cell.setCellValue(columnsKey[i]); 
			}
            for (int j = 0; j < listData.size(); j++) {  
				Map<String,Object> objMap = (Map<String,Object>)listData.get(j); 
                Row rowdata = sheet.createRow(j+1);
                for (int k = 0; k < columnsKey.length; k++) {  
                    Cell cell = rowdata.createCell(k);  
                    cell.setCellValue(objMap.get(columnsKey[k])+"");  
                }  
            }  
	        wb.write(os);
            os.flush();	
			os.close();
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } finally {
            if(os != null){
            	os.close();
            }
	        if (wb != null) {	        	
	            wb=null;  
	        }
	    }
	}
	
    /**
     * 读取excel行数据，自适应Excel2003与Excel2007版本(xls与xlsx格式)
     * @param filepath Excel文件地址全路径
     * @param titleRowNum 标题字段所在行，0为起始行
     * @return List<Map<String, Object>> 标题作为map的key,cell为map的value
     * @throws Exception
     */
	public static List<Map<String, Object>> readExcel(String filepath,int titleRowNum) throws Exception{
		if(filepath == null || "".equals(filepath) || "null".equals(filepath)){
			return null;
		}
	    String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());  
	    InputStream is = null;
	    Workbook wb = null;  
	    try {  
	        is = new FileInputStream(filepath);	          
	        if (fileType.equals("xls")) {  
	            wb = new HSSFWorkbook(is);  
	        } else if (fileType.equals("xlsx")) {  
	            wb = new XSSFWorkbook(is);
	        } else {  
	            throw new Exception("读取的不是excel文件");
	        }	          
	        List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();//对应excel文件 	          
	        int sheetSize = wb.getNumberOfSheets();  
	        for (int i = 0; i < sheetSize; i++) {//遍历sheet页
	            Sheet sheet = wb.getSheetAt(i);            
	            List<String> titles = new ArrayList<String>();//放置所有的标题  
	              
	            int rowSize = sheet.getLastRowNum() + 1;  
	            for (int j = titleRowNum; j < rowSize; j++) {//遍历行  
	                Row row = sheet.getRow(j);
	                if (row == null) {//略过空行  
	                    continue;  
	                }  
	                int cellSize = row.getLastCellNum();//行中有多少个单元格，也就是有多少列  
	                if (j == titleRowNum) {//取标题行字段  
	                    for (int k = 0; k < cellSize; k++) {  
	                        Cell cell = row.getCell(k);  
	                        titles.add(cell.toString());  
	                    }  
	                } else {//其他行是数据行  
	                    Map<String, Object> rowMap = new HashMap<String, Object>();//对应一个数据行 
	                    for (int k = 0; k < titles.size(); k++) {  
	                        Cell cell = row.getCell(k);  
	                        String key = titles.get(k);  
	                        String value = null;  
	                        if (cell != null) {  
	                            value = cell.toString();  
	                        }  
	                        rowMap.put(key, value);  
	                    }  
	                    result.add(rowMap);  
	                } 
	            }
	        }	          
	        return result;  
	    } catch (FileNotFoundException e) {  
	        throw e;  
	    } finally {  
	        if (wb != null) {	        	
	            wb=null;  
	        }  
	        if (is != null) {  
	            is.close();  
	        }  
	    }  
	}
	
	public static void main(String[] args){
		ExcelPoiUtil epu = new ExcelPoiUtil();
		try {
			List<Map<String, Object>> exceList = epu.readExcel("d:/测试新格式表格导入.xlsx",0);
			System.out.println(exceList.size());
			for(Map map : exceList){
				System.out.println("测试标题1:"+map.get("测试标题1"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
