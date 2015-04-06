package com.cgi.chatroom.rest.dto;

/**
 * DTO that represents and error message
 */
public class ErrorDTO {

    private int code;
    private String message;

    public ErrorDTO(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {

        return code;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}