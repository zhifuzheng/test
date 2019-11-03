/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.extEdit.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-12-02
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.datagrid.editors.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function () {

    $.util.namespace("$.fn.datagrid.extensions");

    $.extend($.fn.datagrid.extensions, {

        beginEdit: $.fn.datagrid.methods.beginEdit,

        endEdit: $.fn.datagrid.methods.endEdit,

        cancelEdit: $.fn.datagrid.methods.cancelEdit
    });


    function getRowDom(target, index) {
        if (!$.isNumeric(index) || index < 0) { return $(); }
        var t = $(target), panel = t.datagrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row[datagrid-row-index=" + index + "]");


        var t = $(target),
            p = t.datagrid("getPanel");
        return p.find("div.datagrid-view div.datagrid-body table.datagrid-btable tr.datagrid-row[datagrid-row-index=" + index + "]");
    }

    function beginEdit(target, index) {
        var t = $(target),
            ret = $.fn.datagrid.extensions.beginEdit.call(t, t, index);
        setSingleEditing(target, index);
        createRowExtEditor(target, index);
        setRowEditorFocus(target, index);
        return ret;
    }

    function endEdit(target, index) {
        var t = $(target),
            ret = $.fn.datagrid.extensions.endEdit.call(t, t, index);
        if (t.datagrid("validateRow", index)) {
            disposeRowExtEditor(target, index);
        }
        return ret;
    }

    function cancelEdit(target, index) {
        var t = $(target),
            ret = $.fn.datagrid.extensions.cancelEdit.call(t, t, index);
        disposeRowExtEditor(target, index);
        return ret;
    }


    function createRowExtEditor(target, index) {
        var state = $.data(target, "datagrid"),
            opts = state.options;
        if (!opts.extEditing) { return; }
        var tr = getRowDom(target, index);
        if (!tr || !tr.length) { return; }
        var t = $(target),
            p = t.datagrid("getPanel"),
            v = p.find("div.datagrid-view"),
            v1 = v.find("div.datagrid-view1"),
            v2 = v.find("div.datagrid-view2"),
            b = v2.find("div.datagrid-body").addClass("datagrid-body-rowediting"),
            width = v1.outerWidth(),
            height = tr.outerHeight(),
            pos = tr.position(),
            top = pos.top + height + b.scrollTop() - v2.find("div.datagrid-header").outerHeight(),
            d = $("
").appendTo(b).css("top", top).attr("datagrid-row-index", index);

        $("").appendTo(d).linkbutton({
            plain: false, iconCls: "icon-ok",
            text: "保存"
        }).click(function () {
            endEdit(target, index);
        });
        $("").appendTo(d).linkbutton({
            plain: false,
            iconCls: "icon-cancel",
            text: "取消"
        }).click(function () {
            cancelEdit(target, index);
        });

        var diff = (p.outerWidth() - d.outerWidth()) / 2 - width,
            left = diff > 0 ? diff : 0;
        d.css("left", left);
    }

    function disposeRowExtEditor(target, index) {
        var b = $(target).datagrid("getPanel").find("div.datagrid-view div.datagrid-view2 div.datagrid-body");
        if (!b.length) { return; }
        var d = b.find("div.datagrid-rowediting-panel[datagrid-row-index=" + index + "]").remove();
        if (!b.find("div.datagrid-rowediting-panel[datagrid-row-index]").length) {
            b.removeClass("datagrid-body-rowediting");
        }
    }

    function setSingleEditing(target, index) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;
        if (opts.singleEditing && (state.lastEditIndex != null && state.lastEditIndex != undefined && state.lastEditIndex != index)) {
            endEdit(target, state.lastEditIndex);
        }
        state.lastEditIndex = index;
    }

    function setRowEditorFocus(target, index) {
        var t = $(target),
            state = $.data(target, "datagrid"),
            opts = state.options;
        if (opts.autoFocusField) {
            var editors = t.datagrid("getEditors", index);
            if (editors.length) {
                var editor = $.array.first(editors, function (val) { return val.field == opts.autoFocusField; });
                if (!editor) {
                    editor = editors[0];
                }
                if (editor) {
                    $.util.delay(function () {
                        setEditorFocus(editor);
                    });
                }
            }
        }
    }

    function setEditorFocus(editor) {
        if (!editor || !editor.target || !editor.target.length) {
            return;
        }
        if (editor.actions && $.isFunction(editor.actions.setFocus)) {
            if (editor.actions.focusable == undefined || ($.isFunction(editor.actions.focusable) && editor.actions.focusable(editor.target[0]) == true)) {
                editor.actions.setFocus(editor.target[0]);
            }
        } else {
            if (editor.target.is(":hidden")) {
                if ($.easyui.isComponent(editor.target, "textbox")) {
                    editor.target.textbox("textbox").focus();
                }
            } else {
                editor.target.focus();
            }
        }
    }


    var methods = $.fn.datagrid.extensions.methods = {

        //  重写 easyui-datagrid 的原生方法 beginEdit ，以支持自定义属性 extEditing、singleEditing、autoFocusField 的扩展功能；
        beginEdit: function (jq, index) { return jq.each(function () { beginEdit(this, index); }); },

        //  重写 easyui-datagrid 的原生方法 endEdit ，以支持自定义属性 extEditing 的扩展功能；
        endEdit: function (jq, index) { return jq.each(function () { endEdit(this, index); }); },

        //  重写 easyui-datagrid 的原生方法 cancelEdit ，以支持自定义属性 extEditing 的扩展功能；
        cancelEdit: function (jq, index) { return jq.each(function () { cancelEdit(this, index); }); }
    };

    var defaults = $.fn.datagrid.extensions.defaults = {

        //  扩展 easyui-datagrid 的自定义属性，该属性表示在触发 beginEdit 事件后，是否构建仿 ext-grid-rowediting 行编辑的“保存”和“取消”按钮面板；
        //  Boolean 类型值，默认为 true。
        extEditing: true,

        //  扩展 easyui-datagrid 的自定义属性，该属性表示在一个时刻是否只允许一行数据开启编辑状态(当某行数据开启编辑状态时，其他正在编辑的行将会被自动执行 endEdit 操作)；
        //  Boolean 类型值，默认为 true。
        singleEditing: true,

        //  扩展 easyui-datagrid 的自定义属性，该属性表示在对某行执行 beginEdit 后，是否让特定字段的编辑器对象自动获取输入焦点；
        //  该属性可以为 Boolean（默认，true） 类型或者 String 类型值；
        //  如果是 Boolean 类型，则表示是否启用编辑器对象自动获取焦点功能，在值为 true 的情况下该行的第一个编辑器对象将在 beginEdit 操作后自动获取焦点；
        //  如果是 String 类型，其值表示指定的 field 名称，则表示启用该功能并且指定的 field 将在 beginEdit 操作后自动获取焦点。
        autoFocusField: true
    };

    $.extend($.fn.datagrid.defaults, defaults);
    $.extend($.fn.datagrid.methods, methods);

})(jQuery);