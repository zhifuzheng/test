/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI textbox 扩展
* jeasyui.extensions.textbox.setButtonText.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-02
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.linkbutton.setText.js
*   3、jeasyui.extensions.textbox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.textbox.extensions");

    function setButtonText(target, buttonText) {
        var t = $(target),
            state = $.data(target, "textbox"),
            opts = state.options,
            tb = state.textbox,
            btn = tb.find(".textbox-button");
        opts.buttonText = buttonText;

        if (btn.length) {
            btn.linkbutton("setText", buttonText);
        } else {
            $("").prependTo(tb).linkbutton({
                text: opts.buttonText, iconCls: opts.buttonIcon
            });
            t.textbox("readonly", opts.readonly)
        }
        t.textbox("resize");
    }


    var defaults = $.fn.textbox.extensions.defaults = {};

    var methods = $.fn.textbox.extensions.methods = {

        // 扩展 easyui-textbox 控件的自定义方法；设置 easyui-textbox 控件的 button 按钮文本；
        // 该方法的参数 buttonText 表示被设置的按钮文本内容；
        // 返回值：返回表示当前 easyui-textbox 控件的 jQuery 链式对象。
        setButtonText: function (jq, buttonText) { return jq.each(function () { setButtonText(this, buttonText); }); }
    };

    $.extend($.fn.textbox.defaults, defaults);
    $.extend($.fn.textbox.methods, methods);

})(jQuery);