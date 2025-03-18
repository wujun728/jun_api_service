package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.FileUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelFileService;
import io.github.wujun728.api.core.template.vo.Project;
import io.github.wujun728.api.core.vo.ParamVo;
import io.github.wujun728.api.core.vo.SQLParamVo;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 新增页面
 */
@Service
public class ModelAddFileServiceImpl implements ModelFileService {

    @Override
    public void writeFile(Project project, ApiInfo apiInfo) {

        List<SQLParamVo> sqlParamVos = apiInfo.getParamVos().stream().filter(e -> e.getMaster() != null && e.getMaster() == true).collect(Collectors.toList());
        if(ObjectUtil.isEmpty(sqlParamVos)){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }

        SQLParamVo sqlParamVo = sqlParamVos.get(0);
        String modelName = StringUtil.toLowerCaseFirstOne(StringUtil.toUnderscoreToCamelCase(sqlParamVo.getTableName()));

        List<ParamVo> paramVos = sqlParamVo.getParamVos();
        ParamVo first = null;
        StringBuffer sb = new StringBuffer();
        if(ObjectUtil.isNotEmpty(paramVos)){
            for (ParamVo paramVo:paramVos){
                if(paramVo.getPrimaryKey()==null || paramVo.getPrimaryKey() == false){
                    if(first==null){
                        first = paramVo;
                    }
                    sb.append("\t\t\t<div class=\"layui-form-item\">\n" +
                            "\t\t\t\t<label class=\"layui-form-label\">"+StringUtil.getValidNullStr(paramVo.getColumnComment())+"<i class=\"color-red\">*</i></label>\n" +
                            "\t\t\t\t<div class=\"layui-input-block\">\n" +
                            "\t\t\t\t\t<input type=\"text\" name=\""+paramVo.getAttrName()+"\" lay-verify=\""+paramVo.getAttrName()+"\"\n" +
                            "\t\t\t\t\t\tplaceholder=\"请输入"+StringUtil.getValidNullStr(paramVo.getColumnComment())+"\" value=\""+StringUtil.getValidNullStr(paramVo.getColumnDefault())+"\" autocomplete=\"off\" class=\"layui-input\">\n" +
                            "\t\t\t\t</div>\n" +
                            "\t\t\t</div>\n");
                }
            }
        }
        String modelInputList = sb.toString();
        String attrName = first!=null?first.getAttrName():"";
        String attrComment = first!=null?first.getColumnComment():"";
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("modelComment", sqlParamVo.getTableComment());
                put("modelInputList", modelInputList);
                put("attrName", attrName);
                put("attrComment", attrComment);
                put("modelName", modelName);
                put("apiPath", apiInfo.getApiPath());
            }
        };

        String srcFilePath = project.getTemplateDir() +"page"+ File.separator +"model" + File.separator + "modelAdd.html";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+ File.separator+"page"+ File.separator + modelName + File.separator + modelName+"Add.html";
        FileUtil.copyFile(srcFilePath,destFilePath, map, true);
    }
}
