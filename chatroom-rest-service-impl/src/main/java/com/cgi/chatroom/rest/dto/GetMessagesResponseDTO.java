package com.cgi.chatroom.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO used to return the List of Messages.
 * Contains a list of {@link ChatroomMessage}
 */
public class GetMessagesResponseDTO {

    private List<ChatroomMessage> messages = new ArrayList<>();

    public void addMessage(ChatroomMessage message) {
        messages.add(message);
    }

    public List<ChatroomMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatroomMessage> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "GetMessagesResponseDTO{" +
                "messages=" + messages +
                '}';
    }
}
