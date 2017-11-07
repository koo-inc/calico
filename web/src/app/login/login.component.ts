import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Title } from "@angular/platform-browser";
import { AuthService, LoginForm, AuthInfo } from "../common/api/auth.service";
import { FormGroup } from "@angular/forms";
import { Router } from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public loginForm: FormGroup;

  @Output()
  private authRequiredChange: EventEmitter<boolean> = new EventEmitter();

  constructor(private title: Title, private auth: AuthService, private router: Router) {
    title.setTitle("ログイン");
  }

  ngOnInit(): void {
    this.auth.getForm().then(form => {
      this.loginForm = form;
    });
    this.authRequiredChange.emit(false);
  }

  ngOnDestroy(): void {
    this.authRequiredChange.emit(true);
  }

  login(): void {
    this.auth.login(this.loginForm).subscribe((authInfo: AuthInfo) => {
      if (authInfo.authenticated) {
        this.router.navigateByUrl("/top")
      }
    });
  }

}
