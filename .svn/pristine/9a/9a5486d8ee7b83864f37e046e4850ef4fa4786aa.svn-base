/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton 扩展
* jeasyui.extensions.linkbutton.setText.js
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


    function setText(target, text) {
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options,
            btnText = t.find(".l-btn-text");
        if (text) {
            btnText.removeClass("l-btn-empty").text(text);
        } else {
            btnText.addClass("l-btn-empty").html(" ");
        }
        opts.text = text;
    };


    var defaults = $.fn.linkbutton.extensions.defaults = {};

    var methods = $.fn.linkbutton.extensions.methods = {

        //  扩展 easyui-linkbutton 控件的自定义方法；设置 linkbutton 按钮的文字；该方法定义如下参数：
        //      text:   String 类型值，表示要设置的新的按钮文本内容
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setText: function (jq, text) { return jq.each(function () { setText(this, text); }); }
    };


    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);