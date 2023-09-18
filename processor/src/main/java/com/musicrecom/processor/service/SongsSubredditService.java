package com.musicrecom.processor.service;

import com.musicrecom.processor.entity.SongsSubreddit;
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
public class SongsSubredditService {

    private static final Logger logger = LoggerFactory.getLogger(SongsSubredditService.class);
    @PersistenceContext
    private EntityManager entityManager;

    public boolean isSubredditProcessed(String subredditId) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

            Root<SongsSubreddit> root = criteriaQuery.from(SongsSubreddit.class);
            Predicate subredditIdCondition = criteriaBuilder.equal(root.get("id").get("subredditId"), subredditId);

            criteriaQuery.select(criteriaBuilder.count(root)).where(subredditIdCondition);

            TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
            Long count = typedQuery.getSingleResult();

            return count > 0;
        }
        catch (Exception e) {
            logger.error("An error occurred while checking if subreddit {} is processed: {}", subredditId, e.getMessage());
            return false;
        }
    }


}
