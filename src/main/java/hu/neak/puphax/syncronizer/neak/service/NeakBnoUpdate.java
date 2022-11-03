package hu.neak.puphax.syncronizer.neak.service;

import java.util.ArrayList;
import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnokodok;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXBnokodokRepository;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import hu.neak.puphax.syncronizer.utils.PuphaxConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABBNOInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABBNOOutput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakBnoUpdate {
	private final NeakClient neakClient;
	private final PUPHAXBnokodokRepository bnoRepo;

	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakBnoUpdate(NeakClient neakClient, PUPHAXBnokodokRepository bnoRepo) {
		this.neakClient = neakClient;
		this.bnoRepo = bnoRepo;
	}
	
	public void downloadBnokodok() {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		log.info("Truncate table PUPHAX_BNOKODOK");
		bnoRepo.turncate();
		for(Character c: PuphaxConvert.prefixLetters) {
			List<PUPHAXBnokodok> bnok = new ArrayList<>();
			COBJALAPTABBNOInput params = new COBJALAPTABBNOInput();
	        Alapfilter filter = new Alapfilter();
	        filter.put("BNO", c.toString().concat("%"));
	        params.setSXFILTERVARCHAR2IN(filter.toXML());
			NeakDownloadService<COBJALAPTABBNOInput, TABBNOOutput> dw = new NeakDownloadService<>();
	        
			TABBNOOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::tabbno);
			
            if(out == null || out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
            	continue;
            }
			
        	log.info("Downloaded bnokodok size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
            for(OBJKODTABLAIntType bno : out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA()) {
            	PUPHAXBnokodok bnoMapped = Mapper.mapBnokodok(bno);
            	bnok.add(bnoMapped);
            }
            bnoRepo.saveAll(bnok);
		}
		
		if(bnoRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("BNO tábla letöltése sikertelen!");
		}
		
	}
}
