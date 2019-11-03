/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combobox 扩展
* jeasyui.extensions.combobox.findItem.js
* 开发 落阳
* 最近更新：2015-11-06
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 Lixilin personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combobox.extensions");

    var findItem = function (target, param) {
        var t = $(target), data = t.combobox("getData"), opts = t.combobox("options");
        return $.array.first(data, $.isFunction(param) ? param : function (val) { return val[opts.valueField] == param; });
    };

    var defaults = $.fn.combobox.extensions.defaults = {};

    var methods = $.fn.combobox.extensions.methods = {

        //  扩展 easyui-combobox 的自定义方法；获取符合查找内容的一项；该方法定义如下参数：
        //      param:  表示查找的内容；该方法的参数 param 可以是以下两种类型：
        //          待查找的项数据的 valueField 字段值；
        //          function 类型，该回调函数签名为 function(item, index, items)，其中 item 表示项数据对象、index 表示行索引号、items 表示当前 easyui-combobox 调用 getData 返回的结果集；
        //          如果 param 参数为 function 类型，则 findItem 方法会对当前 easyui-combobox 的每一项数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并返回该项数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 items 直到最后并返回 null。
        //  返回值：返回一个 JSON-Object，该 JSON-Object 为当前 easyui-combobox 数据源中的一个子项，包含 valueField 和 textField 的值；如果未找到相应数据，则返回 null。
        findItem: function (jq, param) {
            return findItem(jq[0], param);
        }
    };


    $.extend($.fn.combobox.defaults, defaults);
    $.extend($.fn.combobox.methods, methods);

})(jQuery);