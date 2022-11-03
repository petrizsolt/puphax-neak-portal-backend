package hu.neak.puphax.syncronizer.mapper.converter;

import java.time.LocalDate;

import javax.xml.datatype.XMLGregorianCalendar;

import org.modelmapper.AbstractConverter;

public class GregorianToLocalDate extends AbstractConverter<XMLGregorianCalendar, LocalDate> {
	@Override
	protected LocalDate convert(XMLGregorianCalendar source) {
		return LocalDate.of(source.getYear(), source.getMonth(), source.getDay());
	}
}
