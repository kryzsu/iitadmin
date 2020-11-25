package hu.miskolc.meiit.iitadmin.repository;

import hu.miskolc.meiit.iitadmin.domain.Artifact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Artifact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {}
