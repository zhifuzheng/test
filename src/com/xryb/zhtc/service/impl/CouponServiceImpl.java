package com.xryb.zhtc.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Coupon;
import com.xryb.zhtc.entity.CouponOfIssuer;
import com.xryb.zhtc.entity.CouponOfUser;
import com.xryb.zhtc.entity.CouponOffline;
import com.xryb.zhtc.entity.CouponUserList;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemCat;
import com.xryb.zhtc.entity.ItemParam;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.util.DateTimeUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
import spark.annotation.Auto;

/**
 * 优惠劵持久成实现类
 * @author apple
 */
public class CouponServiceImpl implements ICouponService {
	@Auto(name = EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name = SqlDao.class)
	private SqlDao sqldao;
	
	
	
	
	
	
	
	
	
	
	/**
	 * 线下优惠劵数据绑定
	 */
	@Override
	public boolean updateOfflineCoupon(String sourceId, boolean closeConn, Map<String, Object> params) {
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sql ="update coupon_offline set couponState=2,vipUUID='"+params.get("vipUUID")+"',vipName='"+params.get("vipName")+"',useTime='"+nowTime+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 线下优惠劵信息
	 */
	@Override
	public CouponOffline findCouponOfflineInfo(String sourceId, boolean closeConn, String couponUUID){
		String sql = "select * from coupon_offline where couponUUID='"+couponUUID+"'";
		return (CouponOffline)sqldao.findEntityBySql(sourceId, sql, CouponOffline.class, closeConn, null);
	}
	
	@Override
	public CouponOfIssuer findCouponOfflineIssuerInfo(String sourceId, boolean closeConn, String couponUUID) {
		String sql = "select ci.*"
				+ " from coupon_offline co join coupon_manage_issuer ci"
				+ " on co.batchUUID=ci.batchUUID"
				+ " where co.couponUUID='"+couponUUID+"'";
		return (CouponOfIssuer) sqldao.findEntityBySql(sourceId, sql, CouponOfIssuer.class, closeConn, null);
	}


	/**
	 * 卡劵立即使用跳转获取商品列表
	 */
	@Override
	public List<Item> findGoodsCouponList(String sourceId, boolean closeConn, Page page, Map<String, String> param) {
		String businessUUID = param.get("businessUUID");
		String catUUID = param.get("catUUID");
		String goodUUID = param.get("goodUUID");
		String searchWords = param.get("searchWords");
		
		StringBuilder sql = new StringBuilder("select * from item where itemStatus='1' and entityUUID='"+businessUUID+"'");
		
		//2.店铺指定分类可用
		if(!StringUtils.isNullOrEmpty(catUUID) && StringUtils.isNullOrEmpty(goodUUID)) {
			sql.append(" and catUUID='"+catUUID+"'");
		}
		
		//3.店铺指定商品可用
		if(!StringUtils.isNullOrEmpty(goodUUID)) {
			sql.append(" and itemUUID='"+goodUUID+"'");
		}
		
		if(!StringUtils.isNullOrEmpty(searchWords)) {//商品名称搜索
			sql.append(" and title like '%"+searchWords+"%'");
		}
		return (List<Item>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, Item.class, page, null);
	}


	/**
	 * 获取店铺商品信息
	 */
	@Override
	public List<Item> findEntityGoodInfo(String sourceId, boolean closeConn, String businessUUID,String catUUID,String goodUUID,Integer nubmber) {
		String sql = null;
		if(nubmber == 1) {
			sql = "select * from item where itemStatus='1' and  entityUUID='"+businessUUID+"' limit 0,3";
		}
		if(nubmber == 2) {
			sql = "select * from item where itemStatus='1' and entityUUID='"+businessUUID+"' and catUUID='"+catUUID+"' limit 0,3";
		}
		if(nubmber == 3) {
			sql = "select * from item where itemStatus='1' and entityUUID='"+businessUUID+"' and itemUUID='"+goodUUID+"'";
		}
		
		return (List<Item>)sqldao.findListBySql(sourceId, sql, Item.class, closeConn, null);
	}


	/**
	 * 领券中心获取可领取优惠劵列表
	 */
	@Override
	public List<CouponUserList> findAllCouponList(String sourceId, boolean closeConn,Page page) {
		String sql = "select * from coupon_manage_issuer where isDel=1 and isRelease=1 and stock>0";
		return (List<CouponUserList>)sqldao.findPageByMysql(sourceId, sql, closeConn, CouponUserList.class, page, null);
	}
	

	/**
	 * 获取分类信息
	 */
	@Override
	public ItemCat findItemCatInfo(String sourceId, boolean closeConn, String catUUID) {
		String sql = "select * from item_cat where catUUID='"+catUUID+"'";
		return (ItemCat) sqldao.findEntityBySql(sourceId, sql, ItemCat.class, closeConn, null);
	}

	/**
	 * 获取指定店铺信息列表
	 */
	@Override
	public List<BusinessApply> findBusinessApplyListByBusinessUUID(String sourceId, boolean closeConn,String businessUUID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from business_apply where businessUUID in(");
		String[] uuidArr = businessUUID.split(",");
		for (String uuid : uuidArr) {
			sql.append("'"+uuid+"',");
		}
		sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		return (List<BusinessApply>)sqldao.findListBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, null);
	}
	
	@Override
	public BusinessApply findBusinessInfo(String sourceId, boolean closeConn, String businessUUID) {
		String sql = "select * from business_apply where businessUUID='"+businessUUID+"'";
		return (BusinessApply) sqldao.findEntityBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}


	/**
	 * 查询零售商列表
	 */
	@Override
	public List<BusinessApply> findBusinessApplyList(String sourceId, boolean closeConn) {
		String sql = "select * from business_apply where approvalStatus='1' and shopState='1' and applyType='0'";
		return (List<BusinessApply>)sqldao.findListBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}

	/**
	 * 查询店铺信息
	 */
	@Override
	public BusinessApply getBusinessInfoByUUID(String sourceId, boolean closeConn, String businessUUID) {
		String sql = "select * from business_apply where businessUUID='"+businessUUID+"'";
		return (BusinessApply) sqldao.findEntityBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}

	/**
	 * 用户零售店铺信息列表
	 */
	@Override
	public List<BusinessApply> getBusinessInfo(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "select * from business_apply where shopState='1' and approvalStatus='1' and applyType='0' and vipUUID='"+vipUUID+"'";
		return (List<BusinessApply>)sqldao.findListBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}
	
	/**
	 * 用户店铺信息列表
	 */
	@Override
	public List<BusinessApply> getAllBusinessInfo(String sourceId, boolean closeConn) {
		String sql = "select * from business_apply where shopState='1' and approvalStatus='1' and applyType='0'";
		return (List<BusinessApply>)sqldao.findListBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}

	/**
	 * 获取指定批次的二维码图片
	 */
	@Override
	public List<CouponOffline> getQRcodeImg(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "select QRcodeImg from coupon_offline where isGet=1 and batchUUID='"+batchUUID+"'";
		return (List<CouponOffline>)sqldao.findListBySql(sourceId, sql, CouponOffline.class, closeConn, null);
	}

	/**
	 * 获取指定批次可下载的二维码图片
	 */
	@Override
	public List<CouponOffline> getIsDownQRcodeImg(String sourceId, boolean closeConn, String batchUUID,Integer downNumber) {
		String sql = "select QRcodeImg,couponNumber from coupon_offline where couponState=1 and batchUUID='"+batchUUID+"' limit 0,"+downNumber;
		return (List<CouponOffline>)sqldao.findListBySql(sourceId, sql, CouponOffline.class, closeConn, null);
	}

	/**
	 * 保存修改优惠劵模版信息
	 */
	@Override
	public boolean updataCouponInfo(String sourceId, boolean closeConn, CouponOfIssuer issuer) {
		return entitydao.updateEntity(sourceId, issuer, closeConn);
	}

	/**
	 *  删除批量生成的优惠劵
	 */
	@Override
	public boolean delPreCoupon(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "delete from coupon where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 *  删除批量生成的优惠劵（线下）
	 */
	@Override
	public boolean delPreCouponOffline(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "delete from coupon_offline where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 获取指定商品信息列表
	 */
	@Override
	public List<ItemParam> findGoodsInfoList(String sourceId, boolean closeConn, String goodsUUID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select stock,itemImg,title,price,itemUUID from item_param where");
		if(goodsUUID.contains(",")) {//有多个商品
			String[] goodsUUIDArr = goodsUUID.split(",");
			sql.append(" itemUUID in(");
			for (String uuid : goodsUUIDArr) {
				sql.append("'"+uuid+"',");
			}
			sql = sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(")");
		}else {
			sql.append(" itemUUID='"+goodsUUID+"'");
		}
		return (List<ItemParam>)sqldao.findListBySql(sourceId, sql.toString(), ItemParam.class, closeConn, null);
	}


	/**
	 * 获取指定分类的信息列表
	 */
	@Override
	public List<ItemCat> findSortInfoList(String sourceId, boolean closeConn, String catUUID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select catUUID,catCode,catName from item_cat where");
		if(catUUID.contains(",")) {//有多个分类
			String[] catUUIDArr = catUUID.split(",");
			sql.append(" catUUID in(");
			for (String uuid : catUUIDArr) {
				sql.append("'"+uuid+"',");
			}
			sql = sql.deleteCharAt(sql.lastIndexOf(","));
			sql.append(")");
		}else {
			sql.append(" catUUID='"+catUUID+"'");
		}
		return (List<ItemCat>)sqldao.findListBySql(sourceId, sql.toString(), ItemCat.class, closeConn, null);
	}
	

	/**
	 * 发行方批量生成优惠劵(线上)
	 */
	@Override
	public boolean createCoupon(String sourceId, boolean closeConn, Coupon coupon) {
		return entitydao.saveEntity(sourceId, coupon, closeConn);
	}
	/**
	 * 发行方批量生成优惠劵(线下)
	 */
	@Override
	public boolean createCouponOffline(String sourceId, boolean closeConn, CouponOffline couponOffline) {
		return entitydao.saveEntity(sourceId, couponOffline, closeConn);
	}
	
	/**
	 * 发行方生成优惠劵管理模版
	 */
	@Override
	public boolean createCouponMng(String sourceId, boolean closeConn, CouponOfIssuer issuer) {
		return entitydao.saveEntity(sourceId,issuer,closeConn);
	}

	/**
	 * 获取店铺所属商品列表
	 */
	@Override
	public List<Item> getGoodsListByEntityUUID(String sourceId, boolean closeConn, String entityUUID,String catUUID) {
		if(StringUtils.isNullOrEmpty(catUUID)) {//不选择商品分类，则直接获取该店铺的所有商品列表
			String sqls = "select * from item where itemStatus='1' and isDel='0' and entityUUID='"+entityUUID+"'";
			return (List<Item>)sqldao.findListBySql(sourceId, sqls, ItemParam.class, closeConn, null);
		}else {
			String sqls = "select * from item where itemStatus='1' and isDel='0' and entityUUID='"+entityUUID+"' and catUUID='"+catUUID+"'";
			return (List<Item>)sqldao.findListBySql(sourceId, sqls, ItemParam.class, closeConn, null);
		}
	}
	/**
	 * 获取发行商商品分类
	 */
	@Override
	public Map<String,String> getGoodsCatUUIDByBusinessUUID(String sourceId, boolean closeConn, String businessUUID) {
		String sql = "select catUUID from business_apply where businessUUID='"+businessUUID+"'";
		return sqldao.findMapBysql(sourceId, sql, closeConn, null);
	}
	@Override
	public List<ItemCat> getGoodsSortList(String sourceId, boolean closeConn, String parentUUID) {
		String sql = "select catUUID,catCode,catName from item_cat where parentUUID='"+parentUUID+"'";
		return (List<ItemCat>)sqldao.findListBySql(sourceId, sql, ItemCat.class, closeConn, null);
	}
	
	/**
	 *获取优惠劵列表
	 */
	@Override
	public List<CouponOfIssuer> findCouponList(String sourceId, boolean closeConn, Map<String,Object> param,Page page) {
		String userType = (String) param.get("userType");
		String sqls = null;
		if("0".equals(userType)) {//平台管理员进入查询
			sqls = "select * from coupon_manage_issuer where isDel=1 ";
			
		}else {
			sqls = "select * from coupon_manage_issuer where isDel=1 and vipUUID='"+param.get("vipUUID")+"' ";
		}
		
		StringBuilder sql = new StringBuilder(sqls);
		
		if(!"noLimit".equals(param.get("business"))) {
			sql.append(" and merchantUUID='"+param.get("business")+"'");
		}
		//类型
		if(!"noLimit".equals(param.get("type"))) {
			sql.append(" and couponType="+param.get("type"));
		}
		if(!"noLimit".equals(param.get("releaseType"))) {
			sql.append(" and releaseType="+param.get("releaseType"));
		}
		//状态
		if(!"noLimit".equals(param.get("status"))) {
			//获取当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			
			//已过领取时间
			if("releaseEndTime".equals(param.get("status"))) {
				sql.append(" and releaseEndTime<'"+nowTime+"'");
			}
			//已过使用时间
			if("disabledTime".equals(param.get("status"))) {
				sql.append(" and disabledTime<'"+nowTime+"'");
			}
			//上下架
			if("1".equals(param.get("status")) || "2".equals(param.get("status")) || "3".equals(param.get("status"))) {
				sql.append(" and isRelease="+param.get("status"));
			}
		}
		//劵名搜索
		if(param.get("searchWords") != null && !"".equals(param.get("searchWords"))) {
			sql.append(" and couponName like '%"+param.get("searchWords")+"%'");
		}
		//排序条件
		if(!"noLimit".equals(param.get("range"))) {
			sql.append(" order by "+param.get("range")+" desc");
		}
		return (List<CouponOfIssuer>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, CouponOfIssuer.class, page, null);
	}
	
	/**
	 * 平台获取优惠劵列表
	 */
	@Override
	public List<CouponOfIssuer> platformFindCouponList(String sourceId, boolean closeConn, Map param,Page page) {
		StringBuilder sql = new StringBuilder("select * from coupon_manage_issuer where fromType=1 and isDel=1 and vipUUID='"+param.get("vipUUID")+"'");
		//类型
		if(!"noLimit".equals(param.get("type"))) {
			sql.append(" and couponType="+param.get("type"));
		}
		
		if(!"noLimit".equals(param.get("releaseType"))) {
			sql.append(" and releaseType="+param.get("releaseType"));
		}
		
		//状态
		if(!"noLimit".equals(param.get("status"))) {
			//获取当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			//已过领取时间
			if("releaseEndTime".equals(param.get("status"))) {
				sql.append(" and releaseEndTime<'"+nowTime+"'");
			}
			//已过使用时间
			if("disabledTime".equals(param.get("status"))) {
				sql.append(" and disabledTime<'"+nowTime+"'");
			}
			//上下架
			if("1".equals(param.get("status")) || "2".equals(param.get("status")) || "3".equals(param.get("status"))) {
				sql.append(" and isRelease="+param.get("status"));
			}
		}
		//劵名搜索
		if(param.get("searchWords") != null && !"".equals(param.get("searchWords"))) {
			sql.append(" and couponName like '%"+param.get("searchWords")+"%'");
		}
		//排序条件
		if(!"noLimit".equals(param.get("range"))) {
			sql.append(" order by "+param.get("range")+" desc");
		}
		return (List<CouponOfIssuer>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, CouponOfIssuer.class, page, null);
	}
	
	/**
	 * 删除指定批次优惠劵
	 */
	@Override
	public boolean deleteCouponByBatchUUID(String sourceId, boolean closeConn, String batchUUID) {
		//只能删除未上架的优惠劵
		String sql = "update coupon_manage_issuer set isDel=2 where isRelease=2 and batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 个人用户删除优惠劵
	 */
	@Override
	public boolean deleteCouponByCouponUUID(String sourceId, boolean closeConn, String[] couponUUID) {
		StringBuilder sql = new StringBuilder("update coupon_manage_user set isDel=2 where couponUUID in(");
		for (String uuid : couponUUID) {
			sql.append("'"+uuid+"',");
		}
		sql = sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		//System.out.println("sql:"+sql);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, null);
	}
	
	/**
	 * 获取优惠劵的相关信息
	 */
	@Override
	public CouponOfIssuer findCouponInfoByBatchUUID(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "select * from coupon_manage_issuer where batchUUID='"+batchUUID+"'";
		return (CouponOfIssuer)sqldao.findEntityBySql(sourceId, sql, CouponOfIssuer.class, closeConn, null);
	}
	
	/**
	 * 上架优惠劵
	 */
	@Override
	public boolean putawayCoupon(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "update coupon_manage_issuer set isRelease=1 where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 下架优惠劵
	 */
	@Override
	public boolean downCoupon(String sourceId, boolean closeConn, String batchUUID) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		String sql = "update coupon_manage_issuer set isRelease=3,downTime='"+nowTime+"' where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 获取用户领取的优惠劵列表
	 */
	@Override
	public List<CouponUserList> findUserCouponList(String sourceId, boolean closeConn, Map param, Page page) {
		String sqls = "select us.batchUUID,us.couponUUID,us.isUse,us.getTime,us.useTime,iss.couponName,iss.couponType,iss.money,iss.discount,iss.effectMoney,iss.disabledTime"
				+ " from coupon_manage_user us join coupon_manage_issuer iss"
				+ " on us.batchUUID=iss.batchUUID where us.isDel=1 and us.vipUUID='"+param.get("vipUUID")+"'";
		StringBuilder sql = new StringBuilder();
		sql.append(sqls);
		//类型
		if(!"noLimit".equals(param.get("type"))) {
			sql.append(" and iss.couponType="+param.get("type"));
		}
		
		//状态
		if(!"noLimit".equals(param.get("status"))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTime = sdf.format(new Date());
			if("3".equals(param.get("status"))) {//已过期
				sql.append(" and iss.disabledTime<'"+nowTime+"'");
			}else {
				sql.append(" and us.isUse="+param.get("status"));
			}
		}
		
		//System.out.println("words:"+param.get("searchWords")+";");
		//劵名搜索
		if(param.get("searchWords") != null && !"".equals(param.get("searchWords"))) {
			sql.append(" and iss.couponName like '%"+param.get("searchWords")+"%'");
		}
		
		//排序条件
		if("money".equals(param.get("range")) || "discount".equals(param.get("range"))) {
			sql.append(" order by iss."+param.get("range")+" desc");
		}
		if("getTime".equals(param.get("range")) || "useTime".equals(param.get("range"))) {
			sql.append(" order by us."+param.get("range")+" desc");
		}
		//System.out.println("sql:"+sql);
		return (List<CouponUserList>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, CouponUserList.class, page, null);
	}
	
	/**
	 * 获取用户领取指定优惠劵的数量
	 */
	@Override
	public Map getNumsCouponOfUser(String sourceId, boolean closeConn, String vipUUID,String batchUUID) {
		String sql = "select count(*) numbers from coupon_manage_user where vipUUID='"+vipUUID+"' and batchUUID='"+batchUUID+"'";
		return (Map)sqldao.findMapBysql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 用户领取优惠劵数据写入
	 */
	@Override
	public boolean allotCouponToUser(String sourceId, boolean closeConn, CouponOfUser user) {
		return entitydao.saveEntity(sourceId, user, closeConn);
	}
	
	/**
	 * 获取未领取优惠劵的couponUUID
	 */
	@Override
	public List<Coupon> getCouponUUIDByBatchUUID(String sourceId, boolean closeConn, String batchUUID) {
		String sql = "select * from coupon where isGet=1 and batchUUID='"+batchUUID+"'";
		return (List<Coupon>)sqldao.findListBySql(sourceId, sql, Coupon.class, closeConn, null);
	}
	
	/**
	 * 修改优惠劵为已领取
	 */
	@Override
	public boolean upCouponIsGet(String sourceId, boolean closeConn, String couponUUID) {
		String sql = "update coupon set isGet=2 where couponUUID='"+couponUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	@Override
	public boolean upCouponIsNoGet(String sourceId, boolean closeConn, String couponUUID) {
		String sql = "update coupon set isGet=1 where couponUUID='"+couponUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 删除写入成功的优惠劵数据
	 */
	@Override
	public boolean delChangeData(String sourceId, boolean closeConn, String couponUUID) {
		String sql = "delete from coupon_manage_user where couponUUID='"+couponUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	
	
	
	
	/**
	 * 修改优惠劵为已使用，设置使用时间，订单uuid
	 */
	@Override
	public boolean updateIsUse(String sourceId, boolean closeConn, String couponUUID,String orderUUID) {
		//1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		String sql = "update coupon_manage_user set isUse=2,orderUUID='"+orderUUID+"',useTime='"+nowTime+"' where couponUUID in(";
		StringBuilder sqls = new StringBuilder(sql);
		String[] uuidArr = couponUUID.split(",");
		for (String uuid : uuidArr) {
			sqls.append("'"+uuid+"',");
		}
		sqls.deleteCharAt(sqls.lastIndexOf(","));
		sqls.append(")");
		return sqldao.executeSql(sourceId, sqls.toString(), closeConn, null);
	}
	
	/**
	 * 优惠劵未使用
	 */
	@Override
	public boolean updateIsNotUse(String sourceId, boolean closeConn, String[] couponUUID) {
		StringBuilder sql = new StringBuilder("update coupon_manage_user set isUse=1 where couponUUID in(");
		for (String uuid : couponUUID) {
			sql.append("'"+uuid+"',");
		}
		sql = sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		//System.out.println("sql:"+sql);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, null);
	}
	
	/**
	 * 获取优惠劵面值/折扣，优惠劵类型
	 */
	@Override
	public Map<String,Object> getSomeInfoCoupon(String sourceId, boolean closeConn, String couponUUID) {
		String sqls = "select iss.goodsUseCondition,iss.goodsUUID,iss.goodsSortCondition,iss.goodsCatUUID,iss.reduceMaxPrice,iss.couponType,iss.money,iss.discount"
				+ " from coupon cs join coupon_manage_issuer iss"
				+ " on cs.batchUUID=iss.batchUUID where cs.couponUUID='"+couponUUID+"'";
		return sqldao.findMapBysql(sourceId, sqls, closeConn, null);
	}
	
	/**
	 * 获取商品信息
	 */
	@Override
	public ItemParam getGoodInfo(String sourceId, boolean closeConn, String goodUUID,String paramUUID) {
		String sql = "select * from item_param where itemUUID='"+goodUUID+"' and paramUUID='"+paramUUID+"'";
		return (ItemParam) sqldao.findEntityBySql(sourceId, sql, ItemParam.class, closeConn, null);
	}
	
	/**
	 * 获取商品信息
	 */
	@Override
	public Item getGoodInfo(String sourceId, boolean closeConn, String goodUUID) {
		String sql = "select * from item_param where itemUUID='"+goodUUID+"'";
		return (Item) sqldao.findEntityBySql(sourceId, sql, Item.class, closeConn, null);
	}
	
	/**
	 * 获取店铺可领优惠劵列表
	 */
	@Override
	public List<CouponUserList> findCouponIsUsed(String sourceId, boolean closeConn, String merchantUUID) {
		//1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		//2.可领取的条件：已上架isRelease=1，线上releaseType=1;库存大于0-stock>0，在领取时间内
		String sqls = "select businessName,batchUUID,effectMoney,couponType,money,discount,useStartTime,disabledTime,fromType"
				+ " from coupon_manage_issuer"
				+ " where fromType=2 and isRelease=1 and releaseType=1 and stock>0 and releaseStartTime<'"+nowTime+ "' and releaseEndTime>'"+nowTime+"' and merchantUUID='"+merchantUUID+"'"
				+ " order by money,discount";
		return (List<CouponUserList>)sqldao.findListBySql(sourceId, sqls, CouponUserList.class, closeConn, null);
	}
	
	/**
	 * 获取平台可领优惠劵列表
	 */
	@Override
	public List<CouponUserList> platformFindCouponIsUsed(String sourceId, boolean closeConn, String merchantUUID) {
		//1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		//2.可领取的条件：已上架isRelease=1，线上releaseType=1;库存大于0-stock>0，在领取时间内
		String sqls = "select batchUUID,effectMoney,couponType,money,discount,useStartTime,disabledTime,fromType"
				+ " from coupon_manage_issuer"
				+ " where fromType=1 and isRelease=1 and releaseType=1 and stock>0 and releaseStartTime<'"+nowTime+ "' and releaseEndTime>'"+nowTime
				+ "' order by money,discount";
		return (List<CouponUserList>)sqldao.findListBySql(sourceId, sqls, CouponUserList.class, closeConn, null);
	}
	
	/**
	 *  查询优惠劵的发行量，领取量，使用量，库存
	 */
	@Override
	public CouponOfIssuer findCouponInfo(String sourceId, boolean closeConn, String batchUUID) {
		String sqls = "select releaseNumbers,getNumbers,usedNumbers,stock"
				+ " from coupon_manage_issuer where batchUUID='"+batchUUID+"'";
		return (CouponOfIssuer)sqldao.findEntityBySql(sourceId, sqls, CouponOfIssuer.class, closeConn, null);
	}
	
	/**
	 *  修改优惠劵的领取量，库存
	 */
	@Override
	public boolean updateCouponGetNumsAndStock(String sourceId, boolean closeConn, String batchUUID,Integer stock,Integer getNumbers) {
		String sql = "update coupon_manage_issuer set stock="+stock+",getNumbers="+getNumbers+" where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 * 修改优惠劵的使用量
	 */
	@Override
	public boolean updateCouponUsedNumbers(String sourceId, boolean closeConn, String batchUUID, Integer usedNumbers) {
		String sql = "update coupon_manage_issuer set usedNumbers="+usedNumbers+" where batchUUID='"+batchUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	
	/**
	 * 手机端获取用户可使用的优惠劵列表
	 */
	@Override
	public List<CouponUserList> findUserCouponListMobile(String sourceId, boolean closeConn, String vipUUID,Page page) {
		//1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		
		//2.已过期，已使用的不获取
		String sqls = "select iss.*,us.couponUUID"
				+ " from coupon_manage_user us join coupon_manage_issuer iss"
				+ " on us.batchUUID=iss.batchUUID"
				+ " where us.isUse=1 and iss.useStartTime<='"+nowTime+"' and iss.disabledTime>'"+nowTime
				+ "' and us.vipUUID='"+vipUUID+"' order by iss.disabledTime";
		return (List<CouponUserList>)sqldao.findPageByMysql(sourceId, sqls, closeConn, CouponUserList.class, page, null);
	}
	
	/**
	 *获取用户领取的对应店铺的优惠劵列表 
	 */
	@Override
	public List<CouponUserList> findUserEntityCouponList(String sourceId, boolean closeConn, String vipUUID,String entityUUID,String itemUUIDArr) {
		String[] arr = itemUUIDArr.split(",");
		String sqls = "select distinct ba.businessName,ba.businessUUID,us.couponUUID,iss.goodsUUID,iss.batchUUID,iss.effectMoney,iss.couponType,iss.money,iss.discount,iss.useStartTime,iss.disabledTime"
				+ " from coupon_manage_user us join coupon_manage_issuer iss"
				+ " on us.batchUUID=iss.batchUUID"
				+ " join business_apply ba"
				+ " on ba.businessUUID=iss.merchantUUID"
				+ " where us.isUse=1 and us.vipUUID='"+vipUUID+"' and iss.merchantUUID='"+entityUUID+"' and";
		StringBuilder sql = new StringBuilder(sqls);
		for (String uuid : arr) {
			sql.append(" goodsUUID like '%"+uuid+"%' or");
		}
		sql = sql.deleteCharAt(sql.lastIndexOf("o"));
		sql = sql.deleteCharAt(sql.lastIndexOf("r"));
		
		System.out.println("sql:"+sql);
		return (List<CouponUserList>)sqldao.findListBySql(sourceId, sql.toString(), CouponUserList.class, closeConn, null);
	}
	
	/**
	 * 订单提交获取用户领取的所有的可用的店铺优惠劵
	 */
	@Override
	public List<CouponUserList> findUserIsUsedCouponList(String sourceId, boolean closeConn, String vipUUID,List<String> entityUUIDArr) {
		// 1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		String sqls = "select us.couponUUID,iss.*"
				+ "	from coupon_manage_user us join coupon_manage_issuer iss"
				+ " on us.batchUUID=iss.batchUUID "
				+ " where iss.fromType=2 and us.isUse=1 and us.vipUUID='"+vipUUID+"' and iss.useStartTime<='"+nowTime+"' and iss.disabledTime>'"+nowTime+"'"
				+ " and iss.merchantUUID in(";
		StringBuilder sql = new StringBuilder(sqls);
		for (String uuid : entityUUIDArr) {
			sql.append("'"+uuid+"',");
		}
		sql = sql.deleteCharAt(sql.lastIndexOf(","));
		sql.append(")");
		return (List<CouponUserList>)sqldao.findListBySql(sourceId, sql.toString(), CouponUserList.class, closeConn, null);
	}
	
	/**
	 * 获取用户领取的平台的可用的优惠劵列表
	 */
	@Override
	public List<CouponUserList> findUserIsUsedPlatformCouponList(String sourceId, boolean closeConn, String vipUUID) {
		// 1.获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = sdf.format(new Date());
		
		String sql = "select distinct iss.*,us.couponUUID"
				+ " from coupon_manage_user us join coupon_manage_issuer iss"
				+ " on us.batchUUID=iss.batchUUID"
				+ " where iss.fromType=1 and us.isUse=1 and us.vipUUID='"+vipUUID+"' and iss.useStartTime<='"+nowTime+"' and iss.disabledTime>'"+nowTime+"'";
		return (List<CouponUserList>)sqldao.findListBySql(sourceId, sql, CouponUserList.class, closeConn, null);
	}

	
	
	@Override
	public List<CouponUserList> getOnLineCouponList(String sourceId, boolean closeConn, String batchUUID,Page page) {
		String sql = "select c.couponNumber,c.isGet,cmu.getTime,cmu.isUse,cmu.useTime,cmu.orderUUID,cmu.vipName"
		+" from coupon c left join coupon_manage_user cmu"
		+" on c.couponUUID=cmu.couponUUID "
		+" where c.batchUUID='"+batchUUID+"'";
		return (List<CouponUserList>)sqldao.findPageByMysql(sourceId, sql, closeConn, CouponUserList.class, page, null);
	}

	@Override
	public List<CouponUserList> getOffLineCouponList(String sourceId, boolean closeConn, String batchUUID,Page page) {
		String sql = "select c.couponNumber,c.isGet,cmu.getTime,cmu.isUse,cmu.useTime,cmu.orderUUID,cmu.vipName"
				+" from coupon_offline c left join coupon_manage_user cmu"
				+" on c.couponUUID=cmu.couponUUID "
				+" where c.batchUUID='"+batchUUID+"'";
		return (List<CouponUserList>)sqldao.findPageByMysql(sourceId, sql, closeConn, CouponUserList.class, page, null);
	}

	/**
	 * 获取优惠劵信息
	 */
	@Override
	public CouponOfIssuer findCouponInfoByCouponUUID(String sourceId, boolean closeConn, String couponUUID) {
		String sql= "select cmi.*"
				+ " from coupon c join coupon_manage_issuer cmi"
				+ " on c.batchUUID=cmi.batchUUID"
				+ " where c.couponUUID='"+couponUUID+"'";
		return (CouponOfIssuer) sqldao.findEntityBySql(sourceId, sql, CouponOfIssuer.class, closeConn, null);
	}

	/**
	 * 订单取消或者超时，返还休会劵给用户
	 */
	@Override
	public boolean returnCouponToUser(String sourceId, boolean closeConn, String couponUUID) {
		String sql = "update coupon_manage_user set isUse=1 where couponUUID='"+couponUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
