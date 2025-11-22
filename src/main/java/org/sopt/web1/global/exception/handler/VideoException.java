package org.sopt.web1.global.exception.handler;

import org.sopt.web1.global.exception.BusinessException;
import org.sopt.web1.global.exception.ErrorCode;

public class VideoException extends BusinessException {
    public VideoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
