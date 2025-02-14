package com.ibeetl.olexec.ext;

import com.ibeetl.olexec.util.HttpRequestLocal;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.H2Style;
import org.beetl.sql.core.db.KeyHolder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对数据库使用做限制
 */
public class H2ExtStyle extends H2Style {

	static int MAX_COUNT = 3;
	@Override
	public SQLExecutor buildExecutor(ExecuteContext executeContext) {
		MyBaseSQLExecutor sqlExecutor = new MyBaseSQLExecutor(executeContext);
		return sqlExecutor;
	}

	public static class MyBaseSQLExecutor  extends BaseSQLExecutor{

		public MyBaseSQLExecutor(ExecuteContext executeContext) {
			super(executeContext);
		}

		@Override
		public int[] insertBatch(Class<?> target, List<?> list){
			if(list.size()>MAX_COUNT){
				throw new UnsupportedOperationException("在线体验批量插入超过 "+MAX_COUNT);
			}
			checkUpdate();
			return super.insertBatch(target,list);
		}
		@Override
		protected int insert(Object paras, KeyHolder holder) {
			checkUpdate();
			return super.insert(paras,holder);
		}

		@Override
		public int update(Class target, Object object) {
			checkUpdate();
			return super.update(target,object);
		}
		@Override
		public int sqlReadyExecuteUpdate(SQLReady p) {
			checkUpdate();
			return super.sqlReadyExecuteUpdate(p);

		}


		@Override
		public int[] sqlReadyBatchExecuteUpdate(SQLBatchReady batch) {
			checkUpdate();
			return super.sqlReadyBatchExecuteUpdate(batch);
		}

		@Override
		public int[] updateBatch(Class<?> target, List<?> list) {
			if(list.size()>MAX_COUNT){
				throw new UnsupportedOperationException("在线体验批量更新超过 "+MAX_COUNT);
			}
			checkUpdate();
			return super.updateBatch(target,list);
		}


		protected void checkUpdate(){
			AtomicInteger count = (AtomicInteger)HttpRequestLocal.getRequest().getAttribute("count");
			if(count.addAndGet(1)>MAX_COUNT){
				throw new UnsupportedOperationException("在线体验一次请求insert，不能超过 "+MAX_COUNT);
			}
		}

	}


}
