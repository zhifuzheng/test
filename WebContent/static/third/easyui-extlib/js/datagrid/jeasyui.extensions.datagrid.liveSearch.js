/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.liveSearch.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-24
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    var liveSearch = function (target, param) {
        var t = $(target), panel = t.datagrid("getPanel"), cells, field, value = param, regular = false, ignoreCase = true, regexp;
        if ($.isPlainObject(param)) {
            value = param.value;
            field = param.field;
            regular = param.regular;
            ignoreCase = param.ignoreCase;
            cells = panel.find("div.datagrid-body tr.datagrid-row td[" + (field ? "field=" + field : "field") + "] div.datagrid-cell");
        } else {
            cells = panel.find("div.datagrid-body tr.datagrid-row td[field] div.datagrid-cell");
        }
        
        regexp = regular ? new RegExp(value, ignoreCase ? "gm" : "igm") : value;
        cells.each(function () {
            var cell = $(this);
            cell.find("span.datagrid-cell-hightlight").replaceWith(function () { return $(this).text(); });

            if (!value) { return; }
            var text = cell.html(); if (!text) { return; }
            cell.html($.string.replaceAll(text, value, "" + value + ""));
        });
    };

    var clearLiveHighLight = function (target, field) {
        var t = $(target), panel = t.datagrid("getPanel"), cells;
        if (field == null || field == undefined) {
            cells = panel.find("div.datagrid-body tr.datagrid-row td[field] div.datagrid-cell");
        }
        else {
            cells = panel.find("div.datagrid-body tr.datagrid-row td[field=" + String(field) + "] div.datagrid-cell");
        }

        cells.each(function () {
            var cell = $(this);
            cell.find("span.datagrid-cell-hightlight").replaceWith(function () { return $(this).text(); });
        });
    };

    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；对当前 easyui-datagrid 中进行高亮关键词查询；该方法的 param 可以定义为如下两种类型：
        //      1、String 类型值：表示要对所有列进行的高亮查询关键词；
        //      2、JSON-Object：表示对特定列进行高亮查询的参数，该对象类型参数包含如下属性：
        //          field:      表示要进行高亮查询的列；
        //          value:      表示要进行高亮查询的关键词；
        //          regular:    Boolean 类型值，默认为 false；指示该关键词是否为正则表达式；
        //          ignoreCase: Boolean 类型值，默认为 true；指示高亮查询时是否忽略大小写。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        liveSearch: function (jq, param) { return jq.each(function () { liveSearch(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；清除当前 easyui-datagrid 中进行高亮关键词查询后产生的高亮效果；该方法的 field 可以定义为如下类型：
        //      1、String 类型值：表示要对特定列的高亮效果进行清除，不提供该参数则表示要清除所有列的高亮效果；
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        clearLiveHighLight: function (jq, field) { return jq.each(function () { clearLiveHighLight(this, field); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);