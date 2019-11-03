<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<style>
		.paramData p{
			font-size: 18px;
	    	text-align: center;
    		line-height: 500px;
		}
	</style>
	<table id="all_order_table"></table>
	<div id="all_order_tb" style="padding:2px 5px;">
		<input class="easyui-combobox" name="entityName" id="entityName" data-options="'prompt':'请输入店铺名称'" style="width:280px;" />
		<a class="easyui-menubutton" data-options="menu:'#del-controll-panel2',iconCls:'icon-help'" style="width:120px;"><span id="isShow">删除状态</span></a>		
		<a class="easyui-menubutton" data-options="menu:'#controll-panel2',iconCls:'icon-help'" style="width:130px;"><span id="orderStatus">订单状态</span></a>
		<input id="orderCondition" data-options="'prompt':'请输入订单编号/总价/买家名称/买家电话'" class="easyui-textbox" style="width:280px;">
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="queryOrder()" data-options="'plain':true">&nbsp;搜索</a>
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="del-controll-panel2" style="width: 80px; display: none;">
		<div onclick="queryDel({'isShow':'0','text':'未删除'});">未删除</div>
		<div onclick="queryDel({'isShow':'1','text':'已删除'});">回收站</div>
		<div class="menu-sep"></div>
		<div onclick="queryDel({'isShow':'','text':'全部'});">全部</div>
	</div>
	<div id="controll-panel2" style="width: 130px; display: none;">
		<div onclick="queryOrderStatus({'orderStatus':'0','text':'待付款'});">待付款</div>
		<div onclick="queryOrderStatus({'orderStatus':'1','text':'待接单'});">待接单</div>
		<div onclick="queryOrderStatus({'orderStatus':'2','text':'待收货'});">待收货</div>
		<div onclick="queryOrderStatus({'orderStatus':'3','text':'待评价'});">待评价</div>
		<div class="menu-sep"></div>
		<div onclick="queryOrderStatus({'orderStatus':'','text':'全部订单'});">全部订单</div>
		<div onclick="queryOrderStatus({'orderStatus':'refund','text':'退款/退货'});">退款/退货</div>
	</div>
	
	<script type="text/javascript">
		//默认订单删除状态
		var isShow = "0";
		//默认订单支付状态
		var orderStatus = "1";
		// 定义列
		var columns = [ [ {
			field : 'entityName',
			title : '店铺名称',
			width : 60,
			align : 'center'
			
		},{
			field : 'orderUUID',
			title : '订单编号',
			width : 120,
			align : 'center'
			
		},{
			field : 'receiverMobile',
			title : '买家联系电话',
			width : 60,
			align : 'center'
			
		},{
			field : 'totalPrice',
			title : '订单总价',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'reducePrice',
			title : '优惠金额',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return 0.00;
			}
		},{
			field : 'payment',
			title : '实付金额',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'totalCount',
			title : '商品数量',
			width : 30,
			align : 'center'
		},{
			field : 'firstIncome',
			title : '一度收入',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
			
		},{
			field : 'secondIncome',
			title : '二度收入',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'orderStatus',
			title : '状态',
			width : 40,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 0){
					return "待付款";
				}
				if(data == 1){
					return "待接单";
				}
				if(data == 2){
					return "待收货";
				}
				if(data == 3){
					return "待评价";
				}
				if(data == 4){
					return "交易完成";
				}
				if(data == -1){
					return "交易取消";
				}
				if(data == -2){
					return "超时关闭";
				}
				if(data == -3){
					return "待退款";
				}
				if(data == -4){
					return "同意退款";
				}
				if(data == -5){
					return "驳回退款";
				}
				if(data == -6){
					return "待退货";
				}
				if(data == -7){
					return "同意退货";
				}
				if(data == -8){
					return "驳回退货";
				}
				if(data == -9){
					return "已全部退货";
				}
			}
		},{
			field : 'operation',
			title : '操作',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				return '<a href="javascript:void(0)" style="margin:3px 0px;" class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="orderLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';;
			}
		} ] ];
		
		var paramColumns = [ [ {
			field : 'itemImg',
			title : '商品图片',
			width : 60,
			formatter : function(data,row,index){
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="<%=basePath %>'+data+'"/>';
			}
		},{
			field : 'title',
			title : '标题',
			width : 80,
			align : 'center'
		},{
			field : 'price',
			title : '价格',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'reducePrice',
			title : '优惠金额',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'payment',
			title : '实付金额',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'count',
			title : '数量',
			width : 30,
			align : 'center'
		},{
			field : 'firstRatio',
			title : '一度抽成',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (row.distributeStatus?data:0) * 100 + '%';
			}
			
		},{
			field : 'secondRatio',
			title : '二度抽成',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (row.distributeStatus?data:0) * 100 + '%';
			}
		} ] ];
		
		$(function(){
			$.ajax({
				url : baseURL+'shop/item/findAllEntity',
				data:{'entityTableName':'business_apply'},
				type:"post",
				success:function(data){
					$('#entityName').combobox({
						data: data,
						valueField:'businessName',
						textField:'businessName',
			            prompt:'请输入店铺名称',
			            hasDownArrow:true,
			            panelHeight:280,
			            filter: function(q, row){
			                var opts = $(this).combobox('options');
			                return row[opts.textField].indexOf(q) == 0;
			            }
			        });
				},
				dataType:'json'
			});
			//初始化订单数据表格
			$('#all_order_table').datagrid({
				iconCls: 'icon-order',
				title: '&nbsp;&nbsp;订单管理',
				columns : columns,
				toolbar : '#all_order_tb',
				url : '<%=basePath %>shop/order/findOrderPage',
				queryParams: {"orderStatus":orderStatus,"isShow":isShow},
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				pageSize : 10,
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到订单信息哦</span>"
			});
		});
		
		//查看订单详情
		function orderLook(rowIndex){
			$('#all_order_table').datagrid('selectRow',rowIndex);
			var row = $('#all_order_table').datagrid('getSelected');
			$('<div>\
				<div class="easyui-layout" data-options="fit:true">\
					<div data-options="region:\'west\',split:true" style="width:800px;">\
						<table id="param_look_table"></table>\
					</div>\
					<div class="paramData" data-options="region:\'center\',border:false">\
					</div>\
				</div>\
			</div>').window({
				width:'1100px',
				height:'550px',
				title : '订单详情',
				modal : true,
			    onOpen : function(){
			    	var _win = $(this);
			    	$.parser.parse($(this));
			    	$('#param_look_table',_win).datagrid({
						columns : paramColumns,
						rownumbers : true,
						url : '<%=basePath %>shop/orderItem/findOrderItemPage',
						queryParams: {"orderUUID":row.orderUUID},
						pagination : true,
						pageList : [4,10,20,30,40],
						pageSize : 4,
						fit : true,
						fitColumns : true,
						nowrap : false,
						singleSelect : true,
						onClickRow : doClickRow,
						emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到商品信息哦</span>"
					});
			    	$('.paramData').html('<p>单击表格查看商品规格</p>');
			    },
			    onClose : function(){
			    	$(this).window('destroy');
			    }
			});
		}
		
		//查看商品规格
		function doClickRow(rowIndex,rowData){
			if(rowData.paramData != null){
				var table = '<table width="100%" cellpadding="0" cellspacing="0">';
				table += '<tr><td colspan="2" style="font-weight:bold;text-align:center;background-color:#EAEAEA;height: 40px;">规格参数</td></tr>';
				var paramData = JSON.parse(rowData.paramData);
		    	for(var key in paramData){
					table += '<tr><td style="width:140px;text-align-last: justify; font-size: 14px;text-align:left;border-bottom:#EAEAEA solid 1px;height: 40px;text-indent: 1em;"><span style="color:red;">'+key+'</span></td><td style="width:200px;border-bottom:#EAEAEA solid 1px;text-indent:2em;">'+paramData[key]+'</td></tr>';
				}
		    	table += "</table>";
		    	$('.paramData').html(table);
			}else{
				$('.paramData').html('<p>这件商品没有商品规格哦</p>');
			}
		}
		
		function queryDel(data){
			isShow = data.isShow;
			$("#isShow").html(data.text);
			$('#all_order_table').datagrid('reload',{'orderStatus':orderStatus,'isShow':isShow});
		}
		
		function queryOrderStatus(data){
			orderStatus = data.orderStatus;
			$("#orderStatus").html(data.text);
			$('#all_order_table').datagrid('reload',{'orderStatus':orderStatus,'isShow':isShow});
		}
		
		function queryOrder(){
			var entityName = $("#entityName").combobox('getValue');
			var condition = $("#orderCondition").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#all_order_table').datagrid('reload',{'orderStatus':orderStatus,'isShow':isShow,'entityName':entityName,"totalPrice":condition});
				return;
			}
			$('#all_order_table').datagrid('reload',{'orderStatus':orderStatus,'isShow':isShow,'entityName':entityName,"orderUUID":condition,"payerName":condition,"receiverMobile":condition});
		}
		
		function initQuery(){
			$("#entityName").combobox('setValue','');
			$("#orderCondition").textbox('setValue','');
			$('#all_order_table').datagrid('reload',{'orderStatus':orderStatus,'isShow':isShow});
		}
		
	</script>