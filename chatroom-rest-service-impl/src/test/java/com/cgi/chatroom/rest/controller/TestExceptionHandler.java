package com.cgi.chatroom.rest.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles the unknown exceptions so that the Unexpected runtime exceptions received
 * from the controller can be debugged and display on a stack trace.
 */
@ControllerAdvice
public class TestExceptionHandler {

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e) {
        e.printStackTrace();
    }
}
