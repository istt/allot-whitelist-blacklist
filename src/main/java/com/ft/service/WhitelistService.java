package com.ft.service;

import com.ft.config.ApplicationProperties;
import com.ft.domain.Whitelist;
import com.ft.repository.WhitelistRepository;
import com.ft.service.dto.DataFileDTO;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Service Implementation for managing Whitelist.
 */
@Service
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
        log.debug("Request to save Whitelist : {}", whitelist);
        if (whitelist.getId() != null) whitelistRepository.delete(whitelist);
        Matcher m = patt.matcher(new String(whitelist.getUrl()));
		 while (m.find()) {
			 try {
				 whitelistRepository.save(new Whitelist().url(m.group()));
			 } catch (Exception e) {
			 }
		 }
       return whitelist;
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

    public void truncate() {
    	log.debug("Request to truncate Whitelist");
    	whitelistRepository.deleteAll();
    }

    // Strip off all empty characters
    final static Pattern patt = Pattern.compile("([^\\s]+)");

    public int importData(DataFileDTO dataFile) {
		int result = 0;
		if (dataFile.getTruncateData())whitelistRepository.deleteAll();
		Matcher m = patt.matcher(new String(dataFile.getDataFile()));
         while (m.find()) {
        	 try {
				whitelistRepository.save(new Whitelist().url(m.group()));
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
		for (Whitelist dnd: whitelistRepository.findAll()){
			rs += dnd.getUrl() + "\n";
		}
		log.debug("File content: " + rs);
		result.setDataFile(rs.getBytes());
		return result;
	}

	@Autowired
	ApplicationProperties props;

	public DataFileDTO writeData() throws IOException {
		DataFileDTO result = exportData();
		FileUtils.writeByteArrayToFile(new File(props.getWhitelistFilePath()), result.getDataFile());
		return result;
	}
}
