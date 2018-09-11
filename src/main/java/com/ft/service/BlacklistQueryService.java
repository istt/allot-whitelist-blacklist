package com.ft.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ft.domain.Blacklist;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.BlacklistRepository;
import com.ft.service.dto.BlacklistCriteria;


/**
 * Service for executing complex queries for Blacklist entities in the database.
 * The main input is a {@link BlacklistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Blacklist} or a {@link Page} of {@link Blacklist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BlacklistQueryService extends QueryService<Blacklist> {

    private final Logger log = LoggerFactory.getLogger(BlacklistQueryService.class);

    private final BlacklistRepository blacklistRepository;

    public BlacklistQueryService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    /**
     * Return a {@link List} of {@link Blacklist} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Blacklist> findByCriteria(BlacklistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Blacklist> specification = createSpecification(criteria);
        return blacklistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Blacklist} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Blacklist> findByCriteria(BlacklistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Blacklist> specification = createSpecification(criteria);
        return blacklistRepository.findAll(specification, page);
    }

    /**
     * Function to convert BlacklistCriteria to a {@link Specification}
     */
    private Specification<Blacklist> createSpecification(BlacklistCriteria criteria) {
        Specification<Blacklist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Blacklist_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Blacklist_.url));
            }
        }
        return specification;
    }

}
