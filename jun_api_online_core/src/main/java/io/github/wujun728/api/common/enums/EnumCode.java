package io.github.wujun728.api.common.enums;

/**
 * 错误码枚举类
 * @version 1.0
 * @date 2024-05-06
 */
public enum EnumCode {

    //200=请求成功
    SUCCESS(200),
    //201=提交成功
    SUBMIT_SUCCESS(201),
    //202=保存成功
    SAVE_SUCCESS(202),
    //203=新增成功
    INSERT_SUCCESS(203),
    //204=修改成功
    UPDATE_SUCCESS(204),
    //205=更新成功
    MODIFY_SUCCESS(205),
    //206=删除成功
    DELETE_SUCCESS(206),
    //207=操作成功
    OP_SUCCESS(207),
    //208=上传成功
    UPLOAD_SUCCESS(207),
    //209=登录成功
    LOGIN_SUCCESS(209),
    //210=发送成功
    SEND_SUCCESS(210),
    //211=导入成功
    IMPORT_SUCCESS(211),
    //212=回调成功
    CALLBACK_SUCCESS(212),
    //213=退出成功
    EXIT_SUCCESS(213),
    //214=密码修改成功
    PASSWORD_UPDATE_SUCCESS(214),
    //215=审核成功
    AUDIT_SUCCESS(215),
    //216=创建成功
    CREATE_SUCCESS(216),
    //217=token获取成功
    TOKEN_GET_SUCCESS(217),
    //218=清除成功
    CLEAR_SUCCESS(218),
    //219=设置成功
    SETTING_SUCCESS(219),
    //220=密码已重置为
    PASSWORD_RESET_AS(220),
    //221=校验通过
    VALID_SUCCESS(221),
    //222=申请成功
    APPLY_SUCCESS(222),
    //223=认证成功
    AUTH_SUCCESS(223),
    //
    //301=新增失败
    INSERT_FAIL(301),
    //302=修改失败
    UPDATE_FAIL(302),
    //303=删除失败
    DELETE_FAIL(303),
    //304=查询失败
    QUERY_FAIL(304),
    //305=分页参数错误
    PAGE_PARAMS_ERROR(305),
    //306=操作失败
    OP_FAIL(306),
    //307=上传失败
    UPLOAD_FAIL(307),
    //308=文件不存在
    FILE_NOT_EXIST(308),
    //309=上传图片内容为空，无法保存
    UPLOAD_FILE_NOT_EXIST(309),
    //310=上传图片为空
    UPLOAD_PHOTO_EMPTY(310),
    //311=该手机号账户不存在
    PHONE_ACCOUNT_EXIST(311),
    //312=验证码错误
    VALID_CODE_ERROR(312),
    //313=用户名或密码错误
    USERNAME_PASSWORD_ERROR(313),
    //314=回调失败
    CALLBACK_ERROR(314),
    //315=参数错误
    PARAMS_ERROR(315),
    //316=用户未登录
    USER_NO_LOGIN(316),
    //317=导入模板必须是xls或xlsx格式
    TEMPLATE_EXL_ELXS(317),
    //318=该数据已存在
    DATA_EXIST(318),
    //319=未找到xls模板文件
    NO_XLS_TEMPLATE(319),
    //320=导入模板必须是zip格式
    TEMPLATE_ZIP(320),
    //321=手机号不能为空
    PHONE_NOT_EMPTY(321),
    //322=账号不能为空
    ACCOUNT_NOT_EMPTY(322),
    //323=密码不能为空
    PASSWORD_NOT_EMPTY(323),
    //324=授权失败
    AUTHORIZE_FAIL(324),
    //325=校验不通过
    VALID_FAIL(325),
    //326=验证码不能为空
    VALID_CODE_NOT_EMPTY(326),
    //327=新密码不能为空
    NEW_PASSWORD_NOT_EMPTY(327),
    //328=验证码已失效
    VALID_CODE_EXPIRED(328),
    //329=该手机号未注册
    PHONE_NOT_REGISTER(329),
    //330=原密码不能为空
    OLD_PASSWORD_NOT_EMPTY(330),
    //331=原密码错误
    OLD_PASSWORD_ERROR(331),
    //332=上传文件为空
    UPLOAD_FILE_NOT_EMPTY(332),
    //333=查询不到省市区字典
    NOT_FOUND_AREA_DICT(333),
    //334=表格中没有数据
    TABLE_DATA_EMPTY(334),
    //335=请求参数错误
    REQUEST_PARAMS_ERROR(335),
    //336=请求频繁
    REQUEST_FREQUENT(336),
    //337=角色ID参数错误
    ROLE_ID_PARAMS_ERROR(337),
    //338=角色名称不能为空
    ROLE_NAME_NOT_EMPTY(338),
    //339=角色ID不能为空
    ROLE_ID_NOT_EMPTY(339),
    //340=该角色名称已存在
    ROLE_NAME_EXIST(340),
    //341=该角色已存在
    ROLE_EXIST(341),
    //342=参数不能为空
    PARAMS_NOT_EMPTY(342),
    //343=不能为空
    CAN_NOT_EMPTY(343),
    //344=该邮箱已存在
    EMAIL_EXIST(344),
    //345=该手机号已存在
    PHONE_EXIST(345),
    //346=该账号已存在
    ACCOUNT_EXIST(346),
    //347=未查到用户信息
    NOT_FOUND_USER(347),
    //348=用户ID不能为空
    USER_ID_NOT_EMPTY(348),
    //349=数据异常
    DATA_EXCEPTION(349),
    //350=预览文件异常
    PREVIEW_FILE_EXCEPTION(350),
    //351=字典参数错误
    DICT_PARAMS_ERROR(351),
    //352=附件不存在
    ATTACHMENT_NOT_EXIST(352),
    //353=下载文件失败
    DOWNLOAD_FILE_ERROR(353),
    //354=申请失败
    APPLY_FAIL(354),

    //401=您的登录信息已过期，请重新登录
    TOKEN_EXPIRED(401),
    //402=您的账号已在其他设备登录
    ACCOUNT_ALREADY_LOGIN(402),
    //403=没有访问权限
    NO_PERMISSION(403),
    //404=未查到数据
    NO_DATA_FOUND(404),
    //500=服务器内部异常
    SERVER_EXCEPTION(500);

    private int code;
    EnumCode(int code){
        this.code = code;
    }

    public int getCode() {

        return code;
    }

    public static EnumCode getEnumCode(int code) {
        for (EnumCode enumCode : EnumCode.values()) {
            if (enumCode.getCode()==code) {
                return enumCode;
            }
        }
        return EnumCode.SERVER_EXCEPTION;
    }

}
