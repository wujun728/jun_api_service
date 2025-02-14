import com.ibeetl.olexec.util.SessionSQLManager;

import org.beetl.core.Configuration;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.gen.SourceBuilder;
import org.beetl.sql.gen.SourceConfig;
import org.beetl.sql.gen.simple.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示sql模板中 like or in
 */
public class SqlTest {


	/**
	 * 在main方法中运行测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		like(sqlManager);
	}

	public static void like(SQLManager sqlManager) {

		/*与本网站使用占位符冲突，所以代码生成需要设置一下，实际系统不需设置*/
		Configuration configuration = BaseTemplateSourceBuilder.getGroupTemplate().getConf();
		configuration.setStatementStart("<%");
		configuration.setStatementEnd("%>");
		configuration.build();

		List<SourceBuilder> sourceBuilder = new ArrayList<>();
		SourceBuilder entityBuilder = new EntitySourceBuilder();
		SourceBuilder mapperBuilder = new MapperSourceBuilder();
		SourceBuilder mdBuilder = new MDSourceBuilder();

		sourceBuilder.add(entityBuilder);
		sourceBuilder.add(mapperBuilder);
		sourceBuilder.add(mdBuilder);

		SourceConfig config = new SourceConfig(sqlManager, sourceBuilder);

		StringOnlyProject project = new StringOnlyProject();
		String tableName = "sys_user";
		config.gen(tableName, project);
		System.out.println(project.getContent());

	}

}
