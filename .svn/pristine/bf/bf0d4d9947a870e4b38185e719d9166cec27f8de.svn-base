/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI validatebox 扩展
* jeasyui.extensions.validatebox.setRequired.js
* 开发 落阳
* 最近更新：2015-10-29
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.validatebox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.validatebox.extensions");

    function setRequired(target, required) {
        var state = $.data(target, "validatebox"),
            opts = state.options;
        opts.required = required;
        $(target).validatebox("validate");
    }

    var defaults = $.fn.validatebox.extensions.defaults = {

    };

    var methods = $.fn.validatebox.extensions.methods = {

        // 扩展 easyui-validatebox 控件的自定义方法；设置 easyui-validatebox 控件的 required 属性；该属性表示表单输入值是否允许为空。
        // 该方法的参数 required 表示被设置的 bool 类型值。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象。
        setRequired: function (jq, required) { return jq.each(function () { setRequired(this, required); }); }
    };

    $.extend($.fn.validatebox.defaults, defaults);
    $.extend($.fn.validatebox.methods, methods);

})(jQuery);