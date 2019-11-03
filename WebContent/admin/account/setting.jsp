<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style>
	.center {
		color: highlight;
		font-size: 18px;
		text-align: center;
		background-color: #EAEAEA;
	}
	.radioSpan {
	    position: relative;
	    border: 0 solid #95B8E7;
	    background-color: #fff;
	    vertical-align: middle;
	    display: inline-block;
	    overflow: hidden;
	    font-size:15px;
	    white-space: nowrap;
	    margin: 0;
	    padding: 0px;
	    -moz-border-radius: 5px 5px 5px 5px;
	    -webkit-border-radius: 5px 5px 5px 5px;
	    border-radius: 5px 5px 5px 5px;
	    display:block;
    }
	.buttonSubmit {
		width: 100px;
		height: 33px;
		background-color: #02a2ff;
		color: #fff !important;
	}
	.buttonSubmit:hover{
		color: #000 !important;
	}
</style>
<div style="padding:0px 0px 0px 10px">
	<form id="itemAddForm" class="itemForm" method="post" style="position: relative;">
		<table style="border-collapse:separate; border-spacing:30px 20px;">
			<tr>
				<td colspan="2" align="center" class="center">商户提现设置</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">佣金提现方式</td>
				<td>
					<span class="radioSpan">
		   				<input type="checkbox" name="busChannel" value="1"><span style="color:green;">提现到银行卡</span>
	   					<input type="checkbox" name="busChannel" value="2"><span style="color:red;">提现到微信</span>
		   			</span>
				</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">最低提现金额</td>
				<td>
	   				<input class="easyui-numberbox" type="text" name="busMin" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">商户提现手续费</td>
				<td>
	   				<input class="easyui-numberbox" type="text" name="busRatio" data-options="min:0,max:100,precision:0,required:true" style="width:280px;"/>%
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" class="center">佣金提现设置</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">商户提现方式</td>
				<td>
					<span class="radioSpan">
		   				<input type="checkbox" name="disChannel" value="1"><span style="color:green;">提现到银行卡</span>
	   					<input type="checkbox" name="disChannel" value="2"><span style="color:red;">提现到微信</span>
		   			</span>
				</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">最低提现金额</td>
				<td>
	   				<input class="easyui-numberbox" type="text" name="disMin" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>
				</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">佣金提现手续费</td>
				<td>
	   				<input class="easyui-numberbox" type="text" name="disRatio" data-options="min:0,max:100,precision:0,required:true" style="width:280px;"/>%
				</td>
			</tr>
		</table>
		<div style="margin-left: 180px;width: 500px;text-align: left;bottom: -100px;position: absolute;">
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="submitForm()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="clearForm()">重置</a>
		</div>
	</form>

</div>
<script type="text/javascript">
	$("#itemAddForm [numberboxname=busMin]").numberbox("setValue","3");
	var witUUID;
	$.ajax({
		async:false,
		type:"post",
		url:'<%=basePath %>system/account/findWitSetting',
		dataType:'json',
		success:function(data){
			if(data){
				witUUID = data.witUUID;
				if(data.busChannel == 3){
					$("#itemAddForm input[name='busChannel']").attr("checked",true);
				}else{
					$("#itemAddForm input[name='busChannel'][value='"+data.busChannel+"']").attr("checked",true);
				}
				
				if(data.disChannel == 3){
					$("#itemAddForm input[name='disChannel']").attr("checked",true);
				}else{
					$("#itemAddForm input[name='disChannel'][value='"+data.disChannel+"']").attr("checked",true);
				}
				
				$("#itemAddForm [name=busMin]").val((data.busMin/100).toFixed(2));
				$("#itemAddForm [name=busRatio]").val(data.busRatio*100);
				$("#itemAddForm [name=disMin]").val((data.disMin/100).toFixed(2));
				$("#itemAddForm [name=disRatio]").val(data.disRatio*100);
			}else{
				$.messager.alert('提示','当前网络繁忙，请稍后重试','warning');
			}
		}
	});
	function submitForm(){
		var busChannel;
		var busbox = $("#itemAddForm input[name=busChannel]:checked");
		if(busbox.length == 2){
			busChannel = 3;
		}else{
			busChannel = $("#itemAddForm input[name=busChannel]:checked").val();
		}
		
		var disChannel;
		var disbox = $("#itemAddForm input[name=disChannel]:checked");
		if(disbox.length == 2){
			disChannel = 3;
		}else{
			disChannel = $("#itemAddForm input[name=disChannel]:checked").val();
		}
		
		var busMin = $("#itemAddForm [numberboxname=busMin]").numberbox("getValue");
		var busRatio = $("#itemAddForm [numberboxname=busRatio]").numberbox("getValue");
		var disMin = $("#itemAddForm [numberboxname=disMin]").numberbox("getValue");
		var disRatio = $("#itemAddForm [numberboxname=disRatio]").numberbox("getValue");
		
		if(!busChannel || !disChannel || !$('#itemAddForm').form('validate')){
			$.messager.alert('提示','表单还没填完哦，请填完表单后再提交!','warning');
			return;
		}
		
		$.post(
			baseURL+"system/account/saveWitSetting",
			{'witUUID':witUUID,'busChannel':busChannel,'busMin':busMin*100,'busRatio':(busRatio/100).toFixed(2),'disChannel':disChannel,'disMin':disMin*100,'disRatio':(disRatio/100).toFixed(2)},
			function(data){
				if(data){
					$.messager.alert('提示','保存成功','info');
				}else{
					$.messager.alert('提示','当前网络繁忙，请稍后重试','warning');
				}
			},
			'json'
		);
	}
	function clearForm(){
		$("#itemAddForm input:checkbox").attr("checked",false);
		$("#itemAddForm [numberboxname=busMin]").numberbox("setValue","");
		$("#itemAddForm [numberboxname=busRatio]").numberbox("setValue","");
		$("#itemAddForm [numberboxname=disMin]").numberbox("setValue","");
		$("#itemAddForm [numberboxname=disRatio]").numberbox("setValue","");
	}
</script>