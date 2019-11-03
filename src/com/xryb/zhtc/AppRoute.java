package com.xryb.zhtc;

import com.xryb.zhtc.action.AccountAction;
import com.xryb.zhtc.action.AddrAction;
import com.xryb.zhtc.action.BaseDictAction;
import com.xryb.zhtc.action.BusinessAction;
import com.xryb.zhtc.action.CartAction;
import com.xryb.zhtc.action.CommodityAction;
import com.xryb.zhtc.action.CouponAction;
import com.xryb.zhtc.action.GxAction;
import com.xryb.zhtc.action.IntegralAction;
import com.xryb.zhtc.action.ItemAction;
import com.xryb.zhtc.action.ItemCatAction;
import com.xryb.zhtc.action.ItemModelAction;
import com.xryb.zhtc.action.MenuAction;
import com.xryb.zhtc.action.MenuRoleAction;
import com.xryb.zhtc.action.MenuUserAction;
import com.xryb.zhtc.action.OrderAction;
import com.xryb.zhtc.action.RoleAction;
import com.xryb.zhtc.action.RoleUserAction;
import com.xryb.zhtc.action.SupplyOrderAction;
import com.xryb.zhtc.action.SystemAction;
import com.xryb.zhtc.action.UserAction;
import com.xryb.zhtc.action.VipAction;
import com.xryb.zhtc.action.WeiXinPayAction;

import spark.annotation.Auto;
import spark.servlet.ISparkApplication;

public class AppRoute implements ISparkApplication {

	@Auto(name = MenuAction.class)
	private MenuAction menuAction;
	
	@Auto(name=MenuRoleAction.class)
	private MenuRoleAction menuRoleAction;

	@Auto(name = MenuUserAction.class)
	private MenuUserAction menuUserAction;

	@Auto(name = RoleAction.class)
	private RoleAction roleAction;

	@Auto(name = RoleUserAction.class)
	private RoleUserAction roleUserAction;

	@Auto(name = UserAction.class)
	private UserAction userAction;

	@Auto(name = VipAction.class)
	private VipAction vipAction;
	
	@Auto(name = BaseDictAction.class)
	private BaseDictAction dictAction;
	
	@Auto(name = SystemAction.class)
	private SystemAction sysAction;

	// ---智跑商城begin---
	/**
	 * 商品分类
	 */
	@Auto(name = ItemCatAction.class)
	private ItemCatAction catAction;
	/**
	 * 商品模板
	 */
	@Auto(name = ItemModelAction.class)
	private ItemModelAction modelAction;
	/**
	 * 商品
	 */
	@Auto(name = ItemAction.class)
	private ItemAction itemAction;
	/**
	 * 购物车
	 */
	@Auto(name = CartAction.class)
	private CartAction cartAction;
	/**
	 * 订单
	 */
	@Auto(name = OrderAction.class)
	private OrderAction orderAction;
	/**
	 * 地址
	 */
	@Auto(name = AddrAction.class)
	private AddrAction addrAction;
	/**
	 * 微信支付
	 */
	@Auto(name = WeiXinPayAction.class)
	private WeiXinPayAction weiXinPayAction;
	/**
	 * 账户
	 */
	@Auto(name = AccountAction.class)
	private AccountAction accountAction;
	//---智跑商城end---
	
	/**
	 * 优惠劵
	 */
	@Auto(name=CouponAction.class)
	private CouponAction couponAction;
	/**
	 * 积分
	 */
	@Auto(name=IntegralAction.class)
	private IntegralAction integralAction;
	
	/**
	 * 商家
	 */
	@Auto(name = BusinessAction.class)
	private BusinessAction businessAction;
	
	/**
	 * 供需订单管理
	 */
	@Auto(name = SupplyOrderAction.class)
	private SupplyOrderAction supplyOrderAction;
	
	/**
	 * 供需发布商品分类
	 */
	@Auto(name = GxAction.class)
	private GxAction gxAction;
	
	/**
	 * 供需管理
	 */
	@Auto(name = CommodityAction.class)
	private CommodityAction commodityAction;
	
	@Override
	public void run() {
		menuAction.run();
		menuRoleAction.run();
		menuUserAction.run();
		roleAction.run();
		roleUserAction.run();
		userAction.run();
		vipAction.run();
		dictAction.run();
		sysAction.run();
		//---智跑商城begin---
		catAction.run();
		modelAction.run();
		itemAction.run();
		cartAction.run();
		orderAction.run();
		addrAction.run();
		weiXinPayAction.run();
		accountAction.run();
		//---智跑商城end---
		//优惠劵
		couponAction.run();
		//积分
		integralAction.run();
		//商家
		businessAction.run();
		//供需管理
		supplyOrderAction.run();
		gxAction.run();
		commodityAction.run();
	}

}
