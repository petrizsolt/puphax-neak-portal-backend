package hu.neak.puphax.syncronizer.neak.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXBnohozzarRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXEuhozzarRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXEuindikaciokRepository;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXEujoghozzarRepository;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJEUPONTTAMOGATEUPONTInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJEUPONTType.OBJEUPONT;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJFELIRHATOSAGIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJINDIKACIOIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJSTRING256IntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TAMOGATEUPONTOutput;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnohozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEuhozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEuindikaciok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEujoghozzar;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXEupontok;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXEupontokRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakEupoint {
	private final NeakClient neakClient;
	private final PUPHAXEupontokRepository eupontokRepo;
	private final PUPHAXEuindikaciokRepository euindikaciokRepo;
	private final PUPHAXEujoghozzarRepository eujoghozzarRepo;
	private final PUPHAXBnohozzarRepository bnohozzarRepo;
	private final PUPHAXEuhozzarRepository euhozzarRepo;
	private static Set<String> epointIds = new HashSet<>();
	
	@Value("${download.try.count}")
	private Integer tryCount;
	
	@Autowired
	public NeakEupoint(NeakClient neakClient, PUPHAXEupontokRepository eupontokRepo, 
			PUPHAXEuindikaciokRepository euindikaciokRepo,
			PUPHAXEujoghozzarRepository eujoghozzarRepo, PUPHAXBnohozzarRepository bnohozzarRepo,
			PUPHAXEuhozzarRepository euhozzarRepo) {
		this.eupontokRepo = eupontokRepo;
		this.euindikaciokRepo = euindikaciokRepo;
		this.eujoghozzarRepo = eujoghozzarRepo;
		this.bnohozzarRepo = bnohozzarRepo;
		this.euhozzarRepo = euhozzarRepo;
		this.neakClient = neakClient;
	}
	
	public static void resetEupointIdTemp() {
		epointIds.clear();
	}
	
	public void checkDownloadedSize() {
		if(eupontokRepo.count() < 10) {
			log.info("Eupoint or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		}
		
		if(euindikaciokRepo.count() < 10) {
			log.info("Euindikaciok or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		}
		
		if(eujoghozzarRepo.count() < 10) {
			log.info("Eujoghozzar or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		}
		
		if(bnohozzarRepo.count() < 10) {
			log.info("Bnohozzar or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		}
		
		if(euhozzarRepo.count() < 10) {
			log.info("Euhozzar or Kategtam table size less than 10!");
			GeneratedIds.unsuccess();
		}
	}
	

	public void downloadEupoint(String eupointId, BigDecimal kategtamId,
			Map<String, Long> bnoMap, Map<String, Long> szakvkodok) {
		saveEuhozzar(kategtamId.toString(), eupointId);
		if(NeakEupoint.epointIds.contains(eupointId)) {
			log.info("EUPOINT ALREADY SAVED: " + eupointId);
			return;
		}
		NeakEupoint.epointIds.add(eupointId);
		COBJEUPONTTAMOGATEUPONTInput params = new COBJEUPONTTAMOGATEUPONTInput();
		params.setNIDNUMBERIN(new BigDecimal(eupointId));

		NeakDownloadService<COBJEUPONTTAMOGATEUPONTInput, TAMOGATEUPONTOutput> dw = new NeakDownloadService<>();
		TAMOGATEUPONTOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::tamogateupont);
		OBJEUPONT outEup = out.getRETURN().getOBJEUPONT();
		
		log.info("EUPOINT downloaded id: " + outEup.getID());
		PUPHAXEupontok eupont = Mapper.map(outEup, PUPHAXEupontok.class);
		eupont.setIdEupontok(outEup.getID().longValue());
		eupontokRepo.save(eupont);

		saveEuindikacio(outEup);
		saveBnohozzar(outEup, bnoMap);
		saveEujoghozzar(outEup, szakvkodok);
	}
	
	
	private void saveEujoghozzar(OBJEUPONT outEup, Map<String, Long> szakvkodok) {
		if(outEup.getFELIRHAT().getOBJFELIRHATOSAG().isEmpty()) {
			return;
		}
		List<PUPHAXEujoghozzar> eujoghozzarList = new ArrayList<>();
		
		for(OBJFELIRHATOSAGIntType jog: outEup.getFELIRHAT().getOBJFELIRHATOSAG()) {
			PUPHAXEujoghozzar mapped = Mapper.map(jog, PUPHAXEujoghozzar.class);
			mapped.setEupontId(outEup.getID().longValue());
			mapped.setKategoriaId(jog.getKATEGORIAID().longValue());
			mapped.setJogosultId(jog.getJOGOSULTID().longValue());
			mapped.setSzakvId(szakvkodok.get(jog.getSZAKVKOD().toString()));
			mapped.setKiintId(jog.getKIINTID().longValue());
			mapped.setAzonosito(GeneratedIds.getEujoghozzarId());
			GeneratedIds.incEujoghozzarId();
			eujoghozzarList.add(mapped);
		}
		log.info("Saving generated EUJOGHOZZAR size: " + eujoghozzarList.size());
		eujoghozzarRepo.saveAll(eujoghozzarList);
	}



	private void saveBnohozzar(OBJEUPONT outEup, Map<String, Long> bnoMap) {
		if(outEup.getFELIRBNO().getOBJSTRING256().isEmpty()) {
			return;
		}
		List<PUPHAXBnohozzar> bnohozzarList = new ArrayList<>();
		for(OBJSTRING256IntType bnoCode: outEup.getFELIRBNO().getOBJSTRING256()) {
			PUPHAXBnohozzar hozzar = new PUPHAXBnohozzar();
			hozzar.setEupontId(outEup.getID().longValue());
			hozzar.setAzonosito(GeneratedIds.getBnohozzarId());
			hozzar.setBnoId(bnoMap.get(bnoCode.getSZOVEG()));
			bnohozzarList.add(hozzar);
			GeneratedIds.incBnohozzarId();
		}
		log.info("Saving generated BNOHOZZAR size: " + bnohozzarList.size());
		bnohozzarRepo.saveAll(bnohozzarList);
	}



	private void saveEuindikacio(OBJEUPONT outEup) {
		if(outEup.getINDIKACIOK().getOBJINDIKACIO().isEmpty()) {
			return;
		}
		List<PUPHAXEuindikaciok> indikaciok = new ArrayList<>();
		
		for(OBJINDIKACIOIntType ind: outEup.getINDIKACIOK().getOBJINDIKACIO()) {
			PUPHAXEuindikaciok indikacio = Mapper.map(ind, PUPHAXEuindikaciok.class);
			indikacio.setEupontId(outEup.getID().longValue());	
			indikacio.setIdEuindikaciok(GeneratedIds.getEuindikaciokId());
			GeneratedIds.incEuindikaciokId();
			indikaciok.add(indikacio);
		}
		log.info("Saving EUINDIKACIOK size: " + indikaciok.size());
		euindikaciokRepo.saveAll(indikaciok);

	}

	private void saveEuhozzar(String kategtamId, String eupointId) {
		PUPHAXEuhozzar euhozzar = new PUPHAXEuhozzar();
		euhozzar.setAzonosito(GeneratedIds.getEuhozzarId());
		euhozzar.setEupontId(Long.valueOf(eupointId));
		euhozzar.setKategtamId(Long.valueOf(kategtamId));
		GeneratedIds.incEuhozzarId();
		log.info("Saving generated EUHOZZAR!");
		euhozzarRepo.save(euhozzar);
	}
}
