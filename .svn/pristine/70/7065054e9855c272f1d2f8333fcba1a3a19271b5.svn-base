<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String regionalUUID = request.getParameter("regionalUUID");
String isEdit = request.getParameter("isEdit");
String regionalParentCode = request.getParameter("regionalParentCode");
String regionalName = request.getParameter("regionalName");
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
<form id="addMenu_form" method="post">
    <input type="hidden" name="id" id="id"/>
	<input type="hidden" name="uuid" id="regionalUUID"/>
	<input type="hidden" name="regionalCode" id="regionalCode"/>
    <input type="hidden" name="regionalParentCode" id="regionalParentCode" value="-1"/>  
    <input type="hidden" name="regionalNameTow" id="regionalNameTow"/>
	<table data-options="fit:true" style="width:98%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr class="lattice1">
			<td align="right">
				<label class="label-name">区域名称:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 380px" name="regionalName" id="regionalName" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr class="lattice">
			<td valign="top" align="right">
				<label class="label-name">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
			</td>
			<td>
				<textarea class="easyui-validatebox" name="regionalMark" id="regionalMark" style="width: 380px" rows="4" maxlength="100"></textarea> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(function(){
	   $("#regionalUUID").val("<%=regionalUUID%>");
	   //初始化
	   if("1"=="<%=isEdit%>" && ""!="<%=regionalUUID%>"){
		   //是编辑数据
		   $("#addMenu_form").form('load', "<%=path%>/system/cls/regionalId?regionalUUID=<%=regionalUUID%>");
		   //$("#orgParentName").removeAttr("onclick");
	   }else{
		   //是添加数据
		   $("#regionalParentCode").val("<%=regionalParentCode%>");
		   $("#regionalNameTow").val("<%=regionalName%>");
	   }
});
function saveMenu(){
	   $.post("<%=path%>/system/cls/regionalSave",$("#addMenu_form").serialize(),function(data){
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