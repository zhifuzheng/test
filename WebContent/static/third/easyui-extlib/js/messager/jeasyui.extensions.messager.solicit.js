/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI messager 扩展
* jeasyui.extensions.messager.solicit.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-25
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.messager.buttonsbox.js
*   3、jeasyui.extensions.messager.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.easyui");


    $.easyui.messager = $.util.isUtilTop ? $.messager : ($.util.$ && $.util.$.messager ? $.util.$.messager : $.messager);

    var defaults = {
        title: "操作提醒",
        yes: "是",
        no: "否",
        ok: "确定",
        cancel: "取消",
        icon: "info",
        position: "topCenter",
        confirmMsg: "您确认要进行该操作？",
        solicitMsg: "您确认要进行该操作？",
        promptMsg: "请输入：",
        progressMsg: "正在处理，请稍等...",
        progressText: "正在处理，请稍等...",
        buttonsboxMsg: "您确认要进行该操作？",
        interval: 300
    };

    if ($.messager.defaults) {
        $.extend($.messager.defaults, defaults);
    } else {
        $.messager.defaults = defaults;
    }

    $.messager.icons = {
        "error": "messager-error",
        "info": "messager-info",
        "question": "messager-question",
        "warning": "messager-warning"
    };
    $.messager.original = {
        show: $.messager.show,
        alert: $.messager.alert,
        confirm: $.messager.confirm,
        prompt: $.messager.prompt,
        progress: $.messager.progress
    };


    // 增加 $.messager.solicit 方法，该方法弹出一个包含三个按钮("是"、"否" 和 "取消")的对话框，点击任意按钮或者关闭对话框时，以执行指定的回调函数；
    // 该方法提供如下方式重载：
    //      function (msg)
    //      function (msg, handler)
    //      function (title, msg)
    //      function (title, msg, handler)
    //      function (options)，options 的格式如 { title, msg, yes, no, cancel, closeOnClick, handler, width, height, buttonsAlign }
    // 上述重载的命名参数中：
    //      title   :
    //      msg     :
    //      handler :
    //      options :
    //      width   :
    //      height  :
    //      buttonsAlign:
    // 返回值：该方法返回一个表示弹出消息框的 easyui-window 对象，这是一个 jQuery 链式对象。
    $.messager.solicit = function (title, msg, handler) {
        var type = typeof arguments[0];
        if (type != "object" && type != "function") {
            if (arguments.length == 1) {
                return $.messager.solicit({ msg: arguments[0] });
            }
            if (arguments.length == 2) {
                var options = $.isFunction(arguments[1])
                    ? { msg: arguments[0], handler: arguments[1] }
                    : { title: arguments[0], msg: arguments[1] };
                return $.messager.solicit(options);
            }
            if (arguments.length > 2) {
                return $.messager.solicit({ title: arguments[0], msg: arguments[1], handler: arguments[2] });
            }
        }
        var opts = $.extend({}, $.messager.solicit.defaults, arguments[0] || {});
        $.extend(opts, {
            buttons: [
                { text: opts.yes, value: true },
                { text: opts.no, value: false },
                { text: opts.cancel, value: undefined }
            ]
        });
        return $.messager.buttonsbox(opts);
    };

    $.messager.solicit.defaults = {
        title: defaults.title,
        yes: defaults.yes,
        no: defaults.no,
        cancel: defaults.cancel,
        msg: defaults.solicitMsg
    };


})(jQuery);