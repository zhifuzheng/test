/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window 扩展
* jeasyui.extensions.window.closeOnEsc.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-15
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.window.extensions");


    $(function () {
        //  在当前打开 modal:true 的 easyui-window 或者 easyui-dialog 时，按 ESC 键关闭顶层的 easyui-window 或者 easyui-dialog 对象。
        $(document).keydown(function (e) {
            if (e.which != 27) {
                return;
            }

            var items = $("div.window-mask:visible");
            if (!items.length) {
                return;
            }
            var item = $.array.max(items, function (a, b) {
                return $(a).css("zindex") - $(b).css("zindex");
            })
            $(item).prevAll("div.panel.window:first").children(".window-body").each(function () {
                if (!$.data(this, "window")) {
                    return;
                }
                var t = $(this),
                    opts = t.window("options");
                if (opts && opts.closable && opts.closeOnEsc && !t.window("header").find(".panel-tool a").attr("disabled")) {
                    t.window("close");
                }
            });
        });
    });

    var methods = $.fn.window.extensions.methods = {

    };

    var defaults = $.fn.window.extensions.defaults = {

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口为模式窗口时，是否在按下 ESC 时关闭该窗口，默认为 true。
        //  落阳注：要使该自定义属性有效，window/dialog 的options中，modal 属性和 closable 属性必须为 true 。
        closeOnEsc: true
    };

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.window.methods, methods);

})(jQuery);