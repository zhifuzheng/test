<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String settledUUID = request.getParameter("settledUUID");
%>
<style>
	.heading{
		display: block;
		width: 90px;
		text-align-last: justify;
		padding-left: 15px;
	}
</style>
<form id="settled_form" method="post">
	<input type="hidden" name="id" id="id">
	<input type="hidden" name="settledUUID" id="settledUUID" value="<%= settledUUID%>">
	<input type="hidden" name="shuoMing" id="shuoMing">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">金额：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="money" id="money" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">选择续费天数:</label>
			</td>
			<td>
				<select id="settledType" name="settledType" class="easyui-combobox" editable=false; style="width:480px;height:32px">
				    <option value="">请选择</option>
				    <option value="0">一个星期(7)</option>
				    <option value="1">半年(185)</option>
				    <option value="2">一年(365)</option>
				</select>	
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	$(function(){
		var settledUUID = "<%=settledUUID%>";
		if("null" != settledUUID){
			var url="<%=path%>/Public/business/settledId?settledUUID=<%=settledUUID%>";
			$.post(url,{},function(data){
				$("#id").val(data.id);
				$("#money").textbox('setValue',data.money);
				$("#settledType").combobox('setValue',data.settledType);
				$("#shuoMing").val(data.shuoMing);
			},'json');
		}
	})
	
	function save(){
		var sm = $("#settledType").find("option:selected").text();
		$("#shuoMing").val(sm);
		if($("#money").val() == ""){
			$.messager.alert('提示',"请输入金额",'error');
		}else if($("#settledType").val() == ""){
			$.messager.alert('提示',"请选择类型",'error');
		}else{
			$.post("<%=path%>/Public/business/settledSave",$("#settled_form").serialize(),function(data){
				$.messager.alert('提示',data.msg,'info');
				$('#settledList_addSettled_div').dialog('close');
				$("#settledList_addSettled_div").remove();
				//刷新列表
				$('#settled_table').datagrid('reload');
		    },'json');
		}
	}
	
	
</script>