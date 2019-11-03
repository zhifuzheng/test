<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String gradeUUID = request.getParameter("gradeUUID");
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
	<input type="hidden" name="id" id="id">
	<input type="hidden" name="gradeUUID" id="gradeUUID" value="<%= gradeUUID%>">
	<input type="hidden" name="gradeName" id="gradeName">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr id="grade">
			<td align="right">
				<label class="heading">选择等级：</label>
			</td>
			<td>
				<select id="number" name="number" style="width:300px;height:32px">
				    <option value="">请选择</option>
				    <option value="1">一级</option>
				    <option value="2">二级</option>
				    <option value="3">三级</option>
				    <option value="4">四级</option>
				    <option value="5">五级</option>
				    <option value="6">六级</option>
				    <option value="7">七级</option>
				    <option value="8">八级</option>
				    <option value="9">九级</option>
				    <option value="10">十级</option>
				</select>	
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">等级名称：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:300px" name="aliasName" id="aliasName" maxlength="20" size="20"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	$(function(){
		$("#grade").show();
		var gradeUUID = "<%=gradeUUID%>";
		if("" != gradeUUID){
			$("#grade").css("display","none");
			var url="<%=path%>/Public/business/businessGradeId?gradeUUID=<%=gradeUUID%>";
			$.post(url,{},function(data){
				$("#id").val(data.id);
				$("#number").val(data.number);
				$("#aliasName").textbox("setValue",data.aliasName);
			},'json');
		}
	})
	
	function gradeSave(){
		$("#gradeName").val($("select[name='number']").find("option:selected").text());
		if($('#number').val() == ""){
			$.messager.alert('提示',"请选择等级",'error');
		}else if($("#aliasName").val() == ""){
			$.messager.alert('提示',"请输入等级名称",'error');
		}else{
			$.post("<%=path%>/Public/business/gradeSave",$("#grade_form").serialize(),function(data){
				$.messager.alert('提示',data.msg,'info');
				$('#gradeList_addGrade_div').dialog('close');
				$("#gradeList_addGrade_div").remove();
				//刷新列表
				$('#grade_table').datagrid('reload');
		    },'json');
		}
	}
	
	
</script>