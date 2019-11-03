<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String businessUUID = request.getParameter("businessUUID");
%>
<Layout style="width:700px;height:250px;">
		<LayoutPanel region="north" style="height:50px;">
			<div>
				<label>零售商名称：</label>
				<input class="easyui-textbox easyui-" type="text" style="width:200px" name="businessName" id="businessName" maxlength="20" size="20"/>
				<label>等级分类：</label>
				<select id="grade" name="grade" style="width:200px; height: 32px;">
				
				</select>
				<a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">&nbsp;搜索</a>
				<a href="#" onclick="retailerOpen('<%=businessUUID%>')" class="easyui-linkbutton" data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">添加</a>
				<a href="#" onclick="removeOpen('<%=businessUUID%>')" class="easyui-linkbutton" iconcls='icon-cancel' style="background-color:#1b8cf2;">移除</a>
				<a href="#" onclick="res()" class='easyui-linkbutton' iconcls='icon-undo' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;">返回</a>
			</div>
		</LayoutPanel>
	</Layout>
<table id="retailer_table" class="easyui-datagrid">
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
		$('#retailer_table').datagrid({
			title: '&nbsp;&nbsp;零售商管理',
			url: '<%=path %>/Public/business/retailerAll?businessUUID=<%=businessUUID%>',
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//
			}
		});
		businessGradeAll();
	});
	
	
	function retailer(businessUUID,gradeName,grade){
		$("#gradeList_addGrade_div").remove();
		$("#gradeList_window_div").append("<div id='gradeList_addGrade_div'></div>");
		var url = "business/grade/retailer_choice_list.jsp?businessUUID="+businessUUID+"&grade="+grade;
		var title = "当前等级分类："+gradeName;
		$("#gradeList_addGrade_div").dialog({
			title: title,
			width: 800,
			height: 600,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					middleSave();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#gradeList_addGrade_div').dialog('close');
					$("#gradeList_addGrade_div").remove();
				}
			}]
		});
	}
	
	function res() {
		var pcUrl = "business/my_business_list.jsp";
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	function businessGradeAll(){
		var url="<%=path%>/Public/business/businessGradeAll";
		$.post(url,{},function(data){
			//alert(JSON.stringify(data));
			var json = eval(data.list);//数组
			$("#grade").append("<option value='' selected='selected'>请选择</option>");
			$.each(json,function(index,item){
				$("#grade").append("<option value='"+json[index].number+"'>"+json[index].aliasName+"</option>");
			});
		},'json');
	}
	
	function retailerOpen(businessUUID){
		var businessUUID = businessUUID;
		var gradeName = $("#grade").find("option:selected").text();
		var grade = $("#grade").val();
		if(grade == ""){
			$.messager.alert('提示',"请选择等级分类",'error');
			return false;
		}
		retailer(businessUUID,gradeName,grade);
	}
	
	//查询
	function query(){
		var businessName = $("#businessName").val();
		var gradeUUID = $("#grade").val();
		$("#retailer_table").datagrid("load",{
			businessName:businessName,
			gradeUUID:gradeUUID
		});
	}
	
	//移除
	function removeOpen(businessUUID) {
		var retailerUUID = "";
		var selRow = $("#retailer_table").datagrid("getSelections"); //返回选中多行
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
		var url = "<%=path%>/Public/business/middleDel?retailerUUID="+retailerUUID+"&supplierUUID=<%=businessUUID%>";
		$.post(url, {}, function(data) {
			$.messager.alert('提示', data.msg, 'info');
			//刷新列表
			$('#retailer_table').datagrid('reload');
		}, 'json');
	}

</script>