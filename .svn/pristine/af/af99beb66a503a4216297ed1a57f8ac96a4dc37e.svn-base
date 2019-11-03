<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

	<table id="vip_table"></table>
	<div id="vip_tb" style="padding:2px 5px;">
		<a class="easyui-menubutton" data-options="menu:'#vipStatus_controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="vipStatus">会员状态</span></a>
		<a class="easyui-menubutton" data-options="menu:'#vipType_controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="vipType">会员级别</span></a>		
		<input id="vipCondition" name="vipCondition" class="easyui-textbox" data-options="'prompt':'昵称/姓名/手机号:'" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="query()" data-options="'plain':true">&nbsp;搜索</a>
   		<a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="vipStatus_controll-panel" style="width: 130px; display: none;">
		<div onclick="queryStatus({'status':'1','statusText':'正常'});">正常</div>
		<div onclick="queryStatus({'status':'0','statusText':'停用'});">停用</div>
		<div class="menu-sep"></div>
		<div onclick="queryStatus({'status':'','statusText':'全部'});">全部</div>
	</div>
	<div id="vipType_controll-panel" style="width: 130px; display: none;">
		<div onclick="queryType({'type':'1','statusText':'普通会员'});">普通会员</div>
		<div onclick="queryType({'type':'2','statusText':'付费会员'});">付费会员</div>
		<div onclick="queryType({'type':'3','statusText':'平台管理员'});">平台管理员</div>
		<div class="menu-sep"></div>
		<div onclick="queryType({'type':'','statusText':'全部'});">全部</div>
	</div>
	
	<script type="text/javascript">
	//默认会员状态
	var vipStatus = "1";
	var vipType = "";
	
 	// 定义列
	var columns = [ [ {
		field : 'avatarUrl',
		title : '会员头像',
		width : 50,
		formatter : function(data,row,index){
			var index = data.indexOf('static');
			if(index != -1){
				data = '<%=basePath %>'+data.substr(index);
			}
			return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+data+'"/>';
		}
	},{
		field : 'nickName',
		title : '昵称',
		width : 80,
		align : 'center'
	},{
		field : 'vipName',
		title : '姓名',
		width : 80,
		align : 'center'
	},{
		field : 'gender',
		title : '性别',
		width : 50,
		align : 'center',
		formatter : function(data,row,index){
			return data == 1?"男":"女";
		}
	},{
		field : 'vipMobile',
		title : '手机号码',
		width : 80,
		align : 'center'
	},{
		field : 'wxNumber',
		title : '微信号',
		width : 80,
		align : 'center'
	},{
		field : 'firstNum',
		title : '一度人脉',
		width : 50,
		align : 'center'
	},{
		field : 'secondNum',
		title : '二度人脉',
		width : 50,
		align : 'center'
	},{
		field : 'vipType',
		title : '会员级别',
		width : 50,
		align : 'center',
		formatter : function(data,row,index){
			return data == 1?"普通会员":data == 2?"付费会员":"平台管理员";
		}
	},{
		field : 'vipStatus',
		title : '状态',
		width : 30,
		align : 'center',
		formatter : function(data,row,index){
			return data == 1?"正常":"停用";
		}
	},{
		field : 'operation',
		title : '操作',
		width : 60,
		align : 'center',
		formatter : function(data,row,index){
			//info primary warning success danger
			var html = "";
			html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" style="margin:5px 0px;" data-options="plain: true, iconCls: \'icon-remove\'" onclick="findSubordinate('+index+',1,1);">&nbsp;一度人脉&nbsp;</a>';
			html += '<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" style="margin:5px 0px;" data-options="plain: true, iconCls: \'icon-remove\'" onclick="findSubordinate('+index+',2,1);">&nbsp;二度人脉&nbsp;</a>';
			return html;
		}
	} ] ];
 	
	$(function(){
		$('#vip_table').datagrid({
			url: '<%=path %>/system/vip/findDistributePage',
			iconCls: 'fa fa-user',
			title: '&nbsp;&nbsp;分销人员管理', 
			columns : columns,
			toolbar: '#vip_tb',
	        rownumbers: true,
	        pagination: true,
	        pageList : [10,20,30,40],
			pageSize : 10,
			fit : true,
			fitColumns : true,
			nowrap : false,
	        singleSelect: true,
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到会员信息哦</span>"
		});
	});
	
	//查看分销层级
	function findSubordinate(rowIndex,level,count){
		var table = '#subordinate_table'+count;
		if(count == 1){
			table = '#vip_table';
		}
		$(table).datagrid('selectRow',rowIndex);
		var row = $(table).datagrid('getSelected');
		var vipUUID = row.vipUUID;
		var title = '';
		var param = {};
		if(level == 1){
			title = '一度人脉';
			param.firstUUID = vipUUID;
		}
		if(level == 2){
			title = '二度人脉';
			param.secondUUID = vipUUID;
		}
		
		count++;
		table = 'subordinate_table'+count;
		// 定义列
		var subordinate = [ [ {
			field : 'avatarUrl',
			title : '会员头像',
			width : 50,
			formatter : function(data,row,index){
				var index = data.indexOf('static');
				if(index != -1){
					data = '<%=basePath %>'+data.substr(index);
				}
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+data+'"/>';
			}
		},{
			field : 'nickName',
			title : '昵称',
			width : 80,
			align : 'center'
		},{
			field : 'vipName',
			title : '姓名',
			width : 80,
			align : 'center'
		},{
			field : 'gender',
			title : '性别',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return data == 1?"男":"女";
			}
		},{
			field : 'vipMobile',
			title : '手机号码',
			width : 80,
			align : 'center'
		},{
			field : 'wxNumber',
			title : '微信号',
			width : 80,
			align : 'center'
		},{
			field : 'firstNum',
			title : '一度人脉',
			width : 50,
			align : 'center'
		},{
			field : 'secondNum',
			title : '二度人脉',
			width : 50,
			align : 'center'
		},{
			field : 'vipType',
			title : '会员级别',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return data == 1?"普通会员":data == 2?"付费会员":"平台管理员";
			}
		},{
			field : 'vipStatus',
			title : '状态',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return data == 1?"正常":"停用";
			}
		},{
			field : 'operation',
			title : '操作',
			width : 60,
			align : 'center',
			formatter : function(data,row,index){
				//info primary warning success danger
				var html = "";
				html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" style="margin:5px 0px;" data-options="plain: true, iconCls: \'icon-remove\'" onclick="findSubordinate('+index+',1,'+count+');">&nbsp;一度人脉&nbsp;</a>';
				html += '<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" style="margin:5px 0px;" data-options="plain: true, iconCls: \'icon-remove\'" onclick="findSubordinate('+index+',2,'+count+');">&nbsp;二度人脉&nbsp;</a>';
				return html;
			}
		} ] ];
		$('<div>\
			<table id="'+table+'"></table>\
		</div>').window({
			width:'1100px',
			height:'550px',
			title : title,
			modal : true,
		    onOpen : function(){
		    	var _win = $(this);
		    	$('#' + table,_win).datagrid({
					columns : subordinate,
					rownumbers : true,
					url : '<%=basePath %>system/vip/findVipPage',
					queryParams: param,
					pagination : true,
					pageList : [10,20,30,40],
					fit : true,
					fitColumns : true,
					nowrap : false,
					singleSelect : true,
					emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到会员人脉信息哦</span>"
				});
		    },
		    onClose : function(){
		    	$(this).window('destroy');
		    }
		});
	}
	
	function queryStatus(data){
		vipStatus = data.status;
		$("#vipStatus").html(data.statusText);
		$('#vip_table').datagrid('reload',{"vipStatus":vipStatus,"vipType":vipType});
	}
	
	function queryType(data){
		vipType = data.type;
		$("#vipType").html(data.statusText);
		$('#vip_table').datagrid('reload',{"vipStatus":vipStatus,"vipType":vipType});
	}

	function query(){
		var condition = $("#vipCondition").val();
		//使用正则表达式判断字符串是否为电话号码
		var regPos = /\d{11}/;
		if(regPos.test(condition)){
			$('#vip_table').datagrid('reload',{"vipStatus":vipStatus,"vipType":vipType,"vipMobile":condition});
			return;
		}
		$('#vip_table').datagrid('reload',{"vipStatus":vipStatus,"vipType":vipType,"nickName":condition,"vipName":condition});
	}
	function initQuery(){
		$("[textboxname=vipCondition]").textbox("setValue",'');
		$('#vip_table').datagrid('reload',{"vipStatus":vipStatus,"vipType":vipType});
	}
	</script>

