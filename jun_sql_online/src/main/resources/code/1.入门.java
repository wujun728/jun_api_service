import com.ibeetl.olexec.util.SessionSQLManager;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.mapper.BaseMapper;
import lombok.Data;
import java.util.List;
import java.util.Date;

/**
 * 入门例子,使用BaseMapper的内置CRUD方法
 * 通过ctrl+点击，可以访问BeetlSQL类的说明，例如ctrl+点击 BaseMapper
 *
 */
public class SqlTest {
	/**
	 * 任意一个类
	 */
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
	 * mapper类,BaseMapper已经包含了若干CRUD方法
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
		queryOne(sqlManager);
		//尝试如下方法
		/*
		updateById(sqlManager);
		template(sqlManager);
		add(sqlManager);
		showAll(sqlManager);
		 */
    }

	/**
	 * 查询用户
	 * @param sqlManager
	 */
	public static void queryOne(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		UserEntity userEntity = userMapper.unique(1);
		System.out.println(userEntity);
	}

	/**
	 * 更新用户
	 * @param sqlManager
	 */
	public static void updateById(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		UserEntity userEntity = userMapper.unique(1);
		userEntity.setName("lijz++");
		userEntity.setCreateTime(new Date());
		userMapper.updateById(userEntity);
	}

	/**
	 * 按照模板查询
	 * @param sqlManager
	 */
	public static void template(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		UserEntity template = new UserEntity();
		template.setName("lucy");
		template.setDepartmentId(1);
		UserEntity user = userMapper.templateOne(template);
		System.out.println(user);
	}

	/**
	 * 新增一个用户
	 * @param sqlManager
	 */
	public static void add(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		UserEntity userEntity = new UserEntity();
		userEntity.setName("newName");
		userEntity.setDepartmentId(1);
		userEntity.setCreateTime(new Date());
		userMapper.insert(userEntity);
	}

	/**
	 * 输出所有用户
	 * @param sqlManager
	 */
    public static void showAll(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(UserMapper.class);
		List<UserEntity> users = userMapper.all();
		for(UserEntity entity:users){
			System.out.println(entity.getName());
		}
	}


}
