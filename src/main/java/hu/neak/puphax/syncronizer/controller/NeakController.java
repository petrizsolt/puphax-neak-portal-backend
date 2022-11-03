package hu.neak.puphax.syncronizer.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

import hu.neak.puphax.syncronizer.neak.service.NeakAtcDownload;
import hu.neak.puphax.syncronizer.neak.service.NeakBnoUpdate;
import hu.neak.puphax.syncronizer.neak.service.NeakEupoint;
import hu.neak.puphax.syncronizer.neak.service.NeakIsoKonyvDownload;
import hu.neak.puphax.syncronizer.neak.service.NeakKihirdetesDownload;
import hu.neak.puphax.syncronizer.neak.service.NeakNicheDownload;
import hu.neak.puphax.syncronizer.neak.service.NeakOrvosokUpdate;
import hu.neak.puphax.syncronizer.neak.service.NeakProductsDownload;
import hu.neak.puphax.syncronizer.neak.service.NeakSzakvkodokUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/rest/neak")
public class NeakController {
	private final NeakProductsDownload neakUpdate;
	private final NeakBnoUpdate neakBno;
	private final NeakSzakvkodokUpdate neakSzakvkodok;
	private final NeakOrvosokUpdate neakOrvosok;
	private final NeakAtcDownload atcDownload;
	private final NeakIsoKonyvDownload isoKonyvDownload;
	private final NeakNicheDownload nicheDownload;
	private final NeakKihirdetesDownload kihirdDownload;

	@Autowired
	public NeakController(NeakProductsDownload neakUpdate, NeakBnoUpdate neakBno, 
			NeakSzakvkodokUpdate neakSzakvkodok, NeakAtcDownload atcDownload,
			NeakOrvosokUpdate neakOrvosok, NeakIsoKonyvDownload isoKonyvDownload,
			NeakNicheDownload nicheDownload, NeakKihirdetesDownload kihirdDownload) {
		this.neakUpdate = neakUpdate;
		this.neakBno = neakBno;
		this.neakSzakvkodok = neakSzakvkodok;
		this.neakOrvosok = neakOrvosok;
		this.atcDownload = atcDownload;
		this.isoKonyvDownload = isoKonyvDownload;
		this.nicheDownload = nicheDownload;
		this.kihirdDownload = kihirdDownload;
	}

	
	@GetMapping("/download/products/supported")
	public void downloadSupportedProducts() {
		GeneratedIds.initUpdate();
		neakUpdate.downloadProducts(true);
	}
	
	@GetMapping("/download/products/unsupported")
	public void downloadUnSupportedProducts() {
		GeneratedIds.initUpdate();
		neakUpdate.downloadProducts(false);
	}

	@GetMapping("/download/bnokodok")
	public void downloadBnokodok() {
		GeneratedIds.initUpdate();
		neakBno.downloadBnokodok();
	}

	@GetMapping("/download/orvosok")
	public void downloadOrvosok() {
		GeneratedIds.initUpdate();
		neakOrvosok.downloadOrvosok();
	}
	
	@GetMapping("/download/szakvkodok")
	public void downloadSzakvkodok() {
		GeneratedIds.initUpdate();
		neakSzakvkodok.downloadSzakvkodok();
	}

	@GetMapping("/download/atckonyv")
	public void downloadAtckonyv() {
		GeneratedIds.initUpdate();
		atcDownload.downloadAllAtcKonyv();
	}
	
	@GetMapping("/download/isokonyv")
	public void downloadIsoKonyv() {
		GeneratedIds.initUpdate();
		isoKonyvDownload.donwloadAllIsoKonyv();
	}
	
	@GetMapping("/download/niche")
	public void downloadNiche() {
		GeneratedIds.initUpdate();
		nicheDownload.downloadAllNiche();
	}
	
	@GetMapping("/download/kihirdetes")
	public void downloadKihirdetes() {
		GeneratedIds.initUpdate();
		kihirdDownload.downloadFreshKihirdetes();
	}
	
	@GetMapping("/requires/update")
	public ResponseEntity<Boolean> requiresUpdatePuphax() {
		GeneratedIds.initUpdate();
		return ResponseEntity.ok(kihirdDownload.requiresUpdate());
	}
	
	@GetMapping("/test/webservice")
	public List<OBJKODTABLAIntType> testWebservice() throws UnsupportedEncodingException {
		GeneratedIds.initUpdate();
		return atcDownload.testNeakWebservice();
	}

	@GetMapping("/download/full/puphax")	
	public void downloadAll() {
		long start = System.currentTimeMillis();
		GeneratedIds.initUpdate();
		
		neakBno.downloadBnokodok();
		neakSzakvkodok.downloadSzakvkodok();
		neakOrvosok.downloadOrvosok();
		atcDownload.downloadAllAtcKonyv();
		isoKonyvDownload.donwloadAllIsoKonyv();
		nicheDownload.downloadAllNiche();

		
		neakUpdate.downloadProducts(true);
		neakUpdate.downloadProducts(false);
		
		kihirdDownload.downloadFreshKihirdetes();
		GeneratedIds.initUpdate();
		NeakEupoint.resetEupointIdTemp();

		long end = System.currentTimeMillis();
		float msec = end - start;
		float sec = msec / 1000F;
		float minutes = sec / 60F;
		log.info("PUPHAX total update finished in: " + minutes + " minutes");
	}
}
