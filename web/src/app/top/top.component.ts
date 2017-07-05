import { Component, OnInit } from '@angular/core';
import { RemoteDataService } from 'calico';
import { EXT_ENUMS } from "app/common/remote-data.config";

@Component({
  selector: 'app-top',
  templateUrl: './top.component.html',
  styleUrls: ['./top.component.scss']
})
export class TopComponent implements OnInit {

  constructor(
    private remoteDataService: RemoteDataService,
  ) { }

  ngOnInit() {
    console.log(this.remoteDataService.get(EXT_ENUMS));
  }

}
