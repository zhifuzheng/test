package com.xryb.zhtc.action;

import java.util.Date;

import com.xryb.zhtc.manage.SystemMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 系统服务action
 * @author wf
 */
public class SystemAction implements ISparkApplication {
	
	@Auto(name=SystemMng.class)
	private SystemMng sysMng;

	@Override
	public void run() {
		
		//pc端
		Spark.post(new Route("/system/properties/findAll") {
			@Override
			public Object handle(Request request, Response response) {
				return sysMng.findAll();
			}
		});
		
		Spark.post(new Route("/system/properties/update") {
			@Override
			public Object handle(Request request, Response response) {
				return sysMng.update(request.raw());
			}
		});
		
		//移动端
		/**
		 * 发送验证码
		 */
		Spark.post(new Route("/mobile/code/getSmsPassCode") {
			@Override
			public Object handle(Request request, Response response) {
				return sysMng.getSmsPassCode(request.raw());
			}
		});
		/**
		 * 获取服务器系统时间
		 */
		Spark.post(new Route("/mobile/time/getSystemTime") {
			@Override
			public Object handle(Request request, Response response) {
				long nowTime = new Date().getTime();
				return "{\"nowTime\":\""+nowTime+"\"}";
			}
		});
		
		Spark.post(new Route("/mobile/properties/isAudit") {
			@Override
			public Object handle(Request request, Response response) {
				return sysMng.isAudit();
			}
		});
	}

}
