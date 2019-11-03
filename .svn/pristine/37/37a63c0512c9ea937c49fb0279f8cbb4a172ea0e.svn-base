<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',title: '文件名称管理（单击文件名称加载文件属性）',iconCls:'icon-book-open',collapsible:false" style="width:30%;">
			<table id="fileName_table"></table>
		</div>
		<div data-options="region:'center','title': '文件属性管理','iconCls': 'icon-cog'">
			<table id="properties_table"></table>
			<div id="properties_tb" style="padding:2px 5px;">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="'iconCls':'icon-add','plain':true" onclick="addProperties()">新增</a>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//后台所有配置文件信息
		var config;
		
		// 定义列
		var columns = [ [ {
			field : 'key',
			title : '属性名',
			width : 200,
			align : 'center'
		},{
			field : 'value',
			title : '属性值',
			width : 200,
			align : 'center'
		},{
			field : 'operation',
			title : '操作',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				return '<a style="margin:2px 3px;" class="easyui-linkbutton button-info button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="editProperties(\''+index+'\');">&nbsp;编辑&nbsp;</a>';;
			}
		} ] ];
		
		$(function(){
			//初始化文件名称数据表格
			$('#fileName_table').datagrid({
				columns : [[{
					field : 'fileName',
					title : '文件名称',
					width : 30,
					align : 'center',
					formatter : function(data,row,index){
						return data.substring(1);
					}
				}]],
				rownumbers : true,
				fit : true,
				fitColumns : true,
				singleSelect: true,
				onClickRow : doClickRow
			});
			//初始化文件属性数据表格
			$('#properties_table').datagrid({
				columns : columns,
				toolbar : '#properties_tb',
				rownumbers : true,
				fit : true,
				fitColumns : true,
				singleSelect: true
			});
			$.ajax({
				type:"post",
				url:'<%=basePath %>system/properties/findAll',
				dataType:'json',
				success:function(data){
					config = data;
					var fileNameAttr = [];
					for(var fileName in data){
						var o = {};
						o['fileName'] = fileName;
						fileNameAttr.push(o);
					}
					$('#fileName_table').datagrid('loadData',fileNameAttr);
				}
			});
		});
		
		function doClickRow(rowIndex,rowData){
			var propertiesAttr = [];
			for(var key in config[rowData.fileName]){
				var properties = {};
				properties['key'] = key;
				properties['value'] = config[rowData.fileName][key];
				propertiesAttr.push(properties);
			}
			$('#properties_table').datagrid('loadData',propertiesAttr);
		}
		
		//新增文件属性
		function addProperties(){
			//获取文件名称
			var row = $('#fileName_table').datagrid('getSelected');
			if(row == null){
				$.messager.alert('提示信息','请在文件名称管理中，选择需要添加属性的文件!','warning');
				return;
			}
			var fileName = row.fileName;
			//创建模板
			var template = $('<form>\
				<table style="width:100%;border-spacing:5px 10px;">\
					<input type="hidden" name="fileName"></input>\
					<tr class="rowHeight">\
						<td>属性名:</td>\
						 <td>\
			            	<input name="key" class="easyui-textbox" type="text" data-options="required:true" style="width:450px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>属性值:</td>\
						 <td>\
			            	<input name="value" class="easyui-textbox" type="text" data-options="required:true" style="width:450px;"></input>\
		            	</td>\
					</tr>\
				</table>\
			</form>');
			$.parser.parse(template);
			$("<div>").css({padding:"15px"}).dialog({
				id : "model_add_dialog",
	    		width : "550px",
	    		height : "220px",
	    		closable : false,
	    		modal : true,
	    		title : "新增文件属性",
			    onOpen : function(){
			    	template.find("input[name=fileName]").val(fileName);
			    	$('#model_add_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						if(!template.form('validate')){
							$.messager.alert('提示','表单还没填完哦，请填完表单再提交!','warning');
							return;
						}
						var key = template.find("input[name=key]").val();
						if(config[fileName][key] != undefined){
							$.messager.alert('提示','已经存在名为\"'+key+'\"的属性！','warning');
							return;
						}
						//提交模板数据
						$.post(
							'<%=basePath %>system/properties/update',
							template.serialize(),
							function(data){
								var key = template.find("input[name=key]").val();
								var value = template.find("input[name=value]").val();
								config[row.fileName][key] = value;
								var propertiesAttr = [];
								for(var key in config[fileName]){
									var properties = {};
									properties['key'] = key;
									properties['value'] = config[fileName][key];
									propertiesAttr.push(properties);
								}
								$('#properties_table').datagrid('loadData',propertiesAttr);
						    	$('#model_add_dialog').dialog("destroy");
							}
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
						template.remove();
				    	$('#model_add_dialog').dialog("destroy");
				    }
				}]
	    	});
		}
		//编辑文件属性
		function editProperties(rowIndex){
			//获取文件名称
			var row = $('#fileName_table').datagrid('getSelected');
			if(row == null){
				$.messager.alert('提示信息','请在文件名称管理中，选择需要编辑属性的文件!','warning');
				return;
			}
			var fileName = row.fileName;
			$('#properties_table').datagrid('selectRow',rowIndex);
			var prRow = $('#properties_table').datagrid('getSelected');
			//创建模板
			var template = $('<form>\
				<table style="width:100%;border-spacing:5px 10px;">\
					<tr class="rowHeight">\
						<td>属性名:</td>\
						 <td>\
			            	<input name="key" class="easyui-textbox" type="text" data-options="required:true" style="width:450px;" disabled=true></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>属性值:</td>\
						 <td>\
			            	<input name="value" class="easyui-textbox" type="text" data-options="required:true" style="width:450px;"></input>\
		            	</td>\
					</tr>\
				</table>\
			</form>');
			$.parser.parse(template);
			$("<div>").css({padding:"5px"}).dialog({
				id : "model_add_dialog",
	    		width : "550px",
	    		height : "200px",
	    		closable : false,
	    		modal : true,
	    		title : "编辑文件属性",
			    onOpen : function(){
			    	template.find("[textboxname=key]").textbox("setValue",prRow.key);
			    	template.find("[textboxname=value]").textbox("setValue",prRow.value);
			    	$('#model_add_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						if(!template.form('validate')){
							$.messager.alert('提示','表单还没填完哦，请填完表单再提交!','warning');
							return;
						}
						var key = template.find("input[name=key]").val();
						var value = template.find("input[name=value]").val();
						//提交模板数据
						$.post(
							'<%=basePath %>system/properties/update',
							{'fileName':fileName,'key':key,'value':value},
							function(data){
								config[fileName][key] = value;
								$('#properties_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: {
								       'fileName':fileName,
								       'key':key,
								       'value':value
								    }
								});
						    	$('#model_add_dialog').dialog("destroy");
							}
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
						template.remove();
				    	$('#model_add_dialog').dialog("destroy");
				    }
				}]
	    	});
		}
	</script>