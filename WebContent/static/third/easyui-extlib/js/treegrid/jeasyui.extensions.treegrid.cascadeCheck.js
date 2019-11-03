/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.cascadeCheck.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-22
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.treegrid.rowState.js
*   3、jeasyui.extensions.treegrid.selectCheck.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.treegrid.extensions");







    function resetExtensionsEvent(t, opts) {
        var target = t[0];
        if (opts.cascadeCheck) {
            //重写 easyui-treegrid 的 onCheck、onUncheck 属性，以支持扩展功能。
            var _onCheck = opts.onCheck;
            opts.onCheck = function (row) {
                if ($.isFunction(_onCheck)) { _onCheck.apply(this, arguments); }
                if (opts.checkOnSelect && opts.singleSelect) { return; }
                var idField = opts.idField, id = row[idField], children, checked, parent = t.treegrid("getParent", id);
                while (parent) {
                    children = t.treegrid("getChildren", parent[idField]);
                    checked = t.treegrid("getChecked");
                    if (!$.array.some(children, function (val) { return !$.array.contains(checked, val); })) {
                        if (!t.treegrid("isChecked", parent[idField])) { t.treegrid("check", parent[idField]); }
                    }
                    parent = t.treegrid("getParent", parent[idField]);
                }
                $.each(t.treegrid("getChildren", id), function (i, n) {
                    if (!t.treegrid("isChecked", n[idField])) { t.treegrid("check", n[idField]); }
                });

            };
            var _onUncheck = opts.onUncheck;
            opts.onUncheck = function (row) {
                if ($.isFunction(_onUncheck)) { _onUncheck.apply(this, arguments); }
                if (opts.checkOnSelect && opts.singleSelect) { return; }
                var idField = opts.idField, id = row[idField], children, checked, parent = t.treegrid("getParent", id);
                while (parent) {
                    children = t.treegrid("getChildren", parent[idField]);
                    checked = t.treegrid("getChecked");
                    if (!$.array.some(children, function (val) { return $.array.contains(checked, val); })) {
                        if (t.treegrid("isChecked", parent[idField])) { t.treegrid("uncheck", parent[idField]); }
                    }
                    parent = t.treegrid("getParent", parent[idField]);
                }
                $.each(t.treegrid("getChildren", id), function (i, n) {
                    t.treegrid("uncheck", n[idField]);
                });
            };
        }
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "treegrid"),
            opts = state.options;
    }

    function initializeExtensionsBefore(target, opts) {
        var t = $(target);

        resetExtensionsEvent(t, opts);
    }

    var _treegrid = $.fn.treegrid.extensions._treegrid = $.fn.treegrid;
    $.fn.treegrid = function (options, param) {
        if (typeof options == "string") {
            return _treegrid.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "treegrid") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.treegrid.parseOptions(this),
                        $.parser.parseOptions(this, [
                            { cascadeCheck: "boolean" }
                        ]), options);
            if (!isInited) {
                //由于部分事件（如onClickRow）的重写必须在初始化之前才有效，因此需提前处理
                initializeExtensionsBefore(this, opts);
            }
            _treegrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.treegrid, _treegrid);

    var defaults = $.fn.treegrid.extensions.defaults = {

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件是否支持级联选择；
        //  Boolean 类型值，默认为 false。
        //  备注：在当 checkOnSelect、singleSelect 这两个属性都为 true 的情况下，不支持级联选择，此时属性 cascadeCheck 无效；
        cascadeCheck: false
    };

    var methods = $.fn.treegrid.extensions.methods = {


    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);