package hu.miskolc.meiit.iitadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hu.miskolc.meiit.iitadmin.IitAdminApp;
import hu.miskolc.meiit.iitadmin.domain.Artifact;
import hu.miskolc.meiit.iitadmin.repository.ArtifactRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ArtifactResource} REST controller.
 */
@SpringBootTest(classes = IitAdminApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArtifactResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DATA = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_CONTENT_TYPE = "image/png";

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtifactMockMvc;

    private Artifact artifact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artifact createEntity(EntityManager em) {
        Artifact artifact = new Artifact().name(DEFAULT_NAME).data(DEFAULT_DATA).dataContentType(DEFAULT_DATA_CONTENT_TYPE);
        return artifact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artifact createUpdatedEntity(EntityManager em) {
        Artifact artifact = new Artifact().name(UPDATED_NAME).data(UPDATED_DATA).dataContentType(UPDATED_DATA_CONTENT_TYPE);
        return artifact;
    }

    @BeforeEach
    public void initTest() {
        artifact = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtifact() throws Exception {
        int databaseSizeBeforeCreate = artifactRepository.findAll().size();
        // Create the Artifact
        restArtifactMockMvc
            .perform(post("/api/artifacts").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artifact)))
            .andExpect(status().isCreated());

        // Validate the Artifact in the database
        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeCreate + 1);
        Artifact testArtifact = artifactList.get(artifactList.size() - 1);
        assertThat(testArtifact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testArtifact.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testArtifact.getDataContentType()).isEqualTo(DEFAULT_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createArtifactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artifactRepository.findAll().size();

        // Create the Artifact with an existing ID
        artifact.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtifactMockMvc
            .perform(post("/api/artifacts").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artifact)))
            .andExpect(status().isBadRequest());

        // Validate the Artifact in the database
        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = artifactRepository.findAll().size();
        // set the field null
        artifact.setName(null);

        // Create the Artifact, which fails.

        restArtifactMockMvc
            .perform(post("/api/artifacts").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artifact)))
            .andExpect(status().isBadRequest());

        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllArtifacts() throws Exception {
        // Initialize the database
        artifactRepository.saveAndFlush(artifact);

        // Get all the artifactList
        restArtifactMockMvc
            .perform(get("/api/artifacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artifact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dataContentType").value(hasItem(DEFAULT_DATA_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA))));
    }

    @Test
    @Transactional
    public void getArtifact() throws Exception {
        // Initialize the database
        artifactRepository.saveAndFlush(artifact);

        // Get the artifact
        restArtifactMockMvc
            .perform(get("/api/artifacts/{id}", artifact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artifact.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dataContentType").value(DEFAULT_DATA_CONTENT_TYPE))
            .andExpect(jsonPath("$.data").value(Base64Utils.encodeToString(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingArtifact() throws Exception {
        // Get the artifact
        restArtifactMockMvc.perform(get("/api/artifacts/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtifact() throws Exception {
        // Initialize the database
        artifactRepository.saveAndFlush(artifact);

        int databaseSizeBeforeUpdate = artifactRepository.findAll().size();

        // Update the artifact
        Artifact updatedArtifact = artifactRepository.findById(artifact.getId()).get();
        // Disconnect from session so that the updates on updatedArtifact are not directly saved in db
        em.detach(updatedArtifact);
        updatedArtifact.name(UPDATED_NAME).data(UPDATED_DATA).dataContentType(UPDATED_DATA_CONTENT_TYPE);

        restArtifactMockMvc
            .perform(
                put("/api/artifacts").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedArtifact))
            )
            .andExpect(status().isOk());

        // Validate the Artifact in the database
        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeUpdate);
        Artifact testArtifact = artifactList.get(artifactList.size() - 1);
        assertThat(testArtifact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testArtifact.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testArtifact.getDataContentType()).isEqualTo(UPDATED_DATA_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtifact() throws Exception {
        int databaseSizeBeforeUpdate = artifactRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtifactMockMvc
            .perform(put("/api/artifacts").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artifact)))
            .andExpect(status().isBadRequest());

        // Validate the Artifact in the database
        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtifact() throws Exception {
        // Initialize the database
        artifactRepository.saveAndFlush(artifact);

        int databaseSizeBeforeDelete = artifactRepository.findAll().size();

        // Delete the artifact
        restArtifactMockMvc
            .perform(delete("/api/artifacts/{id}", artifact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artifact> artifactList = artifactRepository.findAll();
        assertThat(artifactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
