package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXOrvosok;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class OrvosokProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXOrvosok> {

	@Override
	protected void configure() {
		map(source.getKOD()).setPecsetkod(null);
	}

}
