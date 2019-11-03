/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton 扩展
* jeasyui.extensions.linkbutton.setIconAlign.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-17
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.linkbutton.extensions");


    function setIconAlign(target, iconAlign) {
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options,
            left = t.find(".l-btn-left");
        left.removeClass("l-btn-icon-left l-btn-icon-right l-btn-icon-top l-btn-icon-bottom").addClass("l-btn-icon-" + iconAlign);
        opts.iconAlign = iconAlign;
    }


    var defaults = $.fn.linkbutton.extensions.defaults = {};

    var methods = $.fn.linkbutton.extensions.methods = {

        //  扩展 easyui-linkbutton 控件的自定义方法；设置 linkbutton 按钮的图标位置；该方法定义如下参数：
        //      iconAlign:   String 类型值，表示要设置的按钮的图标位置；该参数限定取值 "left"、"right"、"top"、"bottom"
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setIconAlign: function (jq, iconAlign) { return jq.each(function () { setIconAlign(this, iconAlign); }); }
    };


    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);