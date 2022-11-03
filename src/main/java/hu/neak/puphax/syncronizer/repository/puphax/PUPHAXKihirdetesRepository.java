package hu.neak.puphax.syncronizer.repository.puphax;

import java.time.LocalDateTime;
import java.util.Optional;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXKihirdetes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PUPHAXKihirdetesRepository extends JpaRepository<PUPHAXKihirdetes, LocalDateTime> {
	@Transactional
	@Modifying
	@Query(value = "TRUNCATE TABLE NEAK_PUPHAX_KIHIRDETES;", nativeQuery = true)
	void turncate();
	
	Optional<PUPHAXKihirdetes> findFirstByOrderByErvDatumDesc(); 
}
