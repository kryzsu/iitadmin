import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { IitAdminSharedModule } from 'app/shared/shared.module';
import { IitAdminCoreModule } from 'app/core/core.module';
import { IitAdminAppRoutingModule } from './app-routing.module';
import { IitAdminHomeModule } from './home/home.module';
import { IitAdminEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    IitAdminSharedModule,
    IitAdminCoreModule,
    IitAdminHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    IitAdminEntityModule,
    IitAdminAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class IitAdminAppModule {}
