package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.FileUtil;
import io.github.wujun728.api.common.utils.SpringContextUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelApiFileService;
import io.github.wujun728.api.core.template.service.ModelFileService;
import io.github.wujun728.api.core.template.service.ModelPageServiceFactory;
import io.github.wujun728.api.core.template.service.PageTemplateService;
import io.github.wujun728.api.core.template.vo.Project;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class PageTemplateServiceImpl implements PageTemplateService {

    @Resource
    private Project project;

    @Override
    public void createFiles(List<ApiInfo> apiInfos){
        if(ObjectUtil.isEmpty(apiInfos)){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }

        copyTemplates(project);

        for (ApiInfo apiInfo:apiInfos){
            ModelFileService modelFileService = null;
            if(SqlUtl.OP_TYPE_SELECT.equalsIgnoreCase(apiInfo.getOpType())){
                modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.list);
            }else if(SqlUtl.OP_TYPE_INSERT.equalsIgnoreCase(apiInfo.getOpType())){
                modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.add);
            }else if(SqlUtl.OP_TYPE_UPDATE.equalsIgnoreCase(apiInfo.getOpType())){
                modelFileService = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.edit);
            }else{
                continue;
            }
            modelFileService.writeFile(project,apiInfo);
        }

        ModelFileService basejs = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.basejs);
        basejs.writeFile(project,apiInfos.get(0));

        ModelApiFileService apiFileService = SpringContextUtil.getBean(ModelApiJSFileServiceImpl.class);
        apiFileService.writeFile(project,apiInfos);

        ModelFileService navjs = ModelPageServiceFactory.getModelFileService(ModelPageServiceFactory.Page.navjs);
        navjs.writeFile(project, apiInfos.get(0));
    }

    private void copyTemplates(Project project){
        String output = project.getOutputDir() + project.getOutputFolder();
        File out = new File(output);
        if(out.exists()){
            return;
        }
        //复制文件夹
        FileUtil.copyFile(project.getTemplateDir(), project.getOutputDir());
        //重命名
        File file = new File(project.getOutputDir());
        file.listFiles()[0].renameTo(out);

        //清除文件
        cn.hutool.core.io.FileUtil.del(output+File.separator+"page"+File.separator+"model");
        cn.hutool.core.io.FileUtil.del(output+File.separator+"style"+File.separator+"js"+File.separator+"base.js");
        cn.hutool.core.io.FileUtil.del(output+File.separator+"style"+File.separator+"js"+File.separator+"api.js");
    }
}
