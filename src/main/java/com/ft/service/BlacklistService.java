package com.ft.service;

import com.ft.domain.Blacklist;
import com.ft.repository.BlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Blacklist.
 */
@Service
@Transactional
public class BlacklistService {

    private final Logger log = LoggerFactory.getLogger(BlacklistService.class);

    private final BlacklistRepository blacklistRepository;

    public BlacklistService(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    /**
     * Save a blacklist.
     *
     * @param blacklist the entity to save
     * @return the persisted entity
     */
    public Blacklist save(Blacklist blacklist) {
        log.debug("Request to save Blacklist : {}", blacklist);        return blacklistRepository.save(blacklist);
    }

    /**
     * Get all the blacklists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Blacklist> findAll(Pageable pageable) {
        log.debug("Request to get all Blacklists");
        return blacklistRepository.findAll(pageable);
    }


    /**
     * Get one blacklist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Blacklist> findOne(Long id) {
        log.debug("Request to get Blacklist : {}", id);
        return blacklistRepository.findById(id);
    }

    /**
     * Delete the blacklist by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Blacklist : {}", id);
        blacklistRepository.deleteById(id);
    }
}
