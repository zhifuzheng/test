package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

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

import dbengine.util.Page;

/**
 * 用户积分
 * @author apple
 */
public interface IIntegralService {
	/**
	 * 删除积分规则
	 */
	boolean delIntegralRule(String sourceId,boolean closeConn,String ruleUUID);
	
	/**
	 * 获取启用的积分规则梯度列表
	 */
	List<IntegralRule> findIntegralRuleListByType(String sourceId,boolean closeConn,Integer type);
	
	
	/**
	 * 获取积分规则数据列表
	 */
	List<IntegralRule> findIntegralRuleList(String sourceId,boolean closeConn,Page page,String type);
	
	
	/**
	 * 新增或保存积分规则
	 */
	boolean addOrUpdataIntegralRule(String sourceId,boolean closeConn,IntegralRule rule);
	
	
	/**
	 * 积分兑换，购买商品，账户充值成功后写入数据到用户积分详情表中
	 */
	boolean dataAccess(String sourceId,boolean closeConn,IntegralRecordOfUser recordOfUser);
	
	
	/**
	 * 查询用户信息
	 */
	VipInfo findUserInfo(String sourceId,boolean closeConn,String vipUUID);
	
	/**
	 * 删除用户积分商品兑换记录
	 */
	boolean deleteUserIntegralGoodExRecord(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 平台删除用户积分商品兑换记录
	 */
	boolean platformDeleteUserIntegralGoodExRecord(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 会员获取积分兑换商品记录
	 */
	List<IntegralGoodOrder> findIntegralExGoodsList(String sourceId,boolean closeConn,String vipUUID,String searchWords,Page page);
	
	/**
	 * 平台积分兑换商品记录
	 */
	List<IntegralGoodOrder> paltformFindIntegralExGoodsList(String sourceId,boolean closeConn,String searchWords ,Page page);
	
	/**
	 * 创建用户积分数据
	 */
	boolean addUserIntegral(String sourceId,boolean closeConn,Integral integral);
	
	
	/**
	 * 获取用户的账户积分
	 */
	Map<String,Object> getUserIntegral(String sourceId,boolean closeConn,String vipUUID);
	
	/**
	 * 修改用户积分
	 */
	boolean updateUserIntegral(String sourceId,boolean closeConn,Integer integral,String vipUUID);
	
	/**
	 * 获取用户积分详情记录(积分来源和消耗)
	 */
	List<IntegralRecordOfUser> findUserIntegralList(String sourceId,boolean closeConn,String vipUUID,Page page);
	
	/**
	 * PC端获取用户积分详情记录
	 */
	List<IntegralRecordOfUser> findUserIntegralListByType(String sourceId,boolean closeConn,String vipUUID,String type,Page page);
	
	/**
	 * 添加积分商城商品
	 */
	boolean addIntegralGoods(String sourceId,boolean closeConn,IntegralGoods integralgoods);
	
	/**
	 * PC端条件获取积分商城商品
	 */
	List<IntegralGoods> findIntegralGoodsList(String sourceId,boolean closeConn,Page page,Map<String,Object> param);
	
	/**
	 * PC端条件获取平台活动
	 */
	List<PlatformActiveGood> findActiveGoodsList(String sourceId,boolean closeConn,Page page,Map<String,Object> param);
	
	/**
	 * 手机端获取平台活动
	 */
	List<PlatformActiveGood> findActiveGoodsListOfUser(String sourceId,boolean closeConn,Page page);
	
	/**
	 * 获取积分商城可兑换商品
	 */
	List<IntegralGoods> findExIntegralGoodsList(String sourceId,boolean closeConn,Page page);
	
	/**
	 * 获取商品详细信息
	 */
	IntegralGoods getGoodsInfo(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 修改库存
	 */
	boolean updateGoodStock(String sourceId,boolean closeConn,Integer stock,String goodUUID);
	
	/**
	 * 获取店铺信息
	 */
	BusinessApply findBusinessInfoByUUID(String sourceId,boolean closeConn,String businessUUID);
	
	/**
	 * 创建积分商品订单
	 */
	boolean createIntegralGoodOrder(String sourceId,boolean closeConn,IntegralGoodOrder goodOrder);
	
	/**
	 * 创建活动商品订单
	 */
	boolean createActiveGoodOrder(String sourceId,boolean closeConn,PlatformActivityOrder activeOrder);
	
	
	/**
	 * 根据订单UUID获取订单信息
	 */
	IntegralGoodOrder findIntegralGoodOrderInfo(String sourceId,boolean closeConn,String orderUUID);
	
	PlatformActivityOrder findActiveOrderInfo(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 根据用户uuid获取活动订单列表
	 */
	List<PlatformActivityOrder> findUserActiveOrderList(String sourceId,boolean closeConn,String vipUUID,Page page);
	
	/**
	 * 积分商品领取修改订单相关信息
	 */
	boolean updateIntegralGoodInfo(String sourceId,boolean closeConn,String orderUUID);//积分商品
	boolean updateActiveGoodInfo(String sourceId,boolean closeConn,String orderUUID);//官方活动商品
	
	/**
	 * 平台获取积分商城商品兑换订单信息
	 */
	List<IntegralGoodOrder> fingGoodOrderList(String sourceId,boolean closeConn,String keyWords,Integer orderStatus,Page page);
	
	/**
	 * 平台获取积分商城商品
	 */
	List<IntegralGoods> findIntegralShopGoodList(String sourceId,boolean closeConn,String keyWords,Integer goodStatus,Page page);
	
	/**
	 * 平台活动商品添加
	 */
	boolean addActivityGood(String sourceId,boolean closeConn,PlatformActiveGood activeGood);
	boolean editorActivityGood(String sourceId,boolean closeConn,PlatformActiveGood activeGood);
	
	boolean editorIntegralGood(String sourceId,boolean closeConn,IntegralGoods integralGood);
	
	/**
	 * 获取平台活动商品列表
	 */
	List<PlatformActiveGood> findActivityGoodsList(String sourceId,boolean closeConn,Page page);
	
	/**
	 * 平台删除积分商品兑换订单
	 */
	boolean platformDelOrderByOrderUUID(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 平台删除积分商品
	 */
	boolean platformDelGoodByGoodUUID(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 平台下架积分商品
	 */
	boolean platformDownGood(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 平台上架积分商品
	 */
	boolean platformReleaseGood(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 修改用户已参与1元活动
	 */
	boolean userIsJoin(String sourceId,boolean closeConn,String vipUUID);
	
	/**
	 * 获取活动商品信息
	 */
	PlatformActiveGood findPlatformGoodInfo(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 获取活动商品信息
	 */
	OrderItem findOrderItem(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 获取活动商品订单信息
	 */
	IntegralGoodOrder findActivityOrderItem(String sourceId,boolean closeConn,String vipUUID,String goodUUID,String entityUUID);
	
	/**
	 * 修改订单为已过期
	 */
	boolean updateIntegralOrderStatus(String sourceId,boolean closeConn,String orderUUID);//积分商品订单
	boolean updateActiveOrderStatus(String sourceId,boolean closeConn,String orderUUID);//官方活动订单
	
	/**
	 * 平台获取积分详情记录
	 */
	List<IntegralRecordOfUser> platformGetIntegralDetailRecord(String sourceId,boolean closeConn,String type ,Page page);
	
	/**
	 * 删除、上架、下架活动
	 */
	boolean platformOprateActivity(String sourceId,boolean closeConn,String activityUUID,Integer type);
	boolean platformOprateIntegralGoods(String sourceId,boolean closeConn,String goodsUUID,Integer type);
	
	/**
	 * 获取活动信息
	 */
	PlatformActiveGood findActivityInfo(String sourceId,boolean closeConn,String activityUUID);
	
	/**
	 * 获取用户参与活动的次数
	 */
	Map<String,Object> getUserJoinTime(String sourceId,boolean closeConn,String activityUUID,String vipUUID);
 	
	/**
	 * 获取积分商品信息
	 */
	IntegralGoods platformFindIntegralGoodInfo(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 用户删除活动商品订单
	 */
	boolean userDelAvtiveGoodOrder(String sourceId,boolean closeConn,String orderUUID);
	
	/**
	 * 积分商品兑换成功，数据写入
	 */
	boolean createUserIntegralRecord(String sourceId,boolean closeConn,IntegralRecordOfUser ofUser);
	
	/**
	 * 官方活动购买量修改
	 */
	boolean updateActiveGetNumber(String sourceId,boolean closeConn,Integer getNumber,String activityUUID);
	
	/**
	 * 积分商城播报信息获取
	 */
	List<IntegralGoodOrder> getAnnounceMsg(String sourceId,boolean closeConn);
	
	

}
