/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combo 扩展
* jeasyui.extensions.combo.setPrompt.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-02-24
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.textbox.setPrompt.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combo.extensions");

    function setPrompt(target, prompt) {
        var t = $(target), state = $.data(target, "combo"), opts = state.options;
        opts.prompt = prompt;
        t.textbox("setPrompt", prompt);
    };


    var defaults = $.fn.combo.extensions.defaults = {

    };

    var methods = $.fn.combo.extensions.methods = {

        //  扩展 easyui-combo 组件的自定义方法；用于设置该 combo 的 textbox 输入框的 prompt(输入提示文字) 值；该方法定义如下参数：
        //      prompt: String 类型值，表示要被设置的 prompt 值；
        //  返回值：返回表示当前 easyui-combo 控件的 jQuery 链式对象。
        setPrompt: function (jq, prompt) { return jq.each(function () { setPrompt(this, prompt); }); }
    };


    $.extend($.fn.combo.defaults, defaults);
    $.extend($.fn.combo.methods, methods);

})(jQuery);