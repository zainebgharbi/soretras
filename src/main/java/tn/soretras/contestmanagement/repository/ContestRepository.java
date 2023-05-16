package tn.soretras.contestmanagement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.soretras.contestmanagement.domain.Contest;

/**
 * Spring Data JPA repository for the Contest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {}
