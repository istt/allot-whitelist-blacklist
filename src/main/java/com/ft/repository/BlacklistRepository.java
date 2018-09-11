package com.ft.repository;

import com.ft.domain.Blacklist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Blacklist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long>, JpaSpecificationExecutor<Blacklist> {

}
