$.fn.extend({
	pageGuide:function(json){
		var pageWords=["首页","上一页","下一页","尾页","当前第_页","共_页","每页_条"];
		var language=json.language;
		if(language=="en-US"){
			language="zh-CN";
			pageWords=["First Page","Previous Page","Next Page","Last Page","current page :_","total page : _","per page : _"];
		}
		var pageIndex = json.pageIndex;
		var pageSize = json.pageSize;
		var sumPage = json.sumPage;
		//var elem=json.elem;
		var elem = $(this);
		var flag = json.onePageFlag;
		var jump = json.jump;
		var selectPage=json.selectPage;
		var style=json.style;
		
		if(!flag && sumPage < 2){
			elem.hide("");
			return;
		}
		
		// 分页的导航数量
		var pageGuideCount = 7;
		var index = parseInt(pageGuideCount / 2)+1;
		//开始索引
		var startIndex = 1;
		//结束索引
		var endIndex = pageGuideCount;
		if(sumPage <= pageGuideCount){
			startIndex = 1;
			endIndex = sumPage;
		}else{
			if(pageIndex > index){
				var a = sumPage - pageGuideCount;
				var b = pageIndex - index;
				startIndex =  a >= b?(startIndex + b):startIndex + a;
				endIndex = a >= b?(pageGuideCount + b):pageGuideCount + a;
			}
		}
		var str = "";
		// 上一页
		if (pageIndex <= 1) {
			str += "<span>"+pageWords[0]+"</span>";
			str += "<span>"+pageWords[1]+"</span>";
		} else {

			str += "<a href='javascript:;' class='page-first'>"+pageWords[0]+"</a>";
			str += "<a href='javascript:;' class='page-prev'>"+pageWords[1]+"</a>";
			if(startIndex>1 && endIndex-startIndex==pageGuideCount-1){
				str += "<span>...</span>";
			}
		}
		for ( var i = startIndex; i <= endIndex; i++) {
			if (i == pageIndex) {
				str += "<span class='page-curr'>"+i+"</span>";
			} else {
				str += "<a href='javascript:;' class='page-i'>"+ i + "</a>";
			}
		}
		
		// 下一页
		if (pageIndex >= sumPage) {
			str += "<span>"+pageWords[2]+"</span>";
			str += "<span class='page-last'>"+pageWords[3]+"</span>";
		} else {
			if(endIndex<sumPage && endIndex-startIndex==pageGuideCount-1){
				str += "<span>...</span>";
			}
			str += "<a href='javascript:;' class='page-next' >"+pageWords[2]+"</a>";
			str += "<a href='javascript:;' class='page-last' >"+pageWords[3]+"</a>";
		}
		str += "<em>"+pageWords[4].split("_")[0]+"</em><input type='number' class='page_input' value='"+pageIndex+"' max='"
		+sumPage+"' min='1' /><em>"+pageWords[4].split("_")[1]+"，"
		+pageWords[5].split("_")[0]+sumPage+pageWords[5].split("_")[1]+"，</em>";
		
		str += "<em>"+pageWords[6].split("_")[0]+"</em>"+
		"<select class='page_select'>";
		if(!selectPage){
			selectPage = [10,20,50,100];
		}
		for( var i=0;i<selectPage.length;i++ ){
			var page = selectPage[i];
			var sel = page == pageSize ? "selected" : "";
			str +="<option value='"+page+"' "+sel+">"+page+"</option>";
		}
		str +="</select><em class='second_em'>"+pageWords[6].split("_")[1]+"</em>";
		
		elem.html(str);
		
		if(style){
			var background=style.background;
			var color=style.color;
			if(background){
				elem.find(".page-curr").css("background",background);
				elem.find(".page-curr").css("border","1px solid "+background);
			}
			if(color){
				elem.find("a").css("color",color);
				elem.find("span").css("color",color);
				elem.find(".page-curr").css("color","#FFF");
				elem.find(".page_select").css("color",color);
				elem.find("em").css("color",color);				
				elem.find(".page_input").css("color",color);	
			}
		}
		
		//jump方法的参数
		var obj = {
			pageSize : elem.find(".page_select").val()
		};
		
		elem.find(".page-first").click(function(){
			obj["curr"] = 1;
			jump( obj );
		});
		
		elem.find(".page-prev").click(function(){
			obj["curr"] = pageIndex - 1;
			jump( obj );
		});

		//1、2、3、4、5...页
		elem.find(".page-i").click(function(){
			obj["curr"] = this.innerHTML;
			jump( obj );
		});
		//下一页
		elem.find(".page-next").click(function(){
			obj["curr"] = new Number(pageIndex) + 1;
			jump( obj );
		});
		
		//尾页
		elem.find("a[class=page-last]").click(function(){
			obj["curr"] = sumPage;
			jump( obj );
		});
			
		//input框跳转
		elem.find(".page_input").keydown(function(){
			if(window.event.keyCode==13 && this.value!="" ){
				obj["curr"] =  this.value > sumPage ? sumPage : this.value;
				jump( obj );
			}
		});
		
		//修改pageSize
		elem.find(".page_select").change(function(){
			obj["pageSize"] = this.value;
			obj["curr"] = 1;
			jump( obj );
		});
		elem.show();
	}
});