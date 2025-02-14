import com.ibeetl.olexec.util.SessionSQLManager;

import lombok.Data;
import org.beetl.sql.annotation.entity.Auto;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.fetch.annotation.Fetch;
import org.beetl.sql.fetch.annotation.FetchMany;
import org.beetl.sql.fetch.annotation.FetchOne;

import java.util.List;

/**
 * Fetch 能自动抓去额外的的实体对象,在线体验设置了沙箱，有可能查询超时
 */
public class SqlTest {

	/**
	 * fetch注解默认的level为1，即自动抓取一层，更为复杂的例子可以参考S10FetchSample
	 */
	@Data
	@Table(name="sys_user")
	@Fetch
	public static class UserData {
		@Auto
		private Integer id;
		private String name;
		private Integer departmentId;
		@FetchOne("departmentId")
		private DepartmentData dept;
	}

	@Data
	@Table(name="department")
	@Fetch
	public static class DepartmentData {
		@Auto
		private Integer id;
		private String name;
		@FetchMany("departmentId")
		private List<UserData> users;
	}

	/**
	 * 在main方法中运行测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		fetchOne(sqlManager);
	}

	/**
	 *
	 */
	public static void fetchOne(SQLManager sqlManager){
		UserData user = sqlManager.unique(UserData.class,1);
		System.out.println(user.getDept());


	}

	public static void fetchMany(SQLManager sqlManager){
		DepartmentData dept = sqlManager.unique(DepartmentData.class,1);
		System.out.println(dept.getUsers());
	}

}
