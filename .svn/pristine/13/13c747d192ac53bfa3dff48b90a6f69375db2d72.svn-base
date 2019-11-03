<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 积分规则管理 -->
<style>
	.left{
		float: left;
	}
	.buttonSubmit {
		width: 100px;
		height: 33px;
		background-color: #02a2ff;
		color: #fff !important;
	}
	*::-webkit-scrollbar-track{
	border-radius:0px !important;
	}
	#releaseType,
	#type,
	#status,
	#entity-list,
	.select-box{
		width:74%;
		height:30px;
		border:1px solid #ccc;
		border-radius:0px !important;
	}
	
</style>

<table id="ruleData"></table>
<!-- 筛选条件 -->
<div id="addRule" style="width: 100%; height: 50px; margin: auto;">
	<div class="left" style="line-height: 50px; width: 20%">
		<span>规则类型：</span> <select style="width: 50%" id="type"
			onchange="getRuleList()">
			<option value="0">不限</option>
			<option value="1">账户充值</option>
			<option value="2">购买商品</option>
		</select>
	</div>
	<div style="width: 15%; line-height: 50px; float: left">
		<input type="submit" style="cursor: pointer;color: #ffffff; width: 60%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 5%" value="新增积分规则" onclick="createRule()">
		<!-- <button onclick="createRule()" style="color: #ffffff; width: 60%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 5%">新增积分规则</button> -->
		
	</div>
</div>

<!-- 编辑修改和新增 -->
<div id="editor-createRule" style="display: none;">
	<form id="integralRule" method="post">
		<table style="width: 50%; margin: auto;">
			<tr style="display: block; margin: 10px; width: 100%">
				<td style="width: 120px;">规则类型:</td>
				<td style="width: 250px;"><label><input id="recharge"
						checked="checked" type="radio" name="ruleType" value="1" />&nbsp;账户充值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<label><input id="buy" type="radio" name="ruleType"
						value="2" />&nbsp;商品购买</label></td>
			</tr>
			<tr style="display: block; margin: 10px; width: 100%">
				<td style="width: 120px;">梯度最小值:</td>
				<td style="width: 250px;"><input id="grad-min" type="number"
					class="easyui-textbox" name="gradMin" style="width: 100%" /></td>
			</tr>
			<tr style="display: block; margin: 10px; width: 100%">
				<td style="width: 120px;">梯度最大值:</td>
				<td style="width: 250px"><input id="grad-max" type="number"
					class="easyui-textbox" name="gradMax" style="width: 100%" /></td>
			</tr>
			<tr style="display: block; margin: 10px; width: 100%">
				<td style="width: 120px;">梯度计算值:</td>
				<td style="width: 250px;"><input id="calculate-nums"
					type="number" class="easyui-textbox" name="calculateRule"
					style="width: 100%" /></td>
			</tr>
			<tr style="display: block; margin: 10px; width: 100%">
				<td style="width: 120px;">状态:</td>
				<td style="width: 250px;"><label><input id="yes"
						type="radio" name="state" value="1" />&nbsp;启用&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
					<label><input id="not" checked="checked" type="radio"
						name="state" value="2" />&nbsp;停用</label></td>
			</tr>
		</table>
	</form>
	<div style="width: 30%; margin: auto; margin-top: 20px;">
		<input onclick="editorOrCreateRule(1)" type="submit" style="cursor: pointer;color: #ffffff; width: 40%; line-height: 30px; background-color: rgba(255, 0, 0, 0.7); border-radius: 5px; margin-left: 4%" value="确认">
		<input onclick="editorOrCreateRule(2)" type="submit" style="cursor: pointer;color: #ffffff; width: 40%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 4%" value="取消">
		<!-- <button onclick="editorOrCreateRule(1)" style="color: #ffffff; width: 40%; line-height: 30px; background-color: rgba(255, 0, 0, 0.7); border-radius: 5px; margin-left: 4%">确认</button>
		<button onclick="editorOrCreateRule(2)" style="color: #ffffff; width: 40%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 4%">取消</button> -->
	</div>
	<div
		style="width: 50%; margin-top: 10px; margin-left: 180px; color: #8e8e8e;">备注：一个梯度中包含最小值，但不包含最大值；启用不同梯度之间最大最小值不允许出现重叠，例：100≦X<500，200≦X<800为梯度重叠；
		梯度计算值即计算积分时，支付金额乘积的倍数，例如：金额100，梯度计算值1.5，则积分为1.5*100;</div>
</div>


