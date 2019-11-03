/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI treegrid 扩展
* jeasyui.extensions.treegrid.checkPosition.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-03-15
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2016 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.treegrid.extensions");

    var isRootNode = function (target, id) {
        var t = $(target), roots = t.treegrid("getRoots"), node = t.treegrid("find", id);
        return node && $.array.contains(roots, node);
    };

    var isParent = function (target, param) {
        var t = $(target), node = t.treegrid("find", param.id2);
        var children = t.treegrid("getChildren", param.id1);
        return $.array.contains(children, node);
    };

    var isChild = function (target, param) {
        var t = $(target), node = t.treegrid("find", param.id1);
        var children = t.treegrid("getChildren", param.id2);
        return $.array.contains(children, node);
    };

    var isSibling = function (target, param) {
        var t = $(target), p1 = t.treegrid("getParent", param.id1), p2 = t.treegrid("getParent", param.id2);
        return p1 && p2 && p1 == p2;
    };

    var defaults = $.fn.treegrid.extensions.defaults = {

    };

    var methods = $.fn.treegrid.extensions.methods = {

        //  扩展 easyui-treegrid 的自定义方法；判断指定的 tree-node 是否为根节点；该方法定义如下参数：
        //      id: 用于判断的 tree-node 对象的 idField 值。
        //  返回值：如果指定的 jQuery 对象是该 easyui-treegrid 的根节点，则返回 true，否则返回 false。
        isRoot: function (jq, id) { return isRootNode(jq[0], id); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否为另一个节点的父节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 是 tree-node id2 的父节点，则返回 true，否则返回 false。
        isParent: function (jq, param) { return isParent(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否为另一个节点的子节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 是 tree-node id2 的子节点，则返回 true，否则返回 false。
        isChild: function (jq, param) { return isChild(jq[0], param); },

        //  扩展 easyui-treegrid 的自定义方法；判断一个节点是否和另一个节点为具有同一个父节点的平级节点；该方法定义如下参数：
        //      param：  这是一个 JSON-Object，该对象定义如下属性：
        //          id1:    用于判断的第一个 tree-node 对象的 idField 值；
        //          id2:    用于判断的第二个 tree-node 对象的 idField 值；
        //  返回值：如果 tree-node id1 和 tree-node id2 是具有同一个父级节点的平级节点，则返回 true，否则返回 false。
        isSibling: function (jq, param) { return isSibling(jq[0], param); },


    };


    $.extend($.fn.treegrid.defaults, defaults);
    $.extend($.fn.treegrid.methods, methods);

})(jQuery);