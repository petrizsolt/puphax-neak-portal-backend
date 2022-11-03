package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXAtckonyv;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

public class AtcKonyvProperty extends PropertyMap<OBJKODTABLAIntType, PUPHAXAtckonyv> {

	@Override
	protected void configure() {
		map(source.getMEGJEGYZ()).setAngol(null);
		map(source.getKOD()).setAtc(null);
		map(source.getELNEVEZ()).setHatoanyag(null);
		map(source.getELNEVEZ()).setMegnev(null);
	}
	
}
