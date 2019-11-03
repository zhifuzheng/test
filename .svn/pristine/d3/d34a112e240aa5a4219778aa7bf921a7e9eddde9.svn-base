/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI panel 扩展
* jeasyui.extensions.panel.inlocale.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-12
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.base.isEasyUI.js
*   3、jeasyui.extensions.panel.position.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.panel.extensions");
    $.util.namespace("$.fn.window.extensions");
    $.util.namespace("$.fn.dialog.extensions");

    $.extend($.fn.panel.extensions, {
        onMove: $.fn.panel.defaults.onMove
    });

    $.extend($.fn.window.extensions, {
        onMove: $.fn.window.defaults.onMove
    });

    $.extend($.fn.dialog.extensions, {
        onMove: $.fn.dialog.defaults.onMove
    });


    function onMove(left, top) {
        var target = this,
            t = $(target),
            _isWindow = t.panel("isWindow"),
            _isDialog = t.panel("isDialog"),
            plugin = _isDialog ? "dialog" : (_isWindow ? "window" : "panel"),
            originalOnMove = $.fn[plugin].extensions.onMove;

        if ($.isFunction(originalOnMove)) {
            originalOnMove.apply(this, arguments);
        }

        var opts = t.panel("options");
        if (opts.maximized) {
            return t[plugin]("restore");
        }
        if (!opts.inlocale) {
            return;
        }

        var panel = t.panel("panel"),
            parent = panel.parent(),
            isRoot = parent.is("body"),
            scope = isRoot ? $.util.windowSize() : { width: parent.innerWidth(), height: parent.innerHeight() },
            width = $.isNumeric(opts.width) ? opts.width : panel.outerWidth(),
            height = $.isNumeric(opts.height) ? opts.height : panel.outerHeight(),
            flag = false;

        if (left < 0) {
            left = 0;
            flag = true;
        }
        if (top < 0) {
            top = 0;
            flag = true;
        }

        if (left + width > scope.width && left > 0) {
            left = scope.width - width;
            flag = true;
        }
        if (top + height > scope.height && top > 0) {
            top = scope.height - height;
            flag = true;
        }
        console.log(flag);
        if (flag) {
            return t[plugin]("move", { left: left, top: top });
        }
    }


    var methods = $.fn.panel.extensions.methods = {

    };

    var defaults = $.fn.panel.extensions.defaults = {

        //  扩展 easyui-panel/window/dialog 控件的自定义属性，表示该窗口是否无法移除父级对象边界，默认为 true。
        inlocale: true,

        //  重写 easyui-panel/window/dialog 控件的 onMove 事件，以支持自定义属性 inlocale 的功能。
        //  落阳注：虽然 move 方法和 onMove、left、top 属性定义在 easyui-panel 中，但 easyui-panel 并不支持 move 方法和 left、top 属性的使用。
        //      对 easyui-panel 对象调用 move 方法，也会触发 onMove 事件，也可以对 easyui-panel 设定 left 和 top 属性，但不会有实际效果。
        //      原因也很简单：easyui-panel 的 style 中缺少 position:absolute 的定义，从而导致 style 中 left 和 top 属性设定无效。
        //      上述属性和方法，是为 easyui-window、easyui-dialog 以及更复杂的派生组件准备的。
        onMove: onMove
    };

    $.extend($.fn.panel.defaults, defaults);
    $.extend($.fn.panel.methods, methods);

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.dialog.defaults, defaults);

    $.fn.window.extensions.defaults = $.extend({}, defaults);
    $.fn.dialog.extensions.defaults = $.extend({}, defaults);

})(jQuery);