package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.FileUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelFileService;
import io.github.wujun728.api.core.template.vo.Project;
import io.github.wujun728.api.core.vo.QueryColumnVo;
import io.github.wujun728.api.core.vo.SQLParamVo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelListFileServiceImpl implements ModelFileService {

    @Override
    public void writeFile(Project project, ApiInfo apiInfo) {
        if(ObjectUtil.isEmpty(apiInfo.getParamVos())){
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }
        String primaryId = "";
        String sortName = "";
        String sortOrder = "";
        String modelName = "";
        String modelComment = "";
        QueryColumnVo first = null;
        StringBuffer sb = new StringBuffer();
        for (SQLParamVo sqlParamVo:apiInfo.getParamVos()){
            List<QueryColumnVo> queryColumns = sqlParamVo.getQueryColumns();
            if(sqlParamVo.getMaster()!=null && sqlParamVo.getMaster() == true){
                modelName = StringUtil.toLowerCaseFirstOne(StringUtil.toUnderscoreToCamelCase(sqlParamVo.getTableName()));
                modelComment = sqlParamVo.getTableComment();
                for (QueryColumnVo queryColumnVo:queryColumns){
                    if(queryColumnVo.getPrimaryKey() ==null || queryColumnVo.getPrimaryKey() == false){
                        if(first==null){
                            first = queryColumnVo;
                        }
                        sb.append("\n\t\t\t\t\t,{field:'"+queryColumnVo.getAttrName()+"', title: '"+StringUtil.getValidNullStr(queryColumnVo.getColumnComment())+"', sort: true}");
                    }else{
                        primaryId = queryColumnVo.getAttrName();
                    }
                    if("createTime".equals(queryColumnVo.getAttrName())){
                        sortName = "createTime";
                        sortOrder = "desc";
                    }
                }
            }else{
                for (QueryColumnVo queryColumnVo:queryColumns){
                    sb.append("\n\t\t\t\t\t,{field:'"+ queryColumnVo.getAttrName() +"', title: '"+StringUtil.getValidNullStr(queryColumnVo.getColumnComment())+"'}");
                }
            }
        }

        String colsList = sb.toString();
        String primaryIdStr = primaryId;
        String sortNameStr = sortName;
        String sortOrderStr = sortOrder;
        String modelCommentStr = modelComment;
        String modelNameStr = modelName;
        String attrName = first!=null?first.getAttrName():"";
        String attrComment = first!=null?first.getColumnComment():"";
        String delApiPath = StrUtil.isNotBlank(apiInfo.getDeleteApiPath())?apiInfo.getDeleteApiPath():modelName;
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("modelComment", modelCommentStr);
                put("apiPath", apiInfo.getApiPath());
                put("delApiPath", delApiPath);
                put("primaryId", primaryIdStr);
                put("attrName", attrName);
                put("attrComment", attrComment);
                put("modelName", modelNameStr);
                put("sortName", sortNameStr);
                put("sortOrder", sortOrderStr);
                put("colsList", colsList);
            }
        };
        String srcFilePath = project.getTemplateDir() +"page"+ File.separator + "model" + File.separator + "modelList.html";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+ File.separator +"page"+ File.separator + modelName + File.separator + modelName+"List.html";
        FileUtil.copyFile(srcFilePath,destFilePath, map, true);
    }
}
