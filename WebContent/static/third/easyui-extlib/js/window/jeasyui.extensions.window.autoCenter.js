/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window 扩展
* jeasyui.extensions.window.autoCenter.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-14
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
$(function () {

    //  设置当屏幕大小调整时，所有 easyui-window 或 easyui-dialog 窗口在属性 hcenter: true 或 vcenter: true 的情况下自动居中。
    $(window).resize(function () {
        $(".window-body").each(function () {
            var t = $(this),
                state = $.data(this, "window");
            if (!state || !state.options) {
                return;
            }
            var opts = t.window("options");
            if (opts.draggable) {
                if (opts.hcenter || opts.vcenter) {
                    var method = opts.hcenter && opts.vcenter
                        ? "center"
                        : (opts.hcenter ? "hcenter" : "vcenter");
                    t.window(method);
                } else if (opts.inlocale) {
                    t.window("move");
                }
            }
        });
    });

});


(function ($) {

    $.util.namespace("$.fn.window.extensions");

    var methods = $.fn.window.extensions.methods = {

    };

    var defaults = $.fn.window.extensions.defaults = {

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口对象是否在屏幕大小调整的情况下自动进行左右居中，默认为 false。
        hcenter: false,

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口对象是否在屏幕大小调整的情况下自动进行上下居中，默认为 false。
        vcenter: false
    };

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.window.methods, methods);

})(jQuery);