/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.moveRow.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-12-01
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    //  param: { target: number, source: number, point: string("top"/default, "bottom") }
    function moveRow(target, param) {
        if (!param || (param.source == null || param.source == undefined)
            || (param.target == null || param.target == undefined)
            || (param.point != "top" && param.point != "bottom")) {
            return;
        }
        var t = $(target),
            rows = t.datagrid("getRows");
        if (!rows || !rows.length) {
            return;
        }
        var sourceIndex = param.source,
            targetIndex = param.target,
            sourceRow = rows[sourceIndex],
            targetRow = rows[targetIndex];
        if (sourceIndex == targetIndex || sourceRow == undefined || targetRow == undefined) {
            return;
        }
        var state = $.data(target, "datagrid"),
            opts = state.options;
        if ($.isFunction(opts.onBeforeMoveRow) && opts.onBeforeMoveRow.call(target, targetRow, sourceRow, param.point) == false) {
            return;
        }
        t.datagrid("deleteRow", sourceIndex);

        var index = t.datagrid("getRowIndex", targetRow);
        if (param.point == "top") {
            t.datagrid("insertRow", { index: index, row: sourceRow });
        } else {
            rows = t.datagrid("getRows");
            if (index++ >= rows.length) {
                t.datagrid("appendRow", sourceRow);
            } else {
                t.datagrid("insertRow", { index: index, row: sourceRow });
            }
        }
        if ($.isFunction(opts.onMoveRow)) {
            opts.onMoveRow.call(target, targetRow, sourceRow, param.point);
        }
    }

    //  param: { index: number, type: string("up"/default, "down") }
    var shiftRow = function (target, param) {
        var sourceIndex = param.index,
            targetIndex = param.type == "up" ? param.index - 1 : param.index + 1,
            point = param.type == "up" ? "top" : "bottom";
        moveRow(target, {
            source: sourceIndex,
            target: targetIndex,
            point: point
        });
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；移动 easyui-datagrid 中的指定 data-row(数据行) ；该方法的参数 param 为 JSON-Object 类型，包含如下属性定义：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  备注：该方法会触发移动行数据的相应事件；
        moveRow: function (jq, param) { return jq.each(function () { moveRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；移动 easyui-datagrid 中的指定 data-row(数据行) 一行位置；该方法的参数 param 为 JSON-Object 类型，包含如下属性定义：
        //      index: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      type:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "up":      表示移动到上一格位置；
        //          "down":   表示移动到下一格位置；
        //  返回值：返回表示当前 easyui-datagrid 的 jQuery 链式对象。
        //  备注：该方法会触发移动行数据的相应事件；
        shiftRow: function (jq, param) { return jq.each(function () { shiftRow(this, param); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动 data-row(数据行) 之前触发的动作；该事件回调函数提供如下三个参数：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  如果该事件函数返回 false，则会立即停止移动数据行操作；
        onBeforeMoveRow: function (target, source, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示移动 data-row(数据行) 之后触发的动作；该事件回调函数提供如下三个参数：
        //      target: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      source: 表示要移动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示移动到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示移动到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onMoveRow: function (target, source, point) { }
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);