package com.ft.repository;

import com.ft.domain.Blacklist;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Blacklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long>, JpaSpecificationExecutor<Blacklist> {

}
