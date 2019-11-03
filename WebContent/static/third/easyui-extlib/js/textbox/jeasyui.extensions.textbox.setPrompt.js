/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI textbox 扩展
* jeasyui.extensions.textbox.setPrompt.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-04
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.validatebox.js
*   3、jeasyui.extensions.textbox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.textbox.extensions");


    function setPrompt(target, prompt) {
        var state = $.data(target, "textbox"),
            opts = state.options,
            tb = state.textbox,
            box = tb.find(".textbox-text");
        opts.prompt = prompt;
        box.validatebox("setPrompt", prompt);
        //此时页面上显示的prompt仍旧是上一次的信息，需要获得焦点后才会更新，为保证用户体验，再此自动获得焦点后在取消焦点
        box.focus().blur();
    }

    var defaults = $.fn.textbox.extensions.defaults = {};

    var methods = $.fn.textbox.extensions.methods = {

        // 扩展 easyui-textbox 控件的自定义方法；设置 easyui-textbox 控件的 prompt 属性；该属性表示表单输入控件在空值状态下的文字提示信息。
        // 该方法的参数 prompt 表示被设置的 prompt 值。
        // 返回值：返回表示当前 easyui-textbox 控件的 jQuery 链式对象。
        setPrompt: function (jq, prompt) { return jq.each(function () { setPrompt(this, prompt); }); }
    };

    $.extend($.fn.textbox.defaults, defaults);
    $.extend($.fn.textbox.methods, methods);

})(jQuery);