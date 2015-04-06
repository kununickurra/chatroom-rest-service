package com.cgi.chatroom.rest.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom date serializer that uses a decent date representation in JSON.
 */
public class DateTimeJsonSerializer extends JsonSerializer<Date> {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws
            IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String formattedDate = formatter.format(value);
        gen.writeString(formattedDate);
    }
}
