/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI textbox 扩展
* jeasyui.extensions.textbox.setButtonIcon.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-30
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.linkbutton.setIcon.js
*   3、jeasyui.extensions.textbox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.textbox.extensions");

    function setButtonIcon(target, buttonIcon) {
        var t = $(target),
            state = $.data(target, "textbox"),
            opts = state.options,
            tb = state.textbox,
            btn = tb.find(".textbox-button");
        opts.buttonIcon = buttonIcon;

        if (btn.length) {
            btn.linkbutton("setIcon", buttonIcon);
        } else {
            $("").prependTo(tb).linkbutton({
                text: opts.buttonText, iconCls: opts.buttonIcon
            });
            t.textbox("readonly", opts.readonly);
        }
        t.textbox("resize");
    }


    var defaults = $.fn.textbox.extensions.defaults = {};

    var methods = $.fn.textbox.extensions.methods = {

        // 扩展 easyui-textbox 控件的自定义方法；设置 easyui-textbox 控件的 button 按钮图标；
        // 该方法的参数 buttonIcon 表示被设置的按钮图标样式类；
        // 返回值：返回表示当前 easyui-textbox 控件的 jQuery 链式对象。
        setButtonIcon: function (jq, buttonIcon) { return jq.each(function () { setButtonIcon(this, buttonIcon); }); }
    };

    $.extend($.fn.textbox.defaults, defaults);
    $.extend($.fn.textbox.methods, methods);

})(jQuery);