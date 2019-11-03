<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String vipUUID = request.getParameter("vipUUID");
	String permissionStr = "nmqedu";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>权限管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" href="static/css/pintuer.css">
<link rel="stylesheet" href="static/css/admin.css">

<link type="image/x-icon" href="static/images/logo.png"
	rel="shortcut icon" />
<link href="static/images/favicon.ico" rel="bookmark icon" />

<script src="static/js/jquery.js"></script>
<script src="static/js/pintuer.js"></script>
<script src="static/js/respond.js"></script>
<script src="static/js/admin.js"></script>
<script src="static/js/layer.js"></script>
<!-- multiselect -->
<link rel="stylesheet" href="static/multiselect/multi-select.css"
	type="text/css">
<script src="static/multiselect/jquery.quicksearch.js"></script>
<!-- 搜索处理js -->
<script src="static/multiselect/jquery.multi-select.js"></script>

</head>

<body>
	<div class="admin">
		<div class="panel admin-panel">
			<form id="menuTreeForm" method="post">
				<div class="panel-head">
					<strong>权限分配</strong>
				</div>
				<div class="container">
				<select id='selectMenuUser' multiple='multiple'>
				</select>
				</div>
			</form>
			<div class="panel-foot text-center"></div>
		</div>
		<br>
	</div>
	<br>
	<form id="vipPermissionForm" class="form-inline">
		<input type="hidden" id="vipUUID" name="vipUUID" /> 
		<input type="hidden" id="menuUUIDS" name="menuUUIDS" />
	</form>



	<div class="hidden">
		<script type="text/javascript">
			$(function (){
				setMultiSelectVal("<%=vipUUID%>");
			});
			
		//设置multiselect的值
		function setMultiSelectVal(vipUUID){
		    //查所有菜单信息
	    	var url = "<%=path%>/system/menuVip/findMenuToAddPermission?vipUUID="+vipUUID;
	    	var htmlOption = "";
	    	$.post(url,null,function(data){
				if(data!="[]"){
					for(var i=0;i<data.length;i++){
						htmlOption += "<option value='"+data[i].menuUUID+"' "+ data[i].selected +">"+data[i].menuName+"</option>";
					}
				}
				$("#selectMenuUser").html(htmlOption);
				initMultiSelect("selectMenuUser");
	    	},"json");
		}
		//初始化MultiSelect
		function initMultiSelect(selectID){
			$("#"+selectID).multiSelect({
				  selectableHeader: "<input type='text' class='search-input' autocomplete='off' placeholder='输入名称查找'>",
				  selectionHeader: "<input type='text' class='search-input' autocomplete='off' placeholder='输入名称查找'>",
				  afterInit: function(ms){
				  var that = this,
			        $selectableSearch = that.$selectableUl.prev(),
			        $selectionSearch = that.$selectionUl.prev(),
			        selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
			        selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';
				    that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
				    .on('keydown', function(e){
				      if (e.which === 40){
				        that.$selectableUl.focus();
				        return false;
				      }
				    });
				    that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
				    .on('keydown', function(e){
				      if (e.which == 40){
				        that.$selectionUl.focus();
				        return false;
				      }
				    });
				  },
				  afterSelect: function(){
				    this.qs1.cache();
				    this.qs2.cache();
				  },
				  afterDeselect: function(){
				    this.qs1.cache();
				    this.qs2.cache();
				  }
				});
		}
		function validate() {
			if (!$('#selectMenuUser').val()) {
				layer.msg('请选菜单分配权限！', {
					icon : 2
				});
				return false;
			}
			return true;
		}
		//初始化数据
		function getFromSerialize() {
			return $('#vipPermissionForm').serialize();
		}
		//修改数据时
		function initData() {
			$('#vipUUID').val("<%=vipUUID%>");
			$('#menuUUIDS').val($("#selectMenuUser").val());
		}
		</script>
	</div>
</body>
</html>
