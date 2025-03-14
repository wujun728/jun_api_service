package io.github.wujun728.api.common.bean;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.utils.MessageUtils;
import io.github.wujun728.api.common.bean.vo.TotalVo;
import cn.hutool.core.date.DateUtil;
import io.github.wujun728.api.common.bean.vo.EmptyVo;
import io.github.wujun728.api.common.bean.vo.IdVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 返回结果
 * @version 1.0
 * @date 2024-05-06
 */
@ApiModel(value = "请求结果", description = "请求结果")
public class ApiResult<T> {

    @ApiModelProperty(value = "返回代码 200成功 其它失败", example = "200")
    private int code;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "响应时间", example = "2024-05-06 11:21:09")
    private String timestamp;

    public ApiResult(EnumCode enumCode) {
        this.setCode(enumCode);
        this.message = MessageUtils.get(enumCode);
        this.timestamp = DateUtil.now();
        this.setResultData(null);
    }

    public ApiResult(EnumCode enumCode, T data) {
        this.setCode(enumCode);
        this.message = MessageUtils.get(enumCode);
        this.timestamp = DateUtil.now();
        this.setResultData(data);
    }

    public ApiResult(EnumCode enumCode, T data, String message) {
        this.setCode(enumCode);
        this.message = message;
        this.timestamp = DateUtil.now();
        this.setResultData(data);
    }

    private void setResultData(T data){
        if(data!=null){
            this.data = data;
            return;
        }
        Class<T> type = (Class) this.getClass().getGenericSuperclass();
        this.data = JSON.parseObject("{}", type);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @ApiModelProperty(value = "是否成功", example = "true")
    public boolean isSuccess() {
        return this.code == EnumCode.SUCCESS.getCode();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private void setCode(EnumCode enumCode) {
        int eCode = enumCode.getCode();
        if(eCode>=200 && eCode < 300){
            this.code = 200;
        }else{
            this.code = eCode;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 操作成功
     * @param data
     * @return 消息结果
     */
    public static <T> ApiResult<T> success(T data){
        if(data instanceof EnumCode){
            return new ApiResult((EnumCode)data, new EmptyVo());
        }else if(data instanceof Integer){
            return new ApiResult(EnumCode.SUCCESS, new IdVo<Integer>((Integer) data));
        }else if(data instanceof Long){
            return new ApiResult(EnumCode.SUCCESS, new IdVo<Long>((Long) data));
        }else if(data instanceof String){
            return new ApiResult(EnumCode.SUCCESS, new IdVo<String>((String) data));
        }
        return new ApiResult(EnumCode.SUCCESS, data);
    }

    public static <T> ApiResult<T> successData(EnumCode enumCode, T data){
        if(data instanceof Integer){
            return new ApiResult(enumCode, new TotalVo<Integer>((Integer) data));
        }else if(data instanceof Long){
            return new ApiResult(enumCode, new TotalVo<Long>((Long) data));
        }else if(data instanceof String){
            return new ApiResult(enumCode, new TotalVo<String>((String) data));
        }
        return new ApiResult(enumCode, data);
    }

    /**
     * 操作成功
     * @param message 消息
     * @return 消息结果
     */
    public static <T> ApiResult<T> message(String message){
        return new ApiResult(EnumCode.SUCCESS,null,message);
    }

    /**
     * 操作成功
     * @param enumCode
     * @return 消息结果
     */
    public static <T> ApiResult<T> success(EnumCode enumCode, T data){
        if(data instanceof Integer){
            return new ApiResult(enumCode, new IdVo((Integer) data));
        }else if(data instanceof Long){
            return new ApiResult(enumCode, new IdVo((Long) data));
        }else if(data instanceof String){
            return new ApiResult(enumCode, new IdVo((String) data));
        }
        return new ApiResult(enumCode, data);
    }

    /**
     * 操作失败
     * @return 消息结果
     */
    public static ApiResult failure(EnumCode enumCode){
        return new ApiResult(enumCode);
    }

    /**
     * 操作失败
     * @param message 消息
     * @return 消息结果
     */
    public static ApiResult failure(EnumCode enumCode, String message){
        return new ApiResult(enumCode, null, message);
    }

    /**
     * 拒绝访问
     * @param message 消息
     * @return 消息结果
     */
    public static ApiResult forbidden(String message){
        return new ApiResult(EnumCode.NO_PERMISSION, null, message);
    }

    /**
     * authorization过期或为空
     * @param message 消息
     * @return 消息结果
     */
    public static ApiResult authorizationDeny(String message){
        return new ApiResult(EnumCode.TOKEN_EXPIRED, null, message);
    }

    /**
     * 返回对象
     * @param data 数据
     * @return 结果对象
     * @param <T> 数据类型
     */
    public static <T> ApiResult<T> data(T data){
        return new ApiResult<T>(EnumCode.SUCCESS, data);
    }

    /**
     * 返回对象
     * @param data 数据
     * @return 结果对象
     * @param <T> 数据类型
     */
    public static <T> ApiResult<T> data(T data, String message){
        return new ApiResult<T>(EnumCode.SUCCESS, data, message);
    }


    /**
     * 返回分页查询集合
     * @param page 分页数据
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> ApiResult<Page<T>> page(Page<T> page){
        return new ApiResult<Page<T>>(EnumCode.SUCCESS, page);
    }

    /**
     * 返回分页查询集合
     * @param page 分页数据
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> ApiResult<Page<T>> page(Page<T> page, String message){
        return new ApiResult<Page<T>>(EnumCode.SUCCESS, page, message);
    }


    /**
     * 返回分页查询集合
     * @param data 集合数据
     * @param total 总数
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> ApiResult<Page<T>> page(List<T> data, long total){
        return new ApiResult<Page<T>>(EnumCode.SUCCESS, new Page<T>(data,total));
    }

    /**
     * 返回分页查询集合
     * @param data 集合数据
     * @param total 总数
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> ApiResult<Page<T>> page(List<T> data, long total, String message){
        return new ApiResult<Page<T>>(EnumCode.SUCCESS, new Page<T>(data,total),message);
    }

    /**
     * json转换
     * @param json
     * @param type
     * @return
     * @param <T>
     */
    public static <T> ApiResult<T> parseObject(String json, Class<T> type) {
        ApiResult<T> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<T>>(type) {}, Feature.OrderedField);
        return apiResult;
    }

    /**
     * json转换
     * @param json
     * @param type
     * @return
     * @param <T>
     */
    public static <T> ApiResult<List<T>> parseList(String json, Class<T> type) {
        ApiResult<List<T>> apiResult = JSON.parseObject(json, new TypeReference<ApiResult<List<T>>>(type) {}, Feature.OrderedField);
        return apiResult;
    }

}
