package hu.neak.puphax.syncronizer.neak.service;

import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXIsokonyv;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABISOInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABISOOutput;

import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXIsokonyvRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakIsoKonyvDownload {
	private final PUPHAXIsokonyvRepository isokonyvRepo;
	private final NeakClient neakClient;
	
	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakIsoKonyvDownload(PUPHAXIsokonyvRepository isokonyvRepo, NeakClient neakClient) {
		this.isokonyvRepo = isokonyvRepo;
		this.neakClient = neakClient;
	}
	

	public void donwloadAllIsoKonyv() {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		
		log.info("Truncate table PUPHAX_ISOKONYV");
		isokonyvRepo.turncate();
		
		COBJALAPTABISOInput params = new COBJALAPTABISOInput();
		params.setSXFILTERVARCHAR2IN(Alapfilter.EMPTY_FILTER());
		//tabiso
		NeakDownloadService<COBJALAPTABISOInput, TABISOOutput> dw = new NeakDownloadService<>();
		
		TABISOOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::tabiso);
		if(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
			return;
		}
		log.info("Downloaded ISOKONYV size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
		List<PUPHAXIsokonyv> isoBooks = Mapper.mapAll(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA(),
				PUPHAXIsokonyv.class);
		isokonyvRepo.saveAll(isoBooks);
		
		if(isokonyvRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("ISOKONYV tábla letöltése sikertelen!");
		}

		
		log.info("ISOKONYV download done!");
	}
}
