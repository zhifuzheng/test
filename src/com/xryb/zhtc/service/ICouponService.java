package com.xryb.zhtc.service;

import java.util.List;
import java.util.Map;

import com.xryb.zhtc.entity.BusinessApply;
import com.xryb.zhtc.entity.Coupon;
import com.xryb.zhtc.entity.CouponOfIssuer;
import com.xryb.zhtc.entity.CouponOfUser;
import com.xryb.zhtc.entity.CouponOffline;
import com.xryb.zhtc.entity.CouponUserList;
import com.xryb.zhtc.entity.Item;
import com.xryb.zhtc.entity.ItemCat;
import com.xryb.zhtc.entity.ItemParam;

import dbengine.util.Page;

/**
 * 卡劵持久层接口
 * @author apple
 */
public interface ICouponService {
	/**
	 * 优惠劵的生成
	 * @param sourceId 数据源id
	 * @param closeConn 是否关闭连接
	 * @param coupon 优惠劵实体
	 * @return true成功，false失败
	 */
	boolean createCoupon(String sourceId,boolean closeConn,Coupon coupon);//优惠劵生成(线上)
	boolean createCouponOffline(String sourceId,boolean closeConn,CouponOffline couponOffline);//优惠劵生成(线下)
	boolean createCouponMng(String sourceId,boolean closeConn,CouponOfIssuer issuer);//发行商优惠劵管理
	boolean updataCouponInfo(String sourceId,boolean closeConn,CouponOfIssuer issuer);
	BusinessApply getBusinessInfoByUUID(String sourceId,boolean closeConn,String businessUUID);
	
	
	/**
	 * 获取指定批次可下载的二维码图片
	 */
	List<CouponOffline> getIsDownQRcodeImg(String sourceId,boolean closeConn,String batchUUID,Integer downNumber);
	/**
	 * 获取指定批次所有的二维码图片
	 */
	List<CouponOffline> getQRcodeImg(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 获取店铺商所属品列表
	 * @param sourceId 
	 * @param closeConn
	 * @param entityUUID 店铺uuid
	 * @return 商品集合
	 */
	List<Item> getGoodsListByEntityUUID(String sourceId,boolean closeConn,String entityUUID,String catUUID);
	
	/**
	 * 获取发行商的商品分类
	 */
	Map<String,String> getGoodsCatUUIDByBusinessUUID(String sourceId,boolean closeConn,String businessUUID);
	List<ItemCat> getGoodsSortList(String sourceId,boolean closeConn,String parentUUID );
	
	/**
	 * 获取发行商发行的优惠劵列表
	 */
	List<CouponOfIssuer> findCouponList(String sourceId,boolean closeConn,Map<String,Object> param,Page page);
	List<CouponOfIssuer> platformFindCouponList(String sourceId,boolean closeConn,Map param,Page page);
	/**
	 * 获取用户领取的优惠劵列表
	 */
	List<CouponUserList> findUserCouponList(String sourceId,boolean closeConn,Map param,Page page);
	
	
	/**
	 * 发行商删除优惠劵
	 */
	boolean deleteCouponByBatchUUID(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 个人用户删除优惠劵
	 */
	boolean deleteCouponByCouponUUID(String sourceId,boolean closeConn,String[] couponUUID);
	
	/**
	 * 获取优惠劵的相关信息
	 */
	CouponOfIssuer findCouponInfoByBatchUUID(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 上架优惠劵
	 */
	boolean putawayCoupon(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 下架优惠劵
	 */
	boolean downCoupon(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 查询用户领取过指定优惠劵的数量
	 */
	Map getNumsCouponOfUser(String sourceId,boolean closeConn,String vipUUID,String batchUUID);
	
	/**
	 * 分配优惠劵给用户
	 */
	boolean allotCouponToUser(String sourceId,boolean closeConn,CouponOfUser user);
	
	/**
	 * 获取未领取的优惠劵uuid
	 */
	List<Coupon> getCouponUUIDByBatchUUID(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 用户领取成功之后，修改该优惠劵为已领取(isGet-2)
	 */
	boolean upCouponIsGet(String sourceId,boolean closeConn,String couponUUID);
	/**
	 *修改该优惠劵为未领取(isGet-1)
	 */
	boolean upCouponIsNoGet(String sourceId,boolean closeConn,String couponUUID);
	
	/**
	 *修改该优惠劵为已使用(isUse:1-未使用，2-已使用，3-已过期)
	 */
	boolean updateIsUse(String sourceId,boolean closeConn,String couponUUID,String orderUUID);
	
	/**
	 *修改该优惠劵为未使用(isUse:1-未使用，2-已使用，3-已过期)
	 */
	boolean updateIsNotUse(String sourceId,boolean closeConn,String[] couponUUID);
	
	/**
	 * 修改失败删除数据
	 */
	boolean delChangeData(String sourceId,boolean closeConn,String couponUUID);
	
	/**
	 * 获取优惠劵面值/折扣，优惠劵类型
	 */
	Map<String,Object> getSomeInfoCoupon(String sourceId,boolean closeConn,String couponUUID);
	
	/**
	 * 获取指定商品信息
	 */
	ItemParam getGoodInfo(String sourceId,boolean closeConn,String goodUUID,String paramUUID);
	Item getGoodInfo(String sourceId,boolean closeConn,String goodUUID);
	
	/**
	 * 获取店铺可领取的优惠劵列表
	 */
	List<CouponUserList> findCouponIsUsed(String sourceId,boolean closeConn,String merchantUUID);
	
	/**
	 * 获取平台可领取的优惠劵列表
	 */
	List<CouponUserList> platformFindCouponIsUsed(String sourceId,boolean closeConn,String merchantUUID);
	
	/**
	 * 查询优惠劵的领取量，库存
	 */
	CouponOfIssuer findCouponInfo(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 修改优惠劵的领取量，库存
	 */
	boolean updateCouponGetNumsAndStock(String sourceId,boolean closeConn,String batchUUID,Integer stock,Integer getNumbers);
	
	/**
	 * 修改优惠劵的使用量
	 */
	boolean updateCouponUsedNumbers(String sourceId,boolean closeConn,String batchUUID,Integer getNumbers);
	
	/**
	 * 手机端获取用户优惠劵列表
	 */
	List<CouponUserList> findUserCouponListMobile(String sourceId,boolean closeConn,String vipUUID,Page page);
	
	/**
	 * 获取用户领取的对应店铺的优惠劵列表
	 */
	List<CouponUserList> findUserEntityCouponList(String sourceId,boolean closeConn,String vipUUID,String entityUUID,String itemUUIDArr);
	
	/**
	 * 根据购物所有商品的uuid获取对应的优惠劵
	 */
	List<CouponUserList> findUserIsUsedCouponList(String sourceId,boolean closeConn,String vipUUID,List<String> entityUUIDArr);
	
	/**
	 * 根据购物所有商品的uuid获取对应的优惠劵
	 */
	List<CouponUserList> findUserIsUsedPlatformCouponList(String sourceId,boolean closeConn,String vipUUID);
	
	/**
	 * 获取指定分类信息列表
	 */
	List<ItemCat> findSortInfoList(String sourceId,boolean closeConn,String catUUID);
	
	/**
	 * 获取指定商品信息列表
	 */
	List<ItemParam> findGoodsInfoList(String sourceId,boolean closeConn,String goodsUUID);
	
	/**
	 * 删除批量生成的优惠劵（线上）
	 */
	boolean delPreCoupon(String sourceId,boolean closeConn,String batchUUID);
	/**
	 * 删除批量生成的优惠劵（线下）
	 */
	boolean delPreCouponOffline(String sourceId,boolean closeConn,String batchUUID);
	
	/**
	 * 获取店铺信息
	 */
	List<BusinessApply> getBusinessInfo(String sourceId,boolean closeConn,String vipUUID);
	
	List<BusinessApply> getAllBusinessInfo(String sourceId,boolean closeConn);//平台所有店铺信息
	
	
	/**
	 * 获取线上优惠劵使用详情记录
	 */
	List<CouponUserList> getOnLineCouponList(String sourceId,boolean closeConn,String batchUUID,Page page);
	
	/**
	 * 获取线下优惠劵使用详情记录
	 */
	List<CouponUserList> getOffLineCouponList(String sourceId,boolean closeConn,String batchUUID,Page page);
	
	/**
	 * 根据优惠劵couponUUID获取优惠劵信息
	 */
	CouponOfIssuer findCouponInfoByCouponUUID(String sourceId,boolean closeConn,String couponUUID);
	
	/**
	 * 订单取消或者超时，返还休会劵给用户
	 */
	boolean returnCouponToUser(String sourceId,boolean closeConn,String couponUUID);
	/**
	 * 获取零售店铺列表
	 */
	List<BusinessApply> findBusinessApplyList(String sourceId,boolean closeConn);
	/**
	 * 获取指定零售店铺信息
	 */
	List<BusinessApply> findBusinessApplyListByBusinessUUID(String sourceId,boolean closeConn,String businessUUID);
	BusinessApply findBusinessInfo(String sourceId,boolean closeConn,String businessUUID);
	
	
	/**
	 * 获取分类信息
	 */
	ItemCat findItemCatInfo(String sourceId,boolean closeConn,String catUUID);
	
	/**
	 * 领券中心获取可领取优惠劵列表
	 */
	List<CouponUserList> findAllCouponList(String sourceId,boolean closeConn,Page page);
	
	/**
	 * 获取店铺商品信息
	 */
	List<Item> findEntityGoodInfo(String sourceId,boolean closeConn,String businessUUID,String catUUID,String goodUUID,Integer nubmber);
	
	/**
	 * 卡劵立即使用跳转获取商品列表
	 */
	List<Item>  findGoodsCouponList(String sourceId,boolean closeConn,Page page,Map<String, String> param);
	
	/**
	 * 获取线下优惠劵信息
	 */
	CouponOffline findCouponOfflineInfo(String sourceId,boolean closeConn,String couponUUID);
	CouponOfIssuer findCouponOfflineIssuerInfo(String sourceId,boolean closeConn,String couponUUID);
	
	/**
	 * 线下优惠劵数据绑定
	 */
	boolean updateOfflineCoupon(String sourceId,boolean closeConn,Map<String,Object> params);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
