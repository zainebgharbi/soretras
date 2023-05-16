package tn.soretras.contestmanagement.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.soretras.contestmanagement.domain.Contest;
import tn.soretras.contestmanagement.repository.ContestRepository;
import tn.soretras.contestmanagement.service.dto.ContestDTO;
import tn.soretras.contestmanagement.service.mapper.ContestMapper;

/**
 * Service Implementation for managing {@link Contest}.
 */
@Service
@Transactional
public class ContestService {

    private final Logger log = LoggerFactory.getLogger(ContestService.class);

    private final ContestRepository contestRepository;

    private final ContestMapper contestMapper;

    public ContestService(ContestRepository contestRepository, ContestMapper contestMapper) {
        this.contestRepository = contestRepository;
        this.contestMapper = contestMapper;
    }

    /**
     * Save a contest.
     *
     * @param contestDTO the entity to save.
     * @return the persisted entity.
     */
    public ContestDTO save(ContestDTO contestDTO) {
        log.debug("Request to save Contest : {}", contestDTO);
        Contest contest = contestMapper.toEntity(contestDTO);
        contest = contestRepository.save(contest);
        return contestMapper.toDto(contest);
    }

    /**
     * Update a contest.
     *
     * @param contestDTO the entity to save.
     * @return the persisted entity.
     */
    public ContestDTO update(ContestDTO contestDTO) {
        log.debug("Request to update Contest : {}", contestDTO);
        Contest contest = contestMapper.toEntity(contestDTO);
        contest = contestRepository.save(contest);
        return contestMapper.toDto(contest);
    }

    /**
     * Partially update a contest.
     *
     * @param contestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContestDTO> partialUpdate(ContestDTO contestDTO) {
        log.debug("Request to partially update Contest : {}", contestDTO);

        return contestRepository
            .findById(contestDTO.getId())
            .map(existingContest -> {
                contestMapper.partialUpdate(existingContest, contestDTO);

                return existingContest;
            })
            .map(contestRepository::save)
            .map(contestMapper::toDto);
    }

    /**
     * Get all the contests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contests");
        return contestRepository.findAll(pageable).map(contestMapper::toDto);
    }

    /**
     * Get one contest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContestDTO> findOne(Long id) {
        log.debug("Request to get Contest : {}", id);
        return contestRepository.findById(id).map(contestMapper::toDto);
    }

    /**
     * Delete the contest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contest : {}", id);
        contestRepository.deleteById(id);
    }
}
