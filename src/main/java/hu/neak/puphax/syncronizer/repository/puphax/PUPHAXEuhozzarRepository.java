package hu.neak.puphax.syncronizer.repository.puphax;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXEuhozzar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface PUPHAXEuhozzarRepository extends JpaRepository<PUPHAXEuhozzar, Long> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_EUHOZZAR;", nativeQuery = true)
	void turncate();
}
