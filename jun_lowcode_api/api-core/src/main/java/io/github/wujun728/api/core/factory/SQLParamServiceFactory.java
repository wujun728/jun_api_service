package io.github.wujun728.api.core.factory;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.SpringContextUtil;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.service.impl.*;
import io.github.wujun728.api.core.service.impl.*;

public class SQLParamServiceFactory {

    /**
     * 获取service
     * @param opType
     * @return
     */
    public static ApiParamService getApiParamService(String opType){
        if(SqlUtl.OP_TYPE_SELECT.equalsIgnoreCase(opType) || SqlUtl.OP_TYPE_SELECT_ONE.equalsIgnoreCase(opType)){
            return SpringContextUtil.getBean(SQLQueryParamServiceImpl.class);
        }else if(SqlUtl.OP_TYPE_COUNT.equalsIgnoreCase(opType)){
            return SpringContextUtil.getBean(SQLCountQueryParamServiceImpl.class);
        }else if(SqlUtl.OP_TYPE_INSERT.equalsIgnoreCase(opType)){
            return SpringContextUtil.getBean(SQLInsertParamServiceImpl.class);
        }else if(SqlUtl.OP_TYPE_UPDATE.equalsIgnoreCase(opType)){
            return SpringContextUtil.getBean(SQLUpdateParamServiceImpl.class);
        }else if(SqlUtl.OP_TYPE_DELETE.equalsIgnoreCase(opType)){
            return SpringContextUtil.getBean(SQLDeleteParamServiceImpl.class);
        }
        throw new BusinessException(EnumCode.PARAMS_ERROR, "opType 参数错误");
    }
}
