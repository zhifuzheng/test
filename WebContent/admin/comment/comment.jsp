<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

	<table id="comment_table"></table>
	<div id="comment_tb" style="padding:2px 5px;">
		<input class="easyui-combobox" name="entityName" id="entityName" data-options="'prompt':'请输入店铺名称'" style="width:280px;" />
		<a class="easyui-menubutton" data-options="menu:'#controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="status">评论状态</span></a>		
		<input id="condition" name="condition" class="easyui-textbox" data-options="prompt:'请输入商品名称/评论内容/评分/买家名称'" style="width:280px;">				
        <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="query()" data-options="'plain':true">&nbsp;搜索</a>
   		<a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="initQuery()" data-options="'plain':true">&nbsp;清除</a>
	</div>
	<div id="controll-panel" style="width: 130px; display: none;">
		<div onclick="queryStatus({'status':'1','statusText':'正常'});">正常</div>
		<div onclick="queryStatus({'status':'','statusText':'全部'});">全部</div>
		<div class="menu-sep"></div>
		<div onclick="queryStatus({'status':'2','statusText':'违规'});">违规</div>
	</div>
	
	<script type="text/javascript">
	//默认评论状态
	var status = "1";
	
 	// 定义列
	var columns = [ [ {
		field : 'payerLogo',
		title : '买家头像',
		width : 43,
		formatter : function(data,row,index){
			var index = data.indexOf('static');
			if(index != -1){
				data = '<%=basePath %>'+data.substr(index);
			}
			return '<img style="width:100px;height:100px;margin-left:-15px;" src="'+data+'"/>';
		}
	},{
		field : 'payerName',
		title : '买家名称',
		width : 60,
		align : 'center'
	},{
		field : 'entityName',
		title : '店铺名称',
		width : 60,
		align : 'center'
		
	},{
		field : 'title',
		title : '商品名称',
		width : 50,
		align : 'center'
	},{
		field : 'imgList',
		title : '评论图片',
		width : 170,
		align : 'left',
		formatter : function(data,row,index){
			var html = "";
			if(data){
				var list = data.split(",");
				for(var i = 0;i < list.length;i++){
					html += '<img style="width:100px;height:100px;" src="<%=basePath %>'+list[i]+'"/>';
				}
			}else{
				html = "没有评论图片";
			}
			return html;
		}
	},{
		field : 'comment',
		title : '评论内容',
		width : 100,
		align : 'left'
	},{
		field : 'score',
		title : '评分',
		width : 30,
		align : 'center'
	},{
		field : 'status',
		title : '状态',
		width : 20,
		align : 'center',
		formatter : function(data,row,index){
			return data == 1?"正常":"违规";
		}
	},{
		field : 'operation',
		title : '操作',
		width : 50,
		align : 'center',
		formatter : function(data,row,index){
			var html = "";
			if(row.status == "1"){
				//info primary warning success danger
				html = '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="upComment(\''+index+'\',\'2\');">&nbsp;违规&nbsp;</a>';
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="delComment(\''+index+'\');">&nbsp;删除&nbsp;</a>';
			}
			if(row.status == "2"){
				html = '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="upComment(\''+index+'\',\'1\');">&nbsp;撤销&nbsp;</a>';
				html += '&nbsp;&nbsp;<a class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="delComment(\''+index+'\');">&nbsp;删除&nbsp;</a>';
			}
			return html;
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
		$('#comment_table').datagrid({
			url: '<%=path %>/shop/comment/findCommentPage',
			queryParams: {'status':status},
			iconCls: 'icon-model-detail',
			title: '&nbsp;&nbsp;评论管理', 
			columns : columns,
			toolbar: '#comment_tb',
	        rownumbers: true,
	        pagination: true,
	        pageList : [10,20,30,40],
			fit : true,
			fitColumns : true,
			nowrap : false,
	        singleSelect: true,
	        emptyMsg: "<span style='font-size:18px;color:#C6C4C4;'>没有查询到评论信息哦</span>"
		});
	});

    //删除评论信息
	function delComment(rowIndex){
		$('#comment_table').datagrid('selectRow',rowIndex);
		var row = $('#comment_table').datagrid('getSelected');
    	$.messager.confirm("操作提醒","确定要删除该记录吗？不可恢复，建议使用违规操作",function(c){
    		if(c){
    			$.post(
					baseURL+"shop/comment/delComment",
					{commentUUID:row.commentUUID},
					function(data){
						if(data.status == "1"){
							$('#comment_table').datagrid('reload');
						}else{
							$.messager.alert('提示',data.msg,'warning');
						}
					},
					'json'
				);
    		}
    	});
	}
    
	 //修改评论状态
	function upComment(rowIndex, status){
		$('#comment_table').datagrid('selectRow',rowIndex);
		var row = $('#comment_table').datagrid('getSelected');
    	var msg = "确定该评论违规吗？";
    	if (status == 1) {
    		msg = "确定要撤销违规吗？";
    	}
    	$.messager.confirm("操作提醒",msg,function(c){
    		if(c){
    			$.post(
   					baseURL+"shop/comment/upComment",
   					{commentUUID:row.commentUUID,status:status},
   					function(data){
   						if(data.status == "1"){
   							//更新行记录
   							row.status = status;
   							$('#comment_table').datagrid('updateRow',{
   							    index : rowIndex,
   							    row : JSON.stringify(row)
   							});
   						}else{
   							$.messager.alert('提示',data.msg,'warning');
   						}
   					},
   					'json'
   				);
    		}
    	});
	}
	
	function queryStatus(data){
		status = data.status;
		$("#status").html(data.statusText);
		$('#comment_table').datagrid('reload',{'status':status});
	}
	
	function query(){
		var entityName = $("#entityName").combobox('getValue');
		var condition = $("#condition").val().trim();
		//使用正则表达式判断字符串是否为数字
		var reg = /^(\-)?\d+(\.\d+)?$/;
		if(reg.test(condition) && condition.length == 1 && condition > 0 && condition <= 5){
			$('#comment_table').datagrid('reload',{'status':status,'entityName':entityName,'score':condition});
			return;
		}
		$('#comment_table').datagrid('reload',{'status':status,'entityName':entityName,'title':condition,'comment':condition,'payerName':condition});
	}
	
	function initQuery(){
		$("#entityName").combobox('setValue','');
		$("[textboxname=condition]").textbox('setValue','');
		$('#comment_table').datagrid('reload',{'status':status});
	}
	</script>

