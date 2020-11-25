package hu.miskolc.meiit.iitadmin.repository;

import hu.miskolc.meiit.iitadmin.domain.Course;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {}
