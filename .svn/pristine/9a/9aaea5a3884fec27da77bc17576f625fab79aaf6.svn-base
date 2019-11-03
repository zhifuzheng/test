$('.business').click(function(){
	
	$.post(baseURL + 'front/common/findCommonList',{rows:100, page:1, comTypeCode:"16"}, function(data) {
		if(data && data != null && data != '[]'){
			var str ='';
			var data = data.rows;
			 if(data && data.length > 0) {
					for(var i = 0; i < data.length; i++) {
							str +='<div style="position:absolute;width:100px;height:100px;z-indent:2;left:90px;top:42px;color: white;">'+data[i].commonTitle+'</div>\
					<ul class="activeExp">\
					<li style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;'+data[i].commonContent+' </li>\
					</ul>';
					 } 
				 }
			$('.businessactiveExpBox').html(str);
		}else{
			alert('提示操作失败!');
		}
	},'json');
	
	$('.join, .profit, .joinprocess ').removeClass("click");
	$(this).addClass("click");
	$('#join, #profit, #joinprocess ').hide();
	$('#business').show();
})
$('.join').click(function(){
	
	$.post(baseURL + 'front/common/findCommonList',{rows:100, page:1, comTypeCode:"15"}, function(data) {
		if(data && data != null && data != '[]'){
			var str ='';
			var data = data.rows;
			 if(data && data.length > 0) {
					for(var i = 0; i < data.length; i++) {
							str +='<div style="position:absolute;width:100px;height:100px;z-indent:2;left:90px;top:42px;color: white;">'+data[i].commonTitle+'</div>\
					<ul class="activeExp">\
					<li style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;'+data[i].commonContent+' </li>\
					</ul>';
					 } 
				 }
			$('.joinactiveExpBox').html(str);
		}else{
			alert('提示操作失败!');
		}
	},'json');
	
	$('.business, .profit, .joinprocess ').removeClass("click");
	$(this).addClass("click");
	$('#business, #profit, #joinprocess ').hide();
	$('#join').show();
})
$('.profit').click(function(){
	
	$.post(baseURL + 'front/common/findCommonList',{rows:100, page:1, comTypeCode:"17"}, function(data) {
		if(data && data != null && data != '[]'){
			var str ='';
			var data = data.rows;
			 if(data && data.length > 0) {
					for(var i = 0; i < data.length; i++) {
							str +='<div style="position:absolute;width:100px;height:100px;z-indent:2;left:90px;top:42px;color: white;">'+data[i].commonTitle+'</div>\
					<ul class="activeExp">\
					<li style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;'+data[i].commonContent+' </li>\
					</ul>';
					 } 
				 }
			$('.profitactiveExpBox').html(str);
		}else{
			alert('提示操作失败!');
		}
	},'json');
	$('.join, .business, .joinprocess ').removeClass("click");
	$(this).addClass("click");
	$('#join, #business, #joinprocess ').hide();
	$('#profit').show();
})
$('.joinprocess').click(function(){
	
	$.post(baseURL + 'front/common/findCommonList',{rows:100, page:1, comTypeCode:"18"}, function(data) {
		if(data && data != null && data != '[]'){
			var str ='';
			var data = data.rows;
			 if(data && data.length > 0) {
					for(var i = 0; i < data.length; i++) {
							str +='<div style="position:absolute;width:100px;height:100px;z-indent:2;left:90px;top:42px;color: white;">'+data[i].commonTitle+'</div>\
					<ul class="activeExp">\
					<li style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;'+data[i].commonContent+' </li>\
					</ul>';
					 } 
				 }
			$('.joinprocessactiveExpBox').html(str);
		}else{
			alert('提示操作失败!');
		}
	},'json');
	
	$('.join, .profit, .business ').removeClass("click");
	$(this).addClass("click");
	$('#join, #profit, #business ').hide();
	$('#joinprocess').show();
})
