package hu.neak.puphax.syncronizer.neak.service;

import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXNiche;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABNICHEInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABNICHEOutput;

import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXNicheRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakNicheDownload {
	private final PUPHAXNicheRepository nicheRepo;
	private final NeakClient neakClient;
	
	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakNicheDownload(PUPHAXNicheRepository nicheRepo, NeakClient neakClient) {
		this.nicheRepo = nicheRepo;
		this.neakClient = neakClient;
	}
	

	public void downloadAllNiche() {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		
		log.info("Truncate table PUPHAX_NICHE");
		nicheRepo.turncate();
		COBJALAPTABNICHEInput params = new COBJALAPTABNICHEInput();
		params.setSXFILTERVARCHAR2IN(Alapfilter.EMPTY_FILTER());
		
		NeakDownloadService<COBJALAPTABNICHEInput, TABNICHEOutput> dw = new NeakDownloadService<>();
		TABNICHEOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::tabniche);
		
		if(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
			return;
		}
		log.info("Downloaded NICHE size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
		List<PUPHAXNiche> nicheList = Mapper.mapAll(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA(),
				PUPHAXNiche.class);
		nicheRepo.saveAll(nicheList);
		
		if(nicheRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("NICHE tábla letöltése sikertelen!");
		}
		
		log.info("NICHE download done!");
	}
}
