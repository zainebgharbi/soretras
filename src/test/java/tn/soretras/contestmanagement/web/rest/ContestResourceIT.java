package tn.soretras.contestmanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.soretras.contestmanagement.IntegrationTest;
import tn.soretras.contestmanagement.domain.Contest;
import tn.soretras.contestmanagement.domain.enumeration.elevel;
import tn.soretras.contestmanagement.repository.ContestRepository;
import tn.soretras.contestmanagement.service.dto.ContestDTO;
import tn.soretras.contestmanagement.service.mapper.ContestMapper;

/**
 * Integration tests for the {@link ContestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BEGINDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGINDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDDATE = LocalDate.now(ZoneId.systemDefault());

    private static final elevel DEFAULT_LEVEL = elevel.BAC;
    private static final elevel UPDATED_LEVEL = elevel.LICENSE;

    private static final String ENTITY_API_URL = "/api/contests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContestMockMvc;

    private Contest contest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contest createEntity(EntityManager em) {
        Contest contest = new Contest()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .begindate(DEFAULT_BEGINDATE)
            .enddate(DEFAULT_ENDDATE)
            .level(DEFAULT_LEVEL);
        return contest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contest createUpdatedEntity(EntityManager em) {
        Contest contest = new Contest()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .begindate(UPDATED_BEGINDATE)
            .enddate(UPDATED_ENDDATE)
            .level(UPDATED_LEVEL);
        return contest;
    }

    @BeforeEach
    public void initTest() {
        contest = createEntity(em);
    }

    @Test
    @Transactional
    void createContest() throws Exception {
        int databaseSizeBeforeCreate = contestRepository.findAll().size();
        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);
        restContestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isCreated());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate + 1);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContest.getBegindate()).isEqualTo(DEFAULT_BEGINDATE);
        assertThat(testContest.getEnddate()).isEqualTo(DEFAULT_ENDDATE);
        assertThat(testContest.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createContestWithExistingId() throws Exception {
        // Create the Contest with an existing ID
        contest.setId(1L);
        ContestDTO contestDTO = contestMapper.toDto(contest);

        int databaseSizeBeforeCreate = contestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestRepository.findAll().size();
        // set the field null
        contest.setName(null);

        // Create the Contest, which fails.
        ContestDTO contestDTO = contestMapper.toDto(contest);

        restContestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isBadRequest());

        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContests() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get all the contestList
        restContestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].begindate").value(hasItem(DEFAULT_BEGINDATE.toString())))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(DEFAULT_ENDDATE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    void getContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        // Get the contest
        restContestMockMvc
            .perform(get(ENTITY_API_URL_ID, contest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.begindate").value(DEFAULT_BEGINDATE.toString()))
            .andExpect(jsonPath("$.enddate").value(DEFAULT_ENDDATE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContest() throws Exception {
        // Get the contest
        restContestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest
        Contest updatedContest = contestRepository.findById(contest.getId()).get();
        // Disconnect from session so that the updates on updatedContest are not directly saved in db
        em.detach(updatedContest);
        updatedContest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .begindate(UPDATED_BEGINDATE)
            .enddate(UPDATED_ENDDATE)
            .level(UPDATED_LEVEL);
        ContestDTO contestDTO = contestMapper.toDto(updatedContest);

        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContest.getBegindate()).isEqualTo(UPDATED_BEGINDATE);
        assertThat(testContest.getEnddate()).isEqualTo(UPDATED_ENDDATE);
        assertThat(testContest.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContestWithPatch() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest using partial update
        Contest partialUpdatedContest = new Contest();
        partialUpdatedContest.setId(contest.getId());

        partialUpdatedContest.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContest))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContest.getBegindate()).isEqualTo(DEFAULT_BEGINDATE);
        assertThat(testContest.getEnddate()).isEqualTo(DEFAULT_ENDDATE);
        assertThat(testContest.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateContestWithPatch() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeUpdate = contestRepository.findAll().size();

        // Update the contest using partial update
        Contest partialUpdatedContest = new Contest();
        partialUpdatedContest.setId(contest.getId());

        partialUpdatedContest
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .begindate(UPDATED_BEGINDATE)
            .enddate(UPDATED_ENDDATE)
            .level(UPDATED_LEVEL);

        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContest))
            )
            .andExpect(status().isOk());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
        Contest testContest = contestList.get(contestList.size() - 1);
        assertThat(testContest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContest.getBegindate()).isEqualTo(UPDATED_BEGINDATE);
        assertThat(testContest.getEnddate()).isEqualTo(UPDATED_ENDDATE);
        assertThat(testContest.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContest() throws Exception {
        int databaseSizeBeforeUpdate = contestRepository.findAll().size();
        contest.setId(count.incrementAndGet());

        // Create the Contest
        ContestDTO contestDTO = contestMapper.toDto(contest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contest in the database
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContest() throws Exception {
        // Initialize the database
        contestRepository.saveAndFlush(contest);

        int databaseSizeBeforeDelete = contestRepository.findAll().size();

        // Delete the contest
        restContestMockMvc
            .perform(delete(ENTITY_API_URL_ID, contest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contest> contestList = contestRepository.findAll();
        assertThat(contestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
