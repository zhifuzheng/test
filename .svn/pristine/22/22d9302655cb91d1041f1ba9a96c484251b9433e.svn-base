<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	
	<table id="log_table"></table>
	<div id="log_tb" style="padding:2px 5px;">
		<a class="easyui-menubutton" data-options="menu:'#payChannel_controll_panel',iconCls:'icon-help'" style="width:120px;"><span id="payChannel">支付渠道</span></a>		
		<input id="logCondition" data-options="'prompt':'请输入模块名称/操作名称/内容'" class="easyui-textbox" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="queryLog()"  data-options="'plain':true"><i class="icon iconfont icon-search"></i>&nbsp;搜索</a>
		<a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="payChannel_controll_panel" style="width: 100px; display: none;">
		<div onclick="queryPayChannel({'payChannel':'1','text':'微信'});">微信</div>
		<div onclick="queryPayChannel({'payChannel':'0','text':'余额'});">余额</div>
		<div class="menu-sep"></div>
		<div onclick="queryPayChannel({'payChannel':'','text':'全部'});">全部</div>
	</div>
	
	<script type="text/javascript">
		//默认支付渠道
		var payChannel = "";
		
		//定义列
		var columns = [ [ {
			field : 'payChannel',
			title : '交易渠道',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 1){
					return "微信";
				}
				if(data == 2){
					return "支付宝";
				}
				if(data == 3){
					return "余额";
				}
			}
		},{
			field : 'moduleName',
			title : '模块名称',
			width : 30,
			align : 'center'
		},{
			field : 'functionName',
			title : '操作名称',
			width : 50,
			align : 'center'
		},{
			field : 'content',
			title : '内容',
			width : 120,
			align : 'left'
		},{
			field : 'payment',
			title : '交易金额',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'tradeNo',
			title : '交易流水号',
			width : 110,
			align : 'center'
		},{
			field : 'orderNo',
			title : '交易订单号',
			width : 110,
			align : 'center'
		},{
			field : 'payTime',
			title : '支付时间',
			width : 50,
			align : 'center'
		} ] ];
		
		
		$(function(){
			//初始化账户数据表格
			$('#log_table').datagrid({
				iconCls: 'icon-order',
				title: '&nbsp;&nbsp;日志管理',
				columns : columns,
				toolbar : '#log_tb',
				url : '<%=basePath %>system/account/findAccountLogPage',
				queryParams: {"payChannel":payChannel},
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				pageSize : 10,
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到日志信息哦</span>"
			});
		});
		
		function queryPayChannel(data){
			payChannel = data.payChannel;
			$("#payChannel").html(data.text);
			$('#log_table').datagrid('reload',{'payChannel':payChannel});
		}
		
		function queryLog(){
			var condition = $("#logCondition").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#log_table').datagrid('reload',{'payChannel':payChannel,'payment':condition});
				return;
			}
			$('#log_table').datagrid('reload',{'payChannel':payChannel,'moduleName':condition,'functionName':condition,'content':condition});
		}
		
		function initQuery(){
			$("#logCondition").textbox("setValue",'');
			$('#log_table').datagrid('reload',{'payChannel':payChannel});
		}
		
	</script>