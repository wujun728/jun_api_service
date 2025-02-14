import com.ibeetl.olexec.util.SessionSQLManager;
import lombok.Data;
import org.beetl.sql.annotation.builder.AttributeConvert;
import org.beetl.sql.annotation.builder.Builder;
import org.beetl.sql.annotation.entity.Auto;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.clazz.kit.BeanKit;
import org.beetl.sql.core.ExecuteContext;
import org.beetl.sql.core.SQLManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 自定义一个Base64注解，用来处理入库前，加载后的逻辑
 */
public class SqlTest {

	@Data
	@Table(name="sys_user")
	public static class UserData {
		@Auto
		private Integer id;
		@Base64
		private String name;
		private Integer departmentId;
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(value = {ElementType.METHOD, ElementType.FIELD})
	@Builder(Base64Convert.class)
	public static @interface Base64 {

	}


	/**
	 *
	 */
	public static class Base64Convert  implements AttributeConvert {
		Charset utf8  = Charset.forName("UTF-8");

		@Override
		public  Object toDb(ExecuteContext ctx, Class cls, String name, Object pojo) {
			String value= (String) BeanKit.getBeanProperty(pojo,name);
			byte[] bs = java.util.Base64.getEncoder().encode(value.getBytes(utf8));
			return new String(bs,utf8);

		}
		@Override
		public  Object toAttr(ExecuteContext ctx, Class cls, String name, ResultSet rs, int index) throws SQLException {
			String value  = rs.getString(index);
			return new String(java.util.Base64.getDecoder().decode(value),utf8);
		}
	}

	/**
	 * 在main方法中运行测试
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//调用SessionSQLManager获得当前会话的SQLManager
		SQLManager sqlManager = SessionSQLManager.getSQLManger(SqlTest.class);
		UserData userData = new UserData();
		userData.setName("base64_user");
		userData.setDepartmentId(1);
		sqlManager.insert(userData);
		//刷新表格数据，可以看到name是base64编码
		UserData dbUser = sqlManager.unique(UserData.class,userData.getId());
		System.out.println(dbUser);

	}

}
