package com.cgi.chatroom.rest.controller;

import com.cgi.chatroom.rest.controller.converter.DateConverter;
import com.cgi.chatroom.rest.utils.DateUtils;

import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.PublishResponseDTO;
import com.cgi.service.chatroom.type.v1.MessageType;
import com.cgi.service.chatroom.v1.ChatroomService;
import com.cgi.service.chatroom.v1.PublishFault;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import javax.json.Json;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This test class aims to test the Controller implementation as a REST controller.
 * Use of the SpringMVC mocking possibilities together with Mockito allows you to test both the implementation
 * as a Standard Service but also the Configuration (Spring annotations, Jackson,...).
 * Service is tested using row JSON
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatRoomServiceControllerTest {

    private static final String BASE_SERVICE_URL = "/chatroom/publish";

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private static final String USER_AGENT = "User Agent";
    private static final String MESSAGE_CONTENT = "message content";
    private static final String MESSAGE_SENDER = "message sender";
    private static final String EXPECTED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final int MESSAGE_ID = 10;

    private MockMvc mockMvc;

    @Mock
    private ChatroomService chatroomService;

    @Mock
    private DateConverter dateConverter;

    @Mock
    private DateUtils dateUtils;

    @InjectMocks
    private ChatRoomServiceController testedController = new ChatRoomServiceController();

    @Test
    public void shouldPublishMessageSuccessfully() throws Exception {
        // Given
        Date messageSendDate = new Date();
        Date messageCreationDate = new Date();
        XMLGregorianCalendar xmlSendDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        XMLGregorianCalendar xmlCreationDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        given(dateUtils.getSystemTime()).willReturn(messageSendDate);
        given(dateConverter.toXMLGregorianCalendar(messageSendDate)).willReturn(xmlSendDate);
        given(dateConverter.toDate(xmlCreationDate)).willReturn(messageCreationDate);

        // Message returned by the Chatroom service when calling the publish() method
        long messageId = MESSAGE_ID;
        MessageType messageType = new MessageType();
        messageType.setCreationDate(xmlCreationDate);
        messageType.setMessageId(messageId);
        PublishResponseDTO publishResponseDTO = new PublishResponseDTO(messageType);

        // Use argument captor to capture the chatroomService.publish() arguments
        ArgumentCaptor<PublishRequestDTO> chatRoomServiceRequestDTOCaptor = ArgumentCaptor
                .forClass(PublishRequestDTO.class);
        ArgumentCaptor<HeaderDTO> chatRoomServiceHeaderDTOCaptor = ArgumentCaptor.forClass(HeaderDTO.class);
        given(chatroomService
                .publish(chatRoomServiceRequestDTOCaptor.capture(), chatRoomServiceHeaderDTOCaptor.capture()))
                .willReturn(publishResponseDTO);

        // When
        ResultActions result = mockMvc.perform(
                post(BASE_SERVICE_URL).content(buildDefaultRequest()).contentType(MediaType.APPLICATION_JSON).header(
                        USER_AGENT_HEADER_NAME, USER_AGENT) // Add the User agent as part of the request headers.
                        .accept(MediaType.APPLICATION_JSON));
        // Then
        result.andExpect(status().isOk()); // Ensure expected HTTP status
        assertCorrectResponse(messageCreationDate, messageId, result);
        assertCorrectMessageIsPublished(xmlSendDate, chatRoomServiceRequestDTOCaptor.getValue(),
                chatRoomServiceHeaderDTOCaptor.getValue());
    }

    @Test
    public void shouldReturnErrorForUnauthorizedUserAgents() throws Exception {
        // Given
        given(chatroomService.publish(any(PublishRequestDTO.class), any(HeaderDTO.class)))
                .willThrow(PublishFault.class);

        // When
        ResultActions result = mockMvc.perform(
                post(BASE_SERVICE_URL).content(buildDefaultRequest()).contentType(MediaType.APPLICATION_JSON).header(
                        USER_AGENT_HEADER_NAME, USER_AGENT) // Add the User agent as part of teh request.
                        .accept(MediaType.APPLICATION_JSON));
        // Then
        result.andExpect(status().isUnauthorized()); // Ensure expected HTTP status
        // Check the content or the response
        result.andExpect(jsonPath("code").value(1001));
        result.andExpect(jsonPath("message").value("Unauthorized"));
    }

    @Before
    public void initMocks() throws Exception {
        mockMvc = standaloneSetup(testedController).setHandlerExceptionResolvers(createExceptionResolver()).build();
    }

    private String buildDefaultRequest() {
        // Row JSON message
        return Json.createObjectBuilder().add("content", "message content").add("senderNickname", "message sender")
                .build().toString();
    }

    private void assertCorrectResponse(Date messageCreationDate, long messageId, ResultActions result) throws
            Exception {
        // Check the content or the response
        result.andExpect(jsonPath("messageId").value((int) messageId));
        // A specific Json mapping need to be done for the creation date so we can test the returned String directly.
        result.andExpect(
                jsonPath("creationDate").value(new SimpleDateFormat(EXPECTED_DATE_FORMAT).format(messageCreationDate)));
        // Make sure that there are no other member that the 2 member tested.
        result.andExpect(jsonPath("$.*", hasSize(2)));
    }

    // Make sure that the tested controller will call the Chat room Service with the right arguments
    private void assertCorrectMessageIsPublished(XMLGregorianCalendar xmlSendDate, PublishRequestDTO publishRequestDTO,
                                                 HeaderDTO headerDTO) {
        assertNotNull(publishRequestDTO);
        assertThat(publishRequestDTO.getContent(), is(MESSAGE_CONTENT));
        assertThat(publishRequestDTO.getSender(), is(MESSAGE_SENDER));

        assertNotNull(headerDTO);
        assertThat(headerDTO.getUserID(), is("1")); // This is hard coded for now
        assertNotNull(headerDTO.getOrigin());
        assertThat(headerDTO.getOrigin().getUserAgent(), is(USER_AGENT));
        assertThat(headerDTO.getOrigin().getSentDate(), is(xmlSendDate));
    }

    // needed to test the controller advices for exception handling
    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod,
                                                                              Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(RestErrorHandler.class).resolveMethod(exception);
                Object handler = new RestErrorHandler();
                // Test if exception is handled by the tested code
                if (method == null) {
                    // If not just use a default test handler that will print the stack trace.
                    method = new ExceptionHandlerMethodResolver(TestExceptionHandler.class).resolveMethod(exception);
                    handler = new TestExceptionHandler();
                }
                return new ServletInvocableHandlerMethod(handler, method);
            }
        };
        // manual setup of Jackson mapping is required using MappingJackson2HttpMessageConverter
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }
}