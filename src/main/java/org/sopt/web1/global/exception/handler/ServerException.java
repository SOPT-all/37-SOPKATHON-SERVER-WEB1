package org.sopt.web1.global.exception.handler;

import org.sopt.web1.global.exception.BusinessException;
import org.sopt.web1.global.exception.ErrorCode;

public class ServerException extends BusinessException {
    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
