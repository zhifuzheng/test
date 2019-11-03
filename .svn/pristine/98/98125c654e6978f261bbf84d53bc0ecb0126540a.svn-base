/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.getCellData.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-25
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.getRowData.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    var getCellData = function (target, pos) {
        if (!pos || !pos.field || !$.isNumeric(pos.index) || pos.index < 0) { return; }
        var t = $(target), row = t.datagrid("getRowData", pos.index);
        return row ? row[pos.field] : row;
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；获取 easyui-datagrid 中当前页指定单元格的数据；该函数定义如下参数：
        //  pos：表示单元格的位置，为一个 JSON-Object 对象，该 JSON 定义如下属性：
        //          field:  表示要获取的单元格位于哪个列；
        //          index:  表示要获取的单元格位于哪个行的行索引号，从 0 开始；
        //  返回值：如果当前页存在指定列的指定行，则返回该列中指定行及指定列的单元格数据；否则返回 undefined。
        getCellData: function (jq, pos) { return getCellData(jq[0], pos); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);