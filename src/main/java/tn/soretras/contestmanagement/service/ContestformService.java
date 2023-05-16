package tn.soretras.contestmanagement.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.soretras.contestmanagement.domain.Contestform;
import tn.soretras.contestmanagement.repository.ContestformRepository;
import tn.soretras.contestmanagement.service.dto.ContestformDTO;
import tn.soretras.contestmanagement.service.mapper.ContestformMapper;

/**
 * Service Implementation for managing {@link Contestform}.
 */
@Service
@Transactional
public class ContestformService {

    private final Logger log = LoggerFactory.getLogger(ContestformService.class);

    private final ContestformRepository contestformRepository;

    private final ContestformMapper contestformMapper;

    public ContestformService(ContestformRepository contestformRepository, ContestformMapper contestformMapper) {
        this.contestformRepository = contestformRepository;
        this.contestformMapper = contestformMapper;
    }

    /**
     * Save a contestform.
     *
     * @param contestformDTO the entity to save.
     * @return the persisted entity.
     */
    public ContestformDTO save(ContestformDTO contestformDTO) {
        log.debug("Request to save Contestform : {}", contestformDTO);
        Contestform contestform = contestformMapper.toEntity(contestformDTO);
        contestform = contestformRepository.save(contestform);
        return contestformMapper.toDto(contestform);
    }

    /**
     * Update a contestform.
     *
     * @param contestformDTO the entity to save.
     * @return the persisted entity.
     */
    public ContestformDTO update(ContestformDTO contestformDTO) {
        log.debug("Request to update Contestform : {}", contestformDTO);
        Contestform contestform = contestformMapper.toEntity(contestformDTO);
        contestform = contestformRepository.save(contestform);
        return contestformMapper.toDto(contestform);
    }

    /**
     * Partially update a contestform.
     *
     * @param contestformDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContestformDTO> partialUpdate(ContestformDTO contestformDTO) {
        log.debug("Request to partially update Contestform : {}", contestformDTO);

        return contestformRepository
            .findById(contestformDTO.getId())
            .map(existingContestform -> {
                contestformMapper.partialUpdate(existingContestform, contestformDTO);

                return existingContestform;
            })
            .map(contestformRepository::save)
            .map(contestformMapper::toDto);
    }

    /**
     * Get all the contestforms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContestformDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contestforms");
        return contestformRepository.findAll(pageable).map(contestformMapper::toDto);
    }

    /**
     * Get one contestform by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContestformDTO> findOne(Long id) {
        log.debug("Request to get Contestform : {}", id);
        return contestformRepository.findById(id).map(contestformMapper::toDto);
    }

    /**
     * Delete the contestform by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contestform : {}", id);
        contestformRepository.deleteById(id);
    }
}
