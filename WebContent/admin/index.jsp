<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xryb.zhtc.entity.VipInfo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String userUUID = (String)request.getSession().getAttribute("userUUID");
	String userName = (String)request.getSession().getAttribute("userName");
	VipInfo vipinfo = (VipInfo)request.getSession().getAttribute("vipinfo");
	String openId = null;
	if(userName == null){
		userName = vipinfo.getVipName();
		openId = vipinfo.getOpenId();
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>优好采购-管理平台</title>
<link href="../static/jeasyui-plus/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/easyui_animation.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/easyui_plus.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/insdep_theme_default.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/icon.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/easyui.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/themes/insdep/easyui_animation.css" rel="stylesheet" type="text/css">
<link href="../static/jeasyui-plus/plugin/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
 <!-- 
<link rel="stylesheet" href="../static/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
-->
<link rel="stylesheet" href="../static/zTree/css/metroStyle/metroStyle.css" type="text/css">

<link rel="stylesheet" href="../static/third/easyui-extlib/css/jeasyui.extensions.toolbar.css"/>
<link rel="stylesheet" type="text/css" href="../static/kindeditor/themes/default/default.css">
<link rel="stylesheet" type="text/css" href="coupon/layer/default/layer.css">
<style>
.accordion-header-selected
 			.accordion 
 			.accordion-collapse:link,.accordion 
 			.accordion-collapse:visited,.accordion 
 			.accordion-collapse:hover,.accordion .accordion-collapse:active,.accordion .accordion-collapse:focus,.accordion .accordion-expand:link,.accordion .accordion-expand:visited,.accordion .accordion-expand:hover,.accordion .accordion-expand:active,.accordion .accordion-expand:focus {
	background-color: transparent !important;
}

.datalist .datagrid-cell,.m-list li {
	padding-left: 30px
}

.label-top {
	margin: 0 0 10px;
	display: block;
}
.l-btn-plain {
	background: transparent;
	border: 1px solid #00000038 !important;
	filter: none;
}
</style>
<script type="text/javascript">
	/*
	<c:if test="${sessionScope.userinfo == null}">
		alert('登录已过期，请重新登录！');
		window.location = 'login.jsp';
	</c:if>
	 */
</script>
	<script type="text/javascript" src="../static/jeasyui-plus/jquery.min.js"></script>
	<script type="text/javascript" src="../static/jeasyui-plus/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../static/js/jquery-form.js"></script>
	<!-- <script type="text/javascript" src="../static/kindeditor/kindeditor-all-min.js"></script> -->
	<script type="text/javascript" src="../static/kindeditor/kindeditor-all.js"></script>
	<script type="text/javascript" src="../static/kindeditor/lang/zh-CN.js"></script>
	<script type="text/javascript" src="../static/js/shop.js"></script>
	<script type="text/javascript" src="coupon/layer/layer.js"></script>
	<script type="text/javascript" src="../static/js/jquery.select.js"></script>
	
	<script type="text/javascript">
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/jeasyui-plus/themes/insdep/jquery.insdep-extend.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<script type="text/javascript">
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/js/common-tab.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<!-- ztree -->
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/zTree/js/jquery.ztree.core-3.5.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/zTree/js/jquery.ztree.excheck-3.5.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/zTree/js/jquery.ztree.exedit-3.5.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/js/common.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>

	<script>
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/third/justgage-1.2.2/raphael-2.1.4.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/third/justgage-1.2.2/justgage.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/third/Highcharts-5.0.0/js/highcharts.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<script >
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/third/My97DatePicker/WdatePicker.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
	<link rel="stylesheet" href="../static/third/cropper-2.3.4/dist/cropper.min.css" type="text/css"/>
	<script>
	(function() {
	     var s = document.createElement('script');
	     s.type = 'text/javascript';
	     s.async = true;
	     s.src = '<%=path%>/static/third/cropper-2.3.4/dist/cropper.min.js';
	     var x = document.getElementsByTagName('script')[0];
	     x.parentNode.insertBefore(s, x);
	 })();
	</script>
</head>
<body class="easyui-layout" id="master-layout" data-options="fit:true,scroll:'no'">
	<div data-options="region:'north',border:false" style="height: 70px; background: rgba(255, 0, 0, .7); padding: 10px; margin-bottom: 5px; overflow-y: hidden;">
		<img src="../static/images/logo.png" style="width:66px;height: auto;margin-top: -9px;" align="absmiddle" />
		<span style="color: white; font-size: 30px; margin-top:12px;">&nbsp;&nbsp;优好采购管理平台</span>
		<div style="width:670px;height:70px;margin-top:-67px;margin-left: 460px;">	        
	        <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng') > -1}">
	          	<a href="#" style="height:70px;" class="easyui-menubutton" data-options="menu:'#accountmng_topmenu'"><font size="3" color="white">资金管理</font></a>
	        </c:if>
	         <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.withdrawalmng') > -1}">
	          	<a href="#" style="height:70px;" class="easyui-menubutton" data-options="menu:'#withdrawalmng_topmenu'"><font size="3" color="white">提现审核</font></a>
	        </c:if>
	       <%--  <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.licensemng') > -1 }">
	            <a href="#" style="height:70px;" class="easyui-menubutton" data-options="menu:'#licensemng_topmenu'"><font size="3" color="white">认证管理</font></a>
	        </c:if> --%>
	        <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng') > -1 }">
	          	<a href="#" style="height:70px;" class="easyui-menubutton" data-options="menu:'#sysmng_topmenu'"><font size="3" color="white">系统管理</font></a>
	        </c:if>	        
	    </div>
		<div style="float:right; padding-right: 10px;margin-top:-50px;">
		    <table>
			    <tr>
			      <td>		   
			        <div>						
						<span style="color:#fff;">欢迎&nbsp;&nbsp;</span>
						 <i class="fa fa-user" style="color: #fff; line-height: 40px; font-size: 20px;"></i> 
						 <a style="color:#fff;"><span ><%=userName %></span></a>
					</div>
			      </td>
			      <td>
			        <div>
			         <span style="color:#fff;"> &nbsp;|&nbsp;</span>
			          <i class="fa fa-lock" style="color: pink; line-height: 40px; font-size: 20px;"></i>
			          <a href="javascript:upPwd();" style="color:pink;">修改密码</a>
			        </div>
			      </td>
			      <td>
			      	<div>
			          <span style="color:#fff;"> &nbsp;|&nbsp;</span>
			          <i class="fa fa-sign-out" style="color: #ffa; line-height: 40px; font-size: 20px;"></i>
			          <a href="../admin/logout.jsp" style="color:#ffa;">退出</a>
			        </div>
			      </td>
			      <td></td>
			    </tr>
		    </table>
		</div>
	</div>
	
  	<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng') > -1}">
		<div id="accountmng_topmenu" style="width:100px;">
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.accountList') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('account/accountList.jsp')">账户管理</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.accountLog') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('account/accountLogList.jsp')">账户日志</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.accountLog') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('account/setting.jsp')">提现设置</a></div>
			</c:if>
		</div>
	</c:if>
	<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.withdrawalmng') > -1}">
		<div id="withdrawalmng_topmenu" style="width:150px;">
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.withdrawal') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('withdrawal/withdrawal.jsp')">分销商提现审核</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.withdrawal') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('withdrawal/retailWithdrawal.jsp')">零售商提现审核</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.accountmng.withdrawal') > -1}">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('withdrawal/wholesaleWithdrawal.jsp')">批发商提现审核</a></div>
			</c:if>
		</div>
	</c:if>
	<%-- <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.licensemng') > -1 }">
		<div id="licensemng_topmenu" style="width:100px;">
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.licensemng.realNameMng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('audit/realNameList.jsp')">实名认证审核</a></div>
			</c:if>
		</div>
	</c:if> --%>
    <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng') > -1 }">
		<div id="sysmng_topmenu" style="width:100px;">
	    	<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.usermng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('user/userList.jsp')">用户管理</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.menumng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('menu/menuMng.jsp')">权限管理</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.rolemng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('role/roleMng.jsp')">角色管理</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.dictmng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('dict/dictList.jsp')">数据字典</a></div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.propertiesmng') > -1 }">
				<div><a href="javascript:void(0);" onclick="loadCenterLayout('properties/propertiesList.jsp')">配置文件</a></div>
			</c:if>
	    </div>
    </c:if>
	<div data-options="region:'west',split:true,title:'导航菜单'" style="width: 200px;">
		<div class="easyui-accordion" data-options="border:false">
		    
		    <c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng') != -1}">
				<div title="商城管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.itemCatMng') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/itemCatList.jsp')">分类与模板管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.itemMng') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/itemMng.jsp')">零售商商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.itemMng') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/wholesaleItemMng.jsp')">批发商商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.wholesaleItemAdd') > -1 }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/platItemAdd.jsp')">零售商商品发布</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.itemAdd') > -1 }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/platWholesaleItemAdd.jsp')">批发商商品发布</a></li>
						</c:if>
						<c:if test="${sessionScope.vipinfo != null }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/itemList.jsp')">零售商商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.vipinfo != null }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/wholesaleItemList.jsp')">批发商商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.vipinfo != null }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/itemAdd.jsp')">零售商商品发布</a></li>
						</c:if>
						<c:if test="${sessionScope.vipinfo != null }">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/wholesaleItemAdd.jsp')">批发商商品发布</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			
			<!-- 订单管理 -->
			<div title="订单管理" data-options="selected:true">
				<ul class="easyui-datalist" data-options="border:false">
					<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.shopmng.orderMng') > -1}">
						<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/orderMng.jsp')">订单管理</a></li>
					</c:if>
					<c:if test="${sessionScope.vipinfo != null}">
						<li><a href="javascript:void(0);" onclick="loadCenterLayout('shop/orderList.jsp')">订单管理</a></li>
					</c:if>
				</ul>
			</div>
			
			<!-- 评论管理 -->
			<c:if test="${sessionScope.userType == 0}">
				<div title="评论管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('comment/comment.jsp')">评论管理</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			
			<!-- 分销管理 -->
			<c:if test="${sessionScope.userType == 0}">
				<div title="分销管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('distribute/distributeList.jsp')">分销人员</a></li>
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('distribute/setting.jsp')">分销设置</a></li> 
						</c:if>
					</ul>
				</div>
			</c:if>
			
			<!-- 会员管理 -->
			<c:if test="${sessionScope.userType == 0}">
				<div title="会员管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.sysmng.vipmng') > -1 }">
							<div><a href="javascript:void(0);" onclick="loadCenterLayout('vip/vipList.jsp')">会员管理</a></div>
						</c:if>
					</ul>
				</div>
			</c:if>
			
			<!-- 营销管理 -->
			<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null }">
				<div title="卡劵管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/createCoupon.jsp')">商家优惠劵创建</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/couponManage.jsp')">商家优惠劵管理</a></li>
						</c:if>
						<%-- <c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/entityCouponUseDetail.jsp')">商家劵使用详情</a></li>
						</c:if> --%>
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/couponGetRecode.jsp')">我的领劵记录</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/platformCreateCoupon.jsp')">平台优惠劵创建</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/platformCouponManage.jsp')">平台优惠劵管理</a></li>
						</c:if>
						<%-- <c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('coupon/platformCouponUseDetail.jsp')">平台劵使用详情</a></li>
						</c:if> --%>
					</ul>
				</div>
			</c:if>
			
			<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null }">
				<div title="积分管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integral/integralDetailRecode.jsp')">会员积分详情记录</a></li>
						</c:if>
						
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integral/integralGoodsExRecord.jsp')">会员积分商品兑换记录</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integral/integralRuleManage.jsp')">平台积分规则管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integral/platforIntegralDetailRecode.jsp')">平台积分详情记录</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 }"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integral/platformIntegralGoodsExRecord.jsp')">平台积分商品兑换记录</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			<c:if test="${sessionScope.userType == 0 }">
				<div title="积分商城管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integralshop/integralGoodsAdd.jsp')">积分商城商品添加</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integralshop/integralGoodsList.jsp')">积分商城商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integralshop/activeGoodAdd.jsp')">官方优惠活动添加</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.vipinfo != null}"> 
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('integralshop/activeGoodManage.jsp')">官方优惠活动管理</a></li>
						</c:if>
						
					</ul>
				</div>
			</c:if>
			
			
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business') != -1}">
				<div title="零售商供应商" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.business_save') != -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/business_save.jsp')">商户入驻</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.my_business_list') != -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/my_business_list.jsp')">我的商户</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.approval_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/approval_list.jsp')">商户审批</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.administration_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/administration_list.jsp')">商户管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.settled_money_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/settled_money/settled_money_list.jsp')">商家续费设置</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.wholesale_market_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('business/wholesale_market/wholesale_market_list.jsp')">批发市场</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business.grade') > -1 }">
							<div><a href="javascript:void(0);" onclick="loadCenterLayout('business/grade/grade_list.jsp')">等级管理</a></div>
						</c:if>
					</ul>
				</div>
			</c:if>
			
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.img') > -1}">
				<div title="幻灯片" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.img.list') != -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('img/slide_list.jsp')">幻灯片设置</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
			
			
			<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.business') != -1}">
				<div title="供需管理" data-options="selected:true">
					<ul class="easyui-datalist" data-options="border:false">
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.gx.gx_menu_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('gxmenu/gx_menu_list.jsp')">供需分类</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.gx.gx_report_list') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('gx/gx_report_list.jsp')">供需举报</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.gx.gx_commodity_admin') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('gx/gx_commodity_admin.jsp')">供需商品管理</a></li>
						</c:if>
						<c:if test="${sessionScope.userType == 0 || sessionScope.permissionStr.indexOf('sys.gx.gx_demand_admin') > -1}">
							<li><a href="javascript:void(0);" onclick="loadCenterLayout('gx/gx_demand_admin.jsp')">供需需求管理</a></li>
						</c:if>
					</ul>
				</div>
			</c:if>
		</div>
	</div>
	
	<div data-options="region:'south',border:false" style="height: 40px;">
		<div class="footer">
			<div style="margin: 0px auto; text-align: center; font-family:微软雅黑;">
				建议使用&nbsp; <a href="http://dl.360safe.com/se/360se_setup.exe" target="_blank" style="text-decoration: none;color: blue;">360浏览器极速模式</a>/ <a href="https://www.google.com/intl/zh-CN/chrome/browser/" target="_blank" style="text-decoration: none;color: blue;">Chrome</a>/ <a href="http://firefox.com.cn/download/" target="_blank" style="text-decoration: none;color: blue;">Firefox</a>&nbsp;系列浏览器。&nbsp;<br /> 技术支持： <a href="http://www.gzyhcg.com/" target="_blank" style="text-decoration: none;color: blue;">贵阳西软弈博网络科技有限公司</a>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
	</div>
	<div data-options="region:'center'"></div>
	<script>
		function loadCenterLayout(url) {
			if (url) {
				$('#master-layout').layout('panel', 'center').panel('refresh', url);
			} else {
				$('#master-layout').layout('panel', 'center').panel('refresh');
			}
		}
		//修改密码
		function upPwd(){
			$('<div id="up_pwd">\
				<form>\
					<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">\
						<tr>\
							<td align="right">\
								<label>原密码:</label>\
							</td>\
							<td>\
								<input class="easyui-textbox" type="password" name="oldPwd" style="width:300px"  data-options="required:true" />\
							</td>\
						</tr>\
						<tr>\
							<td align="right">\
								<label>新密码:</label>\
							</td>\
							<td>\
								<input class="easyui-textbox" type="password" name="newPwd" style="width:300px" data-options="required:true" />\
							</td>\
						</tr>\
						<tr>\
							<td align="right">\
								<label>确认新密码:</label>\
							</td>\
							<td>\
								<input class="easyui-textbox" type="password" name="rePwd" style="width:300px" data-options="required:true" />\
							</td>\
						</tr>\
					</table>\
				</form>\
			</div>').css({padding:"5px"}).dialog({
				id : "up_pwd",
	    		width : "450px",
	    		height : "240px",
	    		closable: false,
	    		modal: true,
	    		title : "修改密码",
	    		onOpen : function(){
	    			var _dialog = $(this);
	    			$.parser.parse(_dialog);
	    		},
	    		buttons:[{
					text:'确认',
					iconCls: 'icon-ok',
					handler:function(){
						var form = $('#up_pwd').find('form');
						if(!form.form('validate')){
							$.messager.alert('提示信息','表单还没填完哦，请填完表单后再提交!','warning');
							return;
						}
						var oldPwd = form.find("input[name=oldPwd]").val();
						var newPwd = form.find("input[name=newPwd]").val();
						var rePwd = form.find("input[name=rePwd]").val();
						if(newPwd != rePwd){
							$.messager.alert('提示信息','新密码和确认新密码不一致!','warning');
							return;
						}
						var requestUrl = '<%=basePath %>system/user/upUserPwd';
						var requestData = form.serialize();
						var userUUID = '<%= userUUID%>';
						if(userUUID == 'null'){
							requestUrl = '<%=basePath %>system/vip/upPwd';
							requestData = {};
							requestData.oldPwd = oldPwd;
							requestData.newPwd = newPwd;
							requestData.openId = '<%= openId%>';
						}
						$.ajax({
							type:"post",
							url:requestUrl,
							data:requestData,
							dataType:'json',
							success:function(data){
								if(data.status == "1"){
									$.messager.alert("提示", "修改密码成功", "warning");
									$('#up_pwd').dialog('destroy');
								}else{
									$.messager.alert("提示", data.msg, "warning");
								}
							}
						});
					}
				},{
					text:'关闭',
					iconCls: 'icon-cancel',
					handler:function(){
						$('#up_pwd').dialog('destroy');
				    }
				}]
	    	});
		}
	</script>
</body>
</html>