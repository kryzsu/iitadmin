package hu.miskolc.meiit.iitadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Artifact.
 */
@Entity
@Table(name = "artifact")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Artifact implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "data_content_type", nullable = false)
    private String dataContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = "artifacts", allowSetters = true)
    private Milestone milestone;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Artifact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public Artifact data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Artifact dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public Artifact milestone(Milestone milestone) {
        this.milestone = milestone;
        return this;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artifact)) {
            return false;
        }
        return id != null && id.equals(((Artifact) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Artifact{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            "}";
    }
}
