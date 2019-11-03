<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		.breadthT {
			height: 60px;
			width: 100px;
			line-height: 60px;
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
	</style>
	
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west'" style="width:50%;">
			<table id="item_table"></table>
			<div id="item_tb" style="padding:2px 5px;">
				<input id="entityName" class="easyui-combobox" data-options="required:true,editable:false,'prompt':'没有查询到批发商店铺哦'" style="width:200px;" />		
				<a class="easyui-menubutton" data-options="menu:'#controll-panel',iconCls:'icon-help'" style="width:120px;"><span id="itemStatus">商品状态</span></a>
				<input id="condition" data-options="'prompt':'请输入商品名称/价格'" class="easyui-textbox" style="width:200px;">				
	            <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="queryItem()"  data-options="'plain':true">&nbsp;搜索</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
			</div>
			<div id="controll-panel" style="width: 120px; display: none;">
				<div onclick="queryItemStatus({'itemStatus':'1','isDel':'0','statusText':'已上架'});">已上架</div>
				<div onclick="queryItemStatus({'itemStatus':'2','isDel':'0','statusText':'已下架'});">已下架</div>
				<div onclick="queryItemStatus({'itemStatus':'','isDel':'0','statusText':'全部'});">全部</div>
				<div class="menu-sep"></div>
				<div onclick="queryItemStatus({'itemStatus':'','isDel':'1','statusText':'回收站'});">回收站</div>
				<div onclick="queryItemStatus({'itemStatus':'3','isDel':'','statusText':'违规'});">违规</div>
			</div>
		</div>
		<div data-options="region:'center',collapsible:false" style="width:50%;">
			<table id="param_table"></table>
			<div id="param_tb" style="padding:2px 5px;">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="'iconCls':'icon-add','plain':true" onclick="paramAdd()">新增</a>	
				<a class="easyui-menubutton" data-options="menu:'#param_controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="paramStatus">套餐状态</span></a>	
				<input id="paramCondition" data-options="'prompt':'请输入套餐名称/价格'" class="easyui-textbox" style="width:200px;">				
	            <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="paramQuery()"  data-options="'plain':true">&nbsp;搜索</a>
	            <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initParamQuery()" data-options="'plain':true">&nbsp;清除</a>
			</div>
			<div id="param_controll-panel" style="width: 130px; display: none;">
				<div onclick="queryParamStatus({'paramStatus':'1','paramIsDel':'0','statusText':'已上架'});">已上架</div>
				<div onclick="queryParamStatus({'paramStatus':'2','paramIsDel':'0','statusText':'已下架'});">已下架</div>
				<div onclick="queryParamStatus({'paramStatus':'','paramIsDel':'0','statusText':'全部'});">全部</div>
				<div class="menu-sep"></div>
				<div onclick="queryParamStatus({'paramStatus':'','paramIsDel':'1','statusText':'回收站'});">回收站</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//商家对应的数据库表名
		var entityTableName = "business_apply";
		//商家UUID对应的字段名
		var entityUUIDField = "businessUUID";
		//商家名称对应的字段名
		var entityNameField = "businessName";
		//控制商家显示对应的字段名(选填，根据业务需要选择某个字段来控制商家是否显示在下拉列表中，多个字段用","来分割，要保证字段名和字段值的顺序一致)
		var entityShowField = "approvalStatus,shopState,applyType";
		//控制商家显示对应的字段值(选填)
		var entityShowValue = "1,1,1";
		//商家显示时的排序字段(选填，多个字段用","分割，降序排序desc，默认升序asc)
		var entitySortField = "applyTime asc";
		//商家申请人UUID对应的字段名
		var personUUIDField = "vipUUID";
		//商家类型对应的字段名
		var entityTypeField = "applyType";
		//商家类型对应的字段值(0零售商，1批发商)
		var entityTypeValue;
		//商家行业分类编号对应的字段名
		var catUUIDField = "catUUID";
		//商家行业分类编号对应的字段值
		var catUUIDValue = "1";
		//商家行业分类对应的字段名
		var catNameField = "classification";
		//商家行业分类对应的字段值
		var catNameValue;
		
		
		//默认商品删除状态
		var isDel = "0";
		var paramIsDel = "0";
		//默认商品状态
		var itemStatus = "1";
		var paramStatus = "1";
		//默认店铺uuid
		var entityUUID = "-1";
		
		// 定义列
		var columns = [ [ {
			field : 'itemImg',
			title : '商品图片',
			width : 50,
			formatter : function(data,row,index){
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="<%=basePath %>'+data+'"/>';
			}
		},{
			field : 'title',
			title : '商品名称',
			width : 80,
			align : 'center'
		},{
			field : 'tagPrice',
			title : '商品原价',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'price',
			title : '商品现价',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'itemStatus',
			title : '状态',
			width : 30,
			align : 'center',
			formatter : function(data,row,index){
				//商品状态：1上架，2下架，3商品违规
				if(data == 1){
					return "上架";
				}
				if(data == 2){
					return "下架";
				}
				if(data == 3){
					return "违规商品";
				}
			}
		},{
			field : 'operation',
			title : '操作',
			width : 60,
			align : 'center',
			formatter : function(data,row,index){
				var html = "";
				if(row.itemStatus == "1"){
					//info primary warning success danger
					html = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeStatus({\'index\':\''+index+'\',\'status\':\'2\'});">&nbsp;下架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
				}
				if(row.itemStatus == "2"){
					html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeStatus({\'index\':\''+index+'\',\'status\':\'1\'});">&nbsp;上架&nbsp;</a>';
					html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-info button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemEdit(\''+index+'\');">&nbsp;编辑&nbsp;</a>';
					html += '<br/></br><a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
					html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemDel(\''+index+'\');">&nbsp;删除&nbsp;</a>';
				}
				if(row.itemStatus == "3"){
					html = '<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'商品涉嫌违规，请与管理员联系，联系方式xxxxxxxxx！\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
					html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
					html += '<br/></br><a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="itemDel(\''+index+'\');">&nbsp;删除&nbsp;</a>';
				}
				return html;
			}
		} ] ];
		
		var paramColumns = [ [ {
			field : 'itemImg',
			title : '套餐图片',
			width : 50,
			formatter : function(data,row,index){
				return '<img style="width:100px;height:100px;margin-left:-15px;" src="<%=basePath %>'+data+'"/>';
			}
		},{
			field : 'title',
			title : '套餐名称',
			width : 80,
			align : 'center'
		},{
			field : 'grade',
			title : '面向客户',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return data==0?'普通零售商':data+'级零售商';
			}
		},{
			field : 'price',
			title : '价格',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return (data/100).toFixed(2);
			}
		},{
			field : 'stock',
			title : '库存',
			width : 30,
			align : 'center'
		},{
			field : 'operation',
			title : '操作',
			width : 60,
			align : 'center',
			formatter : function(data,row,index){
				var itemRow = $('#item_table').datagrid('getSelected');
				var html = "";
				if(itemRow.itemStatus == "3"){
					html += '<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="paramLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
					html += '&nbsp;<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'商品涉嫌违规，请与管理员联系，联系方式xxxxxxxxx！\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
				}else{
					if(row.paramStatus == "1"){
						//info primary warning success danger
						html = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeParamStatus({\'index\':\''+index+'\',\'status\':\'2\'});">&nbsp;下架&nbsp;</a>';
						html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="paramLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
					}
					if(row.paramStatus == "2"){
						html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="changeParamStatus({\'index\':\''+index+'\',\'status\':\'1\'});">&nbsp;上架&nbsp;</a>';
						html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-info button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="paramEdit(\''+index+'\');">&nbsp;编辑&nbsp;</a>';
						html += '<br/></br><a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="paramLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
						html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="paramDel(\''+index+'\');">&nbsp;删除&nbsp;</a>';
					}
				}
				return html;
			}
		} ] ];
		
		$(function(){
			$.ajax({
				url : baseURL+'shop/item/findEntityList',
				data:{'entityTableName':entityTableName,'personUUIDField':personUUIDField,'entityShowField':entityShowField,'entityShowValue':entityShowValue,'entitySortField':entitySortField},
				type:"post",
				success:function(data){
					if(data.status == "1"){
						data = data.data;
						var loadData = [];
						for(var i = 0;i < data.length;i++){
							var text = data[i][entityNameField];
							var value = JSON.stringify(data[i]);
							loadData.push({'text':text,'value':value})
						}
						$('#entityName').combobox({
							data : loadData,
				            valueField : 'value',
				            textField : 'text',
				            onLoadSuccess : function(){
				            	$(this).combobox("setValue",loadData[0].value);
				            },
				            onChange: function(){
				            	var value = JSON.parse($(this).combobox('getValue'))
				                entityUUID = value[entityUUIDField];
				            	catUUIDValue = value[catUUIDField];
				            	catNameValue = value[catNameField];
				                $('#item_table').datagrid({
				                	url : '<%=basePath %>shop/item/findItemPage',
				    				queryParams: {"entityUUID":entityUUID,"isDel":isDel,"itemStatus":itemStatus}
				                });
				                $('#param_table').datagrid('loadData',{total:0,rows:[]});
				            }
				        });
					}else{
						$.messager.alert('提示',data.msg,'warning');
					}
				},
				dataType:'json'
			});
			//初始化商品数据表格
			$('#item_table').datagrid({
				iconCls: 'icon-item',
				title: '&nbsp;&nbsp;商品管理（单击商品查看套餐）',
				columns : columns,
				toolbar : '#item_tb',
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				onClickRow : doClickRow,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到商品信息哦</span>"
			});
			//初始化套餐数据表格
			$('#param_table').datagrid({
				iconCls: 'icon-param',
				title: '&nbsp;&nbsp;套餐管理',
				columns : paramColumns,
				toolbar : '#param_tb',
				rownumbers : true,
				pagination : true,
				pageList : [10,20,30,40],
				pageSize : 10,
				fit : true,
				fitColumns : true,
				nowrap : false,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到套餐信息哦</span>"
			});
		});
		
		function doClickRow(rowIndex,rowData){
			$('#param_table').datagrid({
				url : '<%=basePath %>shop/item/findParamPage',
				queryParams: {"itemUUID":rowData.itemUUID,"isDel":paramIsDel}
			});
		}
		
		//删除商品
		function itemDel(rowIndex){
			$('#item_table').datagrid('selectRow',rowIndex);
			var row = $('#item_table').datagrid('getSelected');
			var msg = "删除后可以在回收站中找回。";
			var isDelVal = "1";
			if(row.isDel == "1"){
				isDelVal = "2";
				msg = "删除后将不能找回，请谨慎操作！";
			}
			$.messager.confirm("操作提醒", "确定要删除商品\""+row.title+"\"吗？"+msg, function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>shop/item/upItem',
						data:{'itemUUID':row.itemUUID,'isDel':isDelVal},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#item_table').datagrid('reload',{'entityUUID':entityUUID,'isDel':isDel});
								$('#param_table').datagrid('loadData',{total:0,rows:[]});
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		//修改商品状态
		function changeStatus(content){
			var rowIndex = content.index;
			var itemStatus = content.status;
			$('#item_table').datagrid('selectRow',rowIndex);
			var row = $('#item_table').datagrid('getSelected');
			var msg = "";
			if(itemStatus == "1"){
				msg = "确定要将商品\"" + row.title + "\"上架吗？";
			}
			if(itemStatus == "2"){
				msg = "确定要将商品\"" + row.title + "\"下架吗？";
			}
			$.messager.confirm("操作提醒", msg, function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>shop/item/upItem',
						data:{'itemUUID':row.itemUUID,'itemStatus':itemStatus,'isDel':'0'},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								//如果是在回收站中，需要重新加载数据表格，否则只刷新当前行
								if(isDel == "1"){
									$('#item_table').datagrid('reload');
								}
								$('#item_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: {
								        'itemStatus' : itemStatus
								    }
								});
								$('#param_table').datagrid({
									url : '<%=basePath %>shop/item/findParamPage',
									queryParams: {"itemUUID":row.itemUUID,"isDel":paramIsDel}
								});
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		//删除套餐
		function paramDel(rowIndex){
			$('#param_table').datagrid('selectRow',rowIndex);
			var row = $('#param_table').datagrid('getSelected');
			var msg = "删除后可以在回收站中找回。";
			var isDelVal = "1";
			if(row.isDel == "1"){
				isDelVal = "2";
				msg = "删除后将不能找回，请谨慎操作！";
			}
			$.messager.confirm("操作提醒", "确定要删除套餐\""+row.title+"\"吗？"+msg, function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>shop/item/saveOrUpParam',
						data:{'id':row.id,'paramUUID':row.paramUUID,'isDel':isDelVal},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#param_table').datagrid('reload',{'itemUUID':row.itemUUID,'isDel':paramIsDel});
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		//修改套餐状态
		function changeParamStatus(content){
			var rowIndex = content.index;
			var paramStatus = content.status;
			$('#param_table').datagrid('selectRow',rowIndex);
			var row = $('#param_table').datagrid('getSelected');
			var msg = "";
			if(paramStatus == "1"){
				msg = "确定要将套餐\"" + row.title + "\"上架吗？";
			}
			if(paramStatus == "2"){
				msg = "确定要将套餐\"" + row.title + "\"下架吗？";
			}
			$.messager.confirm("操作提醒", msg, function (c) {
				if(c){
					$.ajax({
						type:"post",
						url:'<%=basePath %>shop/item/saveOrUpParam',
						data:{'id':row.id,'paramUUID':row.paramUUID,'paramStatus':paramStatus,'isDel':'0'},
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								//如果是在回收站中，需要重新加载数据表格，否则只刷新当前行
								if(isDel == "1"){
									$('#param_table').datagrid('reload');
								}else{
									$('#param_table').datagrid('updateRow',{
									    index: rowIndex,
									    row: {
									        'paramStatus' : paramStatus
									    }
									});
								}
							}else{
								$.messager.alert("提示", data.msg, "warning");
							}
						}
					});
				}
			});
		}
		
		//编辑商品信息
		function itemEdit(rowIndex){
			$('#item_table').datagrid('selectRow',rowIndex);
			var row = $('#item_table').datagrid('getSelected');
			
			var template = $('<div>\
				<form id="itemEditForm" class="itemForm" method="post">\
					<input type="hidden" name="itemUUID"></input>\
				    <table style="border-collapse:separate; border-spacing:30px 30px;">\
					    <tr>\
				            <td>商品分类:</td>\
				            <td>\
				            	<a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">选择分类</a>\
				            	<input type="hidden" name="catUUID"></input>\
				            	<input type="hidden" name="catCode"></input>\
				            	<input type="hidden" name="catName"></input>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>商品名称:</td>\
				            <td>\
				            	<input class="easyui-textbox" type="text" name="title" data-options="required:true,validType:\'length[1,20]\'" style="width:280px;"></input>\
			            	</td>\
				        </tr>\
				        <tr>\
				            <td>商品简介:</td>\
				            <td>\
				            	<input class="easyui-textbox" name="intro" data-options="required:true,multiline:true,validType:\'length[1,100]\'" style="width:280px;height:60px;"></input>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>商品原价:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="tagPriceView" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>\
				            	<input type="hidden" name="tagPrice"/>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>商品现价:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="priceView" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>\
				            	<input type="hidden" name="price"/>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>展示销量:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="tagMonthlySales" data-options="min:1,max:99999999,precision:0,required:true" style="width:280px;"/>\
				            </td>\
				        </tr>\
						<tr>\
							<td>是否上架:</td>\
							<td>\
								<select class="easyui-combobox" name="itemStatus" data-options="required:true,editable:false" style="width:280px;">\
									<option value="1">立即上架</option>\
									<option value="2">稍后上架</option>\
								</select>\
							</td>\
						</tr>\
						<tr>\
							<td>商品规格:</td>\
							<td>\
								<input id="modelName" class="easyui-combobox" data-options="editable:false" style="width:280px;"/>(选择分类加载商品规格)\
								<input type="hidden" name="modelUUID" />\
							</td>\
						</tr>\
						<tr class="params hide"></tr>\
				        <tr>\
				            <td>商品封面图:</td>\
				            <td>\
				            	 <a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>(点击图片放大)\
				                 <input type="hidden" name="itemImg"/>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>商品展示图:</td>\
				            <td>\
				            	 <a href="javascript:void(0)" class="easyui-linkbutton subImgList">上传图片</a>\
				                 <input type="hidden" name="subImgList" />\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>商品详情图:</td>\
				            <td>\
				            	 <a href="javascript:void(0)" class="easyui-linkbutton detailImgList">上传图片</a>\
				                 <input type="hidden" name="detailImgList" />\
				            </td>\
				        </tr>\
				    </table>\
				</form>\
			</div>');
			//解析easyui的css
			$.parser.parse(template);
			template.find(".textbox .textbox-prompt").css("color","black");
			$("<div>").css({padding:"5px"}).dialog({
				id : "item_edit_dialog",
	    		width : "650px",
	    		height : "600px",
	    		modal:true,
	    		title : "商品编辑",
	    		closable: false,
			    onOpen : function(){
			    	template.find("[name=itemUUID]").val(row.itemUUID);
			    	template.find("[textboxname=title]").textbox("setValue",row.title);
			    	template.find("[textboxname=intro]").textbox("setValue",row.intro);
			    	template.find("[numberboxname=tagPriceView]").numberbox("setValue",(row.tagPrice/100).toFixed(2));
			    	template.find("[numberboxname=priceView]").numberbox("setValue",(row.price/100).toFixed(2));
			    	template.find("[numberboxname=tagMonthlySales]").numberbox("setValue",row.tagMonthlySales);
			    	if(row.modelUUID != undefined){
				    	template.find("[name=modelUUID]").val(row.modelUUID);
			    	}
			    	$('#item_edit_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						//有效性验证
						if(!template.find("form").form('validate')){
							$.messager.alert('提示','表单还未填写完成!','warning');
							return;
						}
						//取商品价格，单位为“分”
						template.find("[name=tagPrice]").val(eval(template.find("[name=tagPriceView]").val()) * 100);
						template.find("[name=price]").val(eval(template.find("[name=priceView]").val()) * 100);
						//template.find("form").serialize()将表单序列号为key-value形式的字符串
						$.post(
							baseURL+"shop/item/upItem",
							template.find("form").serialize(),
							function(data){
								if(data.status == "1"){
									//更新行记录
									row.catUUID = template.find("[name=catUUID]").val();
									row.catCode = template.find("[name=catCode]").val();
									row.catName = template.find("[name=catName]").val();
									row.title = template.find("[name=title]").val();
									row.intro = template.find("[name=intro]").val();
									row.tagPrice = template.find("[name=tagPrice]").val();
									row.price = template.find("[name=price]").val();
									row.tagMonthlySales = template.find("[name=tagMonthlySales]").val();
									row.itemStatus = template.find("[name=itemStatus]").val();
									row.modelUUID = template.find("[name=modelUUID]").val();
									row.itemImg = template.find("[name=itemImg]").val();
									row.subImgList = template.find("[name=subImgList]").val();
									row.subImgList = template.find("[name=subImgList]").val();
									$('#item_table').datagrid('updateRow',{
									    index : rowIndex,
									    row : JSON.stringify(row)
									});
									$('#item_edit_dialog').dialog("destroy");
								}else{
									$.messager.alert('提示',data.msg,'warning');
								}
							},
							'json'
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
						template.remove();
				    	$('#item_edit_dialog').dialog("destroy");
				    }
				}]
	    	});
			//根据catUUID获取模板列表
			$.ajax({
				url : baseURL+'shop/itemModel/findListByCatUUID',
				data:{'catUUID':row.catUUID,'enable':'1'},
				type:"post",
				success:function(data){
					if(data.length == 0){
		                return;
		            }
					var modelData = "";
					var loadData = [];
					for(var i = 0;i < data.length;i++){
						if(data[i].modelUUID == row.modelUUID){
							modelData = JSON.stringify(data[i]);
						}
						var text = data[i].modelName;
						var value = JSON.stringify(data[i]);
						loadData.push({'text':text,'value':value})
					}
					$("#modelName").combobox({
					    data : loadData,
					    valueField : 'value',
					    textField : 'text',
					    onLoadSuccess : function(){
					    	if(modelData != ""){
					    		$(this).combobox('setValue',modelData);
					    		modelData = "";
					    	}
					    },
					    onChange: function(){
					    	//重置规格组件
		   				 	$("#itemEditForm [name=modelUUID]").val('');
		        			$("#itemEditForm .params").hide();
		   				 	$("#itemEditForm .params").empty();
		   				 	
			                var value = $(this).combobox('getValue');
			                if(value.length == 0){
			                	return;
			                }
			                var modelUUID = JSON.parse(value).modelUUID;
			                var modelData = JSON.parse(value).modelData;
			                var attrArr = modelData.split(",");
			                $("#itemEditForm .params").show();
			                $("#itemEditForm [name=modelUUID]").val(modelUUID);
			                var html = '<td colspan="2"><table style="width:380px;"><tr><td colspan="2" class="group">产品参数: </td></tr>';
			    			for(var i = 0;i < attrArr.length;i++){
								html += '<tr"><td style="width:92px;height:60px;">'+attrArr[i]+': </td><td><input class="easyui-textbox" type="text" style="width:280px;" disabled=true></input></td></tr>';
			    			}
			    			html += '</table></td>';
			    			var modelDom = $(html);
			    			$.parser.parse(modelDom);
							$("#itemEditForm .params").append(modelDom);
			            }
					});
				},
				dataType:'json'
			});
			shop.initItemCat({fun:function(node){
				//重置规格组件
				$("#itemEditForm [name=modelUUID]").val('');
                $('#modelName').combobox('clear');//清空选中项
                $('#modelName').combobox('loadData', {});//清空option选项   
                $("#itemEditForm .params").hide();
                $("#itemEditForm .params").empty();
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
			   				 	$("#itemEditForm [name=modelUUID]").val('');
			        			$("#itemEditForm .params").hide();
			   				 	$("#itemEditForm .params").empty();
			   				 	
    			                var value = $(this).combobox('getValue');
    			                if(value.length == 0){
    			                	return;
    			                }
    			                var modelUUID = JSON.parse(value).modelUUID;
    			                var modelData = JSON.parse(value).modelData;
    			                var attrArr = modelData.split(",");
    			                $("#itemEditForm .params").show();
    			                $("#itemEditForm [name=modelUUID]").val(modelUUID);
    			                var html = '<td colspan="2"><table style="width:380px;"><tr><td colspan="2" class="group">产品参数: </td></tr>';
    			    			for(var i = 0;i < attrArr.length;i++){
    								html += '<tr"><td style="width:92px;height:60px;">'+attrArr[i]+': </td><td><input class="easyui-textbox" type="text" style="width:280px;" disabled=true></input></td></tr>';
    			    			}
    			    			html += '</table></td>';
    			    			var modelDom = $(html);
    			    			$.parser.parse(modelDom);
    							$("#itemEditForm .params").append(modelDom);
    			            }
    					});
    				},
    				dataType:'json'
    			});
    		},'catUUID':row.catUUID,'catCode':row.catCode,'catName':row.catName,'rootUUID':catUUIDValue,'rootName':catNameValue});
			
			shop.initOnePicUpload({'itemImg':row.itemImg});
	    	shop.initPicUpload('.subImgList',{'imgList':row.subImgList});
	    	shop.initPicUpload('.detailImgList',{'imgList':row.detailImgList});
		}
		
		//查看商品信息
		function itemLook(rowIndex){
			$('#item_table').datagrid('selectRow',rowIndex);
			var row = $('#item_table').datagrid('getSelected');
			
			$("<div>").css({padding:"5px"}).window({
	    		width : "630px",
	    		height : "650px",
	    		modal:true,
	    		title : "商品信息",
	    		collapsible : false,
	    		minimizable : false,
	    		maximizable : false,
			    onClose : function(){
			    	$(this).window("destroy");
			    },
			    onOpen : function(){
			    	var html = '<div><table cellpadding="0" cellspacing="0" border="1">';
			    	
			    	html += '<tr>';
			    	html += '<td width="150px"><span style="color:red;">总销量</span>:' + row.totalSales + '</td>';
			    	html += '<td width="150px"><span style="color:red;">月销量</span>:' + row.totalMonthlySales + '</td>';
			    	html += '<td width="150px"><span style="color:red;">展示销量</span>:' + row.tagMonthlySales + '</td>';
			    	html += '<td width="150px"><span style="color:red;">评分</span>:' + (row.score == undefined?"0":row.score) + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="2"><span style="color:red;">商品原价</span>:' + (row.price/100).toFixed(2) + '</td>';
			    	html += '<td colspan="2"><span style="color:red;">商品现价</span>:' + (row.tagPrice/100).toFixed(2) + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="2"><span style="color:green;">商品名称</span>:' + row.title + '</td>';
			    	html += '<td colspan="2"><span style="color:green;">商品分类</span>:' + row.catName + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="2" align="center"><span style="color:green;">商品封面图</span></td>';
			    	html += '<td colspan="2" align="center"><span style="color:green;">商品简介</span></td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="2"><img src="<%=basePath %>'+row.itemImg+'" width="300px;" height="300px;"></td>';
			    	html += '<td colspan="2" valign="top">' + row.intro + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="4" align="center"><span style="color:green;">商品展示图</span></td>';
			    	html += '</tr>';
			    	
			    	var subImgDiv = '<div><ul>';
			    	if(row.subImgList != null){
			    		var imgArray = row.subImgList.split(",");
			    		for(var i = 0;i < imgArray.length;i++){
			    			var url = '<%=basePath %>' + imgArray[i];
			    			subImgDiv += '<li"><img src="'+url+'" width="300px;" height="300px;"/></li>';
			    		}
			    	}
			    	subImgDiv += '</ul></div>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="4">'+subImgDiv+'</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="4" align="center"><span style="color:green;">商品详情图</span></td>';
			    	html += '</tr>';
			    	
			    	var detailImgDiv = '<div><ul>';
			    	if(row.detailImgList != null){
			    		var imgArray = row.detailImgList.split(",");
			    		for(var i = 0;i < imgArray.length;i++){
			    			var url = '<%=basePath %>' + imgArray[i];
			    			detailImgDiv += '<li"><img src="'+url+'" width="600px;"/></li>';
			    		}
			    	}
			    	detailImgDiv += '</ul></div>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="4">'+detailImgDiv+'</td>';
			    	html += '</tr>';
			    	
			    	html += '</table></div>';
			    	$(this).html(html);
			    }
	    	});
		}
		
		//商品提示信息
		function tips(content){
			$.messager.alert(content.title, content.msg, content.type);
		}
		
		function queryItemStatus(data){
			itemStatus = data.itemStatus;
			isDel = data.isDel;
			$("#itemStatus").html(data.statusText);
			$('#item_table').datagrid('reload',{'itemStatus':itemStatus,'entityUUID':entityUUID,'isDel':isDel});
			$('#param_table').datagrid('loadData',{total:0,rows:[]});
		}
		
		function queryParamStatus(data){
			var row = $('#item_table').datagrid('getSelected');
			paramStatus = data.paramStatus;
			paramIsDel = data.paramIsDel;
			$("#paramStatus").html(data.statusText);
			$('#param_table').datagrid('reload',{'paramStatus':paramStatus,'itemUUID':row.itemUUID,'isDel':paramIsDel});
		}
		
		function queryItem(){
			var condition = $("#condition").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#item_table').datagrid('reload',{"itemStatus":itemStatus,'entityUUID':entityUUID,'isDel':isDel,"price":condition});
				return;
			}
			$('#item_table').datagrid('reload',{"itemStatus":itemStatus,'entityUUID':entityUUID,'isDel':isDel,"catName":condition,"title":condition,"intro":condition});
			$('#param_table').datagrid('loadData',{total:0,rows:[]});
		}
		
		function initQuery(){
			$("#condition").textbox('setValue','');
			$('#item_table').datagrid('reload',{'itemStatus':itemStatus,'isDel':isDel,'entityUUID':entityUUID});
			$('#param_table').datagrid('loadData',{total:0,rows:[]});
		}
		
		//添加套餐
		function paramAdd(){
			var row = $('#item_table').datagrid('getSelected');
			if(row == null){
				$.messager.alert("提示", "请在商品管理中选择需要添加套餐的商品", "warning");
				return;
			}
			var template = $('<div>\
				<form class="itemForm" method="post">\
					<input type="hidden" name="entityUUID"></input>\
					<input type="hidden" name="entityName"></input>\
					<input type="hidden" name="entityLogo"></input>\
					<input type="hidden" name="catUUID"></input>\
					<input type="hidden" name="itemUUID"></input>\
					<input type="hidden" name="itemType"></input>\
					<input type="hidden" name="distributeStatus"></input>\
				    <table style="border-collapse:separate; border-spacing:50px 30px;">\
				    	<tr>\
					    	<td>零售等级:</td>\
					    	<td>\
								<input id="grade" class="easyui-combobox" data-options="editable:false" style="width:280px;"/>\
							</td>\
				        </tr>\
				    	<tr>\
				            <td>套餐名称:</td>\
				            <td>\
				            	<input class="easyui-textbox" type="text" name="title" data-options="required:true,validType:\'length[1,20]\'" style="width:280px;"></input>\
			            	</td>\
				        </tr>\
				        <tr>\
				            <td>库存数量:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="stock" data-options="min:1,max:99999999,precision:0,required:true" style="width:280px;"/>\
				            </td>\
				        </tr>\
				        <tr>\
							<td>是否上架:</td>\
							<td>\
								<select class="easyui-combobox" name="paramStatus" data-options="required:true,editable:false" style="width:280px;">\
									<option value="1">立即上架</option>\
									<option value="2">稍后上架</option>\
								</select>\
							</td>\
						</tr>\
				        <tr class="grade"></tr>\
				        <tr>\
			            	<td>套餐图片:</td>\
				            <td>\
				            	 <a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>(点击图片放大)\
				                 <input type="hidden" name="itemImg"/>\
				            </td>\
				        </tr>\
				        <tr class="params hide"></tr>\
				    </table>\
				    <input type="hidden" name="paramData"/>\
				    <input type="hidden" name="gradeData" />\
				</form>\
			</div>');
			$.parser.parse(template);
			if(row.modelUUID != null){
				$.ajax({
					url : baseURL+'shop/itemModel/findModel',
					data:{'modelUUID':row.modelUUID},
					type:"post",
					success:function(data){
						template.find(".params").show();
		                var modelData = data.modelData;
		                
		                var attrArr = modelData.split(",");
		                template.find(".params").show();
		                var html = '<td colspan="2"><table style="width:380px;"><tr><td colspan="2" class="group">产品参数: </td></tr>';
		    			for(var i = 0;i < attrArr.length;i++){
							html += '<tr"><td style="width:100px;height:60px;">'+attrArr[i]+': </td><td><input class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input></td></tr>';
		    			}
		    			html += '</table></td>';
		    			var modelDom = $(html);
		    			$.parser.parse(modelDom);
		    			template.find(".params").append(modelDom);
					},
					dataType:'json'
				});
			}
			
			$.ajax({
				url : baseURL+'shop/item/findGradeList',
				type:"post",
				success:function(data){
					var gradeDom = null;
					var gradeData = [];
					if(data.length > 0){
		                var html = '<td colspan="2"><table style="width:385px;">';
		                html += '<tr><td colspan="2" class="group">零售商等级对应价格: </td></tr>';
		                html += '<tr"><td style="width:105px;height:60px;">普通零售商: </td><td><input class="easyui-numberbox" type="text" id="0" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/></td></tr>'
		    			for(var i = 0;i < data.length;i++){
		    				var grade = {};
		    				grade['text'] = data[i].aliasName;
		    				grade['value'] = data[i].number;
		    				gradeData.push(grade);
							html += '<tr"><td style="width:105px;height:60px;">'+data[i].aliasName+': </td><td><input class="easyui-numberbox" type="text" id="'+data[i].number+'" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/></td></tr>';
		    			}
		    			html += '</table></td>';
		    			gradeDom = $(html);
		    			$.parser.parse(gradeDom);
					}
					gradeData.unshift({'text':'普通零售商','value':0});
					gradeData.unshift({'text':'全部','value':-1,'selected':true});
					$("#grade").combobox({
					    data : gradeData,
					    valueField : 'value',
					    textField : 'text',
					    onChange : function(){
					    	 var value = $(this).combobox('getValue');
					    	 if(value == -1){
					    		 template.find(".grade").empty().append(gradeDom);
					    	 }else{
					    		 var text = $(this).combobox('getText');
					    		 var select = $('<td colspan="2">\
					    		 	<table style="width:385px;">\
					    		 		 <tr>\
					    		 			<td colspan="2" class="group">零售商等级对应价格: </td>\
					    		 		 </tr>\
							    		 <tr>\
								          	<td style="width:105px;height:60px;">'+text+':</td>\
								            <td>\
								            	<input class="easyui-numberbox" type="text" id="'+value+'" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>\
								            </td>\
								         </tr>\
						            </table>\
						         </td>');
					    		 $.parser.parse(select);
					    		 template.find(".grade").empty().append(select);
					    	 }
			            }
					});
				},
				dataType:'json'
			});
			
			template.find(".textbox .textbox-prompt").css("color","black");
			$("<div>").css({padding:"5px"}).dialog({
				id : "param_add_dialog",
	    		width : "700px",
	    		height : "480px",
	    		closable : false,
	    		modal : true,
	    		title : "新增套餐",
			    onOpen : function(){
			    	template.find("[name=entityUUID]").val(row.entityUUID);
			    	template.find("[name=entityName]").val(row.entityName);
			    	template.find("[name=entityLogo]").val(row.entityLogo);
			    	template.find("[name=catUUID]").val(row.catUUID);
			    	template.find("[name=itemUUID]").val(row.itemUUID);
			    	template.find("[name=itemType]").val(row.itemType);
			    	template.find("[name=distributeStatus]").val(row.distributeStatus);
			    	$('#param_add_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						//有效性验证
						if(!template.find("form").form('validate')){
							$.messager.alert('提示','表单还未填写完成!','warning');
							return;
						}
						//取商品价格，单位为“分”
						template.find("[name=price]").val(eval(template.find("[name=priceView]").val()) * 100);
						
						//取商品的等级价格
						var gradeJson = {};
						var trs = template.find(".grade table tr");
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
						template.find("[name='gradeData']").val(gradeJson);
						
						//取商品的规格
						var paramJson = {};
						var trs = $(".params table tr");
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
						template.find("[name='paramData']").val(paramJson);
						//template.find("form").serialize()将表单序列号为key-value形式的字符串
						$.post(
							baseURL+"shop/item/saveOrUpParam",
							template.find("form").serialize(),
							function(data){
								if(data.status == "1"){
									$('#param_table').datagrid('reload',{"itemUUID":row.itemUUID,"isDel":paramIsDel});
									$('#param_add_dialog').dialog("destroy");
								}else{
									$.messager.alert('提示',data.msg,'warning');
								}
							},
							'json'
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
						template.remove();
				    	$('#param_add_dialog').dialog("destroy");
				    }
				}]
	    	});
			shop.initOnePicUpload({'itemImg':row.itemImg});
		}
		
		//编辑套餐
		function paramEdit(rowIndex){
			var itemRow = $('#item_table').datagrid('getSelected');
			$('#param_table').datagrid('selectRow',rowIndex);
			var row = $('#param_table').datagrid('getSelected');
			
			var template = $('<div>\
				<form class="itemForm" method="post">\
					<input type="hidden" name="id"></input>\
					<input type="hidden" name="paramUUID"></input>\
				    <table style="border-collapse:separate; border-spacing:50px 30px;">\
					    <tr>\
				            <td>套餐名称:</td>\
				            <td>\
				            	<input class="easyui-textbox" type="text" name="title" data-options="required:true,validType:\'length[1,20]\'" style="width:280px;"></input>\
			            	</td>\
				        </tr>\
				        <tr>\
				            <td>套餐价格:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="priceView" data-options="min:0,max:99999999,precision:2,required:true" style="width:280px;"/>\
				            	<input type="hidden" name="price"/>\
				            </td>\
				        </tr>\
				        <tr>\
				            <td>库存数量:</td>\
				            <td>\
				            	<input class="easyui-numberbox" type="text" name="stock" data-options="min:1,max:99999999,precision:0,required:true" style="width:280px;"/>\
				            </td>\
				        </tr>\
				        <tr>\
							<td>是否上架:</td>\
							<td>\
								<select class="easyui-combobox" name="paramStatus" data-options="required:true,editable:false" style="width:280px;">\
									<option value="1">立即上架</option>\
									<option value="2">稍后上架</option>\
								</select>\
							</td>\
						</tr>\
				        <tr>\
			            	<td>套餐图片:</td>\
				            <td>\
				            	 <a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">上传图片</a>(点击图片放大)\
				                 <input type="hidden" name="itemImg"/>\
				            </td>\
				        </tr>\
				        <tr class="params hide"></tr>\
				    </table>\
				    <input type="hidden" name="paramData"/>\
				</form>\
			</div>');
			$.parser.parse(template);
			if(row.paramData != null){
				template.find(".params").show();
				var paramData = JSON.parse(row.paramData);
				var td = $('<td colspan="2"><table style="width:380px;"></table></td>');
				var paramsTitle = $('<tr><td colspan="2" class="group">产品参数: </td></tr>');
				td.find("table").append(paramsTitle);
				for(var key in paramData){
					var textbox = $('<input class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>');
					var paramsTr = $('<tr><td style="width:100px;height:60px;">'+key+': </td><td></td></tr>');
					paramsTr.find("td").eq(1).append(textbox);
					$.parser.parse(paramsTr);
					paramsTr.find(".easyui-textbox").textbox("setValue",paramData[key]);
					td.find("table").append(paramsTr);
				}
				template.find(".params").append(td);
			}
			template.find(".textbox .textbox-prompt").css("color","black");
			$("<div>").css({padding:"5px"}).dialog({
				id : "param_edit_dialog",
	    		width : "700px",
	    		height : "480px",
	    		closable : false,
	    		modal : true,
	    		title : "编辑套餐",
			    onOpen : function(){
			    	template.find("[name=id]").val(row.id);
			    	template.find("[name=paramUUID]").val(row.paramUUID);
			    	template.find("[textboxname=title]").textbox("setValue",row.title);
			    	template.find("[numberboxname=priceView]").numberbox("setValue",(row.price/100).toFixed(2));
			    	template.find("[numberboxname=stock]").numberbox("setValue",row.stock);
			    	$(this).append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						//有效性验证
						if(!template.find("form").form('validate')){
							$.messager.alert('提示','表单还未填写完成!','warning');
							return;
						}
						//取商品价格，单位为“分”
						template.find("[name=price]").val(eval(template.find("[name=priceView]").val()) * 100);
						//取商品的规格
						var paramJson = {};
						var trs = template.find(".params table tr");
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
						template.find("[name='paramData']").val(paramJson);
						//template.find("form").serialize()将表单序列号为key-value形式的字符串
						$.post(
							baseURL+"shop/item/saveOrUpParam",
							template.find("form").serialize(),
							function(data){
								if(data.status == "1"){
									//更新行记录
									row.title = template.find("[name=title]").val();
									row.price = template.find("[name=price]").val();
									row.stock = template.find("[name=stock]").val();
									row.itemImg = template.find("[name=itemImg]").val();
									row.paramData = template.find("[name=paramData]").val();
									row.paramStatus = template.find("[name=paramStatus]").val();
									$('#param_table').datagrid('updateRow',{
									    index : rowIndex,
									    row : JSON.stringify(row)
									});
									$('#param_edit_dialog').dialog("destroy");
								}else{
									$.messager.alert('提示',data.msg,'warning');
								}
							},
							'json'
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
						template.remove();
				    	$('#param_edit_dialog').dialog("destroy");
				    }
				}]
	    	});
			shop.initOnePicUpload({'itemImg':row.itemImg});
		}
		
		//查看套餐信息
		function paramLook(rowIndex){
			$('#param_table').datagrid('selectRow',rowIndex);
			var row = $('#param_table').datagrid('getSelected');
			$("<div>").css({padding:"5px"}).window({
	    		width : "600px",
	    		height : "420px",
	    		modal:true,
	    		title : "套餐信息",
	    		collapsible : false,
	    		minimizable : false,
	    		maximizable : false,
			    onClose : function(){
			    	$(this).window("destroy");
			    },
			    onOpen : function(){
			    	var html = '<div><table cellpadding="0" cellspacing="0" border="1">';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="4"><span style="color:green;">套餐名称</span>:' + row.title + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td width="150px"><span style="color:red;">月销量</span>:' + row.monthlySales + '</td>';
			    	html += '<td width="150px"><span style="color:red;">总销量</span>:' + row.sales + '</td>';
			    	html += '<td width="150px"><span style="color:red;">套餐价格</span>:' + (row.price/100).toFixed(2) + '</td>';
			    	html += '<td width="150px"><span style="color:red;">库存数量</span>:' + row.stock + '</td>';
			    	html += '</tr>';
			    	
			    	html += '<tr>';
			    	html += '<td colspan="2" align="center"><span style="color:green;">套餐图片</span></td>';
			    	html += '<td colspan="2" align="center"><span style="color:green;">规格参数</span></td>';
			    	html += '</tr>';
			    	
			    	var table = '<table width="100%" cellpadding="0" cellspacing="0">';
			    	if(row.paramData != null){
			    		var paramData = JSON.parse(row.paramData);
			    		for(var key in paramData){
							table += '<tr><td style="width:150px;text-align:left;text-indent:5px;border-bottom:#EAEAEA solid 1px;"><span style="color:red;">'+key+'</span>:</td><td style="width:150px;border-bottom:#EAEAEA solid 1px;">'+paramData[key]+'</td></tr>';
	    				}
			    	}
	    			table += "</table>";
	    			
	    			html += '<tr>';
			    	html += '<td colspan="2"><img src="<%=basePath %>'+row.itemImg+'" width="300px;" height="300px;"></td>';
			    	html += '<td colspan="2" valign="top">'+table+'</td>';
			    	html += '</tr>';
			    	
			    	html += '</table></div>';
			    	$(this).html(html);
			    }
	    	});
		}
		
		function paramQuery(){
			var row = $('#item_table').datagrid('getSelected');
			if(row == null){
				$.messager.alert("提示", "请在商品管理中选择商品", "warning");
				return;
			}
			var itemUUID = row.itemUUID;
			var condition = $("#paramCondition").val();
			//使用正则表达式判断字符串是否为数字
			var regPos = /^(\-)?\d+(\.\d+)?$/;
			if(regPos.test(condition) && condition.length != 11){
				condition = condition*100;
				$('#param_table').datagrid('reload',{"paramStatus":paramStatus,"itemUUID":itemUUID,"price":condition,"isDel":paramIsDel});
				return;
			}
			$('#param_table').datagrid('reload',{"paramStatus":paramStatus,"itemUUID":itemUUID,"title":condition,"isDel":paramIsDel});
		}
		
		function initParamQuery(){
			var row = $('#item_table').datagrid('getSelected');
			var itemUUID = row.itemUUID;
			$("#paramCondition").textbox('setValue','');
			$('#param_table').datagrid('reload',{"paramStatus":paramStatus,"itemUUID":itemUUID,"isDel":paramIsDel});
		}
		
	</script>