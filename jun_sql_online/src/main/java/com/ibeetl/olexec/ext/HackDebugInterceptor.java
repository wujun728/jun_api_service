package com.ibeetl.olexec.ext;

import com.ibeetl.olexec.execute.HackSystem;
import org.beetl.sql.ext.DebugInterceptor;

public class HackDebugInterceptor  extends DebugInterceptor {

	@Override
	protected void println(String str) {
		HackSystem.out.println(str);
	}

	protected void error(String str) {
		HackSystem.out.println(str);
	}
}
