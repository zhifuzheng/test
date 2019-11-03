<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="dict-layout" data-options="fit:true" class="easyui-layout">
	<div data-options="region: 'west',  split: true, title: '角色管理', border:false" iconCls="icon-role" style="width: 463px;">
		<table id="role_table" class="easyui-datagrid">
			<thead>
				<tr>
					<th data-options="field:'id',hidden:true,width:80,align:'center'">id</th>
					<th data-options="field:'roleUUID',hidden:true,width:80,align:'center'">roleUUID</th>
					<th data-options="field:'roleName',width:80,align:'center'">角色名称</th>
					<th data-options="field:'roleMark',width:180,align:'center'">角色说明</th>
					<th data-options="field:'doRole',width:100,align:'center', formatter:roleMngF">操作</th>
				</tr>
			</thead>
		</table>
		<div class="tool" id="role_toolbar">
			<a id="dictgroup_add" onclick="addRole('','0');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-add-h'">新增</a>
			<!-- 
			<a id="dictgroup_update" onclick="roleUpdate()" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-edit'">修改</a> 
			<a id="dictgroup_delete" onclick="roleDelete();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-remove'">删除</a>
			 -->
		</div>
	</div>
	<div data-options="region: 'center', split: true, title: '分配菜单权限', border:false" iconCls="icon-user-menu">
		<div id="menu_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
			<!-- 
	        <a class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-add'" onclick="addMenu()">新增权限菜单</a>
	        <a class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-standard-pencil'" onclick="editMenu()">修改权限菜单</a>
	         -->
			<a class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-tick'" onclick="saveRoleMenu()" style="margin:3px 2px;">确认权限分配</a>
		</div>
		<ul id="roleMenuTree" class="easyui-tree"></ul>
	</div>
	<div data-options="region: 'east', split: true, title: '分配用户', border:false" iconCls="icon-add-user" style="width: 400px;">
		<div id="menu_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
			<a onclick="saveRoleUser();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-tick'" style="margin:3px 2px;">确认已分配的用户</a>
		</div>
		<!-- <div>&nbsp;&nbsp;已分配的用户</div> -->
		<ul id="roleUserTree"></ul>
	</div>
</div>
<div id="roleMng_window_div">
	<div id="role_addRole_div"></div>
	<div id="menu_addMenu_div"></div>
	<div id="menu_addRoleMenu_div"></div>
</div>
<!-- 是否勾选了权限菜单 -->
<input id="isSelectMenu" type="hidden" value="0" />
<!-- 是否勾选了用户 -->
<input id="isSelectUser" type="hidden" value="0" />
<script type="text/javascript">
var selectNode=null;
var row = null;
$(function () {
	 initRoleList();
	 var url = "../system/menu/findMenuByRoleUUID?roleUUID=";
	 //var url = "system/roleMng/tree_data1.json";
	 loadRoleMenuTree(url);

	 var url = "../system/role/findUserByRoleUUID?roleUUID=";
	 loadRoleUserTree(url);
});
//初始化角色列表
function initRoleList(){
	$('#role_table').datagrid({
		title: '',
		fit: true,
		url: baseURL + 'system/role/findRoleList',
        rownumbers: true,
        pagination: true,
        singleSelect: true,
        pageSize : 12,
		pageList : [ 12, 24 ],
		toolbar:'#role_toolbar',
        onSelect: selectRole,
        onUnselect: function(index, row) {
        	//$('#equipmentIdHidden').val('');
        }
	});
}

