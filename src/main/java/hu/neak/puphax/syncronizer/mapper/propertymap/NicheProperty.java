package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXNiche;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class NicheProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXNiche>  {

	@Override
	protected void configure() {
		map(source.getMEGJEGYZ()).setLeiras(null);
		map(source.getKOD()).setIdNiche(null);
		map(source.getELNEVEZ()).setEgyenId(null);
	}

}
