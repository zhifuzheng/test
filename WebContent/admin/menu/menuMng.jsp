<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="dict-layout" data-options="fit:true" class="easyui-layout">
	<div data-options="region: 'west',split: true, title: '权限菜单管理'" iconCls="icon-menu" style="width: 100%;" border="0">
		<div class="tool" style="border-bottom: 1px solid #e1e1e1">
			<a onclick="addOrUpMenu('','-1','顶级菜单','0');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-add-h'" style="margin:3px 2px;">新增顶级权限菜单</a> 
		</div>
	    <ul id="menuTree" class="ztree" style="width:97%;height:90%; border: none; overflow: auto; margin-top:0px;overflow: auto"></ul>
	</div>
	<!--  
	<div data-options="region: 'center', split: true, title: '分配角色', border:false" style="border-left:1px">
	    <div id="menu_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
	        <a class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-ok'" onclick="saveMenuRole()" style="margin:3px 2px;">确认角色分配</a> 
	    </div>
	    <ul id="menuRoleTree" class="ztree" style="width:98%;height:90%; border: none; overflow: auto; margin-top:0px;overflow: auto"></ul>
	</div>
	 
	<div data-options="region: 'east', split: true, title: '分配用户', border:false" style="width: 500px; border-left:1px">
		<div id="menu_toolbar" class="easyui-toolbar" style="border-bottom: 1px solid #e1e1e1">
			<a onclick="saveMenuUser();" class="easyui-linkbutton"  data-options="plain: true, iconCls: 'icon-ok'" style="margin:3px 2px;">确认已分配的用户</a>
	    </div>
		<div>&nbsp;&nbsp;已分配的用户</div>
		<ul id="menuUserTree" class="ztree" style="width:97%;height:90%; border: none; overflow: auto; margin-top:0px;overflow: auto"></ul>
	</div>
	 -->
</div>
<div id="menuMng_window_div">
    <div id="menuMng_addMenu_div"></div>
	<div id="menuMng_addRoleMenu_div"></div>
