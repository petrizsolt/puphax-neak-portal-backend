package hu.neak.puphax.syncronizer.utils;

import java.lang.reflect.Field;

import com.google.common.base.CaseFormat;

import hu.neak.puphax.syncronizer.model.dto.CamelIsString;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {
	public static <T> CamelIsString fieldIsString(String snakeField, Class<T> className) {
		String camelField = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, snakeField);

		try {
			Field f = className.getDeclaredField(camelField);
			f.setAccessible(true);
			Class<?> myType = String.class;
			if(f.getType().isAssignableFrom(myType)) {
				return CamelIsString.builder().isString(true).camelValue(camelField).type(f.getType()).build();
			}
			return CamelIsString.builder().isString(false).camelValue(camelField).type(f.getType()).build();
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	
	
}
