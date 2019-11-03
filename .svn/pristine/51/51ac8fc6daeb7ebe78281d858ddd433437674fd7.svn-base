/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tree 扩展
* jeasyui.extensions.tree.prev.js
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

    function prev(target, nodeTarget) {
        var node = $(nodeTarget);
        if (!node.is(".tree-node")) {
            return null;
        }
        var other = node.closest("li").prev().children(".tree-node");
        if (!other.length) {
            return null;
        }
        return other.length ? $(target).tree("getNode", other[0]) : null;
    }

    var defaults = $.fn.tree.extensions.defaults = {

    };

    var methods = $.fn.tree.extensions.methods = {

        //  扩展 easyui-tree 的自定义方法；获取指定节点的平级上一格位置的 tree-node 节点；该方法定义如下参数：
        //      target:  指定的表示 tree-node 的 jQuery 或 DOM 对象。
        //  返回值：返回 tree-node target 的同级别上一格位置的 tree-node 节点对象；该 tree-node 对象含有如下属性：
        //      id、text、iconCls、checked、state、attributes、target；
        //      如果该 tree-node target 为当前级别的第一个节点即没有上一格节点；则返回 null。
        prev: function (jq, target) { return prev(jq[0], target); }
    };


    $.extend($.fn.tree.defaults, defaults);
    $.extend($.fn.tree.methods, methods);

})(jQuery);