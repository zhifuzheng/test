/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.getTheRoot.js
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

    var getTheRoot = function (treeTarget, target) {
        if (target == undefined) { return null; }
        var t = $(treeTarget); node = $(target);
        if (!$.contains(t[0], target) || !node.is("div.tree-node")) { return null; }

        var parentNode = t.tree("getParent", target);
        if (parentNode == null) { return t.tree("getNode", target); }
        var rootNode;
        while (parentNode != null) {
            var temp = t.tree("getParent", parentNode.target);
            if (temp != null) { parentNode = temp; }
            else { rootNode = parentNode; parentNode = null; }
        }
        return rootNode;
    };

    var defaults = $.fn.tree.extensions.defaults = {

    };

    var methods = $.fn.tree.extensions.methods = {

        //  扩展 easyui-tree 的自定义方法；获取指定节点的根节点；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的根节点(具有和当前 target 同一个关系树)对象；
        //      如果传入的参数 target 是根节点，则返回 target 节点对象；
        //      如果传入的参数 target 未定义，则返回 null；
        //      如果传入的参数 target 不是一个 div.tree-node 或者其不包含在当前 easyui-tree 中，则返回 null。
        getTheRoot: function (jq, target) {
            return getTheRoot(jq[0], target);
        }
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);