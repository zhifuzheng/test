package com.xryb.zhtc.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.xryb.zhtc.entity.GxCommodity;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.SupplyCommodityComment;
import com.xryb.zhtc.entity.SupplyOrder;
import com.xryb.zhtc.entity.SupplyUserAddress;
import com.xryb.zhtc.service.ISupplyOrderService;
import com.xryb.zhtc.util.DateTimeUtil;

import dbengine.dao.EntityDao;
import dbengine.dao.SqlDao;
import dbengine.util.Page;
import spark.annotation.Auto;

public class SupplyOrderServiceImpl implements ISupplyOrderService {
	@Auto(name = EntityDao.class)
	private EntityDao entitydao;

	@Auto(name = SqlDao.class)
	private SqlDao sqldao;
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取数据库中所有需要处理超时的订单
	 */
	@Override
	public List<SupplyOrder> findOverTimeOrder(String sourceId, boolean closeConn,String orderStatus) {
		String sql = "select * from supply_order where orderStatus='"+orderStatus+"";
		return (List<SupplyOrder>)sqldao.findListBySql(sourceId, sql, SupplyOrder.class, closeConn, null);
	}

	/**
	 * 获取用户收货地址数量
	 */
	@Override
	public Map<String,Object> findSupplyUserAddrNumbers(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "select count(*) num from supply_address where isDel='0' and vipUUID='"+vipUUID+"'";
		return sqldao.findMapBysql(sourceId, sql, closeConn, null);
	}

	/**
	 * 商家确认发货
	 */
	@Override
	public boolean SellerConfirmDelivery(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update supply_address set isPay='1',orderStatus='1' where orderUUID='"+orderUUID+"'";
		return false;
	}

	/**
	 * 获取订单商品评论列表
	 */
	@Override
	public List<SupplyCommodityComment> findSupplyOrderComment(String sourceId, boolean closeConn,Page page,String commodityUUID) {
		String sql = "select * from supply_commodity_comment where status='1' and commodityUUID='"+commodityUUID+"'";
		return (List<SupplyCommodityComment>)sqldao.findPageByMysql(sourceId, sql, closeConn, SupplyCommodityComment.class, page, null);
	}

