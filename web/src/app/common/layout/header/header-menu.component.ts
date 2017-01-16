import {Component,ElementRef,OnDestroy,Input,Renderer,EventEmitter} from '@angular/core';
import {DomHandler} from 'primeng/primeng';
import {MenuItem} from 'primeng/primeng';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header-menubarSub',
  template: `
    <ul [ngClass]="{'ui-menubar-root-list ui-helper-clearfix':root, 'ui-widget-content ui-corner-all ui-helper-clearfix ui-menu-child ui-shadow':!root}" class="ui-menu-list"
        (click)="listClick($event)">
      <template ngFor let-child [ngForOf]="(root ? item : item.items)">
        <li #item [ngClass]="{'ui-menuitem ui-widget ui-corner-all':true,'ui-menu-parent':child.items,'ui-menuitem-active':item==activeItem}"
            (mouseenter)="onItemMouseEnter($event,item,child)" (mouseleave)="onItemMouseLeave($event,item)">
          <a #link [href]="child.url||'#'" class="ui-menuitem-link ui-corner-all" 
              [ngClass]="{'ui-state-disabled':child.disabled}" (click)="itemClick($event, child, item)">
            <span class="ui-submenu-icon fa fa-fw" *ngIf="child.items" [ngClass]="{'fa-caret-down':root,'fa-caret-right':!root}"></span>
            <span class="ui-menuitem-icon fa fa-fw" *ngIf="child.icon" [ngClass]="child.icon"></span>
            <span class="ui-menuitem-text">{{child.label}}</span>
          </a>
          <app-header-menubarSub class="ui-submenu" [item]="child" *ngIf="child.items"></app-header-menubarSub>
        </li>
      </template>
    </ul>
    `,
  styles: [`
    ul.ui-menubar-root-list > li.ui-menuitem > a {
      height: 50px !important;
      padding-top: 16px;
      padding-bottom: 16px;
      color: #9d9d9d !important;
      display: inline-block;
    }
    ul.ui-menubar-root-list > li.ui-menuitem > a:hover {
      color: #fff !important;
      background-color: transparent !important;
    }
    ul.ui-menubar-root-list > li.ui-menuitem.ui-menuitem-active > .ui-menuitem-link {
      color: #fff !important;
      background-color: #090909 !important;
    }
  `],
  providers: [DomHandler]
})
export class HeaderMenubarSub {

  @Input() item: MenuItem;

  @Input() root: boolean;

  constructor(public domHandler: DomHandler, public router: Router) {}

  activeItem: any;

  onItemMouseEnter(event: any, item: any, menuitem: MenuItem) {
    if(this.root){
      return;
    }
    if(menuitem.disabled) {
      return;
    }
    this.openSubList(event, item, menuitem);
  }

  openSubList(event: any, item: any, menuitem: MenuItem) {
    this.activeItem = item;
    let nextElement =  item.children[0].nextElementSibling;
    if(nextElement) {
      let sublist = nextElement.children[0];
      sublist.style.zIndex = ++DomHandler.zindex;

      if(this.root) {
        sublist.style.top = (this.domHandler.getOuterHeight(item.children[0])) + 'px';
        sublist.style.left = '0px'
      } else {
        sublist.style.top = '0px';
        sublist.style.left = (this.domHandler.getOuterWidth(item.children[0])) + 'px';
      }
    }
  }

  onItemMouseLeave(event: any, link: any) {
    this.activeItem = null;
  }

  itemClick(event: any, item: MenuItem, itemElem: any) {
    if(item.disabled) {
      event.preventDefault();
      return;
    }

    if(itemElem.children && itemElem.children[0].nextElementSibling){
      event.preventDefault();
      event.stopPropagation();
      this.openSubList(event, itemElem, item);
      return;
    }

    if(!item.url||item.routerLink) {
      event.preventDefault();
    }

    if(item.command) {
      if(!item.eventEmitter) {
        item.eventEmitter = new EventEmitter();
        item.eventEmitter.subscribe(item.command);
      }

      item.eventEmitter.emit({
        originalEvent: event,
        item: item
      });
    }

    if(item.routerLink) {
      this.router.navigate(item.routerLink);
    }
  }

  listClick(event: any) {
    this.activeItem = null;
  }

}

@Component({
  selector: 'app-header-menubar',
  template: `
    <div [ngClass]="{'ui-menubar ui-menu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix':true}" 
        [class]="styleClass" [ngStyle]="style">
      <app-header-menubarSub [item]="model" root="root"></app-header-menubarSub>
    </div>
    `,
  styles: [`
    .ui-menubar {
      background-color: transparent !important;
      border: none !important;
    }
  `],
  providers: [DomHandler]
})
export class HeaderMenubar implements OnDestroy {

  @Input() model: MenuItem[];

  @Input() style: any;

  @Input() styleClass: string;

  constructor(public el: ElementRef, public domHandler: DomHandler, public renderer: Renderer) {}

  unsubscribe(item: any) {
    if(item.eventEmitter) {
      item.eventEmitter.unsubscribe();
    }

    if(item.items) {
      for(let childItem of item.items) {
        this.unsubscribe(childItem);
      }
    }
  }

  ngOnDestroy() {
    if(this.model) {
      for(let item of this.model) {
        this.unsubscribe(item);
      }
    }
  }
}
