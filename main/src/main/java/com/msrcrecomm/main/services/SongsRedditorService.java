package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.SongsRedditor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

@Service
public class SongsRedditorService {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean isRedditorProcessed(String userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<SongsRedditor> root = criteriaQuery.from(SongsRedditor.class);
        Predicate userIdCondition = criteriaBuilder.equal(root.get("id").get("redditorId"), userId);
        criteriaQuery.select(criteriaBuilder.count(root)).where(userIdCondition);

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        Long count = typedQuery.getSingleResult();

        return count > 0;
    }
}

