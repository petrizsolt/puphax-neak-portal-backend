package hu.neak.puphax.syncronizer.neak.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import hu.neak.puphax.syncronizer.filter.Alapfilter;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXAtckonyv;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXAtckonyvRepository;
import hu.neak.puphax.syncronizer.utils.GeneratedIds;
import hu.neak.puphax.syncronizer.utils.Mapper;
import hu.neak.puphax.syncronizer.utils.NeakClient;
import hu.neak.puphax.syncronizer.utils.PuphaxConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.oracle.xmlns.orawsv.puphax.puphaxws.COBJALAPTABATCInput;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.TABATCOutput;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class NeakAtcDownload {
	private final PUPHAXAtckonyvRepository atcRepo;
	private final NeakClient neakClient;

	@Value("${download.try.count}")
	private Integer tryCount;

	@Autowired
	public NeakAtcDownload(PUPHAXAtckonyvRepository atcRepo, NeakClient neakClient) {
		this.atcRepo = atcRepo;
		this.neakClient = neakClient;
	}

	public List<OBJKODTABLAIntType> testNeakWebservice() throws UnsupportedEncodingException {
		COBJALAPTABATCInput params = new COBJALAPTABATCInput();
		Alapfilter filter = new Alapfilter();
		filter.put("ATC", "A02AD01");
		params.setSXFILTERVARCHAR2IN(filter.toXML());
		NeakDownloadService<COBJALAPTABATCInput, TABATCOutput> download = new NeakDownloadService<>();
		TABATCOutput out = download.download(params, tryCount, neakClient.getNeakClient()::tabatc);

		return out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA();
	}

	public void downloadAllAtcKonyv() {
		if(!GeneratedIds.isSuccessUpdate()) {
			log.info("Some table failed, download skipped!");
			return;
		}
		
		log.info("Truncate table PUPHAX_ATCKONYV");
		atcRepo.turncate();
		for (Character c : PuphaxConvert.prefixLetters) {
			COBJALAPTABATCInput params = new COBJALAPTABATCInput();
			Alapfilter filter = new Alapfilter();
			filter.put("ATC", c.toString().concat("%"));
			params.setSXFILTERVARCHAR2IN(filter.toXML());
			NeakDownloadService<COBJALAPTABATCInput, TABATCOutput> download = new NeakDownloadService<>();
			TABATCOutput out = download.download(params, tryCount, neakClient.getNeakClient()::tabatc);

			if (out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().isEmpty()) {
				continue;
			}
			log.info("ATC downloaded size: " + out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA().size());
			List<PUPHAXAtckonyv> downloadedAtc = Mapper
					.mapAll(out.getRETURN().getOBJALAP().getREKORDOK().getOBJKODTABLA(), PUPHAXAtckonyv.class);
			atcRepo.saveAll(downloadedAtc);
		}
		if(atcRepo.count() < 10) {
			GeneratedIds.unsuccess();
			throw new RuntimeException("ATC tábla letöltése sikertelen!");
		}
		log.info("AtcKonyv download complete!");
	}

}
