var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
import { Directive, Input, ElementRef, Renderer } from '@angular/core';
var GlyphiconDirective = (function () {
    function GlyphiconDirective(el, renderer) {
        this.el = el;
        this.renderer = renderer;
        renderer.setElementClass(el.nativeElement, "glyphicon", true);
    }
    GlyphiconDirective.prototype.ngOnInit = function () {
        if (this.glyphicon == null)
            return;
        if (this.glyphicon.match(/^\s*$/))
            return;
        this.renderer.setElementClass(this.el.nativeElement, "glyphicon-" + this.glyphicon.trim(), true);
    };
    return GlyphiconDirective;
}());
__decorate([
    Input('c-glyphicon'),
    __metadata("design:type", String)
], GlyphiconDirective.prototype, "glyphicon", void 0);
GlyphiconDirective = __decorate([
    Directive({
        selector: '[c-glyphicon]',
    }),
    __metadata("design:paramtypes", [ElementRef, Renderer])
], GlyphiconDirective);
export { GlyphiconDirective };
//# sourceMappingURL=glyphicon.directive.js.map