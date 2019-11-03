/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.operateRow.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-12-19
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.findRow.js
*   3、jeasyui.extensions.datagrid.getDom.js
*   4、jeasyui.extensions.datagrid.getRowData.js
*   5、jeasyui.extensions.datagrid.moveRow.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");


    //  param: idField | row | function (row, index, rows)
    function showRow(target, param) {
        var state = $.data(target, "datagrid"),
            t = $(target),
            opts = state.options,
            rows = t.datagrid("getRows");

        var row = t.datagrid("findRow", param),
            index = t.datagrid("getRowIndex", row);
        if (index > -1) {
            $.array.remove(state.hiddenRows, row);
            t.datagrid("getRowDom", index).removeClass("datagrid-row-hidden");
        }
    }

    //  param: idField | row | function (row, index, rows)
    function hideRow(target, param) {
        var state = $.data(target, "datagrid"),
            t = $(target),
            opts = state.options,
            rows = t.datagrid("getRows");

        var row = t.datagrid("findRow", param),
            index = t.datagrid("getRowIndex", row);
        if (index > -1) {
            $.array.attach(state.hiddenRows, row);
            t.datagrid("unselectRow", index).datagrid("uncheckRow", index);
            t.datagrid("getRowDom", index).addClass("datagrid-row-hidden");
        }
    }

    //  param: function (row, index, rows) | array (idField | row | function (row, index, rows)) | boolean (false)
    function hideRows(target, param) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;
        if (param == true) {
            var rows = t.datagrid("getRows");
            $.array.clear(state.hiddenRows);
            $.array.copy(state.hiddenRows, rows);
            $.each(rows, function (i, n) {
                var rowIndex = t.datagrid("getRowIndex", n);
                if (rowIndex > -1) {
                    t.datagrid("getRowDom", rowIndex).addClass("datagrid-row-hidden");
                }
            });
        } else {
            var array = t.datagrid("findRows", param);
            if (array.length) {
                $.each(array, function (i, n) {
                    hideRow(target, n);
                });
            }
        }
    }

    //  param: function (index, row, rows) | array (idField | row | function (index, row, rows)) | boolean (true)
    function showRows(target, param) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;
        if (param == true) {
            var rows = t.datagrid("getRows");
            $.array.clear(state.hiddenRows);
            $.each(rows, function (i, n) {
                var rowIndex = t.datagrid("getRowIndex", n);
                if (rowIndex > -1) {
                    t.datagrid("getRowDom", rowIndex).removeClass("datagrid-row-hidden");
                }
            });
        } else {
            var array = t.datagrid("findRows", param);
            if (array.length) {
                $.each(array, function (i, n) {
                    showRow(target, n);
                });
            }
        }
    }

    function getHiddenRows(target) {
        var state = $.data(target, "datagrid");
        return state.hiddenRows;
    }

    function getVisibleRows(target) {
        var state = $.data(target, "datagrid"), t = $(target),
            rows = t.datagrid("getRows"), hiddenRows = state.hiddenRows;

        return $.array.filter(rows, function (val) { return $.array.contains(hiddenRows, val) ? false : true; });
    }




    function setDraggableStatus(source, state) {
        var icon = source.draggable("proxy").find("span.tree-dnd-icon");
        icon.removeClass("tree-dnd-yes tree-dnd-no").addClass(state ? "tree-dnd-yes" : "tree-dnd-no");
    }

    function getTrIndex(tr) {
        if (!tr) {
            return -1;
        }
        tr = $.util.isJqueryObject(tr) ? tr : $(tr);
        var attr = tr.attr("datagrid-row-index");
        return (attr == null || attr == undefined || attr == "") ? -1 : window.parseInt(attr, 10);
    }

    function getEditingRowIndexes(target) {
        var t = $(target),
            p = t.datagrid("getPanel"),
            indexes = [];
        p.find("div.datagrid-view div.datagrid-body table.datagrid-btable:first tr.datagrid-row.datagrid-row-editing").each(function () {
            var index = getTrIndex(this);
            if (index != -1) {
                indexes.push(index);
            }
        });
        return indexes;
    }

    function resetRowDnd(target, index, t, opts, row, tr) {
        t = t || $(target);
        opts = opts || $.data(target, "datagrid").options;
        if (!opts.rowDnd) { return; }
        row = row || t.datagrid("getRowData", index);
        tr = tr || t.datagrid("getRowDom", index);
        tr.each(function () {
            if ($.data(this, "draggable")) { return; }
            $(this).draggable({
                disabled: false, revert: true, edge: 5, delay: 300, cursor: "default", deltaX: 10, deltaY: 5,
                proxy: function (source) {
                    var tr = $(" ").addClass("datagrid-row datagrid-row-selected"),
                        cells = t.datagrid("getRowDom", index).clone().find("td").removeClass("datagrid-row-over").each(function (i) {
                            if (i < 8) { tr.append(this); }
                        });
                    if (cells.length > 8) {
                        $("...").appendTo(tr);
                    }
                    return $("
").append(tr).appendTo("body").hide();
                },
                onBeforeDrag: function (e) {
                    if (!opts.rowDnd || e.which != 1 || e.target.type == "checkbox" || getEditingRowIndexes(target).length) {
                        return false;
                    }
                    if ($.isFunction(opts.onRowBeforeDrag) && opts.onRowBeforeDrag.call(target, index, row) == false) {
                        return false;
                    }
                    setRowsDroppable();
                },
                onStartDrag: function (e) {
                    $(this).draggable("proxy").css({
                        left: -10000, top: -10000
                    });
                    if ($.isFunction(opts.onRowStartDrag)) {
                        opts.onRowStartDrag.call(target, index, row);
                    }
                },
                onStopDrag: function () {
                    if ($.isFunction(opts.onRowStopDrag)) {
                        opts.onRowStopDrag.call(target, index, row);
                    }
                },
                onDrag: function (e) {
                    var x1 = e.pageX, y1 = e.pageY,
                        x2 = e.data.startX, y2 = e.data.startY,
                        d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
                    if (d > 15) {
                        $(this).draggable("proxy").show();
                    }
                    this.pageY = e.pageY;
                }
            });
        });
        function setRowsDroppable() {
            t.datagrid("getPanel").find("div.datagrid-view div.datagrid-body table.datagrid-btable tr.datagrid-row[datagrid-row-index]").each(function () {
                if ($.data(this, "droppable")) { return; }
                $(this).droppable({
                    accept: "tr.datagrid-row[datagrid-row-index]",
                    onDragEnter: function (e, source) {
                        var dragger = $(source),
                            dropper = $(this),
                            dragIndex = window.parseInt(dragger.attr("datagrid-row-index"), 10),
                            dropIndex = window.parseInt(dropper.attr("datagrid-row-index"), 10),
                            rows = t.datagrid("getRows"),
                            dragRow = rows[dragIndex],
                            dropRow = rows[dropIndex],
                            dropTr = t.datagrid("getRowDom", dropIndex),
                            mark = dropTr.find("td"),
                            dnd = {
                                dragger: dragger,
                                dropper: dropper,
                                dragIndex: dragIndex,
                                dropIndex: dropIndex,
                                dragRow: dragRow,
                                dropRow: dropRow,
                                mark: mark
                            };
                        $.data(this, "datagrid-row-dnd", dnd);
                        if ($.isFunction(opts.onRowDragEnter) && opts.onRowDragEnter.call(target, dropRow, dragRow) == false) {
                            setDraggableStatus(dragger, false);
                            mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom");
                            dropper.droppable("disable");
                        }
                    },
                    onDragOver: function (e, source) {
                        var dropper = $(this),
                            dopts = dropper.droppable("options");
                        if (dopts.disabled) { return; }
                        var dnd = $.data(this, "datagrid-row-dnd"),
                            dragger = dnd.dragger,
                            mark = dnd.mark,
                            pageY = source.pageY,
                            top = dropper.offset().top,
                            height = top + dropper.outerHeight(),
                            cls = pageY > top + (height - top) / 2
                                ? "datagrid-row-dnd-bottom"
                                : "datagrid-row-dnd-top";
                        setDraggableStatus(dragger, true);
                        mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom").addClass(cls);

                        if ($.isFunction(opts.onRowDragOver) && opts.onRowDragOver.call(target, dnd.dropDow, dnd.dragRow) == false) {
                            setDraggableStatus(dragger, false);
                            mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom");
                            dropper.droppable("disable");
                        }
                    },
                    onDragLeave: function (e, source) {
                        var dnd = $.data(this, "datagrid-row-dnd"),
                            dragger = dnd.dragger,
                            mark = dnd.mark;
                        setDraggableStatus(dragger, false);
                        mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom");
                        if ($.isFunction(opts.onRowDragLeave)) {
                            opts.onRowDragLeave.call(target, dnd.dropDow, dnd.dragRow);
                        }
                    },
                    onDrop: function (e, source) {
                        var dnd = $.data(this, "datagrid-row-dnd"),
                            mark = dnd.mark,
                            point = mark.hasClass("datagrid-row-dnd-top") ? "top" : "bottom";
                        if ($.isFunction(opts.onRowBeforeDrop) && opts.onRowBeforeDrop.call(target, dnd.dropDow, dnd.dragRow, point) == false) {
                            mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom");
                            return false;
                        }
                        t.datagrid("moveRow", {
                            target: dnd.dropIndex,
                            source: dnd.dragIndex,
                            point: point
                        });
                        mark.removeClass("datagrid-row-dnd-top datagrid-row-dnd-bottom");
                        if ($.isFunction(opts.onRowDrop)) {
                            opts.onRowDrop.call(target, dnd.dropDow, dnd.dragRow, point);
                        }
                    }
                });
            });
        }
    }

    function initRowMouseEvent(t, opts) {
        var target = t[0];
        t.datagrid("getPanel").panel("body").delegate("tr.datagrid-row", {
            "mouseenter.datagrid-extensions": function (e) {
                var tr = $(this);
                if (tr.is(".datagrid-row-editing")) {
                    return;
                }
                var index = getTrIndex(tr);
                if (index == -1) {
                    return;
                }
                var row = t.datagrid("getRowData", index);
                resetRowDnd(target, index, t, opts, row, tr);
            },
            "mouseleave.datagrid-extensions": function () {
                //var tr = $(this),
                //    index = getTrIndex(tr);
                //if (index == -1) {
                //    return;
                //}
            }
        });
    }

    function initHeaderFiltersData(t, opts) {
        var target = t[0],
            state = $.data(target, "datagrid");
        state.hiddenRows = [];
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

        initHeaderFiltersData(t, opts);
        initRowMouseEvent(t, opts);
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
                        $.fn.datagrid.parseOptions(this), options);
            _datagrid.call(jq, opts, param);
            if (!isInited) {
                initializeExtensions(this);
            }
        });
    };
    $.union($.fn.datagrid, _datagrid);



    var methods = $.fn.datagrid.extensions.methods = {

        //  扩展 easyui-datagrid 的自定义方法；隐藏当前 easyui-datagrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并隐藏该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        hideRow: function (jq, param) { return jq.each(function () { hideRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；显示当前 easyui-datagrid 当前页数据中指定行的数据；该方法的参数 param 可以是以下两种类型：
        //      待查找的行数据的 idField(主键) 字段值；
        //      function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 function 类型，则 findRow 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则表示找到需要查找的结果，立即停止循环调用并显示该行数据；
        //          如果回调函数始终未返回 true，则该回调函数会一直遍历 rows 直到最后。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        showRow: function (jq, param) { return jq.each(function () { showRow(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；隐藏当前 easyui-datagrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 hideRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被隐藏；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 hideRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 hideRows 方法会对数组中的每一项循环调用 hideRow 方法；
        //      Boolean 类型且为 true：则 hideRows 将会隐藏 easyui-datagrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        hideRows: function (jq, param) { return jq.each(function () { hideRows(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；显示当前 easyui-datagrid 当前页数据中指定多行的数据；该方法的参数 param 可以是以下三种类型：
        //      Function 类型，该回调函数签名为 function(row, index, rows)，其中 row 表示行数据对象、index 表示行索引号、rows 表示当前 easyui-datagrid 调用 getRows 返回的结果集；
        //          如果 param 参数为 Function 类型，则 showRows 方法会对当前 easyui-datagrid 的当前页的每一行数据调用该回调函数；
        //          当回调函数返回 true 时，则该行数据将会被显示；
        //      Array 类型，数组中的每一项都可以定义为如下类型：
        //          待查找的行数据的 idField(主键) 字段值；
        //          Function 类型；具体回调函数签名参考 showRow 方法中 param 参数为 function 类型时的定义；
        //          当 param 参数定义为 Array 类型时，则 showRows 方法会对数组中的每一项循环调用 showRow 方法；
        //      Boolean 类型且为 true：则 showRows 将会显示 easyui-datagrid 当前页的所有数据。
        //  返回值：返回表示当前 easyui-datagrid 组件的 jQuery 链式对象。
        showRows: function (jq, param) { return jq.each(function () { showRows(this, param); }); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前 easyui-datagrid 当前页所有隐藏的行数据所构成的一个 Array 对象。
        getHiddenRows: function (jq) { return getHiddenRows(jq[0]); },

        //  扩展 easyui-datagrid 的自定义方法；获取当前 easyui-datagrid 当前页所有显示的行数据所构成的一个 Array 对象。
        getVisibleRows: function (jq) { return getVisibleRows(jq[0]); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义属性，该属性表示是否启用表格的行节点拖动功能；
        //  Boolean 类型值，默认为 false。
        rowDnd: false,

        //  扩展 easyui-datagrid 的自定义事件；该事件表示拖动 data-row(数据行) 之前触发的动作；该事件回调函数提供如下两个参数：
        //      index: 表示要拖动的 data-row(数据行) 索引号(从 0 开始计数)；
        //      row:   表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则取消当前的拖动 data-row(数据行) 操作。
        onRowBeforeDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示开始拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示被拖动的 data-row(数据行) 的索引号，从 0 开始计数；
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onRowStartDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示结束拖动 data-row(数据行) 时触发的动作；该事件回调函数提供如下两个参数：
        //      index:  表示被拖动的 data-row(数据行) 的索引号，从 0 开始计数；
        //      row:    表示被拖动的 data-row(数据行) 的行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onRowStopDrag: function (index, row) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 时所触发的动作；该事件回调函数提供如下两个参数：
        //      targetRow: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      sourceRow: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onRowDragEnter: function (targetRow, sourceRow) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并在上面移动时所触发的动作；该事件回调函数提供如下两个参数：
        //      targetRow: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      sourceRow: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  备注：如果该事件函数返回 false，则立即取消当前的 data-row(数据行) 接收拖动过来对象的操作，并禁用当前 data-row(数据行) 的 droppable 效果；
        onRowDragOver: function (targetRow, sourceRow) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示当有其他的 data-row(数据行) 被拖动至当前 data-row(数据行) 后并拖动离开时所触发的动作；该事件回调函数提供如下两个参数：
        //      targetRow: 表示当前 data-row(数据行) 的行数据对象，是一个 JSON-Object；
        //      sourceRow: 表示拖动过来的 data-row(数据行) 行数据对象，是一个 JSON-Object。
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onRowDragLeave: function (targetRow, sourceRow) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示放置 data-row(数据行) 之前触发的动作；该事件回调函数提供如下三个参数：
        //      targetRow: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      sourceRow: 表示要放置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示放置到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示放置到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        //  如果该事件函数返回 false，则会立即停止放置数据行操作；
        onRowBeforeDrop: function (targetRow, sourceRow, point) { },

        //  扩展 easyui-datagrid 的自定义事件；该事件表示放置 data-row(数据行) 之后触发的动作；该事件回调函数提供如下三个参数：
        //      targetRow: 表示目标位置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      sourceRow: 表示要放置的 data-row(数据行) 索引号(从 0 开始计数)；
        //      point:  表示放置到目标节点 target 的位置，String 类型，可能的值包括：
        //          "top":      表示放置到目标位置 target 的上一格位置；
        //          "bottom":   表示追加为目标位置 target 的下一格位置；
        //  该事件函数中的 this 指向当前 easyui-datagrid 的 DOM 对象(非 jQuery 对象)；
        onRowDrop: function (targetRow, sourceRow, point) { }
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);