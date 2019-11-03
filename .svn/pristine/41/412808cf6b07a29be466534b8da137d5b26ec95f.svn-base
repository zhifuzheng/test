/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI tabs 扩展
* jeasyui.extensions.tabs.operateTab.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2016-02-04
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.base.css
*   3、jeasyui.extensions.base.loading.js
*   4、jeasyui.extensions.messager.progress.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.tabs.extensions");

    $.extend($.fn.tabs.extensions, {
        add: $.fn.tabs.methods.add,
        update: $.fn.tabs.methods.update
    });

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

    function loading(target) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options;
        if (opts.loading == "mask") {
            $.easyui.loading({
                topMost: false,
                locale: target,
                msg: opts.loadMsg
            });
        } else if (opts.loading == "progress") {
            $.messager.progress({
                title: "操作提醒",
                msg: opts.loadMsg,
                interval: 100
            });
        }
    }

    function loaded(target) {
        var t = $(target),
                state = $.data(target, "tabs"),
                opts = state.options;
        if (opts.loading == "mask") {
            $.easyui.loaded({ topMost: false, locale: target });
        } else if (opts.loading == "progress") {
            $.messager.progress("close");
        }
    }

    function updateTabLoading(t, opts, index, popts) {
        if (popts.href && (opts.loading == "mask" || opts.loading == "progress") && (popts.selected || index == -1 || t.tabs("getTabIndex", t.tabs("getSelected")) == index)) {
            var target = t[0],
                onBeforeLoad = popts.onBeforeLoad,
                onLoad = popts.onLoad,
                onLoadError = popts.onLoadError;
            popts.onBeforeLoad = function () {
                var ret = $.isFunction(onBeforeLoad) ? onBeforeLoad.apply(this, arguments) : undefined;
                loading(target);
                if (ret == false) {
                    loaded(target);
                }
                return ret;
            };
            popts.onLoad = function () {
                var ret = $.isFunction(onLoad) ? onLoad.apply(this, arguments) : undefined;
                $.util.delay(function () {
                    loaded(target);
                });
                popts.onLoad = onLoad;
                return ret;
            };
            popts.onLoadError = function () {
                var ret = $.isFunction(onLoadError) ? onLoadError.apply(this, arguments) : undefined;
                $.util.delay(function () {
                    loaded(target);
                });
                popts.onLoadError = onLoadError;
                return ret;
            };
        }
    }

    function beginUpdateHeader(t, opts, index, popts) {
        if (popts.refreshable) {
            popts.tools = $.array.likeArrayNotString(popts.tools)
                ? $.array.merge([], popts.tools, $.fn.tabs.extensions.tabMiniButtons)
                : $.array.merge([], $.fn.tabs.extensions.tabMiniButtons);
        }
        updateTabLineheight(t, opts);
    }

    function updateTabLineheight(t, opts) {
        if (opts.lineheight > 0) {
            var pos = opts.tabPosition;
            if (pos != "top" && pos != "bottom" && pos != "left" && pos != "right") {
                pos = "top";
            }
            t.children("div.tabs-panels").css("padding-" + pos, opts.lineheight + "px").children().children().css("border-" + pos + "-width", "1px");
        }
    }

    function endUpdateHeader(t, opts, index, poptsbak) {
        var p = t.tabs("getTab", index),
            popts = p.panel("options");
        popts.tools = poptsbak.tools;
        if (popts.closeOnDblClick && popts.closable) {
            t.find(">div.tabs-header>div.tabs-wrap>ul.tabs>li>.tabs-inner").eq(index).attr("title", "双击此选项卡标题可以将其关闭");
        }
        //if (opts.dnd) {
        //    enableDnd(t[0], index);
        //}
    }

    $.fn.tabs.extensions.tabMiniButtons = [
        {
            iconCls: "icon-mini-refresh",
            handler: function () {
                var li = $(this).closest("li"),
                    index = li.index();
                $.util.delay(function () {
                    var target = li.closest(".tabs-container")[0];
                    refreshTab(target, index);
                });
            }
        }
    ];





    $.fn.tabs.extensions.closeCurrentTab = function (target, iniframe) {
        if (!target) {
            return;
        }
        var isiframe = iniframe && !$.util.isUtilTop ? true : false;
        if (isiframe && ($.util.currentFrame == null || $.util.currentFrame == undefined)) {
            return;
        }
        var jq = isiframe ? $.util.parent.$ : $,
            current = isiframe ? jq($.util.currentFrame) : jq(target),
            t = current.currentTabs();
        if (t.length) {
            var index = current.currentTabIndex();
            if (index > -1) {
                t.tabs("close", index);
            }
        }
    };

    $.fn.tabs.extensions.refreshCurrentTab = function (target, iniframe) {
        if (!target) {
            return;
        }
        var isiframe = iniframe && !$.util.isUtilTop ? true : false;
        if (isiframe && ($.util.currentFrame == null || $.util.currentFrame == undefined)) {
            return;
        }
        var jq = isiframe ? $.util.parent.$ : $,
            current = isiframe ? jq($.util.currentFrame) : jq(target),
            t = current.currentTabs();
        if (t.length) {
            var index = current.currentTabIndex();
            if (index > -1) {
                t.tabs("refresh", index);
            }
        }
    };






    function refreshTab(target, which) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options,
            p = t.tabs("getTab", which),
            popts = p.panel("options");
        if ($.string.isNullOrWhiteSpace(popts.href)) {
            return;
        }
        var index = t.tabs("getTabIndex", p)
        if ($.isFunction(opts.onBeforeRefresh) && opts.onBeforeRefresh.call(target, opts.title, index) == false) {
            return;
        }
        updateTabLoading(t, opts, index, popts);
        p.panel("refresh");
        if ($.isFunction(opts.onRefresh)) {
            opts.onRefresh.call(target, opts.title, index);
        }
    }

    function addTab(target, options) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options,
            index = $.isNumeric(options.index) ? options.index : -1,
            popts = $.extend({}, $.fn.tabs.extensions.tabOptions, options || {}),
            poptsbak = $.extend({}, popts);
        updateTabLoading(t, opts, index, popts);
        $.fn.tabs.extensions.add.call(t, t, popts);
    }

    function updateTab(target, param) {
        param = $.extend({ tab: null, type: "all", options: null }, param);
        if (!param.tab || !param.tab.length) {
            return;
        }
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options,
            index = t.tabs("getTabIndex", param.tab),
            popts = $.union(param.options, $.fn.tabs.extensions.tabOptions),
            poptsbak = $.extend({}, popts);

        if (param.type == "all") {
            beginUpdateHeader(t, opts, index, popts);
            updateTabLoading(t, opts, index, popts);
            $.fn.tabs.extensions.update.call(t, t, { tab: param.tab, type: param.type, options: popts });
            endUpdateHeader(t, opts, index, poptsbak);

        } else if (param.type == "header") {
            beginUpdateHeader(t, opts, index, popts);
            $.fn.tabs.extensions.update.call(t, t, { tab: param.tab, type: param.type, options: popts });
            endUpdateHeader(t, opts, index, poptsbak);

        } else if (param.type == "body") {
            updateTabLoading(t, opts, index, popts);
            $.fn.tabs.extensions.update.call(t, t, { tab: param.tab, type: param.type, options: popts });
        }
    }

    function insertTab(target, param) {
        if (!param || param.which == null || param.which == undefined || !param.options) {
            return;
        }
        if (param.point == null || param.point == undefined) {
            param.point = "before";
        }
        if (param.point != "before" && param.point != "after") {
            return;
        }
        var t = $(target),
            type = $.type(param.which),
            p = (type == "number" || type == "string") ? t.tabs("getTab", param.which) : $(param.which),
            index = t.tabs("getTabIndex", p);
        if (index == -1) {
            return;
        }
        var i = param.point == "before" ? index : index + 1,
            popts = $.extend({}, param.options, {
                index: i
            });
        t.tabs("add", popts);
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
        if ($.isFunction(opts.onBeforeMove) && opts.onBeforeMove.call(target, targetTab[0], sourceTab[0], point) == false) {
            return;
        }

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

        if ($.isFunction(opts.onMove)) {
            opts.onMove.call(target, targetTab[0], sourceTab[0], point);
        }
    }


    function initTabHeaderDblClickEvent(t, opts) {
        t.children("div.tabs-header").delegate(".tabs-inner", "dblclick", function (e) {
            var li = $(e.target).closest('li');
            if (li.length && !li.is(".tabs-disabled")) {
                var index = li.index(),
                    popts = getTabOption(t[0], index);
                if (popts.closeOnDblClick && popts.closable) {
                    t.tabs("close", index);
                }
            }
        });
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "tabs"),
            opts = state.options;
        initTabHeaderDblClickEvent(t, opts);
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
                            "loading", "loadMsg",
                            { lineheight: "number" }
                        ]), options);
            _tabs.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.tabs, _tabs);



    var tabOptions = $.fn.tabs.extensions.tabOptions = {

        //  该选项卡的 href 是否在 iframe 中打开。
        iniframe: false,

        //  该选项卡是否具有刷新功能。
        refreshable: true,

        //  双击选项卡标题是否能将其关闭，当该选项卡 closable: true 时，该属性有效。
        closeOnDblClick: true,

        //  选项卡页指向的 url 地址
        href: null,

        // 选项卡页图标
        iconCls: "icon-standard-application-form"
    };

    var defaults = $.fn.tabs.extensions.defaults = {

        //  扩展 easyui-tabs 的自定义属性，该属性表示当前选项卡标题栏和选项卡的 pane-body 之间的空白区域高(宽)度(px)；
        //  Number 数值，默认为 2.
        lineheight: 2,

        //  扩展 easyui-tabs 的自定义属性；该属性表示当 easyui-tabs 组件加载 panel 时，显示的遮蔽层进度条类型。
        //  String 类型值，可选的值限定范围如下：
        //      "mask": 表示遮蔽层 mask-loading 进度显示，默认值
        //      "progress": 表示调用 $.messager.progress 进行进度条效果显示
        //      "none": 表示不显示遮蔽层和进度条
        loading: "mask",

        //  扩展 easyui-tabs 的自定义属性；该属性表示当 easyui-tabs 组件加载 panel 时，显示的遮蔽层提示文字内容。
        //  String 类型值
        loadMsg: "正在加载数据，请稍等...",

        //  扩展 easyui-tabs 的自定义事件，当调用 easyui-tabs 的 refresh 方法前，将触发该事件。如果该事件函数返回 false，将中断 refresh 方法的执行。
        //  该事件回调函数签名中定义如下参数：
        //      title: 表示刷新的 tab-panel 的标题 title 值
        //      index: 表示刷新的 tab-panel 的索引号
        onBeforeRefresh: function (title, index) { },

        //  扩展 easyui-tabs 的自定义事件，当调用 easyui-tabs 的 refresh 方法后，将触发该事件。
        //  该事件回调函数签名中定义如下参数：
        //      title: 表示刷新的 tab-panel 的标题 title 值
        //      index: 表示刷新的 tab-panel 的索引号
        onRefresh: function (title, index) { },

        //  扩展 easyui-tabs 的自定义事件，当调用 easyui-tabs 的 move 方法前，将触发该事件。如果该事件函数返回 false，将中断 move 方法的执行。
        //  该事件回调函数签名中定义如下参数：
        //      target: html-DOM 类型值，表示移动目标位置的 tab-panel 的 DOM 对象；
        //      source: html-DOM 类型值，表示要移动的 tab-panel 的 DOM 对象；
        //      point : 移动到目标位置的方式，String 类型值，仅限于定义为如下值：
        //          "before":   表示把 source 选项卡移动至 target 选项卡的前面，默认值；
        //          "after":    表示把 source 选项卡移动至 target 选项卡的后面；
        onBeforeMove: function (target, source, point) { },

        //  扩展 easyui-tabs 的自定义事件，当调用 easyui-tabs 的 move 方法后，将触发该事件。
        //  该事件回调函数签名中定义如下参数：
        //      target: html-DOM 类型值，表示移动目标位置的 tab-panel 的 DOM 对象；
        //      source: html-DOM 类型值，表示要移动的 tab-panel 的 DOM 对象；
        //      point : 移动到目标位置的方式，String 类型值，仅限于定义为如下值：
        //          "before":   表示把 source 选项卡移动至 target 选项卡的前面，默认值；
        //          "after":    表示把 source 选项卡移动至 target 选项卡的后面；
        onMove: function (target, source, point) { },
    };

    var methods = $.fn.tabs.extensions.methods = {

        //  重写 easyui-tabs 的原生方法 add，以支持扩展的功能；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        add: function (jq, options) { return jq.each(function () { addTab(this, options); }); },

        //  重写 easyui-tabs 的原生方法 update，以支持扩展的功能；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        update: function (jq, param) { return jq.each(function () { updateTab(this, param); }); },

        //  扩展 easyui-tabs 的自定义方法；在当前 easyui-tabs 组件上创建一个新的选项卡，并将其移动至指定选项卡的前一格位置；该方法的参数 param 为包含如下属性的 JSON-Object 对象：
        //      options:  表示要创建的新选项卡的属性；是一个 JSON-Object 对象；
        //          该对象的各项属性参考 easyui-tabs 中 add 方法的参数 options；
        //      which : Number、String、jQuery 或 DOM 类型值，表示移动位置的 tab-panel 的索引号、标题 title 值或 jQuery 对象、DOM 对象；
        //      point : 表示新选项卡插入的位置，String 类型值，仅限于定义为如下值：
        //              "before":   表示把新选项卡移动至 which 选项卡的前面，默认值；
        //              "after":    表示把新选项卡移动至 which 选项卡的后面；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        insert: function (jq, param) { return jq.each(function () { insertTab(this, param); }); },

        //  扩展 easyui-tabs 的自定义方法；将指定的 easyui-tabs tab-panel 选项卡页移动至另一位置；该方法定义如下参数：
        //      param:  这是一个 JSON-Object 对象，该对象定义如下属性：
        //          target: Number、String、jQuery 或 DOM 类型值，表示移动目标位置的 tab-panel 的索引号、标题 title 值或 jQuery 对象、DOM 对象；
        //          source: Number、String、jQuery 或 DOM 类型值，表示要移动的 tab-panel 的索引号、标题 title 值或 jQuery 对象、DOM 对象；
        //          point:  移动到目标位置的方式，String 类型值，仅限于定义为如下值：
        //              "before":   表示把 source 选项卡移动至 target 选项卡的前面，默认值；
        //              "after":    表示把 source 选项卡移动至 target 选项卡的后面；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        move: function (jq, param) { return jq.each(function () { moveTab(this, param); }); },

        //  扩展 easyui-tabs 的自定义方法；刷新指定的选项卡；该方法定义如下参数：
        //      which:  表示被刷新的选项卡的 索引号（从 0 开始计数） 或者 标题。
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        refresh: function (jq, which) { return jq.each(function () { refreshTab(this, which); }); },

        //  扩展 easyui-tabs 的自定义方法；显示 easyui-tabs 组件的选项卡页面加载状态遮罩层；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        loading: function (jq) { return jq.each(function () { loading(this); }); },

        //  扩展 easyui-tabs 的自定义方法；关闭 easyui-tabs 组件的选项卡页面加载状态遮罩层；
        //  返回值：返回表示当前选项卡控件 easyui-tabs 的 jQuery 链式对象。
        loaded: function (jq) { return jq.each(function () { loaded(this); }); }
    };

    $.extend($.fn.panel.defaults, $.fn.tabs.extensions.tabOptions);
    $.extend($.fn.tabs.defaults, defaults);
    $.extend($.fn.tabs.methods, methods);


    $.fn.extend({

        //  扩展 jQuery 对象的实例方法；用于关闭当前对象所在的 easyui-tabs 当前选项卡(支持当前选项卡页面为 iframe 加载的情况)。
        //  该方法定义如下参数：
        //      iniframe: Boolean 类型值，表示是否为关闭当前对象所在的父级页面的选项卡；默认为 false。
        //          如果当前页面为顶级页面，
        //          或者当前对象在 iframe 中但是不在当前iframe中的某个 easyui-tabs 内，则参数参数 inframe 无效。
        //  返回值：返回当前 jQuery 链式对象(实际上返回的 jQuery 对象中，所包含的元素已经被销毁，因为其容器 tab-panel 被关闭销毁了)。
        closeCurrentTab: function (iniframe) { return this.each(function () { $.fn.tabs.extensions.closeCurrentTab(this, iniframe); }); },

        //  扩展 jQuery 对象的实例方法；用于刷新当前对象所在的 easyui-tabs 当前选项卡(支持当前选项卡页面为 iframe 加载的情况)。
        //  该方法定义如下参数：
        //      iniframe: Boolean 类型值，表示是否为刷新当前对象所在的父级页面的选项卡；默认为 false。
        //          如果当前页面为顶级页面，
        //          或者当前对象在 iframe 中但是不在当前iframe中的某个 easyui-tabs 内，则参数参数 inframe 无效。
        //  返回值：返回当前 jQuery 链式对象。
        refreshCurrentTab: function (iniframe) { return this.each(function () { $.fn.tabs.extensions.refreshCurrentTab(this, iniframe); }); }
    });

})(jQuery);