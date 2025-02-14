var Lib = {
	tablePage : {
		"layout" : [ 'count', 'prev', 'page', 'next','refresh']
	},
	/* form 种各个事件的处理 */
	initGenrealForm : function(form, layuiForm) {
		layuiForm.on('select', function(data) {
			var dom = data.elem;
			var groupName = $(dom).data("group");
			if (!$.isEmpty(groupName)) {
				Lib._dropdown(layuiForm, data, form, dom, groupName);
				return;
			}
		});

		$(form).find(".date-range-pick").find("input").each(function() {
			laydate = layui.laydate;
			laydate.render({
				elem : $(this)[0],
				range : '至'
			});
		});

		$(form).find(".datetime-range-pick").find("input").each(function() {
            laydate = layui.laydate;
            laydate.render({
                elem : $(this)[0],
                range : '至',
                type: 'datetime'
            });
        });


		$(form).find(".input-date").each(function() {
            laydate = layui.laydate;
            laydate.render({
                elem : $(this)[0],
            });
        })


	},
	_dropdown : function(layuiForm, data, form, select, groupName) {
		var selects = $(form).find("select[data-group='" + groupName + "']");
		var start = 0;
		for (var i = 0; i < selects.length; i++) {
			if ($(select).is(selects[i])) {
				start = i + 1;
				break;
			}
		}
		if (start == selects.length) {
			//最后一个级联，不处理
			return;
		}
		value = data.value;
		if (value != "") {
			Common.post("/core/dict/viewChildren.json", {
				"value" : value,
				"group":groupName
			}, function(serverData) {
				Lib._resetDictSelect(selects[start], serverData);
				layuiForm.render();

			})
		} else {
			for (var j = 0, i = start; i < selects.length; i++, j++) {
				Lib._resetDictSelect(selects[start],[])
			}
			layuiForm.render();
		}

	},

	/* 搜索 */
	doSearchForm : function(form, tableIns, page) {
		var data = form.serializeJson()
		if (page != null) {
			tableIns.reload({
				where : data,
				page: {
				      curr: page
				 }

			});
		} else {
			tableIns.reload({
				where : data
			});
		}

	},
	closeFrame : function() {
		var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
		parent.layer.close(index); // 再执行关闭
	},

	submitForm : function(url,form, paras, callBack) {
		var formPara = form.serializeJson();
		for (var key in paras) {
           formPara[key]=paras[key];
        }
		Common.post(url, formPara, callBack);
	},
	buttonEnable:function(buttonCode,enable){
	    var btn = $("#table-button-"+buttonCode);
	    if(enable){
	        btn.attr('disabled',"false");
	        btn.removeClass("button-disabled")
	    }else{
	        btn.attr('disabled',"true");
	        btn.addClass('button-disabled');
	    }

	},
	getTableHeight : function(queryLine) {
		// 表格相对高度
		if (queryLine == 1) {
			return "full-180";
		} else if (queryLine == 2) {
			return "full-250"
		} else if (queryLine == 3) {
			return "full-350"
		} else {
			return "full"
		}
	}

};
