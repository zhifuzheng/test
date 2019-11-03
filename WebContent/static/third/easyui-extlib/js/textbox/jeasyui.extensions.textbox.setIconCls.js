/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI textbox 扩展
* jeasyui.extensions.textbox.setIconCls.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-30
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.textbox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.textbox.extensions");


    function getAddon(target) {
        var state = $.data(target, "textbox"),
            opts = state.options,
            addon = state.textbox.find(".textbox-addon");
        if (!addon.length) {
            var tb = state.textbox.find(".textbox-text"),
                btn = state.textbox.find(".textbox-button"),
                iconAlign = opts.iconAlign ? opts.iconAlign : opts.iconAlign = $.fn.textbox.defaults.iconAlign;

            addon = $("").insertBefore(tb).css({
                left: (iconAlign == "left" ? (opts.buttonAlign == "left" ? btn._outerWidth() : 0) : ""),
                right: (iconAlign == "right" ? (opts.buttonAlign == "right" ? btn._outerWidth() : 0) : "")
            });
        }
        return addon;
    }

    function setIconCls(target, iconCls) {
        var t = $(target),
            state = $.data(target, "textbox"),
            opts = state.options,
            addon = getAddon(target);
        if (iconCls) {
            if (opts.iconCls) {
                var icon = addon.find(".textbox-icon:last");
                if (icon.length) {
                    var disabled = icon.is(".textbox-icon-disabled");
                    icon.attr("class", "textbox-icon " + iconCls);
                    if (disabled) {
                        icon.addClass("textbox-icon-disabled");
                    }
                } else {
                    createIcon();
                }
            } else {
                createIcon();
            }
            function createIcon() {
                $("").appendTo(addon);
                addon.find(".textbox-icon").each(function (i) {
                    $(this).attr("icon-index", i);
                });
            }
        } else {
            if (opts.iconCls && addon.length) {
                addon.find(".textbox-icon:last").filter("." + opts.iconCls).remove();
            }
            if (addon.length && !addon.children()) {
                addon.remove();
            }
        }
        opts.iconCls = iconCls;
        t.textbox("resize");
    }


    var defaults = $.fn.textbox.extensions.defaults = {};

    var methods = $.fn.textbox.extensions.methods = {

        // 扩展 easyui-textbox 控件的自定义方法；用于设置 easyui-textbox 的显示图标属性 iconCls；
        // 该方法的参数 iconCls 表示被设置的图标的 iconCls 属性，为一个 String 类型值。
        // 返回值：返回表示当前 easyui-textbox 控件的 jQuery 链式对象。
        setIconCls: function (jq, iconCls) { return jq.each(function () { setIconCls(this, iconCls); }); }
    };

    $.extend($.fn.textbox.defaults, defaults);
    $.extend($.fn.textbox.methods, methods);

})(jQuery);