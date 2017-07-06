import { ApplicationInitStatus, Component, OnInit } from '@angular/core';
import { Api, RemoteDataService, ExtEnumService } from 'calico';
import { EXT_ENUMS } from "app/common/remote-data.config";

@Component({
  selector: 'app-top',
  templateUrl: './top.component.html',
  styleUrls: ['./top.component.scss']
})
export class TopComponent implements OnInit {

  constructor(
    private remoteDataService: RemoteDataService,
    private extEnumService: ExtEnumService,
  ) { }

  ngOnInit() {
  }

}
