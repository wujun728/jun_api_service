package io.github.wujun728.api.common.jdbc.util;

public class SqlUtl {

    public static final String OP_TYPE_INSERT = "INSERT";
    public static final String OP_TYPE_UPDATE = "UPDATE";
    public static final String OP_TYPE_SELECT = "SELECT";

    public static final String OP_TYPE_SELECT_ONE = "SELECT_ONE";

    public static final String OP_TYPE_DELETE = "DELETE";

    public static final String OP_TYPE_COUNT = "COUNT";

    public static final String [] OP_TYPES = new String[]{
            OP_TYPE_SELECT,
            OP_TYPE_SELECT_ONE,
            OP_TYPE_UPDATE,
            OP_TYPE_DELETE,
            OP_TYPE_COUNT,
            OP_TYPE_INSERT
    };

    public static final String REGEX_KUOHAO = "(?<=\\{)(.+?)(?=\\})";

    public static final String SQL_KEY_LIMIT = "limit";

    public static final String SQL_KEY_ORDER = "order by";

    /**
     * 条件like数组
     */
    public static final String [] SQL_LIKE_NAME_ARRAY = new String[]{"name","title","type","phone","identityCard","identityId"};

    public static final String [] SQL_KEYS={"rank","key","before",
            "after","column","from","to","end","start","global","like","min","max","point","time","date",
            "year","month","day","event","do","comment","commit","user","value","name","type","code","range","drop",
            "delete","length","exit","required","select","update","delete","insert","sql","database","driver","host",
            "password","url","port","show","schema","where","in","group","join","left","right","index","primary","having",
            "and","or","not","commit","grant","rollback","revoke","rename","drop","alter","create"
    };

    public static boolean isSQLKey(String columnName){
        boolean flag = false;
        for (String keyName:SQL_KEYS){
            if(keyName.equals(columnName.toLowerCase())){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static String getSQLColumn(String columnName){
        if(columnName.indexOf("`")!=-1){
            return columnName;
        }
        return isSQLKey(columnName)?"`"+columnName+"`":columnName;
    }

}
