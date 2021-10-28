package com.gtiee.example.common.web;

import lombok.Getter;

/**
 * 服务业务运行时异常
 *
 * @author wentao.wu
 */
@Getter
public class ServiceException extends RuntimeException {
    public ServiceException(String errorCode, String errorMsg) {
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 异常编码
     */
    private String errorCode;
    /**
     * 异常信息
     */
    private String errorMsg;
}
