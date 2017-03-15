import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HeaderComponent } from './header/header.component';
import { LayoutNormal } from './layout-normal/layout-normal.component';
import { LayoutLogin } from './layout-login/layout-login.component';
import { HeaderMenubar, HeaderMenubarSub } from "app/common/layout/header/header-menu.component";
import { SessionComponent } from "../session/session.component";
import { ModalModule } from 'ng2-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ModalModule,
  ],
  declarations: [
    HeaderComponent,
    HeaderMenubar,
    HeaderMenubarSub,
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
