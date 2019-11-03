/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI messager 扩展
* jeasyui.extensions.messager.show.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-25
*
* 依赖项：
*   1、jquery.jdirk.js
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


    // 重写 $.messager.show 方法，使其支持图标以及默认的单个字符串参数的重载；该方法提供如下重载：
    //      function (msg)
    //      function (title, msg)
    //      function (title, msg, icon)
    //      function (title, msg, icon, position)
    //      function (options)，options 的格式如 { title, msg, icon, position }
    // 上述重载的命名参数中：
    //      msg     :
    //      title   :
    //      icon    :
    //      options :
    // 返回值：该方法返回一个表示弹出消息框的 easyui-window 对象，这是一个 jQuery 链式对象。
    $.messager.show = function (options) {
        var type = typeof arguments[0];
        if (type != "object" && type != "function") {
            return arguments.length == 1
                ? $.messager.show({ msg: arguments[0] })
                : $.messager.show({ title: arguments[0], msg: arguments[1], icon: arguments[2], position: arguments[3] });
        }
        var positions = {
            topLeft: { showType: "show", style: { right: '', left: 0, top: document.body.scrollTop + document.documentElement.scrollTop, bottom: '' } },
            topCenter: { showType: "slide", style: { right: '', top: document.body.scrollTop + document.documentElement.scrollTop, bottom: '' } },
            topRight: { showType: "show", style: { left: '', right: 0, top: document.body.scrollTop + document.documentElement.scrollTop, bottom: '' } },
            centerLeft: { showType: "fade", style: { left: 0, right: '', bottom: '' } },
            center: { showType: "fade", style: { right: '', bottom: '' } },
            centerRight: { showType: "fade", style: { left: '', right: 0, bottom: '' } },
            bottomLeft: { showType: "show", style: { left: 0, right: '', top: '', bottom: -document.body.scrollTop - document.documentElement.scrollTop } },
            bottomCenter: { showType: "slide", style: { right: '', top: '', bottom: -document.body.scrollTop - document.documentElement.scrollTop } },
            bottomRight: { showType: "show" }
        },
            position = arguments[0].position || $.messager.show.defaults.position,
            opts = $.extend({}, $.messager.show.defaults, positions[position], arguments[0] || {});

        var icon = $.messager.icons[opts.icon];
        opts.msg = "
" + String(opts.msg) + "
";
        return $.messager.original.show(opts);
    };
    $.union($.messager.show, $.messager.original.show);

    $.messager.show.defaults = {
        title: defaults.title,
        icon: defaults.icon,
        position: defaults.position
    };


})(jQuery);