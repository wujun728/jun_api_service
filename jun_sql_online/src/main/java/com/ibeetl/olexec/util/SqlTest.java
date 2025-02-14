package com.ibeetl.olexec.util;

import lombok.Data;
import org.beetl.sql.annotation.entity.ResultProvider;
import org.beetl.sql.core.ExecuteContext;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.beetl.sql.core.mapping.ResultSetMapper;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *  自定义一个结果集映射
 */
public class SqlTest {

    @Data
    @ResultProvider(MyResultSetMapper.class)
    public static class ResultSetObject{
        private Integer myId;
        private String myName;

    }

    public static class MyResultSetMapper  implements ResultSetMapper<ResultSetObject> {
        @Override
        public List<ResultSetObject> mapping(ExecuteContext ctx, Class target, ResultSet resultSet, Annotation config) throws SQLException {

            List<ResultSetObject> list = new ArrayList<>();
            while(resultSet.next()){
                ResultSetObject obj = new ResultSetObject();
                obj.setMyId(resultSet.getInt("id"));
                obj.setMyName(resultSet.getString("name"));
                list.add(obj);
            }

            return list;
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
        ResultSetObject resultSetObject = sqlManager.execute(new SQLReady("select * from sys_user where id=?",1),ResultSetObject.class).get(0);
        System.out.println(resultSetObject);

    }

}
