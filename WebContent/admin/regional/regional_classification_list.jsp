<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<div id="dict-layout" data-options="fit:true" class="easyui-layout">
	<div data-options="region: 'west',split: true, title: '区域管理'" iconCls="icon-menu" style="width: 100%;" border="0">
		<div class="tool" style="border-bottom: 1px solid #e1e1e1">
			<a onclick="addOrUpMenu('','-1','顶级菜单','0');" class="easyui-linkbutton" data-options="plain: true, iconCls: 'icon-mini-add-h'" style="margin:3px 2px;">新增区域</a> 
		</div>
	    <ul id="menuTree" class="ztree" style="width:97%;height:90%; border: none; overflow: auto; margin-top:0px;overflow: auto"></ul>
	</div>
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
});
//加载菜单树
function loadMenuTree(){
	var url="<%=path%>/system/cls/regionalAll"; 
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
    	addOrUpMenu(treeNode.regionalUUID,treeNode.regionalCode,treeNode.regionalName,"0");
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
	addOrUpMenu(treeNode.regionalUUID,treeNode.orgParentCode,treeNode.orgParentName,"1");
	return false;
}
//单击选择节点
function zTreeOnClick(event, treeId, treeNode) {
	
	 
};
//删除节点
function zTreeBeforeRemove(treeId, treeNode) {
	selectNode = treeNode;
	var url="<%=path%>/system/cls/regionalDel?regionalUUID="+treeNode.regionalUUID;
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



/**
 * 添加或修改菜单权限
 */
function addOrUpMenu(regionalUUID,regionalParentCode,regionalName,type){//新增/修改菜单	
	$("#menuMng_addMenu_div").remove();
	$("#menuMng_window_div").append("<div id='menuMng_addMenu_div'></div>");
	var url = "regional/regional_classification_save.jsp?regionalUUID="+regionalUUID+"&regionalParentCode="+regionalParentCode+"&regionalName="+regionalName+"&isEdit="+type;
	var title = '新增区域';
	if("1"==type){
		title = '修改区域';
	}
	$("#menuMng_addMenu_div").dialog({
		title : title,
		width : 490,
		height : 240,
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
