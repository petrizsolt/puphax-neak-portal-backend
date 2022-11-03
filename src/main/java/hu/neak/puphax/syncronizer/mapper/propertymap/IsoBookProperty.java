package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXIsokonyv;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class IsoBookProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXIsokonyv> {

	@Override
	protected void configure() {
		map(source.getKOD()).setIso(null);
		map(source.getELNEVEZ()).setMegnevezes(null);
	}

}
