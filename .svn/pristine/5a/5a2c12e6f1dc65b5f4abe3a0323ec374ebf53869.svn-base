<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<style>
		.combobox-item{
			padding-left:130px;
		}
		.rowHeight{
			height: 40px;
			margin-top: 5px;
		}
	</style>

	<table id="dict_table"></table>
	<div id="dict_tb" style="padding:10px 15px;height: 40px; line-height: 40px;">		
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="'iconCls':'icon-add','plain':true" onclick="dictAdd()">新增</a>
		<a class="easyui-menubutton" data-options="menu:'#controll-panel',iconCls:'icon-help'" style="width:120px;"><span id="enableStatus">启用</span></a>
		<input id="condition" data-options="prompt:'请输入编号/名称/描述'" class="easyui-textbox" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="query()"  data-options="'plain':true">&nbsp;搜索</a>
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="controll-panel" style="width: 80px; display: none;">
		<div onclick="queryEnableStatus({'enableStatus':'0','statusText':'停用'});">停用</div>
		<div onclick="queryEnableStatus({'enableStatus':'1','statusText':'启用'});">启用</div>
		<div class="menu-sep"></div>
		<div onclick="queryEnableStatus({'enableStatus':'','statusText':'全部'});">全部</div>
	</div>
	<script type="text/javascript">
		//默认字典状态
		var enableStatus = "1";
		// 定义列
		var columns = [ [ {
			field : 'typeCode',
			title : '分类编号',
			width : 100,
			align : 'center'
		},{
			field : 'typeName',
			title : '分类名称',
			width : 100,
			align : 'center'
		}, {
			field : 'itemCode',
			title : '字典项编号',
			width : 100,
			align : 'center'
		}, {
			field : 'itemName',
			title : '字典项名称',
			width : 100,
			align : 'center'
		}, {
			field : 'sort',
			title : '排序',
			width : 50,
			align : 'center'
		}, {
			field : 'enable',
			title : '状态',
			width : 50,
			align : 'center',
			formatter : function(data,row,index){
				if(data == 0){
					return '停用';
				}
				if(data == 1){
					return '启用';
				}
			}
		}, {
			field : 'operation',
			title : '操作',
			width : 100,
			align : 'center',
			formatter : function(data,row,index){
				var html = "";
				if(row.enable == "1"){
					//info primary warning success danger
					html = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="dictUp({\'index\':\''+index+'\',\'status\':\'0\'});">&nbsp;停用&nbsp;</a>';
				}
				if(row.enable == "0"){
					html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="dictUp({\'index\':\''+index+'\',\'status\':\'1\'});">&nbsp;启用&nbsp;</a>';
				}
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-info button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="dictEdit(\''+index+'\');">&nbsp;编辑&nbsp;</a>';
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="dictLook(\''+index+'\');">&nbsp;查看&nbsp;</a>';
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="dictDel(\''+index+'\');">&nbsp;删除&nbsp;</a>';
				return html;
			}
		} ] ];
		
		$(function(){
			//初始化数据字典数据表格
			$('#dict_table').datagrid({
				iconCls: 'icon-book-open',
				title: '&nbsp;&nbsp;数据字典管理 ',
				columns : columns,
				url : '<%=basePath %>system/baseDict/findDictPage',
				queryParams: {'enable':enableStatus},
				toolbar : '#dict_tb',
				rownumbers : true,
				pagination : true,
				pageList: [10,20,30,40,50],
				fit : true,
				fitColumns : true,
				singleSelect: true,
				emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到字典信息哦</span>",
			});
			
		});
		
		function dictAdd(){
			//创建模板
			var template = $('<form>\
				<table style="width:100%;">\
					<tr class="rowHeight">\
						<td>分类编号:</td>\
						<td>\
			            	<input name="typeCode" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>分类名称:</td>\
						<td>\
			            	<input name="typeName" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项编号:</td>\
						<td>\
			            	<input name="itemCode" class="easyui-textbox" type="text" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项名称:</td>\
						<td>\
			            	<input name="itemName" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>排序:</td>\
						<td>\
			            	<input name="sort" type="text" data-options="required:true" style="width:280px;padding-left:130px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>描述信息:</td>\
						<td>\
			            	<input name="description" class="easyui-textbox" type="text" data-options="multiline:true" style="width:280px;height:60px;"></input>\
		            	</td>\
					</tr>\
				</table>\
				<input type="hidden" name="enable" value="1"/>\
			</form>');
			$.parser.parse(template);
			template.find('[name=sort]').combobox({
				valueField : 'value',
				textField : 'text',
				editable : false
			});
			template.find('[textboxname=typeCode]').textbox({
				inputEvents: $.extend({},$.fn.textbox.defaults.inputEvents,{
                    keyup:function(event){
                    	var typeCode = event.target.value;
                    	$.post(
           					'<%=basePath %>system/baseDict/getTotalByTypeCode',
           					{"typeCode":typeCode},
           					function(data){
           						var arr = [];
           						for(var i = 1;i <= data;i++){
           							arr.push({'text':i,'value':i});
           						}
           						arr.push({'text':i,'value':i,'selected':true});
           						template.find('[textboxname=sort]').combobox('loadData', arr);
           					}
           				);
                    }
        		})
			});
			
			$("<div>").css({padding:"20px"}).dialog({
				id : "add_dialog",
	    		width : "420px",
	    		height : "400px",
	    		closable : false,
	    		modal : true,
	    		title : "新增数据字典(输入分类编号加载排序)",
			    onOpen : function(){
			    	$('#add_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						if(!template.form('validate')){
							$.messager.alert('提示','表单还没填完哦，请填完表单再提交!','warning');
							return;
						}
						$.post(
							'<%=basePath %>system/baseDict/saveOrUpDict',
							template.serialize(),
							function(data){
								if(data){
									$('#add_dialog').dialog("destroy");
									$('#dict_table').datagrid('reload',{'enable':enableStatus});
								}else{
									$.messager.alert('提示','当前网络繁忙，请稍后重试!','info');
								}
							}
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
				    	$('#add_dialog').dialog("destroy");
				    }
				}]
	    	});
		}
		
		function dictDel(rowIndex){
			$('#dict_table').datagrid('selectRow',rowIndex);
			var row = $('#dict_table').datagrid('getSelected');
			$.messager.confirm('提示信息','确定要删除该数据字典吗？',function(confirm){
				if(confirm){
					$.post(
						'<%=basePath %>system/baseDict/delDict',
						{"typeCode":row.typeCode,"dictUUID":row.dictUUID},
						function(data){
							if(data){
								$('#dict_table').datagrid('reload',{'enable':enableStatus});
							}
						}
					);
				}
			});
		}
		
		function dictUp(data){
			var rowIndex = data.index;
			var enable = data.status;
			
			$('#dict_table').datagrid('selectRow',rowIndex);
			var row = $('#dict_table').datagrid('getSelected');
			var msg = "确定要停用数据字典\"" + row.itemName + "\"吗？";
			if(enable == "1"){
				msg = "确定要启用数据字典\"" + row.itemName + "\"吗？";
			}
			row.enable = enable;
			
			$.messager.confirm("操作提醒", msg, function (c) {
				if(c){
					$.post(
						'<%=basePath %>system/baseDict/saveOrUpDict',
						row,
						function(data){
							if(data){
								$('#add_dialog').dialog("destroy");
								$('#dict_table').datagrid('updateRow',{
								    index: rowIndex,
								    row: JSON.stringify(row)
								});
							}else{
								$.messager.alert('提示','当前网络繁忙，请稍后重试!','info');
							}
						}
					);
				}
			});
		}
		
		function dictEdit(rowIndex){
			$('#dict_table').datagrid('selectRow',rowIndex);
			var row = $('#dict_table').datagrid('getSelected');
			
			//创建模板
			var template = $('<form>\
				<table style="width:100%;">\
					<tr class="rowHeight">\
						<td>分类编号:</td>\
						<td>\
			            	<input name="typeCode" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>分类名称:</td>\
						<td>\
			            	<input name="typeName" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项编号:</td>\
						<td>\
			            	<input name="itemCode" class="easyui-textbox" type="text" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项名称:</td>\
						<td>\
			            	<input name="itemName" class="easyui-textbox" type="text" data-options="required:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>排序:</td>\
						<td>\
			            	<input name="sort" type="text" data-options="required:true" style="width:280px;padding-left:130px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>描述信息:</td>\
						<td>\
			            	<input name="description" class="easyui-textbox" type="text" data-options="multiline:true" style="width:280px;height:60px;"></input>\
		            	</td>\
					</tr>\
				</table>\
				<input type="hidden" name="id"/>\
				<input type="hidden" name="dictUUID"/>\
				<input type="hidden" name="enable"/>\
			</form>');
			$.parser.parse(template);
			template.find('[name=sort]').combobox({
				valueField : 'value',
				textField : 'text',
				editable : false
			});
			template.find('[textboxname=typeCode]').textbox({
				inputEvents: $.extend({},$.fn.textbox.defaults.inputEvents,{
                    keyup:function(event){
                    	var typeCode = event.target.value;
                    	$.post(
           					'<%=basePath %>system/baseDict/getTotalByTypeCode',
           					{"typeCode":typeCode},
           					function(data){
           						var arr = [];
           						for(var i = 1;i <= data;i++){
           							if(row.sort == i){
           								arr.push({'text':i,'value':i,'selected':true});
           							}else{
           								arr.push({'text':i,'value':i});
           							}
           						}
           						template.find('[textboxname=sort]').combobox('loadData', arr);
           					}
           				);
                    }
        		})
			});
			
			$("<div>").css({padding:"20px"}).dialog({
				id : "add_dialog",
	    		width : "420px",
	    		height : "400px",
	    		closable : false,
	    		modal : true,
	    		title : "新增数据字典(输入分类编号加载排序)",
			    onOpen : function(){
			    	var typeCode = row.typeCode;
                	$.post(
       					'<%=basePath %>system/baseDict/getTotalByTypeCode',
       					{"typeCode":typeCode},
       					function(data){
       						var arr = [];
       						for(var i = 1;i <= data;i++){
       							if(row.sort == i){
       								arr.push({'text':i,'value':i,'selected':true});
       							}else{
       								arr.push({'text':i,'value':i});
       							}
       						}
       						template.find('[textboxname=sort]').combobox('loadData', arr);
       					}
       				);
			    	
			    	template.find("[name=id]").val(row.id);
			    	template.find("[name=dictUUID]").val(row.dictUUID);
			    	template.find("[name=enable]").val(row.enable);
			    	template.find("[textboxname=typeCode]").textbox("setValue",row.typeCode);
			    	template.find("[textboxname=typeName]").textbox("setValue",row.typeName);
			    	template.find("[textboxname=itemCode]").textbox("setValue",row.itemCode);
			    	template.find("[textboxname=itemName]").textbox("setValue",row.itemName);
			    	template.find("[textboxname=description]").textbox("setValue",row.description);
			    	$('#add_dialog').append(template);
			    },
			    buttons:[{
					text:'保存',
					iconCls: 'icon-ok',
					handler:function(){
						if(!template.form('validate')){
							$.messager.alert('提示','表单还没填完哦，请填完表单再提交!','warning');
							return;
						}
						$.post(
							'<%=basePath %>system/baseDict/saveOrUpDict',
							template.serialize(),
							function(data){
								if(data){
									$('#add_dialog').dialog("destroy");
									$('#dict_table').datagrid('reload');
								}else{
									$.messager.alert('提示','当前网络繁忙，请稍后重试!','info');
								}
							}
						);
					}
				},{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
				    	$('#add_dialog').dialog("destroy");
				    }
				}]
	    	});
		}
		
		function dictLook(rowIndex){
			$('#dict_table').datagrid('selectRow',rowIndex);
			var row = $('#dict_table').datagrid('getSelected');
			
			//创建模板
			var template = $('<form>\
				<table style="width:100%;">\
					<tr class="rowHeight">\
						<td>分类编号:</td>\
						<td>\
			            	<input name="typeCode" class="easyui-textbox" type="text" data-options="disabled:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>分类名称:</td>\
						<td>\
			            	<input name="typeName" class="easyui-textbox" type="text" data-options="disabled:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项编号:</td>\
						<td>\
			            	<input name="itemCode" class="easyui-textbox" type="text" data-options="disabled:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>字典项名称:</td>\
						<td>\
			            	<input name="itemName" class="easyui-textbox" type="text" data-options="disabled:true" style="width:280px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>排序:</td>\
						<td>\
			            	<input name="sort" class="easyui-textbox" type="text" data-options="disabled:true" style="width:280px;padding-left:130px;"></input>\
		            	</td>\
					</tr>\
					<tr class="rowHeight">\
						<td>描述信息:</td>\
						<td>\
			            	<input name="description" class="easyui-textbox" type="text" data-options="multiline:true,disabled:true" style="width:280px;height:60px;"></input>\
		            	</td>\
					</tr>\
			</form>');
			$.parser.parse(template);
			
			$("<div>").css({padding:"20px"}).dialog({
				id : "add_dialog",
	    		width : "420px",
	    		height : "400px",
	    		closable : false,
	    		modal : true,
	    		title : "新增数据字典",
			    onOpen : function(){
			    	template.find("[textboxname=typeCode]").textbox("setValue",row.typeCode);
			    	template.find("[textboxname=typeName]").textbox("setValue",row.typeName);
			    	template.find("[textboxname=itemCode]").textbox("setValue",row.itemCode);
			    	template.find("[textboxname=itemName]").textbox("setValue",row.itemName);
			    	template.find("[textboxname=sort]").textbox("setValue",row.sort);
			    	template.find("[textboxname=description]").textbox("setValue",row.description);
			    	$('#add_dialog').append(template);
			    },
			    buttons:[{
					text:'取消',
					iconCls: 'icon-cancel',
					handler:function(){
				    	$('#add_dialog').dialog("destroy");
				    }
				}]
	    	});
		}
		
		function queryEnableStatus(data){
			enableStatus = data.enableStatus;
			$("#enableStatus").html(data.statusText);
			$('#dict_table').datagrid('reload',{'enable':enableStatus});
		}
		
		function query(){
			var condition = $("#condition").val();
			$('#dict_table').datagrid('reload',{"enable":enableStatus,"typeCode":condition,"itemCode":condition,"typeName":condition,"itemName":condition,"description":condition});
		}
		function initQuery(){
			$('#condition').textbox('setValue','');
			$('#dict_table').datagrid('reload',{'enable':enableStatus});
		}
	</script>

