import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IitAdminSharedModule } from 'app/shared/shared.module';
import { ArtifactComponent } from './artifact.component';
import { ArtifactDetailComponent } from './artifact-detail.component';
import { ArtifactUpdateComponent } from './artifact-update.component';
import { ArtifactDeleteDialogComponent } from './artifact-delete-dialog.component';
import { artifactRoute } from './artifact.route';

@NgModule({
  imports: [IitAdminSharedModule, RouterModule.forChild(artifactRoute)],
  declarations: [ArtifactComponent, ArtifactDetailComponent, ArtifactUpdateComponent, ArtifactDeleteDialogComponent],
  entryComponents: [ArtifactDeleteDialogComponent],
})
export class IitAdminArtifactModule {}
