<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div id="button-bar" style="padding: 10px 15px;">
	<label>状态：</label>
	<select id="stop" name="stop" class="easyui-combobox" editable=false; style="width:200px;height:32px">
	    <option value="">请选择</option>
	    <option value="0">启用</option>
	    <option value="1">停用</option>
	</select>
	<label>标题：</label>
	<input class="easyui-textbox easyui-" type="text" style="width:200px" data-options="prompt:'请输入标题内容'" name="commodityTitle" id="commodityTitle" maxlength="20" size="20"/>			
    <a href="javascript:void(0)" onclick="query()" class='easyui-linkbutton' iconcls='icon-search' data-options="iconCls:'icon-add'" style="background-color:#1b8cf2;color: #fff;">&nbsp;搜索</a>
</div>
<table id="jb_table" class="easyui-datagrid">
	<thead>
		<tr>
			<th data-options="field:'id',align:'center',width:50,hidden:true">id</th>
			<th data-options="field:'vipLogin',align:'center',width:100, formatter:vipLogin">会员头像</th>
			<th data-options="field:'vipName',align:'center',width:200">会员名称</th>
			<th data-options="field:'menuName',align:'center',width:200">分类名称</th>
			<th data-options="field:'commodityTitle',align:'center',width:200">商品标题</th>
			<th data-options="field:'commodityImg',align:'center',width:100, formatter:commodityImg">商品图片</th>
			<th data-options="field:'CZ',width:220,align:'center',formatter:clsListF">操作</th>
		</tr>
	</thead>
</table>

<!-- 显示图片 -->
<div id='shoImg' style="text-align:center;">
	<img alt="" src="" id="setImg">
</div>


<script type="text/javascript">
	var path = "<%=basePath%>";
	var selectRow = null;
	$(function() {
		$('#jb_table').datagrid({
			title: '&nbsp;&nbsp;供需商品管理',
			url: '<%=path %>/Public/sp/commodityAll',
			rownumbers: true,
			pagination: true,
			singleSelect: false,
			nowrap:false,
			onUnselect: function(index, row) {
				//
			}
		});
	});

	//格式化操作
	function clsListF(value, row, index) {
		var stop = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="stopOpen(\'' + row.commodityUUID + '\',\'' + row.stop + '\')">&nbsp;停用&nbsp;</a>';
		var enable = '<a href="javascript:void(0);" class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true" onclick="stopOpen(\'' + row.commodityUUID + '\',\'' + row.stop + '\')">&nbsp;启用&nbsp;</a>';
		if(row.stop == 0){
			return stop;
		}else if(row.stop == 1){
			return enable;
		}
	}
	
	//会员头像
	function vipLogin(value, row, index){
		return "<img style='width:24px;height:24px;' border='1' src='"+row.vipLogin+"'/>";
	}
	
	//商品图片
	function commodityImg(value, row, index){
		var img = "";
		var imgArr = row.commodityImg.split(",");
		for(var i = 0; i < imgArr.length; i++){
			img = path+imgArr[i];
			break;
		}
		return "<img style='width:24px;height:24px;' border='1' title='点击查看图片' onclick=imgOpen(\""+img+"\") src='"+img+"'/>";
	}
	function imgOpen(e){
		$('#shoImg').dialog({
		    title: '图片',
		    width: 800,
			height: 400,
		    resizable:true,
		    closed: false,
		    cache: false,
		    modal: true
		});
		$("#setImg").attr("src",e);
	}

	
	//搜索
	function query(){
		var stop = $("#stop").val();
		var commodityTitle = $("#commodityTitle").val();
		$("#jb_table").datagrid("load",{
			stop:stop,
			commodityTitle:commodityTitle
		});
	}
	
	//停用
	function stopOpen(uuid,stop){
		var msg = "";
		var state = "0";
		if(stop == 0){
			stop = "1";
			msg = "确认停用吗？";
		}else if(stop == 1){
			stop = "0";
			msg = "确认启用吗？";
		}
		$.messager.confirm("操作提醒", msg, function(c) {
			if(c) {
				var url = "<%=path%>/Public/sp/supplyStop";
				$.post(url, {uuid:uuid,state:state,stop:stop}, function(data) {
					if(data) {
						$.messager.alert('提示', '操作成功!', 'info');
						//刷新
						$('#jb_table').datagrid('reload');
					} else {
						$.messager.alert('提示', '操作失败!', 'error');
					}
				}, 'json');
			}
		});
	}
	
</script>