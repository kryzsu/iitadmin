package hu.miskolc.meiit.iitadmin.web.rest;

import hu.miskolc.meiit.iitadmin.domain.Milestone;
import hu.miskolc.meiit.iitadmin.repository.MilestoneRepository;
import hu.miskolc.meiit.iitadmin.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link hu.miskolc.meiit.iitadmin.domain.Milestone}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MilestoneResource {
    private final Logger log = LoggerFactory.getLogger(MilestoneResource.class);

    private static final String ENTITY_NAME = "milestone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MilestoneRepository milestoneRepository;

    public MilestoneResource(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    /**
     * {@code POST  /milestones} : Create a new milestone.
     *
     * @param milestone the milestone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new milestone, or with status {@code 400 (Bad Request)} if the milestone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/milestones")
    public ResponseEntity<Milestone> createMilestone(@Valid @RequestBody Milestone milestone) throws URISyntaxException {
        log.debug("REST request to save Milestone : {}", milestone);
        if (milestone.getId() != null) {
            throw new BadRequestAlertException("A new milestone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Milestone result = milestoneRepository.save(milestone);
        return ResponseEntity
            .created(new URI("/api/milestones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /milestones} : Updates an existing milestone.
     *
     * @param milestone the milestone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated milestone,
     * or with status {@code 400 (Bad Request)} if the milestone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the milestone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/milestones")
    public ResponseEntity<Milestone> updateMilestone(@Valid @RequestBody Milestone milestone) throws URISyntaxException {
        log.debug("REST request to update Milestone : {}", milestone);
        if (milestone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Milestone result = milestoneRepository.save(milestone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, milestone.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /milestones} : get all the milestones.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of milestones in body.
     */
    @GetMapping("/milestones")
    public List<Milestone> getAllMilestones() {
        log.debug("REST request to get all Milestones");
        return milestoneRepository.findAll();
    }

    /**
     * {@code GET  /milestones/:id} : get the "id" milestone.
     *
     * @param id the id of the milestone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the milestone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/milestones/{id}")
    public ResponseEntity<Milestone> getMilestone(@PathVariable Long id) {
        log.debug("REST request to get Milestone : {}", id);
        Optional<Milestone> milestone = milestoneRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(milestone);
    }

    /**
     * {@code DELETE  /milestones/:id} : delete the "id" milestone.
     *
     * @param id the id of the milestone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/milestones/{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable Long id) {
        log.debug("REST request to delete Milestone : {}", id);
        milestoneRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
