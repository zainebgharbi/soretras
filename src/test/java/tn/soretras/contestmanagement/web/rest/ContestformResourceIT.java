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
import tn.soretras.contestmanagement.domain.Contestform;
import tn.soretras.contestmanagement.repository.ContestformRepository;
import tn.soretras.contestmanagement.service.dto.ContestformDTO;
import tn.soretras.contestmanagement.service.mapper.ContestformMapper;

/**
 * Integration tests for the {@link ContestformResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContestformResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/contestforms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContestformRepository contestformRepository;

    @Autowired
    private ContestformMapper contestformMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContestformMockMvc;

    private Contestform contestform;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contestform createEntity(EntityManager em) {
        Contestform contestform = new Contestform().firstname(DEFAULT_FIRSTNAME).lastname(DEFAULT_LASTNAME).birthdate(DEFAULT_BIRTHDATE);
        // Add required entity
        Contest contest;
        if (TestUtil.findAll(em, Contest.class).isEmpty()) {
            contest = ContestResourceIT.createEntity(em);
            em.persist(contest);
            em.flush();
        } else {
            contest = TestUtil.findAll(em, Contest.class).get(0);
        }
        contestform.setContest(contest);
        return contestform;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contestform createUpdatedEntity(EntityManager em) {
        Contestform contestform = new Contestform().firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).birthdate(UPDATED_BIRTHDATE);
        // Add required entity
        Contest contest;
        if (TestUtil.findAll(em, Contest.class).isEmpty()) {
            contest = ContestResourceIT.createUpdatedEntity(em);
            em.persist(contest);
            em.flush();
        } else {
            contest = TestUtil.findAll(em, Contest.class).get(0);
        }
        contestform.setContest(contest);
        return contestform;
    }

    @BeforeEach
    public void initTest() {
        contestform = createEntity(em);
    }

    @Test
    @Transactional
    void createContestform() throws Exception {
        int databaseSizeBeforeCreate = contestformRepository.findAll().size();
        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);
        restContestformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeCreate + 1);
        Contestform testContestform = contestformList.get(contestformList.size() - 1);
        assertThat(testContestform.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testContestform.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testContestform.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
    }

    @Test
    @Transactional
    void createContestformWithExistingId() throws Exception {
        // Create the Contestform with an existing ID
        contestform.setId(1L);
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        int databaseSizeBeforeCreate = contestformRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContestformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestformRepository.findAll().size();
        // set the field null
        contestform.setFirstname(null);

        // Create the Contestform, which fails.
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        restContestformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contestformRepository.findAll().size();
        // set the field null
        contestform.setLastname(null);

        // Create the Contestform, which fails.
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        restContestformMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContestforms() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        // Get all the contestformList
        restContestformMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contestform.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())));
    }

    @Test
    @Transactional
    void getContestform() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        // Get the contestform
        restContestformMockMvc
            .perform(get(ENTITY_API_URL_ID, contestform.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contestform.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContestform() throws Exception {
        // Get the contestform
        restContestformMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContestform() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();

        // Update the contestform
        Contestform updatedContestform = contestformRepository.findById(contestform.getId()).get();
        // Disconnect from session so that the updates on updatedContestform are not directly saved in db
        em.detach(updatedContestform);
        updatedContestform.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).birthdate(UPDATED_BIRTHDATE);
        ContestformDTO contestformDTO = contestformMapper.toDto(updatedContestform);

        restContestformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contestformDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
        Contestform testContestform = contestformList.get(contestformList.size() - 1);
        assertThat(testContestform.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testContestform.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testContestform.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void putNonExistingContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contestformDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contestformDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContestformWithPatch() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();

        // Update the contestform using partial update
        Contestform partialUpdatedContestform = new Contestform();
        partialUpdatedContestform.setId(contestform.getId());

        partialUpdatedContestform.birthdate(UPDATED_BIRTHDATE);

        restContestformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContestform.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContestform))
            )
            .andExpect(status().isOk());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
        Contestform testContestform = contestformList.get(contestformList.size() - 1);
        assertThat(testContestform.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testContestform.getLastname()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testContestform.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void fullUpdateContestformWithPatch() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();

        // Update the contestform using partial update
        Contestform partialUpdatedContestform = new Contestform();
        partialUpdatedContestform.setId(contestform.getId());

        partialUpdatedContestform.firstname(UPDATED_FIRSTNAME).lastname(UPDATED_LASTNAME).birthdate(UPDATED_BIRTHDATE);

        restContestformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContestform.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContestform))
            )
            .andExpect(status().isOk());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
        Contestform testContestform = contestformList.get(contestformList.size() - 1);
        assertThat(testContestform.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testContestform.getLastname()).isEqualTo(UPDATED_LASTNAME);
        assertThat(testContestform.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void patchNonExistingContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contestformDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContestform() throws Exception {
        int databaseSizeBeforeUpdate = contestformRepository.findAll().size();
        contestform.setId(count.incrementAndGet());

        // Create the Contestform
        ContestformDTO contestformDTO = contestformMapper.toDto(contestform);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContestformMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contestformDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contestform in the database
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContestform() throws Exception {
        // Initialize the database
        contestformRepository.saveAndFlush(contestform);

        int databaseSizeBeforeDelete = contestformRepository.findAll().size();

        // Delete the contestform
        restContestformMockMvc
            .perform(delete(ENTITY_API_URL_ID, contestform.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contestform> contestformList = contestformRepository.findAll();
        assertThat(contestformList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
