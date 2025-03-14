package io.github.wujun728.api.core.template.service;

import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.template.vo.Project;

public interface ModelFileService {

    /**
     * 写文件
     * @param project
     * @param apiInfo
     */
    public void writeFile(Project project, ApiInfo apiInfo);

}
