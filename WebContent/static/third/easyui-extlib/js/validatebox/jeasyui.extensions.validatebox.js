/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI validatebox 扩展
* jeasyui.extensions.validatebox.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-10-28
*
* 依赖项：
*   1、jquery.jdirk.js
*   2、jeasyui.extensions.validatebox.css
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($, undefined) {

    $.util.namespace("$.fn.validatebox.extensions");


    function setPrompt(target, prompt) {
        var t = $(target),
            state = $.data(target, "validatebox"),
            opts = state.options;
        opts.prompt = prompt || "";
        t.attr("placeholder", opts.prompt);
        if (!$.html5.testProp("placeholder", t[0].nodeName)) {
            t.unbind("blur.validatebox-extensions focus.validatebox-extensions").bind({
                "focus.validatebox-extensions": function () {
                    t.removeClass("validatebox-prompt");
                    if (t.val() == opts.prompt) {
                        t.val("");
                    }
                },
                "blur.validatebox-extensions": function () {
                    if (!t.val() && opts.prompt) {
                        t.addClass("validatebox-prompt").val(opts.prompt);
                    } else {
                        t.removeClass("validatebox-prompt");
                    }
                }
            }).trigger(t.is(":focus") ? "focus" : "blur");
        }
    };

    function setValue(target, value) {
        var t = $(target),
            state = $.data(target, "validatebox"),
            opts = state.options,
            val = t.val();
        if (val != value) {
            t.val(opts.value = (value ? value : ""));
        }
        t.validatebox("validate");
    };

    function getValue(target) {
        return $(target).val();
    };

    function clear(target) {
        $(target).validatebox("setValue", "");
    };

    function reset(target) {
        var t = $(target),
            state = $.data(target, "validatebox"),
            opts = state.options;
        t.validatebox("setValue", opts.originalValue ? opts.originalValue : "");
    };

    function resize(target, width) {
        var t = $(target),
            state = $.data(target, "validatebox"),
            opts = state.options;
        t._outerWidth(opts.width = width);
    };

    function setDisabled(target, disabled) {
        var t = $(target),
            state = $.data(target, "validatebox");
        if (disabled) {
            if (state && state.options) { state.options.disabled = true; }
            t.attr("disabled", true);
        } else {
            if (state && state.options) { state.options.disabled = false; }
            t.removeAttr("disabled");
        }
    };

    function setReadonly(target, readonly) {
        var t = $(target),
            state = $.data(target, "validatebox");
        readonly = readonly == null || readonly == undefined ? true : !!readonly;

        if (state && state.options) {
            state.options.readonly = readonly;
        }
        if (readonly) {
            t.addClass("validatebox-readonly").attr("readonly", true);
        } else {
            t.removeClass("validatebox-readonly").removeAttr("readonly");
        }
    };





    var _validatebox = $.fn.validatebox;
    $.fn.validatebox = function (options, param) {
        if (typeof options == "string") {
            return _validatebox.apply(this, arguments);
        }
        options = options || {};
        return this.each(function () {
            var t = $(this),
                isInited = $.data(this, "validatebox") ? true : false,
                opts = isInited ? options : $.extend({},
                    $.fn.validatebox.parseOptions(this),
                    $.parser.parseOptions(this,
                        ["prompt", "width", "cls", { autoFocus: "boolean", autoValidate: "boolean" }]
                    ),
                    {
                        value: (t.val() || undefined),
                        disabled: (t.attr("disabled") ? true : undefined),
                        readonly: (t.attr("readonly") ? true : undefined)
                    },
                    options
                );
            _validatebox.call(t, opts);
            if (!isInited) {
                initialize(this);
            }
        });
    };
    $.union($.fn.validatebox, _validatebox);


    function initialize(target) {
        var t = $(target),
            state = $.data(target, "validatebox"),
            opts = state.options;
        if (!opts._initialized) {
            t.addClass("validatebox-f").change(function () {
                opts.value = $(this).val();
                if ($.isFunction(opts.onChange)) {
                    opts.onChange.call(target, opts.value);
                }
            });
            opts.originalValue = opts.value;
            if (opts.value) {
                setValue(target, opts.value);
            }
            if (opts.width && !t.parent().is("span.textbox,span.spinner,span.searchbox")) {
                resize(target, opts.width);
            }
            setPrompt(target, opts.prompt);
            if (opts.autoFocus) {
                $.util.delay(function () { t.focus(); });
            }
            if (!opts.autoValidate) {
                t.removeClass("validatebox-invalid");
                hideTip(target);
            }
            if (opts.cls) {
                t.addClass(opts.cls);
            }
            if (opts.editable != undefined && !opts.editable) {
                opts.readonly = true;
            }
            setDisabled(target, opts.disabled);
            setReadonly(target, opts.readonly);
            opts._initialized = true;
        }
    };

    function hideTip(target) {
        var state = $.data(target, "validatebox");
        state.tip = false;
        $(target).tooltip("hide");
    };



    var defaults = $.fn.validatebox.extensions.defaults = {

        // 扩展 easyui-validatebox 控件的自定义属性；该属性功能类似于 easyui-searchbox 的 prompt 属性，表示该验证输入框的提示文本；
        // String 类型值，默认为 null。
        prompt: null,

        // 扩展 easyui-validatebox 控件的自定义属性；该属性表示在当前页面加载完成后，该 easyui-validatebox 控件是否自动获得焦点。
        // Boolean 类型值，默认为 false。
        autoFocus: false,

        // 扩展 easyui-validatebox 控件的自定义属性；表示是否在该控件初始化完成后立即进行一次验证；默认为 true。
        // Boolean 类型值，默认为 true。
        autoValidate: true,

        // 扩展 easyui-validatebox 控件的自定义属性；表示其初始化时的值。
        // String 类型值，默认为 null。
        value: null,

        // 扩展 easyui-validatebox 控件的自定义属性；表示其初始化时的宽度值。
        // Number 类型值，默认为 null。
        width: null,

        // 扩展 easyui-validatebox 控件的自定义属性；表示该控件在初始化完成后是否设置其为禁用状态(disabled)；默认为 false。
        // Boolean 类型值，默认为 false。
        disabled: false,

        // 扩展 easyui-validatebox 控件的自定义属性；表示该控件在初始化完成后是否设置其为只读状态(readonly)；默认为 false。
        // Boolean 类型值，默认为 false。
        readonly: false,

        // 扩展 easyui-validatebox 控件的自定义属性；表示 easyui-validatebox 初始化时默认需要加载的样式类名；
        // String 类型值，默认为 null。
        // 该值将会被作为 html-class 属性在 easyui-validatebox 初始化完成后加载至 html 标签上。
        cls: null,

        // 扩展 easyui-validatebox 控件的自定义扩展事件，表示输入框在值改变时所触发的事件。
        onChange: function (value) { }
    };

    var methods = $.fn.validatebox.extensions.methods = {

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 设置当前 easyui-validatebox 控件的 prompt 值；
        // 该方法的参数 prompt 表示将被设置的 prompt 值；
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        setPrompt: function (jq, prompt) {
            return jq.each(function () { setPrompt(this, prompt); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 设置当前 easyui-validatebox 控件的输入值。
        // 该方法的参数 value 表示要被设置的值；为一个 String 类型值。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        setValue: function (jq, value) {
            return jq.each(function () { setValue(this, value); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 获取当前 easyui-validatebox 控件的输入值。
        // 返回值：返回当前 easyui-validatebox 控件输入值；
        getValue: function (jq) {
            return getValue(jq[0]);
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 清除当前 easyui-validatebox 控件的输入值，使其为空。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        clear: function (jq) {
            return jq.each(function () { clear(this); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 重置当前 easyui-validatebox 控件的输入值，使其为初始化时的值。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        reset: function (jq) {
            return jq.each(function () { reset(this); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 设置当前 easyui-validatebox 控件的宽度（width）值。
        // 该方法的参数 width 表示将被设置的宽度（width）值；为一个 Number 类型值；该参数可选。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        resize: function (jq, width) {
            return jq.each(function () { resize(this, width); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 启用当前 easyui-validatebox 控件的输入状态。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        enable: function (jq) {
            return jq.each(function () { setDisabled(this, false); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 禁用当前 easyui-validatebox 控件的输入状态。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        disable: function (jq) {
            return jq.each(function () { setDisabled(this, true); });
        },

        // 扩展 easyui-validatebox 控件的自定义方法；
        // 设置当前 easyui-validatebox 控件的为只读状态。
        // 该方法的参数 readonly 是一个可选的 bool 类型值，默认值为 true，表示启用或禁用该控件的只读状态。
        // 返回值：返回表示当前 easyui-validatebox 控件的 jQuery 链式对象；
        readonly: function (jq, readonly) {
            return jq.each(function () { setReadonly(this, readonly); });
        }
    };

    $.extend($.fn.validatebox.defaults, defaults);
    $.extend($.fn.validatebox.methods, methods);



    // 修改 jQuery 本身的成员方法 val；使之支持 easyui-validatebox 的扩展属性 prompt。
    var core_val = $.fn.val;
    $.fn.val = function (value) {
        if (this.length && this.is(".validatebox-text.validatebox-prompt") && !$.html5.testProp("placeholder", this[0].nodeName)) {
            var val, opts = this.validatebox("options");
            if (arguments.length == 0) {
                val = core_val.apply(this, arguments);
                return val == opts.prompt ? "" : val;
            }
            if (value && value != opts.prompt) {
                this.removeClass("validatebox-prompt");
            }
        }
        return core_val.apply(this, arguments);
    };


})(jQuery);