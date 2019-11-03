/**
 * 用于初始化商品信息相关组件
 * @author wf
 **/
var baseURL = '/zhtc/';
(function($, owner) {
	// 编辑器参数
	owner.kingEditorParams = {
		//指定上传文件请求的url。
		uploadJson : baseURL + 'shop/item/fileupload',
		//限制多图片上传张数
		imageUploadLimit : 6,
		height : '500px',
		items : [
					'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'cut', 'copy', 'paste',
					'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
					'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
					'superscript', 'clearhtml', 'quickformat', 'selectall', '/',
					'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
					'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
					'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
					'anchor', 'link', 'unlink'
		       ]
	},
	
	//初始化单图片上传组件
    owner.initOnePicUpload = function(data,params){
		params = params || {};
    	var kingEditorParams = {};
    	$.extend(kingEditorParams,owner.kingEditorParams,params);
		
    	$(".onePicUpload").each(function(i,e){
    		var _ele = $(e);
    		var input = _ele.siblings("input");
    		if(data && data.itemImg){
    			input.val(data.itemImg);
    			input.after('<div class="pics"><ul><li><a href="javascript:void(0);" onclick="shop.itemImgWindow({\'url\':\''+baseURL+data.itemImg+'\'});"><img src="'+baseURL+data.itemImg+'" width="150" height="150"/></a></li></ul></div>');
    		}
	    	$(e).click(function(){
				KindEditor.editor(kingEditorParams).loadPlugin('image', function() {
					this.plugin.imageDialog({
						showRemote : false,
						clickFn : function(url, title, width, height, border, align) {
							input.parent().find("div.pics").remove();
							input.val(url.substring(url.indexOf('static')));
							input.after('<div class="pics"><ul><li><a href="javascript:void(0);" onclick="shop.itemImgWindow({\'url\':\''+url+'\'});"><img src="'+url+'" width="150" height="150"/></a></li></ul></div>');
							this.hideDialog();
						}
					});
				});
			});
    	});
    },
    
    // 初始化多图片上传组件
    owner.initPicUpload = function(selector,data,params){
    	params = params || {};
    	var kingEditorParams = {};
    	$.extend(kingEditorParams,owner.kingEditorParams,params);
    	
    	var _ele = $(selector);
		var input = _ele.siblings("input");
		if(data && data.imgList){
			input.val(data.imgList);
			var div = $('<div class="pics"><ul></ul></div>');
			var imgArray = data.imgList.split(",");
			for(var i = 0;i < imgArray.length;i++){
				var url = baseURL + imgArray[i];
				div.find("ul").append('<li><a href="javascript:void(0);" onclick="shop.imgWindow({\'url\':\''+url+'\'});"><img src="'+url+'" width="150" height="150"/></a></li>');
			}
			_ele.after(div);
		}
    	//给“上传图片按钮”绑定click事件
		_ele.click(function(){
    		//打开图片上传窗口
    		KindEditor.editor(kingEditorParams).loadPlugin('multiimage',function(){
    			var editor = this;
    			editor.plugin.multiImageDialog({
					clickFn : function(urlList) {
						input.parent().find("div.pics").remove();
						var div = $('<div class="pics"><ul></ul></div>');
						var imgArray = [];
						KindEditor.each(urlList, function(i, data) {
							var url = data.url;
							div.find("ul").append('<li><a href="javascript:void(0);" onclick="shop.imgWindow({\'url\':\''+url+'\'});"><img src="'+data.url+'" width="150" height="150"/></a></li>');
							url = url.substring(url.indexOf('static'));
							imgArray.push(url);
						});
						input.val(imgArray.join(","));
						input.after(div);
						editor.hideDialog();
					}
				});
    		});
    	});
    },
    
    // 初始化选择类目组件
    owner.initItemCat = function(data){
    	$(".selectItemCat").each(function(i,e){
    		var _ele = $(e);
    		if(data && data.catUUID && data.catName && data.catCode){
    			_ele.after("<span style='margin-left:10px;cursor:default;'>"+data.catName+"</span>");
    			_ele.parent().find("[name=catUUID]").val(data.catUUID);
    			_ele.parent().find("[name=catCode]").val(data.catCode);
				_ele.parent().find("[name=catName]").val(data.catName);
    		}else{
    			_ele.after("<span style='margin-left:10px;cursor:default;'></span>");
    		}
    		_ele.unbind('click').click(function(){
    			$("<div>").css({padding:"5px"}).html("<ul>")
    			.window({
    				width : '600',
    			    height : "500",
    			    modal : true,
    			    closed : true,
    			    title : '商品分类',
    			    onOpen : function(){
    			    	var _win = this;
    			    	$("ul",_win).tree({
    			    		url : baseURL+'shop/itemCat/findChildrenByParent',
    			    		queryParams: {'id':data.rootUUID},
    			    		animate : true,
    			    		method : "GET",
    			    		onClick : function(node){
    			    			if($(this).tree("isLeaf",node.target)){
    			                    var topCatName = owner.getTopCatName($(this), node);
    			                    if(data.rootName){
    			                    	topCatName = data.rootName + ">" + topCatName;
    			                    }
    			    				//填写到catUUID
    			    				_ele.parent().find("[name=catUUID]").val(node.id);
    			    				_ele.parent().find("[name=catCode]").val(node.catCode);
    			    				_ele.parent().find("[name=catName]").val(topCatName);
    			    				_ele.next().text(topCatName);
    			    				$(_win).window('destroy');
    			    				if(data && data.fun){
    			    					data.fun.call(this,node);
    			    				}
    			    			}
    			    		}
    			    	});
    			    },
    			    onClose : function(){
    			    	$(this).window("destroy");
    			    }
    			}).window('open');
    		});
    	});
    },
    
    //初始化富文本编辑器
    owner.createEditor = function(select,params){
    	params = params || {};
    	var kingEditorParams = {};
    	$.extend(kingEditorParams,owner.kingEditorParams,params);
    	return KindEditor.create(select, kingEditorParams);
    },
    
    //图片放大窗口
    owner.imgWindow = function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : '467px',
    		height : '507px',
    		modal:true,
    		title : "图片放大",
    		collapsible : false,
    		minimizable : false,
    		maximizable : false,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onOpen : function(){
		    	var img = '<img src="'+params.url+'" width="450px;" height="450px;">'
		    	$(this).html(img);
		    }
    	});
    },
    
    //图片放大窗口
    owner.itemImgWindow = function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : '467px',
    		height : '507px',
    		modal:true,
    		title : "图片放大",
    		collapsible : false,
    		minimizable : false,
    		maximizable : false,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onOpen : function(){
		    	var img = '<img src="'+params.url+'" width="450px;" height="450px;">'
		    	$(this).html(img);
		    }
    	});
    },
    
    //递归获取父节点名称，使用>分割
    owner.getTopCatName = function(tree, node){
    	var parent = tree.tree('getParent', node.target);
    	if(parent != null){
    		return owner.getTopCatName(tree, parent) + ">" + node.text;
    	}
        return node.text;
    }
    
}(jQuery, window.shop = {}));
