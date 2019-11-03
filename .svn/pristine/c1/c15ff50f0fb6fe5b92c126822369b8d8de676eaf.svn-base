package com.xryb.zhtc.advice;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xryb.cache.service.IJedisClient;
import com.xryb.cache.service.impl.JedisClientFastMap;
import com.xryb.zhtc.entity.Integral;
import com.xryb.zhtc.entity.IntegralRecordOfUser;
import com.xryb.zhtc.entity.IntegralRule;
import com.xryb.zhtc.entity.Order;
import com.xryb.zhtc.entity.OrderItem;
import com.xryb.zhtc.service.ICouponService;
import com.xryb.zhtc.service.IIntegralService;
import com.xryb.zhtc.service.IVipService;
import com.xryb.zhtc.service.impl.CouponServiceImpl;
import com.xryb.zhtc.service.impl.IntegralServiceImpl;
import com.xryb.zhtc.service.impl.VipServiceImpl;
import com.xryb.zhtc.util.DateTimeUtil;

import spark.annotation.Auto;

public class RechargeOtherAdvice implements IOrderAdvice {
	@Auto(name=JedisClientFastMap.class)
	private IJedisClient jedisClient;
	@Auto(name=CouponServiceImpl.class)
	private ICouponService couponService;
	@Auto(name=IntegralServiceImpl.class)
	private IIntegralService integralService;
	@Auto(name=VipServiceImpl.class)
	private IVipService vipService;
	
	@Override
	public boolean genAdvice(String sourceId, boolean closeConn, Order order, List<OrderItem> itemList)
			throws Exception {
		System.out.println("其他充值订单生成成功");
		return true;
	}

	@Override
	public boolean payAdvice(String sourceId, boolean closeConn, List<Order> orderList, List<OrderItem> itemList)
			throws Exception {
		System.out.println("其他充值支付成功给予一定的用户积分");
		//支付成功后按充值积分规则给予用户一定的积分
		for (Order order : orderList) {
			Integer payment = order.getPayment();//订单充值金额
			//1.获取用户积分
			String vipUUID = order.getPayerUUID();
			Map<String,Object> map = integralService.getUserIntegral(sourceId, closeConn, vipUUID);
			Integer userIntegral  = null;
			if(map == null) {
				//在积分表中创建该用户的积分，初值为0
				Integral integral = new Integral();
				integral.setVipUUID(vipUUID);
				integral.setIntegral(0);
				if(!integralService.addUserIntegral(sourceId, closeConn, integral)) {
					return false;
				}
				userIntegral = 0;
			}
			userIntegral  = (Integer) map.get("integral");
			
			//根据充值积分规则计算用户所得积分
			//2.获取对应类型的积分规则：1-账户充值；2-购买商品，并判断用户购买金额满足那种梯度计算规则
			List<IntegralRule> ruleList = integralService.findIntegralRuleListByType(sourceId, closeConn, 1);
			if (ruleList.size() == 0) {//没有对应的规则，不执行积分给予计算
				return true;
			}
			
			//计算用户所得积分
			Integer integration = 0;
			for (IntegralRule integralRule : ruleList) {
				Double gradMin1 = Double.parseDouble(integralRule.getGradMin())*100;//最小值，单位分
				Integer gradMin = gradMin1.intValue();
				Double gradMax1 = Double.parseDouble(integralRule.getGradMax())*100;//最大值
				Integer gradMax = gradMax1.intValue();
				if (payment >= gradMin && payment < gradMax ) {
					Double rule = Double.parseDouble(integralRule.getCalculateRule());
					Long integral = (long) (payment * rule);
					integration = integral.intValue();
					break;
				}
			}
			//type 1-积分消耗，2-获得积分
			userIntegral += integration;
			
			//4.执行修改
			if(!integralService.updateUserIntegral(sourceId, closeConn, userIntegral, vipUUID)) {
				return false;
			}
			
			//积分详情记录
			IntegralRecordOfUser recordOfUser = new IntegralRecordOfUser();
			recordOfUser.setVipUUID(vipUUID);
			recordOfUser.setIntegration(integration);
			recordOfUser.setMoneyOrIntegral(integration+"");
			recordOfUser.setIntegralFrom(1);//1-账户充值，2-购买商品，3-积分兑换商品
			recordOfUser.setIntegralType(1);//;1-赠送积分，2-消耗积分
			String createTime = DateTimeUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
			recordOfUser.setChangeTime(createTime);
			if(!integralService.dataAccess(sourceId, closeConn, recordOfUser)) {
				return false;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public boolean abolishAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean timeoutAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delAdvice(String sourceId, boolean closeConn, Order order) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
