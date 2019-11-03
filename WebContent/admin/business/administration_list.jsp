<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>店铺状态：</label>
	<select id="shopState" name="shopState" class="easyui-combobox" editable=false; style="width:200px;height:32px">
	    <option value="">请选择</option>
	    <option value="0">停用</option>
	    <option value="1">启用</option>
	</select>		
	<label>条件搜索：</label>
	<input class="easyui-textbox easyui-" type="text" style="width:200px" data-options="prompt:'请输入店铺名称或电话号码'" name="condition" id="condition" maxlength="20" size="20"/>		
     <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
</div>
<table id="sj_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'businessUUID',align:'center',width:50,hidden:true">businessUUID</th>
			<th data-options="field:'businessName',align:'center',width:200">商家名称</th>
			<th data-options="field:'businessAdd',align:'center',width:200">商家详细地址</th>
			<th data-options="field:'synopsis',align:'center',width:200">商家简介</th>
			<th data-options="field:'businessPhone',align:'center',width:200">商家电话</th>
			<th data-options="field:'contacts',align:'center',width:100">联系人</th>
			<th data-options="field:'classification',align:'center',width:100">行业分类</th>
			<th data-options="field:'businessHours',align:'center',width:100">营业时间</th>
			<th data-options="field:'approvalStatus',width:100,align:'center',formatter:approvalStatus">审批状态</th>
			<th data-options="field:'applyTime',align:'center',width:100">申请时间</th>
			<th data-options="field:'bank',align:'center',width:100">开户银行</th>
			<th data-options="field:'bankCard',align:'center',width:100">银行卡号</th>
			<th data-options="field:'bankImg',align:'center',width:100, formatter:bankImg">银行卡图片</th>
			<th data-options="field:'storefrontImg',align:'center',width:100, formatter:storefrontImg">店面图片</th>
			<th data-options="field:'applyType',width:100,align:'center',formatter:applyType">申请类型</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="passList_window_div">
	<div id="passList_addPass_div"></div>
</div>

<div id="editList_window_div">
	<div id="editList_addEdit_div"></div>
</div>

<!-- 查看商家详情 -->
<div id="detailsList_window_div">
	<div id="detailsList_addDetails_div"></div>
</div>

<!-- 显示银行卡正面图片 -->
<div id='bankImg' style="text-align:center;">
	<img alt="" src="" id="setBankImg">
</div>

<!-- 显示店面图片 -->
<div id='storefrontImg' style="text-align:center;">
	<img style="width: 100%;height: 500px;" alt="" src="" id="setStorefrontImg">
</div>

<script type="text/javascript">
	var path = "<%=basePath%>";
	var selectRow = null;
	$(function() {
		$('#sj_table').datagrid({
			title: '&nbsp;&nbsp;商户管理',
			url: '<%=path %>/Public/business/businessAppliesList?approvalStatus=1',
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
		var discontinuation = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="shopStateSave(\'' + row.businessUUID + '\',\'0\')">&nbsp;停用&nbsp;</a>';
		var enable = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="shopStateSave(\'' + row.businessUUID + '\',\'1\')">&nbsp;启用&nbsp;</a>';
		var lookDetails = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="lookDetailsOpen(\'' + row.businessUUID + '\')">&nbsp;查看详情&nbsp;</a>';
		var edit = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="editOpen(\'' + row.businessUUID + '\')">&nbsp;编辑&nbsp;</a>';
		if(row.approvalStatus == 1){
			if(row.shopState == 0){
				return enable+"&nbsp;"+lookDetails+"&nbsp;"+edit;
			}
			if(row.shopState == 1){
				return discontinuation+"&nbsp;"+lookDetails+"&nbsp;"+edit;
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

	//审批
	function approvalOpen(businessUUID) {
		$("#passList_addPass_div").remove();
		$("#passList_window_div").append("<div id='passList_addPass_div'></div>");
		var url = "business/not_pass_save.jsp?businessUUID="+businessUUID;
		var title = '审批';
		$("#passList_addPass_div").dialog({
			title: title,
			width: 800,
			height: 400,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					spSave();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#passList_addPass_div').dialog('close');
					$("#passList_addPass_div").remove();
				}
			}]
		});
	}
	
	//银行卡正面照片
	function bankImg(value, row, index){
		var img = path+row.bankImg
		return "<img style='width:24px;height:24px;' border='1' title='点击查看图片' onclick=subBankImg(\""+img+"\") src='"+img+"'/>";
	}
	function subBankImg(e){
		$('#bankImg').dialog({
		    title: '银行卡正面照',
		    width: 800,
		    height:500,
		    resizable:true,
		    closed: false,
		    cache: false,
		    modal: true
		});
		$("#setBankImg").attr("src",e);
	}
	
	//店面图片
	function storefrontImg(value, row, index){
		var img = path+row.storefrontImg
		return "<img style='width:24px;height:24px;' border='1' title='点击查看图片' onclick=subStorefrontImg(\""+img+"\") src='"+img+"'/>";
	}
	function subStorefrontImg(e){
		$('#storefrontImg').dialog({
		    title: '店面照片',
		    width: 800,
		    height:500,
		    resizable:true,
		    closed: false,
		    cache: false,
		    modal: true
		});
		$("#setStorefrontImg").attr("src",e);
	}
	
	//搜索
	function query(){
		var shopState = $("#shopState").val();
		var condition = $("#condition").val();
		$("#sj_table").datagrid("load",{
			shopState:shopState,
			condition:condition
		});
	}
	
	//停用启用
	function shopStateSave(businessUUID,shopState){
		var url="<%=path%>/Public/business/shopStateSave";
		$.post(url,{"businessUUID":businessUUID,"shopState":shopState},function(data){
			$.messager.alert('提示',data.msg,'info');
			$('#sj_table').datagrid('reload');
		},'json');
	}
	
	//查看商家详情
	function lookDetailsOpen(e){
		$("#detailsList_addDetails_div").remove();
		$("#editList_window_div").append("<div id='detailsList_addDetails_div'></div>");
		var url = "business/details_list.jsp?businessUUID="+e;
		var title = '查看详情';
		$("#detailsList_addDetails_div").dialog({
			title: title,
			width: 900,
			height: 600,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#detailsList_addDetails_div').dialog('close');
					$("#detailsList_addDetails_div").remove();
				}
			}]
		});
	}
	
	//编辑
	function editOpen(businessUUID) {
		$("#editList_addEdit_div").remove();
		$("#passList_window_div").append("<div id='editList_addEdit_div'></div>");
		var url = "business/edit.jsp?businessUUID="+businessUUID;
		var title = '审批';
		$("#editList_addEdit_div").dialog({
			title: title,
			width: 800,
			height: 400,
			closed: false,
			eache: false,
			href: url,
			modal: true,
			buttons: [{
				text: '保存',
				iconCls: 'icon-mini-tick',
				handler: function() {
					editSave();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#editList_addEdit_div').dialog('close');
					$("#editList_addEdit_div").remove();
				}
			}]
		});
	}

</script>