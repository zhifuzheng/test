/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.findRow.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-21
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    function findRow(target, param, t, opts, rows) {
        t = t && t.length ? t : $(target);
        opts = opts ? opts : $.data(target, "datagrid").options;
        rows = rows && rows.length ? rows : t.datagrid("getRows");
        var type = $.type(param);
        if (type == "function") {
            return $.array.first(rows, function (row, index) {
                return param.call(target, row, index, rows);
            });
        } else if (type == "object") {
            return opts.idField && (opts.idField in param)
                ? findRowById(param[opts.idField])
                : $.array.first(rows, function (val) { return val == param; });
        } else {
            return findRowById(param);
        }
        function findRowById(id) {
            return $.array.first(rows, function (row) {
                return row[opts.idField] == id;
            });
        }
    }

    function findRows(target, param) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options,
            rows = t.datagrid("getRows");
        if ($.isFunction(param)) {
            return $.array.filter(rows, function (val, index) {
                return param.call(target, val, index, rows);
            });
        } else if ($.array.likeArrayNotString(param)) {
            var array = $.array.map(param, function (val) {
                return findRow(target, val, t, opts, rows);
            });
            return $.array.filter(array, function (val) {
                return val != null && val != undefined;
            });
        } else {
            return [findRow(target, param, t, opts, rows)];
        }
    }

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；查找当前数据页上的行数据，返回的是一个 JSON 对象；参数 param 表示查找的内容；该方法的参数 param 可以是以下三种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      待查找的行数据对象 row ，若当前 easyui-datagrid 存在 idField 并且 row 对象中存在 idField 则根据 idField 查找，否则根据 row 对象查找；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并返回该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后并返回 null。
        //  返回值：返回一个 JSON-Object，表示一个行数据对象；如果未找到相应数据，则返回 null。
        findRow: function (jq, param) { return findRow(jq[0], param); },

        //  扩展 easyui-datagrid 的自定义方法；查找当前数据页上的行数据；该方法的参数 param 可以是以下两种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 findRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则返回的结果集中将会包含该行数据；
        //          如果该回调函数始终未返回 true，则该方法最终返回一个长度为 0 的数组对象。
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 findRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 findRows 方法会对数组中的每一项循环调用 findRow 方法，并过滤掉 findRow 方法返回 null 的结果行；
        //      落阳注：param 也可以是一个 idField(主键) 字段值，只是返回数据行时会组装成 Array 数组对象。
        //  返回值：返回一个 Array 数组对象；数组中的每一项都是 JSON-Object 类型，表示一个行数据对象；如果未找到相应数据，则返回一个长度为 0 的数组对象。
        findRows: function (jq, param) { return findRows(jq[0], param); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);