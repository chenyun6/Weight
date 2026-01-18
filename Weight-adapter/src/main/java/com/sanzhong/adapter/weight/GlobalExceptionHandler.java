package com.sanzhong.adapter.weight;

import com.common.tools.core.dto.ResultDTO;
import com.common.tools.core.exception.BizException;
import com.common.tools.core.resultcode.CommonResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理所有异常，转换为标准的 ResultDTO 响应格式
 *
 * @author visual-ddd
 * @since 1.0
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理 RuntimeException 异常
     * 优先使用异常消息作为错误提示
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultDTO<?> runtimeExceptionHandler(HttpServletRequest request, RuntimeException ex) {
        String message = ex.getMessage();
        log.error("runtime error, request={}, message={}", request.getRequestURI(), message, ex);

        // 如果 RuntimeException 有消息，直接使用消息；否则使用系统错误消息
        if (message != null && !message.trim().isEmpty()) {
            return ResultDTO.fail(CommonResultCode.SYSTEM_ERROR.getCode(), message);
        }
        return ResultDTO.fail(CommonResultCode.SYSTEM_ERROR.getCode(), CommonResultCode.SYSTEM_ERROR.getMsg());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public ResultDTO<?> bizExceptionHandler(HttpServletRequest request, BizException ex) {
        log.warn("business error, request={}, code={}, message={}", request.getRequestURI(), ex.getCode(), ex.getMessage());
        return ResultDTO.fail(ex.getCode(), request, ex.getMessage());
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResultDTO<?> exceptionHandler(HttpServletRequest request, Exception ex) {
        String message = ex.getMessage();
        log.error("sys error, request={}, message={}", request.getRequestURI(), message, ex);
        return ResultDTO.fail(CommonResultCode.SYSTEM_ERROR.getCode(), CommonResultCode.SYSTEM_ERROR.getMsg());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO<?> methodArgumentNotValidExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.warn("param validation error, request={}, errors={}", request.getRequestURI(), msgList);
        return getMessageList(msgList);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultDTO<?> constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException ex) {
        List<String> msgList = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.warn("constraint violation error, request={}, errors={}", request.getRequestURI(), msgList);
        return getMessageList(msgList);
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler({BindException.class})
    public ResultDTO<?> bindExceptionHandler(HttpServletRequest request, BindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        log.warn("bind error, request={}, errors={}", request.getRequestURI(), msgList);
        return getMessageList(msgList);
    }

    /**
     * 处理类型不匹配异常
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResultDTO<?> typeMismatchExceptionHandler(HttpServletRequest request, TypeMismatchException ex) {
        log.error("param type error, request={}, message={}", request.getRequestURI(), ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.ERROR_PARAM.getCode(), CommonResultCode.ERROR_PARAM.getMsg());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultDTO<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        log.warn("method not supported, request={}, method={}", request.getRequestURI(), request.getMethod());
        return ResultDTO.fail(CommonResultCode.REQUEST_METHOD_ERROR.getCode(), CommonResultCode.REQUEST_METHOD_ERROR.getMsg());
    }

    /**
     * 处理媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultDTO<?> httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest request, HttpMediaTypeNotSupportedException ex) {
        log.warn("media type not supported, request={}", request.getRequestURI());
        return ResultDTO.fail(CommonResultCode.REQUEST_METHOD_ERROR.getCode(), CommonResultCode.REQUEST_METHOD_ERROR.getMsg());
    }

    /**
     * 处理消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultDTO<?> httpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.warn("message not readable, request={}, message={}", request.getRequestURI(), ex.getMessage());
        return ResultDTO.fail(CommonResultCode.ILLEGAL_PARAM.getCode(), CommonResultCode.ILLEGAL_PARAM.getMsg());
    }

    /**
     * 将错误消息列表转换为 ResultDTO
     */
    private ResultDTO<?> getMessageList(List<String> msgList) {
        if (msgList == null || msgList.isEmpty()) {
            return ResultDTO.fail(CommonResultCode.ERROR_PARAM.getCode(), CommonResultCode.ERROR_PARAM.getMsg());
        }
        // 返回第一个错误消息
        return ResultDTO.fail(CommonResultCode.ERROR_PARAM.getCode(), msgList.get(0));
    }
}
