package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXKategtam;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKATEGTAMIntType;

public class KategtamProperty extends PropertyMap<OBJKATEGTAMIntType, PUPHAXKategtam> {

	@Override
	protected void configure() {
		map(source.getID()).setIdKategtam(null);
		map(source.getMINELETKOR()).setMinEletkor(null);
		map(source.getMAXELETKOR()).setMaxEletkor(null);
		map(source.getFIXID()).setFixId(null);
	}

}
