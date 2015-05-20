package com.cgi.chatroom.rest.controller;

import com.cgi.chatroom.rest.controller.converter.DateConverter;
import com.cgi.chatroom.rest.dto.PublishMessageRequestDTO;
import com.cgi.chatroom.rest.dto.PublishMessageResponseDTO;
import com.cgi.chatroom.rest.exception.UnauthorizedUserAgentException;
import com.cgi.chatroom.rest.utils.DateUtils;

import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.PublishResponseDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.v1.ChatroomService;
import com.cgi.service.chatroom.v1.PublishFault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chat room service REST controller implementation
 */
@RestController
@RequestMapping("/chatroom")
public class ChatRoomServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomServiceController.class);

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String DEFAULT_USER_ID = "1";

    private static final String CHROME_REST_CLIENT_MOZILLA_AGENT = "Mozilla";
    private static final String USER_AGENT_DIRTY_HACK = "WebApp";

    @Autowired
    private ChatroomService chatroomServiceClient;

    @Autowired
    private DateConverter dateConverter;

    @Autowired
    private DateUtils dateUtils;

    /**
     * Publish a message to the chat room
     * Only Authorized User Agents can publish messages
     *
     * @param message   Message to be published
     * @param userAgent User agent sending the request to be passed with the HTTP Headers
     * @return PublishMessageResponseDTO containing the messageID created together with the creation date.
     * @throws UnauthorizedUserAgentException in case the user agent is not allowed to publish the message.
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PublishMessageResponseDTO publish(@RequestBody PublishMessageRequestDTO message,
                                             @RequestHeader(USER_AGENT_HEADER) String userAgent) throws
            UnauthorizedUserAgentException {

        LOG.info("publish method called with message {} from user agent [{}]", message, userAgent);

        // Prepare message content
        PublishRequestDTO publishRequestDTO = new PublishRequestDTO();

        // Nick name and message content are simply copied into the SOAP xml publish request.
        publishRequestDTO.setSender(message.getSenderNickname());
        publishRequestDTO.setContent(message.getContent());

        // Prepare headers
        HeaderDTO headerDTO = new HeaderDTO();
        MessageOriginType messageOriginType = new MessageOriginType();
        // The send date populated with the current date (converted to XMLGregorianCalendar)
        messageOriginType.setSentDate(dateConverter.toXMLGregorianCalendar(dateUtils.getSystemTime()));

        // Awful hack that allows debug from Browser, force to WebApp input.
        if (userAgent.contains(CHROME_REST_CLIENT_MOZILLA_AGENT)) {
            messageOriginType.setUserAgent(USER_AGENT_DIRTY_HACK);
        } else {
            messageOriginType.setUserAgent(userAgent);
        }
        headerDTO.setOrigin(messageOriginType);
        // Not used, just hard coded for now...
        headerDTO.setUserID(DEFAULT_USER_ID);
        try {
            // Call Web service
            PublishResponseDTO serviceOutputDTO = chatroomServiceClient.publish(publishRequestDTO, headerDTO);
            PublishMessageResponseDTO responseDTO = new PublishMessageResponseDTO();
            // Return meta info to caller
            responseDTO.setCreationDate(dateConverter.toDate(serviceOutputDTO.getMessage().getCreationDate()));
            responseDTO.setMessageId(serviceOutputDTO.getMessage().getMessageId());
            return responseDTO;
        } catch (PublishFault publishFault) {
            // Just throw UnauthorizedUserAgentException that will be handled by a dedicated exception handler
            // to return an error message.
            throw new UnauthorizedUserAgentException();
        }
    }
}
