import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMilestone, Milestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

@Component({
  selector: 'jhi-milestone-update',
  templateUrl: './milestone-update.component.html',
})
export class MilestoneUpdateComponent implements OnInit {
  isSaving = false;
  courses: ICourse[] = [];
  deadlineDp: any;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required]],
    deadline: [null, [Validators.required]],
    course: [],
  });

  constructor(
    protected milestoneService: MilestoneService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ milestone }) => {
      this.updateForm(milestone);

      this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body || []));
    });
  }

  updateForm(milestone: IMilestone): void {
    this.editForm.patchValue({
      id: milestone.id,
      description: milestone.description,
      deadline: milestone.deadline,
      course: milestone.course,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const milestone = this.createFromForm();
    if (milestone.id !== undefined) {
      this.subscribeToSaveResponse(this.milestoneService.update(milestone));
    } else {
      this.subscribeToSaveResponse(this.milestoneService.create(milestone));
    }
  }

  private createFromForm(): IMilestone {
    return {
      ...new Milestone(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      deadline: this.editForm.get(['deadline'])!.value,
      course: this.editForm.get(['course'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMilestone>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICourse): any {
    return item.id;
  }
}
