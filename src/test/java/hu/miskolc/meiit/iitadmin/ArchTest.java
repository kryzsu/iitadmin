package hu.miskolc.meiit.iitadmin;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("hu.miskolc.meiit.iitadmin");

        noClasses()
            .that()
            .resideInAnyPackage("hu.miskolc.meiit.iitadmin.service..")
            .or()
            .resideInAnyPackage("hu.miskolc.meiit.iitadmin.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..hu.miskolc.meiit.iitadmin.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
