import com.ibeetl.olexec.util.SessionSQLManager;


import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.LambdaQuery;
import org.beetl.sql.core.query.Query;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.Template;
import org.h2.engine.User;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 演示sql模板中 like or in
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


	/**
	 * mapper类
	 */
	public interface UserMapper extends BaseMapper<UserEntity> {
		@Template("select * from sys_user where name like #{name}")
		List<UserEntity> like01(String name);

		@Template("select * from sys_user where name like #{'%'+name+'%'}")
		List<UserEntity> like02(String name);


		@Template("select * from sys_user where department_id in ( #{join(ids)} )")
		List<UserEntity> selectByDeptIds(List<Integer> ids);
	}

	/**
	 * 在main方法中运行测试
	 * @param args
	 */
	public static void main(String [] args){
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		like(sqlManager);
		//in(sqlManager)
	}

	public static void like(SQLManager sqlManager) {
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		List<UserEntity> ret1 = userMapper.like01("%l%");
		//或者
		List<UserEntity> ret2 = userMapper.like02("l");
		System.out.println(ret1);
		System.out.println(ret2);


	}

	public static void in(SQLManager sqlManager) {
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		List<UserEntity> ret = userMapper.selectByDeptIds(Arrays.asList(1,2));
		System.out.println(ret);
	}

}
