package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnokodok;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class BnokodokProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXBnokodok> {

	@Override
	protected void configure() {
		map(source.getKOD()).setIdBnokodok(null);
		map(source.getMEGJEGYZ()).setLeiras(null);
		map(source.getELNEVEZ()).setKod(null);
	}

}
