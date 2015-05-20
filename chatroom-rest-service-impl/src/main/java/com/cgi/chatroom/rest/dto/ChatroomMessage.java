package com.cgi.chatroom.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Chat room message DTO
 */
public class ChatroomMessage {

    private long messageId;
    private String content;
    private String sender;
    private String originUserAgent;
    private Date creationDate;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getOriginUserAgent() {
        return originUserAgent;
    }

    public void setOriginUserAgent(String originUserAgent) {
        this.originUserAgent = originUserAgent;
    }

    // Override default serializer as it just print the numeric value of the date.
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatroomMessage{" +
                "messageId=" + messageId +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", originUserAgent='" + originUserAgent + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
