/**
* jQuery EasyUI 1.4.3
* Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
*
* Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
* To use it on other terms please contact us at info@jeasyui.com
* http://www.jeasyui.com/license_commercial.php
*
* jQuery EasyUI datagrid 组件扩展
* jeasyui.extensions.datagrid.export.js
* 开发 落阳
* 最近更新：2016-06-12
*
* 依赖项：
*   1、jquery.jdirk.js
*
* Copyright (c) 2015 Lixilin personal All rights reserved.
*/
(function ($) {

    var defaults = {
        //解析并导出到excel的地址，用来解析传递过去的列集合、当前页数据集合，并最终导出到excel文件
        exportParserHref: "/Public/ExportToExcelFromDatagrid",
        mergeExtensionProperty: "mergeInfos"
    };

    function delHtmlTag(str) {
        return str.replace(/<[^>]+>/g, "");//去掉所有的html标记
    }

    var getMergeDoms = function (target) {
        var t = $(target), panel = t.datagrid("getPanel");
        return panel.find("div.datagrid-view div.datagrid-body table tr.datagrid-row td.datagrid-td-merged");
    };

    var exportData = function (target, param) {
        var fileType = ["excel"].contains(param.fileType) ? param.fileType : "excel";
        var fileName = param.fileName, isAll = $.string.toBoolean(param.isAll);
        if (fileType == "excel") { return exportDataToExcel(target, fileName, isAll); }
    };

    var exportDataToExcel = function (target, fileName, isAll) {
        var t = $(target), dgOptions = t.datagrid("options"), rows = isAll ? [] : t.datagrid("getRows");
        if (!isAll && rows.length == 0) { return; }
        if (isAll && $.string.isNullOrWhiteSpace(dgOptions.url)) { return; }

        var lastFrozenColumns = getColumns(target, true), lastColumns = getColumns(target, false);
        //冻结列的列数组length，不一定等于常规列的列数组length
        //比如冻结列的合并单元格是2行2列的，而常规列的合并单元格是3行5列的
        //从同一个容器角度来说，冻结列的列数组length小于常规列的列数组length时，应该给冻结列加上rowspan
        //但冻结列的容器和常规列的容器是独立的，所以此时给冻结列加rowspan反而引发js错误
        //那么就需要特别处理这种情况
        //差值，大于0则表示冻结列行数多，小于0则表示常规列行数多，等于0则表示无需处理
        //行数少的，需要补全到同样的行数，并且需要修正其rowspan值
        var diffLength = lastFrozenColumns.length == 0 ? 0 : (lastFrozenColumns.length - lastColumns.length);
        if (diffLength < 0) {
            //常规列行数多
            var abs = Math.abs(diffLength);
            var cloneFrozenColumnsOnTopPosition = $.array.map(lastFrozenColumns[0], function (item) { return $.extend({}, item); });
            for (var i = 0; i < abs ; i++) {
                $.array.insert(lastFrozenColumns, 0, cloneFrozenColumnsOnTopPosition);
            }
            for (var i = 0; i < (abs + 1) ; i++) {
                $.each(lastFrozenColumns[i], function (index, item) {
                    item.rowspan = item.rowspan + abs;
                });
            }
        } else if (diffLength > 0) {
            //冻结列行数多
            var abs = Math.abs(diffLength);
            var cloneColumnsOnTopPosition = $.array.map(lastColumns[0], function (item) { return $.extend({}, item); });
            for (var i = 0; i < abs ; i++) {
                $.array.insert(lastColumns, 0, cloneColumnsOnTopPosition);
            }
            for (var i = 0; i < (abs + 1) ; i++) {
                $.each(lastColumns[i], function (index, item) {
                    item.rowspan = item.rowspan + abs;
                });
            }
        }
        //console.log(lastFrozenColumns);
        //console.log(lastColumns);
        //return;
        var maxGroup = lastFrozenColumns.length >= lastColumns.length ? lastFrozenColumns.length : lastColumns.length;
        var totalColumns = new Array(maxGroup);
        for (var k = 0; k < maxGroup; k++) {
            if (totalColumns[k] == undefined) { totalColumns[k] = []; }
            if (lastFrozenColumns.length > k) {
                $.util.merge(totalColumns[k], lastFrozenColumns[k]);
            }
            if (lastColumns.length > k) {
                $.util.merge(totalColumns[k], lastColumns[k]);
            }
        }
        //用formatter处理rows 为不影响datagrid本身的rows集合，先克隆rows
        var existFields = [], cloneRowsObj = $.extend(true, {}, rows), lastRows = [];
        for (var c in cloneRowsObj) {
            if ($.string.isNumeric(String(c))) { lastRows.push(cloneRowsObj[c]); }
        }

        for (var index = 0; index < totalColumns.length; index++) {
            totalColumns[index].forEach(function (itemCol, itemColIndex) {
                if (itemCol.formatter && $.isFunction(itemCol.formatter)) {
                    lastRows.forEach(function (itemRow, itemRowIndex) {
                        var spe = true, rowIndex = !dgOptions.idField ? itemRowIndex : t.datagrid("getRowIndex", itemRow[dgOptions.idField]);
                        for (var prop in itemRow) {
                            if (prop == itemCol.field) {
                                itemRow[prop] = delHtmlTag(itemCol.formatter.call(itemCol, itemRow[prop], itemRow, rowIndex));
                                if (!existFields.contains(itemCol.field)) { existFields.push(itemCol.field); }
                                spe = false;
                                break;
                            }
                        }
                        if (spe) {
                            //处理特殊情况，遍历了row的属性，却没找到itemCol.field
                            // field 随便写，通过formatter强行显示指定数据
                            //将field的属性写入row中，其值为formatter的返回值
                            if (itemCol.field && !$.string.isNullOrWhiteSpace(itemCol.field)) {
                                //若该field已存在于 其他列的field 中，则不处理formatter，事实上，easyui也不会对这种formatter进行处理
                                if (!existFields.contains(itemCol.field)) {
                                    itemRow[itemCol.field] = delHtmlTag(itemCol.formatter.call(itemCol, itemRow[itemCol.field], itemRow, rowIndex));
                                }
                            }
                        }
                    });
                }
            });
        }

        //从lastRows的每个row中移除属性值类型为 array 或 object 的属性
        for (var k = 0; k < lastRows.length; k++) {
            var row = lastRows[k];
            for (var pro in row) {
                var tempValue = row[pro];
                if ($.isArray(tempValue) || $.util.isObject(tempValue)) { delete row[pro]; }
            }
        }

        if (!isAll) {
            //检测rows的html中是否包含合并标记
            var mergeDoms = getMergeDoms(target);
            if (mergeDoms.length > 0) {
                var mergeExtensionProperty = defaults.mergeExtensionProperty + "_" + $.util.guid("N")
                mergeRules = [];

                //把合并信息扩展进rows中
                $.each(mergeDoms, function (index, item) {
                    var td = $(item), tr = td.parent(), field = td.attr("field"), rowIndex = tr.attr("datagrid-row-index"),
                        rowspan = td.attr("rowspan"), colspan = td.attr("colspan");
                    if ($.string.isNullOrWhiteSpace(rowspan)) { rowspan = "1"; }
                    if ($.string.isNullOrWhiteSpace(colspan)) { colspan = "1"; }
                    var mergeRule = { index: rowIndex, field: field, rowspan: rowspan, colspan: colspan };

                    var mergeRow = lastRows[rowIndex];
                    if (!mergeRow[mergeExtensionProperty]) {
                        mergeRow[mergeExtensionProperty] = [mergeRule];
                    } else {
                        mergeRow[mergeExtensionProperty].push(mergeRule);
                    }
                });

                //序列化rows中的合并信息
                $.each(lastRows, function (index, row) {
                    if (row[mergeExtensionProperty]) {
                        row[mergeExtensionProperty] = JSON.stringify(row[mergeExtensionProperty]);
                    }
                });
            }
        }
        

        //组装参数
        var param = {
            data: JSON.stringify(lastRows),
            columns: JSON.stringify(totalColumns),
            fileName: fileName == undefined ? "" : fileName,
            url: isAll ? dgOptions.url : ""
        };


        //console.log("********************");
        //console.log("最终列集合：");
        //console.log(totalColumns);
        //console.log("********************");
        //console.log("最终本页数据集合：");
        //console.log(lastRows);
        //console.log("********************");
        //console.log("参数：");
        //console.log(param);
        //console.log("********************");
        //return;
        //模拟form提交完成导出excel操作
        $("iframe[name='hiddenIframe']").remove();
        $("form[target='hiddenIframe']").remove();

        //form的action指向，需结合后台配合，因此本扩展不是真正的easyui扩展。
        var tempForm = $('
');
        for (var prop in param) {
            tempForm.append("
" + param[prop] + "
");
        }

        $("body").append("").append(tempForm);
        tempForm.submit();
    };

    var getColumns = function (target, frozen) {
        var t = $(target), dgOptions = t.datagrid("options");

        //取目标列集合
        var fColumns = frozen == true ? (dgOptions.frozenColumns || [[]]) : (dgOptions.columns || [[]]).clone();

        //过滤checkbox列和hidden列
        var ddddd = fColumns.clone();
        for (var k = 0; k < fColumns.length; k++) {
            var tempLen = fColumns[k].length;
            for (var inK = 0; inK < tempLen; inK++) {
                var needRemove = false, itemFc = fColumns[k][inK];
                //checkbox列
                if (itemFc.checkbox && itemFc.checkbox == true) { needRemove = true; }
                //hidden列
                if (!needRemove && itemFc.hidden && itemFc.hidden == true) { needRemove = true; }
                //无title的列、title过滤html标签后为空的列
                if (!needRemove && ($.string.isNullOrWhiteSpace(itemFc.title))) { needRemove = true; }
                if (!needRemove) {
                    itemFc.title = delHtmlTag(itemFc.title);
                    if ($.string.isNullOrWhiteSpace(itemFc.title)) { needRemove = true; }
                }

                if (needRemove) { fColumns[k].removeAt(inK); tempLen--; inK--; }
            }
        }
        //计算列集合的总列数总行数
        //总行数 = fColumns.length
        //总列数 = fColumns[item].colspan之和中最大的
        var colspanCount = $.array.max($.array.map(fColumns, function (itemFc) { return $.array.sum(itemFc, function (item) { return item.colspan || 1; }) })),
            rowspanCount = fColumns.length;

        var lastColumns = [];
        //组建最终列集合的数组结构
        for (var i = 0; i < rowspanCount; i++) {
            lastColumns[i] = new Array(colspanCount);
        }

        var getFixedColumnIndex = function (a) {
            for (var i = 0; i < a.length; i++) {
                if (a[i] == undefined) {
                    return i;
                }
            }
            return -1;
        };
        //往最终列集合里填充数据
        for (var columIndex = 0; columIndex < fColumns.length; columIndex++) {
            fColumns[columIndex].forEach(function (itemFc, itemIndex) {
                var fieldIndex = getFixedColumnIndex(lastColumns[columIndex]); //找到第一个未赋值的元素索引
                if (fieldIndex >= 0) {
                    for (var c = fieldIndex; c < colspanCount ; c++) {
                        var tempCol = $.extend({}, itemFc, {});
                        if (tempCol.colspan == undefined) { tempCol.colspan = 1; }
                        if (tempCol.rowspan == undefined) { tempCol.rowspan = 1; }
                        if ((itemFc.colspan || 1) > 1) {
                            //若列是跨列的，则认为该列的field无效
                            delete tempCol.field;
                        }
                        lastColumns[columIndex][c] = tempCol;
                        if ((itemFc.rowspan || 1) > 1) {
                            for (var d = 1; d < itemFc.rowspan; d++) {
                                if (columIndex + d <= rowspanCount) {
                                    lastColumns[columIndex + d][c] = $.extend({}, tempCol);
                                }
                            }
                        }
                        if ((itemFc.colspan || 1) > 1) {
                            for (var d = 1; d < itemFc.colspan; d++) {
                                if (c + d <= colspanCount) {
                                    lastColumns[columIndex][c + d] = $.extend({}, tempCol);
                                }
                            }
                        }
                        break;
                    }
                }
            });
        }

        return lastColumns;
    };

    var methods = {

        //  扩展 easyui-datagrid 的自定义方法；导出当前页数据到文件；该方法定义如下参数：
        //      param:  这是一个 JSON-Object 对象，该 JSON-Object 可以包含如下属性：
        //          fileType:        字符串，表示要导出的目标文件类型，其值可以是 excel ，若不传递该参数，则当做 excel ；
        //          fileName:        字符串，表示要导出的目标文件名称，若不传递该参数，则使用默认文件名；
        //          isAll:           Boolean值，是否导出全部而非仅当前页数据，如果不传入该参数默认为 false 即导出当前页数据。
        //                           当 isAll 为 true 时，需要当前 easyui-datagrid 控件的 url 属性指示的服务器数据源支持查询所有数据
        //                          （以 rows: 0 方式不分页查询所有数据）。
        //  返回值：返回表示当前 easyui-datagrid 控件的 jQuery 链式对象。
        exportData: function (jq, param) {
            return jq.each(function () {
                exportData(this, param);
            });
        }
    };

    if ($.fn.datagrid.extensions != null && $.fn.datagrid.extensions.methods != null) {
        $.extend($.fn.datagrid.extensions.methods, methods);
    }

    $.extend($.fn.datagrid.methods, methods);

})(jQuery);