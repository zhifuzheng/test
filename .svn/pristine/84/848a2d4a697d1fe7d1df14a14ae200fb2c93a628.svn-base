/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.toggleOnClick.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-22
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tree.extensions");

    function initToggleOnClick(t, opts) {
        t.delegate(".tree-node", "click", function (e) {
            if (!$(e.target).is(".tree-hit") && opts.toggleOnClick) {
                t.tree("toggle", this);
            }
        });
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "tree"),
            opts = t.tree("options");
        initToggleOnClick(t, opts);
    }

    var _tree = $.fn.tree.extensions._tree = $.fn.tree;
    $.fn.tree = function (options, param) {
        if (typeof options == "string") {
            return _tree.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "tree") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.tree.parseOptions(this),
                        $.parser.parseOptions(this, [
                            {
                                toggleOnClick: "boolean"
                            }
                        ]), options);
            _tree.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.tree, _tree);

    var defaults = $.fn.tree.extensions.defaults = {

        //  扩展 easyui-tree 的自定义属性，表示当左键点击带有子节点的条目时，是否自动展开/折叠相应节点。
        //  Boolean 类型，默认为 false。
        //  备注：该功能不会影响到 easyui-tree 的原生事件 onClick。
        toggleOnClick: false
    };

    var methods = $.fn.tree.extensions.methods = {

        
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);