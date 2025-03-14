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
public class ModelNavJSFileServiceImpl implements ModelFileService {

    @Override
    public void writeFile(Project project, ApiInfo apiInfo) {

        StringBuffer sb = new StringBuffer();
        List<SQLParamVo> sqlParamVos = apiInfo.getParamVos().stream().filter(e -> e.getMaster() != null && e.getMaster() == true).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(sqlParamVos)){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }
        SQLParamVo sqlParamVo = sqlParamVos.get(0);
        String modelName = StringUtil.toLowerCaseFirstOne(StringUtil.toUnderscoreToCamelCase(sqlParamVo.getTableName()));

        sb.append("\n{\n" +
                "\t\"title\": \""+sqlParamVo.getTableComment()+"\",\n" +
                "\t\"icon\": \"&#xe653;\",\n" +
                "\t\"spread\": true,\n" +
                "\t\"children\": [{\n" +
                "\t\t\"title\": \""+sqlParamVo.getTableComment()+"\",\n" +
                "\t\t\"icon\": \"&#xe630;\",\n" +
                "\t\t\"href\": \"page/"+modelName+"/"+modelName+"List.html\"\n" +
                "\t}]\n" +
                "},\n");

        sb.append("//${menus}");
        String menus = sb.toString();
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("modelName", modelName);
                put("menus",  menus);
            }
        };

        String srcFilePath = project.getTemplateDir() + "style" +File.separator+"js" + File.separator + "nav.js";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+File.separator +"style" +File.separator+"js" + File.separator + "nav.js";
        File file = new File(destFilePath);
        if(file.exists()){
            srcFilePath = destFilePath;
        }
        FileUtil.copyFile(srcFilePath,destFilePath, map,true);
    }
}
