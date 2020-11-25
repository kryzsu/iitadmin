import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IitAdminTestModule } from '../../../test.module';
import { MilestoneDetailComponent } from 'app/entities/milestone/milestone-detail.component';
import { Milestone } from 'app/shared/model/milestone.model';

describe('Component Tests', () => {
  describe('Milestone Management Detail Component', () => {
    let comp: MilestoneDetailComponent;
    let fixture: ComponentFixture<MilestoneDetailComponent>;
    const route = ({ data: of({ milestone: new Milestone(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IitAdminTestModule],
        declarations: [MilestoneDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MilestoneDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MilestoneDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load milestone on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.milestone).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
