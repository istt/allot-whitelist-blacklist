package com.ft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ft.config.ApplicationProperties;
import com.ft.domain.Whitelist;
import com.ft.service.WhitelistService;
import com.ft.web.rest.util.HeaderUtil;
import com.ft.web.rest.util.PaginationUtil;
import com.ft.service.dto.DataFileDTO;
import com.ft.service.dto.WhitelistCriteria;
import com.ft.service.WhitelistQueryService;
import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    ApplicationProperties props;

    /**
     * POST  /whitelists : Create a new whitelist.
     *
     * @param whitelist the whitelist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new whitelist, or with status 400 (Bad Request) if the whitelist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/whitelists")
    @PutMapping("/whitelists")
    @Timed
    public ResponseEntity<Whitelist> createWhitelist(@RequestBody Whitelist whitelist) throws URISyntaxException {
        log.debug("REST request to save Whitelist : {}", whitelist);
        Whitelist result = whitelistService.save(whitelist);
        return ResponseEntity.created(new URI("/api/whitelists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, null))
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

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/whitelist-import")
    @Timed
    public ResponseEntity<DataFileDTO> importData(@RequestBody DataFileDTO dataFile) throws URISyntaxException {
        int result = whitelistService.importData(dataFile);
        return ResponseEntity.accepted()
            .headers(HeaderUtil.createEntityUpdateAlert("DataFileDTO", String.valueOf(result)))
                .body(dataFile);
    }

    /**
     * POST  /data-files : Create a new dataFile.
     *
     * @param dataFile the dataFile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dataFile, or with status 400 (Bad Request) if the dataFile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException
     */
    @PutMapping("/whitelist-import")
    @Timed
    public ResponseEntity<DataFileDTO> importData() throws URISyntaxException, IOException {
    	DataFileDTO dataFile = new DataFileDTO();
    	dataFile.setTruncateData(true);
    	dataFile.setDataFileContentType("text");
    	dataFile.setDataFile(FileUtils.readFileToByteArray(new File(props.getWhitelistFilePath())));
        return importData(dataFile);
    }

    /**
     * GET  /sub-msisdn/:id : get the "id" dnd.
     *
     * @param id the id of the dnd to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dnd, or with status 404 (Not Found)
     * @throws JsonProcessingException
     */
    @GetMapping("/whitelist-export")
    @Timed
    public ResponseEntity<byte[]> exportData() throws JsonProcessingException {
    	DataFileDTO file = whitelistService.exportData();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(file.getDataFile()));
    }

    @PostMapping("/whitelist-export")
    @Timed
    public ResponseEntity<DataFileDTO> writeData() throws IOException {
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(whitelistService.writeData()));
    }
}
