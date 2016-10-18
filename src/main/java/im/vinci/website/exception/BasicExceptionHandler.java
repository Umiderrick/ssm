package im.vinci.website.exception;

import im.vinci.website.domain.ErrorResult;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.InvalidParameterException;

/**
 * Created by henryhome on 3/31/15.
 */
@ControllerAdvice(basePackages = "im.vinci.website.controller")
public class BasicExceptionHandler {
    public Logger logger = LoggerFactory.getLogger(getClass());

    //global
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult handleExceptions(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ex.toString(), "内部未知错误");
    }

    @ExceptionHandler(value = VinciException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ErrorResult handleVinciExceptions(VinciException ex) {
        logger.warn(ex.getMessage());
        return new ErrorResult(ex.getErrorCode(),ex.getMessage(),ex.getErrorMsgToUser());
    }

    @ExceptionHandler(value = {InvalidParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResult handleGeneralVinciExceptions(VinciException ex) {
        logger.warn(ex.getMessage());
        return new ErrorResult(ex.getErrorCode(), ex.getMessage(), ex.getErrorMsgToUser());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResult handleMssingServletRequestParamter(MissingServletRequestParameterException ex) {
        logger.warn(ex.getMessage());
        return new ErrorResult(HttpStatus.BAD_REQUEST, ex.toString(), "参数不全");
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResult handleTypeMismatch(TypeMismatchException ex) {
        logger.warn(ex.getMessage());
        return new ErrorResult(HttpStatus.BAD_REQUEST, ex.getMessage(), "参数类型不匹配");
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResult handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.warn(ex.getMessage());
        return new ErrorResult(HttpStatus.BAD_REQUEST, ex.getMessage(), "JSON格式不对");
    }

    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult handleInternalServer(NullPointerException ex) {
        logger.error(ex.getMessage(), ex);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ex.toString(), "内部未知错误");
    }

    @ExceptionHandler(value = {BadSqlGrammarException.class, MyBatisSystemException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResult handleBadSqlGrammar(BadSqlGrammarException ex) {
        logger.error(ex.getMessage(), ex);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, ex.toString(), "内部数据库错误");
    }
}



