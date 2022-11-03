package hu.neak.puphax.syncronizer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {
	public static void doNothing(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static LocalDateTime toLocalDateTimeAddedOffset(String time) {
		if(time == null)
			return null;
		
		if(time.contains("T") && time.contains("+"))  {
			OffsetDateTime offsetTime = OffsetDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			return offsetTime.toLocalDateTime().plusSeconds(offsetTime.getOffset().getTotalSeconds());
		} else if(time.contains("T")) {
			return LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}
		
		LocalDate localDate = LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE);
		return LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
	}
	
	public static XMLGregorianCalendar getXmlGregorianNow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
		try {
			date = df.parse(LocalDate.now().toString());
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        try {
			return DatatypeFactory.newInstance()
					.newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), 
							gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH),
							DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage());
		}       
	}
	
	public XMLGregorianCalendar getXmlGregorianNextMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
		try {
			date = df.parse(LocalDate.now().plusMonths(1).toString());
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        try {
			return DatatypeFactory.newInstance()
					.newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), 
							gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH),
							DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage());
		}  
	}
	
	public static XMLGregorianCalendar getXmlGregorianByLocalDate(LocalDate reqDate) {
		System.out.println("Date to convert: " + reqDate);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
		try {
			date = df.parse(reqDate.toString());
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage());
		}
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        
        try {
			return DatatypeFactory.newInstance()
					.newXMLGregorianCalendarDate(gc.get(Calendar.YEAR), 
							gc.get(Calendar.MONTH)+1, gc.get(Calendar.DAY_OF_MONTH),
							DatatypeConstants.FIELD_UNDEFINED);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e.getMessage());
		}       
	}
}
