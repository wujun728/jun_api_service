import com.ibeetl.olexec.util.SessionSQLManager;
import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.ResultProvider;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.mapping.join.AutoJsonMapper;
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
	/* 自动映射，前提是结果集必须符合属性名字*/
	@ResultProvider(AutoJsonMapper.class)
	public static class MyUserView {
		Integer id;
		String name;
		DepartmentEntity dept;
	}

	@Table(name="department")
	@Data
	public static class DepartmentEntity {
		Integer id;
		String name;
	}


	/**
	 * mapper类
	 */
	public interface UserMapper extends BaseMapper<UserEntity>{
		@Sql("select u.id ,u.name  ,d.id `dept.id`,d.name `dept.name` from sys_user u  left join department d on d.id=u.department_id")
		List<MyUserView> allUserView();

	}

	/**
	 * 在main方法中运行测试
	 * @param args
	 */
	public static void main(String [] args){
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		jsonAutoConfig(sqlManager);
	}

	public static void jsonAutoConfig(SQLManager sqlManager) {
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);

		List<MyUserView> list = userMapper.allUserView();
		System.out.println(list.toString());

	}

}