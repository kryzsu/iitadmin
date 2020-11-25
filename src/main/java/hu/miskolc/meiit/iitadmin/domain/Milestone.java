package hu.miskolc.meiit.iitadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Milestone.
 */
@Entity
@Table(name = "milestone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Milestone implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @OneToMany(mappedBy = "milestone")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Artifact> artifacts = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "milestones", allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Milestone description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public Milestone deadline(LocalDate deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public Milestone artifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
        return this;
    }

    public Milestone addArtifact(Artifact artifact) {
        this.artifacts.add(artifact);
        artifact.setMilestone(this);
        return this;
    }

    public Milestone removeArtifact(Artifact artifact) {
        this.artifacts.remove(artifact);
        artifact.setMilestone(null);
        return this;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Course getCourse() {
        return course;
    }

    public Milestone course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Milestone)) {
            return false;
        }
        return id != null && id.equals(((Milestone) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Milestone{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", deadline='" + getDeadline() + "'" +
            "}";
    }
}
