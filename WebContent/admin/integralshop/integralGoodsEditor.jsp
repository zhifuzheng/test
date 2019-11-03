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
<!-- 积分商城商品编辑修改 -->
<div style="padding:0px 0px 0px 10px">
	<form id="goodAddForm" class="goodForm" method="post">
		<table style="border-collapse:separate; border-spacing:30px 30px;">
			<tr>
				<td style="width:10%;">商品名称:</td>
				<td style="width:90%;">
					<input id="title" prompt="请输入商品名称" class="easyui-textbox" type="text" name="title" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			<tr>
				<td style="width:10%;">商品库存:</td>
				<td style="width:90%;">
					<input id="stock" prompt="请输入商品库存" class="easyui-textbox" type="number" name="stock" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			<tr>
				<td style="width:10%;">所需积分:</td>
				<td style="width:90%;">
					<input id="integration" prompt="请输入商品兑换所需积分" class="easyui-textbox" type="number" name="integration" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">领取时间限制:</td>
				<td style="width:90%;">
					<input id="getTime" prompt="请输入领取时间期限(单位小时)" class="easyui-textbox" type="number" name="getTime" data-options="required:true,validType:'length[1,50]'" style="width:90%;"></input>
				</td>
			</tr>	
			
			<tr>
				<td style="width:10%;">是否上架</td>
				<td>
					<label><input id="goodStatus1" type="radio" name="goodStatus" value="1"><span>&nbsp;立即上架&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
					<label><input id="goodStatus3" type="radio"  name="goodStatus" value="3" checked="checked"><span>&nbsp;暂不上架</span></label>
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">商品简介:</td>
				<td style="width:90%;">
					<textarea id="introduce" rows="3" cols="100" name="introduce" data-options="required:true,validType:'length[1,300]'" ></textarea>
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
				<td>商品展示图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton subImgList">上传图片</a>
					<input id="subImgList" type="hidden" name="subImgList" />
				</td>
			</tr>
			<tr>
				<td>商品详情图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton detailImgList">上传图片</a>
					<input id="detailImgList" type="hidden" name="detailImgList" />
				</td>
			</tr>
			
			<tr>
				<td style="width:10%;">商品参数:</td>
				<td style="width:90%;" id="goodsParams">
					<!-- <div style="margin-top:10px;width:90%;">
						<input prompt="请输入参数名称" class="easyui-textbox" style="width:30%;">
						<input prompt="请输入参数内容" class="easyui-textbox" style="width:40%;">
						<a onclick="addGoodParams(this)" href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" style="color:#ffffff;width:12%;line-height:30px;background-color: rgba(0, 180, 0, 0.7);border-radius: 5px;margin-left:5%">添加</a>
					</div> -->
					
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
	shop.initPicUpload('.subImgList');
	shop.initPicUpload('.detailImgList');
	
	/* 页面加载,按默认条件获取优惠劵 */
	$(document).ready(function() {
		var goodUUID ="<%=request.getParameter("goodUUID")%>"; 
		//console.log(goodUUID);
		getGoodInfo(goodUUID);
	})
	
	/* 获取编辑活动的信息 */
	var id = "";
	var basep = "<%=basePath%>"; 
	function getGoodInfo(e){
		$.ajax({
			url:baseURL+"system/integral/getIntegralGoodInfo",
			data:{"goodUUID":e},
			type:"POST",
			dataType:"json",
			success:function(data){
				if(data.status == "1"){
					console.log(JSON.stringify(data.integralGood))
					id = data.integralGood.id;
					//goodImg
					//subImgList
					//detailImgList
					$("#title").textbox("setValue",data.integralGood.title);
					$("#stock").textbox("setValue",data.integralGood.stock);
					$("#integration").textbox("setValue",data.integralGood.integration);
					$("#getTime").textbox("setValue",data.integralGood.getTime);
					
					//goodStatus 1上架，2下架，3未上架
					if(data.integralGood.goodStatus == "1"){//已上架
						$("#goodStatus1").prop("checked",true);
					}else {
						$("#goodStatus3").prop("checked",true);
					}
					$("#introduce").val(data.integralGood.introduce);
					
					//商品参数 品种=月季,颜色=粉红
					var params = data.integralGood.goodParams;
					var paramsArr = params.split(",");
					for(var i = 0;i<paramsArr.length;i++){
						var  arr = paramsArr[i].split("=");
						//2.添加新的参数
						var html = '<div style="margin-top:10px;width:90%;">'
								+ '<input prompt="请输入参数名称" value="'+arr[0]+'" class="easyui-textbox" style="width:30%;">&nbsp;'
								+ '<input prompt="请输入参数内容" value="'+arr[1]+'" class="easyui-textbox" style="width:40%;margin-left:20px;">'
								+ '<a onclick="addGoodParams(this)" href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" style="color:#ffffff;width:12%;line-height:30px;background-color: rgba(255, 0, 0, 0.7);border-radius: 5px;margin-left:5.5%">删除</a>'
								+ '</div>';
						var targetObj = $(html).appendTo("#goodsParams");
						$.parser.parse(targetObj);
					}
					
					var html = '<div style="margin-top:10px;width:90%;">'
					+'<input prompt="请输入参数名称" class="easyui-textbox" style="width:30%;">'
					+'<input prompt="请输入参数内容" class="easyui-textbox" style="width:40%;">'
					+'<a onclick="addGoodParams(this)" href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" style="color:#ffffff;width:12%;line-height:30px;background-color: rgba(0, 180, 0, 0.7);border-radius: 5px;margin-left:6%">添加</a>'
					+'</div>';
					var targetObj = $(html).appendTo("#goodsParams");
					$.parser.parse(targetObj);
				}
			}
		});
	}
	
	function addIntegralGood(e){
		if(e == 1){//保存编辑修改商品
			var title = $("#title").val();
			if(title == undefined || title.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品标题!','warning');
				return;
			}
			var stock = $("#stock").val();
			if(stock == undefined || stock.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品库存!','warning');
				return;
			}
			var integration = $("#integration").val();
			if(integration == undefined || integration.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品价值积分!','warning');
				return;
			}
			var getTime = $("#getTime").val();
			if(getTime == undefined || getTime.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品兑换后领取时间限制!','warning');
				return;
			}
			var introduce = $("#introduce").val();
			if(introduce == undefined || introduce.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请填写商品简介!','warning');
				return;
			}
			/* var goodImg = $("#goodImg").val();
			if(goodImg == undefined || goodImg.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请上传商品封面图!','warning');
				return;
			} */
			/* var subImgList = $("#subImgList").val();
			if(subImgList == undefined || subImgList.replace(/\s/g,"") == ""){
				$.messager.alert('提示','请上传商品展示图!','warning');
				return;
			} */
			/* var detailImgList = $("#detailImgList").val();  */
			
			//获取商品参数
			var divs = $("#goodsParams").children("div");
			console.log("divs:"+divs.length);
			var goodParams = [];
			for(var i = 0; i < divs.length-1; i++){//最后一个不计
				var inp = divs[i].children;
				console.log("input:"+inp.length);
				var paramName = inp[0].value;
				var paramValue = inp[2].value
				if(paramName == undefined || paramName.replace(/\s/g,"") == ""){
					$.messager.alert('提示','添加的参数名不能为空!','warning');
					return;
				}
				if(paramValue == undefined || paramValue.replace(/\s/g,"") == ""){
					$.messager.alert('提示','添加的参数内容不能为空!','warning');
					return;
				}
				var param = paramName+"="+paramValue;
				goodParams.push(param);
			}
			//console.log(JSON.stringify(goodParams));
			var goodAddForm = $("#goodAddForm").serialize();
			$.ajax({
				url:baseURL+"system/integral/editorIntegralGood",
				data:goodAddForm+"&goodParams="+goodParams+"&id="+id,
				type:"POST",
				dataType:"json",
				success:function(data){
					if(data.status == "1"){
						$.messager.alert('提示','编辑保存成功','info');
						loadCenterLayout('integralshop/integralGoodsList.jsp');
					}else{
						$.messager.alert('提示',data.msg,'warning');
					}
				}
			});
		}else if(e == 2){
			loadCenterLayout('integralshop/integralGoodsList.jsp');
		}
	}

	/* 添加商品参数 */
	function addGoodParams(obj){
		 if($(obj).text() == "添加"){
			var inp = $(obj).siblings("input");
			if(inp[0].value == undefined || inp[0].value.replace(/\s/g, "") == ""){
				$.messager.alert('提示','请输入参数名称!','warning');
				 return;
			}
			if(inp[1].value == undefined || inp[1].value.replace(/\s/g, "") == ""){
				$.messager.alert('提示','请输入参数内容!','warning');
				 return;
			} 
			//1. 样式和文本修改
			$(obj).text("删除");
			$(obj).css('background-color', 'rgba(255, 0, 0, 0.7)');
			//2.添加新的参数
			var html = '<div style="margin-top:10px;width:90%;">'
					+ '<input prompt="请输入参数名称" class="easyui-textbox" style="width:30%;">&nbsp;'
					+ '<input prompt="请输入参数内容" class="easyui-textbox" style="width:40%;margin-left:20px;">'
					+ '<a onclick="addGoodParams(this)" href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" style="color:#ffffff;width:12%;line-height:30px;background-color: rgba(0, 180, 0, 0.7);border-radius: 5px;margin-left:5.5%">添加</a>'
					+ '</div>';
			var targetObj = $(html).appendTo("#goodsParams");
			$.parser.parse(targetObj);
		} else {
			layer.confirm('您确认删除该参数么?', function(index){
				//删除该元素
				var div = $(obj).parent();
				div.remove();
				layer.close(index);
			});
		}
	}

	/* 商品详情介绍富文本编辑 */
	/* var editor;
	$(function() {
		editor = KindEditor.create('textarea[name="detailIntroduce"]', {
			resizeType : 1,
			width : "100%",
			height : "200px",
			afterChange : function() {
				this.sync();
			},
			afterBlur : function() {
				this.sync();
			}
		});
	}); */
</script>