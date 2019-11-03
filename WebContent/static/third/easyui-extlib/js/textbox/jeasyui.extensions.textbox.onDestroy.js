/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI textbox 扩展
* jeasyui.extensions.textbox.onDestroy.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-04
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.textbox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.textbox.extensions");

    $.extend($.fn.textbox.extensions, {
        destroy: $.fn.textbox.methods.destroy
    });

    function destroy(target) {
        var t = $(target),
            state = $.data(target, "textbox"),
            opts = state.options;
        if (opts.onBeforeDestroy.call(target) == false) {
            return;
        }
        $.fn.textbox.extensions.destroy.call(target, t);
        opts.onDestroy.call(target);
    }

    var defaults = $.fn.textbox.extensions.defaults = {

        // 扩展 easyui-textbox 的自定义事件；该事件表示在执行 destroy 方法之前进行的操作；
        // 如果该方法返回 false，则表示取消 destroy 方法的执行。
        onBeforeDestroy: function () { },

        // 扩展 easyui-textbox 的自定义事件；
        // 该事件表示在执行 destroy 方法之后进行的操作；
        onDestroy: function () { }
    };

    var methods = $.fn.textbox.extensions.methods = {

        // 重写 easyui-textbox 控件的 destroy 方法，以支持相应扩展功能（onBeforeDestroy、onDestroy 事件）。
        destroy: function (jq) { return jq.each(function () { destroy(this); }); }
    };

    $.extend($.fn.textbox.defaults, defaults);
    $.extend($.fn.textbox.methods, methods);

})(jQuery);