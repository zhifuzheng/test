<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String slideUUID = request.getParameter("slideUUID");
%>
<style>
	.heading{
		display: block;
		width: 90px;
		text-align-last: justify;
		padding-left: 15px;
	}
</style>
<form id="img_form" method="post">
	<input type="hidden" name="id" id="id">
	<input type="hidden" name="slideUUID" id="slideUUID" value="<%= slideUUID%>">
	<table data-options="fit:true" style="width:100%;margin-top: 5px;border-collapse: separate;border-spacing: 5px;">
		<tr>
			<td align="right">
				<label class="heading">标题：</label>
			</td>
			<td>
				<input class="easyui-textbox easyui-" type="text" style="width:480px" name="title" id="title" maxlength="20" size="20"/>
			</td>
		</tr>
		
		<tr>
			<td align="right">
				<label class="heading">幻灯片:</label>
			</td>
			<td>
				<input id="slide" name="slide" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoSlide" style="display: none;">
			<td align="right">
				<label class="heading">幻灯片:</label>
			</td>
			<td>
				<img src="" id="slideImg" width="480px;" height="150px;">
			</td>
		</tr>
		
		<tr>
			<td align="right">
				<label class="heading">详情:</label>
			</td>
			<td>
				<input id="details" name="details" type="text" style="width:480px" accept="image/*">
			</td>
		</tr>
		<tr id="shoDetails" style="display: none;">
			<td align="right">
				<label class="heading">详情:</label>
			</td>
			<td>
				<img src="" id="detailsImg" width="480px;" height="150px;">
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
var path = "<%=basePath%>";//图片路径
var bole = 0;
	//选择文件
	$('#slide').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	//选择文件
	$('#details').filebox({
	    buttonText: '选择图片',
	    buttonAlign: 'left'
	})
	
	$(function(){
		var slideUUID = "<%=slideUUID%>";
		if("" != slideUUID){
			var url="<%=path%>/Public/business/slideImgId?slideUUID=<%=slideUUID%>";
			$.post(url,{},function(data){
				$("#id").val(data.id);
				$("#title").textbox("setValue",data.title);
				$("#slideImg").attr("src",path+data.slide);
				$("#detailsImg").attr("src",path+data.details);
				$("#shoSlide").show();
				$("#shoDetails").show();
			},'json');
		}
	})
	
	function slideSave(){
		alert(bole);
		if($('#title').val() == ""){
			$.messager.alert('提示',"请输入标题",'error');
		}else if("<%=slideUUID%>" == "" && "" == $("#slide").filebox('getValue')){
			$.messager.alert('提示',"请选择幻灯片",'error');
		}else if(bole == 1){
			$.messager.alert('提示',"图片格式为1024*410,请重新选择",'error');
		}else if("<%=slideUUID%>" == "" && "" == $("#details").filebox('getValue')){
			$.messager.alert('提示',"请选择详情图片",'error');
		}else{
			$("#img_form").ajaxSubmit({
				url:'<%=path%>/Public/business/slideSave',
				type:'post',
				dataType:'json',
				semantic:true,
				success : function(data){
					$.messager.alert('提示',data.msg,'info');
					$('#gradeList_addGrade_div').dialog('close');
					$("#gradeList_addGrade_div").remove();
					//刷新列表
					$('#img_table').datagrid('reload');
				},
			});
		}
	}
	
	//图片判断宽高
	$(":input[name='slide']").change(function(){
	   var file = this.files[0];
	   if (window.FileReader) {    
            var reader = new FileReader();    
            reader.readAsDataURL(file);    
            //监听文件读取结束后事件    
            reader.onloadend = function (e) {
            $(".img").attr("src",e.target.result);    //e.target.result就是最后的路径地址
               var image = new Image();
               image.onload = function () {
                    var width = this.width;
                    var height = this.height;
					if(width != 1024 && height != 410){
						$.messager.alert('提示','图片格式为1024*410,请重新选择','error');
						bole = 1;
						return false;
					}else{
						bole = 0;
					}
                };
              image.src=e.target.result;   //需要注意的是：src 属性一定要写到 onload 的后面，否则程序在 IE 中会出错。
            };    
       } 
	});
	
	//显示幻灯片
	$(":input[name='slide']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('slideImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "1024";
	        img.height =  "410";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoSlide").show();
	});
	
	
	//显示详情图片
	$(":input[name='details']").change(function(){
		var reader = new FileReader();
		var file =this;
		var img = document.getElementById('detailsImg');
		//读取File对象的数据
		reader.onload = function(evt){
			//data:img base64 编码数据显示
			img.width  =  "300";
	        img.height =  "800";
			img.src = evt.target.result;
		}
	    reader.readAsDataURL(file.files[0]);
	    $("#shoDetails").show();
	});
	
	
</script>