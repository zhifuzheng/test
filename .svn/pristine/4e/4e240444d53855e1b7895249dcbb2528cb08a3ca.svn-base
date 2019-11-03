/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.getNears.js
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

    function getNears(treeTarget, target) {
        var t = $(treeTarget); target = $(target);
        if (!$.contains(t[0], target[0]) || !target.is("div.tree-node")) { return null; }

        return target.closest("ul").children("li").children("div.tree-node").map(function () {
            return t.tree("getNode", this);
        });
    };

    var defaults = $.fn.tree.extensions.defaults = {

    };

    var methods = $.fn.tree.extensions.methods = {

        //  扩展 easyui-tree 的自定义方法；获取指定节点的同级所有节点(包含自身)；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的同级别(具有和当前 target 同一个父级节点)所有节点构成的一个数组对象；
        //      数组中每一个元素都是一个包含属性 id、text、iconCls、checked、state、attributes、target 的 tree-node 对象。
        //      如果传入的参数 target 是根节点或者未定义 target 参数，则该方法和 getRoots 方法返回的值相同；
        //      如果传入的参数 target 不是一个 div.tree-node 或者其不包含在当前 easyui-tree 中，则返回 null。
        getNears: function (jq, target) { return getNears(jq[0], target); }
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);