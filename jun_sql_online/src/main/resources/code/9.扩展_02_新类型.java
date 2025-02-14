import com.ibeetl.olexec.util.SessionSQLManager;

import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.mapping.type.JavaSqlTypeHandler;
import org.beetl.sql.core.mapping.type.ReadTypeParameter;
import org.beetl.sql.core.mapping.type.WriteTypeParameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Date;


/**
 * 如果处理jdbc不支持的类型，如BigInteger，JsonNode等
 */
public class SqlTest {

	@Table(name="sys_user")
	@Data
	public static class UserEntity {
		@AutoID
		private Integer id;
		private String name;
		//一个新的类型
		BigInteger departmentId;
		private Date createTime;
	}

	/**
	 * Beetlsql 并没有内置对BigInteger支持，这里可以扩展
	 */
	public static class BigIntTypeHandler extends JavaSqlTypeHandler {

		@Override
		public Object getValue(ReadTypeParameter typePara) throws SQLException {
			BigDecimal decimal = typePara.getRs().getBigDecimal(typePara.getIndex());
			return decimal.toBigInteger();
		}
		@Override
		public void setParameter(WriteTypeParameter writeTypeParameter, Object obj)throws SQLException {
			BigInteger bigInteger = (BigInteger)obj;
			BigDecimal bigDecimal = new BigDecimal(bigInteger);
			writeTypeParameter.getPs().setBigDecimal(writeTypeParameter.getIndex(),bigDecimal);
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

		BigIntTypeHandler bigIntTypeHandler = new BigIntTypeHandler();
		sqlManager.getDefaultBeanProcessors().addHandler(BigInteger.class,bigIntTypeHandler);

		UserEntity userEntity = new UserEntity();
		userEntity.setName("big int");
		userEntity.setDepartmentId(new BigInteger("12"));
		sqlManager.insert(userEntity);

		UserEntity dbUserEntity = sqlManager.unique(UserEntity.class,1);
		System.out.println(dbUserEntity);

	}

}
