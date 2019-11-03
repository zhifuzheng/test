/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI panel 扩展
* jeasyui.extensions.panel.setTitle.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-11
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.base.isEasyUI.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.panel.extensions");

    $.extend($.fn.panel.extensions, {
        header: $.fn.panel.methods.header,
        setTitle: $.fn.panel.methods.setTitle
    });

    function isTab(target) {
        var t = $(target),
            panel = t.panel("panel"),
            panels = panel.parent(),
            container = panels.parent();
        return panels.hasClass("tabs-panels") && container.hasClass("tabs-container") && $.easyui.isComponent(container, "tabs");
    }

    function getHeader(target) {
        var t = $(target);
        if (!isTab(target))
            return $.fn.panel.extensions.header.call(t, t);

        var panel = t.panel("panel"),
            index = panel.index(),
            tabs = panel.closest(".tabs-container");
        return tabs.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li").eq(index);
    }

    function setTitle(target, title) {
        var t = $(target);
        if (!isTab(target))
            return $.fn.panel.extensions.setTitle.call(t, t, title);

        var opts = t.panel("options"),
            header = t.panel("header");
        header.find(">a.tabs-inner>span.tabs-title").html(opts.title = title);
    }

    var methods = $.fn.panel.extensions.methods = {

        //  重写 easyui-panel 控件的 header 方法，支持位于 easyui-tabs 中的 tab-panel 部件获取 header 对象；
        //  备注：如果该 panel 位于 easyui-tabs 中，则该方法返回 easyui-tabs 的 div.tabs-header div.tabs-wrap ul.tabs 中对应该 tab-panel 的 li 对象。
        header: function (jq) { return getHeader(jq[0]); },

        //  重写 easyui-panel 控件的 setTitle 方法，支持位于 easyui-tabs 中的 tab-panel 部件设置 title 操作；
        //  返回值：返回当前选项卡控件 easyui-panel 的 jQuery 链式对象。
        setTitle: function (jq, title) { return jq.each(function () { setTitle(this, title); }); }
    };

    var defaults = $.fn.panel.extensions.defaults = {

    };

    $.extend($.fn.panel.defaults, defaults);
    $.extend($.fn.panel.methods, methods);

})(jQuery);