package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ft.domain.Blacklist;
import com.ft.service.BlacklistService;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.BlacklistCriteria;
import com.ft.service.BlacklistQueryService;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Blacklist.
 */
@RestController
@RequestMapping("/api")
public class BlacklistResource {

    private final Logger log = LoggerFactory.getLogger(BlacklistResource.class);

    private static final String ENTITY_NAME = "blacklist";

    private final BlacklistService blacklistService;

    private final BlacklistQueryService blacklistQueryService;

    public BlacklistResource(BlacklistService blacklistService, BlacklistQueryService blacklistQueryService) {
        this.blacklistService = blacklistService;
        this.blacklistQueryService = blacklistQueryService;
    }

    /**
     * POST  /blacklists : Create a new blacklist.
     *
     * @param blacklist the blacklist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blacklist, or with status 400 (Bad Request) if the blacklist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blacklists")
    @PutMapping("/blacklists")
    @Timed
    public ResponseEntity<Blacklist> createBlacklist(@Valid @RequestBody Blacklist blacklist) throws URISyntaxException {
        log.debug("REST request to save Blacklist : {}", blacklist);
        Blacklist result = blacklistService.save(blacklist);
        return ResponseEntity.created(new URI("/api/blacklists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, null))
            .body(result);
    }

    /**
     * GET  /blacklists : get all the blacklists.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of blacklists in body
     */
    @GetMapping("/blacklists")
    @Timed
    public ResponseEntity<List<Blacklist>> getAllBlacklists(BlacklistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Blacklists by criteria: {}", criteria);
        Page<Blacklist> page = blacklistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blacklists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blacklists/:id : get the "id" blacklist.
     *
     * @param id the id of the blacklist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blacklist, or with status 404 (Not Found)
     */
    @GetMapping("/blacklists/{id}")
    @Timed
    public ResponseEntity<Blacklist> getBlacklist(@PathVariable Long id) {
        log.debug("REST request to get Blacklist : {}", id);
        Optional<Blacklist> blacklist = blacklistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blacklist);
    }

    /**
     * DELETE  /blacklists/:id : delete the "id" blacklist.
     *
     * @param id the id of the blacklist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blacklists/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlacklist(@PathVariable Long id) {
        log.debug("REST request to delete Blacklist : {}", id);
        blacklistService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blacklist-import")
    @Timed
    public ResponseEntity<DataFileDTO> importData(@RequestBody DataFileDTO dataFile) throws URISyntaxException {
        int result = blacklistService.importData(dataFile);
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createEntityUpdateAlert("DataFileDTO", String.valueOf(result)))
                .body(dataFile);
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     * @throws JsonProcessingException
     */
    @GetMapping("/blacklist-export")
    @Timed
    public ResponseEntity<byte[]> exportData() throws JsonProcessingException {
    	DataFileDTO file = blacklistService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file.getDataFile()));
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     * @throws IOException
     */
    @PostMapping("/blacklist-export")
    @Timed
    public ResponseEntity<DataFileDTO> exportToFile() throws IOException {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blacklistService.writeData()));
    }
}