//初始化角色菜单树
function loadRoleMenuTree(url){
	 $.ajax({
	     type: 'GET',
	     url: url,
	     success: function (result) {
	         var myJson = eval('(' + result + ')');	         
	         $("#roleMenuTree").tree({
	             //method: 'post',
	             data: myJson,
	             animate:true,
	             lines: true,
	             //url: url,
	             dataPlain: true,//该属性用以启用当前 easyui-tree 控件对平滑数据格式的支持
	             toggleOnClick: false,
	             checkbox: true,
	             check:true,
	             enableContextMenu: false,
	             onClick: function(node){
	             	selectNode = node;
	             },
	             onCheck: function(node,checked){
	             	//设置菜单为已选择状态,为确定是否进行权限调整做判断
	             	$("#isSelectMenu").val("1");
	             }
	        });
	     }
	 });
}
//初始化角色用户树
function loadRoleUserTree(url){
   $("#roleUserTree").tree({
        method: 'post',
        animate:true,
        lines: true,
        url: url,
        dataPlain: true,//该属性用以启用当前 easyui-tree 控件对平滑数据格式的支持
        toggleOnClick: false,
        checkbox: true,
        enableContextMenu: false,
        onClick: function(node){
        	selectNode = node;
        },
        onCheck: function(node,checked){
        	//设置菜单为已选择状态,为确定是否进行权限调整做判断
        	$("#isSelectUser").val("1");
        }
   });
}
function roRoleF(rvalue,rowdata){
	var roleUUID = rowdata.roleUUID;
	return '<a href="javascript:void();" onclick="addRoleMenu(\''+roleUUID+'\')">分配权限</a>'; 
}
//选择
function selectRole(rowIndex, rowData){
	//刷新角色对应的权限
	 //var url = "menu/findMenuByRoleUUID?roleUUID="+rowData.roleUUID;
	 if($("#isSelectMenu").val()=="1"){
		$.messager.confirm("操作提醒", "角色权限分配还没有保存，是否进行保存操作？", function (c) {
			 if(c){
				 //保存操作
				 saveRoleMenu();
				 row = $('#role_table').datagrid('getSelected');
				 var url = "../system/menu/findMenuByRoleUUID?roleUUID="+row.roleUUID;
				 loadRoleMenuTree(url);
			 }else{
				 row = $('#role_table').datagrid('getSelected');
				 var url = "../system/menu/findMenuByRoleUUID?roleUUID="+row.roleUUID;
				 loadRoleMenuTree(url);
			 }
			 $("#isSelectMenu").val("0");
		});
	 }else{
		 row = $('#role_table').datagrid('getSelected');
		 var url = "../system/menu/findMenuByRoleUUID?roleUUID="+row.roleUUID;
		 loadRoleMenuTree(url);
	 }
	 
		//刷新角色对应的用户
	  //var url = "sys/user/findUserByRoleUUID.action?roleUUID="+rowData.roleUUID;
	 if($("#isSelectUser").val()=="1"){
		$.messager.confirm("操作提醒", "角色用户分配还没有保存，是否进行保存操作？", function (c) {
			 if(c){
				 //保存操作
				 saveRoleUser();
				 row = $('#role_table').datagrid('getSelected');
				 var url = "../system/role/findUserByRoleUUID?roleUUID="+rowData.roleUUID;
				 loadRoleUserTree(url);
			 }else{
				 row = $('#role_table').datagrid('getSelected');
				 var url = "../system/role/findUserByRoleUUID?roleUUID="+rowData.roleUUID;
				 loadRoleUserTree(url);
			 }
			 $("#isSelectUser").val("0");
		});
	 }else{
		 row = $('#role_table').datagrid('getSelected');
		 var url = "../system/role/findUserByRoleUUID?roleUUID="+rowData.roleUUID;
		 loadRoleUserTree(url);
	 }
}
/**
 * 添加或修改角色
 */
