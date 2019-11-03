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
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">入驻类型:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="lx" id="lx" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr style="display: none;" id="pf">
			<td align="right">
				<label class="heading">批发市场:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="pfName" id="pfName" maxlength="20" size="20"/>
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
				<label class="heading">x轴:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="longitude" id="longitude" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">y轴:</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="latitude" id="latitude" maxlength="20" size="20"/>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">详细地址：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="businessAdd" id="businessAdd" maxlength="20" size="20"/>
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
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="classification" id="classification" maxlength="20" size="20"/>
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
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="synopsis" id="synopsis" maxlength="20" size="20"/>
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
				<label class="heading">银行卡正面照:</label>
			</td>
			<td>
				<img src="" id="img" width="100%" height="100%">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">店面照片:</label>
			</td>
			<td>
				<img src="" id="getStorefrontImg" width="100%" height="100%">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">工商营业照片:</label>
			</td>
			<td>
				<img src="" id="getBusinessImg" width="100%" height="100%">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">身份证正面照片:</label>
			</td>
			<td>
				<img src="" id="getIdJustImg" width="100%" height="100%">
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="heading">身份证反面照片:</label>
			</td>
			<td>
				<img src="" id="getIdBackImg" width="100%" height="100%">
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var path = "<%=basePath%>";
	$(function(){
		var businessUUID = "<%=businessUUID%>";
		if("null" != businessUUID){
			var url="<%=path%>/Public/business/applyId?businessUUID=<%=businessUUID%>";
			$.post(url,{},function(data){
				if(data.applyType == 0){
					$("#lx").textbox("setValue","零售商");
				}
				if(data.applyType == 1){
					$("#lx").textbox("setValue","供应商");
					$("#pf").show();
					$("#pfName").textbox("setValue",data.pfName);
				}
				$("#businessName").textbox("setValue",data.businessName);
				$("#longitude").textbox("setValue",data.longitude);
				$("#latitude").textbox("setValue",data.latitude);
				$("#businessAdd").textbox("setValue",data.businessAdd);
				$("#businessPhone").textbox("setValue",data.businessPhone);
				$("#contacts").textbox("setValue",data.contacts);
				$("#classification").textbox("setValue",data.classification);
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
				$("#bankCard").textbox("setValue",data.bankCard);
				$("#img").attr("src",path+data.bankImg);
				$("#getStorefrontImg").attr("src",path+data.storefrontImg);
				$("#synopsis").textbox("setValue",data.synopsis);
				$("#getBusinessImg").attr("src",path+data.businessImg);
				$("#getIdJustImg").attr("src",path+data.idJustImg);
				$("#getIdBackImg").attr("src",path+data.idBackImg);
			},'json');
		}
	})
	
</script>