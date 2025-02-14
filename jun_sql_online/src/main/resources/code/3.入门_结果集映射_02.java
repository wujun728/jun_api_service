import com.ibeetl.olexec.util.SessionSQLManager;
import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.JsonMapper;
import org.beetl.sql.annotation.entity.ResultProvider;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.mapping.join.JsonConfigMapper;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.Sql;

import java.util.Date;
import java.util.List;

/**
 * 演示复杂映射
 *
 */
public class SqlTest {

	@Table(name="sys_user")
	@Data
	public static class UserEntity {
		@AutoID
		private Integer id;
		private String name;
		private Integer departmentId;
		private Date createTime;
	}

	@Data
	@ResultProvider(JsonConfigMapper.class)
	/*也可以引用配置文件而不是在代码里配置*/
	@JsonMapper(
			"{'id':'id','name':'name','users':{'id':'u_id','name':'u_name'}}")
	public static class DepartmentInfo {
		Integer id;
		String name;
		List<UserInfo> users;
	}

	@Data
	public static class UserInfo {
		Integer id;
		String name;
	}

	/**
	 * mapper类
	 */
	public interface UserMapper extends BaseMapper<UserEntity>{
		@Sql("select d.id id,d.name name ,u.id u_id,u.name u_name from department d join sys_user u on d.id=u.department_id  where d.id=?")
		List<DepartmentInfo> allUserView(Integer deptId);

	}

	/**
	 * 在main方法中运行测试
	 * @param args
	 */
	public static void main(String [] args){
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		jsonConfig(sqlManager);
	}

	public static void jsonConfig(SQLManager sqlManager) {
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);

		List<DepartmentInfo> list = userMapper.allUserView(1);
		System.out.println(list.toString());

	}

}