package com.alemarch.partbin.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alemarch.partbin.dtos.SortParam;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class CriteriaQueryBuilder {
	public static <T> CriteriaQuery<T> build(
			EntityManager entityManager,
			Class<T> entityClass,
			Map<String, Object> filters,
			SortParam sort
	) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);

		List<Predicate> predicates = filters.entrySet().stream()
			.map((Map.Entry<String, Object> entry) -> {
				String[] paths = entry.getKey().split("\\.");
				Path<Object> fieldPath = root.get(paths[0]);
				for (var i = 1; i < paths.length; i++) {
					fieldPath = fieldPath.get(paths[i]);
				}
				return cb.equal(fieldPath, entry.getValue());
			})
			.collect(Collectors.toList());
		cq.where(predicates.toArray(new Predicate[0]));

		if (sort != null && sort.getParam() != null) {
			if (!sort.getAscending()) {
				cq.orderBy(cb.desc(root.get(sort.getParam())));
			} else {
				cq.orderBy(cb.asc(root.get(sort.getParam())));
			}
		}
		return cq;
	}
}
