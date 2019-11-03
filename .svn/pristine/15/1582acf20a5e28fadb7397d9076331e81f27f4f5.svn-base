<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID");
%>
<link rel="stylesheet" type="text/css" href="https://at.alicdn.com/t/font_1301256_ghs1bsb1ct.css"/>
	<style>
		.dp-h{
			display: flex;
			align-content: center;
		    padding-bottom: 10px;
			border-bottom: 1px solid #ccc;
		}
		
		.dp-h .herad{
			width: 60px;
			height: 60px;
			border-radius: 50%;
			background: no-repeat center center;
			background-size:cover ;
		}
		
		.dp-h .herad img{
			width: 100%;
			height: 100%;
			opacity: 0;
		}
		
		.dp-h .name{
			line-height: 60px;
			padding-left: 10px;
		}
		
		.xs-box {
			display: flex;
			height: 90px;
		    align-items: center;
	        border-radius: 6px;
		    border: 1px solid #f1f1f1;
		    margin-top: 10px;
		    color: #555;
		    background: #fffff1;
		}
		
		.xs-box .sx-box-list{
		    width: 15%;
		    text-align: center;
		    position: relative;
		    border-right: 1px solid #f1f1f1;
		}
		
		.xs-box .sx-box-list:last-child{
			border-right: 0;
		}
		
		.xs-box .sx-box-list .num{
			margin-bottom: 5px;
		    font-size: 25px;
		}
		
		.xs-box .sx-box-list .num .iconfont{
			font-size: 35px;
		}
		
		.xs-box .sx-box-list .num:nth-child(even) .iconfont{
			color: #D58512;
		}
		
		.xs-box .sx-box-list .num:nth-child(odd) .iconfont{
			color: #00AFF0;
		}
		
		.list-Btn a{
			text-decoration: none;
			color: #555;
		}
		
	</style>
		
		<div class="dp-box">
			<div class="dp-h">
				<div class="herad"><img id="storefrontImg" src="img/790_400_0.jpg"/></div>
				<div class="name" id="businessName">店铺名称</div>
			</div>
			<div class="dp-b">
				<div class="xs-box">
					<div class="sx-box-list">
						<div class="onLineTodayMoney">0</div>
						<div class="title">线上今日交易额</div>
					</div>
					<div class="sx-box-list">
						<div class="onLineCountMoney">0</div>
						<div class="title">线上总销售额</div>
					</div>
					<div class="sx-box-list">
						<div class="onLineTodayOrder">0</div>
						<div class="title">线上今日订单</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineTodayMoney">0</div>
						<div class="title">线下今日交易额</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineCountMoney">0</div>
						<div class="title">线下总销售额</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineTodayOrder">0</div>
						<div class="title">线下今日订单</div>
					</div>
				</div>
				
				<div class="xs-box">
					<div class="sx-box-list">
						<div class="onLineTodayRefund">0</div>
						<div class="title">线上今日退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="onLineMoneyRefund">0</div>
						<div class="title">线上总退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="onLineOrderRefund">0</div>
						<div class="title">线上退款订单</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineTodayRefund">0</div>
						<div class="title">线下今日退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineMoneyRefund">0</div>
						<div class="title">线下总退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="offlineOrderRefund">0</div>
						<div class="title">线下退款订单</div>
					</div>
				</div>
				
				<div class="xs-box  list-Btn">
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/order_list.jsp')">
							<div class="num"><i class="iconfont icondingdanguanli"></i></div>
							<div class="title">线上订单管理</div>
						</a>
					</div>
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/offline_order_list.jsp')">
							<div class="num"><i class="iconfont iconshangpinguanli"></i></div>
							<div class="title">线下订单管理</div>
						</a>
					</div>
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/data_statistics_list.jsp')">
							<div class="num"><i class="iconfont icondingdanhexiao"></i></div>
							<div class="title">线上数据统计</div>
						</a>
					</div>
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/offline_refund_statistics.jsp')">
							<div class="num"><i class="iconfont iconshanghuruzhu"></i></div>
							<div class="title">线下退款统计</div>
						</a>
					</div>
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/offline_collection_statistics.jsp')">
							<div class="num"><i class="iconfont icongongxuguanli"></i></div>
							<div class="title">线下收款统计</div>
						</a>
					</div>
					<!-- <div class="sx-box-list">
						<a href="">
							<div class="num"><i class="iconfont iconwodeqianbao"></i></div>
							<div class="title">我的钱包</div>
						</a>
					</div> -->
					<!-- <div class="sx-box-list">
						<a href="">
							<div class="num"><i class="iconfont iconshangpinguanli1"></i></div>
							<div class="title">商品管理</div>
						</a>
					</div> -->
				</div>
			</div>
		</div>
		
		<!-- <script src="js/jquery-v3-3.js" type="text/javascript" charset="utf-8"></script> -->
		<script type="text/javascript">
			var path = "<%=basePath%>";
			$(function(){
				applyId();
				orderSingle();
			})
			
			//根据商家UUID查询
			function applyId(){
				var url = "<%=path%>/Public/business/applyId?businessUUID=<%=businessUUID%>";
				$.post(url,{},function(data){
					//alert(JSON.stringify(data))
					$("#storefrontImg").attr("src",path+data.storefrontImg);
					$(".herad").css({"background-image":"url("+ path+data.storefrontImg +")"});
					$("#businessName").html(data.businessName);
				},'json');
			}
			
			//供应商交易查询
			function orderSingle(){
				var url = "<%=path%>/Public/business/orderSingle?businessUUID=<%=businessUUID%>";
				$.post(url,{},function(data){
					//alert(JSON.stringify(data))
					$(".onLineTodayMoney").html(data.onLineTodayMoney);
					$(".onLineCountMoney").html(data.onLineCountMoney);
					$(".onLineTodayOrder").html(data.onLineTodayOrder);
					$(".offlineTodayMoney").html(data.offlineTodayMoney);
					$(".offlineCountMoney").html(data.offlineCountMoney);
					$(".offlineTodayOrder").html(data.offlineTodayOrder);
					$(".onLineTodayRefund").html(data.onLineTodayRefund);
					$(".onLineMoneyRefund").html(data.onLineMoneyRefund);
					$(".onLineOrderRefund").html(data.onLineOrderRefund);
					$(".offlineTodayRefund").html(data.offlineTodayRefund);
					$(".offlineMoneyRefund").html(data.offlineMoneyRefund);
					$(".offlineOrderRefund").html(data.offlineOrderRefund);
				},'json');
			}
			
			//点击跳转页面
			function orderOpen(pcUrl){
				$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl+"?businessUUID=<%=businessUUID%>");
			}
			
		</script>