</div>
<input id="isSelectRole" type="hidden" value="0"/>
<input id="isSelectUser" type="hidden" value="0"/>
<script type="text/javascript">
var selectNode=null;
var row = null;
$(function () {
	 loadMenuTree();//初始化权限菜单树
	 var url = "sys/role/findRoleByMenuUUID.action?menuUUID=";
	 loadMenuRoleTree(url);//初始化菜单角色树
	 
	 var url = "sys/user/findUserByMenuUUID.action?menuUUID=";
	 loadMenuUserTree(url);//初始化菜单用户树
});
//加载菜单树
function loadMenuTree(){
	var url="<%=path%>/system/menu/findMenuTree"; 
	initZTree("menuTree",url,addHoverDom,zTreeBeforeEditName,zTreeOnClick,zTreeBeforeRemove,setRenameBtn,setRemoveBtn);
}
//添加节点
function addHoverDom(treeId, treeNode){
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId + "' title='新增下级' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    //设置新增按钮
    if (btn) btn.bind("click", function(){
		//存储已选择节点
		selectNode = treeNode;
    	//执行新增方法
    	addOrUpMenu(treeNode.menuUUID,treeNode.menuCode,treeNode.menuName,"0");
        return false;
    });
};
//修改节点
function zTreeBeforeEditName(treeId, treeNode) {
	//var treeObj = $.fn.zTree.getZTreeObj("orgTree");//获取节点树
	 //var nodes = treeObj.getCheckedNodes(true);//获取选中的节点
	//存储已选择节点
	selectNode = treeNode;
	//执行修改方法
	addOrUpMenu(treeNode.menuUUID,treeNode.orgParentCode,treeNode.orgParentName,"1");
	return false;
}
//单击选择节点
function zTreeOnClick(event, treeId, treeNode) {
	//选择
	selectNode = treeNode;
		//刷新菜单对应的角色
	 var url = "system/role/findRoleByMenuUUID?menuUUID="+selectNode.menuUUID;
	 if($("#isSelectRole").val()=="1"){
		$.messager.confirm("操作提醒", "角色权限分配还没有保存，是否进行保存操作？", function (c) {
			 if(c){
				 //保存操作
				 saveRoleMenu();
				 var url = "sys/role/findRoleByMenuUUID.action?menuUUID="+selectNode.menuUUID;
				 loadMenuRoleTree(url);
			 }else{
				 var url = "sys/role/findRoleByMenuUUID.action?menuUUID="+selectNode.menuUUID;
				 loadMenuRoleTree(url);
			 }
			 $("#isSelectMenu").val("0");
		});
	 }else{
		 var url = "system/role/findRoleByMenuUUID?menuUUID="+selectNode.menuUUID;
		 loadMenuRoleTree(url);
	 }
	 
		//刷新菜单对应的用户
	 if($("#isSelectUser").val()=="1"){
		$.messager.confirm("操作提醒", "菜单用户分配还没有保存，是否进行保存操作？", function (c) {
			 if(c){
				 //保存操作
				 saveMenuUser();
				 var url = "system/user/findUserByMenuUUID?menuUUID="+selectNode.menuUUID;
				 loadMenuUserTree(url);//初始化菜单用户树
			 }else{
				 var url = "system/user/findUserByMenuUUID?menuUUID="+selectNode.menuUUID;
				 loadMenuUserTree(url);//初始化菜单用户树
			 }
			 $("#isSelectUser").val("0");
		});
	 }else{
		 var url = "system/user/findUserByMenuUUID?menuUUID="+selectNode.menuUUID;
		 loadMenuUserTree(url);//初始化菜单用户树
	 }
};
//删除节点
function zTreeBeforeRemove(treeId, treeNode) {	
	selectNode = treeNode;
	var url="<%=path%>/system/menu/deleteMenu?menuUUID="+treeNode.menuUUID;
	$.post(url,"",function(data){
		if(data){
			//操作成功
			//layer.msg('操作成功！', {icon: 1});
			 $.messager.alert('提示','操作成功!','info');
			//刷新
			loadMenuTree();
		}
	},'json');
	return false;
};
//修改节点按钮是否可用
function setRenameBtn(treeId,treeNode){
	return true;
}
function setRemoveBtn(treeId, treeNode) {
	//设置删除按钮是否显示 返回true则显示，返回fasle则不显示
	if(!treeNode.isParent){
		return true;
	}	
}
function removeHoverDom(treeId, treeNode) {  
   $("#addBtn_"+treeNode.tId).unbind().remove();
};
//加载菜单角色树
function loadMenuRoleTree(url){
	   $("#menuRoleTree").tree({
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
	        	$("#isSelectRole").val("1");
	        }
	   });
}
//初始化菜单用户树
function loadMenuUserTree(url){
   $("#menuUserTree").tree({
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
/**
 * 分配菜单权限角色
 */
function saveMenuRole(){	
	if(selectNode==null || selectNode==""){
		alert("请选择菜单数据！");
		return;
	}
	var nodes = $('#menuRoleTree').tree('getChecked');
	var selectStr = '';
	if($("#isSelectRole").val()=="0"){
		$.messager.alert("提示", "无角色改动，无需分配！", "warning");
		 return;
	}
	for(var i=0; i<nodes.length; i++){
		if (selectStr != '') selectStr += ',';
		selectStr += nodes[i].roleUUID;
	}
	if(selectStr==null || selectStr==""){
		$.messager.alert("提示", "请选择角色", "warning");
		return;
	}
	var postData = new Object();
	postData["menuUUIDs"]=selectNode.menuUUID;
	postData["roleUUIDs"]=selectStr;
	$.post("<%=path%>/system/menu/saveMenuRole",postData,function(data){
		if(data){
			//刷新菜单权限对应的角色
			 $.messager.alert("提示","操作成功！","info");
			 var url = "system/role/findRoleByMenuUUID?menuUUID="+selectNode.menuUUID;
			 loadMenuRoleTree(url);
			 $("#isSelectRole").val("0");
		}else{
			$.messager.alert("提示", "操作失败，请联系管理员！", "warning");
		}
	});
}



/**
 * 分配菜单权限用户
 */
function saveMenuUser(){	
	if(selectNode==null || selectNode==""){
		$.messager.alert("提示", "请选择菜单数据！", "warning");
		return;
	}
	var nodes = $('#menuUserTree').tree('getChecked');
	var selectStr = '';
	if($("#isSelectUser").val()=="0"){
		$.messager.alert("提示", "无用户改动，无需分配！", "warning");
		 return;
	}
	for(var i=0; i<nodes.length; i++){
		if (selectStr != '') selectStr += ',';
		selectStr += nodes[i].userUUID;
	}
	if(selectStr==null || selectStr==""){
		$.messager.alert("提示", "请选择用户", "warning");
		return;
	}
	var postData = new Object();
	postData["menuUUIDs"]=selectNode.menuUUID;
	postData["userUUIDs"]=selectStr;
	$.post("<%=path%>/sys/menu/saveMenuUser.action",postData,function(data){
		if(data){
			//刷新菜单权限对应的角色
			 $.messager.alert("提示","操作成功！","info");
			 var url = "sys/user/findUserByMenuUUID.action?menuUUID="+selectNode.menuUUID;
			 loadMenuUserTree(url);//初始化菜单用户树
			 $("#isSelectUser").val("0");
		}else{
			$.messager.show("提示", "操作失败，请联系管理员！", "warning");
		}
	});
}
/**
 * 添加或修改菜单权限
 */
function addOrUpMenu(menuUUID,menuParentCode,menuParentName,type){//新增/修改菜单	
	$("#menuMng_addMenu_div").remove();
	$("#menuMng_window_div").append("<div id='menuMng_addMenu_div'></div>");
	var url = "menu/addMenu.jsp?menuUUID="+menuUUID+"&menuParentCode="+menuParentCode+"&menuParentName="+menuParentName+"&isEdit="+type;
	var title = '新增菜单';
	if("1"==type){
		title = '修改菜单';
	}
	$("#menuMng_addMenu_div").dialog({
		title : title,
		width : 605,
		height : 300,
		closed : false,
		eache : false,
		href : url,
		modal : true,
		buttons:[{
			text:'保存',
			iconCls: 'icon-mini-tick',
			handler:function(){saveMenu();}
		},{
			text:'取消',
			iconCls: 'icon-cancel',
			handler:function(){
				$('#menuMng_addMenu_div').dialog('close');
				$("#menuMng_addMenu_div").remove();
		    }
		}]
	});
}
</script>
