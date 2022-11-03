package hu.neak.puphax.syncronizer.repository.puphax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXEujoghozzar;


@Repository
public interface PUPHAXEujoghozzarRepository extends JpaRepository<PUPHAXEujoghozzar, Long> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_EUJOGHOZZAR;", nativeQuery = true)
	void turncate();
}
