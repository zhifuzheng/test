/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.navigating.js
* 开发 糖古屋
* 由 落阳 整理
* 最近更新：2016-05-10
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 Lixilin personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");

    function onNavigatePaginate(paginateType) {
        if (paginateType == "prev") {
            $(this).datagrid("selectRow", $(this).datagrid("getRows").length - 1);
        } else {
            $(this).datagrid("selectRow", 0);
        }
    }

    function prevPage(t, opts) {
        if (opts.pagination) {
            var pager = t.datagrid("getPager"), currentPageNumber = pager.pagination("options").pageNumber;
            if (currentPageNumber > 1) {
                //缓存回调给datagrid对象
                opts.successCallBackForNavigating = function () {
                    if ($.isFunction(opts.onNavigatePaginate)) { opts.onNavigatePaginate.call(this, "prev"); }
                };
                pager.pagination("select", currentPageNumber - 1);
            }
        }
    }

    function nextPage(t, opts) {
        if (opts.pagination) {
            var pager = t.datagrid("getPager"), pagerOpts = pager.pagination("options"),
                currentPageNumber = pagerOpts.pageNumber, recordsTotal = pagerOpts.total, pageSize = pagerOpts.pageSize;
            var totalPage = Math.ceil(recordsTotal / pageSize) || 1;
            if (currentPageNumber < totalPage) {
                //缓存回调给datagrid对象
                opts.successCallBackForNavigating = function () {
                    if ($.isFunction(opts.onNavigatePaginate)) { opts.onNavigatePaginate.call(this, "next"); }
                };
                pager.pagination("select", currentPageNumber + 1);
            }
        }
    }

    function initKeyNavigatingEvent(t, opts) {
        if (!opts.navigatingWithKey) { return; }
        if (opts.pagination) {
            var po = t.datagrid("getPager"), popts = po.pagination("options");
            var _onSelectPage = popts.onSelectPage;
            popts.onSelectPage = function (pn, ps) {
                /*重写 datagrid 的 onSelectPage */
                opts.pageNumber = pn || 1;
                opts.pageSize = ps;
                po.pagination("refresh", { pageNumber: pn, pageSize: ps });

                var queryParams = $.extend({ page: opts.pageNumber, rows: opts.pageSize }, opts.queryParams);
                if (opts.sortName) {
                    $.extend(queryParams, { sort: opts.sortName, order: opts.sortOrder });
                }
                if (opts.onBeforeLoad.call(t[0], queryParams) == false) {
                    return;
                }
                t.datagrid("loading");
                var loadResult = opts.loader.call(t[0], queryParams, function (data) {
                    t.datagrid("loaded");
                    t.datagrid("loadData", data);
                    //执行回调
                    if ($.isFunction(opts.successCallBackForNavigating)) {
                        opts.successCallBackForNavigating.call(t[0], data);
                        opts.successCallBackForNavigating = undefined;
                    }
                }, function () {
                    t.datagrid("loaded");
                    opts.onLoadError.apply(t[0], arguments);
                });
                if (loadResult == false) {
                    t.datagrid("loaded");
                }
            };
        }
        t.datagrid("getPanel").panel("panel").attr('tabindex', 1).off('keydown.navigating').on('keydown.navigating', function (e) {
            switch (e.keyCode) {
                // Up
                case 38:
                    e.preventDefault();
                    var selected = t.datagrid("getSelected");
                    if (selected) {
                        var index = t.datagrid("getRowIndex", selected);
                        if (index > 0) {
                            t.datagrid("selectRow", index - 1);
                        }
                        else {
                            prevPage(t, opts);
                        }
                    } else {
                        t.datagrid("selectRow", t.datagrid("getRows").length - 1);
                    }
                    break;
                // Down
                case 40:
                    e.preventDefault();
                    var selected = t.datagrid("getSelected");
                    if (selected) {
                        var index = t.datagrid("getRowIndex", selected), rows = t.datagrid("getRows");
                        if (index < rows.length - 1) {
                            t.datagrid("selectRow", index + 1);
                        }
                        else {
                            nextPage(t, opts);
                        }
                    } else {
                        t.datagrid("selectRow", 0);
                    }
                    break;
                // Left
                case 37:
                    e.preventDefault(); prevPage(t, opts);
                    break;
                // Right
                case 39:
                    e.preventDefault(); nextPage(t, opts);
                    break;
                // Enter
                case 13:
                    if (opts.onDblClickRow && $.isFunction(opts.onDblClickRow)) {
                        e.preventDefault();
                        var selected = t.datagrid("getSelected");
                        if (selected) {
                            var index = t.datagrid("getRowIndex", selected);
                            opts.onDblClickRow.call(t[0], index, selected);
                        }
                    }
                    break;
            }
        });
    };

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

        initKeyNavigatingEvent(t, opts);
        if (opts.navigatingWithKey) { t.datagrid("getPanel").panel("panel").focus(); }
    }

    var _datagrid = $.fn.datagrid.extensions._datagrid = $.fn.datagrid;
    $.fn.datagrid = function (options, param) {
        if (typeof options == "string") {
            return _datagrid.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var jq = $(this),
                isInited = $.data(this, "datagrid") ? true : false,
                opts = isInited ? options : $.extend({},
                        $.fn.datagrid.parseOptions(this),
                        $.parser.parseOptions(this, [
                            {
                                navigatingWithKey: "boolean"
                            }
                        ]), options);
            _datagrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.datagrid, _datagrid);

    var methods = $.fn.datagrid.extensions.methods = {

    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义属性；表示是否开启按键导航功能；
        //      Up 键：selected 数据行上移
        //      Down 键：selected 数据行下移
        //      Left 键：上一页，仅在有上一页时有效
        //      Right 键：下一页，仅在有下一页时有效
        //      Enter 键：触发 datagrid onDblClickRow 事件，仅在有 selected 数据行时有效
        navigatingWithKey: true,

        //  扩展 easyui-datagrid 的自定义事件；表示通过按键导航进行翻页后触发的事件；该事件回调函数提供如下参数：
        //      paginateType:  表示翻页类型，其值可以是 prev、next，分别表示上一页、下一页；
        //  该事件函数中的 this 指向当前 easyui-datarid 的 DOM 对象(非 jQuery 对象)；
        onNavigatePaginate: onNavigatePaginate
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);