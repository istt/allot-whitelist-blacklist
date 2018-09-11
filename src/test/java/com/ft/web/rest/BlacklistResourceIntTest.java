package com.ft.web.rest;

import com.ft.AppApp;

import com.ft.domain.Blacklist;
import com.ft.repository.BlacklistRepository;
import com.ft.service.BlacklistService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.BlacklistCriteria;
import com.ft.service.BlacklistQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.ArrayList;

import static com.ft.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlacklistResource REST controller.
 *
 * @see BlacklistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class BlacklistResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private BlacklistRepository blacklistRepository;

    

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private BlacklistQueryService blacklistQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlacklistMockMvc;

    private Blacklist blacklist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlacklistResource blacklistResource = new BlacklistResource(blacklistService, blacklistQueryService);
        this.restBlacklistMockMvc = MockMvcBuilders.standaloneSetup(blacklistResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blacklist createEntity(EntityManager em) {
        Blacklist blacklist = new Blacklist()
            .url(DEFAULT_URL);
        return blacklist;
    }

    @Before
    public void initTest() {
        blacklist = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlacklist() throws Exception {
        int databaseSizeBeforeCreate = blacklistRepository.findAll().size();

        // Create the Blacklist
        restBlacklistMockMvc.perform(post("/api/blacklists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blacklist)))
            .andExpect(status().isCreated());

        // Validate the Blacklist in the database
        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeCreate + 1);
        Blacklist testBlacklist = blacklistList.get(blacklistList.size() - 1);
        assertThat(testBlacklist.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createBlacklistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blacklistRepository.findAll().size();

        // Create the Blacklist with an existing ID
        blacklist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlacklistMockMvc.perform(post("/api/blacklists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blacklist)))
            .andExpect(status().isBadRequest());

        // Validate the Blacklist in the database
        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = blacklistRepository.findAll().size();
        // set the field null
        blacklist.setUrl(null);

        // Create the Blacklist, which fails.

        restBlacklistMockMvc.perform(post("/api/blacklists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blacklist)))
            .andExpect(status().isBadRequest());

        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBlacklists() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get all the blacklistList
        restBlacklistMockMvc.perform(get("/api/blacklists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blacklist.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    

    @Test
    @Transactional
    public void getBlacklist() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get the blacklist
        restBlacklistMockMvc.perform(get("/api/blacklists/{id}", blacklist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blacklist.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllBlacklistsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get all the blacklistList where url equals to DEFAULT_URL
        defaultBlacklistShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the blacklistList where url equals to UPDATED_URL
        defaultBlacklistShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllBlacklistsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get all the blacklistList where url in DEFAULT_URL or UPDATED_URL
        defaultBlacklistShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the blacklistList where url equals to UPDATED_URL
        defaultBlacklistShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllBlacklistsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        blacklistRepository.saveAndFlush(blacklist);

        // Get all the blacklistList where url is not null
        defaultBlacklistShouldBeFound("url.specified=true");

        // Get all the blacklistList where url is null
        defaultBlacklistShouldNotBeFound("url.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBlacklistShouldBeFound(String filter) throws Exception {
        restBlacklistMockMvc.perform(get("/api/blacklists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blacklist.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBlacklistShouldNotBeFound(String filter) throws Exception {
        restBlacklistMockMvc.perform(get("/api/blacklists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingBlacklist() throws Exception {
        // Get the blacklist
        restBlacklistMockMvc.perform(get("/api/blacklists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlacklist() throws Exception {
        // Initialize the database
        blacklistService.save(blacklist);

        int databaseSizeBeforeUpdate = blacklistRepository.findAll().size();

        // Update the blacklist
        Blacklist updatedBlacklist = blacklistRepository.findById(blacklist.getId()).get();
        // Disconnect from session so that the updates on updatedBlacklist are not directly saved in db
        em.detach(updatedBlacklist);
        updatedBlacklist
            .url(UPDATED_URL);

        restBlacklistMockMvc.perform(put("/api/blacklists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBlacklist)))
            .andExpect(status().isOk());

        // Validate the Blacklist in the database
        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeUpdate);
        Blacklist testBlacklist = blacklistList.get(blacklistList.size() - 1);
        assertThat(testBlacklist.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingBlacklist() throws Exception {
        int databaseSizeBeforeUpdate = blacklistRepository.findAll().size();

        // Create the Blacklist

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlacklistMockMvc.perform(put("/api/blacklists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blacklist)))
            .andExpect(status().isBadRequest());

        // Validate the Blacklist in the database
        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBlacklist() throws Exception {
        // Initialize the database
        blacklistService.save(blacklist);

        int databaseSizeBeforeDelete = blacklistRepository.findAll().size();

        // Get the blacklist
        restBlacklistMockMvc.perform(delete("/api/blacklists/{id}", blacklist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Blacklist> blacklistList = blacklistRepository.findAll();
        assertThat(blacklistList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Blacklist.class);
        Blacklist blacklist1 = new Blacklist();
        blacklist1.setId(1L);
        Blacklist blacklist2 = new Blacklist();
        blacklist2.setId(blacklist1.getId());
        assertThat(blacklist1).isEqualTo(blacklist2);
        blacklist2.setId(2L);
        assertThat(blacklist1).isNotEqualTo(blacklist2);
        blacklist1.setId(null);
        assertThat(blacklist1).isNotEqualTo(blacklist2);
    }
}
