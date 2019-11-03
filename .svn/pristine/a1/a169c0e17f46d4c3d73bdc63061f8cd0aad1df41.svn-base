<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	String entityUUID = request.getParameter("businessUUID");
%>
	<Layout style="width:700px;height:250px;">
		<LayoutPanel region="north" style="height:50px;">
			<div>
				<label>查询条件：</label>
				<input data-options="prompt:'请输入订单号/收货人姓名/手机号'" id="condition" name="condition" class="easyui-textbox" style="width:300px">&nbsp;&nbsp;
				<a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;搜索</a>
				<a href="javascript:void(0)" onclick="initQuery()" class='easyui-linkbutton' iconcls='icon-reload' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;清除</a>
				<a href="#" onclick="res()" class='easyui-linkbutton' iconcls='icon-undo' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">返回</a>
			</div>
		</LayoutPanel>
	</Layout>
	<table id="order_table" class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
				<th data-options="field:'orderUUID',align:'center',width:50,hidden:true">orderUUID</th>
				<th data-options="field:'entityName',align:'center',width:200">店铺名称</th>
				<th data-options="field:'title',align:'center',width:200">订单标题</th>
				<th data-options="field:'totalPrice',width:300,align:'center',formatter:totalPrice">商品总价</th>
				<th data-options="field:'totalCount',align:'center',width:200">商品总件数</th>
				<!-- <th data-options="field:'CZ',width:300,align:'center',formatter:clsListF">操作</th> -->
			</tr>
		</thead>
	</table>
	
	 <div id="newsList_window_div">
		<div id="newsList_addNews_div"></div>
	</div>
	
	<script type="text/javascript">
    var selectRow = null;
	$(function(){
		$('#order_table').datagrid({
			title: '&nbsp;&nbsp;订单管理', 
			url: '<%=path %>/Public/business/orderAll?entityUUID=<%=entityUUID%>',
	        rownumbers: true,
	        pagination: true,
	        singleSelect: false,
	        onUnselect: function(index, row) {
	        	//
	        }
		});
	});

	//格式化操作
	/* function clsListF(value, row, index) {
		var upHtml = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="addSaveNews(\'' + row.djUUID + '\',\'1\')">&nbsp;编辑&nbsp;</a>';
		return upHtml;
	} */
	
	//商品总价
	function totalPrice(value, row, index){
		return ( row.totalPrice / 100 ).toFixed( 2 );
	}
	
	
	
	
	//查询
	function query(){
		condition = $("#condition").val();
		$("#order_table").datagrid("load",{
			condition:condition
		});
	}
	
	//清除
	function initQuery(){
		$("#condition").textbox('setValue')
	}
	
	function res() {
		var pcUrl = "business/administration/business_open_gys.jsp?businessUUID=<%=entityUUID%>";
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	
	</script>

