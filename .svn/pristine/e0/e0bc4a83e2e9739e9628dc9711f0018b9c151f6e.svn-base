/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI validatebox 扩展
* jeasyui.extensions.validatebox.rules.remote.js
* 开发 落阳
* 最近更新：2015-11-08
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.validatebox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.validatebox.extensions");

    $.extend($.fn.validatebox.extensions, {
        validate: $.fn.validatebox.methods.isValid,
        rules: {
            // 访问远程地址进行验证；参数 param 中不传入查询的目标字段名称，只传入查询的目标字段的值，后台的参数名只能是 Value 和 Key 。
            // 该规则可用于常规的重复性检验，如“判定用户名是否重复”。
            // param参数对象中各参数次序：
            //      0:string 格式，表示正在校验的内容名称，如“用户名”、“账号”
            //      1:string 格式，表示提供验证的远程地址
            //      2:string 格式，表示要排除校验的主键值（如正在编辑的数据的主键值） 或 该主键值所在的控件的 ID；不传则默认为空值
            //      3:string 格式，表示 param[2] 所表示内容的类型，其值可以是“string”或“id”；不传则默认为 string
            remoteValidWithoutField: {
                validator: function (value, param) {
                    var url = param[1];         //提供验证的远程地址
                    var va = value;             //当前控件的值
                    var type = param.length > 3 ? param[3] : "string";
                    var key = param.length > 2 ? (type == "id" ? $("#" + param[2]).val() : param[2]) : "";         //要排除校验的主键值
                    var pa = {
                        Key: key,
                        Value: va
                    };
                    var result = $.util.requestAjaxBoolean(url, pa);
                    return !result;
                },
                message: '{0}有重复记录'
            }
        }
    });

    $.extend($.fn.validatebox.defaults.rules, $.fn.validatebox.extensions.rules);

})(jQuery);