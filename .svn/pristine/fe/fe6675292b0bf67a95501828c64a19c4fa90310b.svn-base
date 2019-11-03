/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tabs 扩展
* jeasyui.extensions.tabs.dndTab.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-02-05
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.tabs.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tabs.extensions");


    function isDisabled(target, which) {
        return $(target).tabs("getTab", which).panel("options").tab.is(".tabs-disabled");
    }

    function getTabOption(target, which) {
        var t = $(target), tab = t.tabs("getTab", which), tabOpts = tab.panel("options");
        return tabOpts;
    };

    function getHeader(target) {
        var t = $(target);

        var panel = t.panel("panel"),
            index = panel.index(),
            tabs = panel.closest(".tabs-container");
        return tabs.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li").eq(index);
    }



    //  param: { target: numer | string | DOM | jQuery, source: numer | string | DOM | jQuery, point: string("before"/default, "after") }
    function moveTab(target, param) {
        if (param == undefined || param == null
            || param.source == undefined || param.source == null
            || param.target == undefined || param.target == null) {
            return;
        }
        var t = $(target),
            sourceType = $.type(param.source),
            sourceTab = (sourceType == "number" || sourceType == "string") ? t.tabs("getTab", param.source) : $(param.source),
            sourceIndex = t.tabs("getTabIndex", sourceTab);
        if (sourceIndex == -1) {
            return;
        }
        var targetType = $.type(param.target),
            targetTab = (targetType == "number" || targetType == "string") ? t.tabs("getTab", param.target) : $(param.target),
            targetIndex = t.tabs("getTabIndex", targetTab);
        if (targetIndex == -1 || targetIndex == sourceIndex) {
            return;
        }
        sourceTab = t.tabs("getTab", sourceIndex);
        targetTab = t.tabs("getTab", targetIndex);

        var point = (param.point == "before" || param.point == "after") ? param.point : "before";
        if ((point == "before" && sourceIndex == (targetIndex - 1)) || (point == "after" && (sourceIndex == targetIndex + 1))) {
            return;
        }
        var state = $.data(target, "tabs"),
            opts = state.options;

        var tabs = t.tabs("tabs"),
            sourceHeader = getHeader(sourceTab[0]),
            targetHeader = getHeader(targetTab[0]),
            sourcePanel = sourceTab.panel("panel"),
            targetPanel = targetTab.panel("panel");

        $.array.removeAt(tabs, sourceIndex);
        var i = targetIndex > sourceIndex ? targetIndex - 1 : targetIndex;
        $.array.insert(tabs, point == "before" ? i : i + 1, sourceTab);

        targetHeader[point](sourceHeader);
        targetPanel[point](sourcePanel);
    }

    function enableDnd(target, which) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options,
            list = t.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li>.tabs-inner");
        list.droppable({
            accept: ".tabs-inner",
            onDragEnter: function (e, source) {
                var dragger = $(source),
                    dropper = $(this),
                    sourceLi = dragger.closest("li"),
                    targetLi = dropper.closest("li"),
                    sourceIndex = sourceLi.index(),
                    targetIndex = targetLi.index(),
                    sourceTab = t.tabs("getTab", sourceIndex),
                    targetTab = t.tabs("getTab", targetIndex);
                if ($.isFunction(opts.onDragEnter) && opts.onDragEnter.call(target, targetTab, sourceTab) == false) {
                    setDraggableStatus(dragger, true);
                    targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom");
                    dropper.droppable("disable");
                }
            },
            onDragOver: function (e, source) {
                var dragger = $(source),
                    dropper = $(this),
                    sourceLi = dragger.closest("li"),
                    targetLi = dropper.closest("li"),
                    sourceIndex = sourceLi.index(),
                    targetIndex = targetLi.index(),
                    sourceTab = t.tabs("getTab", sourceIndex),
                    targetTab = t.tabs("getTab", targetIndex),
                    proxy = dragger.draggable("proxy"),
                    cls = (opts.tabPosition == "top" || opts.tabPosition == "bottom")
                        ? (proxy.offset().left + 25 > dropper.offset().left + targetLi.width() / 2 ? "tabs-dnd-right" : "tabs-dnd-left")
                        : (proxy.offset().top - 5 > dropper.offset().top + targetLi.height() / 2 ? "tabs-dnd-bottom" : "tabs-dnd-top");
                setDraggableStatus(dragger, true);
                targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom").addClass(cls);
                if ($.isFunction(opts.onDragOver) && opts.onDragOver.call(target, targetTab, sourceTab) == false) {
                    setDraggableStatus(dragger, true);
                    targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom");
                    dropper.droppable("disable");
                }
            },
            onDragLeave: function (e, source) {
                var dragger = $(source),
                    dropper = $(this),
                    sourceLi = dragger.closest("li"),
                    targetLi = dropper.closest("li"),
                    sourceIndex = sourceLi.index(),
                    targetIndex = targetLi.index(),
                    sourceTab = t.tabs("getTab", sourceIndex),
                    targetTab = t.tabs("getTab", targetIndex);
                setDraggableStatus(dragger, false);
                targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom");
                if ($.isFunction(opts.onDragLeave)) {
                    opts.onDragLeave.call(target, targetTab, sourceTab);
                }
            },
            onDrop: function (e, source) {
                var dragger = $(source),
                    dropper = $(this),
                    sourceLi = dragger.closest("li"),
                    targetLi = dropper.closest("li"),
                    sourceIndex = sourceLi.index(),
                    targetIndex = targetLi.index(),
                    sourceTab = t.tabs("getTab", sourceIndex),
                    targetTab = t.tabs("getTab", targetIndex),
                    point = (opts.tabPosition == "top" || opts.tabPosition == "bottom")
                        ? (targetLi.is(".tabs-dnd-left") ? "before" : "after")
                        : (targetLi.is(".tabs-dnd-top") ? "before" : "after");
                if ($.isFunction(opts.onBeforeDrop) && opts.onBeforeDrop.call(target, targetTab, sourceTab, point)) {
                    targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom");
                    return;
                }
                moveTab(target, { source: sourceIndex, target: targetIndex, point: point });
                targetLi.removeClass("tabs-dnd-left tabs-dnd-right tabs-dnd-top tabs-dnd-bottom");
                if ($.isFunction(opts.onDrop)) {
                    opts.onDrop.call(target, targetTab, sourceTab, point);
                }
            }
        });
        if (which != null && which != undefined) {
            var p = t.tabs("getTab", which),
                index = t.tabs("getTabIndex", p);
            list = list.eq(index);
        }
        list.draggable({
            disabled: false, revert: true, edge: 5, delay: 300, cursor: "default", deltaX: -25, deltaY: 5,
            proxy: function (source) {
                var html = $(source).clone(),
                    icon = " ";
                return $("
").appendTo("body").append(icon).append(html).hide();
            },
            onBeforeDrag: function (e) {
                if (e.which != 1) {
                    return false;
                }
                var li = $(this).closest("li"),
                    index = li.index(),
                    disabled = isDisabled(target, index);
                if (disabled) {
                    return false;
                }
                var title = getTabOption(target, index).title;
                if ($.isFunction(opts.onBeforeDrag) && opts.onBeforeDrag.call(target, title, index) == false) {
                    return false;
                }
            },
            onStartDrag: function (e) {
                $(this).draggable("proxy").css({
                    left: -10000, top: -10000
                });
                var panels = t.children(".tabs-panels").addClass("tabs-dnd-panels");
                state.shade = $("
").appendTo(panels);

                var li = $(this).closest("li"),
                    index = li.index(),
                    title = getTabOption(target, index).title;
                if ($.isFunction(opts.onStartDrag)) {
                    opts.onStartDrag.call(target, title, index);
                }
            },
            onStopDrag: function () {
                t.children(".tabs-panels").removeClass("tabs-dnd-panels");
                if (state.shade && state.shade.length) {
                    state.shade.remove();
                    state.shade = undefined;
                }
                var li = $(this).closest("li"),
                    index = li.index(),
                    title = getTabOption(target, index).title;
                if ($.isFunction(opts.onStopDrag)) {
                    opts.onStopDrag.call(target, title, index);
                }
            },
            onDrag: function (e) {
                var x1 = e.pageX, y1 = e.pageY,
                    x2 = e.data.startX, y2 = e.data.startY,
                    d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                if (d > 10) {
                    $(this).draggable("proxy").show();
                }
                this.pageY = e.pageY;
            }
        });
        function setDraggableStatus(source, state) {
            var icon = source.draggable("proxy").find("span.tree-dnd-icon");
            icon.removeClass("tree-dnd-yes tree-dnd-no").addClass(state ? "tree-dnd-yes" : "tree-dnd-no");
        }
    }

    function disableDnd(target, which) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options,
            list = t.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li>.tabs-inner");

        if (which != null && which != undefined) {
            var p = t.tabs("getTab", which),
                index = t.tabs("getTabIndex", p);
            list = list.eq(index);
        } else {
            opts.dnd = false;
        }
        list.draggable("disable");
    }


    function initTabHeaderDnd(t, opts) {
        if (opts.dnd) {
            enableDnd(t[0]);
        }
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options;
        initTabHeaderDnd(t, opts);
    }

    var _tabs = $.fn.tabs.extensions._tabs = $.fn.tabs;
    $.fn.tabs = function (options, param) {
        if (typeof options == "string") {
            return _tabs.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "tabs") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.tabs.parseOptions(this),
                        $.parser.parseOptions(this, [
                            { dnd: "boolean" }
                        ]), options);
            _tabs.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.tabs, _tabs);


    var defaults = $.fn.tabs.extensions.defaults = {

        //  扩展 easyui-tabs 的自定义属性；表示是否启用选项卡组件的选项卡头拖动排序功能；
        //  Boolean 类型值，默认为 false。
        dnd: false,

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在开始拖动选项卡头前瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      title : 表示被拖动的选项卡的标题；
        //      index : 表示被拖动的选项卡的索引号（从 0 开始计数）。
        //  如果该事件函数返回 false，则将取消该选项卡的拖动行为。
        onBeforeDrag: function (title, index) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在开始拖动选项卡头后瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      title : 表示被拖动的选项卡的标题；
        //      index : 表示被拖动的选项卡的索引号（从 0 开始计数）。
        onStartDrag: function (title, index) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在停止拖动选项卡头后瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      title : 表示被拖动的选项卡的标题；
        //      index : 表示被拖动的选项卡的索引号（从 0 开始计数）。
        onStopDrag: function (title, index) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在拖动一个选项卡头进入另一个选项卡头区域前瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      target : 表示当前拖放到的目标位置的选项卡的 jQuery-DOM 对象。
        //      source : 表示被拖动的选项卡的 jQuery-DOM 对象。
        //      point  : 表示被拖动的选项卡当前处于拖放到的目标位置选项卡的具体位置，该参数可能的值为：
        //          "before": 表示 source 处于 target 的前一格位置；
        //          "after" : 表示 source 处于 target 的后一格位置；
        //  如果该事件函数返回 false，则将取消该选项卡的拖动放置行为。
        onDragEnter: function (target, source) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在拖动一个选项卡头进入另一个选项卡头区域后在其上方移动时所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      target : 表示当前拖放到的目标位置的选项卡的 jQuery-DOM 对象。
        //      source : 表示被拖动的选项卡的 jQuery-DOM 对象。
        //      point  : 表示被拖动的选项卡当前处于拖放到的目标位置选项卡的具体位置，该参数可能的值为：
        //          "before": 表示 source 处于 target 的前一格位置；
        //          "after" : 表示 source 处于 target 的后一格位置；
        //  如果该事件函数返回 false，则将立即取消该选项卡的拖动放置行为。
        onDragOver: function (target, source) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在拖动一个选项卡头进入另一个选项卡头区域后并拖动离开该区域时所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      target : 表示当前拖放到的目标位置的选项卡的 jQuery-DOM 对象。
        //      source : 表示被拖动的选项卡的 jQuery-DOM 对象。
        //      point  : 表示被拖动的选项卡当前处于拖放到的目标位置选项卡的具体位置，该参数可能的值为：
        //          "before": 表示 source 处于 target 的前一格位置；
        //          "after" : 表示 source 处于 target 的后一格位置；
        onDragLeave: function (target, source) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在拖动一个选项卡头进入另一个选项卡头区域后，松开鼠标以将被拖动的选项卡放置在相应位置前瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      target : 表示当前拖放到的目标位置的选项卡的 jQuery-DOM 对象。
        //      source : 表示被拖动的选项卡的 jQuery-DOM 对象。
        //      point  : 表示被拖动的选项卡当前处于拖放到的目标位置选项卡的具体位置，该参数可能的值为：
        //          "before": 表示 source 处于 target 的前一格位置；
        //          "after" : 表示 source 处于 target 的后一格位置；
        //  如果该事件函数返回 false，则将立即取消该选项卡的拖动放置行为。
        onBeforeDrop: function (target, source, point) { },

        //  扩展 easyui-tabs 的自定义事件；表示当启用选项卡头拖动排序功能时（dnd: true），在拖动一个选项卡头进入另一个选项卡头区域后，松开鼠标以将被拖动的选项卡放置在相应位置后瞬间所触发的动作。
        //  该事件函数签名中定义如下参数：
        //      target : 表示当前拖放到的目标位置的选项卡的 jQuery-DOM 对象。
        //      source : 表示被拖动的选项卡的 jQuery-DOM 对象。
        //      point  : 表示被拖动的选项卡当前处于拖放到的目标位置选项卡的具体位置，该参数可能的值为：
        //          "before": 表示 source 处于 target 的前一格位置；
        //          "after" : 表示 source 处于 target 的后一格位置；
        onDrop: function (target, source, point) { }
    };

    var methods = $.fn.tabs.extensions.methods = {

        //  扩展 easyui-tabs 的自定义方法；启用 easyui-tabs 选项卡组件的选项卡头拖动排序功能；该方法定义如下参数：
        //      which:  指定的选项卡的 索引号 或者 标题。该参数可选，如果未指定该参数，则表示启用 easyui-tabs 组件所有选项卡的选项卡头拖动排序功能。
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        enableDnd: function (jq, which) { return jq.each(function () { enableDnd(this, which); }); },

        //  扩展 easyui-tabs 的自定义方法；禁用 easyui-tabs 选项卡组件的选项卡头拖动排序功能；该方法定义如下参数：
        //      which:  指定的选项卡的 索引号 或者 标题。该参数可选，如果未指定该参数，则表示禁用 easyui-tabs 组件所有选项卡的选项卡头拖动排序功能。
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        disableDnd: function (jq, which) { return jq.each(function () { disableDnd(this, which); }); }
    };


    $.extend($.fn.tabs.defaults, defaults);
    $.extend($.fn.tabs.methods, methods);

})(jQuery);