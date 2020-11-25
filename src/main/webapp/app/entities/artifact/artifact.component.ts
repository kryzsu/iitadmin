import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArtifact } from 'app/shared/model/artifact.model';
import { ArtifactService } from './artifact.service';
import { ArtifactDeleteDialogComponent } from './artifact-delete-dialog.component';

@Component({
  selector: 'jhi-artifact',
  templateUrl: './artifact.component.html',
})
export class ArtifactComponent implements OnInit, OnDestroy {
  artifacts?: IArtifact[];
  eventSubscriber?: Subscription;

  constructor(
    protected artifactService: ArtifactService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.artifactService.query().subscribe((res: HttpResponse<IArtifact[]>) => (this.artifacts = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInArtifacts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IArtifact): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInArtifacts(): void {
    this.eventSubscriber = this.eventManager.subscribe('artifactListModification', () => this.loadAll());
  }

  delete(artifact: IArtifact): void {
    const modalRef = this.modalService.open(ArtifactDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.artifact = artifact;
  }
}
