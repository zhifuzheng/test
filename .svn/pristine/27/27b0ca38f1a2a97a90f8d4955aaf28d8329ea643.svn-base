/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window bug修复
* jeasyui.fixeds.window.cls.js
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
            initCls(target);
            state._inited = true;
        }
    }

    function initCls(target) {
        var t = $(target),
            opts = t.window("options"),
            header = t.window("header"),
            body = t.window("body");

        if (opts.bodyCls) {
            body.addClass(opts.bodyCls);
        }
        if (opts.headerCls) {
            header.addClass(opts.headerCls);
        }
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
                    $.fn.window.parseOptions(this), options);
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

    };

    $.extend($.fn.window.defaults, defaults);
    $.extend($.fn.window.methods, methods);

})(jQuery);