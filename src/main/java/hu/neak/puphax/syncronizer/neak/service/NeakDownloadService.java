package hu.neak.puphax.syncronizer.neak.service;

import java.util.function.Function;

import hu.neak.puphax.syncronizer.exceptions.TooManyTryException;
import hu.neak.puphax.syncronizer.utils.DateUtil;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NeakDownloadService<REQ, RES> {
	
	public  RES download(REQ req, int counter, Function<REQ, RES> func) {
		if(counter <= 0) {
			GeneratedIds.unsuccess();
			throw new TooManyTryException(func.getClass().getSimpleName(), counter);
		}

		try { 
			RES result = func.apply(req);
			if(result == null) {
				throw new Exception("Retry");
			}
			return result;
		} catch(Exception ex) {
			log.info("Neak client call failed: " + ex.getMessage());
			log.info("Try counter: " + counter);
			DateUtil.doNothing(2);
			return download(req, counter -1, func);
		}

	} 
}
