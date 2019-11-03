/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI linkbutton 扩展
* jeasyui.extensions.linkbutton.tooltip.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-10
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.linkbutton.extensions");


    function setTooltip(target, tooltip) {
        var t = $(target), opts = t.linkbutton("options"), isFunc = $.isFunction(tooltip);
        opts.tooltip = tooltip;
        if (opts.tooltip) {
            var topts = { content: !isFunc ? tooltip : null };
            if (isFunc) {
                $.extend(topts, {
                    onShow: function (e) {
                        $(this).tooltip("update", tooltip.call(target, e));
                    }
                });
            }
            t.tooltip(topts);
        }
        if (opts.tooltip == false) {
            t.tooltip("destroy");
        }
    };

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "linkbutton"),
            opts = state.options;
        setTooltip(target, opts.tooltip);
    };

    var _linkbutton = $.fn.linkbutton;
    $.fn.linkbutton = function (options, param) {
        if (typeof options == "string") {
            return _linkbutton.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this), hasInit = $.data(this, "linkbutton") ? true : false,
                opts = hasInit ? options : $.extend({}, $.fn.linkbutton.parseOptions(this), $.parser.parseOptions(this, [
                    "tooltip"
                ]), options);
            _linkbutton.call(jq, opts);
            if (!hasInit) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.linkbutton, _linkbutton);


    var defaults = $.fn.linkbutton.extensions.defaults = {

        //  扩展 easyui-linkbutton 控件的自定义属性；表示 linkbutton 按钮鼠标放置提示。
        //      String 类型或 Function 类型，
        tooltip: null
    };

    var methods = $.fn.linkbutton.extensions.methods = {

        //  扩展 easyui-linkbutton 控件的自定义方法；设置 linkbutton 按钮的 tooltip 属性；该方法定义如下参数：
        //      tooltip: String 类型或 Function 类型，表示要设置的按钮的 prompt 属性值；如果该参数值为 false，则表示销毁该按钮的 easyui-tooltip 效果；
        //  返回值：返回表示当前 easyui-linkbutton 的 jQuery 链式对象。
        setTooltip: function (jq, tooltip) { return jq.each(function () { setTooltip(this, tooltip); }) }
    };

    $.extend($.fn.linkbutton.defaults, defaults);
    $.extend($.fn.linkbutton.methods, methods);

})(jQuery);