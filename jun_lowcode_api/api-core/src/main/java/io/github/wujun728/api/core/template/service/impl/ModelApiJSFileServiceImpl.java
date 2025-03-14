package io.github.wujun728.api.core.template.service.impl;

import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.FileUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.service.ModelApiFileService;
import io.github.wujun728.api.core.template.vo.Project;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModelApiJSFileServiceImpl implements ModelApiFileService {

    @Override
    public void writeFile(Project project, List<ApiInfo> apiInfos) {

        StringBuffer sb = new StringBuffer();
        for (ApiInfo apiInfo:apiInfos){
            if(SqlUtl.OP_TYPE_SELECT.equals(apiInfo.getOpType())){
                sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                        "\t\t"+apiInfo.getApiPath()+"List:{\n" +
                        "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                        "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                        "\t\t},\n");
            }else if(SqlUtl.OP_TYPE_SELECT_ONE.equals(apiInfo.getOpType())){
                sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                        "\t\t"+apiInfo.getApiPath()+"Detail:{\n" +
                        "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                        "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                        "\t\t},\n");
            }else if(SqlUtl.OP_TYPE_INSERT.equals(apiInfo.getOpType())){
                sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                        "\t\t"+apiInfo.getApiPath()+"Add:{\n" +
                        "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                        "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                        "\t\t},\n");
            }else if(SqlUtl.OP_TYPE_UPDATE.equals(apiInfo.getOpType())){
                sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                        "\t\t"+apiInfo.getApiPath()+"Edit:{\n" +
                        "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                        "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                        "\t\t},\n");
            }else if(SqlUtl.OP_TYPE_DELETE.equals(apiInfo.getOpType())){
                sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                        "\t\t"+apiInfo.getApiPath()+"Del:{\n" +
                        "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                        "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                        "\t\t},\n");
            }
        }

        sb.append("//${apiUrls}");
        String apiUrls = sb.toString();
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("tokenName", project.getTokenName());
                put("apiUrls",  apiUrls);
            }
        };

        String srcFilePath = project.getTemplateDir() + "style" +File.separator+"js" + File.separator + "api.js";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+File.separator +"style" +File.separator+"js" + File.separator + "api.js";
        File file = new File(destFilePath);
        if(file.exists()){
            srcFilePath = destFilePath;
        }
        FileUtil.copyFile(srcFilePath,destFilePath, map,true);
    }

    @Override
    public void writeFile(Project project, ApiInfo apiInfo) {
        StringBuffer sb = new StringBuffer();
        if(SqlUtl.OP_TYPE_SELECT.equals(apiInfo.getOpType())){
            sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                    "\t\t"+apiInfo.getApiPath()+"List:{\n" +
                    "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                    "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                    "\t\t},\n");
        }else if(SqlUtl.OP_TYPE_SELECT_ONE.equals(apiInfo.getOpType())){
            sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                    "\t\t"+apiInfo.getApiPath()+"Detail:{\n" +
                    "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                    "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                    "\t\t},\n");
        }else if(SqlUtl.OP_TYPE_INSERT.equals(apiInfo.getOpType())){
            sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                    "\t\t"+apiInfo.getApiPath()+"Add:{\n" +
                    "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                    "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                    "\t\t},\n");
        }else if(SqlUtl.OP_TYPE_UPDATE.equals(apiInfo.getOpType())){
            sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                    "\t\t"+apiInfo.getApiPath()+"Edit:{\n" +
                    "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                    "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                    "\t\t},\n");
        }else if(SqlUtl.OP_TYPE_DELETE.equals(apiInfo.getOpType())){
            sb.append("\t\t//"+apiInfo.getApiName()+"\n" +
                    "\t\t"+apiInfo.getApiPath()+"Del:{\n" +
                    "\t\t\tmethod:'"+apiInfo.getApiType()+"',\n" +
                    "\t\t\turl: serverUrl + '"+project.getApiPathPrefix()+apiInfo.getApiPath()+"'\n" +
                    "\t\t},\n");
        }

        sb.append("//${apiUrls}");
        String apiUrls = sb.toString();
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("tokenName", project.getTokenName());
                put("apiUrls",  apiUrls);
            }
        };

        String srcFilePath = project.getTemplateDir() + "style" +File.separator+"js" + File.separator + "api.js";
        String destFilePath = project.getOutputDir() + project.getOutputFolder()+File.separator +"style" +File.separator+"js" + File.separator + "api.js";
        File file = new File(destFilePath);
        if(file.exists()){
            srcFilePath = destFilePath;
        }
        FileUtil.copyFile(srcFilePath,destFilePath, map,true);
    }
}
