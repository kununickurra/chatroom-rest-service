package com.cgi.chatroom.rest.controller.converter;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Converter used to convert a {@link XMLGregorianCalendar} to/from {@link Date}
 */

@Component
public class DateConverter {

    /**
     * Converts the @link{Date} received to a @link{XMLGregorianCalendar}
     *
     * @param date the input date to convert
     * @return The XMLGregorian Calendar (YYYYmmDD hh:mm:ss:mmm)
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

    /**
     * Converts the @link{XMLGregorianCalendar} received to a @link{Date}
     *
     * @param xmlGregorianCalendar the input date to convertDateConverterTest
     * @return The converted date
     */
    public Date toDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }
}
