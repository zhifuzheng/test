/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combo 扩展
* jeasyui.extensions.combo.setRequired.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-05
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combo.extensions");

    function setRequired(target, required) {
        var t = $(target), opts = t.combo("options"), textbox = t.combo("textbox");
        opts.required = textbox.validatebox("options").required = required;
        t.combo("validate");
    };


    var defaults = $.fn.combo.extensions.defaults = {};

    var methods = $.fn.combo.extensions.methods = {

        //  扩展 easyui-combo 组件的自定义方法；用于启用或者禁用 easyui-combo 控件的非空验证功能，该方法定义如下参数：
        //      required:   Boolean 类型的值，表示启用或者禁用 easyui-combo 控件的非空验证功能。
        //  返回值：返回表示当前 easyui-combo 控件的 jQuery 链式对象。
        setRequired: function (jq, required) { return jq.each(function () { setRequired(this, required); }); }
    };


    $.extend($.fn.combo.defaults, defaults);
    $.extend($.fn.combo.methods, methods);

})(jQuery);