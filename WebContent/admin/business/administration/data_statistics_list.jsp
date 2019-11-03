<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String businessUUID = request.getParameter("businessUUID");
%>
	<Layout style="width:700px;height:250px;">
		<LayoutPanel region="north" style="height:50px;">
			<div>
				<label>开始时间：</label>
				<input id="createTime" name="createTime" type="text" class="easyui-datebox">
				<label>结束时间：</label>
				<input id="closeTime" name="closeTime" type="text" class="easyui-datebox">
				<a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;搜索</a>
				<a href="javascript:void(0)" onclick="initQuery()" class='easyui-linkbutton' iconcls='icon-reload' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;清除</a>
				<a href="#" onclick="res()" class='easyui-linkbutton' iconcls='icon-undo' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">返回</a>
				<label style="color: red;">销售额：</label><span id="totalPrice">0</span>
				<label style="color: red;">下单人数：</label><span id="rs">0</span>
				<label style="color: red;">订单数量：</label><span id="count">0</span>
			</div>
		</LayoutPanel>
	</Layout>
	<table id="order_table" class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
				<th data-options="field:'entityName',align:'center',width:200">店铺名称</th>
				<th data-options="field:'totalPrice',align:'center',width:200">商品总价</th>
				<th data-options="field:'totalCount',align:'center',width:200">商品总件数</th>
				<th data-options="field:'title',align:'center',width:200">商品标题</th>
				<th data-options="field:'personName',align:'center',width:200">买家姓名</th>
				<th data-options="field:'receiverMobile',align:'center',width:200">电话号码</th>
				<th data-options="field:'orderUUID',align:'center',width:250">订单编号</th>
				<!-- <th data-options="field:'itemImg',width:200,align:'center',formatter:itemImg">图片</th> -->
				<th data-options="field:'createTime',align:'center',width:200">时间</th>
				<th data-options="field:'CZ',width:300,align:'center',formatter:clsListF">操作</th>
			</tr>
		</thead>
	</table>
	
	 <div id="newsList_window_div">
		<div id="newsList_addNews_div"></div>
	</div>
	
	<script type="text/javascript">
	var path = "<%=basePath%>";//图片路径
    var selectRow = null;
	$(function(){
		$('#order_table').datagrid({
			title: '&nbsp;&nbsp;数据统计', 
			url: '<%=path %>/Public/business/orderAll?entityUUID=<%=businessUUID%>',
	        rownumbers: true,
	        pagination: true,
	        singleSelect: false,
	        onUnselect: function(index, row) {
	        	//
	        }
		});
		order();
	});

	//格式化操作
	function clsListF(value, row, index) {
		var upHtml = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="addSaveNews(\'' + row.djUUID + '\',\'1\')">&nbsp;编辑&nbsp;</a>';
		return upHtml;
	}
	
	
	
	
	//查询
	function query(){
		createTime = $("#createTime").val();
		closeTime = $("#closeTime").val();
		$("#order_table").datagrid("load",{
			createTime:createTime,
			closeTime:closeTime
		});
	}
	
	//清除
	function initQuery(){
		$("#createTime").textbox('setValue')
		$("#createTime").textbox('setValue')
	}
	
	function res() {
		var pcUrl = "business/administration/business_open_gys.jsp?businessUUID=<%=businessUUID%>";
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	//商品图片
	function itemImg(value, row, index){
		alert(row.bid)
		var img = path+row.itemImg
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}
	
	function order(){
		//销售额
		var xUrl="<%=path%>/Public/business/totalPriceSum?entityUUID=<%=businessUUID%>";
		$.post(xUrl,{},function(data){
			//alert(JSON.stringify(data.totalPrice));
			$("#totalPrice").html(data.totalPrice);
		},'json');
		
		//订单数量
		var dUrl="<%=path%>/Public/business/orderNumber?entityUUID=<%=businessUUID%>";
		$.post(dUrl,{},function(data){
			//alert(JSON.stringify(data.count));
			$("#count").html(data.count);
		},'json');
		
		//下单人数
		var dUrl="<%=path%>/Public/business/placeNumber?entityUUID=<%=businessUUID%>";
		$.post(dUrl,{},function(data){
			//alert(JSON.stringify(data.count));
			$("#rs").html(data.rs);
		},'json');
	}
	
	
	</script>

