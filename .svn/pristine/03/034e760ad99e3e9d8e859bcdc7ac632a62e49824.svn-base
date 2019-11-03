<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID"); 
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>条件搜索：</label>
	<input class="easyui-textbox easyui-" type="text" style="width:200px" data-options="prompt:'请输入订单号或商家名称或手机号码'" name="condition" id="condition" maxlength="20" size="20"/>
	<label>核销状态：</label>		
	<select id="state" name="state" class="easyui-combobox" editable=false; style="width:200px;height:32px">
		<option value="">请选择</option>
	    <option value="0">待核销</option>
	    <option value="1">已核销</option>
	</select>		
    <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
    <a href="#" onclick="res()" class='easyui-linkbutton' iconcls='icon-undo' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">返回</a>
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
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
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
			title: '&nbsp;&nbsp;线下订单管理',
			url: '<%=path %>/Public/business/offlineOrdersAll?businessUUID=<%=businessUUID%>',
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//
			}
		});
	});

	//格式化操作
	function clsListF(value, row, index) {
		var hx = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="hx(\'' + row.offlineUUID + '\')">&nbsp;订单核销&nbsp;</a>';
		var refund = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="refund(\'' + row.offlineUUID + '\')">&nbsp;退款&nbsp;</a>';
		if(row.state == 0){
			return hx;
		}else{
			return refund;
		}
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
	
	//搜索
	function query(){
		var condition = $("#condition").val();
		var state = $("#state").val();
		$("#order_table").datagrid("load",{
			condition:condition,
			state:state
		});
	}
	
	function res() {
		var pcUrl = "business/administration/business_open_gys.jsp?businessUUID=<%=businessUUID%>";
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	function hx(offlineUUID){
		var msg = "确认核销该订单吗？";
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			var url="<%=path%>/Public/business/purchase?offlineUUID="+offlineUUID;
    			$.post(url,{},function(data){
    				$.messager.alert('提示',data.msg,'info');
 					//刷新
 					$('#order_table').datagrid('reload');
    			},'json');
    		}
    	});
	}


</script>