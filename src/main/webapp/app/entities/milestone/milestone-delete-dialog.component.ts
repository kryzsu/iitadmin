import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';

@Component({
  templateUrl: './milestone-delete-dialog.component.html',
})
export class MilestoneDeleteDialogComponent {
  milestone?: IMilestone;

  constructor(protected milestoneService: MilestoneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.milestoneService.delete(id).subscribe(() => {
      this.eventManager.broadcast('milestoneListModification');
      this.activeModal.close();
    });
  }
}
