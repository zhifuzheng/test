<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<style>
	/*默认*/
	.tree-icon {
		background:url('<%=basePath %>static/img/tree/arrow.png') no-repeat center center !important;
	}
	/*折叠时图片*/
	.tree-folder {
		background:url('<%=basePath %>static/img/tree/search.png') no-repeat center center !important;
	} 
	/*展开时图片*/
	.tree-folder-open {
		background:url('<%=basePath %>static/img/tree/look.png') no-repeat center center !important;
	}
	.itemForm .pics ul {
		width:465px;
		list-style:none;
	}
	.itemForm .pics ul li {
		width:150px;
		float:left;
		margin-right:5px;
	}
	.itemForm .group {
		font-weight: bold;
		text-align: center;
		background-color: #EAEAEA;
	}
	.hide {
		display:none;
	}
	.params .textbox .textbox-prompt {
		color:black;
	}
	.buttonSubmit {
		width: 100px;
		height: 33px;
		background-color: #02a2ff;
		color: #fff !important;
	}
	.buttonSubmit:hover{
		color: #000 !important;
	}
</style>
<div style="padding:0px 0px 0px 10px">
	<form id="itemAddForm" class="itemForm" method="post">
		<table style="border-collapse:separate; border-spacing:30px 30px;">
			<tr>
				<td>商铺名称:</td>
				<td>
					<input id="entityName" class="easyui-combobox" data-options="required:true" style="width:280px;"/>
					<input type="hidden" name="entityUUID"></input>
					<input type="hidden" name="entityName"></input>
					<input type="hidden" name="entityLogo"></input>
					<input type="hidden" name="itemType"></input>
					<input type="hidden" name="distributeStatus" value="0"></input>
					<span id="entityType" style="margin-left:30px;cursor:default;color:blue;"></span>
				</td>
			</tr>
			<tr>
				<td>商品名称:</td>
				<td>
					<input class="easyui-textbox" type="text" name="title" data-options="required:true,validType:'length[1,20]'" style="width:280px;"></input>
				</td>
			</tr>
			<tr>
				<td>商品简介:</td>
				<td>
					<input class="easyui-textbox" name="intro" data-options="required:true,multiline:true,validType:'length[1,100]'" style="width:280px;height:60px;"></input>
				</td>
			</tr>
			<tr>
				<td>商品原价:</td>
				<td>
					<input class="easyui-numberbox" type="text" name="tagPriceView" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>
					<input type="hidden" name="tagPrice" />
				</td>
			</tr>
			<tr>
				<td>商品现价:</td>
				<td>
					<input class="easyui-numberbox" type="text" name="priceView" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>
					<input type="hidden" name="price" />
				</td>
			</tr>
			<tr class="grade hide"></tr>
			<tr>
				<td>展示销量:</td>
				<td>
					<input class="easyui-numberbox" type="text" name="tagMonthlySales" data-options="min:1,max:99999999,precision:0,required:true" style="width:280px;"/>
				</td>
			</tr>
			<tr>
				<td>库存数量:</td>
				<td>
					<input class="easyui-numberbox" type="text" name="stock" data-options="min:1,max:99999999,precision:0,required:true" style="width:280px;"/>
				</td>
			</tr>
			<tr>
				<td>是否上架:</td>
				<td>
					<select class="easyui-combobox" name="itemStatus" data-options="required:true,editable:false" style="width:280px;">
						<option value="1">立即上架</option>
						<option value="2">稍后上架</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>商品分类:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">点击选择分类</a>
					<input type="hidden" name="catUUID"></input>
					<input type="hidden" name="catCode"></input>
					<input type="hidden" name="catName"></input>
				</td>
			</tr>
			<tr>
				<td>商品规格:</td>
				<td>
					<input id="modelName" class="easyui-combobox" data-options="editable:false" style="width:280px;"/>(选择分类加载商品规格)
					<input type="hidden" name="modelUUID" />
				</td>
			</tr>
			<tr class="params hide"></tr>
			<tr>
				<td>商品封面图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>(点击图片放大)
					<input type="hidden" name="itemImg" />
				</td>
			</tr>
			<tr>
				<td>商品展示图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton subImgList">上传图片</a>
					<input type="hidden" name="subImgList" />
				</td>
			</tr>
			<tr>
				<td>商品详情图:</td>
				<td>
					<a href="javascript:void(0)" class="easyui-linkbutton detailImgList">上传图片</a>
					<input type="hidden" name="detailImgList" />
				</td>
			</tr>
		</table>
		<input type="hidden" name="paramData" />
		<input type="hidden" name="gradeData" />
		<div style="margin-left:120px;width: 500px; text-align: left;">
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="submitForm()">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton buttonSubmit" onclick="clearForm()">重置</a>
		</div>
	</form>

