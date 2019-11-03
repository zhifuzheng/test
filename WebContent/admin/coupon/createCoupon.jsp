<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style>
.easyui-textbox {
	border: 1px solid #00ff00;
}

.buttonSubmit {
	width: 100px;
	line-height: 33px;
	background-color: rgba(0, 0, 255, 0.7);
	color: #fff !important;
}

#coupon2 {
	visibility: collapse;
}

.th, .td {
	border-top: 1px solid #c5c5c5 !important;
	border-left: 1px solid #c5c5c5 !important;
}

table {
	border-bottom: 1px solid #c5c5c5;
}

input[type="radio"] {
	width: 15px;
	height: 15px;
}






</style>

<!-- 商家添加优惠劵 -->
<div style="padding:10px 10px 10px 10px">
	<form id="couponAdd" class="couponForm" method="post">
		<table style="width:100%;">
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">发行店铺名称:</td>
				<td>
					<!-- <input id="entity-list" type="text" style="width:50%;border:1px solid #ccc;line-height:30px" /> -->
					<input class="easyui-combobox" name="entity-list" id="entity-list" data-options="required:true" style="width:50%;" />
				</td>
			</tr>
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">优惠劵名称:</td>
				<td>
					<input prompt="请输入优惠劵名称(50字以内)" class="easyui-textbox" id="couponName" type="text" name="couponName" data-options="required:true,validType:'length[1,50]'" style="width:50%;margin-top: 30px;"></input>
				</td>
			</tr>
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">优惠劵副标题:</td>
				<td>
					<input prompt="请输入优惠劵副标题(50字以内)" class="easyui-textbox" type="text" name="couponSubhead" style="width:50%;margin-top: 30px;"></input>
				</td>
			</tr>
			<tr class="breadth" style="height:35px;width:100%;">
				<td class="breadthT" style="width:10%">发行方式:</td>
				<td>
					<label><input  type="radio" name="releaseType" checked="checked" value="1"><span>&nbsp;线上&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input  type="radio"  name="releaseType" value="2"><span>&nbsp;线下</span></label>
				</td>
			</tr>
			<tr class="breadth" style="height:20px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">线下优惠劵只能在线下领取，通过扫二维码的方式进行使用;线上优惠劵则是线上领取，直接使用</td>
			</tr>
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">优惠劵类型:</td>
				<td>
					<label><input class="select" type="radio" name="couponType" checked="checked" value="1"><span>&nbsp;代金劵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input class="select" type="radio"  name="couponType" value="2"><span>&nbsp;折扣劵</span></label>
				</td>
			</tr>
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">发行数量:</td>
				<td style="width:92%;line-height:50px;">
					<span >此劵共发放：</span>
					<input prompt="发行数量" class="easyui-textbox" type="number" id="releaseNumbers" name="releaseNumbers" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >张，限每人领取：</span>
					<input prompt="限领数量" class="easyui-textbox" type="number" id="repeatNumber" name="repeatNumber" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >张</span>
				</td>
			</tr>
			<tr class="breadth" style="height:30px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">优惠券发放总数量，没有不能领取或发放。限领张数-1为不限制领取张数</td>
			</tr>
			<!-- 代金劵 -->
			<tr class="breadth" style="height:50px;width:100%" id="coupon1">
				<td class="breadthT" style="width:10%">代金劵领取限制:</td>
				<td style="width:90%;line-height:50px;">
					<span >领取此劵需：</span>
					<input prompt="所需积分" class="easyui-textbox" type="number" id="integration1" name="integration1" data-options="required:true" style="width:10%;margin-top: 30px;" ></input>
					<span >积分，此劵面值：</span>
					<input prompt="面值" class="easyui-textbox" type="number" id="money" name="money" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >元，订单满：</span>
					<input prompt="生效金额" class="easyui-textbox" type="number" id="effectMoney1" name="effectMoney1" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >元可以使用</span>
				</td>
			</tr>
			<!-- 优惠劵 -->
			<tr class="breadth" style="height:50px;width:100%;" id="coupon2">
				<td class="breadthT" style="width:10%;">优惠劵领取限制:</td>
				<td style="width:90%;line-height:50px;">
					<span >领取此劵需：</span>
					<input prompt="所需积分" class="easyui-textbox" type="number" id="integration2" name="integration2" data-options="required:true" style="width:10%;margin-top: 30px;" ></input>
					<span >积分，此劵可打：</span>
					<input prompt="折扣" class="easyui-textbox" type="number" id="discount" name="discount" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >折，订单满：</span>
					<input prompt="生效金额" class="easyui-textbox" type="number" id="effectMoney2" name="effectMoney2" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >元可以使用，最高可优惠</span>
					<input prompt="最高优惠金额" class="easyui-textbox" type="number" id="reduceMaxPrice" name="reduceMaxPrice" data-options="required:true" style="width:10%;margin-top: 30px;"></input>
					<span >元</span>
				</td>
			</tr>
			<tr class="breadth" style="height:30px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">券领取所需的积分是会员个人积分，零积分即免费领取。折扣请填0-1之间2位小数的数值。例：填0.98，相当于9.8折。商品价格是：商品价X0.98</td>
			</tr>
			
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">优惠劵领取时间:</td>
				<td>
					<input prompt="领取开始时间" type="text" id="releaseStartTime" name="releaseStartTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;margin-top: 30px;">
					<span>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;</span>
					<input prompt="领取结束时间" type="text" id="releaseEndTime" name="releaseEndTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;margin-top: 30px;">
				</td>
			</tr>
			<tr class="breadth" style="height:30px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">会员只能在此时间段内领取，未到期或过期，即使满足领取条件也无法领取</td>
			</tr>
			
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">优惠劵使用时间:</td>
				<td>
					<input  prompt="劵开始使用时间" type="text" id="useStartTime" name="useStartTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;margin-top: 30px;">
					<span>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;</span>
					<input prompt="劵失效时间" type="text" id="disabledTime" name="disabledTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;margin-top: 30px;">
				</td>
			</tr>
			<tr class="breadth" style="height:30px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">会员只能在此时间段内使用该劵，未到期或过期，均无法使用</td>
			</tr>
			
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:10%">商品分类使用限制:</td>
				<td>
					<label><input class="sort" id="sortSelect2" type="radio" name="goodsSortCondition" value="2" checked="checked"><span>&nbsp;不添加商品分类限制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input class="sort" id="sortSelect1"  type="radio"  name="goodsSortCondition" value="1"><span>&nbsp;允许以下商品分类使用</span></label>
					<div id="sort-box" style="margin-top:5px;">
						<table style="width: 30%" id="sort-list">
							<thead>
								<tr style="background-color: #f4f4f4;height:30px;">
									<th class="th">商品类别</th>
									<th style="border-right:1px solid #c5c5c5;text-align:center;" class="th">操作</th>
								</tr>
							</thead>
							<tbody id="goods-sort">
								
							</tbody>
						</table>
					</div>
				</td>	
			</tr>
			
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:8%">商品使用限制:</td>
				<td>
					<label><input class="condition" id="goodsSelect2"  type="radio" name="goodsUseCondition" value="2" checked="checked"><span>&nbsp;不添加商品限制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input class="condition" id="goodsSelect1"  type="radio"  name="goodsUseCondition" value="1"><span>&nbsp;允许以下商品使用</span></label>
					<div id="goods-box" style="margin-top:5px;">
						<table style="width: 60%">
							<thead>
								<tr style="background-color: #f4f4f4;height:30px;">
									<th style="width:50%" class="th">商品</th>
									<th style="width:15%" class="th">商品价格</th>
									<th style="width:10%;border-right:1px solid #c5c5c5;text-align:center;" class="th">操作</th>
								</tr>
							</thead>
							<tbody id="goods-list">
							
							</tbody>
						</table>
					</div>
				</td>
			</tr>
			
			<tr class="breadth" style="height:40px;width:100%;">
				<td class="breadthT" style="width:8%">状态:</td>
				<td>
					<label><input  type="radio" name="isRelease" value="1"><span>&nbsp;立即上架&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input  type="radio"  name="isRelease" value="2" checked="checked"><span>&nbsp;暂不上架</span></label>
				</td>
			</tr>
			<tr class="breadth" style="height:20px;width:100%;margin-top:0px;">
				<td class="breadthT" style="width:10%;"></td>
				<td style="color:#8e8e8e;width:90%">选择立即上架，即用户可领取和使用，暂不上架，则无法领取使用，并且可进行编辑修改等操作</td>
			</tr>
			
			<tr class="breadth" style="height:50px;width:100%;">
				<td class="breadthT" style="width:8%">使用说明:</td>
				<td>
					<textarea id="couponExplain" name="couponExplain" rows="6" cols="100" ></textarea>
				</td>
			</tr>
		</table>
		<!-- <input type="hidden" name="paramData" />
		<input type="hidden" name="itemType" /> -->
		<div style="padding:5px;width: 866px; text-align: right;">
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="submitForm()">确认添加</a>
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="clearForm()">重置</a>
		</div>
	</form>
