import { Component } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { DefaultFormComponent } from "app/sample/form/index/index.component";
import { Observable ,  of } from "rxjs";
import { AlertService, ExtEnumService, Api } from "calico";
import { Media } from 'calico/type/media';
import { download } from 'calico/util/file';

@Component({
  selector: 'app-file',
  templateUrl: './file.component.html',
  styles: [`
    .large {
      width: 250px;
    }
  `]
})
export class FileComponent extends DefaultFormComponent {
  constructor(
    alert: AlertService,
    extEnumService: ExtEnumService,
    private fb: FormBuilder,
    private api: Api
  ) {
    super(alert, extEnumService);
  }

  createForm(): Observable<FormGroup> {
    let form = this.fb.group({
      val1: [null],
      val2: [{meta: {name: 'test1', size: 10, type: 'text/plain'}, payload: null}],
      val3: [null, Validators.required],
      val4: [{meta: {name: 'test2', size: 1000, type: 'text/plain'}, payload: null}, (control: AbstractControl) => {
        if (control.value != null && control.value.meta != null && control.value.meta.size >= 100) {
          return {'100バイト以上です': true};
        }
        return {};
      }],
      val5: [null],
    });

    setTimeout(() => {
      form.get('val5').patchValue({meta: {name: 'test3', size: 1000, type: 'text/plain'}, payload: null});
    }, 3000);

    return of(form);
  }

  private download(media: Media) {
    this.api.submit('endpoint/sample/form/echo_file', {media: media}).subscribe((data: {media: Media}) => {
      download(data.media);
    });
  }
}
