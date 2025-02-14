import com.ibeetl.olexec.util.SessionSQLManager;
import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Column;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.Sql;

import java.util.Date;
import java.util.List;

/**
 * 演示常用Mapper，mapper是一种封装了SQLManager的Dao
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

	/*用任意对象来接JDBC结果集，取交集,可以用@Column做特别说明*/
	@Data
	public static class UserVo {
		private Integer id;
		private String name;
		private Integer departmentId;
		private Date createTime;
		@Column("deptartment_name")
		private String deptName;
	}

	/**
	 * mapper类
	 */
	public interface UserMapper extends BaseMapper<UserEntity>{
		@Sql("select u.* ,d.name `deptartment_name` " +
				" from sys_user u  left join department d on d.id=u.department_id")
		List<UserVo> allUserView();

	}

	/**
	 * 在main方法中运行测试
	 * @param args
	 */
	public static void main(String [] args){
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		simple(sqlManager);
	}

	/**
	 * 演示mapper调用
	 * @param sqlManager
	 */
	public static void simple(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(
				UserMapper.class);

		List<UserVo> list = userMapper.allUserView();
		System.out.println(list);
	}

}