</div>

<script type="text/javascript">

	/*获取选中的商品分类的catUUID*/
	function getCatUUID(){
		var catUUID = '' ;
		var radioBoxCatUUID = document.getElementsByName("catUUID");
		for(var i = 0; i<radioBoxCatUUID.length;i++){
			if(radioBoxCatUUID[i].checked){
				catUUID= radioBoxCatUUID[i].value;
				break;
			}
		}
		return catUUID;
	}
	
	/* 选中对应商品时将商品的uuid放入数组中*/
	function getGoodsUUID(){
		var goodsUUID = '' ;
		var radioBoxGoodsUUID = document.getElementsByName("goodsUUID");
		for(var i = 0; i<radioBoxGoodsUUID.length;i++){
			if(radioBoxGoodsUUID[i].checked){
				goodsUUID = radioBoxGoodsUUID[i].value;
				break;
			}
		}
		return goodsUUID;
	}
	

	/* 提交表单,确认添加优惠劵 */
	var isSubmit = false;
	function submitForm(){
		var entityUUID = $("#entity-list").combobox("getValue");
		//console.log('选择的值：'+entityUUID);
		if(entityUUID == undefined || entityUUID == ""){
			$.messager.alert('提示','您还没有店铺，不可发布优惠劵!','warning');
			return;
		}
		
		//提交前判断填写是否符合要求
		//1.优惠劵名必须填写
		if($("#couponName").val() == undefined || $("#couponName").val().replace(/\s/g, "") == ""){
			$.messager.alert('提示','请填写优惠劵名称!','warning');
			return;
		}
		//2.优惠劵类型必须填写
		if($("input[name='couponType']:checked").val() == undefined){
			$.messager.alert('提示','请选择优惠劵类型!','warning');
			return;
		}
		
		//3.优惠劵发行张数
		if($("#releaseNumbers").val() == undefined || $("#releaseNumbers").val().replace(/\s/g, "") == ""){
			$.messager.alert('提示','请填写要发行优惠劵的数量!','warning');
			return;
		}
		//4.优惠劵可重复领取张数
		if($("#repeatNumber").val() == undefined || $("#repeatNumber").val().replace(/\s/g, "") == ""){
			$.messager.alert('提示','请填写优惠劵可重复领取的数量!','warning');
			return;
		} 
		
		if($("input[name='couponType']:checked").val() == '1'){//选中代金劵
			if($('#integration1').val() == undefined || $('#integration1').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写代金劵领取所需积分!','warning');
				return;
			}
			if($('#money').val() == undefined || $('#money').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写代金劵金额!','warning');
				return;
			}
			if($('#effectMoney1').val() == undefined || $('#effectMoney1').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写代金劵满减金额!','warning');
				return;
			}
		}
		if($("input[name='couponType']:checked").val() == '2'){//选中优惠劵
			if($('#integration2').val() == undefined || $('#integration2').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写优惠劵领取所需积分!','warning');
				return;
			}
			if($('#discount').val() == undefined || $('#discount').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写代优惠劵折扣!','warning');
				return;
			}
			if($('#effectMoney2').val() == undefined || $('#effectMoney2').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写优惠劵满减金额!','warning');
				return;
			}
			if($('#reduceMaxPrice').val() == undefined || $('#reduceMaxPrice').val().replace(/\s/g, "") == ""){
				$.messager.alert('提示','请填写最高优惠金额!','warning');
				return;
			}
		}
		//优惠劵领取时间判定
		if($("#releaseStartTime").val() == undefined || $("#releaseStartTime").val().replace(/\s/g,"") == ""){
			$.messager.alert('提示','请填写优惠劵开始领取时间!','warning');
			return;
		}
		if($("#releaseEndTime").val() == undefined || $("#releaseEndTime").val().replace(/\s/g,"") == ""){
			$.messager.alert('提示','请填写优惠劵截止领取时间!','warning');
			return;
		}
		//优惠劵使用时间判定
		if($("#useStartTime").val() == undefined || $("#useStartTime").val().replace(/\s/g,"") == ""){
			$.messager.alert('提示','请填写优惠劵开始使用时间!','warning');
			return;
		}
		if($("#disabledTime").val() == undefined || $("#disabledTime").val().replace(/\s/g,"") == ""){
			$.messager.alert('提示','请填写优惠劵失效时间!','warning');
			return;
		}
		
		//选中指定分类时，必须选择具体分类
		if($("input[name='goodsSortCondition']:checked").val() == '1'){
			var catUUID = getCatUUID();
			if(catUUID == undefined || catUUID==""){//没有选择
				$.messager.alert('提示','请选择具体商品分类限制!','warning');
				return;
			}
		}
		
		// 勾选优惠劵指定商品使用时，必须选择限定的商品 
		if($("input[name='goodsUseCondition']:checked").val() == '1'){
			var goodsUUID = getGoodsUUID();
			if(goodsUUID == undefined || goodsUUID==""){//没有选择
				$.messager.alert('提示','请选择具体商品使用限制!','warning');
				return;
			}
		}
		
		if($("input[name='isRelease']:checked").val() == undefined){
			$.messager.alert('提示','请选择优是否立即上架!','warning');
			return;
		}
		
		/* if($("#couponExplain").val() == undefined || $("#couponExplain").val().replace(/\s/g,"") == ""){
			$.messager.alert('提示','使用说明不能为空!','warning');
			return;
		} */
		
		if(!isSubmit){
			isSubmit = true;
		}else{
			return;
		}
		
		var couponAdd = $("#couponAdd").serialize();
		var goodsUUID = getGoodsUUID()
		var catUUID = getCatUUID()
		var url = "<%=basePath%>";
		 //console.log("开始生成优惠劵")
		//TODO 店铺UUID的获取？
		$.ajax({
			url:baseURL+"system/coupon/createCoupon",
			data:couponAdd+"&fromType=2&goodsUUID="+goodsUUID+"&catUUID="+catUUID+"&merchantUUID="+entityUUID+"&basePath="+url,
			type:"POST",
			dataType:"json",
			success:function(data){
				//alert(JSON.stringify(data))
				if(data.status == "1"){
					isSubmit = false;
					$.messager.alert('提示','优惠劵新增成功','info');
					loadCenterLayout('coupon/createCoupon.jsp');
				}else{
					isSubmit = false;
					$.messager.alert('提示',data.msg,'warning');
				}
			}
		});
		
	}

	/* 重置 */
	function clearForm(){
		loadCenterLayout('coupon/createCoupon.jsp');
	}

	/* 优惠劵选择后领取限制的切换 1-代金劵 2-优惠劵 */
	$(".select").click(function() {
		if (this.value == "2") {//隐藏代金劵,显示优惠劵
			$("#coupon1").css("visibility","collapse");
			$("#coupon2").css("visibility","visible");
		}
		if (this.value == "1") {//隐藏优惠劵，显示代金劵
			$("#coupon2").css("visibility","collapse");
			$("#coupon1").css("visibility","visible");
		}
	});
	
	//$("input[name='goodsSortCondition']:checked").val() == '1'
	//$("input[name='goodsUseCondition']:checked").val() == '1'
	/* 商品分类使用限制  1-限制 2-不限制 */
	$(".sort").click(function() {
		//1.判断用户是否选择店铺
		var entityUUID = $("#entity-list").combobox("getValue");
		if(entityUUID == undefined || entityUUID == ""){
			$("#sortSelect2").prop("checked",true);
			$.messager.alert('提示','请先选择想要发行的店铺!','warning');
			return;
		}
		if (this.value == "2") {//不限制
			//隐藏商品分类框
			$("#sort-box").hide();
			//2.判断用户是否先点击了限制商品
			//删除之前查询的结果，和清空用户之前点击触发添加的数组的值
			$("#goods-sort").empty();
			$("#goods-list").empty();
			if($("input[name='goodsUseCondition']:checked").val() == '1'){//是
				//重新获取商品
				getGoodsList();
			}
		}
		if (this.value == "1") {//限制
			$("#sort-box").show();//显示分类
			$("#goods-box").hide();//关闭之前打开的商品
			$("#goodsSelect2").prop("checked",true);//重新设置为不限制商品
			//获取商品分类列表
			getGoodsSortList()
		}
	});
	/* 商品使用限制  1-限制 2-不限制*/
	$(".condition").click(function() {
		//1.判断用户是否选择店铺
		var entityUUID = $("#entity-list").combobox("getValue");
		if(entityUUID == undefined || entityUUID == ""){
			$("#goodsSelect2").prop("checked",true);
			$.messager.alert('提示','请先选择想要发行的店铺!','warning');
			return;
		}
		if (this.value == "2") {//不限制
			$("#goods-box").hide();
			//删除之前查询的结果，和清空用户之前点击触发添加的数组的值
			$("#goods-list").empty();
		}
		if (this.value == "1") {//限制
			if($("input[name='goodsSortCondition']:checked").val() == '1'){//选中了商品分类
				var catUUID = getCatUUID();
				if(catUUID == undefined || catUUID == ""){//未勾选具体分类信息
					$("#goodsSelect2").prop("checked",true);
					$.messager.alert('提示','请先选择商品分类!','warning');
					return;
				}
			}
			//$("#goods-list").empty();
			$("#goods-box").show();
			//获取商品列表
			getGoodsList();
		}
	});
	
	/* 当用户点击分类信息时，若也勾选了商品显示，则根据具体选择查询商品信息catUUID */
	function getList(obj){
		console.log("点击了商品分类...");
		if($("input[name='goodsUseCondition']:checked").val() == '1'){//是
			$("#goods-list").empty();
			//重新获取商品
			getGoodsList();
		}
	}
	
	/* 获取商品列表 当前对着只能指定一个分类，或者指定一个商品*/
	function getGoodsList(){
		$("#goods-list").empty();//每次查询之前，先清空之前的数据
		var catUUID = getCatUUID();
		var entityUUID = $("#entity-list").val();
		if(entityUUID == undefined || entityUUID == ""){
			$.messager.alert('提示','您还没有店铺，不可发布优惠劵!','warning');
			return;
		}
		$.ajax({
			url:baseURL+'system/coupon/getGoodsList',
			data:{"catUUID":catUUID,"businessUUID":entityUUID},
			type:'POST',
			dataType:'json',
			success:function(data){
				//console.log(JSON.stringify(data));
				//加载显示商品列表
				if(data.status == "1"){
					for(var i = 0;i<data.goodsList.length;i++){
						var html = '<tr style="border-bottom: 1px solid #f4f4f4;height:30px">'+
							'<td style="width: 70%" class="td"><img style="width:20px;height:20px" src="'+baseURL+data.goodsList[i].itemImg+'"> <span>'+data.goodsList[i].title+'</span></td>'+
							'<td style="width: 10%" class="td"><span>¥'+data.goodsList[i].price/100+'</span></td>'+
							'<td style="width: 10%;border-right:1px solid #c5c5c5;text-align:center;" class="td"><input type="radio" name="goodsUUID" value="'+data.goodsList[i].itemUUID+'"></td>'+
							'</tr>';
						$("#goods-list").append(html);
					}
				}
				if(data.status == "-1"){
					$.messager.alert('提示',data.msg,'info');
				}
			}
		});
	}
	
	/* 获取零售店铺商品分类列表 */
	function getGoodsSortList(){
		$("#goods-sort").empty();//每次查询之前，先清空之前的数据
		var entityUUID = $("#entity-list").val();
		if(entityUUID == undefined || entityUUID == ""){
			$.messager.alert('提示','请选择您要发布的店铺!','warning');
			return;
		}
		$.ajax({
			url:baseURL+'system/coupon/getGoodsSortList',
			data:{"businessUUID":entityUUID},
			type:'POST',
			dataType:'json',
			success:function(data){
				if(data.status == "1"){
					//$.messager.alert('提示',JSON.stringify(data),'info');
					//加载显示商品列表
					if(data.status == "1"){
						for(var i = 0;i<data.sortList.length;i++){
							var html = '<tr style="border-bottom: 1px solid #f4f4f4;height:30px">'+
								'<td style="width: 80%" class="td"><span>'+data.sortList[i].catName+'</td>'+
								'<td style="width: 20%;border-right:1px solid #c5c5c5;text-align:center;" class="td"><input type="radio" onclick="getList(this)" name="catUUID" value="'+data.sortList[i].catUUID+'"></td>'+
								'</tr>';
							$("#goods-sort").append(html);
						}
					} 
				}
				
			}
		});
	}
	
	/* 页面加载 */
	$(document).ready(function() {
		$("#goods-box").hide();
		$("#sort-box").hide();
		getEntityInfo();
	})
	
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
					$('#entity-list').combobox({
						data : data.businessApplies,
						valueField : 'businessUUID',
						textField : 'businessName',
						prompt : '请选择发行店铺',
						hasDownArrow : true,
						panelHeight : 300,
						filter : function(q, row) {
							var opts = $(this).combobox('options');
							return row[opts.textField].indexOf(q) == 0;
						}
					});
				}
			}
		});
	}
	
</script>
