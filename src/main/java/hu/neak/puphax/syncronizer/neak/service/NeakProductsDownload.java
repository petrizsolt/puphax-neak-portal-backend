package hu.neak.puphax.syncronizer.neak.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJIDLISTATERMEKLISTAInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJTAMOGATTAMOGATADATInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJTERMEKADATTERMEKADATInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKATEGTAMIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJSTRING256IntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TAMOGATADATOutput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TERMEKADATOutput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TERMEKLISTAOutput;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnokodok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKategtam;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXTamalap;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXTermek;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXBnokodokRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXKategtamRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXSzakvkodokRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXTamalapRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXTermekRepository;
import hu.neak.puphax.syncronizer.utils.DateUtil;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import hu.neak.puphax.syncronizer.utils.PuphaxConvert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakProductsDownload {
	private final NeakClient neakClient;
	private final PUPHAXTermekRepository termekRepo;
	private final PUPHAXTamalapRepository tamalapRepo;
	private final PUPHAXKategtamRepository kategtamRepo;
	private final PUPHAXBnokodokRepository bnoRepo;
	private final PUPHAXSzakvkodokRepository szakvkodokRepo;
	private final NeakEupoint neakEupoint;
	private final HashMap<String, Long> bnokodok = new HashMap<>();
	private final HashMap<String, Long> szakvkodok = new HashMap<>();
	private static final int PAGE_SIZE = 100;
	private static final String NULL_FIELD = "999999999.999999";

	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakProductsDownload(NeakClient neakClient, PUPHAXTermekRepository termekRepo,
			PUPHAXTamalapRepository tamalapRepo, PUPHAXKategtamRepository kategtamRepo, NeakEupoint neakEupoint,
			PUPHAXBnokodokRepository bnoRepo, PUPHAXSzakvkodokRepository szakvkodokRepo) {
		this.neakClient = neakClient;
		this.termekRepo = termekRepo;
		this.tamalapRepo = tamalapRepo;
		this.kategtamRepo = kategtamRepo;
		this.neakEupoint = neakEupoint;
		this.bnoRepo = bnoRepo;
		this.szakvkodokRepo = szakvkodokRepo;
	}

	private void setBnokodokFromDb() {
		List<PUPHAXBnokodok> bnoList = bnoRepo.findAll();
		bnoList.forEach(bno -> bnokodok.put(bno.getKod(), bno.getIdBnokodok()));
	}

	private void setSzakvkodokFromDb() {
		List<PUPHAXSzakvkodok> szakvkodokList = szakvkodokRepo.findAll();
		szakvkodokList.forEach(szakv -> szakvkodok.put(String.valueOf(szakv.getKod()), szakv.getIdSzakvkodok()));
	}

	private void initSupported() {
		setBnokodokFromDb();
		setSzakvkodokFromDb();
		NeakEupoint.resetEupointIdTemp();
		termekRepo.turncateSupportedTables();
	}

	public void downloadProducts(boolean supported) {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		Alapfilter filter = new Alapfilter();

		int page = 1;

		filter.put("LAPOZAS", PuphaxConvert.buildPagingData(page, PAGE_SIZE));
		if(supported) {
			initSupported();
			filter.put("TERMKOD", "TAMTERM");
		} else {
			filter.put("TERMKOD", "NEMTAM");
		}

		//NEMTAM , TAMTERM
		COBJIDLISTATERMEKLISTAInput params = new COBJIDLISTATERMEKLISTAInput();
		params.setSXFILTERVARCHAR2IN(filter.toXML());
		params.setDSPDATEIN(DateUtil.getXmlGregorianNextMonth());
		NeakDownloadService<COBJIDLISTATERMEKLISTAInput, TERMEKLISTAOutput> dw = new NeakDownloadService<>();
		while (true) {
			TERMEKLISTAOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::termeklista);
			List<OBJSTRING256IntType> idList = out.getRETURN().getOBJIDLISTA().getIDLIST().getOBJSTRING256();
			if (idList.size() == 1 && NULL_FIELD.equals(idList.get(0).getSZOVEG())) {
				log.info("TERMEK LISTA: " + idList);
				log.info("Last page found! Download has been stopped!");
				break;
			}
			List<Thread> threads = new ArrayList<>();
			threads.add(new Thread( () ->   downloadTermekDat(idList)));

			if(supported) {
				threads.add(new Thread( () -> downloadTamDat(idList)));
			}
			log.info("Downloaded  " + (supported ? "supported" : "unsupported") + " products size: " + idList.size());
			
			threads.forEach(Thread::start);
			threads.forEach(t -> {
				try {
					t.join();
				} catch (InterruptedException e) {
					log.error(e.getMessage());
					Thread.currentThread().interrupt();
					throw new RuntimeException(e.getMessage());
				}
			});
			
			page += PAGE_SIZE;
			filter.put("LAPOZAS", PuphaxConvert.buildPagingData(page, PAGE_SIZE));
			params.setSXFILTERVARCHAR2IN(filter.toXML());
			log.info("TERMEK_LISTA PARAMS: " + params.getSXFILTERVARCHAR2IN());
		}
		
		if(supported) {
			checkTamDatDownloaded();
		}
		
		if(termekRepo.count() < 10) {
			log.info("Termek table size less than 10!");
			GeneratedIds.unsuccess();
		}
	}
	
	private void checkTamDatDownloaded() {
		neakEupoint.checkDownloadedSize();
		
		if(tamalapRepo.count() < 10 || kategtamRepo.count() < 10) {
			log.info("Tamalap or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		} 
	}
	
	private void downloadTermekDat(List<OBJSTRING256IntType> idList) {
		List<PUPHAXTermek> termekek = new ArrayList<>();
		NeakDownloadService<COBJTERMEKADATTERMEKADATInput, TERMEKADATOutput> dw = new NeakDownloadService<>();
		for (OBJSTRING256IntType id : idList) {
			COBJTERMEKADATTERMEKADATInput params = new COBJTERMEKADATTERMEKADATInput();
			params.setNIDNUMBERIN(new BigDecimal(id.getSZOVEG()));
			TERMEKADATOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::termekadat);
			PUPHAXTermek termek = Mapper.map(out.getRETURN().getOBJTERMEKADAT(), PUPHAXTermek.class);
			termekek.add(termek);
		}
		log.info("Saved termek size: " + termekek.size());
		termekRepo.saveAll(termekek);
	}
	

	private void downloadTamDat(List<OBJSTRING256IntType> idList) {
		List<PUPHAXTamalap> tamalapok = new ArrayList<>();
		List<PUPHAXKategtam> kategtamok = new ArrayList<>();
		NeakDownloadService<COBJTAMOGATTAMOGATADATInput, TAMOGATADATOutput> dw = new NeakDownloadService<>();
		
		for (OBJSTRING256IntType id : idList) {
			COBJTAMOGATTAMOGATADATInput params = new COBJTAMOGATTAMOGATADATInput();
			params.setDSPDATEIN(DateUtil.getXmlGregorianNextMonth());
			params.setNIDNUMBERIN(new BigDecimal(id.getSZOVEG()));
			TAMOGATADATOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::tamogatadat);
			
			PUPHAXTamalap tamalap = Mapper.map(out.getRETURN().getOBJTAMOGAT(), PUPHAXTamalap.class);
			log.info("Tamalap downloaded with id: " + tamalap.getIdTamalap());
			tamalapok.add(tamalap);
		
			for (OBJKATEGTAMIntType kategtam : out.getRETURN().getOBJTAMOGAT().getTAMOGATASOK().getOBJKATEGTAM()) {
				PUPHAXKategtam mappedKateg = Mapper.mapKategtam(kategtam, tamalap.getIdTamalap());
				kategtamok.add(mappedKateg);
				log.info("KATEGTAM downloaded with id: " + mappedKateg.getIdKategtam());
				downloadEupoints(kategtam);
			}
		}
		tamalapRepo.saveAll(tamalapok);
		kategtamRepo.saveAll(kategtamok);
	}

	private void downloadEupoints(OBJKATEGTAMIntType kategtam) {
		if (kategtam.getEUPONTAZON().getOBJSTRING256().isEmpty()) {
			return;
		}
		log.info("EUPOINTS Download started!");
		for (OBJSTRING256IntType euId : kategtam.getEUPONTAZON().getOBJSTRING256()) {
			neakEupoint.downloadEupoint(euId.getSZOVEG(), kategtam.getID(), bnokodok, szakvkodok);
		}
	}
}
