package hu.neak.puphax.syncronizer.repository.puphax;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXIsokonyv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PUPHAXIsokonyvRepository extends JpaRepository<PUPHAXIsokonyv, String> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_ISOKONYV;", nativeQuery = true)
	void turncate();
}
