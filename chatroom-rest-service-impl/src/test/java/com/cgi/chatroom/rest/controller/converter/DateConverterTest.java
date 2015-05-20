package com.cgi.chatroom.rest.controller.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Date converter test class
 */
@RunWith(MockitoJUnitRunner.class)
public class DateConverterTest {

    @Mock
    private XMLGregorianCalendar xmlGregorianCalendarMock;

    @Mock
    private GregorianCalendar gregorianCalendarMock;

    private DateConverter testedClass = new DateConverter();

    @Test
    public void shouldConvertToXMLGregorianCalendarSuccessfully() {
        // Given
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 22);
        calendar.set(Calendar.YEAR, 1988);
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 30);
        calendar.set(Calendar.MILLISECOND, 40);
        // When
        XMLGregorianCalendar xmlCalendar = testedClass.toXMLGregorianCalendar(calendar.getTime());
        // Then
        verifyDate(xmlCalendar, calendar);
        verifyCalendarTimeZoneIsEmpty(xmlCalendar);
    }

    @Test
    public void shouldConvertDateSuccessfully() {
        // Given
        Date expectedDate = new Date();
        given(xmlGregorianCalendarMock.toGregorianCalendar()).willReturn(gregorianCalendarMock);
        given(gregorianCalendarMock.getTimeInMillis()).willReturn(expectedDate.getTime());
        // When
        Date actualDate = testedClass.toDate(xmlGregorianCalendarMock);
        // Then
        assertThat(actualDate, is(expectedDate));
    }

    private void verifyDate(XMLGregorianCalendar xmlCalendar, Calendar expected) {
        GregorianCalendar gregorianCalendar = xmlCalendar.toGregorianCalendar();
        assertThat(gregorianCalendar.get(Calendar.YEAR), is(expected.get(Calendar.YEAR)));
        assertThat(gregorianCalendar.get(Calendar.MONTH), is(expected.get(Calendar.MONTH)));
        assertThat(gregorianCalendar.get(Calendar.YEAR), is(expected.get(Calendar.YEAR)));
        assertThat(gregorianCalendar.get(Calendar.HOUR), is(expected.get(Calendar.HOUR)));
        assertThat(gregorianCalendar.get(Calendar.MINUTE), is(expected.get(Calendar.MINUTE)));
        assertThat(gregorianCalendar.get(Calendar.SECOND), is(expected.get(Calendar.SECOND)));
        assertThat(gregorianCalendar.get(Calendar.MILLISECOND),
                is(expected.get(Calendar.MILLISECOND)));
    }

    private void verifyCalendarTimeZoneIsEmpty(XMLGregorianCalendar calendar) {
        assertThat(calendar.getTimezone(), is(DatatypeConstants.FIELD_UNDEFINED));
    }
}
