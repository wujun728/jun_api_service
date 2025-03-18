package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.SpringContextUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelApiFileService;
import io.github.wujun728.api.core.template.service.ModelFileService;
import io.github.wujun728.api.core.template.service.ModelPageServiceFactory;
import io.github.wujun728.api.core.template.service.PageCreateService;
import io.github.wujun728.api.core.template.vo.Project;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PageCreateServiceImpl implements PageCreateService {

    @Resource
    private Project project;

    @Override
    public void createFile(ApiInfo apiInfo){
        if(apiInfo==null || ObjectUtil.isEmpty(apiInfo.getParamVos())){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }
        ModelFileService modelFileService = null;
        if(SqlUtl.OP_TYPE_SELECT.equalsIgnoreCase(apiInfo.getOpType())){
            modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.list);
        }else if(SqlUtl.OP_TYPE_INSERT.equalsIgnoreCase(apiInfo.getOpType())){
            modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.add);
        }else if(SqlUtl.OP_TYPE_UPDATE.equalsIgnoreCase(apiInfo.getOpType())){
            modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.edit);
        }
        if(modelFileService!=null){
            modelFileService.writeFile(project, apiInfo);

            //apijs
            ModelApiFileService apiFileService = SpringContextUtil.getBean(ModelApiJSFileServiceImpl.class);
            apiFileService.writeFile(project, apiInfo);

            if(SqlUtl.OP_TYPE_SELECT.equalsIgnoreCase(apiInfo.getOpType())){
                //nav.js
                ModelFileService  navFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.navjs);
                navFileService.writeFile(project, apiInfo);
            }
        }
    }
}
