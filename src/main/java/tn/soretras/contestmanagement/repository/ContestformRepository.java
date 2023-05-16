package tn.soretras.contestmanagement.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.soretras.contestmanagement.domain.Contestform;

/**
 * Spring Data JPA repository for the Contestform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContestformRepository extends JpaRepository<Contestform, Long> {}
