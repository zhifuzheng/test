<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 用户优惠劵领取记录 -->
<style>
	.left{
		float: left;
	}
	.buttonSubmit {
		width: 100px;}
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

	<table id="data-list"></table>
	<!-- 筛选条件 -->
	<div style="width:99%;height:50px;" id="condition">
		<div class="left" style="line-height: 50px;width:15%">
			<span>排序：</span>
			<select class="select-box" id="range" onchange="getUserCouponList(1)">
				<option value="noLimit">不限</option>
				<option value="money">面值</option>
				<option value="discount">折扣</option>
				<option value="getTime">领取日期</option>
				<option value="useTime">使用日期</option>
			</select>
		</div>
		<div class="left" style="line-height: 50px;margin-left: 20px;width:20%">
			<span>类型：</span>
			<select id="type" onchange="getUserCouponList(2)">
				<option value="noLimit">不限</option>
				<option value="1">代金劵</option>
				<option value="2">折扣劵</option>
			</select>
		</div>
		<div class="left" style="line-height: 50px;margin-left: 20px;width:15%">
			<span>状态：</span>
			<select id="status" onchange="getUserCouponList(3)">
				<option value="noLimit">不限</option>
				<option value="1">未使用</option>
				<option value="2">已使用</option>
				<option value="3">已过期</option>
			</select>
		</div>
		<!-- 优惠劵名称搜索输入 -->
		<div style="line-height: 50px;width:30%;float: right;">
			<input type="text" class="easyui-textbox" prompt="请输入优惠劵名称搜索" style="width:79%" id="searchWords"></input>
			<input onclick="getUserCouponList(4)" type="submit" style="cursor: pointer;color:#ffffff;width:17%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%" value="搜索">
			<!-- <button onclick="getUserCouponList(4)" style="color:#ffffff;width:17%;line-height:30px;background-color: rgba(0, 0, 255, 0.7);border-radius: 5px;margin-left:2%">搜索</button> -->
		</div>
	</div>
<script>
	var columns = [[
		{
			field:"couponName",
			title:"劵名称",
			width:100,
			align:"center"
		},
		{
			field:"couponType",
			title:"劵类型",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				return data ==1?"代金劵":"折扣劵"
			}
		},
		{
			field:"moneyOrDiscount",
			title:"面值/折扣",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				if(row.couponType == 1){
					return row.money
				}
				if(row.couponType == 2){
					return row.discount*10
				}
			}
		},
		{
			field:"effectMoney",
			title:"使用条件",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				return "满"+data+"元可使用";
			}
		},
		{
			field:"getTime",
			title:"领取日期",
			width:100,
			align:"center"
		},
		{
			field:"useTime",
			title:"使用日期",
			width:100,
			align:"center"
		},
		{
			field:"isUse",//优惠劵的是否使用，1-未使用，2-已使用，3-已过期
			title:"状态",
			width:100,
			align:"center",
			formatter:function(data,row,index){
				return data==1?"未使用":data==2?"已使用":"已过期";
			}
		},
	]];

	
	/* 页面加载,按默认条件获取优惠劵 */
	$(function(){
		//获取查询条件
		var range = $("#range").val();
		var type = $("#type").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		$("#data-list").datagrid({
			url:'<%=path %>'+"/system/coupon/getUserCouponList",
			queryParams:{
				range:range,
				type:type,
				status:status,
				searchWords:searchWords,
			},
			title: '&nbsp;&nbsp;我的领劵记录', 
			toolbar: '#condition',
			columns:columns,
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
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到相关信息</span>"
		});
		
	})
	
	/* 条件获取优惠劵列表 */
	function getUserCouponList(e){
		var range = $("#range").val();
		var type = $("#type").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		$("#data-list").datagrid('reload',{'range':range,'type':type,'status':status,'searchWords':searchWords});
	}
	
	/* // 获取当前时间
	var d=new Date();
		var year=d.getFullYear();
		var month=change(d.getMonth()+1);
		var day=change(d.getDate());
		var hour=change(d.getHours());
		var minute=change(d.getMinutes());
		var seconde=change(d.getSeconds())
		function change(t){
		if(t<10){
				return "0"+t;
		}else{
				return t;
		}
		}
		var nowtime = year + '-' + month + '-' + day + ' ' + hour + ':' + minute+":"+seconde; */

</script>
