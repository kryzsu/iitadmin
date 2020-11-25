import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IitadminSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';

@NgModule({
  imports: [IitadminSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class IitadminHomeModule {}
