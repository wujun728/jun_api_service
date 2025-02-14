package com.ibeetl.olexec.service;

import com.ibeetl.olexec.compile.StringSourceCompiler;
import com.ibeetl.olexec.compile.StringSourceCompilerExtend;
import com.ibeetl.olexec.execute.JavaClassExecutor;
import com.ibeetl.olexec.util.HttpRequestLocal;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

@Service
public class ExecuteStringSourceService {
    /* 客户端发来的程序的运行时间限制 */
    private static final int RUN_TIME_LIMITED = 5;

    /* N_THREAD = N_CPU + 1，因为是 CPU 密集型的操作 */
    private static final int N_THREAD = 5;

    /* 负责执行客户端代码的线程池，根据《Java 开发手册》不可用 Executor 创建，有 OOM 的可能 */
    private static final ExecutorService pool = new ThreadPoolExecutor(N_THREAD, N_THREAD,
            0L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5*N_THREAD));

    private static final String WAIT_WARNING = "服务器忙，请稍后提交";
    private static final String NO_OUTPUT = "Nothing.";

    public String execute(String source, String systemIn) {
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>(); // 编译结果收集器

        // 编译源代码
        byte[] classBytes = StringSourceCompiler.compile(source, compileCollector);

        // 编译不通过，获取并返回编译错误信息
        if (classBytes == null) {
            // 获取编译错误信息
            List<Diagnostic<? extends JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorRes = new StringBuilder();
            for (Diagnostic diagnostic : compileError) {
                compileErrorRes.append("Compilation error at ");
                compileErrorRes.append(diagnostic.getLineNumber());
                compileErrorRes.append(".");
                compileErrorRes.append(System.lineSeparator());
            }
            return compileErrorRes.toString();
        }

        // 运行字节码的main方法
        Callable<String> runTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return JavaClassExecutor.execute(classBytes, systemIn);
            }
        };

        Future<String> res = null;
        try {
            res = pool.submit(runTask);
        } catch (RejectedExecutionException e) {
            return WAIT_WARNING;
        }

        // 获取运行结果，处理非客户端代码错误
        String runResult;
        try {
            runResult = res.get(RUN_TIME_LIMITED, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            runResult = "Program interrupted.";
        } catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = "Time Limit Exceeded.";
        } finally {
            res.cancel(true);
        }
        return runResult != null ? runResult : NO_OUTPUT;
    }

    public String execute2(String source, String systemIn) {
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>(); // 编译结果收集器

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
                compileErrorRes.append(diagnostic.getLineNumber()).append(diagnostic.getMessage(Locale.getDefault()));
                compileErrorRes.append(".");
                compileErrorRes.append(System.lineSeparator());
            }
            return compileErrorRes.toString();
        }

		HttpServletRequest  request = HttpRequestLocal.getRequest();
        // 运行字节码的main方法
        Callable<String> runTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
				HttpRequestLocal.setRequest(request);
                return JavaClassExecutor.execute(compilerExtend, systemIn);
            }
        };

        Future<String> res = null;
        try {
            res = pool.submit(runTask);
        } catch (RejectedExecutionException e) {
            return WAIT_WARNING;
        }

        // 获取运行结果，处理非客户端代码错误
        String runResult;
        try {
            runResult = res.get(RUN_TIME_LIMITED, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            runResult = "Program interrupted.";
        } catch (ExecutionException e) {
            runResult = e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = "Time Limit Exceeded.";
        } finally {
            res.cancel(true);
        }
        return runResult != null ? runResult : NO_OUTPUT;
    }
}
