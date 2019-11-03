<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>用户信息编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" href="static/css/pintuer.css">
	<link rel="stylesheet" href="static/css/admin.css">
	
	<script src="static/js/jquery.js"></script>
	<script src="static/js/pintuer.js"></script>
	<script src="static/js/respond.js"></script>
	<script src="static/js/layer.js"></script>
  </head>
  
<body>
	<div id="container" style="margin:0 auto"></div>
	<div id="myDialog" onload="onLoad()" style="margin-top:10px">
		<div class="dialog-body" style="margin-left:30px">
			<form id="pwdEditForm" method="post" class="form-small" style="width:94%;" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id">
				<input type="hidden" id="userUUID" name="userUUID">
				<input type="hidden" id="loginName" name="loginName">
				<input type="hidden" id="userName" name="userName">
				<input type="hidden" id="userSex" name="userSex">
				<input type="hidden" id="userType" name="userType">
				<input type="hidden" id="userMobile" name="userMobile">
				<input type="hidden" id="userPwd" name="userPwd">
				<input type="hidden" id="userStatus" name="userStatus">
				<input type="hidden" id="createTime" name="createTime">
				<input type="hidden" id="updateTime" name="updateTime">
				<input type="hidden" id="simpleSpelling" name="simpleSpelling">
				<input type="hidden" id="fullSpelling" name="fullSpelling">
				<input type="hidden" id="queryCombination" name="queryCombination">
				<div class="form-group">
					<div class="label">
						<label for="oldPwd"> 旧密码 </label>
					</div>
					<div class="field">
						<input type="text" class="input" id="oldPwd" name="oldPwd" size="36" maxlength="36" value="" placeholder="请输入用户登录名">
					</div>
				</div>
				<div class="form-group">
					<div class="label">
						<label for="newPwd"> 新密码 </label>
					</div>
					<div class="field">
						<input type="text" class="input" id="newPwd" name="newPwd" size="36" maxlength="36" value="" placeholder="请输入用户姓名">
					</div>
				</div>
				<div class="form-group">
					<div class="label">
						<label for="confirmPwd"> 确认新密码 </label>
					</div>
					<div class="field">
						<input type="text" class="input" id="confirmPwd" name="confirmPwd" size="36" maxlength="36" value="" placeholder="请输入手机号码">
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="hidden">
		<script type="text/javascript">
			function validate() {
				if (!$('#oldPwd').val()) {
					layer.msg('旧密码不能为空！', {
						icon : 2
					});
					return false;
				}
				if (!$('#newPwd').val()) {
					layer.msg('新密码不能为空！', {
						icon : 2
					});
					return false;
				}
				if (!$('#confirmPwd').val()) {
					layer.msg('密码确认不能为空！', {
						icon : 2
					});
					return false;
				}
				if (!$('#confirmPwd').val()==$('#newPwd').val()) {
					layer.msg('两次密码输入不一致！', {
						icon : 2
					});
					return false;
				}
				return true;
			}
			//初始化数据
			function getFromSerialize() {
				return $('#pwdEditForm').serialize();
			}
		</script>
	</div>
</body>
</html>
