import com.ibeetl.olexec.util.SessionSQLManager;

import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.*;
import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.DefaultPageResult;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.*;

import java.util.Date;
import java.util.List;

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
	 * mapper类
	 */
	public interface UserMapper extends BaseMapper<UserEntity> {
		@Sql("select * from sys_user where id=?")
		public UserEntity selectById(Integer id);

		@Template("select * from sys_user where department_id=#{deptId}")
		public List<UserEntity> selectByDeptId(Integer deptId);

		@Template("update sys_user set name=#{name} where id=#{id}")
		@Update /*表示update操作*/
		public int updateNameById(Integer id,String name);

		/** 类似spring data那样通过方法名转化成SQL*/
		@SpringData
		List<UserEntity> queryByNameOrderById(String name);

		@Sql("select * from sys_user where department_id = ?")
		PageResult<UserEntity> queryDeptById(Integer id, PageRequest pageRequest);


		@Template("select #{page()} from sys_user where department_id = #{id}")
		PageResult<UserEntity> queryTemplateDeptById(Integer id,PageRequest pageRequest);

		@SqlProvider(provider = UserQueryProvider.class)
		UserEntity queryId(Integer id);

	}

	/*Mapper需要的sql和参数外部提供*/
	public static class UserQueryProvider {
		public SQLReady queryId(Integer id) {
			if (id < 9999) {
				return new SQLReady("select * from sys_user where id=?", 9999);
			} else {
				return new SQLReady("select * from sys_user where id=?", id);

			}
		}
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

		UserEntity userEntity = userMapper.selectById(1);
		System.out.println(userEntity);

		List<UserEntity> userEntityList = userMapper.selectByDeptId(1);
		System.out.println(userEntityList);

		userMapper.updateNameById(1,"lijz+++");

		userEntityList = userMapper.queryByNameOrderById("lijz++");
		System.out.println(userEntityList);

	}


	/**
	 * 演示JDBC SQL翻页
	 */
	public static void jdbcMapperPage(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(
				UserMapper.class);

		PageRequest request = DefaultPageRequest.of(1,10);
		PageResult pr = userMapper.queryDeptById(1,request);
		DefaultPageResult pageResult = (DefaultPageResult)pr;
		printPageResult(pageResult);
	}

	/**
	 * 演示模板SQL 翻页
	 */
	public static void templateMapperPage(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(
				UserMapper.class);

		PageRequest request = DefaultPageRequest.of(1,10);
		PageResult pr = userMapper.queryTemplateDeptById(1,request);
		DefaultPageResult pageResult = (DefaultPageResult)pr;
		printPageResult(pageResult);
	}

	/**
	 * 演示通过外部类指定SQL
	 */
	public static void provider(SQLManager sqlManager){
		UserMapper userMapper = sqlManager.getMapper(
				UserMapper.class);
		UserEntity user = userMapper.queryId(1);
		System.out.println(user);
	}


	private static void printPageResult(DefaultPageResult pageResult){
		System.out.println(pageResult.getPage());
		System.out.println(pageResult.getPageSize());
		System.out.println(pageResult.getTotalRow());
		System.out.println(pageResult.getTotalPage());
		System.out.println(pageResult.getList());
	}

}