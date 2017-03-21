import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HeaderComponent } from './header/header.component';
import { LayoutNormal } from './layout-normal/layout-normal.component';
import { LayoutLogin } from './layout-login/layout-login.component';
import { SessionComponent } from "../session/session.component";
import { CalicoUiModule } from 'calico';
import { DropdownModule } from 'ng2-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    CalicoUiModule,
    DropdownModule,
  ],
  declarations: [
    HeaderComponent,
    LayoutNormal,
    LayoutLogin,
    SessionComponent,
  ],
  exports: [
    HeaderComponent,
    LayoutNormal,
    LayoutLogin,
  ]
})
export class LayoutModule { }
