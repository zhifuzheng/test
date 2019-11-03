<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 积分商城商品管理 -->
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
	<div style="width:100%;height:50px;" id="condition" >
		<div class="left" style="line-height: 50px;width:15%">
			<span>排序：</span>
			<select class="select-box" id="range" onchange="getGoodsList(1)">
				<option value="-1">不限</option>
				<option value="integration">价格</option>
				<option value="createTime">创建日期</option>
			</select>
		</div>
		<div class="left" style="line-height: 50px;margin-left: 20px;width:15%;float:left">
			<span>状态：</span>
			<select id="status" onchange="getGoodsList(2)"><!-- 1上架，2下架，3未上架 -->
				<option value="-1">不限</option>
				<option value="1">已上架</option>
				<option value="2">已下架</option>
				<option value="3">未上架</option>
			</select>
		</div>
		<!-- 商品名称搜索输入 -->
		<div style="line-height: 50px;width:35%;float: right;margin-right:20px">
			<input type="text" class="easyui-textbox" prompt="请输入商品名称搜索" style="width:84%" id="searchWords"></input>
			<input onclick="getGoodsList(3)" type="submit" style="cursor: pointer;color:#ffffff;width:13%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%" value="搜索">
			<!-- <button onclick="getGoodsList(3)" style="color:#ffffff;width:13%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%">搜索</button> -->
		</div>
	</div>
	

<script>
	var columns = [[
		{
			field:'goodImg',
			title:'商品图片',
			width:120,
			align:'center',
			formatter:function(data,row,index){
				var url = '<%=basePath %>'+data;
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+url+'"/>'; 
			}
		},
		{
			field:'title',
			title:'商品标题',
			width:120,
			align:'center',
		},
		{
			field:'integration',
			title:'商品积分价格',
			width:120,
			align:'center',
		},
		{
			field:'getTime',
			title:'领取时间限制(小时)',
			width:120,
			align:'center',
		},
		{
			field:'stock',
			title:'库存',
			width:120,
			align:'center',
		},
		{
			field:'goodStatus',//商品状态：1上架，2下架，3未上架
			title:'状态',
			width:120,
			align:'center',
			formatter:function(data,row,index){
				return data ==1?"已上架":data==2?"已下架":"未上架"
			}
		},
		{
			field:'operation',
			title:'操作',
			width:120,
			align:'center',
			formatter:function(data,row,index){
				var html = "";
				if(row.goodStatus == 1){//已上架 ：下架
					html = '<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="downGood(\''+row.goodUUID+'\');">&nbsp;下架&nbsp;</a>';
				}
				if(row.goodStatus == 2){//已下架：上架，编辑
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineGood(\''+row.goodUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorGood(\''+row.goodUUID+'\');">&nbsp;编辑&nbsp;</a>';
				}
				
				if(row.goodStatus == 3){//未上架：上架，删除,编辑
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineGood(\''+row.activityUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(255, 0, 0, 0.7);color:#fff" class="easyui-linkbutton  button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteGood(\''+row.goodUUID+'\');">&nbsp;删除&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorGood(\''+row.goodUUID+'\');">&nbsp;编辑&nbsp;</a>';
				}
				return html;
				
			}
		}
	]];
	
	/* 数据加载 */
	$(function(){
		var range = $("#range").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		$("#data-list").datagrid({
			url:'<%=path %>'+"/system/integral/getIntegralGoodList",
			queryParams:{range:range,status:status,searchWords:searchWords},
			title: '&nbsp;&nbsp;积分商城商品管理', 
			toolbar: '#condition',
			columns:columns,
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
	
	/* 根据条件查询积分商品列表 */
	function getGoodsList(e){
		var range = $("#range").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		$("#data-list").datagrid('reload',{'range':range,'status':status,'searchWords':searchWords});
	}
	
	
	/* 下架积分商品 */ //1-删除、2-上架、3-下架
	function downGood(goodUUID){
		var type = 3;
		var msg = "您确认下架该商品么？"
		oprateGood(goodUUID,type,msg)
	}
	
	/* 上架积分商品 */
	function onlineGood(goodUUID){
		var type = 2;
		var msg = "您确认上架该商品么？"
		oprateGood(goodUUID,type,msg)
	}
	
	/* 删除积分商品 */
	function deleteGood(goodUUID){
		var type = 1;
		var msg = "您确认删除该商品么？"
		oprateGood(goodUUID,type,msg)
	}
	
	/* 编辑优惠活动(未上架和已下架) */
	function editorGood(goodUUID){
		/* 编辑未上架的优惠劵 */
		loadCenterLayout("integralshop/integralGoodsEditor.jsp?goodUUID="+goodUUID);
	}
	
	/* 1-删除、2-上架、3-下架 */
	function oprateGood(goodUUID,type,msg){
		layer.confirm(msg, function(){
			$.ajax({
				url:baseURL+"system/integral/oprateGoods",
				type:"POST",
				dataType:"json",
				data:{"goodUUID":goodUUID,"type":type},
				success:function(data){
					layer.alert(data.msg);
					if(data.status == 1){
						var range = $("#range").val();
						var status = $("#status").val();
						var searchWords = $("#searchWords").val();
						$("#data-list").datagrid('reload',{'range':range,'status':status,'searchWords':searchWords});
					}
				}
			});
		});
	}
	
</script>
