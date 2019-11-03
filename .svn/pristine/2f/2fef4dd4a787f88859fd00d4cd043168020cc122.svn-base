package com.xryb.zhtc.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.GsonBuilder;

/**
 * Json操作相关工具类（依赖引用google的gson包）
 * @author hshzh
 * 
 */
public class JsonUtil {
	/**
	 * 对象转换为json字符串
	 * @param obj
	 * @return
	 */
	public static String ObjToJson(Object obj) {
		if (obj == null) {
			return "";
		}
		return (new GsonBuilder().create()).toJson(obj);
	}

	/**
	 * json字符串转换为对象
	 * @param json
	 * @param c
	 * @return
	 */
	public static Object JsonToObj(String json, Class<?> c) {
		if (json == null || "".equals(json) || c == null || "null".equals(json)) {
			return null;
		}
		return (new GsonBuilder().create()).fromJson(json, c);
	}
	
	/**
	 * json字符串转换为对象
	 * @param json
	 * @param type
	 * @return
	 */
	public static Object JsonToObj(String json, Type type) {
		if (json == null || "".equals(json) || "null".equals(json) || "{}".equals(json) || type == null) {
			return null;
		}
		return (new GsonBuilder().create()).fromJson(json, type);
	}

	/**
	 * 生成json树
	 * 
	 * @param treeBeanList
	 *            需要生成json树的List<TreeBean>
	 * @param showTopNode
	 *            是否显示顶级节点
	 * @param topCode
	 *            顶级节点编号
	 * @return
	 */
	public static String getJsonTree(List<TreeBean> treeBeanList, boolean showTopNode, String topCode) {
		if (treeBeanList == null || treeBeanList.size() < 1) {
			// 参数错误
			return null;
		}
		try {
			if (!showTopNode) {
				// 不需要生成顶级节点,只需要生成下级节点
				List<TreeBean> newTreeList = new ArrayList<TreeBean>();
				getChildJson(treeBeanList, topCode, newTreeList);
				return ObjToJson(newTreeList);
			} else {
				for (TreeBean tree : treeBeanList) {
					if (topCode.equals(tree.getId())) {// 先找到顶级节点
						// 开始递归生成子节点
						List<TreeBean> newTreeList = new ArrayList<TreeBean>();
						tree.setChildren(newTreeList);
						getChildJson(treeBeanList, topCode, newTreeList);
						return ObjToJson(tree);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * 生成子节点
	 * @param treeBeanList
	 * @param parentCode
	 * @param treeList
	 */
	private static void getChildJson(List<TreeBean> treeBeanList, String parentCode, List<TreeBean> treeList) {
		if (treeBeanList == null || treeBeanList.size() < 1 || parentCode == null || "".equals(parentCode)) {
			return;
		}
		for (TreeBean tree : treeBeanList) {
			if (parentCode.equals(tree.getpId())) {// 判断是否存在直接下级节点
				treeList.add(tree);
				// 递归生成子节点
				List<TreeBean> newTreeList = new ArrayList<TreeBean>();
				tree.setChildren(newTreeList);
				getChildJson(treeBeanList, tree.getId(), newTreeList);
			}
		}
	}
	
	
	/**
	 * 根据参数匹配sql语句,用list中的参数替换StringBuilder中的问号，开发调试sql参数时所用
	 * @param srcSql
	 * @param srcParams
	 * @return 匹配后的sql字符串
	 */
	public static String matchSQLStr(StringBuilder srcSql,List<Object> srcParams) {
		StringBuilder sql = new StringBuilder(srcSql.toString());
		List<Object> params = srcParams;
		
		if(sql==null || params==null ) {
			return "sql或params为NULL，不需要匹配";
		}
		if(sql.length()==0 || params.size()==0) {
			return "sql或params没有内容，不需要匹配";
		}
		
		if(!(params.get(0) instanceof String)) {
			return "params列表的元素不是String，不可匹配";
		}
		
		String questionMark = "?";
		int markNum = 0;//统计问号的次数
		String sqlString = sql.toString();
		System.out.println("---匹配前的sql----->"+sqlString);
        while (sqlString.contains(questionMark)) {
        	sqlString = sqlString.substring(sqlString.indexOf(questionMark) + questionMark.length());
        	markNum++;
        }
        if(markNum != params.size()) {
        	return "params中元素个数和sql中的问号数量不相等";
        }
        
        int pointer = 0;
        String sqlStr2 = sql.toString();
        while (sqlStr2.contains(questionMark)) {
        	String param = (String)params.get(pointer);
        	param = "'"+param+"'";
        	pointer++;
        	int start = sqlStr2.indexOf(questionMark);
        	int end = start+questionMark.length();
        	sql.replace(start, end, param);
        	sqlStr2 = sql.toString(); //为了获取每个没有被替换的问号
        }
        String result = "---匹配后的sql----->"+sql.toString();
		return result;
	}
	
	
	
	
	
	
	
	
	
}
