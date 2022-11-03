package hu.neak.puphax.syncronizer.mapper.propertymap;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXTermek;
import org.modelmapper.PropertyMap;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJTERMEKADATType.OBJTERMEKADAT;

public class TermekProperty extends PropertyMap<OBJTERMEKADAT, PUPHAXTermek> {

	@Override
	protected void configure() {
		map(source.getID()).setIdTermek(null);
		map(source.getPARENTID()).setParentId(null);
		map(source.getERVKEZD()).setErvKezd(null);
		map(source.getERVVEGE()).setErvVege(null);
		map(source.getBRANDID()).setBrandId(null);
		map(source.getEGYENID()).setEgyenId(null);
		map(source.getOHATOMENNY()).setOhatoMenny(null);
		map(source.getHATOMENNY()).setHatoMenny(null);
		map(source.getHATOEGYS()).setHatoEgys(null);
		map(source.getKISZMENNY()).setKiszMenny(null);
		map(source.getKISZEGYS()).setKiszEgys(null);
		map(source.getDDDMENNY()).setDddMenny(null);
		map(source.getDDDEGYS()).setDddEgys(null);
		map(source.getDDDFAKTOR()).setDddFaktor(null);
		map(source.getADAGMENNY()).setAdagMenny(null);
		map(source.getADAGEGYS()).setAdagEgys(null);
		map(source.getFORGENGTID()).setForgengtId(null);
		map(source.getFORGALMAZID()).setForgalmazId(null);
	}

}
