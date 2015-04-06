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

    @ExceptionHandler({UnauthorizedUserAgentException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleException(UnauthorizedUserAgentException e) {
        return new ErrorDTO(1001, "Unauthorized");
    }
}