<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style>
	.buttonSubmit {
		width: 100px;
		height: 33px;
		background-color: rgba(0, 0, 255, 0.7);
		color: #fff !important;
	}
	.goodsParams input:nth-child(1){
		margin-right:5%;
	}
	.textbox{
		margin-right:5%;
	}
</style>
<!-- 平台活动商品 -->
<div style="padding:0px 0px 0px 10px">
	<form id="goodAddForm" class="goodForm" method="post">
		<table style="border-collapse:separate; border-spacing:30px 30px;">
			<tr>
				<td style="width:10%;">活动名称:</td>
				<td style="width:90%;">
					<input id="activityTitle" prompt="请输入活动名称(50字以内)" class="easyui-textbox" type="text" name="activityTitle" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			<tr>
				<td>活动封面图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>
					<input id="activityImg" type="hidden" name="activityImg" />
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<img id="activityImg-box" style="width:200px;height:100px;"/>
				</td>
			</tr>
			<tr>
				<td style="width:10%;">商品名称:</td>
				<td style="width:90%;">
					<input id="title" prompt="请输入活动商品名称(50字以内)" class="easyui-textbox" type="text" name="title" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">商品价格:</td>
				<td style="width:90%;">
					<input id="price" prompt="请输入活动商品价格" class="easyui-textbox" type="number" name="price" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">商品库存:</td>
				<td style="width:90%;">
					<input id="stock" prompt="请输入商品库存" class="easyui-textbox" type="number" name="stock" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
					<div style="color:#8e8e8e;">商品库存只能输入大于0的整数！</div>
				</td>
			</tr>
			
			<tr >
				<td style="width:10%;">领取时间限制:</td>
				<td style="width:90%;">
					<input id="getTime" prompt="请输入领取时间期限(单位小时)" class="easyui-textbox" type="number" name="getTime" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
					<div style="color:#8e8e8e;">用户在下单后，超过该指定时间范围后，即视为放弃领取</div>
				</td>
			</tr>
			
			
			<tr>
				<td style="width:10%">活动时间期限:</td>
				<td style="width:90%;">
					<input  prompt="活动开始时间" type="text" id="activeStartTime" name="activeStartTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;">
					<span>&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;</span>
					<input prompt="活动结束时间" type="text" id="activeEndTime" name="activeEndTime" class="easyui-textbox easyui-datetimebox" data-options="required:true" style="width:15%;">
				</td>
			</tr>	
			
			<tr >
				<td style="width:10%;">活动参与次数:</td>
				<td style="width:90%;">
					<input id="joinLimitTime" prompt="请输入用户可参与该活动的次数" class="easyui-textbox" type="number" name="joinLimitTime" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
					<div style="color:#8e8e8e;">用户可参与该活动的次数，超过即不可再参与</div>
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">是否上架</td>
				<td>
					<label><input id="goodStatus1" type="radio" name="goodStatus" value="1"><span>&nbsp;立即上架&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input id="goodStatus2" type="radio"  name="goodStatus" value="2" checked="checked"><span>&nbsp;暂不上架</span></label>
				</td>
			</tr>
			
			<tr>
				<td>商品封面图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>
					<input id="goodImg" type="hidden" name="goodImg" />
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<img id="goodImg-box" style="width:200px;height:200px;"/>
				</td>
			</tr>
		</table>
		<input type="hidden" name="paramData" />
		<div style="margin-left:120px;width: 500px; text-align: left;">
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="addIntegralGood(1)">确认修改</a>
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="addIntegralGood(2)">取消</a>
		</div>
	</form>

</div>
<script type="text/javascript">
 	//初始化单图片上传组件
	shop.initOnePicUpload();
	//初始化多图片上传组件
	//shop.initPicUpload('.subImgList');
	
	/* 页面加载,按默认条件获取优惠劵 */
	$(document).ready(function() {
		var  activityUUID ="<%=request.getParameter("activityUUID")%>"; 
		getActiveInfo(activityUUID);
	})
	
	/* 获取编辑活动的信息 */
	var id = "";
	var basep = "<%=basePath%>"; 
	function getActiveInfo(e){
		$.ajax({
			url:baseURL+"system/integral/getActivityInfo",
			data:{"activityUUID":e},
			type:"POST",
			dataType:"json",
			success:function(data){
				console.log(JSON.stringify(data))
				if(data.status == "1"){
					
					id = data.activeGood.id;
					$("#activityTitle").textbox("setValue",data.activeGood.activityTitle);
					$("#activityImg-box").attr("src",basep+data.activeGood.activityImg);
					$("#title").textbox("setValue",data.activeGood.title);
					$("#price").textbox("setValue",data.activeGood.price/100);
					$("#stock").textbox("setValue",data.activeGood.stock);
					$("#getTime").textbox("setValue",data.activeGood.getTime);
					$("#activeStartTime").textbox("setValue",data.activeGood.activeStartTime);
					$("#activeEndTime").textbox("setValue",data.activeGood.activeEndTime);
					$("#joinLimitTime").textbox("setValue",data.activeGood.joinLimitTime);
					$("#goodImg-box").attr("src",basep+data.activeGood.goodImg);
					//goodStatus 1上架，2下架，3未上架
					if(data.activeGood.goodStatus == "1"){//已上架
						$("#goodStatus1").prop("checked",true);
					}else{
						$("#goodStatus2").prop("checked",true);
					}
				}
			}
		});
	}

	function addIntegralGood(e){
		if(e == 1){//添加商品
			var activityTitle = $("#activityTitle").val();
			if(activityTitle == undefined || activityTitle.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写活动名称!','warning');
				return;
			}
			
			/* var activityImg = $("#activityImg").val();
			if(activityImg == undefined || activityImg.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请上传活动封面图!','warning');
				return;
			} */
			
			var title = $("#title").val();
			if(title == undefined || title.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品名称!','warning');
				return;
			}
			
			var price = $("#price").val();
			if(price == undefined || price.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品价格!','warning');
				return;
			}
			
			var stock = $("#stock").val();
			if(stock == undefined || stock.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品库存!','warning');
				return;
			}
			
			var getTime = $("#getTime").val();
			if(getTime == undefined || getTime.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品兑换后领取时间限制!','warning');
				return;
			}
			
			//活动时间判定
			if($("#activeStartTime").val() == undefined || $("#activeStartTime").val().replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写活动开始时间!','warning');
				return;
			}
			if($("#activeEndTime").val() == undefined || $("#activeEndTime").val().replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写活动结束时间!','warning');
				return;
			}
			
			var joinLimitTime = $("#joinLimitTime").val();
			if(joinLimitTime == undefined || joinLimitTime.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品价格!','warning');
				return;
			}
			
			/* var goodImg = $("#goodImg").val();
			if(goodImg == undefined || goodImg.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请上传商品封面图!','warning');
				return;
			} */
			
			var goodAddForm = $("#goodAddForm").serialize();
			
			$.ajax({
				url:baseURL+"system/integral/editorActivityGood",
				data:goodAddForm+"&id="+id,
				type:"POST",
				dataType:"json",
				success:function(data){
					//alert(JSON.stringify(data))
					if(data.status == "1"){
						$.messager.alert('提示','保存成功','info');
						loadCenterLayout('integralshop/activeGoodManage.jsp');
					}else{
						$.messager.alert('提示',data.msg,'warning');
					}
				}
			});
		}else if(e == 2){
			loadCenterLayout('integralshop/activeGoodManage.jsp');
		}
	}

	
</script>