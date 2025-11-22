package org.sopt.web1.global.exception.handler;

import org.sopt.web1.global.exception.BusinessException;
import org.sopt.web1.global.exception.ErrorCode;

public class AiException extends BusinessException {
    public AiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