	/**
	 * 修改订单为已支付
	 */
	@Override
	public boolean updateSupplyOrderIspay(String sourceId, boolean closeConn, String orderUUID) {
		//isPay 订单付款状态：0-未付款，1-已付款，默认值为0
		//orderStatus 订单状态：0-待付款，1-已付款，未发货，2-已发货，未签收，3-已签收，未评价，交易完成，4-已评价，
		//-1-交易取消，-2-超时关闭，-3-待退款，-4-同意退款，-5驳回退款，-6待退货，-7同意退货，-8驳回退货，9无可退货商品，默认值为0
		String payTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sql = "update supply_address set isPay='1',orderStatus='1',payTime='"+payTime+"' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 删除订单
	 */
	@Override
	public boolean deleteSupplyAddress(String sourceId, boolean closeConn, String addrUUID) {
		String sql = "update supply_address set isDel='1' where addrUUID='"+addrUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 修改用户所有可用收货地址为非默认收货地址
	 */
	@Override
	public boolean updateAllAddressNotDefault(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "update supply_address set isDefault='0' where isDel='0' and vipUUID='"+vipUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 获取用户默认收货地址信息
	 */
	@Override
	public SupplyUserAddress findUserDefaultAddress(String sourceId, boolean closeConn, String vipUUID) {
		String sql = "select * from supply_address where isDel='0' and isDefault='1' and vipUUID='"+vipUUID+"'";
		return (SupplyUserAddress) sqldao.findEntityBySql(sourceId, sql, SupplyUserAddress.class, closeConn, null);
	}

	/**
	 * 新增或编辑收货地址
	 */
	@Override
	public boolean saveOrUpdateAddr(String sourceId, boolean closeConn, SupplyUserAddress address) {
		if(address.getId() == null) {//新增
			address.setCreateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			return entitydao.saveEntity(sourceId, address, closeConn);
		}else {//编辑修改
			address.setUpdateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			return entitydao.updateEntity(sourceId, address, closeConn);
		}
	}

	/**
	 * 获取用户收货地址列表
	 */
	@Override
	public List<SupplyUserAddress> findSupplyUserAddressList(String sourceId, boolean closeConn,Page page, String vipUUID) {
		String sql = "select * from supply_address where isDel='0' and vipUUID='"+vipUUID+"'";
		return (List<SupplyUserAddress>)sqldao.findPageByMysql(sourceId, sql, closeConn, SupplyUserAddress.class,page, null);
	}

	/**
	 * 生成新订单order
	 */
	@Override
	public boolean createVipOrder(String sourceId, boolean closeConn, Order order) {
		return entitydao.saveEntity(sourceId, order, closeConn);
	}
	

	/**
	 * 评论提交成功，订单状态修改
	 */
	@Override
	public boolean updateSupplyOrderSate(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "update supply_order set orderStatus='4' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 评论提交
	 */
	@Override
	public boolean submitComment(String sourceId, boolean closeConn, SupplyCommodityComment comment) {
		return entitydao.saveEntity(sourceId, comment, closeConn);
	}

	/**
	 * 确认收货
	 */
	@Override
	public boolean confirmReceipt(String sourceId, boolean closeConn, String orderUUID) {
		String nowTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sql = "update supply_order set orderStatus='3',endTime='"+nowTime+"' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 撤销退款退货申请
	 */
	@Override
	public boolean cancelApply(String sourceId, boolean closeConn, SupplyOrder order) {
		String originStatus = order.getOriginStatus();//发起退款退货前的订单状态
		//将发起的退款时间设为空
		//order.setRefundTime("");
		String sql = "update supply_order set orderStatus='"+originStatus+"' where orderUUID='"+order.getOrderUUID()+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 查询订单信息
	 */
	@Override
	public SupplyOrder findOrderInfoByOrderUUID(String sourceId, boolean closeConn, String orderUUID) {
		String sql = "select * from supply_order where orderUUID='"+orderUUID+"'";
		return (SupplyOrder) sqldao.findEntityBySql(sourceId, sql, SupplyOrder.class, closeConn, null);
	}

	/**
	 * 取消订单
	 */
	@Override
	public boolean abolishOrder(String sourceId, boolean closeConn, String orderUUID) {
		String abolishTime = DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		String sql = "update supply_order set orderStatus='-1',abolishTime='"+abolishTime+"' where orderUUID='"+orderUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 修改商品库存
	 */
	@Override
	public boolean updateGoodStock(String sourceId, boolean closeConn, String commodityUUID, Integer stock) {
		String sql = "update gx_commodity set stock='"+stock+"' where commodityUUID='"+commodityUUID+"'";
		return sqldao.executeSql(sourceId, sql, closeConn, null);
	}

	/**
	 * 新增或修改订单
	 */
	@Override
	public boolean saveOrUpdateOrder(String sourceId, boolean closeConn, SupplyOrder order) {
		if (order.getId() == null) {// 新增
			order.setCreateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			return entitydao.saveEntity(sourceId, order, closeConn);
		} else {// 编辑
			order.setUpdateTime(DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			return entitydao.updateEntity(sourceId, order, closeConn);
		}
	}

	/**
	 * 获取供需商品信息
	 */
	@Override
	public GxCommodity findSupplyGoodInfo(String sourceId, boolean closeConn, String commodityUUID) {
		String sql = "select * from gx_commodity where commodityUUID='" + commodityUUID + "'";
		return (GxCommodity) sqldao.findEntityBySql(sourceId, sql, GxCommodity.class, closeConn, null);
	}

	/**
	 * 查看用户买到的订单信息列表
	 */
	@Override
	public List<SupplyOrder> findUserBuySupplyOrderList(String sourceId, boolean closeConn, Page page, String vipUUID,String type) {
		// 会员订单删除状态：1-未删除，2-已删除，默认值为1
		String sql = "select * from supply_order where payerIsDel='1' and payerUUID='" + vipUUID + "'";
		StringBuilder sqlb = new StringBuilder(sql);
		//待支付:下单成功，未支付
		// type: 0-全部，1-待支付，2-待发货，3-待收货，4-待评价，5-退款/退货
		// orderStatus: 订单状态：0-待付款，1-已付款，未发货，2-已发货，未签收，3-已签收，未评价，交易完成，4-已评价，
		//-1-交易取消，-2-超时关闭，-3-待退款，-4-同意退款，-5驳回退款，-6待退货，-7同意退货，-8驳回退货，-9无可退货商品，默认值为0
		if ("0".equals(type)) {
			sqlb.append(" and orderStatus <> '-1' and orderStatus <> '-2' ");
		}
		
		if ("1".equals(type)) {
			sqlb.append(" and orderStatus='0'");
		}

		// 待发货:订单已支付，卖家未发货
		if ("2".equals(type)) {
			sqlb.append(" and orderStatus='1'");
		}

		// 待收货：买家已付款，卖家确认发货，买家未收货
		if ("3".equals(type)) {
			sqlb.append(" and orderStatus='2'");
		}

		// 待评价：买家已收货，未评价
		if ("4".equals(type)) {
			sqlb.append(" and orderStatus='3'");
		}

		// 退款退货
		if ("5".equals(type)) {
			sqlb.append(" and orderStatus='-3' or orderStatus='-4' or orderStatus='-5' or orderStatus='-6' or orderStatus='-7' or orderStatus='-8' or orderStatus='-9'");
		}
		
		return (List<SupplyOrder>) sqldao.findPageByMysql(sourceId, sqlb.toString(), closeConn, SupplyOrder.class, page, null);
	}

	/**
	 * 查询用户卖出的订单信息列表
	 */
	@Override
	public List<SupplyOrder> findUserSellSupplyOrderList(String sourceId, boolean closeConn, Page page, String vipUUID,String type) {
		String sql = "select * from supply_order where sellerIsDel='1' and sellerUUID='" + vipUUID + "'";
		StringBuilder sqlb = new StringBuilder(sql);
		// type: 0-全部，1-待支付，2-待发货，3-待收货，4-待评价，5-退款/退货
		// orderStatus: 订单状态：0-待付款，1-已付款，未发货，2-已发货，未签收，3-已签收，未评价，交易完成，4-已评价，
		//-1-交易取消，-2-超时关闭，-3-待退款，-4-同意退款，-5驳回退款，-6待退货，-7同意退货，-8驳回退货，-9无可退货商品，默认值为0
		
		if ("0".equals(type)) {
			sqlb.append(" and orderStatus <> '-1' and orderStatus <> '-2' ");
		}
		
		//待支付:下单成功，买家未支付
		if ("1".equals(type)) {
			sqlb.append(" and orderStatus='0'");
		} 
		
		//待发货:订单已支付，卖家未发货
		if ("2".equals(type)) {
			sqlb.append(" and orderStatus='1'");
		} 
		
		//待收货：买家已付款，卖家确认发货，买家未收货
		if ("3".equals(type)) {
			sqlb.append(" and orderStatus='2'");
		} 
		
		//待评价：买家已收货，未评价
		if ("4".equals(type)) {
			sqlb.append(" and orderStatus='3'");
		} 
		
		//退款：退款退货
		if ("5".equals(type)) {
			sqlb.append(" and orderStatus='-3' or orderStatus='-4' or orderStatus='-5' or orderStatus='-6' or orderStatus='-7' or orderStatus='-8' or orderStatus='-9'");
		}

		return (List<SupplyOrder>) sqldao.findPageByMysql(sourceId, sqlb.toString(), closeConn, SupplyOrder.class, page, null);
	}

}
