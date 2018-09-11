/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ft.service;

import com.ft.AppApp;
import com.ft.domain.Blacklist;
import com.ft.domain.User;
import com.ft.domain.Whitelist;
import com.ft.repository.BlacklistRepository;
import com.ft.repository.WhitelistRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author adebowale a. odunlami
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
@Transactional
public class RegularizationServiceIntTest {
    
    @Autowired
    private WhitelistRepository whitelistRepository;

    @Autowired
    private BlacklistRepository blacklistRepository;
    
    @Autowired
    private RegularizationService regularizationService;


    @Test
    @Transactional
    public void assertThatWhitelistRegularizationWorks() {
        String url1 = "https://www.google.com";
        String url2 = "https://maps.google.com";
        String url3 = "https://mail.yahoo.com";
        String url4 = "https://news.yahoo.com";
        
        Set<String> urlSet = new HashSet<>();
        urlSet.add(url1);
        urlSet.add(url2);
        urlSet.add(url3);
        urlSet.add(url4);
        assertThat(urlSet.size()).isEqualTo(4);
                
        Whitelist whitelist1 = new Whitelist().url(url1);
        Whitelist whitelist2 = new Whitelist().url(url2);
        Whitelist whitelist3 = new Whitelist().url(url3);
        Whitelist whitelist4 = new Whitelist().url(url4);
        
        whitelistRepository.save(whitelist1);
        List<Whitelist> wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(1);
        
        whitelistRepository.save(whitelist2);
        wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(2);
        
        whitelistRepository.save(whitelist3);
        wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(3);
        
        whitelistRepository.save(whitelist4);
        wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(4);
        
        Blacklist blacklist1 = new Blacklist().url(url1);
        blacklistRepository.save(blacklist1);
        List<Blacklist> blist = blacklistRepository.findAll();
        assertThat(blist).isNotNull();
        assertThat(blist.size()).isEqualTo(1);
        
        regularizationService.deleteBlacklistedUrls();
        wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(3);
        
        Blacklist blacklist2 = new Blacklist().url(url2);
        Blacklist blacklist3 = new Blacklist().url(url3);
        blacklistRepository.save(blacklist2);
        blacklistRepository.save(blacklist3);
        blist = blacklistRepository.findAll();
        assertThat(blist).isNotNull();
        assertThat(blist.size()).isEqualTo(3);
        
        regularizationService.deleteBlacklistedUrls();
        wlist = whitelistRepository.findAll();
        assertThat(wlist).isNotNull();
        assertThat(wlist.size()).isEqualTo(1);
       
    }


}
