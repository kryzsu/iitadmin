import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IArtifact } from 'app/shared/model/artifact.model';
import { ArtifactService } from './artifact.service';

@Component({
  templateUrl: './artifact-delete-dialog.component.html',
})
export class ArtifactDeleteDialogComponent {
  artifact?: IArtifact;

  constructor(protected artifactService: ArtifactService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.artifactService.delete(id).subscribe(() => {
      this.eventManager.broadcast('artifactListModification');
      this.activeModal.close();
    });
  }
}
