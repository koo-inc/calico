import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { ModalModule, PopoverModule } from 'ngx-bootstrap';

import { CalicoCoreModule, CalicoUiModule, CalicoFormModule, CalicoSearchModule } from 'calico';
import { LayoutModule} from './layout/layout.module';

@NgModule({
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
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
