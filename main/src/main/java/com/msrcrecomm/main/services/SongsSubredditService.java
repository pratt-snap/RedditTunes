package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.SongsSubreddit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

@Service
public class SongsSubredditService {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean isSubredditProcessed(String subredditId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

        Root<SongsSubreddit> root = criteriaQuery.from(SongsSubreddit.class);
        Predicate subredditIdCondition = criteriaBuilder.equal(root.get("id").get("subredditId"), subredditId);

        criteriaQuery.select(criteriaBuilder.count(root)).where(subredditIdCondition);

        TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
        Long count = typedQuery.getSingleResult();

        return count > 0;
    }


}
