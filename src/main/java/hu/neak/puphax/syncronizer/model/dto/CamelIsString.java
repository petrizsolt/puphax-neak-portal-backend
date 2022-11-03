package hu.neak.puphax.syncronizer.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CamelIsString {
	private boolean isString;
	private Class<?> type;
	private String camelValue;
}
