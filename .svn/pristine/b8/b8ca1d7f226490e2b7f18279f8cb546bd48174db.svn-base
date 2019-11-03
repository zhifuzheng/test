/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tabs 扩展
* jeasyui.extensions.tabs.tabState.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-01-18
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tabs.extensions");


    function isSelected(target, which) {
        var tabs = $(target), selected = tabs.tabs("getSelected"), index = tabs.tabs("getTabIndex", selected);
        var thisTab = tabs.tabs("getTab", which), thisIndex = tabs.tabs("getTabIndex", thisTab);
        return thisIndex == index;
    };

    function isClosable(target, which) {
        var tabs = $(target), panel = tabs.tabs("getTab", which), panelOpts = panel.panel("options");
        return panelOpts.closable;
    };

    function isDisabled(target, which) {
        return $(target).tabs("getTab", which).panel("options").tab.is(".tabs-disabled");
    }

    var defaults = $.fn.tabs.extensions.defaults = {

    };

    var methods = $.fn.tabs.extensions.methods = {

        //  扩展 easyui-tabs 的自定义方法；判断指定的选项卡是否被选中；该方法定义如下参数：
        //      which:  要判断的选项卡的 索引号 或者 标题。
        //  返回值：如果指定的选项卡被选中，则返回 true，否则返回 false。
        isSelected: function (jq, which) { return isSelected(jq[0], which); },

        //  扩展 easyui-tabs 的自定义方法；判断指定的选项卡是否可关闭(closable:true)；该方法定义如下参数：
        //      which:  要判断的选项卡的 索引号 或者 标题。
        //  返回值：如果指定的选项卡可被关闭(closable:true)，则返回 true，否则返回 false。
        isClosable: function (jq, which) { return isClosable(jq[0], which); },

        //  扩展 easyui-tabs 的自定义方法；判断指定的选项卡是否已被禁用；该方法定义如下参数：
        //      which:  要判断的选项卡的 索引号 或者 标题。
        //  返回值：如果指定的选项卡已被禁用（被执行 disableTab 操作），则返回 true，否则返回 false。
        isDisabled: function (jq, which) { return isDisabled(jq[0], which); }
    };


    $.extend($.fn.tabs.defaults, defaults);
    $.extend($.fn.tabs.methods, methods);

})(jQuery);