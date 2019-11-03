/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tabs 扩展
* jeasyui.extensions.tabs.getTabInfo.js
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


    function getTabOption(target, which) {
        var t = $(target), tab = t.tabs("getTab", which), tabOpts = tab.panel("options");
        return tabOpts;
    };

    function getSelectedOption(target) {
        var t = $(target), tab = t.tabs("getSelected"), tabOpts = tab.panel("options");
        return tabOpts;
    };

    function getSelectedIndex(target) {
        var t = $(target), tab = t.tabs("getSelected"), index = t.tabs("getTabIndex", tab);
        return index;
    };

    function getSelectedTitle(target) {
        var t = $(target), tabOpts = t.tabs("getSelectedOption"), title = tabOpts.title;
        return title;
    };


    var defaults = $.fn.tabs.extensions.defaults = {

    };

    var methods = $.fn.tabs.extensions.methods = {

        //  扩展 easyui-tabs 的自定义方法；获取指定选项卡的属性值集合(option)；
        //  返回值：一个 Json-object 对象，表示指定选项卡的属性值集合(option)。
        getTabOption: function (jq, which) { return getTabOption(jq[0], which); },

        //  扩展 easyui-tabs 的自定义方法；获取当前选中的选项卡的属性值集合 (option)；
        //  返回值：一个 Json-object 对象，表示当前选中的选项卡的属性值集合(option)。
        getSelectedOption: function (jq) { return getSelectedOption(jq[0]); },

        //  扩展 easyui-tabs 的自定义方法；获取当前选中的选项卡的索引号；
        //  返回值：一个 Json-object 对象，表示当前选中的选项卡的索引号。
        getSelectedIndex: function (jq) { return getSelectedIndex(jq[0]); },

        //  扩展 easyui-tabs 的自定义方法；获取当前选中的选项卡的标题。
        //  返回值：一个 Json-object 对象，表示当前选中的选项卡的标题。
        getSelectedTitle: function (jq) { return getSelectedTitle(jq[0]); }
    };


    $.extend($.fn.tabs.defaults, defaults);
    $.extend($.fn.tabs.methods, methods);

})(jQuery);