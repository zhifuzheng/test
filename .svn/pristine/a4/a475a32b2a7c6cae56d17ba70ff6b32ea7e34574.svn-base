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
<table id="img_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'slideUUID',align:'center',width:50,hidden:true">slideUUID</th>
			<th data-options="field:'title',align:'center',width:200">标题</th>
			<th data-options="field:'slide',align:'center',width:100, formatter:slide">幻灯片</th>
			<th data-options="field:'details',align:'center',width:100, formatter:details">详情</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="gradeList_window_div">
	<div id="gradeList_addGrade_div"></div>
</div>

<script type="text/javascript">
var path = "<%=basePath%>";//图片路径
	$(function() {
		$('#img_table').datagrid({
			title: '&nbsp;&nbsp;幻灯片',
			url: '<%=path %>/Public/business/slideImg',
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
		var save = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="saveOpen(\'' + row.slideUUID + '\')">&nbsp;编辑&nbsp;</a>';
		var del = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="del(\'' + row.slideUUID + '\')">&nbsp;删除&nbsp;</a>';
		return save + "&nbsp;" + del;
	}
	
	function saveOpen(slideUUID){
		$("#gradeList_addGrade_div").remove();
		$("#gradeList_window_div").append("<div id='gradeList_addGrade_div'></div>");
		var url = "img/slide_save.jsp?slideUUID="+slideUUID;
		var title = "";
		if(slideUUID == ""){
			title = "新增";
		}else{
			title = "编辑";
		}
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
					slideSave();
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
	
	//幻灯片
	function slide(value, row, index){
		var img = path+row.slide
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}
	
	//详情
	function details(value, row, index){
		var img = path+row.details
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}
	
	//删除
	function del(slideUUID){
		var msg = "确认删除吗？";
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			var url="<%=path%>/Public/business/slideDel?slideUUID="+slideUUID;
    			$.post(url,{},function(data){
    				$.messager.alert('提示',data.msg,'info');
 					//刷新
 					$('#img_table').datagrid('reload');
    			},'json');
    		}
    	});
	}
	

</script>