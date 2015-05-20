package com.cgi.chatroom.rest.controller;

import com.cgi.chatroom.rest.dto.ErrorDTO;
import com.cgi.chatroom.rest.exception.UnauthorizedUserAgentException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles the rest errors thrown from controllers.
 */
@ControllerAdvice
public class RestErrorHandler {

    private static final int UNAUTHORIZED_ACCESS_CODE = 1001;
    private static final String UNAUTHORIZED_ACCESS_MESSAGE = "Unauthorized";

    @ExceptionHandler({UnauthorizedUserAgentException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleException(UnauthorizedUserAgentException e) {
        return new ErrorDTO(UNAUTHORIZED_ACCESS_CODE, UNAUTHORIZED_ACCESS_MESSAGE);
    }
}