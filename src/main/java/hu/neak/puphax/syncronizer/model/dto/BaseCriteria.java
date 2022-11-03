package hu.neak.puphax.syncronizer.model.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class BaseCriteria <T> {
	
	private CriteriaBuilder criteriaBuilder;
	
	private CriteriaQuery<T> criteriaQuery;

	private Root<T> root;
}
