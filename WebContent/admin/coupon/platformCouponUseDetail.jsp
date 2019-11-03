<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 优惠劵使用详情记录 -->
<style>
	.left{
		float: left;
	}
	.buttonSubmit {
		width: 100px;}
		height: 33px;
		background-color: #02a2ff;
		color: #fff !important;
	}
	
	*::-webkit-scrollbar-track{
	border-radius:0px !important;
	}
	#releaseType,
	#type,
	#status,
	#entity-list,
	.select-box{
		width:74%;
		height:30px;
		border:1px solid #ccc;
		border-radius:0px !important;
	}
</style>
	<table id="data-list"></table>
	<div id="condition">
		<button onclick="turnBack()" style="color: #ffffff; width: 10%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px;">返回</button>
	</div>
	
<script>
	var columns = [[
		{
			field:"couponNumber",
			title:"劵编号",
			width:100,
			align:"center"
		},
		{
			field:"isGet",//劵的领取或绑定状态
			title:"是否被领取",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				return data == 1?"未领取":"已领取";
			}
		},
		{
			field:"getTime",
			title:"领取日期",
			width:100,
			align:"center"
		},
		{
			field:"vipName",
			title:"领取人",
			width:100,
			align:"center"
		},
		{
			field:"isUse",//劵使用状态
			title:"使用状态",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				if(row.isGet == 1){//未领取
					return "";
				}else{
					return data == 1?"未使用":data==2?"已使用":"已过期";
				}
			}
		},
		{
			field:"useTime",
			title:"使用日期",
			width:100,
			align:"center"
		}
	]];

	
	/* 页面加载,按默认条件获取优惠劵 */
	$(function(){
		var batchUUID ="<%=request.getParameter("batchUUID")%>"; 
		$("#data-list").datagrid({
			url:'<%=path %>'+"/system/coupon/getDetailCouponList",
			queryParams:{
				batchUUID:batchUUID,
			},
			title: '&nbsp;&nbsp;优惠劵使用详情记录', 
			columns:columns,
			toolbar: '#condition',
			rownumbers: true,
	        pagination: true,
	        pageList : [10,20,30,40],
			pageSize : 10,
			striped:true,
			fit : true,
			fitColumns : true,
			nowrap : false,
	        singleSelect: true,
	        pagePosition:'bottom',
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到相关信息</span>"
		});
		
	})
	
	function turnBack(){
		loadCenterLayout('coupon/platformCouponManage.jsp');
	}

</script>
