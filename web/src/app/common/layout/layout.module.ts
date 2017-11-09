import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { HeaderComponent } from './header/header.component';
import { LayoutNormal } from './layout-normal/layout-normal.component';
import { LayoutLogin } from './layout-login/layout-login.component';
import { CalicoUiModule } from 'calico';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    CalicoUiModule,
    BsDropdownModule,
  ],
  declarations: [
    HeaderComponent,
    LayoutNormal,
    LayoutLogin,
  ],
  exports: [
    HeaderComponent,
    LayoutNormal,
    LayoutLogin,
  ]
})
export class LayoutModule { }
