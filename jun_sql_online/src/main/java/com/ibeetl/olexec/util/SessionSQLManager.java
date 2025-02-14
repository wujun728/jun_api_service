package com.ibeetl.olexec.util;

import com.ibeetl.olexec.ext.H2ExtStyle;
import com.ibeetl.olexec.ext.HackDebugInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.beetl.sql.clazz.kit.ClassLoaderKit;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.H2Style;
import org.beetl.sql.core.engine.template.BeetlTemplateEngine;
import org.beetl.sql.ext.DebugInterceptor;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 为每一个会话准备一个SQLManager,链接到一个内存数据库
 */
public class SessionSQLManager {

	static String INIT_DB = "/schema/h2.sql";

	public synchronized  static SQLManager getSQLManger(){
		HttpSession session = HttpRequestLocal.getRequest().getSession();
		SQLManager sqlManager = (SQLManager)session.getAttribute("sqlManager");
		if(sqlManager==null){
			sqlManager = createSQLManager();
			session.setAttribute("sqlManager",sqlManager);
		}
		return sqlManager;
	}

	public synchronized static SQLManager getSQLManger(Class c){
		SQLManager sqlManager = getSQLManger();
		//重要:使用在线代码的classloader
		BeetlTemplateEngine beetlTemplateEngine = (BeetlTemplateEngine)sqlManager.getSqlTemplateEngine();
		beetlTemplateEngine.getBeetl().getGroupTemplate().setClassLoader(c.getClassLoader());
		sqlManager.setClassLoaderKit(new ClassLoaderKit(c.getClassLoader()));
		return sqlManager;
	}

	protected  static SQLManager createSQLManager(){
		DataSource dataSource = createDataSource();
		ConnectionSource source = ConnectionSourceHelper.getSingle(dataSource);
		SQLManagerBuilder builder = new SQLManagerBuilder(source);
		builder.setNc(new UnderlinedNameConversion());
		builder.setInters(new Interceptor[]{new HackDebugInterceptor()});
		builder.setDbStyle(new H2ExtStyle());
		SQLManager sqlManager = builder.build();
		return sqlManager;
	}

	protected  static DataSource createDataSource(){
		HikariDataSource dataSourceConfig = new HikariDataSource();
		dataSourceConfig.setJdbcUrl("jdbc:h2:mem:dbtest;DB_CLOSE_ON_EXIT=FALSE");
		dataSourceConfig.setUsername("sa");
		dataSourceConfig.setPassword("");
		dataSourceConfig.setDriverClassName("org.h2.Driver");
		dataSourceConfig.setMaximumPoolSize(1);
		initData(dataSourceConfig,INIT_DB);
		return dataSourceConfig;
	}

	private static void initData(DataSource ds,String file)  {
		Connection conn = null;
		try{
			conn = ds.getConnection();
			String[] sqls = getSqlFromFile(file);
			for(String sql:sqls ){
				runSql(conn,sql);
			}

		}catch (SQLException sqlException){
			throw new RuntimeException(sqlException);
		}
		finally {
			try {
				if(conn!=null) {
					conn.close();
				}
			} catch (SQLException sqlException) {
				//ignore
			}
		}
	}

	private static String[] getSqlFromFile(String file){
		try{
			InputStream ins = SessionSQLManager.class.getResourceAsStream(file);
			if(ins==null){
				throw new IllegalArgumentException("无法加载文件 "+file);
			}
			int len = ins.available();
			byte[] bs = new byte[len];
			ins.read(bs);
			String str = new String(bs,"UTF-8");
			String[] sql = str.split(";");
			return sql;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}

	}

	private static void runSql(Connection conn,String sql) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
		ps.close();
	}
}
