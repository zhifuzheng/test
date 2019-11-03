<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID"); 
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>时间：</label>
	<input id="startTime" name="startTime" type="text" class="easyui-datebox">——
	<input id="endingTime" name="endingTime" type="text" class="easyui-datebox">
    <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
    <a href="#" onclick="res()" class='easyui-linkbutton' iconcls='icon-undo' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">返回</a>
    <label>合计收款金额：</label><span id="totalMoney"></span>&nbsp;&nbsp;
    <label>合计订单数量：</label><span id="totalOrder"></span>
</div>
<table id="order_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'offlineUUID',align:'center',width:200">订单编号</th>
			<th data-options="field:'retailerImg',align:'center',width:100, formatter:retailerImg">店铺头像</th>
			<th data-options="field:'retailerName',align:'center',width:200">店铺名称</th>
			<th data-options="field:'commodityImg',align:'center',width:100, formatter:commodityImg">商品图片</th>
			<th data-options="field:'title',align:'center',width:100">商品名称</th>
			<th data-options="field:'totalPrice',align:'center',width:100">商品价格</th>
			<th data-options="field:'number',align:'center',width:100">商品数量</th>
			<th data-options="field:'retailerPhone',align:'center',width:100">商家电话</th>
			<th data-options="field:'time',align:'center',width:150">时间</th>
		</tr>
	</thead>
</table>

<div id="distributionList_window_div">
	<div id="distributionList_addDistribution_div"></div>
</div>

<script type="text/javascript">
	var path = "<%=basePath%>";
	$(function() {
		$('#order_table').datagrid({
			title: '&nbsp;&nbsp;线下收款统计',
			url: '<%=path %>/Public/business/offlineStatistics?businessUUID=<%=businessUUID%>'+"&state="+1,
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//$("#detailedMoney").html(row.detailedMoney);
			}
		});
		detailedRefund();
	});
	
	//搜索
	function query(){
		detailedRefund();
		var startTime = $("#startTime").val();
		var endingTime = $("#endingTime").val();
		$("#order_table").datagrid("load",{
			startTime:startTime,
			endingTime:endingTime
		});
	}
	
	function res() {
		var pcUrl = "business/administration/business_open_gys.jsp?businessUUID=<%=businessUUID%>";
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	function detailedRefund(){
		var startTime = $("#startTime").val();
		var endingTime = $("#endingTime").val();
		var url = "<%=path %>/Public/business/offlineStatistics?businessUUID=<%=businessUUID%>"+"&startTime="+startTime+"&endingTime="+endingTime+"&state="+1;
		$.post(url,{},function(data){
			//alert(JSON.stringify(data))
			$("#totalMoney").html(data.totalMoney);
			$("#totalOrder").html(data.totalOrder);
		},'json');
	}
	
	//店面图片
	function retailerImg(value, row, index){
		var img = path+row.retailerImg
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}
	
	//商品图片
	function commodityImg(value, row, index){
		var img = path+row.commodityImg
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}


</script>