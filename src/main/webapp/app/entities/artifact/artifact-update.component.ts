import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IArtifact, Artifact } from 'app/shared/model/artifact.model';
import { ArtifactService } from './artifact.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from 'app/entities/milestone/milestone.service';

@Component({
  selector: 'jhi-artifact-update',
  templateUrl: './artifact-update.component.html',
})
export class ArtifactUpdateComponent implements OnInit {
  isSaving = false;
  milestones: IMilestone[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    data: [null, [Validators.required]],
    dataContentType: [],
    milestone: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected artifactService: ArtifactService,
    protected milestoneService: MilestoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ artifact }) => {
      this.updateForm(artifact);

      this.milestoneService.query().subscribe((res: HttpResponse<IMilestone[]>) => (this.milestones = res.body || []));
    });
  }

  updateForm(artifact: IArtifact): void {
    this.editForm.patchValue({
      id: artifact.id,
      name: artifact.name,
      data: artifact.data,
      dataContentType: artifact.dataContentType,
      milestone: artifact.milestone,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('iitAdminApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const artifact = this.createFromForm();
    if (artifact.id !== undefined) {
      this.subscribeToSaveResponse(this.artifactService.update(artifact));
    } else {
      this.subscribeToSaveResponse(this.artifactService.create(artifact));
    }
  }

  private createFromForm(): IArtifact {
    return {
      ...new Artifact(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      dataContentType: this.editForm.get(['dataContentType'])!.value,
      data: this.editForm.get(['data'])!.value,
      milestone: this.editForm.get(['milestone'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArtifact>>): void {
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

  trackById(index: number, item: IMilestone): any {
    return item.id;
  }
}
