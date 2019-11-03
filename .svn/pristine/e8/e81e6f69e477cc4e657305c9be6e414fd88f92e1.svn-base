<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID");
	String grade = request.getParameter("grade");
%>
<Layout style="width:700px;height:250px;">
		<LayoutPanel region="north" style="height:50px;">
			<div style="text-align:center;">
				<label>零售商名称：</label>
				<input class="easyui-textbox easyui-" type="text" style="width:200px" name="businessNameTow" id="businessNameTow" maxlength="20" size="20"/>
				<a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;搜索</a>
			</div>
		</LayoutPanel>
</Layout>
<table id="lss_table" class="easyui-datagrid" style="height: 100%;">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'businessUUID',align:'center',width:50,hidden:true">businessUUID</th>
			<th data-options="field:'businessName',align:'center',width:200">商家名称</th>
		</tr>
	</thead>
</table>

<div id="gradeList_window_div">
	<div id="gradeList_addGrade_div"></div>
</div>

<script type="text/javascript">
	$(function() {
		$('#lss_table').datagrid({
			title: '&nbsp;&nbsp;零售商添加',
			url: '<%=path %>/Public/business/retailerChoiceAll?businessUUID=<%=businessUUID%>',
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//
			}
		});
	});
	
	//查询
	function query(){
		var businessName = $("#businessNameTow").textbox('getValue');
		$("#lss_table").datagrid("load",{
			businessName:businessName
		});
	}
	
	//保存
	function middleSave() {
		var retailerUUID = "";
		var selRow = $("#lss_table").datagrid("getSelections"); //返回选中多行
		if(selRow.length == 0) {
			$.messager.alert('提示', '至少选择一项', 'error');
			return false;
		}
		var ids = []; //定义数组，用来记录列表id
		for(var i = 0; i < selRow.length; i++) {
			var id = selRow[i].businessUUID; //取列表中的单个Id
			ids.push(id);
		}
		retailerUUID = ids.join(",");
		var url = "<%=path%>/Public/business/middleSave?retailerUUID="+retailerUUID+"&supplierUUID=<%=businessUUID%>"+"&gradeNumber=<%=grade%>";
		$.post(url, {}, function(data) {
			$.messager.alert('提示', data.msg, 'info');
			$('#gradeList_addGrade_div').dialog('close');
			$("#gradeList_addGrade_div").remove();
			//刷新列表
			$('#retailer_table').datagrid('reload');
		}, 'json');
	}
	

</script>