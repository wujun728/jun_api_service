import com.ibeetl.olexec.util.SessionSQLManager;
import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.DefaultPageResult;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.h2.engine.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQLManager是底层类，为Mapper或者Query等类使用，SQLManager也可以直接被调用
 */
public class SqlTest {
	/**
	 * 任意一个类
	 */
	@Table(name = "sys_user")
	@Data
	public static class UserEntity {
		@AutoID
		private Integer id;
		private String name;
		private Integer departmentId;
		private Date createTime;
	}


	/**
	 * 在main方法中运行测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		simple(sqlManager);
	}

	/**
	 * 内置API
	 * @param sqlManager
	 */
	public static void simple(SQLManager sqlManager) {

		UserEntity userEntity = sqlManager.unique(UserEntity.class,1);
		userEntity.setName("lijz+++");
		sqlManager.updateById(userEntity);
		List<UserEntity> all =  sqlManager.all(UserEntity.class);
		System.out.println(all);
	}


	/**
	 * 直接执行SQL
	 */
	public static void jdbc(SQLManager sqlManager) {
		String sql = "select * from sys_user where id=? and name=?";
		SQLReady sqlReady = new SQLReady(sql,2,"lucy");
		List<UserEntity> list = sqlManager.execute(sqlReady,UserEntity.class);
		System.out.println(list);
	}

	/**
	 * 直接执行模板sql
	 */
	public static void template(SQLManager sqlManager) {

		String sqlTemplate = "update sys_user set name=#{name} where id=#{id}";
		Map<String,Object> paras = new HashMap<>();
		paras.put("id","1");
		paras.put("name","lijz++");
		int ret = sqlManager.executeUpdate(sqlTemplate,paras);
	}


	public static void page(SQLManager sqlManager){
		/**
		 * sql模板语句的page函数能自动把sql模板语句转为为求总数语句
		 */
		String sql = "select #{page('*')} from sys_user where department_id=#{id}";
		PageRequest request = DefaultPageRequest.of(1,10);
		Map map = new HashMap<>();
		map.put("id",1);
		PageResult pr = sqlManager.executePageQuery(sql,UserEntity.class,map,request);
		//强制转化为DefaultPageResult,
		DefaultPageResult pageResult = (DefaultPageResult)pr;
		printPageResult(pageResult);

	}

	private static void printPageResult(DefaultPageResult pageResult) {
		System.out.println(pageResult.getPage());
		System.out.println(pageResult.getPageSize());
		System.out.println(pageResult.getTotalRow());
		System.out.println(pageResult.getTotalPage());
		System.out.println(pageResult.getList());
	}

}