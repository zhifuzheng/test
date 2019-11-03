<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
	<style>
		.img-width-100 {
			width: 100%;
			height: 100%;
			display: block;
		}
		.sc_zjz {
			width:100%;
			text-align:center;
			padding:0px;
		}
		.list_s_none {
			list-style:none;
		}
		.borbot {
			border-bottom:1px solid #efeff4;
		}
		.backfff {
			background:#fff;background-color:#fff!important;
		}
		.height30 {
			height:30px;line-height:30px;
		}
		.paddinglr {
			padding:0px 10px;
		}
		.fonts13 {
			font-size:13px;
		}
		.hidden {
			display:none;
		}
		.radioSpan {
	      position: relative;
	      border: 0 solid #95B8E7;
	      background-color: #fff;
	      vertical-align: middle;
	      display: inline-block;
	      overflow: hidden;
	      font-size:24px;
	      white-space: nowrap;
	      margin: 0;
	      padding: 0;
	      -moz-border-radius: 5px 5px 5px 5px;
	      -webkit-border-radius: 5px 5px 5px 5px;
	      border-radius: 5px 5px 5px 5px;
	      display:block;
	    }
	    .explain{
	    	height: 40px;
	    	line-height: 40px;
	    	font-size: 14px;
	    	font-weight: bold;
	    }
	</style>	
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west'" style="width:50%;">
			<table id="realName_table"></table>
			<div id="realName_tb" style="padding:2px 5px;">		
				<a class="easyui-menubutton" data-options="menu:'#controll-panel',iconCls:'icon-help'" style="width:130px;"><span id="auditStatus">审核状态</span></a>
				<input id="condition" data-options="'prompt':'请输入想要搜索的信息'" class="easyui-textbox" style="width:200px;">				
	            <a href="javascript:void(0)" class="easyui-linkbutton color-blue" onclick="query()"  data-options="'plain':true"><i class="icon iconfont icon-search"></i>&nbsp;搜索</a>
			</div>
			<div id="controll-panel" style="width: 130px; display: none;">
				<div onclick="queryAuditStatus({'auditStatus':'2','statusText':'待审核'});">待审核</div>
				<div onclick="queryAuditStatus({'auditStatus':'4','statusText':'已通过'});">已通过</div>
				<div onclick="queryAuditStatus({'auditStatus':'5','statusText':'未通过'});">未通过</div>
				<div onclick="queryAuditStatus({'auditStatus':'','statusText':'全部'});">全部</div>
				<div onclick="queryAuditStatus({'auditStatus':'1','statusText':'未提交'});">未提交</div>
				<div onclick="queryAuditStatus({'auditStatus':'3','statusText':'审核中'});">审核中</div>
				<div class="menu-sep"></div>
				<div onclick="myAuditTask();">我的审核任务</div>
				<div onclick="giveupAudit();">放弃审核任务</div>
			</div>
		</div>
		<div data-options="region:'center',title: '实名信息/审核意见',iconCls:'icon-inbox-document'">
	   		<ul>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*姓名：&nbsp;<span id="idCardName"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*是否是职业跑步运动员：&nbsp;<span id="isAthlete"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*性别：&nbsp;<span id="idCardSex"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*民族：&nbsp;<span id="idCardMinority"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*身份证出生日期：&nbsp;<span id="idCardBirthday"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*身份证号码：&nbsp;<span id="idCardNumber"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="mui-col-xs-2 fonts13">*身份证住址：&nbsp;<span id="idCardAddr"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*身份证有限期限：&nbsp;<span id="idCardExpire"></span></div>
	   			</li>
	   		
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*现居住城市：&nbsp;<span id="liveProvCity"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*详细地址：&nbsp;<span id="liveDetailAddr"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">工作单位：&nbsp;<span id="workEnterprise"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">工作地址：&nbsp;<span id="workAddr"></span></div>
	   			</li>
	   			<li class="list_s_none borbot backfff height30 paddinglr fonts13">
	   				<div class="fonts13">*常用邮箱：&nbsp;<span id="email"></span></div>
	   			</li>
	   			<li>
	   				<div class="list_s_none borbot backfff height30 paddinglr fonts13">*QQ：&nbsp;<span id="qq"></span></div>
	   			</li>
	   			<li>
	   				<div class="list_s_none borbot backfff height30 paddinglr fonts13">*手机号码：&nbsp;<span id="mobile"></span></div>
	   			</li>
	   			<li id="showOpinion" class="list_s_none borbot backfff paddinglr hidden">
					<p class="sc_zjz fontco4 explain">审批意见</p>
					<textarea style="width:100%;height:150px;" id="auditorOpinion" class="fonts13"></textarea>
				</li>
	   		</ul>
	   		<div class="control" style="height:50px;text-align:center;vertical-align:center;">
	   			<span id="auditInfoStatus" class="radioSpan">
	   				<input type="radio" name="auditInfoStatus" value="4" onclick="toggleArea();"><span style="color:green">通过</span>&nbsp;&nbsp;&nbsp;
   					<input type="radio" name="auditInfoStatus" value="5" onclick="toggleArea();"><span style="color:red">驳回</span>
	   			</span>
			</div>
			<div class="control" onclick="commitAudit();" style="height:50px;text-align:center;vertical-align:center;background: #92ca7b;line-height:50px;font-size:20px;border-radius:6px;color:#fff;cursor:pointer;">
	   			提交审核
			</div>
		</div>
		<div data-options="region:'east',title: '身份证证件照',iconCls:'icon-camera',collapsible:false" style="width:25%;">
			<ul>
				<li class="list_s_none borbot backfff paddinglr">
					<p class="sc_zjz fontco4 explain">*身份证正面照</p>	
					<img id="idCardPositive" class="img-width-100" src="../static/img/audit/1.jpg"/>
				</li>
				<li class="list_s_none borbot backfff paddinglr">
					<p class="sc_zjz fontco4 explain">*手持身份证正面照</p>
					<img id="idCardHand" class="img-width-100" src="../static/img/audit/3.jpg"/>
				</li>
				<li class="list_s_none borbot backfff paddinglr">
					<p class="sc_zjz fontco4 explain">*身份证反面照</p>
					<img id="idCardOppositive" class="img-width-100" src="../static/img/audit/2.jpg"/>
				</li>
			</ul>
			<div class="control" style="height:50px;text-align:center;vertical-align:center;">
	   			<span id="idCardStatus" class="radioSpan">
	   				<input type="radio" name="idCardStatus" value="4" onclick="toggleArea();"><span style="color:green">通过</span>&nbsp;&nbsp;&nbsp;
   					<input type="radio" name="idCardStatus" value="5" onclick="toggleArea();"><span style="color:red">驳回</span>
	   			</span>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		//默认审核状态
		var auditStatus = "2";
		// 定义列
		var columns = [ [ {
			field : 'idCardName',
			title : '姓名',
			width : 40,
			align : 'center'
		},{
			field : 'loginName',
			title : '申请账号',
			width : 50,
			align : 'center'
		},{
			field : 'createTime',
			title : '申请时间',
			width : 50,
			align : 'center'
		},{
			field : 'auditorName',
			title : '审核账号',
			width : 50,
			align : 'center'
		},{
			field : 'auditStatus',
			title : '审核状态',
			width : 40,
			align : 'center',
			formatter : function(data,row,index){
				//审核状态：1未提交，2待审核，3审核中，4审核通过，5审核不通过
				if(data == 1){
					return "未提交";
				}
				if(data == 2){
					return "待审核";
				}
				if(data == 3 || data == 'commit'){
					return "审核中";
				}
				if(data == 4){
					return "已通过";
				}
				if(data == 5){
					return "未通过";
				}
			}
		},{
			field : 'operation',
			title : '操作',
			width : 40,
			align : 'center',
			formatter : function(data,row,index){
				if(row.auditStatus == "1"){
					return '<a class="easyui-linkbutton button-info button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'请等待会员将所有认证资料提交后方可审核！\',\'type\':\'info\'});">&nbsp;信息&nbsp;</a>';
				}
				if(row.auditStatus == "2"){
					return '<a id="beginAudit" class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="beginAudit(\''+index+'\');">&nbsp;审核&nbsp;</a>';
				}
				if(row.auditStatus == "3"){
					if(row.auditorUUID == '${ userinfo.userUUID}'){
						return '<a id="beginAudit" class="easyui-linkbutton button-primary button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="beginAudit(\''+index+'\');">&nbsp;审核&nbsp;</a>';
					}
					return '<a id="beginAudit" class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'审核员 '+row.auditorName+' 正在审核该条记录\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
				}
				if(row.auditStatus == "4"){
					return '<a class="easyui-linkbutton button-success button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'已经认证通过的会员不可再审核！\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
				}
				if(row.auditStatus == "5"){
					return '<a class="easyui-linkbutton button-danger button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'请等待会员重新将所有认证资料提交后方可审核！\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
				}
				if(row.auditStatus == 'commit'){
					return '<a id="beginAudit" class="easyui-linkbutton button-warning button-xs l-btn l-btn-small" data-options="plain: true, iconCls: \'icon-remove\'" onclick="tips({\'title\':\'提示\',\'msg\':\'您正在审核该条记录，请选择通过或者驳回！\',\'type\':\'warning\'});">&nbsp;信息&nbsp;</a>';
				}
			}
		} ] ];
		
		$(function(){
			//初始化实名认证数据表格
			$('#realName_table').datagrid({
				iconCls: 'icon-shield-on',
				title: '&nbsp;&nbsp;会员实名认证（单击表格查看，待审核状态可开启审核）',
				columns : columns,
				url : '<%=basePath %>audit/vip/findRealNamePage',
				queryParams: {'auditStatus':auditStatus},
				toolbar : '#realName_tb',
				rownumbers : true,
				pagination : true,
				pageList: [10,20,30,40,50],
				fit : true,
				fitColumns : true,
				nowrap : false,
				idField : 'id',
				singleSelect: true,
				onSelect : doSelect
			});
			
		});
		function doSelect(rowIndex,rowData){
			//初始化审核控件
			if(rowData.auditStatus == "commit"){
				$(".control").show();
				if($("#showOpinion").hasClass("hidden")){
					$("#showOpinion").removeClass("hidden");
				}
			}else{
				$(".control").hide();
				if(!$("#showOpinion").hasClass("hidden")){
					$("#showOpinion").addClass("hidden");
				}
			}
			//初始化实名认证信息
			if(rowData.idCardName != null){
				$("#idCardName").html(rowData.idCardName);
			}else{
				$("#idCardName").html("");
			}
			if(rowData.isAthlete != null){
				$("#isAthlete").html(rowData.isAthlete == "1"?"是":"否");
			}else{
				$("#isAthlete").html("");
			}
			if(rowData.idCardSex != null){
				$("#idCardSex").html(rowData.idCardSex == "1"?"男":"女");
			}else{
				$("#idCardSex").html("");
			}
			if(rowData.idCardMinority != null){
				$("#idCardMinority").html(rowData.idCardMinority);
			}else{
				$("#idCardMinority").html("");
			}
			if(rowData.idCardBirthday != null){
				$("#idCardBirthday").html(rowData.idCardBirthday);
			}else{
				$("#idCardBirthday").html("");
			}
			if(rowData.idCardNumber != null){
				$("#idCardNumber").html(rowData.idCardNumber);
			}else{
				$("#idCardNumber").html("");
			}
			if(rowData.idCardAddr != null){
				$("#idCardAddr").html(rowData.idCardAddr);
			}else{
				$("#idCardAddr").html("");
			}
			if(rowData.idCardExpire != null){
				$("#idCardExpire").html(rowData.idCardExpire);
			}else{
				$("#idCardExpire").html("");
			}
			if(rowData.liveProvCity != null){
				$("#liveProvCity").html(rowData.liveProvCity);
			}else{
				$("#liveProvCity").html("");
			}
			if(rowData.liveDetailAddr != null){
				$("#liveDetailAddr").html(rowData.liveDetailAddr);
			}else{
				$("#liveDetailAddr").html("");
			}
			if(rowData.workEnterprise != null){
				$("#workEnterprise").html(rowData.workEnterprise);
			}else{
				$("#workEnterprise").html("");
			}
			if(rowData.workAddr != null){
				$("#workAddr").html(rowData.workAddr);
			}else{
				$("#workAddr").html("");
			}
			if(rowData.qq != null){
				$("#qq").html(rowData.qq);
			}else{
				$("#qq").html("");
			}
			if(rowData.email != null){
				$("#email").html(rowData.email);
			}else{
				$("#email").html("");
			}
			if(rowData.mobile != null){
				$("#mobile").html(rowData.mobile);
			}else{
				$("#mobile").html("");
			}
			//初始化身份证证件照
			if(rowData.idCardPositive != null){
				$("#idCardPositive").attr("src",'<%=basePath %>'+rowData.idCardPositive);
			}else{
				$("#idCardPositive").attr("src",'<%=basePath %>'+'static/img/audit/1.jpg');
			}
			if(rowData.idCardHand != null){
				$("#idCardHand").attr("src",'<%=basePath %>'+rowData.idCardHand);
			}else{
				$("#idCardHand").attr("src",'<%=basePath %>'+'static/img/audit/3.jpg');
			}
			if(rowData.idCardOppositive != null){
				$("#idCardOppositive").attr("src",'<%=basePath %>'+rowData.idCardOppositive);
			}else{
				$("#idCardOppositive").attr("src",'<%=basePath %>'+'static/img/audit/2.jpg');
			}
		}
		
		//初始化审核控件
		$(".control").hide();
		//开启审核
		function beginAudit(rowIndex){
			$('#realName_table').datagrid('selectRow',rowIndex);
			var row = $('#realName_table').datagrid('getSelected');
			$.ajax({
				type:"post",
				url:'<%=basePath %>audit/vip/beginRealName',
				data:{'realNameUUID':row.realNameUUID},
				async:false,
				dataType:'json',
				success:function(data){
					if(data.status == "1"){
						$('#realName_table').datagrid('updateRow',{
						    index: rowIndex,
						    row: {
						        'auditStatus' : 'commit'
						    }
						});
					}else{
						$.messager.alert("提示", data.msg, "warning");
						$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
					}
				}
			});
			//控制审核控件的显示与隐藏
			$(".control").show();
			if(row.auditInfoStatus == 4){
				$("#auditInfoStatus").html('<span style="color:green">实名信息已通过</span>');
			}
			if(row.idCardStatus == 4){
				$("#idCardStatus").html('<span style="color:green">身份证证件照已通过</span>');
			}
		}
		//控制审核意见文本框的隐藏和显示
		function toggleArea(){
			var row = $('#realName_table').datagrid('getSelected');
			//获取审核信息
			var auditInfoStatus = row.auditInfoStatus;
			var idCardStatus = row.idCardStatus;
			if($("input[name='auditInfoStatus']").length>0){
				auditInfoStatus = $("input[name='auditInfoStatus']:checked").val();
			}
			if($("input[name='idCardStatus']").length>0){
				idCardStatus = $("input[name='idCardStatus']:checked").val();
			}
			if(idCardStatus == 4 && auditInfoStatus ==4){
				$("#auditorOpinion").val("审核通过");
				if(!$("#showOpinion").hasClass("hidden")){
					$("#showOpinion").addClass("hidden");
				}
			}else{
				$("#auditorOpinion").val("");
				if($("#showOpinion").hasClass("hidden")){
					$("#showOpinion").removeClass("hidden");
				}
			}
		}
		//提交审核
		function commitAudit(){
			var row = $('#realName_table').datagrid('getSelected');
			//获取审核信息
			var auditInfoStatus = row.auditInfoStatus;
			var idCardStatus = row.idCardStatus;
			if($("input[name='auditInfoStatus']").length>0){
				auditInfoStatus = $("input[name='auditInfoStatus']:checked").val();
			}
			if($("input[name='idCardStatus']").length>0){
				idCardStatus = $("input[name='idCardStatus']:checked").val();
			}
			var auditorOpinion = $.trim($("#auditorOpinion").val());
			if(auditInfoStatus == undefined){
				$.messager.alert("提示", "请审核实名信息", "warning");
				return;
			}
			if(idCardStatus == undefined){
				$.messager.alert("提示", "请审核身份证证件照", "warning");
				return;
			}
			if(auditorOpinion == ""){
				$.messager.alert("提示", "请填写审核意见", "warning");
				return;
			}
			if(!$("#showOpinion").hasClass("hidden")){
				$("#showOpinion").addClass("hidden");
			}
			$(".control").hide();
			$.ajax({
				type:"post",
				url:'<%=basePath %>audit/vip/commitRealName',
				data:{'realNameUUID':row.realNameUUID,'idCardStatus':idCardStatus,'auditInfoStatus':auditInfoStatus,'auditorOpinion':auditorOpinion},
				dataType:'json',
				success:function(data){
					if(data.status == "1"){
						$.messager.alert("提示", "审核提交成功！", "info");
						$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
					}else{
						$.messager.alert("提示", data.msg, "warning");
					}
				}
			});
		}
		
		//我的审核任务
		function myAuditTask(){
			$("#auditStatus").html("我的审核任务");
			$('#realName_table').datagrid('reload',{'auditorUUID':'${ sessionScope.userinfo.userUUID}','auditStatus':'3'});
		}
		
		//放弃审核任务
		function giveupAudit(){
			$.messager.confirm("操作提醒", "确定要放弃所有的审核任务吗？", function (c) {
				if(c){
					$("#auditStatus").html("我的审核任务");
					$.ajax({
						type:"post",
						url:'<%=basePath %>audit/vip/giveupRealName',
						dataType:'json',
						success:function(data){
							if(data.status == "1"){
								$('#realName_table').datagrid('reload',{'auditorUUID':'${ sessionScope.userinfo.userUUID}','auditStatus':'3'});
							}
						}
					});
				}
			});
		}
		
		//会员提示信息
		function tips(content){
			$.messager.alert(content.title, content.msg, content.type);
		}
		
		function queryAuditStatus(data){
			auditStatus = data.auditStatus;
			$("#auditStatus").html(data.statusText);
			$('#realName_table').datagrid('reload',{'auditStatus':auditStatus});
		}
		
		function query(){
			var condition = $("#condition").val();
			$('#realName_table').datagrid('reload',{"auditStatus":auditStatus,"loginName":condition,"idCardMemo":condition,"idCardName":condition,"idCardAddr":condition,"liveProvCity":condition,"liveDetailAddr":condition,"workEnterprise":condition,"workAddr":condition,"auditorName":condition});
		}
		
	</script>