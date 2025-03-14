package io.github.wujun728.api.core.template.service;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.utils.SpringContextUtil;
import io.github.wujun728.api.core.template.service.impl.*;
import io.github.wujun728.api.core.template.service.impl.*;

public class ModelPageServiceFactory {


    public enum Page{
        add,
        edit,
        list,
        basejs,
        navjs
    }

    /**
     * 获取service
     * @param page
     * @return
     */
    public static ModelFileService getModelFileService(Page page){
        if(page == Page.add){
            return SpringContextUtil.getBean(ModelAddFileServiceImpl.class);
        }else if(page == Page.edit){
            return SpringContextUtil.getBean(ModelEditFileServiceImpl.class);
        }else if(page == Page.list){
            return SpringContextUtil.getBean(ModelListFileServiceImpl.class);
        }else if(page == Page.basejs){
            return SpringContextUtil.getBean(ModelBaseJSFileServiceImpl.class);
        }else if(page == Page.navjs){
            return SpringContextUtil.getBean(ModelNavJSFileServiceImpl.class);
        }
        throw new BusinessException(EnumCode.PARAMS_ERROR, "page 参数错误");
    }
}
