import com.ibeetl.olexec.util.SessionSQLManager;

import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.LambdaQuery;
import org.beetl.sql.core.query.Query;
import org.beetl.sql.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * 演示Query操作,拼接SQL
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
	public interface UserMapper extends BaseMapper<UserEntity>{

	}

	/**
	 * 在main方法中运行测试
	 * @param args
	 */
	public static void main(String [] args){
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		query(sqlManager);
	}

	public static void query(SQLManager sqlManager) {
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		Query<UserEntity> query = userMapper.createQuery();
		UserEntity entity = query.andEq("name","lijz").single();
		System.out.println(entity);

		List<UserEntity> entities = query.andGreat("department_id",1).orderBy("name").select();
		System.out.println(entity);
	}




}

