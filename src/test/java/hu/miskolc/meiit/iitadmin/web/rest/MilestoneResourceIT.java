package hu.miskolc.meiit.iitadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.miskolc.meiit.iitadmin.IitAdminApp;
import hu.miskolc.meiit.iitadmin.domain.Milestone;
import hu.miskolc.meiit.iitadmin.repository.MilestoneRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MilestoneResource} REST controller.
 */
@SpringBootTest(classes = IitAdminApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MilestoneResourceIT {
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DEADLINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEADLINE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMilestoneMockMvc;

    private Milestone milestone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestone createEntity(EntityManager em) {
        Milestone milestone = new Milestone().description(DEFAULT_DESCRIPTION).deadline(DEFAULT_DEADLINE);
        return milestone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Milestone createUpdatedEntity(EntityManager em) {
        Milestone milestone = new Milestone().description(UPDATED_DESCRIPTION).deadline(UPDATED_DEADLINE);
        return milestone;
    }

    @BeforeEach
    public void initTest() {
        milestone = createEntity(em);
    }

    @Test
    @Transactional
    public void createMilestone() throws Exception {
        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();
        // Create the Milestone
        restMilestoneMockMvc
            .perform(post("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isCreated());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate + 1);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMilestone.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
    }

    @Test
    @Transactional
    public void createMilestoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = milestoneRepository.findAll().size();

        // Create the Milestone with an existing ID
        milestone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilestoneMockMvc
            .perform(post("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = milestoneRepository.findAll().size();
        // set the field null
        milestone.setDescription(null);

        // Create the Milestone, which fails.

        restMilestoneMockMvc
            .perform(post("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeadlineIsRequired() throws Exception {
        int databaseSizeBeforeTest = milestoneRepository.findAll().size();
        // set the field null
        milestone.setDeadline(null);

        // Create the Milestone, which fails.

        restMilestoneMockMvc
            .perform(post("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMilestones() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get all the milestoneList
        restMilestoneMockMvc
            .perform(get("/api/milestones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(DEFAULT_DEADLINE.toString())));
    }

    @Test
    @Transactional
    public void getMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        // Get the milestone
        restMilestoneMockMvc
            .perform(get("/api/milestones/{id}", milestone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(milestone.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.deadline").value(DEFAULT_DEADLINE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMilestone() throws Exception {
        // Get the milestone
        restMilestoneMockMvc.perform(get("/api/milestones/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // Update the milestone
        Milestone updatedMilestone = milestoneRepository.findById(milestone.getId()).get();
        // Disconnect from session so that the updates on updatedMilestone are not directly saved in db
        em.detach(updatedMilestone);
        updatedMilestone.description(UPDATED_DESCRIPTION).deadline(UPDATED_DEADLINE);

        restMilestoneMockMvc
            .perform(
                put("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedMilestone))
            )
            .andExpect(status().isOk());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
        Milestone testMilestone = milestoneList.get(milestoneList.size() - 1);
        assertThat(testMilestone.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMilestone.getDeadline()).isEqualTo(UPDATED_DEADLINE);
    }

    @Test
    @Transactional
    public void updateNonExistingMilestone() throws Exception {
        int databaseSizeBeforeUpdate = milestoneRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestoneMockMvc
            .perform(put("/api/milestones").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(milestone)))
            .andExpect(status().isBadRequest());

        // Validate the Milestone in the database
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMilestone() throws Exception {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone);

        int databaseSizeBeforeDelete = milestoneRepository.findAll().size();

        // Delete the milestone
        restMilestoneMockMvc
            .perform(delete("/api/milestones/{id}", milestone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Milestone> milestoneList = milestoneRepository.findAll();
        assertThat(milestoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
