<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="UTF-8">
<title>用户管理</title>
<!--end-->
<base href="http://localhost:8080/zhtc/" />
</head>

<body>
	<div id="dict-layout" data-options="fit:true" class="easyui-layout">
		<div data-options="region: 'west', title: '用户管理', split: true, border:false" iconCls="icon-users" style="width: 700px;">
			<div class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
				<a class="easyui-linkbutton" data-options="plain: true" iconCls="icon-mini-add-h" onclick="addOrUpUser('','0');">新增用户</a>
				<!-- 
	        <a class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-remove'" onclick="deleteUserFromRegion()">移除用户</a>
	         -->
			</div>
			<table id="userinfo_table" data-options="title:''">
				<thead>
					<tr>
						<!-- <th data-options="field:'ck',checkbox:true"></th> -->
						<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
						<th data-options="field:'uuid',align:'center',width:50,hidden:true">uuid</th>
						<th data-options="field:'loginName',align:'center',width:90">登陆账号</th>
						<th data-options="field:'userName',align:'center',width:80">用户名</th>
						<th data-options="field:'userMobile',align:'center',width:90">手机</th>
						<th data-options="field:'userType',align:'center',width:100,formatter:function(value,row,index){
							if(value == 0) {
								return '超级管理员';
							}else if(value == 1) {
								return '管理员';
							}else{
								return '一般人员';
							}
						}">用户类型</th>
						<th data-options="field:'userSex',align:'center',width:40,hidden:true,formatter:function(value,row,index){
						if(value == '2') {
							return '女';
						}else if(value == '1') {
							return '男';
						}
					}">性别</th>
						<th data-options="field:'userStatus',align:'center',width:40,formatter:function(value,row,index){
						if(value == '1') {
							return '启用';
						}else if(value == '2') {
							return '注销';
						}else if(value == '3') {
							return '停用';
						}
					}">用户状态</th>
						<th data-options="field:'userEmail',align:'center',width:80,hidden:true">邮箱</th>
						<th data-options="field:'createTime',align:'center',width:80,hidden:true">创建时间</th>
						<th data-options="field:'CZ',width:120,align:'center',formatter:userListF">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<div data-options="region: 'center', split: true, title: '分配权限', border:false" iconCls="icon-user-menu">
			<div id="userNetGrid_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
				<a href="javascript:saveUserMenu();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-tick'">确认权限分配</a>
			</div>
			<ul id="userNetGridTree"></ul>
		</div>
		<div data-options="region: 'east', split: true, title: '分配角色',border:false" iconCls="icon-user-role" style="width: 200px;">
			<div id="userRole_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
				<a onclick="saveUserRole();" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-tick'">确认角色分配</a>
			</div>
			<div>&nbsp;&nbsp;已分配的角色&nbsp;</div>
			<ul id="userRoleTree"></ul>
		</div>
	</div>
	<div id="userList_window_div">
		<div id="userList_addUser_div"></div>
	</div>
	<!-- 是否勾选用户角色 -->
	<input id="isSelectUser" type="hidden" value="0" />
	<!-- 是否勾选菜单权限 -->
	<input id="isSelectMenu" type="hidden" value="0" />
	<script type="text/javascript">
    var selectRow = null;
	$(function(){
		initUserList();
		initUserRoleTree();
		initUserMenu();
	});
	function initUserList(){
		$('#userinfo_table').datagrid({
			title: '',
			url: baseURL + 'system/user/findUserList',
	        rownumbers: true,
	        pagination: true,
	        singleSelect: true,
	        toolbar: '#',
	        onSelect: selectUser,
	        onUnselect: function(index, row) {
	        	//$('#equipmentIdHidden').val('');
	        }
		});
	}
	//选择用户信息
	function selectUser(rowindex,rowdata){
		selectRow = $("#userinfo_table").datagrid("getSelected");
	    //-------------------------------------------------
		if($("#isSelectUser").val()=="1"){
			$.messager.confirm("操作提醒", "角色权限分配还没有保存，是否进行保存操作？", function (c) {
				 if(c){
					 //保存操作
				    saveUserRole();
					selectRow = $("#userinfo_table").datagrid("getSelected");
					initUserRoleTree(selectRow.userUUID);
				 }else{
					selectRow = $("#userinfo_table").datagrid("getSelected");
					initUserRoleTree(selectRow.userUUID);
				 }
				 $("#isSelectUser").val("0");
			 });
		 }else{
			selectRow = $("#userinfo_table").datagrid("getSelected");
			initUserRoleTree(selectRow.userUUID);
	     }
	    
		if($("#isSelectMenu").val()=="1"){
			$.messager.confirm("操作提醒", "菜单权限分配还没有保存，是否进行保存操作？", function (c) {
				 if(c){
					 //保存操作
				    saveUserMenu();
					selectRow = $("#userinfo_table").datagrid("getSelected");
					initUserMenu(selectRow.userUUID);
				 }else{
					selectRow = $("#userinfo_table").datagrid("getSelected");
					initUserMenu(selectRow.userUUID);
				 }
				 $("#isSelectMenu").val("0");
			 });
		 }else{
			selectRow = $("#userinfo_table").datagrid("getSelected");
			initUserMenu(selectRow.userUUID);
	     }
	    //-------------------------------------------------
	}
	//新增或修改用户信息
	function addOrUpUser(userUUID,type){//新增/修改菜单
		$("#userList_addUser_div").remove();
		$("#userList_window_div").append("<div id='userList_addUser_div'></div>");
		var url = "user/addUser.jsp?userUUID="+userUUID+"&isEdit="+type;
		var title = '新增用户';
		if("1"==type){
			title = '修改用户';
		}
		$("#userList_addUser_div").css({padding:"10px"}).dialog({
			title : title,
			width : 450,
			height : 450,
			closed : false,
			eache : false,
			href : url,
			modal : true,
			buttons:[{
				text:'保存',
				iconCls: 'icon-mini-tick',
				handler:function(){saveUser();}
			},{
				text:'取消',
				iconCls: 'icon-cancel',
				handler:function(){
					$('#userList_addUser_div').dialog('close');
					$("#userList_addUser_div").remove();
			    }
			}]
		});
	}
    //删除用户信息
	function deleteUser(id){
		$.messager.confirm("操作提醒", "确定删除该用户吗？", function (c) {
			 if(c){
				 //删除操作
				var url="<%=path%>/system/user/deleteUser?userUUIDS="+id;
				$.post(url,"",function(data){
					if(data){
						$('#userinfo_table').datagrid('reload');
					}
				},'json');
			 }
		 });
	}
    //修改用户状态
	function upUserStatus(rowIndex,status){
		$('#userinfo_table').datagrid('selectRow',rowIndex);
		var row = $('#userinfo_table').datagrid('getSelected'); 
		
		var msg = "确定要启用该用户？";
    	if (status == 3) {
    		msg = "确定要停用该用户？";
    	}
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			$.post(
   					'<%=path%>/system/user/updateUserStatus', 
   					{userUUID:row.userUUID,status:status}, 
   					function(data) {
   						if (data) {
   							$('#userinfo_table').datagrid('updateRow',{
   							    index: rowIndex,
   							    row: {
   							        'userStatus' : status
   							    }
   							});
   						}
   					}, 
   					'json'
   				);
	    	}
		});
	}
	//格式化操作
	function userListF(value, row, index) {
		var statusHtml = '';
		if ("1" == row.userStatus) {
			statusHtml = '<a style="background-color: #f2711c !important;"  class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="upUserStatus(\'' + index + '\',\'3\');">&nbsp;停用&nbsp;</a>';
		} else {
			statusHtml = '<a class="easyui-linkbutton button-default button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="upUserStatus(\'' + index + '\',\'1\');">&nbsp;启用&nbsp;</a>';
		}
		var editHtml = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="addOrUpUser(\'' + row.userUUID + '\',\'1\');">&nbsp;修改&nbsp;</a>';
		var deleteHtml = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteUser(\'' + row.userUUID + '\');">&nbsp;删除&nbsp;</a>';
		if("0"==row.userType){
			return "";
		}
		return statusHtml + "&nbsp;" + editHtml + "&nbsp;" + deleteHtml;
	}	
	//加载已分配的角色
	function loadUserRole(userUUID){
		if(userUUID == ""){
			
		}
	}
	//初始化用户关联角色树
	function initUserRoleTree(uuid){
		var url = "../system/role/findRoleByUserUUID?userUUID="+uuid;
	    $("#userRoleTree").tree({
	        method: 'post',
	        animate:true,
	        lines: true,
	        url: url,
	        dataPlain: true,//该属性用以启用当前 easyui-tree 控件对平滑数据格式的支持
	        toggleOnClick: false,
	        checkbox: true,
	        enableContextMenu: false,   
	        onClick: function(node){
	        },
	        onCheck: function(node,checked){
	        	//设置角色为已选择状态,为确定是否进行角色调整做判断
	        	$("#isSelectUser").val("1");
	        }
	    });
	}
	//初始化用户关联菜单权限树
	function initUserMenu(uuid){
		var url = "../system/menu/findMenuByUserUUID?userUUID="+uuid;
	    $("#userNetGridTree").tree({
	        method: 'post',
	        animate:true,
	        lines: true,
	        url: url,
	        dataPlain: true,//该属性用以启用当前 easyui-tree 控件对平滑数据格式的支持
	        toggleOnClick: false,
	        checkbox: true,
	        enableContextMenu: false,   
	        onClick: function(node){
	        }
	    });
	}
	/**
	 * 分配角色
	 */
	function saveUserRole(){
		if(selectRow==null){
			$.messager.alert("提示","请选择需要绑定角色的用户!","warning");
			return;
		}
		//------------------------------------------------------
		var nodes = $('#userRoleTree').tree('getChecked');
		var selectStr = '';
		for(var i=0; i<nodes.length; i++){
			if (selectStr != '') selectStr += ',';
			selectStr += nodes[i].id;
		}
		if(selectStr==null || selectStr==""){
			$.messager.alert("提示", "请选择角色", "warning");
			return;
		}
		var postData = new Object();
		postData["roleUUIDS"]=selectStr;
		postData["userUUIDS"]=selectRow.userUUID;
		postData["operationName"]="user";
		$.post("<%=path%>/system/role/saveRoleUser",postData,function(data){
			if(data){
				//刷新用户角色树
				selectRow = $("#userinfo_table").datagrid("getSelected");
				initUserRoleTree(selectRow.userUUID);
				$.messager.alert("提示","操作成功！","info");
				$("#isSelectUser").val("0");
			}else{
				$.messager.alert("提示", "操作失败，请联系管理员！", "warning");
				$("#isSelectUser").val("0");
			}
		});
	}
	/**
	 * 分配菜单权限
	 */
	function saveUserMenu(){
		if(selectRow==null){
			$.messager.alert("提示","请选择需要分配权限的用户!","warning");
			return;
		}
		var nodes = $('#userNetGridTree').tree('getChecked');
		var selectStr = '';
		for(var i=0; i<nodes.length; i++){
			if(selectStr != '') selectStr += ',';
			selectStr += nodes[i].attributes.menuUUID;
		}
		if(selectStr==null || selectStr==""){
			$.messager.alert("提示", "请选择权限", "warning");
			return;
		}
		var postData = new Object();
		postData["menuUUIDS"]=selectStr;
		postData["userUUID"]=selectRow.userUUID;
		$.post("<%=path%>/system/menu/saveMenuUser", postData, function(data) {
				if (data) {
					//刷新用户角色树
					selectRow = $("#userinfo_table").datagrid("getSelected");
					initUserMenu(selectRow.userUUID);
					$.messager.alert("提示", "操作成功！", "info");
					$("#isSelectMenu").val("0");
				} else {
					$.messager.alert("提示", "操作失败，请联系管理员！", "warning");
					$("#isSelectMenu").val("0");
				}
			});
		}
	</script>
</body>
</html>
