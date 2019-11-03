/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combo 扩展
* jeasyui.extensions.combo.destroy.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-02-15
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combo.extensions");

    $.extend($.fn.combo.extensions, {
        destroy: $.fn.combo.methods.destroy
    });

    function destroy(target) {
        var t = $(target), opts = t.combo("options");
        if ($.isFunction(opts.onBeforeDestroy) && opts.onBeforeDestroy.call(target) == false) { return; }
        $.fn.combo.extensions.destroy.call(target, t);
        if ($.isFunction(opts.onDestroy)) { opts.onDestroy.call(target); }
    };


    var defaults = $.fn.combo.extensions.defaults = {

        //  扩展 easyui-combo 组件的自定义事件；表示当调用 destroy 方法销毁当前 easyui-commbo 控件之前所触发的动作。
        //  如果该事件函数返回 false，将中断 destroy 方法的执行。
        onBeforeDestroy: function () { },

        //  扩展 easyui-combo 组件的自定义事件；表示当调用 destroy 方法销毁当前 easyui-commbo 控件之后所触发的动作。
        onDestroy: function () { }
    };

    var methods = $.fn.combo.extensions.methods = {

        //  重写 easyui-combo 组件的 destroy 方法，以支持相应扩展功能（onBeforeDestroy、onDestroy 事件）。
        destroy: function (jq) { return jq.each(function () { destroy(this); }); }
    };


    $.extend($.fn.combo.defaults, defaults);
    $.extend($.fn.combo.methods, methods);

})(jQuery);