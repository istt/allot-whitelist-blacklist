package com.ft.web.rest;

import com.ft.AppApp;

import com.ft.domain.Whitelist;
import com.ft.repository.WhitelistRepository;
import com.ft.service.WhitelistService;
import com.ft.web.rest.errors.ExceptionTranslator;
import com.ft.service.dto.WhitelistCriteria;
import com.ft.service.WhitelistQueryService;

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
 * Test class for the WhitelistResource REST controller.
 *
 * @see WhitelistResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class WhitelistResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private WhitelistRepository whitelistRepository;

    

    @Autowired
    private WhitelistService whitelistService;

    @Autowired
    private WhitelistQueryService whitelistQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWhitelistMockMvc;

    private Whitelist whitelist;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WhitelistResource whitelistResource = new WhitelistResource(whitelistService, whitelistQueryService);
        this.restWhitelistMockMvc = MockMvcBuilders.standaloneSetup(whitelistResource)
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
    public static Whitelist createEntity(EntityManager em) {
        Whitelist whitelist = new Whitelist()
            .url(DEFAULT_URL);
        return whitelist;
    }

    @Before
    public void initTest() {
        whitelist = createEntity(em);
    }

    @Test
    @Transactional
    public void createWhitelist() throws Exception {
        int databaseSizeBeforeCreate = whitelistRepository.findAll().size();

        // Create the Whitelist
        restWhitelistMockMvc.perform(post("/api/whitelists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whitelist)))
            .andExpect(status().isCreated());

        // Validate the Whitelist in the database
        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeCreate + 1);
        Whitelist testWhitelist = whitelistList.get(whitelistList.size() - 1);
        assertThat(testWhitelist.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createWhitelistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = whitelistRepository.findAll().size();

        // Create the Whitelist with an existing ID
        whitelist.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWhitelistMockMvc.perform(post("/api/whitelists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whitelist)))
            .andExpect(status().isBadRequest());

        // Validate the Whitelist in the database
        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = whitelistRepository.findAll().size();
        // set the field null
        whitelist.setUrl(null);

        // Create the Whitelist, which fails.

        restWhitelistMockMvc.perform(post("/api/whitelists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whitelist)))
            .andExpect(status().isBadRequest());

        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWhitelists() throws Exception {
        // Initialize the database
        whitelistRepository.saveAndFlush(whitelist);

        // Get all the whitelistList
        restWhitelistMockMvc.perform(get("/api/whitelists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whitelist.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    

    @Test
    @Transactional
    public void getWhitelist() throws Exception {
        // Initialize the database
        whitelistRepository.saveAndFlush(whitelist);

        // Get the whitelist
        restWhitelistMockMvc.perform(get("/api/whitelists/{id}", whitelist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(whitelist.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getAllWhitelistsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        whitelistRepository.saveAndFlush(whitelist);

        // Get all the whitelistList where url equals to DEFAULT_URL
        defaultWhitelistShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the whitelistList where url equals to UPDATED_URL
        defaultWhitelistShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllWhitelistsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        whitelistRepository.saveAndFlush(whitelist);

        // Get all the whitelistList where url in DEFAULT_URL or UPDATED_URL
        defaultWhitelistShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the whitelistList where url equals to UPDATED_URL
        defaultWhitelistShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllWhitelistsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        whitelistRepository.saveAndFlush(whitelist);

        // Get all the whitelistList where url is not null
        defaultWhitelistShouldBeFound("url.specified=true");

        // Get all the whitelistList where url is null
        defaultWhitelistShouldNotBeFound("url.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultWhitelistShouldBeFound(String filter) throws Exception {
        restWhitelistMockMvc.perform(get("/api/whitelists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whitelist.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultWhitelistShouldNotBeFound(String filter) throws Exception {
        restWhitelistMockMvc.perform(get("/api/whitelists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    public void getNonExistingWhitelist() throws Exception {
        // Get the whitelist
        restWhitelistMockMvc.perform(get("/api/whitelists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWhitelist() throws Exception {
        // Initialize the database
        whitelistService.save(whitelist);

        int databaseSizeBeforeUpdate = whitelistRepository.findAll().size();

        // Update the whitelist
        Whitelist updatedWhitelist = whitelistRepository.findById(whitelist.getId()).get();
        // Disconnect from session so that the updates on updatedWhitelist are not directly saved in db
        em.detach(updatedWhitelist);
        updatedWhitelist
            .url(UPDATED_URL);

        restWhitelistMockMvc.perform(put("/api/whitelists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWhitelist)))
            .andExpect(status().isOk());

        // Validate the Whitelist in the database
        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeUpdate);
        Whitelist testWhitelist = whitelistList.get(whitelistList.size() - 1);
        assertThat(testWhitelist.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingWhitelist() throws Exception {
        int databaseSizeBeforeUpdate = whitelistRepository.findAll().size();

        // Create the Whitelist

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWhitelistMockMvc.perform(put("/api/whitelists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(whitelist)))
            .andExpect(status().isBadRequest());

        // Validate the Whitelist in the database
        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWhitelist() throws Exception {
        // Initialize the database
        whitelistService.save(whitelist);

        int databaseSizeBeforeDelete = whitelistRepository.findAll().size();

        // Get the whitelist
        restWhitelistMockMvc.perform(delete("/api/whitelists/{id}", whitelist.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Whitelist> whitelistList = whitelistRepository.findAll();
        assertThat(whitelistList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Whitelist.class);
        Whitelist whitelist1 = new Whitelist();
        whitelist1.setId(1L);
        Whitelist whitelist2 = new Whitelist();
        whitelist2.setId(whitelist1.getId());
        assertThat(whitelist1).isEqualTo(whitelist2);
        whitelist2.setId(2L);
        assertThat(whitelist1).isNotEqualTo(whitelist2);
        whitelist1.setId(null);
        assertThat(whitelist1).isNotEqualTo(whitelist2);
    }
}
