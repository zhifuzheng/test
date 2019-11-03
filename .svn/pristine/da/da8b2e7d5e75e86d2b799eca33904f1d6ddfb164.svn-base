package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.BaseDictMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 数据字典action
 * @author wf
 */
public class BaseDictAction implements ISparkApplication {
	
	@Auto(name=BaseDictMng.class)
	private BaseDictMng dictMng;

	@Override
	public void run() {
		/**
		 * 添加或修改数据字典
		 */
		Spark.post(new Route("/system/baseDict/saveOrUpDict", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return dictMng.saveOrUpDict("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据dictUUID删除数据字典
		 */
		Spark.post(new Route("/system/baseDict/delDict", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return dictMng.delDict("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据dictUUID查询数据字典
		 */
		Spark.post(new Route("/system/baseDict/findDict", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return dictMng.findDict("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询数据字典列表
		 */
		Spark.post(new Route("/system/baseDict/findDictPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return dictMng.findDictPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据字典类型查询字典项列表
		 */
		Spark.post(new Route("/mobile/baseDict/findListByTypeCode", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return dictMng.findListByTypeCode("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 查询指定类型的字典项数目
		 */
		Spark.post(new Route("/system/baseDict/getTotalByTypeCode", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return dictMng.getTotalByTypeCode("jdbcread", false, request.raw(), response.raw());
			}
		});
	}

}