</div>
<script type="text/javascript">
	//商家类型，0零售商，1批发商
	var entityType;
	//商家行业分类编号
	var catUUID = "1";
	//商家行业分类
	var catName;
	//商品类型(0零售商品，1批发商品，2积分商品)
	var itemType;
	
	var itemAddEditor;
	
	$(function(){
		$.ajax({
			url : baseURL+'shop/item/findGradeList',
			type:"post",
			success:function(data){
				if(data.length > 0){
					$("#itemAddForm .grade").show();
	                var html = '<td colspan="2"><table style="width:380px;"><tr><td colspan="2" class="group">零售商等级对应价格: </td></tr>';
	                html += '<tr"><td style="width:92px;height:60px;">普通零售商: </td><td><input class="easyui-numberbox" type="text" id="0" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/></td></tr>'
	                for(var i = 0;i < data.length;i++){
						html += '<tr"><td style="width:92px;height:60px;">'+data[i].aliasName+': </td><td><input class="easyui-numberbox" type="text" id="'+data[i].number+'" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/></td></tr>';
	    			}
	    			html += '</table></td>';
	    			var gradeDom = $(html);
	    			$.parser.parse(gradeDom);
					$("#itemAddForm .grade").append(gradeDom);
				}else{
					$("#itemAddForm .grade").hide();
				}
			},
			dataType:'json'
		});
		
		$.ajax({
			url : baseURL+'shop/item/findAllEntity',
			data:{'entityTableName':'business_apply','approvalStatus':1,'shopState':1,'applyType':1},
			type:"post",
			success:function(data){
				if(data.length == 0){
					$.messager.alert('提示','还没有任何一个批发商加盟哦','warning');
				}else{
					var loadData = [];
					for(var i = 0;i < data.length;i++){
						var text = data[i]['businessName'];
						var value = JSON.stringify(data[i]);
						loadData.push({'text':text,'value':value});
					}
					$('#entityName').combobox({
						data : loadData,
			            valueField : 'value',
			            textField : 'text',
			            prompt:'请输入店铺名称',
			            hasDownArrow:true,
			            panelHeight:210,
			            filter: function(q, row){
			                var opts = $(this).combobox('options');
			                return row[opts.textField].indexOf(q) == 0;
			            },
			            onChange: function(){
			            	//重置店铺信息
		            		$("#itemAddForm [name=entityUUID]").val('');
			                $("#itemAddForm [name=entityName]").val('');
			                $("#itemAddForm [name=entityLogo]").val('');
			                $("#itemAddForm [name=itemType]").val('');
			                
		            		//重置分类和规格组件
			                var itemCat = $(".selectItemCat").next("span");
			                if(itemCat.length != 0){
			                	itemCat.text('');
			                }
			                $("#itemAddForm [name=catUUID]").val('');
			                $("#itemAddForm [name=catCode]").val('');
			                $("#itemAddForm [name=catName]").val('');
			                $("#itemAddForm [name=modelUUID]").val('');
			                $('#modelName').combobox('clear');//清空选中项
			                $('#modelName').combobox('loadData', {});//清空option选项   
			                $("#itemAddForm .params").hide();
			                $("#itemAddForm .params").empty();
			            	
			            	var text = $(this).combobox('getText');
			            	var value = $(this).combobox('getValue');
			            	if(text == value){
			            		return;
			            	}
			            	
			                var entityName = text;
			                var value = JSON.parse(value);
			                var entityUUID = value['businessUUID'];
			                var entityLogo = value['storefrontImg'];
			                catUUID = value['catUUID'];
			                catName = value['classification'];
			                entityType = value['applyType'];
			                itemType = value['applyType'];
			                $("#itemAddForm [name=entityName]").val(entityName);
			                $("#itemAddForm [name=entityUUID]").val(entityUUID);
			                $("#itemAddForm [name=entityLogo]").val(entityLogo);
			                $("#itemAddForm [name=itemType]").val(itemType);
			                $("#entityType").text(entityType == "0"?"零售商品":"批发商品");
			                
			              	//初始化类目选择组件
			        		shop.initItemCat({fun:function(node){
			   				 	//重置规格组件
								$("#itemAddForm [name=modelUUID]").val('');
				                $('#modelName').combobox('clear');//清空选中项
				                $('#modelName').combobox('loadData', {});//清空option选项   
				                $("#itemAddForm .params").hide();
				                $("#itemAddForm .params").empty();
			        			//根据catUUID获取模板列表
			        			$.ajax({
			        				url : baseURL+'shop/itemModel/findListByCatUUID',
			        				data:{'catUUID':node.id,'enable':'1'},
			        				type:"post",
			        				success:function(data){
			        					if(data.length == 0){
		        			                return;
		        			            }
			        					var loadData = [];
			        					for(var i = 0;i < data.length;i++){
			        						var text = data[i].modelName;
			        						var value = JSON.stringify(data[i]);
			        						loadData.push({'text':text,'value':value})
			        					}
			        					$("#modelName").combobox({
			        					    data : loadData,
			        					    valueField : 'value',
			        					    textField : 'text',
			        					    onChange: function(){
			        					    	//重置规格组件
			    			   				 	$("#itemAddForm [name=modelUUID]").val('');
			    			        			$("#itemAddForm .params").hide();
			    			   				 	$("#itemAddForm .params").empty();
			    			   				 	
			        			                var value = $(this).combobox('getValue');
			        			                if(value.length == 0){
			        			                	return;
			        			                }
			        			                var modelUUID = JSON.parse(value).modelUUID;
			        			                var modelData = JSON.parse(value).modelData;
			        			                var attrArr = modelData.split(",");
			        			                $("#itemAddForm .params").show();
			        			                $("#itemAddForm [name=modelUUID]").val(modelUUID);
			        			                var html = '<td colspan="2"><table style="width:380px;"><tr><td colspan="2" class="group">产品参数: </td></tr>';
			        			    			for(var i = 0;i < attrArr.length;i++){
			        								html += '<tr"><td style="width:92px;height:60px;">'+attrArr[i]+': </td><td><input class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input></td></tr>';
			        			    			}
			        			    			html += '</table></td>';
			        			    			var modelDom = $(html);
			        			    			$.parser.parse(modelDom);
			        							$("#itemAddForm .params").append(modelDom);
			        			            }
			        					});
			        				},
			        				dataType:'json'
			        			});
			        		},rootUUID:catUUID,rootName:catName});
			            }
			        });
				}
			},
			dataType:'json'
		});
		//初始化单图片上传组件
		shop.initOnePicUpload();
		//初始化多图片上传组件
		shop.initPicUpload('.subImgList');
		shop.initPicUpload('.detailImgList');
	});
	//提交表单
	function submitForm(){
		//表单校验
		var catUUID = $('#itemAddForm [name=catUUID]').val();
		var itemImg = $('#itemAddForm [name=itemImg]').val();
		var subImgList = $('#itemAddForm [name=subImgList]').val();
		var detailImgList = $('#itemAddForm [name=detailImgList]').val();
		if(catUUID == "" || itemImg == "" || subImgList =="" || detailImgList == "" || !$('#itemAddForm').form('validate')){
			$.messager.alert('提示','表单还没填完哦，请填完表单后再提交!','warning');
			return;
		}
		$("#itemAddForm [name=itemType]").val(itemType);
		//取商品价格，单位为分
		$("#itemAddForm [name=tagPrice]").val($("#itemAddForm [name=tagPriceView]").val() * 100);
		$("#itemAddForm [name=price]").val($("#itemAddForm [name=priceView]").val() * 100);
		
		//取商品的等级价格
		var gradeJson = {};
		var trs = $("#itemAddForm .grade table tr");
		for (var i = 1;i < trs.length;i++){
			var tr = trs.eq(i);
			var key = tr.find("td").eq(1).find("input[id]").attr("id");
			var value = $.trim(tr.find("td").eq(1).find("input[type=hidden]").val());
			if(value != ""){
				gradeJson[key] = value*100;
			}
		}
		//把json对象转换成字符串
		if(JSON.stringify(gradeJson) == "{}"){
			gradeJson = "";
		}else{
			gradeJson = JSON.stringify(gradeJson);
		}
		$("#itemAddForm [name='gradeData']").val(gradeJson);
		
		//取商品的规格
		var paramJson = {};
		var trs = $("#itemAddForm .params table tr");
		for (var i = 1;i < trs.length;i++){
			var tr = trs.eq(i);
			var key = $.trim(tr.find("td").eq(0).text());
			var value = $.trim(tr.find("td").eq(1).find("input[type=hidden]").val());
			if(value != ""){
				key = key.substring(0,key.lastIndexOf(":"));
				paramJson[key] = value;
			}
		}
		//把json对象转换成字符串
		if(JSON.stringify(paramJson) == "{}"){
			paramJson = "";
		}else{
			paramJson = JSON.stringify(paramJson);
		}
		$("#itemAddForm [name='paramData']").val(paramJson);
		//$("#itemAddForm").serialize()将表单序列号为key-value形式的字符串
		$.post(
			baseURL+"shop/item/addItem",
			$("#itemAddForm").serialize(),
			function(data){
				if(data.status == "1"){
					$.messager.alert('提示','新增商品成功','info');
					loadCenterLayout('shop/platWholesaleItemAdd.jsp');
				}else{
					$.messager.alert('提示',data.msg,'warning');
				}
			},
			'json'
		);
	}
	
	function clearForm(){
		loadCenterLayout('shop/platWholesaleItemAdd.jsp');
	}
	
</script>