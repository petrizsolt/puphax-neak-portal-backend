package hu.neak.puphax.syncronizer.mapper.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.xml.datatype.XMLGregorianCalendar;

import org.modelmapper.AbstractConverter;

public class GregorianToLocalDateTime extends AbstractConverter<XMLGregorianCalendar, LocalDateTime> {

	@Override
	protected LocalDateTime convert(XMLGregorianCalendar source) {
		ZonedDateTime utcZoned = source.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault());
		return  utcZoned.toLocalDateTime();
	}

}
