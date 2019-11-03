<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!--  平台积分详情记录 -->
<style>
	.left{
		float: left;
	}
	.buttonSubmit {
		width: 100px;
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

	<table id="integral-list"></table>
	<!-- 筛选条件 -->
	<div id="condition" style="width:100%;height:50px;">
		<div class="left" style="line-height: 50px;width:15%">
			<span>类型：</span>
			<select id="type" onchange="getIntegralList()">
				<option value="0">不限</option>
				<option value="1">账户充值</option>
				<option value="2">购买商品</option>
				<option value="3">积分商品兑换</option>
			</select>
		</div>
	</div>


<script>
	//定义列
	var clumn = [[
		{
			field:'integralFrom',
			title:'积分来源',
			width:100,
			align:'center',
			formatter:function(data,row,index){//1-账户充值，2-购买商品，3-积分兑换
				return data == 1? "账户充值":data == 2? "购买商品":"积分兑换";
			}
		},
		{
			field:'integralType',//1-赠送积分，2-消耗积分
			title:'积分类型',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				return data == 1? "赠送积分":"消耗积分";
			}
		},
		{
			field:'moneyOrIntegral',
			title:'金额/积分',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				if(row.integralFrom == 1 || row.integralFrom == 2){//1-账户充值，2-购买商品，3-积分兑换
					return data+" 元";
				}else if(row.integralFrom == 3){
					return data+" 积分";
				}
			}
		},
		{
			field:'integration',
			title:'积分变化值',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				if(row.integralType == 1){//1-赠送积分，2-消耗积分
					return "+"+data;
				}else if(row.integralType == 2){
					return "-"+data;
				}
			}
		},
		{
			field:'changeTime',
			title:'交易日期',
			width:100,
			align : 'center',
		}
	]];
	
	//页面进入加载数据
	$(function(){
		loadData(0);
	});
	
	//条件选择重新加载数据
	function getIntegralList(){
		var condition = $("#type").val();
		$('#integral-list').datagrid('reload',{"type":condition});
	}
	
	function loadData(condition){
		$("#integral-list").datagrid({
			url:'<%=path %>'+"/system/integral/platformGetIntegralDetailRecord",
			queryParams:{type:condition},
			title: '&nbsp;&nbsp;积分详情记录', 
			toolbar: '#condition',
			columns:clumn,
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
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到积分详情信息哦</span>"
		});
	}
	
	
</script>
