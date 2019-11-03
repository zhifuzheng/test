/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combo 扩展
* jeasyui.extensions.combo.autoShowPanel.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-04
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combo.extensions");

    function hideAllComboPanel(target) {
        $.util.pageNestingExecute(function (win) {
            if (!win || !win.document || !win.jQuery) {
                return;
            }
            var jq = win.jQuery;
            if (target) {
                var p = jq(target).closest("span.combo,div.combo-p,div.menu");
                if (p.length) {
                    p.find(".combo-f").each(function () {
                        var pp = jq(this).combo("panel");
                        if (pp.is(":visible")) {
                            pp.panel("close");
                        }
                    });
                    if (target && target.ownerDocument == win.document) {
                        return;
                    }
                }
            }
            jq("body>div.combo-p>div.combo-panel:visible").panel("close");
        });
    }

    //  下面这段代码实现即使在跨 IFRAME 的情况下，一个 WEB-PAGE 中也只能同时显示一个 easyui-combo panel 控件。
    $.util.bindDocumentNestingEvent("mousedown.combo-nesting", function (doc, e) {
        hideAllComboPanel(e.target);
    });




    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "combo"),
            opts = state.options;
        t.combo("textbox").click(function () {
            var p = t.combo("panel");
            if (opts.autoShowPanel && p.is(":hidden")) {
                t.combo("showPanel");
            }
        });
    }


    var _combo = $.fn.combo.extensions._combo = $.fn.combo;
    $.fn.combo = function (options, param) {
        if (typeof options == "string") {
            return _combo.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "combo") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.combo.parseOptions(this),
                        $.parser.parseOptions(this, [
                            { autoShowPanel: "boolean" }
                        ]), options);
            _combo.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.combo, _combo);


    var defaults = $.fn.combo.extensions.defaults = {

        //  扩展 easyui-combo 的自定义属性；表示该 easyui-combo 组件是否在 textbox 文本框获取焦点时自动执行 showPanel 方法以显示下拉 panel 面板；
        //  Boolean 类型值，默认为 true。
        //  落阳注：该自定义属性的功能，其实 easyui-combo 源生就提供，但只有在 editable 属性为 false 的情况下才有效；
        //  而扩展了该自定义属性之后，在 editable 属性为 true 时，也可以使得自动下拉 panel 面板。
        autoShowPanel: true
    };

    var methods = $.fn.combo.extensions.methods = {

    };


    $.extend($.fn.combo.defaults, defaults);
    $.extend($.fn.combo.methods, methods);

})(jQuery);