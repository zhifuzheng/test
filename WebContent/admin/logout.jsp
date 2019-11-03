<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
session.invalidate();
%>
<script>
//浏览器重定向到新的页面
window.location = '<%=path %>/admin/login.jsp';
</script>
