<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

	<table id="account_table"></table>
	<div id="account_tb" style="padding:2px 5px;">
		<a class="easyui-menubutton" data-options="menu:'#type_controll-panel',iconCls:'icon-help'" style="width:120px;"><span id="accountType">账户类型</span></a>
		<a class="easyui-menubutton" data-options="menu:'#controll-panel',iconCls:'icon-help'" style="width:120px;"><span id="accountStatus">账户状态</span></a>		
		<input id="accountCondition" data-options="'prompt':'请输入账号/账户名称'" class="easyui-textbox" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="queryAccount()"  data-options="'plain':true"><i class="icon iconfont icon-search"></i>&nbsp;搜索</a>
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="type_controll-panel" style="width: 80px; display: none;">
	<div onclick="queryAccountType({'accountType':'0','text':'零售商账户'});">零售商账户</div>
		<div onclick="queryAccountType({'accountType':'1','text':'批发商账户'});">批发商账户</div>
		<div onclick="queryAccountType({'accountType':'2','text':'会员账户'});">会员账户</div>
		<div onclick="queryAccountType({'accountType':'3','text':'佣金账户'});">佣金账户</div>
		<div class="menu-sep"></div>
		<div onclick="queryAccountType({'accountType':'','text':'全部'});">全部</div>
	</div>
	<div id="controll-panel" style="width: 80px; display: none;">
		<div onclick="queryAccountStatus({'accountStatus':'1','text':'正常'});">正常</div>
		<div onclick="queryAccountStatus({'accountStatus':'0','text':'冻结'});">冻结</div>
		<div class="menu-sep"></div>
		<div onclick="queryAccountStatus({'accountStatus':'','text':'全部'});">全部</div>
	</div>
	
	<script type="text/javascript">
		//默认账户
	    var accountUUID = "";
		//默认账户状态
		var accountStatus = "1";
		var accountType = "";
		//默认收支类型
		var detailType = "";
		//默认支付渠道
		var payChannel = "";
		//默认收支来源
		var detailSource = "";
		
		//定义列
		var columns = [ [ {
			field : 'entityName',
			title : '账户名称',
			width : 80,
			align : 'center'
			
		},{
			field : 'accountType',
			title : '账户类型',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 0){
					return "零售商";
				}
				if(data == 1){
					return "批发商";
				}
				if(data == 2){
					return "会员";
				}
				if(data == 3){
					return "佣金";
				}
				if(data == 4){
					return "平台";
				}
			}
		},{
			field : 'accountBalance',
			title : '余额',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'accountIncome',
			title : '总收入',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'accountExpend',
			title : '总支出',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'mobile',
			title : '联系电话',
			width : 50,
			align : 'center'
		},{
			field : 'createTime',
			title : '开户时间',
			width : 50,
			align : 'center'
		},{
			field : 'updateTime',
			title : '最近交易',
			width : 50,
			align : 'center'
		},{
			field : 'accountStatus',
			title : '状态',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 0){
					return "冻结";
				}
				if(data == 1){
					return "启用";
				}
			}
		},{
			field : 'operation',
			title : '操作',
			width : 80,
			align : 'center',
			formatter : function(data,row,index){
				var html = '<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="detailLook(\''+index+'\');">&nbsp;账单&nbsp;</a>';
				if(row.accountStatus == '0'){
					html += '&nbsp;<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeStatus({\'index\':\''+index+'\',\'status\':\'1\'});">&nbsp;启用&nbsp;</a>';
				}
				if(row.accountStatus == '1'){
					html += '&nbsp;<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeStatus({\'index\':\''+index+'\',\'status\':\'0\'});">&nbsp;冻结&nbsp;</a>';
				}
				if(row.payPwd){
					html += '&nbsp;<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="resetPwd('+index+');">&nbsp;重置密码&nbsp;</a>';
				}
				return html;
			}
		} ] ];
		
		var detailColumns = [ [ {
			field : 'payerLogo',
			title : '付款人头像',
			width : 50,
			formatter : function(data,row,index){
				var index = data.indexOf('static');
				if(index != -1){
					data = '<%=basePath %>'+data.substr(index);
				}
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+data+'"/>';
			}
		},{
			field : 'payerName',
			title : '付款人名称',
			width : 110,
			align : 'center'
		},{
			field : 'money',
			title : '交易金额',
			width : 80,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'detailType',
			title : '收支类型',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 1){
					return "收入";
				}
				if(data == 2){
					return "支出";
				}
			}
		},{
			field : 'payChannel',
			title : '交易渠道',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 0){
					return "余额";
				}
				if(data == 1){
					return "微信";
				}
			}
		},{
			field : 'detailSource',
			title : '收支来源',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				//明细来源：1充值消费，2购物消费，3进货，4提现，5商家退款，6订单撤销，7交易收入，8其他
				if(data == 1){
					return "充值";
				}
				if(data == 2){
					return "购物";
				}
				if(data == 3){
					return "进货";
				}
				if(data == 4){
					return "提现";
				}
				if(data ==5){
					return "商家退款";
				}
				if(data ==6){
					return "订单撤销";
				}
				if(data ==6){
					return "交易收入";
				}
				return "其他";
			}
		},{
			field : 'createTime',
			title : '支付时间',
			width : 50,
			align : 'center'
		} ] ];
		
		$(function(){
			//初始化账户数据表格
			$('#account_table').datagrid({
				iconCls: 'icon-order',
				title: '&nbsp;&nbsp;账户管理',
				columns : columns,
				toolbar : '#account_tb',
				url : '<%=basePath %>system/account/findAccountPage',
				queryParams: {"accountStatus":accountStatus},
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				pageSize : 10,
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到账户信息哦</span>"
			});
		});
		
		//查看账单
		function detailLook(rowIndex){
			$('#account_table').datagrid('selectRow',rowIndex);
			var row = $('#account_table').datagrid('getSelected');
			$('<div>\
					<table id="detail_table"></table>\
					<div id="detailType_controll_panel" style="width: 130px; display: none;">\
						<div onclick="queryDetailType({\'detailType\':\'1\',\'text\':\'收入\'});">收入</div>\
						<div onclick="queryDetailType({\'detailType\':\'2\',\'text\':\'支出\'});">支出</div>\
						<div class="menu-sep"></div>\
						<div onclick="queryDetailType({\'detailType\':\'\',\'text\':\'全部\'});">全部</div>\
					</div>\
					<div id="payChannel_controll_panel" style="width: 130px; display: none;">\
						<div onclick="queryPayChannel({\'payChannel\':\'1\',\'text\':\'微信\'});">微信</div>\
						<div onclick="queryPayChannel({\'payChannel\':\'0\',\'text\':\'余额\'});">余额</div>\
						<div class="menu-sep"></div>\
						<div onclick="queryPayChannel({\'payChannel\':\'\',\'text\':\'全部\'});">全部</div>\
					</div>\
					<div id="detailSource_controll_panel" style="width: 130px; display: none;">\
						<div onclick="queryDetailSource({\'detailSource\':\'1\',\'text\':\'充值\'});">充值</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'2\',\'text\':\'购物\'});">购物</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'3\',\'text\':\'进货\'});">进货</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'4\',\'text\':\'提现\'});">提现</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'5\',\'text\':\'商家退款\'});">商家退款</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'6\',\'text\':\'订单撤销\'});">订单撤销</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'7\',\'text\':\'交易收入\'});">交易收入</div>\
						<div class="menu-sep"></div>\
						<div onclick="queryDetailSource({\'detailSource\':\'8\',\'text\':\'其他\'});">其他</div>\
						<div onclick="queryDetailSource({\'detailSource\':\'\',\'text\':\'全部\'});">全部</div>\
					</div>\
					<div id="detail_tb" style="padding:2px 5px;">\
						<a class="easyui-menubutton" data-options="menu:\'#detailType_controll_panel\',iconCls:\'icon-help\'" style="width:130px;"><span id="detailType">收支类型</span></a>\
						<a class="easyui-menubutton" data-options="menu:\'#payChannel_controll_panel\',iconCls:\'icon-help\'" style="width:130px;"><span id="payChannel">交易渠道</span></a>\
						<a class="easyui-menubutton" data-options="menu:\'#detailSource_controll_panel\',iconCls:\'icon-help\'" style="width:130px;"><span id="detailSource">收支来源</span></a>\
						<input id="detailCondition" data-options="\'prompt\':\'请输入收款人名称\/交易金额\'" class="easyui-textbox" style="width:200px;">\
				        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="queryDetail()"  data-options="\'plain\':true"><i class="icon iconfont icon-search"></i>&nbsp;搜索</a>\
				        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initDetailQuery()" data-options="\'plain\':true">&nbsp;清除</a>\
					</div>\
			</div>').window({
				width:'800px',
				height:'600px',
				title : '账户"'+row.entityName+'"',
				modal : true,
				onOpen : function(){
					//重置查询参数
					accountUUID = row.accountUUID;
					var _win = $(this);
					$.parser.parse($('#detail_tb',_win));
					$('#detail_table',_win).datagrid({
						columns : detailColumns,
						toolbar : '#detail_tb',
						url : '<%=basePath %>system/account/findAccountDetailPageByPc',
						queryParams: {"accountUUID":row.accountUUID},
						pagination : true,
						pageList : [10,20,30,40],
						pageSize : 10,
						fit : true,
						fitColumns : true,
						nowrap : false,
						singleSelect : true,
						emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到账单信息哦</span>"
					});
				},
				onClose : function(){
					$(this).window('destroy');
				}
			});
		}
		
		//修改账户状态
		function changeStatus(content){
			var rowIndex = content.index;
			var accountStatus = content.status;
			$('#account_table').datagrid('selectRow',rowIndex);
			var row = $('#account_table').datagrid('getSelected');
			var msg = "";
			if(accountStatus == "1"){
				msg = "确定要将账户\"" + row.entityName + "\"恢复正常吗？";
			}
			if(accountStatus == "0"){
				msg = "确定要将账户\"" + row.entityName + "\"冻结吗？";
			}
			$.messager.confirm("操作提醒", msg, function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>system/account/upStatus',
						data:{'accountUUID':row.accountUUID,'accountStatus':accountStatus,'entityUUID':row.entityUUID},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#account_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: {
								        'accountStatus' : accountStatus
								    }
								});
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		//查看账单
		function resetPwd(rowIndex){
			$('#account_table').datagrid('selectRow',rowIndex);
			var row = $('#account_table').datagrid('getSelected');
			
			$.messager.confirm("操作提醒", "确定要重置该账户的支付密码吗？", function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>system/account/resetPwd',
						data:{'entityUUID':row.entityUUID},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#account_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: {
								        'payPwd' : ''
								    }
								});
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		function queryAccountType(data){
			accountType = data.accountType;
			$("#accountType").html(data.text);
			$('#account_table').datagrid('reload',{'accountType':accountType,'accountStatus':accountStatus});
		}
		
		function queryAccountStatus(data){
			accountStatus = data.accountStatus;
			$("#accountStatus").html(data.text);
			$('#account_table').datagrid('reload',{'accountType':accountType,'accountStatus':accountStatus});
		}
		
		function queryAccount(){
			var condition = $("#accountCondition").val();
			$('#account_table').datagrid('reload',{'accountType':accountType,'accountStatus':accountStatus,"accountUUID":condition,"entityName":condition});
		}
		
		function queryDetailType(data){
			detailType = data.detailType;
			$("#detailType").html(data.text);
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource});
		}
		
		function queryPayChannel(data){
			payChannel = data.payChannel;
			$("#payChannel").html(data.text);
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource});
		}
		
		function queryDetailSource(data){
			detailSource = data.detailSource;
			$("#detailSource").html(data.text);
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource});
		}
		
		function queryDetail(){
			var condition = $("#detailCondition").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource,'money':condition});
				return;
			}
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource,'payerName':condition});
		}
		
		function initQuery(){
			$("[textboxname=accountCondition]").textbox("setValue",'');
			$('#account_table').datagrid('reload',{'accountType':accountType,'accountStatus':accountStatus});
		}
		
		function initDetailQuery(){
			$("#detailCondition").textbox("setValue",'');
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource});
		}
		
	</script>