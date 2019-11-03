/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI layout 扩展
* jeasyui.extensions.layout.panels.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-16
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.layout.extensions");


    function getPanels(target, containsCenter) {
        var t = $(target),
            flag = (containsCenter == null || containsCenter == undefined) ? true : containsCenter,
            regions = flag ? ["north", "west", "east", "center", "south"] : ["north", "west", "east", "south"];
        return $.array.reduce(regions, function (prev, val, index) {
            var p = t.layout("panel", val);
            if (p && p.length) {
                prev.push({ region: val, panel: p });
                prev[val] = p;
            }
            return prev;
        }, []);
    }

    var methods = $.fn.layout.extensions.methods = {

        // 扩展 easyui-layout 组件的自定义方法；获取 easyui-layout 组件的所有 panel 面板；
        // 该方法的参数 containsCenter 是一个 boolean 类型值，默认为 true；表示返回的数组中是否包含 center panel。
        // 返回值：该方法返回一个 Array 数组对象；数组中的每个元素都是一个包含如下属性定义的 JSON-Object：
        //     region  : String 类型值，表示该面板所在的位置，可能的值为 "north"、"west"、"east"、"center"、"south"；
        //     panel   : jQuery 对象，表示 easyui-panel 面板对象；
        panels: function (jq, containsCenter) { return getPanels(jq[0], containsCenter); }
    };

    var defaults = $.fn.layout.extensions.defaults = {

    };

    $.extend($.fn.layout.defaults, defaults);
    $.extend($.fn.layout.methods, methods);

})(jQuery);