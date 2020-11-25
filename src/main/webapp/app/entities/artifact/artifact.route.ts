import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IArtifact, Artifact } from 'app/shared/model/artifact.model';
import { ArtifactService } from './artifact.service';
import { ArtifactComponent } from './artifact.component';
import { ArtifactDetailComponent } from './artifact-detail.component';
import { ArtifactUpdateComponent } from './artifact-update.component';

@Injectable({ providedIn: 'root' })
export class ArtifactResolve implements Resolve<IArtifact> {
  constructor(private service: ArtifactService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IArtifact> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((artifact: HttpResponse<Artifact>) => {
          if (artifact.body) {
            return of(artifact.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Artifact());
  }
}

export const artifactRoute: Routes = [
  {
    path: '',
    component: ArtifactComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.artifact.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArtifactDetailComponent,
    resolve: {
      artifact: ArtifactResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.artifact.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArtifactUpdateComponent,
    resolve: {
      artifact: ArtifactResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.artifact.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArtifactUpdateComponent,
    resolve: {
      artifact: ArtifactResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'iitAdminApp.artifact.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
