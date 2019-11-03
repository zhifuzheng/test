/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.deleteRow.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-21
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.findRow.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");

    $.fn.datagrid.extensions.deleteRow = $.fn.datagrid.methods.deleteRow;
    var deleteRow = function (target, param) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options,
            row,index;
        if ($.util.isNumber(param)) {
            var rows = t.datagrid("getRows");
            row = $.array.first(rows, function (itemRow, itemIndex) { return itemIndex == param; });
            index = param;
        }
        else {
            row = t.datagrid("findRow", param);
            index = t.datagrid("getRowIndex", row);
        }
        if (index > -1 && $.isFunction(opts.onBeforeDeleteRow) && opts.onBeforeDeleteRow.call(target, index, row) == false) { return; }
        $.fn.datagrid.extensions.deleteRow.call(target, t, index);
        if ($.isFunction(opts.onDeleteRow)) { opts.onDeleteRow.call(target, index, row); }
    };

    var deleteRows = function (target, param) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;
        if ($.array.likeArrayNotString(param)) {
            $.each(param, function (index, val) { deleteRow(target, val); });
        } else if ($.isFunction(param)) {
            var rows = t.datagrid("getRows"), onBeforeDeleteRowExist = $.isFunction(opts.onBeforeDeleteRow), onDeleteRowExist = $.isFunction(opts.onDeleteRow);
            // 以下是“遍历rows，对每个row调用param回调函数，若返回true则删除行”的方式，但是每次deleteRow都将更新rows的索引
            //$.each(rows, function (index, row) {
            //    if (!row) { return; }
            //    if (param.call(target, row, index, rows) == true) {
            //        var i = t.datagrid("getRowIndex", row);
            //        if (i > -1 && onBeforeDeleteRowExist && opts.onBeforeDeleteRow.call(target, i, row) == false) { return; }
            //        $.fn.datagrid.extensions.deleteRow.call(target, t, i);
            //        if (onDeleteRowExist) { opts.onDeleteRow.call(target, i, row); }
            //    }
            //});
            // 以下是“调用findRows，将符合param回调函数的row组装成数组，最后遍历数组对每项进行删除行”的方式
            // 从常规逻辑来看，这种处理逻辑更合理
            var theRows = t.datagrid("findRows", param);
            $.each(theRows, function (index, row) {
                var i = t.datagrid("getRowIndex", row);
                if (i > -1 && onBeforeDeleteRowExist && opts.onBeforeDeleteRow.call(target, i, row) == false) { return; }
                $.fn.datagrid.extensions.deleteRow.call(target, t, i);
                if (onDeleteRowExist) { opts.onDeleteRow.call(target, i, row); }
            });
        }
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  重写 easyui-datagrid 的 deleteRow 方法；参数 param 表示要删除的内容；该参数可以是以下四种类型：
        //      Number 类型，表示要删除的行索引号，从 0 开始计数；
        //      表示要删除的行数据的 idField(主键) 字段值；
        //      行数据对象；
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 deleteRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRow 方法将会删除该行数据并立即停止和跳出循环操作；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        deleteRow: function (jq, param) { return jq.each(function () { deleteRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；参数 param 表示要删除的内容；该参数可以是以下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 deleteRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示查找到了需要被删除的行，deleteRows 方法将会删除该行数据，并遍历下一行数据直至数数据集的末尾；
        //      Array 类型，数组中的每一项目均表示要删除的行的行索引号或者 idField(主键) 字段值或者行数据对象
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        deleteRows: function (jq, param) { return jq.each(function () { deleteRows(this, param); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 deleteRow 方法前所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 deleteRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行 deleteRow 操作的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件回调函数返回 false，则立即取消即将要执行的 deleteRow 操作。
        onBeforeDeleteRow: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示执行 deleteRow 方法后所触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示要进行 deleteRow 的行的索引号，从 0 开始计数；
        //      row:    表示要进行 deleteRow 操作的行数据对象；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        onDeleteRow: function (index, row) { }
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);