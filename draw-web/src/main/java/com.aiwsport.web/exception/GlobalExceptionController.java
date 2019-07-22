package com.aiwsport.web.exception;

import com.aiwsport.core.DrawServerException;
import com.aiwsport.core.DrawServerExceptionFactor;
import com.aiwsport.core.constant.ResultMsg;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanggjian
 */
@RestControllerAdvice
public class GlobalExceptionController {
    private Logger logger = LogManager.getLogger();

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultMsg handleAllException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        DrawServerException drawServerException;
        if (exception instanceof DrawServerException) {
            drawServerException = (DrawServerException) exception;
        } else if (exception instanceof ServletRequestBindingException) {
            drawServerException = new DrawServerException(DrawServerExceptionFactor.MISSING_PARAM, exception.getMessage());
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            String paramName = ((MethodArgumentTypeMismatchException) exception).getParameter().getParameterName();
            String errorMsg = DrawServerExceptionFactor.CONFIG_PARAM_TYPE_MISMATCH.getErrorMsg() + " [" + paramName + "]";
            drawServerException = new DrawServerException(DrawServerExceptionFactor.CONFIG_PARAM_TYPE_MISMATCH, errorMsg);
        } else if (exception instanceof BindException) {
            BindException result = (BindException) exception;
            FieldError fieldError = result.getFieldError();
            drawServerException = new DrawServerException(DrawServerExceptionFactor.BIND_ERROR, fieldError.getField()+"绑定失败，请校验参数类型");
        } else if (exception instanceof NumberFormatException) {
            drawServerException = new DrawServerException(DrawServerExceptionFactor.PATTERN_ERROR, exception.getMessage());
        } else {
            drawServerException = new DrawServerException(DrawServerExceptionFactor.DEFAULT);
            logger.error(ExceptionUtils.getStackTrace(exception));
        }

        logger.error(ExceptionUtils.getStackTrace(exception));
        ResultMsg resultMsg = new ResultMsg(false, drawServerException.getFactor().getErrorCode(), drawServerException.getMessage());
        return resultMsg;
    }
}
