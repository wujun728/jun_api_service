package org.olexec.service;

import com.ibeetl.olexec.compile.StringSourceCompiler;
import com.ibeetl.olexec.compile.StringSourceCompilerExtend;
import org.junit.Assert;
import org.junit.Test;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Locale;

public class CompileTest {
    @Test
    public void testLib(){


       String str =  System.getProperty("java.home");
       System.out.println(str);


        System.out.println("===========os.name:"+System.getProperties().getProperty("os.name"));

        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>(); // 编译结果收集器
        String source = "import lombok.Data;\n" + "import org.beetl.sql.annotation.entity.AutoID;\n"
                + "import org.beetl.sql.annotation.entity.Table;\n" + "\n" + "/**\n" + " * 角色\n" + " */\n"
                + "@Table(name = \"role\")\n" + "@Data\n" + "public class CoreRole {\n" + "\n" + "\t@AutoID\n"
                + "\tprotected Long id;\n" + "\n" + "\tprivate String name;\n" + "\n" + "\tprivate String type;\n"
                + "\n" + "\tprivate String createDate;\n" + "\n" + "\tpublic static  void main(String[] args){\n"
                + "\t\tSystem.out.println(new CoreRole().getId());\n" + "\t}\n" + "\n" + "}";


        StringSourceCompilerExtend compilerExtend = new StringSourceCompilerExtend();
        // 编译源代码
        boolean result = compilerExtend.compile(source, compileCollector);



        // 编译不通过，获取并返回编译错误信息
        if (!result) {
            // 获取编译错误信息
            List<Diagnostic<? extends JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorRes = new StringBuilder();
            for (Diagnostic diagnostic : compileError) {
                compileErrorRes.append("Compilation error at ");
                compileErrorRes.append(diagnostic.getLineNumber());
                compileErrorRes.append(".");
                compileErrorRes.append(diagnostic.getMessage(Locale.US));
                compileErrorRes.append(System.lineSeparator());
            }
            System.out.println(compileErrorRes.toString());
            Assert.fail();
        }
    }
}
