<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<a href="#" onclick="settledSave('','0')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
</div>
<table id="settled_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'settledUUID',align:'center',width:50,hidden:true">settledUUID</th>
			<th data-options="field:'money',align:'center',width:200">金额</th>
			<th data-options="field:'settledType',align:'center',width:200,formatter:settledType">有效时间</th>
			<th data-options="field:'time',align:'center',width:200">时间</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="settledList_window_div">
	<div id="settledList_addSettled_div"></div>
</div>

<script type="text/javascript">
	var selectRow = null;
	$(function() {
		$('#settled_table').datagrid({
			title: '&nbsp;&nbsp;商家续费设置',
			url: '<%=path %>/Public/business/settledAll',
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
		var edit = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="settledSave(\'' + row.settledUUID + '\',\'1\')">&nbsp;编辑&nbsp;</a>';
		var del = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="settledDel(\'' + row.settledUUID + '\')">&nbsp;删除&nbsp;</a>';
		return edit+"&nbsp;"+del;
	}
	
	function settledType(value, row, index){
		if(row.settledType==0){
			return "7";
		}else if(row.settledType==1){
			return "185";
		}else if(row.settledType==2){
			return "365";
		}
	}
	
	//新增或修改
	function settledSave(settledUUID,type) {
		$("#settledList_addSettled_div").remove();
		$("#settledList_window_div").append("<div id='settledList_addSettled_div'></div>");
		var url = "business/settled_money/settled_save.jsp?settledUUID="+settledUUID;
		var title = '添加';
		if(type == 1){
			title == '编辑';
		}
		$("#settledList_addSettled_div").dialog({
			title: title,
			width: 800,
			height: 400,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					save();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#settledList_addSettled_div').dialog('close');
					$("#settledList_addSettled_div").remove();
				}
			}]
		});
	}
	
	function settledDel(e){
		var msg = "确审删除吗？";
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			var url="<%=path%>/Public/business/settledDel?settledUUID="+e;
    			$.post(url,{},function(data){
    				if(data){
    					 $.messager.alert('提示','操作成功!','info');
    					//刷新
    					$('#settled_table').datagrid('reload');
    				}else{
    					$.messager.alert('提示','操作失败!','error');
    				}
    			},'json');
    		}
    	});
	}

</script>