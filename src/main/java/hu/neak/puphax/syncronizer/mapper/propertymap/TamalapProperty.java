package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXTamalap;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJTAMOGATType.OBJTAMOGAT;

public class TamalapProperty extends PropertyMap<OBJTAMOGAT, PUPHAXTamalap> {

	@Override
	protected void configure() {
		map(source.getID()).setIdTamalap(null);
		map(source.getERVKEZD()).setErvKezd(null);
		map(source.getERVVEGE()).setErvVege(null);
		map(source.getMAXFAB()).setMaxfab(null);
		map(source.getPRASTERMEK()).setPrasTermek(null);
		map(source.getKESTTERM()).setKestTerm(null);
		map(source.getNICHEID()).setNicheId(null);
		map(source.getTERMEKID()).setTermekId(null);
	}

}
