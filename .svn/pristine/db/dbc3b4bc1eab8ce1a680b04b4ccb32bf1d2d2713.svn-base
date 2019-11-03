<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<a href="#" onclick="saveOpen('')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
</div>
<table id="grade_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'gradeUUID',align:'center',width:50,hidden:true">gradeUUID</th>
			<th data-options="field:'aliasName',align:'center',width:200">等级名称</th>
			<th data-options="field:'number',align:'center',width:200">等级编号</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="gradeList_window_div">
	<div id="gradeList_addGrade_div"></div>
</div>

<script type="text/javascript">
	$(function() {
		$('#grade_table').datagrid({
			title: '&nbsp;&nbsp;等价管理',
			url: '<%=path %>/Public/business/businessGradeAll',
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
		var save = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="saveOpen(\'' + row.gradeUUID + '\')">&nbsp;编辑&nbsp;</a>';
		var del = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="del(\'' + row.gradeUUID + '\')">&nbsp;删除&nbsp;</a>';
		return save + "&nbsp;" + del;
	}
	
	function saveOpen(gradeUUID){
		$("#gradeList_addGrade_div").remove();
		$("#gradeList_window_div").append("<div id='gradeList_addGrade_div'></div>");
		var url = "business/grade/grade_save.jsp?gradeUUID="+gradeUUID;
		var title = "";
		if(gradeUUID == ""){
			title = "新增";
		}else{
			title = "编辑";
		}
		$("#gradeList_addGrade_div").dialog({
			title: title,
			width: 480,
			height: 200,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					gradeSave();
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
	
	function del(gradeUUID){
		var msg = "确审删除吗？";
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			var url="<%=path%>/Public/business/gradeDel?gradeUUID="+gradeUUID;
    			$.post(url,{},function(data){
    				if(data){
    					 $.messager.alert('提示','操作成功!','info');
    					//刷新
    					$('#grade_table').datagrid('reload');
    				}else{
    					$.messager.alert('提示','操作失败!','error');
    				}
    			},'json');
    		}
    	});
	}

</script>