package com.gs.swiftcv.core.common;

/**
 * 响应码枚举。
 */
public enum ResultCode {
    /**
     * 成功。
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败。
     */
    FAIL(500, "操作失败"),

    /**
     * 参数错误。
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权。
     */
    UNAUTHORIZED(401, "未授权"),

    /**
     * 禁止访问。
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在。
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 系统错误。
     */
    SYSTEM_ERROR(500, "系统错误");

    private final Integer code;
    private final String message;

    /**
     * 构造响应码枚举。
     *
     * @param code 状态码
     * @param message 状态描述
     */
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码。
     *
     * @return 状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取状态描述。
     *
     * @return 状态描述
     */
    public String getMessage() {
        return message;
    }
}
