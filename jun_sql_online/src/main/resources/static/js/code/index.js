layui.define(['form', 'laydate', 'table', 'element'], function (exports) {
    var form = layui.form;
    var laydate = layui.laydate;
    var element = layui.element;
    var table = layui.table;
    var userTable = null;
    var departmentTable = null;
    var myCodeMirror = null;
    var toolbar = null;
    var platform = getPlatform();

    var docUrl = "https://gitee.com/xiandafu/beetlsql/blob/master/";
    var dict =  {};

    dict["BaseMapper"]=docUrl+"/sql-mapper/src/main/java/org/beetl/sql/mapper/BaseMapper.java";
    dict["SQLManager"] =docUrl+"/sql-core/src/main/java/org/beetl/sql/core/SQLManager.java";
    dict["@Table"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/annotation/entity/Table.java"
    dict["@AutoID"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/annotation/entity/AutoID.java"
    dict["@AssignID"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/annotation/entity/AssignID.java"
    dict["@Column"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/annotation/entity/Column.java"
    dict["PageResult"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/core/page"
    dict["Query"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/core/query/Query.java"
    dict["SQLReady"]=docUrl+"/sql-core/src/main/java/org/beetl/sql/core/SQLReady.java"

    var view = {

        init: function () {
            this.initCodeMirror();
            this.initTable();
            this.initToolBar();
            this.initForm();
        },
        initForm:function(){
            $("#verify-code-image").click(function () {
                var timestamp=new Date().getTime();
                var path = Common.ctxPath + "/verifyCode?timestamp="+timestamp;
                $("#verify-code-image").attr("src", path);
            });
            form.on('select', function(data) {
                var fileName = data.value;
                Common.post("/code", {fileName: fileName}, function (data) {
                    myCodeMirror.setValue(data);
                    myCodeMirror.setCursor(myCodeMirror.lineCount(),0);
                })
            });
        },
        initCodeMirror: function () {
            var source = localStorage.getItem("source");
            if (source == null || source == undefined) {
                source = 'public class Run {\n' +
                    '    public static void main(String[] args) {\n' +
                    '    \n' +
                    '    }\n' +
                    '}';
            }

            var WORD = /[\w$]+/g, RANGE = 500;

              CodeMirror.registerHelper("hint", "anyword", function(editor, options) {
                var word = options && options.word || WORD;
                var range = options && options.range || RANGE;
                var cur = editor.getCursor(), curLine = editor.getLine(cur.line);
                var start = cur.ch, end = start;
                while (end < curLine.length && word.test(curLine.charAt(end))) ++end;
                while (start && word.test(curLine.charAt(start - 1))) --start;
                var curWord = start != end && curLine.slice(start, end);

                var list = [], seen = {};
                function scan(dir) {
                  var line = cur.line, end = Math.min(Math.max(line + dir * range, editor.firstLine()), editor.lastLine()) + dir;
                  for (; line != end; line += dir) {
                    var text = editor.getLine(line), m;
                    word.lastIndex = 0;
                    while (m = word.exec(text)) {
                      if ((!curWord || m[0].indexOf(curWord) == 0) && !seen.hasOwnProperty(m[0])) {
                        seen[m[0]] = true;
                        list.push(m[0]);
                      }
                    }
                  }
                }
                scan(-1);
                scan(1);
                return {list: list, from: CodeMirror.Pos(cur.line, start), to: CodeMirror.Pos(cur.line, end)};
              });


            var el = document.getElementById("codeTextArea");
            myCodeMirror = CodeMirror.fromTextArea(el, {
                mode: "text/x-java",// 语言模式
                theme: "idea",// 主题
                keyMap: "sublime",// 快键键风格
                lineNumbers: true,// 显示行号
                smartIndent: true, //智能缩进
                indentUnit: 4, // 智能缩进单位为4个空格长度
                indentWithTabs: true,  // 使用制表符进行智能缩进
                lineWrapping: true,//
                // 在行槽中添加行号显示器、折叠器、语法检测器`
                gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter", "CodeMirror-lint-markers"],
                foldGutter: true, // 启用行槽中的代码折叠
                autofocus: true,  //自动聚焦
                matchBrackets: true,// 匹配结束符号，比如"]、}"
                autoCloseBrackets: true, // 自动闭合符号
                hintOptions: {
                   completeSingle: false
                },

                styleActiveLine: true // 显示选中行的样式
            });
            // 设置初始文本，这个选项也可以在fromTextArea中配置`
            myCodeMirror.setOption("value", source);
            // 编辑器按键监听
            myCodeMirror.on("keypress", function () {
                //显示智能提示
                myCodeMirror.showHint(); // 注意，注释了CodeMirror库中show-hint.js第131行的代码（阻止了代码补全，同时提供智能提示）

            });
            myCodeMirror.setSize('auto', '550px');
            //不能正常工作
            myCodeMirror.on("mousedown", function(cm, e) {
                var pos = myCodeMirror.coordsChar({left: e.clientX, top: e.clientY});
                // var pos = myCodeMirror.getCursor() // or {line , ch };
                var tok = myCodeMirror.getTokenAt(pos);
                console.log("ctrl-click at", tok.string);
                var key = tok.string;
                if((platform === 'Win' && e.ctrlKey) || (platform === 'Mac' && e.metaKey)){
                     var url = dict[key];
                     if(url!=null){
                         window.open(url, '_blank');
                     }
                }

            });


        },
        initTable: function () {
            userTable = table.render({
                elem: '#userTable',
                height: 500,
                method: 'post',
                url: Common.ctxPath + '/user/list' //数据接口
                , page: Lib.tablePage //开启分页
                , limit: 10,
                cols: [[
                    {
                        field: 'id',
                        title: 'id',
                        fixed: 'left'
                    },
                    {
                        field: 'name',
                        title: '用户名'
                    },
					{
						field: 'departmentId',
						title: '部门ID'
					},
                    {
                        field: 'createTime',
                        title: '创建时间',
                        width: 120
                    }
                ]]
            });

            departmentTable = table.render({
                elem: '#departmentTable',
                height: 500,
                method: 'post',
                url: Common.ctxPath + '/department/list' //数据接口
                , page: Lib.tablePage //开启分页
                , limit: 10,
                cols: [[{
                    field: 'id',
                    title: 'id',
                    fixed: 'left'
                }, {
                    field: 'name',
                    title: '部门名称'
                }]]
            });
        },

        initToolBar: function () {
            toolbar = {
                run: function () { //获取选中数据
                    var thiz = this;
                    var source = myCodeMirror.getValue();
                    var verifyCode = $("#verify-code").val();
                    $(thiz).attr("disabled", true);
                    Common.post("/run", {source: source,verifyCode:verifyCode}, function (data) {
                        if(data.status == 0){
                            $("#runResult").html(data.runResult);
                            $("#verify-div").hide();
                        }
                        else if(data.status == 1){
                            var timestamp=new Date().getTime();
                            var path = Common.ctxPath + "/verifyCode?timestamp="+timestamp;
                            $("#verify-code-image").attr("src", path);
                            $("#verify-code").val("");
                            $("#verify-div").show();
                            Common.error(data.statusText);
                        }
                        $(thiz).removeAttr("disabled");
                    },function () {
                        $(thiz).removeAttr("disabled");
                    })
                    localStorage.setItem("source", source);
                }
            };
            $('.ext-toolbar').on('click', function () {
                var type = $(this).data('type');
                toolbar[type] ? toolbar[type].call(this) : '';
            });
        }
    }

    exports('index', view);

    function getPlatform() {
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        if (isWin) return "Win";
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        if (isMac) return "Mac";
    }



});
