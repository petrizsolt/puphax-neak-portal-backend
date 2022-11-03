package hu.neak.puphax.syncronizer.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PuphaxConvert {
    public static String buildPagingData(int page, int size) {
    	return page + ":" + size;
    }
    
	public static final List<Character> prefixLetters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    
    public static <T> void normalizePuphaObj( T obj) {
    	Field[] allFields = obj.getClass().getDeclaredFields();
    	
    	System.out.println(allFields);
    }
}
