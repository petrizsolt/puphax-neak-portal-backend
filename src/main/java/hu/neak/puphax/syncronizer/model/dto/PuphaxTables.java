package hu.neak.puphax.syncronizer.model.dto;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXAtckonyv;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnohozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnokodok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEuhozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEuindikaciok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEujoghozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEupontok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXIsokonyv;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKategtam;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKihirdetes;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXNiche;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXOrvosok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXTamalap;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXTermek;

/**
 * nullos mezőket még nem töltjük le a neaktól (jelenleg nincs rájuk szükség).
 * 
 * @author petri.zsolt
 *
 */
public enum PuphaxTables {
	 TAMALAP(PUPHAXTamalap.class),
	 BNOKODOK(PUPHAXBnokodok.class),
	 ATCKONYV(PUPHAXAtckonyv.class),
	 BNOHOZZAR(PUPHAXBnohozzar.class),
	 TERMEK(PUPHAXTermek.class),
	 KATEGTAM(PUPHAXKategtam.class),
	 NICHE(PUPHAXNiche.class),
	 EUPONTOK(PUPHAXEupontok.class),
	 ISOKONYV(PUPHAXIsokonyv.class),
	 KIHIRDETES(PUPHAXKihirdetes.class),
	 EUJOGHOZZAR(PUPHAXEujoghozzar.class),
	 EUHOZZAR(PUPHAXEuhozzar.class),
	 BRAND(null),
	 KIINTOR(null),
	 SZAKVKODOK(PUPHAXSzakvkodok.class),
	 CEGEK(null),
	 EUINDIKACIOK(PUPHAXEuindikaciok.class),
	 KODSZOTAR(null),
	 ORVOSOK(PUPHAXOrvosok.class);
	
	private Class<?> className;
	
	PuphaxTables(Class<?> className) {
		this.className = className;
	}
	
	public Class<?> getClassName() {
		return this.className;
	}
}
