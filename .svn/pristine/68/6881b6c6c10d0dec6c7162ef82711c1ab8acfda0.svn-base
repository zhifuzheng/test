/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI panel 扩展
* jeasyui.extensions.panel.position.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-10
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.base.isEasyUI.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.panel.extensions");


    function isRegion(target) {
        var t = $(target),
            body = t.panel("body"),
            panel = t.panel("panel");
        return body.hasClass("layout-body") && panel.hasClass("layout-panel");
    }

    function isTab(target) {
        var t = $(target),
            panel = t.panel("panel"),
            panels = panel.parent(),
            container = panels.parent();
        return panels.hasClass("tabs-panels") && container.hasClass("tabs-container") && $.easyui.isComponent(container, "tabs");
    }

    function isAccordion(target) {
        var t = $(target),
            body = t.panel("body"),
            panel = t.panel("panel"),
            container = panel.parent();
        return body.hasClass("accordion-body") && container.hasClass("accordion") && $.easyui.isComponent(container, "accordion")
            ? true
            : false;
    }

    function isWindow(target) {
        var t = $(target),
            body = t.panel("body");
        return body.hasClass("window-body") && body.parent().hasClass("window") && $.easyui.isComponent(target, "window");
    }

    function isDialog(target) {
        var t = $(target),
            body = t.panel("body");
        return isWindow(target) && $.easyui.isComponent(target, "dialog");
    }

    var methods = $.fn.panel.extensions.methods = {

        //  扩展 easyui-panel 控件的自定义方法；判断当前 easyui-panel 是否为 easyui-layout 的 region-panel 部件；
        //  返回值：如果当前 easyui-panel 是 easyui-layout 的 region-panel 部件，则返回 true，否则返回 false。
        isRegion: function (jq) { return isRegion(jq[0]); },

        //  扩展 easyui-panel 控件的自定义方法；判断当前 easyui-panel 是否为 easyui-tabs 的选项卡；
        //  返回值：如果当前 easyui-panel 是 easyui-tabs 的 tab-panel 部件，则返回 true，否则返回 false。
        isTab: function (jq) { return isTab(jq[0]); },

        //  扩展 easyui-panel 控件的自定义方法；判断当前 easyui-panel 是否为 easyui-accordion 中的一个折叠面板；
        //  返回值：如果当前 easyui-panel 是 easyui-accordion 的 panel 部件，则返回 true，否则返回 false。
        isAccordion: function (jq) { return isAccordion(jq[0]); },

        //  扩展 easyui-panel 控件的自定义方法；判断当前 easyui-panel 是否为 easyui-window 组件；
        //  返回值：如果当前 easyui-panel 是 easyui-window 的 panel 部件，则返回 true，否则返回 false。
        isWindow: function (jq) { return isWindow(jq[0]); },

        //  扩展 easyui-panel 控件的自定义方法；判断当前 easyui-panel 是否为 easyui-dialog 组件；
        //  返回值：如果当前 easyui-panel 是 easyui-dialog 的 panel 部件，则返回 true，否则返回 false。
        isDialog: function (jq) { return isDialog(jq[0]); }
    };

    var defaults = $.fn.panel.extensions.defaults = {

    };

    $.extend($.fn.panel.defaults, defaults);
    $.extend($.fn.panel.methods, methods);

})(jQuery);