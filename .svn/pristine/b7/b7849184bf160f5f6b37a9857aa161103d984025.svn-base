<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>时间日期：</label>
	<input class="easyui-datebox" id="time" name="time"></input>	
	<label>批发市场名称：</label>
	<input class="easyui-textbox easyui-" type="text" style="width:"106px" name="pfNames" id="pfNames" maxlength="20" size="20"/>	
     <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
     <a href="#" onclick="pfOpen('')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>
</div>
<table id="pf_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'pfUUID',align:'center',width:50,hidden:true">pfUUID</th>
			<th data-options="field:'pfName',align:'center',width:200">批发市场名称</th>
			<th data-options="field:'pfAdd',align:'center',width:200">批发市场地址</th>
			<th data-options="field:'pfSynopsis',align:'center',width:200">批发市场简介</th>
			<th data-options="field:'storefrontImg',align:'center',width:100, formatter:storefrontImg">批发市场门店图片</th>
			<th data-options="field:'time',align:'center',width:200">时间</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<div id="pfList_window_div">
	<div id="pfList_addPf_div"></div>
</div>

<script type="text/javascript">
	var path = "<%=basePath%>";
	var selectRow = null;
	$(function() {
		$('#pf_table').datagrid({
			title: '&nbsp;&nbsp;批发市场',
			url: '<%=path %>/Public/business/wholesaleMarketsAll',
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
		var approval = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="pfOpen(\'' + row.pfUUID + '\')">&nbsp;编辑&nbsp;</a>';
		return approval;
	}

	

	//审批
	function pfOpen(pfUUID) {
		$("#pfList_addPf_div").remove();
		$("#pfList_window_div").append("<div id='pfList_addPf_div'></div>");
		var url = "business/wholesale_market/wholesale_market_save.jsp?pfUUID="+pfUUID;
		var title = '';
		if("" == pfUUID){
			title = "新增";
		}else{
			title = "编辑";
		}
		$("#pfList_addPf_div").dialog({
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
					pfSave();
				}
			}, {
				text: '取消',
				iconCls: 'icon-cancel',
				handler: function() {
					$('#pfList_addPf_div').dialog('close');
					$("#pfList_addPf_div").remove();
				}
			}]
		});
	}
	
	//批发市场门店图片
	function storefrontImg(value, row, index){
		var img = path+row.storefrontImg
		return "<img style='width:24px;height:24px;' border='1' src='"+img+"'/>";
	}
	
	//搜索
	function query(){
		var time = $("#time").val();
		var pfName = $("#pfNames").val();
		$("#pf_table").datagrid("load",{
			time:time,
			pfName:pfName
		});
	}
	

</script>