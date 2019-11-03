/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.setIcon.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-21
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tree.extensions");

    function setNodeIcon(target, param) {
        if (!param || !param.target || !param.iconCls) { return; }
        $(target).tree("update", { target: param.target, iconCls: param.iconCls })
    };

    var defaults = $.fn.tree.extensions.defaults = {

    };

    var methods = $.fn.tree.extensions.methods = {

        //  扩展 easyui-tree 的自定义方法；设置指定节点的图标；该方法定义如下参数：
        //      param: JSON-Object 类型值，该对象包含如下属性定义：
        //          target: 表示要设置图标的 easyui-tree node HTML-DOM 对象；
        //          iconCls:表示要设置的节点样式；
        //  返回值：返回表示当前 easyui-tree 组件的 jQuery 对象。
        setIcon: function (jq, param) { return jq.each(function () { setNodeIcon(this, param); }); }
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);