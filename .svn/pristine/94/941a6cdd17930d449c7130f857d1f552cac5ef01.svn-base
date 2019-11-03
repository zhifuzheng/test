<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!-- 优惠劵管理 -->
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
	input[type="checkbox"] {
		width: 15px;
		height: 15px;
	}
</style>
		<table id="data-list" style="width:100%;"></table>
		<!-- 筛选条件 -->
		<div style="width: 100%; height: 100px;" id="condition">
			<div style="height: 50px; margin: auto; width: 99%;">
				<div class="left" style="line-height: 50px; width: 20% !important">
					<span>发行方式：</span> <select style="width: 70%;" id="releaseType"
						onchange="getCouponListby(1)">
						<option value="noLimit">不限</option>
						<option value="1">线上</option>
						<option value="2">线下</option>
					</select>
				</div>
				<div class="left"
					style="line-height: 50px; margin-left: 10px; width: 15%">
					<span>排序：</span> <select class="select-box" id="range"
						onchange="getCouponListby(2)">
						<option value="noLimit">不限</option>
						<option value="createTime">最新</option>
						<option value="money">面值</option>
						<option value="discount">折扣</option>
						<option value="releaseNumbers">发行量</option>
						<option value="getNumbers">领取量</option>
						<option value="usedNumbers">使用量</option>
					</select>
				</div>

				<div class="left"
					style="line-height: 50px; margin-left: 10px; width: 15%">
					<span>类型：</span> <select id="type" onchange="getCouponListby(3)">
						<option value="noLimit">不限</option>
						<option value="1">代金劵</option>
						<option value="2">折扣劵</option>
					</select>
				</div>
				<div class="left"
					style="line-height: 50px; margin-left: 10px; width: 15%">
					<span>状态：</span> <select id="status" onchange="getCouponListby(4)">
						<option value="noLimit">不限</option>
						<option value="1">已上架</option>
						<option value="2">未上架</option>
						<option value="3">已下架</option>
						<option value="releaseEndTime">已过领取时间</option>
						<option value="disabledTime">已过使用时间</option>
					</select>
				</div>
				<!-- 优惠劵名称搜索输入 -->
				<div style="line-height: 50px; width: 30%; float: right;">
					<input type="text" class="easyui-textbox" prompt="请输入优惠劵名称搜索" style="width: 79%" id="searchWords"></input>
					<input onclick="getCouponListby(5)" type="submit" style="cursor: pointer;color: #ffffff; width: 17%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 2%" value="搜索">
					<!-- <button onclick="getCouponListby(5)" style="color: #ffffff; width: 17%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 2%">搜索</button> -->
				</div>
			</div>

			<div style="line-height: 50px; min-width: 30%; width: auto">
				<span>所属店铺：</span> <select style="min-width: 10%; width: auto"
					id="entity-list" onchange="getCouponListby(6)">
					<option value="noLimit">不限</option>
				</select>
			</div>
		</div>
	

	<!-- 线下优惠劵二维码生成下载 -->
	<div id="create-down-QRcode" style="display: none;">
		<form id="integralRule" method="post">
			<table
				style="width: 50%; margin: auto; border-bottom: 0px solid #c5c5c5;">
				<tr style="display: block; margin: 10px; width: 100%">
					<td style="width: 120px;">优惠劵批次编号:</td>
					<td style="width: 250px;" id="batchNumber"></td>
				</tr>
				<tr style="display: block; margin: 10px; width: 100%">
					<td style="width: 120px;">可下载数量:</td>
					<td style="width: 250px;" id="downNumber"></td>
				</tr>
				<tr style="display: block; margin: 10px; width: 100%">
					<td style="width: 120px;">二维码下载数量:</td>
					<td style="width: 250px;"><input id="QRcode-number"
						type="number" class="easyui-textbox" name="QRcodeNumber"
						style="width: 100%" /></td>
				</tr>
			</table>
		</form>
		<div style="width: 45%; margin: auto; margin-top: 20px;">
			<input onclick="downloadCoupon(1,1)" type="submit" style="cursor: pointer;color: #ffffff; width: 25%; line-height: 30px; background-color: rgba(255, 0, 0, 0.7); border-radius: 5px;" value="下载">
			<input onclick="downloadCoupon(3,3)" type="submit" style="cursor: pointer;color: #ffffff; width: 25%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 4%" value="取消">
			<!-- <button onclick="downloadCoupon(1,1)" style="color: #ffffff; width: 25%; line-height: 30px; background-color: rgba(255, 0, 0, 0.7); border-radius: 5px;">下载</button>
			<button onclick="downloadCoupon(3,3)" style="color: #ffffff; width: 25%; line-height: 30px; background-color: rgba(0, 0, 255, 0.7); border-radius: 5px; margin-left: 4%">取消</button> -->
		</div>
		<div
			style="width: 50%; margin-top: 10px; margin-left: 220px; color: #8e8e8e;">
			<p>备注：二维码下载数量不能超过该批次优惠劵剩余可下载数量</p>
		</div>
		<div id="downloadImg"></div>
	</div>



