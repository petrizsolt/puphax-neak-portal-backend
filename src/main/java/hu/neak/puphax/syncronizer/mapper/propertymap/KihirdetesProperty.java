package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXKihirdetes;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKIHIRDELEMIntType;

public class KihirdetesProperty extends PropertyMap<OBJKIHIRDELEMIntType, PUPHAXKihirdetes> {

	@Override
	protected void configure() {
		map(source.getELETBELEP()).setErvDatum(null);
		map(source.getELETBELEP()).setKeszDatum(null);
		map(source.getVER()).setMunkaver(null);
	}

}
