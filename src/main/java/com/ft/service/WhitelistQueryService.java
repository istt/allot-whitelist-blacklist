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

import com.ft.domain.Whitelist;
import com.ft.domain.*; // for static metamodels
import com.ft.repository.WhitelistRepository;
import com.ft.service.dto.WhitelistCriteria;


/**
 * Service for executing complex queries for Whitelist entities in the database.
 * The main input is a {@link WhitelistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Whitelist} or a {@link Page} of {@link Whitelist} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WhitelistQueryService extends QueryService<Whitelist> {

    private final Logger log = LoggerFactory.getLogger(WhitelistQueryService.class);

    private final WhitelistRepository whitelistRepository;

    public WhitelistQueryService(WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    /**
     * Return a {@link List} of {@link Whitelist} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Whitelist> findByCriteria(WhitelistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Whitelist> specification = createSpecification(criteria);
        return whitelistRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Whitelist} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Whitelist> findByCriteria(WhitelistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Whitelist> specification = createSpecification(criteria);
        return whitelistRepository.findAll(specification, page);
    }

    /**
     * Function to convert WhitelistCriteria to a {@link Specification}
     */
    private Specification<Whitelist> createSpecification(WhitelistCriteria criteria) {
        Specification<Whitelist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Whitelist_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Whitelist_.url));
            }
        }
        return specification;
    }

}
