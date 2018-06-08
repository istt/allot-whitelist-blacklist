package com.ft.service;

import com.ft.domain.Blacklist;
import com.ft.repository.BlacklistRepository;
import com.ft.service.dto.DataFileDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Service Implementation for managing Blacklist.
 */
@Service
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
        log.debug("Request to save Blacklist : {}", blacklist);
        if (blacklist.getId() != null) blacklistRepository.delete(blacklist);
        Matcher m = patt.matcher(new String(blacklist.getUrl()));
		 while (m.find()) {
			 try {
				 blacklistRepository.save(
					new Blacklist()
						 .url(m.group())
						 );
			 } catch (Exception e) {
			 }
		 }
        return blacklist;
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

    // Strip off all empty characters
    final static Pattern patt = Pattern.compile("([^\\s]+)");

    public int importData(DataFileDTO dataFile) {
		int result = 0;
		Matcher m = patt.matcher(new String(dataFile.getDataFile()));
         while (m.find()) {
        	 try {
				blacklistRepository.save(new Blacklist().url(m.group()));
				result ++;
			} catch (Exception e) {
			}
         }
		return result;
	}

	/**
	 * Export data into CSV.
	 * FIXME: This isn't the best but we just stick with it first
	 * @return
	 */
	public DataFileDTO exportData() {
		DataFileDTO result = new DataFileDTO();
		result.setDataFileContentType("text/csv");
		String rs = "";
		// Save all MSISDN into CSV
		for (Blacklist dnd: blacklistRepository.findAll()){
			rs += dnd.getUrl() + "\n";
		}
		log.debug("File content: " + rs);
		result.setDataFile(rs.getBytes());
		return result;
	}
}
