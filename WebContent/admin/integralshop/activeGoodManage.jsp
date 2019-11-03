<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 官方活动添加 -->
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

	<table id="data-list"></table>
	<!-- 筛选条件 -->
	<div style="width:100%;height:50px;margin:auto;" id="condition">
		<div class="left" style="line-height: 50px;margin-left: 10px;width:20%;">
			<span>状态：</span>
			<select id="status" onchange="getactiveListby(1)"><!-- 1上架，2下架，3未上架 -->
				<option value="0">不限</option>
				<option value="1">已上架</option>
				<option value="2">已下架</option>
				<option value="3">未上架</option>
			</select>
		</div>
		<!-- 活动名称搜索输入 -->
		<div style="line-height: 50px;width:40%;float: right;margin-right:20px;">
			<input type="text" class="easyui-textbox" prompt="请输入活动名称搜索" style="width:84%" id="searchWords"></input>
			<input onclick="getactiveListby(2)" type="submit" style="cursor: pointer;color:#ffffff;width:13%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%" value="搜索">
			<!-- <button onclick="getactiveListby(2)" style="color:#ffffff;width:13%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%">搜索</button> -->
		</div>
	</div>

<script>
	var clumns = [[
		{
			field:'activityTitle',
			title:'活动名称',
			width:120,
			align:'center',
		},
		{
			field:'joinLimitTime',
			title:'可参与次数',
			width:50,
			align:'center',
		},
		{
			field:'time',//activeStartTime activeEndTime
			title:'活动时间',
			width:190,
			align:'center',
			formatter:function(data,row,index){
				return row.activeStartTime+"~"+row.activeEndTime
			}
		},
		{
			field:'title',
			title:'活动商品名称',
			width:80,
			align:'center',
		},
		{
			field:'price',
			title:'活动商品价格',
			width:55,
			align:'center',
			formatter:function(data,row,index){
				return data/100
			}
		},
		{
			field:'stock',
			title:'库存',
			width:30,
			align:'center',
		},
		{
			field:'getNumber',
			title:'购买量',
			width:30,
			align:'center',
		},
		{
			field:'getTime',
			title:'领取时间限制(小时)',
			width:80,
			align:'center',
		},
		{
			field:'goodStatus',//1上架，2下架，3未上架
			title:'状态',
			width:40,
			align:'center',
			formatter:function(data,row,index){
				return data == 1?"上架":data == 2?"下架":"未上架";
			}
		},
		{
			field:'operation',
			title:'操作',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				var html = "";
				if(row.goodStatus == 1){//上架 ：下架
					html = '<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="downActive(\''+row.activityUUID+'\');">&nbsp;下架&nbsp;</a>';
				}
				if(row.goodStatus == 2){//下架：上架，
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineActive(\''+row.activityUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorActive(\''+row.activityUUID+'\');">&nbsp;编辑&nbsp;</a>';
				}
				
				if(row.goodStatus == 3){//未上架：上架，删除,编辑
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineActive(\''+row.activityUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(255, 0, 0, 0.7);color:#fff" class="easyui-linkbutton  button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteActive(\''+row.activityUUID+'\');">&nbsp;删除&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorActive(\''+row.activityUUID+'\');">&nbsp;编辑&nbsp;</a>';
				}
				return html;
			}
		}
	]];
	
	//页面加载获取数据
	$(function(){
		var status = $("#status").val();
		$("#data-list").datagrid({
			url:'<%=path %>'+"/system/integral/findActiveList",
			queryParams:{status:status},
			title: '&nbsp;&nbsp;官方活动管理', 
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
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到相关信息</span>"
		});
	});
	
	/* 条件获取官方活动 */
	function getactiveListby(e){
		var searchWords = $("#searchWords").val();
		var status = $("#status").val();
		$("#data-list").datagrid('reload',{"searchWords":searchWords,"status":status});
	}
	
	
	
	
	/* 1-删除、2-上架、3-下架活动 */
	function oprateActive(activityUUID,type,msg){
		layer.confirm(msg, function(){
			$.ajax({
				url:baseURL+"system/integral/deleteActive",
				type:"POST",
				dataType:"json",
				data:{"activityUUID":activityUUID,"type":type},
				success:function(data){
					layer.alert(data.msg);
					if(data.status == 1){
						$("#data-list").datagrid('reload');
					}
				}
			});
		});
	}
	
	/* 下架优惠活动 */
	function downActive(activityUUID){
		var type = 3;
		var msg = "您确认下架该活动么？"
		oprateActive(activityUUID,type,msg)
	}
	
	/* 上架优惠活动 */
	function onlineActive(activityUUID){
		var type = 2;
		var msg = "您确认上架该活动么？"
		oprateActive(activityUUID,type,msg)
	}
	
	/* 删除优惠活动 */
	function deleteActive(activityUUID){
		var type = 1;
		var msg = "您确认删除该活动么？"
		oprateActive(activityUUID,type,msg)
	}
	
	/* 编辑优惠活动(未上架和已下架) */
	function editorActive(activityUUID){
		/* 编辑未上架的优惠劵 */
		loadCenterLayout("integralshop/activeGoodEditor.jsp?activityUUID="+activityUUID);
	}
	
	
	
	
	
	
	
	
	
</script>
