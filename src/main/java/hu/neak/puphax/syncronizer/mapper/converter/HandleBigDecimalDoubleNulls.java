package hu.neak.puphax.syncronizer.mapper.converter;

import java.math.BigDecimal;

import org.modelmapper.AbstractConverter;

public class HandleBigDecimalDoubleNulls extends AbstractConverter<BigDecimal, Double> {
	private static final BigDecimal VALUE_NULL = BigDecimal.valueOf(999999999.999999);
	
	@Override
	protected Double convert(BigDecimal source) {
		if(source.equals(VALUE_NULL)) {
			return null;
		}
		return source.doubleValue();
	}

}
