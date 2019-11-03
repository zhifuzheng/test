/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window 扩展
* jeasyui.extensions.window.autoRestore.js
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


    function initialize(target) {
        var state = $.data(target, "window");
        if (!state._inited) {
            initHeaderDblClick(target);
            state._inited = true;
        }
    }

    function initHeaderDblClick(target) {
        var t = $(target),
            header = t.window("header");
        header.unbind("dblclick.window-extensions").bind("dblclick.window-extensions", function () {
            var opts = t.window("options");
            if (opts.autoRestore) {
                if (opts.maximized) {
                    t.window("restore");
                } else if (opts.maximizable) {
                    t.window("maximize");
                }
            }
        });
    }


    var _window = $.fn.window;
    $.fn.window = function (options, param) {
        if (typeof options == "string") {
            return _window.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "window") ? true : false,
                opts = isInited ? options : $.extend({},
                    $.fn.window.parseOptions(this),
                    $.parser.parseOptions(this, [{
                        autoRestore: "boolean"
                    }]), options);
            _window.call(jq, opts, param);
            if (!isInited) {
                initialize(this);
            }
        });
    };
    $.union($.fn.window, _window);

    var methods = $.fn.window.extensions.methods = {

    };

    var defaults = $.fn.window.extensions.defaults = {

        //  扩展 easyui-window 以及 easyui-dialog 控件的自定义属性，表示该窗口是否在双击头部时自动 最大化 或 恢复原尺寸。
        autoRestore: true
    };

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.window.methods, methods);

})(jQuery);