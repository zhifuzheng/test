<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID");
	String applyType = request.getParameter("applyType");
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
						<div class="lstoDayMoney">0</div>
						<div class="title">今日交易额</div>
					</div>
					<div class="sx-box-list">
						<div class="lsCountMoney">0</div>
						<div class="title">总销售额</div>
					</div>
					<div class="sx-box-list">
						<div class="lstoDayOrder">0</div>
						<div class="title">今日订单</div>
					</div>
				</div>
				
				<div class="xs-box">
					<div class="sx-box-list">
						<div class="lsTodayRefund">0</div>
						<div class="title">今日退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="lsMoneyRefund">0</div>
						<div class="title">总退款额</div>
					</div>
					<div class="sx-box-list">
						<div class="lsOrderRefund">0</div>
						<div class="title">退款订单</div>
					</div>
				</div>
				
				<!-- <div class="xs-box  list-Btn">
					<div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/shop_manage/order_list.jsp')">
							<div class="num"><i class="iconfont icondingdanguanli"></i></div>
							<div class="title">订单管理</div>
						</a>
					</div>
					<div class="sx-box-list">
						<a href="#" onclick="dataStatisticsOpen()">
							<div class="num"><i class="iconfont iconshangpinguanli"></i></div>
							<div class="title">数据统计</div>
						</a>
					</div> -->
					<!-- <div class="sx-box-list">
						<a href="#" onclick="orderOpen('business/administration/offline_order_list.jsp')">
							<div class="num"><i class="iconfont iconshangpinguanli"></i></div>
							<div class="title">订单核销</div>
						</a>
					</div> -->
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
				xxOrderStatistics();
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
			
			//零售商交易查询
			function xxOrderStatistics(){
				var url = "<%=path%>/Public/business/xxOrderStatistics?entityUUID=<%=businessUUID%>";
				$.post(url,{},function(data){
					//alert(JSON.stringify(data))
					$(".lstoDayMoney").html(data.lstoDayMoney);
					$(".lsCountMoney").html(data.lsCountMoney);
					$(".lstoDayOrder").html(data.lstoDayOrder);
					$(".lsTodayRefund").html(data.lsTodayRefund);
					$(".lsMoneyRefund").html(data.lsMoneyRefund);
					$(".lsOrderRefund").html(data.lsOrderRefund);
				},'json');
			}
			
			//点击跳转页面
			function orderOpen(pcUrl){
				$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl+"?entityUUID=<%=businessUUID%>&applyType=<%=applyType%>");
			}
			
			//数据统计
			function dataStatisticsOpen(){
				var pcUrl = "business/shop_manage/data_statistics_list.jsp?businessUUID=<%=businessUUID%>&applyType=<%=applyType%>";
				$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
			}
			
		</script>
