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
				<td colspan="2" align="center" class="center">基础设置</td>
			</tr>
			<tr>
				<td style="font-size: 15px;">
					是否开启分销
				</td>
				<td>
					<span class="radioSpan">
		   				<input type="radio" name="isDis" value="1"><span style="color: green;">开启分销</span>
	   					<input type="radio" name="isDis" value="0"><span style="color:red;">关闭分销</span>
	   					（关闭后将会隐藏分销中心入口）
		   			</span>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center" class="center">分销规则</td>
			</tr>
			<tr>
				<td colspan="2">
					<textarea name="rules" style="width:500px; height:400px; font-size: 16px;"></textarea>
				</td>
			</tr>
		</table>
		<div style="margin-left: 160px;width: 500px;text-align: left;bottom: -30px;position: absolute;">
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="submitForm()">保存</a>
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="clearForm()">重置</a>
		</div>
	</form>

</div>
<script type="text/javascript">
	var disUUID;
	$.ajax({
		async:false,
		type:"post",
		url:'<%=basePath %>system/account/findDisSetting',
		dataType:'json',
		success:function(data){
			if(data){
				disUUID = data.disUUID;
				$("#itemAddForm input[name='isDis'][value='"+data.isDis+"']").attr("checked",true);
				$('#itemAddForm [name=rules]').val(data.rules);
			}else{
				$.messager.alert('提示','当前网络繁忙，请稍后重试','warning');
			}
		}
	});
	//提交表单
	function submitForm(){
		//表单校验
		var isDis = $("#itemAddForm input[name='isDis']:checked").val();
		var rules = $('#itemAddForm [name=rules]').val();
		
		if(!isDis){
			$.messager.alert('提示','请选择是否开启分销','warning');
			return;
		}
		if(!rules){
			$.messager.alert('提示','请输入分销规则','warning');
			return;
		}
		
		$.post(
			baseURL+"system/account/saveDisSetting",
			{'disUUID':disUUID,'isDis':isDis,'rules':rules},
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
		$("#itemAddForm input[name='isDis']").prop('checked',false);
		$('#itemAddForm [name=rules]').val('');
	}
</script>