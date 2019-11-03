<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String menuUUID = request.getParameter("menuUUID");
String isEdit = request.getParameter("isEdit");
String menuParentCode = request.getParameter("menuParentCode");
String menuParentName = request.getParameter("menuParentName");
%>
<form id="addMenu_form" method="post">
    <input type="hidden" name="id" id="menuId"/>
	<input type="hidden" name="uuid" id="menuUUID"/>
	<input type="hidden" name="menuCode" id="menuCode"/>
    <input type="hidden" name="menuParentCode" id="menuParentCode" value="-1"/>  
    <input type="hidden" id="menuParentName" name="menuParentName" value="顶级菜单"/>  
    <input type="hidden" name="createTime" id="createTime">
    <input type="hidden" name="createUserUUID" id="createUserUUID">
    <input type="hidden" name="createUserName" id="createUserName">
	<table data-options="fit:true" style="width:98%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label>菜单名称:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 480px" name="menuName" id="menuName" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>菜单权限标识:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 480px" name="menuPermission" id="menuPermission" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td valign="top" align="right">
				<label>备注:</label>
			</td>
			<td>
				<textarea class="easyui-validatebox" name="menuMark" id="menuMark" style="width: 480px" rows="4" maxlength="100"></textarea> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(function(){
	   $("#menuUUID").val("<%=menuUUID%>");
	   //初始化
	   if("1"=="<%=isEdit%>" && ""!="<%=menuUUID%>"){
		   //是编辑数据
		   $("#addMenu_form").form('load', "<%=path%>/system/menu/findMenu?menuUUID=<%=menuUUID%>");
		   //$("#orgParentName").removeAttr("onclick");
	   }else{
		   //是添加数据
		   $("#menuParentCode").val("<%=menuParentCode%>");
		   $("#menuParentName").val("<%=menuParentName%>");
	   }
});
function saveMenu(){
	   $.post("<%=path%>/system/menu/saveOrUpMenu",$("#addMenu_form").serialize(),function(data){
		  if(data){
			  $.messager.alert('提示','操作成功!','info');
			  //刷新列表
			  loadMenuTree();
			  //关闭窗口
			  $('#menuMng_addMenu_div').dialog('close');
			  $('#menuMng_addMenu_div').remove();
		  }else{
			  $.messager.alert("提示", "操作失败！请联系管理员！", "error");
		  }
	   });
}
</script>