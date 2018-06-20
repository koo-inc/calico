import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Api } from "calico";

@Component({
  selector: 'app-submit',
  templateUrl: './submit.component.html',
  styles: [`
    .large {
      width: 350px;
      height: 150px;
    }
  `]
})
export class SubmitComponent implements OnInit {
  forms: FormGroup[];

  constructor(
    private api: Api,
    private fb: FormBuilder,
  ) {
  }

  ngOnInit(): void {
    this.forms = [
      this.fb.group({
        time: 0
      }),
      this.fb.group({
        time: 10
      }),
      this.fb.group({
        time: 20
      }),
      this.fb.group({
        time: 30
      }),
    ];

    setInterval(() => {
      this.send({
        value: undefined
      });
    }, 5000);
  }

  submit(index: number) {
    this.send(this.forms[index]);
  }
  private send(form: any) {
    this.api.submit("endpoint/sample/submit/wait", form).subscribe();
  }
}
