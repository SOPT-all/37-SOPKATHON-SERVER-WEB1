package org.sopt.web1.global.exception.handler;

import org.sopt.web1.global.exception.BusinessException;
import org.sopt.web1.global.exception.ErrorCode;

public class FileException extends BusinessException {
    public FileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
