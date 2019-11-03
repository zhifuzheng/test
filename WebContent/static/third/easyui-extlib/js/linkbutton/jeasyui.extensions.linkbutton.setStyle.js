/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton 扩展
* jeasyui.extensions.linkbutton.setStyle.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-17
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.linkbutton.extensions");


    function initialize(target) {
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options,
            exts = opts.extensions ? opts.extensions : opts.extensions = {};
        if (!exts._initialized) {
            setStyle(target, opts.style);
            exts._initialized = true;
        }
    };

    function setStyle(target, style) {
        if (!style) { return; }
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options;
        if (style) {
            t.css(style);
            $.extend(opts.style ? opts.style : (opts.state = {}), style);
        }
    };

    var _linkbutton = $.fn.linkbutton;
    $.fn.linkbutton = function (options, param) {
        if (typeof options == "string") {
            return _linkbutton.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var t = $(this),
                isInited = $.data(this, "linkbutton") ? true : false,
                opts = isInited ? options : $.extend({},
                    $.fn.linkbutton.parseOptions(this),
                    $.parser.parseOptions(this, ["tooltip"]),
                    options
                );
            _linkbutton.call(t, opts);
            if (!isInited) {
                initialize(this);
            }
        });
    };
    $.union($.fn.linkbutton, _linkbutton);


    var defaults = $.fn.linkbutton.extensions.defaults = {
        //  扩展 easyui-linkbutton 控件的自定义属性；表示 linkbutton 按钮的自定义样式。
        //  JSON-Object 类型
        style: null
    };

    var methods = $.fn.linkbutton.extensions.methods = {

        //  扩展 easyui-linkbutton 控件的自定义方法；设置 linkbutton 按钮的自定义样式；该方法定义如下参数：
        //      style:   JSON-Object 类型，表示要设置的按钮的样式
        //  返回值：返回表示当前 easyui-linkbutton 控件的 jQuery 链式对象；
        setStyle: function (jq, style) { return jq.each(function () { setStyle(this, style); }); }
    };


    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);