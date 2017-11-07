import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http";

import { ModalModule, PopoverModule } from 'ngx-bootstrap';

import { CalicoCoreModule, CalicoUiModule, CalicoFormModule, CalicoSearchModule } from 'calico';
import { LayoutModule} from './layout/layout.module';

@NgModule({
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    CalicoCoreModule,
    CalicoUiModule,
    CalicoFormModule,
    CalicoSearchModule,
    LayoutModule,
    ModalModule,
    PopoverModule,
  ],
})
export class SharedModule { }
