/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.rowState.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-11
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.treegrid.extensions");

    var isChecked = function (target, id) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getChecked");
        return $.array.contains(rows, id, function (val) { return val[opts.idField] == id; });
    };

    var isSelected = function (target, id) {
        var t = $(target), opts = t.treegrid("options"), rows = t.treegrid("getSelections");
        return $.array.contains(rows, id, function (val) { return val[opts.idField] == id; });
    };

    var isEditing = function (target, id) {
        var t = $(target), panel = t.treegrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[node-id=" + id + "]").hasClass("datagrid-row-editing");
    };

    var defaults = $.fn.treegrid.extensions.defaults = {

    };

    var methods = $.fn.treegrid.extensions.methods = {

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否被 check；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 被 check，则返回 true，否则返回 false。
        isChecked: function (jq, id) { return isChecked(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否被 select；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 被 select，则返回 true，否则返回 false。
        isSelected: function (jq, id) { return isSelected(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否开启行编辑状态；该方法的参数 id 表示要判断的节点的 idField 值；
        //  返回值：如果参数 id 所表示的 tree-node 正开启行编辑状态，则返回 true，否则返回 false。
        isEditing: function (jq, id) { return isEditing(jq[0], id); }
    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);