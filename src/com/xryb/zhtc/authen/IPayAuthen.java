package com.xryb.zhtc.authen;

/**
 * 支付验证接口
 * @author wf
 */
public interface IPayAuthen {
	/**
	 * 验证方法
	 */
	boolean authen(String sourceId, boolean closeConn, String entityUUID, String vipUUID);
}
