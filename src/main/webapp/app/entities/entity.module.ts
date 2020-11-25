import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'milestone',
        loadChildren: () => import('./milestone/milestone.module').then(m => m.IitAdminMilestoneModule),
      },
      {
        path: 'artifact',
        loadChildren: () => import('./artifact/artifact.module').then(m => m.IitAdminArtifactModule),
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.IitAdminCourseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class IitAdminEntityModule {}
