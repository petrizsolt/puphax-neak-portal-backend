package hu.neak.puphax.syncronizer.repository.puphax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnohozzar;

@Repository
public interface PUPHAXBnohozzarRepository extends JpaRepository<PUPHAXBnohozzar, Integer> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_BNOHOZZAR;", nativeQuery = true)
	void turncate();
}
