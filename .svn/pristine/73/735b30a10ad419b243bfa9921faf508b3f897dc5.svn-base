<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!--  平台积分商品兑换记录 -->
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
</style>

	<table id="goods-orderList"></table>
	<!-- 筛选条件 -->
	<div style="width:100%;line-height:50px;" id="condition">
		<div>
			<input type="text" class="easyui-textbox" prompt="请输入积分商品名称搜索" style="width:30%" id="searchWords"></input>
			<input onclick="getOrderListByCondition()" type="submit" style="cursor: pointer;color:#ffffff;width:13%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%" value="搜索">
			<!-- <button onclick="getOrderListByCondition()" style="color:#ffffff;width:10%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%">搜索</button> -->
		</div>
	</div>
<script>
	var clumns = [[
		{
			field:'goodImg',
			title:'商品图片',
			width:100,
			align:'center',
			formatter:function(data,value,index){
				var url = '<%=basePath %>'+data;
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+url+'"/>';
			}
		},
		{
			field:'title',
			title:'商品名称',
			width:150,
			align:'center'
		},
		{
			field:'integration',
			title:'商品积分价格',
			width:100,
			align:'center'
		},
		{
			field:'userIntegration',
			title:'用户剩余积分',
			width:100,
			align:'center'
		},
		{
			field:'endTime',
			title:'领取时间',
			width:100,
			align:'center'
		},
		{
			field:'payerName',
			title:'买家名称',
			width:100,
			align:'center'
		},
		{
			field:'orderStatus',//0-未领取，1-已领取，2-已过期
			title:'状态',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				return data == 0?"未领取":data == 1?"已领取":"已过期"
			}
		},
		{
			field:'opration',
			title:'操作',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				if(row.orderStatus == 1 || row.orderStatus == 2){
					var html = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteOrder(\''+row.orderUUID+'\');">&nbsp;删除&nbsp;</a>';
					return html
				}
				
			}
		},
		
	]];
	
	//页面加载获取数据
	$(function(){
		$("#goods-orderList").datagrid({
			url:'<%=path %>'+"/system/integral/platformGetIntegralExGoodsList",
			/* queryParams:{type:condition}, */
			title: '&nbsp;&nbsp;积分商品兑换记录', 
			toolbar: '#condition',
			columns:clumns,
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
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到积分商品兑换信息哦</span>"
		});
	});

	/**  积分商品名称搜索 */
	function getOrderListByCondition(){
		var condition = $("#searchWords").val();
		//console.log("搜索词："+condition);
		if(condition.replace(/\s/g, "") == "" || condition == undefined){//关键词搜索为空
			$("#goods-orderList").datagrid('reload',{});
		}else{
			$("#goods-orderList").datagrid('reload',{"searchWords":condition});
		}
	}

	
	/* 平台删除积分商品兑换订单 */
	function deleteOrder(orderUUID){
		layer.confirm('您确认删除该商品兑换记录吗?', {icon: 3, title:'提示'}, function(index){
			$.ajax({
				url:baseURL+"system/integral/platformDelGoodsExRecordOfPc",
				type:"POST",
				dataType:"json",
				data:{"orderUUID":orderUUID},
				success:function(data){
					$.messager.alert('提示',data.msg,'info');
					if(data.status == "1"){
						$("#goods-orderList").datagrid('reload');
					}
				}
			});
		    layer.close(index);
		});
	}
	
	
</script>
