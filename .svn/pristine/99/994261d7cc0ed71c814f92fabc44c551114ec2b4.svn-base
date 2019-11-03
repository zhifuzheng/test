<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String businessUUID = request.getParameter("businessUUID");
%>
<form id="fx_form" method="post">
	<input type="hidden" name="businessUUID" id="businessUUID" value="<%=businessUUID %>">
	<input type="hidden" name="distributionUUID" id="distributionUUID">
	<input type="hidden" name="id" id="id">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label>是否开启分销商:</label>
			</td>
			<td>
				<select id="openDistribution" name="openDistribution" class="easyui-combobox" editable=false; style="width:480px;height:32px">
				    <option value="">请选择</option>
				    <option value="0">开启</option>
				    <option value="1">关闭</option>
				</select>	
			</td>
		</tr>
		<tr>
			<td align="right">
				<label>设置分销层级:</label>
			</td>
			<td>
				<select id="hierarchy" name="hierarchy" class="easyui-combobox" editable=false; style="width:480px;height:32px">
				    <option value="">请选择</option>
				    <option value="0">一级</option>
				    <option value="1">二级</option>
				</select>	
			</td>
		</tr>
		<tr style="display: none;" id="on">
			<td align="right">
				<label>一级佣金比列:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="onPercentage" id="onPercentage" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr style="display: none;" id="two">
			<td align="right">
				<label>二级佣金比列:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="twoPercentage" id="twoPercentage" maxlength="20" size="20"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	//设置分销层级
	$(document).ready(function () {
		$("#hierarchy").combobox({
			onChange: function (n,o) {
				if($("#hierarchy").val() == 0){
					$("#on").show();
					$("#two").hide();
					$("#twoPercentage").textbox("setValue",'');
				}
				if($("#hierarchy").val() == 1){
					$("#two").show();
					$("#on").hide();
					$("#onPercentage").textbox("setValue",'');
				}
			}
		});
	});
	
	$(function(){
		var url = "<%=path%>/Public/business/distributionUUID?businessUUID=<%=businessUUID%>";
		$.post(url,{},function(data){
			$("#id").val(data.id);
			$("#distributionUUID").val(data.distributionUUID);
			$("#openDistribution").combobox('setValue',data.openDistribution);
			$("#hierarchy").combobox('setValue',data.hierarchy);
			$("#onPercentage").textbox("setValue",data.onPercentage);
			$("#twoPercentage").textbox("setValue",data.twoPercentage);
		},'json');
	})
	
	function distributionSave(){
		if("" == $("#openDistribution").val()){
			$.messager.alert('提示',"请选择是否开启分销商",'error');
			return;
		}
		if($("#hierarchy").val() == ""){
			$.messager.alert('提示',"请选择设置分销层级",'error');
			return;
		}
		if($("#hierarchy").val() == 0){
			if($("#onPercentage").val() == ""){
				$.messager.alert('提示',"请输入一级佣金比列",'error');
				return;
			}
		}
		if($("#hierarchy").val() == 1){
			if($("#twoPercentage").val() == ""){
				$.messager.alert('提示',"请输入二级佣金比列",'error');
				return;
			}
		}
		$("#fx_form").ajaxSubmit({
			url:'<%=path%>/Public/business/distributionSave',
			type:'post',
			dataType:'json',
			semantic:true,
			success : function(data){
				$.messager.alert('提示',data.msg,'info');
				$('#distributionList_addDistribution_div').dialog('close');
				$("#distributionList_addDistribution_div").remove();
			},
		});
	}
</script>