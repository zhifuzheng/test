<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String businessUUID = request.getParameter("businessUUID");
%>
<style>
	.heading{
		display: block;
		width: 90px;
		text-align-last: justify;
		padding-left: 15px;
	}
</style>
<form id="sj_form" method="post">
	<input type="hidden" value="0" name="approvalStatus" id="approvalStatus">
	<input type="hidden" name="id" id="id">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">入驻类型:</label>
			</td>
			<td>
				<select id="applyType" name="applyType" class="easyui-combobox" editable=false; style="width:480px;height:32px">
				    <option value="">请选择</option>
				    <option value="0">零售商</option>
				    <option value="1">供应商</option>
				</select>	
			</td>
		</tr>
		<tr style="display: none;" id="pf">
			<td align="right">
				<label class="heading">批发市场:</label>
			</td>
			<td>
				<select id="pfUUID" name="pfUUID" style="width:480px; height: 32px;">
				
				</select>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">商家名称：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="businessName" id="businessName" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">请输入x轴:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="longitude" id="longitude" maxlength="20" size="20"/>
			</td>
			<td>
				<a href="http://api.map.baidu.com/lbsapi/creatmap/index.html" target="_blank" style="color: red;">点击查看X-Y轴</a>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">请输入y轴:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="latitude" id="latitude" maxlength="20" size="20"/>
			</td>
			<td>
				<a href="http://api.map.baidu.com/lbsapi/creatmap/index.html" target="_blank" style="color: red;">点击查看X-Y轴</a>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">详细地址：</label>
			</td>
			<td>
				<input class="easyui-textbox" name="businessAdd" id="businessAdd" data-options="multiline:true" style="height:60px;width:480px;">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">联系电话：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="businessPhone" id="businessPhone" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">联系人：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="contacts" id="contacts" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">行业分类:</label>
			</td>
			<td>
				<select id="catUUID" name="catUUID" style="width:480px; height: 32px;">
				
				</select>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">营业时间：</label>
			</td>
			<td>
				<input id="ks" class="easyui-timespinner" style="width:80px;">——
				<input id="js" class="easyui-timespinner" style="width:80px;">
				<input type="hidden" id="businessHours" name="businessHours">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">简介：</label>
			</td>
			<td>
				<input class="easyui-textbox" name="synopsis" id="synopsis" data-options="multiline:true" style="height:60px;width:480px;">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">开户银行：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="bank" id="bank" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">银行卡号：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="bankCard" id="bankCard" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">提现密码：</label>
			</td>
			<td>
				<input id="bankPassword" class="easyui-passwordbox" style="width:480px" iconWidth="28" name="bankPassword">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">银行卡正面照:</label>
			</td>
			<td>
				<input id="file" name="bankImg" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoImg" style="display: none;">
			<td align="right">
				<label class="heading">银行卡正面照:</label>
			</td>
			<td>
				<img src="" id="img" width="480px;" height="150px;">
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
		<tr>
			<td align="right">
				<label class="heading">工商营业照:</label>
			</td>
			<td>
				<input id="businessImg" name="businessImg" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoBusinessImg" style="display: none;">
			<td align="right">
				<label class="heading">工商营业照片:</label>
			</td>
			<td>
				<img src="" id="getBusinessImg" width="480px;" height="150px;">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">身份证正面照:</label>
			</td>
			<td>
				<input id="idJustImg" name="idJustImg" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoIdJustImg" style="display: none;">
			<td align="right">
				<label class="heading">身份证正面照片:</label>
			</td>
			<td>
				<img src="" id="getIdJustImg" width="480px;" height="150px;">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">身份证反面照:</label>
			</td>
			<td>
				<input id="idBackImg" name="idBackImg" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoIdBackImg" style="display: none;">
			<td align="right">
				<label class="heading">身份证反面照片:</label>
			</td>
			<td>
				<img src="" id="getIdBackImg" width="480px;" height="150px;">
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var path = "<%=basePath%>";//图片路径
	$(function(){
		//查询分类
		var fUrl = "<%=path%>/mobile/itemCat/getCatList?catUUID=-1";
		$.post(fUrl,{},function(data){
			//alert(JSON.stringify(data));
			var json = eval(data);//数组
			$("#catUUID").append("<option value='' selected='selected'>请选择</option>");
			$.each(json,function(index,item){
				//alert(json[index].catName);
				$("#catUUID").append("<option value='"+json[index].catUUID+","+json[index].catName+"'>"+json[index].catName+"</option>");
			});
		});
		
		//查询批发市场
		var pfUrl = "<%=path%>/Public/business/wholesaleMarketsAll"
			$.post(pfUrl,{},function(data){
				//alert(JSON.stringify(data));
				var json = eval(data.pfList);//数组
				$("#pfUUID").append("<option value='' selected='selected'>请选择</option>");
				$.each(json,function(index,item){
					$("#pfUUID").append("<option value='"+json[index].pfUUID+","+json[index].pfName+"'>"+json[index].pfName+"</option>");
				});
			},'json');
		
		
		var businessUUID = "<%=businessUUID%>";
		if("null" != businessUUID){
			var url="<%=path%>/Public/business/applyId?businessUUID=<%=businessUUID%>";
			$.post(url,{},function(data){
				$("#id").val(data.id);
				$("#applyType").combobox('setValue',data.applyType);
				$("#pfUUID").append("<option value='"+data.pfUUID+","+data.pfName+"' selected='selected'>"+data.pfName+"</option>");
				$("#businessName").textbox("setValue",data.businessName);
				$("#longitude").textbox("setValue",data.longitude);
				$("#latitude").textbox("setValue",data.latitude);
				$("#businessAdd").textbox("setValue",data.businessAdd);
				$("#businessPhone").textbox("setValue",data.businessPhone);
				$("#contacts").textbox("setValue",data.contacts);
				$("#catUUID").append("<option value='"+data.catUUID+","+data.classification+"' selected='selected'>"+data.classification+"</option>");
				var businessHours = data.businessHours.split("—");
				for(i = 0; i < businessHours.length; i++){
					if(i == 0){
						$('#ks').timespinner('setValue', businessHours[i]);
					}else if(i == 1){
						$("#js").timespinner('setValue', businessHours[i]);
					}
				}
				$("#bank").textbox("setValue",data.bank);
				$("#bankCard").textbox("setValue",data.bankCard);
				$("#bankPassword").textbox("setValue",data.bankPassword);
				$("#img").attr("src",path+data.bankImg);
				$("#getStorefrontImg").attr("src",path+data.storefrontImg);
				$("#getBusinessImg").attr("src",path+data.businessImg);
				$("#getIdJustImg").attr("src",path+data.idJustImg);
				$("#getIdBackImg").attr("src",path+data.idBackImg);
				$("#synopsis").textbox("setValue",data.synopsis);
				$("#shoImg").show();
				$("#shoStorefrontImg").show();
				$("#shoBusinessImg").show();
				$("#shoIdJustImg").show();
				$("#shoIdBackImg").show();
				$("#res").show();
			},'json');
		}
	})

	//选择文件
	$('#file').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//选择文件
	$('#storefrontImg').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//选择工商营业照
	$('#businessImg').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//选择身份证正面照
	$('#idJustImg').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//选择身份证反面照
	$('#idBackImg').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//显示店面图片
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
	
	//显示银行卡图片
	$(":input[name='bankImg']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('img');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "150";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoImg").show();
	});
	
	//显示工商营业照
	$(":input[name='businessImg']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('getBusinessImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "150";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoBusinessImg").show();
	});
	
	//显示身份证正面照
	$(":input[name='idJustImg']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('getIdJustImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "150";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoIdJustImg").show();
	});
	
	//显示身份证反面照
	$(":input[name='idBackImg']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('getIdBackImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "150";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoIdBackImg").show();
	});
	
	//保存或修改
	function editSave(){
		/* var money =$("input[name='money']:checked").val();
		alert(money);
		return; */
		var patrn=/^(\w){6,6}$/; 
		if("" == $("#applyType").val()){
			$.messager.alert('提示',"请选择入驻类型",'error');
			return;
		}else if($("#applyType").val() == 1 && "" == $("#pfUUID").val()){
			$.messager.alert('提示',"请选择批发市场",'error');
			return;
		}else if("" == $("#businessName").val()){
			$.messager.alert('提示',"请输入商家名称",'error');
			return;
		}else if("" == $("#longitude").val()){
			$.messager.alert('提示',"请输入X轴",'error');
			return;
		}else if("" == $("#latitude").val()){
			$.messager.alert('提示',"请输入Y轴",'error');
			return;
		}else if("" == $("#businessAdd").val()){
			$.messager.alert('提示',"请输入详细地址",'error');
			return;
		}else if("" == $("#businessPhone").val()){
			$.messager.alert('提示',"请输入电话号码",'error');
			return;
		}else if("" == $("#contacts").val()){
			$.messager.alert('提示',"请输入联系人",'error');
			return;
		}else if("" == $("#catUUID").val()){
			$.messager.alert('提示',"请选择行业分类",'error');
			return;
		}else if("" == $("#ks").val()){
			$.messager.alert('提示',"请选择营业开始时间",'error');
			return;
		}else if("" == $("#js").val()){
			$.messager.alert('提示',"请选择营业结束时间",'error');
			return;
		}else if("" == $("#synopsis").val()){
			$.messager.alert('提示',"请输入简介",'error');
			return;
		}else if("" == $("#bank").val()){
			$.messager.alert('提示',"请输入开户银行",'error');
			return;
		}else if("" == $("#bankCard").val()){
			$.messager.alert('提示',"请输入银行卡号",'error');
			return;
		}else if(!patrn.exec($("#bankPassword").val())){
			$.messager.alert('提示',"提现密码只能为6位",'error');
			return;
		}else if("<%=businessUUID%>" == "null" && "" == $("#file").filebox('getValue')){
			$.messager.alert('提示',"请选择银行卡正面照",'error');
			return;
		}else if("<%=businessUUID%>" == "null" && "" == $("#storefrontImg").filebox('getValue')){
			$.messager.alert('提示',"请选择店面照片",'error');
			return;
		}else if("<%=businessUUID%>" == "null" && "" == $("#businessImg").filebox('getValue')){
			$.messager.alert('提示',"请选择工商执业照",'error');
			return;
		}else if("<%=businessUUID%>" == "null" && "" == $("#idJustImg").filebox('getValue')){
			$.messager.alert('提示',"请选择身份证正面照",'error');
			return;
		}else if("<%=businessUUID%>" == "null" && "" == $("#idBackImg").filebox('getValue')){
			$.messager.alert('提示',"请选择身份证反面照",'error');
			return;
		}
		
		//拼接营业开始结束时间
		$("#businessHours").val($("#ks").val()+"—"+$("#js").val());
		//请求后台
		$("#sj_form").ajaxSubmit({
			url:'<%=path%>/Public/business/editSave',
			type:'post',
			dataType:'json',
			semantic:true,
			success : function(data){
				$.messager.alert('提示',data.msg,'info');
				$('#editList_addEdit_div').dialog('close');
				$("#editList_addEdit_div").remove();
			},
		});
	}
	
	
	//如果选择的是供应商就显示批发市场
	$(document).ready(function () {
		$("#applyType").combobox({
			onChange: function () {
				if($("#applyType").val() == 0){//零售商
					$("#pf").hide();
				}
				if($("#applyType").val() == 1){//供应商
					$("#pf").show();
				}
			}
		});
	});
	
</script>