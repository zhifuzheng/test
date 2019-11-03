/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI progressbar 扩展
* jeasyui.extensions.progressbar.setText.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-16
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.progressbar.extensions");


    function setText(target, text) {
        var t = $(target), opts = t.progressbar("options");
        t.find(".progressbar-text").text(text);
    };


    var defaults = $.fn.progressbar.extensions.defaults = {};

    var methods = $.fn.progressbar.extensions.methods = {

        //  扩展 easyui-progressbar 的自定义方法；设置当前 easyui-progressbar 控件的 text 值；该方法的参数 text 表示将被设置的 text 值；
        //  返回值：返回表示当前 easyui-progressbar 的 jQuery 链式对象。
        setText: function (jq, text) { return jq.each(function () { setText(this, text); }); }
    };


    $.extend($.fn.progressbar.defaults, defaults);
    $.extend($.fn.progressbar.methods, methods);

})(jQuery);