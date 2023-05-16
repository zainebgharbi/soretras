package tn.soretras.contestmanagement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.soretras.contestmanagement.repository.ContestRepository;
import tn.soretras.contestmanagement.service.ContestService;
import tn.soretras.contestmanagement.service.dto.ContestDTO;
import tn.soretras.contestmanagement.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.soretras.contestmanagement.domain.Contest}.
 */
@RestController
@RequestMapping("/api")
public class ContestResource {

    private final Logger log = LoggerFactory.getLogger(ContestResource.class);

    private static final String ENTITY_NAME = "contest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContestService contestService;

    private final ContestRepository contestRepository;

    public ContestResource(ContestService contestService, ContestRepository contestRepository) {
        this.contestService = contestService;
        this.contestRepository = contestRepository;
    }

    /**
     * {@code POST  /contests} : Create a new contest.
     *
     * @param contestDTO the contestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contestDTO, or with status {@code 400 (Bad Request)} if the contest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contests")
    public ResponseEntity<ContestDTO> createContest(@Valid @RequestBody ContestDTO contestDTO) throws URISyntaxException {
        log.debug("REST request to save Contest : {}", contestDTO);
        if (contestDTO.getId() != null) {
            throw new BadRequestAlertException("A new contest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContestDTO result = contestService.save(contestDTO);
        return ResponseEntity
            .created(new URI("/api/contests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contests/:id} : Updates an existing contest.
     *
     * @param id the id of the contestDTO to save.
     * @param contestDTO the contestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contestDTO,
     * or with status {@code 400 (Bad Request)} if the contestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contests/{id}")
    public ResponseEntity<ContestDTO> updateContest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContestDTO contestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contest : {}, {}", id, contestDTO);
        if (contestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContestDTO result = contestService.update(contestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contests/:id} : Partial updates given fields of an existing contest, field will ignore if it is null
     *
     * @param id the id of the contestDTO to save.
     * @param contestDTO the contestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contestDTO,
     * or with status {@code 400 (Bad Request)} if the contestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContestDTO> partialUpdateContest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContestDTO contestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contest partially : {}, {}", id, contestDTO);
        if (contestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContestDTO> result = contestService.partialUpdate(contestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contests} : get all the contests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contests in body.
     */
    @GetMapping("/contests")
    public ResponseEntity<List<ContestDTO>> getAllContests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        Page<ContestDTO> page = contestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contests/:id} : get the "id" contest.
     *
     * @param id the id of the contestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contests/{id}")
    public ResponseEntity<ContestDTO> getContest(@PathVariable Long id) {
        log.debug("REST request to get Contest : {}", id);
        Optional<ContestDTO> contestDTO = contestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contestDTO);
    }

    /**
     * {@code DELETE  /contests/:id} : delete the "id" contest.
     *
     * @param id the id of the contestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contests/{id}")
    public ResponseEntity<Void> deleteContest(@PathVariable Long id) {
        log.debug("REST request to delete Contest : {}", id);
        contestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
