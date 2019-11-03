/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 扩展
* jeasyui.extensions.datagrid.groupSummary.js
* 开发 真真
* 由 落阳 整理及完善
* 最近更新：2016-05-30
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.columnToggle.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.datagrid.extensions");


    $.fn.datagrid.extensions.getColumnFields = $.fn.datagrid.methods.getColumnFields;
    var getColumnFields = function (target, frozen) {
        var t = $(target);
        if (frozen == null || frozen == undefined) {
            return $.fn.datagrid.extensions.getColumnFields.call(t, t, frozen);
        }
        return $.type(frozen) == "string"
            ? $.array.merge([],
                $.fn.datagrid.extensions.getColumnFields.call(t, t, true),
                $.fn.datagrid.extensions.getColumnFields.call(t, t, false))
            : $.fn.datagrid.extensions.getColumnFields.call(t, t, frozen);
    };

    var getColumnOptions = function (target, frozen) {
        var t = $(target), fields = getColumnFields(target, frozen);
        return $.array.map(fields, function (val) { return t.datagrid("getColumnOption", val); });
    };

    function getTrIndex(tr) {
        if (!tr) {
            return -1;
        }
        tr = $.util.isJqueryObject(tr) ? tr : $(tr);
        var attr = tr.attr("datagrid-row-index");
        return (attr == null || attr == undefined || attr == "") ? -1 : window.parseInt(attr, 10);
    }

    function getVisibleRows(target) {
        var t = $(target),
            rows = t.datagrid("getRows"),
            p = t.datagrid("getPanel"),
            indexes = [];
        p.find("div.datagrid-view div.datagrid-body table.datagrid-btable:first tr.datagrid-row:not(.datagrid-row-hidden)").each(function () {
            var index = getTrIndex(this);
            if (index != -1) {
                indexes.push(index);
            }
        });
        return (!rows || !rows.length || !indexes.length)
            ? []
            : $.array.map(indexes, function (i) {
                return rows[i];
            });
    }


    // 分组汇总菜单
    $.fn.datagrid.extensions.groupSummaryMenus = [
        {
            text: "分组汇总", iconCls: "icon-standard-application-side-tree",
            handler: function (e, menuItem, menu, target, field) {
                showGroupSummaryDialog(target, field);
            }
        }
    ];
    //可分组汇总的字段显示窗口
    function showGroupSummaryDialog(target, field) {
        var t = $(target), opts = t.datagrid("options"),
            oriCols = $.extend(true, [], getColumnOptions(target, "all"));
        var data = [], count = 0;
        //可选择的列数据初始化
        $.each(oriCols, function (index, item) {
            if (!item.groupable || !item.field || !item.title) { return; }
            data.push({ id: index, field: item.field, title: item.title, width: item.width });
        });
        //弹框
        var d = $("
").appendTo($("body")).dialog({
            title: "分组汇总配置",
            iconCls: "icon-standard-application-view-detail",
            height: 360,
            width: 520,
            modal: true,
            resizable: true,
            content:
            '
' +
                '
' +
                    '' +
                '' +
                '
' +
                    '
' +
                '' +
            '',
            buttons: [
                {
                    text: "确定", iconCls: "icon-ok", handler: function () {
                        var rows = $("#groupSummarySelectColumnsGrid").datagrid("getRows");
                        if (rows.length > 0) {
                            setTimeout(function () {
                                parseGroupSummaryInvoke(t, opts, rows, oriCols);
                            }, 10);
                        }
                        else { $.messager.alert("操作提醒", "缺少分组字段!"); }
                    }
                },
                {
                    text: "关闭", iconCls: "icon-cancel", handler: function () {
                        d.dialog("destroy");
                    }
                }
            ]
        });
        //重写dialog的x按钮事件，以确保关闭dialog时会销毁dialog
        var toolbutton = d.dialog("header").find(".panel-tool a.panel-tool-close");
        toolbutton.click(function () { d.dialog("destroy"); });
        //布局初始化
        $("#groupSummaryLayout").layout({ fit: true });
        //待选表格
        $("#groupSummaryAllColumnsGrid").datagrid({
            title: "字段列表(单击添加)",
            nowrap: false, rownumbers: true,
            fit: true, singleSelect: true, idField: "id",
            data: data,
            columns: [[
                { title: '字段名称', field: 'title', width: 200, align: "center" }
            ]],
            onClickRow: function (rowIndex, rowData) {
                var rows = $("#groupSummarySelectColumnsGrid").datagrid("getRows");
                if (!$.array.contains(rows, rowData, function (a, b) { return a.id == b.id; })) {
                    $("#groupSummarySelectColumnsGrid").datagrid("appendRow", rowData);
                }
            },
            enableHeaderContextMenu: false,
            enableHeaderClickMenu: false,
            enableGroupSummary: false
        });
        //已选表格
        $("#groupSummarySelectColumnsGrid").datagrid({
            title: "已选字段(单击移除)",
            nowrap: false, rownumbers: true,
            fit: true, singleSelect: true, idField: "id",
            columns: [[
                { title: '字段名称', field: 'title', width: 200, align: "center" }
            ]],
            onClickRow: function (rowIndex, rowData) {
                $("#groupSummarySelectColumnsGrid").datagrid("deleteRow", rowIndex);
            },
            enableHeaderContextMenu: false,
            enableHeaderClickMenu: false,
            enableGroupSummary: false
        });
    }
    function parseGroupSummaryInvoke(t, opts, selfCols, oriCols) {
        oriCols = $.extend(true, [], oriCols);
        var values = [], level = 0;
        var mode = $.util.isBoolean(opts.enableGroupSummary) ? "local" : opts.enableGroupSummary.mode;
        if (mode == "local") {
            var rows = $.extend(true, [], getVisibleRows(t[0]));
            var data = parseGroupSummaryInitRows(t, opts, values, rows, oriCols, selfCols, level);
            parseGroupSummaryLoadData(oriCols, selfCols, data);
        } else {
            if ($.string.isNullOrWhiteSpace(opts.url)) {
                $.messager.alert("操作提醒", "远程数据地址未设置，无法进行分组汇总。"); return;
            }
            //组装参数
            var queryParams = $.extend({}, opts.queryParams);
            if (opts.pagination) {
                //页码为0表示查询所有数据，这需要与后台协定好
                $.extend(queryParams, { page: 0, rows: opts.pageSize });
            }
            if (opts.sortName) {
                $.extend(queryParams, { sort: opts.sortName, order: opts.sortOrder });
            }
            opts.loader.call(t[0], queryParams, function (rows) {
                if ($.isPlainObject(rows)) { rows = rows.rows; }
                var data = parseGroupSummaryInitRows(t, opts, values, rows, oriCols, selfCols, level);
                parseGroupSummaryLoadData(oriCols, selfCols, data);
            }, function () {
                //发生异常时
            });
        }
    }
    //数据组装
    function parseGroupSummaryInitRows(t, opts, pValues, rows, oriCols, selfCols, level) {
        if (selfCols.length <= level) {
            //如果超出层级范围则添加明细数据
            return parseGroupSummaryInitData(pValues, rows, selfCols, level);
        }
        var data = [], selfCol = selfCols[level], len = rows.length;
        var ignoreFormatter = true;
        if ($.isPlainObject(opts.enableGroupSummary)) {
            ignoreFormatter = opts.enableGroupSummary.ignoreFormatter;
        }
        for (var i = 0; i < len; i++) {
            var bit = true, value = rows[i][selfCol.field] || "空白";
            //判断是否满足向上逐级条件
            for (var v = 0; v < pValues.length && bit; v++) {
                var stepValue = rows[i][pValues[v].field] || "空白";
                if (stepValue != pValues[v].value) {
                    bit = false;
                }
            }
            if (bit) {
                //判断是否存在分组
                for (var d = 0; d < data.length && bit; d++) {
                    if (data[d]["groupSummaryName"] == value) {
                        bit = false;
                    }
                }
            }

            if (bit) {
                //写入分组数据
                var row = {};
                var values = $.extend(true, [], pValues);
                values.push({ field: selfCol.field, value: value });
                //递归
                row["children"] = parseGroupSummaryInitRows(t, opts, values, rows, oriCols, selfCols, level + 1);
                parseGroupSummaryInvokeSum(t, row, ignoreFormatter, oriCols, selfCols);
                row["groupSummaryID"] = "g_" + level + "_" + i;
                row["groupSummaryName"] = value;
                row["state"] = "closed";
                data.push(row);
            }
        }
        return data;
    }
    //装载明细
    function parseGroupSummaryInitData(pValues, rows, selfCols, level) {
        var data = [];
        for (var i = 0; i < rows.length; i++) {
            var bit = true;
            //向上逐级条件判断
            for (var v = 0; v < pValues.length && bit; v++) {
                var stepValue = rows[i][pValues[v].field] || "空白";
                if (stepValue != pValues[v].value) {
                    bit = false; break;
                }
            }
            if (bit) {
                var row = $.extend(true, {}, rows[i]);
                row["groupSummaryID"] = "g_" + level + "_" + i;
                row["groupSummaryName"] = "明细";
                data.push(row);
            }
        }
        return data;
    };
    //分组汇总计算
    function parseGroupSummaryInvokeSum(t, row, ignoreFormatter, oriCols, selfCols) {
        var rowIndex = t.datagrid("getRowIndex", row);
        $.each(oriCols, function (i, c) {
            var bit = true;
            //分组列不做汇总
            for (var s = 0; s < selfCols.length && bit; s++) {
                if (selfCols[s].field == c.field) {
                    bit = false;
                }
            }
            if (bit) {
                var temp = 0;
                //汇总计算
                var len = row["children"].length;
                for (var n = 0; n < len; n++) {
                    if (!c.calcable) {
                        temp = ""; break;
                    }
                    var value = row["children"][n][c.field],
                        realValue = ignoreFormatter ? value : (((n != (len - 1)) && c.formatter && $.isFunction(c.formatter)) ? c.formatter.call(t[0], value, row, rowIndex) : value);
                    if ($.type(realValue) == "number") {
                        temp += realValue;
                    }
                    else if ($.type(value) == "number") {
                        temp += value;
                    }
                    else { temp = ""; break; }
                }
                row[c.field] = temp;
            }
        });
    };
    //分组汇总载入数据
    function parseGroupSummaryLoadData(oriCols, selfCols, rows) {
        //弹窗
        var d = $("
").appendTo($("body")).dialog({
            title: "分组汇总",
            modal: true,
            width: $(window).width() - 20,
            height: $(window).height() - 20,
            content: '
',
            buttons: [
                {
                    text: "关闭", iconCls: "icon-cancel", handler: function () {
                        d.dialog("destroy");
                    }
                }
            ]
        });
        //重写dialog的x按钮事件，以确保关闭dialog时会销毁dialog
        var toolbutton = d.dialog("header").find(".panel-tool a.panel-tool-close");
        toolbutton.click(function () { d.dialog("destroy"); });

        var title = "层级：", width = 40, rowspan = 1;
        for (var i = 0; i < selfCols.length; i++) {
            if (i > 0) { title += " > "; }
            title += selfCols[i].title;
            width = selfCols[i].width > width ? selfCols[i].width : width;
            $.each(oriCols, function (index, c) {
                //隐藏分组列
                if (c.field == selfCols[i].field) {
                    c.hidden = true;
                }
                //跨行表头支持
                if (c.rowspan) {
                    rowspan = c.rowspan > rowspan ? c.rowspan : rowspan;
                }
            });
        }
        //树形列添加
        oriCols.insert(0, {
            width: width + (selfCols.length * 50), align: "left",
            title: title, rowspan: rowspan,
            field: "groupSummaryName"
        });
        var tg = $("#groupSummaryGrid").treegrid({
            nowrap: false, border: false,
            fit: true, singleSelect: true,
            remoteSort: false,
            idField: "groupSummaryID",
            treeField: "groupSummaryName",
            columns: [oriCols], data: rows,
            enableHeaderContextMenu: false,
            enableHeaderClickMenu: false,
            enableGroupSummary: false
        });
    };




    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

    }

    function beforeInitialize(opts) {
        if (opts.enableGroupSummary == undefined) {
            opts.enableGroupSummary = { enable: true, mode: "local", ignoreFormatter: true };
        }
        else if ($.isPlainObject(opts.enableGroupSummary)) {
            opts.enableGroupSummary.enable = opts.enableGroupSummary.enable == undefined ? true : opts.enableGroupSummary.enable;
            opts.enableGroupSummary.mode = opts.enableGroupSummary.mode == undefined ? "local" : (["local", "remote"].contains(opts.enableGroupSummary.mode) ? opts.enableGroupSummary.mode : "local");
            opts.enableGroupSummary.ignoreFormatter = opts.enableGroupSummary.ignoreFormatter == undefined ? true : opts.enableGroupSummary.ignoreFormatter;
        }

        if (($.util.isBoolean(opts.enableGroupSummary) && opts.enableGroupSummary) || ($.isPlainObject(opts.enableGroupSummary) && opts.enableGroupSummary.enable)) {
            if (!$.isArray(opts.headerContextMenu)) {
                opts.headerContextMenu = [];
            }
            $.array.merge(opts.headerContextMenu, $.fn.datagrid.extensions.groupSummaryMenus);
        }
        //合并columnOptions
        if (opts.frozenColumns) {
            $.each(opts.frozenColumns, function (ii, cc) {
                $.each(cc, function (i, c) {
                    $.union(c, $.fn.datagrid.extensions.columnOptions);
                });
            });
        }
        if (opts.columns) {
            $.each(opts.columns, function (ii, cc) {
                $.each(cc, function (i, c) {
                    $.union(c, $.fn.datagrid.extensions.columnOptions);
                });
            });
        }
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
                        $.fn.datagrid.parseOptions(this),
                        $.parser.parseOptions(this), options);
            if (!isInited) { beforeInitialize(opts); }
            _datagrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.datagrid, _datagrid);


    var columnOptions = {

        // 表示该列是否可分组，其值可以是 Boolean 类型；
        // 默认为 true。
        // 该属性用于在“列分组”时判定
        groupable: true,

        // 表示该列是否可计算，其值可以是 Boolean 类型；
        // 默认为 false。
        // 该属性用于在“汇总计算”时使用。
        calcable: false
    };
    if ($.fn.datagrid.extensions.columnOptions) {
        $.extend($.fn.datagrid.extensions.columnOptions, columnOptions);
    }
    else {
        $.fn.datagrid.extensions.columnOptions = columnOptions;
    }


    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义属性，该属性表示是否启用分组汇总按钮菜单功能；该属性可以是以下两种数据类型：
        //      1、JSON-Object 类型值，表示分组汇总的参数，该对象类型参数包含如下属性：
        //              enable:      表示是否启用分组汇总按钮菜单功能，默认为 true ；
        //              mode:        表示要分组汇总的数据是本地数据还是远程数据，其值可以是 local、remote，默认为 local ；
        //              ignoreFormatter:     表示在统计时是否忽略 columns 中设置的 formatter 回调函数，默认为 true ；
        //                  注意，如果可计算的列（calcable:true）通过 formatter 后返回的值不是 number 类型，即使 ignoreFormatter 设置为 true，也将忽略该 formatter 效果。
        //      2、Boolean 类型值，表示是否启用分组汇总按钮菜单功能，默认为 false。 
        enableGroupSummary: true
    };

    var methods = $.fn.datagrid.extensions.methods = {


    };


    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);