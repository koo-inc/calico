import { Component } from '@angular/core';
import { GrowlService } from "calico";

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.css'],
})
export class AppComponent {
  constructor(
    private growlService: GrowlService,
  ) { }
}
