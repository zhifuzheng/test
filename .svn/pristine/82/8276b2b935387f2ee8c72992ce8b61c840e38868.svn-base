<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String businessUUID = request.getParameter("businessUUID");
%>
<form id="sj_form" method="post">
	<input type="hidden" name="businessUUID" id="businessUUID" value="<%=businessUUID %>">
	<input type="hidden" name="id" id="id">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label>审批状态:</label>
			</td>
			<td>
				<select id="approvalStatus" name="approvalStatus" style="width:480px; height: 32px;" onchange="sub()">
				    <option value="">请选择</option>
				    <option value="1">审批通过</option>
				    <option value="2">审批不通过</option>
				</select>
			</td>
		</tr>
		<tr style="display: none;" id="sp">
			<td align="right">
				<label>审批意见:</label>
			</td>
			<td>
				<textarea id="reason" name="reason" class="easyui-validatebox" data-options="required:true" style="width: 480px; height: 32px;"></textarea>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	/* $("#approvalStatus").combobox({
	    onChange:function(){
	      	if($("#approvalStatus").val() == 1 || $("#approvalStatus").val() == ""){
	      		alert("0")
	      		$("#sp").hidden();
	      	}else if($("#approvalStatus").val() == 2){
	      		$("#sp").show();
	      	}
	    }
	}); */
	function sub(){
		var approvalStatus = $("#approvalStatus").val();
		if(approvalStatus == 2){
			$("#sp").show();
		}else if(approvalStatus == 1){
			$("#sp").hide();
		}else if(approvalStatus == ""){
			$("#sp").hide();
		}
	}
	
	$(function(){
		var url = "<%=path%>/Public/business/applyId?businessUUID=<%=businessUUID%>";
		$.post(url,{},function(data){
			$("#id").val(data.id);
			//alert(JSON.stringify(data.id));
		},'json');
	})
	
	function spSave(){
		if("" == $("#id").val() || $("#id").val() == null){
			$.messager.alert('提示',"系统繁忙请刷新页面重试",'error');
			return;
		}
		if("" == $("#approvalStatus").val()){
			$.messager.alert('提示',"请选择审批状态",'error');
		}else if($("#approvalStatus").val() == 2 && "" == $("#reason").val()){
			$.messager.alert('提示',"请输入审批意见",'error');
		}else{
			$("#sj_form").ajaxSubmit({
				url:'<%=path%>/Public/business/businessSave',
				type:'post',
				dataType:'json',
				semantic:true,
				success : function(data){
					$.messager.alert('提示',data.msg,'info');
					$('#passList_addPass_div').dialog('close');
					$("#passList_addPass_div").remove();
					//刷新列表
					$('#sj_table').datagrid('reload');
				},
			});
		}
	}
</script>