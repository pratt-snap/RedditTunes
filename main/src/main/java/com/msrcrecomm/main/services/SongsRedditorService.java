package com.msrcrecomm.main.services;

import com.msrcrecomm.main.entity.SongsRedditor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SongsRedditorService {

    private static final Logger logger = LoggerFactory.getLogger(SongsRedditorService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public boolean isRedditorProcessed(String userId) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<SongsRedditor> root = criteriaQuery.from(SongsRedditor.class);
            Predicate userIdCondition = criteriaBuilder.equal(root.get("id").get("redditorId"), userId);
            criteriaQuery.select(criteriaBuilder.count(root)).where(userIdCondition);
            TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
            Long count = typedQuery.getSingleResult();

            return count > 0;
        }
        catch (Exception e) {
            logger.error("An error occurred while checking if redditor {} is processed: {}", userId, e.getMessage());
            return false;
        }
    }

    public Long numberSongsProcessed(String userId) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<SongsRedditor> root = criteriaQuery.from(SongsRedditor.class);
            Predicate userIdCondition = criteriaBuilder.equal(root.get("id").get("redditorId"), userId);
            criteriaQuery.select(criteriaBuilder.count(root)).where(userIdCondition);
            TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
            Long count = typedQuery.getSingleResult();

            return count;
        }
        catch (Exception e) {
            logger.error("An error occurred while counting songs processed for redditor {} {}", userId, e.getMessage());
            return Long.valueOf(0);
        }
    }
}

