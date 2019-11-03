package com.xryb.zhtc.action;

import com.xryb.zhtc.manage.AddrMng;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.annotation.Auto;
import spark.servlet.ISparkApplication;
/**
 * 地址action
 * @author wf
 */
public class AddrAction implements ISparkApplication {
	
	@Auto(name=AddrMng.class)
	private AddrMng addrMng;

	@Override
	public void run() {
		//pc端
		/**
		 * 添加或修改地址
		 */
		Spark.post(new Route("/shop/addr/saveOrUpAddr", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return addrMng.saveOrUpAddr("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据addrUUID删除地址
		 */
		Spark.post(new Route("/shop/addr/delAddr", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.delAddr("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据entityUUID查询地址列表
		 */
		Spark.post(new Route("/shop/addr/findAddrList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.findAddrList("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询地址列表
		 */
		Spark.post(new Route("/shop/addr/findAddrPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.findAddrPage("jdbcread", false, request.raw(), response.raw());
			}
		});
		
		//移动端
		/**
		 * 添加或修改地址
		 */
		Spark.post(new Route("/mobile/addr/saveOrUpAddr", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return addrMng.saveOrUpAddr("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据addrUUID删除地址
		 */
		Spark.post(new Route("/mobile/addr/delAddr", true, "jdbcwrite") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.delAddr("jdbcwrite", false, request.raw(), response.raw());
			}
		});
		/**
		 * 根据entityUUID查询地址列表
		 */
		Spark.post(new Route("/mobile/addr/findAddrList", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.findAddrList("jdbcread", false, request.raw(), response.raw());
			}
		});
		/**
		 * 分页查询地址列表
		 */
		Spark.post(new Route("/mobile/addr/findAddrPage", true, "jdbcread") {
			@Override
			public Object handle(Request request, Response response) {
				return addrMng.findAddrPage("jdbcread", false, request.raw(), response.raw());
			}
		});

	}

}
