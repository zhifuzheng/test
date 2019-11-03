/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.highlightColumn.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-12-02
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.getDom.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    var highlightColumn000000 = function (target, field) {
        if (!field) { return; }
        var t = $(target);
        var state = $.data(t[0], "datagrid"), opts = state.options;
        if (state.highlightField) {
            t.datagrid("getColumnDom", { field: state.highlightField, header: true }).removeClass("datagrid-row-over");
        }
        t.datagrid("getColumnDom", { field: field, header: true }).filter(function () {
            return !$(this).parent().hasClass("datagrid-row-selected");
        }).addClass("datagrid-row-over");
        state.highlightField = field;
    };

    function highlightColumn(target, field) {
        var t = $(target),
            state = $.data(target, "datagrid");
        if (state.highlightField) {
            t.datagrid("getColumnDom", { field: state.highlightField, header: true }).removeClass("datagrid-row-over");
        }
        t.datagrid("getColumnDom", { field: field, header: true }).removeClass("datagrid-row-over").filter(function () {
            return !$(this).closest("tr.datagrid-row").is(".datagrid-row-selected");
        }).addClass("datagrid-row-over");
        state.highlightField = field;
    }

    function unhighlightColumn(target, field) {
        var t = $(target),
            state = $.data(target, "datagrid");
        if (state.highlightField) {
            t.datagrid("getColumnDom", { field: state.highlightField, header: true }).removeClass("datagrid-row-over");
        }
        t.datagrid("getColumnDom", { field: field, header: true }).removeClass("datagrid-row-over");
        state.highlightField = undefined;
    }





    // 初始化单元格鼠标事件
    function initRowCellMouseEvent(t, opts) {
        t.datagrid("getPanel").panel("body").delegate("tr.datagrid-header-row>td[field],tr.datagrid-row>td[field]", {
            "mouseenter.datagrid-extensions": function () {
                if (!opts.autoHighlightColumn) {
                    return;
                }
                var target = t[0],
                    td = $(this),
                    field = td.attr("field");
                highlightColumn(target, field);
            },
            "mouseleave.datagrid-extensions": function () {
                if (!opts.autoHighlightColumn) {
                    return;
                }
                var target = t[0],
                    td = $(this),
                    field = td.attr("field");
                unhighlightColumn(target, field);
            }
        });
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

        initRowCellMouseEvent(t, opts);
    }

    var _datagrid = $.fn.datagrid.extensions._datagrid = $.fn.datagrid;
    $.fn.datagrid = function (options, param) {
        if (typeof options == "string") {
            return _datagrid.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "datagrid") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.tabs.parseOptions(this),
                        $.parser.parseOptions(this, [
                            {
                                autoHighlightColumn: "boolean"
                            }
                        ]), options);
            _datagrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.datagrid, _datagrid);


    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；使当前 easyui-datagrid 中指定的列 DOM 对象高亮显示；该函数定义如下参数：
        //      field: 要高亮显示的列的 field 名；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        highlightColumn: function (jq, field) { return jq.each(function () { highlightColumn(this, field); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义属性，该属性表示是否启用列自动高亮显示功能；
        //  Boolean 类型值，默认为 false。 
        autoHighlightColumn: false
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);