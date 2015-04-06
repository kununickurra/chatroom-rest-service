package com.cgi.chatroom.rest.controller;

import com.cgi.chatroom.rest.controller.converter.DateConverter;
import com.cgi.chatroom.rest.dto.PublishMessageRequestDTO;
import com.cgi.chatroom.rest.dto.PublishMessageResponseDTO;
import com.cgi.chatroom.rest.exception.UnauthorizedUserAgentException;
import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.PulishResponseDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.v1.ChatroomService;
import com.cgi.service.chatroom.v1.PublishFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Chat room service REST controller
 */
@RestController
@RequestMapping("/chatroom")
public class ChatRoomServiceController {

    private static final String USER_AGENT_HEADER = "User-Agent";

    @Autowired
    private ChatroomService chatroomServiceClient;

    @Autowired
    private DateConverter dateConverter;

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    PublishMessageResponseDTO publish(
            @RequestBody PublishMessageRequestDTO message,
            @RequestHeader(USER_AGENT_HEADER) String userAgent) throws UnauthorizedUserAgentException {

        // Prepare message content
        PublishRequestDTO publishRequestDTO = new PublishRequestDTO();
        publishRequestDTO.setSender(message.getSenderNickname());
        publishRequestDTO.setContent(message.getContent());

        // Prepare headers
        HeaderDTO headerDTO = new HeaderDTO();
        MessageOriginType messageOriginType = new MessageOriginType();
        messageOriginType.setSentDate(dateConverter.toXMLGregorianCalendar(new Date()));

        // Allow debug from Browser, force to WebApp input.
        if (userAgent.contains("Mozilla")) {
            messageOriginType.setUserAgent("WebApp");
        } else {
            messageOriginType.setUserAgent(userAgent);
        }
        headerDTO.setOrigin(messageOriginType);
        // Not used
        headerDTO.setUserID("1");
        try {
            // Call Web service
            PulishResponseDTO serviceOutputDTO = chatroomServiceClient.publish(publishRequestDTO, headerDTO);
            PublishMessageResponseDTO responseDTO = new PublishMessageResponseDTO();
            // Return meta info to caller
            responseDTO.setCreationDate(serviceOutputDTO.getMessage().getCreationDate().toGregorianCalendar().getTime());
            responseDTO.setMessageId(serviceOutputDTO.getMessage().getMessageId());
            return responseDTO;
        } catch (PublishFault publishFault) {
            throw new UnauthorizedUserAgentException();
        }
    }
}
