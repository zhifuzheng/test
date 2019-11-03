<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String roleUUID = request.getParameter("roleUUID");
String isEdit = request.getParameter("isEdit");
%>
<style>
	.label-name {
    width: auto;
    text-align: left;
    display: block;
    padding-left: 10px;
}
.lattice1 {
    margin-top: 0px;
    float: left;
}
.lattice {
    margin-top: 10px;
    float: left;
}
</style>
<form id="addRole_form" method="post">
    <input type="hidden" name="id" id="roleId"/>
	<input type="hidden" name="uuid" id="roleUUID"/>
    <input type="hidden" name="createTime" id="createTime">
    <input type="hidden" name="createroleUUID" id="createroleUUID"> 
    <input type="hidden" name="createUserName" id="createUserName">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr class="lattice1">
			<td align="right">
				<label class="label-name">角色名称:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:400px" name="roleName" id="roleName" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr class="lattice">
			<td valign="top" align="right">
				<label class="label-name">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
			</td>
			<td>
				<textarea class="easyui-validatebox" name="roleMark" id="roleMark" style="width: 400px" rows="4" maxlength="100"></textarea> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(function(){
	   $("#roleUUID").val("<%=roleUUID%>");
	   //初始化
	   if("1"=="<%=isEdit%>" && "" != "<%=roleUUID%>"){
		   //是编辑数据
		   $("#addRole_form").form('load', "<%=path%>/system/role/findRole?roleUUID=<%=roleUUID%>");
		   //$("#orgParentName").removeAttr("onclick");
	   }
});
function saveRole(){
	   $.post("<%=path%>/system/role/saveOrUpRole",$("#addRole_form").serialize(),function(data){
		  if(data){
			  //$.messager.show({ icon: "info", msg: "error" });
			  //$.messager.show("提示", "操作成功！", "info", "topCenter");
			  $.messager.alert('提示','操作成功!','info');
			  //刷新列表
			  $('#role_table').datagrid('reload');
			  //关闭窗口
			  $('#role_addRole_div').dialog('close');
			  $("#role_addRole_div").remove();
		  }else{
			  $.messager.alert("提示", "操作失败！请联系管理员！", "error");
		  }
	   });
}
</script>