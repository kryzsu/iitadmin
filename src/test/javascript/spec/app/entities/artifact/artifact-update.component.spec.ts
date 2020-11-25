import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { IitAdminTestModule } from '../../../test.module';
import { ArtifactUpdateComponent } from 'app/entities/artifact/artifact-update.component';
import { ArtifactService } from 'app/entities/artifact/artifact.service';
import { Artifact } from 'app/shared/model/artifact.model';

describe('Component Tests', () => {
  describe('Artifact Management Update Component', () => {
    let comp: ArtifactUpdateComponent;
    let fixture: ComponentFixture<ArtifactUpdateComponent>;
    let service: ArtifactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IitAdminTestModule],
        declarations: [ArtifactUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ArtifactUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArtifactUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ArtifactService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Artifact(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Artifact();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
