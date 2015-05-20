package com.cgi.chatroom.rest.dto;

/**
 * DTO used when publishing a message on the chat room
 */
public class PublishMessageRequestDTO {

    private String senderNickname;
    private String content;

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PublishMessageRequestDTO{" +
                "senderNickname='" + senderNickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
