/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 编辑器扩展-回车聚焦
* jeasyui.extensions.datagrid.edit.enterFocus.js
* 开发 落阳
* 最近更新：2017-03-21
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.rowState.js
*   3、jeasyui.extensions.datagrid.editors.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");

    $.fn.datagrid.extensions.getColumnFields = $.fn.datagrid.methods.getColumnFields;
    var getColumnFields = function (target, frozen) {
        var t = $(target);
        if (frozen == null || frozen == undefined) {
            return $.fn.datagrid.extensions.getColumnFields.call(t, t, frozen);
        }
        return $.type(frozen) == "string"
            ? $.array.merge([],
                $.fn.datagrid.extensions.getColumnFields.call(t, t, true),
                $.fn.datagrid.extensions.getColumnFields.call(t, t, false))
            : $.fn.datagrid.extensions.getColumnFields.call(t, t, frozen);
    };

    var getColumnOptions = function (target, frozen) {
        var t = $(target), fields = getColumnFields(target, frozen);
        return $.array.map(fields, function (val) { return t.datagrid("getColumnOption", val); });
    };

    var editorAvailable = function (target, field) {
        var t = $(target), opts = t.datagrid('options'), col = t.datagrid('getColumnOption', field);
        if (!col.editor) { return false; }

        var flag = typeof (col.editor) == "string", editorType = flag ? col.editor : col.editor.type, editorAction = $.fn.datagrid.defaults.editors[editorType];
        if (!editorAction) { editorType = undefined; }
        if (!editorType) { return false; }

        if (flag) { return true; } else {
            return col.editor.options == undefined ? true : (!(col.editor.options.readonly || col.editor.disabled));
        }
    };

    //找到第一个设置了编辑器并且编辑器可用的列的列名
    var getFirstAvailableEditorField = function (target) {
        return getColumnFields(target, "all").first(function (item) { return editorAvailable(target, item); });
    };

    //自动聚焦到下一个列编辑器
    //t：datagrid-jquery对象
    //opts：datagrid-options对象
    //thisArg：jQ对象，可以是 editor、editor的容器（因为editor可能不存在）
    //field：列名
    //index：索引号，寻找同行的下一列编辑器时可传递，其他时候不能传递，需根据thisArg去获取
    var autoNextFieldEditorFocus = function (t, opts, thisArg, field, index) {
        //console.log("整行编辑：定位下一个编辑器");
        var panel = t.datagrid("getPanel"),
            index = index == undefined ? window.parseInt(thisArg.closest("tr[datagrid-row-index]").attr("datagrid-row-index")) : index;
        //console.log("当前列名：" + field);
        //console.log("当前索引：" + index);
        var cellTd = panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[datagrid-row-index=" + index + "] td[field=" + field + "]");
        var nextCellTd = cellTd.next();
        if (nextCellTd.length == 0) {
            //下一个列的容器td不存在，视为本行编辑列到达结尾，结束本行编辑
            //console.log("下一个列的容器td不存在，视为本行编辑列到达结尾，结束本行编辑");
            t.datagrid("endEdit", index);

            var rows = t.datagrid("getRows");
            if (!rows || !rows.length) { return; }
            var len = rows.length;
            if (index < len - 1) {
                if (!t.datagrid("isEditing", index + 1)) {
                    t.datagrid("beginEdit", index + 1); 
                } else {
                    var editableField = getFirstAvailableEditorField(t);
                    if (!editableField) {
                        return;
                    }
                    var editorNextRow = t.datagrid("getEditor", { field: editableField, index: index + 1 });
                    if (editorNextRow.actions && $.isFunction(editorNextRow.actions.setFocus)) {
                        if ($.isFunction(editorNextRow.actions.focusable) && editorNextRow.actions.focusable(editorNextRow.target[0]) == true) {
                            editorNextRow.actions.setFocus(editorNextRow.target[0]);
                        }
                        else {
                            //不可聚焦，这不科学
                        }
                    } else {
                        editorNextRow.target.focus();
                    }
                }
            } else {
                opts.onAfterFoucsLastEditor.call(t[0], index, field);
            }

            return;
        }
        var nextField = nextCellTd.attr("field"),
            nextCell = nextCellTd.find("div.datagrid-cell"),
            editor = t.datagrid("getEditor", { field: nextField, index: index });
        if (!editor) {
            //下一个列的editor不存在，说明该列无编辑器，寻找下一个列的editor
            //console.log("下一个列的editor不存在，说明该列无编辑器，寻找下一个列的editor");
            autoNextFieldEditorFocus(t, opts, nextCell, nextField, index);
        }
        else {
            if (editor.actions && $.isFunction(editor.actions.setFocus)) {
                if ($.isFunction(editor.actions.focusable) && editor.actions.focusable(editor.target[0]) == true) {
                    editor.actions.setFocus(editor.target[0]);
                }
                else {
                    //不可聚焦，跳过，寻找下一个列的editor
                    //console.log("不可聚焦，跳过，寻找下一个列的editor");
                    autoNextFieldEditorFocus(t, opts, editor.target, nextField, index);
                }
            } else {
                editor.target.focus();
            }
        }
    }

    //绑定“自动聚焦下一个列的编辑器”事件
    //t：datagrid-jquery对象
    //opts：datagrid-options对象
    //input：输入框jquery对象
    //field：当前在编辑的列名
    var bindAutoFocusNextFieldEditor = function (t, opts, input, field) {
        input.keydown(function (e) {
            if (e.which == 13) {
                var val = $(this).val();
                if (opts.stopEnterFocusWhenEmpty && $.string.isNullOrWhiteSpace(val)) { return; }
                autoNextFieldEditorFocus(t, opts, $(this), field);
            }
        });
    };

    // 初始化事件绑定
    function initExtendEventBind(t, opts) {

        if (opts.enterFocusNextEditor) {
            //开始编辑事件
            var _onBeginEdit = opts.onBeginEdit;
            opts.onBeginEdit = function (index, row) {
                if ($.isFunction(_onBeginEdit)) { _onBeginEdit.call(this, index, row); }

                var fields = getColumnFields(t[0], "all"), editors = fields.map(function (item) { return t.datagrid("getEditor", { index: index, field: item }); });
                if (editors.length == 0) { return; }
                
                editors.forEach(function (item) {
                    if (!item) { return; }
                    if (item.actions && $.isFunction(item.actions.input)) {
                        var theInput = item.actions.input(item.target[0]);
                        bindAutoFocusNextFieldEditor(t, opts, theInput, item.field);
                    } else {
                        bindAutoFocusNextFieldEditor(t, opts, item.target, item.field);
                    }
                });

                $.util.delay(function () {
                    //自动聚焦第一个可聚焦的编辑器
                    for (var i = 0; i < editors.length; i++) {
                        var editor = editors[i];
                        if (!editor) { continue; }
                        if (editor.actions && $.isFunction(editor.actions.setFocus)) {
                            if ($.isFunction(editor.actions.focusable) && editor.actions.focusable(editor.target[0]) == true) {
                                editor.actions.setFocus(editor.target[0]); break;
                            }
                            else {
                                //不可聚焦，跳过
                            }
                        } else {
                            editor.target.focus(); break;
                        }
                    }
                });
            };
        }
    }

    function initializeExtensions(target) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;

        initExtendEventBind(t, opts);
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
                                enterFocusNextEditor: "boolean",
                                stopEnterFocusWhenEmpty: "boolean"
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

        //  扩展 easyui-datagrid 的自定义方法；检查指定列是否配置了可用（非只读且未禁用）编辑器；该方法定义如下参数：
        //      field: 列的 field 名；
        //  返回值：若指定列配置了可用（非只读且未禁用）编辑器则返回 true ，否则返回 false 。
        editorAvailable: function (jq, field) { return editorAvailable(jq[0], field); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {


        //  扩展 easyui-datagrid 的自定义属性；表示开启编辑后是否启用回车后自动聚焦下一个列编辑器的功能；
        //  Boolean 类型值，默认 true。
        enterFocusNextEditor: true,

        //  扩展 easyui-datagrid 的自定义属性；表示在 autoFocusNextFieldEditor 为 true 的前提下，当前编辑器内容若为空，是否停止回车聚焦功能；
        //  Boolean 类型值，默认 false。
        stopEnterFocusWhenEmpty: false,

        //  扩展 easyui-datagrid 的自定义事件；表示聚焦到最后一个单元格编辑器之后触发的事件；该事件定义如下参数：
        //      index: 数据行的索引；
        //      field: 列的 field 名；
        //  注意，该事件接口是指“聚焦到最后一行最后一个单元格编辑器之后触发的事件”，并不是每个数据行的最后一个单元格编辑器聚焦后触发。
        onAfterFoucsLastEditor: function (index, field) { }
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);