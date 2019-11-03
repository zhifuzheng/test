package com.xryb.zhtc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Integral;
import com.xryb.zhtc.entity.IntegralGoodOrder;
import com.xryb.zhtc.entity.IntegralGoods;
import com.xryb.zhtc.entity.IntegralRecordOfUser;
import com.xryb.zhtc.entity.IntegralRule;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.entity.PlatformActiveGood;
import com.xryb.zhtc.entity.PlatformActivityOrder;
import com.xryb.zhtc.entity.VipInfo;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.util.DateTimeUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
import spark.annotation.Auto;

public class IntegralServiceImpl implements IIntegralService {
	@Auto(name = EntityDao.class)
	private EntityDao entitydao;
	
	@Auto(name = SqlDao.class)
	private SqlDao sqldao;
	
	
	
	
	
	/**
	 * 积分商城播报信息获取
	 */
	@Override
	public List<IntegralGoodOrder> getAnnounceMsg(String sourceId, boolean closeConn) {
		//最新10条数据
		String sql = "select receiverMobile,title,payerName from integral_good_order order by createTime desc limit 0,10 ";
		return (List<IntegralGoodOrder>)sqldao.findListBySql(sourceId, sql, IntegralGoodOrder.class, closeConn, null);
	}

	/**
	 * 修改活动购买量
	 */
	@Override
	public boolean updateActiveGetNumber(String sourceId, boolean closeConn, Integer getNumber, String activityUUID) {
		String sql = "update platform_active_good set getNumber="+getNumber+" where activityUUID='"+activityUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	@Override
	public boolean createUserIntegralRecord(String sourceId, boolean closeConn, IntegralRecordOfUser ofUser) {
		return entitydao.saveEntity(sourceId, ofUser, closeConn);
	}

	/**
	 * 用户删除活动商品订单
	 */
	@Override
	public boolean userDelAvtiveGoodOrder(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update platform_good_order set userIsDel=1 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 根据用户uuid获取活动订单列表
	 */
	@Override
	public List<PlatformActivityOrder> findUserActiveOrderList(String sourceId, boolean closeConn, String vipUUID,Page page) {
		String sql = "select * from platform_good_order where userIsDel=0 and payerUUID='"+vipUUID+"'";
		return (List<PlatformActivityOrder>)sqldao.findPageByMysql(sourceId, sql, closeConn, PlatformActivityOrder.class, page, null);
	}

	/**
	 * 获取用户参与活动的次数
	 */
	@Override
	public Map<String, Object> getUserJoinTime(String sourceId, boolean closeConn, String activityUUID,String vipUUID) {
		String sql = "select count(*) joinTime from platform_good_order where activityUUID='"+activityUUID+"' and payerUUID='"+vipUUID+"'";
		return sqldao.findMapBysql(sourceId, sql, closeConn, null);
	}

	/**
	 * 手机端获取官方活动列表
	 */
	@Override
	public List<PlatformActiveGood> findActiveGoodsListOfUser(String sourceId, boolean closeConn, Page page) {
		String sql = "select * from platform_active_good where isDel=0 and goodStatus=1";
		return (List<PlatformActiveGood>)sqldao.findPageByMysql(sourceId, sql, closeConn, PlatformActiveGood.class, page, null);
	}
	
	/**
	 * 编辑保存积分商品
	 */
	@Override
	public boolean editorIntegralGood(String sourceId, boolean closeConn, IntegralGoods integralGood) {
		integralGood.setUpdateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return entitydao.updateEntity(sourceId, integralGood, closeConn);
	}

	/**
	 * 获取积分商品信息
	 */
	@Override
	public IntegralGoods platformFindIntegralGoodInfo(String sourceId, boolean closeConn, String goodUUID) {
		String sql = "select * from integral_goods where goodUUID='"+goodUUID+"'";
		return (IntegralGoods) sqldao.findEntityBySql(sourceId, sql, IntegralGoods.class, closeConn, null);
	}

	/**
	 * 获取活动信息
	 */
	@Override
	public PlatformActiveGood findActivityInfo(String sourceId, boolean closeConn, String activityUUID) {
		String sql = "select * from platform_active_good where activityUUID='"+activityUUID+"'";
		return (PlatformActiveGood) sqldao.findEntityBySql(sourceId, sql, PlatformActiveGood.class, closeConn, null);
	}

	/**
	 *  删除、上架、下架活动
	 *  
	 */
	@Override
	public boolean platformOprateActivity(String sourceId, boolean closeConn, String activityUUID,Integer type) {
		//1-删除、2-上架、3-下架活动
		StringBuilder sqls = new StringBuilder("update platform_active_good set");
		if(type == 1 ) {
			sqls.append(" isDel=1");
		}
		if(type == 2) {
			sqls.append(" goodStatus=1");
		}
		if(type == 3) {
			sqls.append(" goodStatus=2");
		}
		sqls.append(" where activityUUID='"+activityUUID+"'");
		System.out.println("sqls:"+sqls);
		return sqldao.executeSql(sourceId, sqls.toString(), closeConn, null);
	}
	
	/**
	 * 删除、上架、下架积分商品
	 */
	@Override
	public boolean platformOprateIntegralGoods(String sourceId, boolean closeConn, String goodUUID,Integer type) {
		//1-删除、2-上架、3-下架活动
		StringBuilder sqls = new StringBuilder("update integral_goods set");
		if(type == 1 ) {
			sqls.append(" isDel=1");
		}
		if(type == 2) {
			sqls.append(" goodStatus=1");//1上架，2下架
		}
		if(type == 3) {
			sqls.append(" goodStatus=2");
		}
		
		sqls.append(" where goodUUID='"+goodUUID+"'");
		System.out.println("sqls:"+sqls);
		return sqldao.executeSql(sourceId, sqls.toString(), closeConn, null);
	}

	/**
	 * 平台获取积分详情记录
	 */
	@Override
	public List<IntegralRecordOfUser> platformGetIntegralDetailRecord(String sourceId, boolean closeConn,String type,Page page) {
		String sql = "select * from integral_change_record where 1=1";//order by changeTime desc
		StringBuilder sqls = new StringBuilder(sql);
		Integer types = Integer.parseInt(type);
		//1-账户充值，2-购买商品，3-积分兑换商品
		if( types != 0) {
			sqls.append(" and integralFrom="+types);
		}
		sqls.append(" order by changeTime desc");
		return (List<IntegralRecordOfUser>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralRecordOfUser.class, page, null);
	}

	/**
	 * 修改订单为已过期
	 */
	@Override
	public boolean updateIntegralOrderStatus(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update integral_good_order set orderStatus=2 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	@Override
	public boolean updateActiveOrderStatus(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update platform_good_order set orderStatus=2 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 获取活动商品订单信息
	 */
	@Override
	public IntegralGoodOrder findActivityOrderItem(String sourceId, boolean closeConn,String vipUUID, String goodUUID,String entityUUID) {
		String sql = "select * from integral_good_order where payerUUID='"+vipUUID+"' and goodUUID='"+goodUUID+"' and entityUUID='"+entityUUID+"'"; 
		return (IntegralGoodOrder) sqldao.findEntityBySql(sourceId, sql, IntegralGoodOrder.class, closeConn, null);
	}
	
	@Override
	public OrderItem findOrderItem(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "select * from order_item where orderUUID='"+orderUUID+"'";
		return (OrderItem) sqldao.findEntityBySql(sourceId, sql, OrderItem.class, closeConn, null);
	}


	/**
	 * 获取活动商品信息
	 */
	@Override
	public PlatformActiveGood findPlatformGoodInfo(String sourceId, boolean closeConn, String goodUUID) {
		String sql = "select * from platform_active_good where goodUUID='"+goodUUID+"'";
		return (PlatformActiveGood) sqldao.findEntityBySql(sourceId, sql, PlatformActiveGood.class, closeConn, null);
	}


	/**
	 * 修改用户已参与1元活动
	 */
	@Override
	public boolean userIsJoin(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "update vip_info set isJoin='1' where vipUUID='"+vipUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 平台下架积分商品
	 */
	@Override
	public boolean platformDownGood(String sourceId, boolean closeConn, String goodUUID) {
		//goodStatus：1上架，2下架，3未上架
		String sql = "update integral_goods set goodStatus=2 where goodUUID='"+goodUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 平台上架积分商品
	 */
	@Override
	public boolean platformReleaseGood(String sourceId, boolean closeConn, String goodUUID) {
		//goodStatus：1上架，2下架，3未上架
		String sql = "update integral_goods set goodStatus=1 where goodUUID='"+goodUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 平台删除积分商品
	 */
	@Override
	public boolean platformDelGoodByGoodUUID(String sourceId, boolean closeConn, String goodUUID) {
		String sql = "update integral_goods set isDel=2 where goodUUID='"+goodUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 平台获取积分商城商品
	 */
	@Override
	public List<IntegralGoods> findIntegralShopGoodList(String sourceId, boolean closeConn, String keyWords,Integer goodStatus,Page page) {
		//goodStatus:1上架，2下架，3未上架，;isDel:0-未删除，1-已删除
		String sql = "select * from integral_goods where isDel=0 and goodStatus="+goodStatus+"";
		StringBuilder sqls = new StringBuilder(sql);
		if(!StringUtils.isNullOrEmpty(keyWords)) {
			sqls.append(" and title like '%"+keyWords+"%'");
		}
		sqls.append(" order by createTime desc");
		return (List<IntegralGoods>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralGoods.class, page, null);
	}


	/**
	 * 平台删除积分商品兑换订单
	 */
	@Override
	public boolean platformDelOrderByOrderUUID(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update integral_good_order set platformIsDel=1 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 * 获取活动商品
	 */
	@Override
	public List<PlatformActiveGood> findActivityGoodsList(String sourceId, boolean closeConn, Page page) {
		String sql = "select * from platform_active_good where goodStatus=1 and isDel=0 and stock>0";
		return (List<PlatformActiveGood>)sqldao.findPageByMysql(sourceId, sql, closeConn, PlatformActiveGood.class, page, null);
	}


	/**
	 * 活动商品添加
	 */
	@Override
	public boolean addActivityGood(String sourceId, boolean closeConn, PlatformActiveGood activeGood) {
		activeGood.setCreateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return entitydao.saveEntity(sourceId, activeGood, closeConn);
	}
	/**
	 * 活动商品编辑修改
	 */
	@Override
	public boolean editorActivityGood(String sourceId, boolean closeConn, PlatformActiveGood activeGood) {
		activeGood.setUpdateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return entitydao.updateEntity(sourceId, activeGood, closeConn);
	}


	/**
	 * 平台获取积分商城商品兑换订单信息
	 */
	@Override
	public List<IntegralGoodOrder> fingGoodOrderList(String sourceId, boolean closeConn, String keyWords,Integer orderStatus,Page page) {
		//orderStatus:0-未领取，1-已领取，2-已过期;platformIsDel:0-未删除，1-已删除
		String sql = "select * from integral_good_order where orderStatus="+orderStatus+" and platformIsDel=0";
		StringBuilder sqls = new StringBuilder(sql);
		if(!StringUtils.isNullOrEmpty(keyWords)) {
			sqls.append(" and title like '%"+keyWords+"%'");
		}
		sqls.append(" order by createTime desc");
		return (List<IntegralGoodOrder>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralGoodOrder.class, page, null);
	}


	/**
	 * 积分商品领取
	 */
	@Override
	public boolean updateIntegralGoodInfo(String sourceId, boolean closeConn, String orderUUID) {
		String endTime = DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
		String sql = "update integral_good_order set orderStatus=1,endTime='"+endTime+"' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	@Override
	public boolean updateActiveGoodInfo(String sourceId, boolean closeConn, String orderUUID) {
		String endTime = DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
		String sql = "update platform_good_order set orderStatus=1,endTime='"+endTime+"' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}


	/**
	 *  获取积分商品订单信息
	 */
	@Override
	public IntegralGoodOrder findIntegralGoodOrderInfo(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "select * from integral_good_order where orderUUID='"+orderUUID+"'";
		return (IntegralGoodOrder) sqldao.findEntityBySql(sourceId, sql, IntegralGoodOrder.class, closeConn, null);
	}
	
	/**
	 *  获取平台后动用户订单信息
	 */
	@Override
	public PlatformActivityOrder findActiveOrderInfo(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "select * from platform_good_order where orderUUID='"+orderUUID+"'";
		return (PlatformActivityOrder) sqldao.findEntityBySql(sourceId, sql, PlatformActivityOrder.class, closeConn, null);
	}
	

	/**
	 * 创建订单
	 */
	@Override
	public boolean createIntegralGoodOrder(String sourceId, boolean closeConn, IntegralGoodOrder goodOrder) {
		return entitydao.saveEntity(sourceId, goodOrder, closeConn);
	}
	
	/**
	 * 创建平台活动订单
	 */
	@Override
	public boolean createActiveGoodOrder(String sourceId, boolean closeConn, PlatformActivityOrder activeOrder) {
		return entitydao.saveEntity(sourceId, activeOrder, closeConn);
	}
	

	/**
	 * 获取店铺信息
	 */
	@Override
	public BusinessApply findBusinessInfoByUUID(String sourceId, boolean closeConn,String businessUUID) {
		String sql = "select * from business_apply where businessUUID='"+businessUUID+"'";
		return (BusinessApply) sqldao.findEntityBySql(sourceId, sql, BusinessApply.class, closeConn, null);
	}

	/**
	 * 库存修改
	 */
	@Override
	public boolean updateGoodStock(String sourceId, boolean closeConn, Integer stock,String goodUUID) {
		String sql = "update integral_goods set stock="+stock+" where goodUUID='"+goodUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 获取商品详细信息
	 */
	@Override
	public IntegralGoods getGoodsInfo(String sourceId, boolean closeConn, String goodUUID) {
		String sql = "select * from integral_goods where goodUUID='"+goodUUID+"'";
		return (IntegralGoods) sqldao.findEntityBySql(sourceId, sql, IntegralGoods.class, closeConn, null);
	}

	/**
	 * 获取积分商城可兑换商品
	 */
	@Override
	public List<IntegralGoods> findExIntegralGoodsList(String sourceId, boolean closeConn, Page page) {
		String sql = "select * from integral_goods where goodStatus=1 and isDel=0 and stock>0";
		return (List<IntegralGoods>)sqldao.findPageByMysql(sourceId, sql, closeConn, IntegralGoods.class, page, null);
	}

	/**
	 * PC获取积分商城商品
	 */
	@Override
	public List<IntegralGoods> findIntegralGoodsList(String sourceId, boolean closeConn,Page page, Map<String, Object> param) {
		StringBuilder sql = new StringBuilder("select * from integral_goods where isDel=0 ");

		// 商品状态 1上架，2下架，3未上架，4商品违规
		if (!"-1".equals(param.get("status"))) {
			sql.append(" and goodStatus=" + param.get("status"));
		}

		// 商品名称
		if (param.get("searchWords") != null && !"".equals(param.get("searchWords"))) {
			sql.append(" and title like '%" + param.get("searchWords") + "%'");
		}

		// 排序条件
		if (!"-1".equals(param.get("range"))) {
			sql.append(" order by " + param.get("range") + " desc");
		}

		System.out.println("sql:"+sql);

		return (List<IntegralGoods>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, IntegralGoods.class, page, null);
	}
	
	/**
	 * PC端条件获取平台活动
	 */
	@Override
	public List<PlatformActiveGood> findActiveGoodsList(String sourceId, boolean closeConn,Page page, Map<String, Object> param) {
		StringBuilder sql = new StringBuilder("select * from platform_active_good where isDel=0 ");
		//活动状态 1上架，2下架，3未上架
		String status = param.get("status")+"";
		if (!"0".equals(status)) {
			sql.append(" and goodStatus=" + status);
		}
		
//		String nowTime = DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
//		if("releaseEndTime".equals(status)) {
//			sql.append(" and activeEndTime<" + nowTime);
//		}
		
		//  活动名称
		if (param.get("searchWords") != null && !"".equals(param.get("searchWords"))) {
			sql.append(" and activityTitle like '%" + param.get("searchWords") + "%'");
		}
		//System.out.println("sql:"+sql.toString());
		return (List<PlatformActiveGood>)sqldao.findPageByMysql(sourceId, sql.toString(), closeConn, PlatformActiveGood.class, page, null);
	}

	/**
	 * 添加积分商城商品
	 */
	@Override
	public boolean addIntegralGoods(String sourceId, boolean closeConn, IntegralGoods integralgoods) {
		System.out.println("id:"+integralgoods.getId());
		if(integralgoods.getId() == null) {//新增
			String goodUUID = UUID.randomUUID().toString().replaceAll("-", "");
			integralgoods.setGoodUUID(goodUUID);
			integralgoods.setCreateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			return entitydao.saveEntity(sourceId, integralgoods, closeConn);
		}else {//修改
			integralgoods.setUpdateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			return entitydao.updateEntity(sourceId, integralgoods, closeConn);
		}
		
	}

	/**
	 * 删除积分规则
	 */
	@Override
	public boolean delIntegralRule(String sourceId, boolean closeConn, String ruleUUID) {
		String sql = "delete from integral_rule where ruleUUID='"+ruleUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	@Override
	public List<IntegralRule> findIntegralRuleListByType(String sourceId, boolean closeConn, Integer type) {
		String  sql = "select gradMin,gradMax,calculateRule from integral_rule where state=1 and ruleType="+type;
		return (List<IntegralRule>)sqldao.findListBySql(sourceId, sql, IntegralRule.class, closeConn, null);
	}

	/**
	 *  获取积分规则数据列表
	 */
	@Override
	public List<IntegralRule> findIntegralRuleList(String sourceId, boolean closeConn,Page page,String type) {
		Integer ruleType = Integer.parseInt(type);
		StringBuilder sqls = new StringBuilder("select * from integral_rule");
		if(ruleType != 0) {//限条件查询
			sqls.append(" where ruleType="+ruleType);
		}
		return (List<IntegralRule>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralRule.class, page, null);
	}

	/**
	 * 新增或保存编辑积分规则
	 */
	@Override
	public boolean addOrUpdataIntegralRule(String sourceId, boolean closeConn, IntegralRule rule) {
		//System.out.println("id:"+rule.getId());
		if(rule.getId() == null) {//新增
			//1.设置uuid
			rule.setRuleUUID(UUID.randomUUID().toString().replaceAll("-", ""));
			//2.设置创建时间
			rule.setCreateTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			//3.判断是否启用
			if(rule.getState() == 1) {// 启用
				//设置启用时间
				rule.setStartUseTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			}
			return entitydao.saveEntity(sourceId, rule, closeConn);
		}else {//更新
			//1.设置修改时间
			rule.setModifiedTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			//2.判断是否启用
			if(rule.getState() == 1) {// 启用
				//设置启用时间
				rule.setStartUseTime(DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			}
			return entitydao.updateEntity(sourceId, rule, closeConn);
		}
	}

	/**
	 * 积分兑换，购买商品，账户充值成功后写入数据到用户积分详情表中
	 */
	@Override
	public boolean dataAccess(String sourceId, boolean closeConn, IntegralRecordOfUser recordOfUser) {
		return entitydao.saveEntity(sourceId, recordOfUser, closeConn);
	}

	/**
	 * 查询用户信息
	 */
	@Override
	public VipInfo findUserInfo(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "select * from vip_info where vipUUID='"+vipUUID+"'";
		return (VipInfo)sqldao.findEntityBySql(sourceId, sql, VipInfo.class, closeConn, null);
	}

	/**
	 * 删除用户积分商品订单兑换记录
	 */
	@Override
	public boolean deleteUserIntegralGoodExRecord(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update integral_good_order set userIsDel=1 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	/**
	 *  平台删除用户积分商品订单兑换记录
	 */
	@Override
	public boolean platformDeleteUserIntegralGoodExRecord(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update integral_good_order set platformIsDel=1 where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * vip用户积分兑换商品记录
	 */
	@Override
	public List<IntegralGoodOrder> findIntegralExGoodsList(String sourceId, boolean closeConn, String vipUUID,String searchWords,Page page) {
		String sql = "select * from integral_good_order where userIsDel=0 and payerUUID='"+vipUUID+"'";
		StringBuilder sqls = new StringBuilder(sql);
		if(!StringUtils.isNullOrEmpty(searchWords)) {
			sqls.append(" and title like '%"+searchWords+"%'");
		}
		sqls.append(" order by createTime desc");
		return (List<IntegralGoodOrder>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralGoodOrder.class, page, null);
	}
	
	/**
	 * 平台获取用户积分兑换商品记录
	 */
	@Override
	public List<IntegralGoodOrder> paltformFindIntegralExGoodsList(String sourceId, boolean closeConn,String searchWords,Page page) {
		String sql = "select * from integral_good_order where platformIsDel=0";
		StringBuilder sqls = new StringBuilder(sql);
		if(!StringUtils.isNullOrEmpty(searchWords)) {
			sqls.append(" and title like '%"+searchWords+"%'");
		}
		sqls.append(" order by createTime desc");
		return (List<IntegralGoodOrder>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralGoodOrder.class, page, null);
	}

	/**
	 * 获取用户积分详情记录(积分来源和消耗)
	 */
	@Override
	public List<IntegralRecordOfUser> findUserIntegralList(String sourceId, boolean closeConn, String vipUUID,Page page) {
		String sql = "select * from integral_change_record where vipUUID='"+vipUUID+"' order by changeTime desc";
		return (List<IntegralRecordOfUser>)sqldao.findPageByMysql(sourceId, sql, closeConn, IntegralRecordOfUser.class, page, null);
	}

	/**
	 * 创建用户积分数据
	 */
	@Override
	public boolean addUserIntegral(String sourceId, boolean closeConn, Integral integral) {
		return entitydao.saveEntity(sourceId, integral, closeConn);
	}

	/**
	 * 获取用户的积分值
	 */
	@Override
	public Map<String, Object> getUserIntegral(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "select integral from integral where vipUUID='"+vipUUID+"'";
		return sqldao.findMapBysql(sourceId, sql, closeConn, null);
	}

	/**
	 * 修改用户积分
	 */
	@Override
	public boolean updateUserIntegral(String sourceId, boolean closeConn, Integer integral,String vipUUID) {
		String sql = "update integral set integral="+integral+" where vipUUID='"+vipUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}
	
	
	/**
	 * PC端获取用户积分详情记录
	 */
	@Override
	public List<IntegralRecordOfUser> findUserIntegralListByType(String sourceId, boolean closeConn, String vipUUID,String type,Page page) {
		String sql = "select * from integral_change_record where vipUUID='"+vipUUID+"'";//order by changeTime desc
		StringBuilder sqls = new StringBuilder(sql);
		Integer types = Integer.parseInt(type);
		//1-账户充值，2-购买商品，3-积分兑换商品
		if( types != 0) {
			sqls.append(" and integralFrom="+types);
		}
		sqls.append(" order by changeTime desc");
		return (List<IntegralRecordOfUser>)sqldao.findPageByMysql(sourceId, sqls.toString(), closeConn, IntegralRecordOfUser.class, page, null);
	}
	
	

}
