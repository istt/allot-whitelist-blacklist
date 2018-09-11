package com.ft.repository;

import com.ft.domain.Whitelist;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Whitelist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WhitelistRepository extends JpaRepository<Whitelist, Long>, JpaSpecificationExecutor<Whitelist> {

}
