package com.xryb.zhtc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.xryb.zhtc.entity.ApplyMsg;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.BusinessDistribution;
import com.xryb.zhtc.entity.BusinessGrade;
import com.xryb.zhtc.entity.BusinessMiddle;
import com.xryb.zhtc.entity.Collection;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.OfflineDetailed;
import com.xryb.zhtc.entity.OfflineOrders;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.SettledMoney;
import com.xryb.zhtc.entity.SlideImg;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.entity.WholesaleMarket;
import com.xryb.zhtc.service.IBusinessService;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
import spark.annotation.Auto;

public class BusinessServiceImpl implements IBusinessService  {
	/**
	 *  注入dao
	 */
	@Auto(name=EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name=SqlDao.class)
	private SqlDao sqldao;

	@Override
	public boolean businessSave(String sourceId, boolean closeConn, BusinessApply businessApply) {
		if(businessApply == null) {
			return false;
		}
		if(businessApply.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, businessApply, false);
		}else {
			return entitydao.updateEntity(sourceId, businessApply, closeConn);
		}
	}

	@Override
	public BusinessApply apply(String sourceId, boolean closeConn, String vipUUID, String applyType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from business_apply where vipUUID = ? and applyType = ?");
		List<Object> params = new ArrayList<>();
		params.add(vipUUID);
		params.add(applyType);
		return (BusinessApply) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, params);
	}

	@Override
	public List<BusinessApply> businessAppliesList(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_apply where 1=1");
		if(findMap != null) {
			if(findMap.get("approvalStatus") != null && !"".equals(findMap.get("approvalStatus")) && !"null".equals(findMap.get("approvalStatus"))) {
				//审批状态(0:待审批，1：审批通过，2：审批不通过)
				sql.append(" and approvalStatus = ?");
				params.add(findMap.get("approvalStatus"));
			}
			if(findMap.get("shopState") != null && !"".equals(findMap.get("shopState")) && !"null".equals(findMap.get("shopState"))) {
				//店铺状态(0：停用，1：启用)
				sql.append(" and shopState = ?");
				params.add(findMap.get("shopState"));
			}
			if(findMap.get("vipUUID") != null && !"".equals(findMap.get("vipUUID")) && !"null".equals(findMap.get("vipUUID"))) {
				//申请人UUID
				sql.append(" and vipUUID = ?");
				params.add(findMap.get("vipUUID"));
			}
			if(findMap.get("applyType") != null && !"".equals(findMap.get("applyType")) && !"null".equals(findMap.get("applyType"))) {
				//申请类型(0：零售商，1：供应商)
				sql.append(" and applyType = ?");
				params.add(findMap.get("applyType"));
			}
			if(findMap.get("pfUUID") != null && !"".equals(findMap.get("pfUUID")) && !"null".equals(findMap.get("pfUUID"))) {
				//批发市场UUID
				sql.append(" and pfUUID = ?");
				params.add(findMap.get("pfUUID"));
			}
			if(findMap.get("condition") != null && !"".equals(findMap.get("condition")) && !"null".equals(findMap.get("condition"))) {
				//条件查询
				sql.append(" and businessName like '%").append(findMap.get("condition").replace("'","")).append("%'");
				sql.append(" or businessPhone = ?");
				params.add(findMap.get("condition"));
			}
		}
		sql.append(" order by applyTime desc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, BusinessApply.class, page, params);
		}
	}

	@Override
	public BusinessApply applyId(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_apply where businessUUID = ?");
		params.add(businessUUID);
		return (BusinessApply) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, params);
	}

	@Override
	public boolean shopStateSave(String sourceId, boolean closeConn, String businessUUID, String shopState) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("update business_apply set shopState = "+shopState+" where businessUUID = ?");
		params.add(businessUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public boolean settledSave(String sourceId, boolean closeConn, SettledMoney settledMoney) {
		if(settledMoney == null) {
			return false;
		}
		if(settledMoney.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, settledMoney, false);
		}else {
			return entitydao.updateEntity(sourceId, settledMoney, closeConn);
		}
	}

	@Override
	public List<SettledMoney> settledAll(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from settled_money where 1=1");
		if(findMap != null) {
			
		}
		sql.append(" order by time asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), SettledMoney.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, SettledMoney.class, page, params);
		}
	}

	@Override
	public SettledMoney settledId(String sourceId, boolean closeConn, String settledUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from settled_money where settledUUID = ?");
		params.add(settledUUID);
		return (SettledMoney) sqldao.findEntityBySql(sourceId, sql.toString(), SettledMoney.class, closeConn, params);
	}

	@Override
	public boolean settledDel(String sourceId, boolean closeConn, String settledUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from settled_money where settledUUID = ?");
		params.add(settledUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<Order> orderAll(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("SELECT * from vip_order where 1=1");
		if(findMap != null) {
			if(findMap.get("entityUUID") != null && !"".equals(findMap.get("entityUUID")) && !"null".equals(findMap.get("entityUUID"))) {
				//店铺UUID
				sql.append(" and entityUUID = ?");
				params.add(findMap.get("entityUUID"));
			}
			if(findMap.get("orderStatus") != null && !"".equals(findMap.get("orderStatus")) && !"null".equals(findMap.get("orderStatus"))) {
				//订单状态：0未付款，1已付款，未发货，2已发货，未签收，3已签收，未评价，交易完成，4已评价
				sql.append(" and orderStatus = ?");
				params.add(findMap.get("orderStatus"));
			}
			if(findMap.get("receiverMobile") != null && !"".equals(findMap.get("receiverMobile")) && !"null".equals(findMap.get("receiverMobile"))) {
				//收货人手机号码
				sql.append(" and receiverMobile = ?");
				params.add(findMap.get("receiverMobile"));
			}
			if(findMap.get("createTime") != null && !"".equals(findMap.get("createTime")) && !"null".equals(findMap.get("createTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("createTime"));
			}
			if(findMap.get("closeTime") != null && !"".equals(findMap.get("closeTime")) && !"null".equals(findMap.get("closeTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(closeTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("closeTime"));
			}
			if(findMap.get("receiverName") != null && !"".equals(findMap.get("receiverName")) && !"null".equals(findMap.get("receiverName"))) {
				//收货人姓名
				sql.append(" and receiverName like '%").append(findMap.get("receiverName").replace("'","")).append("%'");
			}
			if(findMap.get("condition") != null && !"".equals(findMap.get("condition")) && !"null".equals(findMap.get("condition"))) {
				//条件查询
				sql.append(" and payerName like '%").append(findMap.get("condition").replace("'","")).append("%'");
				sql.append(" or receiverMobile = ?");
				params.add(findMap.get("condition"));
				sql.append(" or orderUUID = ?");
				params.add(findMap.get("condition"));
			}
		}
		sql.append(" order by createTime asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), Order.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, Order.class, page, params);
		}
	}

	@Override
	public Map totalPriceSum(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("SELECT count(totalPrice) AS totalPrice FROM vip_order where orderStatus IN(1,2,3,4)");
		if(findMap != null) {
			if(findMap.get("entityUUID") != null && !"".equals(findMap.get("entityUUID")) && !"null".equals(findMap.get("entityUUID"))) {
				//商家UUID
				sql.append(" and entityUUID = ?");
				params.add(findMap.get("entityUUID"));
			}
			if(findMap.get("createTime") != null && !"".equals(findMap.get("createTime")) && !"null".equals(findMap.get("createTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("createTime"));
			}
			if(findMap.get("closeTime") != null && !"".equals(findMap.get("closeTime")) && !"null".equals(findMap.get("closeTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(closeTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("closeTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map orderNumber(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("SELECT count(totalCount) AS count FROM vip_order where orderStatus IN(1,2,3,4)");
		if(findMap != null) {
			if(findMap.get("entityUUID") != null && !"".equals(findMap.get("entityUUID")) && !"null".equals(findMap.get("entityUUID"))) {
				//商家UUID
				sql.append(" and entityUUID = ?");
				params.add(findMap.get("entityUUID"));
			}
			if(findMap.get("createTime") != null && !"".equals(findMap.get("createTime")) && !"null".equals(findMap.get("createTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("createTime"));
			}
			if(findMap.get("closeTime") != null && !"".equals(findMap.get("closeTime")) && !"null".equals(findMap.get("closeTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(closeTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("closeTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map placeNumber(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("SELECT count(*) AS rs FROM vip_order where orderStatus IN(1,2,3,4)");
		if(findMap != null) {
			if(findMap.get("entityUUID") != null && !"".equals(findMap.get("entityUUID")) && !"null".equals(findMap.get("entityUUID"))) {
				//商家UUID
				sql.append(" and entityUUID = ?");
				params.add(findMap.get("entityUUID"));
			}
			if(findMap.get("createTime") != null && !"".equals(findMap.get("createTime")) && !"null".equals(findMap.get("createTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("createTime"));
			}
			if(findMap.get("closeTime") != null && !"".equals(findMap.get("closeTime")) && !"null".equals(findMap.get("closeTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(closeTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("closeTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public BusinessDistribution distributionId(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_distribution where businessUUID = ?");
		params.add(businessUUID);
		return (BusinessDistribution) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessDistribution.class, closeConn, params);
	}

	@Override
	public boolean distributionSave(String sourceId, boolean closeConn, BusinessDistribution businessDistribution) {
		if(businessDistribution == null) {
			return false;
		}
		if(businessDistribution.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, businessDistribution, false);
		}else {
			return entitydao.updateEntity(sourceId, businessDistribution, closeConn);
		}
	}

	@Override
	public BusinessDistribution distributionUUID(String sourceId, boolean closeConn, String distributionUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_distribution where distributionUUID = ?");
		params.add(distributionUUID);
		return (BusinessDistribution) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessDistribution.class, closeConn, params);
	}

	@Override
	public List<Map<String, Object>> orderAllApp(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from vip_order as a LEFT JOIN order_item as b ON a.orderUUID = b.orderUUID where 1=1 and a.isDel = 0");
		if(findMap != null) {
			if(findMap.get("entityUUID") != null && !"".equals(findMap.get("entityUUID")) && !"null".equals(findMap.get("entityUUID"))) {
				//店铺UUID
				sql.append(" and a.entityUUID = ?");
				params.add(findMap.get("entityUUID"));
			}
			if(findMap.get("condition") != null && !"".equals(findMap.get("condition")) && !"null".equals(findMap.get("condition"))) {
				//条件查询
				sql.append(" and a.orderUUID = ?");
				params.add(findMap.get("condition"));
				sql.append(" or a.receiverName like '%").append(findMap.get("condition").replace("'","")).append("%'");
				sql.append(" or a.receiverMobile = ?");
				params.add(findMap.get("condition"));
			}
			if(findMap.get("orderStatus") != null && !"".equals(findMap.get("orderStatus")) && !"null".equals(findMap.get("orderStatus"))) {
				//未评价
				sql.append(" and a.orderStatus = ?");
				params.add(findMap.get("orderStatus"));
			}
		}
		sql.append(" order by a.createTime asc ");
		if(page==null) {
			return sqldao.findMapListBysql(sourceId, sql.toString(), closeConn, params);
		}else {
			return sqldao.findMapListBysql(sourceId, sql.toString(), closeConn, params);
		}
	}

	@Override
	public Map moneyDay(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) AS moneyDay from vip_order where to_days(createTime) = to_days(now()) and entityUUID = ? AND orderStatus IN(1,2,3,4)");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map orderDay(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) AS orderDay from vip_order where to_days(createTime) = to_days(now()) and entityUUID = ? AND orderStatus IN(1,2,3,4)");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<WholesaleMarket> wholesaleMarketsAll(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from wholesale_market where 1=1");
		if(findMap != null) {
			if(findMap.get("pfName") != null && !"".equals(findMap.get("pfName")) && !"null".equals(findMap.get("pfName"))) {
				//批发市场名称
				sql.append(" and pfName like '%").append(findMap.get("pfName").replace("'","")).append("%'");
			}
			if(findMap.get("time") != null && !"".equals(findMap.get("time")) && !"null".equals(findMap.get("time"))) {
				//时间日期
				sql.append(" and time = ?");
				params.add(findMap.get("time"));
			}
		}
		sql.append(" order by time asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), WholesaleMarket.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, WholesaleMarket.class, page, params);
		}
	}

	@Override
	public boolean pfSave(String sourceId, boolean closeConn, WholesaleMarket wholesaleMarket) {
		if(wholesaleMarket == null) {
			return false;
		}
		if(wholesaleMarket.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, wholesaleMarket, false);
		}else {
			return entitydao.updateEntity(sourceId, wholesaleMarket, closeConn);
		}
	}

	@Override
	public WholesaleMarket wholesaleMarketId(String sourceId, boolean closeConn, String pfUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from wholesale_market where pfUUID = ?");
		params.add(pfUUID);
		return (WholesaleMarket) sqldao.findEntityBySql(sourceId, sql.toString(), WholesaleMarket.class, closeConn, params);
	}

	@Override
	public boolean collectionSave(String sourceId, boolean closeConn, Collection collection) {
		if(collection == null) {
			return false;
		}
		if(collection.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, collection, false);
		}else {
			return entitydao.updateEntity(sourceId, collection, closeConn);
		}
	}

	@Override
	public Collection collection(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from collection where 1=1");
		if(findMap != null) {
			if(findMap.get("collectionUUID") != null && !"".equals(findMap.get("collectionUUID")) && !"null".equals(findMap.get("collectionUUID"))) {
				//UUID
				sql.append(" and collectionUUID = ?");
				params.add(findMap.get("collectionUUID"));
			}
			if(findMap.get("externalUUID") != null && !"".equals(findMap.get("externalUUID")) && !"null".equals(findMap.get("externalUUID"))) {
				//外部UUID（如店铺UUID）
				sql.append(" and externalUUID = ?");
				params.add(findMap.get("externalUUID"));
			}
			if(findMap.get("vipUUID") != null && !"".equals(findMap.get("vipUUID")) && !"null".equals(findMap.get("vipUUID"))) {
				//收藏人UUID
				sql.append(" and vipUUID = ?");
				params.add(findMap.get("vipUUID"));
			}
			if(findMap.get("commodityUUID") != null && !"".equals(findMap.get("commodityUUID")) && !"null".equals(findMap.get("commodityUUID"))) {
				//商品UUID
				sql.append(" and commodityUUID = ?");
				params.add(findMap.get("commodityUUID"));
			}
		}
		return (Collection) sqldao.findEntityBySql(sourceId, sql.toString(), Collection.class, closeConn, params);
	}

	@Override
	public List<Collection> collectionAll(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from collection where 1=1");
		if(findMap != null) {
			if(findMap.get("type") != null && !"".equals(findMap.get("type")) && !"null".equals(findMap.get("type"))) {
				//收藏类型（0：商品，1：商家）
				sql.append(" and type = ?");
				params.add(findMap.get("type"));
			}
			if(findMap.get("vipUUID") != null && !"".equals(findMap.get("vipUUID")) && !"null".equals(findMap.get("vipUUID"))) {
				//收藏人UUID
				sql.append(" and vipUUID = ?");
				params.add(findMap.get("vipUUID"));
			}
		}
		sql.append(" order by time asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), Collection.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, Collection.class, page, params);
		}
	}

	@Override
	public boolean collectionDel(String sourceId, boolean closeConn, String collectionUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from collection where collectionUUID = ?");
		params.add(collectionUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public boolean msgSave(String sourceId, boolean closeConn, ApplyMsg applyMsg) {
		if(applyMsg == null) {
			return false;
		}
		if(applyMsg.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, applyMsg, false);
		}else {
			return entitydao.updateEntity(sourceId, applyMsg, closeConn);
		}
	}

	@Override
	public boolean msgDel(String sourceId, boolean closeConn, String msgUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from apply_msg where msgUUID = ?");
		params.add(msgUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public ApplyMsg dateUUIDAll(String sourceId, boolean closeConn, String dateUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from apply_msg where dateUUID = ?");
		params.add(dateUUID);
		return (ApplyMsg) sqldao.findEntityBySql(sourceId, sql.toString(), ApplyMsg.class, closeConn, params);
	}

	@Override
	public List<ApplyMsg> applyMsgsAll(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from apply_msg where 1=1");
		if(findMap != null) {
			if(findMap.get("vipUUID") != null && !"".equals(findMap.get("vipUUID")) && !"null".equals(findMap.get("vipUUID"))) {
				//申请人UUID
				sql.append(" and vipUUID = ?");
				params.add(findMap.get("vipUUID"));
			}
		}
		sql.append(" order by time asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), ApplyMsg.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, ApplyMsg.class, page, params);
		}
	}

	@Override
	public boolean msgReadUp(String sourceId, boolean closeConn, String msgRead, String msgUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("update apply_msg set msgRead = "+msgRead+" where msgUUID = ?");
		params.add(msgUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public ApplyMsg applyMsgId(String sourceId, boolean closeConn, String msgUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from apply_msg where msgUUID = ?");
		params.add(msgUUID);
		return (ApplyMsg) sqldao.findEntityBySql(sourceId, sql.toString(), ApplyMsg.class, closeConn, params);
	}

	@Override
	public boolean gradeSave(String sourceId, boolean closeConn, BusinessGrade businessGrade) {
		if(businessGrade == null) {
			return false;
		}
		if(businessGrade.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, businessGrade, false);
		}else {
			return entitydao.updateEntity(sourceId, businessGrade, closeConn);
		}
	}

	@Override
	public boolean gradeDel(String sourceId, boolean closeConn, String gradeUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from business_grade where gradeUUID = ?");
		params.add(gradeUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<BusinessGrade> businessGradeAll(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_grade where 1=1");
		if(findMap != null) {
			
		}
		sql.append(" order by number asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), BusinessGrade.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, BusinessGrade.class, page, params);
		}
	}

	@Override
	public BusinessGrade businessGradeId(String sourceId, boolean closeConn, String gradeUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_grade where gradeUUID = ?");
		params.add(gradeUUID);
		return (BusinessGrade) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessGrade.class, closeConn, params);
	}

	@Override
	public List<BusinessApply> retailerAll(String sourceId, boolean closeConn, Page page, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select a.* from business_apply as a LEFT JOIN business_middle as b ON a.businessUUID = b.retailerUUID where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家名称UUID
				sql.append(" and b.supplierUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("businessName") != null && !"".equals(findMap.get("businessName")) && !"null".equals(findMap.get("businessName"))) {
				//商家名称
				sql.append(" and a.businessName like '%").append(findMap.get("businessName").replace("'","")).append("%'");
			}
			if(findMap.get("gradeUUID") != null && !"".equals(findMap.get("gradeUUID")) && !"null".equals(findMap.get("gradeUUID"))) {
				//等级分类查询
				sql.append(" and b.gradeUUID = ?");
				params.add(findMap.get("gradeUUID"));
			}
		}
		sql.append(" order by b.time asc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, BusinessApply.class, page, params);
		}
	}

	@Override
	public List<BusinessApply> retailerChoiceAll(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from business_apply where businessUUID NOT IN(select a.businessUUID AS businessUUID from business_apply AS a LEFT JOIN business_middle AS b ON a.businessUUID = b.retailerUUID where 1 = 1 and a.applyType = 0 and a.approvalStatus = 1 and b.supplierUUID = '"+findMap.get("businessUUID")+"') and applyType = 0");
		if(findMap != null) {
			if(findMap.get("businessName") != null && !"".equals(findMap.get("businessName")) && !"null".equals(findMap.get("businessName"))) {
				//商家名称
				sql.append(" and businessName like '%").append(findMap.get("businessName").replace("'","")).append("%'");
			}
		}
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), BusinessApply.class, closeConn, null);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, BusinessApply.class, page, null);
		}
	}

	@Override
	public boolean middleSave(String sourceId, boolean closeConn, BusinessMiddle businessMiddle) {
		if(businessMiddle == null) {
			return false;
		}
		if(businessMiddle.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, businessMiddle, false);
		}else {
			return entitydao.updateEntity(sourceId, businessMiddle, closeConn);
		}
	}

	@Override
	public BusinessMiddle businessMiddle(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_middle where 1=1");
		if(findMap != null) {
			if(findMap.get("middleUUID") != null && !"".equals(findMap.get("middleUUID")) && !"null".equals(findMap.get("middleUUID"))) {
				//UUID
				sql.append(" and middleUUID = ?");
				params.add(findMap.get("middleUUID"));
			}
			if(findMap.get("supplierUUID") != null && !"".equals(findMap.get("supplierUUID")) && !"null".equals(findMap.get("supplierUUID"))) {
				//供应商UUID
				sql.append(" and supplierUUID = ?");
				params.add(findMap.get("supplierUUID"));
			}
			if(findMap.get("retailerUUID") != null && !"".equals(findMap.get("retailerUUID")) && !"null".equals(findMap.get("retailerUUID"))) {
				//零售商UUID
				sql.append(" and retailerUUID = ?");
				params.add(findMap.get("retailerUUID"));
			}
			if(findMap.get("gradeUUID") != null && !"".equals(findMap.get("gradeUUID")) && !"null".equals(findMap.get("gradeUUID"))) {
				//等级UUID
				sql.append(" and gradeUUID = ?");
				params.add(findMap.get("gradeUUID"));
			}
		}
		return (BusinessMiddle) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessMiddle.class, closeConn, params);
	}

	@Override
	public boolean middleDel(String sourceId, boolean closeConn, String middleUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from business_middle where middleUUID = ?");
		params.add(middleUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public boolean offlineSave(String sourceId, boolean closeConn, OfflineOrders offlineOrders) {
		if(offlineOrders == null) {
			return false;
		}
		if(offlineOrders.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, offlineOrders, false);
		}else {
			return entitydao.updateEntity(sourceId, offlineOrders, closeConn);
		}
	}

	@Override
	public OfflineOrders offlineOrdersParameter(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from offline_orders where 1=1");
		if(findMap != null) {
			if(findMap.get("offlineUUID") != null && !"".equals(findMap.get("offlineUUID")) && !"null".equals(findMap.get("offlineUUID"))) {
				//UUID
				sql.append(" and offlineUUID = ?");
				params.add(findMap.get("offlineUUID"));
			}
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//供应商UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("itemUUID") != null && !"".equals(findMap.get("itemUUID")) && !"null".equals(findMap.get("itemUUID"))) {
				//商品UUID
				sql.append(" and itemUUID = ?");
				params.add(findMap.get("itemUUID"));
			}
			if(findMap.get("retailerUUID") != null && !"".equals(findMap.get("retailerUUID")) && !"null".equals(findMap.get("retailerUUID"))) {
				//零售商UUID
				sql.append(" and retailerUUID = ?");
				params.add(findMap.get("retailerUUID"));
			}
			if(findMap.get("state") != null && !"".equals(findMap.get("state")) && !"null".equals(findMap.get("state"))) {
				//商品状态（0：待付款，1：已付款）
				sql.append(" and state = ?");
				params.add(findMap.get("state"));
			}
		}
		return (OfflineOrders) sqldao.findEntityBySql(sourceId, sql.toString(), OfflineOrders.class, closeConn, params);
	}

	@Override
	public List<OfflineOrders> offlineOrdersAll(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from offline_orders where 1=1");
		if(findMap != null) {
			if(findMap.get("offlineUUID") != null && !"".equals(findMap.get("offlineUUID")) && !"null".equals(findMap.get("offlineUUID"))) {
				//UUID
				sql.append(" and offlineUUID = ?");
				params.add(findMap.get("offlineUUID"));
			}
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//供应商UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("itemUUID") != null && !"".equals(findMap.get("itemUUID")) && !"null".equals(findMap.get("itemUUID"))) {
				//商品UUID
				sql.append(" and itemUUID = ?");
				params.add(findMap.get("itemUUID"));
			}
			if(findMap.get("retailerUUID") != null && !"".equals(findMap.get("retailerUUID")) && !"null".equals(findMap.get("retailerUUID"))) {
				//零售商UUID
				sql.append(" and retailerUUID = ?");
				params.add(findMap.get("itemUUID"));
			}
			if(findMap.get("state") != null && !"".equals(findMap.get("state")) && !"null".equals(findMap.get("state"))) {
				//商品状态（0：待付款，1：已付款）
				sql.append(" and state = ?");
				params.add(findMap.get("state"));
			}
			/*if(findMap.get("retailerPhone") != null && !"".equals(findMap.get("retailerPhone")) && !"null".equals(findMap.get("retailerPhone"))) {
				//零售商电话号码
				sql.append(" and retailerPhone = ?");
				params.add(findMap.get("retailerPhone"));
			}
			if(findMap.get("retailerName") != null && !"".equals(findMap.get("retailerName")) && !"null".equals(findMap.get("retailerName"))) {
				//零售商名称
				sql.append(" and retailerName like '%").append(findMap.get("retailerName").replace("'","")).append("%'");
			}*/
			if(findMap.get("condition") != null && !"".equals(findMap.get("condition")) && !"null".equals(findMap.get("condition"))) {
				//条件查询
				sql.append(" and businessName like '%").append(findMap.get("condition").replace("'","")).append("%'");
				sql.append(" or retailerPhone = ?");
				params.add(findMap.get("condition"));
				sql.append(" or offlineUUID = ?");
				params.add(findMap.get("condition"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		sql.append(" order by time desc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), OfflineOrders.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, OfflineOrders.class, page, params);
		}
	}

	@Override
	public Map offlineMoney(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select count(totalPrice) as totalPrice from offline_orders where state = 1");
		if(findMap != null) {
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params); 
	}

	@Override
	public boolean stateUp(String sourceId, boolean closeConn, String offlineUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("update offline_orders set state = 1 where offlineUUID = ?");
		params.add(offlineUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map returnQuantityCount(String sourceId, boolean closeConn, String offlineUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(returnQuantity) as returnQuantity from offline_detailed where offlineUUID = ?");
		params.add(offlineUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public boolean detailedSave(String sourceId, boolean closeConn, OfflineDetailed offlineDetailed) {
		if(offlineDetailed == null) {
			return false;
		}
		if(offlineDetailed.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, offlineDetailed, false);
		}else {
			return entitydao.updateEntity(sourceId, offlineDetailed, closeConn);
		}
	}

	@Override
	public List<OfflineDetailed> offlineDetailedAll(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from offline_detailed where 1=1");
		if(findMap != null) {
			if(findMap.get("offlineUUID") != null && !"".equals(findMap.get("offlineUUID")) && !"null".equals(findMap.get("offlineUUID"))) {
				//线下订单UUID
				sql.append(" and offlineUUID = ?");
				params.add(findMap.get("offlineUUID"));
			}
		}
		sql.append(" order by time desc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), OfflineDetailed.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, OfflineDetailed.class, page, params);
		}
	}

	@Override
	public Map offlineTodayMoney(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from offline_orders where businessUUID = ? and state = 1 and to_days(time) = to_days(now())");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map offlineCountMoney(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from offline_orders where businessUUID = ? and state = 1");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map offlineTodayOrder(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from offline_orders where businessUUID = ? and state = 1 and to_days(time) = to_days(now())");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map offlineTodayRefund(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(returnPrice) as returnPrice from offline_detailed where businessUUID = ? and to_days(time) = to_days(now())");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map offlineMoneyRefund(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(returnPrice) as returnPrice from offline_detailed where businessUUID = ?");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map offlineOrderRefund(String sourceId, boolean closeConn, String businessUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from offline_detailed where businessUUID = ?");
		params.add(businessUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineTodayMoney(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where orderStatus = 1 and entityUUID = ? and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineCountMoney(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where orderStatus = 1 and entityUUID = ?");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineTodayOrder(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from vip_order where orderStatus = 1 and entityUUID = ? and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineTodayRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where orderStatus = -4 and entityUUID = ? and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineMoneyRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where orderStatus = -4 and entityUUID = ?");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map onLineOrderRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from vip_order where orderStatus = -4 and entityUUID = ?");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<OfflineOrders> offlineStatistics(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from offline_orders where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("state") != null && !"".equals(findMap.get("state")) && !"null".equals(findMap.get("state"))) {
				//订单状态（0：待付款，1：已付款）
				sql.append(" and state = ?");
				params.add(findMap.get("state"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		sql.append(" order by time desc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), OfflineOrders.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, OfflineOrders.class, page, params);
		}
	}

	@Override
	public Map totalMoney(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from offline_orders where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("state") != null && !"".equals(findMap.get("state")) && !"null".equals(findMap.get("state"))) {
				//订单状态（0：待付款，1：已付款）
				sql.append(" and state = ?");
				params.add(findMap.get("state"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map totalOrder(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from offline_orders where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("state") != null && !"".equals(findMap.get("state")) && !"null".equals(findMap.get("state"))) {
				//订单状态（0：待付款，1：已付款）
				sql.append(" and state = ?");
				params.add(findMap.get("state"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<OfflineDetailed> detailedRefund(String sourceId, boolean closeConn, Page page,
			Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from offline_detailed where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		sql.append(" order by time desc ");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), OfflineDetailed.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, OfflineDetailed.class, page, params);
		}
	}

	@Override
	public Map detailedMoney(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(returnPrice) as returnPrice from offline_detailed where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map detailedOrder(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from offline_detailed where 1=1");
		if(findMap != null) {
			if(findMap.get("businessUUID") != null && !"".equals(findMap.get("businessUUID")) && !"null".equals(findMap.get("businessUUID"))) {
				//商家UUID
				sql.append(" and businessUUID = ?");
				params.add(findMap.get("businessUUID"));
			}
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lstoDayMoney(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where entityUUID = ? and orderStatus IN(1,2,3,4) and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lsCountMoney(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where entityUUID = ? and orderStatus IN(1,2,3,4)");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lstoDayOrder(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from vip_order where entityUUID = ? and orderStatus IN(1,2,3,4) and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lsTodayRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where entityUUID = ? and orderStatus = -4 and to_days(payTime) = to_days(now())");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lsMoneyRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as totalPrice from vip_order where entityUUID = ? and orderStatus = -4");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map lsOrderRefund(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select COUNT(*) as count from vip_order where entityUUID = ? and orderStatus = -4");
		params.add(entityUUID);
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public BusinessMiddle gysOrlss(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from business_middle where 1=1");
		if(findMap != null) {
			if(findMap.get("supplierUUID") != null && !"".equals(findMap.get("supplierUUID")) && !"null".equals(findMap.get("supplierUUID"))) {
				//供应商UUID
				sql.append(" and supplierUUID = ?");
				params.add(findMap.get("supplierUUID"));
			}
			if(findMap.get("retailerUUID") != null && !"".equals(findMap.get("retailerUUID")) && !"null".equals(findMap.get("retailerUUID"))) {
				//零售商UUID
				sql.append(" and retailerUUID = ?");
				params.add(findMap.get("retailerUUID"));
			}
		}
		return (BusinessMiddle) sqldao.findEntityBySql(sourceId, sql.toString(), BusinessMiddle.class, closeConn, params);
	}

	@Override
	public List<Item> itemList(String sourceId, boolean closeConn, String entityUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from item where 1=1 and entityUUID = ? and itemStatus = 1 and isDel = 0");
		sql.append(" order by createTime desc");
		sql.append(" LIMIT 0,3");
		params.add(entityUUID);
		return sqldao.findListBySql(sourceId, sql.toString(), Item.class, closeConn, params);
	}

	@Override
	public boolean bankSave(String sourceId, boolean closeConn, BusinessApply businessApply) {
		if(businessApply == null) {
			return false;
		}
		if(businessApply.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, businessApply, false);
		}else {
			return entitydao.updateEntity(sourceId, businessApply, closeConn);
		}
	}

	@Override
	public List<SlideImg> slideImg(String sourceId, boolean closeConn, Page page,Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from slide_img where 1=1");
		sql.append(" order by time desc");
		if(page == null) {
			return sqldao.findListBySql(sourceId, sql.toString(), SlideImg.class, closeConn, params);
		}else {
			return sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, SlideImg.class, page, params);
		}
	}

	@Override
	public SlideImg slideImgId(String sourceId, boolean closeConn, String slideUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from slide_img where slideUUID = ?");
		params.add(slideUUID);
		return (SlideImg) sqldao.findEntityBySql(sourceId, sql.toString(), SlideImg.class, closeConn, params);
	}

	@Override
	public boolean slideSave(String sourceId, boolean closeConn, SlideImg slideImg) {
		if(slideImg == null) {
			return false;
		}
		if(slideImg.getId() == null) {//新增
			return entitydao.saveEntity(sourceId, slideImg, false);
		}else {
			return entitydao.updateEntity(sourceId, slideImg, closeConn);
		}
	}

	@Override
	public boolean slideDel(String sourceId, boolean closeConn, String slideUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("delete from slide_img where slideUUID = ?");
		params.add(slideUUID);
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public boolean businessXl(String sourceId, boolean closeConn, String businessUUID, Integer salesVolume) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("update business_apply set salesVolume = '"+salesVolume+"' where businessUUID = '"+businessUUID+"'");
		return sqldao.executeSql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public List<OrderItem> orderItemsAll(String sourceId, boolean closeConn,Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from order_item where 1=1 and orderStatus IN(1,2,3,4)");
		if(findMap != null) {
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return sqldao.findListBySql(sourceId, sql.toString(), OrderItem.class, closeConn, params);
	}

	@Override
	public Map ptXxNumber(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(totalPrice) as countMoney from offline_orders where state = 1");
		if(findMap != null) {
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(time, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map ptXsNumber(String sourceId, boolean closeConn, Map<String, String> findMap) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select SUM(price) as countMoney from order_item where orderStatus IN(1,2,3,4)");
		if(findMap != null) {
			if(findMap.get("startTime") != null && !"".equals(findMap.get("startTime")) && !"null".equals(findMap.get("startTime"))) {
				//开始时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') >= ?");
				params.add(findMap.get("startTime"));
			}
			if(findMap.get("endingTime") != null && !"".equals(findMap.get("endingTime")) && !"null".equals(findMap.get("endingTime"))) {
				//结束时间
				sql.append(" and DATE_FORMAT(createTime, '%Y-%m-%d') <= ?");
				params.add(findMap.get("endingTime"));
			}
		}
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, params);
	}

	@Override
	public Map ptXxMoney(String sourceId, boolean closeConn) {
		StringBuilder sql = new StringBuilder();
		sql.append("select SUM(totalPrice) as totalPrice from offline_orders where state = 1 and to_days(time) = to_days(now())");
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, null);
	}

	@Override
	public Map ptXsMoney(String sourceId, boolean closeConn) {
		StringBuilder sql = new StringBuilder();
		sql.append("select SUM(price) as totalPrice from order_item where orderStatus IN(1,2,3,4) and to_days(createTime) = to_days(now())");
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, null);
	}

	@Override
	public Map ptXxOrder(String sourceId, boolean closeConn) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COUNT(*) as count from offline_orders where state = 1 and to_days(time) = to_days(now())");
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, null);
	}

	@Override
	public Map ptXsOrder(String sourceId, boolean closeConn) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COUNT(*) as count from order_item where orderStatus IN(1,2,3,4) and to_days(createTime) = to_days(now())");
		return (Map)sqldao.findMapBysql(sourceId, sql.toString(), closeConn, null);
	}

	@Override
	public VipInfo vipInfoId(String sourceId, boolean closeConn, String vipUUID) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("select * from vip_info where vipUUID = ?");
		params.add(vipUUID);
		return (VipInfo) sqldao.findEntityBySql(sourceId, sql.toString(), VipInfo.class, closeConn, params);
	}

}
