<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String uuid = request.getParameter("uuid");
%>
<style>
	.heading{
		display: block;
		width: 90px;
		text-align-last: justify;
		padding-left: 15px;
	}
</style>
<form id="grade_form" method="post">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">需求标题：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="commodityTitle" id="commodityTitle" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">需求详情：</label>
			</td>
			<td>
				<input class="easyui-textbox" name="details" id="details" data-options="multiline:true" style="height:60px;width:480px;">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">详情图：</label>
			</td>
			<td>
				<img alt="" src="" width="100%" height="100%" id="detailsImg" name="detailsImg"> 
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">图片：</label>
			</td>
			<td>
				<img alt="" src="" width="100%" height="100%" id="demandImg" name="demandImg"> 
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	var path = "<%=basePath%>";
	$(function(){
		var id  = "<%=uuid%>";
		if("" != id){
			var url="<%=path%>/Public/sp/gxDemandId?demandUUID=<%=uuid%>";
			$.post(url,{},function(data){
				$("#commodityTitle").textbox("setValue",data.commodityTitle);
				$("#details").textbox("setValue",data.details);
				$("#detailsImg").attr("src",path+data.detailsImg);
				$("#demandImg").attr("src",path+data.demandImg);
			},'json');
		}
	})
	
	
</script>