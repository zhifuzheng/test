var baseURL = '/zhtc/';
var baseURL1 = '/zhtc';

function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) return unescape(r[2]);
	return null;
}

function doLoginOut() {
	 var mymessage=confirm("您确认要注销当前登录用户吗？");
		if (mymessage==true){
			$.ajax({
				type: "get",
				async: false,
				url: baseURL + 'user/removeSession.action?logintoken=' + localStorage.getItem('logintoken'),
				dataType: "jsonp", 
				success: function(data) {
					if(data){
						if ('1' == data[0].success) {
							window.location.replace(baseURL +"front/signin.html");
						} else {
							alert(data.message);
						}
					}
				},
				error: function(msg) {
					alert("操作失败")
					console.log(msg);
				}
			});
		}
}

/**
 * 初始化ZTree
 * hshzh 封装
 * treeId 树的id
 * url 请求地址
 * addHoverDom 添加按钮的回调方法，参数为（treeId, treeNode）返回false则不显示添加按钮
 * zTreeBeforeEditName 修改按钮的回调方法，参数为（treeId, treeNode）
 * zTreeOnClick 单击节点的回调方法，参数为（event, treeId, treeNode）
 * zTreeBeforeRemove  删除按钮的回调方法，参数为（treeId, treeNode）
 * setRenameBtn 是否设置修改按钮的回调方法，参数为（treeId, treeNode），返回false则不显示修改按钮
 * setRemoveBtn 是否设置删除按钮的回调方法，参数为（treeId, treeNode），返回false则不显示删除按钮
 */
function initZTree(treeId,url,addHoverDom,zTreeBeforeEditName,zTreeOnClick,zTreeBeforeRemove,setRenameBtn,setRemoveBtn) {
		var setting = {
			     check: {
			          enable: true,
			          chkStyle: "checkbox",
			          chkboxType: { "Y": "", "N": "" }/**（1）Y、N、"p"和“s”说明 Y 属性定义 checkbox 被勾选后的情况；
			           											 N 属性定义 checkbox 取消勾选后的情况；  
																"p" 表示操作会影响父级节点；  
																"s" 表示操作会影响子级节点。

																（2）chkboxType: { "Y": "s", "N": "ps" }
																        表示checkbox勾选操作，只影响子节点；取消勾选操作，影响父子节点*/
			     },
		         view: {
		        	 addDiyDom: null,//用于在节点上固定显示用户自定义控件 function addDiyDom(treeId, treeNode),数据量大时影响性能
		             addHoverDom: addHoverDom,//用于当鼠标移动到节点上时，显示用户自定义控件(目前只自定义了添加按钮)，显示隐藏状态同 zTree 内部的编辑、删除按钮
		             removeHoverDom: removeHoverDom,//用于当鼠标移出节点时，隐藏用户自定义控件，显示隐藏状态同 zTree 内部的编辑、删除按钮
		             selectedMulti: false,//设置是否允许同时选中多个节点。
		             autoCancelSelected: false,//点击节点时，按下 Ctrl 或 Cmd 键是否允许取消选择操作。
		             dblClickExpand: true,//双击节点时，是否自动展开父节点的标识
		             expandSpeed: "normal",//节点展开、折叠时的动画速度，三种预定速度之一的字符串("slow", "normal", or "fast")
		             fontCss : {},//个性化文字样式，只针对 在节点上显示的<a>对象。默认值：{},{color:"red"},也可以是回调函数function setFontCss(treeId, treeNode) {return treeNode.level == 0 ? {color:"red"} : {};};
		             showIcon: true,//设置是否显示节点的图标,也可以是回调函数
		             showLine: true//是否显示节点之间的连线
		         },
		         callback : {
		        	 beforeAsync: null,//用于捕获异步加载之前的事件回调函数
		        	 beforeCheck: null,//用于捕获 勾选 或 取消勾选 之前的事件回调函数，并且根据返回值确定是否允许 勾选 或 取消勾选
		        	 beforeClick: null,//用于捕获单击节点之前的事件回调函数，并且根据返回值确定是否允许单击操作
		        	 beforeCollapse: null,//用于捕获父节点折叠之前的事件回调函数，并且根据返回值确定是否允许折叠操作
		        	 beforeDblClick: null,//用于捕获鼠标双击之前的事件回调函数，并且根据返回值确定触发 onDblClick 事件回调函数
		        	 beforeDrag: null,//用于捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作
		        	 beforeDragOpen: null,//用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作
		        	 beforeDrop: null,//用于捕获节点拖拽操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作
		        	 beforeEditName : zTreeBeforeEditName,//用于捕获节点编辑按钮的 click 事件，并且根据返回值确定是否允许进入名称编辑状态
		        	 onClick: zTreeOnClick,//用于捕获节点被点击的事件回调函数
		        	 beforeRemove : zTreeBeforeRemove//用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作
		         },
		         async: {
			     		enable: true,//是否开启异步加载模式
			     		url:url,//Ajax获取数据的 URL地址
			     		type:"post",//Ajax的http请求模式(get,post)
			     		dataType:"json",//Ajax获取的数据类型(text,json)
			     		contentType: "application/json",//Ajax 提交参数的数据类型
		     	 },
		         data: {
		             simpleData: {
		                 enable: true//数据是否采用简单数据模式
		             }
		         },
		         edit: {
		             enable: true,//设置zTree是否处于编辑状态
		             removeTitle: "删除",//删除按钮的 Title 辅助信息
		 			 renameTitle: "修改",//编辑名称按钮的 Title 辅助信息
		 			 showRemoveBtn: setRemoveBtn,//设置是否显示删除按钮
		 			 showRenameBtn: setRenameBtn//设置是否显示修改按钮
		         }
		     };
		    function removeHoverDom(treeId, treeNode) {
	           $("#addBtn_"+treeNode.tId).unbind().remove();
	        };
			return $.fn.zTree.init($("#"+treeId),setting);
}
