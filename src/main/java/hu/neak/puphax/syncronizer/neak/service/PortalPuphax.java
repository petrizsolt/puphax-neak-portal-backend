package hu.neak.puphax.syncronizer.neak.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import hu.neak.puphax.syncronizer.model.dto.BaseCriteria;
import hu.neak.puphax.syncronizer.model.dto.CamelIsString;
import hu.neak.puphax.syncronizer.model.dto.PuphaxTables;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKihirdetes;
import hu.neak.puphax.syncronizer.repository.puphax.PUPHAXKihirdetesRepository;
import hu.neak.puphax.syncronizer.utils.DateUtil;
import hu.neak.puphax.syncronizer.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PortalPuphax {
	private final PUPHAXKihirdetesRepository kihirdRepo;


	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public PortalPuphax(PUPHAXKihirdetesRepository kihirdRepo) {
		this.kihirdRepo = kihirdRepo;
	}

	public ResponseEntity<?> getTableWithPage(PuphaxTables table, Integer page, Integer size, String field,
                                              String value) {
		if(table.getClassName() == null ) {
			return ResponseEntity.ok(new ArrayList<>());
		}
		return ResponseEntity.ok(getDatabaseData(page, size, field, value, table.getClassName()));
	}

	public Long getTableSize(PuphaxTables table, String field, String value) {
		if(table.getClassName() == null) {
			return 0L;
		}
		return getDatabaseCount(field, value, table.getClassName());
	}

	public <T> List<T> getDatabaseData(Integer page, Integer size, String field, String value, Class<T> className) {
		Pageable pageOf = PageRequest.of(page, size);
		return getRepositoryData(field, value, pageOf, false, className);
	}

	public <T> Long getDatabaseCount(String field, String value, Class<T> className) {
		T l = getRepositoryData(field, value, null, true, className).get(0);
		return  (Long) l;
	}

	@Transactional
	public String publishVersion() {
		Optional<PUPHAXKihirdetes> latest = kihirdRepo.findFirstByOrderByErvDatumDesc();
		if (!latest.isPresent()) {
			return "PUPHAX version not found!";
		}
		PUPHAXKihirdetes saveKihird = latest.get();
		Long idKihird = toIdKihirdetes(saveKihird.getErvDatum());
		saveKihird.setIdKihirdetes(idKihird);

		kihirdRepo.save(saveKihird);
		DateUtil.doNothing(5);
		return "New PUPHAX version published with id: " + idKihird;
	}

	private Long toIdKihirdetes(LocalDateTime ervDat) {
		try {
			return Long.valueOf(ervDat.toLocalDate().toString().replace("-", ""));
		} catch (Exception e) {
			throw new RuntimeException(
					"A pupha érvényesség ideje nem konvertálható KihirdetésID-ba! ervDat: " + ervDat);
		}
	}

	private Object handleType(Object value, Class<?> type) {
		if (type.equals(LocalDateTime.class)) {
			return DateUtil.toLocalDateTimeAddedOffset(value.toString());
		}
		return value;
	}

	private <T> List<T> getRepositoryData(String field, Object value, Pageable page, boolean justCount,
			Class<T> className) {
		BaseCriteria<T> criteria = getBaseCriteria(className, justCount);
		if (field != null && value != null) {
			log.info("Get Database data with filter: field: " + field + " value: " + value);
			CamelIsString camelField = ReflectionUtils.fieldIsString(field, className);
			value = handleType(value, camelField.getType());

			if (camelField.isString()) {
				criteria.getCriteriaQuery().where(criteria.getCriteriaBuilder()
						.like(criteria.getRoot().get(camelField.getCamelValue()), "%" + value + "%"));
			} else {
				criteria.getCriteriaQuery().where(
						criteria.getCriteriaBuilder().equal(criteria.getRoot().get(camelField.getCamelValue()), value));
			}
		}

		if (justCount) {
			return entityManager.createQuery(criteria.getCriteriaQuery()).getResultList();
		}

		return entityManager.createQuery(criteria.getCriteriaQuery()).setFirstResult((int) page.getOffset())
				.setMaxResults(page.getPageSize()).getResultList();

	}

	@SuppressWarnings("unchecked")
	private <T> BaseCriteria<T> getBaseCriteria(Class<T> className, boolean isCount) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> cq = cb.createQuery(className);
	
		Root<T> root = cq.from(className);

		if (isCount) {
			cq.select((Selection<? extends T>) cb.count(root));
		} else {
			cq.select(root);
		}

		return BaseCriteria.<T>builder().criteriaBuilder(cb).criteriaQuery(cq).root(root).build();
	}
}
