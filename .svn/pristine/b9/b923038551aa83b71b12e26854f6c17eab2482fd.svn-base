package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.PropertiesMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 系统配置文件action
 * @author wf
 */
public class PropertiesAction implements ISparkApplication {
	
	@Auto(name=PropertiesMng.class)
	private PropertiesMng prMng;

	@Override
	public void run() {
		
		Spark.post(new Route("/system/properties/findAll") {
			@Override
			public Object handle(Request request, Response response) {
				return prMng.findAll();
			}
		});
		
		Spark.post(new Route("/system/properties/update") {
			@Override
			public Object handle(Request request, Response response) {
				return prMng.update(request.raw());
			}
		});
		
		Spark.post(new Route("/mobile/properties/isAudit") {
			@Override
			public Object handle(Request request, Response response) {
				return prMng.isAudit();
			}
		});
	}

}
