<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xryb.zhtc.entity.VipInfo" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String auditorUUID = (String)request.getSession().getAttribute("userUUID");
	VipInfo vipinfo = (VipInfo)request.getSession().getAttribute("vipinfo");
	if(auditorUUID == null){
		auditorUUID = vipinfo.getVipUUID();
	}
%>
	<style>
		.img-width-100 {
			width: 100%;
			height: 100%;
			display: block;
		}
		.sc_zjz {
			width:100%;
			text-align:center;
			padding:0px;
		}
		.list_s_none {
			list-style:none;
		}
		.borbot {
			border-bottom:1px solid #efeff4;
		}
		.backfff {
			background:#fff;background-color:#fff!important;
		}
		.height30 {
			height:30px;line-height:30px;
		}
		.paddinglr {
			padding:0px 10px;
		}
		.fonts13 {
			font-size:13px;
		}
		.hidden {
			display:none;
		}
		.radioSpan {
	      position: relative;
	      border: 0 solid #95B8E7;
	      background-color: #fff;
	      vertical-align: middle;
	      display: inline-block;
	      overflow: hidden;
	      font-size:24px;
	      white-space: nowrap;
	      margin: 0;
	      padding: 0;
	      -moz-border-radius: 5px 5px 5px 5px;
	      -webkit-border-radius: 5px 5px 5px 5px;
	      border-radius: 5px 5px 5px 5px;
	      display:block;
	    }
	</style>
	<table id="withdrawal_table"></table>
	<div id="withdrawal_tb" style="padding:2px 5px;">		
		<a class="easyui-menubutton" data-options="menu:'#withdrawal_controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="auditStatus">审核状态</span></a>
		<input id="condition" data-options="'prompt':'请输入分销商姓名/联系电话/审核人名称'" class="easyui-textbox" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="query()"  data-options="'plain':true">&nbsp;搜索</a>
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="withdrawal_controll-panel" style="width: 130px; display: none;">
		<div onclick="queryAuditStatus({'auditStatus':'1','statusText':'待审核'});">待审核</div>
		<div onclick="queryAuditStatus({'auditStatus':'2','statusText':'审核中'});">审核中</div>
		<div onclick="queryAuditStatus({'auditStatus':'3','statusText':'已通过'});">已通过</div>
		<div onclick="queryAuditStatus({'auditStatus':'4','statusText':'已驳回'});">已驳回</div>
		<div class="menu-sep"></div>
		<div onclick="queryAuditStatus({'auditStatus':'','statusText':'全部'});">全部</div>
	</div>
	
	<script type="text/javascript">
		var accountType = "3";
		//默认审核状态
		var auditStatus = "1";
		//默认收支类型
		var detailType = "";
		//默认支付渠道
		var payChannel = "";
		//默认收支来源
		var detailSource = "";
		
		//定义列
		var columns = [ [ {
			field : 'personLogo',
			title : '分销商头像',
			width : 50,
			formatter : function(data,row,index){
				var index = data.indexOf('static');
				if(index != -1){
					data = '<%=basePath %>'+data.substr(index);
				}
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+data+'"/>';
			}
		},{
			field : 'personName',
			title : '分销商姓名',
			width : 100,
			align : 'center'
		},{
			field : 'payment',
			title : '提现金额',
			width : 80,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'channel',
			title : '提现方式',
			width : 80,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 1){
					return "提现到银行卡";
				}
				if(data == 2){
					return "提现到微信";
				}
			}
		},{
			field : 'createTime',
			title : '申请时间',
			width : 50,
			align : 'center'
		},{
			field : 'auditorName',
			title : '审核人名称',
			width : 100,
			align : 'center'
		},{
			field : 'auditTime',
			title : '审核时间',
			width : 50,
			align : 'center'
		},{
			field : 'auditStatus',
			title : '状态',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 1){
					return "待审核";
				}
				if(data == 2){
					return "审核中";
				}
				if(data == 3){
					return "通过";
				}
				if(data == 4){
					return "驳回";
				}
			}
		},{
			field : 'operation',
			title : '操作',
			width : 100,
			align : 'center',
			formatter : function(data,row,index){
				var html = '';
				var auditorUUID = '<%=auditorUUID%>';
				if(row.auditStatus == "2" && row.auditorUUID == auditorUUID){
					html += '<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="giveUp(\''+index+'\');">&nbsp;放弃审核&nbsp;</a><br/><br/>';
				}
				if(row.auditStatus == "1" || (row.auditStatus == "2" && row.auditorUUID == auditorUUID)){
					html += '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="agree(\''+index+'\');">&nbsp;同意&nbsp;</a>';
				}
				if(row.auditStatus == "1" || (row.auditStatus == "2" && row.auditorUUID == auditorUUID)){
					html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="reject(\''+index+'\');">&nbsp;驳回&nbsp;</a><br/><br/>';
				}
				html += '<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="detail(\''+index+'\');">&nbsp;查看详情&nbsp;</a>';
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
			$('#withdrawal_table').datagrid({
				iconCls: 'icon-order',
				title: '&nbsp;&nbsp;分销商提现审核',
				columns : columns,
				toolbar : '#withdrawal_tb',
				url : '<%=basePath %>system/account/findWithdrawalPage',
				queryParams: {"accountType":accountType,"auditStatus":auditStatus},
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				pageSize : 10,
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到提现信息哦</span>"
			});
		});
		
		function agree(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
			
			$.ajax({
				type:"post",
				url:'<%=basePath %>audit/account/beginWithdrawal',
				data:{'withdrawalUUID':row.withdrawalUUID},
				dataType:'json',
				success:function(data){
					if(data.status == "1"){
						$.messager.confirm("操作提醒", "同意该分销商提现申请？", function (c) {
							if(c){
								$.ajax({
									type:"post",
									url:'<%=basePath %>audit/account/commitWithdrawal',
									data:{'withdrawalUUID':row.withdrawalUUID,'auditStatus':3,'auditorOpinion':'审核通过'},
									dataType:'json',
									success:function(data){
										if(data.status == "1"){
											$('#withdrawal_table').datagrid('updateRow',{
											    index: rowIndex,
											    row: {
											        'auditStatus' : '3'
											    }
											});
										}else{
											$.messager.alert("提示", data.msg, "warning");
										}
									}
								});
							}
						});
					}else{
						$.messager.alert("提示", data.msg, "warning");
						$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
					}
				}
			});
		}
		
		function reject(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
			
			$.ajax({
				type:"post",
				url:'<%=basePath %>audit/account/beginWithdrawal',
				data:{'withdrawalUUID':row.withdrawalUUID},
				dataType:'json',
				success:function(data){
					if(data.status == "1"){
						$('<div id="auditor_opinion">\
							<input class="easyui-textbox" type="text" name="auditorOpinion" style="width:350px;height:220px;"  data-options="required:true,multiline:true" />\
						</div>').css({padding:"20px"}).dialog({
							id : "auditor_opinion",
				    		width : "400px",
				    		height : "350px",
				    		closable: false,
				    		modal: true,
				    		title : "审批意见",
				    		onOpen : function(){
				    			var _dialog = $(this);
				    			$.parser.parse(_dialog);
				    		},
				    		buttons:[{
								text:'确认',
								iconCls: 'icon-ok',
								handler:function(){
									var auditorOpinion = $('#auditor_opinion [textboxname=auditorOpinion]').textbox('getValue');
									if(!auditorOpinion){
										$.messager.alert("提示", "请填写审批意见", "warning");
										return;
									}
									$.ajax({
										type:"post",
										url:'<%=basePath %>audit/account/commitWithdrawal',
										data:{'withdrawalUUID':row.withdrawalUUID,'auditStatus':4,'auditorOpinion':auditorOpinion},
										dataType:'json',
										success:function(data){
											if(data.status == "1"){
												$('#withdrawal_table').datagrid('updateRow',{
												    index: rowIndex,
												    row: {
												        'auditStatus' : '4'
												    }
												});
												$('#auditor_opinion').dialog('destroy');
											}else{
												$.messager.alert("提示", data.msg, "warning");
											}
										}
									});
								}
							},{
								text:'关闭',
								iconCls: 'icon-cancel',
								handler:function(){
									$('#auditor_opinion').dialog('destroy');
							    }
							}]
				    	});
					}else{
						$.messager.alert("提示", data.msg, "warning");
						$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
					}
				}
			});
		}
		
		function giveUp(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
			
			$.messager.confirm("操作提醒", "确定要放弃该审核任务？", function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>audit/account/giveupWithdrawal',
						data:{'withdrawalUUID':row.withdrawalUUID},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#withdrawal_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: {
								        'auditStatus' : '1',
								        'auditorUUID' : '',
								        'auditorName' : '',
								        'auditOpinion' : '',
								        'auditTime' : ''
								    }
								});
							}else{
								$.messager.alert("提示", data.msg, "warning");
								$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
							}
						}
					});
				}
			});
		}
		
		function detail(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
			
			$.ajax({
				type:"post",
				url:'<%=basePath %>mobile/vip/findVipByOpenId',
				data:{'openId':row.openId},
				dataType:'json',
				success:function(data){
					if(data.status == "1"){
						var vipinfo = data.vipinfo;
						$('<div id="withdrawal_win">\
							<div class="easyui-layout" data-options="fit:true">\
								<div data-options="region:\'west\',title:\'提现信息\',iconCls:\'icon-withdrawal\',collapsible:false" style="width:300px;">\
							   		<ul>\
								   		<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">账户类型：&nbsp;<span id="accountType"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">账户名称：&nbsp;<span id="entityName"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">提现金额：&nbsp;<span id="payment"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">提现方式：&nbsp;<span id="channel"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">提现卡号：&nbsp;<span id="cardNumber"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">开户银行：&nbsp;<span id="bankName"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">卡类型：&nbsp;<span id="cardType"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">\
							   					<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" style="margin-left:50px;" onclick="accountLook(\''+rowIndex+'\');">&nbsp;查看账户&nbsp;</a>\
							   					<a class="easyui-linkbutton button-info button-xs l-btn l-btn-small" onclick="detailLook(\''+rowIndex+'\');">&nbsp;查看账单&nbsp;</a>\
							   				</div>\
							   			</li>\
							   		</ul>\
								</div>\
								<div data-options="region:\'center\',title:\'申请人信息\',iconCls:\'icon-man\'">\
							   		<ul>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">姓名：&nbsp;<span id="vipName"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">性别：&nbsp;<span id="gender"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">出生日期：&nbsp;<span id="birthday"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="mui-col-xs-2 fonts13">住址：&nbsp;<span id="address"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">联系电话：&nbsp;<span id="vipMobile"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">微信号：&nbsp;<span id="wxNumber"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">会员级别：&nbsp;<span id="vipType"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">会员状态：&nbsp;<span id="vipStatus"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">注册时间：&nbsp;<span id="createTime"></span></div>\
							   			</li>\
							   		</ul>\
								</div>\
								<div data-options="region:\'east\',title:\'审核信息\',iconCls:\'icon-inbox-document\',collapsible:false" style="width:300px;">\
							   		<ul>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">审核人：&nbsp;<span id="auditorName"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">审核状态：&nbsp;<span id="status"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
							   				<div class="fonts13">审核时间：&nbsp;<span id="auditTime"></span></div>\
							   			</li>\
							   			<li class="list_s_none borbot backfff paddinglr">\
											<p class="sc_zjz fontco4 explain">审批意见</p>\
											<textarea style="width:100%;height:220px;" id="auditorOpinion" class="fonts13"></textarea>\
										</li>\
							   		</ul>\
								</div>\
							</div>\
						</div>').window({
							width:'900px',
							height:'460px',
							title : '提现申请',
							modal : true,
							onOpen : function(){
								//重置查询参数
								accountUUID = row.accountUUID;
								var _win = $(this);
								$.parser.parse(_win);
								//初始化申请人信息
								$("#vipName").html(vipinfo.vipName);
								$("#gender").html(vipinfo.gender == "1"?"男":"女");
								$("#birthday").html(vipinfo.birthday);
								$("#address").html(vipinfo.address);
								$("#vipMobile").html(vipinfo.vipMobile);
								if(vipinfo.wxNumer){
									$("#wxNumber").html(vipinfo.wxNumber);
								}else{
									$("#wxNumber").parent('div').parent('li').hide();
								}
								$("#vipType").html(vipinfo.vipType == "1"?"普通会员":vipinfo.vipType == "2"?"付费会员":"平台管理员");
								$("#vipStatus").html(vipinfo.vipStatus == "1"?"正常":"停用");
								$("#createTime").html(vipinfo.createTime);
								//初始化提现信息
								$("#accountType").html(row.accountType == "0"?"零售商账户":row.accountType == "1"?"批发商账户":row.accountType == "2"?"会员账户":row.accountType == "3"?"佣金账户":"平台账户");
								$("#entityName").html(row.entityName);
								$("#payment").html((row.payment/100).toFixed(2)+"	元");
								$("#channel").html(row.channel == "1"?"银行卡":"微信");
								if(row.cardNumber != null){
									$("#cardNumber").html(row.cardNumber);
								}else{
									$("#cardNumber").parent('div').parent('li').hide();
								}
								if(row.bankName != null){
									$("#bankName").html(row.bankName);
								}else{
									$("#bankName").parent('div').parent('li').hide();
								}
								if(row.cardType != null){
									$("#cardType").html(row.cardType);
								}else{
									$("#cardType").parent('div').parent('li').hide();
								}
								//初始化审核信息
								$("#auditorName").html(row.auditorName);
								$("#status").html(row.auditStatus == "1"?"待审核":row.auditStatus == "2"?"审核中":row.auditStatus == "3"?"已通过":"已驳回");
								$("#auditTime").html(row.auditTime);
								$("#auditorOpinion").html(row.auditorOpinion);
							},
							onClose : function(){
								$(this).window('destroy');
							}
						});
					}else{
						$.messager.alert("提示", data.msg, "warning");
					}
				}
			});
		}
		
		//查看账户
		function accountLook(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
			$.ajax({
				type:"post",
				url:'<%=basePath %>system/account/findAccountByPc',
				data:{'accountUUID':row.accountUUID},
				dataType:'json',
				success:function(data){
					var account = data;
					$('<div>\
						<ul>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">账户名称：&nbsp;<span id="entityName"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">账户状态：&nbsp;<span id="accountStatus"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">余额：&nbsp;<span id="accountBalance"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">总收入：&nbsp;<span id="accountIncome"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">总支出：&nbsp;<span id="accountExpend"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">绑定手机号：&nbsp;<span id="mobile"></span></div>\
				   			</li>\
				   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">\
				   				<div class="fonts13">开户时间：&nbsp;<span id="createTime"></span></div>\
				   			</li>\
				   		</ul>\
					</div>').css({padding:"15px"}).window({
						width:'300px',
						height:'320px',
						title : '账户信息',
						modal : true,
						onOpen : function(){
							//重置查询参数
							var _win = $(this);
							//初始化账户信息
							$("#entityName",_win).html(account.entityName);
							$("#accountStatus",_win).html(account.accountStatus == "0"?"冻结":"正常");
							$("#accountBalance",_win).html((account.accountBalance/100).toFixed(2)+"	元");
							$("#accountIncome",_win).html((account.accountIncome/100).toFixed(2)+"	元");
							$("#accountExpend",_win).html((account.accountExpend/100).toFixed(2)+"	元");
							$("#mobile",_win).html(account.mobile);
							$("#createTime",_win).html(account.createTime);
						},
						onClose : function(){
							$(this).window('destroy');
						}
					});
				}
			});
			
		}
		
		//查看账单
		function detailLook(rowIndex){
			$('#withdrawal_table').datagrid('selectRow',rowIndex);
			var row = $('#withdrawal_table').datagrid('getSelected');
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
				title : '历史账单',
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
			}).window('open');
		}
		
		function queryAuditStatus(data){
			auditStatus = data.auditStatus;
			$("#auditStatus").html(data.statusText);
			$('#withdrawal_table').datagrid('reload',{"accountType":accountType,'auditStatus':auditStatus});
		}
		
		function query(){
			var condition = $("#condition").val();
			//使用正则表达式判断字符串是否为电话号码
			var regPos = /\d{11}/;
			if(regPos.test(condition)){
				$('#withdrawal_table').datagrid('reload',{"accountType":accountType,"auditStatus":auditStatus,"mobile":condition});
				return;
			}
			$('#withdrawal_table').datagrid('reload',{"accountType":accountType,"auditStatus":auditStatus,"personName":condition,"auditorName":condition});
		}
		
		function initQuery(){
			$("#condition").textbox("setValue",'');
			$('#withdrawal_table').datagrid('reload',{'accountType':accountType,'auditStatus':auditStatus});
		}
		
		function queryDetailType(data){
			detailType = data.detailType;
			$("#detailType").html(data.text);
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource});
		}
		
		function initDetailQuery(){
			$("#detailCondition").textbox("setValue",'');
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
			var condition = $("#detailCondition2").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource,'money':condition});
				return;
			}
			$('#detail_table').datagrid('reload',{'accountUUID':accountUUID,'detailType':detailType,'payChannel':payChannel,'detailSource':detailSource,'orderNo':condition,'otherName':condition});
		}
		
	</script>