package tn.soretras.contestmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.soretras.contestmanagement.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
