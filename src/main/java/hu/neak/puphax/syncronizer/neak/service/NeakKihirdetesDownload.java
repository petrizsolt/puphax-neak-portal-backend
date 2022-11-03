package hu.neak.puphax.syncronizer.neak.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import hu.neak.puphax.syncronizer.exceptions.TooManyTryException;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKihirdetes;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXKihirdetesRepository;
import hu.neak.puphax.syncronizer.utils.DateUtil;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJKIHIRDKIHIRDInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.KIHIRDOutput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKIHIRDELEMIntType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NeakKihirdetesDownload {
	private final PUPHAXKihirdetesRepository kihirdetesRepo;
	private final NeakClient neakClient;
	
	@Value("${download.try.count:5}")
	private Integer tryCount;

	@Autowired
	public NeakKihirdetesDownload(PUPHAXKihirdetesRepository kihirdetesRepo, NeakClient neakClient) {
		this.kihirdetesRepo = kihirdetesRepo;
		this.neakClient = neakClient;
	}
	
	public void downloadFreshKihirdetes() {
		log.info("Truncate table PUPHAX_KIHIRDETES");
		kihirdetesRepo.turncate();
		
		PUPHAXKihirdetes kihirdetes = donwloadLatestKihird();
		if(kihirdetes == null) {
			kihirdetes = new PUPHAXKihirdetes();
			kihirdetes.setIdKihirdetes(-2L);
		}
		
		if(GeneratedIds.isSuccessUpdate()) {
			log.info("PUPHAX_KIHIRDETES done! donloaded kihirdDate: " + kihirdetes.getErvDatum());
		} else {
			log.info("PUPHAX update failed!");
			kihirdetes.setIdKihirdetes(-2L);
		}
		
		kihirdetesRepo.save(kihirdetes);
	}
	
	public boolean requiresUpdate() {
		Optional<PUPHAXKihirdetes> latestDbKihird = kihirdetesRepo.findFirstByOrderByErvDatumDesc();
		if(!latestDbKihird.isPresent()) {
			log.info("Kihirdetes not found in database! requires update!");
			return true;
		}

		PUPHAXKihirdetes latestNeakKihird = donwloadLatestKihird();
		
		if(latestNeakKihird.getErvDatum().isAfter(latestDbKihird.get().getErvDatum()) && 
				"E".equals(latestNeakKihird.getStatus()) ) {
			log.info("New puphax version found! database version: " + latestDbKihird.get().getErvDatum() + " neak_version: " 
						+ latestNeakKihird.getErvDatum());
			return true;
		}
		
		log.info("Database puphax version is up to date! database version: " + latestDbKihird.get().getErvDatum() + " neak_version: " 
				+ latestNeakKihird.getErvDatum() + " Status: " + latestNeakKihird.getStatus());
		return false;
	}

	private PUPHAXKihirdetes donwloadLatestKihird() {
		int minusMonths = 1;
		COBJKIHIRDKIHIRDInput params = new COBJKIHIRDKIHIRDInput();
		LocalDate date = LocalDate.now().minusMonths(minusMonths);

		params.setDSTARTDATEIN(DateUtil.getXmlGregorianByLocalDate(date));
		NeakDownloadService<COBJKIHIRDKIHIRDInput, KIHIRDOutput> dw = new NeakDownloadService<>();
		KIHIRDOutput out = dw.download(params, tryCount, neakClient.getNeakClient()::kihird);
		
		while(out.getRETURN().getOBJKIHIRD().getKIHIRDETESEK().getOBJKIHIRDELEM().isEmpty()) {
			minusMonths++;
			date = LocalDate.now().minusMonths(minusMonths);
			log.info("Kihirdetes not found! new start date: " + date);
			params.setDSTARTDATEIN(DateUtil.getXmlGregorianByLocalDate(date));

			out = dw.download(params, tryCount, neakClient.getNeakClient()::kihird);
			if(minusMonths > 24) {
				throw new TooManyTryException("KIHIRDETES_DATE", tryCount);
			}
		}
		List<PUPHAXKihirdetes> kihirdetesek = new ArrayList<>();
		LocalDateTime nowTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		
		for(OBJKIHIRDELEMIntType kih: out.getRETURN().getOBJKIHIRD().getKIHIRDETESEK().getOBJKIHIRDELEM()) {
			PUPHAXKihirdetes kihirdetes = Mapper.map(kih, PUPHAXKihirdetes.class);
			kihirdetes.setIdKihirdetes(-1L);
			kihirdetes.setPublished(nowTime);
			kihirdetesek.add(kihirdetes);
		}
		
		Collections.sort(kihirdetesek);
		return kihirdetesek.get(0);
	}
}
