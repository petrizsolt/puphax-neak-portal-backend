package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class SzakvkodokProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXSzakvkodok> {

	@Override
	protected void configure() {
		map(source.getELNEVEZ()).setLeiras(null);
	}

}
