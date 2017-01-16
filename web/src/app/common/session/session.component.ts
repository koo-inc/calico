import { Component, OnInit, OnDestroy, ElementRef, Inject, ViewChild } from '@angular/core';
import { Router } from "@angular/router";
import { AuthService, AuthInfo } from "../api/auth.service";
import { DOCUMENT } from "@angular/platform-browser";
import { Subscription, Observable } from "rxjs";
import {ModalDirective} from "ng2-bootstrap";

@Component({
  selector: 'app-session',
  templateUrl: 'session.component.html',
  styleUrls: ['session.component.scss']
})
export class SessionComponent implements OnInit, OnDestroy {

  @ViewChild(ModalDirective)
  private modal: ModalDirective;

  private initialized: boolean = false;

  private view: any;
  private focusListener: (e: any) => void;
  private subscription: Subscription;

  private static activateEvents = ['mousemove', 'touchstart', 'pointerdown', 'pointermove'];

  constructor(
      private router: Router,
      @Inject(DOCUMENT) private doc: any,
      private elem: ElementRef,
      private authService: AuthService) {

    this.view = doc.defaultView || {
      addEventListener: (type: string, listener: (e: any) => void, useCapture: boolean) => { },
      removeEventListener: (type: string, listener: (e: any) => void, useCapture: boolean) => { },
    };

    this.focusListener = (e: any) => {
      let having = this.having([this.elem.nativeElement], e.target);
      if (!having) {
        this.elem.nativeElement.firstElementChild.focus();
      }
    };
  }

  ngOnInit(): void {
    this.view.addEventListener('focus', this.focusListener, true);

    const interval = 60 * 1000;

    let eventIntervalStream = Observable.from(SessionComponent.activateEvents)
      .flatMap(event => Observable.fromEvent(this.doc, event))
      .throttleTime(100)
      .map((e:any) => e.timeStamp)
      .bufferCount(2, 1)
      .map(l => l[1] - l[0]);

    let firstActionStream = eventIntervalStream
      .map(x => x >= interval)
      ;

    let actingStream = eventIntervalStream
      .buffer(Observable.interval(interval))
      .map(l => l.length > 0)
    ;

    this.subscription = Observable.merge(
      Observable.of(true),
      firstActionStream,
      actingStream
    )
      .filter(x => x)
      .flatMap(() => this.authService.keep())
      .subscribe((authInfo: AuthInfo) => {
        this.initialized = true;
        if(!authInfo.authenticated){
          this.modal.show();
        }
      });
  }

  ngOnDestroy(): void {
    this.view.removeEventListener('focus', this.focusListener, true);
    this.subscription.unsubscribe();
  }

  gotoLogin(): void {
    this.router.navigateByUrl('login').then(_ => this.modal.hide());
  }

  private having(elements: any[], target: any): boolean {
    if (elements == null) return false;

    for (let element of elements) {
      if (target === element) {
        return true;
      }
      if (this.having(element.children, target)) {
        return true;
      }
    }
    return false;
  }
}
