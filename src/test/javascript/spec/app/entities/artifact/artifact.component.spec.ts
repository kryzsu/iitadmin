import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IitAdminTestModule } from '../../../test.module';
import { ArtifactComponent } from 'app/entities/artifact/artifact.component';
import { ArtifactService } from 'app/entities/artifact/artifact.service';
import { Artifact } from 'app/shared/model/artifact.model';

describe('Component Tests', () => {
  describe('Artifact Management Component', () => {
    let comp: ArtifactComponent;
    let fixture: ComponentFixture<ArtifactComponent>;
    let service: ArtifactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IitAdminTestModule],
        declarations: [ArtifactComponent],
      })
        .overrideTemplate(ArtifactComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArtifactComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtifactService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Artifact(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.artifacts && comp.artifacts[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
