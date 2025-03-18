package io.github.wujun728.api.core.template.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lowcode.config")
public class Project {

    /**
     * 项目名称
     */
    private String projectName ="platform-v2";

    /**
     * 请求token名称
     */
    private String tokenName = "Authorization";

    /**
     * 应用服务地址
     */
    private String serverUrl = "http://127.0.0.1:8080";

    /**
     * API前缀
     */
    private String apiPathPrefix = "/lowcode/api/";

    /**
     * 页面模板模板
     */
    private String templateDir = "D:\\admin\\project\\lowcode-template\\";


    /**
     * 生成的项目目录
     */
    private String outputDir = "D:\\admin\\project\\lowcode-output\\";

    /**
     * 生成的项目文件夹
     */
    private String outputFolder = "platform-auto";

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getApiPathPrefix() {
        return apiPathPrefix;
    }

    public void setApiPathPrefix(String apiPathPrefix) {
        this.apiPathPrefix = apiPathPrefix;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
