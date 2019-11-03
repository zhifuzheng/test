/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.getColumnData.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-25
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    var getColumnData = function (target, field) {
        var t = $(target), rows = t.datagrid("getRows");
        return $.array.map(rows, function (val) { return val[field]; });
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；获取 easyui-datagrid 中当前也指定列所有行的单元格数据所构成的一个数组；该函数定义如下参数：
        //      field: 要获取的数据的列的 field 名；
        //  返回值：返回一个数组，数组中每一个元素都是其数据行的该列的值，数组的长度等于 grid.datagrid("getRows") 的长度；
        //          如果传入的列名不存在，则返回数组的长度同样等于 grid.datagrid("getRows") 的长度，只是每个元素的值都为 undefined.
        getColumnData: function (jq, field) { return getColumnData(jq[0], field); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);