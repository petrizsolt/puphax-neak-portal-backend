package hu.neak.puphax.syncronizer.neak.service;

import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.enable.scheduling")
public class ScheduledTasks {
	private final NeakProductsDownload neakUpdate;
	private final NeakBnoUpdate neakBno;
	private final NeakSzakvkodokUpdate neakSzakvkodok;
	private final NeakOrvosokUpdate neakOrvosok;
	private final NeakAtcDownload atcDownload;
	private final NeakIsoKonyvDownload isoKonyvDownload;
	private final NeakNicheDownload nicheDownload;
	private final NeakKihirdetesDownload kihirdDownload;

	@Autowired
	public ScheduledTasks(NeakProductsDownload neakUpdate, NeakBnoUpdate neakBno, 
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
	
	@Scheduled(cron = "${puphax.update.timer}", zone = "Europe/Budapest")
	private void updatePuphaxShuddled() { 
		if(!kihirdDownload.requiresUpdate()) {
			return;
		}
		log.info("PUPHAX automatic update started...");
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
