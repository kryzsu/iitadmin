import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMilestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { MilestoneDeleteDialogComponent } from './milestone-delete-dialog.component';

@Component({
  selector: 'jhi-milestone',
  templateUrl: './milestone.component.html',
})
export class MilestoneComponent implements OnInit, OnDestroy {
  milestones?: IMilestone[];
  eventSubscriber?: Subscription;

  constructor(protected milestoneService: MilestoneService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.milestoneService.query().subscribe((res: HttpResponse<IMilestone[]>) => (this.milestones = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMilestones();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMilestone): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMilestones(): void {
    this.eventSubscriber = this.eventManager.subscribe('milestoneListModification', () => this.loadAll());
  }

  delete(milestone: IMilestone): void {
    const modalRef = this.modalService.open(MilestoneDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.milestone = milestone;
  }
}
