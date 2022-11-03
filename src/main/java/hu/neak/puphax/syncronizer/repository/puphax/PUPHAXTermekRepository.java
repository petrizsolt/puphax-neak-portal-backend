package hu.neak.puphax.syncronizer.repository.puphax;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXTermek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PUPHAXTermekRepository extends JpaRepository<PUPHAXTermek, Long> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_TERMEK;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_TAMALAP;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_KATEGTAM;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_EUHOZZAR;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_EUPONTOK;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_EUINDIKACIOK;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_BNOHOZZAR;\n"
			+ "TRUNCATE TABLE NEAK_PUPHAX_EUJOGHOZZAR;\n", nativeQuery = true)
	void turncateSupportedTables();
}
