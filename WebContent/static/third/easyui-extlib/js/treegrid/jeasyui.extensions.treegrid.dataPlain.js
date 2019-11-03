/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.dataPlain.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-21
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.tree.dataPlain.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.treegrid.extensions");

    $.extend($.fn.treegrid.extensions, {
        _loadFilter: $.fn.treegrid.defaults.loadFilter
    });


    function resetExtensionsEvent(t, opts) {
        var target = t[0],
            state = $.data(target, "treegrid");
        if (opts.dataPlain) {
            //重写 easyui-treegrid 的 loadFilter 属性，以支持平滑数据格式。
            var _loadFilter = opts.loadFilter;
            opts.loadFilter = function (data, parent) {
                if ($.isFunction($.fn.treegrid.extensions._loadFilter)) { data = $.fn.treegrid.extensions._loadFilter.apply(this, arguments); }
                if ($.isFunction(_loadFilter)) { data = _loadFilter.apply(this, arguments); }

                var isArray = $.array.likeArray(data) && !$.util.isString(data), rows = isArray ? data : data.rows;
                if (!rows || !rows.length) { return data; }

                rows = opts.dataPlain ? $.fn.tree.extensions.dataPlainConverter(rows, opts) : rows;
                if (parent != null && parent != undefined) { return isArray ? rows : { total: rows.length, rows: rows }; }
                return isArray ? rows : { total: data.length || rows.length, rows: rows };
            };
        }
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "treegrid"),
            opts = state.options;

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
                            "parentField",
                            { dataPlain: "boolean" }
                        ]), options);
            _treegrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.treegrid, _treegrid);


    var defaults = $.fn.treegrid.extensions.defaults = {

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件是否支持平滑数据格式。
        //  当支持平滑数据格式时，数据元素中不需要通过指定 children 来指定子节点，而是支持通过 pid 属性来指示其父级节点。
        //  Boolean 类型值，默认为 false。
        dataPlain: false,

        //  扩展 easyui-treegrid 的自定义属性，表示当前 easyui-treegrid 控件支持平滑数据格式时，用哪个 field 表示当前行数据的父级节点 idField 值
        //  String 类型值，默认为 "pid"。
        parentField: "pid"
    };

    var methods = $.fn.treegrid.extensions.methods = {

    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);