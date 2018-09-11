/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ft.service;

import com.ft.repository.WhitelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author adebowale a. odunlami
 */
@Service
public class RegularizationService {
    
    private final Logger log = LoggerFactory.getLogger(RegularizationService.class);

    private final WhitelistRepository whitelistRepository;

    public RegularizationService(WhitelistRepository whitelistRepository) {
        this.whitelistRepository = whitelistRepository;
    }

    public void deleteBlacklistedUrls() {
        log.debug("Request to delete all Whitelist urls that are in the Blacklist record");
        whitelistRepository.deleteBlacklistedUrls();
    }
}