function addRole(roleUUID,type){
	$("#role_addRole_div").remove();
	$("#roleMng_window_div").append("<div id='role_addRole_div'></div>");
	var title = '新增角色';
	if("1"==type){
		title = '修改角色';
	}
	var url = "role/addRole.jsp?roleUUID="+roleUUID+"&isEdit="+type;
	$("#role_addRole_div").dialog({
		title : title,
		width : 500,
		height : 240,
		closed : false,
		eache : false,
		href : url,
		modal : true,
		buttons:[{
			text:'保存',
			iconCls: 'icon-mini-tick',
			handler:function(){saveRole();}
		},{
			text:'取消',
			iconCls: 'icon-cancel',
			handler:function(){
				$('#role_addRole_div').dialog('close');
				$("#role_addRole_div").remove();
			}
		}]
	});
}
//删除角色
function deleteRole(roleUUID){
	$.messager.confirm("操作提醒", "确定删除该角色吗？", function (c) {
		 if(c){
			$.post("<%=path%>/system/role/deleteRole?roleUUID="+roleUUID,"",function(data){
				  if(data){
					  //$.messager.show({ icon: "info", msg: "error" });
					  $.messager.alert("提示", "操作成功！", "info");
					  //刷新列表
					  $('#role_table').datagrid('reload');
				  }else{
					  $.messager.alert("提示", "操作失败！请联系管理员！", "warning");
				  }
			});
	     }
   });
}
/**
 * 角色分配菜单权限
 */
function saveRoleMenu(){
	if(row==null || row==""){
		$.messager.alert("提示", "请选择角色数据！", "warning");
		return;
	}
	var nodes = $('#roleMenuTree').tree('getChecked');
	var selectStr = '';
	if($("#isSelectMenu").val()=="0"){
		$.messager.alert("提示", "无权限改动，无需分配！", "warning");
		 return;
	}
	for(var i=0; i<nodes.length; i++){
		if (selectStr != '') selectStr += ',';
		selectStr += nodes[i].attributes.menuUUID;
	}
	if(selectStr==null || selectStr==""){
		$.messager.alert("提示", "请选择权限菜单", "warning");
		return;
	}
	var postData = new Object();
	postData["menuUUIDS"]=selectStr;

	postData["roleUUID"]=row.roleUUID;
	$.post("<%=path%>/system/menuRole/saveMenuRole",postData,function(data){
		if(data.status == "1"){
			//刷新角色对应的权限
			 var url = "../system/menu/findMenuByRoleUUID.action?roleUUID="+row.uuid;
			 loadRoleMenuTree(url);
			 $.messager.alert("提示","操作成功！","info");
			 $("#isSelectMenu").val("0");
		}else{
			$.messager.alert("提示", "操作失败，请联系管理员！", "warning");
		}
	},"json");
}

/**
 * 角色分配用户
 */
function saveRoleUser(){
	if(row==null || row==""){
		$.messager.alert("提示", "请选择角色数据！", "warning");
		return;
	}
	var nodes = $('#roleUserTree').tree('getChecked');
	var selectStr = '';
	if($("#isSelectUser").val()=="0"){
		$.messager.show("提示", "无权限改动，无需分配！", "warning", "topCenter");
		 return;
	}
	for(var i=0; i<nodes.length; i++){
		if (selectStr != '') selectStr += ',';
		selectStr += nodes[i].userUUID;
	}
	if(selectStr==null || selectStr==""){
		$.messager.alert("提示", "请选择权限菜单", "warning");
		return;
	}
	var postData = new Object();
	postData["userUUIDS"]=selectStr;
	postData["roleUUIDS"]=row.roleUUID;
	postData["operationName"]="role";
	$.post("<%=path%>/system/role/saveRoleUser", postData, function(data) {
			if (data) {
				//刷新角色对应的权限
				var url = "../system/menu/findUserByRoleUUID?roleUUID=" + row.uuid;
				loadRoleUserTree(url);
				$.messager.alert("提示", "操作成功！", "info");
				$("#isSelectUser").val("0");
			} else {
				$.messager.alert("提示", "操作失败，请联系管理员！", "warning");
			}
		});
	}
	/**
	 * 格式化角色操作
	 */
	function roleMngF(value, row, index) {
		var editHtml = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="addRole(\'' + row.roleUUID + '\',\'1\');">&nbsp;修改&nbsp;</a>';
		var deleteHtml = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteRole(\'' + row.roleUUID + '\');">&nbsp;删除&nbsp;</a>';
		return editHtml + "&nbsp;" + deleteHtml;
	}
</script>
