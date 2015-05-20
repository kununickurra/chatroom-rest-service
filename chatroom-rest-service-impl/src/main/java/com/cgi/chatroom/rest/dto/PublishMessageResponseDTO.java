package com.cgi.chatroom.rest.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * DTO used to return meta information concerning the message when publishing.
 */
public class PublishMessageResponseDTO {

    private Long messageId;
    private Date creationDate;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    // Override default serializer as it just print the numeric value of the date.
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "PublishMessageResponseDTO{" +
                "creationDate=" + creationDate +
                ", messageId=" + messageId +
                '}';
    }
}
