package hu.neak.puphax.syncronizer.mapper.converter;

import java.math.BigDecimal;

import org.modelmapper.AbstractConverter;

public class HandleBigDecimalLongNulls extends AbstractConverter<BigDecimal, Long> {
	private static final BigDecimal VALUE_NULL = BigDecimal.valueOf(999999999.999999);
	
	@Override
	protected Long convert(BigDecimal source) {
		if(source.equals(VALUE_NULL)) {
			return null;
		}
		return source.longValue();
	}

}
