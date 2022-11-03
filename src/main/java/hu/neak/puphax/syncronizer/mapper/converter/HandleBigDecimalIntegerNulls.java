package hu.neak.puphax.syncronizer.mapper.converter;

import java.math.BigDecimal;

import org.modelmapper.AbstractConverter;

public class HandleBigDecimalIntegerNulls extends AbstractConverter<BigDecimal, Integer> {
	private static final BigDecimal VALUE_NULL = BigDecimal.valueOf(999999999.999999);
	
	@Override
	protected Integer convert(BigDecimal source) {
		if(source.equals(VALUE_NULL)) {
			return null;
		}
		return source.intValue();
	}

}
