import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CoreModule } from "app/common/core.module";
import { SharedModule } from "app/common/shared.module";

import { AppComponent } from './app.component';
import { routing } from './app.routing';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    CoreModule.forRoot(),
    SharedModule,
    routing,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
