package com.ft.service;

import com.ft.domain.Whitelist;
import com.ft.repository.WhitelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Whitelist.
 */
@Service
@Transactional
public class WhitelistService {

    private final Logger log = LoggerFactory.getLogger(WhitelistService.class);

    private final WhitelistRepository whitelistRepository;

    public WhitelistService(WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    /**
     * Save a whitelist.
     *
     * @param whitelist the entity to save
     * @return the persisted entity
     */
    public Whitelist save(Whitelist whitelist) {
        log.debug("Request to save Whitelist : {}", whitelist);        return whitelistRepository.save(whitelist);
    }

    /**
     * Get all the whitelists.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Whitelist> findAll(Pageable pageable) {
        log.debug("Request to get all Whitelists");
        return whitelistRepository.findAll(pageable);
    }


    /**
     * Get one whitelist by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<Whitelist> findOne(Long id) {
        log.debug("Request to get Whitelist : {}", id);
        return whitelistRepository.findById(id);
    }

    /**
     * Delete the whitelist by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Whitelist : {}", id);
        whitelistRepository.deleteById(id);
    }
}