<script>
	//定义列
	var columns = [[
		{
			field:'batchNumber',
			title:'批次编号',
			width:90,
			align:'center'
		},
		{
			field:'releaseType',//劵发行方式，1-线上，2-线下
			title:'发行方式',
			width:40,
			align:'center',
			formatter:function(data,row,index){
				return data ==1?"线上":"线下";
			}
		},
		{
			field:'couponName',
			title:'名称',
			width:120,
			align:'center'
		},
		{
			field:'couponType',//劵的类型，1-代金劵，2-优惠劵(折扣劵)
			title:'类型',
			width:40,
			align:'center',
			formatter:function(data,row,index){
				return data ==1?"代金劵":"折扣劵";
			}
		},
		{
			field:'moneyOrDiscount',//money discount
			title:'面值/折扣',
			width:50,
			align:'center',
			formatter:function(data,row,index){
				if(row.couponType == 1){
					return row.money;
				}else{
					return row.discount;
				}
			}
		},
		{
			field:'effectMoney',//effectMoney
			title:'使用条件',
			width:70,
			align:'center',
			formatter:function(data,row,index){
				return "满"+data+"元可用";
			}
		},
		{
			field:'releaseNumberOrStock',//releaseNumbers  stock
			title:'发行/剩余数量',
			width:60,
			align:'center',
			formatter:function(data,row,index){
				return row.releaseNumbers+"/"+row.stock;
			}
		},
		{
			field:'activityTitle',//getNumbers usedNumbers
			title:'领取/使用数量',
			width:60,
			align:'center',
			formatter:function(data,row,index){
				return row.getNumbers+"/"+row.usedNumbers;
			}
		},
		{
			field:'isRelease',//优惠劵是否上架,1-已上架，2-未上架,3-已下架
			title:'状态',
			width:40,
			align:'center',
			formatter:function(data,row,index){
				return data ==1?"已上架":data ==2?"未上架":"已下架";
			}
		},
		{
			field:'operation',
			title:'操作',
			width:130,
			align:'left',
			formatter:function(data,row,index){//1-已上架，2-未上架,3-已下架
				var html = "";
				if(row.isRelease == 1){//上架 ：下架
					html = '<a style="background-color:rgba(255, 0, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="downCoupon(\''+row.batchUUID+'\');">&nbsp;下架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(100, 100, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="getCouponUseRecord(\''+row.batchUUID+'\');">&nbsp;使用详情&nbsp;</a>';
				}
				if(row.isRelease == 3){//下架：上架，编辑
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineCoupon(\''+row.batchUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(100, 100, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="getCouponUseRecord(\''+row.batchUUID+'\');">&nbsp;使用详情&nbsp;</a>';
					/* html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorCoupon(\''+row.batchUUID+'\');">&nbsp;编辑&nbsp;</a>'; */
				}
				
				if(row.isRelease == 2){//未上架：上架，删除,编辑
					html = '<a style="background-color:rgba(0, 180, 0, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="onlineCoupon(\''+row.batchUUID+'\');">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(255, 0, 0, 0.7);color:#fff" class="easyui-linkbutton  button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="deleteCoupon(\''+row.batchUUID+'\');">&nbsp;删除&nbsp;</a>';
					html += '&nbsp;&nbsp;<a style="background-color:rgba(0, 0, 255, 0.7);color:#fff" class="easyui-linkbutton button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editorCoupon(\''+row.batchUUID+'\');">&nbsp;编辑&nbsp;</a>';
				}
				
				if(row.releaseType == 2 && row.isRelease == 1){//线下
					html += "&nbsp;&nbsp;<a style='background-color:rgba(0, 180, 0, 0.7);color:#fff' class='easyui-linkbutton button-xs l-btn l-btn-small' data-options='plain: true, iconCls:icon-remove' onclick='downloadQRcode("+JSON.stringify(row)+")'>&nbsp;二维码下载&nbsp;</a>";
				}
				return html;
			}
		}
	]];

	/* 数据加载 */
	$(function(){
		//获取查询条件
		var range = $("#range").val();
		var type = $("#type").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		var releaseType = $("#releaseType").val();
		var business = $("#entity-list").val();//店铺uuid（一个用户可能有多件店铺）
		$("#data-list").datagrid({
			url:'<%=path %>'+"/system/coupon/findCouponList",
			queryParams:{
				range:range,
				type:type,
				status:status,
				searchWords:searchWords,
				releaseType:releaseType,
				business:business
			},
			title: '&nbsp;&nbsp;优惠劵管理', 
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
		//店铺列表
		getEntityInfo();
	});
	
	/* 条件获取优惠劵列表 */
	function getCouponListby(e){
		//获取查询条件
		var range = $("#range").val();
		var type = $("#type").val();
		var status = $("#status").val();
		var searchWords = $("#searchWords").val();
		var releaseType = $("#releaseType").val();
		var business = $("#entity-list").val();//店铺uuid（一个用户可能有多件店铺）
		$("#data-list").datagrid('reload',{'range':range,'type':type,'status':status,'searchWords':searchWords,'releaseType':releaseType,'business':business});
	}
	
	/* 下架优惠劵 */
	function downCoupon(batchUUID){
		layer.confirm('您确认下架该优惠劵么?', function(){
			$.ajax({
				url:baseURL+"system/coupon/downCoupons",
				type:"POST",
				dataType:"json",
				data:{"batchUUID":batchUUID},
				success:function(data){
					layer.alert(data.msg);
					if(data.status == 1){
						//获取查询条件
						var range = $("#range").val();
						var type = $("#type").val();
						var status = $("#status").val();
						var searchWords = $("#searchWords").val();
						var releaseType = $("#releaseType").val();
						var business = $("#entity-list").val();//店铺uuid（一个用户可能有多件店铺）
						$("#data-list").datagrid('reload',{'range':range,'type':type,'status':status,'searchWords':searchWords,'releaseType':releaseType,'business':business});
					}
				}
			});
		});
	}
	
	/* 上架优惠劵 */
	function onlineCoupon(batchUUID){
		layer.confirm('您确认上架批优惠劵么?', function(){
			$.ajax({
				url:baseURL+"system/coupon/putawayCoupons",
				type:"POST",
				dataType:"json",
				data:{"batchUUID":batchUUID},
				success:function(data){
					layer.alert(data.msg);
					if(data.status == 1){
						//获取查询条件
						var range = $("#range").val();
						var type = $("#type").val();
						var status = $("#status").val();
						var searchWords = $("#searchWords").val();
						var releaseType = $("#releaseType").val();
						var business = $("#entity-list").val();//店铺uuid（一个用户可能有多件店铺）
						$("#data-list").datagrid('reload',{'range':range,'type':type,'status':status,'searchWords':searchWords,'releaseType':releaseType,'business':business});
					}
				}
			});
		});
	}
	
	/* 删除优惠劵 */
	function deleteCoupon(batchUUID){
		layer.confirm('您确认删除该批优惠劵么?', function(){
			$.ajax({
				url:baseURL+"system/coupon/deleteCoupons",
				type:"POST",
				dataType:"json",
				data:{"batchUUID":batchUUID},
				success:function(data){
					layer.alert(data.msg);
					if(data.status == 1){
						//获取查询条件
						var range = $("#range").val();
						var type = $("#type").val();
						var status = $("#status").val();
						var searchWords = $("#searchWords").val();
						var releaseType = $("#releaseType").val();
						var business = $("#entity-list").val();//店铺uuid（一个用户可能有多件店铺）
						$("#data-list").datagrid('reload',{'range':range,'type':type,'status':status,'searchWords':searchWords,'releaseType':releaseType,'business':business});
					}
				}
			});
		});
	}
	
	/* 编辑优惠劵(未上架和已下架) */
	function editorCoupon(batchUUID){
		/* 编辑未上架的优惠劵 */
		loadCenterLayout("coupon/editorCoupon.jsp?batchUUID="+batchUUID);
	}

	
	var index = null;
	var batchUUID = null;
	/* 线下优惠劵二维码下载 */
	function downloadQRcode(row){
		var batchNumber = row.batchNumber;
		var downNumber = row.releaseNumbers-row.usedNumbers;//可下载数量，未使用和未绑定二维码均可下载
		$("#batchNumber").text(batchNumber);
		//设置可下载数量
		$("#downNumber").text(downNumber);
		batchUUID = row.batchUUID;
		//弹出层
		index = layer.open({
	  		type: 1,
	  		title:'线下优惠劵二维码下载',
	  		area: ['800px', '450px'],
	  		fixed: true, 
	  		maxmin: true,
	  		content: $('#create-down-QRcode')
		});
	}
	
	/* 确认和取消 */
	function downloadCoupon(e,f){
		 if(e == 1 || e == 2){//下载
			//判断用户输入的下载量是否大于可下载数量
			var isDownNumber = parseInt($("#downNumber").text()); //可下载数量
			var downNumber = parseInt($("#QRcode-number").val());//下载数量
			
			if(downNumber > isDownNumber){
				layer.alert("下载数量不能超过可下载数量");
				return;
			}
			/* 发送请求获取二维码图片 */
			$.ajax({
				url:baseURL+"system/coupon/getCouponQRcodeImgList",
				type:"POST",
				dataType:"json",
				data:{"batchUUID":batchUUID,"downNumber":downNumber,'basePath':'<%=basePath%>'},
				success:function(data){
					if(data.status == "1"){
						var url = '<%=basePath%>'+data.filezip;
						var html = '<a id="dw" href="'+url+'" download=""></a>';
						$("#downloadImg").append(html);
						//自动触发a标签的点击事件
						document.getElementById("dw").click();
						//TODO 如何删除中间产生的下载图片压缩包	
						layer.close(index);
						loadCenterLayout('coupon/couponManage.jsp');
					}else{
						layer.alert("系统繁忙，请稍后重试");
					}
				}
			});
		 }else{
			 layer.close(index);
		 }
	}
	
	/* 优惠劵使用详情 */
	function getCouponUseRecord(batchUUID){
		loadCenterLayout("coupon/couponUseDetail.jsp?batchUUID="+batchUUID);
	}
	/* 获取登陆用户的店铺信息 */
	function getEntityInfo(){
		$.ajax({
			url:baseURL+'system/coupon/getUserEntityInfo',
			type:'POST',
			dataType:'json',
			success:function(data){
				if(data.status == "-1"){
					$.messager.alert('提示',data.msg,'info');
				}
				if(data.status == "1"){
					for(var i = 0;i<data.businessApplies.length;i++){
						var html = '<option value="'+data.businessApplies[i].businessUUID+'">'+data.businessApplies[i].businessName+'</option>';
						$("#entity-list").append(html);
					}
				}
			}
		});
	}
	
	
</script>
