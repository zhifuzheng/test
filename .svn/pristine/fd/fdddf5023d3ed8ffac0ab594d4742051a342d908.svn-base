/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.selectCheck.js
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

    $.extend($.fn.treegrid.extensions, {
        select: $.fn.treegrid.methods.select,
        unselect: $.fn.treegrid.methods.unselect
    });
    var selectRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        $.fn.treegrid.extensions.select.call(t, t, id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { $.fn.treegrid.extensions.select.call(t, t, this[opts.idField]); });
        }
    };

    var unselectRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        $.fn.treegrid.extensions.unselect.call(t, t, id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { $.fn.treegrid.extensions.unselect.call(t, t, this[opts.idField]); });
        }
    };

    var checkRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        t.datagrid("checkRow", id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { t.datagrid("checkRow", this[opts.idField]); });
        }
    };

    var uncheckRow = function (target, param) {
        param = $.isPlainObject(param) ? param : { id: param, cascade: false };
        var id = param.id, cascade = param.cascade ? true : false, t = $(target);
        t.datagrid("uncheckRow", id);
        if (cascade) {
            var opts = t.treegrid("options");
            $.each(t.treegrid("getChildren", id), function () { t.datagrid("uncheckRow", this[opts.idField]); });
        }
    };


    var defaults = $.fn.treegrid.extensions.defaults = {

    };

    var methods = $.fn.treegrid.extensions.methods = {

        //  重写 easyui-treegrid 的源生方法 select，使之功能更加丰富；
        //  选择 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 idField(主键) 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要选择的行节点的 idField(主键) 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点一并选择。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        select: function (jq, param) { return jq.each(function () { selectRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法，选择 easyui-treegrid 当前页的某行节点数据；
        //  同 select 方法；
        selectRow: function (jq, param) { return jq.each(function () { selectRow(this, param); }); },

        //  重写 easyui-treegrid 的源生方法 unselect，使之功能更加丰富；
        //  不选择 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 idField(主键) 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要选择的行节点的 idField(主键) 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点一并不选择。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        unselect: function (jq, param) { return jq.each(function () { unselectRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；不选择 easyui-treegrid 当前页的某行节点数据；
        //  同 unselect 方法；
        unselectRow: function (jq, param) { return jq.each(function () { unselectRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；选中 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 idField(主键) 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要选中的行节点的 idField(主键) 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点一并选中。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        check: function (jq, param) { return jq.each(function () { checkRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；选中 easyui-treegrid 当前页的某行节点数据；
        //  同 check 方法。
        checkRow: function (jq, param) { return jq.each(function () { checkRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；不选中 easyui-treegrid 当前页的某行节点数据；该方法的参数 param 可以定义为如下两种类型：
        //      1、表示行节点的 idField(主键) 值；
        //      2、JSON-Object 对象，该对象需定义如下属性：
        //          id:     表示要选中的行节点的 idField(主键) 值；
        //          cascade:Boolean 类型值，默认为 false，表示是否连同其子级节点一并不选中。
        //  返回值：返回表示当前 easyui-treegrid 组件的 jQuery 链式对象。
        uncheck: function (jq, param) { return jq.each(function () { uncheckRow(this, param); }); },

        //  扩展 easyui-treegrid 的自定义方法；不选中 easyui-treegrid 当前页的某行节点数据；
        //  同 uncheck 方法。
        uncheckRow: function (jq, param) { return jq.each(function () { uncheckRow(this, param); }); }
    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);