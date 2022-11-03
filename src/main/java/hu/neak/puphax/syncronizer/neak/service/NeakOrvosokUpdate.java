package hu.neak.puphax.syncronizer.neak.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXOrvosok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXOrvosokRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXSzakvkodokRepository;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABORVKEPInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABORVKEPOutput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakOrvosokUpdate {
	private final PUPHAXOrvosokRepository orvosokRepo;
	private final PUPHAXSzakvkodokRepository szakvkodokRepo;
	private final NeakClient neakClient;
	private final HashMap<String, Long> szakvkodok = new HashMap<>();
	
	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakOrvosokUpdate(PUPHAXOrvosokRepository orvosokRepo, NeakClient neakClient,
			PUPHAXSzakvkodokRepository szakvkodokRepo) {
		this.orvosokRepo = orvosokRepo;
		this.neakClient = neakClient;
		this.szakvkodokRepo = szakvkodokRepo;
	}
	
	private void setSzakvkodokFromDb() {
		List<PUPHAXSzakvkodok> szakvkodokList = szakvkodokRepo.findAll();
		szakvkodokList.forEach(szakv -> szakvkodok.put(String.valueOf(szakv.getKod()), szakv.getIdSzakvkodok()));
	}


	public void downloadOrvosok() { 
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		
		setSzakvkodokFromDb();
		log.info("Truncate table PUPHAX_ORVOSOK");
		orvosokRepo.turncate();
		NeakDownloadService<COBJALAPTABORVKEPInput, TABORVKEPOutput> dw = new NeakDownloadService<>();
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				COBJALAPTABORVKEPInput params = new COBJALAPTABORVKEPInput();
				Alapfilter filter = new Alapfilter();
				filter.put("PECSET", String.valueOf(i).concat(String.valueOf(j)).concat("%"));
				params.setSXFILTERVARCHAR2IN(filter.toXML());
				TABORVKEPOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::taborvkep);
				if(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
					continue;
				}
				log.info("Downloaded orvosok size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
				List<PUPHAXOrvosok> allDoc = new ArrayList<>();
				for(OBJKODTABLAIntType orvos: out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA()) {
					PUPHAXOrvosok doc = Mapper.map(orvos, PUPHAXOrvosok.class);
					doc.setEesztAzon(GeneratedIds.getOrvosokEesztAzon());
					GeneratedIds.incOrvosokEesztAzon();
					doc.setSzakvId(szakvkodok.get(orvos.getELNEVEZ()));
					allDoc.add(doc);
				}
				orvosokRepo.saveAll(allDoc);
			}
		}
		
		if(orvosokRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("ORVOSOK tábla letöltése sikertelen!");
		}
		
	}	
}
