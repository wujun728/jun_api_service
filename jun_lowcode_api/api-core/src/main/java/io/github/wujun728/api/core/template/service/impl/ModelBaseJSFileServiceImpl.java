package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.FileUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelFileService;
import io.github.wujun728.api.core.template.vo.Project;
import io.github.wujun728.api.core.vo.SQLParamVo;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ModelBaseJSFileServiceImpl implements ModelFileService {

    @Override
    public void writeFile(Project project, ApiInfo apiInfo) {

        List<SQLParamVo> sqlParamVos = apiInfo.getParamVos().stream().filter(e -> e.getMaster() != null && e.getMaster() == true).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(sqlParamVos)){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }
        SQLParamVo sqlParamVo = sqlParamVos.get(0);
        String modelName = StringUtil.toLowerCaseFirstOne(StringUtil.toUnderscoreToCamelCase(sqlParamVo.getTableName()));

        Map<String,String> map = new HashMap<String,String>(){
            {
                put("modelComment", sqlParamVo.getTableComment());
                put("modelName", modelName);
                put("projectName", project.getProjectName());
                put("serverUrl", project.getServerUrl());
            }
        };
        String srcFilePath = project.getTemplateDir() + "style" +File.separator+"js" + File.separator + "base.js";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+File.separator +"style" +File.separator+"js" + File.separator + "base.js";
        FileUtil.copyFile(srcFilePath,destFilePath, map,false);
    }
}
