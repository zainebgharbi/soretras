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
import tn.soretras.contestmanagement.repository.ContestformRepository;
import tn.soretras.contestmanagement.service.ContestformService;
import tn.soretras.contestmanagement.service.dto.ContestformDTO;
import tn.soretras.contestmanagement.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.soretras.contestmanagement.domain.Contestform}.
 */
@RestController
@RequestMapping("/api")
public class ContestformResource {

    private final Logger log = LoggerFactory.getLogger(ContestformResource.class);

    private static final String ENTITY_NAME = "contestform";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContestformService contestformService;

    private final ContestformRepository contestformRepository;

    public ContestformResource(ContestformService contestformService, ContestformRepository contestformRepository) {
        this.contestformService = contestformService;
        this.contestformRepository = contestformRepository;
    }

    /**
     * {@code POST  /contestforms} : Create a new contestform.
     *
     * @param contestformDTO the contestformDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contestformDTO, or with status {@code 400 (Bad Request)} if the contestform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contestforms")
    public ResponseEntity<ContestformDTO> createContestform(@Valid @RequestBody ContestformDTO contestformDTO) throws URISyntaxException {
        log.debug("REST request to save Contestform : {}", contestformDTO);
        if (contestformDTO.getId() != null) {
            throw new BadRequestAlertException("A new contestform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContestformDTO result = contestformService.save(contestformDTO);
        return ResponseEntity
            .created(new URI("/api/contestforms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contestforms/:id} : Updates an existing contestform.
     *
     * @param id the id of the contestformDTO to save.
     * @param contestformDTO the contestformDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contestformDTO,
     * or with status {@code 400 (Bad Request)} if the contestformDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contestformDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contestforms/{id}")
    public ResponseEntity<ContestformDTO> updateContestform(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContestformDTO contestformDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contestform : {}, {}", id, contestformDTO);
        if (contestformDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contestformDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contestformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContestformDTO result = contestformService.update(contestformDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contestformDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contestforms/:id} : Partial updates given fields of an existing contestform, field will ignore if it is null
     *
     * @param id the id of the contestformDTO to save.
     * @param contestformDTO the contestformDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contestformDTO,
     * or with status {@code 400 (Bad Request)} if the contestformDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contestformDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contestformDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contestforms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContestformDTO> partialUpdateContestform(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContestformDTO contestformDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contestform partially : {}, {}", id, contestformDTO);
        if (contestformDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contestformDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contestformRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContestformDTO> result = contestformService.partialUpdate(contestformDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contestformDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contestforms} : get all the contestforms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contestforms in body.
     */
    @GetMapping("/contestforms")
    public ResponseEntity<List<ContestformDTO>> getAllContestforms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contestforms");
        Page<ContestformDTO> page = contestformService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contestforms/:id} : get the "id" contestform.
     *
     * @param id the id of the contestformDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contestformDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contestforms/{id}")
    public ResponseEntity<ContestformDTO> getContestform(@PathVariable Long id) {
        log.debug("REST request to get Contestform : {}", id);
        Optional<ContestformDTO> contestformDTO = contestformService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contestformDTO);
    }

    /**
     * {@code DELETE  /contestforms/:id} : delete the "id" contestform.
     *
     * @param id the id of the contestformDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contestforms/{id}")
    public ResponseEntity<Void> deleteContestform(@PathVariable Long id) {
        log.debug("REST request to delete Contestform : {}", id);
        contestformService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
