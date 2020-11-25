import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMilestone, Milestone } from 'app/shared/model/milestone.model';
import { MilestoneService } from './milestone.service';
import { MilestoneComponent } from './milestone.component';
import { MilestoneDetailComponent } from './milestone-detail.component';
import { MilestoneUpdateComponent } from './milestone-update.component';

@Injectable({ providedIn: 'root' })
export class MilestoneResolve implements Resolve<IMilestone> {
  constructor(private service: MilestoneService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMilestone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((milestone: HttpResponse<Milestone>) => {
          if (milestone.body) {
            return of(milestone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Milestone());
  }
}

export const milestoneRoute: Routes = [
  {
    path: '',
    component: MilestoneComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.milestone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MilestoneDetailComponent,
    resolve: {
      milestone: MilestoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.milestone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MilestoneUpdateComponent,
    resolve: {
      milestone: MilestoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.milestone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MilestoneUpdateComponent,
    resolve: {
      milestone: MilestoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.milestone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
