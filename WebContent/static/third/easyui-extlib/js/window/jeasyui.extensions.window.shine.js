/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI window 扩展
* jeasyui.extensions.window.shine.js
* 开发 流云
* 由 落阳 整理
* 最近更新：2015-11-13
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 ChenJianwei personal All rights reserved.
*/
(function ($) {

    $(function () {

        //  点击模式（options.modal = true）对话框（例如 easyui-messager、easyui-window、easyui-dialog）的背景遮蔽层使窗口闪动
        $("body").on("click", "div.window-mask", function (e) {
            $(this).prevAll("div.panel.window:first").shine(125, 6);
        });

    });

})(jQuery);