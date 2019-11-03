/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI messager 扩展
* jeasyui.extensions.messager.buttonsbox.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-25
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.messager.css
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


    // 增加 $.messager.buttonsbox 方法，该方法用于创建一个包含多个按钮的对话框，点击对话框中的任意按钮，以执行指定的回调函数；
    // 该方法提供如下方式重载：
    //      function (buttons)
    //      function (buttons, handler)
    //      function (msg, buttons)
    //      function (msg, buttons, handler)
    //      function (title, msg, buttons)
    //      function (title, msg, buttons, handler)
    //      function (options)，options 的格式如 { title, msg, buttons, closeOnClick, handler, width, height }
    //  上述重载的命名参数中：
    //      title   : 表示对话框的标题；
    //      msg     : 表示对话框内的文本提示内容；
    //      buttons : 该参数为一个 Array 数组格式，数组中的每一项都应是一个格式如 { iconCls: string, text: string, value: string, plain: boolean } 的 JSON-Object。
    //      handler : 该参数为一个格式如 function (index, value) 的回调函数，其中
    //          index : 表示被点击按钮的索引号（从 0 开始计数，如果该值在回调函数执行时为 -1，则表示是由于对话框被点击关闭按钮而引发的事件）；
    //          value : 表示按钮定义的 value 属性值。
    //          在点击按钮时，handler 回调函数内的 this 指向按钮本身的 HTML-DOM 对象；
    //          如果是对话框被点击关闭按钮，则 handler 回调函数内的 this 指向表示对话框 easyui-window 窗体 body 的 HTML-DOM 对象。
    //      closeOnClick : 表示在点击按钮后，是否关闭对话框窗体；Boolean 类型值，默认为 true。
    //      width   : Number 类型；表示对话框的宽度；可选，一般用于对话框包含很多按钮的情况下，用于增加对话框的宽度以免按钮换行；
    //      height  : Number 类型；表示对话框的高度；可选。
    //      buttonsAlign: String 类型；表示按钮靠左或者靠右显示；可选的值为 "left"、"center" 或 "right"，默认为 "center"
    // 返回值：该方法返回一个表示弹出消息框的 easyui-window 对象，这是一个 jQuery 链式对象。
    $.messager.buttonsbox = function (title, msg, buttons, handler) {
        var type = typeof arguments[0];
        if (type != "object" && type != "function") {
            if (arguments.length == 1) {
                if ($.util.likeArrayNotString(arguments[0])) {
                    return $.messager.buttonsbox({ buttons: arguments[0] });
                }
            }
            if (arguments.length == 2) {
                var options = $.isFunction(arguments[1])
                    ? { buttons: arguments[0], handler: arguments[1] }
                    : { msg: arguments[0], buttons: arguments[1] };
                return $.messager.buttonsbox(options);
            }
            if (arguments.length == 3) {
                var options = $.isFunction(arguments[2])
                    ? { msg: arguments[0], buttons: arguments[1], handler: arguments[2] }
                    : { title: arguments[0], msg: arguments[1], buttons: arguments[2] };
                return $.messager.buttonsbox(options);
            }
            if (arguments.length > 3) {
                return $.messager.buttonsbox({ title: arguments[0], msg: arguments[1], buttons: arguments[2], handler: arguments[3] });
            }
        }
        if ($.util.likeArrayNotString(arguments[0])) {
            return $.messager.buttonsbox({ buttons: arguments[0], handler: arguments[1] });
        }
        var opts = $.extend({}, $.messager.buttonsbox.defaults, arguments[0] || {}),
            win = $.messager.confirm(opts.title, opts.msg, opts.handler),
            winopts = win.window("options"),
            onClose = winopts.onClose,
            buttons = win.find(".messager-button").empty();
        winopts.onClose = function () {
            if ($.isFunction(onClose)) { onClose.apply(this, arguments); }
            if ($.isFunction(opts.handler)) { opts.handler.call(this, -1, undefined); }
        };
        opts.buttons = opts.buttons.length ? opts.buttons : [{ text: "确定" }];
        $.each(opts.buttons, function (i, n) {
            var item = $.extend({}, $.messager.buttonsbox.item, n || {}, {
                onClick: function () {
                    if (opts.closeOnClick) {
                        winopts.onClose = onClose;
                        win.window("close");
                    }
                    if ($.isFunction(opts.handler)) { opts.handler.call(this, i, item.value); }
                }
            });
            $("").attr({ index: i, value: item.value }).appendTo(buttons).linkbutton(item);
        });
        if (opts.buttonsAlign) {
            if (!$.array.contains(["left", "center", "right"], opts.buttonsAlign))
                opts.buttonsAlign = "center";

            if (opts.buttonsAlign == "left" || opts.buttonsAlign == "right")
                buttons.css("float", opts.buttonsAlign);
        }

        if (opts.width || opts.height) {
            win.window("resize", opts);
        }
        win.children("div.messager-button").children("a:first").focus();
        return win;
    };

    $.messager.buttonsbox.defaults = {
        title: defaults.title,
        msg: defaults.buttonsboxMsg,
        buttons: [],
        closeOnClick: true,
        handler: function (index, value) { }
    };
    $.messager.buttonsbox.item = { iconCls: null, text: "", value: undefined, plain: false };


})(jQuery);