/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combobox 扩展
* jeasyui.extensions.combobox.getSelected.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-06
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combobox.extensions");

    function getSelected(target) {
        var t = $(target), opts = t.combobox("options"),
            value = t.combobox("getValue"), data = t.combobox("getData");
        return $.array.first(data, function (val) { return val[opts.valueField] == value; });
    };

    var defaults = $.fn.combobox.extensions.defaults = {};

    var methods = $.fn.combobox.extensions.methods = {

        //  扩展 easyui-combobox 的自定义方法；该方法用于获取当前选择了的项；
        //  返回值：返回一个 JSON-Object，该 JSON-Object 为当前 easyui-combobox 数据源中的一个子项，包含 valueField 和 textField 的值；
        //      如果当前 easyui-combobox 没有选中任何值，则返回 null。
        getSelected: function (jq) { return getSelected(jq[0]); }
    };


    $.extend($.fn.combobox.defaults, defaults);
    $.extend($.fn.combobox.methods, methods);

})(jQuery);