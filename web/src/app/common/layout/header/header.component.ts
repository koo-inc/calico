import { Component } from '@angular/core';
import { AuthService, AuthInfo } from "app/common/api/auth.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(
    private router: Router,
    private auth: AuthService
  ) { }

  items = [
      {
        label: '顧客',
        routerLink: ['/customer/index']
      },
      {
        label: 'マスタ',
        items: [
          {
            label: 'ユーザ',
            routerLink: ['/userinfo/index']
          }
        ]
      },
      {
        label: 'Sample',
        items: [
          {
            label: 'FormUI',
            routerLink: ['/sample/form']
          },
          {
            label: 'Validation',
            routerLink: ['/sample/validation']
          },
          {
            label: 'Popover',
            routerLink: ['/sample/popover']
          },
          {
            label: 'Mail送信',
            routerLink: ['']
          }
        ]
      },
    ];

  logout() {
    this.auth.logout().subscribe((authInfo: AuthInfo) => {
      this.router.navigateByUrl("/login")
    });
  }

}
