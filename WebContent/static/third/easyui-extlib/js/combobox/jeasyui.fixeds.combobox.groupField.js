/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI combobox bug修复
* jeasyui.fixeds.combobox.groupField.js
* 开发 落阳
* 最近更新：2015-11-06
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 Lixilin personal All rights reserved.
*/
(function ($) {

    $.util.namespace("$.fn.combobox.extensions");

    $.fn.combobox.extensions._loadData = $.fn.combobox.methods.loadData;
    var loadData = function (target, data, remainText) {
        var state = $.data(target, 'combobox');
        var opts = state.options;
        state.data = opts.loadFilter.call(target, data);
        state.groups = [];
        data = state.data;

        var selected = $(target).combobox('getValues');
        var dd = [];
        //添加一个记录group历史记录的数组
        var tempGroups = [];
        //添加存放组和组对应的项的对象
        var tempDataForGroups = {}, tempDataForGroupItems = {};
        for (var i = 0; i < data.length; i++) {
            var row = data[i];
            var v = row[opts.valueField] + '';
            var s = row[opts.textField];
            var g = row[opts.groupField];

            if (g) {
                if (!$.array.contains(tempGroups, g)) {
                    tempGroups.push(g);
                    state.groups.push(g);
                    //添加组信息
                    var temp1 = '
';
                    temp1 += opts.groupFormatter ? opts.groupFormatter.call(target, g) : g;
                    temp1 += '
';
                    tempDataForGroups[g] = temp1;
                }
            } else {
                //无分组
                g = "";
            }

            //添加组-项信息
            var cls = 'combobox-item' + (row.disabled ? ' combobox-item-disabled' : '') + (g ? ' combobox-gitem' : '');
            var temp2 = '
';
            temp2 += opts.formatter ? opts.formatter.call(target, row) : s;
            temp2 += '
';

            if (tempDataForGroupItems[g] == undefined) {
                tempDataForGroupItems[g] = [];
            }
            tempDataForGroupItems[g].push(temp2);

            if (row['selected'] && $.inArray(v, selected) == -1) {
                selected.push(v);
            }
        }
        //重组tempDataForGroups和tempDataForGroupItems
        var hasGroup = false;
        for (var c in tempDataForGroups) {
            var item = tempDataForGroups[c]; //组信息
            if ($.util.isString(item)) {
                hasGroup = true;
                dd.push(item);
                var items = tempDataForGroupItems[c];
                if (items && $.util.isArray(items)) {
                    items.forEach(function (row) {
                        dd.push(row);
                    });
                }
            }
        }

        if (!hasGroup) {
            //只组装tempDataForGroupItems
            var items = tempDataForGroupItems[""];
            if (items && $.util.isArray(items)) {
                items.forEach(function (row) {
                    dd.push(row);
                });
            }
        }

        $(target).combo('panel').html(dd.join(''));

        if (opts.multiple) {
            setValues(target, selected, remainText);
        } else {
            setValues(target, selected.length ? [selected[selected.length - 1]] : [], remainText);
        }

        opts.onLoadSuccess.call(target, data);
    };

    function setValues(target, values, remainText) {
        var opts = $.data(target, 'combobox').options;
        var panel = $(target).combo('panel');

        if (!$.isArray(values)) { values = values.split(opts.separator) }
        panel.find('div.combobox-item-selected').removeClass('combobox-item-selected');
        var vv = [], ss = [];
        for (var i = 0; i < values.length; i++) {
            var v = values[i];
            var s = v;
            opts.finder.getEl(target, v).addClass('combobox-item-selected');
            var row = opts.finder.getRow(target, v);
            if (row) {
                s = row[opts.textField];
            }
            vv.push(v);
            ss.push(s);
        }

        if (!remainText) {
            $(target).combo('setText', ss.join(opts.separator));
        }
        $(target).combo('setValues', vv);
    }


    //解析data，若该 combobox 存在 groupField ，则重组 data，使其按照 groupField 排序。
    function parseRourceData(groupField, data) {
        if ($.string.isNullOrWhiteSpace(groupField)) { return data; }

        var tempGroups = [], tempItems = [];
        var len = data.length;
        //遍历data，分离出有groupField的项和没有groupField的项，并且对groupField依次排序
        for (var index = 0; index < len; index++) {
            var item = data[index], groupName = item[groupField];
            if ($.string.isNullOrWhiteSpace(groupName)) { tempItems.push(item); data.removeAt(index); index--; len--; }
            else {
                if (!$.array.contains(tempGroups, groupName)) { tempGroups.push(groupName); }
            }
        }
        //遍历依次排序的groupName集合
        for (var index = 0; index < tempGroups.length; index++) {
            var groupName = tempGroups[index];
            var dataLen = data.length;
            for (var dataIndex = 0; dataIndex < dataLen; dataIndex++) {
                var item = data[dataIndex];
                if (groupName == item[groupField]) { tempItems.push(item); data.removeAt(dataIndex); dataIndex--; dataLen--; }
            }
        }
        return tempItems;
    }


    $.fn.combobox.extensions._loader = $.fn.combobox.defaults.loader;
    var loader = function (param, success, error) {
        var t = $(this), opts = t.combobox('options');
        if (!opts.url) return false;
        $.ajax({
            type: opts.method,
            url: opts.url,
            data: param,
            dataType: 'json',
            success: function (data) {
                //在这里重组data，使其按照 groupField 排序。
                var newData = parseRourceData(opts.groupField, data);
                success(newData);
            },
            error: function () {
                error.apply(this, arguments);
            }
        });
    };


    var defaults = $.fn.combobox.extensions.defaults = {
        // 重写 easyui-combobox 的 loader 属性，以修复“使用groupField对数据进行分组时，必须将同组数据按顺序排列，否则无法识别同组数据”的bug
        loader: loader
    };

    var methods = $.fn.combobox.extensions.methods = {

        //  重写 easyui-combobox 的 loadData 方法；修复其“使用groupField对数据进行分组时，必须将同组数据按顺序排列，否则无法识别同组数据”的bug；该方法定义如下参数：
        //      data:  表示要加载的数据集；
        //  返回值：返回表示当前 easyui-combobox 控件的 jQuery 链式对象。
        loadData: function (jq, data) {
            return jq.each(function () {
                loadData(this, data);
            });
        }
    };


    $.extend($.fn.combobox.defaults, defaults);
    $.extend($.fn.combobox.methods, methods);

})(jQuery);