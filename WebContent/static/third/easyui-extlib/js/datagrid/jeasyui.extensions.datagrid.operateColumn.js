/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.operateColumn.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-12-18
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.getColumnInfo.js
*   3、jeasyui.extensions.datagrid.getDom.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");



    function unfreezeColumn(target, field) {
        var state = $.data(target, "datagrid");
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行取消冻结表格列的操作。");
        }
        var t = $(target), fields = t.datagrid("getColumnFields", false)
        if ($.array.contains(fields, field)) {
            return;
        }
        t.datagrid("moveColumn", {
            target: fields[0],
            source: field,
            point: "before"
        });
    }

    function freezeColumn(target, field) {
        var state = $.data(target, "datagrid");
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行冻结表格列的操作。");
        }
        var t = $(target), frozenFields = t.datagrid("getColumnFields", true),
            isFrozen = isFrozenColumn(target, field, frozenFields);
        if (isFrozen) { return; }
        t.datagrid("moveColumn", {
            target: frozenFields[frozenFields.length - 1],
            source: field,
            point: "after"
        });
    }




    //  param: { field: string, point: string("before"/default, "after") }
    function shiftColumn(target, param) {
        if (!param || !param.field || (param.point != "before" && param.point != "after")) { return; }
        var state = $.data(target, "datagrid");
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行移动表格列的操作。");
        }
        var sourceField = param.field,
            targetColumn = param.point == "before" ? getPrevVisibleColumn(target, sourceField) : getNextVisibleColumn(target, sourceField);
        if (!targetColumn || !targetColumn.field) { return; }
        var t = $(target), targetField = targetColumn.field;
        t.datagrid("moveColumn", {
            target: targetField,
            source: sourceField,
            point: param.point
        });
    }

    function getPrevVisibleColumn(target, field) {
        if (!field) { return undefined; }
        var t = $(target), cols = t.datagrid("getColumnOptions", "all");
        if (!cols || !cols.length) { return undefined; }
        var index = -1;
        return $.array.last(cols, function (val, i) {
            if (val.field == field) {
                index = i;
            }
            return index != -1 && i < index && !val.hidden ? true : false;
        });
    }

    function getNextVisibleColumn(target, field) {
        if (!field) { return undefined; }
        var t = $(target), cols = t.datagrid("getColumnOptions", "all");
        if (!cols || !cols.length) { return undefined; }
        var index = -1;
        return $.array.first(cols, function (val, i) {
            if (val.field == field) {
                index = i;
            }
            return index != -1 && i > index && !val.hidden ? true : false;
        });
    }




    //  param: { target: string/field, source: string/field, point: string("before"/default, "after") }
    function moveColumn(target, param) {
        if (!param || !param.target || !param.source || param.target == param.source || (param.point != "before" && param.point != "after")) {
            return;
        }
        var state = $.data(target, "datagrid"),
            opts = state.options;
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行移动表格列的操作。");
        }
        var t = $(target),
            targetField = param.target,
            sourceField = param.source,
            fields = t.datagrid("getColumnFields", "all"),
            targetIndex = $.array.indexOf(fields, targetField),
            sourceIndex = $.array.indexOf(fields, sourceField);
        if (targetIndex == -1 || sourceIndex == -1 || targetIndex == sourceIndex) {
            return;
        }
        var sourceColumnOpt = t.datagrid("getColumnOption", sourceField);
        if (sourceColumnOpt.movable != undefined && sourceColumnOpt.movable == false) { return; }
        var frozenFields = t.datagrid("getColumnFields", true),
            targetFrozen = isFrozenColumn(target, targetField, frozenFields),
            sourceFrozen = isFrozenColumn(target, sourceField, frozenFields),
            point = param.point;
        if (sourceIndex == (point == "before" ? targetIndex - 1 : targetIndex + 1) && (targetFrozen == sourceFrozen)) {
            return;
        }
        if ($.isFunction(opts.onBeforeMoveColumn) && opts.onBeforeMoveColumn.call(target, sourceField, targetField, point) == false) {
            return;
        }
        var p = t.datagrid("getPanel"),
            v = p.find("div.datagrid-view"),
            v1 = v.find("div.datagrid-view1"),
            v2 = v.find("div.datagrid-view2"),
            hr1 = v1.find("div.datagrid-header table.datagrid-htable tr.datagrid-header-row"),
            hr2 = v2.find("div.datagrid-header table.datagrid-htable tr.datagrid-header-row"),
            br1 = v1.find("div.datagrid-body table.datagrid-btable tr.datagrid-row"),
            br2 = v2.find("div.datagrid-body table.datagrid-btable tr.datagrid-row"),
            targetHeaderTd = (targetFrozen ? hr1 : hr2).find("td[field=" + targetField + "]"),
            sourceHeaderTd = (sourceFrozen ? hr1 : hr2).find("td[field=" + sourceField + "]"),
            targetRow = targetFrozen ? br1 : br2,
            sourceRow = sourceFrozen ? br1 : br2,
            targetCopts = t.datagrid("getColumnOption", targetField),
            sourceCopts = sourceColumnOpt,
            targetColumns = targetFrozen ? opts.frozenColumns[0] : opts.columns[0],
            sourceColumns = sourceFrozen ? opts.frozenColumns[0] : opts.columns[0];

        targetHeaderTd[point](sourceHeaderTd);
        targetRow.each(function (i) {
            var targetTd = $(this).find("td[field=" + targetField + "]"),
                sourceTd = $(sourceRow[i]).find("td[field=" + sourceField + "]");
            targetTd[point](sourceTd);
        });

        $.array.remove(sourceColumns, sourceCopts);
        var targetCoptsIndex = $.array.indexOf(targetColumns, targetCopts);
        $.array.insert(targetColumns, point == "before" ? targetCoptsIndex : targetCoptsIndex + 1, sourceCopts);

        t.datagrid("fixColumnSize");
        if (sourceFrozen) {
            if (!targetFrozen) {
                var index = $.array.indexOf(state.columnOptions, targetCopts, function (col) { return col.field == targetField; });
                $.array.insert(state.columnOptions, point == "before" ? index : index + 1, sourceCopts);
                $.array.insert(state.originalColumnOptions, point == "before" ? index : index + 1, $.extend({}, sourceCopts));
            }
        } else {
            var index = $.array.indexOf(state.columnOptions, sourceCopts, function (col) { return col.field == sourceField; });
            if (targetFrozen) {
                $.array.removeAt(state.columnOptions, index);
                $.array.removeAt(state.originalColumnOptions, index);
            } else {
                var copts = state.columnOptions[index],
                    bcopts = state.originalColumnOptions[index];
                $.array.removeAt(state.columnOptions, index);
                $.array.removeAt(state.originalColumnOptions, index);

                var tindex = $.array.indexOf(state.columnOptions, targetCopts, function (col) { return col.field == targetField; });
                $.array.insert(state.columnOptions, point == "before" ? tindex : tindex + 1, copts);
                $.array.insert(state.originalColumnOptions, point == "before" ? tindex : tindex + 1, bcopts);
            }
        }

        if ($.isFunction(opts.onMoveColumn)) {
            opts.onMoveColumn.call(target, sourceField, targetField, point);
        }
    }

    function popColumn(target, field) {
        var state = $.data(target, "datagrid");
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行删除表格列的操作。");
        }
        var t = $(target),
            copts = t.datagrid("getColumnOption", field);
        if (copts) {
            t.datagrid("deleteColumn", field);
            return copts;
        } else {
            return undefined;
        }
    }



    function deleteColumn(target, field) {
        var state = $.data(target, "datagrid");
        if (state.multiLineHeaders) {
            $.error("不支持在多行表头情况下执行删除表格列的操作。");
        }
        var opts = state.options;
        if ($.isFunction(opts.onBeforeDeleteColumn) && opts.onBeforeDeleteColumn.call(target, field) == false) {
            return;
        }
        var frozen = isFrozenColumn(target, field), removed = removeField(target, field, frozen);
        if (removed) {
            var t = $(target);
            t.datagrid("getColumnDom", { field: field, header: true }).remove();
            if (frozen) {
                t.datagrid("fixColumnSize");
            }
            if ($.isFunction(opts.onDeleteColumn)) {
                opts.onDeleteColumn.call(target, field);
            }
        }
    }

    function removeField(target, field, frozen) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options,
            frozen = frozen == undefined ? isFrozenColumn(target, field) : frozen;
        return remove(frozen ? opts.frozenColumns : opts.columns);
        function remove(columns) {
            if (!columns || !columns.length) {
                return false;
            }
            var ret = false;
            $.each(columns, function (index, cols) {
                var i = $.array.indexOf(cols, field, function (col) {
                    return col.field == field;
                });
                if (i > -1) {
                    $.array.removeAt(cols, i);
                    ret = true;
                    $.array.remove(state.columnOptions, field, function (col) {
                        return col.field == field;
                    });
                    $.array.remove(state.originalColumnOptions, field, function (col) {
                        return col.field == field;
                    });
                }
            });
            return ret;
        }
    }

    function isFrozenColumn(target, field, frozenFields) {
        if (!field) {
            return undefined;
        }
        var fields = frozenFields && frozenFields.length ? frozenFields : $(target).datagrid("getColumnFields", true);
        return $.array.contains(fields, field);
    }



    function isMultiLineHeaders(target) {
        var state = $.data(target, "datagrid"),
            opts = state.options;
        if (opts.columns && opts.columns.length > 1 && opts.columns[0].length && opts.columns[1].length) {
            return true;
        }
        if (opts.frozenColumns && opts.frozenColumns.length > 1 && opts.frozenColumns[0].length && opts.frozenColumns[1].length) {
            return true;
        }
        return false;
    }

    function initExtendColumnOptions(t, opts) {
        var target = t[0],
            state = $.data(target, "datagrid"),
            cols = t.datagrid("getColumnOptions", "all");
        $.each(cols, function (i, col) {
            $.union(col, $.fn.datagrid.extensions.columnOptions);
        });
        var columnOptions = t.datagrid("getColumnOptions", false);
        state.columnOptions = $.array.filter(columnOptions, function (col) {
            return col.title ? true : false;
        });
        state.originalColumnOptions = $.array.map(state.columnOptions, function (col) {
            return $.extend({}, col);
        });
        state.multiLineHeaders = isMultiLineHeaders(target);
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

        initExtendColumnOptions(t, opts);
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
                        $.fn.datagrid.parseOptions(this), options);
            _datagrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.datagrid, _datagrid);



    //  增加了 easyui-datagrid 中列 columnOption 的部分自定义扩展属性
    var columnOptions = $.fn.datagrid.extensions.columnOptions = {

        // 表示该列是否可移动，其值可以是 Boolean 类型；
        // 默认为 true。
        // 该属性用户在“移动列”时判定
        movable: true
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；删除指定的列；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        deleteColumn: function (jq, field) { return jq.each(function () { deleteColumn(this, field); }); },

        //  扩展 easyui-datagrid 的自定义方法；删除指定的列并返回该列的 ColumnOption 值；该方法的参数 field 表示要删除的列的 field 值；
        //  返回值：返回参数 field 值所表示的列的 ColumnOption 值。如果当前 easyui-datagrid 不存在该列，则返回 undefined。
        popColumn: function (jq, field) { return popColumn(jq[0], field); },

        //  扩展 easyui-datagrid 的自定义方法；移动指定的列到另一位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      target: 表示目标位置列的 field 值；
        //      source: 表示要移动的列的 field 值；
        //      point:  表示移动到目标列的位置，String 类型，可选的值包括：
        //          before: 表示将 source 列移动至 target 列的左侧；
        //          after:  表示将 source 列移动值 target 列的右侧；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        moveColumn: function (jq, param) { return jq.each(function () { moveColumn(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动指定的列挪动一格位置；该方法的参数 param 为一个 JSON-Object，定义包含如下属性：
        //      field:  表示要挪动的列的 field 值；
        //      porint: 表示挪动 field 列的方式，String 类型，可选的值包括：
        //          before: 表示将该列向左挪动一格；
        //          after:  表示将该列向右挪动一格；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        shiftColumn: function (jq, param) { return jq.each(function () { shiftColumn(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；冻结指定的列；该方法的参数 field 表示要冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个冻结列，否则此方法无效；
        freezeColumn: function (jq, field) { return jq.each(function () { freezeColumn(this, field); }); },

        //  扩展 easyui-datagrid 的自定义方法；取消冻结指定的列；该方法的参数 field 表示要取消冻结的列的 field 值。
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  注意：此方法在多行表头情况下无效。
        //      当前表格在执行此方法前必须存在至少一个非冻结列，否则此方法无效；
        unfreezeColumn: function (jq, field) { return jq.each(function () { unfreezeColumn(this, field); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义事件；该事件表示删除指定的列前触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  备注：如果该事件回调函数返回 false，则不进行删除列的操作。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeDeleteColumn: function (field) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示删除指定的列后触发的动作；该事件回调函数提供如下参数：
        //      field:  表示要被删除的列的 field 值。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onDeleteColumn: function (field) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动指定的列前触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  备注：如果该事件回调函数返回 false，则不进行移动列的操作。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onBeforeMoveColumn: function (source, target, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动指定的列后触发的动作；该事件回调函数提供如下参数：
        //      source:  表示要被移动的列的 field 值。
        //      target:  表示目标位置的列的 field 值。
        //      point :  表示移动的方式；这是一个 String 类型值，可能的值包括：
        //          "before":   表示将列 source 移动至列 target 的前一格位置；
        //          "after" :   表示将列 source 移动至列 target 的后一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onMoveColumn: function (source, target, point) { }
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);