<script>
	//定义列
	var clumn = [[
		{
			field:'ruleType',
			title:'规则类型',
			width:100,
			align:'center',
			formatter:function(data,row,index){//1-账户充值；2-购买商品
				return data == 1?"账户充值":"购买商品"
			}
		},
		{
			field:'grade',
			title:'规则触发梯度',
			width:100,
			align:'center',
			formatter:function(data,row,index){
				return row.gradMin+"≦X<"+row.gradMax;
			}	
		},
		{
			field:'calculateRule',
			title:'规则计算数值',
			width:100,
			align:'center'
		},
		{
			field:'state',
			title:'状态',
			width:100,
			align:'center',
			formatter:function(data,row,index){//该梯度状态；1-启用，2-停用
				return data == 1?"启用":"停用"
			}	
		},
		{
			field:'operation',
			title:'操作',
			width:100,
			align : 'center',
			formatter:function(data,row,index){
				var html = "";
				var ruleUUID = row.ruleUUID;
				var rowData = JSON.stringify(row);
				html = "<a class='easyui-linkbutton button-danger button-xs l-btn l-btn-small' data-options='plain: true, iconCls: icon-remove' onclick='editorRule("+rowData+")'>&nbsp;编辑&nbsp;</a>";
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteRule(\''+ruleUUID+'\');">&nbsp;删除&nbsp;</a>';
				return html;
			}
		}
	]];
	
	$(function(){
		var condition = $("#type").val();
		$("#ruleData").datagrid({
			url:'<%=path %>'+"/system/integral/getIntegralRuleList",
			queryParams:{type:condition},
			title: '&nbsp;&nbsp;积分规则管理', 
			toolbar: '#addRule',
			columns:clumn,
			rownumbers: true,
	        pagination: true,
	        pageList : [10,20,30,40],
			pageSize : 10,
			striped:true,
			fit : true,
			fitColumns : true,
			nowrap : false,
	        singleSelect: true,
	        pagePosition:'bottom',
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到积分规则信息哦</span>"
		});
	});
	
	/* 重新按条件加载获取 */
	function getRuleList(){
		var condition = $("#type").val();
		$('#ruleData').datagrid('reload',{"type":condition});
	}
	
	
	var index = null;
	var id = "";
	/* 点击新增按钮，弹出新增信息输入层 */
	function createRule(){
		id = "";
		//清除之前的数据
		$("#recharge").prop("checked",true);
		$("#grad-min").textbox("setValue","");
		$("#grad-max").textbox("setValue","");
		$("#calculate-nums").textbox("setValue","");
		$("#not").prop("checked",true);
		//弹出层
		index = layer.open({
	  		type: 1,
	  		title:'新增积分规则',
	  		area: ['800px', '500px'],
	  		fixed: true, 
	  		maxmin: true,
	  		content: $('#editor-createRule')
		});
	}
	
	/* 点击编辑按钮，弹出信息修改层 */
	function editorRule(rowData){
		//赋值
		id = rowData.id;
		if(rowData.ruleType == 1){
			$("#recharge").prop("checked",true);
		}else if(rowData.ruleType == 2){
			$("#buy").prop("checked",true);
		}
		$("#grad-min").textbox("setValue",rowData.gradMin);
		$("#grad-max").textbox("setValue",rowData.gradMax);
		$("#calculate-nums").textbox("setValue",rowData.calculateRule);
		//是否启用
		if(rowData.state == 2){
			$("#not").prop("checked",true);
		}else if(rowData.state == 1){
			$("#yes").prop("checked",true);
		}
		
		//弹出层
		index = layer.open({
	  		type: 1,
	  		title:'编辑修改',
	  		area: ['800px', '500px'],
	  		fixed: true, 
	  		maxmin: true,
	  		content: $('#editor-createRule')
		});
	} 
	
	
	/* 点击弹出层的确定或取消 */
	var isSubmit = false;
	function editorOrCreateRule(e){
		if(e == 1){//确认
			//判断值的正确性,只能输入大于0的整数
			var reg = /^([1-9]\d*(\.\d*[1-9])?)|(0\.\d*[1-9])$/;
			var gradMin = $("#grad-min").val();
			if(gradMin == undefined || gradMin.replace(/\s/g, "") == ""){
				layer.alert('请填写梯度最小值!');
				return;
			}else if(!reg.test(gradMin)){
				layer.alert('请填写正确的梯度值!');
				return;
			}
			
			var gradMax = $("#grad-max").val()
			if( gradMax == undefined || gradMax.replace(/\s/g, "") == ""){
				layer.alert('请填写梯度最大值!');
				return;
			}else if(!reg.test(gradMax)){
				layer.alert('请填写正确的梯度值!');
				return;
			}
			
			var calculate = $("#calculate-nums").val();
			if( calculate == undefined || calculate.replace(/\s/g, "") == ""){
				layer.alert('请填写梯度计算值!');
				return;
			}else if(!reg.test(calculate)){
				layer.alert('请填写正确的梯度计算值!');
				return;
			}
			
			var min = parseFloat(gradMin);
			var max = parseFloat(gradMax);
			if(min >= max){
				layer.alert('梯度最小值不能大于等于梯度最大值');
				return;
			}
			
			if(!isSubmit){
				isSubmit = true;
			}else{
				return;
			}
			
			$.ajax({
				url:baseURL+"system/integral/createIntegralRule",
				data:$("#integralRule").serialize()+"&id="+id,
				type:"POST",
				dataType:"json",
				success:function(data){
					/* $.messager.alert('提示',data.msg,'info'); */
					/* 执行数据写入成功后关闭弹出层 */
					if(data.status == "1"){
						isSubmit = false;
						layer.close(index);
						$('#ruleData').datagrid('reload');
					}
				}
			});
		}else {//取消
			layer.close(index);//关闭弹出层
		}
	}
	
	/* 点击删除指定积分规则 */
	function deleteRule(ruleUUID){
		layer.confirm('您确认删除该积分规则么？', function(index){
			$.ajax({
				url:baseURL+"system/integral/deleteIntegralRule",
				data:{"ruleUUID":ruleUUID},
				type:"POST",
				dataType:"json",
				success:function(data){
					layer.alert(data.msg);	
					$('#ruleData').datagrid('reload');
				}
			});
	     });
	}
	
</script>
