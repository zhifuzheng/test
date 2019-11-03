<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String userUUID = request.getParameter("userUUID");
String isEdit = request.getParameter("isEdit");
String menuParentCode = request.getParameter("menuParentCode");
String menuParentName = request.getParameter("menuParentName");
%>
<form id="addUser_form" method="post">
    <input type="hidden" name="id" id="userId"/>
	<input type="hidden" name="uuid" id="userUUID"/>
    <input type="hidden" name="createTime" id="createTime">
    <input type="hidden" name="createUserUUID" id="createUserUUID"> 
    <input type="hidden" name="createUserName" id="createUserName">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label>登陆账号:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:300px" name="loginName" id="loginName" required="required" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>登陆密码:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="password" style="width:300px"  name="userPwd" id="userPwd" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>用户名:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 300px" name="userName" id="userName" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>用户手机:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 300px" name="userMobile" id="userMobile" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>用户邮箱:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width: 300px" name="userEmail" id="userEmail" required="required" maxlength="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>用户类型:</label>
			</td>
			<td>
				<select class="easyui-combobox" name="userType" id="userType" style="width:300px">
				     <option value="2">一般人员</option>
				     <option value="1">管理员</option>			     
				</select>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>性别:</label>
			</td>
			<td>
				<select class="easyui-combobox" name="userSex" id="userSex" style="width:300px">
				     <option value="1">男</option>
				     <option value="2">女</option>				     
				</select>
			</td>
		</tr>
		<tr>
			<td valign="top" align="right">
				<label>备注:</label>
			</td>
			<td>
				<textarea class="easyui-validatebox" name="userDescription" id="userDescription" style="width: 300px" rows="4" maxlength="100"></textarea> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
$(function(){
	   //$("#userUUID").val("<%=userUUID%>");
	   //初始化
	   if("1"=="<%=isEdit%>" && ""!="<%=userUUID%>"){
		   //是编辑数据
		   $("#addUser_form").form('load', "<%=path%>/system/user/findUserInfo?userUUID=<%=userUUID%>");
	   }
});
function saveUser(){
		if(!$('#addUser_form').form('validate')){
			$.messager.alert('提示','表单还没填完哦，请填完表单后再提交!','warning');
			return;
		}
	   $.post("<%=path%>/system/user/addOrUpUser",$("#addUser_form").serialize(),function(data){
		  if(data){
			  $('#userList_addUser_div').dialog('destroy');
			  //刷新列表
			  $('#userinfo_table').datagrid('reload');
		  }else{
			  $.messager.show("提示", "操作失败！请联系管理员！", "error");
		  }
	   });
}
</script>