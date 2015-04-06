package com.cgi.chatroom.rest.controller.converter;

import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Converter used to generate a {@link javax.xml.datatype.XMLGregorianCalendar} from a given
 * Date.
 */

@Component
public class DateConverter {

    /**
     * Converts the @link{Date} received to a @link{XMLGregorianCalendar}
     * TimeZone and Time (HH:MM:SS:MMs) information should not be present in the generated calendar
     *
     * @param date the input date to convert
     * @return The XMLGregorian Calendar with only the Date information (YYYYmmDD)
     */
    public XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            xmlGregorianCalendar.setYear(calendar.get(Calendar.YEAR));
            xmlGregorianCalendar.setMonth(calendar.get(Calendar.MONTH) + 1);
            xmlGregorianCalendar.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            xmlGregorianCalendar.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            xmlGregorianCalendar.setMinute(calendar.get(Calendar.MINUTE));
            xmlGregorianCalendar.setSecond(calendar.get(Calendar.SECOND));
            xmlGregorianCalendar.setMillisecond(calendar.get(Calendar.MILLISECOND));
            return xmlGregorianCalendar;
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
