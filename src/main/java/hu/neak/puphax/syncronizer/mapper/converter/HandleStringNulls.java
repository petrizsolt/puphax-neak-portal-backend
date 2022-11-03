package hu.neak.puphax.syncronizer.mapper.converter;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.AbstractConverter;

public class HandleStringNulls extends AbstractConverter<String, String> {
	private static final List<String> NULL_FIELDS = Arrays.asList("-", "-/", "-/-", "999999999.999999");
	
	@Override
	protected String convert(String source) {
		
		if(source != null && NULL_FIELDS.contains(source)) {
			return null;
		}
		
		return source;
	}

}
