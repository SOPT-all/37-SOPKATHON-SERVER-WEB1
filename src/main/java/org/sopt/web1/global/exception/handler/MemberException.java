package org.sopt.web1.global.exception.handler;

import org.sopt.web1.global.exception.BusinessException;
import org.sopt.web1.global.exception.ErrorCode;

public class MemberException extends BusinessException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
