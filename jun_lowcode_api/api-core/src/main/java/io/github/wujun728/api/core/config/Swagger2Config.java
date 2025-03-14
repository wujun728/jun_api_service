package io.github.wujun728.api.core.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Swagger注册
 * @version 1.0
 * @date 2024-05-06
 */
//@Configuration("lowCodeSwagger2Config")
//@EnableSwagger2WebMvc
public class Swagger2Config {
    public static String SYS_NAME = "低代码API生成服务";
    public static String DEFAULT_VERSION = "V1.0.0";

    @Resource
    private OpenApiExtensionResolver openApiExtensionResolver;

    private ApiInfo apiInfo(String sysName,String version) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(sysName)
                .description(sysName+"接口文档")
                .termsOfServiceUrl("")
                .contact(new Contact("admin", "","admin@admin.com"))
                .version(version).build();
        return apiInfo;
    }

    @Bean("lowCodeCreateAPI")
    public Docket createAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host("")
                .apiInfo(apiInfo(SYS_NAME,DEFAULT_VERSION))
                .pathMapping("/")
                .groupName("低代码模块接口文档"+DEFAULT_VERSION)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.wujun728.api.core.controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildSettingExtensions())
                .globalOperationParameters(getParameters())
                .globalResponseMessage(RequestMethod.POST,getResponseMessages())
                .globalResponseMessage(RequestMethod.PUT,getResponseMessages())
                .globalResponseMessage(RequestMethod.GET,getResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE,getResponseMessages())
                .globalResponseMessage(RequestMethod.OPTIONS,getResponseMessages())
                .globalResponseMessage(RequestMethod.PATCH,getResponseMessages())
                .globalResponseMessage(RequestMethod.HEAD,getResponseMessages())
                .globalResponseMessage(RequestMethod.TRACE,getResponseMessages())
                ;
    }

    private static List<Parameter> getParameters(){
        List<Parameter> pars = new ArrayList<Parameter>();
        return pars;
    }

    private static List<ResponseMessage> getResponseMessages(){
        List<ResponseMessage> responseMessages = new ArrayList<>();
        ResponseMessage responseMessage = new ResponseMessageBuilder().code(200).message("请求成功").build();
//        ResponseMessage responseMessage2 = new ResponseMessageBuilder().code(401).message("您的登录信息已过期，请重新登录").build();
//        ResponseMessage responseMessage3 = new ResponseMessageBuilder().code(402).message("您的账号已在其他设备登录").build();
        ResponseMessage responseMessage4 = new ResponseMessageBuilder().code(403).message("没有访问权限").build();
        ResponseMessage responseMessage5 = new ResponseMessageBuilder().code(404).message("未查到数据").build();
        ResponseMessage responseMessage6 = new ResponseMessageBuilder().code(500).message("服务器内部异常").build();
        responseMessages.add(responseMessage);
//        responseMessages.add(responseMessage2);
//        responseMessages.add(responseMessage3);
        responseMessages.add(responseMessage4);
        responseMessages.add(responseMessage5);
        responseMessages.add(responseMessage6);
        return responseMessages;
    }
}
