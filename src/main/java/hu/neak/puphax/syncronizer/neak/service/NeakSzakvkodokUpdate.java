package hu.neak.puphax.syncronizer.neak.service;

import java.util.ArrayList;
import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXSzakvkodokRepository;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABOSZAKKEPInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABOSZAKKEPOutput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakSzakvkodokUpdate {
	private final NeakClient neakClient;
	private final PUPHAXSzakvkodokRepository szakvkodokRepo;
	
	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakSzakvkodokUpdate(NeakClient neakClient, PUPHAXSzakvkodokRepository szakvkodokRepo) {
		this.neakClient = neakClient;
		this.szakvkodokRepo = szakvkodokRepo;
	}
	
	public void downloadSzakvkodok() {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		
		log.info("Truncate table PUPHAX_SZAKVKODOK");
		szakvkodokRepo.turncate(); 
		
		COBJALAPTABOSZAKKEPInput params = new COBJALAPTABOSZAKKEPInput();
		params.setSXFILTERVARCHAR2IN(Alapfilter.EMPTY_FILTER());
		long id = 1;
		NeakDownloadService<COBJALAPTABOSZAKKEPInput, TABOSZAKKEPOutput> dw = new NeakDownloadService<>();
		TABOSZAKKEPOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::taboszakkep);
		if(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
			return;
		}
		log.info("Downloaded szakvkodok size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
		List<PUPHAXSzakvkodok> szakvkodok = new ArrayList<>();
		for(OBJKODTABLAIntType sz: out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA()) {
			PUPHAXSzakvkodok mappedSz = Mapper.mapAllSzakvkodok(sz);
			mappedSz.setIdSzakvkodok(id);
			szakvkodok.add(mappedSz);
			id++;
		}
		szakvkodokRepo.saveAll(szakvkodok);
		
		if(szakvkodokRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("SZAKVKODOK tábla letöltése sikertelen!");
		}
		
		log.info("SZAKVKODOK download complete!");
	}
}
