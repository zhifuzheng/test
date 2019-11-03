/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton 扩展
* jeasyui.extensions.linkbutton.setSize.js
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


    function setSize(target, size) {
        if (!size) { return; }
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options;
        t.removeClass("l-btn-small l-btn-large").addClass("l-btn-" + size);
        opts.size = size;
    };


    var defaults = $.fn.linkbutton.extensions.defaults = {};

    var methods = $.fn.linkbutton.extensions.methods = {

        //  扩展 easyui-linkbutton 控件的自定义方法；设置 linkbutton 按钮的 size 属性；该方法定义如下参数：
        //      size:   String 类型，表示要设置的按钮的 size 属性值；该参数限定取值 'small','large'
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setSize: function (jq, size) { return jq.each(function () { setSize(this, size); }); }
    };


    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);