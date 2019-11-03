/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.isRoot.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-21
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tree.extensions");

    function isRoot(target, nodeTarget) {
        var t = $(target),
            nodeDOM = $(nodeTarget)[0],
            roots = t.tree("getRoots");
        return $.array.some(roots, function (node) {
            return node.target == nodeDOM;
        });
    }

    var defaults = $.fn.tree.extensions.defaults = {

    };

    var methods = $.fn.tree.extensions.methods = {

        //  扩展 easyui-tree 的自定义方法；判断指定的 tree-node 是否为根节点；该方法定义如下参数：
        //      target: 用于判断的 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：如果指定的 jQuery 对象是该 easyui-tree 的根节点，则返回 true，否则返回 false。
        isRoot: function (jq, target) { return isRoot(jq[0], target); }
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);