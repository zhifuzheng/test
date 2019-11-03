/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.tooltip.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-22
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.getColumnInfo.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.treegrid.extensions");

    $.extend($.fn.treegrid.extensions, {
        beginEdit: $.fn.treegrid.methods.beginEdit
    });



    function beginEdit(target, id) {
        var t = $(target),
            ret = $.fn.treegrid.extensions.beginEdit.call(t, id),
            editing = isEditing(target, id);
        if (!editing) {
            return ret;
        }
        hideRowTooltip(target, id);
        return ret;
    }

    var isEditing = function (target, id) {
        var t = $(target), panel = t.treegrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[node-id=" + id + "]").hasClass("datagrid-row-editing");
    };

    var getRowDom = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false,
            t = $(target), opts = t.treegrid("options"), panel = t.treegrid("getPanel"),
            dom = panel.find(".datagrid-view .datagrid-body tr.datagrid-row[node-id=" + id + "]");
        if (cascade) {
            var children = t.treegrid("getChildren", id);
            $.each(children, function (i, n) { var d = getRowDom(target, n[opts.idField]); dom = dom.add(d); });
        }
        return dom;
    };

    var getCellDom = function (target, pos) {
        if (!pos || !pos.field || pos.id == null || pos.id == undefined) { return $(); }
        var t = $(target), tr = getRowDom(target, pos.id);
        return tr.find("td[field=" + pos.field + "] .datagrid-cell");
    };

    var getCellHtml = function (target, pos) {
        var cell = getCellDom(target, pos);
        return cell && cell.length ? cell.text() : undefined;
    };


    function initExtendColumnOptions(t, opts) {
        var target = t[0],
            state = $.data(target, "treegrid"),
            cols = t.treegrid("getColumnOptions", "all");
        $.each(cols, function (i, col) {
            $.union(col, $.fn.treegrid.extensions.columnOptions);
        });
    }

    function initRowMouseEvent(t, opts) {
        var target = t[0];
        t.treegrid("getPanel").panel("body").delegate("tr.datagrid-row", {
            "mouseenter.datagrid-extensions": function (e) {
                var tr = $(this);
                if (tr.is(".datagrid-row-editing")) { return; }

                var id = tr.attr("node-id"), row = t.treegrid("find", id);
                showRowTooltip(target, id, t, opts, row, tr, e);
            },
            "mouseleave.datagrid-extensions": function () {
                var tr = $(this), id = tr.attr("node-id");
                hideRowTooltip(target, id, tr);
            }
        });
    }

    function showRowTooltip(target, id, t, opts, row, tr, e) {
        t = t || $(target);
        opts = opts || $.data(target, "treegrid").options;
        row = row || t.treegrid("find", id);
        tr = tr || getRowDom(target, id);
        if (opts.rowTooltip) {
            var content = $.isFunction(opts.rowTooltip) ? opts.rowTooltip.call(target, id, row) : getRowTooltipContent(target, id, row);
            tr.each(function () {
                if (!$.data(this, "tooltip")) {
                    $(this).tooltip({
                        content: content,
                        trackMouse: true,
                        showDelay: opts.tooltipDelay
                    }).tooltip("show");
                }
            });
        } else {
            tr.children("td[field]").each(function () {
                var td = $(this),
                    field = td.attr("field"),
                    copts = t.datagrid("getColumnOption", field);
                if (!copts || !copts.tooltip) { return; }
                if (!$.data(td[0], "tooltip")) {
                    var content = $.isFunction(copts.tooltip) ? copts.tooltip.call(target, row[field], row) : getCellHtml(target, { field: field, id: id });
                    td.tooltip({
                        content: content,
                        trackMouse: true,
                        showDelay: opts.tooltipDelay
                    });
                }
                if (e && e.target) {
                    if (td[0] == e.target || $.contains(t[0], e.target)) {
                        td.tooltip("show");
                    }
                } else {
                    td.tooltip("show");
                }
            });
        }
    }

    function hideRowTooltip(target, id, tr) {
        tr = tr || getRowDom(target, id);
        tr.tooltip("destroy").children("td[field]").each(function () {
            $(this).tooltip("destroy");
        });
    }

    function getRowTooltipContent(target, id, row) {
        var t = $(target), cols = t.datagrid("getColumnOptions", "all"),
            content = $("
");
        $.each(cols, function (i, copts) {
            if (!copts || !copts.field || !copts.title) { return; }
            var text = getCellHtml(target, { field: copts.field, id: id });
            content.append("" + copts.title + ":" + text + "");
        });
        return content;
    }



    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "treegrid"),
            opts = state.options;
        initExtendColumnOptions(t, opts);
        initRowMouseEvent(t, opts);
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
                            {
                                tooltipDelay: "number"
                            },
                            {
                                rowTooltip: "boolean"
                            }
                        ]), options);
            _treegrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.treegrid, _treegrid);



    //  增加了 easyui-treegrid 中列 columnOption 的部分自定义扩展属性
    var columnOptions = $.fn.treegrid.extensions.columnOptions = {

        // 表示是否启用该列的 tooptip 效果，其值可以是以下两种类型：
        //      Boolean 类型，表示是否启用该列的 tooltip；
        //      Function 类型，其函数签名为 function (value, rowData)，表示为该列启用 tooltip 的方式；
        //          该回调函数返回一个 String 类型值，表示 tooltip 的 content 内容。
        // 默认为 Boolean 类型，值为 false。
        tooltip: false
    };

    var defaults = $.fn.treegrid.extensions.defaults = {

        //  扩展 easyui-treegrid 的自定义属性，该属性表示是否启用行数据的 tooltip 功能；
        //  该属性可以是一个 Boolean 类型值；也可以是一个签名为 function(nodeId, rowData) 的回调函数；
        //  如果该参数是一个回调函数，则表示启用行数据的 tooltip 功能，并且该函数的返回值为 tooltip 的 content 值。
        //  默认为 Boolean 类型，值为 false。
        //  注意：当启用该配置属性后，所有列的 tooltip 属性就会自动失效。
        rowTooltip: false,

        // 扩展 easyui-treegrid 的自定义属性，该属性表示 rowTooltip 为 true 或 列的扩展属性 tooltip 为 true 时，显示 tooltip 时的延迟毫秒数；
        // 该属性的值是 Number 类型，默认为 300 。
        tooltipDelay: 300
    };

    var methods = $.fn.treegrid.extensions.methods = {

        //  重写 easyui-treegrid 的原生方法 beginEdit ，以支持自定义属性 rowTooltip 和列属性 tooltip 的扩展功能；
        beginEdit: function (jq, id) { return jq.each(function () { beginEdit(this, id); }); }
    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);