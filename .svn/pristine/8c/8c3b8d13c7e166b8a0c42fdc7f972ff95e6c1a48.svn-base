/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tabs 扩展
* jeasyui.extensions.tabs.setTitle.js
* 开发 落阳
* 最近更新：2016-02-02
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tabs.extensions");


    function setTitle(target, param) {
        if (!param || !(param.which || $.isNumeric(param.which)) || !param.title) { return; }
        var t = $(target), tab = t.tabs("getTab", param.which);
        t.tabs("update", {
            tab: tab,
            type:"header",
            options: {
                title: param.title
            }
        });
    };


    var defaults = $.fn.tabs.extensions.defaults = {

    };

    var methods = $.fn.tabs.extensions.methods = {

        //  扩展 easyui-tabs 的自定义方法；重设指定选项卡的标题名；该方法定义如下参数：
        //      param:  这是一个 JSON-Object 对象，该对象定义如下属性：
        //          which: 需要重设标题名的选项卡的 索引号(index) 或者原标题名(title)；
        //          title: 新的标题名；
        //  返回值：返回当前选项卡控件 easyui-tabs 的 jQuery 对象。
        setTitle: function (jq, param) { return jq.each(function () { setTitle(this, param); }); }
    };


    $.extend($.fn.tabs.defaults, defaults);
    $.extend($.fn.tabs.methods, methods);

})(jQuery);