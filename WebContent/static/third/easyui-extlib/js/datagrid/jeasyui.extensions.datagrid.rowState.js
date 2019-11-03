/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.rowState.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-24
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    var isChecked = function (target, index) {
        if (index == null || index == undefined) {
            return false;
        }
        var t = $(target), rows = t.datagrid("getChecked"),
            list = $.array.map(rows, function (val) { return t.datagrid("getRowIndex", val); });
        return $.array.contains(list, index);
    };

    var isSelected = function (target, index) {
        if (index == null || index == undefined) {
            return false;
        }
        var t = $(target), rows = t.datagrid("getSelections"),
            list = $.array.map(rows, function (val) { return t.datagrid("getRowIndex", val); });
        return $.array.contains(list, index);
    };

    var isEditing = function (target, index) {
        if (index == null || index == undefined) {
            return false;
        }
        var t = $(target), panel = t.datagrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[datagrid-row-index=" + index + "]").hasClass("datagrid-row-editing");
    };

    var getEditingRowIndex = function (target) {
        var array = getEditingRowIndexs(target);
        return array.length ? array[0] : -1;
    };

    var getEditingRowIndexs = function (target) {
        var t = $(target), panel = t.datagrid("getPanel"),
            rows = panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row.datagrid-row-editing").map(function () {
                return window.parseInt($(this).attr("datagrid-row-index"));
            }),
            array = $.array.distinct($.array.clone(rows));
        return array;
    };



    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否被 check；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 被 check，则返回 true，否则返回 false。
        isChecked: function (jq, index) { return isChecked(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否被 select；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 被 select，则返回 true，否则返回 false。
        isSelected: function (jq, index) { return isSelected(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；判断指定的 data-row(数据行) 是否开启行编辑状态；该方法的参数 index 表示要判断的行的索引号，从 0 开始计数；
        //  返回值：如果参数 index 所表示的 data-row(数据行) 正开启行编辑状态，则返回 true，否则返回 false。
        isEditing: function (jq, index) { return isEditing(jq[0], index); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前表格中第一个开启了编辑状态的数据行的索引号(从 0 开始计数)；
        //  返回值：如果当前表格中存在开启了行编辑状态的行，则返回第一个编辑行的行索引号(从 0 开始计数)；否则返回 -1。
        getEditingRowIndex: function (jq) { return getEditingRowIndex(jq[0]); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前表格中所有开启了行编辑状态的行的索引号(从 0 开始计数)；
        //  返回值：返回一个数组，数组中包含当前表格中所有已经开启了行编辑状态的行的索引号(从 0 开始计数)。
        getEditingRowIndexs: function (jq) { return getEditingRowIndexs(jq[0]); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);