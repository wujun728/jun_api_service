package com.ibeetl.olexec.util;

public class ClassLoaderUtil {
	public static ClassLoader get(){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if(loader!=null){
			return loader;
		}else{
			return ClassLoaderUtil.class.getClassLoader();
		}
	}
}
