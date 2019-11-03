/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI validatebox 扩展
* jeasyui.extensions.validatebox.updateRules.js
* 开发 落阳
* 最近更新：2016-03-10
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.rules.js（可以不依赖）
*
* Copyright (c) 2016 Lixilin personal All rights reserved.
*/
(function ($, undefined) {

    $.util.namespace("$.fn.validatebox.extensions");

    var rules = {

        // 强行验证失败，有些特殊的情况下需要用到。结合扩展方法“addValidType、removeValidType”使用。
        forceValidFail: {
            validator: function (value, param) {
                var result = param[1];
                return result;
            },
            message: "{0}"
        }
    };
    $.extend($.fn.validatebox.defaults.rules, rules);


    //从规则集合中获取“存在于现有validatebox规则集合”中的那部分规则
    function getEffectValidType(rs) {
        var thisRules = [];
        var allRules = $.fn.validatebox.defaults.rules;
        rs.forEach(function (validType) {
            var types = /([a-zA-Z_]+)(.*)/.exec(validType);
            var tempRules = allRules[types[1]];
            //var validParams = eval(types[2]);
            if (tempRules != undefined) { thisRules.push(validType); }
        });

        return thisRules;
    }

    function addValidType(target, rs) {
        var currentRules = [];
        if ($.util.isString(rs)) { currentRules.push(rs); }
        else if ($.util.isArray(rs)) { currentRules = rs; }
        else { return; }
        if (currentRules.length == 0) { return; }

        var thisRules = getEffectValidType(currentRules);
        if (thisRules.length == 0) { return; }

        var t = $(target), opts = t.validatebox("options");
        var _validType = opts.validType ? ($.array.likeArrayNotString(opts.validType) ? opts.validType : [opts.validType]) : [];
        if (_validType.length && _validType.length >= 0) {
            thisRules.forEach(function (rule) {
                _validType.push(rule);
            });
        }
        else {
            _validType = thisRules;
        }
        opts.validType = _validType;
        t.validatebox("validate");
    };

    function removeValidType(target, rs) {
        var currentRules = [];
        if ($.util.isString(rs)) { currentRules.push(rs); }
        else if ($.util.isArray(rs)) { currentRules = rs; }
        else { return; }
        if (currentRules.length == 0) { return; }

        var thisRules = getEffectValidType(currentRules);
        if (thisRules.length == 0) { return; }

        var t = $(target), opts = t.validatebox("options");
        var _validType = opts.validType;
        if (_validType.length && _validType.length > 0) {
            thisRules.forEach(function (rule) {
                $.array.remove(_validType, rule, function (item) { return rule == item; });
            });
            opts.validType = _validType;
            t.validatebox("validate");
        }
    }

    var methods = $.fn.validatebox.extensions.methods = {

        // 扩展 easyui-validatebox 控件的自定义方法；用于添加 easyui-validatebox 控件的验证规则，该方法定义如下参数：
        //      rules:   表示要动态添加的规则，该参数可以是如下类型
        //          1、String类型：表示一个要动态添加的规则；
        //          2、Array类型：表示一组要动态添加的规则；
        //  返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象。
        addValidType: function (jq, rules) { return jq.each(function () { addValidType(this, rules); }); },

        // 扩展 easyui-validatebox 控件的自定义方法；用于移除 easyui-validatebox 控件的验证规则，该方法定义如下参数：
        //      rules:   表示要动态移除的规则，该参数可以是如下类型
        //          1、String类型：表示一个要动态移除的规则；
        //          2、Array类型：表示一组要动态移除的规则；
        //  返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象。
        removeValidType: function (jq, rules) { return jq.each(function () { removeValidType(this, rules); }); }
    };


    $.extend($.fn.validatebox.methods, methods);

})(jQuery);