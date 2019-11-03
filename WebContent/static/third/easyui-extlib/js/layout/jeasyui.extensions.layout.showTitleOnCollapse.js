/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI layout 扩展
* jeasyui.extensions.layout.showTitleOnCollapse.js
* 开发 夏悸
* 由 落阳 整理
* 最近更新：2015-11-17
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.layout.extensions");

    $.extend($.fn.layout.extensions, { onCollapse: $.fn.layout.paneldefaults.onCollapse });

    var showTitleOnCollapse = function () {
        if ($.isFunction($.fn.layout.extensions.onCollapse)) { $.fn.layout.extensions.onCollapse.call(this); }
        
        var layout = $(this).parents(".layout"), layoutOpts = layout.data("layout").options;
        if (!layoutOpts.showTitleOnCollapse) { return; }

        //获取当前region的配置属性
        var opts = $(this).panel("options");
        //获取key
        var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase() + opts.region.substring(1); 
        //从layout的缓存对象中取得对应的收缩对象
        var expandPanel = layout.data("layout").panels[expandKey];
        if (expandPanel) {
            //存在，说明是调用Collapse方法触发onCollapse
            var title = opts.collapsedTitle == null ? opts.title : opts.collapsedTitle;
            //针对横向和竖向的不同处理方式
            if (opts.region == "west" || opts.region == "east") {
                //竖向的文字打竖,其实就是切割文字加br
                var split = [];
                for (var i = 0; i < title.length; i++) {
                    split.push(title.substring(i, i + 1));
                }
                expandPanel.panel("body").addClass("panel-title").css("text-align", "center").html(split.join("
"));
            } else {
                expandPanel.panel("setTitle", title);
            }
        }
        else {
            //不存在，说明初始化的options中已设定了collapsed属性为true
            //初始化 layout 时，次序是： 1、初始化 panel => 2、panel 内部调用 onCollapse => 3、layout 内部调用 setInitState > collapsePanel（用于将折叠后的panel独立存储在 state.panels 中）
            //因此在这里事件中，layout.data("layout").panels 中是不包含 expandKey 属性的，因此此事件对应的是步骤2 ，而 expandKey 属性的追加，是在步骤3完成的。
            //若要使本扩展支持“layout-panel 在初始化的时候通过 collapsed属性为true 方式进行折叠”的情况，好像必须修改步骤3中的代码，这量比较大，好像也不太合理。暂时不处理。
            //console.log(expandKey);
            //console.log(layout.data("layout").panels);
        }
    };


    var methods = $.fn.layout.extensions.methods = {

    };

    var defaults = $.fn.layout.extensions.defaults = {

        //  扩展 easyui-layout 控件的自定义属性，该属性表示当 easyui-layout 的 panel 折叠时是否显示其 collapsedTitle（若该属性未定义，则显示 panel 的 title 内容），默认为 false 。
        //  该扩展目前尚不支持“以设置panel的初始化collapsed属性为true”的方式，即无法在 easyui-layout 初始化时对其 panel 进行若折叠则显示标题的处理。
        showTitleOnCollapse: false
    };

    var paneldefaults = {
        //  扩展 easyui-layout 控件 的 panel 的自定义属性，该属性表示扩展属性 showTitleOnCollapse 为 true 的 easyui-layout 在折叠 panel 时要显示的面板标题。
        //  该属性未定义时表示使用 easyui-layout 的 panel 的 title 作为其值。
        collapsedTitle: null,

        //  重写 easyui-layout 控件的 panel 配置中的原生事件 onCollapse，以支持相应扩展功能。
        onCollapse: showTitleOnCollapse
    };

    $.extend($.fn.layout.paneldefaults, paneldefaults);

    $.extend($.fn.layout.defaults, defaults);
    $.extend($.fn.layout.methods, methods);

})(jQuery);