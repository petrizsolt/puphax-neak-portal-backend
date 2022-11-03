package hu.neak.puphax.syncronizer.filter;
import java.util.HashMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Alapfilter extends HashMap<String, String> {
	private static final long serialVersionUID = -5871713838515907511L;

	public String toXML() {
		String str = "<alapfilter>" +
                this.entrySet().stream().map(
                        entry -> "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">"
                    ).collect(Collectors.joining("")) +
                    "</alapfilter>";
		log.info(str);
        return str;
    }
	
	
	public static final String EMPTY_FILTER() {
		String str = "<alapfilter></alapfilter>";
		log.info(str);
        return str;
    }
}
