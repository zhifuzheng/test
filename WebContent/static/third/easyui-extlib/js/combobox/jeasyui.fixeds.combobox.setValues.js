/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combobox 扩展
* jeasyui.fixeds.combobox.setValues.js
* 开发 落阳
* 最近更新：2016-03-14
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2016 Lixilin personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combobox.extensions");

    function setValues(target, values, remainText) {
        var opts = $.data(target, 'combobox').options;
        var panel = $(target).combo('panel');
        if (typeof (values) == "string" && values.length == 0) {
            var data = $(target).combobox("getData"), containEmpty = false;
            for (var i = 0; i < data.length; i++) {
                var item = data[i], val = item[opts.valueField];
                if (val == values) { containEmpty = true; break; }
            }
            if (!containEmpty) {
                values = [];
            }
        }
        if (!$.isArray(values)) { values = values.split(opts.separator) }
        panel.find('div.combobox-item-selected').removeClass('combobox-item-selected');
        var vv = [], ss = [];
        for (var i = 0; i < values.length; i++) {
            var v = values[i];
            var s = v;
            opts.finder.getEl(target, v).addClass('combobox-item-selected');
            var row = opts.finder.getRow(target, v);
            if (row) {
                s = row[opts.textField];
            }
            vv.push(v);
            ss.push(s);
        }

        if (!remainText) {
            $(target).combo('setText', ss.join(opts.separator));
        }
        $(target).combo('setValues', vv);
    }

    var defaults = $.fn.combobox.extensions.defaults = {};

    var methods = $.fn.combobox.extensions.methods = {

        //  重写 easyui-combobox 的 setValues 方法；以修复 reset 后可能导致 input 中显示多余的分割符的 bug ；该方法定义如下参数：
        //      values:  表示要设置的项数据的 valueField 字段值；可以是以下两种类型：
        //          array 类型，其中每个元素都是要设置的项数据的 valueField 字段值；
        //          string 类型，表示要设置的项数据的 valueField 字段值；
        //  返回值：返回表示当前 easyui-combobox jQuery 链式对象。
        setValues: function (jq, values) { return jq.each(function () { setValues(this, values); });}
    };


    $.extend($.fn.combobox.defaults, defaults);
    $.extend($.fn.combobox.methods, methods);

})(jQuery);