package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.VipInfo;

import dbengine.util.Page;
/**
 * vip相关service
 * @author wf
 */
public interface IVipService extends IBaseService<VipInfo> {

	List<VipInfo> findDistributePage(String sourceId, boolean closeConn, Page page, Map<String, String> findMap, String order);
	
}
