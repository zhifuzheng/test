<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pfUUID = request.getParameter("pfUUID");
%>
<style>
	.heading{
		display: block;
		width: 90px;
		text-align-last: justify;
		padding-left: 15px;
	}
</style>
<form id="ps_form" method="post">
	<input type="hidden" name="id" id="id">
	<input type="hidden" name="pfUUID" id="pfUUID" value="<%=pfUUID%>">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">批发市场名称：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="pfName" id="pfName" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">批发市场地址：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="pfAdd" id="pfAdd" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">批发市场简介：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="pfSynopsis" id="pfSynopsis" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">店面照片:</label>
			</td>
			<td>
				<input id="storefrontImg" name="storefrontImg" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoStorefrontImg" style="display: none;">
			<td align="right">
				<label class="heading">店面照片:</label>
			</td>
			<td>
				<img src="" id="getStorefrontImg" width="480px;" height="150px;">
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var path = "<%=basePath%>";//图片路径
	$(function(){
		var pfUUID = "<%=pfUUID%>";
		if("null" != pfUUID){
			var url="<%=path%>/Public/business/wholesaleMarketId?pfUUID=<%=pfUUID%>";
			$.post(url,{},function(data){
				//alert(JSON.stringify(data));
				$("#id").val(data.id);
				$("#pfName").textbox('setValue',data.pfName);
				$("#pfAdd").textbox('setValue',data.pfAdd);
				$("#pfSynopsis").textbox('setValue',data.pfSynopsis);
				$("#getStorefrontImg").attr("src",path+data.storefrontImg);
				$("#shoStorefrontImg").show();
			},'json');
		}
	})

	
	//选择文件
	$('#storefrontImg').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//显示批发市场门店图片
	$(":input[name='storefrontImg']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('getStorefrontImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "150";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoStorefrontImg").show();
	});
	
	
	
	//保存或修改
	function pfSave(){
		if("" == $("#pfName").val()){
			$.messager.alert('提示',"请输入批发市场名称",'error');
		}else if("" == $("#pfAdd").val()){
			$.messager.alert('提示',"请输入批发市场地址",'error');
		}else if("" == $("#pfSynopsis").val()){
			$.messager.alert('提示',"请输入批发市场简介",'error');
		}else if("<%=pfUUID%>" == "null" && "" == $("#storefrontImg").filebox('getValue')){
			$.messager.alert('提示',"请选择店面照片",'error');
		}else{
			//请求后台
			$("#ps_form").ajaxSubmit({
				url:'<%=path%>/Public/business/pfSave',
				type:'post',
				dataType:'json',
				semantic:true,
				success : function(data){
					$.messager.alert('提示',data.msg,'info');
					$('#pfList_addPf_div').dialog('close');
					$("#pfList_addPf_div").remove();
					//刷新列表
					$('#pf_table').datagrid('reload');
				},
			});
		}
	}
	
	
</script>