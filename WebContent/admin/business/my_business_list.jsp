<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>条件搜索：</label>
	<input class="easyui-textbox easyui-" type="text" style="width:400px" data-options="prompt:'请输入店铺名称或电话号码'" name="condition" id="condition" maxlength="20" size="20"/>			
    <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
</div>
<table id="sj_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'businessUUID',align:'center',width:50,hidden:true">businessUUID</th>
			<th data-options="field:'businessName',align:'center',width:200">商家名称</th>
			<th data-options="field:'businessAdd',align:'center',width:200">商家详细地址</th>
			<th data-options="field:'businessPhone',align:'center',width:200">商家电话</th>
			<th data-options="field:'contacts',align:'center',width:100">联系人</th>
			<th data-options="field:'classification',align:'center',width:100">行业分类</th>
			<th data-options="field:'businessHours',align:'center',width:100">营业时间</th>
			<th data-options="field:'approvalStatus',width:100,align:'center',formatter:approvalStatus">审批状态</th>
			<th data-options="field:'dueTime',align:'center',width:100">到期时间</th>
			<th data-options="field:'applyType',width:100,align:'center',formatter:applyType">申请类型</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="distributionList_window_div">
	<div id="distributionList_addDistribution_div"></div>
</div>

<script type="text/javascript">
	var selectRow = null;
	$(function() {
		$('#sj_table').datagrid({
			title: '&nbsp;&nbsp;我的商家',
			url: '<%=path %>/Public/business/myBusiness',
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//
			}
		});
	});

	//格式化操作
	function clsListF(value, row, index) {
		if(row.shopState == 0){
			return "您的店铺已被停用请联系管理员";
		}else if(row.shopState == 1){
			if(row.approvalStatus == 0){
				return "暂无操作"
			}else if(row.approvalStatus == 1){
				//审批通过进行操作
				var orderAll = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="orderAllOpen(\'' + row.businessUUID + '\')">&nbsp;订单管理&nbsp;</a>';
				var dataStatistics = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="dataStatisticsOpen(\'' + row.businessUUID + '\')">&nbsp;数据统计&nbsp;</a>';
				var distribution = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="distributionOpen(\'' + row.businessUUID + '\')">&nbsp;分销设置&nbsp;</a>';
				var grade = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="gradeOpen(\'' + row.businessUUID + '\')">&nbsp;零售商等级设置&nbsp;</a>';
				var businessOpen = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="businessOpen(\'' + row.businessUUID + '\',\'' + row.applyType + '\')">&nbsp;进入店铺&nbsp;</a>';
				if(row.applyType == 0){//零售商
					//return orderAll+"&nbsp;"+dataStatistics+"&nbsp;"+distribution;
					return businessOpen;
				}else if(row.applyType == 1){//供应商
					//订单管理-商品管理-数据统计-商家等级设置
					//return orderAll+"&nbsp;"+dataStatistics+"&nbsp;"+grade;
					return businessOpen+"&nbsp;"+grade;
				}
			}else if(row.approvalStatus == 2){
				var notPass = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="notPassOpen(\'' + row.businessUUID + '\')">&nbsp;重新提交&nbsp;</a>';
				var lookNotPass = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="lookNotPassOpen(\'' + row.reason + '\')">&nbsp;查看事由&nbsp;</a>';
				return notPass + "&nbsp;" + lookNotPass;
			}
		}
	}

	//审核状态
	function approvalStatus(value, row, index) {
		if(row.approvalStatus == 0){
			return "审核中";
		}else if(row.approvalStatus == 1){
			return "审核通过";
		}else if(row.approvalStatus == 2){
			return "审核不通过";
		}
	}
	
	//申请类型
	function applyType(value, row, index){
		if(row.applyType == 0){
			return "零售商";
		}else if(row.applyType == 1){
			return "供应商";
		}
	}
	
	//重新提交
	function notPassOpen(e){
		var pcUrl = "business/business_save.jsp?businessUUID="+e;
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}

	//查看事由
	function lookNotPassOpen(e){
		$.messager.alert('审批事由', e);
	}
	
	//订单管理
	function orderAllOpen(e){
		//跳转order_list.jsp
		var pcUrl = "business/shop_manage/order_list.jsp?entityUUID="+e;
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	//数据统计
	function dataStatisticsOpen(e){
		var pcUrl = "business/shop_manage/data_statistics_list.jsp?businessUUID="+e;
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	//分销设置
	function distributionOpen(e){
		$("#distributionList_addDistribution_div").remove();
		$("#distributionList_window_div").append("<div id='distributionList_addDistribution_div'></div>");
		var url = "business/shop_manage/distribution_save.jsp?businessUUID="+e;
		var title = '分销设置';
		$("#distributionList_addDistribution_div").dialog({
			title: title,
			width: 900,
			height: 600,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					distributionSave();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#distributionList_addDistribution_div').dialog('close');
					$("#distributionList_addDistribution_div").remove();
				}
			}]
		});
	}
	
	function gradeOpen(businessUUID){
		var pcUrl = "business/grade/retailer_list.jsp?businessUUID="+businessUUID;
		$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
	}
	
	//进入店铺
	function businessOpen(businessUUID,applyType){
		/* alert("模板正在开发中");
		return; */
		if(applyType == 0){//零售商
			var pcUrl = "business/administration/business_open_lsy.jsp?businessUUID="+businessUUID+"&applyType="+applyType;
			$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
		}else if(applyType == 1){//供应商
			var pcUrl = "business/administration/business_open_gys.jsp?businessUUID="+businessUUID+"&applyType="+applyType;
			$('#master-layout').layout('panel', 'center').panel('refresh', pcUrl);
		}
	}
	
	//搜索
	function query(){
		var condition = $("#condition").val();
		$("#sj_table").datagrid("load",{
			condition:condition
		});
	}

</script>