package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ft.domain.Whitelist;
import com.ft.service.WhitelistService;
import com.ft.web.rest.errors.BadRequestAlertException;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.WhitelistCriteria;
import com.ft.service.WhitelistQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Whitelist.
 */
@RestController
@RequestMapping("/api")
public class WhitelistResource {

    private final Logger log = LoggerFactory.getLogger(WhitelistResource.class);

    private static final String ENTITY_NAME = "whitelist";

    private final WhitelistService whitelistService;

    private final WhitelistQueryService whitelistQueryService;

    public WhitelistResource(WhitelistService whitelistService, WhitelistQueryService whitelistQueryService) {
        this.whitelistService = whitelistService;
        this.whitelistQueryService = whitelistQueryService;
    }

    /**
     * POST  /whitelists : Create a new whitelist.
     *
     * @param whitelist the whitelist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new whitelist, or with status 400 (Bad Request) if the whitelist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/whitelists")
    @Timed
    public ResponseEntity<Whitelist> createWhitelist(@Valid @RequestBody Whitelist whitelist) throws URISyntaxException {
        log.debug("REST request to save Whitelist : {}", whitelist);
        if (whitelist.getId() != null) {
            throw new BadRequestAlertException("A new whitelist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Whitelist result = whitelistService.save(whitelist);
        return ResponseEntity.created(new URI("/api/whitelists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /whitelists : Updates an existing whitelist.
     *
     * @param whitelist the whitelist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated whitelist,
     * or with status 400 (Bad Request) if the whitelist is not valid,
     * or with status 500 (Internal Server Error) if the whitelist couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/whitelists")
    @Timed
    public ResponseEntity<Whitelist> updateWhitelist(@Valid @RequestBody Whitelist whitelist) throws URISyntaxException {
        log.debug("REST request to update Whitelist : {}", whitelist);
        if (whitelist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Whitelist result = whitelistService.save(whitelist);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, whitelist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /whitelists : get all the whitelists.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of whitelists in body
     */
    @GetMapping("/whitelists")
    @Timed
    public ResponseEntity<List<Whitelist>> getAllWhitelists(WhitelistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Whitelists by criteria: {}", criteria);
        Page<Whitelist> page = whitelistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/whitelists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /whitelists/:id : get the "id" whitelist.
     *
     * @param id the id of the whitelist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the whitelist, or with status 404 (Not Found)
     */
    @GetMapping("/whitelists/{id}")
    @Timed
    public ResponseEntity<Whitelist> getWhitelist(@PathVariable Long id) {
        log.debug("REST request to get Whitelist : {}", id);
        Optional<Whitelist> whitelist = whitelistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(whitelist);
    }

    /**
     * DELETE  /whitelists/:id : delete the "id" whitelist.
     *
     * @param id the id of the whitelist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/whitelists/{id}")
    @Timed
    public ResponseEntity<Void> deleteWhitelist(@PathVariable Long id) {
        log.debug("REST request to delete Whitelist : {}", id);
        whitelistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
